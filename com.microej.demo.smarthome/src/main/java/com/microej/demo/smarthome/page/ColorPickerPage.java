/*
 * Java
 *
 * Copyright 2017 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.ColorPicker;

import ej.widget.navigation.page.Page;

/**
 * A page presenting the color picker.
 */
public class ColorPickerPage extends Page {

	private final ColorPicker colorPicker;

	/**
	 * Instantiates the page.
	 * 
	 * @param light
	 *            the light to update.
	 */
	public ColorPickerPage(final Light light) {
		this.colorPicker = new ColorPicker(light);
		addClassSelector(ClassSelectors.BODY);
		setWidget(this.colorPicker);
	}

	/**
	 * Gets the colorPicker.
	 *
	 * @return the colorPicker.
	 */
	public ColorPicker getColorPicker() {
		return this.colorPicker;
	}
}
