/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.light;

import com.microej.demo.smarthome.data.Provider;

/**
 * Provides a list of registered lights.
 */
public interface LightProvider extends Provider<Light> {
	// Used for service loader.
}
