package com.cybershare.dbowlizer.generate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OutputOntologyFixer 
{
	
	public static void fixFinalOntology(OWLEntitiesBundle owlEntitiesBundle)
	{
		try
		{

			String oldString = owlEntitiesBundle.getActivityIndividual().getIRI().toString();
			String newString = oldString.replace("_databasename", "_"+owlEntitiesBundle.getDatabaseName());
			String pathStr = owlEntitiesBundle.getDb2OWLMappingPrimitivePhysicalURI().toString();
			
			if (pathStr.contains("file:"))
				pathStr = pathStr.replace("file:", "");
			
			Path path = Paths.get(pathStr);
			Charset charset = StandardCharsets.UTF_8;
	
			String content = new String(Files.readAllBytes(path), charset);
			content = content.replaceAll(oldString, newString);
			Files.write(path, content.getBytes(charset));
			
			pathStr = owlEntitiesBundle.getDb2OWLMappingComplexPhysicalURI().toString();
			if (pathStr.contains("file:"))
				pathStr = pathStr.replace("file:", "");
			path = Paths.get(pathStr);
	
			content = new String(Files.readAllBytes(path), charset);
			content = content.replaceAll(oldString, newString);
			Files.write(path, content.getBytes(charset));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
