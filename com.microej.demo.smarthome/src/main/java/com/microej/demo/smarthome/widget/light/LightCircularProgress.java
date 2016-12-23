/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.light;

import com.microej.demo.smarthome.widget.CircularProgressWidget;

import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.widget.model.BoundedRangeModel;

/**
 *
 */
public class LightCircularProgress extends CircularProgressWidget {



	private static final int ANIMATION_DURATION = 300;

	/**
	 * Constructor
	 */
	public LightCircularProgress(final BoundedRangeModel model) {
		super(model, ANIMATION_DURATION);
	}

	/**
	 * Renders the widget
	 */
	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		// render parent
		super.renderContent(g, style, bounds);

		// set style
		if (isEnabled() && customColor != null) {
			// fill circle
			final int innerD = diameter / 2;
			final int innerX = x + (diameter - innerD) / 2;
			final int innerY = y + (diameter - innerD) / 2;
			g.setColor(customColor);
			g.removeBackgroundColor();
			g.fillCircle(innerX, innerY, innerD);

			// draw anti aliased circle
			final AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
			antiAliasedShapes.setThickness(2);
			antiAliasedShapes.setFade(1);
			antiAliasedShapes.drawCircle(g, innerX, innerY, innerD);

			// draw border
			g.setColor(style.getBackgroundColor());
			antiAliasedShapes.setThickness(1);
			antiAliasedShapes.drawCircle(g, innerX-1, innerY-1, innerD+2);
		}
	}

	/**
	 *
	 */
	public void setColor(final int color) {
		customColor = color;
		repaint();
	}
}
