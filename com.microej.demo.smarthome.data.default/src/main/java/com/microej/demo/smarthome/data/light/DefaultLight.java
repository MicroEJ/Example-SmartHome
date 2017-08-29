/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.light;

import com.microej.demo.smarthome.data.impl.AbstractDevice;

/**
 * An implementation of a {@link Light}.
 */
public class DefaultLight extends AbstractDevice<LightEventListener> implements Light {

	private boolean on;
	private int color;
	private float brightness;

	/**
	 * Instantiates a {@link DefaultLight}.
	 * @param name the light's name.
	 */
	public DefaultLight(final String name) {
		super(name);
	}

	/**
	 * Instantiates a {@link DefaultLight}, duplicating the light given in parameter.
	 * @param light the light to duplicate.
	 */
	public DefaultLight(final Light light) {
		super(light.getName());
		this.on = light.isOn();
		this.color = light.getColor();
		this.brightness = light.getBrightness();
	}

	@Override
	public int getColor() {
		return this.color;
	}

	@Override
	public float getBrightness() {
		return this.brightness;
	}

	@Override
	public void setColor(final int color) {
		this.color = color;
		for (final LightEventListener lightEventListener : this.listeners) {
			lightEventListener.onColorChange(color);
		}
	}

	@Override
	public void setBrightness(final float brightness) {
		if (this.brightness != brightness) {
			this.brightness = brightness;
			for (final LightEventListener lightEventListener : this.listeners) {
				lightEventListener.onBrightnessChange(brightness);
			}
		}
	}

	@Override
	public void switchOn(final boolean on) {
		if (this.on != on) {
			this.on = on;
			for (final LightEventListener lightEventListener : this.listeners) {
				lightEventListener.onStateChange(on);
			}
		}
	}

	@Override
	public boolean isOn() {
		return this.on;
	}
}
