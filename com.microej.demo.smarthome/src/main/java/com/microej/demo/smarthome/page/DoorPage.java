/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.ProviderListener;
import com.microej.demo.smarthome.data.door.DefaultDoorProvider;
import com.microej.demo.smarthome.data.door.Door;
import com.microej.demo.smarthome.data.door.DoorProvider;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.DoorWidget;
import com.microej.demo.smarthome.widget.ImageMenuButton;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.widget.composed.ToggleBox;
import ej.widget.composed.ToggleWrapper;
import ej.widget.toggle.RadioModel;

/**
 * A page displaying the door status.
 */
public class DoorPage extends DevicePage<com.microej.demo.smarthome.data.door.Door> implements ProviderListener<Door> {

	/**
	 * Instantiates a DoorPage.
	 */
	public DoorPage() {
		final DoorProvider provider = ServiceLoaderFactory.getServiceLoader().getService(DoorProvider.class, DefaultDoorProvider.class);
		final Door[] list = provider.list();
		for (final Door door : list) {
			newElement(door);
		}
		provider.addListener(this);
	}

	@Override
	protected ToggleWrapper createMenuButton() {
		final ImageMenuButton imageMenuButton = new ImageMenuButton(Images.SECURITY);
		final ToggleBox toggleBox = new ToggleBox(new RadioModel(), imageMenuButton);
		toggleBox.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return toggleBox;
	}

	@Override
	public void newElement(final Door element) {
		final DoorWidget device = new DoorWidget(element);
		addDevice(element, device);
	}

	@Override
	public void removeElement(final Door element) {
		removeDevice(element);
	}
}