/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.ImageMenuButton;
import com.microej.demo.smarthome.widget.Menu;
import com.microej.demo.smarthome.widget.MenuButton;

import ej.widget.container.Dock;
import ej.widget.navigation.transition.NoTransitionManager;

/**
 *
 */
public class DashBoard extends MenuNavigatorPage {

	private static final String[] pagesURL = { InformationPage.class.getName(), GraphPage.class.getName() };

	/**
	 *
	 */
	public DashBoard() {
		super(pagesURL);

		Dock mainDock = new Dock();
		NoTransitionManager manager = new NoTransitionManager();
		navigator.setTransitionManager(manager);

		mainDock.setCenter(navigator);
		Menu menu = initMenu();
		menu.addClassSelector(ClassSelectors.DASHBOARD_MENU);
		mainDock.addTop(menu);
		setWidget(mainDock);

	}

	@Override
	protected MenuButton createMenuButton() {
		ImageMenuButton imageMenuButton = new ImageMenuButton(Images.DASHBOARD);
		imageMenuButton.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return imageMenuButton;
	}
}