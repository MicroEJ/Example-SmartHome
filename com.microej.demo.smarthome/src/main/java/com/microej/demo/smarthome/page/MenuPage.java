/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.navigator.DirectNavigator;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.MenuButton;

import ej.widget.listener.OnClickListener;
import ej.widget.navigation.page.Page;

/**
 *
 */
public abstract class MenuPage extends Page {

	private DirectNavigator navigator;
	private final MenuButton button;

	public MenuPage() {
		super();

		button = createMenuButton();
		if (button != null) {
			button.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
			button.addOnClickListener(new OnClickListener() {

				@Override
				public void onClick() {
					getNavigator().show(MenuPage.this, true);
				}
			});
		}
	}

	/**
	 * Gets the button.
	 *
	 * @return the button.
	 */
	public MenuButton getMenuButton() {
		return button;
	}

	public DirectNavigator getNavigator() {
		return navigator;
	}

	public void setNavigator(DirectNavigator navigator) {
		this.navigator = navigator;
	}

	/**
	 * @return
	 */
	protected abstract MenuButton createMenuButton();
}
