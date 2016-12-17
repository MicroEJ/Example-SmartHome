/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.dashboard;

import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.data.thermostat.ThermostatEventListener;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.thermostat.TemperatureLabel;

import ej.components.dependencyinjection.ServiceLoaderFactory;

/**
 *
 */
public class ThermostatDashboard extends DeviceDashboard {

	private final Thermostat thermostat;
	private final ThermostatEventListener listener;

	/**
	 * @param name
	 */
	public ThermostatDashboard() {
		super(Images.AIRCONDITIONNER);
		thermostat = ServiceLoaderFactory.getServiceLoader().getService(Thermostat.class);

		TemperatureLabel thermostatLabel = new TemperatureLabel(thermostat.getTemperature(),
				thermostat.getMaxTemperature());
		add(thermostatLabel);
		thermostatLabel.addClassSelector(ClassSelectors.DASHBOARD_ITEM_TEXT);
		thermostatLabel.addClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT);

		listener = new ThermostatEventListener() {

			@Override
			public void onTemperatureChange(int temperature) {
				updateState();

			}


			@Override
			public void onTargetTemperatureChange(int targetTemperature) {
				updateState();

			}
		};
	}

	@Override
	public void showNotify() {
		super.showNotify();
		thermostat.addListener(listener);
		updateState();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		thermostat.removeListener(listener);
	}

	private void updateState() {
		setActive(thermostat.getTargetTemperature() != thermostat.getTemperature());
		repaint();
	}

}
