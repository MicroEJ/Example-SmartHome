/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.light;

import java.util.Random;

import com.microej.demo.smarthome.data.impl.Provider;

/**
 *
 */
public class LightProvider extends Provider<com.microej.demo.smarthome.data.light.Light>
implements com.microej.demo.smarthome.data.light.LightProvider {

	private static Random rand = new Random();
	/**
	 *
	 */
	public LightProvider() {
		super();
		final Light light1 = newLight("Kitchen");
		light1.setColor(0xEE502E); // Coral
		final Light light2 = newLight("Room");
		light2.setColor(0xFFC845); // Chick
		final Light light3 = newLight("Entrance");
		light3.setColor(0xFFFFFF); // White
		add(light1);
		add(light2);
		add(light3);
	}

	/**
	 * @param name
	 * @return
	 */
	private Light newLight(final String name) {
		final Light light = new Light(name);
		light.setBrightness(rand.nextFloat());
		light.switchOn((rand.nextInt() & 1) == 0);
		light.setColor(rand.nextInt(0xFFFFFF));
		return light;
	}

	@Override
	public com.microej.demo.smarthome.data.light.Light[] list() {
		com.microej.demo.smarthome.data.light.Light[] list = new com.microej.demo.smarthome.data.light.Light[devices
		                                                                                                     .size()];
		list = devices.toArray(list);
		return list;
	}

}
