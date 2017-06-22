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
 * A label displaying a bounded range model value.
 */
public class BoundedRangeLabel extends StyledComposite {

	private final BoundedRangeModel model;
	private final Label type;
	private final MaxWidthLabel value;
	private final OnValueChangeListener onValueChangeListener;

	/**
	 * Instantiates a BoundedRangeLabel
	 * 
	 * @param boundedRange
	 *            the model.
	 */
	public BoundedRangeLabel(final BoundedRangeModel boundedRange) {
		this.model = boundedRange;
		value = new MaxWidthLabel(computeText(getValue(boundedRange.getValue())),
				String.valueOf(getValue(boundedRange.getMaximum())));
		type = new Label(Strings.PERCENT);
		type.addClassSelector(ClassSelectors.TYPE);
		this.add(value);
		this.add(type);
		onValueChangeListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(final int newValue) {
				value.setText(computeText(newValue));

			}

			@Override
			public void onMinimumValueChange(final int newMinimum) {
				// Not used

			}

			@Override
			public void onMaximumValueChange(final int newMaximum) {
				// Not used

			}
		};
	}

	private int getValue(final int value) {
		return value / 10;
	}

	private String computeText(final int newValue) {
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
	public Rectangle validateContent(final Style style, final Rectangle bounds) {
		value.validate(MWT.NONE, MWT.NONE);
		type.validate(MWT.NONE, MWT.NONE);
		final int widthHint = value.getPreferredWidth() + type.getPreferredWidth();
		final int heightHint = Math.max(value.getPreferredHeight(), value.getPreferredHeight());

		return new Rectangle(0, 0, widthHint, heightHint);
	}

	@Override
	protected void setBoundsContent(final Rectangle bounds) {
		final int boundsX = bounds.getX();
		final int boundsY = bounds.getY();
		final int boundsWidth = bounds.getWidth();
		final int boundsHeight = bounds.getHeight();

		final int valueWidth = value.getPreferredWidth();
		final int typeWidth = type.getPreferredWidth();
		final int height = Math.max(value.getPreferredHeight(), value.getPreferredHeight());

		final int alignment = getStyle().getAlignment();
		final int x = AlignmentHelper.computeXLeftCorner(valueWidth + typeWidth, boundsX, boundsWidth, alignment);
		final int y = AlignmentHelper.computeYTopCorner(height, boundsY, boundsHeight, alignment);
		value.setBounds(x, y, valueWidth, height);
		type.setBounds(x + valueWidth, y, typeWidth, height);
	}

	@Override
	public void addClassSelector(final String classSelector) {
		super.addClassSelector(classSelector);
		value.addClassSelector(classSelector);
		type.addClassSelector(classSelector);
	}

	@Override
	public void removeClassSelector(final String classSelector) {
		super.removeClassSelector(classSelector);
		value.removeClassSelector(classSelector);
		type.removeClassSelector(classSelector);
	}
}
