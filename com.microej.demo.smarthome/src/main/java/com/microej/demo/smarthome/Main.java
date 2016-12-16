/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome;

import com.microej.demo.smarthome.page.SmartHomePage;
import com.microej.demo.smarthome.style.StylePopulator;

import ej.microui.MicroUI;
import ej.mwt.Desktop;
import ej.mwt.Panel;
import ej.widget.navigation.navigator.SimpleNavigator;

/**
 *
 */
public class Main {

	private static Desktop desktop;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MicroUI.start();

		StylePopulator.initializeStylesheet();

		SimpleNavigator navigator = new SimpleNavigator();
		navigator.show(SmartHomePage.class.getName(), true);

		desktop = new Desktop();
		Panel panel = new Panel();
		panel.setWidget(navigator);
		panel.show(desktop, true);
		desktop.show();
	}

	public static void showDesktop() {
		desktop.show();
	}

}
