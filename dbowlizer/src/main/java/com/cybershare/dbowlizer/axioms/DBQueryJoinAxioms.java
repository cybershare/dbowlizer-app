
package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBQueryJoin;
import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */

public class DBQueryJoinAxioms extends Axioms {
    
    private DBQueryJoin dbqueryjoin;

    public DBQueryJoinAxioms(OWLIndividual individual, DBQueryJoin dbqueryjoin, OWLUtils bundle) {
        super(individual, bundle);
        this.dbqueryjoin = dbqueryjoin;
    }

    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBQueryJoin());
        this.setRelation();
        this.setAttributes();
    }
    
    public void setRelation(){
        OWLIndividual relationIndividual = null;
        DBRelation relation = dbqueryjoin.getRelation();
        relationIndividual = Individuals.getIndividual(relation, bundle);
        OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, relationIndividual);
        add(axiom);
    }
    
    public void setAttributes(){
        OWLIndividual attributeIndividual = null;
        for (DBAttribute attribute : dbqueryjoin.getAttributes()){
            attributeIndividual = Individuals.getIndividual(attribute, bundle);
            OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, attributeIndividual);
            add(axiom);
        }
    }
}
