package com.cybershare.dbowlizer.generate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLFacet;

import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;

public class ViewsWithJoinsMappingHandler 
{
	private static OWLOntologyManager ontologyManager;
	private static OWLDataFactory factory;
	private static Reasoner mappingReasoner;
	private static String individualURI;
	private static DefaultPrefixManager basePrefix;
	private static OWLOntology db2OWLPrimitiveOntology;
	private static OWLClass dbAttributeClass;
	private static OWLClass dbAttributeAliasClass;
	private static OWLClass dbQueryConditionClass;
	private static OWLClass dbQueryClass;
	private static OWLObjectProperty hasPartProperty;
	private static OWLOntology db2OWLComplexOntology;
	private static OWLClass dbJoinViewClass;
	private static OWLClass dbJoinProjectionViewClass;
	private static OWLClass dbJoinSelectionViewClass;
	private static OWLDataProperty hasOperatorDP;
	private static OWLDataProperty hasValueDP;
	private static OWLObjectProperty hasAttributeDomainObjectProperty;
	private static OWLNamedIndividual db_string_individual;
	private static IRI xsd_string_IRI;
	private static OWLNamedIndividual db_integer_individual;
	private static OWLNamedIndividual db_float_individual;
	private static OWLNamedIndividual db_date_individual;
	private static IRI xsd_date_IRI;
	private static OWLObjectProperty subClassOfMapping;
	private static OWLNamedIndividual db_double_individual;
	private static OWLClass dbQueryJoinClass;
	private static OWLObjectProperty hasRangeMappingOP;
	private static OWLObjectProperty hasDomainMappingOP;
	private static OWLObjectProperty hasObjectPropertyMappingOP;
	private static OWLObjectProperty hasSubClassOfRangeMappingOP;
	private static OWLObjectProperty hasSubClassOfDomainMappingOP;
	private static IRI xsd_minInclusive_IRI;
	private static IRI xsd_maxInclusive_IRI;
	private static IRI xsd_pattern_IRI;
	
	public static void processViewsWithJoins(OWLEntitiesBundle owlEntitiesBundle)
	{
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.properties");
		ontologyManager = owlEntitiesBundle.getOntologyManager();
		factory = ontologyManager.getOWLDataFactory();
		mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		individualURI = owlEntitiesBundle.getIndividualURI();
		basePrefix = owlEntitiesBundle.getBasePrefix();
		db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		dbAttributeClass = owlEntitiesBundle.getDbAttributeClass();
		dbAttributeAliasClass = owlEntitiesBundle.getDbAttributeAliasClass();
		dbQueryConditionClass = owlEntitiesBundle.getDbQueryConditionClass();
		dbQueryClass = owlEntitiesBundle.getDbQueryClass();
		hasPartProperty = owlEntitiesBundle.getHasPartProperty();
		db2OWLComplexOntology = owlEntitiesBundle.getDb2OWLComplexOntology();
		String baseURI = owlEntitiesBundle.getBaseURI();
		dbJoinViewClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("dbJoinViewClass")));
		dbJoinProjectionViewClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("dbJoinProjectionViewClass")));
		dbJoinSelectionViewClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("dbJoinSelectionViewClass")));
		hasOperatorDP = owlEntitiesBundle.getHasOperator();
		hasValueDP = owlEntitiesBundle.getHasValue();
		hasAttributeDomainObjectProperty = owlEntitiesBundle.getHasAttributeDomainObjectProperty();	
		db_string_individual = owlEntitiesBundle.getDb_string_individual();
		xsd_string_IRI = owlEntitiesBundle.getXsd_string_IRI();
		db_integer_individual = owlEntitiesBundle.getDb_integer_individual();
		db_float_individual = owlEntitiesBundle.getDb_float_individual();
		db_date_individual = owlEntitiesBundle.getDb_date_individual();
		xsd_date_IRI = owlEntitiesBundle.getXsd_date_IRI();
		subClassOfMapping = owlEntitiesBundle.getSubClassOfMapping();
		db_double_individual = owlEntitiesBundle.getDb_double_individual();
		dbQueryJoinClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("dbQueryJoinClass")));
		hasRangeMappingOP = owlEntitiesBundle.getHasRangeMapping();
		hasDomainMappingOP = owlEntitiesBundle.getHasDomainMapping();
		hasObjectPropertyMappingOP = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("hasObjectPropertyMapping")));
		hasSubClassOfRangeMappingOP = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("hasSubClassOfRangeMapping")));
		hasSubClassOfDomainMappingOP = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("hasSubClassOfDomainMapping")));
		xsd_minInclusive_IRI = owlEntitiesBundle.getXsd_minInclusive_IRI();
		xsd_maxInclusive_IRI = owlEntitiesBundle.getXsd_maxInclusive_IRI();
		xsd_pattern_IRI = owlEntitiesBundle.getXsd_pattern_IRI();
		
		
		OWLClass dbJoinProjectionSelectionViewClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("dbJoinProjectionSelectionViewClass")));;
				
		
		NodeSet<OWLNamedIndividual> joinViewsNodeSet = mappingReasoner.getInstances(dbJoinViewClass,false);
        
		Set<OWLNamedIndividual> joinViews = new HashSet<OWLNamedIndividual>();
		if (joinViewsNodeSet != null)
			joinViews=joinViewsNodeSet.getFlattened(); 
        

		//##Processing join views, only the join information is sent.

        NodeSet<OWLNamedIndividual> queryNodeSet = mappingReasoner.getInstances(dbQueryClass,false);
        for (OWLNamedIndividual joinView : joinViews)
        {

            NodeSet<OWLNamedIndividual> hasPartQueryNodeSet = mappingReasoner.getObjectPropertyValues(joinView,hasPartProperty);
            ArrayList<OWLNamedIndividual> hasPartQueries = VirtualConceptMappingHandler.returnIntersection(hasPartQueryNodeSet,queryNodeSet);
            if (hasPartQueries.size()==1)
            {

                processJoinsForView(joinView,hasPartQueries.get(0),null,null);
            }
        }
        
        //##Processing join projection views, information about join and aliases is sent.

        NodeSet<OWLNamedIndividual> joinProjectionViewsNodeSet = mappingReasoner.getInstances(dbJoinProjectionViewClass,false);
        Set<OWLNamedIndividual> joinProjectionViews = new HashSet<OWLNamedIndividual>();
		if (joinProjectionViewsNodeSet != null)
        	joinProjectionViews =joinProjectionViewsNodeSet.getFlattened();
		
		for (OWLNamedIndividual joinProjectionView : joinProjectionViews)
		{
            NodeSet<OWLNamedIndividual> hasPartQueryNodeSet = mappingReasoner.getObjectPropertyValues(joinProjectionView,hasPartProperty);
            ArrayList<OWLNamedIndividual> hasPartQueries = VirtualConceptMappingHandler.returnIntersection(hasPartQueryNodeSet,queryNodeSet);
            OWLNamedIndividual joinQuery = null;
            if (hasPartQueries.size()==1)
            {
                joinQuery = hasPartQueries.get(0);
            }
            
            if (joinQuery != null)
            {
            		
                //#Processing the projection restrictions for the view.
                HashMap<OWLNamedIndividual, ArrayList<OWLClassExpression>> aliasesRestrictions = processProjectionsForJoins(joinQuery);
                //#Processing the joins for the view including the projection restrictions.
                processJoinsForView(joinProjectionView,joinQuery,aliasesRestrictions, null);
            }
		}

        //##Processing join selection views new!

        NodeSet<OWLNamedIndividual> joinSelectionViewsNodeSet = mappingReasoner.getInstances(dbJoinSelectionViewClass,false);
        Set<OWLNamedIndividual> joinSelectionViews = new HashSet<OWLNamedIndividual>();
		if (joinSelectionViewsNodeSet != null)
        	joinSelectionViews =joinSelectionViewsNodeSet.getFlattened();
		
		for (OWLNamedIndividual joinSelectionView : joinSelectionViews)
		{
            NodeSet<OWLNamedIndividual> hasPartQueryNodeSet = mappingReasoner.getObjectPropertyValues(joinSelectionView,hasPartProperty);
            ArrayList<OWLNamedIndividual> hasPartQueries = VirtualConceptMappingHandler.returnIntersection(hasPartQueryNodeSet,queryNodeSet);
            OWLNamedIndividual joinQuery = null;
            if (hasPartQueries.size()==1)
            {
            	joinQuery = hasPartQueries.get(0);
            }
            if (joinQuery != null)
            {
                //#Processing the conditions for the view.
            	HashMap<OWLNamedIndividual, OWLClass> conditionRestrictions = processConditionsForJoins(joinQuery);

                processJoinsForView(joinSelectionView,joinQuery,null,conditionRestrictions);
          	}
		}
		  
        //##Processing join projection selection views
        NodeSet<OWLNamedIndividual> joinProjectionSelectionViewsNodeSet = mappingReasoner.getInstances(dbJoinProjectionSelectionViewClass,false);
        
        Set<OWLNamedIndividual> joinProjectionSelectionViews = new HashSet<OWLNamedIndividual>();
        if (joinProjectionSelectionViewsNodeSet!=null)
        {
        	joinProjectionSelectionViews = joinProjectionSelectionViewsNodeSet.getFlattened();
        }
        
        for (OWLNamedIndividual joinProjectionSelectionView : joinProjectionSelectionViews)
        {
            NodeSet<OWLNamedIndividual> hasPartQueryNodeSet = mappingReasoner.getObjectPropertyValues(joinProjectionSelectionView,hasPartProperty);
            ArrayList<OWLNamedIndividual> hasPartQueries = VirtualConceptMappingHandler.returnIntersection(hasPartQueryNodeSet,queryNodeSet);
            OWLNamedIndividual joinQuery = null;
            if (hasPartQueries.size()==1)
            {
                joinQuery = hasPartQueries.get(0);
            }
            if (joinQuery!=null)
            {
                //#Processing the projection restrictions for the view.
                HashMap<OWLNamedIndividual, ArrayList<OWLClassExpression>> aliasesRestrictions = processProjectionsForJoins(joinQuery);
                //#Processing the conditions for the view.
                HashMap<OWLNamedIndividual, OWLClass> conditionRestrictions = processConditionsForJoins(joinQuery);
                //#Processing the joins for the view including the projection restrictions.
                processJoinsForView(joinProjectionSelectionView,joinQuery,aliasesRestrictions,conditionRestrictions);
            }
        }
		
		
	}

	private static void processJoinsForView(OWLNamedIndividual view, OWLNamedIndividual query,	HashMap<OWLNamedIndividual, ArrayList<OWLClassExpression>> projectionRestrictions, HashMap<OWLNamedIndividual, OWLClass> conditionRestrictions) {

        NodeSet<OWLNamedIndividual> hasPartJoinNodeSet = mappingReasoner.getObjectPropertyValues(query,hasPartProperty);
        NodeSet<OWLNamedIndividual> attributeNodeSet = mappingReasoner.getInstances(dbAttributeClass,false);
        NodeSet<OWLNamedIndividual> joinsNodeSet = mappingReasoner.getInstances(dbQueryJoinClass,false);
        ArrayList<OWLNamedIndividual> joins = VirtualConceptMappingHandler.returnIntersection(hasPartJoinNodeSet,joinsNodeSet);

        OWLClass domainClass = null;
        OWLClass rangeClass = null;

        HashMap<Integer, HashMap<String, OWLClass>> joinsWithDomainAndRange = new HashMap<Integer, HashMap<String, OWLClass>>();

        if (joins != null)
        {
            int joinCounter=0;
            for (OWLNamedIndividual join : joins)
            {
            	
            	HashMap<String, OWLClass> joinDomainAndRange = new HashMap<String, OWLClass>();
                joinCounter+=1;
                //#View class domain correspond now to join class domain.
                OWLClass joinClassDomain = factory.getOWLClass((view.getIRI().toString().replace(individualURI,""))+"_join_"+joinCounter+"_domain",basePrefix);
                
               // #If the view does not have a projection or a condition restriction, it creates directly the domain and range classes and it's asserted
               // #equivalent to the domain class (arbitrarily).
                
                if (projectionRestrictions==null && conditionRestrictions==null && joinCounter==1)
                {
                	
                    OWLClass viewClassWithoutProjection = factory.getOWLClass(view.getIRI().toString().replace(individualURI,""),basePrefix);
                    OWLEquivalentClassesAxiom equivalentAxiom = factory.getOWLEquivalentClassesAxiom(viewClassWithoutProjection,joinClassDomain);
                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,equivalentAxiom));
                    try {
						ontologyManager.saveOntology(db2OWLComplexOntology);
					} catch (OWLOntologyStorageException e) {

						e.printStackTrace();
					}
                }
 
                //#Processing domain and range mapping.
                OWLClass joinClassRange = factory.getOWLClass((view.getIRI().toString().replace(individualURI,""))+"_join_"+joinCounter+"_range",basePrefix);
                joinDomainAndRange.put("joinClassDomain",joinClassDomain);
                joinDomainAndRange.put("joinClassRange",joinClassRange);

                NodeSet<OWLNamedIndividual> hasRangeMappingNodeSet = mappingReasoner.getObjectPropertyValues(join,hasRangeMappingOP);
                //System.out.println("Has Range Mapping Size: " + hasRangeMappingNodeSet.getFlattened().size());
                OWLNamedIndividual[] hasRangeMapping = null;
                if (hasRangeMappingNodeSet!=null)
                {
                	Set<OWLNamedIndividual> hasRangeMappingSet = hasRangeMappingNodeSet.getFlattened();
                	hasRangeMapping = hasRangeMappingSet.toArray(new OWLNamedIndividual[hasRangeMappingSet.size()]);
                }
                
                NodeSet<OWLNamedIndividual> hasDomainMappingNodeSet = mappingReasoner.getObjectPropertyValues(join,hasDomainMappingOP);
                //System.out.println("hasDomainMappingNodeSet Size: " + hasDomainMappingNodeSet.getFlattened().size());
                OWLNamedIndividual[] hasDomainMapping = null;
                if (hasDomainMappingNodeSet != null)
                {
                	Set<OWLNamedIndividual> hasDomainMappingSet = hasDomainMappingNodeSet.getFlattened();
                	hasDomainMapping = hasDomainMappingSet.toArray(new OWLNamedIndividual[hasDomainMappingSet.size()]);
                }
                
                
                NodeSet<OWLNamedIndividual> hasPropertyMappingNodeSet = mappingReasoner.getObjectPropertyValues(join,hasObjectPropertyMappingOP);
                //System.out.println("hasPropertyMappingNodeSet Size: " + hasPropertyMappingNodeSet.getFlattened().size());
                OWLNamedIndividual[] hasPropertyMapping = null;
                if (hasPropertyMappingNodeSet != null)
                {
                	Set<OWLNamedIndividual> hasPropertyMappingSet = hasDomainMappingNodeSet.getFlattened();
                	hasPropertyMapping = hasPropertyMappingSet.toArray(new OWLNamedIndividual[hasPropertyMappingSet.size()]);
                }
                
                NodeSet<OWLNamedIndividual> hasSubClassOfRangeMappingNodeSet = mappingReasoner.getObjectPropertyValues(join,hasSubClassOfRangeMappingOP);
                //System.out.println("hasSubClassOfRangeMappingNodeSet Size: " + hasSubClassOfRangeMappingNodeSet.getFlattened().size());
                OWLNamedIndividualImpl[] hasSubClassOfRangeMapping = null;
                if  (hasSubClassOfRangeMappingNodeSet != null)
                {
                	Set<OWLNamedIndividual> hasSubClassOfRangeMappingSet = hasSubClassOfRangeMappingNodeSet.getFlattened();
                	hasSubClassOfRangeMapping = hasSubClassOfRangeMappingSet.toArray(new OWLNamedIndividualImpl[hasSubClassOfRangeMappingSet.size()]);
                }
                
                NodeSet<OWLNamedIndividual> hasSubClassOfDomainMappingNodeSet = mappingReasoner.getObjectPropertyValues(join,hasSubClassOfDomainMappingOP);
                //System.out.println("hasSubClassOfDomainMappingNodeSet Size: " + hasSubClassOfDomainMappingNodeSet.getFlattened().size());
                OWLNamedIndividualImpl[] hasSubClassOfDomainMapping = null;
                if  (hasSubClassOfDomainMappingNodeSet != null)
                {
                	Set<OWLNamedIndividual> hasSubClassOfDomainMappingSet = hasSubClassOfDomainMappingNodeSet.getFlattened();
                	hasSubClassOfDomainMapping = hasSubClassOfDomainMappingSet.toArray(new OWLNamedIndividualImpl[hasSubClassOfDomainMappingSet.size()]);
                }
                
                //System.out.println("Has Property Mapping: " + hasPropertyMapping + " length: " + hasPropertyMapping.length);
                
                if (hasPropertyMapping!=null && hasPropertyMapping.length==1)
                {
                    OWLNamedIndividual attribute = hasPropertyMapping[0];
                    //System.out.println("This is what's missing:" + attribute.getIRI());
                    //#To come here to the object name
                    String objectName = (view.getIRI().toString().replace(individualURI,""))+"_join_"+joinCounter+"_"+((attribute.getIRI().toString()).replace(individualURI,""));
                    
                    //#check whether the attribute is a datatype property or an object property
                    OWLObjectProperty currentOWLObjectProp = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
                    OWLSubObjectPropertyOfAxiom subPropAxiom = factory.getOWLSubObjectPropertyOfAxiom(currentOWLObjectProp,factory.getOWLTopObjectProperty());
                    OWLObjectProperty currentOWLObjectPropInverse = factory.getOWLObjectProperty(getInversePropertyName(objectName),basePrefix);
                    OWLSubObjectPropertyOfAxiom subPropAxiomInverse = factory.getOWLSubObjectPropertyOfAxiom(currentOWLObjectPropInverse,factory.getOWLTopObjectProperty());
                    OWLInverseObjectPropertiesAxiom inverseAxiom = factory.getOWLInverseObjectPropertiesAxiom(currentOWLObjectProp,currentOWLObjectPropInverse);
                    ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,subPropAxiom));
                    ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,subPropAxiomInverse));
                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,inverseAxiom));
                    
                    try {
						ontologyManager.saveOntology(db2OWLPrimitiveOntology);
						ontologyManager.saveOntology(db2OWLComplexOntology);
					} catch (OWLOntologyStorageException e) {

						e.printStackTrace();
					}
                    

                    //#Processing domain for join.
                    if (hasDomainMapping!=null && hasDomainMapping.length==1)
                    {
                        OWLNamedIndividual domain = hasDomainMapping[0];
                        String domainClassName = (domain.getIRI().toString()).replace(individualURI,"");
                        OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(domainClassName),basePrefix);
                        OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(domainClassName),basePrefix);
                        if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true) | db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
                        {
                            System.out.println("Warning: domain of "+ currentOWLObjectProp.getIRI().toString()+ " could not be processed since " + domain.getIRI().toString()+ " is mapped as an OWL property instead of an OWL class."); 
                        }
                        else
                        {
                            OWLClass possibleDomainClass = factory.getOWLClass(domainClassName,basePrefix);

                            OWLClass restrictedClass = null;
                            if (conditionRestrictions != null)
                            {
                            	restrictedClass = conditionRestrictions.get(domain);
                            }
                            
                            OWLObjectPropertyDomainAxiom restrictionAxiom = null;
                            //#If the domain contains a condition, the domain will become the class created using the condition, not the complete relation.
                            domainClass = possibleDomainClass;
                            
                            if (restrictedClass != null)
                            {
                            	
                            	//TODO: :( data type problems :/
                            	domainClass = restrictedClass;

                            }
                            else
                            {
                                domainClass=possibleDomainClass;
                                
                            }

                            restrictionAxiom = factory.getOWLObjectPropertyDomainAxiom(currentOWLObjectProp,domainClass);
                        	ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));

                        	try {
								ontologyManager.saveOntology(db2OWLComplexOntology);
							} catch (OWLOntologyStorageException e) {

								e.printStackTrace();
							}
                            
                            //TODO: Is this useful?
                            //joinPropertiesInPrimitive.push(currentOWLObjectProp)
  
                            joinDomainAndRange.put("domain",domainClass);
                            

                            //#TO DO : See if this works.
                            if (projectionRestrictions==null && joinCounter==1)
                            {

                                OWLClass viewClassWithoutProjection = factory.getOWLClass(view.getIRI().toString().replace(individualURI,""),basePrefix);
                                OWLEquivalentClassesAxiom equivalentAxiom = factory.getOWLEquivalentClassesAxiom(viewClassWithoutProjection,joinClassDomain);
                                //#Previously
                                equivalentAxiom=factory.getOWLEquivalentClassesAxiom(viewClassWithoutProjection,domainClass);
                                ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,equivalentAxiom));
                                
                                try {
									ontologyManager.saveOntology(db2OWLComplexOntology);
								} catch (OWLOntologyStorageException e) {

									e.printStackTrace();
								}
                            }
                                                        
                        }
                    }
                    else
                    {
                        System.out.println("Warning: The join " + join.getIRI().toString() + "contains more than one domain so it couldn't  be processed."); 
                    }
 
                    //#Processing range for join.
                    if (hasRangeMapping!=null && hasRangeMapping.length==1)
                    {
                        OWLNamedIndividual range = hasRangeMapping[0];

                        String rangeClassName = (range.getIRI().toString()).replace(individualURI,"");
                        OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(rangeClassName),basePrefix);
                        OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(rangeClassName),basePrefix);
                        if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true) || db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
                        {
                            System.out.println("Warning: range of "+ currentOWLObjectProp.getIRI().toString()+ " could not be processed since " + range.getIRI().toString()+ " is mapped as an OWL property instead of an OWL class."); 
                        }
                        else
                        {
                            OWLClass possibleRangeClass = factory.getOWLClass(rangeClassName,basePrefix);

                            OWLClass restrictedClass = null;
                            if (conditionRestrictions != null)
                            {
                            	restrictedClass = conditionRestrictions.get(range);

                            }
                            
                            rangeClass = possibleRangeClass;
                            //#If the range contains a condition, the range will become the class created using the condition, not the complete relation.
                            if (restrictedClass != null)
                            {
                            	//TODO: Datatype problem
                                rangeClass=restrictedClass;
                            }
                            else
                            {

                                rangeClass = possibleRangeClass;
                            }
                            

                            OWLObjectPropertyRangeAxiom restrictionAxiom = factory.getOWLObjectPropertyRangeAxiom(currentOWLObjectProp,rangeClass);

                            ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));

                            try {
								ontologyManager.saveOntology(db2OWLComplexOntology);
							} catch (OWLOntologyStorageException e) {

								e.printStackTrace();
							}
                            joinDomainAndRange.put("range",rangeClass);
                        }
                    }
                    else
                    {

                        System.out.println("Warning: The join " + join.getIRI().toString() + "contains more than one range so it couldn't  be processed."); 
                    }
 
                    if (domainClass!=null && rangeClass!=null && currentOWLObjectProp!=null)
                    {
                    	//#Processing instances of the join
                        NodeSet<OWLNamedIndividual> hasPartAttributeSet = mappingReasoner.getObjectPropertyValues(join,hasPartProperty);
                        ArrayList<OWLNamedIndividual> hasPartAttribute = VirtualConceptMappingHandler.returnIntersection(hasPartAttributeSet,attributeNodeSet);
                        if (hasPartAttribute==null || hasPartAttribute.size()!=2)
                        {
                            System.out.println("Warning: The instances of the join "+join.getIRI().toString()+" could not be processed since it has more than one attribute.");
                        }
                        else
                        {
                        
//                        individualsMapper.retrieveDbRecordsForJoin(currentOWLObjectProp,(hasPartAttribute[0].getIRI().toString()).replace(individualURI,""),(hasPartAttribute[1].getIRI().toString()).replace(individualURI,""),domainClassName,rangeClassName);
//                            #puts "Processing the instances of the join "+currentOWLObjectProp.getIRI().toString()+ " with attributes "+(hasPartAttribute[0].getIRI().toString()).replace(individualURI,"")+" and "+(hasPartAttribute[1].getIRI().toString()).replace(individualURI,"")
                        }
                        
                        //#Adding the restriction of aliases (if any) to joinClassDomain/joinClassDomain

                        if (hasSubClassOfDomainMapping!=null && hasSubClassOfDomainMapping.length>0)
                        {
                        	
                        	for (OWLNamedIndividualImpl aliasRestriction : hasSubClassOfDomainMapping)
                        	{
                        		
                        		ArrayList<OWLClassExpression> subClassRestrictionsArray = null;
                        		if (projectionRestrictions != null)
                        		{
                        			subClassRestrictionsArray = projectionRestrictions.get(aliasRestriction);
                        		}
                        		 
                                if (subClassRestrictionsArray != null)
                                {
                                	for (OWLClassExpression subClassRestriction : subClassRestrictionsArray)
                                	{
                                        OWLSubClassOfAxiom subClassRestrictionAxiom = factory.getOWLSubClassOfAxiom(joinClassDomain,subClassRestriction);
                                        ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,subClassRestrictionAxiom));
                                	}
                                }
                        	}
                        }
                        
                        if (hasSubClassOfRangeMapping!=null && hasSubClassOfRangeMapping.length>0)
                        {
                        	
                        	for (OWLNamedIndividualImpl aliasRestriction : hasSubClassOfRangeMapping)
                        	{
                        		ArrayList<OWLClassExpression> subClassRestrictionsArray = null;
                        		if (projectionRestrictions!=null)
                        		{
                        			subClassRestrictionsArray = projectionRestrictions.get(aliasRestriction);
                        		}

                                if (subClassRestrictionsArray!=null)
                                {
                                	
                                	for (OWLClassExpression subClassRestriction : subClassRestrictionsArray)
                                	{

                                        OWLSubClassOfAxiom subClassRestrictionAxiom = factory.getOWLSubClassOfAxiom(joinClassRange,subClassRestriction);
                                        ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,subClassRestrictionAxiom));
                                	}
                                }
                        	}
                        }
                        
                        //#Processing domain subclass
                        OWLObjectSomeValuesFrom someCardinality = factory.getOWLObjectSomeValuesFrom(currentOWLObjectProp,rangeClass);
                        OWLEquivalentClassesAxiom restrictionAxiom = factory.getOWLEquivalentClassesAxiom(joinClassDomain,someCardinality);
                        //joinDomainAndRange.put("joinClassDomainEquivalentAxioms",someCardinality);
                        OWLSubClassOfAxiom subClassAxiomDomain = factory.getOWLSubClassOfAxiom(joinClassDomain,domainClass);
                        ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
                        ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,subClassAxiomDomain));

                        //#Processing range subclass
                        OWLObjectSomeValuesFrom someCardinalityInv = factory.getOWLObjectSomeValuesFrom(currentOWLObjectPropInverse,domainClass);
                        OWLEquivalentClassesAxiom restrictionAxiomInv = factory.getOWLEquivalentClassesAxiom(joinClassRange,someCardinalityInv);
//                        joinDomainAndRange["joinClassRangeEquivalentAxioms"]=someCardinalityInv
                        OWLSubClassOfAxiom subClassAxiomRange = factory.getOWLSubClassOfAxiom(joinClassRange,rangeClass);
                        ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiomInv));
                        ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,subClassAxiomRange));
                        try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {

							e.printStackTrace();
						}
                    }
                    else
                    {
                        System.out.println("Warning the instances of the join "+currentOWLObjectProp.getIRI().toString()+ " could not be processed because either the domain, range or join attributes could not be retrieved"); 
                    }
                }
                else
                {

                    System.out.println("Warning: The join " + join.getIRI().toString() + "contains more than one from attribute so it couldn't  be processed."); 
                }
                joinsWithDomainAndRange.put(joinCounter,joinDomainAndRange);

            }

            OWLClass joinClassDomain = factory.getOWLClass(view.getIRI().toString().replace(individualURI,""),basePrefix);
            OWLClass viewClass = joinClassDomain;
            
            for (Integer joinKey : joinsWithDomainAndRange.keySet()) 
            {
            	HashMap<String, OWLClass> joinValue = joinsWithDomainAndRange.get(joinKey);
            
                OWLClass joinDomainClassName = joinValue.get("domain");

                for(Integer comparingJoinKey : joinsWithDomainAndRange.keySet())
                {
                	HashMap<String, OWLClass> comparingJoinValue = joinsWithDomainAndRange.get(comparingJoinKey);
                
                    if (!joinKey.equals(comparingJoinKey))
                    {
                        OWLClass comparingDomainClassName = comparingJoinValue.get("domain");
                        if (comparingDomainClassName == null)
                        	System.out.println("comparingDomainClassName is nulls");
                        //#Checking if they have the same domain
                        if (comparingDomainClassName != null && joinDomainClassName.getIRI().toString().equals(comparingDomainClassName.getIRI().toString()))
                        {
                            HashSet<OWLClass> intersection = new HashSet<OWLClass>();
                            OWLClass intersection1 = joinValue.get("joinClassDomain");
                            intersection.add(intersection1);
                            OWLClass intersection2 = comparingJoinValue.get("joinClassDomain");
                            intersection.add(intersection2);
                            OWLObjectIntersectionOf restrictionsIntersection = factory.getOWLObjectIntersectionOf(intersection);
                            String intersectionClassName = (intersection1.getIRI().toString()).replace("_domain","").replace(basePrefix.getDefaultPrefix(),"")+"_and_"+(intersection2.getIRI().toString().replace("_domain","")).replace(basePrefix.getDefaultPrefix(),"");
                            OWLClass intersectionClass = factory.getOWLClass(intersectionClassName,basePrefix);
                            OWLEquivalentClassesAxiom equivalenceAxiom = factory.getOWLEquivalentClassesAxiom(intersectionClass,restrictionsIntersection);
                            ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,equivalenceAxiom));
                            OWLEquivalentClassesAxiom viewEquivalenceAxiom = factory.getOWLEquivalentClassesAxiom(intersectionClass,viewClass);
                            ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,viewEquivalenceAxiom));
                            try {
								ontologyManager.saveOntology(db2OWLComplexOntology);
							} catch (OWLOntologyStorageException e) {

								e.printStackTrace();
							}
                        }
                        
                        OWLClass comparingRangeClassName = comparingJoinValue.get("range");
                        //#Checking if they have the same domain
                        if (comparingRangeClassName != null && joinDomainClassName.getIRI().toString().equals(comparingRangeClassName.getIRI().toString()))
                        {
                        	HashSet<OWLClass> intersection = new HashSet<OWLClass>();
                        	OWLClass intersection1=joinValue.get("joinClassDomain");
                            intersection.add(intersection1);
                            OWLClass intersection2 = comparingJoinValue.get("joinClassRange");
                            intersection.add(intersection2);
                            OWLObjectIntersectionOf restrictionsIntersection = factory.getOWLObjectIntersectionOf(intersection);
                            String intersectionClassName = (intersection1.getIRI().toString()).replace("_domain","").replace(basePrefix.getDefaultPrefix(),"")+"_and_"+(intersection2.getIRI().toString().replace("_range","")).replace(basePrefix.getDefaultPrefix(),"");
                            OWLClass intersectionClass = factory.getOWLClass(intersectionClassName,basePrefix);
                            OWLEquivalentClassesAxiom equivalenceAxiom = factory.getOWLEquivalentClassesAxiom(intersectionClass,restrictionsIntersection);
                            ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,equivalenceAxiom));
                            OWLEquivalentClassesAxiom viewEquivalenceAxiom = factory.getOWLEquivalentClassesAxiom(intersectionClass,viewClass);
                            ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,viewEquivalenceAxiom));
                            try {
								ontologyManager.saveOntology(db2OWLComplexOntology);
							} catch (OWLOntologyStorageException e) {

								e.printStackTrace();
							}
                        }
                    }
                }
            }
 
            for (Integer joinKey : joinsWithDomainAndRange.keySet()) 
            {
            	HashMap<String, OWLClass> joinValue = joinsWithDomainAndRange.get(joinKey);
                OWLClass joinRangeClassName = joinValue.get("range");

                for(Integer comparingJoinKey : joinsWithDomainAndRange.keySet())
                {
                	HashMap<String, OWLClass> comparingJoinValue = joinsWithDomainAndRange.get(comparingJoinKey);
                
                    if ((!joinKey.equals( comparingJoinKey)) && joinKey>comparingJoinKey)
                    {
                        OWLClass comparingRangeClassName = comparingJoinValue.get("range");
                        //#Checking if they have the same domain
                        if (joinRangeClassName != null && joinRangeClassName.getIRI().toString().equals(comparingRangeClassName.getIRI().toString()))
                        {
                            HashSet<OWLClass> intersection = new HashSet<OWLClass>();
                            OWLClass intersection1 = joinValue.get("joinClassRange");
                            intersection.add(intersection1);
                            OWLClass intersection2=comparingJoinValue.get("joinClassRange");
                            intersection.add(intersection2);
                            OWLObjectIntersectionOf restrictionsIntersection = factory.getOWLObjectIntersectionOf(intersection);

                            String intersectionClassName = (intersection1.getIRI().toString()).replace("_range","").replace(basePrefix.getDefaultPrefix(),"")+"_and_"+(intersection2.getIRI().toString().replace("_range","")).replace(basePrefix.getDefaultPrefix(),"");

                            OWLClass intersectionClass = factory.getOWLClass(intersectionClassName,basePrefix);
                            OWLEquivalentClassesAxiom equivalenceAxiom = factory.getOWLEquivalentClassesAxiom(intersectionClass,restrictionsIntersection);
                            ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,equivalenceAxiom));
                            OWLEquivalentClassesAxiom viewEquivalenceAxiom = factory.getOWLEquivalentClassesAxiom(intersectionClass,viewClass);
                            ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,viewEquivalenceAxiom));
                            try {
								ontologyManager.saveOntology(db2OWLComplexOntology);
							} catch (OWLOntologyStorageException e) {

								e.printStackTrace();
							}
                        }

                        OWLClass comparingDomainClassName = comparingJoinValue.get("domain");
                        //#Checking if they have the same domain
                        if (comparingDomainClassName != null && joinRangeClassName.getIRI().toString().equals(comparingDomainClassName.getIRI().toString()))
                        {
                            HashSet<OWLClass> intersection = new HashSet<OWLClass>();
                            OWLClass intersection1 = joinValue.get("joinClassRange");
                            intersection.add(intersection1);
                            OWLClass intersection2 = comparingJoinValue.get("joinClassDomain");
                            intersection.add(intersection2);
                            OWLObjectIntersectionOf restrictionsIntersection = factory.getOWLObjectIntersectionOf(intersection);
                            String intersectionClassName = (intersection1.getIRI().toString()).replace("_range","").replace(basePrefix.getDefaultPrefix(),"")+"_and_"+(intersection2.getIRI().toString().replace("_domain","")).replace(basePrefix.getDefaultPrefix(),"");
                            OWLClass intersectionClass = factory.getOWLClass(intersectionClassName,basePrefix);
                            OWLEquivalentClassesAxiom equivalenceAxiom = factory.getOWLEquivalentClassesAxiom(intersectionClass,restrictionsIntersection);
                            ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,equivalenceAxiom));
                            OWLEquivalentClassesAxiom viewEquivalenceAxiom = factory.getOWLEquivalentClassesAxiom(intersectionClass,viewClass);
                            ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,viewEquivalenceAxiom));
                            try {
								ontologyManager.saveOntology(db2OWLComplexOntology);
							} catch (OWLOntologyStorageException e) {

								e.printStackTrace();
							}
                        }
                    }
                }
            }

            try {
				ontologyManager.saveOntology(db2OWLComplexOntology);
			} catch (OWLOntologyStorageException e) {

				e.printStackTrace();
			}

        }
		
	}

	private static HashMap<OWLNamedIndividual, OWLClass> processConditionsForJoins(OWLNamedIndividual query) {
		HashMap<OWLNamedIndividual, OWLClass> conditionRestrictions = new HashMap<OWLNamedIndividual, OWLClass>();
        NodeSet<OWLNamedIndividual> queryConditionsNodeSet = mappingReasoner.getInstances(dbQueryConditionClass,false);
        NodeSet<OWLNamedIndividual> attributeNodeSet = mappingReasoner.getInstances(dbAttributeClass,false);

        
        
        NodeSet<OWLNamedIndividual> hasPartConditionSet = mappingReasoner.getObjectPropertyValues(query,hasPartProperty);
        ArrayList<OWLNamedIndividual> hasPartConditions = VirtualConceptMappingHandler.returnIntersection(hasPartConditionSet,queryConditionsNodeSet);
        if (hasPartConditions.size()>0)
        {
        	for (OWLNamedIndividual condition : hasPartConditions)
        	{
        		
                String conditionName = (condition.getIRI().toString()).replace(individualURI,"");
                NodeSet<OWLNamedIndividual> hasPartAttributeSet = mappingReasoner.getObjectPropertyValues(condition,hasPartProperty);
                
                ArrayList<OWLNamedIndividual> hasPartAttribute = VirtualConceptMappingHandler.returnIntersection(hasPartAttributeSet,attributeNodeSet);
                if (hasPartAttribute.size()==1)
                {
                	
                    OWLNamedIndividual attribute = hasPartAttribute.get(0);
                    String objectName = (attribute.getIRI().toString()).replace(individualURI,"");

                    //#check whether the attribute is a datatype property or an object property
                    OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
                    OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
                    if (db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
                    {
                        System.out.println("Warning: The faceted restriction cannot be added to the relation " + objectName+ " because is mapped as an OWL object property instead of a data property."); 
                    }
                    else
                    {

                        if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true))
                        {
                        	
                        	Set<OWLLiteral> hasOperatorSet = mappingReasoner.getDataPropertyValues(condition,hasOperatorDP);
                            OWLLiteral[] hasOperator = hasOperatorSet.toArray(new OWLLiteral[hasOperatorSet.size()]);
                            Set<OWLLiteral> hasValueSet = mappingReasoner.getDataPropertyValues(condition,hasValueDP);
                            OWLLiteral[] hasValue = hasValueSet.toArray(new OWLLiteral[hasValueSet.size()]);

                            
                            if (hasOperator.length>1 || hasOperator.length==0 || hasOperator==null)
                            {
                                System.out.println("Warning: There is more than one operator or there is no operator for the condition: " + condition.getIRI().toString());
                            }
                            else
                            {
                                if (hasValue.length>1 || hasValue.length==0)
                                {
                                    System.out.println("Warning: There is more than one value or there is no value for the condition: " + condition.getIRI().toString());
                                }
                                else
                                {
                                    OWLLiteral operator = hasOperator[0];
                                    OWLLiteral value = hasValue[0];

                                    Set<OWLNamedIndividual> datatypeSetSet = mappingReasoner.getObjectPropertyValues(attribute,hasAttributeDomainObjectProperty).getFlattened();
                                    OWLNamedIndividual[] datatypeSet = datatypeSetSet.toArray(new OWLNamedIndividual[datatypeSetSet.size()]);
                                    if (datatypeSet.length!=1)
                                    {
                                        System.out.println("Warning: the faceted restriction for the attribute " + attribute.getIRI().toString() + "cannot be processed since it does not have exactly 1 datatype"); 
                                    }
                                    else
                                    {
                                    	OWLNamedIndividual datatype = datatypeSet[0];
                                    
                                    	OWLDatatype datatypeIRI = null;
                                    	if (datatype.getIRI().toString().contains("string"))
                                        //if (datatype.getIRI()==db_string_individual.getIRI())
                                        {
                                            datatypeIRI = factory.getOWLDatatype(xsd_string_IRI);
                                        }
                                        else
                                        {
                                        	if (datatype.getIRI().toString().contains("integer"))
                                            //if (datatype.getIRI().equals(db_integer_individual.getIRI()))
                                            {
                                                datatypeIRI=factory.getIntegerOWLDatatype();
                                            }
                                            else
                                            {
                                            	if (datatype.getIRI().toString().contains("double"))
                                                //if (datatype.getIRI().equals(db_float_individual.getIRI()))
                                                {
                                                    datatypeIRI=factory.getDoubleOWLDatatype();
                                                }
                                                else
                                                {
                                                	if (datatype.getIRI().toString().contains("date"))
                                                    //if (datatype.getIRI()==db_date_individual.getIRI())
                                                    {
                                                        datatypeIRI=factory.getOWLDatatype(xsd_date_IRI);
                                                    }
                                                }
                                            }
                                        }
//                                        
                                        if (datatypeIRI == null)
                                        {
                                            System.out.println("DDWarning: range of "+ condition.getIRI().toString() + "could not be processed since " + datatype.getIRI().toString()+ " is not a valid data type.");
                                        }
                                        else
                                        {
                                        	
                                            NodeSet<OWLNamedIndividual> relationNodeSet = mappingReasoner.getObjectPropertyValues(condition,subClassOfMapping );
                                            
                                            OWLNamedIndividual[] relations = null;
                                            if (relationNodeSet != null)
                                            {
                                            	Set<OWLNamedIndividual> relationsSet = relationNodeSet.getFlattened();
                                            	relations = relationsSet.toArray(new OWLNamedIndividual[relationsSet.size()]);
                                            }
                                            
                                            OWLNamedIndividual relation = null;
                                            if (relations!=null && relations.length==1)
                                            {
                                            	relation = relations[0];
                                            }
                                            
                                            if (relation != null)
                                            {
                                                OWLClass objectClass = factory.getOWLClass((relation.getIRI().toString()).replace(individualURI,""),basePrefix);
                                                //TODO:
                                                OWLClass facet = createAnonymousFacet(conditionName,possibleDataProperty,operator,value,datatype,objectClass);
                                                
                                                conditionRestrictions.put(relation,facet);

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    System.out.println("Warning: This condition contains more than one attribute: "  + condition.getIRI().toString());
                    for (OWLNamedIndividual att : hasPartAttribute)
                    	System.out.println(att.getIRI().toString());

                }
                
        	}
        }

        return conditionRestrictions;
	}

	private static OWLClass createAnonymousFacet(String conditionName, OWLDataProperty possibleDataProperty,
			OWLLiteral operator, OWLLiteral value, OWLNamedIndividual datatypeIndividual, OWLClass objectClass) {
		
		
		//TODO: there is a very similar method somewhere else
		OWLDatatype datatype = null;
		OWLClass facet = null;
		
		if (datatypeIndividual.getIRI().toString().contains("integer"))//equals(db_integer_individual))
		//if (datatypeIndividual.equals(db_integer_individual))
		{
			datatype = factory.getIntegerOWLDatatype();
		}
		else if (datatypeIndividual.getIRI().toString().contains("float"))//if (datatypeIndividual.equals(db_float_individual))
		{
			datatype = factory.getDoubleOWLDatatype();
		}
		else if (datatypeIndividual.getIRI().toString().contains("double"))//if (datatypeIndividual.equals(db_double_individual))
		{
			datatype = factory.getDoubleOWLDatatype();
		}
		else if (datatypeIndividual.getIRI().toString().contains("string"))//if (datatypeIndividual.equals(db_string_individual))
		{
			datatype = factory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#string"));
		}

		
		OWLDatatypeRestriction dataRange = null;
		OWLObjectIntersectionOf equivalentClass = null;
		if (datatype != null)
		{
			String operatorValue = operator.getLiteral().toString();


			if (operatorValue.equals(">"))
			{
				dataRange = factory.getOWLDatatypeRestriction(datatype,OWLFacet.getFacet(xsd_minInclusive_IRI),value);
			}
			else if (operatorValue.equals("<"))
			{
				dataRange = factory.getOWLDatatypeRestriction(datatype,OWLFacet.getFacet(xsd_maxInclusive_IRI),value);
			}
			else if (operatorValue.equals("="))
			{
				dataRange = factory.getOWLDatatypeRestriction(datatype,OWLFacet.getFacet(xsd_pattern_IRI),value);
			}


			HashSet<OWLClassExpression> intersectionClasses = new HashSet<OWLClassExpression> (); 
			OWLClass conditionClass = null;
			if (dataRange != null)
			{

				OWLDataSomeValuesFrom someValuesFrom = factory.getOWLDataSomeValuesFrom(possibleDataProperty,dataRange);

				intersectionClasses.add(someValuesFrom);
				intersectionClasses.add(objectClass);
				equivalentClass  = factory.getOWLObjectIntersectionOf(intersectionClasses);
				
				conditionClass =factory.getOWLClass(conditionName,basePrefix);
			    OWLEquivalentClassesAxiom equivalentClassesAxiom = factory.getOWLEquivalentClassesAxiom(conditionClass,equivalentClass);
			    facet = conditionClass;
			    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,equivalentClassesAxiom));
			}
			else
			{

				System.out.println("The operator " + operator.getLiteral().toString() + " for the condition in the class " + conditionClass + " could not be processed."); 
			}
		}
		else
		{
			System.out.println("The datatype" + datatypeIndividual.getIRI().toString()+"cannot be mapped to a facet");
		}


		return facet;
	}

	private static HashMap<OWLNamedIndividual, ArrayList<OWLClassExpression>> processProjectionsForJoins(OWLNamedIndividual query) {
		
		HashMap<OWLNamedIndividual, ArrayList<OWLClassExpression>> aliasesRestrictions = new HashMap<OWLNamedIndividual, ArrayList<OWLClassExpression>>(); 

        NodeSet<OWLNamedIndividual> attributeNodeSet = mappingReasoner.getInstances(dbAttributeClass,false);

        NodeSet<OWLNamedIndividual> aliasAttributeNodeSet = mappingReasoner.getInstances(dbAttributeAliasClass,false);
//        hasPartAliases=Array.new

        NodeSet<OWLNamedIndividual> hasPartAliasesSet = mappingReasoner.getObjectPropertyValues(query,hasPartProperty);
        ArrayList<OWLNamedIndividual> hasPartAliases = VirtualConceptMappingHandler.returnIntersection(hasPartAliasesSet,aliasAttributeNodeSet);
        if (hasPartAliases.size()>0)
        {
        	
        	for (OWLNamedIndividual aliases : hasPartAliases)
        	{

        		ArrayList<OWLClassExpression> projectionRestrictions = new ArrayList<OWLClassExpression>();

                NodeSet<OWLNamedIndividual> hasPartAttributeSet = mappingReasoner.getObjectPropertyValues(aliases,hasPartProperty);
                ArrayList<OWLNamedIndividual> hasPartAttribute = VirtualConceptMappingHandler.returnIntersection(hasPartAttributeSet,attributeNodeSet);
                if (hasPartAttribute.size()==1)
                {
                    OWLNamedIndividual attribute = hasPartAttribute.get(0);
                    String objectName = (attribute.getIRI().toString()).replace(individualURI,"");

                    //#check whether the attribute is a datatype property or an object property
                    OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
                    OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
                    OWLClass possibleClass = factory.getOWLClass(objectName,basePrefix);
                    if (db2OWLPrimitiveOntology.containsClassInSignature(possibleClass.getIRI(),true))
                    {
                        System.out.println("Warning: the exists some restriction of: "+ possibleClass.getIRI().toString() + " could not be processed since " + possibleObjectProperty.getIRI().toString()+ " is mapped as an OWL class instead of an OWL object property."); 
                    }
                    else
                    {
                        if (db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
                        {
                            OWLObjectSomeValuesFrom someCardinality = factory.getOWLObjectSomeValuesFrom(possibleObjectProperty,factory.getOWLThing());
                            projectionRestrictions.add(someCardinality);
                        }
                        else
                        {
                            if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true))
                            {
                                OWLDataSomeValuesFrom someCardinality = factory.getOWLDataSomeValuesFrom(possibleDataProperty,factory.getTopDatatype());
                                projectionRestrictions.add(someCardinality);

                            }
                        }
                    }
                }
                aliasesRestrictions.put(aliases, projectionRestrictions);
//                #puts "aliases:" + aliases.getIRI().toString() + " projection Restrictions "+projectionRestrictions[0].toString()
        	}
        }
        return aliasesRestrictions;
	}
	
	private static String getPropertyName(String objectName)
	{
		return "has" + objectName;		
	}
	
	private static String getInversePropertyName(String objectName)
	{
		return "is"+objectName+"Of";
	}
                
   
}
