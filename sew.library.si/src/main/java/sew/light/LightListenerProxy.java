/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sew.light;

import ej.kf.Proxy;

/**
 *
 */
public class LightListenerProxy extends Proxy<LightListener> implements LightListener {

	@Override
	public void onLightUpdate(final Light light) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
