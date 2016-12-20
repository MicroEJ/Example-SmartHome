/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.ProviderListener;
import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.data.light.LightProvider;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.ImageMenuButton;
import com.microej.demo.smarthome.widget.MenuButton;
import com.microej.demo.smarthome.widget.light.LightWidget;

import ej.components.dependencyinjection.ServiceLoaderFactory;

/**
 *
 */
public class LightPage extends DevicePage<Light> implements ProviderListener<Light> {

	/**
	 *
	 */
	public LightPage() {
		LightProvider provider = ServiceLoaderFactory.getServiceLoader().getService(LightProvider.class);
		provider.addListener(this);
		Light[] list = provider.list();
		for (Light light : list) {
			newElement(light);
		}
	}

	@Override
	protected MenuButton createMenuButton() {
		ImageMenuButton imageMenuButton = new ImageMenuButton(Images.LIGHTS);
		imageMenuButton.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return imageMenuButton;
	}

	@Override
	public void newElement(Light element) {
		LightWidget device = new LightWidget(element);
		addDevice(element, device);
		if (isShown()) {
			revalidate();
			device.startAnimation();
		}
	}

	@Override
	public void removeElement(Light element) {
		removeDevice(element);
	}
}
