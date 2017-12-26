/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.style.HomeImageLoader;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.Menu;
import com.microej.demo.smarthome.widget.TimeWidget;

import ej.mwt.MWT;
import ej.mwt.Widget;
import ej.style.image.DefaultImageLoader;
import ej.widget.basic.ButtonImage;
import ej.widget.basic.Label;
import ej.widget.container.Dock;
import ej.widget.container.transition.SlideScreenshotTransitionContainer;
import ej.widget.container.transition.TransitionContainer;
import ej.widget.listener.OnClickListener;

/**
 * The main page.
 */
public class SmartHomePage extends MenuNavigatorPage {

	private static final String[] PAGE_URL = { DashBoardPage.class.getName(), ThermostatPage.class.getName(),
			LightPage.class.getName(), DoorPage.class.getName() };

	private ButtonImage storeButton;

	/**
	 * Instantiates a SmartHomePage.
	 */
	public SmartHomePage() {
		super(PAGE_URL);
		Dock mainDock = new Dock();
		mainDock.addTop(createHeader());
		final Menu menu = initMenu();
		menu.addClassSelector(ClassSelectors.FOOTER);
		mainDock.addBottom(menu);
		mainDock.setCenter(createMainContent());

		this.setWidget(mainDock);
	}

	private Widget createMainContent() {
		getTransitionContainer().addClassSelector(ClassSelectors.BODY);
		return getTransitionContainer();
	}

	private Widget createHeader() {
		final Dock dock = new Dock();
		dock.setCenter(new Label(Strings.SMARTHOME_TITLE));
		dock.addRight(new TimeWidget());
		// Store image might not be in the path, disable the logger.
		Logger logger = Logger.getLogger(DefaultImageLoader.class.getName());
		Level level = logger.getLevel();
		logger.setLevel(Level.SEVERE);
		this.storeButton = new ButtonImage(HomeImageLoader.getAbsolutePath(Images.STORE));
		logger.setLevel(level);
		dock.addLeft(this.storeButton);
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
		this.storeButton.addOnClickListener(onClickListener);
	}

	@Override
	protected TransitionContainer createTransitionContainer() {
		return new SlideScreenshotTransitionContainer(MWT.LEFT, false, false);
	}
}
