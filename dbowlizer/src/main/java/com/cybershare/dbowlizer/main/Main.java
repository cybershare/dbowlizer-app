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

package com.cybershare.dbowlizer.main;

import java.sql.Connection;

import org.json.simple.JSONObject;

import com.cybershare.dbowlizer.build.Builder;
import com.cybershare.dbowlizer.build.Director;
import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.db2rdf.R2RMLfactory;
import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBCandidateKey;
import com.cybershare.dbowlizer.dbmodel.DBForeignKey;
import com.cybershare.dbowlizer.dbmodel.DBPrimaryKey;
import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.dbmodel.DBSchema;
import com.cybershare.dbowlizer.generate.MappedEntitiesBundle;
import com.cybershare.dbowlizer.generate.OutputOntologyGenerator;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import com.cybershare.dbowlizer.ontology.OWLVisitor;
import com.cybershare.dbowlizer.utils.DriverSelector;
import com.cybershare.dbowlizer.utils.Settings;
import com.cybershare.dbowlizer.views.DBSchema2Owl;

public class Main {

    public static void main(String[] args) {
        
        /* Load configuration settings */
        Settings settings = new Settings("/schema2owl.config.properties");
        settings.setUuid("312312-312312-312312"); //used for testing json link creation
        
        /* Database Connector */
        DriverSelector selector = new DriverSelector(settings.getDriver());
        selector.setDataSource(settings.getHost(), settings.getPort(), settings.getDbname(), null);
        final Connection connection = selector.getConnection(settings.getUser(), settings.getPassword());
        
        //setup the empty product
        ModelProduct product = new ModelProduct();
        
        //setup the builder of the product
        Builder builder = new Builder(product);
        
        //setup the builder director
        Director director = new Director(builder);
        
        //get the data source and pass to the director
        director.construct(connection, selector);
        
        //create visitor to convert model product to axioms
        OWLUtils bundle = new OWLUtils(settings);
        OWLVisitor visitor = new OWLVisitor(bundle);
        
        //visit models and construct axioms  
        for(DBAttribute attribute : product.getAttributes())
            visitor.visit(attribute);
        
        for(DBRelation relation : product.getRelations())
            visitor.visit(relation);
        
        for(DBSchema schema : product.getSchemas())
            visitor.visit(schema);
        
        for(DBPrimaryKey primarykey: product.getPrimaryKeys())
            visitor.visit(primarykey);
        
        for(DBForeignKey foreignkey : product.getForeignKeys())
            visitor.visit(foreignkey);
        
        for(DBCandidateKey candidatekey: product.getCandidateKeys())
            visitor.visit(candidatekey);
        
        //TODO: Move the view parsing to builder pattern
        //for(DBView view : product.getViews())
        //visitor.visit(view);
        
        DBSchema2Owl dbSchema2Owl = new DBSchema2Owl(product, bundle);
        dbSchema2Owl.parseViewsAndCreateAxioms();
        
        //dump relational-model ontology
        bundle.saveOntology();
        
        OutputOntologyGenerator oPOG = new OutputOntologyGenerator(bundle, product, settings);
        MappedEntitiesBundle mappedEntitiesBundle = oPOG.createMappingOutputOntology();
        
        R2RMLfactory r2rmlFactory= new R2RMLfactory(product,mappedEntitiesBundle, settings);
        r2rmlFactory.startProduction();

    }

}
