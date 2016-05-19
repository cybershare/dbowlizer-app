package com.cybershare.dbowlizer.generate;

import java.util.HashMap;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class MappedEntitiesBundle {
	
	
	private HashMap<OWLClass, String> owlClassToRelationNameMap;
	private HashMap<OWLDataProperty, String> owlDataPropertyToAttributeName;
	private HashMap<OWLObjectProperty, String> owlObjectPropertyToAttributeName;
		
	public MappedEntitiesBundle()
	{
		owlClassToRelationNameMap = new HashMap<OWLClass, String>();
		owlDataPropertyToAttributeName = new HashMap<OWLDataProperty, String>();
		owlObjectPropertyToAttributeName = new HashMap<OWLObjectProperty, String>();
	}
	

	public void addOWLClassToRelationNameMapping(OWLClass owlClass, String relationName)
	{
		owlClassToRelationNameMap.put(owlClass, relationName);
	}
	
	public void addOWLDataPropertyToAttributeNameMapping(OWLDataProperty owlDataProperty, String attributeName)
	{
		owlDataPropertyToAttributeName.put(owlDataProperty, attributeName);
	}
	
	public void addOWLObjectPropertyToAttributeNameMapping(OWLObjectProperty owlObjectProperty, String attributeName)
	{
		owlObjectPropertyToAttributeName.put(owlObjectProperty, attributeName);
	}


	public HashMap<OWLClass, String> getOwlClassToRelationNameMap() {
		return owlClassToRelationNameMap;
	}


	public HashMap<OWLDataProperty, String> getOwlDataPropertyToAttributeName() {
		return owlDataPropertyToAttributeName;
	}


	public HashMap<OWLObjectProperty, String> getOwlObjectPropertyToAttributeName() {
		return owlObjectPropertyToAttributeName;
	}

	public void printAll(){
        for(final OWLClass owlClass : owlClassToRelationNameMap.keySet()){
        	String owlclass=owlClassToRelationNameMap.get(owlClass);
        	System.out.println("OwlClass: "+ owlClass.getIRI());
        	System.out.println("Table: "+ owlclass);
        }   
        System.out.println();
        for(final OWLDataProperty owlDataProperty : owlDataPropertyToAttributeName.keySet()){
            String owlDataproperty=owlDataPropertyToAttributeName.get(owlDataProperty);
            System.out.println("OwlDataProperty: "+ owlDataProperty.getIRI());
            System.out.println("Attribute: "+ owlDataproperty);
        }   
        System.out.println();
        for(final OWLObjectProperty owlObjectProperty : owlObjectPropertyToAttributeName.keySet()){
            String owlObjectproperty=owlObjectPropertyToAttributeName.get(owlObjectProperty);
            System.out.println("OwlObjectProperty: "+ owlObjectProperty.getIRI());
            System.out.println("Attribute: "+ owlObjectproperty);
        }   
	
	}

}
