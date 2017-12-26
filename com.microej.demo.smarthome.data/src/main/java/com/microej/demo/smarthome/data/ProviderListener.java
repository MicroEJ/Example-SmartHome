/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data;

/**
 * A listener of a provider.
 * @param <T> the type of device.
 */
public interface ProviderListener<T extends Device<?>> {
	/**
	 * Call back function when a new device is added.
	 * @param element the new device.
	 */
	void newElement(T element);

	/**
	 * Call back function when a new device is removed.
	 * @param element the removed device.
	 */
	void removeElement(T element);
}
