/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.app;

import ej.wadapps.app.Activity;

/**
 *
 */
public class SmartHomeActivity implements Activity {

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// com.microej.demo.smarthome.Main.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick() {
		// ServiceLoaderFactory.getServiceLoader().getService(ExitHandler.class).exit();
		//
		// }
		// });

	}

	@Override
	public void onRestart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		com.microej.demo.smarthome.Main.main(new String[0]);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		com.microej.demo.smarthome.Main.stopRobot();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

}
