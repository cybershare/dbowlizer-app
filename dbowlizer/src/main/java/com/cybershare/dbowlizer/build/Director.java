/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.cybershare.dbowlizer.build;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cybershare.dbowlizer.utils.DriverSelector;

import schemacrawler.schema.Database;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.InformationSchemaViews;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;

/**
 *
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
public class Director {
    
    private Builder builder;
    
    public Director(Builder builder){
        this.builder = builder;
    }
    
    
    /* Will start Database object from schemacrawler and send it
    to the buildSchema Model */
    
    public void construct(Connection dbSource, DriverSelector selector){
        // Create the options
        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        //Hashmap to store Key=tableName, Value=Query for our views
        HashMap<String, String> queries = new HashMap<String, String>();
        // Set what details are required in the schema - this affects the
        // time taken to crawl the schema
        options.setSchemaInfoLevel(SchemaInfoLevel.detailed());

        options.setRoutineInclusionRule(new ExcludeAll());

        // for postgres and mysql use lower case
        options.setSchemaInclusionRule(new RegularExpressionInclusionRule(selector.getSchema()));

        //Bundled Options
        InformationSchemaViews schemaViews = selector.mapSchemaInformation();
        //VIEW_DEFINITION FROM INFORMATION_SCHEMA.VIEWS ORDER BY TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME");

        options.setInformationSchemaViews(schemaViews);

        try {
            // Get the schema definition
            final Database database = SchemaCrawlerUtility.getDatabase(dbSource,options);
            Statement stmt          = null;
            ResultSet rs            = null;
            try {
                stmt = dbSource.createStatement();
                rs   = stmt.executeQuery(schemaViews.getViewsSql());
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                //Go through our Result set to get tName/query
                while (rs.next()) {
                    String key   = null;
                    String value = null;
                    for (int i = 1; i <= columnsNumber; i++) {
                        if(rsmd.getColumnName(i).equals("TABLE_NAME"))
                            key   = rs.getString(i);
                        if(rsmd.getColumnName(i).equals("VIEW_DEFINITION"))
                            value = rs.getString(i);
                    }
                    queries.put(key, value);
                }
            }
            catch (SQLException ex){
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: "     + ex.getSQLState());
                System.out.println("VendorError: "  + ex.getErrorCode());
            }
            finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException sqlEx) {  }
                    rs = null;
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException sqlEx) {  }
                    stmt = null;
                }
            }
            buildSchemaModel(database, queries);
        } catch (SchemaCrawlerException ex) {
            Logger.getLogger(Director.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* build the schema datasets */
    private void buildSchemaModel(Database database, HashMap<String, String> queries){
        builder.buildAttributeDomains();
        builder.buildAttributeRestrictions();
        for (final Schema schema: database.getSchemas()){
            System.out.println();
            System.out.println("S--> " + schema);
            builder.buildSchema(schema);
            for (final Table table: database.getTables(schema)){
                if (!(table instanceof View)){
                    System.out.println();
                    System.out.println("T--> " + table);
                    builder.buildRelation(table);
                }
            }
            // View Tables
            for (final Table table: database.getTables(schema)){
                if (table instanceof View){
                    table.getName();
                    System.out.println("V--> " + table);
                    String query = queries.get(table.getName());
                    System.out.println("Q--> " + query);
                    builder.buildView(table, query);
                }
            }
        }
    }
    
    public Builder getBuilder() {
        return builder;
    }
    
}
