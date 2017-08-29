/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.door;

import com.microej.demo.smarthome.data.impl.AbstractDevice;

/**
 * A simple implementation of a {@link Door}.
 */
public class DefaultDoor extends AbstractDevice<DoorEventListener> implements Door {

	private boolean open;

	/**
	 * Instantiates a DefaultDoor.
	 * @param name the name of the door.
	 */
	public DefaultDoor(final String name) {
		super(name);
		this.open = false;
	}

	@Override
	public boolean isOpen() {
		return this.open;
	}

	/**
	 * Set the door state.
	 * @param open true if the door is open.
	 */
	public void setOpen(final boolean open) {
		this.open = open;
		for (final DoorEventListener doorEventListener : this.listeners) {
			doorEventListener.onStateChange(open);
		}
	}
}
