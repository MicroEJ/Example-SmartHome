/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.data.ProviderListener;
import com.microej.demo.smarthome.data.light.DefaultLightProvider;
import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.data.light.LightProvider;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.widget.ImageMenuButton;
import com.microej.demo.smarthome.widget.light.LightWidget;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.mwt.Widget;
import ej.widget.composed.ToggleBox;
import ej.widget.composed.ToggleWrapper;
import ej.widget.container.Grid;
import ej.widget.toggle.RadioModel;

/**
 * A page displaying the lights devices.
 */
public class LightPage extends DevicePage<Light> implements ProviderListener<Light> {

	private Thread animationThread;
	private List<Light> lights;
	private boolean isOddShowNotify;

	/**
	 * Instantiates a LightPage.
	 */
	public LightPage() {
		this.lights = new ArrayList<>();
		final LightProvider provider = ServiceLoaderFactory.getServiceLoader().getService(LightProvider.class, DefaultLightProvider.class);
		provider.addListener(this);
		final Light[] list = provider.list();
		for (final Light light : list) {
			newElement(light);
		}

	}

	@Override
	protected ToggleWrapper createMenuButton() {
		final ImageMenuButton imageMenuButton = new ImageMenuButton(Images.LIGHTS);
		final ToggleBox toggleBox = new ToggleBox(new RadioModel(), imageMenuButton);
		toggleBox.addClassSelector(ClassSelectors.FOOTER_MENU_BUTTON);
		return toggleBox;
	}

	@Override
	public void newElement(final Light element) {
		this.lights.add(element);
		final LightWidget device = new LightWidget(element);
		device.resetAnimation();
		addDevice(element, device);
		if (isShown()) {
			revalidate();
			device.startAnimation();
		}
	}

	@Override
	public void removeElement(final Light element) {
		this.lights.remove(element);
		removeDevice(element);
	}

	@Override
	public void showNotify() {
		super.showNotify();

		if(this.isOddShowNotify){
			startAnimation();
		}
		this.isOddShowNotify = !this.isOddShowNotify;
	}

	@Override
	public void hideNotify() {
		stopAnimation();
		super.hideNotify();
		if(!this.isOddShowNotify){
				resetLight();
		}
	}

	private void resetLight() {
		Widget[] widgets = ((Grid) getWidget(0)).getWidgets();
		for (int i = 0; i < widgets.length; i++) {
			LightWidget widget = (LightWidget) widgets[i];
			widget.resetAnimation();
		}
	}

	private synchronized void startAnimation() {
		if (this.animationThread == null) {
			stopAnimation();
			final Widget[] widgets = ((Grid) getWidget(0)).getWidgets();
			final SequentialAnimation animation = new SequentialAnimation(widgets);
			this.animationThread = new Thread(animation);
			this.animationThread.start();
		}
	}

	private synchronized void stopAnimation() {
		if (this.animationThread != null) {
			this.animationThread.interrupt();
			this.animationThread = null;
		}
	}

	private class SequentialAnimation implements Runnable {

		private final Widget[] widgets;

		/**
		 * Instantiates a SequentialAnimation.
		 * @param widgets the widgets.
		 */
		SequentialAnimation(final Widget[] widgets) {
			this.widgets = widgets.clone();
		}

		@Override
		public void run() {
			for (int i = 0; i < this.widgets.length; i++) {
				final LightWidget lightWidget = (LightWidget) this.widgets[i];
				if (lightWidget.isEnabled()) {
					lightWidget.startAnimation();
					synchronized (lightWidget) {
						try {
							lightWidget.wait();
						} catch (final InterruptedException e) {
							// When interrupted, stop the thread.
							return;
						}
					}
				}
			}
			LightPage.this.animationThread = null;
		}
	}

	/**
	 * Counts the number of available lights.
	 * Used by the robot.
	 * @return the number of available lights.
	 */
	public int countLights() {
		return this.devicesMap.size();
	}

	/**
	 * Checks if a light is on.
	 * Used by the robot.
	 * @param lightId the light id.
	 * @return true if the light is on.
	 */
	public boolean isLightOn(int lightId) {
		return this.lights.get(lightId).isOn();
	}

	/**
	 * Sets the brightness of a light.
	 * Used by the robot.
	 * @param lightId the light id.
	 * @param brightness the brightness.
	 */
	public void setLightBrightness(int lightId, float brightness) {
		this.lights.get(lightId).setBrightness(brightness);

	}

	/**
	 * Switches on a light.
	 * Used by the robot.
	 * @param lightId the light id.
	 * @param on the new state.
	 */
	public void switchOn(int lightId, boolean on) {
		this.lights.get(lightId).switchOn(on);

	}

	/**
	 * Opens the color picker of a light.
	 * Used by the robot.
	 * @param lightId the light id.
	 */
	public void openColorPicker(int lightId) {
		LightWidget widget = (LightWidget) this.devicesMap.get(this.lights.get(lightId));
		widget.openColorPicker();
	}
}
