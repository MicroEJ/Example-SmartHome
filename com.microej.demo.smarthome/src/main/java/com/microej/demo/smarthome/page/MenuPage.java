/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.Menu;

import ej.widget.composed.ToggleWrapper;
import ej.widget.listener.OnStateChangeListener;
import ej.widget.navigation.page.Page;

/**
 *
 */
public abstract class MenuPage extends Page {

	private final ToggleWrapper button;
	private Menu menu;

	public MenuPage() {
		super();

		button = createMenuButton();
		addClassSelector(ClassSelectors.BODY);
		if (button != null) {
			button.addOnStateChangeListener(new OnStateChangeListener() {

				@Override
				public void onStateChange(final boolean newState) {
					if(newState){
						menu.show(MenuPage.this);
					}
				}
			});
		}
	}

	/**
	 * Gets the button.
	 *
	 * @return the button.
	 */
	public ToggleWrapper getMenuButton() {
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
	public void setMenu(final Menu menu) {
		this.menu = menu;
	}

	/**
	 * @return
	 */
	protected abstract ToggleWrapper createMenuButton();
}
