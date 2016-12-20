/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.impl;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.data.ElementListener;

/**
 *
 */
public class Device<EL extends ElementListener> implements com.microej.demo.smarthome.data.Device<EL> {

	private final String name;
	protected final List<EL> listeners;

	/**
	 * @param name
	 */
	public Device(String name) {
		super();
		this.name = name;
		listeners = new ArrayList<>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addListener(EL listener) {
		listeners.add(listener);

	}

	@Override
	public void removeListener(EL listener) {
		listeners.remove(listener);

	}
}
