/*
 * Java
 *
 * Copyright 2015 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ej.widget.navigation;

import ej.microui.display.GraphicsContext;
import ej.mwt.Widget;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.StyleHelper;
import ej.widget.composed.Wrapper;
import ej.widget.navigation.page.Page;

/**
 * Manages navigation between several pages.
 */
public class Navigator extends Wrapper {

	/**
	 * The current page.
	 */
	private Page currentPage;

	private TransitionManager transitionManager;

	/**
	 * Gets the current page.
	 *
	 * @return the current page.
	 */
	public Page getCurrentPage() {
		return this.currentPage;
	}

	/**
	 * Sets the current page.
	 *
	 * @param page
	 *            the page to set as current one.
	 */
	protected void setCurrentPage(Page page) {
		this.currentPage = page;
	}

	/**
	 * Sets the transition manager.
	 *
	 * @param transitionManager
	 *            the transition manager to set.
	 * @throws IllegalArgumentException
	 *             if the given transition manager is already associated with a navigation.
	 */
	public void setTransitionManager(TransitionManager transitionManager) {
		TransitionManager previousTransitionManager = this.transitionManager;
		if (previousTransitionManager != null) {
			previousTransitionManager.setNavigator(null);
		}
		transitionManager.setNavigator(this);
		this.transitionManager = transitionManager;
	}

	/**
	 * Gets the transition manager.
	 *
	 * @return the transition manager.
	 */
	public TransitionManager getTransitionManager() {
		return this.transitionManager;
	}

	/**
	 * Shows a new page. If there is already a page shown, it is hidden.
	 * <p>
	 * If a transition manager has been set, it will be used to animate the transition.
	 *
	 * @param newPage
	 *            the new page to show.
	 * @param forward
	 *            <code>true</code> if going to a new page, <code>false</code> if going back in the stack of pages.
	 */
	protected void show(Page newPage, boolean forward) {
		TransitionManager transitionManager = this.transitionManager;
		Page currentPage = this.currentPage;
		if (transitionManager != null) {
			transitionManager.animate(currentPage, newPage, forward);
		} else {
			updateCurrentPage(newPage);
			setChildBounds(newPage);
			repaint();
		}
	}

	/**
	 * Gets whether or not it is possible to go backward.
	 *
	 * @return <code>true</code> if it is possible to go backward, <code>false</code> otherwise.
	 */
	protected boolean canGoBackward() {
		return false;
	}

	/**
	 * Gets the previous page.
	 *
	 * @return the previous page or <code>null</code> if cannot go backward.
	 */
	protected Page getPreviousPage() {
		return null;
	}

	/**
	 * Shows the previous page.
	 * <p>
	 * Nothing is done if cannot go backward.
	 */
	protected void goBackward() {
		// Do nothing.
	}

	/**
	 * Gets whether or not it is possible to go forward.
	 *
	 * @return <code>true</code> if it is possible to go forward, <code>false</code> otherwise.
	 */
	protected boolean canGoForward() {
		return false;
	}

	/**
	 * Gets the next page.
	 *
	 * @return the next page or <code>null</code> if cannot go forward.
	 */
	protected Page getNextPage() {
		return null;
	}

	/**
	 * Shows the next page.
	 * <p>
	 * Nothing is done if cannot go forward.
	 */
	protected void goForward() {
		// Do nothing.
	}

	@Override
	public void renderContent(GraphicsContext g, Style style, Rectangle bounds) {
		TransitionManager transitionManager = this.transitionManager;
		if (transitionManager != null) {
			transitionManager.render(g, style, bounds);
		}
	}

	@Override
	public boolean handleEvent(int event) {
		TransitionManager transitionManager = this.transitionManager;
		if (transitionManager != null) {
			return transitionManager.handleEvent(event);
		} else {
			return super.handleEvent(event);
		}
	}

	/**
	 * Replaces the current page by the new one.
	 *
	 * @param newPage
	 *            the new page.
	 */
	protected void updateCurrentPage(Page newPage) {
		Page currentPage = this.currentPage;
		if (currentPage != null) {
			remove(currentPage);
		}
		this.currentPage = newPage;
		add(newPage);
	}

	/**
	 * Sets the bounds of a new page.
	 *
	 * @param newPage
	 *            the new page.
	 * @see #getContentBounds()
	 */
	protected void setChildBounds(Page newPage) {
		Rectangle contentBounds = getContentBounds();
		newPage.validate(contentBounds.getWidth(), contentBounds.getHeight());
		newPage.setBounds(contentBounds.getX(), contentBounds.getY(), contentBounds.getWidth(),
				contentBounds.getHeight());
	}

	/**
	 * Gets the contents bounds for a child page.
	 *
	 * @return the bounds usable by a child page.
	 *
	 * @see StyleHelper#computeContentBounds(Rectangle, Style)
	 */
	protected Rectangle getContentBounds() {
		Rectangle bounds = new Rectangle(0, 0, getWidth(), getHeight());
		Style style = getStyle();
		Rectangle contentBounds = StyleHelper.computeContentBounds(bounds, style);
		return contentBounds;
	}

	@Override
	// Make it accessible to the transition manager.
	protected void add(Widget widget) {
		super.add(widget);
	}

	@Override
	// Make it accessible to the transition manager.
	protected void remove(Widget widget) {
		super.remove(widget);
	}

}
