/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.light;

import java.util.Random;

import com.microej.demo.smarthome.data.impl.AbstractProvider;


public class DefaultLightProvider extends AbstractProvider<Light> implements LightProvider {

	private static Random rand = new Random();

	public DefaultLightProvider() {
		super();
		final DefaultLight light1 = newLight("Kitchen");
		light1.setColor(0xEE502E); // Coral
		final DefaultLight light2 = newLight("Room");
		light2.setColor(0xFFC845); // Chick
		final DefaultLight light3 = newLight("Entrance");
		light3.setColor(0xFFFFFF); // White
		add(light1);
		add(light2);
		add(light3);
	}

	private DefaultLight newLight(final String name) {
		final DefaultLight light = new DefaultLight(name);
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
