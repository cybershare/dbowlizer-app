package com.cybershare.dbowlizer.generate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLFacet;

public class VirtualConceptMappingHandler 
{
	private static OWLOntologyManager ontologyManager;
	private static OWLDataFactory factory;
	private static Reasoner mappingReasoner;
	private static OWLObjectProperty hasPartProperty;
	private static OWLClass dbQueryClass;
	private static OWLClass dbQueryConditionClass;
	private static OWLClass dbAttributeClass;
	private static String individualURI;
	private static DefaultPrefixManager basePrefix;
	private static OWLOntology db2OWLPrimitiveOntology;
	private static OWLOntology db2OWLComplexOntology;
	private static OWLDataProperty hasOperatorDP;
	private static OWLDataProperty hasValueDP;
	private static OWLObjectProperty hasDomainMapping;
	private static OWLObjectProperty hasRangeMapping;
	private static OWLObjectProperty hasAttributeDomainObjectProperty;
	private static OWLNamedIndividual db_string_individual;
	private static IRI xsd_string_IRI;
	private static OWLNamedIndividual db_integer_individual;
	private static OWLNamedIndividual db_float_individual;
	private static OWLNamedIndividual db_date_individual;
	private static IRI xsd_date_IRI;
	private static Object db_double_individual;
	private static IRI xsd_minInclusive_IRI;
	private static IRI xsd_maxInclusive_IRI;
	private static IRI xsd_pattern_IRI;
	private static OWLClass dbAttributeAliasClass;


	public static void processVirtualConceptMappings(OWLEntitiesBundle owlEntitiesBundle)
	{
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.properties");
		ontologyManager = owlEntitiesBundle.getOntologyManager();
		factory = ontologyManager.getOWLDataFactory();
		mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		hasPartProperty = owlEntitiesBundle.getHasPartProperty();
		dbQueryClass = owlEntitiesBundle.getDbQueryClass();
		dbQueryConditionClass = owlEntitiesBundle.getDbQueryConditionClass();
		dbAttributeClass = owlEntitiesBundle.getDbAttributeClass();
		db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		basePrefix = owlEntitiesBundle.getBasePrefix();
		individualURI = owlEntitiesBundle.getIndividualURI();
		String baseURI = owlEntitiesBundle.getBaseURI(); 

		OWLObjectProperty subClassOfMapping = owlEntitiesBundle.getSubClassOfMapping();
		OWLObjectProperty propertyRestrictedClassMapping = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("propertyRestrictedClassMapping")));
		db2OWLComplexOntology = owlEntitiesBundle.getDb2OWLComplexOntology();
		OWLObjectProperty equivalentClassMapping = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("equivalentClassMapping")));
		OWLClass continuantCls = owlEntitiesBundle.getContinuantCls();

		hasOperatorDP = owlEntitiesBundle.getHasOperator();
		hasValueDP = owlEntitiesBundle.getHasValue();
		hasDomainMapping = owlEntitiesBundle.getHasDomainMapping();
		hasRangeMapping = owlEntitiesBundle.getHasRangeMapping();
		hasAttributeDomainObjectProperty = owlEntitiesBundle.getHasAttributeDomainObjectProperty();
		db_string_individual = owlEntitiesBundle.getDb_string_individual();
		xsd_string_IRI = owlEntitiesBundle.getXsd_string_IRI();
		db_integer_individual = owlEntitiesBundle.getDb_integer_individual();
		db_float_individual = owlEntitiesBundle.getDb_float_individual();
		db_date_individual = owlEntitiesBundle.getDb_date_individual();
		xsd_date_IRI = owlEntitiesBundle.getXsd_date_IRI();
		db_double_individual = owlEntitiesBundle.getDb_double_individual();
		xsd_minInclusive_IRI = owlEntitiesBundle.getXsd_minInclusive_IRI();
		xsd_maxInclusive_IRI = owlEntitiesBundle.getXsd_maxInclusive_IRI();
		xsd_pattern_IRI = owlEntitiesBundle.getXsd_pattern_IRI();

		dbAttributeAliasClass = owlEntitiesBundle.getDbAttributeAliasClass();
				
		OWLClass virtualConceptMappingClass = factory.getOWLClass(propertiesManager.getString("virtualConceptMapping"),basePrefix);
        System.out.println("virtual concept mapping class: "+ virtualConceptMappingClass.getIRI());
		
		
		//#Querying the reasoner to retrieve all instances of the class, direct.
		NodeSet<OWLNamedIndividual> conceptIndividualsNodeSet = mappingReasoner.getInstances(virtualConceptMappingClass,false);
		//#Create the OWL classes corresponding to these individuals and add them into the output ontology.
		if (conceptIndividualsNodeSet != null)
		{
			//boolean hierarchy;
			//#Set of named individuals
			Set<OWLNamedIndividual> entityConceptIndividuals = conceptIndividualsNodeSet.getFlattened();
			for (OWLNamedIndividual virtualCls : entityConceptIndividuals)
			{
				String virtualClsName = virtualCls.getIRI().toString().replace(individualURI,"");
				
				if (virtualClsName.contains(":")){
					virtualClsName = virtualClsName.substring(virtualClsName.indexOf(":")+1);
				    System.out.println("views class: "+ virtualClsName);
				}
				
				OWLClass currentOWLCls = factory.getOWLClass(virtualClsName,basePrefix);
				//#checking whether it is a subclass, equivalent class, super class||just an independent class.
				OWLObjectIntersectionOf restrictedSuperClass = null;

				NodeSet<OWLNamedIndividual> subClassNodeSet = mappingReasoner.getObjectPropertyValues(virtualCls,subClassOfMapping);
				NodeSet<OWLNamedIndividual> superClassNodeSet = mappingReasoner.getObjectPropertyValues(virtualCls,propertyRestrictedClassMapping);
				//#Set of named individuals
				//#For views that contain a subset of a relation (with a select condition).
				Set<OWLNamedIndividual> subClassIndividuals = subClassNodeSet.getFlattened();

				if (subClassIndividuals.size()>0)
				{
					for (OWLNamedIndividual subClassObj : subClassIndividuals)
					{

						String objectName = subClassObj.getIRI().toString().replace(individualURI,"");
						//#puts "Check that the class is not a property."
						OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);

						OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
						if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true) || db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
						{
							System.out.println("Warning: The subclass relation between " + subClassObj.getIRI().toString() + "and" + currentOWLCls.getIRI().toString() + "could not be processed since " + subClassObj.getIRI().toString()+ " is mapped as an OWL property instead of an OWL class."); 
						}
						else
						{
							OWLClass objectClass = factory.getOWLClass(objectName,basePrefix);
							OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(currentOWLCls,objectClass);
							ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,restrictionAxiom));
							
		                    //PROV-O
		    				PROVOHandler.owlClassCreated(owlEntitiesBundle, objectClass, objectName, db2OWLPrimitiveOntology);
							try {
								ontologyManager.saveOntology(db2OWLPrimitiveOntology);
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
							//hierarchy=true;
							restrictedSuperClass = processConditionsForView(virtualCls,currentOWLCls,objectClass,superClassNodeSet);
							//#If there are no projections, the class is asserted as sublcass of the conditions.
							if (superClassNodeSet.getFlattened().size()<1 && restrictedSuperClass!=null)
							{

								OWLEquivalentClassesAxiom restrictionAxiomEq = factory.getOWLEquivalentClassesAxiom(currentOWLCls,restrictedSuperClass);

								ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiomEq));
								try {
									ontologyManager.saveOntology(db2OWLComplexOntology);
								} catch (OWLOntologyStorageException e) {
									e.printStackTrace();
								}

							}
						}					
					}
				}

				//#Set of named individuals
				if (superClassNodeSet != null)
				{
					Set<OWLNamedIndividual> superClassIndividuals = superClassNodeSet.getFlattened();
					if (superClassIndividuals.size()>0)
					{
						for (OWLNamedIndividual superClassObj : superClassIndividuals)
						{
							String objectName = superClassObj.getIRI().toString().replace(individualURI,"");
							//#Check that the class is not a property.
							OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
							OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
							if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true) || db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
							{
								System.out.println("Warning: The subclass relation between " + superClassObj.getIRI().toString() + "and" + currentOWLCls.getIRI().toString() + "could not be processed since " + superClassObj.getIRI().toString()+ " is mapped as an OWL property instead of an OWL class.");
							}
							else
							{
								OWLClass objectClass = factory.getOWLClass(objectName,basePrefix);
								OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(objectClass,currentOWLCls);
								//hierarchy=true;
								processProjectionsForViews(virtualCls,currentOWLCls,objectClass,restrictedSuperClass);
							}
						}
					}
				}

				NodeSet<OWLNamedIndividual> eqClassNodeSet = mappingReasoner.getObjectPropertyValues(virtualCls,equivalentClassMapping);	
				//#Set of named individuals

				if (eqClassNodeSet != null)
				{
					Set<OWLNamedIndividual> eqClassIndividuals = eqClassNodeSet.getFlattened();
					//System.out.println("set size: "+eqClassIndividuals.size());
					if (eqClassIndividuals.size()>0)
					{
						for (OWLNamedIndividual eqClassObj : eqClassIndividuals)
						{
							String objectName = eqClassObj.getIRI().toString().replace(individualURI,"");
							//#Check that the class is not a property.
							OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
							OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
							if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true) || db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
							{
								System.out.println("Warning: The subclass relation between " + eqClassObj.getIRI().toString() + "and" + currentOWLCls.getIRI().toString() + "could not be processed since " + eqClassObj.getIRI().toString()+ " is mapped as an OWL property instead of an OWL class."); 
							}
							else
							{
								OWLClass objectClass = factory.getOWLClass(objectName,basePrefix);
								OWLEquivalentClassesAxiom restrictionAxiom = factory.getOWLEquivalentClassesAxiom(currentOWLCls,objectClass);
								ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,restrictionAxiom));
								try {
									ontologyManager.saveOntology(db2OWLPrimitiveOntology);
								} catch (OWLOntologyStorageException e) {
									e.printStackTrace();
								}
								//hierarchy=true;
							}
						}
					}
				}


				OWLSubClassOfAxiom subClsAxiom = factory.getOWLSubClassOfAxiom(currentOWLCls,continuantCls);
				ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,subClsAxiom));
				
				//PROV-O
				PROVOHandler.owlClassCreated(owlEntitiesBundle, currentOWLCls, virtualClsName, db2OWLPrimitiveOntology);
				try {
					ontologyManager.saveOntology(db2OWLPrimitiveOntology);
				} catch (OWLOntologyStorageException e) {

					e.printStackTrace();
				}

				//#Annotation property
				OWLAnnotationProperty commentProperty = factory.getRDFSComment();
				OWLLiteral commentValue = factory.getOWLLiteral("A class that represents an entity generated from "+virtualClsName.toString());
				//#OWL Annotation
				OWLAnnotation commentAnnotation = factory.getOWLAnnotation(commentProperty,commentValue);

				OWLAnnotationAssertionAxiom annotationAxiom = factory.getOWLAnnotationAssertionAxiom(currentOWLCls.getIRI(),commentAnnotation);
				ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,annotationAxiom));
			}

		}
		try {
			ontologyManager.saveOntology(db2OWLPrimitiveOntology);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}


	private static void processProjectionsForViews(OWLNamedIndividual virtualCls, OWLClass currentOWLCls,
			OWLClass objectClass, OWLObjectIntersectionOf restrictedSuperClass) {

        NodeSet<OWLNamedIndividual> hasPartNodeSet = mappingReasoner.getObjectPropertyValues(virtualCls,hasPartProperty);
        NodeSet<OWLNamedIndividual> queriesNodeSet = mappingReasoner.getInstances(dbQueryClass,true);
        NodeSet<OWLNamedIndividual> attributeNodeSet = mappingReasoner.getInstances(dbAttributeClass,false);

        NodeSet<OWLNamedIndividual> aliasAttributeNodeSet = mappingReasoner.getInstances(dbAttributeAliasClass,false);

        ArrayList<OWLNamedIndividual> hasPartQueries = returnIntersection(hasPartNodeSet,queriesNodeSet);
        if (hasPartQueries.size() > 0)
        {
        	for (OWLNamedIndividual query : hasPartQueries)
        	{
        
                NodeSet<OWLNamedIndividual> hasPartAliasesSet = mappingReasoner.getObjectPropertyValues(query,hasPartProperty);
                ArrayList<OWLNamedIndividual> hasPartAliases = returnIntersection(hasPartAliasesSet,aliasAttributeNodeSet);
                if (hasPartAliases.size()>0)
                {
                	for (OWLNamedIndividual aliases : hasPartAliases)
                	{

                        NodeSet<OWLNamedIndividual> hasPartAttributeSet = mappingReasoner.getObjectPropertyValues(aliases,hasPartProperty);
                        ArrayList<OWLNamedIndividual> hasPartAttribute = returnIntersection(hasPartAttributeSet,attributeNodeSet);
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
                                System.out.println("Warning: the exists some restriction of: "+ possibleClass.getIRI().toString() + " could not be processed since " + possibleClass.getIRI().toString()+ " is mapped as an OWL class instead of an OWL object property."); 
                            }
                            else
                            { 
                            	
                            	OWLAxiom restrictionAxiom = null;
                            	OWLObjectIntersectionOf restrictionsAxiom = null;
                                if (db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
                                {
                                    
                                    OWLObjectSomeValuesFrom someCardinality = factory.getOWLObjectSomeValuesFrom(possibleObjectProperty,factory.getOWLThing());
                                    //#add here the restricted superclasses
                                    if (restrictedSuperClass!=null)
                                    {
                                        //#puts "Enter the if retricted super class !- nil object property"
                                        HashSet<OWLClassExpression> intersection = new HashSet<OWLClassExpression>();
										intersection.add(restrictedSuperClass);
                                        intersection.add(someCardinality);
                                        
                                        //TODO: Ask VN
                                        restrictionsAxiom = factory.getOWLObjectIntersectionOf(intersection);
                                        //DIego added this:
//                                      restrictionAxiom=factory.getOWLEquivalentClassesAxiom(currentOWLCls,restrictionsAxiom);
                                    }
                                    else
                                    {
                                    	
                                        restrictionAxiom=factory.getOWLEquivalentClassesAxiom(currentOWLCls,someCardinality);
                                        
                                    }
                                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
                                    try {
										ontologyManager.saveOntology(db2OWLComplexOntology);
									} catch (OWLOntologyStorageException e) {
										e.printStackTrace();
									}
                                
                                }
                                else
                                {
                                    if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true))
                                    {
                                        OWLDataSomeValuesFrom someCardinality = factory.getOWLDataSomeValuesFrom(possibleDataProperty,factory.getTopDatatype());

                                        if (restrictedSuperClass != null)
                                        {
                                        	HashSet<OWLClassExpression> intersection = new HashSet<OWLClassExpression>();
											intersection.add(restrictedSuperClass);
                                            intersection.add(someCardinality);
                                            //TODO: Ask VN
                                            restrictionsAxiom=factory.getOWLObjectIntersectionOf(intersection);
                                            restrictionAxiom=factory.getOWLEquivalentClassesAxiom(currentOWLCls,restrictionsAxiom);
                                        }
                                        else
                                        {

                                            restrictionAxiom=factory.getOWLEquivalentClassesAxiom(currentOWLCls,someCardinality);
                                        }
                                        
                                        ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
                                        try {
											ontologyManager.saveOntology(db2OWLComplexOntology);
										} catch (OWLOntologyStorageException e) {

											e.printStackTrace();
										}
                                    }
                                }
                            }
                        }
                	}
                }
        	}
        }

	}


	//TODO: move this method to a utils class
	public static OWLObjectIntersectionOf processConditionsForView(OWLNamedIndividual virtualCls,
			OWLClass currentOWLCls, OWLClass objectClass, NodeSet<OWLNamedIndividual> superClassNodeSet) {
		OWLObjectIntersectionOf facet = null;
		ArrayList<OWLNamedIndividual> hasPartQueries = new ArrayList<OWLNamedIndividual>();
		NodeSet<OWLNamedIndividual> hasPartNodeSet = mappingReasoner.getObjectPropertyValues(virtualCls,hasPartProperty);
		NodeSet<OWLNamedIndividual> queriesNodeSet = mappingReasoner.getInstances(dbQueryClass,true);
		NodeSet<OWLNamedIndividual> queryConditionsNodeSet = mappingReasoner.getInstances(dbQueryConditionClass,false);
		NodeSet<OWLNamedIndividual> attributeNodeSet = mappingReasoner.getInstances(dbAttributeClass,false);

		hasPartQueries = returnIntersection(hasPartNodeSet,queriesNodeSet);
		if (hasPartQueries.size() > 0)
		{

			for (OWLNamedIndividual query : hasPartQueries)
			{

				NodeSet<OWLNamedIndividual> hasPartConditionSet = mappingReasoner.getObjectPropertyValues(query,hasPartProperty);
				ArrayList<OWLNamedIndividual> hasPartConditions = returnIntersection(hasPartConditionSet,queryConditionsNodeSet);
				if (hasPartConditions.size() > 0)
				{
					for (OWLNamedIndividual condition : hasPartConditions)
					{
						NodeSet<OWLNamedIndividual> hasPartAttributeSet = mappingReasoner.getObjectPropertyValues(condition,hasPartProperty);
						ArrayList<OWLNamedIndividual> hasPartAttribute = returnIntersection(hasPartAttributeSet,attributeNodeSet);
						if (hasPartAttribute.size()==1)
						{
							OWLNamedIndividual attribute = hasPartAttribute.get(0);
							String objectName = attribute.getIRI().toString().replace(individualURI,"");

							//#check whether the attribute is a datatype property||an object property
							OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName).toString(),basePrefix);
							OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(objectName).toString(),basePrefix);
							//VN:#TO DO: Here a new class will be added for the domain and range of that object/datatype property. 
							if (db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
							{
								String viewPropertyName = (virtualCls.getIRI().toString()).replace(individualURI,"");

								String viewClassName = (virtualCls.getIRI().toString()).replace(individualURI,"")+"_domain";
								OWLObjectProperty viewObjectProperty = factory.getOWLObjectProperty(viewPropertyName,basePrefix);
								OWLSubObjectPropertyOfAxiom subPropertyAxiom = factory.getOWLSubObjectPropertyOfAxiom(viewObjectProperty,possibleObjectProperty);
								ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,subPropertyAxiom));
								Set<OWLLiteral> hasOperatorSet = mappingReasoner.getDataPropertyValues(condition,hasOperatorDP);
								OWLLiteral[] hasOperator = mappingReasoner.getDataPropertyValues(condition,hasOperatorDP).toArray(new OWLLiteral[hasOperatorSet.size()] );
								Set<OWLLiteral> hasValueSet = mappingReasoner.getDataPropertyValues(condition,hasValueDP);
								OWLLiteral[] hasValue = hasValueSet.toArray(new OWLLiteral[hasValueSet.size()] );
								if (hasOperator.length!=1 || hasValue.length!=1)
								{
									System.out.println("The operator||the value of the condition "+condition.getIRI().toString()+" is not exactly one");  
								}  
								else
								{
									OWLLiteral operator = hasOperator[0];
									OWLLiteral value = hasValue[0];
									Set<OWLNamedIndividual> hasDomainSet = mappingReasoner.getObjectPropertyValues(attribute,hasDomainMapping).getFlattened();
									OWLNamedIndividual[] hasDomain = hasDomainSet.toArray(new OWLNamedIndividual[hasDomainSet.size()]);

									Set<OWLNamedIndividual> hasRangeSet = mappingReasoner.getObjectPropertyValues(attribute,hasRangeMapping).getFlattened();
									OWLNamedIndividual[] hasRange = hasRangeSet.toArray(new OWLNamedIndividual[hasRangeSet.size()]);

									if (hasDomain==null || hasRange==null||hasRange.length!=1||hasDomain.length!=1)
									{
										System.out.println("Warning: the domain||the range of the condition "+condition.getIRI().toString()+" is not exactly one"); 
									}
									else
									{
										String propertyDomain = (hasDomain[0].getIRI().toString()).replace(individualURI,"");
										String propertyRange = (hasRange[0].getIRI().toString()).replace(individualURI,"");

										facet=createFacetedRestrictionForProperty(objectName,possibleObjectProperty,operator,value,propertyDomain,propertyRange,virtualCls);
										
									}
								}
							}
							else
							{
								

								if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true))
								{

									Set<OWLLiteral> hasOperatorSet = mappingReasoner.getDataPropertyValues(condition,hasOperatorDP);
									OWLLiteral[] hasOperator = mappingReasoner.getDataPropertyValues(condition,hasOperatorDP).toArray(new OWLLiteral[hasOperatorSet.size()] );
									Set<OWLLiteral> hasValueSet = mappingReasoner.getDataPropertyValues(condition,hasValueDP);
									OWLLiteral[] hasValue = hasValueSet.toArray(new OWLLiteral[hasValueSet.size()] );

									if (hasOperator.length>1||hasOperator.length==0)
									{
										System.out.println("Warning: There is more than one operator or there is no operator for the condition: " + condition.getIRI().toString()); 
									}
									else
									{
										if (hasValue.length>1||hasValue.length==0)
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
												System.out.println("Warning: the faceted restriction for the attribute " + attribute.getIRI().toString() + "cannot be processed since it has more than one datatype"); 
											}
											else
											{
												OWLNamedIndividual datatype = datatypeSet[0];
												OWLDatatype datatypeIRI = null;

												if (datatype.getIRI().toString().contains("string"))
												//if (datatype.getIRI().equals(db_string_individual.getIRI()))
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
															//if (datatype.getIRI().equals(db_date_individual.getIRI()))
															{
																datatypeIRI=factory.getOWLDatatype(xsd_date_IRI);
															}

														}		
													}
												}

												//TODO: You changed this from != to == due to a possible bug
												if (datatypeIRI==null)
												{
													System.out.println("Warning: range of "+ condition.getIRI().toString() + "could not be processed since " + datatype.getIRI().toString()+ " is not a valid data type."); 												
												}
												else
												{
													facet = createFacetedRestriction(currentOWLCls,possibleDataProperty,operator,value,datatype,objectClass);
													
													if (superClassNodeSet==null)
													{
														//TODO: Ask VN Que onda
														//OWLEquivalentClassesAxiom equivalentClassesAxiom = factory.getOWLEquivalentClassesAxiom((OWLClassExpression) virtualCls,facet);
														OWLEquivalentClassesAxiom equivalentClassesAxiom = factory.getOWLEquivalentClassesAxiom(currentOWLCls,facet);
														
														ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,equivalentClassesAxiom));
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

							System.out.println("Warninng: This condition contains more than one attribute: "  + condition.getIRI().toString());
							for (OWLNamedIndividual att : hasPartAttribute)
							{
								System.out.println(att.getIRI().toString()); 
							}
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

		return facet;
	}


	private static OWLObjectIntersectionOf createFacetedRestriction(OWLClass virtualCls,
			OWLDataProperty possibleDataProperty, OWLLiteral operator, OWLLiteral value, OWLNamedIndividual datatypeIndividual,
			OWLClass objectClass) {

		OWLObjectIntersectionOf equivalentClass = null;
		OWLDatatype datatype = null;

		if (datatypeIndividual.getIRI().toString().contains("integer"))
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
			if (dataRange != null)
			{

				OWLDataSomeValuesFrom someValuesFrom = factory.getOWLDataSomeValuesFrom(possibleDataProperty,dataRange);

				intersectionClasses.add(someValuesFrom);
				intersectionClasses.add(objectClass);
				equivalentClass = factory.getOWLObjectIntersectionOf(intersectionClasses);
			}
			else
			{

				System.out.println("The operator " + operator.getLiteral().toString() + " for the condition in the class " + virtualCls.toString() + " could not be processed."); 
			}
		}
		else
		{
			System.out.println("The datatype" + datatypeIndividual.getIRI().toString()+"cannot be mapped to a facet");
		}


		return equivalentClass;


	}


	private static OWLObjectIntersectionOf createFacetedRestrictionForProperty(String attribute,
			OWLObjectProperty objectProperty, OWLLiteral operator, OWLLiteral value, String domain,
			String range, OWLNamedIndividual view) {

		OWLObjectIntersectionOf facet = null;
		//TODO: ASK VN que onda
		//		individualsInRange=individualsMapper.retrieveDbRecordsWithPKInRange(attribute,objectProperty,range,operator,value);
		//	    individualsSet=HashSet.new
		//	    individualsInRange.each do |individual|
		//	      individualsSet.add(individual)
		//	    end

		
		HashSet<OWLNamedIndividual> individualsSet = new HashSet<OWLNamedIndividual>();
		OWLClassExpression oneOfClass = factory.getOWLObjectOneOf(individualsSet);

		OWLClassExpression someValuesFrom = factory.getOWLObjectSomeValuesFrom(objectProperty,oneOfClass);

		HashSet<OWLClassExpression> intersectionClasses = new HashSet<OWLClassExpression>();
		intersectionClasses.add(someValuesFrom);
		intersectionClasses.add(factory.getOWLClass(domain,basePrefix));
		OWLObjectIntersectionOf equivalentClass = factory.getOWLObjectIntersectionOf(intersectionClasses);

		facet = equivalentClass;
		
		return facet;		


	}


	private static String getPropertyName(String objectName)
	{
		return "has" + objectName;		
	}

	//TODO: Move this method to a utils class
	public static ArrayList<OWLNamedIndividual> returnIntersection(NodeSet<OWLNamedIndividual> nodeSet1,NodeSet<OWLNamedIndividual>nodeSet2)
	{
		ArrayList<OWLNamedIndividual> returnedArray = new ArrayList<OWLNamedIndividual>();
		if ((nodeSet1 !=null) && (nodeSet2 != null))
		{
			Set<OWLNamedIndividual> nodeSet1Individuals = nodeSet1.getFlattened();

			Set<OWLNamedIndividual> nodeSet2Individuals = nodeSet2.getFlattened();

			for (OWLNamedIndividual nodeSet1Individual: nodeSet1Individuals)
			{
				for (OWLNamedIndividual nodeSet2Individual: nodeSet2Individuals)
				{
					if (nodeSet1Individual.getIRI().toString().equals(nodeSet2Individual.getIRI().toString()))
					{
						returnedArray.add(nodeSet1Individual);
						break;
					}
				}
				//				if (nodeSet2Individuals.contains(nodeSet1Individual))
				//				{
				//					returnedArray.add(nodeSet1Individual);
				//				}
			}
		}		
		return returnedArray;
	}



}
