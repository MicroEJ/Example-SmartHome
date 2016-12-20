/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome;

import com.microej.demo.smarthome.navigator.DirectNavigator;
import com.microej.demo.smarthome.page.SmartHomePage;
import com.microej.demo.smarthome.style.StylePopulator;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.MicroUI;
import ej.mwt.Desktop;
import ej.mwt.Panel;

/**
 *
 */
public class Main {

	private static Desktop desktop;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ServiceLoaderFactory.getServiceLoader().getService(Timer.class, Timer.class).schedule(new TimerTask() {

			@Override
			public void run() {
				Thread.currentThread().setName("MainTimer");

			}
		}, 0);

		MicroUI.start();

		StylePopulator.initializeStylesheet();

		SmartHomePage mainPage = new SmartHomePage();
		DirectNavigator navigator = new DirectNavigator();
		navigator.show(mainPage, true);

		desktop = new Desktop();
		Panel panel = new Panel();
		panel.setWidget(navigator);
		panel.show(desktop, true);
		desktop.show();

		// Starts robot.
		new HomeRobot();
	}

	public static Desktop getDesktop() {
		return desktop;
	}
}
