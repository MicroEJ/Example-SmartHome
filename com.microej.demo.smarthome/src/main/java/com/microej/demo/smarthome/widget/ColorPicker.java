/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import java.util.concurrent.Executor;

import com.microej.demo.smarthome.Main;
import com.microej.demo.smarthome.data.light.DefaultLight;
import com.microej.demo.smarthome.data.light.Light;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.style.HomeImageLoader;
import com.microej.demo.smarthome.util.Images;
import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.widget.light.LightCircleWidget;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.generator.Buttons;
import ej.microui.event.generator.Pointer;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.util.concurrent.SingleThreadExecutor;
import ej.widget.basic.Button;
import ej.widget.basic.Image;
import ej.widget.composed.Wrapper;
import ej.widget.container.Dock;
import ej.widget.container.Grid;
import ej.widget.listener.OnClickListener;

/**
 * A color picker.
 */
public class ColorPicker extends Dock {

	private static final int CIRCLE_DIAMETER = 120;
	private static final int SELECTED_CIRCLE_RADIUS = 5;

	private final Button closeButton;
	private final Image image;
	private int selectedX;
	private int selectedY;
	private int clickX;
	private int clickY;
	private boolean pressedInside;
	private final Light initialLight;
	private final Light light;

	/**
	 * Instantiates a ColorPicker.
	 *
	 * @param light
	 *            the light model to use.
	 */
	public ColorPicker(final Light light) {
		super();
		this.light = light;
		// set class
		addClassSelector(ClassSelectors.PICKER);

		// image
		this.image = new Image(HomeImageLoader.loadImage(Images.COLOR_PICKER)) {
			@Override
			public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
				if (isVisible()) {
					super.renderContent(g, style, bounds);
					renderSelectedCircle(g, style);
				}
			}

			@Override
			public boolean isTransparent() {
				return false;
			}
		};
		this.image.addClassSelector(ClassSelectors.PICKER_PLAIN_BACKGROUND);

		final OnClickListener onClickCloseListener = new OnClickListener() {
			@Override
			public void onClick() {
				Main.backToMainPage();
			}
		};
		final OnClickListener onClickResetListener = new OnClickListener() {

			@Override
			public void onClick() {
				light.setColor(ColorPicker.this.initialLight.getColor());
				onClickCloseListener.onClick();

			}
		};

		// title label
		Button titleLabel = new Button(Strings.COLOR_PICKER_TITLE);
		titleLabel.addOnClickListener(onClickResetListener);
		titleLabel.addClassSelector(ClassSelectors.PICKER_TITLE_LABEL);

		// close button
		this.closeButton = new Button(Strings.OK);
		this.closeButton.addOnClickListener(onClickCloseListener);
		this.closeButton.addClassSelector(ClassSelectors.PICKER_CLOSE_BUTTON);

		// top bar
		final Grid topBar = new Grid(true, 2);
		topBar.add(titleLabel);
		topBar.add(this.closeButton);

		// split
		addTop(topBar);

		this.initialLight = new DefaultLight(light);
		final CircleWidget initialColorWidget = new LightCircleWidget(this.initialLight);
		final LimitedButtonWrapper initialColorWidgetButton = new LimitedButtonWrapper();
		initialColorWidgetButton.setWidget(initialColorWidget);
		initialColorWidgetButton.addOnClickListener(onClickResetListener);
		initialColorWidget.addClassSelector(ClassSelectors.LIGHT_PROGRESS);
		initialColorWidget.addClassSelector(ClassSelectors.PICKER_PLAIN_BACKGROUND);

		LightCircleWidget currentColorWidget = new LightCircleWidget(light);
		final LimitedButtonWrapper currentColorWidgetButton = new LimitedButtonWrapper();
		currentColorWidgetButton.setWidget(currentColorWidget);
		currentColorWidgetButton.addOnClickListener(onClickCloseListener);
		currentColorWidget.addClassSelector(ClassSelectors.LIGHT_PROGRESS);
		currentColorWidget.addClassSelector(ClassSelectors.PICKER_PLAIN_BACKGROUND);

		initialColorWidget.setPreferredSize(CIRCLE_DIAMETER, CIRCLE_DIAMETER);
		addLeft(initialColorWidgetButton);
		setCenter(this.image);
		currentColorWidget.setPreferredSize(CIRCLE_DIAMETER, CIRCLE_DIAMETER);
		addRight(currentColorWidgetButton);

		// set initial state
		this.selectedX = -1;
		this.selectedY = -1;
	}

	/**
	 * Renders the selected circle.
	 */
	private void renderSelectedCircle(final GraphicsContext g, final Style style) {
		// draw selected circle
		if (this.selectedX != -1 && this.selectedY != -1) {
			final int circleX = this.selectedX - SELECTED_CIRCLE_RADIUS;
			final int circleY = this.selectedY - SELECTED_CIRCLE_RADIUS;

			g.setColor(style.getForegroundColor());
			g.removeBackgroundColor();
			final AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
			antiAliasedShapes.setThickness(1);
			antiAliasedShapes.setFade(1);
			antiAliasedShapes.drawCircle(g, circleX, circleY, SELECTED_CIRCLE_RADIUS << 1);
		}
	}

	/**
	 * Handles click events.
	 */
	@Override
	public boolean handleEvent(final int event) {
		if (Event.getType(event) == Event.POINTER) {
			final Pointer pointer = (Pointer) Event.getGenerator(event);
			this.clickX = pointer.getX();
			this.clickY = pointer.getY();
			Main.SetAnchor(this.clickX, this.clickY);
			final int pointerX = this.image.getRelativeX(this.clickX);
			final int pointerY = this.image.getRelativeY(this.clickY);
			if(performTouch(Buttons.getAction(event), pointerX, pointerY)){						
				return true;
			}
		}
		return super.handleEvent(event);
	}

	/**
	 * Perform a touch event.
	 *
	 * @param action
	 *            the action of the event.
	 * @param pointerX
	 *            the x coordinate.
	 * @param pointerY
	 *            the y coordinate.
	 * @return true if the event has been handled.
	 */
	public boolean performTouch(final int action, int pointerX, int pointerY) {
		ej.microui.display.Image source = this.image.getSource();
		int width = source.getWidth();
		int height = source.getHeight();
		int widgetWidth = this.image.getWidth();
		int widgetHeight = this.image.getHeight();
		int xOffset = (widgetWidth - width) >> 1;
			int yOffset = (widgetHeight - height) >> 1;
			if (pointerX > xOffset && pointerX < widgetWidth -xOffset && pointerY > yOffset
					&& pointerY < widgetHeight-yOffset) {
				final int centerX = (width >> 1) + xOffset;
				final int centerY = (height >> 1) + yOffset;
				final int dX = pointerX - centerX;
				final int dY = pointerY - centerY;
				final int d = (int) Math.sqrt(dX * dX + dY * dY);
				final int r = getRadius();

				// Use closest position within the circle.
				if (d > r) {
					pointerX = centerX + dX * r / d;
					pointerY = centerY + dY * r / d;
				} else {
					if (action == Buttons.PRESSED) {
						this.pressedInside = true;
					}
				}

				if (this.pressedInside && (this.selectedX != pointerX || this.selectedY != pointerY)) {
					if (action == Buttons.RELEASED) {
						this.pressedInside = false;
					}

					this.selectedX = pointerX;
					this.selectedY = pointerY;
					this.image.repaint();

					final int readPixel = source.readPixel(pointerX - xOffset, pointerY - yOffset);
					this.light.setColor(readPixel);
					return true;
				}
			}
			return false;
	}

	/**
	 * Gets the circle radius.
	 * @return the circle radius.
	 */
	public int getRadius() {
		return (this.image.getSource().getWidth() >> 1) - (SELECTED_CIRCLE_RADIUS + 1);
	}

	/**
	 * Gets the image.
	 *
	 * @return the image.
	 */
	public Image getImage() {
		return this.image;
	}


	/**
	 * Use by automaton.
	 */
	public void close() {
		this.closeButton.performClick();
	}
}
