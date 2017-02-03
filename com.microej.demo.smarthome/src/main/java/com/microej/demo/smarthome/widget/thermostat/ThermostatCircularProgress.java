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

/**
 * A circulat progress for a thermostat.
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
	 * Instantiates a ThermostatCircularProgress.
	 *
	 * @param model
	 *            the model.
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
			public void onTransitionStart(final TransitionManager transitionManager) {
				stopAnimation();
				resetAnimation();

			}

			@Override
			public void onTransitionStop(final TransitionManager manager) {
				if (isShown()) {
					resetAnimation();
					startAnimation();
				}

			}
		};
	}

	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		super.renderContent(g, style, bounds);
		if (targetAngle != 0 && getValueAnimation().isFinished()) {
			final AntiAliasedShapes shapes = AntiAliasedShapes.Singleton;
			if (targetAngle > 0) {
				g.setColor(colors.getStyle().getForegroundColor());
			} else {
				g.setColor(colors.getStyle().getBackgroundColor());
			}
			shapes.drawCircleArc(g, getCircleOffset() + getCircleX(), getCircleOffset() + getCircleY(), (getDiameter() - (getCircleOffset() << 1)),
					getStartAngle() + getCurrentArcAngle(),
					targetAngle);
		}
	}

	@Override
	public void setValue(final int value) {
		target.setStart(value);
		super.setValue(value);
	}

	/**
	 * Gets the target temperature.
	 *
	 * @return the target temperature.
	 */
	public int getTargetValue() {
		return target.getTargetValue();
	}

	/**
	 * Saves the target temperature into the model.
	 */
	public void validateTagetValue() {
		model.setTargetValue(target.getTargetValue());
	}

	private void updateAngle() {
		final int computeAngle = computeAngle(
				target.getCurrentValue() - getValueAnimation().getCurrentValue() + model.getMinimum());
		if (computeAngle != targetAngle) {
			targetAngle = computeAngle;
			repaint();
		}
	}

	@Override
	protected void performValueChange(final int value) {
		setLocalTarget(value);
	}

	/**
	 * Sets the current target temperature.
	 *
	 * @param value
	 *            the temperature.
	 */
	public void setLocalTarget(final int value) {
		target.setTargetValue(value);
		target.start();
		startAnimation();
		notifyListeners();
	}

	@Override
	public void showNotify() {
		transitionListener.onTransitionStart(null);
		TransitionManager.addTransitionListener(transitionListener);
		setLocalTarget(model.getTargetValue());
		model.addOnTargetValueChangeListener(listener);
		model.register();
		super.showNotify();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		transitionListener.onTransitionStart(null);
		TransitionManager.addTransitionListener(transitionListener);
		model.removeOnTargetValueChangeListener(listener);
		model.unregister();
	}

	/**
	 * Adds a listener.
	 *
	 * @param listener
	 *            the listener.
	 */
	public void addOnTargetValueChangeListener(final OnValueChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener.
	 *
	 * @param listener
	 *            the listener.
	 */
	public void removeOnTargetValueChangeListener(final OnValueChangeListener listener) {
		listeners.remove(listener);
	}

	private void notifyListeners() {
		for (final OnValueChangeListener changeListener : listeners) {
			changeListener.onValueChange(target.getTargetValue());
		}
	}

	@Override
	public void resetAnimation() {
		super.resetAnimation();
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
			target.start(currentTimeMillis);
		}
		return true;
	}
}
