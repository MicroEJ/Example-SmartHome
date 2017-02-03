/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.impl;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.data.Device;
import com.microej.demo.smarthome.data.ProviderListener;

public class AbstractProvider<T extends Device<?>>
{
	private final List<ProviderListener<T>> listeners;
	protected final List<T> devices;

	public AbstractProvider() {
		super();
		listeners = new ArrayList<>();
		devices = new ArrayList<>();
	}

	public void addListener(final ProviderListener<T> listener) {
		listeners.add(listener);

	}

	public void removeListener(final ProviderListener<T> listener) {
		listeners.remove(listener);

	}

	protected void add(final T device) {
		devices.add(device);
		for (final ProviderListener<T> listener : listeners) {
			listener.newElement(device);
		}
	}

	protected void remove(final T device) {
		devices.remove(device);
		for (final ProviderListener<T> listener : listeners) {
			listener.removeElement(device);
		}
	}
}
