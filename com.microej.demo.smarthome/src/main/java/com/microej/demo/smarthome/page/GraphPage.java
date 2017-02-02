/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.power.Power;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MenuButton;
import com.microej.demo.smarthome.widget.PowerWidget;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.mwt.Widget;
import ej.widget.basic.Label;
import ej.widget.navigation.TransitionListener;
import ej.widget.navigation.TransitionManager;

/**
 * This class represents the page which shows the power chart
 */
public class GraphPage extends MenuPage {

	private final TransitionListener listener;

	/**
	 * Constructor
	 */
	public GraphPage() {
		super();
		final Power power = ServiceLoaderFactory.getServiceLoader().getService(Power.class);
		final PowerWidget powerWidget = new PowerWidget(power);
		setWidget(powerWidget);

		listener = new TransitionListener() {

			@Override
			public void onTransitionStart(final TransitionManager transitionManager) {
				if (isInHierarchy(GraphPage.this, transitionManager.getTo())) {
					powerWidget.reload();
				}

			}

			@Override
			public void onTransitionStop(final TransitionManager manager) {
				if (isShown()) {
					powerWidget.startAnimation();
				}

			}

		};
	}

	/**
	 * Creates the menu button
	 */
	@Override
	protected MenuButton createMenuButton() {
		final MenuButton menuButton = new MenuButton(new Label(Strings.MAXPOWERTODAY));
		menuButton.addClassSelector(ClassSelectors.DASHBOARD_MENU_BUTTON);
		return menuButton;
	}


	@Override
	public void showNotify() {
		TransitionManager.addTransitionListener(listener);
		super.showNotify();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		TransitionManager.removeTransitionListener(listener);
	}

	private boolean isInHierarchy(Widget widget, final Widget hierarchy) {
		if (hierarchy == null) {
			return false;
		}
		while (widget != null) {
			if (hierarchy == widget) {
				return true;
			}
			widget = widget.getParent();
		}
		return false;
	}
}
