package com.cybershare.dbowlizer.reasoner;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

/**
 * 
 * @author Diego Aguirre <daguirre6 at miners.utep.edu>
 */

public class ReasonerManager {

	public static void addAxiomsThroughReasoner(OWLOntologyManager manager, OWLOntology ontology)
	{
	
		Configuration conf = new Configuration();
		conf.ignoreUnsupportedDatatypes=true; //by default is set to 'false'

		Reasoner hermitReasoner = new Reasoner(conf, ontology);
//		PelletReasoner pelletReasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
//		pelletReasoner.getKB().realize();
		OWLReasoner owlReasoner = hermitReasoner;

		final List<InferredAxiomGenerator<? extends OWLAxiom>> axiomGenerators =
				new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();

		InferredOntologyGenerator iog = new InferredOntologyGenerator(owlReasoner);
		

		System.out.println("Axioms before running Hermit: " +ontology.getAxiomCount());
		iog.fillOntology(manager, ontology);
		System.out.println("Axioms after running Hermit: " +ontology.getAxiomCount());


	}
}

