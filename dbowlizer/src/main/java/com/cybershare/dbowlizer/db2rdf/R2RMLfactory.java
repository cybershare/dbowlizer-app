package com.cybershare.dbowlizer.db2rdf;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import org.apache.jena.rdf.model.Model;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.generate.MappedEntitiesBundle;
import com.cybershare.dbowlizer.utils.Settings;

import eu.optique.api.mapping.R2RMLMappingManager;
import eu.optique.api.mapping.TriplesMap;
import eu.optique.api.mapping.impl.jena.JenaR2RMLMappingManagerFactory;

public class R2RMLfactory {
    
    private ModelProduct product;
    private MappedEntitiesBundle generator;
    private ArrayList<TriplesNode> tripsArray= new ArrayList<TriplesNode>();
    private R2RMLMappingManager mappingManager= new JenaR2RMLMappingManagerFactory().getR2RMLMappingManager();
    private String schemaName;
    private Settings settings;
    
    public R2RMLfactory(ModelProduct product, MappedEntitiesBundle mappedEntitiesBundle, Settings settings){
        this.product=product;
        this.generator=mappedEntitiesBundle;
        this.settings = settings;
        this.schemaName=product.getSchemas().get(0).getSchemaName()+":";
        
    }
    
    public void startProduction(){
        owlClassGenerator();
        owlDataPropertyGenerator();
        owlObjectProperties();
        saveAllTriplesMap();
    }
    
    
    public void owlClassGenerator(){
        HashMap<OWLClass, String> owlClassToRelationNameMap =generator.getOwlClassToRelationNameMap();
        for(final OWLClass owlClass : owlClassToRelationNameMap.keySet()){
            String dbRelationName = owlClassToRelationNameMap.get(owlClass);
            DBRelation dbrelation= product.getRelation(schemaName+dbRelationName);
            owlClassTriplesMap(owlClass, dbrelation);
        }
    }
    
    public void owlDataPropertyGenerator(){
        R2RMLTemplate templates= new R2RMLTemplate();
        TriplesMap tripTemp;
        HashMap<OWLDataProperty, String> owlDataPropertyToRelation =generator.getOwlDataPropertyToAttributeName();
        for(final OWLDataProperty owldataProperty : owlDataPropertyToRelation.keySet()){
            String dbRelationName = getJustTheNameOFTable(owlDataPropertyToRelation.get(owldataProperty)); //The dbRelationName would be in upperCase
            DBRelation dbrelation= product.getRelation(schemaName+dbRelationName);
            for(int i=0;i<tripsArray.size();i++){
                TriplesMap trip=tripsArray.get(i).getTriplesMap();
                if(dbrelation.getIdentification().equalsIgnoreCase(schemaName+tripsArray.get(i).getNameOfTriplesMap()))
                    for(final DBAttribute attribute: dbrelation.getAttributes()){
                        if(attribute.isPK()&&(!attribute.isFK())&&(dbrelation.hasOnePK())){
                        }
                        else if(attribute.getColumnName().equalsIgnoreCase(getJustTheCoulmnName(owlDataPropertyToRelation.get(owldataProperty)))){
                            tripTemp=templates.addDataPropertiesToTriplesMap(trip,owldataProperty,attribute);
                            tripsArray.get(i).setTriplesMap(tripTemp);
                        }
                    }
            }
        }
    }
    
    public void owlClassTriplesMap(OWLClass owlclass, DBRelation dbTable){
        OWLClassTemplate classTemplates= new OWLClassTemplate();
        TriplesMap trip=null;
        if(dbTable.hasOnePKThatIsFK()){
            trip=classTemplates.owlClassTemplateForNoOwnPK(owlclass,dbTable);
        }
        if(dbTable.hasMoreThanOnePK()){
            trip=classTemplates.OwlClassMulPKTemplate(owlclass, dbTable);
        }
        else if(dbTable.hasOwnPK()){
            trip=classTemplates.OwlClassOwnPKTemplate(owlclass, dbTable);
        }
        tripsArray.add(new TriplesNode(trip, dbTable.getTableName()));
    }
    
    //TODO need to do the correct TriplesMap for WorksFor and subproject
    public void owlObjectProperties(){
        ObjectPropertyTemplate opt=new ObjectPropertyTemplate(product,generator.getOwlClassToRelationNameMap());
        TriplesMap tripTemp;
        HashMap<OWLObjectProperty, String> owlObjectProperties =generator.getOwlObjectPropertyToAttributeName();
        for(final OWLObjectProperty objectProperty: owlObjectProperties.keySet()){
            String dbRelationName = getJustTheNameOFTable(owlObjectProperties.get(objectProperty));
            DBRelation dbrelation= product.getRelation(schemaName+dbRelationName);
            if(dbrelation.areAllAttributesPK()&&dbrelation.getAttributes().size()==2){
                tripTemp=opt.twoRelationTemplate(objectProperty, owlObjectProperties.get(objectProperty));
                tripsArray.add(new TriplesNode(tripTemp,owlObjectProperties.get(objectProperty)));
            }
            else{
                for(int i=0;i<tripsArray.size();i++){
                    TriplesMap trip=tripsArray.get(i).getTriplesMap();
                    if(dbrelation.getTableName().equalsIgnoreCase(tripsArray.get(i).getNameOfTriplesMap())){
                        tripTemp=opt.addObjectPredicate(trip, objectProperty, owlObjectProperties.get(objectProperty)); //the DBAttribute is put as TableName.COlumnName
                        tripsArray.get(i).setTriplesMap(tripTemp);
                    }
                    
                }
            }
        }
    }
    
    public void saveAllTriplesMap(){
        for(int i=0;i<tripsArray.size();i++){
            try {
                saveTriplesMapInFile(tripsArray.get(i).getTriplesMap(),tripsArray.get(i).getNameOfTriplesMap());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public void saveTriplesMapInFile(TriplesMap trip, String nameOfTriplesMap) throws Exception{
        Collection<TriplesMap> coll = new HashSet<TriplesMap>();
        coll.add(trip);
        Model out = mappingManager.exportMappings(coll,Model.class);
        FileOutputStream fos =new FileOutputStream (new File(settings.getOutputDir() + "TriplesMap_"+nameOfTriplesMap+".ttl"));
        settings.addMappingName("TriplesMap_"+nameOfTriplesMap+".ttl");
        out.write(fos, "TURTLE" );
    }
    
    
    /*
    * Input would be the TableName.AttributeName
    * Return just the AttributeName
    */
    public String getJustTheCoulmnName(String fullTableName){
        String[] splitidentification= fullTableName.split("\\.");
        return splitidentification[1];
    }
    
    /*
    * Input would be the TableName.AttributeName
    * Return just the TableName
    */
    public String getJustTheNameOFTable(String fullTableName){
        String[] splitidentification= fullTableName.split("\\.");
        return splitidentification[0];
    }
    
}	