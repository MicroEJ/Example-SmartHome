/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.philipshue;

import java.util.Random;

import com.microej.demo.smarthome.data.impl.Device;
import com.microej.demo.smarthome.data.light.LightEventListener;
import com.microej.demo.smarthome.util.ExecutorUtils;

import sew.light.Light;
import sew.light.LightListener;
import sew.light.util.Color;

/**
 *
 */
public class HueLightSensor extends Device<LightEventListener>
implements com.microej.demo.smarthome.data.light.Light, LightListener {

	private static final float MIN_DIFF = 0.95f;
	private final Light light;
	private final Color color;
	private int rgbColor;
	private boolean isOn;

	/**
	 * @param light
	 * @param lightGroup
	 */
	public HueLightSensor(final Light light) {
		super(light.getName());
		this.light = light;

		final Random rand = new Random(System.nanoTime());
		color = new Color(rand.nextFloat() * 360, 0.8f, 1f);
		rgbColor = color.toRGB();
		isOn = true;
		light.addListener(this);
	}

	@Override
	public int getColor() {
		return rgbColor;
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
	public void setColor(final int rgbColor) {
		if (this.rgbColor != rgbColor) {
			this.rgbColor = rgbColor;
			notifyColorChange(rgbColor);
			color.setColor(rgbColor);
			updateHueColor();
		}
	}



	@Override
	public void setBrightness(final float brightness) {
		final float value = color.getValue();
		final float f = value * MIN_DIFF;
		final float g = value * (2 - MIN_DIFF);
		if (brightness < f || brightness > g) {
			color.setValue(brightness);
			updateHueColor();
			notifyBrightness(brightness);
		}
	}


	@Override
	public void switchOn(final boolean on) {
		if (isOn != on) {
			isOn = on;
			ExecutorUtils.getExecutor(ExecutorUtils.LOW_PRIORITY).execute(new Runnable() {

				@Override
				public void run() {
					light.setOn(isOn);
				}
			});
			notifyState(on);
		}
	}

	@Override
	public void onLightUpdate(final Light light) {
		final Color newColor = light.getColor();
		final int rgb = newColor.toRGB();
		final float brightness = newColor.getValue();
		final boolean on = light.isOn();

		if (on != isOn) {
			isOn = on;
			notifyState(on);
		}

		if (rgb != rgbColor) {
			rgbColor = rgb;
			notifyColorChange(rgb);
		}

		if (brightness != color.getValue()) {
			color.setValue(brightness);
			notifyBrightness(brightness);
		}
	}

	private void updateHueColor() {
		ExecutorUtils.getExecutor(ExecutorUtils.LOW_PRIORITY).execute(new Runnable() {

			@Override
			public void run() {
				light.setColor(color);
			}
		});
	}

	/**
	 *
	 */
	private void notifyState(final boolean isOn) {
		for (final LightEventListener listener : listeners) {
			listener.onStateChange(isOn);
		}
	}

	/**
	 *
	 */
	private void notifyBrightness(final float value) {
		for (final LightEventListener listener : listeners) {
			listener.onBrightnessChange(value);
		}

	}

	private void notifyColorChange(final int rgbColor) {
		for (final LightEventListener lightEventListener : listeners) {
			lightEventListener.onColorChange(rgbColor);
		}
	}
}
