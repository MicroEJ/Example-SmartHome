/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome;

import com.microej.demo.smarthome.navigator.DirectNavigator;
import com.microej.demo.smarthome.page.SmartHomePage;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.style.StylePopulator;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.MicroUI;
import ej.mwt.Desktop;
import ej.mwt.Panel;
import ej.widget.listener.OnClickListener;
import ej.widget.navigation.transition.CircleSplashTransitionManager;
import ej.widget.navigation.transition.SplashTransitionManager;

/**
 *
 */
public class Main {

	private static Desktop desktop;
	private static OnClickListener onClickListener;
	private static HomeRobot homeRobot;
	private static SplashTransitionManager transitionManager;
	private static DirectNavigator navigator;
	private static SmartHomePage mainPage;

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

		mainPage = new SmartHomePage();
		if (onClickListener != null) {
			mainPage.addOnClickListener(onClickListener);
		}
		navigator = new DirectNavigator();
		navigator.addClassSelector(ClassSelectors.PICKER);
		transitionManager = new CircleSplashTransitionManager();
		navigator.setTransitionManager(transitionManager);
		transitionManager.setDuration(300);
		// transitionManager.setDuration(3000);
		navigator.show(mainPage, true);

		desktop = new Desktop();
		final Panel panel = new Panel();
		panel.setWidget(navigator);
		panel.showFullScreen(desktop);
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

	/**
	 * Gets the navigator.
	 *
	 * @return the navigator.
	 */
	public static DirectNavigator getNavigator() {
		return navigator;
	}

	/**
	 * Gets the transitionManager.
	 *
	 * @return the transitionManager.
	 */
	public static SplashTransitionManager getTransitionManager() {
		return transitionManager;
	}

	/**
	 * Go back to the main page.
	 */
	public static void goToMainPage() {
		navigator.show(mainPage, false);
	}
}
