/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.chart;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.motion.Motion;
import ej.motion.bounce.BounceEaseOutMotion;
import ej.motion.back.BackEaseInMotion;
import ej.motion.linear.LinearMotion;
import ej.mwt.MWT;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.ElementAdapter;
import ej.style.util.StyleHelper;

/** IPR start **/

/**
 * Represents a chart with basic functionality.
 */
public abstract class BasicChart extends Chart implements Animation {

	protected static final int LEFT_PADDING = 55;
	protected static final int STEP_X = 30;

	private static final int APPARITION_DURATION = 750;
	private static final int APPARITION_STEPS = 100;

	private static final int BUBBLE_RADIUS = 50;
	private static final int ARROW_RADIUS = 14;

	private static final int BUBBLE_ANIM_DURATION = 800;
	private static final int BUBBLE_ANIM_NUM_STEPS = 100;

	public static final String CLASS_SELECTOR_SCALE = "chart-scale";
	public static final String CLASS_SELECTOR_SELECTED_INFO = "chart-selected-info";
	public static final String CLASS_SELECTOR_SELECTED_VALUE = "chart-selected-value";

	/**
	 * Elements
	 */
	private ElementAdapter scaleElement;
	private ElementAdapter selectedInfoElement;
	private ElementAdapter selectedValueElement;

	/**
	 * Animation
	 */
	private Motion motion;
	private int currentApparitionStep;
	private int bubbleAnimationStep;

	/**
	 * Constructor
	 */
	public BasicChart() {
		super();
		this.scaleElement = new ElementAdapter();
		this.scaleElement.addClassSelector(CLASS_SELECTOR_SCALE);
		this.selectedInfoElement = new ElementAdapter();
		this.selectedInfoElement.addClassSelector(CLASS_SELECTOR_SELECTED_INFO);
		this.selectedValueElement = new ElementAdapter();
		this.selectedValueElement.addClassSelector(CLASS_SELECTOR_SELECTED_VALUE);
	}

	/**
	 * Animation
	 */
	@Override
	public void showNotify() {
		super.showNotify();
		if (isEnabled()) {
			this.motion = new LinearMotion(0, APPARITION_STEPS, APPARITION_DURATION);
			this.currentApparitionStep = 0;
			Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
			animator.startAnimation(this);
		} else {
			this.currentApparitionStep = APPARITION_STEPS;
		}
	}

	@Override
	public void hideNotify() {
		Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
		animator.stopAnimation(this);
		super.hideNotify();
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		this.currentApparitionStep = this.motion.getCurrentValue();
		repaint();
		return !this.motion.isFinished();
	}

	protected float getAnimationRatio() {
		int threshold = APPARITION_STEPS / 3;
		if (this.currentApparitionStep < threshold) {
			return 0.0f;
		} else {
			return (float) (this.currentApparitionStep - threshold) / (APPARITION_STEPS - threshold);
		}
	}

	/**
	 * Handle pointer events
	 */
	@Override
	public boolean handleEvent(int event) {
		if (Event.getType(event) == Event.POINTER) {
			Rectangle margin = new Rectangle();
			getStyle().getMargin().unwrap(margin);

			Pointer pointer = (Pointer) Event.getGenerator(event);
			int pointerX = pointer.getX() - getAbsoluteX() - margin.getX();
			int pointerY = pointer.getY() - getAbsoluteY() - margin.getY();

			int action = Pointer.getAction(event);
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
	private void onPointerClicked(int pointerX, int pointerY) {
		int xStart = LEFT_PADDING;
		int xEnd = xStart + getPoints().size() * STEP_X;
		if (pointerX >= xStart && pointerX < xEnd) {
			int selectedPoint = getPoints().size() * (pointerX - xStart) / (xEnd - xStart);
			selectPoint(new Integer(selectedPoint));
			playBubbleAnimation(true);
		} else {
			//selectPoint(null);
			playBubbleAnimation(false);
		}

		//playBubbleAnimation(BasicChart.this.getSelectedPoint() != null);
	}

	/**
	 * Play bubble animation
	 */
	private void playBubbleAnimation(boolean expand) {
		this.bubbleAnimationStep = (expand ? 0 : BUBBLE_ANIM_NUM_STEPS);
		final Motion bubbleMotion;
		if (expand) {
			bubbleMotion = new BounceEaseOutMotion(0, BUBBLE_ANIM_NUM_STEPS, BUBBLE_ANIM_DURATION);
		} else {
			bubbleMotion = new BackEaseInMotion(0, BUBBLE_ANIM_NUM_STEPS, BUBBLE_ANIM_DURATION);
		}

		Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
		animator.startAnimation(new Animation() {
			@Override
			public boolean tick(long currentTimeMillis) {
				int step = bubbleMotion.getCurrentValue();
				BasicChart.this.bubbleAnimationStep = (expand ? step : BUBBLE_ANIM_NUM_STEPS - step);
				boolean finished = bubbleMotion.isFinished();
				if (!expand && finished) {
					selectPoint(null);
				}
				repaint();
				return !finished;
			}
		});
	}

	/**
	 * Render scale
	 */
	protected void renderScale(GraphicsContext g, Style style, Rectangle bounds, float topValue) {
		Font font = StyleHelper.getFont(style);
		int fontHeight = font.getHeight();

		int numScaleValues = getNumScaleValues();
		int yBarBottom = getBarBottom(fontHeight, bounds);
		int yBarTop = getBarTop(fontHeight, bounds);
		int xScale = LEFT_PADDING - fontHeight / 2;

		Style scaleStyle = this.scaleElement.getStyle();
		Font scaleFont = StyleHelper.getFont(scaleStyle);
		int foregroundColor = scaleStyle.getForegroundColor();
		int backgroundColor = scaleStyle.getBackgroundColor();

		g.setFont(scaleFont);

		// draw values and lines
		for (int i = 1; i < numScaleValues + 1; i++) {
			float scaleValue = topValue * i / numScaleValues;
			String scaleString = Integer.toString((int) scaleValue) + getUnit();
			int yScale = yBarBottom + (yBarTop - yBarBottom) * i / numScaleValues;

			g.setColor(foregroundColor);
			g.drawString(scaleString, xScale, yScale, GraphicsContext.RIGHT | GraphicsContext.VCENTER);

			g.setColor(backgroundColor);
			int strokeStyle = (i == numScaleValues ? GraphicsContext.SOLID : GraphicsContext.DOTTED);
			g.setStrokeStyle(strokeStyle);
			g.drawLine(LEFT_PADDING, yScale, bounds.getWidth(), yScale);
		}

		// render bottom values
		int pointIndex = 0;
		for (ChartPoint chartPoint : getPoints()) {
			int currentX = LEFT_PADDING + pointIndex * STEP_X;

			String name = chartPoint.getName();
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
	protected void renderSelectedPointValue(GraphicsContext g, Style style, Rectangle bounds) {
		Integer selectedPointIndex = getSelectedPoint();
		if (selectedPointIndex != null) {
			ChartPoint selectedPoint = getPoints().get(selectedPointIndex);
			float selectedPointValue = selectedPoint.getValue();
			int fontHeight = StyleHelper.getFont(style).getHeight();

			// calculate value position
			int valueX = LEFT_PADDING + selectedPointIndex * STEP_X;
			int valueY = getValueY(fontHeight, bounds, selectedPointValue);

			// calculate radius
			float animationRatio = (float) this.bubbleAnimationStep / BUBBLE_ANIM_NUM_STEPS;
			int bubbleRadius = (int) (BUBBLE_RADIUS * animationRatio);
			int arrowRadius = (int) (ARROW_RADIUS * animationRatio);
			int fullRadius = bubbleRadius + arrowRadius;

			// calculate bubble position
			int diffY = bounds.getHeight()/2 - valueY;
			int centerY = valueY + (int) (diffY * animationRatio);
			double angle = Math.asin((double) (centerY - valueY) / fullRadius);
			int moveX = (int) (Math.cos(angle) * fullRadius);
			int centerX = valueX;
			if (valueX + getX() < getPanel().getWidth()/2) {
				centerX += moveX;
			} else {
				centerX -= moveX;
			}

			// calculate arrow position
			int arrow1X = centerX;
			int arrow1Y = centerY - bubbleRadius*3/4;
			int arrow2X = centerX;
			int arrow2Y = centerY + bubbleRadius*3/4;

			// fill arrow
			g.fillPolygon(new int[] { valueX, valueY, arrow1X, arrow1Y, arrow2X, arrow2Y });

			// draw anti aliased arrow
			AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
			antiAliasedShapes.setThickness(1);
			antiAliasedShapes.setFade(1);
			antiAliasedShapes.drawLine(g, valueX, valueY, arrow1X, arrow1Y);
			antiAliasedShapes.drawLine(g, valueX, valueY, arrow2X, arrow2Y);

			// fill bubble
			int cX = centerX - bubbleRadius;
			int cY = centerY - bubbleRadius;
			int cD = 2 * bubbleRadius;
			g.fillCircle(cX, cY, cD);

			// draw anti aliased bubble
			antiAliasedShapes.setThickness(2);
			antiAliasedShapes.drawCircle(g, cX, cY, cD);

			if (this.bubbleAnimationStep >= BUBBLE_ANIM_NUM_STEPS*3/4) {
				// set info style
				Style infoStyle = this.selectedInfoElement.getStyle();
				Font infoFont = StyleHelper.getFont(infoStyle);
				g.setFont(infoFont);
				g.setColor(infoStyle.getForegroundColor());
	
				// draw info string
				String infoString = selectedPoint.getFullName();
				g.drawString(infoString, centerX, centerY-bubbleRadius/2, GraphicsContext.HCENTER | GraphicsContext.VCENTER);

				// set value style
				Style valueStyle = this.selectedValueElement.getStyle();
				Font valueFont = StyleHelper.getFont(valueStyle);
				g.setFont(valueFont);
				g.setColor(valueStyle.getForegroundColor());
	
				// draw value string
				float value = selectedPoint.getValue();
				String valueString = Integer.toString((int) value);
				if (getUnit() != null) {
					valueString += " " + getUnit();
				}
				g.drawString(valueString, centerX, centerY+bubbleRadius/3, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			}
		}
	}

	@Override
	public Rectangle validateContent(Style style, Rectangle bounds) {
		int height = bounds.getHeight();
		int width = bounds.getWidth();
		int fontHeight = StyleHelper.getFont(style).getHeight();
		if (height == MWT.NONE) {
			height = 4 * fontHeight;
		}
		if (width == MWT.NONE) {
			width = LEFT_PADDING + getPoints().size() * STEP_X;
		}
		return new Rectangle(0, 0, width, height);
	}

	@Override
	protected void setBoundsContent(Rectangle bounds) {
		// do nothing
	}

	/**
	 * Gets the top position of the chart content
	 */
	protected int getBarTop(int fontHeight, Rectangle bounds) {
		return 5;
	}

	/**
	 * Gets the bottom position of the chart content
	 */
	protected int getBarBottom(int fontHeight, Rectangle bounds) {
		return bounds.getHeight() - fontHeight;
	}

	/**
	 * Gets the position of a chart value
	 */
	protected int getValueY(int fontHeight, Rectangle bounds, float value) {
		int yBarBottom = getBarBottom(fontHeight, bounds);
		int yBarTop = getBarTop(fontHeight, bounds);
		float topValue = getMaxScaleValue();

		float finalLength = (yBarBottom - yBarTop) * (value / topValue);
		int apparitionLength = (int) (finalLength * getAnimationRatio());
		return yBarBottom - apparitionLength;
	}
}

/** IPR end **/
