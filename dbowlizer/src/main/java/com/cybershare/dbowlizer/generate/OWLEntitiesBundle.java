package com.cybershare.dbowlizer.generate;

import com.cybershare.dbowlizer.build.ModelProduct;
import java.io.File;
import java.util.HashSet;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.cybershare.dbowlizer.reasoner.ReasonerManager;
import com.cybershare.dbowlizer.utils.Settings;

public class OWLEntitiesBundle 
{

	private OWLOntologyManager ontologyManager;
	
	private OWLDataFactory factory;
	
	private Reasoner mappingReasoner;;
	
	private String baseURI;
	private String individualURI;
	
	private DefaultPrefixManager basePrefix;
	
	private OWLOntology db2OWLPrimitiveOntology;
	private OWLOntology db2OWLComplexOntology;
 
	private OWLDataProperty hasValue; 
	private OWLDataProperty hasOperator; 
	private OWLDataProperty hasDefaultValueMapping;
	
	private OWLObjectProperty hasAttributeDomainObjectProperty; 
	private OWLObjectProperty subClassOfMapping; 
	private OWLObjectProperty hasPartProperty;
	private OWLObjectProperty hasDomainMapping;
	private OWLObjectProperty hasRangeMapping;
	
	private OWLClass unaryObjectPropertyMapping;
	private OWLClass virtualPropertyMappingClass; 
	private OWLClass dbQueryClass; 
	private OWLClass dbQueryConditionClass; 
	private OWLClass dbAttributeClass;
	private OWLClass dbAttributeAliasClass;
	private OWLClass continuantCls;
	
	private HashSet<OWLNamedIndividual> datatypeIndividuals;
	
	private OWLNamedIndividual db_string_individual;
	private OWLNamedIndividual db_integer_individual;
	private OWLNamedIndividual db_date_individual; 
	private OWLNamedIndividual db_float_individual; 
	private OWLNamedIndividual db_double_individual;
	

	private IRI xsd_string_IRI; 
	private IRI xsd_date_IRI;
	private IRI xsd_minInclusive_IRI;
	private IRI xsd_maxInclusive_IRI;
	private IRI xsd_pattern_IRI;
	private OWLOntology baseOntology;
	private ModelProduct product;
	private Settings settings;
        
	public OWLEntitiesBundle(OWLOntology baseOntology, ModelProduct product, Settings settings)
	{
		this.baseOntology = baseOntology;
        this.product = product;
        this.settings = settings;
		//#In this one we will first process the inferences of the methodology mapping file and then realize the ontology and leave it in memory. It will not call the following line.
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");

		createOntologyMappingWithMethodology(propertiesManager.getString("inferencesOntology"));
	}

	private void createOntologyMappingWithMethodology(String ontologyStr)  
	{
		try
		{
			ontologyManager = OWLManager.createOWLOntologyManager();
			factory = ontologyManager.getOWLDataFactory();
			//#Required ontology URIs are created.
			//#The base URI and individual URI can be used as prefixes in OWLAPI ver. 3
			ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");
			//ExternalPropertiesManager databasePropertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.database.properties");
	
			baseURI = propertiesManager.getString("baseURI") + "/";
			
			basePrefix = new DefaultPrefixManager(baseURI);
			System.out.println(baseURI);
            //TODO: get 0 is ugly
			String dbSchemaName = product.getSchemas().get(0).getSchemaName();
			individualURI = propertiesManager.getString("sourceURI") + dbSchemaName.trim().replace(" ",",") + ":";
			System.out.println(individualURI);
			//individualURI = propertiesManager.getString("sourceURI") + dbSchemaName.trim().replace(" ",",")+":";
			IRI db2OWLMappingPrimitiveLogicalURI = IRI.create(propertiesManager.getString("sourceURI")+dbSchemaName+("-mapped-by-")+propertiesManager.getString("methodology")+"-primitive.owl");
	                
                        
			IRI db2OWLMappingPrimitivePhysicalURI = IRI.create(settings.getOutputDirFile()+dbSchemaName+("-mapped-by-")+propertiesManager.getString("methodology")+"-primitive.owl");
	                settings.addOntologyName(dbSchemaName+("-mapped-by-")+propertiesManager.getString("methodology")+"-primitive.owl");
			SimpleIRIMapper outputPrimitiveMapper = new SimpleIRIMapper(db2OWLMappingPrimitiveLogicalURI,db2OWLMappingPrimitivePhysicalURI);
	
			ontologyManager.addIRIMapper(outputPrimitiveMapper);	
			IRI db2OWLMappingComplexLogicalURI = IRI.create(propertiesManager.getString("sourceURI")+dbSchemaName+("-mapped-by-")+propertiesManager.getString("methodology")+"-complex.owl");
			IRI db2OWLMappingComplexPhysicalURI = IRI.create(settings.getOutputDirFile()+dbSchemaName+("-mapped-by-")+propertiesManager.getString("methodology")+"-complex.owl");
			settings.addOntologyName(dbSchemaName+("-mapped-by-")+propertiesManager.getString("methodology")+"-complex.owl");
                        SimpleIRIMapper outputComplexMapper = new SimpleIRIMapper(db2OWLMappingComplexLogicalURI,db2OWLMappingComplexPhysicalURI);
			ontologyManager.addIRIMapper(outputComplexMapper);
	
			IRI db2OWLMappedIndividualsLogicalURI = IRI.create(propertiesManager.getString("sourceURI")+dbSchemaName+("-mapped-by-")+propertiesManager.getString("methodology")+"-individuals.owl");
			IRI db2OWLMappedIndividualsPhysicalURI = IRI.create(settings.getOutputDirFile()+dbSchemaName+("-mapped-by-")+propertiesManager.getString("methodology")+"-individuals.owl");
                        SimpleIRIMapper outputIndividualsMapper = new SimpleIRIMapper(db2OWLMappedIndividualsLogicalURI,db2OWLMappedIndividualsPhysicalURI);
			ontologyManager.addIRIMapper(outputIndividualsMapper);
	
			//# Interacting with OWL API to create the new ontology from the 	dbSchema received
			//#Ontology is created based on the IRI, throws an OWLOntologyCreationException
			db2OWLPrimitiveOntology = ontologyManager.createOntology(db2OWLMappingPrimitiveLogicalURI);
			db2OWLComplexOntology = ontologyManager.createOntology(db2OWLMappingComplexLogicalURI);
			//#Saving ontology in physical location
			ontologyManager.saveOntology(db2OWLPrimitiveOntology);
			//#Complex ontology will import primitive ontology.
			OWLImportsDeclaration importDeclaration = factory.getOWLImportsDeclaration(db2OWLMappingPrimitivePhysicalURI);
	
			ontologyManager.applyChange(new AddImport(db2OWLComplexOntology,importDeclaration));
			//#Saving ontology in physical location
			ontologyManager.saveOntology(db2OWLComplexOntology);
	
			//#This method will be called only when the inferences ontology has to be loaded and reasoned about.
			createClassesAndPropertiesURIs();
			readMethodologyMappingInstances();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void readMethodologyMappingInstances() throws OWLOntologyCreationException {
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");

		//#This method will be changed when the inferences ontology is already in memory. It is not necessary.
		IRI mappingOntologyWebURI = IRI.create(propertiesManager.getString("mappingOntologyWebURI"));
		//#Testing the reading of the ontology plus inferences

		//#This step can be replaced with the actual reading of the ontology methodology, the db individuals and using OWL and SWRL reasoners.
		String filePath = propertiesManager.getString("mappingOntologyFile");
		//#Testing the reading of the ontology and realizing it.
		
		File mapInferOntologyFile = new File(filePath);
		File mapInferOntologyJavaFile= new File(mapInferOntologyFile.getPath());
		IRI dbInferencesPhysicalURI = IRI.create(mapInferOntologyJavaFile);
		
		//#Code to load an ontology from a file	#localInferOntology=ontologyManager.loadOntologyFromOntologyDocument(mapInferOntologyJavaFile)
		SimpleIRIMapper inferencesMapper = new SimpleIRIMapper(mappingOntologyWebURI,dbInferencesPhysicalURI);

		ontologyManager.addIRIMapper(inferencesMapper);
		IRI mapInferLogicalIRI = mappingOntologyWebURI;
 
		OWLOntology dbInferencesOntology = ontologyManager.loadOntology(mapInferLogicalIRI);		

	    
	    ontologyManager.addAxioms(dbInferencesOntology, baseOntology.getAxioms());
	    
		Configuration conf = new Configuration();
		conf.ignoreUnsupportedDatatypes=true; //by default is set to 'false'
		mappingReasoner = new Reasoner(conf, dbInferencesOntology);
		
		ReasonerManager.addAxiomsThroughReasoner(ontologyManager, dbInferencesOntology); 
	}
	
	private void createClassesAndPropertiesURIs() {
		ExternalPropertiesManager propertiesManager = ExternalPropertiesManager.getInstance("/schema2owl.config.original.properties");
		//#Start of the creation of properties and classes necessaries for the ontology.
		hasValue = factory.getOWLDataProperty(IRI.create(baseURI+propertiesManager.getString("hasValueProperty")));
		hasOperator = factory.getOWLDataProperty(IRI.create(baseURI+propertiesManager.getString("hasOperatorProperty")));
		hasAttributeDomainObjectProperty = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("hasAttributeDomainProperty")));
		unaryObjectPropertyMapping = factory.getOWLClass(propertiesManager.getString("unaryObjectPropertyMapping"),basePrefix);
		
		subClassOfMapping = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("subClassOfRestriction")));;
		virtualPropertyMappingClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("virtualPropertyMapping")));
		dbQueryClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("dbQueryClass")));
		dbQueryConditionClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("dbQueryConditionClass")));
		dbAttributeClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("dbAttributeClass")));
		dbAttributeAliasClass = factory.getOWLClass(IRI.create(baseURI+propertiesManager.getString("dbAttributeAliasClass")));
		hasPartProperty = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("hasPartProperty")));
		hasDomainMapping = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("hasDomainMapping")));
		hasRangeMapping = factory.getOWLObjectProperty(IRI.create(baseURI+propertiesManager.getString("hasRangeMapping")));

		hasDefaultValueMapping = factory.getOWLDataProperty(IRI.create(baseURI+propertiesManager.getString("hasDefaultValueMapping")));

		datatypeIndividuals = new HashSet<OWLNamedIndividual>();
		db_string_individual = factory.getOWLNamedIndividual(IRI.create(baseURI+propertiesManager.getString("db_string")));
		datatypeIndividuals.add(db_string_individual);
		db_integer_individual = factory.getOWLNamedIndividual(IRI.create(baseURI+propertiesManager.getString("db_integer")));
		db_double_individual = factory.getOWLNamedIndividual(IRI.create(baseURI+propertiesManager.getString("db_double")));
		datatypeIndividuals.add(db_integer_individual);
		db_date_individual = factory.getOWLNamedIndividual(IRI.create(baseURI+propertiesManager.getString("db_date")));
		datatypeIndividuals.add(db_date_individual);
		db_float_individual = factory.getOWLNamedIndividual(IRI.create(baseURI+propertiesManager.getString("db_float")));
		datatypeIndividuals.add(db_float_individual);

		xsd_string_IRI = IRI.create("http://www.w3.org/2001/XMLSchema#string");
		xsd_date_IRI=IRI.create("http://www.w3.org/2001/XMLSchema#date");
		xsd_minInclusive_IRI=IRI.create("http://www.w3.org/2001/XMLSchema#minInclusive");
		xsd_maxInclusive_IRI=IRI.create("http://www.w3.org/2001/XMLSchema#maxInclusive");
		xsd_pattern_IRI=IRI.create("http://www.w3.org/2001/XMLSchema#pattern");
		continuantCls=factory.getOWLClass(IRI.create(propertiesManager.getString("continuantCls")));

		OWLLiteral continuantComment = factory.getOWLLiteral("An informational entity is an entity that is an abstraction of, representation for, description of, or information about some entity (whether a type or instance), and only has informational entities as its parts.");

		OWLAnnotation commentAnnotation = factory.getOWLAnnotation(factory.getRDFSComment(),continuantComment);
		OWLAnnotationAssertionAxiom annotationAxiom = factory.getOWLAnnotationAssertionAxiom(continuantCls.getIRI(),commentAnnotation);
				
		ontologyManager.applyChange(new AddAxiom(db2OWLPrimitiveOntology,annotationAxiom));

	}

	public OWLOntologyManager getOntologyManager() {
		return ontologyManager;
	}

	public OWLDataFactory getFactory() {
		return factory;
	}

	public Reasoner getMappingReasoner() {
		return mappingReasoner;
	}

	public String getBaseURI() {
		return baseURI;
	}

	public String getIndividualURI() {
		return individualURI;
	}

	public DefaultPrefixManager getBasePrefix() {
		return basePrefix;
	}

	public OWLOntology getDb2OWLPrimitiveOntology() {
		return db2OWLPrimitiveOntology;
	}

	public OWLOntology getDb2OWLComplexOntology() {
		return db2OWLComplexOntology;
	}

	public OWLDataProperty getHasValue() {
		return hasValue;
	}

	public OWLDataProperty getHasOperator() {
		return hasOperator;
	}

	public OWLDataProperty getHasDefaultValueMapping() {
		return hasDefaultValueMapping;
	}

	public OWLObjectProperty getHasAttributeDomainObjectProperty() {
		return hasAttributeDomainObjectProperty;
	}

	public OWLObjectProperty getSubClassOfMapping() {
		return subClassOfMapping;
	}

	public OWLObjectProperty getHasPartProperty() {
		return hasPartProperty;
	}

	public OWLObjectProperty getHasDomainMapping() {
		return hasDomainMapping;
	}

	public OWLObjectProperty getHasRangeMapping() {
		return hasRangeMapping;
	}

	public OWLClass getUnaryObjectPropertyMapping() {
		return unaryObjectPropertyMapping;
	}

	public OWLClass getVirtualPropertyMappingClass() {
		return virtualPropertyMappingClass;
	}

	public OWLClass getDbQueryClass() {
		return dbQueryClass;
	}

	public OWLClass getDbQueryConditionClass() {
		return dbQueryConditionClass;
	}

	public OWLClass getDbAttributeClass() {
		return dbAttributeClass;
	}

	public OWLClass getDbAttributeAliasClass() {
		return dbAttributeAliasClass;
	}

	public OWLClass getContinuantCls() {
		return continuantCls;
	}

	public HashSet<OWLNamedIndividual> getDatatypeIndividuals() {
		return datatypeIndividuals;
	}

	public OWLNamedIndividual getDb_string_individual() {
		return db_string_individual;
	}

	public OWLNamedIndividual getDb_integer_individual() {
		return db_integer_individual;
	}

	public OWLNamedIndividual getDb_date_individual() {
		return db_date_individual;
	}

	public OWLNamedIndividual getDb_float_individual() {
		return db_float_individual;
	}

	public OWLNamedIndividual getDb_double_individual() {
		return db_double_individual;
	}

	public IRI getXsd_string_IRI() {
		return xsd_string_IRI;
	}

	public IRI getXsd_date_IRI() {
		return xsd_date_IRI;
	}

	public IRI getXsd_minInclusive_IRI() {
		return xsd_minInclusive_IRI;
	}

	public IRI getXsd_maxInclusive_IRI() {
		return xsd_maxInclusive_IRI;
	}

	public IRI getXsd_pattern_IRI() {
		return xsd_pattern_IRI;
	}

}
