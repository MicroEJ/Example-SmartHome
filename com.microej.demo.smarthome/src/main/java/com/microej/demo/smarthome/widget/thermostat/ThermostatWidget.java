/*
 * Java
 *
 * Copyright 2016-2018 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.thermostat;

import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.model.ThermostatBoundedRangeModel;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.LimitedButtonWrapper;
import com.microej.demo.smarthome.widget.OverlapComposite;

import ej.mwt.Widget;
import ej.widget.basic.Label;
import ej.widget.composed.ButtonWrapper;
import ej.widget.composed.Wrapper;
import ej.widget.container.Grid;
import ej.widget.container.List;
import ej.widget.listener.OnClickListener;
import ej.widget.listener.OnValueChangeListener;

/**
 * A thermostat widget.
 */
public class ThermostatWidget extends Grid {

	private static final int TEMPERATURE_FACTOR = 10;
	private final Thermostat thermostat;
	private final ThermostatBoundedRangeModel model;
	private final ButtonWrapper button;
	private TemperatureLabel desiredTemperature;
	private TemperatureLabel currentTemperature;
	private String lastClassSelector = null;
	private Label desiredLabel;
	private final OverlapComposite composite;
	private final ThermostatCircularProgress thermostatCircularProgress;

	/**
	 * Instantiates a ThermostatWidget.
	 *
	 * @param thermostat
	 *            the model.
	 */
	public ThermostatWidget(final Thermostat thermostat) {
		super(true, 3);
		this.thermostat = thermostat;
		this.model = new ThermostatBoundedRangeModel(thermostat);

		this.composite = new OverlapComposite();

		this.thermostatCircularProgress = new ThermostatCircularProgress(this.model);
		this.thermostatCircularProgress.addClassSelector(ClassSelectors.THERMOSTAT);
		this.composite.add(this.thermostatCircularProgress);
		final OnValueChangeListener onValueChangeListener = new ThermostatValueChangeListener(
				this.thermostatCircularProgress);
		this.thermostatCircularProgress.addOnTargetValueChangeListener(onValueChangeListener);
		this.thermostatCircularProgress.addOnValueChangeListener(onValueChangeListener);

		this.button = new LimitedButtonWrapper();
		this.button.setAdjustedToChild(false);
		final Label okLabel = new Label(Strings.OK);
		okLabel.addClassSelector(ClassSelectors.THERMOSTAT_VALIDATE);
		this.button.setWidget(okLabel);
		this.button.addOnClickListener(new OnClickListener() {

			@Override
			public void onClick() {
				ThermostatWidget.this.thermostatCircularProgress.validateTagetValue();
				// Removes OK button.
				updateButton(0, 0);

			}
		});
		this.composite.add(this.button);
		this.button.setVisible(false);

		add(createCurrentLabel());
		add(this.composite);

		add(createDesiredLabel());
		updateClassSelectors(this.model.getValue(), this.model.getTargetValue());
	}

	private Widget createDesiredLabel() {
		this.desiredLabel = new Label(Strings.DESIRED);
		this.desiredLabel.addClassSelector(ClassSelectors.THERMOSTAT_TOP_LABEL);
		this.desiredTemperature = new TemperatureLabel(this.thermostat.getTargetTemperature(), this.thermostat.getMaxTemperature());
		this.desiredTemperature.addClassSelector(ClassSelectors.THERMOSTAT_BOTTOM_LABEL);
		return createLabel(this.desiredLabel, this.desiredTemperature);
	}

	private Widget createCurrentLabel() {
		final Label topLabel = new Label(Strings.CURRENT);
		topLabel.addClassSelector(ClassSelectors.THERMOSTAT_TOP_LABEL);
		topLabel.addClassSelector(ClassSelectors.THERMOSTAT_CURRENT);
		this.currentTemperature = new TemperatureLabel(this.model.getValue(), this.model.getMaximum());
		this.currentTemperature.addClassSelector(ClassSelectors.THERMOSTAT_BOTTOM_LABEL);

		return createLabel(topLabel, this.currentTemperature);
	}

	private static Widget createLabel(final Widget top, final Widget bottom) {
		final Wrapper label = new Wrapper();
		final List list = new List(false);
		list.add(top);
		list.add(bottom);
		label.setWidget(list);
		label.setAdjustedToChild(false);

		return label;
	}

	@Override
	public void setBounds(final int x, final int y, final int width, final int height) {
		super.setBounds(x, y, width, height);
	}

	private void updateClassSelectors(final int current, final int target) {
		if (current == target) {
			setDesiredClassSelector(null);
		} else if (current > target) {
			setDesiredClassSelector(ClassSelectors.THERMOSTAT_DESIRED_COLD);
		} else /* current < target */ {
			setDesiredClassSelector(ClassSelectors.COLOR_CORAL);
		}
	}

	private void setDesiredClassSelector(final String classSelector) {
		if (this.lastClassSelector != classSelector) {
			if (this.lastClassSelector != null) {
				this.desiredTemperature.removeClassSelector(this.lastClassSelector);
				this.desiredLabel.removeClassSelector(this.lastClassSelector);
			}
			if (classSelector != null) {
				this.desiredTemperature.addClassSelector(classSelector);
				this.desiredLabel.addClassSelector(classSelector);
			}

			this.lastClassSelector = classSelector;
		}
	}

	private void updateButton(final int targetTemperature, final int targetValue) {
		if (targetTemperature == targetValue && this.button.isVisible()) {
			this.button.setVisible(false);
			if (isShown()) {
				this.composite.revalidateSubTree();
			}
		} else if (targetTemperature != targetValue && !this.button.isVisible()) {
			this.button.setVisible(true);
			if (isShown()) {
				this.composite.revalidateSubTree();
			}
		}
	}

	public void switchTemperaturesForm(boolean celsius) {
		if (!this.desiredTemperature.isCelsius() && celsius) {
			this.desiredTemperature.onClick();
		} else if (this.desiredTemperature.isCelsius() && !celsius) {
			this.desiredTemperature.onClick();
		}
		if (!this.currentTemperature.isCelsius() && celsius) {
			this.currentTemperature.onClick();
		} else if (this.currentTemperature.isCelsius() && !celsius) {
			this.currentTemperature.onClick();
		}
	}

	private class ThermostatValueChangeListener implements OnValueChangeListener {

		private final ThermostatCircularProgress thermostatCircularProgress;

		/**
		 * @param thermostatCircularProgress
		 */
		ThermostatValueChangeListener(final ThermostatCircularProgress thermostatCircularProgress) {
			this.thermostatCircularProgress = thermostatCircularProgress;
		}

		@Override
		public void onValueChange(final int newValue) {
			ThermostatWidget.this.desiredTemperature.setTemperature(this.thermostatCircularProgress.getTargetValue());
			ThermostatWidget.this.currentTemperature.setTemperature(ThermostatWidget.this.thermostat.getTemperature());
			final int localTarget = this.thermostatCircularProgress.getTargetValue() / TEMPERATURE_FACTOR;
			final int target = ThermostatWidget.this.thermostat.getTargetTemperature() / TEMPERATURE_FACTOR;
			updateClassSelectors(ThermostatWidget.this.thermostat.getTemperature() / TEMPERATURE_FACTOR, localTarget);
			updateButton(target, localTarget);
		}

		@Override
		public void onMinimumValueChange(final int newMinimum) {
			// Not used.

		}

		@Override
		public void onMaximumValueChange(final int newMaximum) {
			// Not used.

		}
	}

	/**
	 * Sets the target temperature.
	 * Used by the robot.
	 * @param value the target temperature.
	 */
	public void setTargetTemperature(float value) {
		final int temperature = (int) ((this.thermostatCircularProgress.getMaximum() - this.thermostatCircularProgress.getMinimum()) * value
				+ this.thermostatCircularProgress.getMinimum());
		this.thermostatCircularProgress.setLocalTarget(temperature);
	}

	/**
	 * Validate the target temperature.
	 * Used by the robot.
	 */
	public void validateTemperature(){
		this.button.performClick();
	}
}
