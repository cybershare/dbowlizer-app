package com.cybershare.dbowlizer.generate;

import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
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
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");
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
		System.out.println("concept mapping class: "+conceptMappingClass.toString());

		//Create the OWL classes corresponding to these individuals and add them into the output ontology
		if (conceptIndividualsNodeSet != null)
		{
			//Set of named individuals
			Set<OWLNamedIndividual> entityConceptIndividuals = conceptIndividualsNodeSet.getFlattened();

			for (OWLNamedIndividual owlCls : entityConceptIndividuals)
			{
				String iriStr = owlCls.getIRI().toString();

				String dbRelationName = iriStr.replace(individualURI, "");
				
                System.out.println(iriStr);
                System.out.println(individualURI);     
				System.out.println(dbRelationName);
				System.out.println(basePrefix);
				
				OWLClass currentOWLCls = factory.getOWLClass(dbRelationName,basePrefix);
				
				OWLSubClassOfAxiom subClsAxiom = factory.getOWLSubClassOfAxiom(currentOWLCls,continuantCls);
				
				ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology, subClsAxiom));
				
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
