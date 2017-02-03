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

	public static String getMenuPath(final String name) {
		return getMenuPath(name, false);
	}

	public static String getMenuPath(String name, final boolean active) {
		name = MENU + name;
		return getImagePath(name, active);
	}

	public static String getDashBoardPath(final String name) {
		return getDashBoardPath(name, false);
	}

	public static String getDashBoardPath(String name, final boolean active) {
		name = DASHBOARD + name;
		return getImagePath(name, active);
	}

	public static Image loadDashBoard(final String name) {
		return loadDashBoard(name, false);
	}

	public static Image loadDashBoard(final String name, final boolean active) {
		return loadImage(getDashBoardPath(name, active));
	}

	public static Image loadMenu(final String name) {
		return loadImage(getMenuPath(name));
	}

	public static Image loadMenu(final String name, final boolean active) {
		return loadImage(getMenuPath(name, active));
	}

	public static Image loadImage(final String name, final boolean active) {
		return loadImage(getImagePath(name, active));
	}

	public static String getImagePath(String name, final boolean active) {
		if (active) {
			name += ACTIVE;
		}
		return name;
	}

	public static String getAbsolutePath(final String name) {
		return BASE + name + SUFFIX;
	}

	public static Image loadImage(final String name) {
		return StyleHelper.getImage(getAbsolutePath(name));
	}
}
