
package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttributeRestriction;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */
public class DBAttributeRestrictionAxioms extends Axioms {
    
    private DBAttributeRestriction dbattributerestriction;

    public DBAttributeRestrictionAxioms(OWLIndividual individual, DBAttributeRestriction dbattributerestriction, OWLUtils bundle) {
        super(individual, bundle);
        this.dbattributerestriction = dbattributerestriction;
    }

    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBAttributeRestriction());
    }
        
}
