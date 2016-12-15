/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.philipshue;

import java.io.IOException;

import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.wadapps.app.BackgroundService;
import sew.philipshue.PhilipsHueDirectManager;

/**
 *
 */
public class PhilipsHueBackgroundService implements BackgroundService {

	private PhilipsHueDirectManager philipsHueDirectManager;
	private NetworkCallback callBack;

	@Override
	public void onStart() {

		ConnectivityManager connectivityManager = ServiceLoaderFactory.getServiceLoader()
				.getService(ConnectivityManager.class);
		if (connectivityManager != null) {
			callBack =new NetworkCallback() {
				@Override
				public void onAvailable(Network network) {
					startManager();
				}
			};
			connectivityManager.registerNetworkCallback(null, callBack);
			if (connectivityManager.getActiveNetworkInfo().isConnected()) {
				startManager();
			}
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}

	private synchronized void startManager() {
		if (philipsHueDirectManager == null) {
			try {
				philipsHueDirectManager = new PhilipsHueDirectManager("192.168.6.33");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
