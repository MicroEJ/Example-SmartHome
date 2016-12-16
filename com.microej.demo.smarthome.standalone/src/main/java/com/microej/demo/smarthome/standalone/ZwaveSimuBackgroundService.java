/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.standalone;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import ej.ecom.io.Connector;

/**
 *
 */
public class ZwaveSimuBackgroundService extends ZWaveBackgroundService {

	static {
		LogManager.getLogManager();
		Logger.getLogger("ZWaveLogger");
		try {
			Connector.open("comm://aaa");
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}
}
