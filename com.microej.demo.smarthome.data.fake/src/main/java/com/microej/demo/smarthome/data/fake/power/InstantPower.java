/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.power;

/**
 *
 */
public class InstantPower implements com.microej.demo.smarthome.data.power.InstantPower {

	private final long date;
	private final int power;

	/**
	 * @param date
	 * @param power
	 */
	public InstantPower(long date, int power) {
		super();
		this.date = date;
		this.power = power;
	}

	@Override
	public long getDate() {
		return date;
	}

	@Override
	public int getPower() {
		return power;
	}

}
