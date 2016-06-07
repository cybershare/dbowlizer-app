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
import com.cybershare.dbowlizer.dbmodel.DBForeignKey;
import com.cybershare.dbowlizer.dbmodel.DBPrimaryKey;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

public class DBForeignKeyAxioms extends Axioms {
    
    private DBForeignKey dbforeignkey;

    public DBForeignKeyAxioms(OWLIndividual individual, DBForeignKey dbforeignkey, OWLUtils bundle) {
        super(individual, bundle);
        this.dbforeignkey = dbforeignkey;
    }

    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBForeignKey());
        addAttribute();
        addReference();
        if(this.dbforeignkey.isNonPrimaryFK()){
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBNonPrimaryForeignKey());
        }
    }

    private void addAttribute(){
        OWLIndividual attributeIndividual = null;
        DBAttribute attribute = dbforeignkey.getAttribute();
        attributeIndividual = Individuals.getIndividual(attribute, bundle);
        OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, attributeIndividual);
        add(axiom); 
    }
    
    private void addReference(){
        OWLIndividual referencesIndividual = null;
        DBPrimaryKey primarykey = dbforeignkey.getReferencedKey();
        referencesIndividual = Individuals.getIndividual(primarykey, bundle);
        OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_references(), individual, referencesIndividual);
        add(axiom); 
    }
    
}
