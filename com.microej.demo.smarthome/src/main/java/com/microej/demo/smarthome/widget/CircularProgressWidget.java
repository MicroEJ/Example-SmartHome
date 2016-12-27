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
import ej.widget.model.DefaultBoundedRangeModel;

/**
 *
 */
public class CircularProgressWidget extends BoundedRange implements Animation {

	private static final int ANIMATION_STEPS = 8;

	private static final int EVENT_RATE = 50;

	private static final int DEFAULT_START_ANGLE = -120;
	private static final int DEFAULT_ARC_ANGLE = -300;
	private static final int DEFAULT_FADE_FULL = 1;
	private static final int DEFAULT_THICKNESS_FULL = 1;
	private static final int DEFAULT_FADE = 2;
	private static final int DEFAULT_THICKNESS = 3;
	protected int startAngle = DEFAULT_START_ANGLE;
	private int arcAngle = DEFAULT_ARC_ANGLE;
	private int fadeFull = DEFAULT_FADE_FULL;
	private int thicknessFull = DEFAULT_THICKNESS_FULL;
	private final OnValueChangeListener listener;
	private OnAnimationEndListener onAnimationEndListener;
	protected int fade = DEFAULT_FADE;
	protected int thickness = DEFAULT_THICKNESS;
	protected int animationProgress = 1;

	// used to compute relative position.
	protected int x;
	protected int y;
	protected int diameter;
	protected int offset;
	protected int currentArcAngle;
	protected Integer customColor;
	protected final ValueAnimation valueAnimation;

	private long nextEvent;

	// private final TransitionListener transitionListener;

	private boolean animated;


	/**
	 * @param model
	 */
	public CircularProgressWidget(final BoundedRangeModel model) {
		this(model, ValueAnimation.DEFAULT_DURATION);
	}

	/**
	 * @param model
	 */
	public CircularProgressWidget(final BoundedRangeModel model, final int duration) {
		super(model);
		animationProgress = model.getMaximum() / ANIMATION_STEPS;
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

	/**
	 * @param min
	 * @param max
	 * @param initialValue
	 */
	public CircularProgressWidget(final int min, final int max, final int initialValue) {
		this(new DefaultBoundedRangeModel(min, max, initialValue));
	}

	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		x = AlignmentHelper.computeXLeftCorner(diameter, 0, bounds.getWidth(),
				style.getAlignment());
		y = AlignmentHelper.computeYTopCorner(diameter, 0, bounds.getHeight(),
				style.getAlignment());
		final AntiAliasedShapes shapes = AntiAliasedShapes.Singleton;
		g.setColor(style.getBackgroundColor());
		shapes.setFade(fadeFull);
		shapes.setThickness(thicknessFull);
		shapes.drawCircleArc(g, x, y, diameter, startAngle, arcAngle);

		if (isEnabled() && currentArcAngle != 0) {
			if (customColor != null) {
				g.setColor(customColor);
			} else {
				g.setColor(style.getForegroundColor());
			}

			shapes.setFade(fade);
			shapes.setThickness(thickness);
			shapes.drawCircleArc(g, offset + x, offset + y, (diameter - (offset << 1)), startAngle,
					currentArcAngle);
		}
	}


	@Override
	public void setBounds(final int x, final int y, final int width, final int height) {
		super.setBounds(x, y, width, height);
		final Rectangle bounds = getContentBounds();

		final int diameterAvailable = Math.min(bounds.getWidth() - bounds.getX() * 2, bounds.getHeight() - bounds.getY() * 2);
		offset = (thicknessFull + fadeFull) >> 1;
			diameter = diameterAvailable - offset;
	}
	/**
	 * @return
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
				valueAnimation.setTargetValue(value);
				startAnimation();
			}
		}
	}

	protected void performValueChange(final int value) {
		setValue(value);
	}

	/**
	 * @param pointerX
	 * @param pointerY
	 */
	private int computeValue(final int pointerX, final int pointerY) {
		final int dx = pointerX - (x + (diameter >> 1));
		final int dy = pointerY - (y + (diameter >> 1));

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
		// transitionListener.onTransitionStart(0, 0, null, null);
		addOnValueChangeListener(listener);
		// TransitionManager.addGlobalTransitionListener(transitionListener);
		super.showNotify();
	}

	@Override
	public void hideNotify() {
		// transitionListener.onTransitionStart(0, 0, null, null);
		super.hideNotify();
		removeOnValueChangeListener(listener);
		// TransitionManager.removeGlobalTransitionListener(transitionListener);
	}

	public void initAnimation() {
		valueAnimation.reset();
		currentArcAngle = computeAngle(valueAnimation.getCurrentValue());
	}

	public void startAnimation() {
		if (!animated) {
			animated = true;
			// if (isShown()) {
			valueAnimation.start();
			final Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
			animator.startAnimation(this);
			// } else {
			// currentArcAngle = computeAngle(valueAnimation.getCurrentValue());
			// }
		}
	}

	public void stopAnimation() {
		valueAnimation.stop();
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

	/**
	 *
	 */
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

	public boolean doTick(final long currentTimeMillis) {
		if (valueAnimation.isFinished()) {
			return false;
		}
		valueAnimation.tick(currentTimeMillis);
		currentArcAngle = computeAngle(valueAnimation.getCurrentValue());
		repaint();
		return true;
	}
}
