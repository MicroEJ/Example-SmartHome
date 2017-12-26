/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.style.HomeImageLoader;

import ej.widget.basic.image.ImageToggle;

/**
 * An Image menu button.
 */
public class ImageMenuButton extends ImageToggle {

	private final String name;

	/**
	 * Instantiates an ImageMenuButton.
	 * 
	 * @param name
	 *            the image name.
	 */
	public ImageMenuButton(final String name) {
		super();
		this.name = name;
	}

	@Override
	protected String getCheckedImagePath() {
		final String absolutePath = HomeImageLoader.getAbsolutePath(HomeImageLoader.getMenuPath(this.name, true));
		return absolutePath;
	}

	@Override
	protected String getUncheckedImagePath() {
		final String absolutePath = HomeImageLoader.getAbsolutePath(HomeImageLoader.getMenuPath(this.name, false));
		return absolutePath;
	}

}
