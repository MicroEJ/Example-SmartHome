/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.style.HomeImageLoader;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.Menu;
import com.microej.demo.smarthome.widget.TimeWidget;

import ej.mwt.MWT;
import ej.mwt.Widget;
import ej.widget.basic.Label;
import ej.widget.composed.ButtonImage;
import ej.widget.container.Dock;
import ej.widget.listener.OnClickListener;
import ej.widget.navigation.transition.PushScreenshotTransitionManager;

/**
 * The main page.
 */
public class SmartHomePage extends MenuNavigatorPage {

	private final static String[] pagesURL = {
			DashBoardPage.class.getName(),
			ThermostatPage.class.getName(),
			LightPage.class.getName(),
			DoorPage.class.getName() };

	private final Dock mainDock;

	private ButtonImage storeButton;

	/**
	 * Instantiates a SmartHomePage
	 */
	public SmartHomePage() {
		super(pagesURL);

		mainDock = new Dock();
		mainDock.addTop(createHeader());
		final Menu menu = initMenu();
		menu.addClassSelector(ClassSelectors.FOOTER);
		mainDock.addBottom(menu);
		mainDock.setCenter(createMainContent());

		this.setWidget(mainDock);
	}

	private Widget createMainContent() {
		getNavigator().addClassSelector(ClassSelectors.BODY);
		// create navigator
		final PushScreenshotTransitionManager manager = new PushScreenshotTransitionManager(MWT.LEFT);
		getNavigator().setTransitionManager(manager);

		manager.setDuration(300);

		return getNavigator();
	}

	private Widget createHeader() {
		final Dock dock = new Dock();
		dock.setCenter(new Label(Strings.SMARTHOME_TITLE));
		dock.addRight(new TimeWidget());
		storeButton = new ButtonImage(HomeImageLoader.getAbsolutePath(Images.STORE));
		dock.addLeft(storeButton);
		dock.addClassSelector(ClassSelectors.HEADER);
		return dock;
	}

	/**
	 * Add a listener for the Store button.
	 *
	 * @param onClickListener
	 *            the listener.
	 */
	public void addOnClickListener(final OnClickListener onClickListener) {
		storeButton.addOnClickListener(onClickListener);
	}
}
