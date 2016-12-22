/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data;

/**
 *
 */
public interface Device<EL extends ElementListener> {

	String getName();

	void addListener(EL listener);

	void removeListener(EL listener);
}
