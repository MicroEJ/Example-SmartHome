/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.thermostat;

import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MaxWidthLabel;

import ej.widget.basic.Label;
import ej.widget.composed.Wrapper;
import ej.widget.container.Flow;

/**
 *
 */
public class DiffLabel extends Wrapper {

	private static final String MAX_WIDTH_LABEL = "+40°";
	private int dif = 0;
	private final Label value;
	private final Label degree;
	private final Label topLabel;

	// TODO remove link with label.

	/**
	 * @param thermostat
	 */
	public DiffLabel(Thermostat thermostat, Label topLabel) {
		super();
		this.topLabel = topLabel;
		Flow labels = new Flow(true);
		value = new MaxWidthLabel(MAX_WIDTH_LABEL);
		value.addClassSelector(ClassSelectors.THERMOSTAT_DIFF_VALUE);
		degree = new Label(Strings.DEGREE);
		degree.addClassSelector(ClassSelectors.THERMOSTAT_DIFF_VALUE_DEGREE);
		setDif(0);
		value.setText(String.valueOf(getDif()));
		labels.add(value);
		labels.add(degree);
		setAdjustedToChild(false);
		setWidget(labels);
	}

	/**
	 * Gets the dif.
	 *
	 * @return the dif.
	 */
	public int getDif() {
		return dif;
	}

	/**
	 * Sets the dif.
	 *
	 * @param dif
	 *            the dif to set.
	 */
	public void setDif(int dif) {
		int temp = dif / 10;
		if(temp!=dif){
			updateClassSelector(temp);
			this.dif = temp;
			StringBuilder builder = new StringBuilder();
			if (this.dif > 0) {
				builder.append(Strings.PLUS);
			}
			builder.append(String.valueOf(this.dif));
			value.setText(builder.toString());
		}
	}

	/**
	 * @param dif2
	 */
	private void updateClassSelector(int dif) {
		boolean updateStyle = false;
		if (this.dif > 0 && dif <= 0) {
			topLabel.removeClassSelector(ClassSelectors.COLOR_CORAL);
			value.removeClassSelector(ClassSelectors.COLOR_CORAL);
			updateStyle = true;
		} else if (this.dif < 0 && dif >= 0) {
			value.removeClassSelector(ClassSelectors.THERMOSTAT_DESIRED_COLD);
			topLabel.removeClassSelector(ClassSelectors.THERMOSTAT_DESIRED_COLD);
			updateStyle = true;
		}

		if (updateStyle || this.dif == 0) {
			if (dif > 0) {
				topLabel.addClassSelector(ClassSelectors.COLOR_CORAL);
				value.addClassSelector(ClassSelectors.COLOR_CORAL);
				updateStyle = true;
			} else if (dif < 0) {
				value.addClassSelector(ClassSelectors.THERMOSTAT_DESIRED_COLD);
				topLabel.addClassSelector(ClassSelectors.THERMOSTAT_DESIRED_COLD);
				updateStyle = true;
			}
		}
	}
}
