/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget.chart;

import ej.style.Element;
import ej.style.util.ElementAdapter;

/** IPR start **/

/**
 * Represents a point of a Chart
 */
public class ChartPoint extends ElementAdapter {

	/**
	 * Attributes
	 */
	private String name;
	private String fullName;
	private float value;
	private Element parentElement;

	/**
	 * Constructor
	 * @param name
	 *          the name.
	 * @param fullName
	 *          the full name.
	 * @param value
	 *          the value.
	 */
	public ChartPoint(String name, String fullName, float value) {
		super();
		this.name = name;
		this.fullName = fullName;
		this.value = value;
		this.parentElement = null;
	}

	/**
	 * Gets the name.
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 * @param name
	 *          the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the full name.
	 * @return the full name.
	 */
	public String getFullName() {
		return this.fullName;
	}

	/**
	 * Sets the full name.
	 * @param fullName
	 *          the full name to set.
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Gets the value.
	 * @return the value.
	 */
	public float getValue() {
		return this.value;
	}

	/**
	 * Sets the value.
	 * @param value
	 *          the value to set.
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * Gets the parentElement.
	 * @return parentElement
	 *          the parentElement.
	 */
	@Override
	public Element getParentElement() {
		return this.parentElement;
	}

	/**
	 * Sets the parentElement.
	 * @param parentElement
	 *          the parentElement to set.
	 */
	public void setParentElement(Element parentElement) {
		this.parentElement = parentElement;
	}
}

/** IPR end **/
