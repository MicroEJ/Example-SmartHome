/*
 * Sébastien Eon 2016 / CC0-1.0
 */
package sew.light.util;

import ej.color.ColorHelper;

public class Color {

	private float hue;
	private float saturation;
	private float value;

	/**
	 * @param hue
	 * @param saturation
	 * @param value
	 */
	public Color(float hue, float saturation, float value) {
		super();
		this.hue = hue % 360f;
		this.saturation = saturation;
		this.value = value;
	}

	/**
	 * Gets the hue.
	 *
	 * @return the hue.
	 */
	public float getHue() {
		return this.hue;
	}

	/**
	 * Sets the hue.
	 *
	 * @param hue
	 *            the hue to set.
	 */
	public void setHue(float hue) {
		this.hue = hue % 360f;
	}

	/**
	 * Gets the saturation.
	 *
	 * @return the saturation.
	 */
	public float getSaturation() {
		return this.saturation;
	}

	/**
	 * Sets the saturation.
	 *
	 * @param saturation
	 *            the saturation to set.
	 */
	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value.
	 */
	public float getValue() {
		return this.value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the value to set.
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * @param rgbColor
	 */
	public void setColor(int rgbColor) {
		setColor(ColorHelper.getRed(rgbColor), ColorHelper.getGreen(rgbColor), ColorHelper.getBlue(rgbColor));

	}

	/**
	 * Set the color from RGB, doesn't change the brightness.
	 *
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(int red, int green, int blue) {
		float h, s;

		float r = red / 255f;
		float g, b;
		float min, max, delta;

		min = Math.min(Math.min(red, green), blue);
		max = Math.max(Math.max(red, green), blue);

		delta = max - min;

		if (delta == 0) {
			h = 0;
			s = 0;
		} else {
			// S
			s = delta / max;

			// H
			if (red == max) {
				h = (green - blue) / delta; // between yellow & magenta
			} else if (green == max) {
				h = 2 + (blue - red) / delta; // between cyan & yellow
			} else {
				h = 4 + (red - green) / delta; // between magenta & cyan
			}

			h *= 60; // degrees
		}
		if (h < 0) {
			h += 360;
		}

		this.saturation = s;
		this.hue = h;
		// this.value = max / 255f;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Color) {
			Color other = (Color) obj;
			return other.hue == this.hue && other.saturation == this.saturation && other.value == this.value;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	public int toRGB() {
		float hue = this.hue / 360f;
		float saturation = this.saturation;
		float value = this.value;
		// Ugly fix for our designer.
		value = value * 0.15f + 0.85f;
		float r, g, b;

		int h = (int) (hue * 6);
		float f = hue * 6 - h;
		float p = value * (1 - saturation);
		float q = value * (1 - f * saturation);
		float t = value * (1 - (1 - f) * saturation);

		if (h == 0) {
			r = value;
			g = t;
			b = p;
		} else if (h == 1) {
			r = q;
			g = value;
			b = p;
		} else if (h == 2) {
			r = p;
			g = value;
			b = t;
		} else if (h == 3) {
			r = p;
			g = q;
			b = value;
		} else if (h == 4) {
			r = t;
			g = p;
			b = value;
		} else if (h <= 6) {
			r = value;
			g = p;
			b = q;
		} else {
			throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", "
					+ saturation + ", " + value);
		}

		int rgb = ColorHelper.getColor((int) (r * 255), (int) (g * 255), (int) (b * 255));
		// System.out.println("LightsListPage.getRGB() 0x" + Integer.toHexString(rgb) + ", Input was " + hue + ", "
		// + saturation + ", " + value);
		return rgb;
	}
}
