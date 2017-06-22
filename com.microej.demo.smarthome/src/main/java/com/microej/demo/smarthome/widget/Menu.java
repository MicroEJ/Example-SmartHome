/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.page.MenuPage;

import ej.mwt.Widget;
import ej.widget.composed.ToggleWrapper;
import ej.widget.container.Grid;
import ej.widget.navigation.navigator.SimpleNavigator;
import ej.widget.toggle.ToggleGroup;

/**
 * A menu between pages.
 */
public class Menu extends Grid {

	private final SimpleNavigator navigator;
	private final ToggleGroup group;
	private ToggleWrapper currentButton;

	/**
	 * Instantiates a Menu.
	 *
	 * @param navigator
	 *            the navigator.
	 */
	public Menu(final SimpleNavigator navigator) {
		super(false, 1);
		group = new ToggleGroup();
		this.navigator = navigator;
	}

	@Override
	public void add(final Widget widget) throws NullPointerException, IllegalArgumentException {
		throw new IllegalArgumentException();
	}

	/**
	 * Adds a button.
	 *
	 * @param button
	 *            the button to add.
	 */
	public void add(final ToggleWrapper button) {
		if (getWidgetsCount() == 0) {
			currentButton = button;
			button.setChecked(true);
		}
		super.add(button);
		group.addToggle(button.getToggle());
	}

	/**
	 * Shows a page.
	 *
	 * @param page
	 *            the page to show.
	 */
	public void show(final MenuPage page) {
		final ToggleWrapper button = page.getMenuButton();
		boolean forward = false;

		if (button != currentButton) {
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
			navigator.show(page, forward);
		}
	}
}
