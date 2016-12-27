/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome;

import java.io.IOException;

import ej.basedriver.Controller;
import ej.basedriver.util.AbstractDriverService;
import ej.basedriver.util.CommPortConnection;
import ej.basedriver.util.DriverService;
import ej.basedriver.zwave.ZwaveController;
import ej.ecom.Device;
import ej.ecom.HardwareDescriptor;
import ej.ecom.io.CommPort;
import ej.wadapps.app.BackgroundService;

/**
 *
 */
public class ZWaveBackgroundService implements BackgroundService {

	private static final int ZWAVE_BAUDRATE = 115200;

	private static final String SD_VENDOR_ID = "0x0658";
	private static final String SD_PRODUCT_ID = "0x0200";
	private static final String ZWAVE_SERIAL_PREFIX = "ZWDG";


	private DriverService driver;

	@Override
	public void onStart() {
		driver = new AbstractDriverService() {

			@Override
			protected Controller create(final CommPort port) {
				if (!check(port)) {
					return null;
				}
				try {
					final ZwaveController zwaveController = new ZwaveController(port,
							new CommPortConnection(port, ZWAVE_BAUDRATE));
					return zwaveController;
				} catch (final IOException e) {
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

	protected boolean check(final CommPort port) {
		final HardwareDescriptor<Device> descriptor = port.getDescriptor();
		final String idVendor = descriptor.getProperty("usb/idVendor");
		final String idProduct = descriptor.getProperty("usb/idProduct");
		final String iSerialNumber = descriptor.getProperty("usb/iSerialNumber");
		final boolean isSigmaDesignDevice = idVendor != null && idVendor.equals(SD_VENDOR_ID) && idProduct != null
				&& idProduct.equals(SD_PRODUCT_ID);
		final boolean isZwaveSerialNumber = iSerialNumber != null && iSerialNumber.startsWith(ZWAVE_SERIAL_PREFIX);
		return (isSigmaDesignDevice || isZwaveSerialNumber);
	}
}
