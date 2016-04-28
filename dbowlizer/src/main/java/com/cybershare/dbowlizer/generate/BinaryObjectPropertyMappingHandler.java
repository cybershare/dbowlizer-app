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

public class BinaryObjectPropertyMappingHandler 
{
	public static void processBinaryObjectPropertyMappings(OWLEntitiesBundle owlEntitiesBundle, MappedEntitiesBundle mappedEntitiesBundle)
	{
		OWLOntologyManager ontologyManager = owlEntitiesBundle.getOntologyManager();
		OWLDataFactory factory = ontologyManager.getOWLDataFactory();
		Reasoner mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		OWLOntology db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		DefaultPrefixManager basePrefix = owlEntitiesBundle.getBasePrefix();
		
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");
		OWLClass binaryObjectPropertyMapping = factory.getOWLClass(propertiesManager.getString("binaryObjectPropertyMapping"),basePrefix);
		String individualURI = owlEntitiesBundle.getIndividualURI();
		//#Create the OWL object properties corresponding to these individuals and add them into the output ontology.
		//#Querying the reasoner to retrieve all instances of the class, direct and indirect.
		NodeSet<OWLNamedIndividual> objectPropertiesNodeSet = mappingReasoner.getInstances(binaryObjectPropertyMapping,false);
		
		if (objectPropertiesNodeSet != null)
		{
			//#Set of named individuals

			Set<OWLNamedIndividual> binaryObjectPropertyIndividuals = objectPropertiesNodeSet.getFlattened();
			
			for (OWLNamedIndividual objectProp : binaryObjectPropertyIndividuals)
			{

				String iriStr = objectProp.getIRI().toString();
				
				String dbAttName = iriStr.replace(individualURI, "");

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
