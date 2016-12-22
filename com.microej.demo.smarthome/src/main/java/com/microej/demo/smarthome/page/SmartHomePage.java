/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.Menu;
import com.microej.demo.smarthome.widget.OverlapingComposite;
import com.microej.demo.smarthome.widget.TimeWidget;

import ej.mwt.Widget;
import ej.widget.basic.Label;
import ej.widget.container.Dock;
import ej.widget.navigation.page.Page;
import ej.widget.navigation.transition.HorizontalScreenshotTransitionManager;

/**
 *
 */
public class SmartHomePage extends MenuNavigatorPage {

	private final static String[] pagesURL = {
			DashBoardPage.class.getName(),
			ThermostatPage.class.getName(),
			LightPage.class.getName(),
			DoorPage.class.getName() };

	private final Dock mainDock;

	/**
	 *
	 */
	public SmartHomePage() {
		super(pagesURL);

		mainDock = new Dock();
		mainDock.addTop(createHeader());
		Menu menu = initMenu();
		menu.addClassSelector(ClassSelectors.FOOTER);
		mainDock.addBottom(menu);
		mainDock.setCenter(createMainContent());

		this.setWidget(mainDock);
	}


	/**
	 * @return
	 */
	private Widget createMainContent() {
		navigator.addClassSelector(ClassSelectors.BODY);
		// create navigator
		// navigator.setTransitionManager(new HorizontalTransitionManager() {
		navigator.setTransitionManager(new HorizontalScreenshotTransitionManager() {
			@Override
			protected void setCurrentPage(Page newPage) {
				super.setCurrentPage(newPage);
			}
		});

		return navigator;
	}

	/**
	 * @return
	 */
	private Widget createHeader() {
		OverlapingComposite dock = new OverlapingComposite();
		dock.add(new Label(Strings.SMARTHOME_TITLE));
		dock.add(new TimeWidget());
		dock.addClassSelector(ClassSelectors.HEADER);
		return dock;
	}


}
