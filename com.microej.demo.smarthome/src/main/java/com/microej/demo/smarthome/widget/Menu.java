/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.mwt.Widget;
import ej.widget.container.List;
import ej.widget.listener.OnClickListener;

/**
 *
 */
public class Menu extends List {

	private MenuButton active;

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
}
