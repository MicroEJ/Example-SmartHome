/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.data.door.Door;
import com.microej.demo.smarthome.data.door.DoorEventListener;
import com.microej.demo.smarthome.style.HomeImageLoader;
import com.microej.demo.smarthome.util.Images;

import ej.widget.basic.Image;

/**
 *
 */
public class DoorWidget extends DeviceWidget<Door> implements DoorEventListener {

	private final Image door;
	private boolean open;

	/**
	 * @param model
	 */
	public DoorWidget(Door model) {
		super(model);
		door = new Image();

		// Force change of state see setOpen.
		open = !model.isOpen();
		setOpen(!open);

		setCenter(door);
	}

	@Override
	public void onStateChange(boolean open) {
		setOpen(open);

	}

	/**
	 * @param open
	 */
	private void setOpen(boolean open) {
		if (this.open != open) {
			this.open = open;
			if (open) {
				door.setSource(HomeImageLoader.loadImage(Images.DOOR_OPEN));
			} else {
				door.setSource(HomeImageLoader.loadImage(Images.DOOR_CLOSED));
			}
		}
	}

	@Override
	public void showNotify() {
		super.showNotify();
		model.addListener(this);
		setOpen(model.isOpen());
	}

	@Override
	public void hideNotify() {
		model.removeListener(this);
	}

}
