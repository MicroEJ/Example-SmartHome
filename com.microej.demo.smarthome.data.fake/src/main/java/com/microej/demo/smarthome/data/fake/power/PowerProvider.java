/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.power;

import java.util.Random;

import com.microej.demo.smarthome.data.impl.Provider;
import com.microej.demo.smarthome.data.power.InstantPower;

import ej.bon.TimerTask;

/**
 *
 */
public class PowerProvider extends Provider<com.microej.demo.smarthome.data.power.Power>
implements com.microej.demo.smarthome.data.power.PowerProvider {

	private static final long HOUR_IN_MS = 1000 * 60 * 60;

	private long lastPowerTime;
	private final Random rand = new Random();

	/**
	 * Constructor
	 */
	public PowerProvider() {
		super();

		Power power = new Power();
		add(power);

		lastPowerTime = System.currentTimeMillis();
		for (int i = 0; i < power.getMaxPowerAtATime(); i++) {
			addInstantPower(power);
		}

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				addInstantPower(power);
			}
		}, 2_000, 2_000);
	}

	/**
	 * Gets the power list
	 */
	@Override
	public com.microej.demo.smarthome.data.power.Power[] list() {
		com.microej.demo.smarthome.data.power.Power[] list = new com.microej.demo.smarthome.data.power.Power[devices
		                                                                                                     .size()];
		list = devices.toArray(list);
		return list;
	}

	/**
	 * Adds a power data value
	 */
	public synchronized void addInstantPower(Power power) {
		lastPowerTime += HOUR_IN_MS;
		int powerValue = rand.nextInt(power.getMaxPowerConsumption());
		InstantPower instantPower = new com.microej.demo.smarthome.data.fake.power.InstantPower(lastPowerTime, powerValue);
		power.addInstantPower(instantPower);
	}

}
