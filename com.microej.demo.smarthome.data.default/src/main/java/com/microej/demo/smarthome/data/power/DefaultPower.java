/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.power;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.microej.demo.smarthome.data.impl.Device;
import com.microej.demo.smarthome.data.power.InstantPower;
import com.microej.demo.smarthome.data.power.Power;
import com.microej.demo.smarthome.data.power.PowerEventListener;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;

/**
 *
 */
public class DefaultPower extends Device<PowerEventListener>
implements Power {

	private static final long HOUR_IN_MS = 1000 * 60 * 60;

	private long lastPowerTime;
	private final Random rand = new Random();

	/**
	 * Values
	 */
	private static final int INITIAL_HOUR = 7;
	private static final int MAX_POWER_AT_A_TIME = 24;
	private static final int MAX_PC = 6000;

	/**
	 * Attributes
	 */
	private final List<InstantPower> powers;


	/**
	 * Constructor
	 */
	public DefaultPower() {
		super(DefaultPower.class.getSimpleName());
		powers = new ArrayList<InstantPower>(MAX_POWER_AT_A_TIME);

		lastPowerTime = System.currentTimeMillis();
		for (int i = INITIAL_HOUR; i < getMaxPowerAtATime() + INITIAL_HOUR; i++) {
			addInstantPower();
		}

		ServiceLoaderFactory.getServiceLoader().getService(Timer.class).schedule(new TimerTask() {
			@Override
			public void run() {
				addInstantPower();
			}
		}, 2_000, 2_000);
	}

	@Override
	public InstantPower getInstantPowerConsumption() {
		return powers.get(powers.size()-1);
	}

	@Override
	public InstantPower[] getPowerConsumptions() {
		final InstantPower[] powersArray = new InstantPower[powers.size()];
		return powers.toArray(powersArray);
	}

	@Override
	public int getMaxPowerConsumption() {
		return MAX_PC;
	}

	/**
	 * Gets the maximum number of power values
	 */
	public int getMaxPowerAtATime() {
		return MAX_POWER_AT_A_TIME;
	}

	/**
	 * Adds an instant power
	 */
	public void addInstantPower(final InstantPower instantPower) {
		if (powers.size() >= MAX_POWER_AT_A_TIME) {
			powers.remove(0);
		}
		powers.add(instantPower);

		for (final PowerEventListener powerEventListener : listeners) {
			powerEventListener.onInstantPower(instantPower);
		}
	}


	/**
	 * Adds a power data value
	 */
	private synchronized void addInstantPower() {
		lastPowerTime += HOUR_IN_MS;
		final int powerValue = rand.nextInt(getMaxPowerConsumption());
		final InstantPower instantPower = new DefaultInstantPower(lastPowerTime, powerValue);
		addInstantPower(instantPower);
	}
}
