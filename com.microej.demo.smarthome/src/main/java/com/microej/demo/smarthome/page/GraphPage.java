/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.power.DefaultPowerMeter;
import com.microej.demo.smarthome.data.power.PowerMeter;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.chart.PowerWidget;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.widget.basic.Label;
import ej.widget.composed.ToggleWrapper;
import ej.widget.toggle.RadioModel;

/**
 * This class represents the page which shows the power chart.
 */
public class GraphPage extends MenuPage {

	private PowerWidget powerWidget;

	/**
	 * Constructor.
	 */
	public GraphPage() {
		super();
		final PowerMeter power = ServiceLoaderFactory.getServiceLoader().getService(PowerMeter.class, DefaultPowerMeter.class);
		this.powerWidget = new PowerWidget(power);
		setWidget(this.powerWidget);
	}

	/**
	 * Creates the menu button.
	 */
	@Override
	protected ToggleWrapper createMenuButton() {
		final Label label = new Label(Strings.MAXPOWERTODAY);
		final ToggleWrapper menuButton = new ToggleWrapper(new RadioModel());
		menuButton.setWidget(label);
		menuButton.addClassSelector(ClassSelectors.DASHBOARD_MENU_BUTTON);
		return menuButton;
	}

	@Override
	public void showNotify() {
		super.showNotify();
		this.powerWidget.reload();
		this.powerWidget.startAnimation();
	}
	
	/**
	 * Select a point in the chart.
	 * Used by the robot.
	 * @param id the id of the point, null for none.
	 */
	public void selectPoint(Integer id) {
		this.powerWidget.selectPoint(id);
	}
}
