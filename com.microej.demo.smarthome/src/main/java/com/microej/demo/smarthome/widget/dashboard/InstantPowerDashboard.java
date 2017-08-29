/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.dashboard;

import com.microej.demo.smarthome.data.power.InstantPower;
import com.microej.demo.smarthome.data.power.PowerEventListener;
import com.microej.demo.smarthome.data.power.PowerMeter;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MaxWidthLabel;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.widget.animation.AnimationListener;
import ej.widget.animation.AnimationListenerRegistry;
import ej.widget.basic.Label;
import ej.widget.container.Flow;
import ej.widget.container.Grid;

/**
 * A dashboard tile displaing the current power consumption.
 */
public class InstantPowerDashboard extends Grid {

	private final PowerEventListener powerEventListener;
	private final Label power;
	private final InstantPowerBar powerBar;
	private final AnimationListener animationListener;

	/**
	 * Instantiates an InstantPowerDashboard.
	 */
	public InstantPowerDashboard() {
		super(true, 2);
		final Flow text = new Flow(true);
		final PowerMeter myPower = ServiceLoaderFactory.getServiceLoader().getService(PowerMeter.class);
		this.power = new MaxWidthLabel(String.valueOf(myPower.getMaxPowerConsumption()));
		this.power.addClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT);
		this.power.addClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION);
		this.powerEventListener = new PowerEventListener() {

			@Override
			public void onInstantPower(final InstantPower instantPower) {
				updateInstantPower(instantPower);

			}

		};
		text.add(this.power);
		final Label watt = new Label(Strings.WATT);
		watt.addClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT);
		text.add(watt);

		this.powerBar = new InstantPowerBar(0, myPower.getMaxPowerConsumption(), 0);
		this.powerBar.addClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION_BAR);

		add(text);
		add(this.powerBar);

		this.animationListener = new AnimationListener() {

			@Override
			public void onStartAnimation() {
				// Not used.
			}

			@Override
			public void onStopAnimation() {
				if (isShown()) {
					final PowerMeter myPower = ServiceLoaderFactory.getServiceLoader().getService(PowerMeter.class);
					myPower.addListener(InstantPowerDashboard.this.powerEventListener);
					updateInstantPower(myPower.getInstantPowerConsumption());
				}
			}
		};
		// Add itself as a listener as it is the first page.
		myPower.addListener(this.powerEventListener);
	}

	@Override
	public void showNotify() {
		super.showNotify();
		final PowerMeter myPower = ServiceLoaderFactory.getServiceLoader().getService(PowerMeter.class);
		updateInstantPower(myPower.getInstantPowerConsumption());
		AnimationListenerRegistry.register(this.animationListener);
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		final PowerMeter myPower = ServiceLoaderFactory.getServiceLoader().getService(PowerMeter.class);
		myPower.removeListener(this.powerEventListener);
		AnimationListenerRegistry.unregister(this.animationListener);
	}

	private void updateInstantPower(final InstantPower instantPower) {
		final int value = instantPower.getPower();
		this.power.setText(String.valueOf(value));
		this.powerBar.setValue(value);
	}
}
