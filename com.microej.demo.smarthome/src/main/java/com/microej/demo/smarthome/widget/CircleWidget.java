/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
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
			this.x = AlignmentHelper.computeXLeftCorner(this.diameter, 0, bounds.getWidth(), style.getAlignment());
			this.y = AlignmentHelper.computeYTopCorner(this.diameter, 0, bounds.getHeight(), style.getAlignment());
			final int innerD = this.diameter / 2;
			final int innerX = this.x + (this.diameter - innerD) / 2;
			final int innerY = this.y + (this.diameter - innerD) / 2;
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

	@Override
	public Rectangle validateContent(final Style style, final Rectangle bounds) {
		return new Rectangle(0, 0, getPreferredWidth(), getPreferredHeight());
	}

	@Override
	public boolean contains(final int x, final int y) {
		final int radius = this.diameter / 2;
		final int dX = x - (this.x + radius);
		final int dY = y - (this.y + radius);
		final int rPointer = dX * dX + dY * dY;
		final int innerRadius = radius / 2;
		return rPointer <= innerRadius * innerRadius;
	}

	@Override
	public void setBounds(final int x, final int y, final int width, final int height) {
		super.setBounds(x, y, width, height);
		this.diameter = Math.min(width, height);
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
}
