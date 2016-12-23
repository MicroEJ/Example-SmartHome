package __SmartHome__.generated;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.components.registry.BundleRegistry;
import ej.wadapps.management.ActivitiesScheduler;
import ej.wadapps.registry.SharedRegistryFactory;

public class SmartHomeActivityStandalone {

	public static void main(String[] args) {
		SharedRegistryFactory.getSharedRegistry().register(BundleRegistry.class, new StandaloneRegistry());

		// Start entry point.
		new SmartHomeEntryPoint().start();

		// SmartHomeActivity
		ActivitiesScheduler activitiesScheduler = ServiceLoaderFactory.getServiceLoader()
				.getService(ActivitiesScheduler.class);
		com.microej.demo.smarthome.app.SmartHomeActivity activity = new com.microej.demo.smarthome.app.SmartHomeActivity();
		activitiesScheduler.show(activity);
	}

}