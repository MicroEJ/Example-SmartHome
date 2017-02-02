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


public class ColorPickerPage extends Page {

	private final ColorPicker colorPicker;

	public ColorPickerPage(final Light light) {
		colorPicker = new ColorPicker(light);
		addClassSelector(ClassSelectors.BODY);
		setWidget(colorPicker);
	}

	/**
	 * Gets the colorPicker.
	 *
	 * @return the colorPicker.
	 */
	public ColorPicker getColorPicker() {
		return colorPicker;
	}
}
