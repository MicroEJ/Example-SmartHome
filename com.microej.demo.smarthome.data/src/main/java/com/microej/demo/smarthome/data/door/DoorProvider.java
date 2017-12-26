/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.door;

import com.microej.demo.smarthome.data.Provider;

/**
 * Provides a list of registered doors.
 */
public interface DoorProvider extends Provider<Door> {
	// Used for service loader.
}
