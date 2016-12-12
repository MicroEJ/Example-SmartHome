/*
 * Java
 *
 * Copyright 2015 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.navigator;

import ej.widget.navigation.Navigator;
import ej.widget.navigation.page.Page;

/**
 * Manages the navigation between several pages.
 */
public class DirectNavigator extends Navigator {

	/**
	 * Shows a new page.
	 *
	 * @param url
	 *            the URL of the page to show.
	 * @param forward
	 *            <code>true</code> if going to a new page, <code>false</code> if going back in the stack of pages.
	 */
	@Override
	public void show(Page page, boolean forward) {
		if (this.getCurrentPage() != page) {
			super.show(page, forward);
		}
	}
}
