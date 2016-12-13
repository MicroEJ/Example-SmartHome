/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.power;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.microej.demo.smarthome.data.fake.Device;
import com.microej.demo.smarthome.data.fake.Provider;
import com.microej.demo.smarthome.data.power.InstantPower;
import com.microej.demo.smarthome.data.power.PowerEventListener;

import ej.bon.TimerTask;

/**
 *
 */
public class PowerConsumption extends Device<PowerEventListener>
implements com.microej.demo.smarthome.data.power.PowerConsumption {

	private static final int MIN_PC = 0;
	private static final int MAX_PC = 6000;
	private static final int MAX_POWER_AT_A_TIME = 24;
	private static final long HOUR_IN_MS = 1000 * 60 * 60;
	private long lastPower;
	private final Random ran = new Random();
	private InstantPower instantPower;

	private final List<InstantPower> powers;


	/**
	 * @param name
	 */
	public PowerConsumption() {
		super(PowerConsumption.class.getSimpleName());
		powers = new ArrayList<InstantPower>(MAX_POWER_AT_A_TIME);
		lastPower = System.currentTimeMillis();
		for (int i = 0; i < MAX_POWER_AT_A_TIME / 2; i++) {
			addInstantPower();
		}

		Provider.timer.schedule(new TimerTask() {

			@Override
			public void run() {
				addInstantPower();

			}
		}, 2_000, 2_000);
	}

	@Override
	public InstantPower getInstantPowerConsumption() {
		return instantPower;
	}

	@Override
	public InstantPower[] getPowerConsumptions() {
		InstantPower[] powersArray = new InstantPower[powers.size()];
		return powers.toArray(powersArray);
	}

	public synchronized void addInstantPower() {
		lastPower += HOUR_IN_MS;
		instantPower = new com.microej.demo.smarthome.data.fake.power.InstantPower(lastPower,
				computeInstantPower(HOUR_IN_MS));
		if (powers.size() >= MAX_POWER_AT_A_TIME) {
			powers.remove(0);
			powers.add(instantPower);
		}
		notifyListeners();
	}

	/**
	 *
	 */
	private void notifyListeners() {
		for (PowerEventListener powerEventListener : listeners) {
			powerEventListener.onInstantPower(instantPower);
		}
	}

	/**
	 * @param hourInMs
	 * @return
	 */
	private int computeInstantPower(long hourInMs) {
		return ran.nextInt(getMaxPowerConsumption() - MIN_PC) + MIN_PC;
	}

	@Override
	public int getMaxPowerConsumption() {
		return MAX_PC;
	}

}
