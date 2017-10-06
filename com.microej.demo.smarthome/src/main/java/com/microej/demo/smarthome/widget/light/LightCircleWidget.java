/*
 * Java
 *
 * Copyright 2017 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.light;

import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.data.light.LightEventListener;
import com.microej.demo.smarthome.widget.CircleWidget;

import ej.style.Style;

/**
 * A CircleWidget using a light as a model.
 */
public class LightCircleWidget extends CircleWidget implements LightEventListener {

	private final Light light;

	/**
	 * Instantiates a LightCircleWidget.
	 * 
	 * @param light
	 *            the model.
	 */
	public LightCircleWidget(final Light light) {
		super();
		this.light = light;

		setEnabled(light.isOn());
	}

	@Override
	protected int getColor(final Style style) {
		return this.light.getColor();
	}

	@Override
	public void onColorChange(final int color) {
		repaint();
	}

	@Override
	public void onBrightnessChange(final float brightness) {
		// Do nothing.

	}

	@Override
	public void onStateChange(final boolean on) {
		setEnabled(on);

	}

	@Override
	public void showNotify() {
		super.showNotify();
		this.light.addListener(this);
		setEnabled(this.light.isOn());
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		this.light.removeListener(this);
	}

}
