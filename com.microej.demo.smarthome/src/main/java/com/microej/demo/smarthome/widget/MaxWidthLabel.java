/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.microui.display.Font;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.StyleHelper;
import ej.widget.basic.Label;

/**
 * A label with a max width label.
 */
public class MaxWidthLabel extends Label {

	private String maxText;

	/**
	 * Instantiates a MaxWidthLabel.
	 *
	 * @param maxText
	 *            A text with the max size.
	 */
	public MaxWidthLabel(final String maxText) {
		super();
		this.maxText = maxText;
	}

	/**
	 * Instantiates a MaxWidthLabel.
	 *
	 * @param text
	 *            the text to display.
	 * @param maxText
	 *            A text with the max size.
	 */
	public MaxWidthLabel(final String text, final String maxtText) {
		super(text);
		maxText = maxtText;
	}

	@Override
	public Rectangle validateContent(final Style style, final Rectangle availableSize) {
		final Font font = StyleHelper.getFont(style);
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
	public void setMaxText(final String maxText) {
		this.maxText = maxText;
		invalidate();
		repaint();
	}
}
