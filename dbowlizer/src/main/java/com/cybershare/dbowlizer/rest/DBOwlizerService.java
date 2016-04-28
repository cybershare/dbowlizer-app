package com.cybershare.dbowlizer.rest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.cybershare.dbowlizer.main.Main;
import com.cybershare.dbowlizer.main.Service;
import com.cybershare.dbowlizer.utils.Settings;

@Path("/")
public class DBOwlizerService
{
	@POST
	@Produces("application/json")
	public Response run(@FormParam("dbowlspec")String inputJsonObj)
	{
        //Create json object from input
        
		
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
        
        //Call to dbowlizer with the settings as input
        Service.service(settings);
        
        String result = inputJsonObj.toString();
        return Response.status(200).entity(result).build();
	}
}
