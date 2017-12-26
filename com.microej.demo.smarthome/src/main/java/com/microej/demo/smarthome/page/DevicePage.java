/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
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
 * @param <D> the type of device.
 */
public abstract class DevicePage<D extends Device<?>> extends MenuPage {

	/**
	 * Map of the devices and views.
	 */
	protected final Map<D, Widget> devicesMap;
	private final Object sync = new Object();
	private final Grid devices;
	private final Label noDeviceWidget;

	/**
	 * Instantiates a DevicePage.
	 */
	public DevicePage() {
		super();
		this.devices = new Grid(false, 1);
		this.devicesMap = new HashMap<>();
		this.noDeviceWidget = new Label(Strings.NO_DEVICE_FOUND);
		this.devices.add(this.noDeviceWidget);
		setWidget(this.devices);
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
		synchronized (this.sync) {
			if (this.devices.getWidgetsCount() == 1 && this.devices.getWidgets()[0] == this.noDeviceWidget) {
				this.devices.remove(this.noDeviceWidget);
			}

			this.devices.add(device);
			this.devicesMap.put(element, device);

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
		synchronized (this.sync) {
			final Widget deviceWidget = this.devicesMap.get(device);
			this.devices.remove(deviceWidget);
			this.devicesMap.remove(device);

			if (this.devices.getChildrenCount() == 0) {
				this.devices.add(this.noDeviceWidget);
				revalidate();
			}
		}
	}

	/**
	 * Removes all the devices.
	 */
	protected void removeDevices() {
		synchronized (this.sync) {
			this.devices.removeAllWidgets();
			this.devicesMap.clear();

			this.devices.add(this.noDeviceWidget);
			revalidate();
		}
	}
}
