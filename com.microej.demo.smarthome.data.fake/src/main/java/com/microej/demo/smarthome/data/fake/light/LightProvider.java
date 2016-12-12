/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.light;

import com.microej.demo.smarthome.data.fake.Provider;

/**
 *
 */
public class LightProvider extends Provider<com.microej.demo.smarthome.data.light.Light>
implements com.microej.demo.smarthome.data.light.LightProvider {

	/**
	 *
	 */
	public LightProvider() {
		super();
		Light light1 = new Light("Kitchen");
		Light light2 = new Light("Room");
		Light light3 = new Light("Entrance");
		add(light1);
		add(light2);
		add(light3);
	}

	@Override
	public com.microej.demo.smarthome.data.light.Light[] list() {
		com.microej.demo.smarthome.data.light.Light[] list = new com.microej.demo.smarthome.data.light.Light[devices
				.size()];
		list = devices.toArray(list);
		return list;
	}

}
