/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.mwt.Widget;
import ej.style.State;
import ej.widget.composed.ButtonWrapper;

/**
 *
 */
public class MenuButton extends ButtonWrapper {

	private boolean focus = false;

	/**
	 * @param simpleName
	 */
	public MenuButton(Widget widget) {
		super();
		setWidget(widget);
	}

	public MenuButton() {
		super();
	}

	@Override
	public boolean isInState(State state) {
		if (state == State.Focus) {
			return focus;
		} else {
			return super.isInState(state);
		}
	}

	public void setFocus(boolean focus) {
		if (focus != this.focus) {
			this.focus = focus;
			updateStyle();
		}
	}
}
