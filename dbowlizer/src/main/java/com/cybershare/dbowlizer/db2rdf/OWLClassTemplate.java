package com.cybershare.dbowlizer.db2rdf;

import java.util.HashMap;

import org.apache.jena.rdf.model.ResourceFactory;
import org.semanticweb.owlapi.model.OWLClass;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBRelation;

import eu.optique.api.mapping.LogicalTable;
import eu.optique.api.mapping.MappingFactory;
import eu.optique.api.mapping.R2RMLMappingManager;
import eu.optique.api.mapping.SubjectMap;
import eu.optique.api.mapping.TriplesMap;
import eu.optique.api.mapping.impl.jena.JenaR2RMLMappingManagerFactory;


public class OWLClassTemplate {
private HashMap<OWLClass, String> owlClassToRelationNameMap;
private R2RMLMappingManager mappingManager= new JenaR2RMLMappingManagerFactory().getR2RMLMappingManager();
private MappingFactory mappingFactory= mappingManager.getMappingFactory();


public OWLClassTemplate(){
}

public OWLClassTemplate(HashMap<OWLClass, String> owlClassToRelationNameMap){
	this.owlClassToRelationNameMap=owlClassToRelationNameMap;
}

public TriplesMap OwlClassMulPKTemplate(OWLClass owlclass, DBRelation dbrelation){
	LogicalTable lt= mappingFactory.createSQLBaseTableOrView(dbrelation.getTableName());
	SubjectMap sm = mappingFactory.createSubjectMap(mappingFactory.createTemplate());
	sm.setTemplate(mappingFactory.createTemplate(owlclass.getIRI().toString()+"/"+subjectTemplateString(dbrelation)));
	sm.addClass(ResourceFactory.createResource(owlclass.getIRI().toString()));
	TriplesMap trip = mappingFactory.createTriplesMap(lt,sm);
	trip.setResource(ResourceFactory.createResource(owlclass.getIRI().toString()));
	return trip;
}

public TriplesMap OwlClassOwnPKTemplate(OWLClass owlclass, DBRelation dbrelation){
	LogicalTable lt= mappingFactory.createSQLBaseTableOrView(dbrelation.getTableName());
	TriplesMap trip = null;
	SubjectMap sm = mappingFactory.createSubjectMap(mappingFactory.createTemplate());
	for(final DBAttribute column: dbrelation.getAttributes()){
		if(column.isPK()){
			sm.setTemplate(mappingFactory.createTemplate(owlclass.getIRI().toString()+"/{"+column.getColumnName()+"}"));
			sm.addClass(ResourceFactory.createResource(owlclass.getIRI().toString()));
			trip = mappingFactory.createTriplesMap(lt,sm);
			trip.setResource(ResourceFactory.createResource(owlclass.getIRI().toString()));
			return trip;
			}
	}
	System.out.println("The table didnt have the appropriate Primary Key");
	return trip;
}

public TriplesMap owlClassTemplateForNoOwnPK(OWLClass owlclass, DBRelation dbrelation){
	LogicalTable lt= mappingFactory.createSQLBaseTableOrView(dbrelation.getTableName());
	SubjectMap sm = mappingFactory.createSubjectMap(mappingFactory.createTemplate(owlclass.getIRI().toString()));
	sm.addClass(ResourceFactory.createResource(owlclass.getIRI().toString()));
	for(final DBAttribute column: dbrelation.getAttributes()){
		if(column.isPK()){
			sm.setTemplate(mappingFactory.createTemplate(getBaseIRI(owlclass)+"/"+foreignIRIClassName(dbrelation)+"/{"+column.getColumnName()+"}"));
			}
		}
	TriplesMap trip = mappingFactory.createTriplesMap(lt,sm);
	trip.setResource(ResourceFactory.createResource(owlclass.getIRI().toString()));
	//trip.setResource(ResourceFactory.createResource(getBaseIRI(owlclass)+"/"+
		//	Character.toUpperCase(dbrelation.getTableName().charAt(0)) + dbrelation.getTableName().substring(1)));
	return trip;
}

public String foreignIRIClassName(DBRelation dbRelaiton){
	for(DBAttribute attribute: dbRelaiton.getAttributes()){
		if(attribute.isPK()){
			return capitalizeFirstLetter(attribute.getReferencedRelationName());
		}
	}
	return "Error";
	
}


public String capitalizeFirstLetter(String original) {
	if (original == null || original.length() == 0) {
		return original;
	}
	return original.substring(0, 1).toUpperCase() + original.substring(1);
}

public String subjectTemplateString(DBRelation dbrelation){
	String template="";
	for(DBAttribute dbAttribute: dbrelation.getAttributes()){
		if(dbAttribute.isPK()){
			template=template+"_{"+dbAttribute.getColumnName()+"}";
		}
	}
	return template.substring(1);
}

	public String getBaseIRI(OWLClass owlClass){
		String[] base= owlClass.getIRI().toString().split("\\/");
		String baseIRI="";
		for(int i=0;i<base.length-1;i++){
			baseIRI=baseIRI+"/"+base[i];
		}
		return baseIRI.substring(1);
	}

}
