/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.navigator.DirectNavigator;
import com.microej.demo.smarthome.page.MenuPage;

import ej.mwt.Widget;
import ej.widget.composed.ToggleWrapper;
import ej.widget.container.Grid;
import ej.widget.toggle.ToggleGroup;

/**
 *
 */
public class Menu extends Grid {

	private final DirectNavigator navigator;
	private final ToggleGroup group;
	private ToggleWrapper currentButton;

	/**
	 * @param navigator
	 */
	public Menu(final DirectNavigator navigator) {
		super(false, 1);
		group = new ToggleGroup();
		this.navigator = navigator;
	}

	@Override
	public void add(final Widget widget) throws NullPointerException, IllegalArgumentException {
		throw new IllegalArgumentException();
	}

	public void add(final ToggleWrapper button) {
		if (getWidgetsCount() == 0) {
			currentButton = button;
			button.setChecked(true);
		}
		super.add(button);
		group.addToggle(button.getToggle());
	}

	/**
	 * @param menuPage
	 */
	public void show(final MenuPage menuPage) {
		final ToggleWrapper button = menuPage.getMenuButton();
		boolean forward = false;

		for (final Widget widget : getWidgets()) {
			if (widget == button) {
				forward = false;
				break;
			}
			if (widget == currentButton) {
				forward = true;
				break;
			}
		}
		currentButton = button;
		navigator.show(menuPage, forward);
	}
}
