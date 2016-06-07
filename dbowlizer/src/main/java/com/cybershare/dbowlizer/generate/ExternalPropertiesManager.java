/*******************************************************************************
 * ========================================================================
 * DBOWLizer
 * http://dbowlizer.cybershare.utep.edu
 * Copyright (c) 2016, CyberShare Center of Excellence <cybershare@utep.edu>.
 * All rights reserved.
 * ------------------------------------------------------------------------
 *   
 *     This file is part of DBOWLizer
 *
 *     DBOWLizer is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     DBOWLizer is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with DBOWLizer.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.cybershare.dbowlizer.generate;

import java.util.HashMap;

import schemacrawler.schemacrawler.Config;

public class ExternalPropertiesManager {
	
	private static HashMap<String, ExternalPropertiesManager> propertiesFileManagers = new HashMap<String, ExternalPropertiesManager>(); 
	private Config resource;
	private HashMap<String, String> readStrings;
	
	private ExternalPropertiesManager(String propertiesFile)
	{
		resource = Config.loadResource(propertiesFile);
		readStrings = new HashMap<String, String>();

	}
	
	public static ExternalPropertiesManager getInstance(String propertiesFile)
	{
		if (propertiesFileManagers.containsKey(propertiesFile))
			return propertiesFileManagers.get(propertiesFile);
		
		ExternalPropertiesManager manager = new ExternalPropertiesManager(propertiesFile);
		propertiesFileManagers.put(propertiesFile, manager);
		return manager;						
	}
	
	public String getString(String propertyStr)
	{
		if (readStrings.containsKey(propertyStr))
			return readStrings.get(propertyStr);
		
		String valueStr = resource.getStringValue(propertyStr, null);
		readStrings.put(propertyStr, valueStr);
		
		return valueStr;
		
	}
	
}
