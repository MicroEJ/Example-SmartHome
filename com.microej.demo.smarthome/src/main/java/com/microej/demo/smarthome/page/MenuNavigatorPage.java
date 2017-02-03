/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.navigator.DirectNavigator;
import com.microej.demo.smarthome.widget.Menu;

import ej.widget.composed.ToggleWrapper;
import ej.widget.navigation.Navigator;
import ej.widget.navigation.page.ClassNameURLResolver;

/**
 *
 */
public class MenuNavigatorPage extends MenuPage {

	private MenuPage[] pages;
	protected final DirectNavigator navigator;
	protected final Menu menu;

	/**
	 * @param pagesURL
	 */
	public MenuNavigatorPage(final String[] pagesURL) {
		super();
		navigator = new DirectNavigator();
		menu = new Menu(navigator);
		initPages(pagesURL);

		navigator.show(pages[0], true);
	}

	/**
	 * @param pagesURL
	 *
	 */
	private void initPages(final String[] pagesURL) {
		pages = new MenuPage[pagesURL.length];
		final ClassNameURLResolver urlResolver = new ClassNameURLResolver();
		for (int i = 0; i < pagesURL.length; i++) {
			final String pageName = pagesURL[i];
			final MenuPage p = (MenuPage) urlResolver.resolve(pageName);
			p.setMenu(menu);
			pages[i] = p;
		}
	}

	/**
	 * @return
	 */
	protected Menu initMenu() {
		for (final MenuPage page : pages) {
			menu.add(page.getMenuButton());
		}
		return menu;
	}

	@Override
	protected ToggleWrapper createMenuButton() {
		return null;
	}

	/**
	 * @return
	 */
	public Navigator getNavigator() {
		return navigator;
	}

}
