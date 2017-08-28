/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.light;

import com.microej.demo.smarthome.data.ElementListener;

/**
 *
 */
public interface LightEventListener extends ElementListener {
	void onColorChange(int color);

	void onBrightnessChange(float brightness);

	/**
	 * @param on
	 */
	void onStateChange(boolean on);
}
