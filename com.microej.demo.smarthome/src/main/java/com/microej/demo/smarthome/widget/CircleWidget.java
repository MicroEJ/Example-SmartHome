/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.style.Style;
import ej.style.container.AlignmentHelper;
import ej.style.container.Rectangle;
import ej.widget.StyledWidget;

/**
 * A widget displaying a circle with a border.
 */
public class CircleWidget extends StyledWidget {

	private int diameter;

	private int x;
	private int y;

	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		if (isEnabled()) {
			x = AlignmentHelper.computeXLeftCorner(diameter, 0, bounds.getWidth(),
					style.getAlignment());
			y = AlignmentHelper.computeYTopCorner(diameter, 0, bounds.getHeight(),
					style.getAlignment());
			final int innerD = diameter / 2;
			final int innerX = x + (diameter - innerD) / 2;
			final int innerY = y + (diameter - innerD) / 2;
			g.removeBackgroundColor();
			g.setColor(getColor(style));
			g.fillCircle(innerX, innerY, innerD);

			// draw anti aliased circle
			final AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
			antiAliasedShapes.setThickness(2);
			antiAliasedShapes.setFade(1);

			// draw border
			g.setColor(style.getBackgroundColor());
			antiAliasedShapes.setThickness(2);
			antiAliasedShapes.drawCircle(g, innerX, innerY, innerD);
		}
	}

	/**
	 * Gets the foreground color.
	 *
	 * @param style
	 *            the style of the widget.
	 * @return the foreground color to use.
	 */
	protected int getColor(final Style style) {
		return style.getForegroundColor();
	}

	@Override
	public Rectangle validateContent(final Style style, final Rectangle bounds) {
		return new Rectangle(0, 0, getPreferredWidth(), getPreferredHeight());
	}

	@Override
	public boolean contains(final int x, final int y) {
		final int radius = diameter / 2;
		final int dX = x - (this.x + radius);
		final int dY = y - (this.y + radius);
		final int d2 = dX * dX + dY * dY;
		final int r = radius >> 1;
		return d2 <= r * r;
	}

	@Override
	public void setBounds(final int x, final int y, final int width, final int height) {
		super.setBounds(x, y, width, height);
		diameter = Math.min(width, height);
	}
}
