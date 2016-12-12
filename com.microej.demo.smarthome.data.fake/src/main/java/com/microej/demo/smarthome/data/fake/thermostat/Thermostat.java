/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.thermostat;

import com.microej.demo.smarthome.data.fake.Device;
import com.microej.demo.smarthome.data.thermostat.ThermostatEventListener;

/**
 *
 */
public class Thermostat extends Device<ThermostatEventListener>
implements com.microej.demo.smarthome.data.thermostat.Thermostat {

	private static final int MAX = 400;
	private static final int MIN = 50;
	private int temperature = 220;
	private int target = 240;
	/**
	 * @param name
	 */
	public Thermostat(String name) {
		super(name);
	}

	@Override
	public int getTemperature() {
		return temperature;
	}

	@Override
	public int getMinTemperature() {
		return MIN;
	}

	@Override
	public int getMaxTemperature() {
		return MAX;
	}

	@Override
	public int getTargetTemperature() {
		return target;
	}

	@Override
	public void setTargetTemperature(int temperature) {
		this.target = temperature;
		for (ThermostatEventListener thermostatEventListener : listeners) {
			thermostatEventListener.onTargetTemperatureChange(temperature);
		}
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
		for (ThermostatEventListener thermostatEventListener : listeners) {
			thermostatEventListener.onTemperatureChange(temperature);
		}
	}
}
