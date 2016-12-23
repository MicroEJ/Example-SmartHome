package __SmartHome__.generated;

import ej.wadapps.management.ActivitiesList;

import ej.wadapps.management.BackgroundServicesList;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.components.registry.BundleActivator;

public class SmartHomeActivator implements BundleActivator {

	com.microej.demo.smarthome.app.SmartHomeActivity com__microej__demo__smarthome__app__SmartHomeActivity;
	com.microej.demo.smarthome.ZWaveBackgroundService com__microej__demo__smarthome__ZWaveBackgroundService;
	 com.microej.demo.smarthome.data.philipshue.PhilipsHueBackgroundService  com__microej__demo__smarthome__data__philipshue__PhilipsHueBackgroundService;

	@Override
	public void initialize() {
		this.com__microej__demo__smarthome__app__SmartHomeActivity = new com.microej.demo.smarthome.app.SmartHomeActivity();
		this.com__microej__demo__smarthome__ZWaveBackgroundService = new com.microej.demo.smarthome.ZWaveBackgroundService();
		this. com__microej__demo__smarthome__data__philipshue__PhilipsHueBackgroundService = new  com.microej.demo.smarthome.data.philipshue.PhilipsHueBackgroundService();
	}

	@Override
	public void link() {
		ActivitiesList activitieslist = ServiceLoaderFactory.getServiceLoader().getService(ActivitiesList.class);
		activitieslist.add(this.com__microej__demo__smarthome__app__SmartHomeActivity);

		BackgroundServicesList backgroundserviceslist = ServiceLoaderFactory.getServiceLoader().getService(BackgroundServicesList.class);
		backgroundserviceslist.add(this.com__microej__demo__smarthome__ZWaveBackgroundService);
		backgroundserviceslist.add(this. com__microej__demo__smarthome__data__philipshue__PhilipsHueBackgroundService);

	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		ActivitiesList activitieslist = ServiceLoaderFactory.getServiceLoader().getService(ActivitiesList.class);
		activitieslist.remove(this.com__microej__demo__smarthome__app__SmartHomeActivity);

		BackgroundServicesList backgroundserviceslist = ServiceLoaderFactory.getServiceLoader().getService(BackgroundServicesList.class);
		backgroundserviceslist.remove(this.com__microej__demo__smarthome__ZWaveBackgroundService);
		backgroundserviceslist.remove(this. com__microej__demo__smarthome__data__philipshue__PhilipsHueBackgroundService);

	}

}