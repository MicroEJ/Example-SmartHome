/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.philipshue;

import com.microej.demo.smarthome.data.fake.Device;
import com.microej.demo.smarthome.data.light.LightEventListener;

import sew.light.Light;
import sew.light.util.Color;

/**
 *
 */
public class HueLightSensor extends Device<LightEventListener>
implements com.microej.demo.smarthome.data.light.Light {

	private final Light light;

	/**
	 * @param light
	 * @param lightGroup
	 */
	public HueLightSensor(Light light) {
		super(light.getName());
		this.light = light;
	}

	@Override
	public int getColor() {
		return light.getColor().toRGB();
	}

	@Override
	public float getBrightness() {
		return light.getColor().getValue();
	}

	@Override
	public boolean isOn() {
		return light.isOn();
	}

	@Override
	public void setColor(int rgbColor) {
		Color color = light.getColor();
		color.setHue(rgbColor);
		light.setColor(color);
	}

	@Override
	public void setBrightness(float brightness) {
		Color color = light.getColor();
		color.setValue(brightness);
		light.setColor(color);
	}

	@Override
	public void switchOn(boolean on) {
		light.setOn(on);
	}

}
