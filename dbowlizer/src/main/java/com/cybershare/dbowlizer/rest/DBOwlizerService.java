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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Settings
        Settings settings = new Settings("/schema2owl.config.properties");
        settings.setOutputDir(settings.getOutputDir() + uuid.toString() + "/");
        settings.setOntologyFile(settings.getOutputDir() + "relational-to-ontology-mapping-dbwolizer.owl");
        
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Build output json
        
        String result = inputJsonObj.toString();
        return Response.status(200).entity(result).build();
	}
}
