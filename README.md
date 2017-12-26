[//]: # (Markdown)
[//]: # (Copyright 2017 IS2T. All rights reserved.)
[//]: # (For demonstration purpose only.)
[//]: # (IS2T PROPRIETARY. Use is subject to license terms.)

# Overview
This repository contains the smarthome demo.

# Usage
The project `com.microej.demo.smarthome` depends on the two other projects, to run it ivy must find its dependencies.

There are two ways to do it:
1. Resolve dependencies in workspace, this solution dosen't work if you want to wrap smarthome into a sandboxed application
	1. Go to `Windows -> Preferences -> Ivy -> Classpath container`
	2. Make sure that **Resolve dependencies in workspace** is checked
2. Build the projects
	1. Build the project [com.microej.demo.smarthome.data](com.microej.demo.smarthome.data) with easy Ant
	2. Build the project [com.microej.demo.smarthome.data.default](com.microej.demo.smarthome.data.default) with easy Ant

Now you can follow com.microej.demo.smarthome [README](com.microej.demo.smarthome/README.md).

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