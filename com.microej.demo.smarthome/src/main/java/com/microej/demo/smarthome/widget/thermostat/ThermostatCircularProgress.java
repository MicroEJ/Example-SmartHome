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
import ej.widget.animation.AnimationListener;
import ej.widget.animation.AnimationListenerRegistry;
import ej.widget.listener.OnValueChangeListener;

/**
 * A circulat progress for a thermostat.
 */
public class ThermostatCircularProgress extends CircularProgressWidget {

	private static final int THICKNESS = 8;

	private final ThermostatBoundedRangeModel model;

	private final ValueAnimation target;
	private int targetAngle;

	private final OnValueChangeListener listener;
	private final ElementAdapter colors;
	private final List<OnValueChangeListener> listeners;

	private final AnimationListener animationListener;

	/**
	 * Instantiates a ThermostatCircularProgress.
	 *
	 * @param model
	 *            the model.
	 */
	public ThermostatCircularProgress(final ThermostatBoundedRangeModel model) {
		super(model);
		setThickness(THICKNESS);
		this.model = model;
		this.target = new ValueAnimation(model.getValue(), model.getTargetValue(), model.getTargetValue(),
				model.getMaximum());
		this.listener = new OnValueChangeListener() {
			@Override
			public void onValueChange(final int newValue) {
				ThermostatCircularProgress.this.target.setTargetValue(newValue);
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

		this.listeners = new ArrayList<>();

		// colors is used to handle multiple colors, the background color of colors is the color to use to draw target
		// arc when colder
		this.colors = new ElementAdapter(this);
		this.colors.addClassSelector(ClassSelectors.THERMOSTAT_TARGET_COLOR);

		this.animationListener = new AnimationListener() {

			@Override
			public void onStopAnimation() {
				if (isShown()) {
					resetAnimation();
					startAnimation();
				}
			}

			@Override
			public void onStartAnimation() {
				stopAnimation();
				resetAnimation();

			}
		};
	}

	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		super.renderContent(g, style, bounds);
		if (this.targetAngle != 0 && getValueAnimation().isFinished()) {
			final AntiAliasedShapes shapes = AntiAliasedShapes.Singleton;
			if (this.targetAngle > 0) {
				g.setColor(this.colors.getStyle().getForegroundColor());
			} else {
				g.setColor(this.colors.getStyle().getBackgroundColor());
			}
			shapes.drawCircleArc(g, getCircleOffset() + getCircleX(), getCircleOffset() + getCircleY(),
					(getDiameter() - (getCircleOffset() << 1)), getStartAngle() + getCurrentArcAngle(), this.targetAngle);
		}
	}

	@Override
	public void setValue(final int value) {
		this.target.setStart(value);
		super.setValue(value);
	}

	/**
	 * Gets the target temperature.
	 *
	 * @return the target temperature.
	 */
	public int getTargetValue() {
		return this.target.getTargetValue();
	}

	/**
	 * Saves the target temperature into the model.
	 */
	public void validateTagetValue() {
		this.model.setTargetValue(this.target.getTargetValue());
	}

	private void updateAngle() {
		final int computeAngle = computeAngle(
				this.target.getCurrentValue() - getValueAnimation().getCurrentValue() + this.model.getMinimum());
		if (computeAngle != this.targetAngle) {
			this.targetAngle = computeAngle;
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
		this.target.setTargetValue(value);
		this.target.start();
		startAnimation();
		notifyListeners();
	}

	@Override
	public void showNotify() {
		this.animationListener.onStartAnimation();
		AnimationListenerRegistry.register(this.animationListener);
		setLocalTarget(this.model.getTargetValue());
		this.model.addOnTargetValueChangeListener(this.listener);
		this.model.register();
		super.showNotify();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		this.animationListener.onStartAnimation();
		AnimationListenerRegistry.unregister(this.animationListener);
		this.model.removeOnTargetValueChangeListener(this.listener);
		this.model.unregister();
	}

	/**
	 * Adds a listener.
	 *
	 * @param listener
	 *            the listener.
	 */
	public void addOnTargetValueChangeListener(final OnValueChangeListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Removes a listener.
	 *
	 * @param listener
	 *            the listener.
	 */
	public void removeOnTargetValueChangeListener(final OnValueChangeListener listener) {
		this.listeners.remove(listener);
	}

	private void notifyListeners() {
		for (final OnValueChangeListener changeListener : this.listeners) {
			changeListener.onValueChange(this.target.getTargetValue());
		}
	}

	@Override
	public void resetAnimation() {
		super.resetAnimation();
		this.target.reset();
		this.targetAngle = 0;
	}

	@Override
	public boolean doTick(final long currentTimeMillis) {
		final boolean tick = super.doTick(currentTimeMillis);
		if (!tick) {
			if (this.target.isFinished()) {
				return false;
			}
			this.target.tick(currentTimeMillis);
			updateAngle();
		} else {
			// So the animation starts at the end of the first one.
			this.target.start(currentTimeMillis);
		}
		return true;
	}
}
