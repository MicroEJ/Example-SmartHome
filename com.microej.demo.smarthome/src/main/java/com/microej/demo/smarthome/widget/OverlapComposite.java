/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.mwt.MWT;
import ej.mwt.Widget;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.widget.StyledComposite;

/**
 * Composite positioning all its childs one over the other.
 */
public class OverlapComposite extends StyledComposite {

	@Override
	public Rectangle validateContent(final Style style, final Rectangle bounds) {
		int widthHint = bounds.getWidth();
		int heightHint = bounds.getHeight();
		final boolean maxWidth = (widthHint == MWT.NONE);
		final boolean maxHeight = (heightHint == MWT.NONE);

		for (final Widget w : getWidgets()) {
			w.validate(widthHint, heightHint);
			if (maxWidth) {
				widthHint = Math.max(widthHint, w.getPreferredWidth());
			}
			if (maxHeight) {
				heightHint = Math.max(heightHint, w.getPreferredHeight());
			}
		}

		return new Rectangle(0, 0, widthHint, heightHint);
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

	/**
	 * provides access to its child.
	 */
	@Override
	public void add(final Widget widget) throws NullPointerException, IllegalArgumentException {
		super.add(widget);
	}
	
	@Override
	public boolean handleEvent(int event) {
		Widget[] widgets = getWidgets();
		for (int i = widgets.length - 1; i >=0 ; i--) {
			Widget widget = widgets[i];
			if(widget.isEnabled() && widget.handleEvent(event)){
				break;
			}
		}
		return true;
	}
}
