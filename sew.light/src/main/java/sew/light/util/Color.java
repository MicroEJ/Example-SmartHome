/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sew.light.util;

public interface Color {

	/**
	 * Gets the hue.
	 *
	 * @return the hue.
	 */
	double getHue();

	/**
	 * Sets the hue.
	 *
	 * @param hue
	 *            the hue to set.
	 */
	void setHue(double hue);

	/**
	 * Gets the saturation.
	 *
	 * @return the saturation.
	 */
	double getSaturation();

	/**
	 * Sets the saturation.
	 *
	 * @param saturation
	 *            the saturation to set.
	 */
	void setSaturation(double saturation);

	/**
	 * Gets the value.
	 *
	 * @return the value.
	 */
	double getValue();

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the value to set.
	 */
	void setValue(double value);

	/**
	 * @param rgbColor
	 */
	void setColor(int rgbColor);

	/**
	 * Set the color from RGB, doesn't change the brightness.
	 *
	 * @param red
	 * @param green
	 * @param blue
	 */
	void setColor(int red, int green, int blue);

	int toRGB();
}
