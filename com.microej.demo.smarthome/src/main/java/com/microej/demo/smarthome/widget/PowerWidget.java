/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import java.util.List;

import com.microej.demo.smarthome.data.power.InstantPower;
import com.microej.demo.smarthome.data.power.Power;
import com.microej.demo.smarthome.data.power.PowerEventListener;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.widget.chart.BasicChart;
import com.microej.demo.smarthome.widget.chart.ChartPoint;
import com.microej.demo.smarthome.widget.chart.LineChart;

import ej.widget.composed.Wrapper;
import ej.widget.container.Scroll;
import ej.widget.navigation.TransitionListener;
import ej.widget.navigation.TransitionManager;
import ej.widget.navigation.page.Page;

/**
 *
 */
public class PowerWidget extends Wrapper implements PowerEventListener {

	/**
	 * Values
	 */
	private static final String UNIT_STRING = "W";
	private static final int NUM_SCALE_VALUES = 3;

	/**
	 * Attributes
	 */
	private final BasicChart chart;
	private final Power model;
	private final TransitionListener listener;

	/**
	 * Constructor
	 */
	public PowerWidget(Power model) {
		super();

		this.model = model;

		// create chart
		this.chart = new LineChart();
		this.chart.addClassSelector(ClassSelectors.CHART);

		// add chart points
		for (int h = 0; h < 24; h++) {
			String name = Integer.toString(h);
			String fullName = name + ":00";
			ChartPoint point = new ChartPoint(name, fullName, -1.0f);
			point.addClassSelector(ClassSelectors.CHART_POINT);
			this.chart.addPoint(point);
		}

		// set chart scale
		this.chart.setScale(NUM_SCALE_VALUES, model.getMaxPowerConsumption());

		// set chart unit
		this.chart.setUnit(UNIT_STRING);

		// create scroll
		Scroll scroll = new Scroll(true, false);
		scroll.setWidget(this.chart);
		scroll.addClassSelector(ClassSelectors.CHART_SCROLL);

		// load chart data
		reload();

		setWidget(scroll);

		listener = new TransitionListener() {

			@Override
			public void onTransitionStop() {
				if (isShown()) {
					chart.startAnimation();
				}

			}

			@Override
			public void onTransitionStep(int step) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTransitionStart(int transitionsSteps, int transitionsStop, Page from, Page to) {
				reload();
				chart.stopAnimation();

			}
		};
	}

	/**
	 * On new power data
	 */
	@Override
	public void onInstantPower(InstantPower instantPower) {
		List<ChartPoint> points = this.chart.getPoints();
		ChartPoint lastPoint = points.get(points.size() - 1);
		if (lastPoint.getValue() >= 0.0f) {
			Integer selectedPoint = this.chart.getSelectedPoint();
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
		List<ChartPoint> points = this.chart.getPoints();
		int index = 0;
		for (InstantPower instantPower : model.getPowerConsumptions()) {
			float value = instantPower.getPower();
			points.get(index).setValue(value);
			index++;
		}

		this.chart.repaint();
	}

	@Override
	public void showNotify() {
		System.out.println("PowerWidget.showNotify()");
		TransitionManager.addGlobalTransitionListener(listener);
		super.showNotify();
	}

	@Override
	public void hideNotify() {
		System.out.println("PowerWidget.hideNotify()");
		super.hideNotify();
		TransitionManager.removeGlobalTransitionListener(listener);
	}

}
