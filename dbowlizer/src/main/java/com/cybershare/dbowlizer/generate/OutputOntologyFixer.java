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
