/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.thermostat;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.model.ThermostatBoundedRangeModel;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.CircularProgressWidget;

import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.ElementAdapter;
import ej.widget.listener.OnValueChangeListener;

/**
 *
 */
public class ThermostatCircularProgress extends CircularProgressWidget {

	private final ThermostatBoundedRangeModel model;
	private int target;
	private int targetAngle;
	private final OnValueChangeListener listener;
	private final ElementAdapter colors;
	private final List<OnValueChangeListener> listeners;

	/**
	 * @param model
	 */
	public ThermostatCircularProgress(ThermostatBoundedRangeModel model) {
		super(model);
		this.model = model;
		listener = new OnValueChangeListener(){
			@Override
			public void onValueChange(int newValue) {
				updateAngle();

			}

			@Override
			public void onMaximumValueChange(int newMaximum) {
				// Not used

			}

			@Override
			public void onMinimumValueChange(int newMinimum) {
				// Not used

			}
		};

		listeners = new ArrayList<>();

		// colors is used to handle multiple colors, the background color of colors is the color to use to draw target
		// arc when colder
		colors = new ElementAdapter(this);
		colors.addClassSelector(ClassSelectors.THERMOSTAT_TARGET_COLOR);
	}

	@Override
	public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
		super.renderContent(g, style, bounds);
		if (targetAngle != 0) {
			AntiAliasedShapes shapes = AntiAliasedShapes.Singleton;
			if (targetAngle > 0) {
				g.setColor(colors.getStyle().getForegroundColor());
			} else {
				g.setColor(colors.getStyle().getBackgroundColor());
			}
			shapes.drawCircleArc(g, offset + x, offset + y, (diameter - (offset << 1)),
					startAngle + currentArcAngle,
					targetAngle);
		}
	}

	@Override
	public void setValue(int value) {
		super.setValue(value);
		updateAngle();
	}

	public int getTargetValue() {
		return target;
	}

	public void validateTagetValue() {
		model.setTargetValue(target);
	}

	/**
	 *
	 */
	private void updateAngle() {
		int computeAngle = computeAngle(target - model.getValue() + model.getMinimum());
		if (computeAngle != targetAngle) {
			targetAngle = computeAngle;
			repaint();
		}
	}

	@Override
	protected void performValueChange(int value) {
		setLocalTarget(value);
	}

	private void setLocalTarget(int value) {
		target = value;
		updateAngle();
		notifyListeners();
	}

	@Override
	public void showNotify() {
		super.showNotify();
		setLocalTarget(model.getTargetValue());
		model.addOnTargetValueChangeListener(listener);
		model.register();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		model.removeOnTargetValueChangeListener(listener);
		model.unregister();
	}

	public void addOnTargetValueChangeListener(OnValueChangeListener listener) {
		listeners.add(listener);
	}

	public void removeOnTargetValueChangeListener(OnValueChangeListener listener) {
		listeners.remove(listener);
	}

	private void notifyListeners() {
		for (OnValueChangeListener changeListener : listeners) {
			changeListener.onValueChange(target);
		}
	}
}
