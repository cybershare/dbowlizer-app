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

package com.cybershare.dbowlizer.build;


import static com.cybershare.dbowlizer.utils.HelperUtils.remNoneAlphaNum;
import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBAttributeDomain;
import com.cybershare.dbowlizer.dbmodel.DBAttributeRestriction;
import com.cybershare.dbowlizer.dbmodel.DBCandidateKey;
import com.cybershare.dbowlizer.dbmodel.DBForeignKey;
import com.cybershare.dbowlizer.dbmodel.DBPrimaryKey;
import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.dbmodel.DBSchema;
import com.cybershare.dbowlizer.dbmodel.DBView;
import com.cybershare.dbowlizer.utils.HelperUtils;

import java.util.ArrayList;
import java.util.List;
import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;

public class Builder {
    
    private ModelProduct product;
    
    public Builder(ModelProduct product){
        this.product = product;
    }
    
    public void buildAttributeDomains(){
        List<String> adomains = new ArrayList<String>();
        adomains.add("dbowl_date");
        adomains.add("dbowl_double");
        adomains.add("dbowl_float");
        adomains.add("dbowl_integer");
        adomains.add("dbowl_string");
        for(String domain : adomains){
            DBAttributeDomain dbattributedomain = product.getAttributeDomain(domain); 
            dbattributedomain.setAttributeDomainName(domain);
        }
    }
    
    public void buildAttributeRestrictions(){
        List<String> arestrictions = new ArrayList<String>();
        arestrictions.add("dbowl_not_null");
        arestrictions.add("dbowl_unique");
        for(String restriction : arestrictions){
            DBAttributeRestriction dbattributerestriction = product.getAttributeRestriction(restriction); 
            dbattributerestriction.setAttributeRestrictionName(restriction);
        }    
    
    }
    
    public void buildSchema(Schema schema){
        String fullSchemaName = schema.getFullName();
        DBSchema dbschema = product.getSchema(fullSchemaName);
        dbschema.setSchemaName(fullSchemaName);
    }

    public void buildRelation(Table table){ 
        String fullRelationName = table.getSchema() + ":" + table.getName();
        //get current schema
        DBSchema dbschema = product.getSchema(table.getSchema().getFullName()); 
        DBRelation relation = product.getRelation(fullRelationName); 
        relation.setRelationName(fullRelationName);
        //add relation to the schema model
        dbschema.addRelation(relation); 
        for (final Column column: table.getColumns()){ 
            String c = remNoneAlphaNum(column.getName());
            String fullColumnName = table.getSchema() + ":" + table.getName() + "." + c;
            DBAttribute attribute = product.getAttribute(fullColumnName);
            attribute.setDatatype(column.getColumnDataType().toString()); 
            System.out.println(attribute.getDatatype());
            attribute.setDefaultvalue(column.getDefaultValue()); 
            attribute.setNN(!column.isNullable()); 
            
            //set attribute restrictions
            if(attribute.isNN())
                attribute.addRestriction(product.getAttributeRestriction("dbowl_not_null"));
            
            //set attribute domain
            attribute.setDomain(product.getAttributeDomain(HelperUtils.getAttributeDomain(attribute.getDatatype())));
            
            relation.addAttribute(attribute);
            if (column.isPartOfPrimaryKey()){
                attribute.setPK(true);
                System.out.println("Primary key found: " + fullColumnName);
                String pk = table.getSchema() + ":" + table.getName() + ".primary_key";
                DBPrimaryKey primarykey = product.getPrimaryKey(pk);
                primarykey.setPrimaryKeyName(pk);
                primarykey.addAttribute(attribute);
                relation.setPrimaryKey(primarykey);
            }
            if(column.isPartOfForeignKey()){
                attribute.setFK(true);
                System.out.println("Foreign key found: " + fullColumnName); 
                System.out.println("Referenced Table:   "+column.getReferencedColumn().getParent().getName()+" and the refrence column name is "+ column.getReferencedColumn().getName());
                System.out.println("Referenced Column: " + column.getReferencedColumn().getShortName());
                String referenceColumn = column.getReferencedColumn().getShortName();
                attribute.setReferencedRelationName(column.getReferencedColumn().getParent().getName());
                String fk = table.getSchema() + ":" + table.getName() + ".fk_" + column.getShortName() +".foreign_key";
                String referencePrimaryKey = column.getReferencedColumn().getParent().toString();
                referencePrimaryKey = referencePrimaryKey.replaceAll("\\.", ":");
                referencePrimaryKey = referencePrimaryKey + ".primary_key";
                DBPrimaryKey primarykey = product.getPrimaryKey(referencePrimaryKey);
               
                DBForeignKey foreignkey = product.getForeignKey(fk);
                foreignkey.setAttribute(attribute);
                foreignkey.setReferencedKey(primarykey);
                
                relation.addForeignKeys(foreignkey);
            }
            
            if(column.isNullable()){
                attribute.setNN(false);
                System.out.println("Found nullable attribute: " + fullColumnName);
            }
            
            if(column.isPartOfUniqueIndex()){
                attribute.setUN(true);
                System.out.println("Found unique attribute: " + fullColumnName);
            }
            
            if(attribute.isCandidateKey()){
                DBCandidateKey candidatekey = product.getCandidateKey(fullColumnName + "index.candidate_key");
                candidatekey.setKeyAttribute(attribute);
            }
        }
    }
    
    public void buildView(Table table, String query){
        String fullViewName = table.getSchema() + ":" + table.getName();
        DBView view = product.getView(fullViewName);
        view.setViewName(fullViewName);
        view.setDefinition(query);
        DBSchema dbschema = product.getSchema(table.getSchema().getFullName());
        dbschema.addView(view); 
    }

	public ModelProduct getProduct() {
		return product;
	}
    
}
