/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;

import ej.mwt.MWT;
import ej.style.Style;
import ej.style.container.AlignmentHelper;
import ej.style.container.Rectangle;
import ej.widget.StyledComposite;
import ej.widget.basic.Label;
import ej.widget.listener.OnValueChangeListener;
import ej.widget.model.BoundedRangeModel;

/**
 *
 */
public class BoundedRangeLabel extends StyledComposite {

	private final BoundedRangeModel model;
	private final Label type;
	private final MaxWidthLabel value;
	private final OnValueChangeListener onValueChangeListener;

	/**
	 * @param boundedRange
	 */
	public BoundedRangeLabel(BoundedRangeModel boundedRange) {
		this.model = boundedRange;
		value = new MaxWidthLabel(computeText(getValue(boundedRange.getValue())),
				String.valueOf(getValue(boundedRange.getMaximum())));
		type = new Label(Strings.PERCENT);
		type.addClassSelector(ClassSelectors.TYPE);
		this.add(value);
		this.add(type);
		onValueChangeListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(int newValue) {
				value.setText(computeText(newValue));

			}

			@Override
			public void onMinimumValueChange(int newMinimum) {
				// Not used

			}

			@Override
			public void onMaximumValueChange(int newMaximum) {
				// Not used

			}
		};
	}

	/**
	 * @param value2
	 * @return
	 */
	private int getValue(int value) {
		return value / 10;
	}

	private String computeText(int newValue) {
		if (!isEnabled()) {
			return "";
		}
		return String.valueOf(getValue(newValue));
	}

	@Override
	public void showNotify() {
		super.showNotify();
		model.addOnValueChangeListener(onValueChangeListener);
		value.setText(computeText(model.getValue()));
	}

	@Override
	public void hideNotify() {
		model.removeOnValueChangeListener(onValueChangeListener);
		super.hideNotify();
	}

	@Override
	public Rectangle validateContent(Style style, Rectangle bounds) {
		value.validate(MWT.NONE, MWT.NONE);
		type.validate(MWT.NONE, MWT.NONE);
		int widthHint = value.getPreferredWidth() + type.getPreferredWidth();
		int heightHint = Math.max(value.getPreferredHeight(), value.getPreferredHeight());

		return new Rectangle(0, 0, widthHint, heightHint);
	}

	@Override
	protected void setBoundsContent(Rectangle bounds) {
		int boundsX = bounds.getX();
		int boundsY = bounds.getY();
		int boundsWidth = bounds.getWidth();
		int boundsHeight = bounds.getHeight();

		int valueWidth = value.getPreferredWidth();
		int typeWidth = type.getPreferredWidth();
		int height = Math.max(value.getPreferredHeight(), value.getPreferredHeight());

		int alignment = getStyle().getAlignment();
		int x = AlignmentHelper.computeXLeftCorner(valueWidth + typeWidth, boundsX, boundsWidth, alignment);
		int y = AlignmentHelper.computeYTopCorner(height, boundsY, boundsHeight, alignment);
		value.setBounds(x, y, valueWidth, height);
		type.setBounds(x + valueWidth, y, typeWidth, height);
	}

	@Override
	public void addClassSelector(String classSelector) {
		super.addClassSelector(classSelector);
		value.addClassSelector(classSelector);
		type.addClassSelector(classSelector);
	}

	@Override
	public void removeClassSelector(String classSelector) {
		super.removeClassSelector(classSelector);
		value.removeClassSelector(classSelector);
		type.removeClassSelector(classSelector);
	}

}
