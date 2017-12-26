/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.power;

/**
 * An implementation of an {@link InstantPower}.
 */
public class DefaultInstantPower implements InstantPower {

	private final long date;
	private final int power;

	/**
	 * Instantiate an {@link InstantPower}.
	 * @param date the date of the power measurement.
	 * @param power the power.
	 */
	public DefaultInstantPower(final long date, final int power) {
		super();
		this.date = date;
		this.power = power;
	}

	@Override
	public long getDate() {
		return this.date;
	}

	@Override
	public int getPower() {
		return this.power;
	}

}
