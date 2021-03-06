/*
 * Java
 *
 * Copyright 2016-2018 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.dashboard;

import com.microej.demo.smarthome.data.thermostat.DefaultThermostat;
import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.data.thermostat.ThermostatEventListener;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.thermostat.TemperatureLabel;

import ej.components.dependencyinjection.ServiceLoaderFactory;

/**
 * A dashboard tiles displaying the state of the thermostat.
 */
public class ThermostatDashboard extends DeviceDashboard {

	private final Thermostat thermostat;
	private final ThermostatEventListener listener;
	private final TemperatureLabel thermostatLabel;

	/**
	 * Instantiates a ThermostatDashboard.
	 */
	public ThermostatDashboard() {
		super(Images.AIRCONDITIONNER);
		this.thermostat = ServiceLoaderFactory.getServiceLoader().getService(Thermostat.class, DefaultThermostat.class);

		final TemperatureLabel thermostatLabel = new TemperatureLabel(this.thermostat.getTemperature(),
				this.thermostat.getMaxTemperature());
		add(thermostatLabel);
		thermostatLabel.addClassSelector(ClassSelectors.DASHBOARD_ITEM_TEXT);
		thermostatLabel.addClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT);

		this.listener = new ThermostatEventListener() {

			@Override
			public void onTemperatureChange(final int temperature) {
				thermostatLabel.setTemperature(temperature);
				updateState();

			}

			@Override
			public void onTargetTemperatureChange(final int targetTemperature) {
				updateState();

			}
		};
		this.thermostatLabel = thermostatLabel;
	}

	/**
	 * Sets the temperature scale of the temperature label
	 *
	 * @param celsius
	 *            true if the temperature scale must be changed as celsius,
	 *            false if the temperature scale must be changed as fahrenheit
	 */
	public void setTemperatureScale(boolean celsius) {
		this.thermostatLabel.setCelsius(celsius);
	}

	@Override
	public void showNotify() {
		super.showNotify();
		this.thermostat.addListener(this.listener);
		updateState();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		this.thermostat.removeListener(this.listener);
	}

	private void updateState() {
		setActive(this.thermostat.getTargetTemperature() != this.thermostat.getTemperature());
		repaint();
	}

}
