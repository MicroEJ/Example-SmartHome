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
public interface Provider<T extends Device<?>> {
	T[] list();

	void addListener(ProviderListener<T> listener);

	void removeListener(ProviderListener<T> listener);
}
