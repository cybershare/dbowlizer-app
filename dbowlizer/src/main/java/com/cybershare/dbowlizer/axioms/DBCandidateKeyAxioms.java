
package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBCandidateKey;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */
public class DBCandidateKeyAxioms extends Axioms {
    
    private DBCandidateKey dbcandidatekey;

    public DBCandidateKeyAxioms(OWLIndividual individual, DBCandidateKey dbcandidatekey, OWLUtils bundle) {
        super(individual, bundle);
        this.dbcandidatekey = dbcandidatekey;
    }

    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBCandidateKey());
        this.addAttribute();
    }

    
    private void addAttribute(){
        OWLIndividual attributeIndividual = null;
        DBAttribute attribute = dbcandidatekey.getKeyAttribute();
        attributeIndividual = Individuals.getIndividual(attribute, bundle);
        OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasAttribute(), individual, attributeIndividual);
        add(axiom);
    }
    
}
