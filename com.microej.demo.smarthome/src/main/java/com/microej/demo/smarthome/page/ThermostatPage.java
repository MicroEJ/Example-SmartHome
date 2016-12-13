/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.data.thermostat.ThermostatProvider;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.ImageMenuButton;
import com.microej.demo.smarthome.widget.MenuButton;
import com.microej.demo.smarthome.widget.thermostat.ThermostatWidget;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.mwt.Widget;

/**
 *
 */
public class ThermostatPage extends DevicePage {

	/**
	 *
	 */
	public ThermostatPage() {
		super();
		ThermostatProvider provider = ServiceLoaderFactory.getServiceLoader().getService(ThermostatProvider.class);
		Thermostat[] list = provider.list();
		for (Thermostat thermostat : list) {
			addDevice(new ThermostatWidget(thermostat));
		}

	}


	@Override
	protected MenuButton createMenuButton() {
		ImageMenuButton imageMenuButton = new ImageMenuButton(Images.AIRCONDITIONNER);
		imageMenuButton.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return imageMenuButton;
	}

	@Override
	public void addDevice(Widget device) {
		// removeAllWidgets();
		super.addDevice(device);
	}
}