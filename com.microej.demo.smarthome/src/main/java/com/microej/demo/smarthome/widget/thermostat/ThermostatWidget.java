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
	private TemperatureLabel desiredTemperature;
	private TemperatureLabel currentTemperature;
	private String lastClassSelector = null;
	private Label desiredLabel;

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
		OnValueChangeListener onValueChangeListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(int newValue) {
				desiredTemperature.setTemperature(thermostatCircularProgress.getTargetValue());
				currentTemperature.setTemperature(thermostat.getTemperature());
				updateClassSelectors(thermostat.getTemperature() / 10,
						thermostatCircularProgress.getTargetValue() / 10);
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
		thermostatCircularProgress.addOnTargetValueChangeListener(onValueChangeListener);
		thermostatCircularProgress.addOnValueChangeListener(onValueChangeListener);

		button = new LimitedButtonWrapper();
		button.setAdjustedToChild(false);
		Label widget = new Label(Strings.OK);
		widget.addClassSelector(ClassSelectors.THERMOSTAT_VALIDATE);
		button.setWidget(widget);
		button.addOnClickListener(new OnClickListener() {

			@Override
			public void onClick() {
				thermostatCircularProgress.validateTagetValue();

			}
		});
		composite.add(button);


		add(createCurrentLabel());
		add(composite);

		add(createDesiredLabel());
		updateClassSelectors(model.getValue(), model.getTargetValue());
	}

	/**
	 * @return
	 */
	private Widget createDesiredLabel() {
		desiredLabel = new Label(Strings.DESIRED);
		desiredLabel.addClassSelector(ClassSelectors.THERMOSTAT_TOP_LABEL);
		desiredTemperature = new TemperatureLabel(thermostat.getTargetTemperature(), thermostat.getMaxTemperature());
		desiredTemperature.addClassSelector(ClassSelectors.THERMOSTAT_BOTTOM_LABEL);
		return createLabel(desiredLabel, desiredTemperature);
	}

	/**
	 * @return
	 */
	private Widget createCurrentLabel() {
		Label topLabel = new Label(Strings.CURRENT);
		topLabel.addClassSelector(ClassSelectors.THERMOSTAT_TOP_LABEL);
		topLabel.addClassSelector(ClassSelectors.THERMOSTAT_CURRENT);
		currentTemperature = new TemperatureLabel(model.getValue(), model.getMaximum());
		currentTemperature.addClassSelector(ClassSelectors.THERMOSTAT_BOTTOM_LABEL);

		return createLabel(topLabel, currentTemperature);
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

	private void updateClassSelectors(int current, int target) {
		if (current == target) {
			setDesiredClassSelector(null);
		} else if (current > target) {
			setDesiredClassSelector(ClassSelectors.THERMOSTAT_DESIRED_COLD);
		} else /* current < target */ {
			setDesiredClassSelector(ClassSelectors.COLOR_CORAL);
		}
	}

	private void setDesiredClassSelector(String classSelector) {
		if (lastClassSelector != classSelector) {
			if (lastClassSelector != null) {
				desiredTemperature.removeClassSelector(lastClassSelector);
				desiredLabel.removeClassSelector(lastClassSelector);
			}
			if (classSelector != null) {
				desiredTemperature.addClassSelector(classSelector);
				desiredLabel.addClassSelector(classSelector);
			}

			lastClassSelector = classSelector;
		}
	}
}
