/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.light;

import com.microej.demo.smarthome.Main;
import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.data.light.LightEventListener;
import com.microej.demo.smarthome.page.ColorPickerPage;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.DeviceWidget;
import com.microej.demo.smarthome.widget.LimitedButtonWrapper;
import com.microej.demo.smarthome.widget.OnAnimationEndListener;
import com.microej.demo.smarthome.widget.OverlapComposite;

import ej.widget.basic.image.ImageSwitch;
import ej.widget.composed.ButtonWrapper;
import ej.widget.composed.ToggleWrapper;
import ej.widget.listener.OnClickListener;
import ej.widget.listener.OnStateChangeListener;
import ej.widget.listener.OnValueChangeListener;
import ej.widget.model.BoundedRangeModel;
import ej.widget.model.DefaultBoundedRangeModel;
import ej.widget.toggle.ToggleModel;

/**
 * A widget displaying the state of a light.
 */
public class LightWidget extends DeviceWidget<Light> implements OnStateChangeListener, OnAnimationEndListener, LightEventListener, OnValueChangeListener {

	/**
	 * Attributes.
	 */
	private final LightCircularProgress progress;
	private final ImageSwitch switchButton;
	private boolean isChangingState;
	private ButtonWrapper circularButton;

	/**
	 * Instantiates a LightWidget.
	 *
	 * @param light
	 *            the model.
	 */
	public LightWidget(final Light light) {
		super(light);
		
		addClassSelector(ClassSelectors.LIGHT_WIDGET);

		final OverlapComposite overlapingComposite = new OverlapComposite();
		// circular progress
		final BoundedRangeModel boundedRange = new DefaultBoundedRangeModel(0, 1000, 0);
		this.progress = new LightCircularProgress(boundedRange, light);
		this.progress.resetAnimation();

		this.progress.addClassSelector(ClassSelectors.LIGHT_PROGRESS);

		overlapingComposite.add(this.progress);

		this.circularButton = new LimitedButtonWrapper();
		final LightCircleWidget lightCircleWidget = new LightCircleWidget(light);
		this.circularButton.setWidget(lightCircleWidget);
		this.circularButton.addOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				openColorPicker();
			}
		});

		lightCircleWidget.addClassSelector(ClassSelectors.LIGHT_PROGRESS);
		overlapingComposite.add(this.circularButton);
		setCenter(overlapingComposite);

		// toggle button
		final ToggleModel toggle = new ToggleModel(light.isOn());
		toggle.addOnStateChangeListener(this);
		final ToggleWrapper wrapper = new ToggleWrapper(toggle);
		this.switchButton = new ImageSwitch();
		this.switchButton.setChecked(light.isOn());
		wrapper.addClassSelector(ClassSelectors.LIGHT_PROGRESS);
		wrapper.setWidget(this.switchButton);

		// place widgets
		addBottom(wrapper);
	}

	@Override
	public boolean isEnabled() {
		return this.model.isOn();
	}

	/**
	 * Handle state change event.
	 */
	@Override
	public synchronized void onStateChange(final boolean on) {
		if (!this.isChangingState && (on != this.model.isOn() || this.switchButton.isChecked()!=on)) {
			this.isChangingState = true;
			if (on) {
				this.progress.resetAnimation();
				this.progress.startAnimation();
			}
			this.switchButton.setChecked(on);
			this.model.switchOn(on);
			this.circularButton.setEnabled(on);
		}
		this.isChangingState = false;
	}

	/**
	 * Changes light color.
	 */
	// Public to provide access to robot.
	public void openColorPicker() {
		// calculate animation source position
		final int sourceX = this.progress.getAbsoluteX() + this.progress.getWidth() / 2;
		final int sourceY = this.progress.getAbsoluteY() + this.progress.getHeight() / 2;

		// create color picker

		Main.SetAnchor(sourceX, sourceY);

		// create dialog
		final ColorPickerPage page = new ColorPickerPage(this.model);
		Main.Show(page, true);
	}

	/**
	 * Starts the animation.
	 */
	public void startAnimation() {
		this.progress.startAnimation();
	}

	/**
	 * Resets the animation.
	 */
	public void resetAnimation() {
		this.progress.resetAnimation();
	}

	/**
	 * Stops the animation.
	 */
	public void stopAnimation() {
		this.progress.stopAnimation();
	}

	@Override
	public void onAnimationEnd() {
		synchronized (this) {
			notify();
		}
	}
	
	@Override
	public void showNotify() {
		this.model.addListener(this);
		this.progress.addOnValueChangeListener(this);
		this.progress.setOnAnimationEndListener(this);
		boolean on = this.model.isOn();
		this.switchButton.setChecked(on);
		this.model.switchOn(on);
		
		super.showNotify();
	}
	
	@Override
	public void hideNotify() {
		this.model.removeListener(this);
		this.progress.removeOnValueChangeListener(this);
		this.progress.setOnAnimationEndListener(null);
		super.hideNotify();
	}

	@Override
	public void onColorChange(int color) {
		// Nothing to do.
		
	}

	@Override
	public void onBrightnessChange(float brightness) {
		this.progress.setValue((int)((this.progress.getMaximum()-this.progress.getMinimum())*brightness+this.progress.getMinimum()));
	}

	@Override
	public void onValueChange(int newValue) {
		this.model.setBrightness(LightWidget.this.progress.getPercentComplete());
		
	}

	@Override
	public void onMaximumValueChange(int newMaximum) {
		// Nothing to do.
		
	}

	@Override
	public void onMinimumValueChange(int newMinimum) {
		// Nothing to do.
		
	}
	
}
