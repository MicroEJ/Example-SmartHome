/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.chart;

import com.microej.demo.smarthome.Main;
import com.microej.demo.smarthome.style.ClassSelectors;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.generator.Buttons;
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
	/**
	 * Top position of the chart content.
	 */
	private static final int BAR_TOP = 5;

	private static final int MAX_BUBLLE_OFFSET = 3;

	private static final int APPARITION_DURATION = 400;
	private static final int APPARITION_STEPS = 100;

	private static final int BUBBLE_RADIUS = 50;
	private static final int ARROW_RADIUS = 14;

	private static final int BUBBLE_ANIM_DURATION = 200;
	private static final int BUBBLE_ANIM_NUM_STEPS = 100;

	private static final int TEXT_DRAWING_MINIMUM_STEP = BUBBLE_ANIM_NUM_STEPS * 3 / 4;

	private final ElementAdapter scaleElement;
	private final ElementAdapter selectedInfoElement;
	private final ElementAdapter selectedValueElement;

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
		this.motion = new NoMotion(0, 0);
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
		if(Main.USE_ANIMATION){
			this.currentApparitionStep = 0;
		}
	}

	/**
	 * Starts the animation.
	 */
	public void startAnimation() {
		resetAnimation();
		this.motion = new LinearMotion(this.currentApparitionStep, APPARITION_STEPS, APPARITION_DURATION);
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
		}
		return (float) (this.currentApparitionStep - threshold) / (APPARITION_STEPS - threshold);
	}

	@Override
	public boolean handleEvent(final int event) {
		if (Event.getType(event) == Event.POINTER) {
			final Rectangle margin = new Rectangle();
			getStyle().getMargin().unwrap(margin);

			final Pointer pointer = (Pointer) Event.getGenerator(event);
			final int pointerX = pointer.getX() - getAbsoluteX() - margin.getX();

			final int action = Buttons.getAction(event);
			switch (action) {
			case Buttons.RELEASED:
				onPointerClicked(pointerX);
				break;
			default:
				break;
			}
		}
		return super.handleEvent(event);
	}

	/**
	 * Handles pointer clicked event.
	 */
	private void onPointerClicked(final int pointerX) {
		final int xStart = LEFT_PADDING;
		final int xEnd = xStart + getPoints().size() * STEP_X;
		if (pointerX >= xStart && pointerX < xEnd) {
			final int selectedPoint = getPoints().size() * (pointerX - xStart) / (xEnd - xStart);
			selectPoint(Integer.valueOf(selectedPoint));
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
	 * Play bubble animation.
	 */
	private void playBubbleAnimation(final boolean expand) {
		final Motion bubbleMotion;
		if (expand) {
			bubbleMotion = new LinearMotion(0, BUBBLE_ANIM_NUM_STEPS, BUBBLE_ANIM_DURATION);
		} else {
			bubbleMotion = new LinearMotion(BUBBLE_ANIM_NUM_STEPS, 0, BUBBLE_ANIM_DURATION);
		}
		this.bubbleAnimationStep = bubbleMotion.getStartValue();

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
	 * @param g the graphic context.
	 * @param style the style.
	 * @param bounds the bounds.
	 */
	protected void renderScale(final GraphicsContext g, final Style style, final Rectangle bounds) {
		final float topValue = getMaxScaleValue();
		final Font font = StyleHelper.getFont(style);
		final int fontHeight = font.getHeight();

		final int numScaleValues = getNumScaleValues();
		final int yBarBottom = getBarBottom(fontHeight, bounds);
		final int yBarTop = getBarTop(fontHeight, bounds);
		final int xScale = LEFT_PADDING - (fontHeight >> 1);

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
	 * Checks wether a point is display.
	 * @param firstDisplay the first point display.
	 * @param lastDisplay the last point display.
	 * @param selectedPointIndex the point to check.
	 * @return True if the point is displayed.
	 */
	protected boolean selectIsDisplay(final int firstDisplay, final int lastDisplay, final Integer selectedPointIndex){
		// TODO remove magic number (3=the further you can see a part of the bubble).
		return (selectedPointIndex != null && selectedPointIndex.intValue() > firstDisplay - MAX_BUBLLE_OFFSET
				&& selectedPointIndex.intValue() < lastDisplay + MAX_BUBLLE_OFFSET);
	}
	/**
	 * Render selected point value.
	 * @param g the graphic context.
	 * @param style the style.
	 * @param bounds the bounds.
	 * @param selectedPointIndex the point to display.
	 */
	protected void renderSelectedPointValue(final GraphicsContext g, final Style style, final Rectangle bounds, final int selectedPointIndex) {
		final ChartPoint selectedPoint = getPoints().get(selectedPointIndex);
		final float selectedPointValue = selectedPoint.getValue();

		// calculate value position
		final int valueX = LEFT_PADDING + selectedPointIndex * STEP_X;
		final int valueY = getValueY(style, bounds, selectedPointValue);

		// calculate radius
		final float animationRatio = (float) this.bubbleAnimationStep / BUBBLE_ANIM_NUM_STEPS;
		final int bubbleRadius = (int) (BUBBLE_RADIUS * animationRatio);
		final int arrowRadius = (int) (ARROW_RADIUS * animationRatio);
		final int fullRadius = bubbleRadius + arrowRadius;

		// calculate bubble position
		final int centerY = computeCenterY(bounds, valueY, animationRatio);
		final int centerX = computeCenterX(valueX, fullRadius, valueY, centerY);

		drawArow(g, valueX, valueY, bubbleRadius, centerY, centerX);
		drawBubble(g, bubbleRadius, centerY, centerX);

		if (this.bubbleAnimationStep >= TEXT_DRAWING_MINIMUM_STEP) {
			drawBubbleText(g, selectedPoint, bubbleRadius, centerY, centerX);
		}
	}

	private void drawBubbleText(final GraphicsContext g, final ChartPoint selectedPoint, final int bubbleRadius,
			final int centerY, int centerX) {
		// set info style
		final Style infoStyle = this.selectedInfoElement.getStyle();
		final Font infoFont = StyleHelper.getFont(infoStyle);
		g.setFont(infoFont);
		g.setColor(infoStyle.getForegroundColor());

		// draw info string
		final String infoString = selectedPoint.getFullName();
		g.drawString(infoString, centerX, centerY - (bubbleRadius >> 1),
				GraphicsContext.HCENTER | GraphicsContext.VCENTER);

		// set value style
		final Style valueStyle = this.selectedValueElement.getStyle();
		final Font valueFont = StyleHelper.getFont(valueStyle);
		g.setFont(valueFont);
		g.setColor(valueStyle.getForegroundColor());

		// draw value string
		final float value = selectedPoint.getValue();
		StringBuffer valueString = new StringBuffer();
		valueString.append((int) value);
		String unit = getUnit();
		if (unit != null) {
			valueString.append(unit);
		}
		g.drawString(valueString.toString(), centerX, centerY + bubbleRadius / 6,
				GraphicsContext.HCENTER | GraphicsContext.VCENTER);
	}

	private int computeCenterY(final Rectangle bounds, final int valueY, final float animationRatio) {
		final int diffY = (bounds.getHeight() >> 1) - valueY;
		final int centerY = valueY + (int) (diffY * animationRatio);
		return centerY;
	}

	private int computeCenterX(final int valueX, final int fullRadius, final int valueY, final int centerY) {
		final double angle = Math.asin((double) (centerY - valueY) / fullRadius);
		final int moveX = (int) (Math.cos(angle) * fullRadius);
		int centerX = valueX;
		if (centerX - moveX < fullRadius) {
			centerX += moveX;
		} else if (centerX + moveX >= getWidth() - fullRadius) {
			centerX -= moveX;
		} else {
			if (valueX + getX() < (getPanel().getWidth() >> 1)) {
				centerX += moveX;
			} else {
				centerX -= moveX;
			}
		}
		return centerX;
	}

	private void drawBubble(final GraphicsContext g, final int bubbleRadius, final int centerY, int centerX) {
		// fill bubble
		final int cX = centerX - bubbleRadius;
		final int cY = centerY - bubbleRadius;
		final int diameter = 2 * bubbleRadius;
		g.fillCircle(cX, cY, diameter);

		// draw anti aliased bubble
		final AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
		antiAliasedShapes.setThickness(2);
		if (!isAnimated()) {
			antiAliasedShapes.drawCircle(g, cX, cY, diameter);
		} else {
			g.drawCircle(cX, cY, diameter);
		}
	}

	private void drawArow(final GraphicsContext g, final int valueX, final int valueY,
			final int bubbleRadius, final int centerY, int centerX) {
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
	}

	@Override
	public Rectangle validateContent(final Style style, final Rectangle bounds) {
		int height = bounds.getHeight();
		int width = bounds.getWidth();
		final int fontHeight = StyleHelper.getFont(style).getHeight();
		if (height == MWT.NONE) {
			height = fontHeight << 2;
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
	 * Gets the top position of the chart content.
	 * @param fontHeight the font height.
	 * @param bounds the bounds.
	 * @return the top position of the chart content.
	 */
	protected int getBarTop(final int fontHeight, final Rectangle bounds) {
		return BAR_TOP;
	}

	/**
	 * Gets the bottom position of the chart content.
	 * @param fontHeight the font height.
	 * @param bounds the bounds.
	 * @return the bottom position of the chart content.
	 */
	protected int getBarBottom(final int fontHeight, final Rectangle bounds) {
		return bounds.getHeight() - fontHeight;
	}

	/**
	 * Gets the position of a chart value.
	 * @param style the style.
	 * @param bounds the bounds of the chart.
	 * @param value the value.
	 * @return the y position of the value.
	 */
	protected int getValueY(final Style style, final Rectangle bounds, final float value) {
		final int fontHeight = StyleHelper.getFont(style).getHeight();
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
