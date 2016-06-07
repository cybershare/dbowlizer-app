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


public class ReasonerManager {

	public static void addAxiomsThroughReasoner(OWLOntologyManager manager, OWLOntology ontology)
	{
	
		Configuration conf = new Configuration();
		conf.ignoreUnsupportedDatatypes=true; //by default is set to 'false'

		Reasoner hermitReasoner = new Reasoner(conf, ontology);
		OWLReasoner owlReasoner = hermitReasoner;

		final List<InferredAxiomGenerator<? extends OWLAxiom>> axiomGenerators =
				new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();

		InferredOntologyGenerator iog = new InferredOntologyGenerator(owlReasoner);
		
		System.out.println("Axioms before running Hermit: " +ontology.getAxiomCount());
		iog.fillOntology(manager, ontology);
		System.out.println("Axioms after running Hermit: " +ontology.getAxiomCount());

	}
}

