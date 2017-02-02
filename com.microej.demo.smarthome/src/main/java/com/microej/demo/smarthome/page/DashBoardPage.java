/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.ImageMenuButton;
import com.microej.demo.smarthome.widget.Menu;
import com.microej.demo.smarthome.widget.MenuButton;

import ej.mwt.MWT;
import ej.widget.container.Dock;
import ej.widget.navigation.TransitionManager;
import ej.widget.navigation.transition.OverlapScreenshotTransitionManager;

/**
 *
 */
public class DashBoardPage extends MenuNavigatorPage {

	private static final String[] pagesURL = { InformationPage.class.getName(), GraphPage.class.getName() };

	/**
	 *
	 */
	public DashBoardPage() {
		super(pagesURL);

		final Dock mainDock = new Dock();
		final TransitionManager manager = new OverlapScreenshotTransitionManager(MWT.BOTTOM);
		manager.setDuration(300);
		navigator.setTransitionManager(manager);

		mainDock.setCenter(navigator);
		final Menu menu = initMenu();
		menu.addClassSelector(ClassSelectors.DASHBOARD_MENU);
		mainDock.addTop(menu);
		setWidget(mainDock);

	}

	@Override
	protected MenuButton createMenuButton() {
		final ImageMenuButton imageMenuButton = new ImageMenuButton(Images.DASHBOARD);
		imageMenuButton.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return imageMenuButton;
	}
}