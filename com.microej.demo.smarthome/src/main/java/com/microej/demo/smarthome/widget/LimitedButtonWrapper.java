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
 * A button that is pressed when its only child is pressed.
 */
public class LimitedButtonWrapper extends ButtonWrapper {

	@Override
	public boolean handleEvent(final int event) {
		if (Event.getType(event) == Event.POINTER) {
			final Pointer pointer = (Pointer) Event.getGenerator(event);
			final int x = this.getRelativeX(pointer.getAbsoluteX());
			final int y = this.getRelativeY(pointer.getAbsoluteY());
			if (getWidget(0).contains(x, y)) {
				super.handleEvent(event);
				return true;
			}
			return false;
		}
		return super.handleEvent(event);
	}
}
