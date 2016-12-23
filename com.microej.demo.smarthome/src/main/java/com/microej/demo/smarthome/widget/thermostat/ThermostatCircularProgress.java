/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.thermostat;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.model.ThermostatBoundedRangeModel;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.CircularProgressWidget;
import com.microej.demo.smarthome.widget.ValueAnimation;

import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.ElementAdapter;
import ej.widget.listener.OnValueChangeListener;
import ej.widget.navigation.TransitionListener;
import ej.widget.navigation.TransitionManager;
import ej.widget.navigation.page.Page;

/**
 *
 */
public class ThermostatCircularProgress extends CircularProgressWidget {

	private final ThermostatBoundedRangeModel model;

	private final ValueAnimation target;
	private int targetAngle;

	private final OnValueChangeListener listener;
	private final ElementAdapter colors;
	private final List<OnValueChangeListener> listeners;

	private final TransitionListener transitionListener;

	/**
	 * @param model
	 */
	public ThermostatCircularProgress(final ThermostatBoundedRangeModel model) {
		super(model);
		setThickness(8);
		this.model = model;
		target = new ValueAnimation(model.getValue(), model.getTargetValue(), model.getTargetValue(),
				model.getMaximum());
		listener = new OnValueChangeListener(){
			@Override
			public void onValueChange(final int newValue) {
				target.setTargetValue(newValue);
				startAnimation();
				notifyListeners();


			}

			@Override
			public void onMaximumValueChange(final int newMaximum) {
				// Not used

			}

			@Override
			public void onMinimumValueChange(final int newMinimum) {
				// Not used

			}
		};

		listeners = new ArrayList<>();

		// colors is used to handle multiple colors, the background color of colors is the color to use to draw target
		// arc when colder
		colors = new ElementAdapter(this);
		colors.addClassSelector(ClassSelectors.THERMOSTAT_TARGET_COLOR);

		transitionListener = new TransitionListener() {

			@Override
			public void onTransitionStop() {
				if (isShown()) {
					initAnimation();
					startAnimation();
				}

			}

			@Override
			public void onTransitionStep(final int step) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTransitionStart(final int transitionsSteps, final int transitionsStop, final Page from,
					final Page to) {
				stopAnimation();
				initAnimation();

			}
		};
	}

	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		super.renderContent(g, style, bounds);
		if (targetAngle != 0 && valueAnimation.isFinished()) {
			final AntiAliasedShapes shapes = AntiAliasedShapes.Singleton;
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
	public void setValue(final int value) {
		target.setStart(value);
		super.setValue(value);
	}

	public int getTargetValue() {
		return target.getTargetValue();
	}

	public void validateTagetValue() {
		model.setTargetValue(target.getTargetValue());
	}

	/**
	 *
	 */
	private void updateAngle() {
		final int computeAngle = computeAngle(
				target.getCurrentValue() - valueAnimation.getCurrentValue() + model.getMinimum());
		if (computeAngle != targetAngle) {
			targetAngle = computeAngle;
			repaint();
		}
	}

	@Override
	protected void performValueChange(final int value) {
		setLocalTarget(value);
	}

	public void setLocalTarget(final int value) {
		target.setTargetValue(value);
		target.start();
		startAnimation();
		notifyListeners();
	}

	@Override
	public void showNotify() {
		transitionListener.onTransitionStart(0, 0, null, null);
		TransitionManager.addGlobalTransitionListener(transitionListener);
		setLocalTarget(model.getTargetValue());
		model.addOnTargetValueChangeListener(listener);
		model.register();
		super.showNotify();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		transitionListener.onTransitionStart(0, 0, null, null);
		TransitionManager.removeGlobalTransitionListener(transitionListener);
		model.removeOnTargetValueChangeListener(listener);
		model.unregister();
	}

	public void addOnTargetValueChangeListener(final OnValueChangeListener listener) {
		listeners.add(listener);
	}

	public void removeOnTargetValueChangeListener(final OnValueChangeListener listener) {
		listeners.remove(listener);
	}

	private void notifyListeners() {
		for (final OnValueChangeListener changeListener : listeners) {
			changeListener.onValueChange(target.getTargetValue());
		}
	}

	@Override
	public void initAnimation() {
		super.initAnimation();
		target.reset();
		targetAngle = 0;
	}

	@Override
	public boolean doTick(final long currentTimeMillis) {
		final boolean tick = super.doTick(currentTimeMillis);
		if (!tick) {
			if (target.isFinished()) {
				return false;
			}
			target.tick(currentTimeMillis);
			updateAngle();
		} else {
			// So the animation starts at the end of the first one.
			// TODO call it only once.
			target.start(currentTimeMillis);
		}
		return true;
	}
}
