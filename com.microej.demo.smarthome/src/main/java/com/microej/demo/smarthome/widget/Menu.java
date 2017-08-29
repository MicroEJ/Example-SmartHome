/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.page.MenuPage;

import ej.mwt.Widget;
import ej.widget.composed.ToggleWrapper;
import ej.widget.container.Grid;
import ej.widget.container.transition.TransitionContainer;
import ej.widget.toggle.ToggleGroup;
import ej.widget.toggle.ToggleModel;

/**
 * A menu between pages.
 */
public class Menu extends Grid {

	private final TransitionContainer transitionContainer;
	private final ToggleGroup group;
	private ToggleWrapper currentButton;
	private MenuPage currentPage;

	/**
	 * Instantiates a Menu.
	 *
	 * @param transitionContainer
	 *            the navigator.
	 */
	public Menu(final TransitionContainer transitionContainer) {
		super(false, 1);
		this.group = new ToggleGroup();
		this.transitionContainer = transitionContainer;
	}

	@Override
	public void add(final Widget widget) throws NullPointerException, IllegalArgumentException {
		throw new IllegalArgumentException();
	}

	/**
	 * Adds a button.
	 *
	 * @param button
	 *            the button to add.
	 */
	public void add(final ToggleWrapper button) {
		ToggleModel toggle = button.getToggle();
		this.group.addToggle(toggle);
		if (this.currentButton == null) {
			this.currentButton = button;
			button.setChecked(true);
		}
		super.add(button);
	}

	/**
	 * Shows a page.
	 *
	 * @param page
	 *            the page to show.
	 */
	public void show(final MenuPage page) {
		final ToggleWrapper button = page.getMenuButton();
		boolean forward = false;

		if (button != this.currentButton) {
			for (final Widget widget : getWidgets()) {
				if (widget == button) {
					forward = false;
					break;
				}
				if (widget == this.currentButton) {
					forward = true;
					break;
				}
			}
			this.currentButton = button;
			this.currentPage = page;
			this.transitionContainer.show(page, forward);
		}
	}
	
	/**
	 * Goes to the next page.
	 * Used by the robot.
	 */
	public void goToNextPage(){
		final Widget[] buttons = getWidgets();
		for (int i = 0; i < buttons.length; i++) {
			ToggleWrapper button = (ToggleWrapper) buttons[i];
			if (button == this.currentButton) {
				button = (ToggleWrapper) buttons[(i + 1) % buttons.length];
				button.getToggle().toggle();
				break;
			}
		}
	}

	public MenuPage getCurrentPage() {
		return this.currentPage;
	}
}
