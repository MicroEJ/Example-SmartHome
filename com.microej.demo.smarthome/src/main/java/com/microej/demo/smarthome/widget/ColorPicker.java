/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import java.util.ArrayList;
import java.util.List;
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
import ej.microui.event.generator.Pointer;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.widget.basic.Image;
import ej.widget.composed.Button;
import ej.widget.composed.Wrapper;
import ej.widget.container.Dock;
import ej.widget.container.Grid;
import ej.widget.listener.OnClickListener;
import ej.widget.listener.OnValueChangeListener;

public class ColorPicker extends Dock {

	private static final int CIRCLE_DIAMETER = 120;
	private static final int INPUT_RATE = 30;
	private static final int SELECTED_CIRCLE_RADIUS = 5;

	/**
	 * Attributes
	 */
	private final List<OnValueChangeListener> listeners;
	private final Button titleLabel;
	private final Button closeButton;
	private final Image image;
	private int selectedX;
	private int selectedY;
	private int clickX;
	private int clickY;
	private boolean pressedInside;
	private long nextInput = -1;
	private final CircleWidget currentColorWidget;
	private final Light initialLight;
	private final Light light;


	/**
	 * Constructor
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
					renderSelectedCircle(g, style, bounds);
				}
			}
		};

		// image wrapper
		final Wrapper imageWrapper = new Wrapper();
		imageWrapper.setAdjustedToChild(false);
		imageWrapper.setWidget(this.image);

		final OnClickListener onClickCloseListener = new OnClickListener() {
			@Override
			public void onClick() {
				Main.goToMainPage();
			}
		};
		final OnClickListener onClickResetListener = new OnClickListener() {

			@Override
			public void onClick() {
				notifyListeners(initialLight.getColor());
				onClickCloseListener.onClick();

			}
		};

		// title label
		this.titleLabel = new Button(Strings.COLOR_PICKER_TITLE);
		titleLabel.addOnClickListener(onClickResetListener);
		this.titleLabel.addClassSelector(ClassSelectors.PICKER_TITLE_LABEL);

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

		initialLight = new DefaultLight(light);
		final CircleWidget initialColorWidget = new LightCircleWidget(initialLight);
		final LimitedButtonWrapper initialColorWidgetButton = new LimitedButtonWrapper();
		initialColorWidgetButton.setWidget(initialColorWidget);
		initialColorWidgetButton.addOnClickListener(onClickResetListener);
		initialColorWidget.addClassSelector(ClassSelectors.LIGHT_PROGRESS);

		currentColorWidget = new LightCircleWidget(light);
		final LimitedButtonWrapper currentColorWidgetButton = new LimitedButtonWrapper();
		currentColorWidgetButton.setWidget(currentColorWidget);
		currentColorWidgetButton.addOnClickListener(onClickCloseListener);
		currentColorWidget.addClassSelector(ClassSelectors.LIGHT_PROGRESS);

		initialColorWidget.setPreferredSize(CIRCLE_DIAMETER, CIRCLE_DIAMETER);
		addLeft(initialColorWidgetButton);
		setCenter(imageWrapper);
		currentColorWidget.setPreferredSize(CIRCLE_DIAMETER, CIRCLE_DIAMETER);
		addRight(currentColorWidgetButton);

		// set initial state
		this.listeners = new ArrayList<OnValueChangeListener>();
		this.selectedX = -1;
		this.selectedY = -1;
	}

	// /**
	// * Renders the widget
	// */
	// @Override
	// public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
	// // render parent
	// g.setColor(style.getBackgroundColor());
	// final int x = g.getClipX();
	// final int y = g.getClipY();
	// g.fillRect(x, y, x + g.getClipWidth(), y + g.getClipHeight());
	// }

	/**
	 * Renders the selected circle
	 */
	public void renderSelectedCircle(final GraphicsContext g, final Style style, final Rectangle bounds) {
		// draw selected circle
		if (this.selectedX != -1 && this.selectedY != -1) {
			final int circleX = this.selectedX - SELECTED_CIRCLE_RADIUS;
			final int circleY = this.selectedY - SELECTED_CIRCLE_RADIUS;

			g.setColor(style.getForegroundColor());
			g.removeBackgroundColor();
			final AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
			antiAliasedShapes.setThickness(1);
			antiAliasedShapes.setFade(1);
			antiAliasedShapes.drawCircle(g, circleX, circleY, 2*SELECTED_CIRCLE_RADIUS);
		}
	}

	/**
	 * Handles click events
	 */
	@Override
	public boolean handleEvent(final int event) {
		final long currentTime = System.currentTimeMillis();
		if (currentTime > nextInput) {
			if (nextInput == -1) {
				nextInput = 0;
			} else {
				nextInput = currentTime + INPUT_RATE;
			}
			if (Event.getType(event) == Event.POINTER) {
				final Pointer pointer = (Pointer) Event.getGenerator(event);
				clickX = pointer.getX();
				clickY = pointer.getY();
				final int action = Pointer.getAction(event);
				switch(action) {
				case Pointer.PRESSED:
				case Pointer.DRAGGED:
				case Pointer.RELEASED:
					Main.getTransitionManager().setTarget(clickX, clickY);
					final int pointerX = this.image.getRelativeX(clickX);
					final int pointerY = this.image.getRelativeY(clickY);
					if (pointerX > 0 && pointerX < this.image.getWidth() && pointerY > 0
							&& pointerY < this.image.getHeight()) {
						performClick(action, pointerX, pointerY);
						return true;
					}
				}
			}
			return super.handleEvent(event);
		}
		return true;
	}

	/**
	 * @param pointerX
	 * @param pointerY
	 */
	public boolean performClick(final int action, int pointerX, int pointerY) {
		final int centerX = (this.image.getWidth() >> 1);
		final int centerY = this.image.getHeight() >> 1;
		final int dX = pointerX - centerX;
		final int dY = pointerY - centerY;
		final int d = (int) Math.sqrt(dX * dX + dY * dY);
		final int r = getRadius();

		// Use closest position within the circle.
		if (d > r) {
			pointerX = centerX + dX * r / d;
			pointerY = centerY + dY * r / d;
		} else {
			if (action == Pointer.PRESSED) {
				pressedInside = true;
			}
		}

		if (!pressedInside || (selectedX == pointerX && selectedY != pointerY)) {
			return false;
		}

		if (action == Pointer.RELEASED) {
			pressedInside = false;
		}

		this.selectedX = pointerX;
		this.selectedY = pointerY;
		image.repaint();
		final int readPixel = image.getSource().readPixel(selectedX, selectedY);
		notifyListeners(readPixel);
		return true;
	}

	/**
	 * Gets the circle radius
	 */
	public int getRadius() {
		return (this.image.getWidth() >> 1) - (SELECTED_CIRCLE_RADIUS + 1);
	}

	/**
	 * Adds a listener
	 */
	public void addOnValueChangeListener(final OnValueChangeListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Notifies the listeners that a new color has been picked
	 */
	private void notifyListeners(final int color) {
		light.setColor(color);
		ServiceLoaderFactory.getServiceLoader().getService(Executor.class).execute(new Runnable() {

			@Override
			public void run() {
				for (final OnValueChangeListener listener : listeners) {
					listener.onValueChange(color);
				}
			}
		});
	}

	/**
	 * Gets the image.
	 *
	 * @return the image.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Gets the closeButton.
	 * @return the closeButton.
	 */
	public Button getCloseButton() {
		return closeButton;
	}
}
