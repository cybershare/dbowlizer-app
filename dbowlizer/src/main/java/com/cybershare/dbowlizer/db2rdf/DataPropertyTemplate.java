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

package com.cybershare.dbowlizer.db2rdf;


import org.apache.jena.rdf.model.ResourceFactory;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;

import eu.optique.api.mapping.MappingFactory;
import eu.optique.api.mapping.ObjectMap;
import eu.optique.api.mapping.PredicateMap;
import eu.optique.api.mapping.PredicateObjectMap;
import eu.optique.api.mapping.R2RMLMappingManager;
import eu.optique.api.mapping.TermMap.TermMapType;
import eu.optique.api.mapping.TriplesMap;
import eu.optique.api.mapping.impl.jena.JenaR2RMLMappingManagerFactory;

public class DataPropertyTemplate {
	private R2RMLMappingManager mappingManager= new JenaR2RMLMappingManagerFactory().getR2RMLMappingManager();
	private MappingFactory mappingFactory= mappingManager.getMappingFactory();

	public DataPropertyTemplate(){		
	}
	
	public TriplesMap addDataPropertiesToTriplesMap(TriplesMap trip, OWLDataProperty dataProperty, DBAttribute column){
		TriplesMap tripTemp = trip;
		tripTemp.addPredicateObjectMap(pomDataProperties(dataProperty,column));
		return tripTemp;
	}
	
	public  PredicateObjectMap pomDataProperties(OWLDataProperty dataProperty, DBAttribute column){
		PredicateMap predicate = mappingFactory.createPredicateMap(TermMapType.CONSTANT_VALUED,dataProperty.getIRI().toString());
		ObjectMap object = mappingFactory.createObjectMap(TermMapType.COLUMN_VALUED,column.getColumnName());
		try{
			XSDs xsds = new XSDs();
			object.setDatatype(ResourceFactory.createResource(xsds.getXSDStrings(column.getDatatype())));
			}
		catch(Exception e){
			System.out.println(e);
		}
		PredicateObjectMap pom = mappingFactory.createPredicateObjectMap(predicate,object);
		return pom;
	}
	
	public String getNameOWLClass(OWLClass owlclass){
		String[] base= owlclass.getIRI().toString().split(":");
		return base[base.length-1];
	}

	public String getBaseIRI(OWLClass owlClass){
		String[] base= owlClass.getIRI().toString().split(":");
		String baseIRI="";
		for(int i=0;i<base.length-1;i++){
			baseIRI=baseIRI+base[i];
		}
		return baseIRI;
	}
	
	public String getBaseIRI(OWLDataProperty dataProperty){
		String[] base= dataProperty.getIRI().toString().split(":");
		String baseIRI="";
		for(int i=0;i<base.length;i++){
			baseIRI=baseIRI+base[i];
		}
		return baseIRI;
	}
	
	public String columnName(String dbAttribute){
		String[] base= dbAttribute.split("\\.");
		return base[base.length-1];	
	}
	
	public String tableName(String dbAttribute){
		String[] base= dbAttribute.split("\\.");
		return base[0];	
	}
	
	
}
