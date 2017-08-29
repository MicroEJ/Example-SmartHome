/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome;

import java.util.Random;
import java.util.logging.Logger;

import com.microej.demo.smarthome.page.ColorPickerPage;
import com.microej.demo.smarthome.page.DashBoardPage;
import com.microej.demo.smarthome.page.DoorPage;
import com.microej.demo.smarthome.page.GraphPage;
import com.microej.demo.smarthome.page.InformationPage;
import com.microej.demo.smarthome.page.LightPage;
import com.microej.demo.smarthome.page.MenuNavigatorPage;
import com.microej.demo.smarthome.page.MenuPage;
import com.microej.demo.smarthome.page.ThermostatPage;
import com.microej.demo.smarthome.widget.ColorPicker;

import ej.automaton.Automaton;
import ej.automaton.impl.AutomatonManagerImpl;
import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.microui.display.Display;
import ej.microui.event.generator.Buttons;
import ej.widget.navigation.page.Page;

/**
 * Robot that simulates user interaction.
 */
public class HomeRobot implements Automaton {

	private static final String TIMER_NAME = "Robot"; //$NON-NLS-1$
	
	/**
	 * Delay before the robot starts.
	 */
	private static final int INACTIVITY = 5_000;
	/**
	 * The period in between two actions.
	 */
	private static final int PERIOD = 2_500;
	private static final int INITIAL_STATE = 0;

	private final Random rand;
	private Timer robotTimer;
	private Logger logger;
	private int state;
	private AutomatonManagerImpl<Automaton> manager;
	private boolean isNavigatingDashboard;

	/**
	 * Instantiates a {@link HomeRobot}.
	 */
	public HomeRobot() {
		this.rand = new Random();
		this.logger = Logger.getLogger(getClass().getName());
		this.robotTimer = new Timer();
		this.robotTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				Thread.currentThread().setName(TIMER_NAME);
				Thread.currentThread().setPriority(Thread.NORM_PRIORITY - 2);
			}
		}, 0);
		this.manager = new AutomatonManagerImpl<Automaton>(this.robotTimer, this, INACTIVITY);
	}

	@Override
	public void onStart() {
		this.state = INITIAL_STATE;
		this.isNavigatingDashboard = true;
	}

	@Override
	public synchronized void onStop() {
		// Nothing to do.
	}

	/**
	 * Arms the robot.
	 */
	public void arm() {
		if (this.manager != null) {
			this.manager.arm();
		}
	}

	/**
	 * Stops the robot.
	 */
	public void stop() {
		if (this.manager != null) {
			this.manager.stop();
		}
		if (this.robotTimer != null) {
			this.robotTimer.cancel();
			this.robotTimer = null;
		}
	}

	@Override
	public void run() {
		final Display display = Display.getDefaultDisplay();
		display.waitForEvent();
		display.waitForEvent();
		display.waitForEvent();

		final Page lastPage = Main.getCurrentPage();
		if (lastPage instanceof DashBoardPage) {
			automate((DashBoardPage) lastPage);
		} else if (lastPage instanceof ThermostatPage) {
			automate((ThermostatPage) lastPage);
		} else if (lastPage instanceof LightPage) {
			automate((LightPage) lastPage);
		} else if (lastPage instanceof DoorPage) {
			automate((DoorPage) lastPage);
		} else if (lastPage instanceof ColorPickerPage) {
			automate((ColorPickerPage) lastPage);
		} else {
			this.logger.warning("Robot Unsupported: " + lastPage); //$NON-NLS-1$
		}
		this.state++;
	}


	/**
	 * Automate one action on a page.
	 *
	 * @param lastPage
	 *            the page to automate.
	 * @return true if an action has been done.
	 */
	private boolean automate(final GraphPage lastPage) {
		switch (this.state) {
		case INITIAL_STATE:
			lastPage.selectPoint(Integer.valueOf(this.rand.nextInt(10)));
			break;
		case INITIAL_STATE + 1:
			lastPage.selectPoint(null);
		break;
		default:
			return false;
		}
		return true;
	}

	private void automate(DashBoardPage lastPage) {
		MenuPage currentPage = lastPage.getCurrentPage();
		if(currentPage instanceof InformationPage){
			goToNextSubmenu(currentPage, lastPage);
		} else if( currentPage instanceof GraphPage){
			if(!automate((GraphPage)currentPage)){
				goToNextSubmenu(currentPage, lastPage);
			}
		} else {
			this.logger.warning("Robot Unsupported: " + currentPage); //$NON-NLS-1$
		}
	}

	/**
	 * Automate one action on a page.
	 *
	 * @param lastPage
	 *            the page to automate.
	 */
	private void automate(final ColorPickerPage lastPage) {
		final ColorPicker colorPicker = (ColorPicker) lastPage.getWidget(0);
		switch (this.state) {
		case INITIAL_STATE + 2:
			final int width = colorPicker.getImage().getWidth();
		final int height = colorPicker.getImage().getHeight();
		final int r = colorPicker.getRadius();
		final double angle = this.rand.nextFloat() * (2.0 * Math.PI);
		final float distance = (this.rand.nextFloat() / 2.0f + 0.5f) * r;
		final int x = (int) (distance * Math.cos(angle)) + width / 2;
		final int y = (int) (distance * Math.sin(angle)) + height / 2;
		colorPicker.performTouch(Buttons.PRESSED, x, y);
		colorPicker.performTouch(Buttons.RELEASED, x, y);
		break;
		case INITIAL_STATE + 3:
			colorPicker.close();
		break;
		default:
			break;
		}
	}

	/**
	 * Automate one action on a page.
	 *
	 * @param lastPage
	 *            the page to automate.
	 */
	private void automate(final DoorPage lastPage) {
		goToNextPage(lastPage);
	}

	/**
	 * Automate one action on a page.
	 *
	 * @param lastPage
	 *            the page to automate.
	 */
	private void automate(final LightPage lastPage) {
		int lightsCount = lastPage.countLights();
		// If there aren't any light, jump to the next page
		if (lightsCount == 0 && this.state < INITIAL_STATE + 4) {
			this.state = INITIAL_STATE + 4;
			return;
		}
		final int lightId = this.rand.nextInt(lightsCount);

		switch (this.state) {
		case INITIAL_STATE:
			// pick a light and switch it on or change the brightness
			if (lastPage.isLightOn(lightId)) {
				lastPage.setLightBrightness(lightId, this.rand.nextFloat());
			} else {
				lastPage.switchOn(lightId, (this.rand.nextInt() & 1) == 0);
			}
			break;
		case INITIAL_STATE + 1:
			// pick a light and switch it on or open the color picker for it
			if (lastPage.isLightOn(lightId)) {
				lastPage.openColorPicker(lightId);
			} else {
				lastPage.switchOn(lightId, (this.rand.nextInt() & 1) == 0);
			}
		break;
		case INITIAL_STATE + 2:
			// see automate color picker.
		case INITIAL_STATE + 3:
			// see automate color picker.
			break;
		case INITIAL_STATE + 4:
			// Pick a light and switch it
			lastPage.switchOn(lightId, (this.rand.nextInt() & 1) == 0);
		break;
		case INITIAL_STATE + 5:
		default:
			goToNextPage(lastPage);
			break;
		}
	}

	/**
	 * Automate one action on a page.
	 *
	 * @param lastPage
	 *            the page to automate.
	 */
	private void automate(final ThermostatPage lastPage) {
		switch (this.state) {
		case INITIAL_STATE:
			lastPage.setTarget(this.rand.nextFloat());
			break;
		case INITIAL_STATE + 1:
			lastPage.validateTemperature();
		break;
		case INITIAL_STATE + 2:
		default:
			goToNextPage(lastPage);
			break;
		}
	}

	private void goToNextPage(final MenuPage menuPage) {
		this.state = INITIAL_STATE - 1;
		menuPage.goToNextPage();
	}

	private void goToNextSubmenu(MenuPage menuPage, MenuNavigatorPage menuNavigatorPage){
		if(this.isNavigatingDashboard){
			goToNextPage(menuPage);
			this.isNavigatingDashboard = false;
		} else {
			goToNextPage(menuNavigatorPage);
			this.isNavigatingDashboard = true;
		}
	}


	@Override
	public long getPeriod() {
		return PERIOD;
	}

}
