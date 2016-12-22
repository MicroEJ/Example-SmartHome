/*
 * Copyright 2014-2015 IS2T. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found at http://www.is2t.com/open-source-bsd-license/.
 */
package ej.widget.util;

import ej.microui.display.Colors;
import ej.microui.display.Displayable;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.mwt.Composite;
import ej.mwt.Desktop;
import ej.mwt.Panel;
import ej.mwt.Renderable;
import ej.mwt.Widget;

/**
 * Utilities to take a screenshot of displayable objects.
 */
public class DrawScreenHelper {

	// Prevents initialization.
	private DrawScreenHelper() {
	}

	/**
	 * Draws the given displayable in the given graphic context.
	 *
	 * @param g
	 *            the graphic context where to draw the displayable.
	 * @param displayable
	 *            the displayable to draw.
	 */
	public static void draw(GraphicsContext g, Displayable displayable) {
		displayable.paint(g);
	}

	/**
	 * Draws the given desktop in the given graphic context.
	 *
	 * @param g
	 *            the graphic context where to draw the desktop.
	 * @param desktop
	 *            the desktop to draw.
	 */
	public static void draw(GraphicsContext g, Desktop desktop) {
		// Save graphics context state.
		int translateX = g.getTranslateX();
		int translateY = g.getTranslateY();
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipWidth = g.getClipWidth();
		int clipHeight = g.getClipHeight();

		reset(g);
		desktop.render(g);

		// Restore graphics context state useful for composite.
		g.translate(translateX - g.getTranslateX(), translateY - g.getTranslateY());
		g.setClip(clipX, clipY, clipWidth, clipHeight);

		Panel[] panels = desktop.getPanels();
		for (int i = panels.length; --i >= 0;) {
			Panel child = panels[i];
			drawChild(g, child);
		}
	}

	/**
	 * @param g
	 */
	private static void reset(GraphicsContext g) {
		g.setColor(Colors.BLACK);
		g.removeBackgroundColor();
		// and the font is the default font
		Font defaultFont = Font.getDefaultFont();
		defaultFont.resetRatios();
		g.setFont(defaultFont);
		// and stroke style is SOLID
		g.setStrokeStyle(GraphicsContext.SOLID);

		g.setEllipsis(false);
	}

	/**
	 * Draws the given panel in the given graphic context.
	 *
	 * @param g
	 *            the graphic context where to draw the panel.
	 * @param panel
	 *            the panel to draw.
	 */
	public static void draw(GraphicsContext g, Panel panel) {
		// Save graphics context state.
		int translateX = g.getTranslateX();
		int translateY = g.getTranslateY();
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipWidth = g.getClipWidth();
		int clipHeight = g.getClipHeight();

		reset(g);
		panel.render(g);

		// Restore graphics context state useful for composite.
		g.translate(translateX - g.getTranslateX(), translateY - g.getTranslateY());
		g.setClip(clipX, clipY, clipWidth, clipHeight);

		Widget child = panel.getWidget();
		if (child != null && child.isVisible()) {
			drawChild(g, child);
		}
	}

	/**
	 * Draws the given widget in the given graphic context.
	 * <p>
	 * If the given widget is a composite, its children are not drawn.
	 *
	 * @param g
	 *            the graphic context where to draw the widget.
	 * @param widget
	 *            the widget to draw.
	 */
	public static void draw(GraphicsContext g, Widget widget) {
		reset(g);
		widget.render(g);
	}

	/**
	 * Draws the given composite in the given graphic context.
	 *
	 * @param g
	 *            the graphic context where to draw the composite.
	 * @param composite
	 *            the composite to draw.
	 */
	public static void draw(GraphicsContext g, Composite composite) {
		// Save graphics context state.
		int translateX = g.getTranslateX();
		int translateY = g.getTranslateY();
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipWidth = g.getClipWidth();
		int clipHeight = g.getClipHeight();

		draw(g, (Widget) composite);

		// Restore graphics context state useful for composite.
		g.translate(translateX - g.getTranslateX(), translateY - g.getTranslateY());
		g.setClip(clipX, clipY, clipWidth, clipHeight);

		Widget[] widgets = composite.getWidgets();
		int widgetslength = widgets.length;
		for (int j = -1; ++j < widgetslength;) {
			Widget child = widgets[j];

			if (child.isVisible()) {
				drawChild(g, child);
			}
		}
	}

	/**
	 * Draws the given renderable in the given graphic context.
	 *
	 * @param g
	 *            the graphic context where to draw the renderable.
	 * @param composite
	 *            the renderable to draw.
	 */
	private static void draw(GraphicsContext g, Renderable renderable) {
		// Composite must be checked before Widget (subclass).
		if (renderable instanceof Composite) {
			draw(g, (Composite) renderable);
		} else if (renderable instanceof Widget) {
			draw(g, (Widget) renderable);
		} else if (renderable instanceof Panel) {
			draw(g, (Panel) renderable);
		} else if (renderable instanceof Desktop) {
			draw(g, (Widget) renderable);
		}
	}

	private static void drawChild(GraphicsContext g, Renderable renderable) {
		// Save graphics context state.
		int translateX = g.getTranslateX();
		int translateY = g.getTranslateY();
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipWidth = g.getClipWidth();
		int clipHeight = g.getClipHeight();

		// Shift to renderable origin and clip to its size.
		g.translate(renderable.getX(), renderable.getY());
		g.clipRect(0, 0, renderable.getWidth(), renderable.getHeight());

		// Draw renderable.
		draw(g, renderable);

		// Restore graphics context state.
		g.translate(translateX - g.getTranslateX(), translateY - g.getTranslateY());
		g.setClip(clipX, clipY, clipWidth, clipHeight);
	}

}
