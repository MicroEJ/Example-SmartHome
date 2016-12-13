/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.data.light.LightProvider;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.ImageMenuButton;
import com.microej.demo.smarthome.widget.LightWidget;
import com.microej.demo.smarthome.widget.MenuButton;

import ej.components.dependencyinjection.ServiceLoaderFactory;

/**
 *
 */
public class LightPage extends DevicePage {

	/**
	 *
	 */
	public LightPage() {
		super();
		LightProvider provider = ServiceLoaderFactory.getServiceLoader().getService(LightProvider.class);
		Light[] list = provider.list();
		for (Light light : list) {
			LightWidget device = new LightWidget(light);
			addDevice(device);
		}
	}

	@Override
	protected MenuButton createMenuButton() {
		ImageMenuButton imageMenuButton = new ImageMenuButton(Images.LIGHTS);
		imageMenuButton.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return imageMenuButton;
	}
}
