/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.widget.composed.ButtonWrapper;

/**
 *
 */
public class LimitedButtonWrapper extends ButtonWrapper {

	@Override
	public boolean handleEvent(int event) {
		if (Event.getType(event) == Event.POINTER) {
			Pointer pointer = (Pointer) Event.getGenerator(event);
			int x = this.getRelativeX(pointer.getAbsoluteX());
			int y = this.getRelativeY(pointer.getAbsoluteY());
			if (isInBound(x, y)) {
				super.handleEvent(event);
				return true;
			} else {
				return false;
			}
		}
		return super.handleEvent(event);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean isInBound(int x, int y) {
		return getWidget(0).contains(x, y);
	}


}
