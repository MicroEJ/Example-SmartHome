/*
 * Sébastien Eon 2016 / CC0-1.0
 */
package sew.philipshue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import sew.upnp.Device;

public class PhilipsHueManager {

	private final Collection<Device> devices;
	private final Map<String, DeviceManager> devicesManagers;

	public PhilipsHueManager() throws IOException {
		this.devices = new ArrayList<>();
		this.devicesManagers = new HashMap<>();
	}

	protected void update(Device device) {
		String friendlyName = device.getFriendlyName();
		if (friendlyName.startsWith("Philips hue")) {
			DeviceManager deviceManager = this.devicesManagers.get(friendlyName);
			if (deviceManager == null) {
				deviceManager = new DeviceManager(this, device);
				this.devicesManagers.put(friendlyName, deviceManager);
				this.devices.add(device);
			} else {
				deviceManager.update();
			}
		}
	}
}
