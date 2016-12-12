/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.fake.light;

import com.microej.demo.smarthome.data.fake.Device;
import com.microej.demo.smarthome.data.light.LightEventListener;

/**
 *
 */
public class Light extends Device<LightEventListener> implements com.microej.demo.smarthome.data.light.Light {

	private boolean on = true;
	private int color = 0x556644;
	private float brightness = 0.4f;
	/**
	 * @param name
	 */
	public Light(String name) {
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
	public void setColor(int color) {
		this.color = color;
		for (LightEventListener lightEventListener : listeners) {
			lightEventListener.onColorChange(color);
		}
	}

	@Override
	public void setBrightness(float brightness) {
		this.brightness = brightness;
		for (LightEventListener lightEventListener : listeners) {
			lightEventListener.onBrightnessChange(brightness);
		}
	}

	@Override
	public void switchOn(boolean on) {
		this.on = on;
		for (LightEventListener lightEventListener : listeners) {
			lightEventListener.onStateChange(on);
		}
	}

	@Override
	public boolean isOn() {
		return on;
	}
}
