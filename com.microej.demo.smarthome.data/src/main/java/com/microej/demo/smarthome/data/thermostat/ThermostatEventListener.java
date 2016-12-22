/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.thermostat;

import com.microej.demo.smarthome.data.ElementListener;

/**
 *
 */
public interface ThermostatEventListener extends ElementListener {
	void onTemperatureChange(int temperature);

	void onTargetTemperatureChange(int targetTemperature);
}
