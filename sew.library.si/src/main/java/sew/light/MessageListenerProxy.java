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
public class MessageListenerProxy extends Proxy<MessageListener> implements MessageListener {

	@Override
	public void onNewMessage(final String message) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onDiscardMessage(final String message) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
