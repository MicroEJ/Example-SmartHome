/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import java.util.Calendar;

import com.microej.demo.smarthome.util.Strings;
import com.microej.demo.smarthome.util.Utils;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.style.Style;
import ej.style.container.Rectangle;
import ej.style.util.StyleHelper;
import ej.widget.StyledWidget;

/**
 * A widget displaying the time.
 */
public class TimeWidget extends StyledWidget {

	// used for size.
	private static final String DATE = "00:00 PM";
	private static final int REFRESH_RATE = 800;
	private static final int UPDATE_RATE = 30;
	private TimerTask update;
	private final Object sync = new Object();
	private int refresh = UPDATE_RATE;
	private String start;
	private String end;

	/**
	 * instantiates a TimeWidget.
	 */
	public TimeWidget() {
		super();
		update();
	}

	@Override
	public void renderContent(final GraphicsContext g, final Style style, final Rectangle bounds) {
		final Font font = StyleHelper.getFont(style);
		final StringBuilder builder = new StringBuilder(start);
		if ((refresh & 1) == 0) {
			builder.append(Strings.HOUR_MIN_SEPARATOR);
		} else {
			builder.append(' ');
		}
		builder.append(end);

		style.getTextManager().drawText(g, builder.toString(), font, style.getForegroundColor(), bounds,
				style.getAlignment());
	}


	private void update() {
		if (isShown() && getPanel().isActive() || start == null) {

			refresh++;
			if (refresh > UPDATE_RATE) {
				final Calendar calendar = Calendar.getInstance();
				final boolean am = calendar.get(Calendar.AM_PM) == Calendar.AM;
				int hour = calendar.get(Calendar.HOUR);
				if (hour == 0) {
					hour = 12;
				}
				final int min = calendar.get(Calendar.MINUTE);
				start = Utils.formatDoubleDigits(hour);
				final StringBuilder builder = new StringBuilder(Utils.formatDoubleDigits(min));
				builder.append(Strings.TIME_SEPARATOR);
				if (am) {
					builder.append(Strings.AM);
				} else {
					builder.append(Strings.PM);
				}
				end = builder.toString();
			}

			repaint();
		}
	}

	@Override
	public Rectangle validateContent(final Style style, final Rectangle bounds) {
		final Font font = StyleHelper.getFont(style);
		return style.getTextManager().computeContentSize(DATE, font, bounds);
	}

	@Override
	public void showNotify() {
		super.showNotify();
		final Timer timer = ServiceLoaderFactory.getServiceLoader().getService(Timer.class);
		synchronized (sync) {
			if (update == null) {
				update = new TimerTask() {

					@Override
					public void run() {
						update();

					}
				};
				timer.schedule(update, REFRESH_RATE, REFRESH_RATE);
			}
		}
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		if (update != null) {
			update.cancel();
			update=null;
		}
	}
}
