/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget.chart;

import java.util.List;

import com.microej.demo.smarthome.data.power.InstantPower;
import com.microej.demo.smarthome.data.power.Power;
import com.microej.demo.smarthome.data.power.PowerEventListener;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;

import ej.widget.composed.Wrapper;
import ej.widget.container.Scroll;

/**
 * Widget diplaying the power usage.
 */
public class PowerWidget extends Wrapper implements PowerEventListener {

	private static final int NUM_SCALE_VALUES = 3;

	private final BasicChart chart;
	private final Power model;

	/**
	 * Instantiates a PowerWidget.
	 *
	 * @param model
	 *            the model.
	 */
	public PowerWidget(final Power model) {
		super();

		this.model = model;

		// create chart
		this.chart = new LineChart();
		this.chart.addClassSelector(ClassSelectors.CHART);

		// add chart points
		for (int h = 0; h < 24; h++) {
			int hour = h % 13;
			if (h > 12) {
				hour++;
			}
			final String name = Integer.toString(h);
			String fullName = Integer.toString(hour) + Strings.HOUR_END;
			if (h > 12) {
				fullName += Strings.PM;
			} else {
				fullName += Strings.AM;
			}
			final ChartPoint point = new ChartPoint(name, fullName, -1.0f);
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
	 * On new power data
	 */
	@Override
	public void onInstantPower(final InstantPower instantPower) {
		final List<ChartPoint> points = this.chart.getPoints();
		final ChartPoint lastPoint = points.get(points.size() - 1);
		if (lastPoint.getValue() >= 0.0f) {
			final Integer selectedPoint = this.chart.getSelectedPoint();
			if (selectedPoint != null) {
				if (selectedPoint.intValue() > 0) {
					this.chart.selectPoint(selectedPoint.intValue() - 1);
				} else {
					this.chart.selectPoint(null);
				}
			}
		}

		reload();
	}

	/**
	 * Reload chart points
	 */
	public void reload() {
		final List<ChartPoint> points = this.chart.getPoints();
		int index = 0;
		for (final InstantPower instantPower : model.getPowerConsumptions()) {
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
		chart.startAnimation();
	}

	/**
	 * Stops the animation.
	 * 
	 * @see com.microej.demo.smarthome.widget.chart.BasicChart#stopAnimation()
	 */
	public void stopAnimation() {
		chart.stopAnimation();
	}

}
