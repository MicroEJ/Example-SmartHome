/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.util;

import ej.bon.IllegalStateException;
import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;

/**
 * A watchdog using a java timer.
 */
public class SoftWatchDog {
	// Delay for autostart.
	private static final int DEFAULT_DELAY = 40_000;

	// last time feed.
	private long feed = 0;
	private TimerTask timeoutTask;

	private WatchDogCallBack callback;

	private int delay;
	private Timer timer;

	/**
	 * Constructor of a Watch Dog.
	 *
	 * @param delay
	 *            Delay before timeout.
	 */
	public SoftWatchDog(int delay) {
		super();
		this.delay = delay;
	}

	/**
	 *
	 * Constructor of a Watch Dog.
	 *
	 */
	public SoftWatchDog() {
		this(DEFAULT_DELAY);
	}

	/**
	 * Starts the dog with the described delay.
	 *
	 * @param callBack
	 *            Instance to callback when the time run-out.
	 * @throws IllegalStateException
	 *             when the watchdog is already started.
	 * @throws IllegalArgumentException
	 *             when the callBack is null.
	 */
	public void start(WatchDogCallBack callBack) throws IllegalStateException, IllegalArgumentException {
		if (isRunning()) {
			throw new IllegalStateException();
		}
		if (callBack == null) {
			throw new IllegalArgumentException();
		}
		callback = callBack;
		synchronized (this) {
			feed();
			this.timeoutTask = new TimeoutTask();
			getTimer().schedule(this.timeoutTask, 0);
		}
	}

	/**
	 * Starts the dog with the described delay.
	 *
	 * @param callBack
	 *            Instance to callback when the time run-out.
	 * @param delay
	 *            Delay in ms before the timeout should be more than 0.
	 * @throws IllegalStateException
	 *             when the watchdog is already started.
	 * @throws IllegalArgumentException
	 *             when the callBack is null or delay is less or equal to 0.
	 */
	public void start(WatchDogCallBack callBack, int delay) throws IllegalStateException, IllegalArgumentException {
		if (delay <= 0) {
			throw new IllegalArgumentException();
		}
		this.delay = delay;
		start(callBack);
	}

	/**
	 * Stops the watch dog.
	 */
	public void stop() {
		synchronized (this) {
			if (isRunning()) {
				timeoutTask.cancel();
				timeoutTask = null;
			}
		}
	}

	/**
	 * Feeds the dog.
	 */
	public void feed() {
		this.feed = System.currentTimeMillis();
	}

	private class TimeoutTask extends TimerTask {

		@Override
		public void run() {
			long currentTime = System.currentTimeMillis();
			long delayRemainning = feed + delay - currentTime;
			synchronized (this) {
				if (delayRemainning > 0) {
					timeoutTask = new TimeoutTask();
					getTimer().schedule(timeoutTask, delayRemainning);
				} else {
					timeout();
				}
			}
		}
	}

	/**
	 * The time is running out!
	 */
	private void timeout() {
		callback.timeOut();

	}



	/**
	 * Whether the watch dog is running or not.
	 *
	 * @return true if the watchdog is on.
	 */
	public boolean isRunning() {
		return timeoutTask != null;
	}

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
}
