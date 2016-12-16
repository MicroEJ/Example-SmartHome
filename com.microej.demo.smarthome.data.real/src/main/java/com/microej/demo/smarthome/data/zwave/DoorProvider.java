/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
		System.out.println("DoorProvider.DoorProvider()");
		sensors = new HashMap<>();
		listener = new RegistrationListener<DryContact>() {

			@Override
			public void deviceRegistered(RegistrationEvent<DryContact> event) {
				addDevice(event.getDevice());
			}


			@Override
			public void deviceUnregistered(RegistrationEvent<DryContact> event) {
				removeDevice(event.getDevice());
			}
		};

		DeviceManager.addRegistrationListener(listener, DryContact.class);
		Iterator<DryContact> list = DeviceManager.list(DryContact.class);
		while (list.hasNext()) {
			addDevice(list.next());
		}
	}

	@Override
	public Door[] list() {
		com.microej.demo.smarthome.data.door.Door[] list = new com.microej.demo.smarthome.data.door.Door[devices
		                                                                                                 .size()];
		list = devices.toArray(list);
		return list;
	}

	private void addDevice(DryContact device) {
		// Only add "pure" dry contact to avoid adding thermostat as a dry contact.
		if (device.getParent().getChildren().length == 1) {
			ZwaveDoorSensor zwaveDoorSensor = new ZwaveDoorSensor(getController(device), generateName(), device);
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

	private void removeDevice(DryContact device) {
		ZwaveDoorSensor zwaveDoorSensor = sensors.get(device);
		if (zwaveDoorSensor != null) {
			remove(zwaveDoorSensor);
		}
	}

	/**
	 * @param device
	 * @return
	 */
	private EventControllerListener getController(Device device) {
		if (device == null) {
			return null;
		}
		if (device instanceof Controller) {
			ControllerListener controllerListener = ((Controller) device).getListener();
			if (controllerListener instanceof EventControllerListener) {
				return (EventControllerListener) controllerListener;
			}
		}
		return getController(device.getParent());
	}


}
