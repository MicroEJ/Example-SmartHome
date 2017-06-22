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
import com.microej.demo.smarthome.data.ElementListener;


public abstract class AbstractDevice<EL extends ElementListener> implements Device<EL> {

	protected final List<EL> listeners;
	private final String name;

	/**
	 * @param name
	 */
	public AbstractDevice(final String name) {
		super();
		this.name = name;
		listeners = new ArrayList<>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addListener(final EL listener) {
		listeners.add(listener);

	}

	@Override
	public void removeListener(final EL listener) {
		listeners.remove(listener);

	}
}
