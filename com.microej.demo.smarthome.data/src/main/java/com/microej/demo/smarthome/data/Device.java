/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data;

/**
 * A device as a name and can have some listeners.
 * @param <EL> The listener
 */
public interface Device<EL extends Object> {

	/**
	 * Gets the name of the device.
	 * @return the name of the device.
	 */
	String getName();

	/**
	 * Adds a listener to be notify when an events occurs.
	 * @param listener the listener to add.
	 */
	void addListener(EL listener);

	/**
	 * Removes a listener.
	 * @param listener the listener to remove.
	 */
	void removeListener(EL listener);
}
