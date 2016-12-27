/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.standalone;

import com.microej.demo.smarthome.data.zwave.ZwaveControllerManager;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.components.registry.BundleRegistry;
import ej.components.registry.util.BundleRegistryHelper;

/**
 *
 */
public class Main {

	public static void main(final String[] args) {
		final BundleRegistry registry = ServiceLoaderFactory.getServiceLoader().getService(BundleRegistry.class);
		BundleRegistryHelper.startup(registry);


		new ZwaveControllerManager();

		final ZwaveSimuBackgroundService bg = new ZwaveSimuBackgroundService();
		bg.onStart();

		final PhilipsHueBackgroundService philipsHueBackgroundService = new PhilipsHueBackgroundService();
		philipsHueBackgroundService.onStart();


		com.microej.demo.smarthome.Main.main(args);
	}
}
