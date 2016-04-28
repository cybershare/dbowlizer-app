package com.cybershare.dbowlizer.ontology;

import com.cybershare.dbowlizer.axioms.DBAttributeAliasAxioms;
import com.cybershare.dbowlizer.axioms.DBAttributeAxioms;
import com.cybershare.dbowlizer.axioms.DBAttributeDomainAxioms;
import com.cybershare.dbowlizer.axioms.DBAttributeRestrictionAxioms;
import com.cybershare.dbowlizer.axioms.DBCandidateKeyAxioms;
import com.cybershare.dbowlizer.axioms.DBForeignKeyAxioms;
import com.cybershare.dbowlizer.axioms.DBPrimaryKeyAxioms;
import com.cybershare.dbowlizer.axioms.DBQueryAxioms;
import com.cybershare.dbowlizer.axioms.DBQueryConditionAxioms;
import com.cybershare.dbowlizer.axioms.DBQueryJoinAxioms;
import com.cybershare.dbowlizer.axioms.DBRelationAxioms;
import com.cybershare.dbowlizer.axioms.DBSchemaAxioms;
import com.cybershare.dbowlizer.axioms.DBViewAxioms;
import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBAttributeAlias;
import com.cybershare.dbowlizer.dbmodel.DBAttributeDomain;
import com.cybershare.dbowlizer.dbmodel.DBAttributeRestriction;
import com.cybershare.dbowlizer.dbmodel.DBCandidateKey;
import com.cybershare.dbowlizer.dbmodel.DBForeignKey;
import com.cybershare.dbowlizer.dbmodel.DBPrimaryKey;
import com.cybershare.dbowlizer.dbmodel.DBQuery;
import com.cybershare.dbowlizer.dbmodel.DBQueryCondition;
import com.cybershare.dbowlizer.dbmodel.DBQueryJoin;
import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.dbmodel.DBSchema;
import com.cybershare.dbowlizer.dbmodel.DBView;
import com.cybershare.dbowlizer.dbmodel.Visitor;
import org.semanticweb.owlapi.model.OWLIndividual;



public class OWLVisitor implements Visitor{

   private OWLUtils bundle;
	
   public OWLVisitor(OWLUtils bundle){
	this.bundle = bundle;
   }
        
    public void visit(DBSchema dbschema) {
        OWLIndividual ind = Individuals.getIndividual(dbschema, bundle);
	DBSchemaAxioms axioms = new DBSchemaAxioms(ind, dbschema, bundle);
	axioms.setAxioms();
	bundle.addAxioms(axioms);
    } 

    public void visit(DBRelation dbrelation) {
        OWLIndividual ind = Individuals.getIndividual(dbrelation, bundle);
	DBRelationAxioms axioms = new DBRelationAxioms(ind, dbrelation, bundle);
	axioms.setAxioms();
	bundle.addAxioms(axioms);
    }

    public void visit(DBAttribute dbattribute) {
        OWLIndividual ind = Individuals.getIndividual(dbattribute, bundle);
        DBAttributeAxioms axioms = new DBAttributeAxioms(ind, dbattribute, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms);
    }
    
    public void visit(DBView dbview){
        OWLIndividual ind = Individuals.getIndividual(dbview, bundle);
        DBViewAxioms axioms = new DBViewAxioms(ind, dbview, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms);   
    }
    
    public void visit(DBAttributeAlias dbattributealias){
        OWLIndividual ind = Individuals.getIndividual(dbattributealias, bundle);
        DBAttributeAliasAxioms axioms = new DBAttributeAliasAxioms(ind, dbattributealias, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms);     
    }
    
    public void visit(DBQuery dbquery){
        OWLIndividual ind = Individuals.getIndividual(dbquery, bundle);
        DBQueryAxioms axioms = new DBQueryAxioms(ind, dbquery, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms);     
    }
    
    public void visit(DBQueryCondition dbquerycondition){
        OWLIndividual ind = Individuals.getIndividual(dbquerycondition, bundle);
        DBQueryConditionAxioms axioms = new DBQueryConditionAxioms(ind, dbquerycondition, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms);     
    }
    
    public void visit(DBQueryJoin dbqueryjoin){
        OWLIndividual ind = Individuals.getIndividual(dbqueryjoin, bundle);
        DBQueryJoinAxioms axioms = new DBQueryJoinAxioms(ind, dbqueryjoin, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms); 
    }
    
    public void visit(DBCandidateKey dbcandidatekey){
        OWLIndividual ind = Individuals.getIndividual(dbcandidatekey, bundle);
        DBCandidateKeyAxioms axioms = new DBCandidateKeyAxioms(ind, dbcandidatekey, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms);  
    }  
    
    public void visit(DBPrimaryKey dbprimarykey){
        OWLIndividual ind = Individuals.getIndividual(dbprimarykey, bundle);
        DBPrimaryKeyAxioms axioms = new DBPrimaryKeyAxioms(ind, dbprimarykey, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms);  
    }  
    
    public void visit(DBForeignKey dbforeignkey){
        OWLIndividual ind = Individuals.getIndividual(dbforeignkey, bundle);
        DBForeignKeyAxioms axioms = new DBForeignKeyAxioms(ind, dbforeignkey, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms);    
    }
    
    public void visit(DBAttributeDomain dbattributedomain){
        OWLIndividual ind = Individuals.getIndividual(dbattributedomain, bundle);
        DBAttributeDomainAxioms axioms = new DBAttributeDomainAxioms(ind, dbattributedomain, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms); 
    }
    
    
    public void visit(DBAttributeRestriction dbattributerestriction){
        OWLIndividual ind = Individuals.getIndividual(dbattributerestriction, bundle);
        DBAttributeRestrictionAxioms axioms = new DBAttributeRestrictionAxioms(ind, dbattributerestriction, bundle);
        axioms.setAxioms();
        bundle.addAxioms(axioms);    
    }

}
