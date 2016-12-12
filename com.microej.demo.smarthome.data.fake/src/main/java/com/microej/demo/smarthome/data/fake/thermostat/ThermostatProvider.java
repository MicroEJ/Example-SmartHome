/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.thermostat;

import java.util.Random;

import com.microej.demo.smarthome.data.fake.Provider;

import ej.bon.TimerTask;

/**
 *
 */
public class ThermostatProvider extends Provider<com.microej.demo.smarthome.data.thermostat.Thermostat>
implements com.microej.demo.smarthome.data.thermostat.ThermostatProvider {

	/**
	 *
	 */
	public ThermostatProvider() {
		super();
		Thermostat thermostat = new Thermostat("Thermostat");
		add(thermostat);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				int temperature = thermostat.getTemperature();
				temperature--;
				if (temperature < thermostat.getMinTemperature()) {
					temperature = thermostat.getMaxTemperature();
				}
				thermostat.setTemperature(temperature);
			}
		}, 1_000, 10_000);

		timer.schedule(new TimerTask() {
			Random rand = new Random();
			@Override
			public void run() {
				// int target = thermostat.getTargetTemperature();
				// target--;
				// if (target < thermostat.getMinTemperature()) {
				// target = thermostat.getMaxTemperature();
				// }
				int dif = thermostat.getMaxTemperature() - thermostat.getMinTemperature();
				int target = rand.nextInt(dif);
				thermostat.setTargetTemperature(target + thermostat.getMinTemperature());
			}
		}, 1_500, 20_000);
	}

	@Override
	public com.microej.demo.smarthome.data.thermostat.Thermostat[] list() {
		com.microej.demo.smarthome.data.thermostat.Thermostat[] list = new com.microej.demo.smarthome.data.thermostat.Thermostat[devices
		                                                                                                                         .size()];
		list = devices.toArray(list);
		return list;
	}
}
