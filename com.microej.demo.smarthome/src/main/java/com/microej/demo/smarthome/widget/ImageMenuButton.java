/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.style.HomeImageLoader;

import ej.widget.basic.Image;

/**
 *
 */
public class ImageMenuButton extends MenuButton {

	private final Image image;
	private final String name;
	private boolean focus = false;

	/**
	 * @param source
	 */
	public ImageMenuButton(String name) {
		super();
		this.name = name;
		image = new Image(HomeImageLoader.loadMenu(name));
		setWidget(image);
	}

	@Override
	public void setFocus(boolean focus) {
		if (this.focus != focus) {
			this.focus = focus;
			image.setSource(HomeImageLoader.loadMenu(name, focus));
			image.repaint();
			super.setFocus(focus);
		}
	}
}
