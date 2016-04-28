package com.cybershare.dbowlizer.reasoner;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;

/**
 * 
 * @author Diego Aguirre <daguirre6 at miners.utep.edu>
 */

public class ReasonerManager {

	public static void addAxiomsThroughReasoner(OWLOntologyManager manager, OWLOntology ontology)
	{
		//		OWLReasoner hermit = new Reasoner.ReasonerFactory().createReasoner(ontology);
		//		
		//		System.out.println("Hermit: Is consitent: " + hermit.isConsistent());
		//		
		//		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		//		
		//		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		//		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		//		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
		//		
		//		
		//		OWLReasoner r = reasonerFactory.createReasoner(ontology);
		//		System.out.println(reasonerFactory.getReasonerName() + "|" + r.getReasonerName());

		Configuration conf = new Configuration();
		conf.ignoreUnsupportedDatatypes=true; //by default is set to 'false'
		Reasoner hermitReasoner = new Reasoner(conf, ontology);
		
//		hermitReasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
//		hermitReasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
//		hermitReasoner.precomputeInferences(InferenceType.DATA_PROPERTY_ASSERTIONS);
//		hermitReasoner.precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);
//		hermitReasoner.precomputeInferences(InferenceType.DIFFERENT_INDIVIDUALS);
//		hermitReasoner.precomputeInferences(InferenceType.DISJOINT_CLASSES);
//		hermitReasoner.precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
//		hermitReasoner.precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
//		hermitReasoner.precomputeInferences(InferenceType.SAME_INDIVIDUAL);


		final List<InferredAxiomGenerator<? extends OWLAxiom>> axiomGenerators =
				new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
//		axiomGenerators.add(new InferredClassAssertionAxiomGenerator());
//		axiomGenerators.add(new InferredDataPropertyCharacteristicAxiomGenerator());
//		axiomGenerators.add(new InferredEquivalentClassAxiomGenerator());
//		axiomGenerators.add(new InferredEquivalentDataPropertiesAxiomGenerator());
//		axiomGenerators.add(new InferredEquivalentObjectPropertyAxiomGenerator());
//		axiomGenerators.add(new InferredInverseObjectPropertiesAxiomGenerator());
//		axiomGenerators.add(new InferredObjectPropertyCharacteristicAxiomGenerator());

		// NOTE: InferredPropertyAssertionGenerator significantly slows down
		// inference computation
//		axiomGenerators.add(new org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator());
//
//		axiomGenerators.add(new InferredSubClassAxiomGenerator());
//		axiomGenerators.add(new InferredSubDataPropertyAxiomGenerator());
//		axiomGenerators.add(new InferredSubObjectPropertyAxiomGenerator());
//

		InferredOntologyGenerator iog = new InferredOntologyGenerator(hermitReasoner);
		

		System.out.println("Axioms before running Hermit: " +ontology.getAxiomCount());
		iog.fillOntology(manager, ontology);
		System.out.println("Axioms after running Hermit: " +ontology.getAxiomCount());


	}
}

