/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.model;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.data.thermostat.Thermostat;
import com.microej.demo.smarthome.data.thermostat.ThermostatEventListener;

import ej.widget.listener.OnValueChangeListener;
import ej.widget.model.DefaultBoundedRangeModel;

/**
 * A model for a thermostat.
 */
public class ThermostatBoundedRangeModel extends DefaultBoundedRangeModel implements ThermostatEventListener {

	private final Thermostat thermostat;
	private final List<OnValueChangeListener> listeners;

	/**
	 * Instantiates a ThermostatBoundedRangeModel.
	 *
	 * @param thermostat
	 *            the thermostat.
	 */
	public ThermostatBoundedRangeModel(final Thermostat thermostat) {
		super(thermostat.getMinTemperature(), thermostat.getMaxTemperature(), thermostat.getTemperature());
		this.thermostat = thermostat;
		this.listeners = new ArrayList<>();
	}

	@Override
	public void onTemperatureChange(final int temperature) {
		setValue(temperature);

	}

	@Override
	public void onTargetTemperatureChange(final int targetTemperature) {
		for (final OnValueChangeListener onValueChangeListener : this.listeners) {
			onValueChangeListener.onValueChange(targetTemperature);
		}
	}

	/**
	 * Set the target temperature.
	 *
	 * @param target
	 *            target temperature.
	 */
	public void setTargetValue(int target) {
		int targetValue = target;
		if (this.thermostat.getTargetTemperature() != targetValue) {
			targetValue = Math.max(getMinimum(), targetValue);
			targetValue = Math.min(getMaximum(), targetValue);
			this.thermostat.setTargetTemperature(targetValue);
			for (final OnValueChangeListener onValueChangeListener : this.listeners) {
				onValueChangeListener.onValueChange(targetValue);
			}
		}
	}

	/**
	 * Gets the target temperature.
	 *
	 * @return the target temperature.
	 */
	public int getTargetValue() {
		return this.thermostat.getTargetTemperature();
	}

	/**
	 * Adds a listener to be notified on target temperature changes.
	 *
	 * @param listener
	 *            the listener.
	 * @return true (as specified by {@link List#add(Object)})
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addOnTargetValueChangeListener(final OnValueChangeListener listener) {
		return this.listeners.add(listener);
	}

	/**
	 * Removes a listener.
	 *
	 * @param listener
	 *            the listener.
	 * @return true (as specified by {@link List#remove(Object)})
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean removeOnTargetValueChangeListener(final OnValueChangeListener listener) {
		return this.listeners.remove(listener);
	}

	/**
	 * Registers the model as a listener of the thermostat.
	 */
	public void register() {
		this.thermostat.addListener(this);
	}

	/**
	 * Unregisters the model as a listener of the thermostat.
	 */
	public void unregister() {
		this.thermostat.removeListener(this);
	}
}
