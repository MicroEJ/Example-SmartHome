/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.standalone;

import ej.ecom.Device;
import ej.ecom.HardwareDescriptor;
import ej.ecom.io.CommPort;

/**
 *
 */
public class ZwaveEmbBackgroundService extends ZWaveBackgroundService {

	private static final String SD_VENDOR_ID = "0x0658";
	private static final String SD_PRODUCT_ID = "0x0200";
	private static final String ZWAVE_SERIAL_PREFIX = "ZWDG";

	@Override
	protected boolean check(CommPort port) {
		HardwareDescriptor<Device> descriptor = port.getDescriptor();
		String idVendor = descriptor.getProperty("usb/idVendor");
		String idProduct = descriptor.getProperty("usb/idProduct");
		String iSerialNumber = descriptor.getProperty("usb/iSerialNumber");
		boolean isSigmaDesignDevice = idVendor != null && idVendor.equals(SD_VENDOR_ID) && idProduct != null
				&& idProduct.equals(SD_PRODUCT_ID);
		boolean isZwaveSerialNumber = iSerialNumber != null && iSerialNumber.startsWith(ZWAVE_SERIAL_PREFIX);
		return (isSigmaDesignDevice || isZwaveSerialNumber);
	}
}
