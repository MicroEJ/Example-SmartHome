/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import java.util.HashMap;
import java.util.Map;

import com.microej.demo.smarthome.data.Device;

import ej.mwt.Widget;
import ej.widget.container.Grid;

/**
 *
 */
public abstract class DevicePage<D extends Device<?>> extends MenuPage {

	private final Object sync = new Object();
	private final Map<D, Widget> devicesMap;
	private final Grid devices;

	/**
	 *
	 */
	public DevicePage() {
		super();
		devices = new Grid(false, 1);

		devicesMap = new HashMap<>();
		setWidget(devices);
	}

	public void addDevice(D element, Widget device) {
		synchronized (sync) {
			devices.add(device);
			devicesMap.put(element, device);
		}
	}

	public void removeDevice(D device) {
		synchronized (sync) {
			Widget deviceWidget = devicesMap.get(device);
			devices.remove(deviceWidget);
			devicesMap.remove(device);
		}
	}

	@Override
	protected void removeAllWidgets() {
		devices.removeAllWidgets();
		devicesMap.clear();
	}
}
