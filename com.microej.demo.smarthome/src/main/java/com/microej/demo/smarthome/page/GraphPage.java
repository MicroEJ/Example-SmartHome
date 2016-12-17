/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
import ej.widget.navigation.page.Page;

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
		Power power = ServiceLoaderFactory.getServiceLoader().getService(Power.class);
		PowerWidget powerWidget = new PowerWidget(power);
		setWidget(powerWidget);

		listener = new TransitionListener() {

			@Override
			public void onTransitionStop() {
				if (isShown()) {
					powerWidget.startAnimation();
				}

			}

			@Override
			public void onTransitionStep(int step) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTransitionStart(int transitionsSteps, int transitionsStop, Page from, Page to) {
				if (isInHierarchy(GraphPage.this, to)) {
					powerWidget.reload();
				}
			}

		};
	}

	/**
	 * Creates the menu button
	 */
	@Override
	protected MenuButton createMenuButton() {
		MenuButton menuButton = new MenuButton(new Label(Strings.MAXPOWERTODAY));
		menuButton.addClassSelector(ClassSelectors.DASHBOARD_MENU_BUTTON);
		return menuButton;
	}


	@Override
	public void showNotify() {
		TransitionManager.addGlobalTransitionListener(listener);
		super.showNotify();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		TransitionManager.removeGlobalTransitionListener(listener);
	}

	private boolean isInHierarchy(Widget widget, Widget hierarchy) {
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
