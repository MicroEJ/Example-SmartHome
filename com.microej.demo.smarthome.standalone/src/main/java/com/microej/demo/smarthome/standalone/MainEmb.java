/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.standalone;

import com.microej.demo.smarthome.Main;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.components.registry.BundleRegistry;
import ej.components.registry.util.BundleRegistryHelper;

/**
 *
 */
public class MainEmb {

	public static void main(String[] args) {
		BundleRegistry registry = ServiceLoaderFactory.getServiceLoader().getService(BundleRegistry.class);
		BundleRegistryHelper.startup(registry);

		ZwaveEmbBackgroundService bg = new ZwaveEmbBackgroundService();
		bg.onStart();

		PhilipsHueBackgroundService philipsHueBackgroundService = new PhilipsHueBackgroundService();
		philipsHueBackgroundService.onStart();

		Main.main(args);
	}
}
