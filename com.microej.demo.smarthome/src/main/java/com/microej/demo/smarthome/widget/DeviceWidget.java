/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.data.Device;

import ej.widget.basic.Label;
import ej.widget.container.Dock;

/**
 *
 */
public class DeviceWidget<D extends Device<?>> extends Dock {

	protected final D model;

	/**
	 * @param model
	 */
	public DeviceWidget(D model) {
		this.model = model;
		this.addBottom(new Label(model.getName()));
	}


}
