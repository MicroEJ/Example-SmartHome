/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sew.philipshue;

import java.io.IOException;

import sew.upnp.Device;
import sew.upnp.DeviceListener;
import sew.upnp.UpnpManager;

/**
 *
 */
public class PhilipsHueUpnpManager extends PhilipsHueManager implements DeviceListener {
	private final UpnpManager upnpManager;

	public PhilipsHueUpnpManager() throws IOException {
		super();
		this.upnpManager = new UpnpManager();
		this.upnpManager.addDeviceListener(this);
	}

	public void scan() {
		this.upnpManager.discover("urn:schemas-upnp-org:device:basic:1");// "urn:schemas-upnp-org:device:basic:1"
	}

	@Override
	public void newDevice(Device device) {
		update(device);
	}

	@Override
	public void updateDevice(Device device) {
		update(device);
	}
}
