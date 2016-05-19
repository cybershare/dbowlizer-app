package com.cybershare.dbowlizer.views;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBAttributeAlias;
import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.dbmodel.DBView;
import com.cybershare.dbowlizer.dbmodel.Element;
import com.cybershare.dbowlizer.generate.ExternalPropertiesManager;
import com.cybershare.dbowlizer.ontology.OWLUtils;

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
	private OWLClass dbQueryConditionClass;
	private OWLClass dbAttributeClass;
	// OWLNamedIndividual
	private OWLNamedIndividual all_columns_individual;
	// OwlDataProperty
	private OWLDataProperty hasAliasName;
	private OWLDataProperty hasOperatorProperty;
	private OWLDataProperty hasValueProperty;
	// ObjectProperty
	private OWLObjectProperty hasPartObjectProperty;
	private OWLObjectProperty hasGroupByAttributeProperty;
	// Conf File

	private OWLOntology extractedOntology;
	private ModelProduct modelProduct;
	private OWLUtils bundle;
	private OWLDataProperty hasAggregateFunctionProperty;


	public DBSchema2Owl(ModelProduct modelProduct,  OWLUtils bundle) {
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");
		this.modelProduct = modelProduct;
		this.bundle = bundle; 
		this.manager = bundle.getManager();
		this.factory = this.manager.getOWLDataFactory();
		this.baseURI = propertiesManager.getString("baseURI") + "/";
		
		this.extractedOntology = bundle.getOntology();

		//OWL Class
		this.dbQueryConditionClass = factory.getOWLClass(IRI.create(baseURI + propertiesManager.getString("dbQueryConditionClass")));
		this.dbQueryJoinClass = factory.getOWLClass(IRI.create(baseURI + propertiesManager.getString("dbQueryJoinClass")));
		this.dbViewClass = factory.getOWLClass(IRI.create(baseURI + propertiesManager.getString("dbViewClass")));
		this.dbQueryClass = factory.getOWLClass(IRI.create(baseURI + propertiesManager.getString("dbQueryClass")));
		this.dbAttributeAliasClass = factory.getOWLClass(IRI.create(baseURI + propertiesManager.getString("dbAttributeAliasClass")));
		this.dbAttributeClass = factory.getOWLClass(IRI.create(baseURI + propertiesManager.getString("dbAttributeClass")));
		//OwlNamedIndividual
		this.all_columns_individual = factory
				.getOWLNamedIndividual(IRI.create(baseURI + propertiesManager.getString("all_columns_individual")));
		//Data Property
		this.hasAliasName = factory.getOWLDataProperty(IRI.create(baseURI + propertiesManager.getString("hasAliasNameProperty")));
		this.hasAggregateFunctionProperty = factory.getOWLDataProperty(IRI.create(baseURI+propertiesManager.getString("hasAggregateProperty")));
		this.hasOperatorProperty = factory.getOWLDataProperty(IRI.create(baseURI + propertiesManager.getString("hasOperatorProperty")));
		this.hasValueProperty = factory.getOWLDataProperty(IRI.create(baseURI + propertiesManager.getString("hasValueProperty")));
		//Object Property
		this.hasPartObjectProperty = factory.getOWLObjectProperty(IRI.create(baseURI + propertiesManager.getString("hasPartProperty")));
		this.hasGroupByAttributeProperty= factory.getOWLObjectProperty(IRI.create(baseURI + propertiesManager.getString("hasGroupByAttributeProperty")));
	}
	
	public void parseViewsAndCreateAxioms()
	{
		List<DBView> views = this.modelProduct.getSchemas().get(0).getViews();
		DbViewParser dbViewParser = new DbViewParser(modelProduct);
		
		List<DbView> orgViews = new ArrayList<DbView>();
		for (DBView view : views)
		{
			
			DbView dbView = dbViewParser.parseView(view.getDefinition(), view.getViewName());
			
			
			orgViews.add(dbView);
			
		}
		
		for (DbView orgView : orgViews)
		{
			createDBView(orgView);
		}
		
		
	}

	// TODO: check the .capitalize methods or whatever that is
	public void createDBView(DbView dbView) {
		
		
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");
		String individualURI = propertiesManager.getString("sourceURI") + modelProduct.getSchemas().get(0).getSchemaName().trim().replace(" ",",")+":";
		
		// Creating the instances of the DB table
		//OWLNamedIndividual view = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName().toUpperCase()));
		OWLNamedIndividual view = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName()));
		//OWLNamedIndividual query = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName().toUpperCase() + "_query"));
		OWLNamedIndividual query = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName()+ "_query"));
		OWLClassAssertionAxiom instanceAxiom = factory.getOWLClassAssertionAxiom(dbViewClass, view);
		OWLClassAssertionAxiom instanceAxiom2 = factory.getOWLClassAssertionAxiom(dbQueryClass, query);
		AddAxiom addAxiom = new AddAxiom(extractedOntology, instanceAxiom);
		manager.applyChange(addAxiom);
		AddAxiom addAxiom2 = new AddAxiom(extractedOntology, instanceAxiom2);
		manager.applyChange(addAxiom2);

		// Adding the sql query part of the view
		OWLObjectPropertyAssertionAxiom hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty,
				view, query);
		AddAxiom attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
		manager.applyChange(attributePartAxiom);

		// Adding the columns alias of the select statement part of the view
		if (!dbView.getColumn_alias().isEmpty()) {
			int aliasID = 1;
			for (DBAttributeAlias currentAlias : dbView.getColumn_alias()) {
				
				// An instance of the attribute alias is created
				//OWLNamedIndividual aliasName = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName().toUpperCase() + "_column_alias_" + aliasID));
				OWLNamedIndividual aliasName = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName() + "_column_alias_" + aliasID));
				OWLClassAssertionAxiom attributeAxiom = factory.getOWLClassAssertionAxiom(dbAttributeAliasClass, aliasName);
				AddAxiom attributeAddAxiom = new AddAxiom(extractedOntology, attributeAxiom);
				manager.applyChange(attributeAddAxiom);

				// The attribute alias is added as part of the view
				hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, query, aliasName);
				attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
				manager.applyChange(attributePartAxiom);

				OWLNamedIndividual columnName;
				// The column that is part of the current Alias is added to the
				// alias
				String identification = currentAlias.getAttribute().getIdentification();
				identification = identification.substring(identification.indexOf(":") + 1);
				if (currentAlias.getAttribute() != null) {
					if (!currentAlias.getAttribute().getColumnName().equals("all"))
						//columnName = factory.getOWLNamedIndividual(IRI.create(individualURI + currentAlias.getIdentification().toUpperCase()));
						columnName = factory.getOWLNamedIndividual(IRI.create(individualURI + identification));
					else
						columnName = all_columns_individual;
				
					
					hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, aliasName, columnName);
					attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
					manager.applyChange(attributePartAxiom);

				}

				// The alias name is added to the current alias through the
				// hasAliasName datatype property
				if (currentAlias.getAttribute() != null) {
				
					String colStr = currentAlias.getAttribute().getIdentification();
					colStr = colStr.substring(colStr.indexOf(":") + 1);
					//OWLDataPropertyAssertionAxiom aliasNameAxiom = factory.getOWLDataPropertyAssertionAxiom(hasAliasName,aliasName, currentAlias.getAttribute().getColumnName().toUpperCase());
					OWLDataPropertyAssertionAxiom aliasNameAxiom = factory.getOWLDataPropertyAssertionAxiom(hasAliasName,aliasName, colStr);
					AddAxiom aliasAxiom = new AddAxiom(extractedOntology, aliasNameAxiom);
					manager.applyChange(aliasAxiom);
				}

				// The aggregator function is added to the alias
				// TODO: Maybe unable to do this one because data structure
				// lacks the alias_agg
				if (currentAlias.getAlias_agg() != null)
				{
					//OWLDataPropertyAssertionAxiom aliasAggAxiom = factory.getOWLDataPropertyAssertionAxiom(hasAggregateFunctionProperty,aliasName,currentAlias.getAlias_agg().toUpperCase());
					OWLDataPropertyAssertionAxiom aliasAggAxiom = factory.getOWLDataPropertyAssertionAxiom(hasAggregateFunctionProperty,aliasName,currentAlias.getAlias_agg());
					AddAxiom aliasAxiom = new AddAxiom(extractedOntology,aliasAggAxiom);
					manager.applyChange(aliasAxiom);
				}
				aliasID += 1;
			}

			// The tables that are part of the query are added
			if (!dbView.getTables().isEmpty())
				for (DBRelation table : dbView.getTables()) {
					//OWLNamedIndividual tableName = factory.getOWLNamedIndividual(IRI.create(individualURI + table.getTableName().toUpperCase()));
					OWLNamedIndividual tableName = factory.getOWLNamedIndividual(IRI.create(individualURI + table.getTableName()));
					
					hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, query, tableName);
					attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
					manager.applyChange(attributePartAxiom);
				}

			OWLObjectSomeValuesFrom hasPartSomeRestrictions;
			// The JOINED tables taht are part of the query are added
			if (dbView.getJoins().size() > 0) {
				int joinID = 1;
				for (DbViewJoin join : dbView.getJoins()) {
					// The Table is added as part of the query
					//OWLNamedIndividual joinedTableName = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName().toUpperCase() + "_join_" + joinID));
					OWLNamedIndividual joinedTableName = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName() + "_join_" + joinID));

					// The join is made an instance of dbQueryJoinClass
					OWLClassAssertionAxiom condAxiom = factory.getOWLClassAssertionAxiom(dbQueryJoinClass, joinedTableName);
					AddAxiom condAddAxiom = new AddAxiom(extractedOntology, condAxiom);
					manager.applyChange(condAddAxiom);

					
					hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, query, joinedTableName);
					attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
					manager.applyChange(attributePartAxiom);

					// The Tables that are part of the join are added

					//OWLNamedIndividual table1Name = factory.getOWLNamedIndividual(IRI.create(individualURI + join.getTable().getTableName().toUpperCase()));
					OWLNamedIndividual table1Name = factory.getOWLNamedIndividual(IRI.create(individualURI + join.getTable().getTableName()));
					

					hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, joinedTableName, table1Name);
					AddAxiom tablePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
					manager.applyChange(tablePartAxiom);

					
					// The columns that are part of the join are added
					
					
					String col1Str = join.getColumn1().getIdentification();
					col1Str = col1Str.substring(col1Str.indexOf(":") + 1);
					
					String col2Str = join.getColumn2().getIdentification();
					col2Str = col2Str.substring(col2Str.indexOf(":") + 1);
					//OWLNamedIndividual col1Name = factory.getOWLNamedIndividual(IRI.create(individualURI + join.getColumn1().getColumnName().toUpperCase()));
					OWLNamedIndividual col1Name = factory.getOWLNamedIndividual(IRI.create(individualURI + col1Str));
					//OWLNamedIndividual col2Name = factory.getOWLNamedIndividual(IRI.create(individualURI + join.getColumn2().getColumnName().toUpperCase()));
					OWLNamedIndividual col2Name = factory.getOWLNamedIndividual(IRI.create(individualURI + col2Str));
					
					
					hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, joinedTableName, col1Name);
					AddAxiom partAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
					manager.applyChange(partAxiom);
					hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, joinedTableName, col2Name);
					partAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
					manager.applyChange(partAxiom);
					joinID += 1;
				}
			} else {
				hasPartSomeRestrictions = factory.getOWLObjectSomeValuesFrom(hasPartObjectProperty, dbQueryJoinClass);
				OWLObjectComplementOf notHasPartSomeRestrictions = factory.getOWLObjectComplementOf(hasPartSomeRestrictions);
				OWLClassAssertionAxiom notAxiom = factory.getOWLClassAssertionAxiom(notHasPartSomeRestrictions, query);
				addAxiom = new AddAxiom(extractedOntology, notAxiom);
				manager.applyChange(addAxiom);
			}

			// The WHERE restrictions/conditions that are part of the query are
			// added
			if (dbView.getRestrictions().size() > 0) {
				int condID = 1;
				OWLNamedIndividual condName;
				for (DbViewRestriction condition : dbView.getRestrictions()) {
					// The condition is added as part of the query
					//condName = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName().toUpperCase() + "_condition_" + condID));
					condName = factory.getOWLNamedIndividual(IRI.create(individualURI + dbView.getName()+ "_condition_" + condID));

					// The condition is made an instance of dbQueryConditionClass
					OWLClassAssertionAxiom condAxiom = factory.getOWLClassAssertionAxiom(dbQueryConditionClass, condName);
					AddAxiom condAddAxiom = new AddAxiom(extractedOntology, condAxiom);
					manager.applyChange(condAddAxiom);
					hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, query, condName);
					attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
					manager.applyChange(attributePartAxiom);

					// The column that is part of the condition are added
					String colStr = condition.getColumn().getIdentification();
					colStr = colStr.substring(colStr.indexOf(":") + 1);
					//OWLNamedIndividual colName = factory.getOWLNamedIndividual(IRI.create(individualURI + condition.getColumn().getColumnName().toUpperCase()));
					OWLNamedIndividual colName = factory.getOWLNamedIndividual(IRI.create(individualURI + colStr));
					hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, condName, colName);
					AddAxiom partAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
					manager.applyChange(partAxiom);

					// The operator that is part of the condition is added
					OWLDataPropertyAssertionAxiom opAxiom = factory.getOWLDataPropertyAssertionAxiom(hasOperatorProperty,
							condName, condition.getOperator());
					addAxiom = new AddAxiom(extractedOntology, opAxiom);
					manager.applyChange(addAxiom);

					// The value that is part of the condition is added
					OWLDataPropertyAssertionAxiom valAxiom = factory.getOWLDataPropertyAssertionAxiom(hasValueProperty, condName,
							condition.getValue());
					addAxiom = new AddAxiom(extractedOntology, valAxiom);
					manager.applyChange(addAxiom);

					condID++;
				}
			} else {
				hasPartSomeRestrictions = factory.getOWLObjectSomeValuesFrom(hasPartObjectProperty, dbQueryConditionClass);
				OWLObjectComplementOf notHasPartSomeRestrictions = factory.getOWLObjectComplementOf(hasPartSomeRestrictions);
				OWLClassAssertionAxiom notAxiom = factory.getOWLClassAssertionAxiom(notHasPartSomeRestrictions, query);
				addAxiom = new AddAxiom(extractedOntology, notAxiom);
				manager.applyChange(addAxiom);

			}

			// The GROUP BY attribute is added
			if (dbView.getGroup_by_columns().size() > 0)
				for (DBAttribute column : dbView.getGroup_by_columns()) {
					String colStr = column.getIdentification();
					colStr = colStr.substring(colStr.indexOf(":") + 1);
					//OWLNamedIndividual colName = factory.getOWLNamedIndividual(IRI.create(individualURI + column.getColumnName().toUpperCase()));
					OWLNamedIndividual colName = factory.getOWLNamedIndividual(IRI.create(individualURI + colStr));
					hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasGroupByAttributeProperty, query, colName);
					attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
					manager.applyChange(attributePartAxiom);
				}
			else{
				hasPartSomeRestrictions = factory.getOWLObjectSomeValuesFrom(hasGroupByAttributeProperty, dbAttributeClass);
				OWLObjectComplementOf notHasPartSomeRestrictions = factory.getOWLObjectComplementOf(hasPartSomeRestrictions);
				OWLClassAssertionAxiom notAxiom = factory.getOWLClassAssertionAxiom(notHasPartSomeRestrictions, query);
				addAxiom = new AddAxiom(extractedOntology, notAxiom);
				manager.applyChange(addAxiom);
			}
			if(dbView.isCopy()){
				//OWLNamedIndividual relationName = factory.getOWLNamedIndividual(IRI.create(individualURI+dbView.getTables().get(0).getTableName().toUpperCase()));
				OWLNamedIndividual relationName = factory.getOWLNamedIndividual(IRI.create(individualURI+dbView.getTables().get(0).getTableName()));
				hasPartAxiom = factory.getOWLObjectPropertyAssertionAxiom(hasPartObjectProperty, query, all_columns_individual);
				attributePartAxiom = new AddAxiom(extractedOntology, hasPartAxiom);
				manager.applyChange(attributePartAxiom);
			}
			else{
				OWLNegativeObjectPropertyAssertionAxiom notHasPartAxiom = factory.getOWLNegativeObjectPropertyAssertionAxiom(hasPartObjectProperty, query, all_columns_individual);
				attributePartAxiom = new AddAxiom(extractedOntology, notHasPartAxiom);
				manager.applyChange(attributePartAxiom);
			}
		}
	}
}
















