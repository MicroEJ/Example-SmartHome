/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.door;

import com.microej.demo.smarthome.data.door.DoorEventListener;
import com.microej.demo.smarthome.data.fake.Device;

/**
 *
 */
public class Door extends Device<DoorEventListener> implements com.microej.demo.smarthome.data.door.Door {

	private boolean open = false;

	/**
	 * @param name
	 */
	public Door(String name) {
		super(name);
	}


	@Override
	public boolean isOpen() {
		return open;
	}


	public void setOpen(boolean open) {
		this.open = open;
		for (DoorEventListener doorEventListener : listeners) {
			doorEventListener.onStateChange(open);
		}
	}
}
