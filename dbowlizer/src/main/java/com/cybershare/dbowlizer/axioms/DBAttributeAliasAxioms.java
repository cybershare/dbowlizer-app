
package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBAttributeAlias;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */
public class DBAttributeAliasAxioms extends Axioms {
    
    private DBAttributeAlias dbattributealias;

    public DBAttributeAliasAxioms(OWLIndividual individual, DBAttributeAlias dbattributealias, OWLUtils bundle) {
        super(individual, bundle);
        this.dbattributealias = dbattributealias;
    }

    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBAttributeAlias());
        this.setAttribute();
        this.setAliasName();
    }
    
    private void setAttribute(){
        OWLIndividual attributeIndividual = null;
        DBAttribute attribute = dbattributealias.getAttribute();
        attributeIndividual = Individuals.getIndividual(attribute, bundle);
        OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, attributeIndividual);
        add(axiom);
    }
    
    private void setAliasName(){
        OWLAxiom axiom = bundle.getFactory().getOWLDataPropertyAssertionAxiom(vocabulary_RelationalToModel.getDataProperty_hasAliasName(), individual, dbattributealias.getAttributeAliasName());
        add(axiom);  
    }
    
}
