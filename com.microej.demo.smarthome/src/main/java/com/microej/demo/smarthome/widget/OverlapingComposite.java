/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
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

	private static int EVENT_RATE = 30;

	private long nextEvent;

	@Override
	public Rectangle validateContent(final Style style, final Rectangle bounds) {
		final int widthHint = bounds.getWidth();
		final int heightHint = bounds.getHeight();
		int width = widthHint;
		int height = heightHint;
		final boolean maxWidth = (widthHint == MWT.NONE);
		final boolean maxHeight = (heightHint == MWT.NONE);

		for (final Widget w : getWidgets()) {
			w.validate(widthHint, heightHint);
			if (maxWidth) {
				width = Math.max(width, w.getPreferredWidth());
			}
			if (maxHeight) {
				height = Math.max(height, w.getPreferredHeight());
			}
		}

		return new Rectangle(0, 0, width, height);
	}

	@Override
	protected void setBoundsContent(final Rectangle bounds) {
		final int boundsX = bounds.getX();
		final int boundsY = bounds.getY();
		final int boundsWidth = bounds.getWidth();
		final int boundsHeight = bounds.getHeight();
		for (final Widget w : getWidgets()) {
			final int preferredWidth = w.getPreferredWidth();
			final int preferredHeight = w.getPreferredHeight();
			if (preferredWidth != 0 && preferredHeight != 0) {
				w.setBounds(boundsX, boundsY, boundsWidth, boundsHeight);
			} else {
				w.setBounds(0, 0, 0, 0);
			}
		}
	}

	@Override
	public void add(final Widget widget) throws NullPointerException, IllegalArgumentException {
		super.add(widget);
	}

	@Override
	public boolean handleEvent(final int event) {
		// Avoid OutOfEvent exception.
		final long currentTime = System.currentTimeMillis();
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
