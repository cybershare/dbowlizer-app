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

import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;


public class ConceptRestrictionMappingHandler 
{
	private static String baseURI;
	private static DefaultPrefixManager basePrefix;
	private static String individualURI;
	
	public static void processConceptRestrictionMappings(OWLEntitiesBundle owlEntitiesBundle)
	{
		individualURI = owlEntitiesBundle.getIndividualURI();
		baseURI = owlEntitiesBundle.getBaseURI();
		basePrefix = owlEntitiesBundle.getBasePrefix();
		OWLOntologyManager ontologyManager = owlEntitiesBundle.getOntologyManager();
		OWLDataFactory factory = ontologyManager.getOWLDataFactory();
		Reasoner mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		OWLOntology db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		DefaultPrefixManager basePrefix = owlEntitiesBundle.getBasePrefix();
		
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.properties");

		OWLClass continuantCls = owlEntitiesBundle.getContinuantCls();

		OWLObjectProperty min1Restriction = factory.getOWLObjectProperty(IRI.create(owlEntitiesBundle.getBaseURI()+propertiesManager.getString("min1Restriction")));;
		OWLObjectProperty max1Restriction = factory.getOWLObjectProperty(IRI.create(owlEntitiesBundle.getBaseURI()+propertiesManager.getString("max1Restriction")));;
		OWLObjectProperty exactly1Restriction = factory.getOWLObjectProperty(IRI.create(owlEntitiesBundle.getBaseURI()+propertiesManager.getString("exactly1Restriction")));
		OWLObjectProperty existsSomeRestriction = factory.getOWLObjectProperty(IRI.create(owlEntitiesBundle.getBaseURI()+propertiesManager.getString("existsSomeRestriction")));
		OWLObjectProperty hasRangeMapping = owlEntitiesBundle.getHasRangeMapping();
		OWLObjectProperty onlyFromRestriction = factory.getOWLObjectProperty(IRI.create(owlEntitiesBundle.getBaseURI()+propertiesManager.getString("onlyFromRestriction")));
		OWLObjectProperty subClassOfRestriction = factory.getOWLObjectProperty(IRI.create(owlEntitiesBundle.getBaseURI()+propertiesManager.getString("subClassOfRestriction")));

		OWLClass conceptMappingClass = factory.getOWLClass(propertiesManager.getString("entityConceptMapping"),basePrefix);
		NodeSet<OWLNamedIndividual> conceptIndividualsNodeSet = mappingReasoner.getInstances(conceptMappingClass,false);
		if (conceptIndividualsNodeSet != null)
		{
			//Set of named individuals
			Set<OWLNamedIndividual> entityConceptIndividuals = conceptIndividualsNodeSet.getFlattened();
			processMappings(entityConceptIndividuals, mappingReasoner, ontologyManager, db2OWLPrimitiveOntology, db2OWLPrimitiveOntology, factory, continuantCls, basePrefix, min1Restriction, max1Restriction, exactly1Restriction, existsSomeRestriction, hasRangeMapping, onlyFromRestriction, subClassOfRestriction);
		}

		conceptMappingClass = factory.getOWLClass(propertiesManager.getString("relationConceptMapping"),basePrefix);

		conceptIndividualsNodeSet = mappingReasoner.getInstances(conceptMappingClass,false);
		//Create the OWL classes corresponding to these individuals and add them into the output ontology.

		if (conceptIndividualsNodeSet != null)
		{
			//#Set of named individuals
			Set<OWLNamedIndividual> relationConceptIndividuals = conceptIndividualsNodeSet.getFlattened();
			processMappings(relationConceptIndividuals, mappingReasoner, ontologyManager, db2OWLPrimitiveOntology, db2OWLPrimitiveOntology, factory, continuantCls, basePrefix, min1Restriction, max1Restriction, exactly1Restriction, existsSomeRestriction, hasRangeMapping, onlyFromRestriction, subClassOfRestriction);
		}

	}

	private static void processMappings(Set<OWLNamedIndividual> conceptIndividuals, Reasoner mappingReasoner, OWLOntologyManager ontologyManager, OWLOntology db2OWLPrimitiveOntology, OWLOntology db2OWLComplexOntology, OWLDataFactory factory, OWLClass continuantCls, DefaultPrefixManager basePrefix, OWLObjectProperty min1Restriction, OWLObjectProperty max1Restriction, OWLObjectProperty exactly1Restriction, OWLObjectProperty existsSomeRestriction, OWLObjectProperty hasRangeMapping, OWLObjectProperty onlyFromRestriction, OWLObjectProperty subClassOfRestriction)
	{
		//#Min1 object property restriction.
		for (OWLNamedIndividual currentIndividual : conceptIndividuals)
		{
			String iriStr = currentIndividual.getIRI().toString();
			
			String currentIndividualName = iriStr.replace(individualURI, "");
			OWLClass owlClass = factory.getOWLClass(currentIndividualName,basePrefix);

			NodeSet<OWLNamedIndividual> min1NodeSet = mappingReasoner.getObjectPropertyValues(currentIndividual,min1Restriction);	
			//#Set of named individuals
			if (min1NodeSet != null)
			{
				Set<OWLNamedIndividual>min1Individuals = min1NodeSet.getFlattened();
				for (OWLNamedIndividual min1Obj : min1Individuals)
				{
					iriStr = min1Obj.getIRI().toString();
					String objectName = iriStr.replace(individualURI, "");
					String propertyType = getPropertyInPrimitive(objectName, db2OWLPrimitiveOntology, factory, ontologyManager);
					//#Check that the property is not a class.
					OWLClass possibleClass = factory.getOWLClass(objectName,basePrefix);
					if (db2OWLPrimitiveOntology.containsClassInSignature(possibleClass.getIRI(),true))
					{
						System.out.println("Warning: the minCardinality restriction of: "+ owlClass.getIRI().toQuotedString() + "could not be processed since " + min1Obj.getIRI().toQuotedString()+ " is mapped as an OWL class instead of an OWL object property.");
					}
					else
					{
						if (propertyType.equals("objectProperty"))
						{
							//#Get reference to the object prop
							OWLObjectProperty objectProp = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
							OWLObjectMinCardinality minCardinality = factory.getOWLObjectMinCardinality(1,objectProp); 
							OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,minCardinality);
							ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
							try {
								ontologyManager.saveOntology(db2OWLComplexOntology);
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
						}
						else
						{
							if (propertyType.equals("dataProperty"))
							{
								//#Get reference to the data prop
								OWLDataProperty dataProp = factory.getOWLDataProperty(getPropertyName(objectName) , basePrefix);
								OWLDataMinCardinality minCardinality = factory.getOWLDataMinCardinality(1,dataProp); 
								OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,minCardinality);
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

			//#Processing max 1 cardinality restriction.
			NodeSet<OWLNamedIndividual> max1NodeSet = mappingReasoner.getObjectPropertyValues(currentIndividual,max1Restriction);	
			//#Set of named individuals
			if (max1NodeSet != null)
			{
				Set<OWLNamedIndividual> max1Individuals=max1NodeSet.getFlattened();
				for (OWLNamedIndividual max1Obj : max1Individuals)
				{
					iriStr = max1Obj.getIRI().toString();
					String objectName = iriStr.replace(individualURI, "");

					String propertyType = getPropertyInPrimitive(objectName, db2OWLComplexOntology, factory, ontologyManager);
					//#Check that the property is not a class.
					OWLClass possibleClass = factory.getOWLClass(objectName,basePrefix);
					if (db2OWLPrimitiveOntology.containsClassInSignature(possibleClass.getIRI(),true))
					{
						System.out.println("Warning: the max1Cardinality restriction of: "+ owlClass.getIRI().toString() + "could not be processed since " + max1Obj.getIRI().toString()+ " is mapped as an OWL class instead of an OWL object property.");
					}
					else
					{
						if (propertyType.equals("objectProperty"))
						{
							//#Get reference to the object prop
							OWLObjectProperty objectProp = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
							OWLObjectMaxCardinality maxCardinality = factory.getOWLObjectMaxCardinality(1,objectProp); 
							OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,maxCardinality);
							ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));

							try {
								ontologyManager.saveOntology(db2OWLComplexOntology);
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
						}
						else
						{
							if (propertyType.equals("dataProperty"))
							{
								//#Get reference to the data prop
								OWLDataProperty dataProp = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
								OWLDataMaxCardinality maxCardinality = factory.getOWLDataMaxCardinality(1,dataProp); 
								OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,maxCardinality);
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

			//#Processing exactly 1 cardinality restriction.
			NodeSet<OWLNamedIndividual> exactly1NodeSet = mappingReasoner.getObjectPropertyValues(currentIndividual,exactly1Restriction);	
			//#Set of named individuals
			if (exactly1NodeSet != null)
			{
				Set<OWLNamedIndividual> exactly1Individuals = exactly1NodeSet.getFlattened();
				for(OWLNamedIndividual exactly1Obj : exactly1Individuals)
				{
					iriStr = exactly1Obj.getIRI().toString();
					String objectName = iriStr.replace(individualURI, "");
					String propertyType = getPropertyInPrimitive(objectName, db2OWLComplexOntology, factory, ontologyManager);
					//#Check that the property is not a class.
					OWLClass possibleClass = factory.getOWLClass(objectName,basePrefix);

					if (db2OWLPrimitiveOntology.containsClassInSignature(possibleClass.getIRI(),true))
					{
						System.out.println("=== Warning: the exactly 1 Cardinality restriction of: "+ owlClass.getIRI().toString() + " could not be processed since " + exactly1Obj.getIRI().toString()+ " is mapped as an OWL class instead of an OWL object property."); 
					}
					else
					{
						if (propertyType.equals("objectProperty"))
						{
							//#Get reference to the object prop
							OWLObjectProperty objectProp = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
							OWLObjectExactCardinality exactlyCardinality = factory.getOWLObjectExactCardinality(1,objectProp); 
							OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,exactlyCardinality);
							ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
							try {
								ontologyManager.saveOntology(db2OWLComplexOntology);
							} catch (OWLOntologyStorageException e) {
								e.printStackTrace();
							}
						}
						else
						{
							if (propertyType.equals("dataProperty"))
							{
								//#Get reference to the data prop
								OWLDataProperty dataProp = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
								OWLDataExactCardinality exactlyCardinality = factory.getOWLDataExactCardinality(1,dataProp); 
								OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,exactlyCardinality);
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
			//#Processing existsSome restriction.
			NodeSet<OWLNamedIndividual> someNodeSet = mappingReasoner.getObjectPropertyValues(currentIndividual,existsSomeRestriction);	
			//#Set of named individuals
			if (someNodeSet != null)
			{
				Set<OWLNamedIndividual> someIndividuals = someNodeSet.getFlattened();
				for (OWLNamedIndividual someObj : someIndividuals)
				{
					iriStr = someObj.getIRI().toString();
					String objectName = iriStr.replace(individualURI, "");

					String propertyType = getPropertyInPrimitive(objectName, db2OWLComplexOntology, factory, ontologyManager);
					//#Check that the property is not a class.
					OWLClass possibleClass = factory.getOWLClass(objectName,basePrefix);
					if (db2OWLPrimitiveOntology.containsClassInSignature(possibleClass.getIRI(),true))
					{
						System.out.println("Warning: the exists some restriction of: "+ owlClass.getIRI().toString() + " could not be processed since " + someObj.getIRI().toString()+ " is mapped as an OWL class instead of an OWL object property.");
					}
					else
					{

						if (propertyType.equals("objectProperty"))
						{

							//#Get reference to the object prop
							OWLObjectProperty objectProp = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
							NodeSet<OWLNamedIndividual> rangeAxiomsSet = mappingReasoner.getObjectPropertyValues(someObj,hasRangeMapping);

							Set<OWLNamedIndividual> rangeAxioms = rangeAxiomsSet.getFlattened(); 

							String rangeClassName = null;
							if (rangeAxioms != null && rangeAxioms.size() == 1)
							{
								for (OWLNamedIndividual rangeAxion : rangeAxioms)
								{
									String auxStr = rangeAxion.getIRI().toString();
									rangeClassName = auxStr.replace(individualURI, "");

									break;

								}	                            
							}

							if (rangeClassName!=null)
							{
								OWLObjectSomeValuesFrom someCardinality = factory.getOWLObjectSomeValuesFrom(objectProp,factory.getOWLClass(rangeClassName,basePrefix));
								OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,someCardinality);
								ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));

								try {
									ontologyManager.saveOntology(db2OWLComplexOntology);
								} catch (OWLOntologyStorageException e) {

									e.printStackTrace();
								}
							}
							else
							{
								System.out.println("Warning: The existential qualified property restriction could be processed properly, since the range is unknown for property "+ objectProp.getIRI().toString()); 
								OWLObjectSomeValuesFrom someCardinality = factory.getOWLObjectSomeValuesFrom(objectProp,factory.getOWLThing());
								OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,someCardinality);
								ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
								try {
									ontologyManager.saveOntology(db2OWLComplexOntology);
								} catch (OWLOntologyStorageException e) {

									e.printStackTrace();
								}
							}
						}
						else
						{
							if (propertyType.equals("dataProperty"))
							{

								//#Get reference to the data prop
								OWLDataProperty dataProp = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);

								OWLDataSomeValuesFrom someCardinality = factory.getOWLDataSomeValuesFrom(dataProp,factory.getTopDatatype());
								OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,someCardinality);

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

			//#Processing onlyFrom restriction.
			NodeSet<OWLNamedIndividual> onlyNodeSet = mappingReasoner.getObjectPropertyValues(currentIndividual, onlyFromRestriction);
			//#Set of named individuals
			if (onlyNodeSet != null)
			{
				Set<OWLNamedIndividual> onlyIndividuals = onlyNodeSet.getFlattened();
				for (OWLNamedIndividual onlyObj: onlyIndividuals)
				{
					iriStr = onlyObj.getIRI().toString();
					String objectName = iriStr.replace(individualURI, "");

					//#Check that the property is not a class.
					OWLClass possibleClass = factory.getOWLClass(objectName,basePrefix);
					if (db2OWLPrimitiveOntology.containsClassInSignature(possibleClass.getIRI(),true))
					{
						System.out.println("Warning: the all values from restriction of: "+ owlClass.getIRI().toString() + "could not be processed since " + onlyObj.getIRI().toString()+ " is mapped as an OWL class instead of an OWL object property.");
					}
					else
					{
						String propertyType = getPropertyInPrimitive(objectName, db2OWLComplexOntology, factory, ontologyManager);
						if (propertyType=="objectProperty")
						{
							//#Get reference to the object prop
							OWLObjectProperty objectProp = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);

							NodeSet<OWLNamedIndividual> rangeAxiomsSet = mappingReasoner.getObjectPropertyValues(onlyObj,hasRangeMapping);
							Set<OWLNamedIndividual> rangeAxioms = rangeAxiomsSet.getFlattened();

							String rangeClassName = null;
							if (rangeAxiomsSet != null)
							{
								if (rangeAxioms!=null && rangeAxioms.size()==1)
								{
									for (OWLNamedIndividual rangeAxion : rangeAxioms)
									{
										String auxStr = rangeAxion.getIRI().toString();
										rangeClassName = auxStr.replace(individualURI, "");

										break;

									}

								}
							}

							if (rangeClassName != null)
							{
								OWLObjectAllValuesFrom onlyCardinality = factory.getOWLObjectAllValuesFrom(objectProp,factory.getOWLClass(rangeClassName,basePrefix));
								OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,onlyCardinality);
								ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
								try {
									ontologyManager.saveOntology(db2OWLComplexOntology);
								} catch (OWLOntologyStorageException e) {
									e.printStackTrace();
								}
							}
							else
							{
								System.out.println("Warning: The universal qualified restriction could not be processed properly, since the range is unknown for property "+ objectProp.getIRI().toString()); 
								OWLObjectAllValuesFrom onlyCardinality = factory.getOWLObjectAllValuesFrom(objectProp,factory.getOWLThing());
								OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,onlyCardinality);

								ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
								try {
									ontologyManager.saveOntology(db2OWLComplexOntology);
								} catch (OWLOntologyStorageException e) {
									e.printStackTrace();
								}
							}
						}
						else
						{

							if (propertyType.equals("dataProperty"))
							{
								//#Get reference to the data prop
								OWLDataProperty dataProp = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);

								OWLDataAllValuesFrom onlyCardinality = factory.getOWLDataAllValuesFrom(dataProp,factory.getTopDatatype());
								OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,onlyCardinality);

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
	        
            //#Processing subClassOf restriction.
            NodeSet<OWLNamedIndividual> subClassNodeSet = mappingReasoner.getObjectPropertyValues(currentIndividual,subClassOfRestriction);
            //#Set of named individuals
            if (subClassNodeSet != null)
            {

            	Set<OWLNamedIndividual> subClassIndividuals = subClassNodeSet.getFlattened();
            	for (OWLNamedIndividual subClassObj : subClassIndividuals)
            	{
            		iriStr = subClassObj.getIRI().toString();
					String objectName = iriStr.replace(individualURI, "");
                    //#Check that the class is not a property.
					OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
					OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
					
                    if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true) || db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
                    {
                        System.out.println("Warning: The subclass relation between " + subClassObj.getIRI().toString() + "and" + owlClass.getIRI().toString() + "could not be processed since " + subClassObj.getIRI().toString() + " is mapped as an OWL property instead of an OWL class."); 
                    }
                    else
                    {
                    	OWLClass objectClass = factory.getOWLClass(objectName,basePrefix);
                    	OWLSubClassOfAxiom restrictionAxiom = factory.getOWLSubClassOfAxiom(owlClass,objectClass);
                    	
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



	private static String getPropertyName(String objectName)
	{
		return "has" + objectName;		
	}

	private static String getPropertyNameWPrefix(String objectName)
	{
		return baseURI + getPropertyName(objectName);
	}



	private static String getPropertyInPrimitive(String propertyName, OWLOntology db2OWLPrimitiveOntology, OWLDataFactory factory, OWLOntologyManager ontologyManager)
	{
		String objectPropName = getPropertyNameWPrefix(propertyName);
		String property;

		if (db2OWLPrimitiveOntology.containsObjectPropertyInSignature(IRI.create(objectPropName)))
		{

			property = "objectProperty";
		}
		else
		{
			if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(IRI.create(objectPropName)))
			{
				//TODO: The following line is not commented out in the original code
				//OWLDataProperty dataProp = factory.getOWLDataProperty(getPropertyName(propertyName),basePrefix);
				property = "dataProperty";
			}
			else
			{
				//#If no object property nor data property is found, a data property is created since it's the safest option, no range is required.
				OWLDataProperty dataProp = factory.getOWLDataProperty(getPropertyName(propertyName),basePrefix);

				OWLSubDataPropertyOfAxiom dataPropAxiom = factory.getOWLSubDataPropertyOfAxiom(dataProp,factory.getOWLTopDataProperty());
				ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,dataPropAxiom));

				try {
					ontologyManager.saveOntology(db2OWLPrimitiveOntology);
				} catch (OWLOntologyStorageException e) {

					e.printStackTrace();
				}

				property = "dataProperty";
			}
		}
		return property;

	}

}
