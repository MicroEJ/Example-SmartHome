/*
 * Java
 *
 * Copyright 2016-2017 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import java.util.Calendar;

import com.microej.demo.smarthome.util.Strings;

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

	private static final int MID_DAY = 12;
	// used for size.
	private static final String DATE = "00:00 PM"; //$NON-NLS-1$
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
		final StringBuilder builder = new StringBuilder(this.start);
		if ((this.refresh & 1) == 0) {
			builder.append(Strings.HOUR_MIN_SEPARATOR);
		} else {
			builder.append(' ');
		}
		builder.append(this.end);

		style.getTextManager().drawText(g, builder.toString(), font, style.getForegroundColor(), bounds,
				style.getAlignment());
	}

	private void update() {
		if (isShown() && getPanel().isActive() || this.start == null) {

			this.refresh++;
			if (this.refresh > UPDATE_RATE) {
				final Calendar calendar = Calendar.getInstance();
				final boolean am = calendar.get(Calendar.AM_PM) == Calendar.AM;
				int hour = calendar.get(Calendar.HOUR);
				if (hour == 0) {
					hour = MID_DAY;
				}
				final int min = calendar.get(Calendar.MINUTE);
				this.start = formatDoubleDigits(hour);
				final StringBuilder builder = new StringBuilder(formatDoubleDigits(min));
				builder.append(Strings.TIME_SEPARATOR);
				if (am) {
					builder.append(Strings.AM);
				} else {
					builder.append(Strings.PM);
				}
				this.end = builder.toString();
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
		synchronized (this.sync) {
			if (this.update == null) {
				this.update = new TimerTask() {

					@Override
					public void run() {
						update();

					}
				};
				timer.schedule(this.update, REFRESH_RATE, REFRESH_RATE);
			}
		}
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		if (this.update != null) {
			this.update.cancel();
			this.update = null;
		}
	}
	
	/**
	 * Format an integer to a double digit string.
	 *
	 * @param integer
	 * @return the integer as a string.
	 */
	private static String formatDoubleDigits(final int integer) {
		if (integer < 10) {
			return '0' + String.valueOf(integer);
		}
		return String.valueOf(integer);
	}
}
