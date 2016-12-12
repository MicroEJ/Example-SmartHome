/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.thermostat;

import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.data.thermostat.ThermostatEventListener;
import com.microej.demo.smarthome.util.Strings;

import ej.widget.basic.Label;

/**
 *
 */
public class ThermostatLabel extends Label {
	private final Thermostat model;
	private final ThermostatEventListener thermostatEventListener;

	/**
	 *
	 */
	public ThermostatLabel(Thermostat model) {
		super();
		this.model = model;
		thermostatEventListener = new ThermostatEventListener() {

			@Override
			public void onTemperatureChange(int temperature) {
				updateTemperature(temperature);

			}

			@Override
			public void onTargetTemperatureChange(int targetTemperature) {
				// TODO Auto-generated method stub

			}
		};
		updateTemperature(model.getTemperature());
	}

	/**
	 * @param temperature
	 */
	private void updateTemperature(int temperature) {
		String temperatureString = temperature / 10 + Strings.DEGREE;
		setText(temperatureString);

	}

	@Override
	public void showNotify() {
		super.showNotify();
		model.addListener(thermostatEventListener);
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		model.addListener(thermostatEventListener);
	}

}
