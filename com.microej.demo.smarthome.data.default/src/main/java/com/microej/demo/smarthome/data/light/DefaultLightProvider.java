/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.light;

import java.util.Random;

import com.microej.demo.smarthome.data.impl.AbstractProvider;

/**
 * An implementation of a LightProvider. Providing {@link DefaultLight}.
 */
public class DefaultLightProvider extends AbstractProvider<Light> implements LightProvider {

	private static final Random RAND = new Random();
	private static final String[] NAMES = {"Kitchen", "Room", "Entrance"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	private static final int[] COLORS = {0xEE502E, 0xFFC845, 0xFFFFFF};

	/**
	 * Instantiates a {@link DefaultLightProvider}.
	 */
	public DefaultLightProvider() {
		super();
		for (int i = 0; i < NAMES.length; i++) {
			final DefaultLight light = new DefaultLight(NAMES[i]);
			light.setBrightness(RAND.nextFloat());
			light.switchOn((RAND.nextInt() & 1) == 0);
			light.setColor(COLORS[i]);
			add(light);
		}
	}

	@Override
	public com.microej.demo.smarthome.data.light.Light[] list() {
		com.microej.demo.smarthome.data.light.Light[] list = new com.microej.demo.smarthome.data.light.Light[this.devices
				.size()];
		list = this.devices.toArray(list);
		return list;
	}

}
