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

import ej.mwt.MWT;
import ej.widget.composed.ToggleBox;
import ej.widget.composed.ToggleWrapper;
import ej.widget.container.Dock;
import ej.widget.container.transition.SlideScreenshotTransitionContainer;
import ej.widget.container.transition.TransitionContainer;
import ej.widget.toggle.RadioModel;

/**
 * Page presenting the dashboard and the graph.
 */
public class DashBoardPage extends MenuNavigatorPage {

	private static final String[] PAGES_URL = { InformationPage.class.getName(), GraphPage.class.getName() };

	/**
	 * Instantiates a dashboard page.
	 */
	public DashBoardPage() {
		super(PAGES_URL);

		final Dock mainDock = new Dock();
		mainDock.setCenter(getTransitionContainer());
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

	@Override
	protected TransitionContainer createTransitionContainer() {
		SlideScreenshotTransitionContainer slideTransitionContainer = new SlideScreenshotTransitionContainer(MWT.BOTTOM,
				true, false);
		return slideTransitionContainer;
	}
}