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

import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class DatatypePropertyMappingHandler 
{
	public static void processDatatypePropertyMappings(OWLEntitiesBundle owlEntitiesBundle, MappedEntitiesBundle mappedEntitiesBundle)
	{
		OWLOntologyManager ontologyManager = owlEntitiesBundle.getOntologyManager();
		OWLDataFactory factory = ontologyManager.getOWLDataFactory();
		Reasoner mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		OWLOntology db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		DefaultPrefixManager basePrefix = owlEntitiesBundle.getBasePrefix();
		String individualURI = owlEntitiesBundle.getIndividualURI();
		
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.properties");
		

		OWLClass datatypePropertyMapping = factory.getOWLClass(propertiesManager.getString("datatypePropertyMapping"),basePrefix);

		//#Create the OWL data properties corresponding to these individuals and add them into the output ontology.
		//#Querying the reasoner to retrieve all instances of the class, direct and indirect.
		NodeSet<OWLNamedIndividual> dataPropertiesNodeSet = mappingReasoner.getInstances(datatypePropertyMapping,false);
		if (dataPropertiesNodeSet != null)
		{

			Set<OWLNamedIndividual> dataPropertyIndividuals = dataPropertiesNodeSet.getFlattened();
			
			for(OWLNamedIndividual dataProp : dataPropertyIndividuals)
			{

				String iriStr = dataProp.getIRI().toString();
				String dbAttName = iriStr.replace(individualURI, "");
				
				OWLDataProperty currentOWLDataProp = factory.getOWLDataProperty(getPropertyName(dbAttName),basePrefix);
				//#This line adds Literal as the top datatype when the range is not known.
				OWLSubDataPropertyOfAxiom dataPropAxiom = factory.getOWLSubDataPropertyOfAxiom(currentOWLDataProp,factory.getOWLTopDataProperty());
				ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,dataPropAxiom));

				mappedEntitiesBundle.addOWLDataPropertyToAttributeNameMapping(currentOWLDataProp, dbAttName);
			}
			
			try {
				ontologyManager.saveOntology(db2OWLPrimitiveOntology);
			} catch (OWLOntologyStorageException e) {
				e.printStackTrace();
			}
		}


	}
	
	private static String getPropertyName(String objectName)
	{
		return "has" + objectName;		
	}


}
