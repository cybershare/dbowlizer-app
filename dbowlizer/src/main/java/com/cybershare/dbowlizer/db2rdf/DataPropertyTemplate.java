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
	
	
	
	/*
	 * These methods will trim the IRI into the last '/'
	 * in order to get the BaseIRI.
	 * 
	 */
	//Todo finish this mehtod
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
