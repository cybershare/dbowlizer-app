package com.cybershare.dbowlizer.views;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.cybershare.dbowlizer.dbmodel.DBAttributeAlias;
import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.dbmodel.DBSchema;
import com.cybershare.dbowlizer.dbmodel.Element;
import com.github.andrewoma.dexx.collection.HashMap;

public class DBSchema2Owl {
  private OWLOntologyManager manager;
  private OWLOntology ontology;
  private OWLDataFactory factory;
  private DbView dbView;
  private String baseURI;
  // OWLClass
  private OWLClass dbViewClass;
  private OWLClass dbQueryClass;
  private OWLClass dbAttributeAliasClass;
  private OWLClass dbQueryJoinClass;
  // OWLNamedIndividual
  private OWLNamedIndividual all_columns_individual;
  // OwlDataProperty
  private OWLDataProperty hasAliasName;
  private HashMap<String, String> owlConfig;
  private OWLOntology extractedOntology;
  private OWLObjectProperty hasPartObjectProperty;
  private IRI ontologyURI;

  public DBSchema2Owl(HashMap<String, String> owlConfig, DBSchema dbSchema) throws OWLOntologyCreationException {
    this.manager = OWLManager.createOWLOntologyManager();
    this.factory = this.manager.getOWLDataFactory();
    this.baseURI = owlConfig.get("baseURI");
    this.dbViewClass = factory.getOWLClass(IRI.create(baseURI + owlConfig.get("dbViewClass")));
    this.dbQueryClass = factory.getOWLClass(IRI.create(baseURI + owlConfig.get("dbQueryClass")));
    this.dbAttributeAliasClass = factory.getOWLClass(IRI.create(baseURI + owlConfig.get("dbAttributeAliasClass")));
    // TODO: check:
    // @ontologyURI =
    // IRI.create(@owlConfig['sourceURI']+dbSchema.name+"-relational-model-individuals.owl")
    this.ontologyURI = IRI
        .create(owlConfig.get("sourceURI") + dbSchema.getSchemaName() + "-relational-model-individuals.owl");
    this.extractedOntology = manager.createOntology(ontologyURI);
    this.hasPartObjectProperty = factory.getOWLObjectProperty(IRI.create(baseURI + owlConfig.get("hasPartProperty")));
    this.all_columns_individual = factory
        .getOWLNamedIndividual(IRI.create(baseURI + owlConfig.get("all_columns_individual")));
    this.hasAliasName = factory.getOWLDataProperty(IRI.create(baseURI + owlConfig.get("hasAliasNameProperty")));
    this.dbQueryJoinClass = factory.getOWLClass(IRI.create(baseURI + owlConfig.get("dbQueryJoinClass")));

  }

  // TODO: check the .capitalize methods or whatever that is
  public void createDBView(DbView dbView, String individualURI) {
    //Creating the instances of the DB table
    OWLNamedIndividual view  = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName()));
    OWLNamedIndividual query = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName() + "_query"));
    OWLClassAssertionAxiom instanceAxiom = factory.getOWLClassAssertionAxiom(dbViewClass, view);
    OWLClassAssertionAxiom instanceAxiom2 = factory.getOWLClassAssertionAxiom(dbQueryClass, query);
    AddAxiom addAxiom = new AddAxiom(extractedOntology, instanceAxiom);
    manager.applyChange(addAxiom);
    AddAxiom addAxiom2 = new AddAxiom(extractedOntology, instanceAxiom2);
    manager.applyChange(addAxiom2);

    //Adding the sql query part of the view
    OWLObjectPropertyAssertionAxiom hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, view, query);
    AddAxiom attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
    manager.applyChange(attributePartAxiom);
    
    //Adding the columns alias of the select statement part of the view
    if (!dbView.getColumn_alias().isEmpty()){
      int aliasID = 1;
      for(Element currentAlias1: dbView.getColumn_alias()){
        DBAttributeAlias currentAlias = (DBAttributeAlias) currentAlias1;
        //An instance of the attribute alias is created
        OWLNamedIndividual aliasName = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName() + "_column_alias_" + aliasID));
        OWLClassAssertionAxiom attributeAxiom = factory.getOWLClassAssertionAxiom(dbAttributeAliasClass, aliasName);
        AddAxiom attributeAddAxiom = new AddAxiom(extractedOntology, attributeAxiom);
        manager.applyChange(attributeAddAxiom);

        //The attribute alias is added as part of the view
        hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, query, aliasName);
        attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
        manager.applyChange(attributePartAxiom);
        
        OWLNamedIndividual columnName;
        //The column that is part of the current Alias is added to the alias
        if (currentAlias.getAttribute() != null){
          if (!currentAlias.getAttribute().getColumnName().equals("all"))
            columnName = factory.getOWLNamedIndividual(IRI.create(individualURI + currentAlias.getIdentification()));
          else
            columnName = all_columns_individual;
          hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, aliasName, columnName);
          attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
          manager.applyChange(attributePartAxiom);
          
        }
        
        //The alias name is added to the current alias through the hasAliasName datatype property
        if (currentAlias.getAttribute() != null){
          OWLDataPropertyAssertionAxiom aliasNameAxiom = factory.getOWLDataPropertyAssertionAxiom(hasAliasName, aliasName, currentAlias.getAttribute().getColumnName());
          AddAxiom aliasAxiom = new AddAxiom(extractedOntology, aliasNameAxiom);
          manager.applyChange(aliasAxiom);
        }
        
        //The aggregator function is added to the alias
        //TODO: Maybe unable to do this one because data structure lacks the alias_agg
        //if (currentAlias.getAttribute().getAlias_agg()
        aliasID +=1;
      }
      //The tables that are part of the query are added
      if (!dbView.getTables().isEmpty())
        for (DBRelation table :dbView.getTables()){
          OWLNamedIndividual tableName = factory.getOWLNamedIndividual(IRI.create(individualURI + table.getTableName()));
          hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, query, tableName);
          attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
          manager.applyChange(attributePartAxiom);
        }
      
      OWLObjectSomeValuesFrom hasPartSomeRestrictions;
      //The JOINED tables taht are part of the query are added
      if (dbView.getJoins().size() > 0){
        int joinID = 1;
        for (DbViewJoin join: dbView.getJoins()){
          //The Table is added as part of the query
          OWLNamedIndividual joinedTableName = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName() + "_join_" + joinID));
          
          //The join is made an instance of dbQueryJoinClass
          OWLClassAssertionAxiom condAxiom = factory.getOWLClassAssertionAxiom(dbQueryJoinClass, joinedTableName);
          AddAxiom condAddAxiom = new AddAxiom(extractedOntology, condAxiom);
          manager.applyChange(condAddAxiom);
          
          hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, query, joinedTableName);
          attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
          manager.applyChange(attributePartAxiom);
          
          //The Tables that are part of the join are added
          
          OWLNamedIndividual table1Name = factory.getOWLNamedIndividual(IRI.create(individualURI + join.getTable().getTableName()));
          hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, joinedTableName, table1Name);
          AddAxiom tablePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
          manager.applyChange(tablePartAxiom);
          
          //The columns that are part of the join are added
          OWLNamedIndividual col1Name = factory.getOWLNamedIndividual(IRI.create(individualURI + join.getColumn1().getColumnName()));
          OWLNamedIndividual col2Name = factory.getOWLNamedIndividual(IRI.create(individualURI + join.getColumn2().getColumnName()));
          hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, joinedTableName, col1Name);
          AddAxiom partAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
          hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, joinedTableName, col2Name);
          partAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
          manager.applyChange(partAxiom);
          joinID += 1;
        }
      }
      else {
        hasPartSomeRestrictions = factory.getOWLObjectSomeValuesFrom(hasPartObjectProperty, dbQueryJoinClass);
        OWLObjectComplementOf notHasPartSomeRestrictions = factory.getOWLObjectComplementOf(hasPartSomeRestrictions);
        OWLClassAssertionAxiom notAxiom = factory.getOWLClassAssertionAxiom(notHasPartSomeRestrictions, query);
        addAxiom = new AddAxiom(extractedOntology, notAxiom);
        manager.applyChange(addAxiom);
    }
      
      //The WHERE restrictions/conditions that are part of the query are added
      if (dbView.getRestrictions().size() > 0);
  }


  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
}
