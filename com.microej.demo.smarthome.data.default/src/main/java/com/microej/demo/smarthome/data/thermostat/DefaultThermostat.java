/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.thermostat;

import com.microej.demo.smarthome.data.impl.AbstractDevice;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;

/**
 * An implementation of a {@link Thermostat}.
 */
public class DefaultThermostat extends AbstractDevice<ThermostatEventListener> implements Thermostat {

	private static final int TEMP_CHANGE_DELAY = 10_000;
	private static final String NAME = "Thermostat"; //$NON-NLS-1$
	private static final int INITIAL_TARGET = 240;
	private static final int INITIAL_TEMPERATURE = 220;
	private static final int MAX = 400;
	private static final int MIN = 50;
	private int temperature;
	private int target;

	/**
	 * Instantiates a {@link DefaultThermostat} with {@value NAME} name.
	 */
	public DefaultThermostat() {
		this(NAME); 
		this.temperature = INITIAL_TEMPERATURE;
		this.target = INITIAL_TARGET;
	}

	/**
	 * Instantiates a {@link DefaultThermostat}.
	 * @param name the thermostat name.
	 */
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
		}, TEMP_CHANGE_DELAY, TEMP_CHANGE_DELAY);
	}

	@Override
	public int getTemperature() {
		return this.temperature;
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
		return this.target;
	}

	@Override
	public void setTargetTemperature(final int temperature) {
		this.target = temperature;
		for (final ThermostatEventListener thermostatEventListener : this.listeners) {
			thermostatEventListener.onTargetTemperatureChange(temperature);
		}
	}
	
	private void setTemperature(final int temperature) {
		this.temperature = temperature;
		for (final ThermostatEventListener thermostatEventListener : this.listeners) {
			thermostatEventListener.onTemperatureChange(temperature);
		}
	}
}
