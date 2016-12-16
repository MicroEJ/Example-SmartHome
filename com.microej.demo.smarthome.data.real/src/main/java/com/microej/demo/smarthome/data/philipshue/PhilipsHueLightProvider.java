/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.philipshue;

import java.util.HashMap;
import java.util.Map;

import com.microej.demo.smarthome.data.impl.Provider;
import com.microej.demo.smarthome.util.ExecutorUtils;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import sew.light.Light;
import sew.light.LightManager;
import sew.light.LightsListener;

/**
 *
 */
public class PhilipsHueLightProvider extends Provider<com.microej.demo.smarthome.data.light.Light>
implements com.microej.demo.smarthome.data.light.LightProvider {

	private final Map<Light, HueLightSensor> lights;

	/**
	 * @param realDataProvider
	 * @param lightGroup
	 */
	public PhilipsHueLightProvider() {
		lights = new HashMap<>();
		LightManager lightManager = ServiceLoaderFactory.getServiceLoader().getService(LightManager.class);
		lightManager.addLightsListener(new LightsListener() {

			@Override
			public void onRemoveLight(Light light) {
				removeLight(light);

			}


			@Override
			public void onAddLight(Light light) {
				addLight(light);

			}


		});

		ExecutorUtils.getExecutor(ExecutorUtils.LOW_PRIORITY).execute(new Runnable() {

			@Override
			public void run() {
				for (Light light : lightManager.getLights()) {
					addLight(light);
				}
			}
		});

	}

	private void addLight(Light light) {
		HueLightSensor sensor = new HueLightSensor(light);
		lights.put(light, sensor);
		add(sensor);
	}

	private void removeLight(Light light) {
		HueLightSensor sensor = lights.get(light);
		if (sensor != null) {
			remove(sensor);
			lights.remove(light);
		}
	}

	@Override
	public com.microej.demo.smarthome.data.light.Light[] list() {
		com.microej.demo.smarthome.data.light.Light[] list = new com.microej.demo.smarthome.data.light.Light[devices
		                                                                                                     .size()];
		list = devices.toArray(list);
		return list;
	}
}
