/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.util;

/**
 *
 */
public class Utils {

	/**
	 * Format an integer to a double digit string.
	 *
	 * @param integer
	 * @return
	 */
	public static String formatDoubleDigits(int integer) {
		if (integer < 10) {
			return '0' + String.valueOf(integer);
		} else {
			return String.valueOf(integer);
		}
	}

}
