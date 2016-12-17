/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.thermostat;

import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MaxWidthLabel;

/**
 *
 */
public class TemperatureLabel extends MaxWidthLabel {

	/**
	 * @param maxText
	 */
	public TemperatureLabel(int maxTemperature) {
		super(toTemperatureString(maxTemperature));
	}


	/**
	 * @param value
	 * @param maximum
	 */
	public TemperatureLabel(int value, int maximum) {
		this(maximum);
		setTemperature(value);
	}

	/**
	 * @param temperature
	 */
	public void setTemperature(int temperature) {
		String temperatureString = toTemperatureString(temperature);
		setText(temperatureString);

	}

	private static String toTemperatureString(int temperature) {
		return temperature / 10 + Strings.DEGREE;
	}
}
