# Overview
Connect to various appliances (thermostats, lights, doors, power controls...), monitor and control them with useful services including home status overview, and various control for temperature, light and door.
This project contains the business and view of smarthome, as well as an entry point.

# Usage
## Run on MicroEJ Simulator
1. Right Click on the project
2. Select **Run as -> MicroEJ Application**
3. Select your platform 
4. Press **Ok**


## Run on device
### Build
1. Right Click on [Main.java](com.microej.demo.smarthome/src/main/java/com/microej/demo/smarthome/Main.java)
2. Select **Run as -> Run Configuration** 
3. Select **MicroEJ Application** configuration kind
4. Click on **New launch configuration** icon
5. In **Execution** tab
	1. In **Target** frame, in **Platform** field, select a relevant platform (but not a virtual device)
	2. In **Execution** frame
		1. Select **Execute on Device**
		2. In **Settings** field, select **Build & Deploy**
6. Press **Apply**
7. Press **Run**
8. Copy the generated `.out` file path

### Flash
1. Use the appropriate flashing tool.

# Requirements
* MicroEJ Studio or SDK 4.1 or later
* A platform with at least:
  * EDC-1.2 or higher
  * BON-1.2 or higher
  * MICROUI-2.0 or higher

# Dependencies
_All dependencies are retrieved transitively by Ivy resolver_.

# Source
N/A

# Restrictions
None.