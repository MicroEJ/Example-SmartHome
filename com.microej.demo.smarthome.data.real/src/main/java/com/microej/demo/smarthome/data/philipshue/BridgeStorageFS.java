/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.data.philipshue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import sew.philipshue.BridgeStorage;

/**
 *
 */
public class BridgeStorageFS implements BridgeStorage {

	private static final String HueFolder = "HueBridges";

	static File root;

	static {
		root = new File(File.listRoots()[0], HueFolder);
		if (!root.exists()) {
			root.mkdir();
		}
	}

	@Override
	public String getUsername(String name) {
		File file = new File(root, name);
		if (file.exists()) {
			StringBuilder builder = new StringBuilder();
			byte[] buffer = new byte[20];
			try {
				FileInputStream fin = new FileInputStream(file);
				int available = fin.available();
				while (available > 0) {
					int len = Math.min(available, buffer.length - 1);
					int read = fin.read(buffer, 0, len);
					if (read > 0) {
						builder.append(new String(buffer, 0, read));
					}
					available = fin.available();
				}
				fin.close();
				return builder.toString();
			} catch (IOException e) {
			}
		}
		return null;
	}

	@Override
	public void removeUsername(String name) {
		File file = new File(root, name);
		if (file.exists()) {
			file.delete();
		}
	}

	@Override
	public void store(String name, String username) {
		File file = new File(root, name);
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);
				out.write(username.getBytes());
				out.close();
			} catch (IOException e) {
			}
		}
	}
}
