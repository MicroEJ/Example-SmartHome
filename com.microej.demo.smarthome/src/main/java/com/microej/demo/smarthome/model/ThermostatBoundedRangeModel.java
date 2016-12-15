/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.model;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.data.thermostat.ThermostatEventListener;

import ej.widget.listener.OnValueChangeListener;
import ej.widget.model.DefaultBoundedRangeModel;

/**
 *
 */
public class ThermostatBoundedRangeModel extends DefaultBoundedRangeModel implements ThermostatEventListener {

	private final Thermostat thermostat;
	private final List<OnValueChangeListener> listeners;

	/**
	 * @param minimum
	 * @param maximum
	 * @param initialValue
	 */
	public ThermostatBoundedRangeModel(Thermostat thermostat) {
		super(thermostat.getMinTemperature(), thermostat.getMaxTemperature(), thermostat.getTemperature());
		this.thermostat = thermostat;
		listeners = new ArrayList<>();
	}

	@Override
	public void onTemperatureChange(int temperature) {
		setValue(temperature);

	}

	@Override
	public void onTargetTemperatureChange(int targetTemperature) {
		for (OnValueChangeListener onValueChangeListener : listeners) {
			onValueChangeListener.onValueChange(targetTemperature);
		}
	}

	/**
	 * @param targetTemperature
	 */
	public void setTargetValue(int target) {
		if (thermostat.getTargetTemperature() != target) {
			target = Math.max(getMinimum(), target);
			target = Math.min(getMaximum(), target);
			thermostat.setTargetTemperature(target);
			for (OnValueChangeListener onValueChangeListener : listeners) {
				onValueChangeListener.onValueChange(target);
			}
		}
	}

	public int getTargetValue() {
		return thermostat.getTargetTemperature();
	}
	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addOnTargetValueChangeListener(OnValueChangeListener e) {
		return listeners.add(e);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean removeOnTargetValueChangeListener(OnValueChangeListener o) {
		return listeners.remove(o);
	}

	public void register() {
		thermostat.addListener(this);
	}

	public void unregister() {
		thermostat.removeListener(this);
	}

	public int getDiff() {
		return getTargetValue() - getValue();
	}
}
