/*
 * Java
 *
 * Copyright 2016-2018 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.dashboard.DoorDashboard;
import com.microej.demo.smarthome.widget.dashboard.InstantPowerDashboard;
import com.microej.demo.smarthome.widget.dashboard.LightsDashboard;
import com.microej.demo.smarthome.widget.dashboard.ThermostatDashboard;

import ej.widget.basic.Label;
import ej.widget.composed.ToggleWrapper;
import ej.widget.container.Grid;
import ej.widget.container.List;
import ej.widget.toggle.RadioModel;

/**
 * A page providing an overview of the system.
 */
public class InformationPage extends MenuPage {

	private final ThermostatDashboard thermostatDashboard;

	/**
	 * Instantiates an Information Page.
	 */
	public InformationPage() {
		final Grid grid = new Grid(false, 2);
		final Grid devices = new Grid(true, 3);
		this.thermostatDashboard = new ThermostatDashboard();
		devices.add(this.thermostatDashboard);
		devices.add(new LightsDashboard());
		devices.add(new DoorDashboard());

		grid.add(devices);
		final List instantPower = new List(true);
		final Label label = new Label(Strings.INSTANT_POWER);
		instantPower.add(label);
		final InstantPowerDashboard power = new InstantPowerDashboard();
		instantPower.add(power);
		grid.add(instantPower);

		setWidget(grid);
	}

	@Override
	protected ToggleWrapper createMenuButton() {
		final Label label = new Label(Strings.INFORMATION);
		final ToggleWrapper menuButton = new ToggleWrapper(new RadioModel());
		menuButton.setWidget(label);
		menuButton.addClassSelector(ClassSelectors.DASHBOARD_MENU_BUTTON);
		return menuButton;
	}

	/**
	 * Sets the temperature scale of the thermostat dashboard
	 *
	 * @param celsius
	 *            true if the temperature scale must be changed as celsius,
	 *            false if the temperature scale must be changed as fahrenheit
	 */
	public void setTemperatureScale(boolean celsius) {
		this.thermostatDashboard.setTemperatureScale(celsius);
	}

}
