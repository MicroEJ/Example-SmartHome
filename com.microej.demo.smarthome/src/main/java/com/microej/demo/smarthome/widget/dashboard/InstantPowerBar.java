/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.dashboard;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.motion.Motion;
import ej.motion.linear.LinearMotion;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.StyleHelper;
import ej.widget.basic.BoundedRange;

/**
 * A bar displaying the current power consumption.
 */
public class InstantPowerBar extends BoundedRange {

	private static final int FADE = 1;
	private static final int THICKNESS = 4;
	/**
	 * The duration of a motion.
	 */
	protected static final int MOTION_DURATION = 500;
	private final Object sync = new Object();
	private int target;
	private Animation animation;
	private Motion motion;

	/**
	 * Instantiates an InstantPowerBar.
	 * @param min the minimum value possible.
	 * @param max the maximum value possible.
	 * @param initialValue the initial value.
	 */
	public InstantPowerBar(final int min, final int max, final int initialValue) {
		super(min, max, initialValue);
		this.target = initialValue;
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		stopAnimation();
	}

	@Override
	public void setValue(final int value) {
		if (value != this.target) {
			this.target = value;
			startAnimation();
		}
	}

	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		g.setColor(style.getBackgroundColor());
		final int y = (bounds.getY() + bounds.getHeight()) >> 1;
		final int x = bounds.getX();
		int width = bounds.getWidth();
		g.drawLine(x, y, x + width, y);

		g.setColor(style.getForegroundColor());
		AntiAliasedShapes.Singleton.setFade(FADE);
		AntiAliasedShapes.Singleton.setThickness(THICKNESS);
		final float complete = getPercentComplete();
		width = (int) (width * complete);
		if(width<=0){
			width = 1;
		}
		AntiAliasedShapes.Singleton.drawLine(g, x, y, x + width, y);
	}

	@Override
	public Rectangle validateContent(final Style style, final Rectangle availableSize) {
		final int size = StyleHelper.getFont(style).getHeight() << 1;
		return new Rectangle(0, 0, size, size);
	}

	private void startAnimation() {
		synchronized (this.sync) {
			if (this.animation == null) {
				this.motion = new LinearMotion(getValue(), this.target, MOTION_DURATION);

				this.motion.start();
				final Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
				animator.startAnimation(new Animation() {

					@Override
					public boolean tick(final long currentTimeMillis) {
						final int dif = InstantPowerBar.this.target - getValue();
						if (dif == 0) {
							InstantPowerBar.this.animation = null;
							return false;
						}
						setModelValue(InstantPowerBar.this.motion.getCurrentValue());
						return true;
					}
				});
			}
		}
	}

	private void setModelValue(final int i) {
		super.setValue(i);

	}

	private void stopAnimation() {
		synchronized (this.sync) {
			if (this.animation != null) {
				final Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
				animator.stopAnimation(this.animation);
				this.animation = null;
			}
		}
	}
}
