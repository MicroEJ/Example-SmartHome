/*
 * Java
 *
 * Copyright 2016-2018 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.thermostat;

import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MaxWidthLabel;

import ej.widget.composed.ButtonWrapper;

/**
 * A label displaying the temperature.
 */
public class TemperatureLabel extends ButtonWrapper {

	private static final int FAR_BASE = 32;
	private static final double FAR_RATIO = 1.8;
	private static final int TEMPERATURE_FACTOR = 10;
	private final MaxWidthLabel label;
	private boolean celsius = false;
	private int temperature;

	/**
	 * Instatiate a TemperatureLabel.
	 *
	 * @param maxTemperature
	 *            the maximum temperature.
	 */
	public TemperatureLabel(final int maxTemperature) {
		super();
		this.label = new MaxWidthLabel(toTemperatureString(convert(maxTemperature, false)));
		setWidget(this.label);
	}

	/**
	 * Instatiate a TemperatureLabel.
	 *
	 * @param value
	 *            the current value.
	 * @param maximum
	 *            the maximum temperature.
	 */
	public TemperatureLabel(final int value, final int maximum) {
		this(maximum);
		setTemperature(value);
	}

	/**
	 * Sets the temperature.
	 *
	 * @param temperature
	 *            the temperature.
	 */
	public void setTemperature(final int temperature) {
		this.temperature = temperature;
		updateText();

	}

	private void updateText() {
		final String temperatureString = toTemperatureString(
				convert(this.temperature / TEMPERATURE_FACTOR, this.celsius));
		this.label.setText(temperatureString);
	}

	/**
	 * Convert a celsius temperature to a format.
	 *
	 * @param temperature
	 *            the temperature.
	 * @param celsius
	 *            true if the target temperature is in celsius, false if the target temperature is in F.
	 * @return
	 */
	private static int convert(final int temperature, final boolean celsius) {
		if (celsius) {
			return temperature;
		}
		return (int) (temperature * FAR_RATIO + FAR_BASE);
	}

	private String toTemperatureString(final int temperature) {
		final StringBuilder builder = new StringBuilder();
		builder.append(temperature);
		if (this.celsius) {
			builder.append(Strings.DEGREE_CELSIUS);
		} else {
			builder.append(Strings.DEGREE_FAHRENHEIT);
		}
		return builder.toString();
	}

	@Override
	public void addClassSelector(final String classSelector) {
		super.addClassSelector(classSelector);
		this.label.addClassSelector(classSelector);
	}

	@Override
	public void removeClassSelector(final String classSelector) {
		super.removeClassSelector(classSelector);
		this.label.removeClassSelector(classSelector);
	}

	@Override
	public void removeAllClassSelectors() {
		super.removeAllClassSelectors();
		this.label.removeAllClassSelectors();

	}

	public boolean isCelsius() {
		return this.celsius;
	}

	public void setCelsius(boolean celsius) {
		this.celsius = celsius;
		updateText();
	}

	@Override
	public void onClick() {
		this.celsius = !this.celsius;
		updateText();
	}
}
