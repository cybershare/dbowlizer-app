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
import com.cybershare.dbowlizer.dbmodel.DBAttributeRestriction;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;


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
        this.setDomain();
    }
    
    public void setNonKeyAttribute(){
        if (attribute.is_nonKeyAttribute())
            this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBNonKeyAttribute());
    }
    
    public void setDefaultValue(){
        if (attribute.getDefaultvalue() != null) {
            OWLAxiom axiom = bundle.getFactory().getOWLDataPropertyAssertionAxiom(vocabulary_RelationalToModel.getDataProperty_hasDefaultValue(), individual, attribute.getDefaultvalue());
            add(axiom);
        }
    }
    
    public void setDomain(){
        OWLIndividual domainIndividual = null;
        if (attribute.getDomain() != null) {
            domainIndividual = Individuals.getIndividual(attribute.getDomain(), bundle);
            OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasAttributeDomain(), individual, domainIndividual);
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
