/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MenuButton;

import ej.widget.basic.Label;

/**
 *
 */
public class GraphPage extends MenuPage {

	/**
	 *
	 */
	public GraphPage() {
		setWidget(new Label("Graph"));
	}

	@Override
	protected MenuButton createMenuButton() {
		MenuButton menuButton = new MenuButton(new Label(Strings.MAXPOWERTODAY));
		menuButton.addClassSelector(ClassSelectors.DASHBOARD_MENU_BUTTON);
		return menuButton;
	}


}
