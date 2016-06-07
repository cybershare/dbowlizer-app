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
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class EntityConceptMappingHandler 
{
	public static void processEntityConceptMappings(OWLEntitiesBundle owlEntitiesBundle, MappedEntitiesBundle mappedEntitiesBundle)
	{
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.properties");
		OWLOntologyManager ontologyManager = owlEntitiesBundle.getOntologyManager();
		OWLDataFactory factory = ontologyManager.getOWLDataFactory();
		Reasoner mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		OWLOntology db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		DefaultPrefixManager basePrefix = owlEntitiesBundle.getBasePrefix();
				
		OWLClass continuantCls = owlEntitiesBundle.getContinuantCls();
		OWLClass conceptMappingClass = factory.getOWLClass(propertiesManager.getString("entityConceptMapping"),basePrefix);
		
		String individualURI = owlEntitiesBundle.getIndividualURI();

		//Querying the reasoner to retrieve all instances of the class, direct and indirect

		NodeSet<OWLNamedIndividual> conceptIndividualsNodeSet = mappingReasoner.getInstances(conceptMappingClass,false);

		//Create the OWL classes corresponding to these individuals and add them into the output ontology
		if (conceptIndividualsNodeSet != null)
		{
			//Set of named individuals
			Set<OWLNamedIndividual> entityConceptIndividuals = conceptIndividualsNodeSet.getFlattened();

			for (OWLNamedIndividual owlCls : entityConceptIndividuals)
			{
				String iriStr = owlCls.getIRI().toString();

				String dbRelationName = iriStr.replace(individualURI, "");
				
				OWLClass currentOWLCls = factory.getOWLClass(dbRelationName,basePrefix);
				
				OWLSubClassOfAxiom subClsAxiom = factory.getOWLSubClassOfAxiom(currentOWLCls,continuantCls);
				
				ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology, subClsAxiom));
				
				//PROV-O
				PROVOHandler.owlClassCreated(owlEntitiesBundle, currentOWLCls, dbRelationName, db2OWLPrimitiveOntology);
				
				//Annotation property
				OWLAnnotationProperty commentProperty = factory.getRDFSComment();
				OWLLiteral commentValue = factory.getOWLLiteral("A class that represents an entity generated from the database relation " + dbRelationName);
				
				//OWL Annotation
				OWLAnnotation commentAnnotation = factory.getOWLAnnotation(commentProperty,commentValue);
				OWLAnnotationAssertionAxiom annotationAxiom = factory.getOWLAnnotationAssertionAxiom(currentOWLCls.getIRI(),commentAnnotation);
				
				ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,annotationAxiom));
				
				mappedEntitiesBundle.addOWLClassToRelationNameMapping(currentOWLCls, dbRelationName);
			}
			
			try {
				ontologyManager.saveOntology(db2OWLPrimitiveOntology);
			} catch (OWLOntologyStorageException e) {
				
				e.printStackTrace();
			}
		}
		
	}
}
