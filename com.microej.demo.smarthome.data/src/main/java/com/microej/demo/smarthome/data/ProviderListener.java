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
public interface ProviderListener<T extends Device<?>> {
	void newElement(T element);

	void removeElement(T element);
}
