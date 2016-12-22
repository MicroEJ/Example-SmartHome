/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
import ej.widget.navigation.TransitionListener;
import ej.widget.navigation.TransitionManager;
import ej.widget.navigation.page.Page;

/**
 *
 */
public class CircularProgressWidget extends BoundedRange implements Animation {

	private static final int ANIMATION_STEPS = 8;

	private static int EVENT_RATE = 50;

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

	private final TransitionListener transitionListener;

	private boolean animated;


	/**
	 * @param model
	 */
	public CircularProgressWidget(BoundedRangeModel model) {
		super(model);
		animationProgress = model.getMaximum() / ANIMATION_STEPS;
		currentArcAngle = computeAngle(getMinimum());
		listener = new OnValueChangeListener() {
			@Override
			public void onValueChange(int newValue) {
				setValue(newValue);

			}

			@Override
			public void onMaximumValueChange(int newMaximum) {
				// Not used

			}

			@Override
			public void onMinimumValueChange(int newMinimum) {
				// Not used

			}
		};

		transitionListener = new TransitionListener() {

			@Override
			public void onTransitionStop() {
				if (isShown()) {
					startAnimation();
				}

			}

			@Override
			public void onTransitionStep(int step) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTransitionStart(int transitionsSteps, int transitionsStop, Page from, Page to) {
				stopAnimation();
				initAnimation();

			}
		};

		valueAnimation = new ValueAnimation(model.getMinimum(), model.getValue(), model.getValue(),
				model.getMaximum());
	}

	/**
	 * @param min
	 * @param max
	 * @param initialValue
	 */
	public CircularProgressWidget(int min, int max, int initialValue) {
		this(new DefaultBoundedRangeModel(min, max, initialValue));
	}

	@Override
	public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
		x = AlignmentHelper.computeXLeftCorner(diameter, 0, bounds.getWidth(),
				style.getAlignment());
		y = AlignmentHelper.computeYTopCorner(diameter, 0, bounds.getHeight(),
				style.getAlignment());
		AntiAliasedShapes shapes = AntiAliasedShapes.Singleton;
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
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		Rectangle bounds = getContentBounds();

		int diameterAvailable = Math.min(bounds.getWidth() - bounds.getX() * 2, bounds.getHeight() - bounds.getY() * 2);
		offset = (thicknessFull + fadeFull) >> 1;
			diameter = diameterAvailable - offset;
	}
	/**
	 * @return
	 */
	protected int computeAngle(int value) {
		float min =getMinimum();
		int angle = (int) (((value - min) / (getMaximum() - min)) * arcAngle);
		return angle;
	}

	@Override
	public Rectangle validateContent(Style style, Rectangle bounds) {
		int size = StyleHelper.getFont(style).getHeight() << 1;
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
	public void setStartAngle(int startAngle) {
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
	public void setArcAngle(int arcAngle) {
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
	public void setFadeFull(int fadeFull) {
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
	public void setThicknessFull(int thicknessFull) {
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
	public void setFade(int fade) {
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
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	@Override
	public boolean handleEvent(int event) {
		if (isEnabled()) {
			long currentTime = System.currentTimeMillis();
			if (currentTime > nextEvent) {
				nextEvent = currentTime + EVENT_RATE;
				if(Event.getType(event)==Event.POINTER){
					Pointer pointer = (Pointer) Event.getGenerator(event);
					int action = Pointer.getAction(event);
					switch (action) {
					case Pointer.DRAGGED:
					case Pointer.PRESSED:
					case Pointer.RELEASED:
						Rectangle rect = new Rectangle();
						getStyle().getMargin().wrap(rect);
						int pointerX = pointer.getX() + rect.getX();
						int pointerY = pointer.getY() + rect.getY();
						int computeValue = computeValue(this.getRelativeX(pointerX), this.getRelativeY(pointerY));
						performValueChange(computeValue);
						return true;
					}
				}
			}
		}
		return super.handleEvent(event);
	}

	@Override
	public void setValue(int value) {
		synchronized (this) {
			if (value != getValue()) {
				super.setValue(value);
				valueAnimation.setTargetValue(value);
				startAnimation();
			}
		}
	}

	protected void performValueChange(int value) {
		setValue(value);
	}

	/**
	 * @param pointerX
	 * @param pointerY
	 */
	private int computeValue(int pointerX, int pointerY) {
		int dx = pointerX - (x + (diameter >> 1));
		int dy = pointerY - (y + (diameter >> 1));

		double atan2 = Math.toDegrees(Math.atan2(dy, dx));

		if (atan2 < 0) {
			atan2 += 360;
		}
		atan2 += startAngle;
		if (atan2 < 0) {
			atan2 = 360 + atan2;
		}

		int absArc = Math.abs(arcAngle);
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

		double percent = atan2 / absArc;
		return (int) ((getMaximum() - getMinimum()) * percent + getMinimum());
	}

	@Override
	public void showNotify() {
		transitionListener.onTransitionStart(0, 0, null, null);
		addOnValueChangeListener(listener);
		TransitionManager.addGlobalTransitionListener(transitionListener);
		super.showNotify();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		removeOnValueChangeListener(listener);
		TransitionManager.removeGlobalTransitionListener(transitionListener);
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
			Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
			animator.startAnimation(this);
			// } else {
			// currentArcAngle = computeAngle(valueAnimation.getCurrentValue());
			// }
		}
	}

	public void stopAnimation() {
		valueAnimation.stop();
		animated = false;
		Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
		animator.stopAnimation(this);
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		if (!doTick(currentTimeMillis)) {
			animated = false;
			return false;
		}
		return true;
	}

	public boolean doTick(long currentTimeMillis) {
		if (valueAnimation.isFinished()) {
			return false;
		}
		valueAnimation.tick(currentTimeMillis);
		currentArcAngle = computeAngle(valueAnimation.getCurrentValue());
		repaint();
		return true;
	}
}
