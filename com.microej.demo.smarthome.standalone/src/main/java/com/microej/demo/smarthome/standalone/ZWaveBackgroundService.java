/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.standalone;

import java.io.IOException;
import java.util.logging.Level;

import ej.basedriver.Controller;
import ej.basedriver.util.AbstractDriverService;
import ej.basedriver.util.CommPortConnection;
import ej.basedriver.util.DriverService;
import ej.basedriver.zwave.ZwaveController;
import ej.ecom.io.CommPort;
import ej.wadapps.app.BackgroundService;

/**
 *
 */
public class ZWaveBackgroundService implements BackgroundService {

	private static final int ZWAVE_BAUDRATE = 115200;

	private DriverService driver;

	@Override
	public void onStart() {
		driver = new AbstractDriverService() {

			@Override
			protected Controller create(CommPort port) {
				if (!check(port)) {
					return null;
				}
				try {
					ZwaveController zwaveController = new ZwaveController(port,
							new CommPortConnection(port, ZWAVE_BAUDRATE));
					return zwaveController;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

		};
		driver.start();

	}

	@Override
	public void onStop() {
		driver.stop();
	}

	protected boolean check(CommPort port) {
		return true;
	}
}
