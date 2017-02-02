/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.door;

import com.microej.demo.smarthome.data.door.Door;
import com.microej.demo.smarthome.data.door.DoorProvider;
import com.microej.demo.smarthome.data.impl.Provider;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;

/**
 *
 */
public class DefaultDoorProvider extends Provider<Door> implements DoorProvider {

	/**
	 *
	 */
	public DefaultDoorProvider() {
		super();
		final DefaultDoor door = new DefaultDoor("Entrance");
		add(door);
		ServiceLoaderFactory.getServiceLoader().getService(Timer.class).schedule(new TimerTask() {

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
