/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome;

import com.microej.demo.smarthome.page.SmartHomePage;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.style.StylePopulator;

import ej.automaton.Automaton;
import ej.automaton.impl.AutomatonManagerImpl;
import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.bon.Util;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.MicroUI;
import ej.mwt.Desktop;
import ej.mwt.Panel;
import ej.widget.listener.OnClickListener;
import ej.widget.navigation.navigator.SimpleNavigator;
import ej.widget.navigation.transition.CircleSplashTransitionManager;
import ej.widget.navigation.transition.SplashTransitionManager;

/**
 *
 */
public class Main {

	// The time used for the
	private static final long START_TIME = 1467324061000l;

	/**
	 * Delay before the robot starts.
	 */
	private static final int INACTIVITY = 40_000;
	private static Desktop desktop;
	private static OnClickListener onClickListener;
	private static AutomatonManagerImpl<Automaton> homeRobotManager;
	private static SplashTransitionManager transitionManager;
	private static SimpleNavigator navigator;
	private static SmartHomePage mainPage;
	private static Timer robotTimer;

	/**
	 * Entry point.
	 *
	 * @param args
	 *            not used.
	 */
	public static void main(final String[] args) {

		// Sets a default starting time to avoid 0:00.

		Util.setCurrentTimeMillis(START_TIME);
		ServiceLoaderFactory.getServiceLoader().getService(Timer.class).schedule(new TimerTask() {

			@Override
			public void run() {
				Thread.currentThread().setName("MainTimer");

			}
		}, 0);

		MicroUI.start();

		StylePopulator.initializeStylesheet();


		mainPage = new SmartHomePage();

		// Set clicklistener for home button (used in sandboxed).
		if (onClickListener != null) {
			mainPage.addOnClickListener(onClickListener);
		}

		// Create the main page.
		navigator = new SimpleNavigator();
		navigator.addClassSelector(ClassSelectors.PICKER);
		transitionManager = new CircleSplashTransitionManager();
		navigator.setTransitionManager(transitionManager);
		transitionManager.setDuration(200);
		navigator.show(mainPage, true);

		desktop = new Desktop();
		final Panel panel = new Panel();
		panel.setWidget(navigator);
		panel.showFullScreen(desktop);
		desktop.show();

		// Creates and arm the robot.
		robotTimer = new Timer();
		homeRobotManager = new AutomatonManagerImpl<Automaton>(robotTimer, new HomeRobot(), INACTIVITY);
		homeRobotManager.arm();
	}

	/**
	 * Gets the main desktop.
	 *
	 * @return
	 */
	public static Desktop getDesktop() {
		return desktop;
	}

	/**
	 * Sets the onClickListener for the store button. It needs to be set before the main is called.
	 *
	 * @param onClickListener
	 *            the onClickListener to set.
	 */
	public static void setOnClickListener(final OnClickListener onClickListener) {
		Main.onClickListener = onClickListener;
	}

	/**
	 * Stops the robot and its timer.
	 */
	public static void stopRobot() {
		if (homeRobotManager != null) {
			homeRobotManager.stop();
		}
		if (robotTimer != null) {
			robotTimer.cancel();
			robotTimer = null;
		}
	}

	/**
	 * Gets the navigator.
	 *
	 * @return the navigator.
	 */
	public static SimpleNavigator getNavigator() {
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
	public static void backToMainPage() {
		navigator.show(mainPage, false);
	}
}
