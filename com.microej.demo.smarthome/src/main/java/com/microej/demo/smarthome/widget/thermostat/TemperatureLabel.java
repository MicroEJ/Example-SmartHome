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
 *
 */
public class TemperatureLabel extends ButtonWrapper {

	private final MaxWidthLabel label;
	private boolean celsius = false;
	private int temperature;


	/**
	 * @param maxText
	 */
	public TemperatureLabel(final int maxTemperature) {
		super();
		label = new MaxWidthLabel(toTemperatureString(convert(maxTemperature, false)));
		setWidget(label);
	}


	/**
	 * @param value
	 * @param maximum
	 */
	public TemperatureLabel(final int value, final int maximum) {
		this(maximum);
		setTemperature(value);
	}

	/**
	 * @param temperature
	 */
	public void setTemperature(final int temperature) {
		this.temperature = temperature;
		updateText();

	}

	/**
	 *
	 */
	private void updateText() {
		final String temperatureString = toTemperatureString(convert(temperature, celsius));
		label.setText(temperatureString);
	}


	/**
	 * @param temperature2
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
