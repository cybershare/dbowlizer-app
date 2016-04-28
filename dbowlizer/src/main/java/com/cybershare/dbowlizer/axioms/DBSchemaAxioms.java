
package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.dbmodel.DBSchema;
import com.cybershare.dbowlizer.dbmodel.DBView;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */

public class DBSchemaAxioms extends Axioms {
    
    private DBSchema dbschema;

    public DBSchemaAxioms(OWLIndividual individual, DBSchema dbschema, OWLUtils bundle) {
        super(individual, bundle);
        this.dbschema = dbschema;
    }
    
    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBSchema());
        addRelations();
        addViews();
    }

    private void addRelations(){
        OWLIndividual relationIndividual = null;
        for (DBRelation relation : dbschema.getRelations()){
            relationIndividual = Individuals.getIndividual(relation, bundle);
            OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, relationIndividual);
            add(axiom);
        }
    }
    
    private void addViews(){
        OWLIndividual viewIndividual = null;
        for (DBView view : dbschema.getViews()){
            viewIndividual = Individuals.getIndividual(view, bundle);
            OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, viewIndividual);
            add(axiom);
        }
    }
    
}
