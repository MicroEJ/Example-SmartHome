/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.dashboard;

import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.data.light.LightEventListener;
import com.microej.demo.smarthome.data.light.LightProvider;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.util.Strings;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.widget.basic.Label;

/**
 *
 */
public class LightsDashboard extends DeviceDashboard {

	private final Label number;
	private int countOn = 0;
	private final LightEventListener listener;
	private final Label lightLabel;
	/**
	 * @param name
	 */
	public LightsDashboard() {
		super(Images.LIGHTS);
		number = new Label();
		number.addClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT);
		lightLabel = new Label();
		lightLabel.addClassSelector(ClassSelectors.DASHBOARD_ITEM_TEXT);

		add(number);
		add(lightLabel);

		listener = new LightEventListener() {

			@Override
			public void onStateChange(boolean on) {
				if (on) {
					countOn++;
				} else {
					countOn--;
				}
				updateCount();
			}

			@Override
			public void onColorChange(int color) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onBrightnessChange(float brightness) {
				// TODO Auto-generated method stub

			}
		};
	}

	@Override
	public void showNotify() {
		super.showNotify();

		LightProvider provider = ServiceLoaderFactory.getServiceLoader().getService(LightProvider.class);
		Light[] list = provider.list();
		countOn = 0;
		for (Light light : list) {
			if(light.isOn()){
				countOn++;
			}

			light.addListener(listener);
		}
		updateCount();
	}

	private void updateCount() {
		number.setText(String.valueOf(countOn));
		if (countOn > 1) {
			lightLabel.setText(Strings.LIGHTS_ON);
		} else {
			lightLabel.setText(Strings.LIGHT_ON);
		}
		if (countOn == 0) {
			setActive(false);
		} else {
			setActive(true);
		}
		repaint();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		LightProvider provider = ServiceLoaderFactory.getServiceLoader().getService(LightProvider.class);
		Light[] list = provider.list();
		for (Light light : list) {
			light.removeListener(listener);
		}
	}
}
