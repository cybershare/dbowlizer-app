/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBForeignKey;
import com.cybershare.dbowlizer.dbmodel.DBPrimaryKey;
import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
public class DBRelationAxioms extends Axioms {
    
    private DBRelation dbrelation;

    public DBRelationAxioms(OWLIndividual individual, DBRelation dbrelation, OWLUtils bundle) {
        super(individual, bundle);
        this.dbrelation = dbrelation;
    }

    
    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBRelation());
        addAttributes();
        addPrimaryKey();
        addForeignKeys();
    }

    private void addAttributes(){
        OWLIndividual attributeIndividual = null;
        for (DBAttribute attribute : dbrelation.getAttributes()){
            attributeIndividual = Individuals.getIndividual(attribute, bundle);
            OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, attributeIndividual);
            add(axiom);
        }
    }
    
    private void addPrimaryKey(){
        OWLIndividual primaryKeyIndividual = null;
        DBPrimaryKey pk = dbrelation.getPrimaryKey();
        primaryKeyIndividual = Individuals.getIndividual(pk, bundle);
        OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, primaryKeyIndividual);
        add(axiom);
    }
    
    private void addForeignKeys(){
        OWLIndividual foreignKeyIndividual = null;
        for (DBForeignKey fk : dbrelation.getForeignKeys()){
            foreignKeyIndividual = Individuals.getIndividual(fk, bundle);
            OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, foreignKeyIndividual);
            add(axiom);
        }
    }
    
}
