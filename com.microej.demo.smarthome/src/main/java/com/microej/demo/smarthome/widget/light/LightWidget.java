/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.light;

import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.data.light.LightEventListener;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.ColorPicker;
import com.microej.demo.smarthome.widget.DeviceWidget;

import ej.mwt.Desktop;
import ej.mwt.Panel;
import ej.widget.composed.ButtonImage;
import ej.widget.listener.OnClickListener;
import ej.widget.listener.OnValueChangeListener;
import ej.widget.model.BoundedRangeModel;
import ej.widget.model.DefaultBoundedRangeModel;

/**
 *
 */
public class LightWidget extends DeviceWidget<Light> implements LightEventListener {

	/**
	 * Values
	 */
	private static final String IMAGE_TOGGLE_ON = "/images/toggle_on.png";
	private static final String IMAGE_TOGGLE_OFF = "/images/toggle_off.png";

	/**
	 * Attributes
	 */
	private final LightCircularProgress circular;
	private final ButtonImage toggleButton;

	/**
	 * Constructor
	 */
	public LightWidget(Light model) {
		super(model);
		model.addListener(this);
		addClassSelector(ClassSelectors.LIGHT_WIDGET);

		// circular progress
		BoundedRangeModel boundedRange = new DefaultBoundedRangeModel(0, 1000, 0);
		circular = new LightCircularProgress(boundedRange, new OnClickListener() {
			@Override
			public void onClick() {
				changeColor();
			}
		});
		circular.addClassSelector(ClassSelectors.LIGHT_PROGRESS);

		// toggle button
		toggleButton = new ButtonImage(model.isOn() ? IMAGE_TOGGLE_ON : IMAGE_TOGGLE_OFF);
		toggleButton.addOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				toggle();
			}
		});
		toggleButton.addClassSelector(ClassSelectors.LIGHT_PROGRESS);

		// place widgets
		setCenter(circular);
		addBottom(toggleButton);

		// set initial state
		circular.setColor(model.getColor());
		setBrightness(model.getBrightness());
	}

	/**
	 * Handle color change event
	 */
	@Override
	public void onColorChange(int color) {
		circular.setColor(color);
	}

	/**
	 * Handle brightness change event
	 */
	@Override
	public void onBrightnessChange(float brightness) {
		setBrightness(brightness);
	}

	/**
	 * Handle state change event
	 */
	@Override
	public void onStateChange(boolean on) {
		circular.setEnabled(on);
		toggleButton.setSource(on ? IMAGE_TOGGLE_ON : IMAGE_TOGGLE_OFF);
	}

	/**
	 * Sets the brightness value
	 */
	private void setBrightness(float brightness) {
		int min = circular.getMinimum();
		int max = circular.getMaximum();
		float value = brightness * (max - min) + min;
		circular.setValue((int) value);
	}

	/**
	 * Toggles the light
	 */
	private void toggle() {
		model.switchOn(!model.isOn());
	}

	/**
	 * Changes light color
	 */
	private void changeColor() {
		// calculate animation source position
		int sourceX = circular.getAbsoluteX() + circular.getWidth() / 2;
		int sourceY = circular.getAbsoluteY() + circular.getHeight() / 2;

		// create color change listener
		OnValueChangeListener listener = new OnValueChangeListener() {
			@Override
			public void onValueChange(int newValue) {
				model.setColor(newValue);
			}

			@Override
			public void onMinimumValueChange(int newMinimum) {
			}

			@Override
			public void onMaximumValueChange(int newMaximum) {
			}
		};

		// create color picker
		ColorPicker picker = new ColorPicker(sourceX, sourceY);
		picker.addOnValueChangeListener(listener);

		// show color picker
		Desktop desktop = new Desktop();
		Panel panel = new Panel();
		panel.setWidget(picker);
		panel.show(desktop, true);
		desktop.show();
	}
}
