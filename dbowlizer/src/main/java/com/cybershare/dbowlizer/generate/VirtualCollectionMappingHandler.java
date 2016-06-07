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

package com.cybershare.dbowlizer.generate;

import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class VirtualCollectionMappingHandler 
{
	private static OWLOntologyManager ontologyManager;
	private static OWLDataFactory factory;
	private static Reasoner mappingReasoner;
	private static String individualURI;
	private static DefaultPrefixManager basePrefix;
	private static OWLOntology db2OWLPrimitiveOntology;
	private static OWLClass virtualCollectionMappingClass;
	private static OWLClass dbQueryClass;
	private static OWLClass dbRelationClass;
	private static OWLClass dbAttributeClass;
	private static OWLClass dbAttributeAliasClass;
	private static OWLObjectProperty hasPartProperty;
	private static OWLDataProperty hasAggregateFunctionProperty;
	private static String countAggregateFunction;
	private static OWLObjectProperty hasGroupByProperty;
	private static OWLClass collectionCls;
	private static OWLObjectProperty hasMemberProperty;
	
	public static void processVirtualCollectionMappings(OWLEntitiesBundle owlEntitiesBundle)
	{
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.properties");

		ontologyManager = owlEntitiesBundle.getOntologyManager();
		factory = ontologyManager.getOWLDataFactory();
		mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		individualURI = owlEntitiesBundle.getIndividualURI();
		basePrefix = owlEntitiesBundle.getBasePrefix();
		db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		String baseURI = owlEntitiesBundle.getBaseURI();
		virtualCollectionMappingClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("virtualCollectionMapping")));
		dbQueryClass = owlEntitiesBundle.getDbQueryClass();
		dbRelationClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("dbRelationClass")));
		dbAttributeClass = owlEntitiesBundle.getDbAttributeClass();
		dbAttributeAliasClass = owlEntitiesBundle.getDbAttributeAliasClass();
		hasPartProperty = owlEntitiesBundle.getHasPartProperty();
		hasAggregateFunctionProperty = factory.getOWLDataProperty(IRI.create(baseURI+propertiesManager.getString("hasAggregateProperty")));
		countAggregateFunction = propertiesManager.getString("count");
		hasGroupByProperty = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("hasGroupByAttributeProperty")));
		collectionCls = factory.getOWLClass(IRI.create(propertiesManager.getString("collectionCls")));
		hasMemberProperty = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("hasMemberProperty")));
				
		
		NodeSet<OWLNamedIndividual> aggregationViewsNodeSet = mappingReasoner.getInstances(virtualCollectionMappingClass,false);
        //#Create the OWL classes corresponding to these individuals and add them into the output ontology.
        NodeSet<OWLNamedIndividual> queryNodeSet = mappingReasoner.getInstances(dbQueryClass,false);

        NodeSet<OWLNamedIndividual> relationNodeSet = mappingReasoner.getInstances(dbRelationClass,false);
        NodeSet<OWLNamedIndividual> attributeNodeSet = mappingReasoner.getInstances(dbAttributeClass,false);
        NodeSet<OWLNamedIndividual> attributeAliasNodeSet = mappingReasoner.getInstances(dbAttributeAliasClass,false);

        if (aggregationViewsNodeSet!=null)
        {
            //#Set of named individuals
            Set<OWLNamedIndividual> aggregationViews = aggregationViewsNodeSet.getFlattened();
            for (OWLNamedIndividual aggregationView : aggregationViews)
            {

                //String viewName = aggregationView.getIRI().toString().replace(individualURI,"");
                //#Get the query name
                NodeSet<OWLNamedIndividual> hasPartNodeSet = mappingReasoner.getObjectPropertyValues(aggregationView,hasPartProperty);
                ArrayList<OWLNamedIndividual> viewQueries = VirtualConceptMappingHandler.returnIntersection(hasPartNodeSet,queryNodeSet);
                
                OWLNamedIndividual query = null;
                if (viewQueries!=null && viewQueries.size()==1)
                {
                	query = viewQueries.get(0);
;
                }
                OWLNamedIndividual relation = null;
                OWLNamedIndividual groupByAttribute = null;
                String aggregateFunctionStr = null;
                if (query!=null)
                {
                    hasPartNodeSet=mappingReasoner.getObjectPropertyValues(query,hasPartProperty);
                    ArrayList<OWLNamedIndividual> queryRelations = VirtualConceptMappingHandler.returnIntersection(hasPartNodeSet,relationNodeSet);

                    
                    if (queryRelations!=null && queryRelations.size()==1)
                    {
                    	relation = queryRelations.get(0);
                    }
                    ArrayList<OWLNamedIndividual> queryAttributeAlias = VirtualConceptMappingHandler.returnIntersection(hasPartNodeSet,attributeAliasNodeSet);
                    
                    for (OWLNamedIndividual attributeAlias : queryAttributeAlias)
                    {
                        Set<OWLLiteral> aggregateFunctionSet = mappingReasoner.getDataPropertyValues(attributeAlias,hasAggregateFunctionProperty);

                        for (OWLLiteral aggregateFunction : aggregateFunctionSet)
                        {
                            if( aggregateFunction.getLiteral().toUpperCase().equals(countAggregateFunction.toUpperCase()))
                            {
                                aggregateFunctionStr = countAggregateFunction;
                                break;
                            }
                        }
                    }
                    NodeSet<OWLNamedIndividual> groupByAttributeNodeSet = mappingReasoner.getObjectPropertyValues(query,hasGroupByProperty);
                    
                    ArrayList<OWLNamedIndividual> groupByAttributeArray = VirtualConceptMappingHandler.returnIntersection(groupByAttributeNodeSet,attributeNodeSet);

                    if (groupByAttributeArray!=null && groupByAttributeArray.size()==1)
                    {
                    	groupByAttribute = groupByAttributeArray.get(0);
                    }
                }

                if (relation!=null && aggregateFunctionStr!=null && groupByAttribute!=null)
                { 
                    OWLClass relationClass = factory.getOWLClass(relation.getIRI().toString().replace(individualURI,""),basePrefix);
                    OWLClass collectionClass = factory.getOWLClass(relation.getIRI().toString().replace(individualURI,"")+"Collection",basePrefix);

                    OWLSubClassOfAxiom subClsAxiom = factory.getOWLSubClassOfAxiom(collectionClass,collectionCls);
                    ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,subClsAxiom));

                    //PROV-O
    				PROVOHandler.owlClassCreated(owlEntitiesBundle, collectionClass, relation.getIRI().toString().replace(individualURI,"")+"Collection", db2OWLPrimitiveOntology);

                    //#existentialRestriction
                    OWLObjectSomeValuesFrom someCardinality = factory.getOWLObjectSomeValuesFrom(hasMemberProperty,relationClass);
                    subClsAxiom = factory.getOWLSubClassOfAxiom(collectionClass,someCardinality);
                    ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,subClsAxiom));

                    OWLClass groupClass = factory.getOWLClass(relation.getIRI().toString().replace(individualURI,"")+"Group",basePrefix);
                    subClsAxiom = factory.getOWLSubClassOfAxiom(groupClass,collectionClass);
                    ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,subClsAxiom));

                    //PROV-O
    				PROVOHandler.owlClassCreated(owlEntitiesBundle, groupClass, relation.getIRI().toString().replace(individualURI,"")+"Group", db2OWLPrimitiveOntology);
                    
                    String propertyName = (groupByAttribute.getIRI().toString()).replace(individualURI,"");
                    OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(propertyName),basePrefix);
                    
                    if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true))
                    {
//                        individualsMapper.populateCollectionWithDataPropertyFilter(viewName,relationClass,aggregateFunction,propertyName, collectionClass,groupClass,hasPartProperty,hasMemberProperty,hasCountProperty,possibleDataProperty,basePrefix)
                    }
                    else
                    {

                    }
                }
            }
        }
        try {
			ontologyManager.saveOntology(db2OWLPrimitiveOntology);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

    private static String getPropertyName(String objectName)
    {
    	return "has"+objectName;
    }

}
