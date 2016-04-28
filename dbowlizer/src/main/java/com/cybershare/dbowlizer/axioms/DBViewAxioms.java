/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBView;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
public class DBViewAxioms extends Axioms {
    
    private DBView dbview;

    public DBViewAxioms(OWLIndividual individual, DBView dbview, OWLUtils bundle) {
        super(individual, bundle);
        this.dbview = dbview;
    }

    
    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBView());
    }    
    
}
