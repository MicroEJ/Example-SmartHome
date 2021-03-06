/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.thermostat;

/**
 * Listener of thermostat events.
 */
public interface ThermostatEventListener  {
	
	/**
	 * Call back function called when the temperature changed.
	 * @param temperature the new temperature.
	 */
	void onTemperatureChange(int temperature);


	/**
	 * Call back function called when the target temperature changed.
	 * @param targetTemperature the new target temperature.
	 */
	void onTargetTemperatureChange(int targetTemperature);
}
