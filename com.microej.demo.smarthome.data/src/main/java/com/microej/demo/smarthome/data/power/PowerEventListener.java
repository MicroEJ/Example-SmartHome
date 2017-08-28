/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.power;

import com.microej.demo.smarthome.data.ElementListener;

/**
 *
 */
public interface PowerEventListener extends ElementListener {
	void onInstantPower(InstantPower instantPower);
}
