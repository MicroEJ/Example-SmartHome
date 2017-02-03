/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.light;

import com.microej.demo.smarthome.Main;
import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.page.ColorPickerPage;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.ColorPicker;
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
 *
 */
public class LightWidget extends DeviceWidget<Light>
implements OnStateChangeListener, OnAnimationEndListener {

	/**
	 * Attributes
	 */
	private final LightCircularProgress progress;
	private final ImageSwitch switchButton;

	/**
	 * Constructor
	 */
	public LightWidget(final Light light) {
		super(light);
		addClassSelector(ClassSelectors.LIGHT_WIDGET);

		final OverlapComposite overlapingComposite = new OverlapComposite();
		// circular progress
		final BoundedRangeModel boundedRange = new DefaultBoundedRangeModel(0, 1000, 0);
		progress = new LightCircularProgress(boundedRange, light);
		progress.resetAnimation();

		progress.addOnValueChangeListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(final int newValue) {
				light.setBrightness(progress.getPercentComplete());

			}

			@Override
			public void onMinimumValueChange(final int newMinimum) {
			}

			@Override
			public void onMaximumValueChange(final int newMaximum) {
			}
		});

		progress.addClassSelector(ClassSelectors.LIGHT_PROGRESS);
		progress.setOnAnimationEndListener(this);

		overlapingComposite.add(progress);


		final ButtonWrapper circularButton = new LimitedButtonWrapper();
		final LightCircleWidget lightCircleWidget = new LightCircleWidget(light);
		circularButton.setWidget(lightCircleWidget);
		circularButton.addOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				openColorPicker();
			}
		});

		lightCircleWidget.addClassSelector(ClassSelectors.LIGHT_PROGRESS);
		overlapingComposite.add(circularButton);
		setCenter(overlapingComposite);

		// toggle button
		final ToggleModel toggle = new ToggleModel(light.isOn());
		toggle.addOnStateChangeListener(this);
		final ToggleWrapper wrapper = new ToggleWrapper(toggle);
		switchButton = new ImageSwitch();
		switchButton.setChecked(light.isOn());
		wrapper.addClassSelector(ClassSelectors.LIGHT_PROGRESS);
		wrapper.setWidget(switchButton);

		// place widgets
		addBottom(wrapper);
	}

	@Override
	public boolean isEnabled() {
		return model.isOn();
	}

	/**
	 * Handle state change event
	 */
	@Override
	public void onStateChange(final boolean on) {
		if (on != model.isOn()) {
			if (on) {
				progress.resetAnimation();
				progress.startAnimation();
			}
			switchButton.setChecked(on);
			model.switchOn(on);
		}
	}

	/**
	 * Changes light color
	 */
	private void openColorPicker() {
		// calculate animation source position
		final int sourceX = progress.getAbsoluteX() + progress.getWidth() / 2;
		final int sourceY = progress.getAbsoluteY() + progress.getHeight() / 2;

		// create color picker
		Main.getTransitionManager().setTarget(sourceX, sourceY);

		// create dialog
		final ColorPickerPage page = new ColorPickerPage(model);
		final ColorPicker colorPicker = page.getColorPicker();
		// colorPicker.addOnValueChangeListener(listener);
		Main.getNavigator().show(page, true);
	}

	public void startAnimation() {
		progress.startAnimation();
	}

	public void resetAnimation() {
		progress.resetAnimation();
	}


	public void stopAnimation() {
		progress.stopAnimation();
	}

	@Override
	public void onAnimationEnd() {
		synchronized (this) {
			notify();
		}
	}
}
