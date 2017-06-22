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
import com.microej.demo.smarthome.widget.ToggleBox;

import ej.mwt.MWT;
import ej.widget.composed.ToggleWrapper;
import ej.widget.container.Dock;
import ej.widget.navigation.TransitionManager;
import ej.widget.navigation.transition.OverlapScreenshotTransitionManager;
import ej.widget.toggle.RadioModel;

/**
 * Page presenting the dashboard and the graph.
 */
public class DashBoardPage extends MenuNavigatorPage {

	private static final String[] pagesURL = { InformationPage.class.getName(), GraphPage.class.getName() };


	public DashBoardPage() {
		super(pagesURL);

		final Dock mainDock = new Dock();
		final TransitionManager manager = new OverlapScreenshotTransitionManager(MWT.BOTTOM);
		manager.setDuration(300);
		getNavigator().setTransitionManager(manager);

		mainDock.setCenter(getNavigator());
		final Menu menu = initMenu();
		menu.addClassSelector(ClassSelectors.DASHBOARD_MENU);
		mainDock.addTop(menu);
		setWidget(mainDock);

	}

	@Override
	protected ToggleWrapper createMenuButton() {
		final ImageMenuButton imageMenuButton = new ImageMenuButton(Images.DASHBOARD);
		final ToggleBox toggleBox = new ToggleBox(new RadioModel(), imageMenuButton);
		toggleBox.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return toggleBox;
	}
}