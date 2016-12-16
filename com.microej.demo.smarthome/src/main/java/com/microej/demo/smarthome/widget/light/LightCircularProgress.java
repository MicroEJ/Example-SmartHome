/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.light;

import com.microej.demo.smarthome.widget.CircularProgressWidget;

import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.widget.listener.OnClickListener;
import ej.widget.model.BoundedRangeModel;

/**
 *
 */
public class LightCircularProgress extends CircularProgressWidget {

	/**
	 * Attributes
	 */
	private OnClickListener onClickListener;

	/**
	 * Constructor
	 */
	public LightCircularProgress(BoundedRangeModel model, OnClickListener onClickListener) {
		super(model);
		this.onClickListener = onClickListener;
	}

	/**
	 * Renders the widget
	 */
	@Override
	public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
		// render parent
		super.renderContent(g, style, bounds);

		// set style
		if (isEnabled() && customColor != null) {
			// fill circle
			int innerD = diameter / 2;
			int innerX = x + (diameter - innerD) / 2;
			int innerY = y + (diameter - innerD) / 2;
			g.setColor(customColor);
			g.fillCircle(innerX, innerY, innerD);
	
			// draw anti aliased circle
			AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
			antiAliasedShapes.setThickness(2);
			antiAliasedShapes.setFade(1);
			antiAliasedShapes.drawCircle(g, innerX, innerY, innerD);
		}
	}

	/**
	 * Handles click events
	 */
	@Override
	public boolean handleEvent(int event) {
		if (isEnabled()) {
			if (Event.getType(event) == Event.POINTER) {
				Pointer pointer = (Pointer) Event.getGenerator(event);
				Rectangle rect = new Rectangle();
				getStyle().getMargin().wrap(rect);
				int pointerX = this.getRelativeX(pointer.getX() + rect.getX());
				int pointerY = this.getRelativeY(pointer.getY() + rect.getY());
				int radius = diameter / 2;
				int dX = pointerX - (x + radius);
				int dY = pointerY - (y + radius);
				int d2 = dX*dX + dY*dY;
				if (d2 <= (radius/2) * (radius/2)) {
					if (Pointer.getAction(event) == Pointer.PRESSED) {
						this.onClickListener.onClick();
					}
					return true;
				}
			}
		}
		return super.handleEvent(event);
	}

	/**
	 * 
	 */
	public void setColor(int color) {
		customColor = color;
		repaint();
	}
}
