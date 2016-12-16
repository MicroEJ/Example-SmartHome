/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MenuButton;
import com.microej.demo.smarthome.widget.dashboard.DoorDashboard;
import com.microej.demo.smarthome.widget.dashboard.InstantPowerDashboard;
import com.microej.demo.smarthome.widget.dashboard.LightsDashboard;
import com.microej.demo.smarthome.widget.dashboard.ThermostatDashboard;

import ej.widget.basic.Label;
import ej.widget.container.Grid;
import ej.widget.container.List;

/**
 *
 */
public class InformationPage extends MenuPage {

	/**
	 *
	 */
	public InformationPage() {
		Grid grid = new Grid(false, 2);
		Grid devices = new Grid(true, 3);
		devices.add(new ThermostatDashboard());
		devices.add(new LightsDashboard());
		devices.add(new DoorDashboard());

		grid.add(devices);
		List instantPower = new List(true);
		Label label = new Label(Strings.INSTANT_POWER);
		instantPower.add(label);
		InstantPowerDashboard power = new InstantPowerDashboard();
		instantPower.add(power);
		grid.add(instantPower);

		setWidget(grid);
	}

	@Override
	protected MenuButton createMenuButton() {
		MenuButton menuButton = new MenuButton(new Label(Strings.INFORMATION));
		menuButton.addClassSelector(ClassSelectors.DASHBOARD_MENU_BUTTON);
		return menuButton;
	}

}
