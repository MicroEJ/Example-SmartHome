/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.Menu;
import com.microej.demo.smarthome.widget.MenuButton;

import ej.widget.listener.OnClickListener;
import ej.widget.navigation.page.Page;

/**
 *
 */
public abstract class MenuPage extends Page {

	private final MenuButton button;
	private Menu menu;

	public MenuPage() {
		super();

		button = createMenuButton();
		addClassSelector(ClassSelectors.BODY);
		if (button != null) {
			button.addOnClickListener(new OnClickListener() {

				@Override
				public void onClick() {
					menu.show(MenuPage.this);
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

	/**
	 * Gets the menu.
	 *
	 * @return the menu.
	 */
	public Menu getMenu() {
		return menu;
	}

	/**
	 * Sets the menu.
	 *
	 * @param menu
	 *            the menu to set.
	 */
	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	/**
	 * @return
	 */
	protected abstract MenuButton createMenuButton();
}
