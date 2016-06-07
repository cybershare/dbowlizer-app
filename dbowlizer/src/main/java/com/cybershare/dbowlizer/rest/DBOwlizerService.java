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

package com.cybershare.dbowlizer.rest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cybershare.dbowlizer.main.Service;
import com.cybershare.dbowlizer.utils.Settings;

@Path("/")
public class DBOwlizerService
{
	@POST
	@Produces("application/json")
	public Response run(@FormParam("dbowlspec")String inputJsonString)
	{
        //Create json object from input
		JSONObject inputJsonObj = null;
		try {
			inputJsonObj = (JSONObject)new JSONParser().parse(inputJsonString);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Make unique directory for output files 
        UUID uuid = java.util.UUID.randomUUID();
        new File("/var/www/vhosts/dbowlizer-output/"+ uuid.toString()).mkdir();
        try {
			Runtime.getRuntime().exec("chmod 777 " + "/var/www/vhosts/dbowlizer-output/"+ uuid.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        //Settings
        Settings settings = new Settings("/schema2owl.config.properties");
        settings.setOutputDir(settings.getOutputDir() + uuid.toString() + "/");
        settings.setOutputDirFile(settings.getOutputDirFile() + uuid.toString() + "/");
        settings.setOntologyFile(settings.getOutputDir() + "relational-to-ontology-mapping-output.owl");
        settings.setUuid(uuid.toString());
        
      //Parse input Json settings
        JSONObject dbsettings = (JSONObject)inputJsonObj.get("dbsettings");
		if (dbsettings != null){
			settings.setDriver(dbsettings.get("driver").toString());
			settings.setHost(dbsettings.get("host").toString());
			settings.setPort(dbsettings.get("port").toString());
			settings.setDbname(dbsettings.get("dbname").toString());
			settings.setUser(dbsettings.get("user").toString());
			settings.setPassword(dbsettings.get("password").toString());
		}
        
        //Call to dbowlizer with the settings as input
        Service.service(settings);
        
        //Set permissions on folder and files for all users
        try {
			Runtime.getRuntime().exec("chmod 777 -R " + "/var/www/vhosts/dbowlizer-output/"+ uuid.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        //Build output json
        //Output names on a json
        JSONObject jsonOutput = new JSONObject();
    	int i=0;
        for(String ontologyName: settings.getOntologyNames()){
        	jsonOutput.put("Ontology-Link-" + i, settings.getOutputURL() + settings.getUuid() + "/" + ontologyName);
        	i++;
        }
        i = 0;
        for(String mappingName: settings.getMappingNames()){
        	jsonOutput.put("Mapping-Link-" + i, settings.getOutputURL() + settings.getUuid() + "/" +  mappingName);
        	i++;
        }
        
        System.out.println(jsonOutput.toJSONString());
        String result = jsonOutput.toString();
        result = result.replaceAll("\\\\", "");
        return Response.status(200).entity(result).build();
	}
}
