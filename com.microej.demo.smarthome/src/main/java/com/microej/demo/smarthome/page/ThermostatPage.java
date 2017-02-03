/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.page;

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
 *
 */
public class ThermostatPage extends DevicePage<Thermostat> {

	/**
	 *
	 */
	public ThermostatPage() {
		super();
		final Thermostat thermostat = ServiceLoaderFactory.getServiceLoader().getService(Thermostat.class);
		addDevice(thermostat, new ThermostatWidget(thermostat));
	}


	@Override
	protected ToggleWrapper createMenuButton() {
		final ImageMenuButton imageMenuButton = new ImageMenuButton(Images.AIRCONDITIONNER);
		final ToggleBox toggleBox = new ToggleBox(new RadioModel(), imageMenuButton);
		toggleBox.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return toggleBox;
	}

}