/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.chart;

import java.util.List;

import com.microej.demo.smarthome.data.power.InstantPower;
import com.microej.demo.smarthome.data.power.PowerEventListener;
import com.microej.demo.smarthome.data.power.PowerMeter;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;

import ej.widget.composed.Wrapper;
import ej.widget.container.Scroll;

/**
 * Widget diplaying the power usage.
 */
public class PowerWidget extends Wrapper implements PowerEventListener {

	private static final int HOUR_IN_DAY = 24;

	private static final int NUM_SCALE_VALUES = 3;

	private final BasicChart chart;
	private final PowerMeter model;

	/**
	 * Instantiates a PowerWidget.
	 *
	 * @param model
	 *            the model.
	 */
	public PowerWidget(final PowerMeter model) {
		super();

		this.model = model;

		// create chart
		this.chart = new LineChart();
		this.chart.addClassSelector(ClassSelectors.CHART);

		// add chart points
		for (int h = 0; h < HOUR_IN_DAY; h++) {
			// Uncomment to use 12 hours.
			//			int halfADay = HOUR_IN_DAY >> 1;
			//			int hour = h % (halfADay+1);
			//			if (h > halfADay) {
			//				hour++;
			//			}
			final String name = Integer.toString(h);
			StringBuffer fullName = new StringBuffer();
			fullName.append(Integer.toString(h)).append(Strings.HOUR_END);
			// Uncomment to use 12 hours.
			//			fullName.append(Integer.toString(hour)).append(Strings.HOUR_END);
			//			if (h > halfADay) {
			//				fullName.append(Strings.PM);
			//			} else {
			//				fullName.append(Strings.AM);
			//			}
			final ChartPoint point = new ChartPoint(name, fullName.toString(), -1.0f);
			point.addClassSelector(ClassSelectors.CHART_POINT);
			this.chart.addPoint(point);
		}

		// set chart scale
		this.chart.setScale(NUM_SCALE_VALUES, model.getMaxPowerConsumption());

		// set chart unit
		this.chart.setUnit(Strings.WATT);

		// create scroll
		final Scroll scroll = new Scroll(true, false);
		scroll.setWidget(this.chart);
		scroll.addClassSelector(ClassSelectors.CHART_SCROLL);

		// load chart data
		reload();

		setWidget(scroll);
	}

	/**
	 * On new power data.
	 */
	@Override
	public void onInstantPower(final InstantPower instantPower) {
		final List<ChartPoint> points = this.chart.getPoints();
		final ChartPoint lastPoint = points.get(points.size() - 1);
		if (lastPoint.getValue() >= 0.0f) {
			final Integer selectedPoint = this.chart.getSelectedPoint();
			if (selectedPoint != null) {
				if (selectedPoint.intValue() > 0) {
					this.chart.selectPoint(Integer.valueOf(selectedPoint.intValue() - 1));
				} else {
					this.chart.selectPoint(null);
				}
			}
		}

		reload();
	}

	/**
	 * Reload chart points.
	 */
	public void reload() {
		final List<ChartPoint> points = this.chart.getPoints();
		int index = 0;
		for (final InstantPower instantPower : this.model.getPowerConsumptions()) {
			final float value = instantPower.getPower();
			points.get(index).setValue(value);
			index++;
		}

		this.chart.repaint();
	}

	/**
	 * Starts the animation.
	 * 
	 * @see com.microej.demo.smarthome.widget.chart.BasicChart#startAnimation()
	 */
	public void startAnimation() {
		this.chart.startAnimation();
	}

	/**
	 * Stops the animation.
	 * 
	 * @see com.microej.demo.smarthome.widget.chart.BasicChart#stopAnimation()
	 */
	public void stopAnimation() {
		this.chart.stopAnimation();
	}

	/**
	 * Select a point in the chart.
	 * Used by the robot.
	 * @param id the id of the point, null for none.
	 */
	public void selectPoint(Integer id) {
		this.chart.selectPoint(id);
	}
}
