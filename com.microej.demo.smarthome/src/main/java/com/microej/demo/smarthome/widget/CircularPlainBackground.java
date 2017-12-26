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
import ej.style.background.Background;
import ej.style.container.AlignmentHelper;
import ej.style.container.Rectangle;

/**
 * A circular background.
 */
public class CircularPlainBackground implements Background {

	private static final int THICKNESS = 2;
	private static final int FADE = 1;

	@Override
	public boolean isTransparent() {
		return true;
	}

	@Override
	public void apply(final GraphicsContext g, final Rectangle bounds, final int color) {
		final int width = bounds.getWidth();
		final int height = bounds.getHeight();
		final int diameter = Math.min(width, height) - (THICKNESS << 1);
		final int x = AlignmentHelper.computeXLeftCorner(diameter, bounds.getX(), width,
				GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		final int y = AlignmentHelper.computeYTopCorner(diameter, bounds.getY(), height,
				GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		g.setColor(color);
		g.fillCircle(x, y, diameter);

		AntiAliasedShapes.Singleton.setFade(FADE);
		AntiAliasedShapes.Singleton.setThickness(THICKNESS);
		AntiAliasedShapes.Singleton.drawCircle(g, x, y, diameter);
	}

}
