/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.iot;

import android.net.ConnectivityManager;
import ej.bon.Timer;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.components.registry.BundleActivator;
import ej.components.registry.BundleRegistry;
import ej.components.registry.util.BundleRegistryHelper;
import ej.net.NetworkChangeCallback;
import ej.net.PollerConnectivityManager;

/**
 *
 */
public class ConnectivityManagerActivator implements BundleActivator {

	private PollerAndroidConnectivityManger poller;

	@Override
	public void initialize() {
		BundleRegistry registry = BundleRegistryHelper.getRegistry();
		poller = new PollerAndroidConnectivityManger();
		registry.register(ConnectivityManager.class, poller);

	}

	@Override
	public void link() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {


	}

	@Override
	public void stop() {

	}

	private class PollerAndroidConnectivityManger extends ConnectivityManager implements NetworkChangeCallback {
		private final PollerConnectivityManager poller;

		/**
		 *
		 */
		public PollerAndroidConnectivityManger() {
			super();
			poller = new PollerConnectivityManager(ServiceLoaderFactory.getServiceLoader().getService(Timer.class));
			poller.setNetworkChangeCallback(this);
		}

		@Override
		public void notifyAvailable(boolean available) {
			super.notifyNetworkCallbacks(available);

		}

	}
}
