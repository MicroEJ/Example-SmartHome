/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.power;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.data.impl.Device;
import com.microej.demo.smarthome.data.power.InstantPower;
import com.microej.demo.smarthome.data.power.PowerEventListener;

/**
 *
 */
public class Power extends Device<PowerEventListener>
implements com.microej.demo.smarthome.data.power.Power {

	/**
	 * Values
	 */
	private static final int MAX_POWER_AT_A_TIME = 15;
	private static final int MAX_PC = 6000;

	/**
	 * Attributes
	 */
	private final List<InstantPower> powers;


	/**
	 * Constructor
	 */
	public Power() {
		super(Power.class.getSimpleName());
		powers = new ArrayList<InstantPower>(MAX_POWER_AT_A_TIME);
	}

	@Override
	public InstantPower getInstantPowerConsumption() {
		return powers.get(powers.size()-1);
	}

	@Override
	public InstantPower[] getPowerConsumptions() {
		InstantPower[] powersArray = new InstantPower[powers.size()];
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
	public void addInstantPower(InstantPower instantPower) {
		if (powers.size() >= MAX_POWER_AT_A_TIME) {
			powers.remove(0);
		}
		powers.add(instantPower);

		for (PowerEventListener powerEventListener : listeners) {
			powerEventListener.onInstantPower(instantPower);
		}
	}

}
