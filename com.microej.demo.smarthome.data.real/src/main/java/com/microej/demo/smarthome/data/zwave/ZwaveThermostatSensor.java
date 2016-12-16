/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.zwave;

import java.io.IOException;
import java.util.Iterator;

import com.microej.demo.smarthome.data.impl.Device;
import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.data.thermostat.ThermostatEventListener;
import com.microej.demo.smarthome.util.ExecutorUtils;

import ej.basedriver.EventControllerListener;
import ej.basedriver.MultilevelSensor;
import ej.basedriver.MultilevelValue;
import ej.basedriver.ThermostatMode;
import ej.basedriver.event.EventHandler;
import ej.basedriver.event.MultilevelSensorEvent;
import ej.basedriver.event.ThermostatEvent;
import ej.ecom.DeviceManager;
import ej.ecom.RegistrationEvent;
import ej.ecom.RegistrationListener;

/**
 *
 */
public class ZwaveThermostatSensor extends Device<ThermostatEventListener> implements Thermostat {


	private static final int MIN_TEMPERATURE = 50;
	private static final int MAX_TEMPERATURE = 400;
	private static final int DEFAULT_TEMPERATURE = 220;
	private int target = DEFAULT_TEMPERATURE;
	private int current = DEFAULT_TEMPERATURE;

	private volatile ej.basedriver.Thermostat thermostat;
	private volatile MultilevelSensor sensor;
	private final EventHandler<MultilevelSensor, MultilevelSensorEvent> sensorEventHandler;
	private EventHandler<ej.basedriver.Thermostat, ThermostatEvent> thermostatEventHandler;
	private RegistrationListener<MultilevelSensor> sensorRegistrationListener;
	private RegistrationListener<ej.basedriver.Thermostat> thermostatRegistrationListener;


	/**
	 * @param name
	 */
	public ZwaveThermostatSensor() {
		super("Temperature");
		sensorEventHandler = new EventHandler<MultilevelSensor, MultilevelSensorEvent>() {

			@Override
			public void handleEvent(final MultilevelSensorEvent event) {
				setCurrentTemperature(fromThermostat(event.getValue()));
			}

			@Override
			public void handleError(final MultilevelSensorEvent error) {
				System.out.println("MultilevelSensorEvent error");

			}
		};

		thermostatEventHandler = new EventHandler<ej.basedriver.Thermostat, ej.basedriver.event.ThermostatEvent>() {

			@Override
			public void handleEvent(final ThermostatEvent event) {
				ThermostatMode mode = event.getMode();
				if (mode == null) {
					return;
				}
				final double value = event.getValue(mode);
				setTargetTemperature(fromThermostat(value));
			}

			@Override
			public void handleError(final ThermostatEvent error) {
				System.out.println("ThermostatEvent error");

			}
		};

		registerThermostat();
		registerSensor();
	}

	@Override
	public int getTemperature() {
		if (sensor == null) {
			return current;
		}
		double lastKnownValue = sensor.getLastKnownValue();
		if (lastKnownValue == MultilevelValue.UNKNOWN) {
			return current;
		}
		return fromThermostat(lastKnownValue);
	}

	/**
	 * @return
	 */
	private int getCurrent() {
		return current;
	}

	@Override
	public int getMinTemperature() {
		return MIN_TEMPERATURE;
	}

	@Override
	public int getMaxTemperature() {
		return MAX_TEMPERATURE;
	}

	@Override
	public int getTargetTemperature() {
		if (thermostat == null) {
			return target;
		}
		ThermostatMode mode = thermostat.getLastknownMode();
		if (mode == null) {
			return target;
		}
		double lastKnownValue = mode.getLastKnownValue();
		if (lastKnownValue == MultilevelValue.UNKNOWN) {
			return target;
		}
		return fromThermostat(lastKnownValue);
	}

	/**
	 * @return
	 */
	private int getTarget() {
		return target;
	}

	@Override
	public void setTargetTemperature(int temperature) {
		if (temperature != target) {
			this.target = temperature;
			if (thermostat != null) {
				ThermostatMode mode = thermostat.getLastknownMode();
				if (mode != null) {
					ExecutorUtils.getExecutor(ExecutorUtils.LOW_PRIORITY).execute(new Runnable() {

						@Override
						public void run() {
							try {
								mode.setValue(toThermostat(temperature));
							} catch (IOException e) {
							}
						}
					});
				}
			}
			for (ThermostatEventListener thermostatEventListener : listeners) {
				thermostatEventListener.onTargetTemperatureChange(getTarget());
			}
		}
	}

	private void setCurrentTemperature(int value) {
		if (value != current) {
			this.current = value;
			for (ThermostatEventListener thermostatEventListener : listeners) {
				thermostatEventListener.onTemperatureChange(getCurrent());
			}
		}
	}

	/**
	 *
	 */
	private void registerSensor() {
		loadSensor();
		sensorRegistrationListener = new RegistrationListener<MultilevelSensor>() {

			@Override
			public void deviceRegistered(RegistrationEvent<MultilevelSensor> event) {
				if (sensor == null) {
					sensor = event.getDevice();
				}
			}

			@Override
			public void deviceUnregistered(RegistrationEvent<MultilevelSensor> event) {
				if (sensor == event.getDevice()) {
					sensor = null;
				}
				loadSensor();
			}
		};
		DeviceManager.addRegistrationListener(sensorRegistrationListener, MultilevelSensor.class);
	}

	private void loadSensor() {
		Iterator<MultilevelSensor> sensors = DeviceManager.list(MultilevelSensor.class);
		if (sensors.hasNext()) {
			sensor = sensors.next();
		}
	}

	private void registerThermostat() {
		loadThermostat();
		thermostatRegistrationListener = new RegistrationListener<ej.basedriver.Thermostat>() {

			@Override
			public void deviceRegistered(RegistrationEvent<ej.basedriver.Thermostat> event) {
				if (thermostat == null) {
					thermostat = event.getDevice();
				}

			}

			@Override
			public void deviceUnregistered(RegistrationEvent<ej.basedriver.Thermostat> event) {
				if (thermostat == event.getDevice()) {
					thermostat = null;
				}
				loadThermostat();
			}
		};
		DeviceManager.addRegistrationListener(thermostatRegistrationListener, ej.basedriver.Thermostat.class);
	}

	private void loadThermostat() {
		Iterator<ej.basedriver.Thermostat> thermostats = DeviceManager.list(ej.basedriver.Thermostat.class);
		if (thermostats.hasNext()) {
			thermostat = thermostats.next();
		}
	}

	public void addController(EventControllerListener controller) {
		controller.addEventHandler(MultilevelSensor.class.getName(), sensorEventHandler);
		controller.addEventHandler(ej.basedriver.Thermostat.class.getName(), thermostatEventHandler);
	}

	/**
	 * @param device
	 */
	public void removeController(EventControllerListener controller) {
		controller.removeEventHandler(sensorEventHandler);
		controller.removeEventHandler(thermostatEventHandler);
	}

	private int fromThermostat(double value) {
		return (int) (value * 10);
	}

	private double toThermostat(int value) {
		return value / 10d;
	}

}
