/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.thermostat;

import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MaxWidthLabel;

import ej.widget.composed.ButtonWrapper;

/**
 * A label displaying the temperature.
 */
public class TemperatureLabel extends ButtonWrapper {

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
		label = new MaxWidthLabel(toTemperatureString(convert(maxTemperature, false)));
		setWidget(label);
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
		final String temperatureString = toTemperatureString(convert(temperature, celsius));
		label.setText(temperatureString);
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
	private int convert(final int temperature, final boolean celsius) {
		if (celsius) {
			return temperature;
		}
		return (int) (temperature * 1.8 + 32);
	}

	private String toTemperatureString(final int temperature) {
		final StringBuilder builder = new StringBuilder();
		builder.append((temperature + 5) / 10);
		if (celsius) {
			builder.append(Strings.DEGREE_CELSIUS);
		} else {
			builder.append(Strings.DEGREE_FAHRENHEIT);
		}
		return builder.toString();
	}

	@Override
	public void performClick() {
		super.performClick();
		celsius = !celsius;
		updateText();
	}

	@Override
	public void addClassSelector(final String classSelector) {
		super.addClassSelector(classSelector);
		label.addClassSelector(classSelector);
	}

	@Override
	public void removeClassSelector(final String classSelector) {
		super.removeClassSelector(classSelector);
		label.removeClassSelector(classSelector);
	}

	@Override
	public void removeAllClassSelectors() {
		super.removeAllClassSelectors();
		label.removeAllClassSelectors();

	}
}
