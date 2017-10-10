/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.door;


/**
 * A listener for door events.
 */
public interface DoorEventListener {

	/**
	 * Call-back function when the state changes.
	 * @param open true if the new state is open.
	 */
	void onStateChange(boolean open);

}
