/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.style;

import ej.style.font.FontProfile;
import ej.style.font.loader.AbstractFontLoader;

/**
 * The font loader used in the application.
 */
public class HomeFontLoader extends AbstractFontLoader {
	private static final int XX_SMALL_HEIGHT = 12;
	private static final int X_SMALL_HEIGHT = 18;
	private static final int SMALL_HEIGHT = 20;
	private static final int MEDIUM_HEIGHT = 30;
	private static final int LARGE_HEIGHT = 39;
	private static final int X_LARGE_HEIGHT = 59;
	private static final int XX_LARGE_HEIGHT = 102;

	@Override
	protected int getFontHeight(FontProfile fontProfile) {
		switch (fontProfile.getSize()) {
		case LENGTH:
			return fontProfile.getSizeValue();
		case XX_SMALL:
			return XX_SMALL_HEIGHT;
		case X_SMALL:
			return X_SMALL_HEIGHT;
		case SMALL:
			return SMALL_HEIGHT;
		case MEDIUM:
			return MEDIUM_HEIGHT;
		case LARGE:
			return LARGE_HEIGHT;
		case X_LARGE:
			return X_LARGE_HEIGHT;
		case XX_LARGE:
			return XX_LARGE_HEIGHT;
		default:
			return MEDIUM_HEIGHT;
		}
	}

}
