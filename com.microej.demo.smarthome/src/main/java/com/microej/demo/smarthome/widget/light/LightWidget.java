/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.light;

import com.microej.demo.smarthome.Main;
import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.data.light.LightEventListener;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.CircleWidget;
import com.microej.demo.smarthome.widget.ColorPicker;
import com.microej.demo.smarthome.widget.DeviceWidget;
import com.microej.demo.smarthome.widget.OnAnimationEndListener;
import com.microej.demo.smarthome.widget.OverlapingComposite;

import ej.mwt.Panel;
import ej.widget.basic.image.ImageSwitch;
import ej.widget.listener.OnClickListener;
import ej.widget.listener.OnStateChangeListener;
import ej.widget.listener.OnValueChangeListener;
import ej.widget.model.BoundedRangeModel;
import ej.widget.model.DefaultBoundedRangeModel;

/**
 *
 */
public class LightWidget extends DeviceWidget<Light>
implements LightEventListener, OnStateChangeListener, OnAnimationEndListener {

	/**
	 * Attributes
	 */
	private final LightCircularProgress circular;
	private final ImageSwitch switchButton;
	private final CircleWidget circularButton;

	/**
	 * Constructor
	 */
	public LightWidget(final Light model) {
		super(model);
		model.addListener(this);
		addClassSelector(ClassSelectors.LIGHT_WIDGET);

		final OverlapingComposite overlapingComposite = new OverlapingComposite();
		// circular progress
		final BoundedRangeModel boundedRange = new DefaultBoundedRangeModel(0, 1000, 0);
		circular = new LightCircularProgress(boundedRange);

		circular.addOnValueChangeListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(final int newValue) {
				model.setBrightness(circular.getPercentComplete());

			}

			@Override
			public void onMinimumValueChange(final int newMinimum) {
			}

			@Override
			public void onMaximumValueChange(final int newMaximum) {
			}
		});
		circular.addClassSelector(ClassSelectors.LIGHT_PROGRESS);
		circular.setOnAnimationEndListener(this);

		overlapingComposite.add(circular);


		circularButton = new CircleWidget(new OnClickListener() {
			@Override
			public void onClick() {
				changeColor();
			}
		});
		circularButton.addClassSelector(ClassSelectors.LIGHT_PROGRESS);
		overlapingComposite.add(circularButton);

		// toggle button
		switchButton = new ImageSwitch();
		switchButton.setChecked(model.isOn());
		switchButton.addOnStateChangeListener(this);
		switchButton.addClassSelector(ClassSelectors.LIGHT_PROGRESS);

		// place widgets
		setCenter(overlapingComposite);
		addBottom(switchButton);


		final float value = model.getBrightness();
		onBrightnessChange(value);

		// set initial state
		onColorChange(model.getColor());
	}

	/**
	 * Handle color change event
	 */
	@Override
	public void onColorChange(final int color) {
		circular.setColor(color);
		circularButton.setColor(color);
	}

	/**
	 * Handle brightness change event
	 */
	@Override
	public void onBrightnessChange(final float brightness) {
		final int min = circular.getMinimum();
		final int max = circular.getMaximum();
		final float value = (max - min) * brightness + min;
		circular.setValue((int) value);
	}

	@Override
	public boolean isEnabled() {
		return circular.isEnabled();
	}
	/**
	 * Handle state change event
	 */
	@Override
	public void onStateChange(final boolean on) {
		circular.setEnabled(on);
		circularButton.setEnabled(on);
		if (on) {
			circular.initAnimation();
			circular.startAnimation();
		}
		switchButton.setChecked(on);
		model.switchOn(on);
	}

	@Override
	public void showNotify() {
		super.showNotify();
		onColorChange(model.getColor());
	}

	/**
	 * Changes light color
	 */
	private void changeColor() {
		// calculate animation source position
		final int sourceX = circular.getAbsoluteX() + circular.getWidth() / 2;
		final int sourceY = circular.getAbsoluteY() + circular.getHeight() / 2;

		// create color change listener
		final OnValueChangeListener listener = new OnValueChangeListener() {
			@Override
			public void onValueChange(final int newValue) {
				model.setColor(newValue);
				circularButton.setColor(newValue);
			}

			@Override
			public void onMinimumValueChange(final int newMinimum) {
			}

			@Override
			public void onMaximumValueChange(final int newMaximum) {
			}
		};

		// create color picker
		final ColorPicker picker = new ColorPicker(sourceX, sourceY, model.getColor(), getPanel());
		picker.addOnValueChangeListener(listener);

		// create dialog
		final Panel dialog = new Panel();
		dialog.setWidget(picker);

		// set close dialog listener
		final OnClickListener closeButtonListener = new OnClickListener() {
			@Override
			public void onClick() {
				getPanel().show(Main.getDesktop());
				dialog.hide();
			}
		};
		picker.setCloseButtonListener(closeButtonListener);

		// show dialog
		dialog.show(Main.getDesktop(), true);
		getPanel().hide();
	}

	public void startAnimation() {
		circular.startAnimation();
	}


	/**
	 *
	 */
	public void stopAnimation() {
		circular.stopAnimation();
		circular.initAnimation();

	}

	@Override
	public void onAnimationEnd() {
		synchronized (this) {
			notify();
		}
	}
}
