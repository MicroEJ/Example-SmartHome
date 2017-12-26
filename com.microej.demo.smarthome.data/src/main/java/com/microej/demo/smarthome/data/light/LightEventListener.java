/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.light;

/**
 * A listener for the lights events.
 */
public interface LightEventListener  {
	/**
	 * Call back function when the color changes.
	 * @param color the new color.
	 */
	void onColorChange(int color);

	/**
	 * Call back function when the brightness changes.
	 * @param brightness the new brightness.
	 */
	void onBrightnessChange(float brightness);

	/**
	 * Call back function when the light state changes.
	 * @param on the new state.
	 */
	void onStateChange(boolean on);
}
