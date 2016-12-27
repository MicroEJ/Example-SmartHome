/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.widget;

import com.microej.demo.smarthome.data.door.Door;
import com.microej.demo.smarthome.data.door.DoorEventListener;
import com.microej.demo.smarthome.style.ClassSelectors;
import com.microej.demo.smarthome.style.HomeImageLoader;
import com.microej.demo.smarthome.util.Images;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.motion.Motion;
import ej.motion.linear.LinearMotion;
import ej.widget.basic.Image;

/**
 *
 */
public class DoorWidget extends DeviceWidget<Door> implements DoorEventListener, Animation {

	/**
	 * Values
	 */
	private static final int ANIMATION_DURATION = 400;
	private static final int ANIMATION_STEPS = 4;

	/**
	 * Attributes
	 */
	private final Image door;
	private boolean open;
	private Motion motion;

	/**
	 * @param model
	 */
	public DoorWidget(Door model) {
		super(model);
		addClassSelector(ClassSelectors.DOOR_WIDGET);

		this.door = new Image();

		// Force change of state see setOpen.
		this.open = !model.isOpen();
		setOpen(!this.open);

		setCenter(this.door);
	}

	@Override
	public void onStateChange(boolean open) {
		setOpen(open);
	}

	private void setOpen(boolean open) {
		if (this.open != open) {
			int animStep = (this.open ? 0 : ANIMATION_STEPS);
			String image = getImage(animStep);

			this.open = open;
			this.motion = new LinearMotion(animStep, ANIMATION_STEPS-animStep, ANIMATION_DURATION);
			this.door.setSource(HomeImageLoader.loadImageRoot(image));
			Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
			animator.startAnimation(this);
		}
	}

	private String getImage(int animStep) {
		switch (animStep) {
		case 0:
			return Images.DOOR_OPEN;
		case 1:
			return Images.DOOR_CLOSE_TO_OPEN;
		case 2:
			return Images.DOOR_CLOSE_TO_CLOSED;
		case 3:
			return Images.DOOR_CLOSER_TO_CLOSED;
		case 4:
		default:
			return Images.DOOR_CLOSED;
		}
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		String image = getImage(this.motion.getCurrentValue());
		this.door.setSource(HomeImageLoader.loadImageRoot(image));
		return !this.motion.isFinished();
	}

	@Override
	public void showNotify() {
		super.showNotify();
		this.model.addListener(this);
		setOpen(this.model.isOpen());
	}

	@Override
	public void hideNotify() {
		this.model.removeListener(this);
	}

}
