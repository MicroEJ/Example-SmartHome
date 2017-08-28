/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.thermostat;

import com.microej.demo.smarthome.data.impl.AbstractDevice;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;


public class DefaultThermostat extends AbstractDevice<ThermostatEventListener> implements Thermostat {

	private static final int MAX = 400;
	private static final int MIN = 50;
	private int temperature;
	private int target;

	public DefaultThermostat() {
		this("Thermostat");
		temperature = 220;
		target = 240;
	}


	public DefaultThermostat(final String name) {
		super(name);

		ServiceLoaderFactory.getServiceLoader().getService(Timer.class).schedule(new TimerTask() {

			@Override
			public void run() {
				int temperature = getTemperature();
				temperature--;
				if (temperature < getMinTemperature()) {
					temperature = getMaxTemperature();
				}
				setTemperature(temperature);
			}
		}, 1_000, 10_000);
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
	public void setTargetTemperature(final int temperature) {
		this.target = temperature;
		for (final ThermostatEventListener thermostatEventListener : listeners) {
			thermostatEventListener.onTargetTemperatureChange(temperature);
		}
	}

	public void setTemperature(final int temperature) {
		this.temperature = temperature;
		for (final ThermostatEventListener thermostatEventListener : listeners) {
			thermostatEventListener.onTemperatureChange(temperature);
		}
	}
}
