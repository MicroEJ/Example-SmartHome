/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.animation.Animation;
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
		if (targetValue == currentValue) {
			return false;
		}
		if (targetValue > currentValue) {
			currentValue = Math.min(targetValue, currentValue + getValueProgress(currentTimeMillis));
		} else /* if (targetValue < currentValue) */ {
			currentValue = Math.max(targetValue, currentValue - getValueProgress(currentTimeMillis));
		}
		return true;
	}

	private int getValueProgress(final long currentTimeMillis) {
		final long elapsed = currentTimeMillis - lastTick;
		lastTick = currentTimeMillis;
		return (int) (elapsed * valueProgress);
	}

	@Override
	public boolean isFinished() {
		return currentValue==targetValue;
	}

	/**
	 * Gets the targetValue.
	 *
	 * @return the targetValue.
	 */
	public int getTargetValue() {
		return targetValue;
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
		return currentValue;
	}

	@Override
	public long getDuration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void start() {
		start(System.currentTimeMillis());

	}

	/**
	 * Starts the animation.
	 *
	 * @param currentTimeMillis
	 *            the currentTime.
	 */
	public void start(final long currentTimeMillis) {
		lastTick = currentTimeMillis;

	}

	/**
	 * Reset the animation.
	 */
	public void reset() {
		currentValue = start;

	}

	@Override
	public int getStartValue() {
		return start;
	}

	@Override
	public int getStopValue() {
		return targetValue;
	}

	@Override
	public int getValue(final long elapsed) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Stops the animation.
	 */
	public void stop() {
		currentValue = targetValue;

	}

	/**
	 * Set the start value.
	 * 
	 * @param value
	 *            the start value to set.
	 */
	public void setStart(final int value) {
		start = value;
	}

}
