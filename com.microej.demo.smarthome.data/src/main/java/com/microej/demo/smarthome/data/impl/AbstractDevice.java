/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.impl;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.data.Device;

/**
 * An abstraction of a device.
 * @param <EL> the type of device listener.
 */
public abstract class AbstractDevice<EL extends Object> implements Device<EL> {

	/**
	 * The device listeners.
	 */
	protected final List<EL> listeners;
	private final String name;

	/**
	 * Instantiates a device.
	 * @param name the name of the device.
	 */
	public AbstractDevice(final String name) {
		super();
		this.name = name;
		this.listeners = new ArrayList<>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void addListener(final EL listener) {
		this.listeners.add(listener);

	}

	@Override
	public void removeListener(final EL listener) {
		this.listeners.remove(listener);

	}
}
