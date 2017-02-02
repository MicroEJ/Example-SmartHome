/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.door;

import com.microej.demo.smarthome.data.door.Door;
import com.microej.demo.smarthome.data.door.DoorEventListener;
import com.microej.demo.smarthome.data.impl.Device;

/**
 *
 */
public class DefaultDoor extends Device<DoorEventListener> implements Door {

	private boolean open = false;

	/**
	 * @param name
	 */
	public DefaultDoor(final String name) {
		super(name);
	}


	@Override
	public boolean isOpen() {
		return open;
	}


	public void setOpen(final boolean open) {
		this.open = open;
		for (final DoorEventListener doorEventListener : listeners) {
			doorEventListener.onStateChange(open);
		}
	}
}
