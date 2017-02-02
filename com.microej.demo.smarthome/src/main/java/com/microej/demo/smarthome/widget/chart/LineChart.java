/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
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
			public void onTransitionStart(final TransitionManager transitionManager) {
				isInTransition = true;

			}

			@Override
			public void onTransitionStop(final TransitionManager manager) {
				isInTransition = false;

			}
		};
	}

	/**
	 * Render widget
	 */
	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		final int clipStart = g.getClipX();
		final int clipEnd = clipStart + g.getClipWidth();
		final Font font = StyleHelper.getFont(style);
		final int fontHeight = font.getHeight();

		final int yBarBottom = getBarBottom(fontHeight, bounds);
		final int yBarTop = getBarTop(fontHeight, bounds);

		final float topValue = getMaxScaleValue();
		final float graphHeightRatio = (yBarBottom - yBarTop) / topValue;

		final float animationRatio = getAnimationRatio();

		final int foregroundColor = style.getForegroundColor();

		// draw scale
		g.setFont(font);
		renderScale(g, style, bounds, topValue);

		// draw points
		final AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
		antiAliasedShapes.setThickness(1);
		antiAliasedShapes.setFade(1);

		int firstPointIndexDisplayed = -1;
		int lastPointIndexDisplayed = -1;
		int previousX = -1;
		int previousY = -1;
		int pointIndex = 0;
		int nextX = LEFT_PADDING;

		// Draw the line.
		final List<ChartPoint> points = getPoints();
		if (animationRatio == 0) {
			firstPointIndexDisplayed = 0;
			g.setColor(foregroundColor);
			g.removeBackgroundColor();
			antiAliasedShapes.drawLine(g, LEFT_PADDING, yBarBottom, LEFT_PADDING * points.size(), yBarBottom);
		} else {
			for (final ChartPoint chartPoint : points) {
				final int currentX = nextX;

				// Comput the position of the next point.
				pointIndex++;
				nextX = LEFT_PADDING + pointIndex * STEP_X;
				// Draw only if next point is visible.
				if (nextX > clipStart) {
					if (firstPointIndexDisplayed == -1) {
						firstPointIndexDisplayed = pointIndex - 1;
					}
					final float value = chartPoint.getValue();

					if (value < 0.0f) {
						previousX = -1;
						previousY = -1;
					} else {
						final int finalLength = (int) (graphHeightRatio * value);
						final int apparitionLength = (int) (finalLength * animationRatio);
						final int yTop = yBarBottom - apparitionLength;
						final int currentY = yTop;

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
			final int radius = 4;
			nextX = LEFT_PADDING + pointIndex * STEP_X;
			for (final Iterator<ChartPoint> iterator = points.listIterator(pointIndex); iterator.hasNext();) {
				final ChartPoint chartPoint = iterator.next();
				final int currentX = nextX;

				// Comput the position of the next point.
				pointIndex++;
				nextX = LEFT_PADDING + pointIndex * STEP_X;
				// Draw only if next point is visible.
				if (nextX > clipStart) {
					// Draw only if next point is visible.
					final float value = chartPoint.getValue();
					if (value < 0.0f) {
						pointIndex++;
						continue;
					}

					final Style pointStyle = chartPoint.getStyle();
					final int backgroundColor = pointStyle.getBackgroundColor();
					final int chartPointForegroundColor = pointStyle.getForegroundColor();

					final int finalLength = (int) (graphHeightRatio * value);
					final int apparitionLength = (int) (finalLength * animationRatio);
					final int yTop = yBarBottom - apparitionLength;
					final int currentY = yTop;

					g.setColor(backgroundColor);
					final int x = currentX - radius;
					final int y = currentY - radius;
					final int diameter = radius << 1;
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
		TransitionManager.addTransitionListener(listener);
		super.showNotify();

	}

	@Override
	public void hideNotify() {
		TransitionManager.removeTransitionListener(listener);
		resetAnimation();
		super.hideNotify();
	}

	@Override
	protected boolean isAnimated() {
		return isInTransition;
	}
}

/** IPR end **/
