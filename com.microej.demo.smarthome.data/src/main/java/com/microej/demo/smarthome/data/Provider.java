/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data;

/**
 * A provider provides a list of device.
 * @param <T> the type of device.
 */
public interface Provider<T extends Device<?>> {
	/**
	 * Gets the list of device.
	 * @return A list of device.
	 */
	T[] list();

	/**
	 * Adds a listener to be notified when a device is registered or unregistered.
	 * @param listener the listener.
	 */
	void addListener(ProviderListener<T> listener);

	/**
	 * Removes a listener.
	 * @param listener the listener.
	 */
	void removeListener(ProviderListener<T> listener);
}
