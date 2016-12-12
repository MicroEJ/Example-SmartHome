/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.data.ProviderListener;

import ej.bon.Timer;

/**
 *
 */
public class Provider<T extends com.microej.demo.smarthome.data.Device>
{
	public final static Timer timer = new Timer();
	private final List<ProviderListener<T>> listeners;
	protected final List<T> devices;

	/**
	 *
	 */
	public Provider() {
		super();
		listeners = new ArrayList<>();
		devices = new ArrayList<>();
	}

	public void addListener(ProviderListener<T> listener) {
		listeners.add(listener);

	}

	public void removeListener(ProviderListener<T> listener) {
		listeners.remove(listener);

	}

	protected void add(T device) {
		devices.add(device);
		for (ProviderListener<T> listener : listeners) {
			listener.newElement(device);
		}
	}

	protected void remove(T device) {
		devices.remove(device);
		for (ProviderListener<T> listener : listeners) {
			listener.removeElement(device);
		}
	}
}
