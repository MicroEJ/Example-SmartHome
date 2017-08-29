/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.light;

import com.microej.demo.smarthome.data.Device;

/**
 * A light device.
 */
public interface Light extends Device<LightEventListener> {

	/**
	 * Gets the light color.
	 * @return the color.
	 */
	int getColor();

	/**
	 * Gets the brightness of the light.
	 * @return the brightness.
	 */
	float getBrightness();

	/**
	 * Checks if the light is on.
	 * @return true if the light is on.
	 */
	boolean isOn();

	/**
	 * Sets the light color.
	 * @param color the color.
	 */
	void setColor(int color);

	/**
	 * Sets the brightness.
	 * @param brightness the brightness to set.
	 */
	void setBrightness(float brightness);

	/**
	 * Switch the light.
	 * @param on true if on.
	 */
	void switchOn(boolean on);
}
