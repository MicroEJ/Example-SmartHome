/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
import ej.widget.navigation.page.Page;

/**
 *
 */
public class InstantPowerDashboard extends Grid {

	private final PowerEventListener powerEventListener;
	private final Label power;
	private final InstantPowerBar powerBar;
	private final TransitionListener transitionListener;

	/**
	 *
	 */
	public InstantPowerDashboard() {
		super(true, 2);
		Flow text = new Flow(true);
		Power myPower = ServiceLoaderFactory.getServiceLoader().getService(Power.class);
		power = new MaxWidthLabel(String.valueOf(myPower.getMaxPowerConsumption()));
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

		powerBar = new InstantPowerBar(0, myPower.getMaxPowerConsumption(), 0);
		powerBar.addClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION_BAR);

		add(text);
		add(powerBar);

		transitionListener = new TransitionListener() {

			@Override
			public void onTransitionStop() {
				if (isShown()) {
					Power myPower = ServiceLoaderFactory.getServiceLoader().getService(Power.class);
					myPower.addListener(powerEventListener);
					updateInstantPower(myPower.getInstantPowerConsumption());
				}

			}

			@Override
			public void onTransitionStep(int step) {

			}

			@Override
			public void onTransitionStart(int transitionsSteps, int transitionsStop, Page from, Page to) {

			}
		};
		// Add itself as a listener as it is the first page.
		myPower.addListener(powerEventListener);
	}

	@Override
	public void showNotify() {
		super.showNotify();
		Power myPower = ServiceLoaderFactory.getServiceLoader().getService(Power.class);
		updateInstantPower(myPower.getInstantPowerConsumption());
		TransitionManager.addGlobalTransitionListener(transitionListener);
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		Power myPower = ServiceLoaderFactory.getServiceLoader().getService(Power.class);
		myPower.removeListener(powerEventListener);
		TransitionManager.removeGlobalTransitionListener(transitionListener);
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
