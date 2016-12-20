/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ej.widget.basic;

import java.util.ArrayList;
import java.util.List;

import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.style.State;
import ej.widget.StyledWidget;
import ej.widget.listener.OnStateChangeListener;

/**
 * A box is a widget that can be checked or unchecked.
 */
public abstract class Box extends StyledWidget {

	private boolean checked;
	private final List<OnStateChangeListener> listeners = new ArrayList<>();

	/**
	 * Sets the state of the toggle.
	 *
	 * @param checked
	 *            the new state of the toggle.
	 */
	public void setChecked(boolean checked) {
		if (this.checked != checked) {
			this.checked = checked;
			// Do not update style if not shown.
			if (isShown()) {
				// Always repaint the widget even if the style has not changed.
				updateStyleOnly();
				repaint();
			}
			for (OnStateChangeListener listener : listeners) {
				listener.onStateChange(checked);
			}
		}

	}

	/**
	 * Gets whether or not the box is checked.
	 *
	 * @return <code>true</code> if the box is checked otherwise <code>false</code>.
	 */
	public boolean isChecked() {
		return this.checked;
	}

	@Override
	public boolean isInState(State state) {
		return (state == State.Checked && this.checked) || super.isInState(state);
	}

	/**
	 * @param listener
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addOnStateChangeListener(OnStateChangeListener listener) {
		return listeners.add(listener);
	}

	/**
	 * @param listener
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean removeOnStateChangeListener(OnStateChangeListener listener) {
		return listeners.remove(listener);
	}

	@Override
	public boolean handleEvent(int event) {
		if (isEnabled()) {
			if (Event.getType(event) == Event.POINTER) {
				if (Pointer.getAction(event) == Pointer.RELEASED) {
					setChecked(!checked);
				}
				return true;
			}
		}
		return super.handleEvent(event);
	}
}