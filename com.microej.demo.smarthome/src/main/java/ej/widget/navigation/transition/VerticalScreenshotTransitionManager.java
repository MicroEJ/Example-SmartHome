/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ej.widget.navigation.transition;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.display.Display;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.motion.Motion;
import ej.motion.linear.LinearMotion;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.widget.navigation.Navigator;
import ej.widget.navigation.TransitionManager;
import ej.widget.navigation.page.Page;
import ej.widget.util.DrawScreenHelper;

/**
 * A transition manager that makes the pages slide horizontally.
 * <p>
 * A screenshot of the pages is done at the beginning of the transition, then for each step of the transition, these
 * images are drawn.
 * <p>
 * This transition manager allows faster transitions than {@link HorizontalTransitionManager} but requires more runtime
 * memory.
 */
public class VerticalScreenshotTransitionManager extends TransitionManager {

	private static final long DURATION = 300;
	/**
	 * Arbitrary desktop ratio to decide to go back or to stay on current page.
	 */
	private static final int MIN_SHIFT_RATIO = 10;

	private int contentX;
	private int contentY;
	private Image oldImage;
	private Image newImage;
	private boolean animating;
	private int shift;
	private int currentY;

	// Drag state.
	private boolean pressed;
	private int pressedX;
	private boolean dragged;

	private Page previousPage;
	private Page nextPage;
	private boolean forward;

	@Override
	public void animate(Page oldPage, Page newPage, boolean forward) {
		this.forward = forward;
		Navigator navigation = getNavigator();
		Rectangle contentBounds = getContentBounds();

		int height = navigation.getHeight();
		this.contentX = contentBounds.getX();
		this.contentY = contentBounds.getY();
		int contentWidth = contentBounds.getWidth();
		int contentHeight = contentBounds.getHeight();

		if (!forward) {
			this.shift = height;
		} else {
			this.shift = -height;
		}

		int startY = 0;

		// Create image of the current page.
		createCurrentScreenshot(oldPage, contentWidth, contentHeight);

		// Create image of the new page.
		createNewPageScreenshot(newPage, contentWidth, contentHeight);

		notifyTransitionStart(startY, -shift, oldPage, newPage);

		long duration = DURATION - (DURATION * Math.abs(startY) / Math.abs(this.shift));
		Motion motion = new LinearMotion(startY, -this.shift, duration);

		this.animating = true;
		Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class, Animator.class);
		animator.startAnimation(new VerticalScreenshotAnimation(navigation, newPage, oldPage, motion));
	}

	private void createCurrentScreenshot(Page oldPage, int contentWidth, int contentHeight) {
		this.oldImage = Image.createImage(contentWidth, contentHeight);
		GraphicsContext g = this.oldImage.getGraphicsContext();
		g.drawRegion(Display.getDefaultDisplay(), oldPage.getAbsoluteX(), oldPage.getAbsoluteY(), contentWidth,
				contentHeight, 0, 0, GraphicsContext.LEFT | GraphicsContext.TOP);
		removePage(oldPage);
	}

	private void createNewPageScreenshot(Page newPage, int contentWidth, int contentHeight) {
		this.newImage = createPageScreenshot(newPage, contentWidth, contentHeight);
	}

	private Image createPageScreenshot(Page page, int contentWidth, int contentHeight) {
		page.validate(contentWidth, contentHeight);
		page.setSize(contentWidth, contentHeight);
		Image image = Image.createImage(contentWidth, contentHeight);
		GraphicsContext g = image.getGraphicsContext();
		DrawScreenHelper.draw(g, page);
		return image;
	}

	@Override
	public void render(GraphicsContext g, Style style, Rectangle bounds) {
		if (this.animating || this.dragged) {
			int contentY = this.contentY;
			Image front = this.oldImage;
			Image back = this.newImage;
			int shift = contentY + this.shift + currentY;
			if (!forward) {
				front = newImage;
				back = oldImage;
				shift = contentY + currentY;
			}
			g.drawImage(front, contentX, contentY, GraphicsContext.LEFT | GraphicsContext.TOP);
			g.drawImage(back, contentX, shift,
					GraphicsContext.LEFT | GraphicsContext.TOP);
		}
	}

	@Override
	public boolean handleEvent(int event) {
		if (canGoBackward() || canGoForward()) {
			if (Event.getType(event) == Event.POINTER) {
				Pointer pointer = (Pointer) Event.getGenerator(event);
				int pointerX = pointer.getX();
				switch (Pointer.getAction(event)) {
				case Pointer.PRESSED:
					return onPointerPressed(pointerX);
				case Pointer.RELEASED:
					return onPointerReleased(pointerX);
				case Pointer.DRAGGED:
					return false;
				}
			}
		}
		return super.handleEvent(event);
	}

	private boolean onPointerPressed(int pointerX) {
		// Initiate drag.
		this.pressed = true;
		this.pressedX = pointerX;
		this.dragged = false;
		return false;
	}

	private void resetDragState() {
		// Reset drag state.
		this.dragged = false;
		this.previousPage = null;
		this.nextPage = null;
	}


	private boolean onPointerReleased(int pointerX) {
		this.pressed = false;
		if (this.dragged) {
			Navigator navigation = getNavigator();
			Page currentPage = navigation.getCurrentPage();
			int width = navigation.getWidth();
			int moveX = pointerX - this.pressedX;
			int minShiftX = width / MIN_SHIFT_RATIO;
			if (moveX > minShiftX) {
				// Go backward.
				removePage(this.previousPage);
				goBackward();
			} else if (-moveX > minShiftX) {
				// Go forward.
				removePage(this.nextPage);
				goForward();
			} else {
				// Restore current page.
				show(currentPage, moveX > 0);
			}
			return true;
		}
		return false;
	}

	private void resetAnimationState() {
		this.animating = false;
		this.oldImage = null;
		this.newImage = null;
	}

	private final class VerticalScreenshotAnimation implements Animation {
		private final Page newPage;
		private final Navigator navigation;
		private final Motion motion;
		private final Display display;

		private VerticalScreenshotAnimation(Navigator navigation, Page newPage, Page oldPage, Motion motion) {
			this.newPage = newPage;
			this.navigation = navigation;
			this.motion = motion;
			this.display = Display.getDefaultDisplay();
			startTransition();
		}

		@Override
		public boolean tick(long currentTimeMillis) {
			int currentValue = this.motion.getCurrentValue();
			boolean finished = motion.isFinished();
			VerticalScreenshotTransitionManager.this.currentY = currentValue;
			this.navigation.repaint();
			if (finished) {
				this.display.callSerially(new Runnable() {
					@Override
					public void run() {
						end();
					}
				});
			}
			return !finished;
		}

		private void end() {
			Page newPage = this.newPage;
			addPage(newPage);
			setCurrentPage(newPage);
			setChildBounds(newPage);
			this.navigation.repaint();
			// this.oldPage.onTransitionStop();
			// newPage.onTransitionStop();
			resetAnimationState();
			resetDragState();
			stopTransition();
			notifyTransitionStop();
		}

	}

}