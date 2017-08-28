/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.thermostat;

import com.microej.demo.smarthome.data.Device;

/**
 *
 */
public interface Thermostat extends Device<ThermostatEventListener> {

	int getTemperature();

	int getMinTemperature();

	int getMaxTemperature();

	int getTargetTemperature();

	void setTargetTemperature(int temperature);
}
