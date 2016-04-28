package com.cybershare.dbowlizer.generate;

import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class VirtualPropertyMappingHandler 
{
	private static OWLOntologyManager ontologyManager;
	private static OWLDataFactory factory;
	private static Reasoner mappingReasoner;
	private static OWLClass virtualPropertyMappingClass;
	private static String individualURI;
	private static DefaultPrefixManager basePrefix;
	private static OWLClass continuantCls;
	private static OWLOntology db2OWLPrimitiveOntology;

	public static void processVirtualPropertyMappings(OWLEntitiesBundle owlEntitiesBundle)
	{
		ontologyManager = owlEntitiesBundle.getOntologyManager();
		factory = ontologyManager.getOWLDataFactory();
		mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		virtualPropertyMappingClass = owlEntitiesBundle.getVirtualPropertyMappingClass();
		individualURI = owlEntitiesBundle.getIndividualURI();
		basePrefix = owlEntitiesBundle.getBasePrefix();
		continuantCls = owlEntitiesBundle.getContinuantCls();
		db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();

		NodeSet<OWLNamedIndividual> propertyIndividualsNodeSet = mappingReasoner.getInstances(virtualPropertyMappingClass,false);
		//#Create the OWL classes corresponding to these individuals and add them into the output ontology.
		if (propertyIndividualsNodeSet != null)
		{
			//#Set of named individuals
			Set<OWLNamedIndividual> propertyIndividuals = propertyIndividualsNodeSet.getFlattened();
			for (OWLNamedIndividual virtualProperty : propertyIndividuals)
			{
				String virtualPropertyName = virtualProperty.getIRI().toString().replace(individualURI,"");
				//#This property will be a subclass of the original property and will have as domain the

				OWLObjectProperty currentOWLProperty = factory.getOWLObjectProperty(virtualPropertyName,basePrefix);

				//#This will be equivalent to the condition restrictions obtained
				OWLClass currentOWLPropertyDomainClass = factory.getOWLClass(virtualPropertyName+"_domain",basePrefix);

				//TODO: The method will be moved to a utils class
				OWLObjectIntersectionOf restrictedDomainClass = VirtualConceptMappingHandler.processConditionsForView(virtualProperty,null,null,null);
				
				if (currentOWLPropertyDomainClass != null && restrictedDomainClass != null)
				{
					
					OWLEquivalentClassesAxiom equivalentAxiom = factory.getOWLEquivalentClassesAxiom(currentOWLPropertyDomainClass,restrictedDomainClass);

					OWLSubClassOfAxiom subClsAxiom = factory.getOWLSubClassOfAxiom(currentOWLPropertyDomainClass,continuantCls);
					ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,subClsAxiom));
					ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,equivalentAxiom));

					OWLObjectPropertyDomainAxiom propertyDomainAxiom = factory.getOWLObjectPropertyDomainAxiom(currentOWLProperty,currentOWLPropertyDomainClass);
					ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,propertyDomainAxiom));

					try {
						ontologyManager.saveOntology(db2OWLPrimitiveOntology);
					} catch (OWLOntologyStorageException e) {
						e.printStackTrace();
					}
				}

				//#Annotation property
				OWLAnnotationProperty commentProperty = factory.getRDFSComment();
				OWLLiteral commentValue = factory.getOWLLiteral("A class that represents an entity generated from "+virtualPropertyName.toString());
				//#OWL Annotation
				OWLAnnotation commentAnnotation = factory.getOWLAnnotation(commentProperty,commentValue);

				OWLAnnotationAssertionAxiom annotationAxiom = factory.getOWLAnnotationAssertionAxiom(currentOWLProperty.getIRI(),commentAnnotation);
				ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,annotationAxiom));

			}

		}
		try {
			ontologyManager.saveOntology(db2OWLPrimitiveOntology);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}


	}




}
