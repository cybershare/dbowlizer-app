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
		
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");
		

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
				//TODO: Make sure this will not cause problems in the future
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
