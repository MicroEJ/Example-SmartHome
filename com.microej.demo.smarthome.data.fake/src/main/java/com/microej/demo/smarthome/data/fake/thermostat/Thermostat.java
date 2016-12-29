/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.thermostat;

import com.microej.demo.smarthome.data.impl.Device;
import com.microej.demo.smarthome.data.thermostat.ThermostatEventListener;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;

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
	 *
	 */
	public Thermostat() {
		this("Thermostat");
	}

	/**
	 * @param name
	 */
	public Thermostat(final String name) {
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

		// ServiceLoaderFactory.getServiceLoader().getService(Timer.class).schedule(new TimerTask() {
		// Random rand = new Random();
		//
		// @Override
		// public void run() {
		// // int target = thermostat.getTargetTemperature();
		// // target--;
		// // if (target < thermostat.getMinTemperature()) {
		// // target = thermostat.getMaxTemperature();
		// // }
		// int dif = getMaxTemperature() - getMinTemperature();
		// int target = rand.nextInt(dif);
		// setTargetTemperature(target + getMinTemperature());
		// }
		// }, 1_500, 20_000);
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
