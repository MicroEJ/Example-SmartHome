/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.dashboard;

import com.microej.demo.smarthome.data.door.Door;
import com.microej.demo.smarthome.data.door.DoorEventListener;
import com.microej.demo.smarthome.data.door.DoorProvider;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.util.Strings;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.widget.basic.Label;

/**
 *
 */
public class DoorDashboard extends DeviceDashboard {

	private final Label lockLabel;
	private final DoorEventListener listener;
	private int doorOpen = 0;

	/**
	 * @param name
	 */
	public DoorDashboard() {
		super(Images.SECURITY);

		lockLabel = new Label();
		lockLabel.addClassSelector(ClassSelectors.DASHBOARD_ITEM_TEXT);
		add(lockLabel);

		listener = new DoorEventListener() {

			@Override
			public void onStateChange(boolean open) {
				if (open) {
					doorOpen++;
				} else {
					doorOpen--;
				}
				updateDoors();

			}
		};
	}

	@Override
	public void showNotify() {
		super.showNotify();
		DoorProvider provider = ServiceLoaderFactory.getServiceLoader().getService(DoorProvider.class);
		Door[] list = provider.list();
		doorOpen = 0;
		for (Door door : list) {
			if (door.isOpen()) {
				doorOpen++;
			}
			door.addListener(listener);
		}
		updateDoors();
	}

	/**
	 *
	 */
	private void updateDoors() {
		if (doorOpen > 0) {
			lockLabel.setText(Strings.LOCKS_ARE_OPENED);
			setActive(false);
		} else {
			lockLabel.setText(Strings.LOCKS_ARE_CLOSED);
			setActive(true);
		}
		repaint();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		DoorProvider provider = ServiceLoaderFactory.getServiceLoader().getService(DoorProvider.class);
		Door[] list = provider.list();
		for (Door door : list) {
			door.removeListener(listener);
		}
	}

}
