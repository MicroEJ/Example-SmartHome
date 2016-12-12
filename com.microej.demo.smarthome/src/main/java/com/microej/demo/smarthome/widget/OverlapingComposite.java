/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.mwt.MWT;
import ej.mwt.Widget;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.widget.StyledComposite;

/**
 *
 */
public class OverlapingComposite extends StyledComposite {

	private static int EVENT_RATE = 80;

	private long nextEvent;

	@Override
	public Rectangle validateContent(Style style, Rectangle bounds) {
		int widthHint = bounds.getWidth();
		int heightHint = bounds.getHeight();
		int width = bounds.getWidth();
		int height = bounds.getHeight();
		boolean maxWidth = (widthHint == MWT.NONE);
		boolean maxHeight = (heightHint == MWT.NONE);

		for (Widget w : getWidgets()) {
			w.validate(widthHint, heightHint);
			if (maxWidth) {
				width = Math.max(width, w.getPreferredWidth());
			}
			if (maxHeight) {
				height = Math.max(height, w.getPreferredHeight());
			}
		}

		return new Rectangle(0, 0, widthHint, heightHint);
	}

	@Override
	protected void setBoundsContent(Rectangle bounds) {
		int boundsX = bounds.getX();
		int boundsY = bounds.getY();
		int boundsWidth = bounds.getWidth();
		int boundsHeight = bounds.getHeight();
		for (Widget w : getWidgets()) {
			w.setBounds(boundsX, boundsY, boundsWidth, boundsHeight);
			// int preferredWidth = w.getPreferredWidth();
			// int preferredHeight = w.getPreferredHeight();
			// int alignment = getStyle().getAlignment();
			// int x = AlignmentHelper.computeXLeftCorner(preferredWidth, boundsX, boundsWidth, alignment);
			// int y = AlignmentHelper.computeYTopCorner(preferredHeight, boundsY, boundsHeight, alignment);
			// w.setBounds(x, y, preferredWidth, preferredHeight);
		}
	}

	@Override
	public void add(Widget widget) throws NullPointerException, IllegalArgumentException {
		super.add(widget);
	}

	@Override
	public boolean handleEvent(int event) {
		// Avoid OutOfEvent exception.
		long currentTime = System.currentTimeMillis();
		if (currentTime > nextEvent) {
			nextEvent = currentTime + EVENT_RATE;
			int i = getWidgetsCount();
			while (i > 0) {
				i--;
				if (getWidget(i).handleEvent(event)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
}
