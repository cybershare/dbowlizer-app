
package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBAttributeRestriction;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;


/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */
public class DBAttributeAxioms extends Axioms {
    
    private DBAttribute attribute;

    public DBAttributeAxioms(OWLIndividual individual, DBAttribute attribute, OWLUtils bundle) {
        super(individual, bundle);
        this.attribute = attribute;
    }

    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBAttribute());
        this.setNonKeyAttribute();
        this.setDefaultValue();
        this.setRestrictions();
    }
    
    public void setNonKeyAttribute(){
        if (attribute.is_nonKeyAttribute())
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBNonKeyAttribute());
    }
    
    public void setDefaultValue(){
        //OWLLiteral defaultValueIndividual = null;
        if (attribute.getDefaultvalue() != null) {
            //defaultValueIndividual = bundle.getFactory().getOWLLiteral(attribute.getDefaultvalue());
            OWLAxiom axiom = bundle.getFactory().getOWLDataPropertyAssertionAxiom(vocabulary_RelationalToModel.getDataProperty_hasDefaultValue(), individual, attribute.getDefaultvalue());
            add(axiom);
        }
    }
    
    public void setRestrictions(){
        if(attribute.isNN()){
            OWLIndividual restrictionIndividual = null;
            for (DBAttributeRestriction restriction : attribute.getAttributeRestrictions()){
                restrictionIndividual = Individuals.getIndividual(restriction, bundle);
                OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasAttributeRestriction(), individual, restrictionIndividual);
                add(axiom);
            }
        }
    }

}
