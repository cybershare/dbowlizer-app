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

package com.cybershare.dbowlizer.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.DatabaseConnectionOptions;
import schemacrawler.schemacrawler.InformationSchemaViews;
import schemacrawler.schemacrawler.SchemaCrawlerException;


public class DriverSelector {
    
    private String _dbms_name = null;
    private DataSource _datasource = null;
    
    private final String _mysql_driver = "com.mysql.jdbc.Driver";
    //private final String _oracle_thin_driver = "org.postgresql.Driver";
    private final String _postgre_driver = "oracle.jdbc.driver.OracleDriver"; 
    
    private final String _mysql_properties = "/schemacrawler-mysql.config.properties";
    private final String _oracle_properties = "/schemacrawler-oracle.config.properties";
    private final String _postgre_properties = "/schemacrawler-postgresql.config.properties";
    
    private String _host = "localhost";
    private String _port;
    private String _schema;
    private String _sid = "xe";
    private String _username;
    private String _password;

    public DriverSelector(String dbms_name) {
        _dbms_name = dbms_name;
    }

    public void setDataSource(String host, String port, String schema, String sid){
        this._host = host;
        this._port = port;
        this._schema = schema;
        if (sid != null)
                _sid = sid;

        if (_dbms_name.equalsIgnoreCase("mysql"))
            mysqlDataSource();
        else if (_dbms_name.equalsIgnoreCase("oracle")){}
            //oracleDataSource();
        else if (_dbms_name.equalsIgnoreCase("postgre"))
            postgreDataSource();
    }
    
    public Connection getConnection (String username, String password){
        try {
            this._username = username;
            this._password = password;
            Connection connection = _datasource.getConnection(_username, _password);
            return connection;
        } catch (SQLException ex) {
            Logger.getLogger(DriverSelector.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public InformationSchemaViews mapSchemaInformation(){
        InformationSchemaViews informationSchemaViews = new InformationSchemaViews();
        String propertiesFile = null;
        if (_dbms_name.equalsIgnoreCase("mysql"))
            propertiesFile = _mysql_properties;
        else if (_dbms_name.equalsIgnoreCase("oracle"))
            propertiesFile = _oracle_properties;
        else if (_dbms_name.equalsIgnoreCase("postgre"))
            propertiesFile = _postgre_properties;
        
        //Select configuration file
        Config resource = Config.loadResource(propertiesFile);
       
        //Set sql values from the Config object
        //informationSchemaViews.setRoutinesSql(resource.getStringValue("select.INFORMATION_SCHEMA.ROUTINES", null));
        //informationSchemaViews.setTableConstraintsSql(resource.getStringValue("select.INFORMATION_SCHEMA.TABLE_CONSTRAINTS", null));
        //informationSchemaViews.setTriggersSql(resource.getStringValue("select.INFORMATION_SCHEMA.TRIGGERS", null));
        informationSchemaViews.setViewsSql(resource.getStringValue("select.INFORMATION_SCHEMA.VIEWS", null));
 
        return informationSchemaViews;
    }
    
    /* Data source Setters */
    
    private void mysqlDataSource(){
        try {
            _datasource = new DatabaseConnectionOptions(_mysql_driver, "jdbc:mysql://"+_host+":"+_port+"/"+_schema);
        } catch (SchemaCrawlerException ex) {
            Logger.getLogger(DriverSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void postgreDataSource(){
        try {
            _datasource = new DatabaseConnectionOptions(_postgre_driver, "jdbc:postgresql://"+_host+":"+_port+"/"+_schema);
        } catch (SchemaCrawlerException ex) {
            Logger.getLogger(DriverSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Oracle Maven dependencies no longer available.
    /*
    private void oracleDataSource(){
        try {
            _schema = _schema.toUpperCase();
            _datasource = new DatabaseConnectionOptions(_oracle_thin_driver, "jdbc:oracle:thin:"+_schema+"@"+_host+":"+_port+"/"+_sid);
        } catch (SchemaCrawlerException ex) {
            Logger.getLogger(DriverSelector.class.getName()).log(Level.SEVERE, null, ex);
        } 
    } */
   
    
    /* Getters and Setters */
    
    public String getDbms_name() {
        return _dbms_name;
    }

    public void setDbms_name(String dbms_name) {
        this._dbms_name = dbms_name;
    }

    public String getSchema() {
        return _schema;
    }
       
}
