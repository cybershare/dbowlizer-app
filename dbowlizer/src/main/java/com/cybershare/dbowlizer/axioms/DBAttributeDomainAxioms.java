
package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttributeDomain;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */
public class DBAttributeDomainAxioms extends Axioms {
    
    private DBAttributeDomain dbattributedomain;

    public DBAttributeDomainAxioms(OWLIndividual individual, DBAttributeDomain dbattributedomain, OWLUtils bundle) {
        super(individual, bundle);
        this.dbattributedomain = dbattributedomain;
    }

    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBAttributeDomain());
    }
        
}
