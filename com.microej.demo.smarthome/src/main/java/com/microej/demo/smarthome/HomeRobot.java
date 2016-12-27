/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.microej.demo.smarthome.page.DoorPage;
import com.microej.demo.smarthome.page.GraphPage;
import com.microej.demo.smarthome.page.InformationPage;
import com.microej.demo.smarthome.page.LightPage;
import com.microej.demo.smarthome.page.MenuNavigatorPage;
import com.microej.demo.smarthome.page.MenuPage;
import com.microej.demo.smarthome.page.ThermostatPage;
import com.microej.demo.smarthome.util.ExecutorUtils;
import com.microej.demo.smarthome.widget.CircleWidget;
import com.microej.demo.smarthome.widget.ColorPicker;
import com.microej.demo.smarthome.widget.Menu;
import com.microej.demo.smarthome.widget.MenuButton;
import com.microej.demo.smarthome.widget.OverlapingComposite;
import com.microej.demo.smarthome.widget.PowerWidget;
import com.microej.demo.smarthome.widget.chart.Chart;
import com.microej.demo.smarthome.widget.light.LightCircularProgress;
import com.microej.demo.smarthome.widget.light.LightWidget;
import com.microej.demo.smarthome.widget.thermostat.ThermostatCircularProgress;
import com.microej.demo.smarthome.widget.thermostat.ThermostatWidget;
import com.microej.demo.util.Robot;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.microui.display.Display;
import ej.microui.event.EventGenerator;
import ej.microui.event.generator.Pointer;
import ej.mwt.Composite;
import ej.mwt.Panel;
import ej.mwt.Widget;
import ej.widget.basic.BoundedRange;
import ej.widget.basic.Box;
import ej.widget.basic.image.ImageSwitch;
import ej.widget.composed.ButtonWrapper;
import ej.widget.container.Grid;
import ej.widget.container.Scroll;
import ej.widget.navigation.Navigator;
import ej.widget.navigation.page.Page;

/**
 *
 */
public class HomeRobot extends Robot {

	private static final int DELAY = 40_000;
	private static final int PERIOD = 2_500;
	private static final Timer timer = new Timer();
	private static final int INITIAL_STATE = 0;
	private static final Random rand = new Random();

	private int state = INITIAL_STATE;
	private boolean dashBoardForward = true;

	static {
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Thread.currentThread().setName("Robot");
				Thread.currentThread().setPriority(ExecutorUtils.VERY_LOW_PRIORITY);

			}
		}, 0);
	}

	/**
	 */
	public HomeRobot() {
		super(PERIOD, DELAY, timer);

		// Reset on pointer events.
		addEventGeneratorTrigger(EventGenerator.get(Pointer.class, 0));

		// Automatically starts.
		startWatchdog();
	}

	@Override
	protected void automate() {
		final Display display = Display.getDefaultDisplay();
		display.waitForEvent();
		display.waitForEvent();
		display.waitForEvent();

		final List<Composite> hierrary = getPageHierrary();
		if (hierrary.size() > 0) {
			final Composite lastPage = hierrary.get(hierrary.size() - 1);
			if (lastPage instanceof InformationPage) {
				automate(hierrary, (InformationPage) lastPage);
			} else if (lastPage instanceof GraphPage) {
				automate(hierrary, (GraphPage) lastPage);
			} else if (lastPage instanceof ThermostatPage) {
				automate(hierrary, (ThermostatPage) lastPage);
			} else if (lastPage instanceof LightPage) {
				automate(hierrary, (LightPage) lastPage);
			} else if (lastPage instanceof ColorPicker) {
				automate(hierrary, (ColorPicker) lastPage);
			} else if (lastPage instanceof DoorPage) {
				automate(hierrary, (DoorPage) lastPage);
			} else {
				System.out.println("Unsupported: " + lastPage);
			}
			state++;
		}
	}

	/**
	 * @param hierrary
	 * @param lastPage
	 */
	private void automate(final List<Composite> hierrary, final ColorPicker colorPicker) {
		switch (state) {
		case INITIAL_STATE + 2:
			final int width = colorPicker.getImage().getWidth();
		final int height = colorPicker.getImage().getHeight();
		final int r = colorPicker.getRadius();
		final double angle = rand.nextFloat() * (2.0 * Math.PI);
		final float distance = (rand.nextFloat()/2.0f + 0.5f) * r;
		final int x = (int) (distance * Math.cos(angle)) + width/2;
		final int y = (int) (distance * Math.sin(angle)) + height/2;
		colorPicker.performClick(Pointer.PRESSED, x, y);
		colorPicker.performClick(Pointer.RELEASED, x, y);
		break;
		case INITIAL_STATE + 3:
			colorPicker.getCloseButton().performClick();
		break;
		default:
			break;
		}
	}

	/**
	 * @param hierrary
	 * @param lastPage
	 */
	private void automate(final List<Composite> hierrary, final DoorPage lastPage) {
		goToNext(lastPage.getMenu(), lastPage.getMenuButton());
	}

	/**
	 * @param hierrary
	 * @param lastPage
	 */
	private void automate(final List<Composite> hierrary, final LightPage lastPage) {
		final Grid lights = (Grid) lastPage.getWidget(0);
		final int widgetsCount = lights.getWidgetsCount();
		if (widgetsCount == 0 && state < INITIAL_STATE + 4) {
			state = INITIAL_STATE + 4;
			return;
		}

		Box switchButton = null;
		BoundedRange lightCircularProgress = null;
		CircleWidget circleWidget = null;
		LightWidget light = null;
		final int nextInt = rand.nextInt(widgetsCount);
		final Widget widget = lights.getWidget(nextInt);
		if (widget instanceof LightWidget) {
			light = (LightWidget) widget;
			final OverlapingComposite composite = (OverlapingComposite) light.getWidget(1);
			lightCircularProgress = (LightCircularProgress) composite.getWidget(0);
			circleWidget = (CircleWidget) composite.getWidget(1);
			switchButton = (ImageSwitch) light.getWidget(2);
		} else if (state < INITIAL_STATE + 4) {
			state = INITIAL_STATE + 4;
			return;
		}

		switch (state) {
		case INITIAL_STATE:
			if (switchButton.isChecked()) {
				final int value = (int) ((lightCircularProgress.getMaximum() - lightCircularProgress.getMinimum()) * rand.nextFloat() + lightCircularProgress.getMinimum());
				lightCircularProgress.setValue(value);
			}
			break;
		case INITIAL_STATE + 1:
			if (switchButton.isChecked()) {
				circleWidget.performClick();
			}
		break;
		case INITIAL_STATE + 2:
		case INITIAL_STATE + 3:
			// see automate color picker.
			break;
		case INITIAL_STATE + 4:
			light.onStateChange((rand.nextInt() & 1) == 0);
		break;
		case INITIAL_STATE + 5:
		default:
			goToNext(lastPage.getMenu(), lastPage.getMenuButton());
			break;
		}
	}

	/**
	 * @param hierrary
	 * @param lastPage
	 */
	private void automate(final List<Composite> hierrary, final ThermostatPage lastPage) {
		final Grid thermostats = (Grid) lastPage.getWidget(0);
		final ThermostatWidget thermostat = (ThermostatWidget) thermostats.getWidget(0);
		final OverlapingComposite composite = (OverlapingComposite) thermostat.getWidget(1);
		final ThermostatCircularProgress progress = (ThermostatCircularProgress) composite.getWidget(0);
		final ButtonWrapper validate = (ButtonWrapper) composite.getWidget(1);
		switch (state) {
		case INITIAL_STATE:
			final int value = (int) ((progress.getMaximum() - progress.getMinimum()) * rand.nextFloat() + progress.getMinimum());
			progress.setLocalTarget(value);
			break;
		case INITIAL_STATE + 1:
			validate.performClick();
		break;
		case INITIAL_STATE + 2:
		default:
			goToNext(lastPage.getMenu(), lastPage.getMenuButton());
			break;
		}
	}

	/**
	 * @param hierrary
	 * @param lastPage
	 */
	private void automate(final List<Composite> hierrary, final GraphPage lastPage) {
		final PowerWidget powerWidget = (PowerWidget) lastPage.getWidget(0);
		final Scroll scroll = (Scroll) powerWidget.getWidget(0);
		final Chart chart = (Chart) scroll.getWidget(0);
		switch (state) {
		case INITIAL_STATE:
			chart.selectPoint(rand.nextInt(10));
			break;
		case INITIAL_STATE + 1:
			chart.selectPoint(null);
		break;
		case INITIAL_STATE + 2:
		default:
			goToNextDashboard(hierrary, lastPage, false);
			break;
		}
	}

	/**
	 * @param hierrary
	 * @param lastPage
	 */
	private void automate(final List<Composite> hierrary, final InformationPage lastPage) {
		goToNextDashboard(hierrary, lastPage, true);
	}

	private void goToNextDashboard(final List<Composite> hierrary, final MenuPage lastPage, final boolean forward) {
		if (dashBoardForward == forward) {
			goToNext(lastPage.getMenu(), lastPage.getMenuButton());
		} else {
			dashBoardForward = forward;
			final MenuNavigatorPage navigatorPage = (MenuNavigatorPage) hierrary.get(hierrary.size() - 2);
			final Menu menu = navigatorPage.getMenu();
			goToNext(menu, navigatorPage.getMenuButton());
		}
	}

	private void goToNext(final Menu menu, final MenuButton current) {
		state = INITIAL_STATE - 1;
		final Widget[] buttons = menu.getWidgets();
		for (int i = 0; i < buttons.length; i++) {
			MenuButton button = (MenuButton) buttons[i];
			if (button == current) {
				if (i == buttons.length - 1) {
					button = (MenuButton) buttons[0];
				} else {
					button = (MenuButton) buttons[i + 1];
				}
				button.performClick();
			}
		}
	}

	private List<Composite> getPageHierrary() {
		final List<Composite> pages = new ArrayList<>();
		final Panel activePanel = Main.getDesktop().getActivePanel();
		if (activePanel != null) {
			Widget widget = activePanel.getWidget();
			while (widget != null) {
				if (widget instanceof Navigator) {
					final Navigator navigator = (Navigator) widget;
					final Page currentPage = navigator.getCurrentPage();
					pages.add(currentPage);
					if (currentPage instanceof MenuNavigatorPage) {
						final MenuNavigatorPage menuNavigatorPage = (MenuNavigatorPage) currentPage;
						widget = menuNavigatorPage.getNavigator();
					} else {
						widget = null;
					}
				} else {
					if (widget instanceof Composite) {
						pages.add((Composite) widget);
					}
					widget = null;
				}
			}
		}
		return pages;
	}
}
