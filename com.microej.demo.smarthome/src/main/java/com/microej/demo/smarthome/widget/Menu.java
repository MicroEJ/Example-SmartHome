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
import ej.widget.container.Grid;
import ej.widget.listener.OnClickListener;

/**
 *
 */
public class Menu extends Grid {

	private MenuButton active;
	private final DirectNavigator navigator;

	/**
	 * @param navigator
	 */
	public Menu(DirectNavigator navigator) {
		super(false, 1);
		this.navigator = navigator;
	}

	@Override
	public void add(Widget widget) throws NullPointerException, IllegalArgumentException {
		throw new IllegalArgumentException();
	}

	public void add(final MenuButton button) throws NullPointerException, IllegalArgumentException {
		super.add(button);
		button.addOnClickListener(new OnClickListener() {

			@Override
			public void onClick() {
				setActive(button);
			}
		});

		if (active == null) {
			setActive(button);
		}
	}

	/**
	 * @param widget
	 */
	private synchronized void setActive(MenuButton button) {
		if (active != null) {
			active.setFocus(false);
		}
		active = button;
		active.setFocus(true);
	}

	/**
	 * @param menuPage
	 */
	public void show(MenuPage menuPage) {
		MenuButton button = menuPage.getMenuButton();
		boolean forward = false;

		for (Widget widget : getWidgets()) {
			if (widget == button) {
				forward = false;
				break;
			}
			if (widget == active) {
				forward = true;
				break;
			}
		}
		navigator.show(menuPage, forward);
	}
}
