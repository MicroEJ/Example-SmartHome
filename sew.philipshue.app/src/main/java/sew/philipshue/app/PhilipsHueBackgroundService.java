/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package sew.philipshue.app;

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
import ej.wadapps.registry.SharedRegistryFactory;
import ej.wadapps.storage.Storage;
import sew.light.LightManager;
import sew.light.LightManagerImpl;
import sew.philipshue.PhilipsHueDirectManager;

/**
 *
 */
public class PhilipsHueBackgroundService implements BackgroundService {

	private static final String HUE_DEFAULT_IP = "192.168.1.3";
	private static final String HUE_IP_ID = "HueBridgeIP";
	protected PhilipsHueDirectManager philipsHueDirectManager;
	private NetworkCallback callBack;
	private LightManager lightManager;

	@Override
	public void onStart() {
		lightManager = ServiceLoaderFactory.getServiceLoader().getService(LightManager.class, LightManagerImpl.class);
		final ConnectivityManager connectivityManager = ServiceLoaderFactory.getServiceLoader()
				.getService(ConnectivityManager.class);
		if (connectivityManager != null) {
			callBack = new NetworkCallback() {
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
		// System.out.println("PhilipsHueBackgroundService.onStart()" + lightManager.getLights().length);
		// lightManager.addLightsListener(new LightsListener() {
		//
		// @Override
		// public void onRemoveLight(final Light light) {
		// System.out.println("onRemoveLight()");
		//
		// }
		//
		// @Override
		// public void onAddLight(final Light light) {
		// System.out.println("onAddLight()");
		//
		// }
		// });

		SharedRegistryFactory.getSharedRegistry().register(LightManager.class, lightManager);
	}

	@Override
	public void onStop() {
		SharedRegistryFactory.getSharedRegistry().unregister(LightManager.class, lightManager);

		final ConnectivityManager connectivityManager = ServiceLoaderFactory.getServiceLoader()
				.getService(ConnectivityManager.class);
		if (connectivityManager != null) {
			connectivityManager.unregisterNetworkCallback(callBack);
		}
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
				}
			}

		});
	}

	private String loadIp() {
		// Load the ip
		final Storage storage = ServiceLoaderFactory.getServiceLoader().getService(Storage.class);
		if (storage != null) {
			try (InputStream inputStream = storage.load(HUE_IP_ID);) {
				if (inputStream != null) {
					final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					return reader.readLine();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}

			// If no IP exist save the default one.
			final byte[] charArray = HUE_DEFAULT_IP.getBytes();
			try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(charArray);) {
				storage.store(HUE_IP_ID, byteArrayInputStream);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return HUE_DEFAULT_IP;
	}
}
