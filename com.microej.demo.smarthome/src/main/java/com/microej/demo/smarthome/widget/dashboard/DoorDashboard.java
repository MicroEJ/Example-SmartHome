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
 * A widget displaying the door states.
 */
public class DoorDashboard extends DeviceDashboard {

	private final Label lockLabel;
	private final DoorEventListener listener;
	private int doorOpen = 0;

	/**
	 * Instantiate a DoorDashboard.
	 */
	public DoorDashboard() {
		super(Images.SECURITY);

		this.lockLabel = new Label();
		this.lockLabel.addClassSelector(ClassSelectors.DASHBOARD_ITEM_TEXT);
		add(this.lockLabel);

		this.listener = new DoorEventListener() {

			@Override
			public void onStateChange(final boolean open) {
				if (open) {
					DoorDashboard.this.doorOpen++;
				} else {
					DoorDashboard.this.doorOpen--;
				}
				updateDoors();

			}
		};
	}

	@Override
	public void showNotify() {
		super.showNotify();
		final DoorProvider provider = ServiceLoaderFactory.getServiceLoader().getService(DoorProvider.class);
		final Door[] list = provider.list();
		this.doorOpen = 0;
		for (final Door door : list) {
			if (door.isOpen()) {
				this.doorOpen++;
			}
			door.addListener(this.listener);
		}
		updateDoors();
	}

	private void updateDoors() {
		if (this.doorOpen > 0) {
			this.lockLabel.setText(Strings.LOCKS_ARE_CLOSED);
			setActive(true);
		} else {
			this.lockLabel.setText(Strings.LOCKS_ARE_OPENED);
			setActive(false);
		}
		repaint();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		final DoorProvider provider = ServiceLoaderFactory.getServiceLoader().getService(DoorProvider.class);
		final Door[] list = provider.list();
		for (final Door door : list) {
			door.removeListener(this.listener);
		}
	}

}
