/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.light;

import com.microej.demo.smarthome.data.impl.Device;
import com.microej.demo.smarthome.data.light.LightEventListener;

/**
 *
 */
public class Light extends Device<LightEventListener> implements com.microej.demo.smarthome.data.light.Light {

	private boolean on;
	private int color;
	private float brightness;
	/**
	 * @param name
	 */
	public Light(final String name) {
		super(name);
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public float getBrightness() {
		return brightness;
	}

	@Override
	public void setColor(final int color) {
		this.color = color;
		for (final LightEventListener lightEventListener : listeners) {
			lightEventListener.onColorChange(color);
		}
	}

	@Override
	public void setBrightness(final float brightness) {
		if (this.brightness != brightness) {
			this.brightness = brightness;
			for (final LightEventListener lightEventListener : listeners) {
				lightEventListener.onBrightnessChange(brightness);
			}
		}
	}

	@Override
	public void switchOn(final boolean on) {
		if (this.on != on) {
			this.on = on;
			for (final LightEventListener lightEventListener : listeners) {
				lightEventListener.onStateChange(on);
			}
		}
	}

	@Override
	public boolean isOn() {
		return on;
	}
}
