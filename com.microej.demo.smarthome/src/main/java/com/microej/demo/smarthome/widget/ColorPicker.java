/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import java.util.ArrayList;
import java.util.List;

import com.microej.demo.smarthome.Main;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.util.Strings;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.motion.Motion;
import ej.motion.linear.LinearMotion;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.widget.basic.Image;
import ej.widget.basic.Label;
import ej.widget.composed.Button;
import ej.widget.composed.Wrapper;
import ej.widget.container.Split;
import ej.widget.listener.OnClickListener;
import ej.widget.listener.OnValueChangeListener;

public class ColorPicker extends Wrapper implements Animation {

	/**
	 * Values
	 */
	private static final String TITLE = "Pick a color";
	private static final String IMAGE_FILENAME = "/images/color_picker.png";
	private static final int SELECTED_CIRCLE_RADIUS = 5;
	private static final int MAX_CIRCLE_RADIUS = 420;
	private static final int ANIM_NUM_STEPS = MAX_CIRCLE_RADIUS;
	private static final int ANIM_DURATION = 450;
	private static final float TOP_BAR_RATIO = 0.70f;
	private static final float SPLIT_RATIO = 0.135f;

	/**
	 * Attributes
	 */
	private final int sourceX;
	private final int sourceY;
	private final List<OnValueChangeListener> listeners;
	private final Label titleLabel;
	private final Button closeButton;
	private final Image image;

	private final Motion motion;
	private int currentAnimStep;
	private int selectedX;
	private int selectedY;
	private boolean pressedInside;

	/**
	 * Constructor
	 */
	public ColorPicker(int sourceX, int sourceY) {
		// set class
		addClassSelector(ClassSelectors.PICKER);

		// image
		this.image = new Image(IMAGE_FILENAME) {
			@Override
			public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
				super.renderContent(g, style, bounds);
				renderSelectedCircle(g, style, bounds);
			}
		};

		// image wrapper
		Wrapper imageWrapper = new Wrapper();
		imageWrapper.setAdjustedToChild(false);
		imageWrapper.setWidget(this.image);

		// title label
		this.titleLabel = new Label(TITLE);
		this.titleLabel.addClassSelector(ClassSelectors.PICKER_TITLE_LABEL);

		// close button
		this.closeButton = new Button(Strings.OK);
		this.getCloseButton().addOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				Main.showDesktop();
			}
		});
		this.getCloseButton().addClassSelector(ClassSelectors.PICKER_CLOSE_BUTTON);

		// top bar
		Split topBar = new Split(true, TOP_BAR_RATIO);
		topBar.setFirst(titleLabel);
		topBar.setLast(this.getCloseButton());

		// split
		Split split = new Split(false, SPLIT_RATIO);
		split.setFirst(topBar);
		split.setLast(imageWrapper);
		setWidget(split);

		// set initial state
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.listeners = new ArrayList<OnValueChangeListener>();
		this.motion = new LinearMotion(0, ANIM_NUM_STEPS, ANIM_DURATION);
		this.currentAnimStep = 0;
		this.selectedX = -1;
		this.selectedY = -1;

		// hide widgets
		this.titleLabel.setVisible(false);
		this.getCloseButton().setVisible(false);
		this.image.setVisible(false);

		// start animation
		Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
		animator.startAnimation(this);
	}

	/**
	 * Renders the widget
	 */
	@Override
	public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
		// render parent
		super.renderContent(g, style, bounds);

		// fill circle
		int circleR = MAX_CIRCLE_RADIUS * this.currentAnimStep / ANIM_NUM_STEPS;
		int circleX = sourceX - circleR;
		int circleY = sourceY - circleR;
		g.setColor(style.getBackgroundColor());
		g.removeBackgroundColor();
		g.fillCircle(circleX, circleY, 2*circleR);

		// draw anti aliased circle
		AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
		antiAliasedShapes.setThickness(2);
		antiAliasedShapes.setFade(1);
		antiAliasedShapes.drawCircle(g, circleX, circleY, 2*circleR);
	}

	/**
	 * Renders the selected circle
	 */
	public void renderSelectedCircle(GraphicsContext g, Style style, Rectangle bounds) {
		// draw selected circle
		if (this.selectedX != -1 && this.selectedY != -1) {
			int circleX = this.selectedX - SELECTED_CIRCLE_RADIUS;
			int circleY = this.selectedY - SELECTED_CIRCLE_RADIUS;

			g.setColor(style.getForegroundColor());
			g.removeBackgroundColor();
			AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
			antiAliasedShapes.setThickness(1);
			antiAliasedShapes.setFade(1);
			antiAliasedShapes.drawCircle(g, circleX, circleY, 2*SELECTED_CIRCLE_RADIUS);
		}
	}

	/**
	 * Handles click events
	 */
	@Override
	public boolean handleEvent(int event) {
		if (Event.getType(event) == Event.POINTER) {
			Pointer pointer = (Pointer) Event.getGenerator(event);
			int action = Pointer.getAction(event);
			switch(action) {
			case Pointer.PRESSED:
			case Pointer.DRAGGED:
			case Pointer.RELEASED:
				int pointerX = this.image.getRelativeX(pointer.getX());
				int pointerY = this.image.getRelativeY(pointer.getY());
				if (performClick(action, pointerX, pointerY)) {
					return true;
				}
			}
		}
		return super.handleEvent(event);
	}

	/**
	 * @param pointerX
	 * @param pointerY
	 */
	public boolean performClick(int action, int pointerX, int pointerY) {
		int dX = pointerX - this.image.getWidth() / 2;
		int dY = pointerY - this.image.getHeight() / 2;
		int d = (int) Math.sqrt(dX * dX + dY * dY);
		int r = this.image.getWidth() / 2 - (SELECTED_CIRCLE_RADIUS + 1);

		if (d > r) {
			pointerX = this.image.getWidth() / 2 + dX * r / d;
			pointerY = this.image.getHeight() / 2 + dY * r / d;
		} else {
			if (action == Pointer.PRESSED) {
				pressedInside = true;
			}
		}

		if (!pressedInside) {
			return false;
		}

		if (action == Pointer.RELEASED) {
			pressedInside = false;
		}

		this.selectedX = pointerX;
		this.selectedY = pointerY;
		repaint();

		int[] argbData = new int[1];
		this.image.getSource().getARGB(argbData, 0, 1, pointerX, pointerY, 1, 1);
		notifyListeners(argbData[0]);
		return true;

	}


	/**
	 * Updates the animation
	 */
	@Override
	public boolean tick(long currentTimeMillis) {
		this.currentAnimStep = this.motion.getCurrentValue();
		showWidgets();
		repaint();
		return !this.motion.isFinished();
	}

	/**
	 * Shows the widgets once they
	 */
	private void showWidgets() {
		// when to show the widgets (animation ratios)
		float[] showTitle = new float[] { 0.28f, 0.60f, 0.95f };
		float[] showImage = new float[] { 0.65f, 0.35f, 0.65f };
		float[] showClose = new float[] { 0.95f, 0.60f, 0.28f };

		int position = 1; // center
		if (this.sourceX < getWidth()/3) {
			position = 0; // left
		} else if (this.sourceX >= getWidth()*2/3) {
			position = 2; // right
		}

		// show title label
		if (!this.titleLabel.isVisible() && this.currentAnimStep >= ANIM_NUM_STEPS*showTitle[position]) {
			this.titleLabel.setVisible(true);
			this.titleLabel.revalidate();
		}

		// show image
		if (!this.image.isVisible() && this.currentAnimStep >= ANIM_NUM_STEPS*showImage[position]) {
			this.image.setVisible(true);
			this.image.revalidate();
		}

		// show close button
		if (!this.getCloseButton().isVisible() && this.currentAnimStep >= ANIM_NUM_STEPS*showClose[position]) {
			this.getCloseButton().setVisible(true);
			this.getCloseButton().revalidate();
		}
	}

	/**
	 * Adds a listener
	 */
	public void addOnValueChangeListener(OnValueChangeListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Notifies the listeners that a new color has been picked
	 */
	private void notifyListeners(int color) {
		for (OnValueChangeListener listener : this.listeners) {
			listener.onValueChange(color);
		}
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
