/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package sew.philipshue;

/**
 *
 */
public interface BridgeStorage {

	public String getUsername(String friendlyName);

	public void removeUsername(String friendlyName);

	public void store(String friendlyName, String username);
}
