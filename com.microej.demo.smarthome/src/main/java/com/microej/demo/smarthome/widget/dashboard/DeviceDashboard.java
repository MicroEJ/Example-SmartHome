/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.dashboard;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.style.HomeImageLoader;

import ej.style.State;
import ej.widget.basic.Image;
import ej.widget.container.List;

/**
 * A widget displaying a device state.
 */
public abstract class DeviceDashboard extends List {

	private boolean active = false;
	private final Image image;
	private final String name;

	/**
	 * Instantiates a DeviceDashboard.
	 *
	 * @param name
	 *            the name of the device.
	 */
	public DeviceDashboard(final String name) {
		super();
		this.name = name;
		this.image = new Image(HomeImageLoader.loadDashBoard(name));
		this.image.addClassSelector(ClassSelectors.DASHBOARD_ITEM_ICON);
		add(this.image);
	}

	@Override
	public boolean isInState(final State state) {
		if (state == State.Active) {
			return this.active;
		}
		return super.isInState(state);
	}

	/**
	 * Sets the active state.
	 *
	 * @param active
	 *            the active state to set.
	 */
	public void setActive(final boolean active) {
		if (active != this.active) {
			this.active = active;
			this.image.setSource(HomeImageLoader.loadDashBoard(this.name, active));
		}
	}
}
