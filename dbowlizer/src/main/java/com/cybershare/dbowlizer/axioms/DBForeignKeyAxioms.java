/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBForeignKey;
import com.cybershare.dbowlizer.dbmodel.DBPrimaryKey;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
public class DBForeignKeyAxioms extends Axioms {
    
    private DBForeignKey dbforeignkey;

    public DBForeignKeyAxioms(OWLIndividual individual, DBForeignKey dbforeignkey, OWLUtils bundle) {
        super(individual, bundle);
        this.dbforeignkey = dbforeignkey;
    }

    
    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBForeignKey());
        addAttribute();
        addReference();
        
        //Foreign key classification
        if(this.dbforeignkey.isNonPrimaryFK()){
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBNonPrimaryForeignKey());
        }
    }

    private void addAttribute(){
        OWLIndividual attributeIndividual = null;
        DBAttribute attribute = dbforeignkey.getAttribute();
        attributeIndividual = Individuals.getIndividual(attribute, bundle);
        OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, attributeIndividual);
        add(axiom); 
    }
    
    private void addReference(){
        OWLIndividual referencesIndividual = null;
        DBPrimaryKey primarykey = dbforeignkey.getReferencedKey();
        referencesIndividual = Individuals.getIndividual(primarykey, bundle);
        OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_references(), individual, referencesIndividual);
        add(axiom); 
    }
    
}
