/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome;

import com.microej.demo.smarthome.data.light.LightProvider;
import com.microej.demo.smarthome.page.SmartHomePage;
import com.microej.demo.smarthome.style.StylePopulator;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.MicroUI;
import ej.mwt.Desktop;
import ej.mwt.Panel;

/**
 *
 */
public class Main {

	private static SmartHomePage smartHomePage;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MicroUI.start();

		StylePopulator.initializeStylesheet();

		ServiceLoaderFactory.getServiceLoader().getService(LightProvider.class);

		Desktop desktop = new Desktop();
		Panel panel = new Panel();
		smartHomePage = new SmartHomePage();
		panel.setWidget(smartHomePage);
		panel.show(desktop, true);
		desktop.show();
	}
}
