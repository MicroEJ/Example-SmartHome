/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.philipshue;

import com.microej.demo.smarthome.data.impl.Device;
import com.microej.demo.smarthome.data.light.LightEventListener;
import com.microej.demo.smarthome.util.ExecutorUtils;

import sew.light.Light;
import sew.light.util.Color;

/**
 *
 */
public class HueLightSensor extends Device<LightEventListener>
implements com.microej.demo.smarthome.data.light.Light {

	private final Light light;
	private final Color color;
	private boolean isOn;

	/**
	 * @param light
	 * @param lightGroup
	 */
	public HueLightSensor(Light light) {
		super(light.getName());
		this.light = light;
		color = new Color(0, 0, 0);
		isOn = true;
		ExecutorUtils.getExecutor(ExecutorUtils.LOW_PRIORITY).execute(new Runnable() {

			@Override
			public void run() {
				Color color = light.getColor();
				color.setValue(color.getValue());
				color.setHue(color.getHue());
				color.setSaturation(color.getSaturation());
				isOn = light.isOn();
			}
		});
	}

	@Override
	public int getColor() {
		return color.toRGB();
	}

	@Override
	public float getBrightness() {
		return color.getValue();
	}

	@Override
	public boolean isOn() {
		return isOn;
	}

	@Override
	public void setColor(int rgbColor) {
		color.setHue(rgbColor);
		ExecutorUtils.getExecutor(ExecutorUtils.LOW_PRIORITY).execute(new Runnable() {

			@Override
			public void run() {
				light.setColor(color);
			}
		});
	}

	@Override
	public void setBrightness(float brightness) {
		color.setValue(brightness);
		ExecutorUtils.getExecutor(ExecutorUtils.LOW_PRIORITY).execute(new Runnable() {

			@Override
			public void run() {
				light.setColor(color);
			}
		});
	}

	@Override
	public void switchOn(boolean on) {
		if (isOn != on) {
			isOn = on;
			ExecutorUtils.getExecutor(ExecutorUtils.LOW_PRIORITY).execute(new Runnable() {

				@Override
				public void run() {
					light.setOn(isOn);
				}
			});
		}
	}

}
