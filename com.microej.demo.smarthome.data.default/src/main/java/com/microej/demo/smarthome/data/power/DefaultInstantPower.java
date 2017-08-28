/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.power;

public class DefaultInstantPower implements InstantPower {

	private final long date;
	private final int power;


	public DefaultInstantPower(final long date, final int power) {
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
