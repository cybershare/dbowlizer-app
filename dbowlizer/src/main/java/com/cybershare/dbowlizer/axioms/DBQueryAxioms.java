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

import com.cybershare.dbowlizer.dbmodel.DBAttributeAlias;
import com.cybershare.dbowlizer.dbmodel.DBQuery;
import com.cybershare.dbowlizer.dbmodel.DBQueryCondition;
import com.cybershare.dbowlizer.dbmodel.DBQueryJoin;
import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.ontology.Individuals;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;


public class DBQueryAxioms extends Axioms {
    
    private DBQuery dbquery;

    public DBQueryAxioms(OWLIndividual individual, DBQuery dbquery, OWLUtils bundle) {
        super(individual, bundle);
        this.dbquery = dbquery;
    }

    @Override
    public void setAxioms() {
        this.addTypeAxiom(this.vocabulary_RelationalToModel.getOWLClass_DBQuery());
        this.setAliases();
        this.setRelations();
        this.setConditions();
        this.setJoins();
    }
    
    private void setAliases(){
        OWLIndividual aliasIndividual = null;
        if(dbquery.isSet_aliases()){
            for (DBAttributeAlias alias : dbquery.getAliases()){
                aliasIndividual = Individuals.getIndividual(alias, bundle);
                OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, aliasIndividual);
                add(axiom);
            }
        }
    }
    
    private void setRelations(){
        OWLIndividual relationIndividual = null;
        if(dbquery.isSet_relations()){
            for (DBRelation relation : dbquery.getRelations()){
                relationIndividual = Individuals.getIndividual(relation, bundle);
                OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, relationIndividual);
                add(axiom);
            }
        }
    }
    
    private void setConditions(){
        OWLIndividual conditionIndividual = null;
        if(dbquery.isSet_conditions()){
            for (DBQueryCondition condition : dbquery.getConditions()){
                conditionIndividual = Individuals.getIndividual(condition, bundle);
                OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, conditionIndividual);
                add(axiom);
            }
        }       
    }
    
    private void setJoins(){
        OWLIndividual joinIndividual = null;
        if(dbquery.isSet_joins()){
            for (DBQueryJoin join : dbquery.getJoins()){
                joinIndividual = Individuals.getIndividual(join, bundle);
                OWLAxiom axiom = bundle.getFactory().getOWLObjectPropertyAssertionAxiom(vocabulary_RelationalToModel.getObjectProperty_hasPart(), individual, joinIndividual);
                add(axiom);
            }
        }       
    }
    
}
