/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.style.HomeImageLoader;

import ej.widget.basic.image.ImageToggle;

public class ImageMenuButton extends ImageToggle {

	private final String name;

	public ImageMenuButton(final String name) {
		super();
		this.name = name;
	}

	@Override
	protected String getCheckedImagePath() {
		final String absolutePath = HomeImageLoader.getAbsolutePath(HomeImageLoader.getMenuPath(name, true));
		return absolutePath;
	}

	@Override
	protected String getUncheckedImagePath() {
		final String absolutePath = HomeImageLoader.getAbsolutePath(HomeImageLoader.getMenuPath(name, false));
		return absolutePath;
	}


}
