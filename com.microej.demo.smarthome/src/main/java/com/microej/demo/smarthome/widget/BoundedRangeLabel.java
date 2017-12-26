/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
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
	 * Instantiates a BoundedRangeLabel.
	 * 
	 * @param boundedRange
	 *            the model.
	 */
	public BoundedRangeLabel(final BoundedRangeModel boundedRange) {
		this.model = boundedRange;
		this.value = new MaxWidthLabel(computeText(getValue(boundedRange.getValue())),
				String.valueOf(getValue(boundedRange.getMaximum())));
		this.type = new Label(Strings.PERCENT);
		this.type.addClassSelector(ClassSelectors.TYPE);
		this.add(this.value);
		this.add(this.type);
		this.onValueChangeListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(final int newValue) {
				BoundedRangeLabel.this.value.setText(computeText(newValue));

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

	@Override
	public void addClassSelector(final String classSelector) {
		super.addClassSelector(classSelector);
		this.value.addClassSelector(classSelector);
		this.type.addClassSelector(classSelector);
	}

	@Override
	public void removeClassSelector(final String classSelector) {
		super.removeClassSelector(classSelector);
		this.value.removeClassSelector(classSelector);
		this.type.removeClassSelector(classSelector);
	}

	@Override
	public void showNotify() {
		super.showNotify();
		this.model.addOnValueChangeListener(this.onValueChangeListener);
		this.value.setText(computeText(this.model.getValue()));
	}

	@Override
	public void hideNotify() {
		this.model.removeOnValueChangeListener(this.onValueChangeListener);
		super.hideNotify();
	}

	@Override
	public Rectangle validateContent(final Style style, final Rectangle bounds) {
		this.value.validate(MWT.NONE, MWT.NONE);
		this.type.validate(MWT.NONE, MWT.NONE);
		final int widthHint = this.value.getPreferredWidth() + this.type.getPreferredWidth();
		final int heightHint = Math.max(this.value.getPreferredHeight(), this.value.getPreferredHeight());

		return new Rectangle(0, 0, widthHint, heightHint);
	}

	@Override
	protected void setBoundsContent(final Rectangle bounds) {
		final int boundsX = bounds.getX();
		final int boundsY = bounds.getY();
		final int boundsWidth = bounds.getWidth();
		final int boundsHeight = bounds.getHeight();

		final int valueWidth = this.value.getPreferredWidth();
		final int typeWidth = this.type.getPreferredWidth();
		final int height = Math.max(this.value.getPreferredHeight(), this.value.getPreferredHeight());

		final int alignment = getStyle().getAlignment();
		final int x = AlignmentHelper.computeXLeftCorner(valueWidth + typeWidth, boundsX, boundsWidth, alignment);
		final int y = AlignmentHelper.computeYTopCorner(height, boundsY, boundsHeight, alignment);
		this.value.setBounds(x, y, valueWidth, height);
		this.type.setBounds(x + valueWidth, y, typeWidth, height);
	}

	private static int getValue(final int value) {
		return value / 10;
	}
	
	private String computeText(final int newValue) {
		if (!isEnabled()) {
			return ""; //$NON-NLS-1$
		}
		return String.valueOf(getValue(newValue));
	}

}
