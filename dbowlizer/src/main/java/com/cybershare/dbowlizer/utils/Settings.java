
package com.cybershare.dbowlizer.utils;

/**
 *
 * @author Luis
 */

import java.util.ArrayList;
import java.util.List;
import schemacrawler.schemacrawler.Config;


public class Settings {
   private final Config resource; 
   private final String propertiesFile;
   
   //Database settings
   private String driver;
   private String host;
   private String port;
   private String dbname;
   private String user;
   private String password;
   private String uuid;
   
   //Ontology settings
   private String ontologyFile;
   private String baseIRI;
   private String importsOntology;
   
   //Output file settings
   private String outputDir;
   private String outputDirFile;

   //Server settings
   private String outputURL;
   
   //Output File Names
   private ArrayList<String> ontologies;
   private ArrayList<String> mappings;
   
    
   public Settings(String propertiesFile){
      this.ontologies = new ArrayList<String>(); 
      this.mappings = new ArrayList<String>();
       
      this.propertiesFile = propertiesFile;   
      resource = Config.loadResource(propertiesFile);   
      //Ontology settings
      this.baseIRI = resource.getStringValue("baseIRI", null);
      this.importsOntology = resource.getStringValue("importsOntology", null);
      //Database settings
      this.driver = resource.getStringValue("driver", "mysql");
      this.host = resource.getStringValue("host", null);
      this.port = resource.getStringValue("port", null);
      this.dbname = resource.getStringValue("dbname", null);
      this.user = resource.getStringValue("user", null);
      this.password = resource.getStringValue("password", null);
      //Output file settings
      this.outputDir= resource.getStringValue("outputDir", null);
      this.outputDirFile= resource.getStringValue("outputDirFile", null);
      this.ontologyFile = resource.getStringValue("OntologyFile", null);
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getOntologyFile() {
        return ontologyFile;
    }

    public void setOntologyFile(String ontologyFile) {
        this.ontologyFile = ontologyFile;
    }

    public String getOutputURL() {
        return outputURL;
    }

    public void setOutputURL(String outputURL) {
        this.outputURL = outputURL;
    }
    
    public String getBaseIRI() {
        return baseIRI;
    }
    
    public void setBaseIRI(String baseIRI) {
        this.baseIRI = baseIRI;
    }
    
    public String getImportsOntology() {
        return importsOntology;
    }
    
    public void setImportsOntology(String importsOntology) {
        this.importsOntology = importsOntology;
    }

    public String getOutputDirFile() {
        return outputDirFile;
    }

    public void setOutputDirFile(String outputDirFile) {
        this.outputDirFile = outputDirFile;
    }
    
    //get output names (ontologies and mappings)
    
    public List<String> getOntologyNames() {
        return ontologies;
    }

    public void addOntologyName(String ontologyName){
        ontologies.add(ontologyName);
    }
    
    public List<String> getMappingNames() {
        return mappings;
    }

    public void addMappingName(String mappingName){
        ontologies.add(mappingName);
    }
    
}
