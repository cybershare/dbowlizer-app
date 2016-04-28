package com.cybershare.dbowlizer.generate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class DataPropertyRestrictionMappingHandler 
{

	public static void processDataPropertyRestrictionMappings(OWLEntitiesBundle owlEntitiesBundle)
	{
		OWLOntologyManager ontologyManager = owlEntitiesBundle.getOntologyManager();
		OWLDataFactory factory = ontologyManager.getOWLDataFactory();
		Reasoner mappingReasoner = owlEntitiesBundle.getMappingReasoner();
		OWLOntology db2OWLPrimitiveOntology = owlEntitiesBundle.getDb2OWLPrimitiveOntology();
		DefaultPrefixManager basePrefix = owlEntitiesBundle.getBasePrefix();
		
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");
		
		OWLClass datatypePropertyMapping = factory.getOWLClass(propertiesManager.getString("datatypePropertyMapping"),basePrefix);
		HashSet<OWLLiteral> literalSet = new HashSet<OWLLiteral>();
		
		OWLObjectProperty hasDomainMapping = owlEntitiesBundle.getHasDomainMapping();
		OWLDataProperty hasAllowedValue = factory.getOWLDataProperty(IRI.create(owlEntitiesBundle.getBaseURI()+propertiesManager.getString("hasAllowedValueProperty")));
		OWLObjectProperty hasRangeMapping = owlEntitiesBundle.getHasRangeMapping();
		
		OWLNamedIndividual db_string_individual = owlEntitiesBundle.getDb_string_individual();
		IRI xsd_string_IRI = owlEntitiesBundle.getXsd_string_IRI();
		OWLNamedIndividual db_integer_individual = owlEntitiesBundle.getDb_integer_individual();
		OWLNamedIndividual db_float_individual = owlEntitiesBundle.getDb_float_individual();
		OWLNamedIndividual db_date_individual = owlEntitiesBundle.getDb_date_individual();
		IRI xsd_date_IRI = owlEntitiesBundle.getXsd_date_IRI();
		OWLDataProperty hasDefaultValueMapping = owlEntitiesBundle.getHasDefaultValueMapping();
		OWLClass functionalDataPropertyMapping = factory.getOWLClass(IRI.create(owlEntitiesBundle.getBaseURI()+propertiesManager.getString("functionalDataPropertyMapping")));

		OWLOntology db2OWLComplexOntology = owlEntitiesBundle.getDb2OWLComplexOntology();

		String individualURI = owlEntitiesBundle.getIndividualURI();
		
		NodeSet<OWLNamedIndividual> dataPropertiesNodeSet = mappingReasoner.getInstances(datatypePropertyMapping,false);
		if (dataPropertiesNodeSet == null)
			return;

		Set<OWLNamedIndividual> dataPropertyIndividuals = dataPropertiesNodeSet.getFlattened();

		//VL#To do: Process the hasDefaultValue property. It may be a comment.


		//#Querying the reasoner to retrieve instances of the enumeration range property
		Set<OWLNamedIndividual> propertiesWithEnnumeratedRange = null;
		OWLClass ennumerationRangeMapping = factory.getOWLClass(propertiesManager.getString("ennumerationRangeMapping"),basePrefix);
		NodeSet<OWLNamedIndividual> dataPropertiesEnnumerationRangeSet = mappingReasoner.getInstances(ennumerationRangeMapping,false);
		if (dataPropertiesEnnumerationRangeSet != null)
		{
			propertiesWithEnnumeratedRange = dataPropertiesEnnumerationRangeSet.getFlattened();
		}

		for (OWLNamedIndividual currentIndividual : dataPropertyIndividuals)
		{
			String iriStr = currentIndividual.getIRI().toString();
			String objectName = iriStr.replace(individualURI, "");
			OWLDataProperty owlDataProperty = factory.getOWLDataProperty(getPropertyName(objectName),basePrefix);
			//#Processing data property domain.
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
						System.out.println("Warning: domain of "+ owlDataProperty.getIRI().toString() + " could not be processed since " + domainObj.getIRI().toString()+ " is mapped as an OWL property instead of an OWL class."); 

					}
					else
					{
						OWLClass objectClass = factory.getOWLClass(objectName,basePrefix);
						OWLDataPropertyDomainAxiom restrictionAxiom = factory.getOWLDataPropertyDomainAxiom(owlDataProperty,objectClass);
						ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));
						try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {

							e.printStackTrace();
						}
					}
				}
			}

			//#Processing data property range.
			//#First it is checked that there is no restricted range.
			//#Checking if the property does not have an customed ennumerated range.
			if (propertiesWithEnnumeratedRange.contains(currentIndividual))
			{
				Set<OWLLiteral> allowedValues = mappingReasoner.getDataPropertyValues(currentIndividual,hasAllowedValue);
				for (OWLLiteral currentValue: allowedValues)
				{
					if (!currentValue.toString().contains("internal:anonymous-constants"))
					{
						literalSet.add(currentValue);
					}
				}
				OWLDataOneOf oneOfRange = factory.getOWLDataOneOf(literalSet);
				OWLDataPropertyRangeAxiom rangeOneOfAxiom = factory.getOWLDataPropertyRangeAxiom(owlDataProperty,oneOfRange);
				//#The range axiom is applied
				if (rangeOneOfAxiom != null)
				{
					ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,rangeOneOfAxiom));
					try {
						ontologyManager.saveOntology(db2OWLComplexOntology);
					} catch (OWLOntologyStorageException e) {
						e.printStackTrace();
					}
				}
			}
			else
			{
				NodeSet<OWLNamedIndividual> rangeNodeSet = mappingReasoner.getObjectPropertyValues(currentIndividual,hasRangeMapping);
				//#Set of named individuals
				if (rangeNodeSet != null)
				{
					Set<OWLNamedIndividual> rangeIndividuals = rangeNodeSet.getFlattened();
					for (OWLNamedIndividual rangeObj : rangeIndividuals)
					{
						OWLDataPropertyRangeAxiom restrictionAxiom = null;
						//TODO: Fix this
						//if (rangeObj.getIRI().equals(db_string_individual.getIRI()))
						if (rangeObj.getIRI().toString().contains("db_string"))
						{
							restrictionAxiom = factory.getOWLDataPropertyRangeAxiom(owlDataProperty,factory.getOWLDatatype(xsd_string_IRI));
						}
						else
						{
							//if (rangeObj.getIRI().equals(db_integer_individual.getIRI()))
							if (rangeObj.getIRI().toString().contains("db_integer"))
							{
								restrictionAxiom = factory.getOWLDataPropertyRangeAxiom(owlDataProperty,factory.getIntegerOWLDatatype());
							}
							else
							{
								//if (rangeObj.getIRI().equals(db_float_individual.getIRI()))
								if (rangeObj.getIRI().toString().contains("db_double"))
								{
									restrictionAxiom = factory.getOWLDataPropertyRangeAxiom(owlDataProperty,factory.getDoubleOWLDatatype());

								}
								else
								{

									//if (rangeObj.getIRI().equals(db_date_individual.getIRI()))
									if (rangeObj.getIRI().toString().contains("db_date"))
									{
										restrictionAxiom = factory.getOWLDataPropertyRangeAxiom(owlDataProperty,factory.getOWLDatatype(xsd_date_IRI));
									}
								}
							}
						}

						//#The range axiom is applied
						if (restrictionAxiom != null)
						{
							ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,restrictionAxiom));

							try {
								ontologyManager.saveOntology(db2OWLComplexOntology);
							} catch (OWLOntologyStorageException e) {

								e.printStackTrace();
							}
						}
						else
						{
							System.out.println("Warning: range of "+ owlDataProperty.getIRI().toString() + "could not be processed since " + rangeObj.getIRI().toString()+ " is not a valid data type."); 
						}
					}
				}
			}

			//#Processing data property default value.
			Set<OWLLiteral> defaultIndividuals = mappingReasoner.getDataPropertyValues(currentIndividual,hasDefaultValueMapping);
			//#Set of named individuals
			if (defaultIndividuals != null)
			{
				if  (defaultIndividuals.size() > 1)
				{
					System.out.println("Warning: the default value of of "+ owlDataProperty.getIRI().toString() + "could not be processed since the default value is not unique"); 
				}
				else
				{
					for (OWLLiteral defaultObj : defaultIndividuals)
					{
						OWLLiteral commentValue = factory.getOWLLiteral("hasDefaultValue="+defaultObj.toString());
						OWLAnnotation commentAnnotation = factory.getOWLAnnotation(factory.getRDFSComment(),commentValue);
						OWLAnnotationAssertionAxiom annotationAxiom = factory.getOWLAnnotationAssertionAxiom(owlDataProperty.getIRI(),commentAnnotation);
						ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,annotationAxiom));
						try {
							ontologyManager.saveOntology(db2OWLComplexOntology);
						} catch (OWLOntologyStorageException e) {
							e.printStackTrace();
						}
					}
				}            
			}

			//#Processing functional properties
			NodeSet<OWLNamedIndividual> functionalPropertiesNodeSet = mappingReasoner.getInstances(functionalDataPropertyMapping,true);

			if (functionalPropertiesNodeSet != null)
			{
				Set<OWLNamedIndividual> functionalProperties = functionalPropertiesNodeSet.getFlattened();

				if (functionalProperties != null)
				{
					for (OWLNamedIndividual functionalProperty : functionalProperties)
					{
             
						if (functionalProperty.getIRI().equals(currentIndividual.getIRI()))
						{

							OWLFunctionalDataPropertyAxiom functionalAxiom = factory.getOWLFunctionalDataPropertyAxiom(owlDataProperty);
							ontologyManager.applyChange(new AddAxiom(db2OWLComplexOntology,functionalAxiom));
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



	//TODO: Repeated method. Refactor
	private static String getPropertyName(String objectName)
	{
		return "has" + objectName;		
	}


}
