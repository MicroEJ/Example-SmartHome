/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import java.util.ArrayList;

import com.microej.demo.smarthome.navigator.DirectNavigator;
import com.microej.demo.smarthome.widget.Menu;
import com.microej.demo.smarthome.widget.MenuButton;

import ej.widget.navigation.Navigator;
import ej.widget.navigation.page.ClassNameURLResolver;

/**
 *
 */
public class MenuNavigatorPage extends MenuPage {

	private java.util.List<MenuPage> pages;
	protected final DirectNavigator navigator;
	protected final Menu menu;

	/**
	 * @param pagesURL
	 */
	public MenuNavigatorPage(String[] pagesURL) {
		super();
		navigator = new DirectNavigator();
		menu = new Menu(navigator);
		initPages(pagesURL);

		navigator.show(pages.get(0), true);
	}

	/**
	 * @param pagesURL
	 *
	 */
	private void initPages(String[] pagesURL) {
		pages = new ArrayList<>();
		ClassNameURLResolver urlResolver = new ClassNameURLResolver();
		for (String pageName : pagesURL) {
			MenuPage p = (MenuPage) urlResolver.resolve(pageName);
			p.setMenu(menu);
			pages.add(p);
		}
	}

	/**
	 * @return
	 */
	protected Menu initMenu() {
		for (MenuPage page : pages) {
			menu.add(page.getMenuButton());
		}
		return menu;
	}

	@Override
	protected MenuButton createMenuButton() {
		return null;
	}

	/**
	 * @return
	 */
	public Navigator getNavigator() {
		return navigator;
	}

}
