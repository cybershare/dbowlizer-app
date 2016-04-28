package com.cybershare.dbowlizer.generate;

import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class UnaryObjectPropertyMappingHandler 
{
	public static void processUnaryObjectPropertyMappings(OWLEntitiesBundle owlEntitiesBundle, MappedEntitiesBundle mappedEntitiesBundle)
	{
		OWLOntologyManager ontologyManager = owlEntitiesBundle.getOntologyManager();
		OWLDataFactory factory = ontologyManager.getOWLDataFactory();
		Reasoner mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		OWLOntology db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		DefaultPrefixManager basePrefix = owlEntitiesBundle.getBasePrefix();

		
		OWLClass unaryObjectPropertyMapping = owlEntitiesBundle.getUnaryObjectPropertyMapping();
		
		//#Querying the reasoner to retrieve all instances of the class, direct and indirect.
		NodeSet<OWLNamedIndividual> objectPropertiesNodeSet = mappingReasoner.getInstances(unaryObjectPropertyMapping,false);
		
		//#Create the OWL object properties corresponding to these individuals and add them into the output ontology.
		if (objectPropertiesNodeSet != null)
		{
			//#Set of named individuals
			Set<OWLNamedIndividual> unaryObjectPropertyIndividuals = objectPropertiesNodeSet.getFlattened();
			for (OWLNamedIndividual objectProp : unaryObjectPropertyIndividuals)
			{

				String iriStr = objectProp.getIRI().toString();

				String dbAttName = iriStr.replace(owlEntitiesBundle.getIndividualURI(), "");
				
				OWLObjectProperty currentOWLObjectProp = factory.getOWLObjectProperty(getPropertyName(dbAttName),basePrefix);

				OWLSubObjectPropertyOfAxiom subPropAxiom = factory.getOWLSubObjectPropertyOfAxiom(currentOWLObjectProp,factory.getOWLTopObjectProperty());
				ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,subPropAxiom));

				mappedEntitiesBundle.addOWLObjectPropertyToAttributeNameMapping(currentOWLObjectProp, dbAttName);
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
