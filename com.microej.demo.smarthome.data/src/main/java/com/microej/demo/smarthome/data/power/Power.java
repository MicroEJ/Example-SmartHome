/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.data.power;

import com.microej.demo.smarthome.data.Device;

/**
 *
 */
public interface Power extends Device<PowerEventListener> {

	public InstantPower getInstantPowerConsumption();

	public InstantPower[] getPowerConsumptions();

	public int getMaxPowerConsumption();

}
