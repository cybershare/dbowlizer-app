package com.cybershare.dbowlizer.generate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class AliasesMappingHandler 
{
	private static OWLOntologyManager ontologyManager;
	private static OWLDataFactory factory;
	private static Reasoner mappingReasoner;
	private static OWLClass virtualPropertyMappingClass;
	private static String individualURI;
	private static DefaultPrefixManager basePrefix;
	private static OWLOntology db2OWLPrimitiveOntology;
	private static OWLClass dbAttributeClass;
	private static OWLClass dbAttributeAliasClass;
	private static OWLClass dbQueryConditionClass;
	private static OWLClass dbQueryClass;
	private static OWLObjectProperty hasPartProperty;
	private static OWLDataProperty hasAliasNameProperty;
	private static OWLOntology db2OWLComplexOntology;
	
	public static void processAliases(OWLEntitiesBundle owlEntitiesBundle)
	{
		
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");
		ontologyManager = owlEntitiesBundle.getOntologyManager();
		
		factory = ontologyManager.getOWLDataFactory();
		hasAliasNameProperty = factory.getOWLDataProperty(IRI.create(owlEntitiesBundle.getBaseURI()+propertiesManager.getString("hasAliasNameProperty")));
		
		mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		virtualPropertyMappingClass = owlEntitiesBundle.getVirtualPropertyMappingClass();
		individualURI = owlEntitiesBundle.getIndividualURI();
		basePrefix = owlEntitiesBundle.getBasePrefix();
		db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		dbAttributeClass = owlEntitiesBundle.getDbAttributeClass();
		dbAttributeAliasClass = owlEntitiesBundle.getDbAttributeAliasClass();
		dbQueryConditionClass = owlEntitiesBundle.getDbQueryConditionClass();
		dbQueryClass = owlEntitiesBundle.getDbQueryClass();
		hasPartProperty = owlEntitiesBundle.getHasPartProperty();
		db2OWLComplexOntology = owlEntitiesBundle.getDb2OWLComplexOntology();
		
		NodeSet<OWLNamedIndividual> attributeNodeSet = mappingReasoner.getInstances(dbAttributeClass,false);
        NodeSet<OWLNamedIndividual> aliasAttributesNodeSet = mappingReasoner.getInstances(dbAttributeAliasClass,false);
        NodeSet<OWLNamedIndividual> conditionNodeSet = mappingReasoner.getInstances(dbQueryConditionClass,false);
        NodeSet<OWLNamedIndividual> virtualPropertyNodeSet = mappingReasoner.getInstances(virtualPropertyMappingClass,false);
        
        Set<OWLNamedIndividual> virtualProperty = new HashSet<OWLNamedIndividual>();
        if (virtualPropertyNodeSet != null)
        {
        	virtualProperty = virtualPropertyNodeSet.getFlattened();	
        }
         
        NodeSet<OWLNamedIndividual> queryNodeSet = mappingReasoner.getInstances(dbQueryClass,false);
        NodeSet<OWLNamedIndividual> hasPartQuerySet = null;
        ArrayList<OWLNamedIndividual> propertyQuery = null;
        
        ArrayList<OWLNamedIndividual> hasPartQuery = new ArrayList<OWLNamedIndividual>();
        ArrayList<OWLNamedIndividual> hasPartCondition = new ArrayList<OWLNamedIndividual>();
        ArrayList<OWLNamedIndividual> hasPartAttribute = new ArrayList<OWLNamedIndividual>();

        for (OWLNamedIndividual property : virtualProperty)
        {
            hasPartQuerySet = mappingReasoner.getObjectPropertyValues(property,hasPartProperty);
            //TODO: This method will be moved to a utils class
            propertyQuery = VirtualConceptMappingHandler.returnIntersection(hasPartQuerySet,queryNodeSet);
            hasPartQuery.addAll(propertyQuery);
        }
        
        for (OWLNamedIndividual query : hasPartQuery)
        {
            NodeSet<OWLNamedIndividual> hasPartConditionSet = mappingReasoner.getObjectPropertyValues(query,hasPartProperty);
            //TODO: This method will be moved to a utils class
            ArrayList<OWLNamedIndividual> condition = VirtualConceptMappingHandler.returnIntersection(hasPartConditionSet,conditionNodeSet);
            hasPartCondition.addAll(condition);
        }

        for (OWLNamedIndividual condition : hasPartCondition)
        {
        	NodeSet<OWLNamedIndividual> hasPartAttributeSet = mappingReasoner.getObjectPropertyValues(condition,hasPartProperty);
            //TODO: This method will be moved to a utils class

        	ArrayList<OWLNamedIndividual> attribute = VirtualConceptMappingHandler.returnIntersection(hasPartAttributeSet,attributeNodeSet);
        	hasPartAttribute.addAll(attribute);

        }
 
        ArrayList<OWLNamedIndividual> virtualPropertyAttributes = hasPartAttribute;
        Set<OWLNamedIndividual> aliasAttributesAll = null;
        if (aliasAttributesNodeSet != null)
        {	
        	aliasAttributesAll = aliasAttributesNodeSet.getFlattened();
        }


        if (aliasAttributesAll != null)
        {
        	for (OWLNamedIndividual aliasIndividual : aliasAttributesAll)
        	{

                NodeSet<OWLNamedIndividual> hasPartAttributeSet = mappingReasoner.getObjectPropertyValues(aliasIndividual,hasPartProperty);
                //TODO: The method will be moved to a Utils class
                hasPartAttribute = VirtualConceptMappingHandler.returnIntersection(hasPartAttributeSet,attributeNodeSet);
                
                if (hasPartAttribute.size()==1)
                {
                    OWLNamedIndividual attribute=hasPartAttribute.get(0);
                    if (!(iriIncludedInSet(attribute,virtualPropertyAttributes)))
                    {

                        String objectName = (attribute.getIRI().toString()).replace(individualURI,"");

                        //#check whether the attribute is a datatype property or an object property
                        OWLObjectProperty possibleObjectProperty = factory.getOWLObjectProperty(getPropertyName(objectName),basePrefix);
                        OWLDataProperty possibleDataProperty = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
                        
                        //#process object property aliases
                        if (db2OWLPrimitiveOntology.containsObjectPropertyInSignature(possibleObjectProperty.getIRI(),true))
                        {
                            Set<OWLLiteral> hasNameSet = mappingReasoner.getDataPropertyValues(aliasIndividual,hasAliasNameProperty);
                            OWLLiteral[] hasName = hasNameSet.toArray(new OWLLiteral[hasNameSet.size()]);
                            if (hasName.length != 1)
                            {
                                System.out.println("Warning, the alias :"+aliasIndividual.getIRI().toString()+" contains more than one alias name."); 
                            }
                            else
                            {
                                OWLLiteral aliasName = hasName[0];

                                //TODO: do
                                
                               
                                String columnText = aliasIndividual.getIRI().toString().replace(individualURI,"")+"_";
                                int columnTextIndex = findFirstMatchIndex(columnText, "_column_alias_\\d\\d*");
                                
                                if (columnTextIndex != -1)
                                	columnText = aliasIndividual.getIRI().toString().replace(individualURI,"").substring(0,columnTextIndex)+"_";

                                
                                OWLObjectProperty aliasObjectProperty;
                                
                                if (columnTextIndex != -1)
                                {
                                	aliasObjectProperty=factory.getOWLObjectProperty(getPropertyName(columnText+(aliasName.getLiteral().toString())),basePrefix);
                                }
                                else
                                {
                                    //#without the name of the view
                                    aliasObjectProperty = factory.getOWLObjectProperty(getPropertyName(aliasName.getLiteral().toString()),basePrefix);
                                }

                                OWLSubObjectPropertyOfAxiom aliasAxiom = factory.getOWLSubObjectPropertyOfAxiom(aliasObjectProperty,possibleObjectProperty);
                                ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,aliasAxiom));
                                try {
									ontologyManager.saveOntology(db2OWLComplexOntology);
								} catch (OWLOntologyStorageException e) {

									e.printStackTrace();
								}
                            }
                        }
                        
                        //#process data property aliases
                        if (db2OWLPrimitiveOntology.containsDataPropertyInSignature(possibleDataProperty.getIRI(),true))
                        {
                            Set<OWLLiteral> hasNameSet = mappingReasoner.getDataPropertyValues(aliasIndividual,hasAliasNameProperty);
                            OWLLiteral[] hasName = hasNameSet.toArray(new OWLLiteral[hasNameSet.size()]);
                            if (hasName.length!=1)
                            {
                                System.out.println("Warning, the alias :"+aliasIndividual.getIRI().toString()+" contains more than one alias name.");
                            }
                            else
                            {
                                OWLLiteral aliasName = hasName[0];
                                
                                //TODO: This
                                String columnText = aliasIndividual.getIRI().toString().replace(individualURI,"")+"_";
                                int columnTextIndex = findFirstMatchIndex(columnText, "_column_alias_\\d\\d*");
                                
                                if (columnTextIndex != -1)
                                	columnText = aliasIndividual.getIRI().toString().replace(individualURI,"").substring(0,columnTextIndex)+"_";

                                OWLDataProperty aliasDataProperty;
                                if (columnTextIndex != -1)
                                {
                                    aliasDataProperty = factory.getOWLDataProperty(getPropertyName(columnText+(aliasName.getLiteral().toString())),basePrefix);
                                }
                                else
                                {
                                    //#without the name of the view
                                    aliasDataProperty = factory.getOWLDataProperty(getPropertyName(aliasName.getLiteral().toString()),basePrefix);
                                }
//                                equivalentProperties=HashSet.new

                                OWLSubDataPropertyOfAxiom aliasAxiom = factory.getOWLSubDataPropertyOfAxiom(aliasDataProperty,possibleDataProperty);
                                ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,aliasAxiom));
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
                
    private static boolean iriIncludedInSet(OWLNamedIndividual individual, ArrayList<OWLNamedIndividual> individualArray)
    {

    	for (OWLNamedIndividual currentIndividual : individualArray)
    		if (currentIndividual.getIRI().equals(individual.getIRI()))
    				return true;
    	
    	return false;
    	
    }
    
    private static String getPropertyName(String objectName)
    {
    	return "has"+objectName;
    }

    
    private static int findFirstMatchIndex(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        // Check all occurrences
        while (matcher.find()) {
            return matcher.start();
            
        }
        
        return -1;
    }
}
