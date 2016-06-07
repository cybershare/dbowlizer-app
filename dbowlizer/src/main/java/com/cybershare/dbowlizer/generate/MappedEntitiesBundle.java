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
