/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.data.Device;

import ej.widget.basic.Label;
import ej.widget.container.Dock;

/**
 * An abstract device widget.
 */
public abstract class DeviceWidget<D extends Device<?>> extends Dock {

	/**
	 * The model.
	 */
	protected final D model;

	/**
	 * Instantiates a DeviceWidget.
	 *
	 * @param model
	 *            the model of the widget.
	 */
	public DeviceWidget(final D model) {
		this.model = model;
		this.addTop(new Label(model.getName()));
	}
}
