/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.standalone;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import ej.basedriver.Controller;
import ej.basedriver.util.AbstractDriverService;
import ej.basedriver.util.CommPortConnection;
import ej.basedriver.util.DriverService;
import ej.basedriver.zwave.ZwaveController;
import ej.ecom.io.CommPort;
import ej.ecom.io.Connector;
import ej.wadapps.app.BackgroundService;
import ej.wadapps.basedriver.zwave.DefaultZwaveLogger;

/**
 *
 */
public class ZwaveStandaloneBackgroundService implements BackgroundService {

	static {
		LogManager.getLogManager();
		Logger.getLogger("ZWaveLogger");
		try {
			Connector.open("comm://aaa");
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}
	private DriverService driver;

	@Override
	public void onStart() {
		driver = new AbstractDriverService() {

			@Override
			protected Controller create(CommPort port) {
				try {
					DefaultZwaveLogger logger = new DefaultZwaveLogger();
					logger.getLogger().setLevel(Level.SEVERE);
					ZwaveController zwaveController = new ZwaveController(port, new CommPortConnection(port, 115200),
							logger);
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

}
