package com.cybershare.dbowlizer.db2rdf;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashSet;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.semanticweb.owlapi.model.OWLClass;

import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.generate.OutputOntologyGenerator;
import com.cybershare.dbowlizer.dbmodel.DBRelation;

import eu.optique.api.mapping.LogicalTable;
import eu.optique.api.mapping.MappingFactory;
import eu.optique.api.mapping.ObjectMap;
import eu.optique.api.mapping.PredicateMap;
import eu.optique.api.mapping.PredicateObjectMap;
import eu.optique.api.mapping.R2RMLMappingManager;
//import eu.optique.api.mapping.R2RMLMappingManagerFactory;
import eu.optique.api.mapping.SubjectMap;
import eu.optique.api.mapping.TermMap.TermMapType;
import eu.optique.api.mapping.TriplesMap;
import eu.optique.api.mapping.impl.jena.JenaR2RMLMappingManagerFactory;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;

public class R2rmlMapping{ 
	private final String dl = "http://semanticscience.org/resource/dl";
	private final String ex= "http://ontology.cybershare.utep.edu/resource/"; //I gather from resourceURI but don't know if that is the ex:
	private final String xsd= "http://www.w3.org/2001/XMLSchema#";
	private final String rr= "http://www.w3.org/ns/r2rml#";
	private int triplesCount=1;
	private R2RMLMappingManager mm;
	private MappingFactory mf;
	private SubjectMap sm;
	private PredicateMap pm;
	private ObjectMap om;
	
	public R2rmlMapping(){	
		
	mm= new JenaR2RMLMappingManagerFactory().getR2RMLMappingManager();
	mf= mm.getMappingFactory();
	sm=mf.createSubjectMap(mf.createTemplate());
	pm= mf.createPredicateMap(TermMapType.CONSTANT_VALUED,"");
	om=mf.createObjectMap(TermMapType.COLUMN_VALUED, "");	
	}
	
	
	
	
	
	public void startMapping(){
		
		
	}
	
	public void generateOutSource(Table table){
		LogicalTable lt = mf.createSQLBaseTableOrView(table.getName());
		SubjectMap sm = mf.createSubjectMap(mf.createTemplate());
		TriplesMap trip = mf.createTriplesMap(lt, sm);
		
		
		
		
	}
	
	public void generateManager(Table table){
		LogicalTable lt = mf.createSQLBaseTableOrView(table.getName());
		SubjectMap sm = mf.createSubjectMap(mf.createTemplate());
		TriplesMap trip = mf.createTriplesMap(lt, sm);
		System.out.println("The table type is  --> "+table.getTableType().toString());
		
		for(final Column column: table.getColumns()){
			
			if(column.isPartOfForeignKey()){
				PredicateObjectMap pom;
				if(column.isPartOfPrimaryKey()){
					sm.setTemplate(mf.createTemplate(dl+"/"+column.getParent().getName()+"/{"+column.getName()+"}"));
					sm.addClass(ResourceFactory.createResource("http://semanticscience.org/resource/dl:Manager"));
					PredicateMap predicate = mf.createPredicateMap(TermMapType.CONSTANT_VALUED, dl+":has"+capitalizeFirstLetter(column.getParent().getName())+"."+column.getName().toLowerCase());
					ObjectMap object = mf.createObjectMap(mf.createTemplate(dl+":"+capitalizeFirstLetter(column.getReferencedColumn().getParent().getName())+"{"+column.getReferencedColumn().getName()+"}"));
					pom = mf.createPredicateObjectMap(predicate,object);
				}
				else{
					PredicateMap predicate = mf.createPredicateMap(TermMapType.CONSTANT_VALUED, dl+":has"+capitalizeFirstLetter(column.getParent().getName())+"."+column.getName().toLowerCase());
					ObjectMap object = mf.createObjectMap(mf.createTemplate(dl+":"+capitalizeFirstLetter(column.getReferencedColumn().getParent().getName())+"{"+column.getReferencedColumn().getName()+"}"));
					pom = mf.createPredicateObjectMap(predicate,object);		
				}
				
				trip.addPredicateObjectMap(pom);

			}
			else 	
				trip.addPredicateObjectMap(pomDataProperties(column));
		}
		try {
			this.saveTriple(trip);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
//<#TriplesMap3>
//rr:logicalTable [ rr:tableName "EMP2DEPT" ];
//rr:subjectMap [ rr:template "http://data.example.com/employee={EMPNO}/department={DEPTNO}" ];
//rr:predicateObjectMap [
 //   rr:predicate ex:employee;
 //   rr:objectMap [ rr:template "http://data.example.com/employee/{EMPNO}" ];
//];
//rr:predicateObjectMap [
//    rr:predicate ex:department;
//    rr:objectMap [ rr:template "http://data.example.com/department/{DEPTNO}" ];
//].
public TriplesMap generateM2M(OWLClass class1, Column column1, OWLClass class2, Column column2){
	String str = class1.getIRI().toString();
	LogicalTable lt = mf.createSQLBaseTableOrView(column1.getParent().getName());
	SubjectMap sm = mf.createSubjectMap(mf.createTemplate());
	
	sm.setTemplate(mf.createTemplate(dl+column1.getParent().getName()+"={"+column1.getName()+"}/"+column2.getParent().getName()+"={"+column2.getName()));
	TriplesMap trip = mf.createTriplesMap(lt, sm);
	
	PredicateMap predicate = mf.createPredicateMap(TermMapType.CONSTANT_VALUED,dl+"has"+capitalizeFirstLetter(column1.getParent().getName())+"."+column1.getName());
	ObjectMap object = mf.createObjectMap(mf.createTemplate(dl+column1.getParent().getName()+"/{"+column1+"}"));
	PredicateObjectMap pom = mf.createPredicateObjectMap(predicate,object);
	
	PredicateMap predicate2 = mf.createPredicateMap(TermMapType.CONSTANT_VALUED,dl+"has"+capitalizeFirstLetter(column1.getParent().getName())+"."+column1.getName());
	ObjectMap object2 = mf.createObjectMap(mf.createTemplate(dl+column1.getParent().getName()+"/{"+column1+"}"));
	PredicateObjectMap pom2 = mf.createPredicateObjectMap(predicate2,object2);
	
	trip.addPredicateObjectMap(pom);
	trip.addPredicateObjectMap(pom2);
	
	return trip;
}

public TriplesMap generateClassDataTypeProperties(OWLClass class1, DBRelation relation){
	LogicalTable lt=mf.createSQLBaseTableOrView(relation.getRelationName());
	SubjectMap sm=mf.createSubjectMap(mf.createTemplate());
	
	sm.setTemplate(mf.createTemplate(class1.getIRI().toString()+"={"+relation.getPrimaryKey().getPrimaryKeyName()+"}"));
	//for(final DBAttribute dbattribute: relation.getAttributes()){	}
	TriplesMap trip= mf.createTriplesMap(lt,sm); 
return trip; 	
}


public TriplesMap generateM2M(Table table){
	LogicalTable lt = mf.createSQLBaseTableOrView(table.getName());
	
	SubjectMap sm = mf.createSubjectMap(mf.createTemplate());
	String smTemplate=dl+"/";
	
	for(final Column column: table.getColumns()){
		smTemplate=smTemplate+table.getName()+"={"+column.getName()+"}/";
	}
	
	sm.setTemplate(mf.createTemplate(removeLastChar(smTemplate)));
	
	TriplesMap trip = mf.createTriplesMap(lt, sm);
	trip.setResource(ResourceFactory.createResource(dl+capitalizeFirstLetter(table.getName())));
	
	for(final Column column: table.getColumns()){
	PredicateMap predicate = mf.createPredicateMap(TermMapType.CONSTANT_VALUED,dl+":has"+capitalizeFirstLetter(column.getParent().getName())+"."+column.getName());
	ObjectMap object = mf.createObjectMap(mf.createTemplate(dl+column.getParent().getName()+"/{"+column.getName()+"}"));
	PredicateObjectMap pom = mf.createPredicateObjectMap(predicate,object);
	trip.addPredicateObjectMap(pom);
	}
	
	return trip;
}


public  TriplesMap generateSingleTable(Table table){
	 LogicalTable lt = mf.createSQLBaseTableOrView(table.getName());
	 
	 SubjectMap sm = mf.createSubjectMap(mf.createTemplate());
	 TriplesMap trip= null;
	 
	 for(final Column column: table.getColumns()){
		 if(column.isPartOfPrimaryKey()){
			sm.setTemplate(mf.createTemplate(dl+":"+table.getName()+"/{"+column.getName()+"}"));
			sm.addClass(ResourceFactory.createResource(dl+":"+capitalizeFirstLetter(table.getName())));
			trip = mf.createTriplesMap(lt,sm);
		 }
		 else { 
			trip.addPredicateObjectMap(pomDataProperties(column));
		
		 	}
		 }
		
	 trip.setResource(ResourceFactory.createResource(dl+":"+capitalizeFirstLetter(table.getName())));
	 return trip;
}

public  PredicateObjectMap pomDataProperties(Column column){
	
	PredicateMap predicate = mf.createPredicateMap(TermMapType.CONSTANT_VALUED,dl+":"+"has"+capitalizeFirstLetter(column.getParent().getName())+"."+column.getName().toLowerCase());
	ObjectMap object = mf.createObjectMap(TermMapType.COLUMN_VALUED,column.getName());
	try{
		XSDs xsds = new XSDs();
		object.setDatatype(ResourceFactory.createResource(xsds.getXSDStrings(column.getColumnDataType().toString())));
		}
	catch(Exception e){
		System.out.println(e);
	}
	PredicateObjectMap pom = mf.createPredicateObjectMap(predicate,object);
	return pom;
}



public void saveTriple(TriplesMap trip) throws Exception{
	Collection<TriplesMap> coll = new HashSet<TriplesMap>();
	coll.add(trip);
	 
	Model out = mm.exportMappings(coll,Model.class);
	  
	FileOutputStream fos =new FileOutputStream (new File("../../Mappings/Generated/TriplesMap"+triplesCount+".ttl"));
	out.write(fos, "TURTLE" );	
	triplesCount++;
}



	public String capitalizeFirstLetter(String original) {
		if (original == null || original.length() == 0) {
			return original;
		}
		return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
	
	 private String removeLastChar(String str) {
	        return str.substring(0,str.length()-1);
	    }
}
