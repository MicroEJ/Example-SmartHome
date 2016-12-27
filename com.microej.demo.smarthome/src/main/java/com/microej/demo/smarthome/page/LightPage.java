/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.data.ProviderListener;
import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.data.light.LightProvider;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.ImageMenuButton;
import com.microej.demo.smarthome.widget.MenuButton;
import com.microej.demo.smarthome.widget.light.LightWidget;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.mwt.Widget;
import ej.widget.container.Grid;
import ej.widget.navigation.TransitionListener;
import ej.widget.navigation.TransitionManager;
import ej.widget.navigation.page.Page;

/**
 *
 */
public class LightPage extends DevicePage<Light> implements ProviderListener<Light> {

	private final TransitionListener transitionListener;
	private Thread animationThread;

	/**
	 *
	 */
	public LightPage() {
		final LightProvider provider = ServiceLoaderFactory.getServiceLoader().getService(LightProvider.class);
		provider.addListener(this);
		final Light[] list = provider.list();
		for (final Light light : list) {
			newElement(light);
		}

		transitionListener = new TransitionListener() {

			@Override
			public void onTransitionStop() {
				if (isShown() && devicesMap.size() > 0) {
					startAnimation();
				}
			}

			@Override
			public void onTransitionStep(final int step) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTransitionStart(final int transitionsSteps, final int transitionsStop, final Page from,
					final Page to) {
				if (devicesMap.size() > 0) {
					stopAnimation();
				}
			}
		};
	}

	@Override
	protected MenuButton createMenuButton() {
		final ImageMenuButton imageMenuButton = new ImageMenuButton(Images.LIGHTS);
		imageMenuButton.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return imageMenuButton;
	}

	@Override
	public void newElement(final Light element) {
		final LightWidget device = new LightWidget(element);
		addDevice(element, device);
		if (isShown()) {
			revalidate();
			device.startAnimation();
		}
	}

	@Override
	public void removeElement(final Light element) {
		removeDevice(element);
	}

	@Override
	public void showNotify() {
		transitionListener.onTransitionStart(0, 0, null, null);
		TransitionManager.addGlobalTransitionListener(transitionListener);
		super.showNotify();
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		transitionListener.onTransitionStart(0, 0, null, null);
		TransitionManager.removeGlobalTransitionListener(transitionListener);
	}

	private synchronized void startAnimation() {
		if (animationThread == null) {
			stopAnimation();
			final Widget[] widgets = ((Grid) getWidget(0)).getWidgets();
			final SequentialAnimation animation = new SequentialAnimation(widgets);
			animationThread = new Thread(animation);
			animationThread.start();
		}


	}

	private synchronized void stopAnimation() {
		for (final Widget widget : devicesMap.values()) {
			final LightWidget lightWidget = (LightWidget) widget;
			lightWidget.stopAnimation();
		}
		if (animationThread != null) {
			animationThread.interrupt();
			animationThread = null;
		}
	}

	private class SequentialAnimation implements Runnable {

		private final Widget[] widgets;

		/**
		 * @param widgets
		 */
		public SequentialAnimation(final Widget[] widgets) {
			this.widgets = widgets;
		}

		@Override
		public void run() {
			for (int i = 0; i < widgets.length; i++) {
				final LightWidget lightWidget = (LightWidget) widgets[i];
				if (lightWidget.isEnabled()) {
					lightWidget.startAnimation();
					synchronized (lightWidget) {
						try {
							lightWidget.wait();
						} catch (final InterruptedException e) {
							return;
						}
					}
				}
			}
			animationThread = null;
		}
	}
}
