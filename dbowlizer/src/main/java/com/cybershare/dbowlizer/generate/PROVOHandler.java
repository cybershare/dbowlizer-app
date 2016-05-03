package com.cybershare.dbowlizer.generate;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class PROVOHandler 
{
	
	public static void owlClassCreated(OWLEntitiesBundle owlEntitiesBundle, OWLClass owlClass, String derivedFromStr, OWLOntology ontology)
	{
		OWLDataFactory factory = owlEntitiesBundle.getFactory();
		DefaultPrefixManager basePrefix = owlEntitiesBundle.getBasePrefix();
		OWLOntologyManager ontologyManager = owlEntitiesBundle.getOntologyManager();
		
		//Handling wasDerivedFrom property
		OWLIndividual ind = factory.getOWLNamedIndividual(IRI.create(basePrefix.getDefaultPrefix() + owlEntitiesBundle.getDatabaseName() + ":" + derivedFromStr));
		OWLClassAssertionAxiom instanceAxiom = factory.getOWLClassAssertionAxiom(factory.getOWLThing(), ind);
		AddAxiom addAxiom = new AddAxiom(ontology, instanceAxiom);
		ontologyManager.applyChange(addAxiom);
		
		OWLObjectHasValue derivedFrom = factory.getOWLObjectHasValue(owlEntitiesBundle.getWasDerivedFrom(), ind);
		
		OWLSubClassOfAxiom subClsAxiom = factory.getOWLSubClassOfAxiom(owlClass,derivedFrom);
		ontologyManager.applyChange(new AddAxiom(ontology, subClsAxiom));
		
		//Handling wasGeneratedBy property
		OWLObjectHasValue generatedBy = factory.getOWLObjectHasValue(owlEntitiesBundle.getWasGeneratedBy(), owlEntitiesBundle.getActivityIndividual());
		
		subClsAxiom = factory.getOWLSubClassOfAxiom(owlClass,generatedBy);
		ontologyManager.applyChange(new AddAxiom(ontology, subClsAxiom));
		
		
	}

}
