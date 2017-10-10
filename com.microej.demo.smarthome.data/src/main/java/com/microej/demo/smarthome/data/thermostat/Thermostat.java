/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.thermostat;

import com.microej.demo.smarthome.data.Device;

/**
 * A thermostat.
 */
public interface Thermostat extends Device<ThermostatEventListener> {

	/**
	 * Gets the temperature.
	 * @return the temperature.
	 */
	int getTemperature();

	/**
	 * Gets the minimum temperature supported.
	 * @return the minimum temperature supported.
	 */
	int getMinTemperature();

	/**
	 * Gets the maximum temperature supported.
	 * @return the maximum temperature supported.
	 */
	int getMaxTemperature();

	/**
	 * Gets the target temperature.
	 * @return the target temperature.
	 */
	int getTargetTemperature();

	/**
	 * Sets the target temperature.
	 * @param temperature the target temperature.
	 */
	void setTargetTemperature(int temperature);
}
