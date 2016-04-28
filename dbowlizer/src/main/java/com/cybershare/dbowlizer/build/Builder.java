/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cybershare.dbowlizer.build;


import com.cybershare.dbowlizer.db2rdf.R2rmlMapping;
import com.cybershare.dbowlizer.db2rdf.XSDs;//Remove Eric was testing

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
import java.util.ArrayList;
import java.util.List;
import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
public class Builder {
    
    private ModelProduct product;
    
    public Builder(ModelProduct product){
        this.product = product;
    }
    
    private void reset(){
        // attribute = null;
    }
    
    public void buildAttributeDomains(){
        List<String> adomains = new ArrayList<String>();
        adomains.add("dbowl:db_date");
        adomains.add("dbowl:db_double");
        adomains.add("dbowl:db_float");
        adomains.add("dbowl:db_integer");
        adomains.add("dbowl:db_string");
        for(String domain : adomains){
            DBAttributeDomain dbattributedomain = product.getAttributeDomain(domain); 
            dbattributedomain.setAttributeDomainName(domain);
        }
    }
    
    public void buildAttributeRestrictions(){
        List<String> arestrictions = new ArrayList<String>();
        arestrictions.add("dbowl:db_not_null");
        arestrictions.add("dbowl:db_unique");
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
        DBSchema dbschema = product.getSchema(table.getSchema().getFullName()); //get current schema
        DBRelation relation = product.getRelation(fullRelationName); //Eric C did this change to test. fullRelationName was change to table.getName()
        relation.setRelationName(fullRelationName);
        dbschema.addRelation(relation); //add relation to the schema model
        for (final Column column: table.getColumns()){ 
            String c = remNoneAlphaNum(column.getName());
            String fullColumnName = table.getSchema() + ":" + table.getName() + "." + c;
            DBAttribute attribute = product.getAttribute(fullColumnName);
            attribute.setDatatype(column.getColumnDataType().toString()); //datatype
            System.out.println(attribute.getDatatype());
            attribute.setDefaultvalue(column.getDefaultValue()); //default attribute value
            attribute.setNN(!column.isNullable()); //is not null?
            //set attribute restrictions
            if(attribute.isNN())
                attribute.addRestriction(product.getAttributeRestriction("dbowl:db_not_null"));
            
            //set attribute domain
            
            
            
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
               // System.out.println();
                System.out.println("Refrencing table name:   "+column.getReferencedColumn().getParent().getName()+" and the refrence column name is "+ column.getReferencedColumn().getName());//Remove this, Eric is testing
               //System.out.println();
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
        DBSchema dbschema = product.getSchema(table.getSchema().getFullName()); //get current schema
        dbschema.addView(view); //add view to the schema model
    }
    
    public void assemble(){
        this.reset();
    }

	public ModelProduct getProduct() {
		return product;
	}
    
    
}
