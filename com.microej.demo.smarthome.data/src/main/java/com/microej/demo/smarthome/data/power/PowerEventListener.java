/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.power;

import com.microej.demo.smarthome.data.ElementListener;

/**
 *
 */
public interface PowerEventListener extends ElementListener {
	void onInstantPower(InstantPower instantPower);
}
