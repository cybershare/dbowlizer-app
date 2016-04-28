package com.cybershare.dbowlizer.generate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class ObjectPropertyRestrictionMappingHandler 
{
	
	private static DefaultPrefixManager basePrefix;
	private static String individualURI;
	public static void processObjectPropertyRestrictionMappings(OWLEntitiesBundle owlEntitiesBundle)
	{
		basePrefix = owlEntitiesBundle.getBasePrefix();
		OWLOntologyManager ontologyManager = owlEntitiesBundle.getOntologyManager();
		OWLDataFactory factory = ontologyManager.getOWLDataFactory();
		Reasoner mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		OWLOntology db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");
		String baseURI = owlEntitiesBundle.getBaseURI();
		
		OWLClass transitiveObjectPropertyMapping = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("transitiveObjectPropertyMapping")));
		
		OWLClass irreflexiveObjectPropertyMapping = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("irreflexiveObjectPropertyMapping")));
		OWLClass asymmetricObjectPropertyMapping = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("asymmetricObjectPropertyMapping")));
		OWLClass functionalObjectPropertyMapping = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("functionalObjectPropertyMapping")));
		OWLClass inverseFunctionalObjectPropertyMapping = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("inverseFunctionalObjectPropertyMapping")));
		
		OWLClass reflexiveObjectPropertyMapping = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("reflexiveObjectPropertyMapping")));
		
		OWLClass symmetricObjectPropertyMapping = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("symmetricObjectPropertyMapping")));
		OWLClass subPropertyOfIsPartOf = factory.getOWLClass(IRI.create(propertiesManager.getString("subPropertyOfIsPartOfMapping")));
		OWLObjectProperty hasDomainMapping = owlEntitiesBundle.getHasDomainMapping();
		OWLObjectProperty hasRangeMapping = owlEntitiesBundle.getHasRangeMapping();
		OWLObjectProperty hasInverseMapping = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("hasInversePropertyMapping")));
		OWLDataProperty hasDefaultValueMapping = owlEntitiesBundle.getHasDefaultValueMapping();
		OWLObjectProperty isPartOfProperty = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("isPartOfProperty")));
		
		OWLClass unaryObjectPropertyMapping = owlEntitiesBundle.getUnaryObjectPropertyMapping();
		
		individualURI = owlEntitiesBundle.getIndividualURI();
        HashSet<OWLNamedIndividual> datatypeIndividuals = owlEntitiesBundle.getDatatypeIndividuals();

        
		OWLClass binaryObjectPropertyMapping = factory.getOWLClass(propertiesManager.getString("binaryObjectPropertyMapping"),basePrefix);

		
        NodeSet<OWLNamedIndividual> objectPropertiesNodeSet = mappingReasoner.getInstances(unaryObjectPropertyMapping,false);
        if (objectPropertiesNodeSet != null)
        {
            Set<OWLNamedIndividual> unaryObjectPropertyIndividuals = objectPropertiesNodeSet.getFlattened();
            processObjectPropertyRestrictionMappings(mappingReasoner, ontologyManager, factory, db2OWLPrimitiveOntology, db2OWLPrimitiveOntology, unaryObjectPropertyIndividuals, 1, transitiveObjectPropertyMapping, asymmetricObjectPropertyMapping, functionalObjectPropertyMapping, inverseFunctionalObjectPropertyMapping, reflexiveObjectPropertyMapping, irreflexiveObjectPropertyMapping, symmetricObjectPropertyMapping, subPropertyOfIsPartOf, hasDomainMapping, hasRangeMapping, datatypeIndividuals, hasInverseMapping, hasDefaultValueMapping, isPartOfProperty);
        }
        
        objectPropertiesNodeSet = mappingReasoner.getInstances(binaryObjectPropertyMapping,false);
        if (objectPropertiesNodeSet != null)
        {
        	Set<OWLNamedIndividual> binaryObjectPropertyIndividuals = objectPropertiesNodeSet.getFlattened();
        	processObjectPropertyRestrictionMappings(mappingReasoner, ontologyManager, factory, db2OWLPrimitiveOntology, db2OWLPrimitiveOntology, binaryObjectPropertyIndividuals, 2, transitiveObjectPropertyMapping, asymmetricObjectPropertyMapping, functionalObjectPropertyMapping, inverseFunctionalObjectPropertyMapping, reflexiveObjectPropertyMapping, irreflexiveObjectPropertyMapping, symmetricObjectPropertyMapping, subPropertyOfIsPartOf, hasDomainMapping, hasRangeMapping, datatypeIndividuals, hasInverseMapping, hasDefaultValueMapping, isPartOfProperty);
        }
            
            

	}

	
	public static void processObjectPropertyRestrictionMappings(Reasoner mappingReasoner, OWLOntologyManager ontologyManager, OWLDataFactory factory, OWLOntology db2OWLPrimitiveOntology, OWLOntology db2OWLComplexOntology, Set<OWLNamedIndividual> objectPropertyIndividuals,int cardinality, OWLClass transitiveObjectPropertyMapping, OWLClass asymmetricObjectPropertyMapping, OWLClass functionalObjectPropertyMapping, OWLClass inverseFunctionalObjectPropertyMapping, OWLClass reflexiveObjectPropertyMapping, OWLClass irreflexiveObjectPropertyMapping, OWLClass symmetricObjectPropertyMapping, OWLClass subPropertyOfIsPartOf, OWLObjectProperty hasDomainMapping, OWLObjectProperty hasRangeMapping, HashSet<OWLNamedIndividual> datatypeIndividuals, OWLObjectProperty hasInverseMapping, OWLDataProperty hasDefaultValueMapping, OWLObjectProperty isPartOfProperty)
	{
	    
		NodeSet<OWLNamedIndividual> transitivePropertiesNodeSet = mappingReasoner.getInstances(transitiveObjectPropertyMapping,true);
		Set<OWLNamedIndividual> transitiveProperties = null;
		Set<OWLNamedIndividual> asymmetricProperties = null;
		Set<OWLNamedIndividual> functionalProperties = null;
		Set<OWLNamedIndividual> inverseFunctionalProperties = null;
		Set<OWLNamedIndividual> reflexiveProperties = null;
		Set<OWLNamedIndividual> irreflexiveProperties = null;
		Set<OWLNamedIndividual> symmetricProperties = null;
		Set<OWLNamedIndividual> subPropertyOfIsPartOfSet = null;
		
		if (transitivePropertiesNodeSet != null)
			transitiveProperties = transitivePropertiesNodeSet.getFlattened();
		  
	    NodeSet<OWLNamedIndividual> asymmetricPropertiesNodeSet = mappingReasoner.getInstances(asymmetricObjectPropertyMapping,true);
	    
	    
		if (asymmetricPropertiesNodeSet != null)
	    	asymmetricProperties = asymmetricPropertiesNodeSet.getFlattened(); 
	    
	    NodeSet<OWLNamedIndividual> functionalPropertiesNodeSet = mappingReasoner.getInstances(functionalObjectPropertyMapping,true);
	    if (functionalPropertiesNodeSet != null)
	    	functionalProperties=functionalPropertiesNodeSet.getFlattened();
	    
	    NodeSet<OWLNamedIndividual> inverseFunctionalPropertiesNodeSet = mappingReasoner.getInstances(inverseFunctionalObjectPropertyMapping,true);
	    if (inverseFunctionalPropertiesNodeSet != null)
	    	inverseFunctionalProperties=inverseFunctionalPropertiesNodeSet.getFlattened();
	    
	    NodeSet<OWLNamedIndividual> reflexivePropertiesNodeSet = mappingReasoner.getInstances(reflexiveObjectPropertyMapping,true);
	    if (inverseFunctionalPropertiesNodeSet != null)
	    	reflexiveProperties=reflexivePropertiesNodeSet.getFlattened(); 
	    
	    NodeSet<OWLNamedIndividual> irreflexivePropertiesNodeSet = mappingReasoner.getInstances(irreflexiveObjectPropertyMapping,true);
	    if (irreflexivePropertiesNodeSet != null)
	    	irreflexiveProperties = irreflexivePropertiesNodeSet.getFlattened(); 
	    
	    NodeSet<OWLNamedIndividual> symmetricPropertiesNodeSet = mappingReasoner.getInstances(symmetricObjectPropertyMapping,true);
	    
	    symmetricProperties = symmetricPropertiesNodeSet.getFlattened(); 
	    
	    NodeSet<OWLNamedIndividual> subPropertyOfIsPartOfNodeSet = mappingReasoner.getInstances(subPropertyOfIsPartOf,true);
	    
	    if (subPropertyOfIsPartOfNodeSet != null)
	    	subPropertyOfIsPartOfSet = subPropertyOfIsPartOfNodeSet.getFlattened();

	    for (OWLNamedIndividual currentIndividual : objectPropertyIndividuals)
	    {
	    	String iriStr = currentIndividual.getIRI().toString();
			String objectName = iriStr.replace(individualURI, "");
			OWLObjectProperty owlObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
	        //#Processing object property domain.
	        NodeSet<OWLNamedIndividual> domainNodeSet = mappingReasoner.getObjectPropertyValues(currentIndividual,hasDomainMapping);
	        //#Set of named individuals
	        if (domainNodeSet != null)
	        {
	            Set<OWLNamedIndividual> domainIndividuals = domainNodeSet.getFlattened();
	            for (OWLNamedIndividual domainObj : domainIndividuals)
	            {
	            	iriStr = domainObj.getIRI().toString();
	    			objectName = iriStr.replace(individualURI, "");

	                //#Check that the class is not a property.
	                OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
	                OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
	                
	                if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true) || db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
	                {

	                    System.out.println("Warning: domain of "+ owlObjectProperty.getIRI().toString()+ " could not be processed since " + domainObj.getIRI().toString()+ " is mapped as an OWL property instead of an OWL class."); 
	                }
	                else
	                {
	                	OWLClass objectClass = factory.getOWLClass(objectName,basePrefix);
	                	OWLObjectPropertyDomainAxiom restrictionAxiom = factory.getOWLObjectPropertyDomainAxiom(owlObjectProperty,objectClass);
	                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
	                    
	                    try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {

							e.printStackTrace();
						}
	                }   
	            }
	        }
	        //#Processing object property range.
	        NodeSet<OWLNamedIndividual> rangeNodeSet = mappingReasoner.getObjectPropertyValues(currentIndividual,hasRangeMapping);
	        //#Set of named individuals
	        if (rangeNodeSet!= null)
	        {
	            Set<OWLNamedIndividual> rangeIndividuals = rangeNodeSet.getFlattened();
	            for (OWLNamedIndividual rangeObj : rangeIndividuals)
	            {
	            	//TODO: Make sure this works
	                if (datatypeIndividuals.contains(rangeObj))
	                {
	                    System.out.println("Error: The range of an object property cannot be a data type. This property should be asserted as a datatype property. The range assertion was not created for property:"+owlObjectProperty.getIRI().toString()); 
	                }
	                else
	                {
	                	iriStr = rangeObj.getIRI().toString();
		    			objectName = iriStr.replace(individualURI, "");

	                    //#Check that the class is not a property.
		    			OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);

		    			OWLDataProperty possibleDataProperty=factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);

	                    if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true) || db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
	                    {
	                        System.out.println("Warning: range of "+ owlObjectProperty.getIRI().toString() + "could not be processed since " + rangeObj.getIRI().toString()+ " is mapped as an OWL property instead of an OWL class.");
	                    }
	                    else
	                    {
	                        OWLClass objectClass = factory.getOWLClass(objectName,basePrefix);
	                        OWLObjectPropertyRangeAxiom restrictionAxiom = factory.getOWLObjectPropertyRangeAxiom(owlObjectProperty,objectClass);
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
	        
	        //#Processing inverse property mapping.
	        NodeSet<OWLNamedIndividual> inverseNodeSet = mappingReasoner.getObjectPropertyValues(currentIndividual,hasInverseMapping);
	        //#Set of named individuals
	        if (inverseNodeSet != null)
	        {
	            Set<OWLNamedIndividual> inverseIndividuals = inverseNodeSet.getFlattened();
	            if (inverseIndividuals.size() > 0)
	            {
	            	for (OWLNamedIndividual inverseObj : inverseIndividuals)
	            	{
	            		iriStr = inverseObj.getIRI().toString();
		    			objectName = iriStr.replace(individualURI, "");

	                    //#Check that the property is not a class.
	                    OWLClass possibleClass = factory.getOWLClass(objectName,basePrefix);
	                    if (db2OWLPrimitiveOntology.containsClassInSignature(possibleClass.getIRI(),true))
	                    {
	                        System.out.println("Warning: the inverse property  of: "+ owlObjectProperty.getIRI().toString() + "could not be processed since " + inverseObj.getIRI().toString()+ " is mapped as an OWL class instead of an OWL object property."); 
	                    }
	                    else
	                    {
	                    	OWLObjectProperty inverseObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
	                        OWLInverseObjectPropertiesAxiom restrictionAxiom = factory.getOWLInverseObjectPropertiesAxiom(owlObjectProperty,inverseObjectProperty);
	                        ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
	                       
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
	                if (cardinality==2)
	                {
	                	System.out.println("Warning: There is no inverse property of the binary object property: "+ owlObjectProperty.getIRI().toString() + " which should be defined."); 
	                }


	            }
	        }
	        
	        //#Processing data property default value.
	        Set<OWLLiteral> defaultIndividuals = mappingReasoner.getDataPropertyValues(currentIndividual,hasDefaultValueMapping);
	        //#Set of named individuals
	        if (defaultIndividuals != null)
	        {
	            if  (defaultIndividuals.size()>1)
	            {
	                System.out.println("Warning: the default value of of "+ owlObjectProperty.getIRI().toString() + "could not be processed since the default value is not unique"); 
	            }
	            else
	            {
	            	for (OWLLiteral defaultObj : defaultIndividuals)
	            	{
	                    OWLLiteral commentValue = factory.getOWLLiteral("hasDefaultValue="+defaultObj.toString());
	                    OWLAnnotation commentAnnotation = factory.getOWLAnnotation(factory.getRDFSComment(),commentValue);
	                    OWLAnnotationAssertionAxiom annotationAxiom = factory.getOWLAnnotationAssertionAxiom(owlObjectProperty.getIRI(),commentAnnotation);
	                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,annotationAxiom));
	                    try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {
							e.printStackTrace();
						}
	            	}
	            }
	        }

	        //#Processing transitive properties
	        if (transitiveProperties != null)
	        {
	        	for (OWLNamedIndividual transitiveProperty : transitiveProperties)
	        	{
	        		//TODO: Make sure this workds
	                if (transitiveProperty.getIRI().equals( currentIndividual.getIRI()))
	                {
	                    OWLTransitiveObjectPropertyAxiom transitiveAxiom = factory.getOWLTransitiveObjectPropertyAxiom(owlObjectProperty);
	                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,transitiveAxiom));
	                    try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {
							e.printStackTrace();
						}
	                }
	        	}
	        }

	        //#Processing functional properties
	        if (functionalProperties != null)
	        {
	        	for (OWLNamedIndividual functionalProperty : functionalProperties)
	        	{
	                if (functionalProperty.getIRI().equals(currentIndividual.getIRI()))
	                {
	                    OWLFunctionalObjectPropertyAxiom functionalAxiom = factory.getOWLFunctionalObjectPropertyAxiom(owlObjectProperty);
	                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,functionalAxiom));
	                    try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {
							e.printStackTrace();
						}
	                }
	        	}
	        }

	        //#Processing inverse functional properties
	        if (inverseFunctionalProperties != null)
	        {
	        	for (OWLNamedIndividual inverseFunctionalProperty : inverseFunctionalProperties)
	        	{
	                if (inverseFunctionalProperty.getIRI().equals(currentIndividual.getIRI()))
	                {
	                    OWLInverseFunctionalObjectPropertyAxiom inverseFunctionalAxiom = factory.getOWLInverseFunctionalObjectPropertyAxiom(owlObjectProperty);
	                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,inverseFunctionalAxiom));
	                    try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {
							e.printStackTrace();
						}
	                }
	        	}
	        }
	        
	        //#Processing inverse functional properties
	        if (reflexiveProperties != null)
	        {
	        	for (OWLNamedIndividual reflexiveProperty : reflexiveProperties)
	        	{
	                if ( reflexiveProperty.getIRI().equals(currentIndividual.getIRI()))
	                {
	                	OWLReflexiveObjectPropertyAxiom reflexiveAxiom = factory.getOWLReflexiveObjectPropertyAxiom(owlObjectProperty);
	                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,reflexiveAxiom));
	                    try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {
							e.printStackTrace();
						}
	                }
	        	}
	        }
 
	        //#Processing irreflexive functional properties
	        if (irreflexiveProperties != null)
	        {
	        	for (OWLNamedIndividual irreflexiveProperty : irreflexiveProperties)
	        	{
	                if ( irreflexiveProperty.getIRI().equals(currentIndividual.getIRI()))
	                {
	                	OWLIrreflexiveObjectPropertyAxiom irreflexiveAxiom = factory.getOWLIrreflexiveObjectPropertyAxiom(owlObjectProperty);
	                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,irreflexiveAxiom));
	                    try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {
							e.printStackTrace();
						}
	                }
	        	}
	        }

	        //#Processing symmetric functional properties
	        if (symmetricProperties != null)
	        {
	        	for (OWLNamedIndividual symmetricProperty : symmetricProperties)
	        	{
	                if ( symmetricProperty.getIRI().equals(currentIndividual.getIRI()))
	                {
	                	OWLSymmetricObjectPropertyAxiom symmetricAxiom = factory.getOWLSymmetricObjectPropertyAxiom(owlObjectProperty);
	                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,symmetricAxiom));
	                    try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {
							e.printStackTrace();
						}
	                }
	        	}
	        }
        
	        //#Processing asymmetric functional properties
	        if (asymmetricProperties != null)
	        {
	        	for (OWLNamedIndividual asymmetricProperty : asymmetricProperties)
	        	{
	                if ( asymmetricProperty.getIRI().equals(currentIndividual.getIRI()))
	                {
	                	OWLAsymmetricObjectPropertyAxiom asymmetricAxiom = factory.getOWLAsymmetricObjectPropertyAxiom(owlObjectProperty);
	                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,asymmetricAxiom));
	                    try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {
							e.printStackTrace();
						}
	                }
	        	}
	        }

	        //#Processing sub object property of is part of
	        if (subPropertyOfIsPartOfSet != null)
	        {
	        	for (OWLNamedIndividual subProperty : subPropertyOfIsPartOfSet)
	        	{
	                if ( subProperty.getIRI().equals(currentIndividual.getIRI()))
	                {
	                	OWLSubObjectPropertyOfAxiom subPropertyAxiom = factory.getOWLSubObjectPropertyOfAxiom(owlObjectProperty,isPartOfProperty);
	                    ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,subPropertyAxiom));
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

	//TODO: Repeated method. Refactor
	private static String getPropertyName(String objectName)
	{
		return "has" + objectName;		
	}


}
