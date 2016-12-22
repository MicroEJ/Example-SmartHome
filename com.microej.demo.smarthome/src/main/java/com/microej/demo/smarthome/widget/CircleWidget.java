/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.style.Style;
import ej.style.container.AlignmentHelper;
import ej.style.container.Rectangle;
import ej.widget.StyledWidget;
import ej.widget.listener.OnClickListener;

/**
 *
 */
public class CircleWidget extends StyledWidget {

	private int customColor;
	private int diameter;

	/**
	 * Attributes
	 */
	private final OnClickListener onClickListener;
	private int x;
	private int y;

	/**
	 *
	 */
	public CircleWidget(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	@Override
	public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
		if (isEnabled()) {
			x = AlignmentHelper.computeXLeftCorner(diameter, 0, bounds.getWidth(),
					style.getAlignment());
			y = AlignmentHelper.computeYTopCorner(diameter, 0, bounds.getHeight(),
					style.getAlignment());
			int innerD = diameter / 2;
			int innerX = x + (diameter - innerD) / 2;
			int innerY = y + (diameter - innerD) / 2;
			g.setColor(customColor);
			g.removeBackgroundColor();
			g.fillCircle(innerX, innerY, innerD);

			// draw anti aliased circle
			AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
			antiAliasedShapes.setThickness(2);
			antiAliasedShapes.setFade(1);
			// antiAliasedShapes.drawCircle(g, innerX, innerY, innerD);

			// draw border
			g.setColor(style.getBackgroundColor());
			antiAliasedShapes.setThickness(2);
			antiAliasedShapes.drawCircle(g, innerX, innerY, innerD);
		}

	}

	@Override
	public Rectangle validateContent(Style style, Rectangle bounds) {
		return new Rectangle(0, 0, getPreferredWidth(), getPreferredHeight());
	}

	/**
	 * Handles click events
	 */
	@Override
	public boolean handleEvent(int event) {
		if (isEnabled()) {
			if (Event.getType(event) == Event.POINTER) {
				Pointer pointer = (Pointer) Event.getGenerator(event);
				int pointerX = this.getRelativeX(pointer.getX());
				int pointerY = this.getRelativeY(pointer.getY());
				int radius = diameter / 2;
				int dX = pointerX - (x + radius);
				int dY = pointerY - (y + radius);
				int d2 = dX * dX + dY * dY;
				int r = radius / 2;
				if (d2 <= r * r) {
					if (Pointer.getAction(event) == Pointer.RELEASED) {
						performClick();
						return true;
					}
				}
			}
		}
		return super.handleEvent(event);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		diameter = Math.min(width, height);
	}

	// @Override
	// public boolean isTransparent() {
	// return false;
	// }


	public void performClick() {
		this.onClickListener.onClick();
	}

	public void setColor(int color) {
		if (color != customColor) {
			customColor = color;
			repaint();
		}
	}
}
