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

/**
 * Represents a line chart with several ordered points.
 */
public class LineChart extends BasicChart {

	private int firstPointIndexDisplayed;
	private int lastPointIndexDisplayed;

	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		final Font font = StyleHelper.getFont(style);

		final float animationRatio = getAnimationRatio();

		// draw scale
		renderScale(g, style, bounds);

		// Draw the line.
		final List<ChartPoint> points = getPoints();
		drawPointLines(g, style, bounds, animationRatio, points, font.getHeight());
		drawPointCircles(g, bounds, animationRatio, points, font.getHeight());

		// draw selected point value
		final Integer selectedPointIndex = getSelectedPoint();
		if(selectIsDisplay(this.firstPointIndexDisplayed, this.lastPointIndexDisplayed, selectedPointIndex)){
			renderSelectedPointValue(g, style, bounds, selectedPointIndex.intValue());
		}
	}

	/**
	 * Draw all the lines between points.
	 * Updates firstPointIndexDisplayed and lastPointIndexDisplayed.
	 * @param g the Graphic Context.
	 * @param style the style0
	 * @param bounds the bounds.
	 * @param animationRatio the current animation ratio.
	 * @param points the point to display.
	 * @param fontHeight the font height.
	 */
	private void drawPointLines(final GraphicsContext g, final Style style, final Rectangle bounds, final float animationRatio,
			final List<ChartPoint> points, final int fontHeight) {
		final int clipStart = g.getClipX();
		final int clipEnd = clipStart + g.getClipWidth();

		final int yBarBottom = getBarBottom(fontHeight, bounds);

		final float graphHeightRatio = getheightRatio(yBarBottom, getBarTop(fontHeight, bounds));
		final int foregroundColor = style.getForegroundColor();

		final AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
		antiAliasedShapes.setThickness(1);
		antiAliasedShapes.setFade(1);

		this.firstPointIndexDisplayed = -1;
		this.lastPointIndexDisplayed = -1;
		int previousX = -1;
		int previousY = -1;
		int pointIndex = 0;
		int nextX = LEFT_PADDING;
		if (animationRatio == 0) {
			// If the animation is not started, it is not necessary to draw the lines one by one.
			g.setColor(foregroundColor);
			g.removeBackgroundColor();
			antiAliasedShapes.drawLine(g, LEFT_PADDING, yBarBottom, LEFT_PADDING * points.size(), yBarBottom);
			this.firstPointIndexDisplayed = 0;
		} else {
			for (final ChartPoint chartPoint : points) {
				final int currentX = nextX;

				// Compute the position of the next point.
				pointIndex++;
				nextX = LEFT_PADDING + pointIndex * STEP_X;
				// Draw only if next point is visible.
				if (nextX > clipStart) {
					if (this.firstPointIndexDisplayed == -1) {
						this.firstPointIndexDisplayed = pointIndex - 1;
					}
					final float value = chartPoint.getValue();

					if (value < 0.0f) {
						previousX = -1;
						previousY = -1;
					} else {
						final int currentY = computeY(animationRatio, yBarBottom, graphHeightRatio, value);

						if (previousY != -1) {
							drawLine(g, foregroundColor, antiAliasedShapes, previousX, previousY, currentX, currentY);
						}

						previousX = currentX;
						previousY = currentY;
					}

					// Stops drawing point after clip
					this.lastPointIndexDisplayed = pointIndex;
					if (currentX > clipEnd) {
						break;
					}
				}
			}
		}
	}

	/**
	 * Computes the Y position of a value.
	 * @param animationRatio the current animation ratio.
	 * @param yBarBottom The Y bar bottom position
	 * @param graphHeightRatio the Height ratio.
	 * @param value the value.
	 * @return the Y position of the value.
	 */
	private int computeY(final float animationRatio, final int yBarBottom, final float graphHeightRatio,
			final float value) {
		final int finalLength = (int) (graphHeightRatio * value);
		final int apparitionLength = (int) (finalLength * animationRatio);
		final int yTop = yBarBottom - apparitionLength;
		final int currentY = yTop;
		return currentY;
	}

	private void drawLine(final GraphicsContext g, final int foregroundColor,
			final AntiAliasedShapes antiAliasedShapes, int previousX, int previousY, final int currentX,
			final int currentY) {
		g.setColor(foregroundColor);
		g.removeBackgroundColor();
		if (!isAnimated()) {
			antiAliasedShapes.drawLine(g, previousX, previousY, currentX, currentY);
		} else {
			g.drawLine(previousX, previousY, currentX, currentY);
		}
	}

	private float getheightRatio(final int yBarBottom, final int yBarTop) {
		return (yBarBottom - yBarTop) / getMaxScaleValue();
	}

	/**
	 * Draw the points from firstPointIndexDisplayed.
	 * @param g the graphic context.
	 * @param bounds the char bounds.
	 * @param animationRatio the animation ratio.
	 * @param points the points to draw.
	 * @param fontHeight the font height.
	 */
	private void drawPointCircles(final GraphicsContext g, final Rectangle bounds, final float animationRatio,
			final List<ChartPoint> points, final int fontHeight) {
		final AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
		final int clipStart = g.getClipX();
		final int clipEnd = clipStart + g.getClipWidth();
		final int yBarBottom = getBarBottom(fontHeight, bounds);
		final float graphHeightRatio = getheightRatio(yBarBottom, getBarTop(fontHeight, bounds));

		// draw circles
		int pointIndex = this.firstPointIndexDisplayed;
		if (this.firstPointIndexDisplayed != -1) {
			final int radius = 4;
			int nextX = LEFT_PADDING + pointIndex * STEP_X;
			for (final Iterator<ChartPoint> iterator = points.listIterator(pointIndex); iterator.hasNext();) {
				final ChartPoint chartPoint = iterator.next();
				final int currentX = nextX;

				// Compute the position of the next point.
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
					final int currentY = computeY(animationRatio, yBarBottom, graphHeightRatio, value);
					drawCircle(g, animationRatio, antiAliasedShapes, currentX, currentY, radius, pointStyle);

					// Stops drawing point after clip
					if (currentX > clipEnd) {
						break;
					}
				}
			}
		}
	}

	private void drawCircle(final GraphicsContext g, final float animationRatio,
			final AntiAliasedShapes antiAliasedShapes, final int currentX, final int currentY, final int radius, Style pointStyle) {
		final int backgroundColor = pointStyle.getBackgroundColor();
		final int chartPointForegroundColor = pointStyle.getForegroundColor();
		final int x = currentX - radius;
		final int y = currentY - radius;
		final int diameter = radius << 1;
		g.setColor(backgroundColor);
		g.fillCircle(x, y + 1, diameter);
		g.setColor(chartPointForegroundColor);
		if (!isAnimated() || animationRatio == 0) {
			antiAliasedShapes.drawCircle(g, x, y, diameter);
		} else {
			g.drawCircle(x, y, diameter);
		}
	}

	@Override
	public void hideNotify() {
		resetAnimation();
		super.hideNotify();
	}
}
