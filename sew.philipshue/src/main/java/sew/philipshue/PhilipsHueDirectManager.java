/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sew.philipshue;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import sew.upnp.Device;

/**
 *
 */
public class PhilipsHueDirectManager extends PhilipsHueManager {

	public PhilipsHueDirectManager(String ip) throws IOException {
		Map<String, String> args = new HashMap<>();
		args.put("LOCATION", "http://" + ip + "/description.xml");
		Device bridge = new Device(InetAddress.getByName(ip), args);
		newDevice(bridge);
	}

	private void newDevice(Device device) {
		update(device);
	}
}
