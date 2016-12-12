/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.style.background.Background;
import ej.style.container.AlignmentHelper;
import ej.style.container.Rectangle;

/**
 *
 */
public class CircularPlainBackground implements Background {

	private static int THICKNESS = 2;
	private static int FADE = 1;

	@Override
	public boolean isTransparent() {
		return true;
	}

	@Override
	public void apply(GraphicsContext g, Rectangle bounds, int color) {
		int width = bounds.getWidth();
		int height = bounds.getHeight();
		int diameter = Math.min(width, height) - (THICKNESS << 1);
		int x = AlignmentHelper.computeXLeftCorner(diameter, bounds.getX(), width,
				GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		int y = AlignmentHelper.computeYTopCorner(diameter, bounds.getY(), height,
				GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		g.setColor(color);
		g.fillCircle(x, y, diameter);

		AntiAliasedShapes.Singleton.setFade(FADE);
		AntiAliasedShapes.Singleton.setThickness(THICKNESS);
		AntiAliasedShapes.Singleton.drawCircle(g, x, y, diameter);
	}

}
