/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.zwave;

import java.util.Iterator;

import com.microej.demo.smarthome.data.thermostat.Thermostat;

import ej.basedriver.Controller;
import ej.basedriver.ControllerListener;
import ej.basedriver.EventControllerListener;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.ecom.DeviceManager;
import ej.ecom.RegistrationEvent;
import ej.ecom.RegistrationListener;

/**
 *
 */
public class ZwaveControllerManager {

	private RegistrationListener<Controller> controllerRegistrationListener;
	private final ZwaveThermostatSensor temperatureService;


	/**
	 *
	 */
	public ZwaveControllerManager() {
		temperatureService = (ZwaveThermostatSensor) ServiceLoaderFactory.getServiceLoader()
				.getService(Thermostat.class);
		registerControllers();
	}

	/**
	 *
	 */
	private void registerControllers() {
		controllerRegistrationListener = new RegistrationListener<Controller>() {

			@Override
			public void deviceRegistered(RegistrationEvent<Controller> event) {
				Controller device = event.getDevice();
				addHandlers(device.getListener());
			}

			@Override
			public void deviceUnregistered(RegistrationEvent<Controller> event) {
				Controller device = event.getDevice();
				removeHandlers(device.getListener());
			}

		};
		DeviceManager.addRegistrationListener(controllerRegistrationListener, Controller.class);
		Iterator<Controller> controllerIt = DeviceManager.list(Controller.class);
		while (controllerIt.hasNext()) {
			Controller controller = controllerIt.next();
			addHandlers(controller.getListener());
		}

	}

	private void removeHandlers(ControllerListener controller) {
		if (controller instanceof EventControllerListener) {
			temperatureService.removeController((EventControllerListener) controller);
		}
	}

	private void addHandlers(ControllerListener controller) {
		if (controller instanceof EventControllerListener) {
			temperatureService.addController((EventControllerListener) controller);
		}
	}
}
