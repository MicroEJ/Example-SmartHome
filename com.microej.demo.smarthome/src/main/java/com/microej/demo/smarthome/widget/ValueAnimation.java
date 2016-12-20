/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.animation.Animation;
import ej.motion.Motion;

/**
 *
 */
public class ValueAnimation implements Motion, Animation {

	private static final int DURATION = 500;
	private int start;
	private int currentValue;
	private int targetValue;
	private final float valueProgress;
	private long lastTick;



	/**
	 * @param start
	 * @param currentValue
	 * @param targetValue
	 */
	public ValueAnimation(int start, int currentValue, int targetValue) {
		this(start, currentValue, targetValue, targetValue);
	}

	/**
	 * @param start
	 * @param currentValue
	 * @param targetValue
	 */
	public ValueAnimation(int start, int currentValue, int targetValue, int maxValue) {
		super();
		this.start = start;
		this.currentValue = currentValue;
		this.targetValue = targetValue;
		this.valueProgress = ((float) maxValue) / DURATION;
	}

	@Override
	public boolean tick(long currentTimeMillis) {
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

	/**
	 * @param currentTimeMillis
	 * @return
	 */
	private int getValueProgress(long currentTimeMillis) {
		long elapsed = currentTimeMillis - lastTick;
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
	public void setTargetValue(int targetValue) {
		this.targetValue = targetValue;
	}

	/**
	 * Gets the currentValue.
	 *
	 * @return the currentValue.
	 */
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
	 * @param currentTimeMillis
	 */
	public void start(long currentTimeMillis) {
		lastTick = currentTimeMillis;

	}

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
	public int getValue(long elapsed) {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 */
	public void stop() {
		currentValue = targetValue;

	}

	/**
	 * @param value
	 */
	public void setStart(int value) {
		start = value;
	}

}
