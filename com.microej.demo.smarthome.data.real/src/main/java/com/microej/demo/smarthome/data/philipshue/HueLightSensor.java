/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.philipshue;

import java.util.Random;
import java.util.concurrent.Executor;

import com.microej.demo.smarthome.data.impl.AbstractDevice;
import com.microej.demo.smarthome.data.light.LightEventListener;

import ej.util.concurrent.SingleThreadExecutor;
import sew.light.Light;
import sew.light.LightListener;
import sew.light.util.Color;
import sew.light.util.HSVColor;

/**
 *
 */
public class HueLightSensor extends AbstractDevice<LightEventListener>
implements com.microej.demo.smarthome.data.light.Light, LightListener {

	private static final double MIN_DIFF = 0.95f;
	public static final Executor EXECUTOR = new SingleThreadExecutor();
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
		color = new HSVColor(rand.nextDouble() * 360, 0.8f, 1f);
		isOn = true;
		EXECUTOR.execute(new Runnable() {

			@Override
			public void run() {
				onLightUpdate(light);
			}
		});
		rgbColor = color.toRGB();
		light.addListener(this);
	}

	@Override
	public int getColor() {
		return rgbColor;
	}

	@Override
	public float getBrightness() {
		return (float) color.getValue();
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
		final double value = color.getValue();
		final double f = value * MIN_DIFF;
		final double g = value * (2 - MIN_DIFF);
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
			EXECUTOR.execute(new Runnable() {

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
		final double brightness = newColor.getValue();
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
		EXECUTOR.execute(new Runnable() {

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
	private void notifyBrightness(final double value) {
		for (final LightEventListener listener : listeners) {
			listener.onBrightnessChange((float) value);
		}

	}

	private void notifyColorChange(final int rgbColor) {
		for (final LightEventListener lightEventListener : listeners) {
			lightEventListener.onColorChange(rgbColor);
		}
	}
}
