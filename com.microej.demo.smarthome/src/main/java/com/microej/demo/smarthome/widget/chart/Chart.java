/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.chart;

import java.util.ArrayList;
import java.util.List;

import ej.widget.StyledComposite;

/**
 * Represents a chart with several ordered points.
 */
public abstract class Chart extends StyledComposite {

	/**
	 * Values
	 */
	private static final int DEFAULT_NUM_SCALE_VALUES = 1;
	private static final float DEFAULT_MAX_SCALE_VALUE = 100.0f;
	/**
	 * Points
	 */
	private final List<ChartPoint> points;
	private Integer selectedPointIndex;

	/**
	 * Scale
	 */
	private int numScaleValues;
	private float maxScaleValue;

	/**
	 * Unit
	 */
	private String unit;

	/**
	 * Instantiates a Chart.
	 */
	public Chart() {
		this.points = new ArrayList<>();
		this.selectedPointIndex = null;
		this.numScaleValues = DEFAULT_NUM_SCALE_VALUES;
		this.maxScaleValue = DEFAULT_MAX_SCALE_VALUE;
		this.unit = null;
	}

	/**
	 * Adds a point
	 *
	 * @param chartPoint
	 *            the point to add
	 */
	public void addPoint(final ChartPoint chartPoint) {
		this.points.add(chartPoint);
		chartPoint.setParentElement(this);
	}

	/**
	 * Removes all points
	 */
	public void removeAllPoints() {
		this.points.clear();
		this.selectedPointIndex = null;
	}

	/**
	 * Selects one of the points
	 *
	 * @param pointIndex
	 *            the index of the point to select
	 */
	public void selectPoint(final Integer pointIndex) {
		// check the index
		if (pointIndex != null) {
			final int pointIndexInt = pointIndex.intValue();
			if (pointIndexInt < 0 || pointIndexInt >= this.points.size()) {
				throw new IndexOutOfBoundsException();
			}
		}

		if (pointIndex != this.selectedPointIndex) {
			// select newly selected point
			this.selectedPointIndex = pointIndex;
			if (pointIndex != null) {
				final ChartPoint newPoint = this.points.get(pointIndex.intValue());
				if (newPoint.getValue() < 0.0f) {
					this.selectedPointIndex = null;
				}
			}

			// repaint the chart
			repaint();
		}
	}

	/**
	 * Gets the list of points
	 *
	 * @return the list of points
	 */
	public List<ChartPoint> getPoints() {
		return this.points;
	}

	/**
	 * Gets the selected point
	 *
	 * @return the index of the currently selected point
	 */
	public Integer getSelectedPoint() {
		return this.selectedPointIndex;
	}

	/**
	 * Sets the scale
	 *
	 * @param numScaleValues
	 *            the number of values to show on the scale
	 * @param maxScaleValue
	 *            the maximum value of the scale
	 */
	public void setScale(final int numScaleValues, final float maxScaleValue) {
		this.numScaleValues = numScaleValues;
		this.maxScaleValue = maxScaleValue;
		repaint();
	}

	/**
	 * Gets the number of scale values
	 * @return the number of values to show on the scale
	 */
	public int getNumScaleValues() {
		return this.numScaleValues;
	}

	/**
	 * Gets the maximum value of the scale
	 * @return the maximum value of the scale
	 */
	public float getMaxScaleValue() {
		return this.maxScaleValue;
	}

	/**
	 * Sets the unit
	 *
	 * @param unit
	 *            the unit string
	 */
	public void setUnit(final String unit) {
		this.unit = unit;
		repaint();
	}

	/**
	 * Gets the unit
	 *
	 * @return the unit string
	 */
	public String getUnit() {
		return this.unit;
	}
}
