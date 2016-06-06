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
