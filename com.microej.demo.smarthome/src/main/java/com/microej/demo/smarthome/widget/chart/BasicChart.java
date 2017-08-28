/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.chart;

import com.microej.demo.smarthome.style.ClassSelectors;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.motion.Motion;
import ej.motion.linear.LinearMotion;
import ej.motion.none.NoMotion;
import ej.mwt.MWT;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.ElementAdapter;
import ej.style.util.StyleHelper;

/**
 * Represents a chart with basic functionality.
 */
public abstract class BasicChart extends Chart implements Animation {

	/**
	 * The left padding of the chart.
	 */
	protected static final int LEFT_PADDING = 55;

	/**
	 * The distance in between points.
	 */
	protected static final int STEP_X = 40;

	private static final int APPARITION_DURATION = 400;
	private static final int APPARITION_STEPS = 100;

	private static final int BUBBLE_RADIUS = 50;
	private static final int ARROW_RADIUS = 14;

	private static final int BUBBLE_ANIM_DURATION = 200;
	private static final int BUBBLE_ANIM_NUM_STEPS = 100;

	/**
	 * Elements
	 */
	private final ElementAdapter scaleElement;
	private final ElementAdapter selectedInfoElement;
	private final ElementAdapter selectedValueElement;

	/**
	 * Animation
	 */
	private Motion motion;
	private int currentApparitionStep;
	private int bubbleAnimationStep;

	/**
	 * Instantiates a BasicChart.
	 */
	public BasicChart() {
		super();
		this.scaleElement = new ElementAdapter();
		this.scaleElement.addClassSelector(ClassSelectors.CHART_SCALE);
		this.selectedInfoElement = new ElementAdapter();
		this.selectedInfoElement.addClassSelector(ClassSelectors.CHART_SELECTED_INFO);
		this.selectedValueElement = new ElementAdapter();
		this.selectedValueElement.addClassSelector(ClassSelectors.CHART_SELECTED_VALUE);
		motion = new NoMotion(0, 0);
	}


	@Override
	public void showNotify() {
		super.showNotify();
		if (isEnabled()) {
			resetAnimation();
		} else {
			this.currentApparitionStep = APPARITION_STEPS;
		}
	}

	/**
	 * Reset the animation.
	 */
	public void resetAnimation() {
		this.currentApparitionStep = 0;
	}

	/**
	 * Starts the animation.
	 */
	public void startAnimation() {
		this.motion = new LinearMotion(0, APPARITION_STEPS, APPARITION_DURATION);
		resetAnimation();
		final Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
		animator.startAnimation(this);
	}

	@Override
	public void hideNotify() {
		stopAnimation();
		super.hideNotify();
	}

	/**
	 * Stops the animation.
	 */
	public void stopAnimation() {
		final Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
		animator.stopAnimation(this);
	}

	@Override
	public boolean tick(final long currentTimeMillis) {
		this.currentApparitionStep = this.motion.getCurrentValue();
		repaint();
		return !this.motion.isFinished();
	}

	/**
	 * Get the animation ration.
	 *
	 * @return the animation ratio.
	 */
	protected float getAnimationRatio() {
		final int threshold = APPARITION_STEPS / 3;
		if (this.currentApparitionStep < threshold) {
			return 0.0f;
		} else {
			return (float) (this.currentApparitionStep - threshold) / (APPARITION_STEPS - threshold);
		}
	}

	@Override
	public boolean handleEvent(final int event) {
		if (Event.getType(event) == Event.POINTER) {
			final Rectangle margin = new Rectangle();
			getStyle().getMargin().unwrap(margin);

			final Pointer pointer = (Pointer) Event.getGenerator(event);
			final int pointerX = pointer.getX() - getAbsoluteX() - margin.getX();
			final int pointerY = pointer.getY() - getAbsoluteY() - margin.getY();

			final int action = Pointer.getAction(event);
			switch (action) {
			case Pointer.RELEASED:
				onPointerClicked(pointerX, pointerY);
				break;
			}
		}
		return super.handleEvent(event);
	}

	/**
	 * Handles pointer clicked event
	 */
	private void onPointerClicked(final int pointerX, final int pointerY) {
		final int xStart = LEFT_PADDING;
		final int xEnd = xStart + getPoints().size() * STEP_X;
		if (pointerX >= xStart && pointerX < xEnd) {
			final int selectedPoint = getPoints().size() * (pointerX - xStart) / (xEnd - xStart);
			selectPoint(new Integer(selectedPoint));
		} else {
			selectPoint(null);
		}
	}

	@Override
	public void selectPoint(final Integer pointIndex) {
		playBubbleAnimation(true);
		super.selectPoint(pointIndex);
	}

	/**
	 * Play bubble animation
	 */
	private void playBubbleAnimation(final boolean expand) {
		final Motion bubbleMotion;
		if (expand) {
			bubbleMotion = new LinearMotion(0, BUBBLE_ANIM_NUM_STEPS, BUBBLE_ANIM_DURATION);
		} else {
			bubbleMotion = new LinearMotion(BUBBLE_ANIM_NUM_STEPS, 0, BUBBLE_ANIM_DURATION);
		}
		bubbleAnimationStep = bubbleMotion.getStartValue();

		final Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
		animator.startAnimation(new Animation() {
			@Override
			public boolean tick(final long currentTimeMillis) {
				final int step = bubbleMotion.getCurrentValue();
				BasicChart.this.bubbleAnimationStep = step;
				final boolean finished = bubbleMotion.isFinished();
				if (!expand && finished) {
					selectPoint(null);
				}
				repaint();
				return !finished;
			}
		});
	}

	/**
	 * Render the scale.
	 */
	protected void renderScale(final GraphicsContext g, final Style style, final Rectangle bounds, final float topValue) {
		final Font font = StyleHelper.getFont(style);
		final int fontHeight = font.getHeight();

		final int numScaleValues = getNumScaleValues();
		final int yBarBottom = getBarBottom(fontHeight, bounds);
		final int yBarTop = getBarTop(fontHeight, bounds);
		final int xScale = LEFT_PADDING - fontHeight / 2;

		final Style scaleStyle = this.scaleElement.getStyle();
		final Font scaleFont = StyleHelper.getFont(scaleStyle);
		final int foregroundColor = scaleStyle.getForegroundColor();
		final int backgroundColor = scaleStyle.getBackgroundColor();

		g.setFont(scaleFont);

		// draw values and lines
		for (int i = 1; i < numScaleValues + 1; i++) {
			final float scaleValue = topValue * i / numScaleValues;
			final String scaleString = Integer.toString((int) scaleValue) + getUnit();
			final int yScale = yBarBottom + (yBarTop - yBarBottom) * i / numScaleValues;

			g.setColor(foregroundColor);
			g.drawString(scaleString, xScale, yScale, GraphicsContext.RIGHT | GraphicsContext.VCENTER);

			g.setColor(backgroundColor);
			final int strokeStyle = (i == numScaleValues ? GraphicsContext.SOLID : GraphicsContext.DOTTED);
			g.setStrokeStyle(strokeStyle);
			g.drawLine(LEFT_PADDING, yScale, bounds.getWidth(), yScale);
		}

		// render bottom values
		int pointIndex = 0;
		for (final ChartPoint chartPoint : getPoints()) {
			final int currentX = LEFT_PADDING + pointIndex * STEP_X;

			final String name = chartPoint.getName();
			if (name != null) {
				g.setColor(foregroundColor);
				g.drawString(name, currentX, bounds.getHeight(), GraphicsContext.HCENTER | GraphicsContext.BOTTOM);
			}

			pointIndex++;
		}
	}

	/**
	 * Render selected point value
	 */
	protected void renderSelectedPointValue(final GraphicsContext g, final Style style, final Rectangle bounds, final int firstDisplay,
			final int lastDisplay) {
		final Integer selectedPointIndex = getSelectedPoint();
		// TODO remove magic number (3=the further you can see a part of the bubble).
		if (selectedPointIndex != null && selectedPointIndex > firstDisplay - 3
				&& selectedPointIndex < lastDisplay + 3) {
			final ChartPoint selectedPoint = getPoints().get(selectedPointIndex);
			final float selectedPointValue = selectedPoint.getValue();
			final int fontHeight = StyleHelper.getFont(style).getHeight();

			// calculate value position
			final int valueX = LEFT_PADDING + selectedPointIndex * STEP_X;
			final int valueY = getValueY(fontHeight, bounds, selectedPointValue);

			// calculate radius
			final float animationRatio = (float) this.bubbleAnimationStep / BUBBLE_ANIM_NUM_STEPS;
			final int bubbleRadius = (int) (BUBBLE_RADIUS * animationRatio);
			final int arrowRadius = (int) (ARROW_RADIUS * animationRatio);
			final int fullRadius = bubbleRadius + arrowRadius;

			// calculate bubble position
			final int diffY = bounds.getHeight()/2 - valueY;
			final int centerY = valueY + (int) (diffY * animationRatio);
			final double angle = Math.asin((double) (centerY - valueY) / fullRadius);
			final int moveX = (int) (Math.cos(angle) * fullRadius);
			int centerX = valueX;
			if (centerX-moveX < fullRadius) {
				centerX += moveX;
			} else if(centerX+moveX >= getWidth()-fullRadius) {
				centerX -= moveX;
			} else {
				if (valueX + getX() < getPanel().getWidth() / 2) {
					centerX += moveX;
				} else {
					centerX -= moveX;
				}
			}

			// calculate arrow position
			final int arrow1X = centerX;
			final int radius = bubbleRadius * 3 / 4;
			final int arrow1Y = centerY - radius;
			final int arrow2X = centerX;
			final int arrow2Y = centerY + radius;

			// fill arrow
			g.fillPolygon(new int[] { valueX, valueY, arrow1X, arrow1Y, arrow2X, arrow2Y });

			// draw anti aliased arrow
			final AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
			antiAliasedShapes.setThickness(1);
			antiAliasedShapes.setFade(1);

			if (!isAnimated()) {
				antiAliasedShapes.drawLine(g, valueX, valueY, arrow1X, arrow1Y);
				antiAliasedShapes.drawLine(g, valueX, valueY, arrow2X, arrow2Y);
			} else {
				g.drawLine(valueX, valueY, arrow1X, arrow1Y);
				g.drawLine(valueX, valueY, arrow2X, arrow2Y);
			}

			// fill bubble
			final int cX = centerX - bubbleRadius;
			final int cY = centerY - bubbleRadius;
			final int cD = 2 * bubbleRadius;
			g.fillCircle(cX, cY, cD);

			// draw anti aliased bubble
			antiAliasedShapes.setThickness(2);
			if (!isAnimated()) {
				antiAliasedShapes.drawCircle(g, cX, cY, cD);
			} else {
				g.drawCircle(cX, cY, cD);
			}

			if (this.bubbleAnimationStep >= BUBBLE_ANIM_NUM_STEPS*3/4) {
				// set info style
				final Style infoStyle = this.selectedInfoElement.getStyle();
				final Font infoFont = StyleHelper.getFont(infoStyle);
				g.setFont(infoFont);
				g.setColor(infoStyle.getForegroundColor());

				// draw info string
				final String infoString = selectedPoint.getFullName();
				g.drawString(infoString, centerX, centerY-bubbleRadius/2, GraphicsContext.HCENTER | GraphicsContext.VCENTER);

				// set value style
				final Style valueStyle = this.selectedValueElement.getStyle();
				final Font valueFont = StyleHelper.getFont(valueStyle);
				g.setFont(valueFont);
				g.setColor(valueStyle.getForegroundColor());

				// draw value string
				final float value = selectedPoint.getValue();
				String valueString = Integer.toString((int) value);
				if (getUnit() != null) {
					valueString += getUnit();
				}
				g.drawString(valueString, centerX, centerY + bubbleRadius / 6,
						GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			}
		}
	}

	@Override
	public Rectangle validateContent(final Style style, final Rectangle bounds) {
		int height = bounds.getHeight();
		int width = bounds.getWidth();
		final int fontHeight = StyleHelper.getFont(style).getHeight();
		if (height == MWT.NONE) {
			height = 4 * fontHeight;
		}
		if (width == MWT.NONE) {
			width = LEFT_PADDING + getPoints().size() * STEP_X;
		}
		return new Rectangle(0, 0, width, height);
	}

	@Override
	protected void setBoundsContent(final Rectangle bounds) {
		// do nothing
	}

	/**
	 * Gets the top position of the chart content
	 */
	protected int getBarTop(final int fontHeight, final Rectangle bounds) {
		return 5;
	}

	/**
	 * Gets the bottom position of the chart content
	 */
	protected int getBarBottom(final int fontHeight, final Rectangle bounds) {
		return bounds.getHeight() - fontHeight;
	}

	/**
	 * Gets the position of a chart value
	 */
	protected int getValueY(final int fontHeight, final Rectangle bounds, final float value) {
		final int yBarBottom = getBarBottom(fontHeight, bounds);
		final int yBarTop = getBarTop(fontHeight, bounds);
		final float topValue = getMaxScaleValue();

		final float finalLength = (yBarBottom - yBarTop) * (value / topValue);
		final int apparitionLength = (int) (finalLength * getAnimationRatio());
		return yBarBottom - apparitionLength;
	}

	/**
	 * Gets whether the chart is animated.
	 *
	 * @return whether the chart is animated.
	 */
	protected boolean isAnimated() {
		return false;
	}
}
