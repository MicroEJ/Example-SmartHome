/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.door;

import java.util.Random;

import com.microej.demo.smarthome.data.impl.AbstractProvider;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;

/**
 * An implementation of a DoorProvider. Providing {@link DefaultDoor}.
 */
public class DefaultDoorProvider extends AbstractProvider<Door> implements DoorProvider {

	private static final String[] NAMES = {"Entrance", "Garden"}; //$NON-NLS-1$ //$NON-NLS-2$
	private static final int MIN_DELAY = 2_000; 
	private static final int MAX_DELAY = 5_000; 
	
	/**
	 * Instantiates a door provider.
	 */
	public DefaultDoorProvider() {
		super();
		final Random rand = new Random();
		for(String name: NAMES){
			final DefaultDoor door = new DefaultDoor(name);
			add(door);
			ServiceLoaderFactory.getServiceLoader().getService(Timer.class, Timer.class).schedule(new TimerTask() {

				boolean open = true;

				@Override
				public void run() {
					door.setOpen(this.open);
					this.open = !this.open;
				}
			}, MIN_DELAY, MIN_DELAY+rand.nextInt(MAX_DELAY-MIN_DELAY));
		}
	}

	@Override
	public com.microej.demo.smarthome.data.door.Door[] list() {
		com.microej.demo.smarthome.data.door.Door[] list = new com.microej.demo.smarthome.data.door.Door[this.devices
		                                                                                                 .size()];
		list = this.devices.toArray(list);
		return list;
	}
}
