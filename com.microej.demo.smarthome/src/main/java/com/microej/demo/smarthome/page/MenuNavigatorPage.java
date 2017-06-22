/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.widget.Menu;

import ej.widget.composed.ToggleWrapper;
import ej.widget.navigation.navigator.SimpleNavigator;
import ej.widget.navigation.page.ClassNameURLResolver;

/**
 * Abstract page providing a navigator and a menu.
 */
public class MenuNavigatorPage extends MenuPage {

	private MenuPage[] pages;
	private final SimpleNavigator navigator;
	private final Menu menu;

	/**
	 * Instantiates the page.
	 *
	 * @param pagesURL
	 *            url of the pages to display.
	 */
	public MenuNavigatorPage(final String[] pagesURL) {
		super();
		navigator = new SimpleNavigator();
		menu = new Menu(navigator);
		initPages(pagesURL);

		getNavigator().show(pages[0], true);
	}

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
	 * Initialize the menu.
	 *
	 * @return The menu.
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
	 * Gets the navigator of the page.
	 * 
	 * @return the navigator.
	 */
	public SimpleNavigator getNavigator() {
		return navigator;
	}

}
