/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.data.light.LightEventListener;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;

import ej.microui.display.GraphicsContext;
import ej.widget.basic.Label;
import ej.widget.listener.OnClickListener;
import ej.widget.listener.OnValueChangeListener;
import ej.widget.model.BoundedRangeModel;
import ej.widget.model.DefaultBoundedRangeModel;

/**
 *
 */
public class LightWidget extends DeviceWidget<Light> implements LightEventListener {

	private final CircularProgressWidget circular;
	private final OnValueChangeListener onValueChangeListener;
	private final LimitedButtonWrapper buttonWrapper;
	private final BoundedRangeLabel boundedRangeLabel;
	private final Label off;
	private boolean isEnabled = true;

	public LightWidget(Light model) {
		super(model);
		addClassSelector(ClassSelectors.LIGHT_WIDGET);

		BoundedRangeModel boundedRange = new DefaultBoundedRangeModel(0, 1000, 0);
		circular = new CircularProgressWidget(boundedRange);
		circular.addClassSelector(ClassSelectors.LIGHT_PROGRESS);

		OverlapingComposite composite = new OverlapingComposite();
		composite.add(circular);
		buttonWrapper = new LimitedButtonWrapper();
		boundedRangeLabel = new BoundedRangeLabel(boundedRange);
		boundedRangeLabel.addClassSelector(ClassSelectors.LIGHT_VALUE);
		buttonWrapper.setAdjustedToChild(false);
		buttonWrapper.setWidget(boundedRangeLabel);

		off = new Label(Strings.OFF) {
			@Override
			public void render(GraphicsContext g) {
				super.render(g);
			}

		};
		off.addClassSelector(ClassSelectors.LIGHT_VALUE);
		off.addClassSelector(ClassSelectors.LIGHT_VALUE_OFF);

		composite.add(buttonWrapper);
		buttonWrapper.addOnClickListener(new OnClickListener() {

			@Override
			public void onClick() {
				toggle();

			}

		});

		setValue((int) (model.getBrightness() * circular.getMaximum()));
		setCenter(composite);

		onValueChangeListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(int newValue) {
				model.setBrightness(((float) newValue - circular.getMinimum()) / circular.getMaximum());

			}

			@Override
			public void onMinimumValueChange(int newMinimum) {
				// Not needed.

			}

			@Override
			public void onMaximumValueChange(int newMaximum) {
				// Not needed.

			}
		};

		setState(model.isOn());
	}

	@Override
	public void showNotify() {
		super.showNotify();
		model.addListener(this);
		circular.addOnValueChangeListener(onValueChangeListener);
	}

	@Override
	public void hideNotify() {
		model.removeListener(this);
		circular.removeOnValueChangeListener(onValueChangeListener);
	}

	@Override
	public void onColorChange(int color) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBrightnessChange(float brightness) {
		setValue((int) (brightness * circular.getMaximum()));

	}

	@Override
	public void onStateChange(boolean on) {
		// TODO Auto-generated method stub

	}

	private void setValue(int value) {
		circular.setValue(value);
	}

	private void setState(boolean on) {
		isEnabled = on;
		if (isEnabled) {
			buttonWrapper.setWidget(boundedRangeLabel);
		} else {
			buttonWrapper.setWidget(off);
		}
		model.switchOn(isEnabled);

		buttonWrapper.partialRevalidate();
		// Set enable trigers the repaint of the circular and button.
		circular.setEnabled(isEnabled);
		// buttonWrapper.revalidate();
	}

	private void toggle() {
		isEnabled = !isEnabled;
		setState(isEnabled);
	}
}
