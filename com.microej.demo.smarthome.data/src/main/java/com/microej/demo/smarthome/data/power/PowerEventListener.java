/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.power;

/**
 * A power meter event listener.
 */
public interface PowerEventListener  {
	/**
	 * Call back function when an instant power is generated.
	 * @param instantPower the instantPower.
	 */
	void onInstantPower(InstantPower instantPower);
}
