/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.power;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.microej.demo.smarthome.data.impl.AbstractDevice;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;


/**
 * An implementation of a {@link PowerMeter}.
 */
public class DefaultPowerMeter extends AbstractDevice<PowerEventListener> implements PowerMeter {

	private static final long HOUR_IN_MS = 1000 * 60 * 60;

	private static final int INITIAL_HOUR = 7;
	private static final int MAX_POWER_AT_A_TIME = 24;
	private static final int MAX_PC = 6000;

	private static final Random RAND = new Random();

	private final List<InstantPower> powers;
	private long lastPowerTime;

	/**
	 * Instantiates a {@link DefaultPowerMeter}.
	 */
	public DefaultPowerMeter() {
		super(DefaultPowerMeter.class.getSimpleName());
		this.powers = new ArrayList<InstantPower>(MAX_POWER_AT_A_TIME);

		this.lastPowerTime = System.currentTimeMillis();
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
		synchronized (this.powers) {
			return this.powers.get(this.powers.size() - 1);
		}
	}

	@Override
	public InstantPower[] getPowerConsumptions() {
		synchronized (this.powers) {
			final InstantPower[] powersArray = new InstantPower[this.powers.size()];
			return this.powers.toArray(powersArray);
		}
	}

	@Override
	public int getMaxPowerConsumption() {
		return MAX_PC;
	}

	private int getMaxPowerAtATime() {
		return MAX_POWER_AT_A_TIME;
	}

	private void addInstantPower(final InstantPower instantPower) {
		synchronized (this.powers) {
			if (this.powers.size() >= MAX_POWER_AT_A_TIME) {
				this.powers.remove(0);
			}
			this.powers.add(instantPower);
		}

		for (final PowerEventListener powerEventListener : this.listeners) {
			powerEventListener.onInstantPower(instantPower);
		}
	}


	/**
	 * Adds a power data value
	 */
	private synchronized void addInstantPower() {
		this.lastPowerTime += HOUR_IN_MS;
		final int powerValue = RAND.nextInt(getMaxPowerConsumption());
		final InstantPower instantPower = new DefaultInstantPower(this.lastPowerTime, powerValue);
		addInstantPower(instantPower);
	}
}
