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

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.bon.Util;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.MicroUI;
import ej.mwt.Desktop;
import ej.mwt.Panel;
import ej.widget.container.transition.CircleSplashScreenshotTransitionContainer;
import ej.widget.container.transition.SplashScreenshotTransitionContainer;
import ej.widget.listener.OnClickListener;
import ej.widget.navigation.page.Page;

/**
 * The entry point of smarthome.
 */
public class Main {

	private static final int ANIMATION_DURATION = 200;

	// The time use at boot time.
	private static final long START_TIME = 1467324061000l;

	private static OnClickListener ON_CLICK_LISTENER;
	private static SplashScreenshotTransitionContainer TRANSITION_CONTAINER;
	private static SmartHomePage MAIN_PAGE;

	private static HomeRobot AUTOMATON;

	private static Page CURRENT_PAGE;

	
	private Main(){
		// Avoid instantiation.
	}
	
	/**
	 * Entry point.
	 *
	 * @param args
	 *            not used.
	 */
	public static void main(final String[] args) {

		// Sets a default starting time to avoid 0:00.
		if (System.currentTimeMillis() < START_TIME) {
			Util.setCurrentTimeMillis(START_TIME);
		}

		ServiceLoaderFactory.getServiceLoader().getService(Timer.class, Timer.class).schedule(new TimerTask() {

			@Override
			public void run() {
				Thread.currentThread().setName("MainTimer"); //$NON-NLS-1$

			}
		}, 0);

		MicroUI.start();

		StylePopulator.initializeStylesheet();

		MAIN_PAGE = new SmartHomePage();

		// Set clicklistener for home button (used in sandboxed).
		if (ON_CLICK_LISTENER != null) {
			MAIN_PAGE.addOnClickListener(ON_CLICK_LISTENER);
		}

		// Create the main page.
		TRANSITION_CONTAINER = new CircleSplashScreenshotTransitionContainer(true);
		TRANSITION_CONTAINER.addClassSelector(ClassSelectors.PICKER);
		TRANSITION_CONTAINER.setDuration(ANIMATION_DURATION);
		backToMainPage();

		Desktop desktop = new Desktop();
		final Panel panel = new Panel();
		panel.setWidget(TRANSITION_CONTAINER);
		panel.showFullScreen(desktop);
		desktop.show();

		// Creates and arm the robot.
		AUTOMATON = new HomeRobot();
		AUTOMATON.arm();
	}

	/**
	 * Sets the onClickListener for the store button. It needs to be set before the main is called.
	 *
	 * @param onClickListener
	 *            the onClickListener to set.
	 */
	public static void setOnClickListener(final OnClickListener onClickListener) {
		Main.ON_CLICK_LISTENER = onClickListener;
	}

	/**
	 * Stops the robot and its timer.
	 */
	public static void stopRobot() {
		AUTOMATON.stop();
	}

//	/**
//	 * Gets the navigator.
//	 *
//	 * @return the navigator.
//	 */
//	public static SplashScreenshotTransitionContainer getTransitionContainer() {
//		return TRANSITION_CONTAINER;
//	}

	/**
	 * Go back to the main page.
	 */
	public static void backToMainPage() {
		CURRENT_PAGE = MAIN_PAGE;
		TRANSITION_CONTAINER.show(MAIN_PAGE, false);
	}

	public static Page getCurrentPage() {
		Page page = CURRENT_PAGE;
		if(page == MAIN_PAGE){
			return MAIN_PAGE.getCurrentPage();
		}
		return page;
	}

	public static void SetAnchor(int sourceX, int sourceY) {
		TRANSITION_CONTAINER.setAnchor(sourceX, sourceY);
	}

	public static void Show(Page page, boolean forward) {
		CURRENT_PAGE = page;
		TRANSITION_CONTAINER.show(page, forward);
	}
}
