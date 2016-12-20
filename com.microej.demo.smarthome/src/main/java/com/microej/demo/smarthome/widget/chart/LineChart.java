/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.chart;

import java.util.Iterator;
import java.util.List;

import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.StyleHelper;
import ej.widget.navigation.TransitionListener;
import ej.widget.navigation.TransitionManager;
import ej.widget.navigation.page.Page;

/** IPR start **/

/**
 * Represents a line chart with several ordered points.
 */
public class LineChart extends BasicChart {

	private final TransitionListener listener;

	private boolean isInTransition = false;

	/**
	 *
	 */
	public LineChart() {
		super();
		listener = new TransitionListener() {

			@Override
			public void onTransitionStop() {
				isInTransition = false;

			}

			@Override
			public void onTransitionStep(int step) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTransitionStart(int transitionsSteps, int transitionsStop, Page from, Page to) {
				isInTransition = true;

			}
		};
	}

	/**
	 * Render widget
	 */
	@Override
	public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
		int clipStart = g.getClipX();
		int clipEnd = clipStart + g.getClipWidth();
		Font font = StyleHelper.getFont(style);
		int fontHeight = font.getHeight();

		int yBarBottom = getBarBottom(fontHeight, bounds);
		int yBarTop = getBarTop(fontHeight, bounds);

		float topValue = getMaxScaleValue();
		float graphHeightRatio = (yBarBottom - yBarTop) / topValue;

		float animationRatio = getAnimationRatio();

		int foregroundColor = style.getForegroundColor();

		// draw scale
		g.setFont(font);
		renderScale(g, style, bounds, topValue);

		// draw points
		AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
		antiAliasedShapes.setThickness(1);
		antiAliasedShapes.setFade(1);

		int firstPointIndexDisplayed = -1;
		int lastPointIndexDisplayed = -1;
		int previousX = -1;
		int previousY = -1;
		int pointIndex = 0;
		int nextX = LEFT_PADDING;

		// Draw the line.
		List<ChartPoint> points = getPoints();
		if (animationRatio == 0) {
			firstPointIndexDisplayed = 0;
			g.setColor(foregroundColor);
			g.removeBackgroundColor();
			antiAliasedShapes.drawLine(g, LEFT_PADDING, yBarBottom, LEFT_PADDING * points.size(), yBarBottom);
		} else {
			for (ChartPoint chartPoint : points) {
				int currentX = nextX;

				// Comput the position of the next point.
				pointIndex++;
				nextX = LEFT_PADDING + pointIndex * STEP_X;
				// Draw only if next point is visible.
				if (nextX > clipStart) {
					if (firstPointIndexDisplayed == -1) {
						firstPointIndexDisplayed = pointIndex - 1;
					}
					float value = chartPoint.getValue();

					if (value < 0.0f) {
						previousX = -1;
						previousY = -1;
					} else {
						int finalLength = (int) (graphHeightRatio * value);
						int apparitionLength = (int) (finalLength * animationRatio);
						int yTop = yBarBottom - apparitionLength;
						int currentY = yTop;

						if (previousY != -1) {
							g.setColor(foregroundColor);
							g.removeBackgroundColor();
							if (!isAnimated()) {
								antiAliasedShapes.drawLine(g, previousX, previousY, currentX, currentY);
							} else {
								g.drawLine(previousX, previousY, currentX, currentY);
							}
						}

						previousX = currentX;
						previousY = currentY;
					}

					// Stops drawing point after clip
					lastPointIndexDisplayed = pointIndex;
					if (currentX > clipEnd) {
						break;
					}
				}
			}
		}



		// draw circles
		pointIndex = firstPointIndexDisplayed;
		if (firstPointIndexDisplayed != -1) {
			int radius = 4;
			nextX = LEFT_PADDING + pointIndex * STEP_X;
			for (Iterator<ChartPoint> iterator = points.listIterator(pointIndex); iterator.hasNext();) {
				ChartPoint chartPoint = iterator.next();
				int currentX = nextX;

				// Comput the position of the next point.
				pointIndex++;
				nextX = LEFT_PADDING + pointIndex * STEP_X;
				// Draw only if next point is visible.
				if (nextX > clipStart) {
					// Draw only if next point is visible.
					float value = chartPoint.getValue();
					if (value < 0.0f) {
						pointIndex++;
						continue;
					}

					Style pointStyle = chartPoint.getStyle();
					int backgroundColor = pointStyle.getBackgroundColor();
					int chartPointForegroundColor = pointStyle.getForegroundColor();

					int finalLength = (int) (graphHeightRatio * value);
					int apparitionLength = (int) (finalLength * animationRatio);
					int yTop = yBarBottom - apparitionLength;
					int currentY = yTop;

					g.setColor(backgroundColor);
					int x = currentX - radius;
					int y = currentY - radius;
					int diameter = radius << 1;
					g.fillCircle(x, y + 1, diameter);
					g.setColor(chartPointForegroundColor);

					if (!isAnimated() || animationRatio == 0) {
						antiAliasedShapes.drawCircle(g, x, y, diameter);
					} else {
						g.drawCircle(x, y, diameter);
					}

					// Stops drawing point after clip
					if (currentX > clipEnd) {
						break;
					}
				}
			}

			// draw selected point value
			renderSelectedPointValue(g, style, bounds, firstPointIndexDisplayed, lastPointIndexDisplayed);
		}
	}

	@Override
	public void showNotify() {
		TransitionManager.addGlobalTransitionListener(listener);
		super.showNotify();

	}

	@Override
	public void hideNotify() {
		TransitionManager.removeGlobalTransitionListener(listener);
		super.hideNotify();
	}

	@Override
	protected boolean isAnimated() {
		return isInTransition;
	}
}

/** IPR end **/
