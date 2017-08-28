/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.style.Style;
import ej.style.container.AlignmentHelper;
import ej.style.container.Rectangle;
import ej.style.util.StyleHelper;
import ej.widget.basic.BoundedRange;
import ej.widget.listener.OnValueChangeListener;
import ej.widget.model.BoundedRangeModel;

/**
 * A progress bar showing its value in an arc.
 */
public class CircularProgressWidget extends BoundedRange implements Animation {

	private static final int EVENT_RATE = 50;

	private static final int DEFAULT_START_ANGLE = -120;
	private static final int DEFAULT_ARC_ANGLE = -300;
	private static final int DEFAULT_FADE_FULL = 1;
	private static final int DEFAULT_THICKNESS_FULL = 1;
	private static final int DEFAULT_FADE = 2;
	private static final int DEFAULT_THICKNESS = 3;

	private int arcAngle = DEFAULT_ARC_ANGLE;
	private int fadeFull = DEFAULT_FADE_FULL;
	private int thicknessFull = DEFAULT_THICKNESS_FULL;
	private final OnValueChangeListener listener;
	private OnAnimationEndListener onAnimationEndListener;
	private int fade = DEFAULT_FADE;
	private int thickness = DEFAULT_THICKNESS;
	private int startAngle = DEFAULT_START_ANGLE;

	// used to compute relative position.
	private int circleX;
	private int circleY;
	private int diameter;
	private int offset;
	private int currentArcAngle;
	private final ValueAnimation valueAnimation;

	private long nextEvent;

	private boolean animated;


	/**
	 * Instantiates a CircularProgressWidget
	 *
	 * @param model
	 *            the model.
	 */
	public CircularProgressWidget(final BoundedRangeModel model) {
		this(model, ValueAnimation.DEFAULT_DURATION);
	}

	/**
	 *
	 * Instantiates a CircularProgressWidget
	 *
	 * @param model
	 *            the model.
	 * @param duration
	 *            the duration of the animation.
	 */
	public CircularProgressWidget(final BoundedRangeModel model, final int duration) {
		super(model);
		currentArcAngle = computeAngle(getMinimum());
		listener = new OnValueChangeListener() {
			@Override
			public void onValueChange(final int newValue) {
				setValue(newValue);

			}

			@Override
			public void onMaximumValueChange(final int newMaximum) {
				// Not used

			}

			@Override
			public void onMinimumValueChange(final int newMinimum) {
				// Not used

			}
		};

		valueAnimation = new ValueAnimation(model.getMinimum(), model.getValue(), model.getValue(), model.getMaximum());
	}

	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		circleX = AlignmentHelper.computeXLeftCorner(getDiameter(), 0, bounds.getWidth(), style.getAlignment());
		circleY = AlignmentHelper.computeYTopCorner(getDiameter(), 0, bounds.getHeight(), style.getAlignment());
		final AntiAliasedShapes shapes = AntiAliasedShapes.Singleton;
		g.setColor(style.getBackgroundColor());
		shapes.setFade(fadeFull);
		shapes.setThickness(thicknessFull);
		shapes.drawCircleArc(g, getCircleX(), getCircleY(), getDiameter(), startAngle, arcAngle);

		if (isEnabled() && getCurrentArcAngle() != 0) {
			g.setColor(getColor(style));

			shapes.setFade(fade);
			shapes.setThickness(thickness);
			shapes.drawCircleArc(g, getCircleOffset() + getCircleX(), getCircleOffset() + getCircleY(), (getDiameter() - (getCircleOffset() << 1)), startAngle,
					getCurrentArcAngle());
		}
	}

	/**
	 * Gets the color of the foreground.
	 *
	 * @param style
	 *            the style of the widget.
	 * @return the color.
	 */
	protected int getColor(final Style style) {
		return style.getForegroundColor();
	}

	@Override
	public void setBounds(final int x, final int y, final int width, final int height) {
		super.setBounds(x, y, width, height);
		final Rectangle bounds = getContentBounds();

		final int diameterAvailable = Math.min(bounds.getWidth() - bounds.getX() * 2, bounds.getHeight() - bounds.getY() * 2);
		offset = ((thicknessFull + fadeFull) >> 1);
		diameter = diameterAvailable - getCircleOffset();
	}

	/**
	 * Computes the angle for a value within the BoundedRangeModel.
	 *
	 * @return the angle.
	 */
	protected int computeAngle(final int value) {
		final float min =getMinimum();
		final int angle = (int) (((value - min) / (getMaximum() - min)) * arcAngle);
		return angle;
	}

	@Override
	public Rectangle validateContent(final Style style, final Rectangle bounds) {
		final int size = StyleHelper.getFont(style).getHeight() << 1;
		return new Rectangle(0, 0, size, size);
	}

	@Override
	public boolean handleEvent(final int event) {
		if (isEnabled()) {
			final long currentTime = System.currentTimeMillis();
			if (currentTime > nextEvent) {
				nextEvent = currentTime + EVENT_RATE;
				if(Event.getType(event)==Event.POINTER){
					final Pointer pointer = (Pointer) Event.getGenerator(event);
					final int action = Pointer.getAction(event);
					switch (action) {
					case Pointer.DRAGGED:
					case Pointer.PRESSED:
					case Pointer.RELEASED:
						final Rectangle rect = new Rectangle();
						getStyle().getMargin().wrap(rect);
						final int pointerX = pointer.getX() + rect.getX();
						final int pointerY = pointer.getY() + rect.getY();
						final int computeValue = computeValue(this.getRelativeX(pointerX), this.getRelativeY(pointerY));
						performValueChange(computeValue);
						return true;
					}
				}
			}
		}
		return super.handleEvent(event);
	}

	@Override
	public void setValue(final int value) {
		synchronized (this) {
			if (value != getValue()) {
				super.setValue(value);
				getValueAnimation().setTargetValue(value);
				startAnimation();
			}
		}
	}

	/**
	 * Sets the new value.
	 *
	 * @param value
	 *            the new value.
	 */
	protected void performValueChange(final int value) {
		setValue(value);
	}


	private int computeValue(final int pointerX, final int pointerY) {
		final int dx = pointerX - (getCircleX() + (getDiameter() >> 1));
		final int dy = pointerY - (getCircleY() + (getDiameter() >> 1));

		double atan2 = Math.toDegrees(Math.atan2(dy, dx));

		if (atan2 < 0) {
			atan2 += 360;
		}
		atan2 += startAngle;
		if (atan2 < 0) {
			atan2 = 360 + atan2;
		}

		final int absArc = Math.abs(arcAngle);
		if (atan2 > absArc) {
			if (dx < 0) {
				return getMinimum();
			} else {
				return getMaximum();
			}
		}
		if (atan2 == 0) {
			return getMinimum();
		}

		final double percent = atan2 / absArc;
		return (int) ((getMaximum() - getMinimum()) * percent + getMinimum());
	}

	@Override
	public void showNotify() {
		addOnValueChangeListener(listener);
		super.showNotify();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		removeOnValueChangeListener(listener);
	}

	/**
	 * Resets the animation.
	 */
	public void resetAnimation() {
		stopAnimation();
		getValueAnimation().reset();
		currentArcAngle = computeAngle(getValueAnimation().getCurrentValue());
	}

	/**
	 * Starts the animation.
	 */
	public void startAnimation() {
		if (!animated) {
			animated = true;
			getValueAnimation().start();
			final Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
			animator.startAnimation(this);
		}
	}

	/**
	 * Stops the animation.
	 */
	public void stopAnimation() {
		getValueAnimation().stop();
		animationEnd();
		final Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
		animator.stopAnimation(this);
	}

	@Override
	public boolean tick(final long currentTimeMillis) {
		if (!doTick(currentTimeMillis)) {
			animationEnd();
			return false;
		}
		return true;
	}

	private void animationEnd() {
		animated = false;
		if (onAnimationEndListener != null) {
			onAnimationEndListener.onAnimationEnd();
		}
	}

	/**
	 * Sets the onAnimationEndListener.
	 *
	 * @param onAnimationEndListener
	 *            the onAnimationEndListener to set.
	 */
	public void setOnAnimationEndListener(final OnAnimationEndListener onAnimationEndListener) {
		this.onAnimationEndListener = onAnimationEndListener;
	}

	/**
	 *
	 * Tick function called during the animation.
	 *
	 * @param currentTimeMillis
	 *            the currentTime.
	 * @return false if the animation is finished.
	 */
	protected boolean doTick(final long currentTimeMillis) {
		if (getValueAnimation().isFinished()) {
			return false;
		}
		getValueAnimation().tick(currentTimeMillis);
		currentArcAngle = computeAngle(getValueAnimation().getCurrentValue());
		repaint();
		return true;
	}

	/**
	 * Gets the diameter.
	 * @return the diameter.
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * Gets the x.
	 * @return the x.
	 */
	public int getCircleX() {
		return circleX;
	}

	/**
	 * Gets the y.
	 * @return the y.
	 */
	public int getCircleY() {
		return circleY;
	}

	/**
	 * Gets the valueAnimation.
	 * @return the valueAnimation.
	 */
	public ValueAnimation getValueAnimation() {
		return valueAnimation;
	}

	/**
	 * Gets the offset.
	 * @return the offset.
	 */
	public int getCircleOffset() {
		return offset;
	}

	/**
	 * Gets the currentArcAngle.
	 * @return the currentArcAngle.
	 */
	public int getCurrentArcAngle() {
		return currentArcAngle;
	}

	/**
	 * Gets the startAngle.
	 *
	 * @return the startAngle.
	 */
	public int getStartAngle() {
		return startAngle;
	}

	/**
	 * Sets the startAngle.
	 *
	 * @param startAngle
	 *            the startAngle to set.
	 */
	public void setStartAngle(final int startAngle) {
		this.startAngle = startAngle % 360;
		repaint();
	}

	/**
	 * Gets the arcAngle.
	 *
	 * @return the arcAngle.
	 */
	public int getArcAngle() {
		return arcAngle;
	}

	/**
	 * Sets the arcAngle.
	 *
	 * @param arcAngle
	 *            the arcAngle to set.
	 */
	public void setArcAngle(final int arcAngle) {
		this.arcAngle = arcAngle % 360;
		repaint();
	}

	/**
	 * Gets the fadeFull.
	 *
	 * @return the fadeFull.
	 */
	public int getFadeFull() {
		return fadeFull;
	}

	/**
	 * Sets the fadeFull.
	 *
	 * @param fadeFull
	 *            the fadeFull to set.
	 */
	public void setFadeFull(final int fadeFull) {
		this.fadeFull = fadeFull;
	}

	/**
	 * Gets the thicknessFull.
	 *
	 * @return the thicknessFull.
	 */
	public int getThicknessFull() {
		return thicknessFull;
	}

	/**
	 * Sets the thicknessFull.
	 *
	 * @param thicknessFull
	 *            the thicknessFull to set.
	 */
	public void setThicknessFull(final int thicknessFull) {
		this.thicknessFull = thicknessFull;
	}

	/**
	 * Gets the fade.
	 *
	 * @return the fade.
	 */
	public int getFade() {
		return fade;
	}

	/**
	 * Sets the fade.
	 *
	 * @param fade
	 *            the fade to set.
	 */
	public void setFade(final int fade) {
		this.fade = fade;
	}

	/**
	 * Gets the thickness.
	 *
	 * @return the thickness.
	 */
	public int getThickness() {
		return thickness;
	}

	/**
	 * Sets the thickness.
	 *
	 * @param thickness
	 *            the thickness to set.
	 */
	public void setThickness(final int thickness) {
		this.thickness = thickness;
	}
}
