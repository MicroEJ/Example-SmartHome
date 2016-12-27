/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
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
	private static final String LOW_PRIORITY_PROPERTY = "ExecutorUtils.LOW_PRIORITY";
	private static final String VERY_LOW_PRIORITY_PROPERTY = "ExecutorUtils.VERY_LOW_PRIORITY";
	private static final Map<Integer, Executor> executors = new HashMap<>();
	public static final int LOW_PRIORITY = (System.getProperty(LOW_PRIORITY_PROPERTY) == null)
			? Thread.NORM_PRIORITY - 1 : Integer.valueOf(System.getProperty(LOW_PRIORITY_PROPERTY));
	public static final int VERY_LOW_PRIORITY = (System.getProperty(VERY_LOW_PRIORITY_PROPERTY) == null)
			? Thread.NORM_PRIORITY - 1 : Integer.valueOf(System.getProperty(VERY_LOW_PRIORITY_PROPERTY));

	public static Executor getExecutor(final int priority) {
		Executor executor = executors.get(priority);
		if (executor == null) {
			executor = new SingleThreadExecutor();
			executor.execute(new Runnable() {

				@Override
				public void run() {
					final Thread currentThread = Thread.currentThread();
					currentThread.setName("Executor" + priority);
					currentThread.setPriority(priority);
				}
			});
			executors.put(priority, executor);
		}
		return executor;
	}

}
