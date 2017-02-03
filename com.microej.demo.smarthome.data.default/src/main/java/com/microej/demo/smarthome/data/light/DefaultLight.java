/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.light;

import com.microej.demo.smarthome.data.impl.AbstractDevice;


public class DefaultLight extends AbstractDevice<LightEventListener> implements Light {

	private boolean on;
	private int color;
	private float brightness;

	public DefaultLight(final String name) {
		super(name);
	}


	public DefaultLight(final Light light) {
		super(light.getName());
		on = light.isOn();
		color = light.getColor();
		brightness = light.getBrightness();
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
