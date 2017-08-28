/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.light;

import com.microej.demo.smarthome.data.Device;

/**
 *
 */
public interface Light extends Device<LightEventListener> {

	int getColor();

	float getBrightness();

	boolean isOn();

	void setColor(int color);

	void setBrightness(float brightness);

	void switchOn(boolean on);
}
