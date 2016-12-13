/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.standalone;

import java.net.SocketException;

import com.microej.demo.smarthome.data.zwave.ZwaveControllerManager;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.components.registry.BundleRegistry;
import ej.components.registry.util.BundleRegistryHelper;

/**
 *
 */
public class Main {

	public static void main(String[] args) throws SocketException {
		BundleRegistry registry = ServiceLoaderFactory.getServiceLoader().getService(BundleRegistry.class);
		BundleRegistryHelper.startup(registry);

		new ZwaveControllerManager();

		ZwaveStandaloneBackgroundService bg = new ZwaveStandaloneBackgroundService();
		bg.onStart();

		// PhilipsHueBackgroundService philipsHueBackgroundService = new PhilipsHueBackgroundService();
		// philipsHueBackgroundService.onStart();

		com.microej.demo.smarthome.Main.main(args);
	}
}
