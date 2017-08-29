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
 * The Image loader used for the demo.
 */
public class HomeImageLoader extends DefaultImageLoader {

	private static final String SUFFIX = ".png"; //$NON-NLS-1$
	private static final String ACTIVE = "_active"; //$NON-NLS-1$
	private static final String BASE = "/images/"; //$NON-NLS-1$
	private static final String MENU = "menu/"; //$NON-NLS-1$
	private static final String DASHBOARD = "dashboard/"; //$NON-NLS-1$

	/**
	 * Gets the path for a menu image.
	 *
	 * @param name
	 *            the name of the image.
	 * @return the path from the images root folder the the image.
	 */
	public static String getMenuPath(final String name) {
		return getMenuPath(name, false);
	}

	/**
	 * Gets the path for a menu image.
	 *
	 * @param name
	 *            the name of the image.
	 * @param active
	 *            Whether the version of the image should be active or not.
	 * @return the path from the images root folder the the image.
	 */
	public static String getMenuPath(String name, final boolean active) {
		return getImageName(MENU + name, active);
	}

	/**
	 * Gets the path for a dashboard image.
	 *
	 * @param name
	 *            the name of the image.
	 * @return the path from the images root folder the the image.
	 */
	public static String getDashBoardPath(final String name) {
		return getDashBoardPath(name, false);
	}

	/**
	 * Gets the path for a dashboard image.
	 *
	 * @param name
	 *            the name of the image.
	 * @param active
	 *            Whether the version of the image should be active or not.
	 * @return the path from the images root folder the the image.
	 */
	public static String getDashBoardPath(String name, final boolean active) {
		return getImageName(DASHBOARD + name, active);
	}

	/**
	 * Gets the image name depending on its state.
	 *
	 * @param name
	 *            the base image name.
	 * @param active
	 *            Whether the version of the image should be active or not.
	 * @return The image name.
	 */
	private static String getImageName(String name, final boolean active) {
		if (active) {
			name += ACTIVE;
		}
		return name;
	}

	/**
	 * Get absolute path to an image with its suffix.
	 *
	 * @param name
	 *            the image name.
	 * @return The path.
	 */
	public static String getAbsolutePath(final String name) {
		return BASE + name + SUFFIX;
	}

	/**
	 * Gets a dashboard image.
	 *
	 * @param name
	 *            the name of the image.
	 * @return the retrieved image.
	 * @throws NullPointerException
	 *             if the given source is <code>null</code>.
	 */
	public static Image loadDashBoard(final String name) throws NullPointerException {
		return loadDashBoard(name, false);
	}

	/**
	 * Gets a dashboard image.
	 *
	 * @param name
	 *            the name of the image.
	 * @param active
	 *            Whether the version of the image should be active or not.
	 * @return the retrieved image.
	 * @throws NullPointerException
	 *             if the given source is <code>null</code>.
	 */
	public static Image loadDashBoard(final String name, final boolean active) throws NullPointerException {
		return loadImage(getDashBoardPath(name, active));
	}

	/**
	 * Gets a menu image.
	 *
	 * @param name
	 *            the name of the image.
	 * @return the retrieved image.
	 * @throws NullPointerException
	 *             if the given source is <code>null</code>.
	 */
	public static Image loadMenu(final String name) throws NullPointerException {
		return loadImage(getMenuPath(name));
	}

	/**
	 * Gets a menu image.
	 *
	 * @param name
	 *            the name of the image.
	 * @param active
	 *            Whether the version of the image should be active or not.
	 * @return the retrieved image.
	 * @throws NullPointerException
	 *             if the given source is <code>null</code>.
	 */
	public static Image loadMenu(final String name, final boolean active) throws NullPointerException {
		return loadImage(getMenuPath(name, active));
	}

	/**
	 * Gets an image.
	 *
	 * @param name
	 *            the name of the image.
	 * @return the retrieved image.
	 * @throws NullPointerException
	 *             if the given source is <code>null</code>.
	 */
	public static Image loadImage(final String name) throws NullPointerException {
		return StyleHelper.getImage(getAbsolutePath(name));
	}
}
