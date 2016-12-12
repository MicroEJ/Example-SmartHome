/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.dashboard;

import com.microej.demo.smarthome.data.power.InstantPower;
import com.microej.demo.smarthome.data.power.PowerConsumption;
import com.microej.demo.smarthome.data.power.PowerEventListener;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MaxWidthLabel;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.widget.basic.Label;
import ej.widget.container.Flow;
import ej.widget.container.Grid;

/**
 *
 */
public class InstantPowerDashboard extends Grid {

	private final PowerEventListener powerEventListener;
	private final Label power;
	private final InstantPowerBar powerBar;

	/**
	 *
	 */
	public InstantPowerDashboard() {
		super(true, 2);
		Flow text = new Flow(true);
		PowerConsumption powerConsumption = ServiceLoaderFactory.getServiceLoader().getService(PowerConsumption.class);
		power = new MaxWidthLabel(String.valueOf(powerConsumption.getMaxPowerConsumption()));
		power.addClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT);
		power.addClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION);
		powerEventListener = new PowerEventListener() {

			@Override
			public void onInstantPower(InstantPower instantPower) {
				updateInstantPower(instantPower);

			}

		};
		text.add(power);
		Label watt = new Label(Strings.WATT);
		watt.addClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT);
		text.add(watt);

		powerBar = new InstantPowerBar(0, powerConsumption.getMaxPowerConsumption(), 0);
		powerBar.addClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION_BAR);

		add(text);
		add(powerBar);

	}

	@Override
	public void showNotify() {
		super.showNotify();
		PowerConsumption powerConsumption = ServiceLoaderFactory.getServiceLoader().getService(PowerConsumption.class);
		powerConsumption.addListener(powerEventListener);
		updateInstantPower(powerConsumption.getInstantPowerConsumption());
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		PowerConsumption powerConsumption = ServiceLoaderFactory.getServiceLoader().getService(PowerConsumption.class);
		powerConsumption.removeListener(powerEventListener);
	}

	private void updateInstantPower(InstantPower instantPower) {
		int value = instantPower.getPower();
		power.setText(String.valueOf(value));
		powerBar.setValue(value);

	}

	@Override
	public boolean handleEvent(int event) {
		return false;
	}
}
