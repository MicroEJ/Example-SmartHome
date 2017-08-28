/*
 * Java
 *
 * Copyright 2015-2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import ej.giml.annotation.ElementChild;
import ej.mwt.Widget;
import ej.style.State;
import ej.widget.basic.Box;
import ej.widget.composed.ToggleWrapper;
import ej.widget.toggle.ToggleModel;

/**
 * A facility to use a toggle.
 * <p>
 * It is simply a toggle the box, based on a toggle model.
 *
 * @see ToggleModel
 */
public class ToggleBox extends ToggleWrapper {

	private final Box box;

	/**
	 * Creates a toggle with the given text to display. The text cannot be <code>null</code>.
	 * <p>
	 * The box state is updated along with the state of the toggle.
	 *
	 * @param box
	 *            the widget representing the state of the toggle.
	 * @see State#Checked
	 */
	public ToggleBox(final Box box) {
		this(new ToggleModel(), box);
	}

	/**
	 * Creates a toggle with the given text to display. The text cannot be <code>null</code>.
	 * <p>
	 * The box state is updated along with the state of the toggle.
	 *
	 * @param toggleModel
	 *            the toggle model.
	 * @param box
	 *            the widget representing the state of the toggle.
	 * @see State#Checked
	 */
	public ToggleBox(final ToggleModel toggleModel, final Box box) {
		super(toggleModel);
		this.box = box;
		add(box);
	}

	@Override
	@ElementChild(prefix = "setWidget", disabled = true)
	public void setWidget(final Widget widget) {
		throw new IllegalArgumentException();
	}

	@Override
	public void onStateChange(final boolean checked) {
		super.onStateChange(checked);
		if (this.box != null) {
			this.box.setChecked(checked);
		}
	}
}
