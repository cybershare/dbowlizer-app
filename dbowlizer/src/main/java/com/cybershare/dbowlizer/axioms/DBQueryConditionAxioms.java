
package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBQueryCondition;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */

public class DBQueryConditionAxioms extends Axioms {
    
    private DBQueryCondition dbquerycondition;

    public DBQueryConditionAxioms(OWLIndividual individual, DBQueryCondition dbquerycondition, OWLUtils bundle) {
        super(individual, bundle);
        this.dbquerycondition = dbquerycondition;
    }

    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBQueryCondition());
        this.setAttribute();
        this.setValue();
        this.setOperator();
    }
    
    private void setAttribute(){
        OWLIndividual attributeIndividual = null;
        DBAttribute attribute = dbquerycondition.getAttribute();
        attributeIndividual = Individuals.getIndividual(attribute, bundle);
        OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, attributeIndividual);
        add(axiom);
    }
    
    private void setValue(){
        OWLAxiom axiom = bundle.getFactory().getOWLDataPropertyAssertionAxiom(vocabulary_RelationalToModel.getDataProperty_hasValue(), individual, dbquerycondition.getValue());
        add(axiom); 
    } 
    
    private void setOperator(){
        OWLAxiom axiom = bundle.getFactory().getOWLDataPropertyAssertionAxiom(vocabulary_RelationalToModel.getDataProperty_hasOperator(), individual, dbquerycondition.getValue());
        add(axiom);    
    }
    
}
