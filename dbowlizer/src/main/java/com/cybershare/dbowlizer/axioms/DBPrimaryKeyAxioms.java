package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBPrimaryKey;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
public class DBPrimaryKeyAxioms extends Axioms {
    
    private DBPrimaryKey dbprimarykey;

    public DBPrimaryKeyAxioms(OWLIndividual individual, DBPrimaryKey dbprimarykey, OWLUtils bundle) {
        super(individual, bundle);
        this.dbprimarykey = dbprimarykey;
    }

    
    @Override
    public void setAxioms() {
        //Add class type DBPrimaryKey
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBPrimaryKey());
        addAttribute();
        
        //Primary key Classification, add corresponding subclass axiom
        if(dbprimarykey.isIndependent_PK())
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBIndependentPrimaryKey());
        if(dbprimarykey.isBinaryDependent_PK())
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBBinaryDependentPrimaryKey());
        if(dbprimarykey.isMultipleDependent_PK())
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBMultipleDependentPrimaryKey());
        if(dbprimarykey.isPartiallyBinaryDependent_PK())
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBPartiallyBinaryDependentPrimaryKey());
        if(dbprimarykey.isPartiallyMultipleDependent_PK())
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBPartiallyMultipleDependentPrimaryKey());
        if(dbprimarykey.isPartiallySingleDependent_PK())
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBPartiallySingleDependentPrimaryKey());
        if(dbprimarykey.isSingleDependent_PK())
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBSingleDependentPrimaryKey());
    }

    private void addAttribute(){
        OWLIndividual attributeIndividual = null;
        for (DBAttribute attribute : dbprimarykey.getAttributes()){
            attributeIndividual = Individuals.getIndividual(attribute, bundle);
            OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, attributeIndividual);
            add(axiom);
        }
    }
    
}
