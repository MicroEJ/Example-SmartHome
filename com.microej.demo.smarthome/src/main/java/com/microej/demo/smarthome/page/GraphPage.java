/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.page;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.MenuButton;
import com.microej.demo.smarthome.widget.chart.Chart;
import com.microej.demo.smarthome.widget.chart.ChartPoint;
import com.microej.demo.smarthome.widget.chart.LineChart;

import ej.widget.basic.Label;
import ej.widget.container.Scroll;

/**
 * This class represents the page which shows the power chart
 */
public class GraphPage extends MenuPage {

	/**
	 * Values
	 */
	private static final String UNIT_STRING = "W";
	private static final int NUM_SCALE_VALUES = 3;
	private static final float MAX_POWER_SUBSCRIBED = 6000.0f;

	/**
	 * Constructor
	 */
	public GraphPage() {
		// create chart
		Chart chart = new LineChart();
		chart.addClassSelector(ClassSelectors.CHART);

		// add chart points
		for (int h = 0; h < 24; h++) {
			String name = Integer.toString(h);
			String fullName = name + ":00";
			float value = generateRandomValue();
			ChartPoint point = new ChartPoint(name, fullName, value);
			point.addClassSelector(ClassSelectors.CHART_POINT);
			chart.addPoint(point);
		}

		// set chart scale
		chart.setScale(NUM_SCALE_VALUES, MAX_POWER_SUBSCRIBED);

		// set chart unit
		chart.setUnit(UNIT_STRING);

		// create scroll
		Scroll scroll = new Scroll(true, false);
		scroll.setWidget(chart);
		scroll.addClassSelector(ClassSelectors.CHART_SCROLL);

		// set page widget
		setWidget(scroll);
	}

	/**
	 * Creates the menu button
	 */
	@Override
	protected MenuButton createMenuButton() {
		MenuButton menuButton = new MenuButton(new Label(Strings.MAXPOWERTODAY));
		menuButton.addClassSelector(ClassSelectors.DASHBOARD_MENU_BUTTON);
		return menuButton;
	}

	/**
	 * Generates a random for the chart
	 */
	private static float generateRandomValue() {
		return (float) Math.random() * MAX_POWER_SUBSCRIBED;
	}

}
