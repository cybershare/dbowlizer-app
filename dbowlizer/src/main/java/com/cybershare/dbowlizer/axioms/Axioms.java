package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.ontology.OWLUtils;
import com.cybershare.dbowlizer.vocabulary.RelationalToModel;
import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

public abstract class Axioms extends ArrayList<OWLAxiom> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected OWLUtils bundle;	
	protected OWLIndividual individual;
	
	protected RelationalToModel vocabulary_RelationalToModel;
	//protected ELSEWebData vocabulary_data;
		
	protected Axioms(OWLIndividual individual, OWLUtils bundle){
		this.individual = individual;
		this.bundle = bundle;
		initializeVocabularies();
	}
	
	private void initializeVocabularies(){
		vocabulary_RelationalToModel = new RelationalToModel(bundle);
		//vocabulary_data = new ELSEWebData(bundle);
	}
		
	protected void addTypeAxiom(OWLClass owlClass){
		OWLClassAssertionAxiom classAssertionAxiom = bundle.getFactory().getOWLClassAssertionAxiom(owlClass, individual);
		add(classAssertionAxiom);
	}	
	
	public abstract void setAxioms();

}
