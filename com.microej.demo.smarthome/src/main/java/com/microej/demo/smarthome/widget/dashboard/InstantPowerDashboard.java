/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.dashboard;

import com.microej.demo.smarthome.data.power.InstantPower;
import com.microej.demo.smarthome.data.power.Power;
import com.microej.demo.smarthome.data.power.PowerEventListener;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MaxWidthLabel;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.widget.basic.Label;
import ej.widget.container.Flow;
import ej.widget.container.Grid;
import ej.widget.navigation.TransitionListener;
import ej.widget.navigation.TransitionManager;

/**
 * A dashboard tile displaing the current power consumption.
 */
public class InstantPowerDashboard extends Grid {

	private final PowerEventListener powerEventListener;
	private final Label power;
	private final InstantPowerBar powerBar;
	private final TransitionListener transitionListener;

	/**
	 * Instantiates an InstantPowerDashboard.
	 */
	public InstantPowerDashboard() {
		super(true, 2);
		final Flow text = new Flow(true);
		final Power myPower = ServiceLoaderFactory.getServiceLoader().getService(Power.class);
		power = new MaxWidthLabel(String.valueOf(myPower.getMaxPowerConsumption()));
		power.addClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT);
		power.addClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION);
		powerEventListener = new PowerEventListener() {

			@Override
			public void onInstantPower(final InstantPower instantPower) {
				updateInstantPower(instantPower);

			}

		};
		text.add(power);
		final Label watt = new Label(Strings.WATT);
		watt.addClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT);
		text.add(watt);

		powerBar = new InstantPowerBar(0, myPower.getMaxPowerConsumption(), 0);
		powerBar.addClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION_BAR);

		add(text);
		add(powerBar);

		transitionListener = new TransitionListener() {

			@Override
			public void onTransitionStart(final TransitionManager transitionManager) {

			}

			@Override
			public void onTransitionStop(final TransitionManager manager) {
				if (isShown()) {
					final Power myPower = ServiceLoaderFactory.getServiceLoader().getService(Power.class);
					myPower.addListener(powerEventListener);
					updateInstantPower(myPower.getInstantPowerConsumption());
				}

			}
		};
		// Add itself as a listener as it is the first page.
		myPower.addListener(powerEventListener);
	}

	@Override
	public void showNotify() {
		super.showNotify();
		final Power myPower = ServiceLoaderFactory.getServiceLoader().getService(Power.class);
		updateInstantPower(myPower.getInstantPowerConsumption());
		TransitionManager.addTransitionListener(transitionListener);
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		final Power myPower = ServiceLoaderFactory.getServiceLoader().getService(Power.class);
		myPower.removeListener(powerEventListener);
		TransitionManager.removeTransitionListener(transitionListener);
	}

	private void updateInstantPower(final InstantPower instantPower) {
		final int value = instantPower.getPower();
		power.setText(String.valueOf(value));
		powerBar.setValue(value);
	}
}
