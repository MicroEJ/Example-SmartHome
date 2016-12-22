/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package ej.widget.navigation.transition;

import ej.widget.navigation.TransitionManager;
import ej.widget.navigation.page.Page;

/**
 *
 */
public class NoTransitionManager extends TransitionManager {

	@Override
	protected void animate(Page oldPage, Page newPage, boolean forward) {
		showPage(oldPage, newPage);

	}

}
