/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import ej.mwt.Widget;
import ej.widget.container.Grid;

/**
 *
 */
public abstract class DevicePage extends MenuPage {

	private final Grid devices;

	/**
	 *
	 */
	public DevicePage() {
		super();
		devices = new Grid(false, 1);

		setWidget(devices);
	}

	public void addDevice(Widget device) {
		devices.add(device);
	}

}
