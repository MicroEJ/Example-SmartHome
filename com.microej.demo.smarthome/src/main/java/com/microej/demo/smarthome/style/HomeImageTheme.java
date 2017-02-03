/*
 * Java
 *
 * Copyright 2015 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.style;

import com.microej.demo.smarthome.util.Images;

import ej.widget.basic.image.ImageTheme;

/**
 * The image theme used in the demo.
 */
public class HomeImageTheme implements ImageTheme {

	@Override
	public String getCheckboxCheckedPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCheckboxUncheckedPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSwitchCheckedPath() {
		return HomeImageLoader.getAbsolutePath(Images.TOGGLE_ON);
	}

	@Override
	public String getSwitchUncheckedPath() {
		return HomeImageLoader.getAbsolutePath(Images.TOGGLE_OFF);
	}

	@Override
	public String getRadioButtonCheckedPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRadioButtonUncheckedPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSliderHorizontalBarPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSliderVerticalBarPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSliderCursorPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getProgressBarHorizontalPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getProgressBarVerticalPath() {
		throw new UnsupportedOperationException();
	}

}
