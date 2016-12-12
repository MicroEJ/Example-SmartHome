/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.door;

import com.microej.demo.smarthome.data.fake.Provider;

import ej.bon.TimerTask;

/**
 *
 */
public class DoorProvider extends Provider<com.microej.demo.smarthome.data.door.Door>
implements com.microej.demo.smarthome.data.door.DoorProvider {

	/**
	 *
	 */
	public DoorProvider() {
		super();
		Door door = new Door("Entrance");
		add(door);
		timer.schedule(new TimerTask() {

			boolean open = true;

			@Override
			public void run() {
				door.setOpen(open);
				open = !open;
			}
		}, 1_000, 5_000);
	}

	@Override
	public com.microej.demo.smarthome.data.door.Door[] list() {
		com.microej.demo.smarthome.data.door.Door[] list = new com.microej.demo.smarthome.data.door.Door[devices
		                                                                                                 .size()];
		list = devices.toArray(list);
		return list;
	}

}
