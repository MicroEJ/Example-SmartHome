/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.philipshue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.microej.demo.smarthome.data.impl.AbstractProvider;
import com.microej.demo.smarthome.data.light.Light;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import sew.light.LightManager;
import sew.light.LightsListener;

/**
 *
 */
public class PhilipsHueLightProvider extends AbstractProvider<Light>
implements com.microej.demo.smarthome.data.light.LightProvider, LightsListener {

	private final static int MAX_WAIT = 10_000;
	private final static int MIN_WAIT = 1_000;
	private final static int INCREASE_WAIT = 1_000;
	private final Map<sew.light.Light, HueLightSensor> lights;
	private LightManager lightManager;
	private TimerTask task;
	private int delay = MIN_WAIT;

	/**
	 * @param realDataProvider
	 * @param lightGroup
	 */
	public PhilipsHueLightProvider() {
		lights = new HashMap<>();
		start();

	}

	public void start() {
		pollForLightManager();
	}

	public synchronized void stop(){
		if(task!=null){
			task.cancel();
			task = null;
		}
	}

	/**
	 *
	 */
	private synchronized void pollForLightManager() {
		task = null;
		final LightManager newlightManager = ServiceLoaderFactory.getServiceLoader().getService(LightManager.class);
		if (newlightManager != null && lightManager == null) {
			delay = MIN_WAIT;
			lightManager = newlightManager;
			lightManager.addLightsListener(this);

			HueLightSensor.EXECUTOR.execute(new Runnable() {

				@Override
				public void run() {
					for (final sew.light.Light light : lightManager.getLights()) {
						onAddLight(light);
					}
				}
			});
		} else if (newlightManager == null && lightManager != null) {
			delay = MIN_WAIT;
			lightManager = null;
			final Set<sew.light.Light> keySet = new HashSet<>(lights.keySet());
			for (final sew.light.Light light : keySet) {
				onRemoveLight(light);
			}
		} else {
			delay = Math.min(delay + INCREASE_WAIT, MAX_WAIT);
		}
		task = new TimerTask() {

			@Override
			public void run() {
				pollForLightManager();

			}
		};
		ServiceLoaderFactory.getServiceLoader().getService(Timer.class).schedule(task, delay);

	}

	@Override
	public com.microej.demo.smarthome.data.light.Light[] list() {
		com.microej.demo.smarthome.data.light.Light[] list = new com.microej.demo.smarthome.data.light.Light[devices
		                                                                                                     .size()];
		list = devices.toArray(list);
		return list;
	}

	@Override
	public void onAddLight(final sew.light.Light light) {
		final HueLightSensor sensor = new HueLightSensor(light);
		lights.put(light, sensor);
		add(sensor);

	}

	@Override
	public void onRemoveLight(final sew.light.Light light) {
		final HueLightSensor sensor = lights.get(light);
		if (sensor != null) {
			remove(sensor);
			lights.remove(light);
		}
	}
}
