/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.util;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.event.EventGenerator;
import ej.microui.util.EventHandler;

/**
 * A robot that animate the pages.
 */
public abstract class Robot implements WatchDogCallBack {

	private static final int DEFAULT_PERIOD = 1_000;

	private final SoftWatchDog watchDog;
	private int period = DEFAULT_PERIOD;
	private int delay;

	private boolean useWatchdog = false;

	private TimerTask task;
	private Timer timer;

	/**
	 * Instantiate robot.
	 *
	 * @param period
	 *            The period in between task.
	 */
	public Robot(int period) {
		watchDog = new SoftWatchDog();
		this.period = period;
	}

	/**
	 * Instantiate robot.
	 *
	 * @param period
	 *            The period in between task.
	 * @param delay
	 *            The delay before starting.
	 */
	public Robot(int period, int delay) {
		this.period = period;
		watchDog = new SoftWatchDog(delay);
	}

	/**
	 * Instantiate robot.
	 *
	 * @param period
	 *            The period in between task.
	 * @param delay
	 *            The delay before starting.
	 * @param timer
	 *            The timer for the robot.
	 */
	public Robot(int period, int delay, Timer timer) {
		this(period, delay);
		this.timer = timer;
	}

	/**
	 * Automatically starts after a delay.
	 */
	public void startWatchdog() {
		setUseWatchdog(true);
		watchDog.start(this);
	}

	/**
	 * Cancels the auto start.
	 */
	public void stopWatchdog() {
		watchDog.stop();
	}

	/**
	 * Stops the robot.
	 */
	public void stop() {
		if (isUseWatchdog() && !watchDog.isRunning()) {
			startWatchdog();
		}

		synchronized (this) {
			if (this.task != null) {
				this.task.cancel();
				this.task = null;
			}
		}
	}

	/**
	 * Starts the robot.
	 */
	public void start() {
		stopWatchdog();

		synchronized (this) {
			this.task = new AutomateTask();
			getTimer().schedule(this.task, 0);
		}
	}

	/**
	 * Changes the period for the next robot automate.
	 *
	 * @param period
	 *            The period.
	 * @see automate
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * Gets the delay before automatically starting.
	 *
	 * @return the delay.
	 */
	public int getDelay() {
		return this.delay;
	}

	/**
	 * Sets the delay before automatically starting.
	 *
	 * @param delay
	 *            the delay to set.
	 */
	public void setDelay(int delay) {
		this.delay = delay;
		if (watchDog.isRunning()) {
			stopWatchdog();
			startWatchdog();
		}
	}

	/**
	 * Resets the autoStart delay.
	 */
	public void feed() {
		watchDog.feed();
	}

	private class AutomateTask extends TimerTask {
		@Override
		public void run() {
			automate();
			synchronized (this) {
				Robot.this.task = new AutomateTask();
				getTimer().schedule(Robot.this.task, Robot.this.period);
			}
		}
	}

	/**
	 * Fully stops the robot.
	 */
	public void cancel() {
		setUseWatchdog(false);
		stopWatchdog();
		stop();
	}

	/**
	 * Adds an event generator to feed the robot.
	 *
	 * @param generator
	 *            Every event from this generator will feed the robot.
	 */
	public void addEventGeneratorTrigger(EventGenerator generator) {
		final EventHandler generatorEventHandler = generator.getEventHandler();
		generator.setEventHandler(new EventHandler() {
			@Override
			public boolean handleEvent(int event) {
				if (isRunning()) {
					stop();
				} else {
					feed();
				}
				return generatorEventHandler.handleEvent(event);
			}

		});
	}

	private boolean isRunning() {
		return task != null;
	}

	/**
	 * Automate an action.
	 */
	protected abstract void automate();

	/**
	 * Sets the timer.
	 *
	 * @param timer
	 *            the timer to set.
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	/**
	 * Gets the timer.
	 *
	 * @return the timer or the default timer.
	 */
	private Timer getTimer() {
		if (timer != null) {
			return timer;
		} else {
			return ServiceLoaderFactory.getServiceLoader().getService(Timer.class);
		}
	}

	@Override
	public void timeOut() {
		start();
	}

	/**
	 * Gets the useWatchdog.
	 *
	 * @return the useWatchdog.
	 */
	public boolean isUseWatchdog() {
		return useWatchdog;
	}

	/**
	 * Sets the useWatchdog.
	 *
	 * @param useWatchdog
	 *            the useWatchdog to set.
	 */
	public void setUseWatchdog(boolean useWatchdog) {
		this.useWatchdog = useWatchdog;
	}
}