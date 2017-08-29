/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.power;

/**
 * A power consumption at a date.
 */
public interface InstantPower {

	/**
	 * Gets the date in milliseconds.
	 * @return the date of the power.
	 */
	long getDate();

	/**
	 * Gets the power value.
	 * @return the power value.
	 */
	int getPower();
}
