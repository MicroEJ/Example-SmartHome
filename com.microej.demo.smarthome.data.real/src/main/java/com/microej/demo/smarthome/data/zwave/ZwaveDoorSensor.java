/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.zwave;

import com.microej.demo.smarthome.data.door.Door;
import com.microej.demo.smarthome.data.door.DoorEventListener;
import com.microej.demo.smarthome.data.impl.Device;

import ej.basedriver.BinaryState;
import ej.basedriver.DryContact;
import ej.basedriver.EventControllerListener;
import ej.basedriver.event.DryContactEvent;
import ej.basedriver.event.EventHandler;

/**
 *
 */
public class ZwaveDoorSensor extends Device<DoorEventListener>
implements EventHandler<DryContact, DryContactEvent>, Door {

	private final DryContact dryContact;
	private int state = BinaryState.ON;

	/**
	 * @param generateName
	 */
	public ZwaveDoorSensor(EventControllerListener controller, String name, DryContact dryContact) {
		super(name);
		this.dryContact = dryContact;
		controller.addEventHandler(DryContact.class.getName(), this, dryContact);
	}

	@Override
	public void handleEvent(DryContactEvent event) {
		if (this.state == BinaryState.ON && event.getDevice().getLastKnownState() == BinaryState.OFF) {
			this.state = BinaryState.OFF;
			notifyChange();
		} else if (this.state == BinaryState.OFF && event.getDevice().getLastKnownState() == BinaryState.ON) {
			this.state = BinaryState.ON;
			notifyChange();
		}

	}

	@Override
	public void handleError(DryContactEvent error) {
		// Nothing to do.

	}

	private void notifyChange() {
		for (DoorEventListener doorEventListener : listeners) {
			doorEventListener.onStateChange(isOpen());
		}
	}

	public DryContact getDryContact() {
		return dryContact;
	}

	@Override
	public boolean isOpen() {
		return state == BinaryState.ON;
	}
}
