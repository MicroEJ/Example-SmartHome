/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.chart;

import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.StyleHelper;

/** IPR start **/

/**
 * Represents a line chart with several ordered points.
 */
public class LineChart extends BasicChart {

	/**
	 * Render widget
	 */
	@Override
	public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
		Font font = StyleHelper.getFont(style);
		int fontHeight = font.getHeight();

		int yBarBottom = getBarBottom(fontHeight, bounds);
		int yBarTop = getBarTop(fontHeight, bounds);

		float topValue = getMaxScaleValue();

		// draw scale
		g.setFont(font);
		renderScale(g, style, bounds, topValue);

		// draw points
		AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
		antiAliasedShapes.setThickness(1);
		antiAliasedShapes.setFade(1);

		int previousX = -1;
		int previousY = -1;
		int pointIndex = 0;
		for (ChartPoint chartPoint : getPoints()) {
			int currentX = LEFT_PADDING + pointIndex * STEP_X;
			float value = chartPoint.getValue();

			if (value < 0.0f) {
				previousX = -1;
				previousY = -1;
			} else {
				int finalLength = (int) ((yBarBottom - yBarTop) * value / topValue);
				int apparitionLength = (int) (finalLength * getAnimationRatio());
				int yTop = yBarBottom - apparitionLength;
				int currentY = yTop;

				if (previousY != -1) {
					g.setColor(style.getForegroundColor());
					g.removeBackgroundColor();
					antiAliasedShapes.drawLine(g, previousX, previousY, currentX, currentY);
				}

				previousX = currentX;
				previousY = currentY;
			}

			pointIndex++;
		}

		// draw circles
		int radius = 4;
		pointIndex = 0;
		for (ChartPoint chartPoint : getPoints()) {
			int currentX = LEFT_PADDING + pointIndex * STEP_X;
			float value = chartPoint.getValue();
			if (value < 0.0f) {
				pointIndex++;
				continue;
			}

			int backgroundColor = chartPoint.getStyle().getBackgroundColor();
			int foregroundColor = chartPoint.getStyle().getForegroundColor();

			int finalLength = (int) ((yBarBottom - yBarTop) * value / topValue);
			int apparitionLength = (int) (finalLength * getAnimationRatio());
			int yTop = yBarBottom - apparitionLength;
			int currentY = yTop;

			g.setColor(backgroundColor);
			g.fillCircle(currentX-radius, currentY-radius+1, 2*radius);
			g.setColor(foregroundColor);
			antiAliasedShapes.drawCircle(g, currentX-radius, currentY-radius, 2*radius);

			pointIndex++;
		}

		// draw selected point value
		renderSelectedPointValue(g, style, bounds);
	}
}

/** IPR end **/
