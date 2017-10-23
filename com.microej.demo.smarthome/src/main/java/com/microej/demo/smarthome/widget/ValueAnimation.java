/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.animation.Animation;
import ej.bon.Util;
import ej.motion.Motion;

/**
 * Value changing over the time.
 */
public class ValueAnimation implements Motion, Animation {

	/**
	 * The default duration of the animation.
	 */
	public static final int DEFAULT_DURATION = 350;
	private int start;
	private int currentValue;
	private int targetValue;
	private final float valueProgress;
	private long lastTick;

	/**
	 * Instantiates a ValueAnimation.
	 *
	 * @param start
	 *            the starting value.
	 * @param currentValue
	 *            the current value
	 * @param targetValue
	 *            the target value.
	 */
	public ValueAnimation(final int start, final int currentValue, final int targetValue) {
		this(start, currentValue, targetValue, targetValue);
	}

	/**
	 * Instantiates a ValueAnimation.
	 *
	 * @param start
	 *            the starting value.
	 * @param currentValue
	 *            the current value
	 * @param targetValue
	 *            the target value.
	 * @param maxValue
	 *            the maximum value.
	 */
	public ValueAnimation(final int start, final int currentValue, final int targetValue, final int maxValue) {
		this(start, currentValue, targetValue, maxValue, DEFAULT_DURATION);
	}

	/**
	 * Instantiates a ValueAnimation.
	 *
	 * @param start
	 *            the starting value.
	 * @param currentValue
	 *            the current value
	 * @param targetValue
	 *            the target value.
	 * @param maxValue
	 *            the maximum value.
	 * @param duration
	 *            the duration of the animation.
	 */
	public ValueAnimation(final int start, final int currentValue, final int targetValue, final int maxValue,
			final int duration) {
		super();
		this.start = start;
		this.currentValue = currentValue;
		this.targetValue = targetValue;
		this.valueProgress = ((float) maxValue) / duration;
	}

	@Override
	public boolean tick(final long currentTimeMillis) {
		int currentValue = this.currentValue;
		if (this.targetValue == currentValue) {
			return false;
		}
		int progress = getValueProgress(currentTimeMillis);
		if (this.targetValue > currentValue) {
			this.currentValue = Math.min(this.targetValue, currentValue + progress);
		} else /* if (targetValue < currentValue) */ {
			this.currentValue = Math.max(this.targetValue, currentValue - progress);
		}
		return true;
	}

	private int getValueProgress(final long currentTimeMillis) {
		final long elapsed = currentTimeMillis - this.lastTick;
		this.lastTick = currentTimeMillis;
		Long progress = Long.valueOf((long) (elapsed * this.valueProgress));
		return progress.intValue();
	}

	@Override
	public boolean isFinished() {
		return this.currentValue == this.targetValue;
	}

	/**
	 * Gets the targetValue.
	 *
	 * @return the targetValue.
	 */
	public int getTargetValue() {
		return this.targetValue;
	}

	/**
	 * Sets the targetValue.
	 *
	 * @param targetValue
	 *            the targetValue to set.
	 */
	public void setTargetValue(final int targetValue) {
		this.targetValue = targetValue;
	}

	@Override
	public int getCurrentValue() {
		return this.currentValue;
	}

	@Override
	public long getDuration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void start() {
		start(Util.platformTimeMillis());

	}

	/**
	 * Starts the animation.
	 *
	 * @param currentTimeMillis
	 *            the currentTime.
	 */
	public void start(final long currentTimeMillis) {
		this.lastTick = currentTimeMillis;
	}

	/**
	 * Reset the animation.
	 */
	public void reset() {
		this.currentValue = this.start;

	}

	@Override
	public int getStartValue() {
		return this.start;
	}

	@Override
	public int getStopValue() {
		return this.targetValue;
	}

	@Override
	public int getValue(final long elapsed) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Stops the animation.
	 */
	public void stop() {
		this.currentValue = this.targetValue;

	}

	/**
	 * Set the start value.
	 * 
	 * @param value
	 *            the start value to set.
	 */
	public void setStart(final int value) {
		this.start = value;
	}

}
