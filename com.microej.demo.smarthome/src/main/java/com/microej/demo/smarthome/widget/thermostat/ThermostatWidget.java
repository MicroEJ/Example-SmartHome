/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.thermostat;

import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.model.ThermostatBoundedRangeModel;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.LimitedButtonWrapper;
import com.microej.demo.smarthome.widget.OverlapingComposite;

import ej.mwt.Widget;
import ej.widget.basic.Label;
import ej.widget.composed.ButtonWrapper;
import ej.widget.composed.Wrapper;
import ej.widget.container.Grid;
import ej.widget.container.List;
import ej.widget.listener.OnClickListener;
import ej.widget.listener.OnValueChangeListener;

/**
 *
 */
public class ThermostatWidget extends Grid {

	private final Thermostat thermostat;
	private final ThermostatBoundedRangeModel model;
	private final ButtonWrapper button;
	private DiffLabel diff;

	/**
	 * @param thermostat
	 */
	public ThermostatWidget(Thermostat thermostat) {
		super(false, 1);
		this.thermostat = thermostat;
		model = new ThermostatBoundedRangeModel(thermostat);

		OverlapingComposite composite = new OverlapingComposite();

		ThermostatCircularProgress thermostatCircularProgress = new ThermostatCircularProgress(model);
		thermostatCircularProgress.addClassSelector(ClassSelectors.THERMOSTAT);
		composite.add(thermostatCircularProgress);
		OnValueChangeListener onTargetValueChangeListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(int newValue) {
				boolean visible = newValue != model.getTargetValue();
				if (visible != button.isVisible()) {
					button.setVisible(visible);
					button.revalidate();
				}
				diff.setDif(newValue - thermostat.getTemperature());

			}

			@Override
			public void onMinimumValueChange(int newMinimum) {
				// Not used.

			}

			@Override
			public void onMaximumValueChange(int newMaximum) {
				// Not used.

			}
		};
		thermostatCircularProgress.addOnTargetValueChangeListener(onTargetValueChangeListener);

		button = new LimitedButtonWrapper();
		button.setAdjustedToChild(false);
		Label widget = new Label(Strings.OK);
		widget.addClassSelector(ClassSelectors.THERMOSTAT_VALIDATE);
		button.setWidget(widget);
		button.addOnClickListener(new OnClickListener() {

			@Override
			public void onClick() {
				thermostatCircularProgress.validateTagetValue();
				onTargetValueChangeListener.onValueChange(thermostatCircularProgress.getTargetValue());

			}
		});
		composite.add(button);


		add(createCurrentLabel());
		add(composite);

		add(createDesiredLabel());
	}

	/**
	 * @return
	 */
	private Widget createDesiredLabel() {
		Label topLabel = new Label(Strings.DESIRED);
		topLabel.addClassSelector(ClassSelectors.THERMOSTAT_TOP_LABEL);
		diff = new DiffLabel(thermostat, topLabel);
		diff.setDif(thermostat.getTargetTemperature() - thermostat.getTemperature());
		diff.addClassSelector(ClassSelectors.THERMOSTAT_BOTTOM_LABEL);
		return createLabel(topLabel, diff);
	}

	/**
	 * @return
	 */
	private Widget createCurrentLabel() {
		Label topLabel = new Label(Strings.CURRENT);
		topLabel.addClassSelector(ClassSelectors.THERMOSTAT_TOP_LABEL);
		topLabel.addClassSelector(ClassSelectors.THERMOSTAT_CURRENT);
		ThermostatLabel bottomLabel = new ThermostatLabel(thermostat);
		bottomLabel.addClassSelector(ClassSelectors.THERMOSTAT_BOTTOM_LABEL);

		return createLabel(topLabel, bottomLabel);
	}

	private Widget createLabel(Widget top, Widget bottom) {
		Wrapper label = new Wrapper();
		List list = new List(false);
		list.add(top);
		list.add(bottom);
		label.setWidget(list);
		label.setAdjustedToChild(false);

		return label;
	}
}
