/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.dashboard;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.style.HomeImageLoader;

import ej.style.State;
import ej.widget.basic.Image;
import ej.widget.container.List;

/**
 *
 */
public class DeviceDashboard extends List {

	private boolean active = false;
	private final Image image;
	private final String name;

	/**
	 *
	 */
	public DeviceDashboard(String name) {
		super();
		this.name = name;
		image = new Image(HomeImageLoader.loadDashBoard(name));
		image.addClassSelector(ClassSelectors.DASHBOARD_ITEM_ICON);
		add(image);
	}

	@Override
	public boolean isInState(State state) {
		if (state == State.Active) {
			return active;
		}
		return super.isInState(state);
	}

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the active to set.
	 */
	public void setActive(boolean active) {
		if (active != this.active) {
			this.active = active;
			image.setSource(HomeImageLoader.loadDashBoard(name, active));
		}
	}
}
