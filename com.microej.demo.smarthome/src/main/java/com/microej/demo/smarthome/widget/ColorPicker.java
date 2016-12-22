/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.style.HomeImageLoader;
import com.microej.demo.smarthome.util.Images;
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
import ej.mwt.Composite;
import ej.mwt.Panel;
import ej.mwt.Widget;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.widget.StyledComposite;
import ej.widget.StyledWidget;
import ej.widget.basic.Image;
import ej.widget.composed.Button;
import ej.widget.composed.Wrapper;
import ej.widget.container.Dock;
import ej.widget.container.Grid;
import ej.widget.listener.OnClickListener;
import ej.widget.listener.OnValueChangeListener;
import ej.widget.util.DrawScreenHelper;

public class ColorPicker extends Dock implements Animation {

	private static final int CIRCLE_DIAMETER = 120;
	private static final int INPUT_RATE = 30;
	private static final int SELECTED_CIRCLE_RADIUS = 5;
	private static final int MAX_CIRCLE_RADIUS = 420;
	private static final int ANIM_NUM_STEPS = MAX_CIRCLE_RADIUS;
	private static final int ANIM_DURATION = 250;

	/**
	 * Attributes
	 */
	private int sourceX;
	private int sourceY;
	private final List<OnValueChangeListener> listeners;
	private OnClickListener closeButtonListener;
	private final Button titleLabel;
	private final Button closeButton;
	private final Image image;
	private Motion motion;
	private boolean closeAnim;
	private int currentAnimStep;
	private int selectedX;
	private int selectedY;
	private int clickX;
	private int clickY;
	private boolean pressedInside;
	private long nextInput = -1;
	private ej.microui.display.Image screenshot;
	private final CircleWidget currentColorWidget;
	private final Panel background;


	/**
	 * Constructor
	 */
	public ColorPicker(int sourceX, int sourceY, int initialColor, Panel background) {
		super();
		this.background = background;
		// set class
		addClassSelector(ClassSelectors.PICKER);

		// image
		this.image = new Image(HomeImageLoader.loadImage(Images.COLOR_PICKER)) {
			@Override
			public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
				if (isVisible()) {
					super.renderContent(g, style, bounds);
					renderSelectedCircle(g, style, bounds);
				}
			}
		};

		// image wrapper
		Wrapper imageWrapper = new Wrapper();
		imageWrapper.setAdjustedToChild(false);
		imageWrapper.setWidget(this.image);

		OnClickListener onClickCloseListener = new OnClickListener() {
			@Override
			public void onClick() {
				playCloseAnimation();
			}
		};
		OnClickListener onClickResetListener = new OnClickListener() {

			@Override
			public void onClick() {
				notifyListeners(initialColor);
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
		Grid topBar = new Grid(true, 2);
		topBar.add(titleLabel);
		topBar.add(this.closeButton);

		// split
		addTop(topBar);

		CircleWidget initialColorWidget = new CircleWidget(onClickResetListener);
		initialColorWidget.setColor(initialColor);
		initialColorWidget.addClassSelector(ClassSelectors.LIGHT_PROGRESS);

		currentColorWidget = new CircleWidget(onClickCloseListener);
		currentColorWidget.setColor(initialColor);
		currentColorWidget.addClassSelector(ClassSelectors.LIGHT_PROGRESS);

		initialColorWidget.setPreferredSize(CIRCLE_DIAMETER, CIRCLE_DIAMETER);
		addLeft(initialColorWidget);
		setCenter(imageWrapper);
		currentColorWidget.setPreferredSize(CIRCLE_DIAMETER, CIRCLE_DIAMETER);
		addRight(currentColorWidget);

		// set initial state
		this.listeners = new ArrayList<OnValueChangeListener>();
		this.selectedX = -1;
		this.selectedY = -1;

		this.sourceX = sourceX;
		this.sourceY = sourceY;
	}

	/**
	 * Renders the widget
	 */
	@Override
	public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
		// render parent
		// super.renderContent(g, style, bounds);

		if (currentAnimStep < ANIM_NUM_STEPS) {
			g.drawImage(screenshot, 0, 0, GraphicsContext.TOP | GraphicsContext.LEFT);
			// fill circle
			int circleR = MAX_CIRCLE_RADIUS * this.currentAnimStep / ANIM_NUM_STEPS;
			int circleX = sourceX - circleR;
			int circleY = sourceY - circleR;
			int diameter = circleR << 1;
			g.setColor(style.getBackgroundColor());
			g.removeBackgroundColor();
			g.fillCircle(circleX, circleY, diameter);
			// g.drawCircle(circleX, circleY, diameter);


			// draw anti aliased circle
			// AntiAliasedShapes antiAliasedShapes = AntiAliasedShapes.Singleton;
			// antiAliasedShapes.setThickness(2);
			// antiAliasedShapes.setFade(1);
			// antiAliasedShapes.drawCircle(g, circleX, circleY, diameter);
		} else {
			g.setColor(style.getBackgroundColor());
			int x = g.getClipX();
			int y = g.getClipY();
			g.fillRect(x, y, x + g.getClipWidth(), y + g.getClipHeight());
		}
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
		long currentTime = System.currentTimeMillis();
		if (currentTime > nextInput) {
			if (nextInput == -1) {
				nextInput = 0;
			} else {
				nextInput = currentTime + INPUT_RATE;
			}
			if (Event.getType(event) == Event.POINTER) {
				Pointer pointer = (Pointer) Event.getGenerator(event);
				int action = Pointer.getAction(event);
				switch(action) {
				case Pointer.PRESSED:
				case Pointer.DRAGGED:
				case Pointer.RELEASED:
					clickX = pointer.getX();
					clickY = pointer.getY();
					int pointerX = this.image.getRelativeX(clickX);
					int pointerY = this.image.getRelativeY(clickY);
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
	public boolean performClick(int action, int pointerX, int pointerY) {
		int centerX = this.image.getWidth() >> 1;
					int centerY = this.image.getHeight() >> 1;
					int dX = pointerX - centerX;
					int dY = pointerY - centerY;
					int d = (int) Math.sqrt(dX * dX + dY * dY);
					int r = getRadius();

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
					notifyListeners(image.getSource().readPixel(selectedX, selectedY));
					return true;

	}

	/**
	 * Gets the circle radius
	 */
	public int getRadius() {
		return (this.image.getWidth() >> 1) - (SELECTED_CIRCLE_RADIUS + 1);
	}

	/**
	 * Plays the open animation
	 */
	private void playOpenAnimation() {
		screenshot = getPanel().getDesktop().getDisplay().getScreenshot();
		this.motion = new LinearMotion(0, ANIM_NUM_STEPS, ANIM_DURATION);
		this.currentAnimStep = 0;
		this.closeAnim = false;
		for (Widget widget : getWidgets()) {
			hideWidgets(widget);
		}

		Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
		animator.startAnimation(this);
	}

	@Override
	public void showNotify() {
		super.showNotify();
		// start animation
		// playOpenAnimation();
	}

	/**
	 * Plays the close animation
	 */
	private void playCloseAnimation() {
		if (motion.isFinished()) {
			DrawScreenHelper.draw(screenshot.getGraphicsContext(), background);
			this.sourceX = clickX;
			this.sourceY = clickY;
			this.motion = new LinearMotion(ANIM_NUM_STEPS, 0, ANIM_DURATION);
			this.currentAnimStep = ANIM_NUM_STEPS;
			this.closeAnim = true;

			Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
			animator.startAnimation(this);
		}
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		// Starts animation.
		playOpenAnimation();
	}

	/**
	 * Updates the animation
	 */
	@Override
	public boolean tick(long currentTimeMillis) {
		boolean finished = this.motion.isFinished();
		this.currentAnimStep = this.motion.getCurrentValue();
		// showWidgets();
		for (Widget widget : getWidgets()) {
			showWidgets(widget);
		}
		repaint();
		return !finished;
	}

	private void showWidgets(Widget w) {
		if (w instanceof Composite) {
			for (Widget widget : ((Composite) w).getWidgets()) {
				showWidgets(widget);
			}
			return;
		}
		if (changeState(w)) {
			w.setVisible(!this.closeAnim);
			if (w instanceof StyledComposite) {
				((StyledComposite) w).partialRevalidate();
			} else {
				((StyledWidget) w).partialRevalidate();
			}
		}
		// close dialog
		if (this.closeAnim && this.currentAnimStep == 0) {
			this.closeButtonListener.onClick();
		}
	}

	private void hideWidgets(Widget w) {
		if (w instanceof Composite) {
			for (Widget widget : ((Composite) w).getWidgets()) {
				hideWidgets(widget);
			}
			return;
		}
		w.setVisible(false);
	}

	/**
	 * @param w
	 * @return
	 */
	private boolean changeState(Widget w) {
		int r = MAX_CIRCLE_RADIUS * this.currentAnimStep / ANIM_NUM_STEPS;
		int xStart = w.getAbsoluteX();
		int xEnd = xStart + w.getWidth();
		int yStart = w.getAbsoluteY();
		int yEnd = xStart + w.getHeight();
		int showStartX = sourceX - r;
		int showEndX = sourceX + r;
		int showStartY = sourceY - r;
		int showEndY = sourceY + r;

		if (closeAnim) {
			return w.isVisible() && (xEnd > showEndX || xStart < showStartX || yEnd > showEndY || yStart < showStartY);
		} else {
			return !w.isVisible() && (xEnd < showEndX && xStart > showStartX)
					&& (yEnd < showEndY && yStart > showStartY);
		}
	}

	/**
	 * Sets the close button listener
	 */
	public void setCloseButtonListener(OnClickListener listener) {
		this.closeButtonListener = listener;
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
		currentColorWidget.setColor(color);
		ServiceLoaderFactory.getServiceLoader().getService(Executor.class).execute(new Runnable() {

			@Override
			public void run() {
				for (OnValueChangeListener listener : listeners) {
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

	@Override
	public void validate(int widthHint, int heightHint) {
		super.validate(widthHint, heightHint);
	}

	@Override
	public boolean isTransparent() {
		return false;
	}
}
