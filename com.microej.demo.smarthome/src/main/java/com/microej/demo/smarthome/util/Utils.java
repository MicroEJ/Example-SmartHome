/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.util;

/**
 * Utils functions.
 */
public class Utils {

	/**
	 * Format an integer to a double digit string.
	 *
	 * @param integer
	 * @return the integer as a string.
	 */
	public static String formatDoubleDigits(final int integer) {
		if (integer < 10) {
			return '0' + String.valueOf(integer);
		} else {
			return String.valueOf(integer);
		}
	}



}
