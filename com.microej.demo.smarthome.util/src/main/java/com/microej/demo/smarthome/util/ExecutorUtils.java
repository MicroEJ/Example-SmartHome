/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import ej.util.concurrent.SingleThreadExecutor;

/**
 *
 */
public class ExecutorUtils {
	private static final Map<Integer, Executor> executors = new HashMap<>();
	// public static final int LOW_PRIORITY = Thread.NORM_PRIORITY - 1;
	// public static final int VERY_LOW_PRIORITY = LOW_PRIORITY - 1;
	public static final int LOW_PRIORITY = Thread.NORM_PRIORITY;
	public static final int VERY_LOW_PRIORITY = LOW_PRIORITY;

	public static Executor getExecutor(int priority) {
		Executor executor = executors.get(priority);
		if (executor == null) {
			executor = new SingleThreadExecutor();
			executor.execute(new Runnable() {

				@Override
				public void run() {
					Thread currentThread = Thread.currentThread();
					currentThread.setName("Executor" + priority);
					currentThread.setPriority(priority);
				}
			});
			executors.put(priority, executor);
		}
		return executor;
	}

}
