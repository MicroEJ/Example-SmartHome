/*
 * Java
 *
 * Copyright 2016-2018 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.thermostat.DefaultThermostat;
import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.ImageMenuButton;
import com.microej.demo.smarthome.widget.thermostat.ThermostatWidget;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.widget.composed.ToggleBox;
import ej.widget.composed.ToggleWrapper;
import ej.widget.toggle.RadioModel;

/**
 * A page displaying a thermostat.
 */
public class ThermostatPage extends DevicePage<Thermostat> {

	private final Thermostat thermostat;
	private final ThermostatWidget thermostatWidget;

	/**
	 * Instantiates a ThermostatPage.
	 */
	public ThermostatPage() {
		super();
		this.thermostat = ServiceLoaderFactory.getServiceLoader().getService(Thermostat.class, DefaultThermostat.class);
		this.thermostatWidget = new ThermostatWidget(this.thermostat);
		addDevice(this.thermostat, this.thermostatWidget);
	}

	@Override
	protected ToggleWrapper createMenuButton() {
		final ImageMenuButton imageMenuButton = new ImageMenuButton(Images.AIRCONDITIONNER);
		final ToggleBox toggleBox = new ToggleBox(new RadioModel(), imageMenuButton);
		toggleBox.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return toggleBox;
	}

	/**
	 * Sets the target temperature. Used by the Robot.
	 * @param temperaturePercent the temperature percentage.
	 */
	public void setTarget(float temperaturePercent) {
		this.thermostatWidget.setTargetTemperature(temperaturePercent);
	}

	/**
	 * Sets the temperature scale of the desired and current temperature labels
	 *
	 * @param celsius
	 *            true if the temperature scale must be changed as celsius,
	 *            false if the temperature scale must be changed as fahrenheit
	 */
	public void setTemperatureScale(boolean celsius) {
		this.thermostatWidget.setTemperatureScale(celsius);
	}

	/**
	 * Validates the target temperature.
	 * Used by the robot.
	 */
	public void validateTemperature() {
		this.thermostatWidget.validateTemperature();
	}
}
