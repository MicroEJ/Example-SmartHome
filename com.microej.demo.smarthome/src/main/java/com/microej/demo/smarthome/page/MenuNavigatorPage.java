/*
 * Java
 *
 * Copyright 2016-2018 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.widget.Menu;

import ej.widget.composed.ToggleWrapper;
import ej.widget.container.transition.TransitionContainer;
import ej.widget.navigation.page.ClassNameURLResolver;

/**
 * Abstract page providing a navigator and a menu.
 */
public abstract class MenuNavigatorPage extends MenuPage {

	private MenuPage[] pages;
	private final TransitionContainer transitionContainer;
	private final Menu menu;

	/**
	 * Instantiates the page.
	 *
	 * @param pagesURL
	 *            url of the pages to display.
	 */
	public MenuNavigatorPage(final String[] pagesURL) {
		super();
		this.transitionContainer = createTransitionContainer();
		this.menu = new Menu(this.transitionContainer);
		initPages(pagesURL);

		this.transitionContainer.show(this.pages[0], true);
	}

	private void initPages(final String[] pagesURL) {
		this.pages = new MenuPage[pagesURL.length];
		final ClassNameURLResolver urlResolver = new ClassNameURLResolver();
		for (int i = 0; i < pagesURL.length; i++) {
			final String pageName = pagesURL[i];
			final MenuPage p = (MenuPage) urlResolver.resolve(pageName);
			p.setMenu(this.menu);
			this.pages[i] = p;
		}
	}

	/**
	 * Gets a page using its simple class name
	 *
	 * @param pageName
	 *            the simple class name of the page
	 *
	 * @return the corresponding page
	 */
	public MenuPage getPage(String pageName) {
		for (MenuPage page : this.pages) {
			if (pageName.equals(page.getCurrentURL())) {
				return page;
			}
		}
		return null;
	}

	/**
	 * Initialize the menu.
	 *
	 * @return The menu.
	 */
	protected Menu initMenu() {
		for (final MenuPage page : this.pages) {
			this.menu.add(page.getMenuButton());
		}
		return this.menu;
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
	public TransitionContainer getTransitionContainer() {
		return this.transitionContainer;
	}

	/**
	 * Creates the transition container.
	 * @return the transition container.
	 */
	protected abstract TransitionContainer createTransitionContainer();

	/**
	 * Gets the current shownPage.
	 * Used by the robot.
	 * @return the current shownPage.
	 */
	public MenuPage getCurrentPage() {
		MenuPage page = this.menu.getCurrentPage();
		if(page==null){
			return this.pages[0];
		}
		return page;
	}
}
