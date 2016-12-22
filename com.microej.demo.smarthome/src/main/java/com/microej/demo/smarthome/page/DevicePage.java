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
import com.microej.demo.smarthome.util.Strings;

import ej.mwt.Widget;
import ej.widget.basic.Label;
import ej.widget.container.Grid;

/**
 *
 */
public abstract class DevicePage<D extends Device<?>> extends MenuPage {

	private final Object sync = new Object();
	private final Map<D, Widget> devicesMap;
	private final Grid devices;
	private final Label noDeviceWidget;

	/**
	 *
	 */
	public DevicePage() {
		super();
		devices = new Grid(false, 1);
		devicesMap = new HashMap<>();
		noDeviceWidget = new Label(Strings.NO_DEVICE_FOUND);
		devices.add(noDeviceWidget);
		setWidget(devices);
	}

	public void addDevice(D element, Widget device) {
		synchronized (sync) {
			if (devices.getWidgetsCount() == 1 && devices.getWidgets()[0] == noDeviceWidget) {
				devices.remove(noDeviceWidget);
			}

			devices.add(device);
			devicesMap.put(element, device);

			revalidate();
		}
	}

	public void removeDevice(D device) {
		synchronized (sync) {
			Widget deviceWidget = devicesMap.get(device);
			devices.remove(deviceWidget);
			devicesMap.remove(device);

			if (devices.getChildrenCount() == 0) {
				devices.add(noDeviceWidget);
				revalidate();
			}
		}
	}

	protected void removeDevices() {
		synchronized (sync) {
			devices.removeAllWidgets();
			devicesMap.clear();

			devices.add(noDeviceWidget);
			revalidate();
		}
	}
}
