/*******************************************************************************
 * ========================================================================
 * DBOWLizer
 * http://dbowlizer.cybershare.utep.edu
 * Copyright (c) 2016, CyberShare Center of Excellence <cybershare@utep.edu>.
 * All rights reserved.
 * ------------------------------------------------------------------------
 *   
 *     This file is part of DBOWLizer
 *
 *     DBOWLizer is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     DBOWLizer is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with DBOWLizer.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.cybershare.dbowlizer.axioms;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBPrimaryKey;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;

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
        addRestriction();
        
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
    
    private void addRestriction(){
    	int attributeCount = dbprimarykey.getAttributeCount();
    	//System.out.println("attribute count: " +  attributeCount);
    	OWLObjectExactCardinality someCardinality = bundle.getFactory().getOWLObjectExactCardinality(attributeCount, vocabulary_RelationalToModel.getObjectProperty_hasPart(), vocabulary_RelationalToModel.getOWLClass_DBAttribute());
    	//System.out.println("exact cardinality: " +  someCardinality.toString());
    	OWLIndividual primarykeyIndividual = Individuals.getIndividual(dbprimarykey, bundle);
    	OWLAxiom axiom = bundle.getFactory().getOWLClassAssertionAxiom((OWLClassExpression)someCardinality, primarykeyIndividual);
    	add(axiom);
    }
    
}
