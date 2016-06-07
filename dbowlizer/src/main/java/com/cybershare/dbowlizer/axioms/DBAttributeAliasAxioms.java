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
import com.cybershare.dbowlizer.dbmodel.DBAttributeAlias;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

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
