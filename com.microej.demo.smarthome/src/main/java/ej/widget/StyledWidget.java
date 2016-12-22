/*
 * Java
 *
 * Copyright 2015 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package ej.widget;

import ej.microui.display.Display;
import ej.microui.display.GraphicsContext;
import ej.mwt.Widget;
import ej.style.Element;
import ej.style.State;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.selector.InstanceSelector;
import ej.style.util.StyleHelper;

/**
 * A widget using a style specified in a style sheet. Especially the style sheet defines a margin, a border and a
 * padding for each widget.
 */
public abstract class StyledWidget extends Widget implements Element, StyledRenderable {

	// Some (most) of the code in this class is duplicated in all StyledRenderable implementors for footprint
	// optimization (flash & RAM).

	/**
	 * The cached transparent state.
	 */
	private boolean transparent;
	/**
	 * The cached style.
	 */
	private Style style;
	/**
	 * The class selectors.
	 */
	private String[] classSelectors;

	/**
	 * Creates a styled widget without class selector.
	 */
	protected StyledWidget() {
		this.classSelectors = StyledHelper.EMPTY_STRING_ARRAY;
	}

	@Override
	public boolean isTransparent() {
		return this.transparent;
	}

	@Override
	public Element getParentElement() {
		return StyledHelper.getParentElement(this);
	}

	@Override
	public Element[] getChildrenElements() {
		return EMPTY_ELEMENT_ARRAY;
	}

	@Override
	public Element getChild(int index) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public int getChildrenCount() {
		return 0;
	}

	@Override
	public void mergeStyle(Style style) {
		StyleHelper.getStylesheet().addRule(new InstanceSelector(this), style);
		updateStyle();
	}

	@Override
	public void removeInstanceStyle() {
		StyleHelper.getStylesheet().removeRule(new InstanceSelector(this));
		updateStyle();
	}

	@Override
	public Style getStyle() {
		if (this.style == null) {
			setStyle(StyledHelper.getStyleFromStylesheet(this));
		}
		return this.style;
	}

	private void setStyle(Style newStyle) {
		this.style = newStyle;
		this.transparent = this.style.getBackground().isTransparent();
	}

	/**
	 * Updates the style of the widget.
	 */
	protected void updateStyle() {
		// Do not update style if not shown. See validate method.
		if (isShown() && updateStyleOnly()) {
			repaint();
		}
	}

	/**
	 * Update widget style without repainting it.
	 *
	 * @return <code>true</code> if the style has been updated, <code>false</code> otherwise.
	 */
	protected boolean updateStyleOnly() {
		Style newStyle = StyledHelper.getStyleFromStylesheet(this);

		if (!newStyle.equals(this.style)) {
			setStyle(newStyle);
			return true;
		}
		return false;
	}

	/**
	 * Gets the content bounds of this widget. That means the size minus the different outlines.
	 *
	 * @return the content bounds.
	 * @see #getWidth()
	 * @see #getHeight()
	 * @see StyleHelper#computeContentBounds(Rectangle, Style)
	 */
	protected Rectangle getContentBounds() {
		Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
		return StyleHelper.computeContentBounds(bounds, getStyle());
	}

	@Override
	public void render(GraphicsContext g) {
		StyledHelper.render(g, this);
	}

	@Override
	public void validate(int widthHint, int heightHint) {
		if (isVisible()) {
			setStyle(StyledHelper.getStyleFromStylesheet(this));
			Rectangle bounds = StyledHelper.validate(widthHint, heightHint, this);
			setPreferredSize(bounds.getWidth(), bounds.getHeight());
		} else {
			setPreferredSize(0, 0);
		}
	}

	@Override
	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		updateStyle();
	}

	@Override
	public boolean hasClassSelector(String classSelector) {
		return StyledHelper.hasClassSelector(this.classSelectors, classSelector);
	}

	@Override
	public void addClassSelector(String classSelector) {
		this.classSelectors = StyledHelper.addClassSelector(this.classSelectors, classSelector);
		updateStyle();
	}

	@Override
	public void removeClassSelector(String classSelector) {
		this.classSelectors = StyledHelper.removeClassSelector(this.classSelectors, classSelector);
		updateStyle();
	}

	@Override
	public void setClassSelectors(String classSelector) {
		this.classSelectors = StyledHelper.setClassSelectors(classSelector);
		updateStyle();
	}

	@Override
	public void removeAllClassSelectors() {
		this.classSelectors = StyledHelper.EMPTY_STRING_ARRAY;
		updateStyle();
	}

	@Override
	public boolean isInState(State state) {
		return (state == State.Enabled && isEnabled()) || (state == State.Disabled && !isEnabled());
	}

	@Override
	public String getAttribute(String attribute) {
		return null;
	}

	/**
	 *
	 */
	public void partialRevalidate() {
		Display.getDefaultDisplay().callSerially(new Runnable() {
			@Override
			public void run() {
				int buttonWrapperX = getX();
				int buttonWrapperY = getY();
				int buttonWrapperWidth = getWidth();
				int buttonWrapperHeight = getHeight();
				validate(buttonWrapperWidth, buttonWrapperHeight);
				setBounds(buttonWrapperX, buttonWrapperY, buttonWrapperWidth, buttonWrapperHeight);
			}
		});

	}

}
