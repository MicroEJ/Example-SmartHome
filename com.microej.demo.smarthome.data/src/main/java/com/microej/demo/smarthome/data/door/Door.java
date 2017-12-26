/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.door;

import com.microej.demo.smarthome.data.Device;

/**
 * A door.
 */
public interface Door extends Device<DoorEventListener> {

	/**
	 * Checks whether the door is open or closed.
	 * @return true if the door is open.
	 */
	boolean isOpen();

}
