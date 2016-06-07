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

package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.ontology.OWLUtils;
import com.cybershare.dbowlizer.vocabulary.RelationalToModel;
import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

public abstract class Axioms extends ArrayList<OWLAxiom> {

	private static final long serialVersionUID = 1L;
	
	protected OWLUtils bundle;	
	protected OWLIndividual individual;
	
	protected RelationalToModel vocabulary_RelationalToModel;
		
	protected Axioms(OWLIndividual individual, OWLUtils bundle){
		this.individual = individual;
		this.bundle = bundle;
		initializeVocabularies();
	}
	
	private void initializeVocabularies(){
		vocabulary_RelationalToModel = new RelationalToModel(bundle);
	}
		
	protected void addTypeAxiom(OWLClass owlClass){
		OWLClassAssertionAxiom classAssertionAxiom = bundle.getFactory().getOWLClassAssertionAxiom(owlClass, individual);
		add(classAssertionAxiom);
	}	
	
	public abstract void setAxioms();

}
