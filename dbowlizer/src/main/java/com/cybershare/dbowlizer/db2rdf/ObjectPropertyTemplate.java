package com.cybershare.dbowlizer.db2rdf;

import java.util.HashMap;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBRelation;

import eu.optique.api.mapping.LogicalTable;
import eu.optique.api.mapping.MappingFactory;
import eu.optique.api.mapping.ObjectMap;
import eu.optique.api.mapping.PredicateMap;
import eu.optique.api.mapping.PredicateObjectMap;
import eu.optique.api.mapping.R2RMLMappingManager;
import eu.optique.api.mapping.SubjectMap;
import eu.optique.api.mapping.TriplesMap;
import eu.optique.api.mapping.TermMap.TermMapType;
import eu.optique.api.mapping.impl.jena.JenaR2RMLMappingManagerFactory;

public class ObjectPropertyTemplate{
    private ModelProduct product;
    private R2RMLMappingManager mappingManager= new JenaR2RMLMappingManagerFactory().getR2RMLMappingManager();
    private MappingFactory mappingFactory= mappingManager.getMappingFactory();
    private HashMap<OWLClass, String> owlClassToRelationNameMap;
    private String schemaName;
    
    
    public ObjectPropertyTemplate(ModelProduct product,HashMap<OWLClass,String> owlClassToRelationNameMap ){
        this.product=product;
        this.owlClassToRelationNameMap=owlClassToRelationNameMap;
        this.schemaName=product.getSchemas().get(0).getSchemaName()+":";
    }
    
    
    public TriplesMap addObjectPredicate(TriplesMap trip, OWLObjectProperty objProperty,String dbRelationNAttribute){
        TriplesMap tripTemp=trip;
        DBRelation table =product.getRelation(schemaName+tableName(dbRelationNAttribute));
        String referenceTableName=getReferenceTableName(table, columnName(dbRelationNAttribute));
        String foreignBaseIRI= getForeignOWLClassIRI(referenceTableName);
        PredicateObjectMap pom= pomObjectProperty(objProperty.getIRI().toString(),foreignBaseIRI,columnName(dbRelationNAttribute));
        tripTemp.addPredicateObjectMap(pom);
        return tripTemp;
    }
    
    public PredicateObjectMap pomObjectProperty(String objPropertyIRI, String foreignBaseIRI, String attributeName){
        PredicateMap predicate = mappingFactory.createPredicateMap(TermMapType.CONSTANT_VALUED,objPropertyIRI);
        ObjectMap object = mappingFactory.createObjectMap(TermMapType.CONSTANT_VALUED,foreignBaseIRI+"/{"+attributeName+"}");
        PredicateObjectMap pom = mappingFactory.createPredicateObjectMap(predicate,object);
        return pom;
    }
    public String getReferenceTableName(DBRelation table, String columnName){
        for(DBAttribute attribute: table.getAttributes()){
            if(attribute.getColumnName().equalsIgnoreCase(columnName)){
                return attribute.getReferencedRelationName();
            }
        }
        return "";
        
    }
    
    public TriplesMap twoRelationTemplate(OWLObjectProperty objProperty, String dbAttribute){
        DBRelation table =product.getRelation(schemaName+tableName(dbAttribute));
        LogicalTable lt= mappingFactory.createSQLBaseTableOrView(table.getTableName());
        SubjectMap sm = mappingFactory.createSubjectMap(mappingFactory.createTemplate());
        //TODO comment this out once they send the owlclasses in capitalize.
        sm.setTemplate(mappingFactory.createTemplate(getBaseIRI(objProperty)+getOppositeColumn(table, columnName(dbAttribute))));
        TriplesMap trip = mappingFactory.createTriplesMap(lt,sm);
        TriplesMap tripTemp= addObjectPredicate(trip, objProperty,dbAttribute);
        //System.out.println("twoRelationTemplte "+ getBaseIRI(objProperty)+getOppositeColumn(table, columnName(dbAttribute)));
        return tripTemp;
    }
    
    
    public String getOppositeColumn(DBRelation dbrelation, String attribute){
        String oppositeColumnIRI=null;
        for(DBAttribute atribute: dbrelation.getAttributes()){
            if(!(atribute.getColumnName().equalsIgnoreCase(attribute))){
                //TODO comment this out once they send the owlclasses in capitalize.
                oppositeColumnIRI="/"+atribute.getReferencedRelationName()+"/{"+atribute.getColumnName()+"}";
                //oppositeColumnIRI=":"+capitalizeFirstLetter(atribute.getReferencedRelationName())+"/{"+atribute.getColumnName()+"}";
            }
        }
        return oppositeColumnIRI;
    }
    
    
    
    public String getForeignOWLClassIRI(String referenceTableName){
        for(final OWLClass owlClass : owlClassToRelationNameMap.keySet()){
            String dbRelationName = owlClassToRelationNameMap.get(owlClass);
            if(dbRelationName.equalsIgnoreCase(referenceTableName)){
                return owlClass.getIRI().toString();
            }
        }
        return "";
    }
    public String columnName(String dbAttribute){
        String[] base= dbAttribute.split("\\.");
        return base[base.length-1];
    }
    
    public String tableName(String dbAttribute){
        String[] base= dbAttribute.split("\\.");
        return base[0];
    }
    
    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
    
    public String getBaseIRI(OWLObjectProperty objectProperty){
        String[] base= objectProperty.getIRI().toString().split("\\/");
        String baseIRI="";
        for(int i=0;i<base.length-1;i++){
            baseIRI=baseIRI+"/"+base[i];
        }
        return baseIRI.substring(1);
    }
}