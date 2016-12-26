/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.standalone;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.microej.demo.smarthome.util.ExecutorUtils;

import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.wadapps.app.BackgroundService;
import ej.wadapps.storage.Storage;
import sew.philipshue.PhilipsHueDirectManager;

/**
 *
 */
public class PhilipsHueBackgroundService implements BackgroundService {

	private static final String HUE_DEFAULT_IP = "192.168.6.33";
	private static final String HUE_IP_ID = "HueBridgeIP";
	protected PhilipsHueDirectManager philipsHueDirectManager;
	private NetworkCallback callBack;

	@Override
	public void onStart() {

		final ConnectivityManager connectivityManager = ServiceLoaderFactory.getServiceLoader()
				.getService(ConnectivityManager.class);
		if (connectivityManager != null) {
			callBack =new NetworkCallback() {
				@Override
				public void onAvailable(final Network network) {
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
		ExecutorUtils.getExecutor(ExecutorUtils.LOW_PRIORITY).execute(new Runnable() {

			@Override
			public void run() {
				if (philipsHueDirectManager == null) {
					try {
						final String ip = loadIp();
						philipsHueDirectManager = new PhilipsHueDirectManager(ip);

					} catch (final IOException e) {
						e.printStackTrace();
					}
				}}

		});
	}

	private String loadIp() {
		// Load the ip
		final Storage storage = ServiceLoaderFactory.getServiceLoader().getService(Storage.class);
		if (storage != null) {
			InputStream inputStream;
			try {
				inputStream = storage.load(HUE_IP_ID);
				if (inputStream != null) {
					final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					return reader.readLine();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}

			// If no IP exist save the default one.
			final byte[] charArray = HUE_DEFAULT_IP.getBytes();
			final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(charArray);
			try {
				storage.store(HUE_IP_ID, byteArrayInputStream);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return HUE_DEFAULT_IP;
	}
}
