/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.power.Power;
import com.microej.demo.smarthome.data.power.PowerProvider;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MenuButton;
import com.microej.demo.smarthome.widget.PowerWidget;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.widget.basic.Label;

/**
 * This class represents the page which shows the power chart
 */
public class GraphPage extends DevicePage<Power> {

	/**
	 * Constructor
	 */
	public GraphPage() {
		super();
		PowerProvider provider = ServiceLoaderFactory.getServiceLoader().getService(PowerProvider.class);
		Power[] list = provider.list();
		for (Power power : list) {
			addDevice(power, new PowerWidget(power));
		}
	}

	/**
	 * Creates the menu button
	 */
	@Override
	protected MenuButton createMenuButton() {
		MenuButton menuButton = new MenuButton(new Label(Strings.MAXPOWERTODAY));
		menuButton.addClassSelector(ClassSelectors.DASHBOARD_MENU_BUTTON);
		return menuButton;
	}


}
