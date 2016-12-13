/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.door.Door;
import com.microej.demo.smarthome.data.door.DoorProvider;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.DoorWidget;
import com.microej.demo.smarthome.widget.ImageMenuButton;
import com.microej.demo.smarthome.widget.MenuButton;

import ej.components.dependencyinjection.ServiceLoaderFactory;

/**
 *
 */
public class DoorPage extends DevicePage {

	/**
	 *
	 */
	public DoorPage() {
		super();
		DoorProvider provider = ServiceLoaderFactory.getServiceLoader().getService(DoorProvider.class);
		Door[] list = provider.list();
		for (Door door : list) {
			addDevice(new DoorWidget(door));
		}
	}

	@Override
	protected MenuButton createMenuButton() {
		ImageMenuButton imageMenuButton = new ImageMenuButton(Images.SECURITY);
		imageMenuButton.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return imageMenuButton;
	}
}