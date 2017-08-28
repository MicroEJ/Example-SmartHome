/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
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
 * An abstract page to display a type of device.
 */
public abstract class DevicePage<D extends Device<?>> extends MenuPage {

	private final Object sync = new Object();
	protected final Map<D, Widget> devicesMap;
	private final Grid devices;
	private final Label noDeviceWidget;

	/**
	 * Instantiates a DevicePage.
	 */
	public DevicePage() {
		super();
		devices = new Grid(false, 1);
		devicesMap = new HashMap<>();
		noDeviceWidget = new Label(Strings.NO_DEVICE_FOUND);
		devices.add(noDeviceWidget);
		setWidget(devices);
	}

	/**
	 * Adds a device to the page.
	 *
	 * @param element
	 *            the device.
	 * @param device
	 *            The assiciated widget.
	 */
	public void addDevice(final D element, final Widget device) {
		synchronized (sync) {
			if (devices.getWidgetsCount() == 1 && devices.getWidgets()[0] == noDeviceWidget) {
				devices.remove(noDeviceWidget);
			}

			devices.add(device);
			devicesMap.put(element, device);

			revalidate();
		}
	}

	/**
	 * Removes a device.
	 *
	 * @param device
	 *            the device.
	 */
	public void removeDevice(final D device) {
		synchronized (sync) {
			final Widget deviceWidget = devicesMap.get(device);
			devices.remove(deviceWidget);
			devicesMap.remove(device);

			if (devices.getChildrenCount() == 0) {
				devices.add(noDeviceWidget);
				revalidate();
			}
		}
	}

	/**
	 * Removes all the devices.
	 */
	protected void removeDevices() {
		synchronized (sync) {
			devices.removeAllWidgets();
			devicesMap.clear();

			devices.add(noDeviceWidget);
			revalidate();
		}
	}
}
