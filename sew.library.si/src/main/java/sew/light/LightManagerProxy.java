/*
 * Sébastien Eon 2016 / CC0-1.0
 */
package sew.light;

import ej.kf.Proxy;

public class LightManagerProxy extends Proxy<LightManager> implements LightManager {

	@Override
	public void addLightsListener(final LightsListener lightsListener) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void removeLightsListener(final LightsListener lightsListener) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void addLight(final Light light) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void removeLight(final Light light) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Light[] getLights() {
		try {
			return (Light[]) invokeRef();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addMessageListener(final MessageListener messageListener) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void removeMessageListener(final MessageListener messageListener) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void sendMessage(final String message) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void discardMessage(final String message) {
		try {
			invoke();
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
