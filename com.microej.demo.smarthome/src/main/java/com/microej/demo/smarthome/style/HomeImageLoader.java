/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.style;

import ej.microui.display.Image;
import ej.style.image.DefaultImageLoader;
import ej.style.util.StyleHelper;

/**
 *
 */
public class HomeImageLoader extends DefaultImageLoader {

	private static final String SUFFIX = ".png";
	private static final String ACTIVE = "_active";
	private static final String BASE = "/images/";
	private static final String MENU = "menu/";
	private static final String DASHBOARD = "dashboard/";

	public static Image loadDashBoard(String name) {
		return loadDashBoard(name, false);
	}

	public static Image loadDashBoard(String name, boolean active) {
		name = DASHBOARD + name;
		return loadImage(name, active);
	}

	public static Image loadMenu(String name) {
		return loadMenu(name, false);
	}

	public static Image loadMenu(String name, boolean active) {
		name = MENU + name;
		return loadImage(name, active);
	}

	public static Image loadImage(String name, boolean active) {
		if (active) {
			name += ACTIVE;
		}
		return loadImage(name);
	}

	public static String getImageSource(String name) {
		return BASE + name + SUFFIX;
	}

	/**
	 * @param name
	 * @return
	 */
	public static Image loadImage(String name) {
		return StyleHelper.getImage(getImageSource(name));
	}
}
