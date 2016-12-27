/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
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
import ej.widget.listener.OnClickListener;

/**
 *
 */
public class Main {

	private static Desktop desktop;
	private static OnClickListener onClickListener;
	private static HomeRobot homeRobot;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		ServiceLoaderFactory.getServiceLoader().getService(Timer.class).schedule(new TimerTask() {

			@Override
			public void run() {
				Thread.currentThread().setName("MainTimer");

			}
		}, 0);

		MicroUI.start();

		StylePopulator.initializeStylesheet();

		final SmartHomePage mainPage = new SmartHomePage();
		if (onClickListener != null) {
			mainPage.addOnClickListener(onClickListener);
		}
		final DirectNavigator navigator = new DirectNavigator();
		navigator.show(mainPage, true);

		desktop = new Desktop();
		final Panel panel = new Panel();
		panel.setWidget(navigator);
		panel.show(desktop, true);
		desktop.show();

		// Starts robot.
		homeRobot = new HomeRobot();
	}

	public static Desktop getDesktop() {
		return desktop;
	}

	/**
	 * Sets the onClickListener.
	 *
	 * @param onClickListener
	 *            the onClickListener to set.
	 */
	public static void setOnClickListener(final OnClickListener onClickListener) {
		Main.onClickListener = onClickListener;
	}

	public static void stopRobot() {
		if (homeRobot != null) {
			homeRobot.cancel();
		}
	}
}
