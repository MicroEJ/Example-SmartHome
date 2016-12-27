/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sew.light.util;

import ej.kf.Proxy;

/**
 *
 */
public class ColorProxy extends Proxy<Color> implements Color {

	@Override
	public double getHue() {
		try {
			return invokeDouble();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void setHue(final double hue) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public double getSaturation() {
		try {
			return invokeDouble();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void setSaturation(final double saturation) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public double getValue() {
		try {
			return invokeDouble();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void setValue(final double value) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void setColor(final int rgbColor) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void setColor(final int red, final int green, final int blue) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int toRGB() {
		try {
			return invokeInt();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
