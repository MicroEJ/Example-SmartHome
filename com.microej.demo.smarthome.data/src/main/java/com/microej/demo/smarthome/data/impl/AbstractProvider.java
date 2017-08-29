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
import com.microej.demo.smarthome.data.Provider;
import com.microej.demo.smarthome.data.ProviderListener;

/**
 * An abstraction of a provider.
 * @param <T> The type of device provided.
 */
public abstract class AbstractProvider<T extends Device<?>> implements Provider<T>{
	
	/**
	 * The list of the devices.
	 */
	protected final List<T> devices;
	
	private final List<ProviderListener<T>> listeners;

	/**
	 * Instantiates an AbstractProvider.
	 */
	public AbstractProvider() {
		super();
		this.listeners = new ArrayList<>();
		this.devices = new ArrayList<>();
	}

	@Override
	public void addListener(final ProviderListener<T> listener) {
		this.listeners.add(listener);

	}

	@Override
	public void removeListener(final ProviderListener<T> listener) {
		this.listeners.remove(listener);

	}

	/**
	 * Adds a device and notify its listeners.
	 * @param device the new device.
	 */
	protected void add(final T device) {
		this.devices.add(device);
		for (final ProviderListener<T> listener : this.listeners) {
			listener.newElement(device);
		}
	}

	/**
	 * Removes a device and notify its listeners.
	 * @param device the device to remove.
	 */
	protected void remove(final T device) {
		this.devices.remove(device);
		for (final ProviderListener<T> listener : this.listeners) {
			listener.removeElement(device);
		}
	}
}
