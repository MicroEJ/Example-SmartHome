/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.zwave;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.microej.demo.smarthome.data.door.Door;
import com.microej.demo.smarthome.data.impl.Provider;

import ej.basedriver.Controller;
import ej.basedriver.ControllerListener;
import ej.basedriver.DryContact;
import ej.basedriver.EventControllerListener;
import ej.ecom.Device;
import ej.ecom.DeviceManager;
import ej.ecom.RegistrationEvent;
import ej.ecom.RegistrationListener;

/**
 *
 */
public class DoorProvider extends Provider<com.microej.demo.smarthome.data.door.Door>
implements com.microej.demo.smarthome.data.door.DoorProvider {

	private final RegistrationListener<DryContact> listener;
	private final Map<DryContact, ZwaveDoorSensor> sensors;

	/**
	 *
	 */
	public DoorProvider() {
		super();
		sensors = new HashMap<>();
		listener = new RegistrationListener<DryContact>() {

			@Override
			public void deviceRegistered(final RegistrationEvent<DryContact> event) {
				addDevice(event.getDevice());
			}


			@Override
			public void deviceUnregistered(final RegistrationEvent<DryContact> event) {
				removeDevice(event.getDevice());
			}
		};

		DeviceManager.addRegistrationListener(listener, DryContact.class);
		final Iterator<DryContact> list2 = DeviceManager.list(DryContact.class);
		while (list2.hasNext()) {
			addDevice(list2.next());
		}
	}

	@Override
	public Door[] list() {
		com.microej.demo.smarthome.data.door.Door[] list = new com.microej.demo.smarthome.data.door.Door[devices
		                                                                                                 .size()];
		list = devices.toArray(list);
		return list;
	}

	private void addDevice(final DryContact device) {
		// Only add "pure" dry contact to avoid adding thermostat as a dry contact.
		final Device parent = device.getParent();
		if (parent == null || parent.getChildren().length == 1) {
			final ZwaveDoorSensor zwaveDoorSensor = new ZwaveDoorSensor(getController(device), generateName(), device);
			sensors.put(device, zwaveDoorSensor);
			add(zwaveDoorSensor);
		}
	}

	/**
	 * @return
	 */
	private String generateName() {
		return "Entrance";
	}

	private void removeDevice(final DryContact device) {
		final ZwaveDoorSensor zwaveDoorSensor = sensors.get(device);
		if (zwaveDoorSensor != null) {
			remove(zwaveDoorSensor);
		}
	}

	/**
	 * @param device
	 * @return
	 */
	private EventControllerListener getController(final Device device) {
		if (device == null) {
			return null;
		}
		if (device instanceof Controller) {
			final ControllerListener controllerListener = ((Controller) device).getListener();
			if (controllerListener instanceof EventControllerListener) {
				return (EventControllerListener) controllerListener;
			}
		}
		return getController(device.getParent());
	}
}
