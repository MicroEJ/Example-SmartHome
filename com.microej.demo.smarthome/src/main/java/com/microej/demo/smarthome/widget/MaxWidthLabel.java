/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.microui.display.Font;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.StyleHelper;
import ej.widget.basic.Label;

/**
 *
 */
public class MaxWidthLabel extends Label {

	private String maxText;
	/**
	 *
	 */
	public MaxWidthLabel(String maxText) {
		super();
		this.maxText = maxText;
	}

	/**
	 * @param text
	 */
	public MaxWidthLabel(String text, String maxtText) {
		super(text);
		maxText = maxtText;
	}

	@Override
	public Rectangle validateContent(Style style, Rectangle availableSize) {
		Font font = StyleHelper.getFont(style);
		return style.getTextManager().computeContentSize(maxText, font, availableSize);
	}

	/**
	 * Gets the maxText.
	 *
	 * @return the maxText.
	 */
	public String getMaxText() {
		return maxText;
	}

	/**
	 * Sets the maxText.
	 *
	 * @param maxText
	 *            the maxText to set.
	 */
	public void setMaxText(String maxText) {
		this.maxText = maxText;
	}

}
