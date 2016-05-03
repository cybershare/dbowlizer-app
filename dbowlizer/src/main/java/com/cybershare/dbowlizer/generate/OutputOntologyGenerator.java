package com.cybershare.dbowlizer.generate;

import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import com.cybershare.dbowlizer.utils.Settings;

public class OutputOntologyGenerator 
{
	
	private OWLUtils bundle;
    private ModelProduct product;
    private Settings settings;
    
	public OutputOntologyGenerator(OWLUtils bundle, ModelProduct product, Settings settings){
		this.bundle = bundle;
		this.product = product;
		this.settings = settings;
	}

	public MappedEntitiesBundle createMappingOutputOntology() 
	{
		//#In this one we will first process the inferences of the methodology mapping file and then realize the ontology and leave it in memory. It will not call the following line.
		
		System.out.println("1. [Start] Creating initial OWL Entities and inferences...");
		OWLEntitiesBundle owlEntitiesBundle = new OWLEntitiesBundle(bundle.getOntology(), product, settings);
		MappedEntitiesBundle mappedEntitiesBundle = new MappedEntitiesBundle();
		System.out.println("1. [Done] Creating initial OWL Entities and inferences...\n");
		
		System.out.println("2. [Start] Processing Entity Concept Mappings...");
		EntityConceptMappingHandler.processEntityConceptMappings(owlEntitiesBundle, mappedEntitiesBundle);
		System.out.println("2. [Done] Processing Entity Concept Mappings...\n");

		System.out.println("3. [Start] Processing Relation Concept Mappings...");
		RelationConceptMappingHandler.processRelationConceptMappings(owlEntitiesBundle, mappedEntitiesBundle);
		System.out.println("3. [Done] Processing Relation Concept Mappings...\n");

		System.out.println("4. [Start] Processing Unary Object Property Mappings...");
		UnaryObjectPropertyMappingHandler.processUnaryObjectPropertyMappings(owlEntitiesBundle, mappedEntitiesBundle);
		System.out.println("4. [Done] Processing Unary Object Property Mappings...\n");

		System.out.println("5. [Start] Processing Binary Object Property Mappings...");		
		BinaryObjectPropertyMappingHandler.processBinaryObjectPropertyMappings(owlEntitiesBundle, mappedEntitiesBundle);
		System.out.println("5. [Done] Processing Binary Object Property Mappings...\n");

		System.out.println("6. [Start] Processing Datatype Property Mappings...");
		DatatypePropertyMappingHandler.processDatatypePropertyMappings(owlEntitiesBundle, mappedEntitiesBundle);
		System.out.println("6. [Done] Processing Datatype Property Mappings...\n");

		System.out.println("7. [Start] Processing Concept Restriction Mappings...");
		ConceptRestrictionMappingHandler.processConceptRestrictionMappings(owlEntitiesBundle);
		System.out.println("7. [Done] Processing Concept Restriction Mappings...\n");

		System.out.println("8. [Start] Processing Object Property Restriction Mappings...");		
		ObjectPropertyRestrictionMappingHandler.processObjectPropertyRestrictionMappings(owlEntitiesBundle);
		System.out.println("8. [Done] Processing Object Property Restriction Mappings...\n");

		System.out.println("9. [Start] Processing Data Property Restriction Mappings...");		
		DataPropertyRestrictionMappingHandler.processDataPropertyRestrictionMappings(owlEntitiesBundle);
		System.out.println("9. [Done] Processing Data Property Restriction Mappings...\n");

		System.out.println("10. [Start] Processing Virtual Concept Mappings...");
		VirtualConceptMappingHandler.processVirtualConceptMappings(owlEntitiesBundle);
		System.out.println("10. [Done] Processing Virtual Concept Mappings...\n");

		System.out.println("11. [Start] Processing Virtual Property Mappings...");
		VirtualPropertyMappingHandler.processVirtualPropertyMappings(owlEntitiesBundle);
		System.out.println("11. [Done] Processing Virtual Property Mappings...\n");

		System.out.println("12. [Start] Processing Aliases...");
		AliasesMappingHandler.processAliases(owlEntitiesBundle);
		System.out.println("12. [Done] Processing Aliases...\n");

		System.out.println("13. [Start] Processing Views With Joins...");
		ViewsWithJoinsMappingHandler.processViewsWithJoins(owlEntitiesBundle);
		System.out.println("13. [Done] Processing Views With Joins...\n");

		System.out.println("14. [Start] Processing Virtual Collection Mappings...");
		VirtualCollectionMappingHandler.processVirtualCollectionMappings(owlEntitiesBundle);
		System.out.println("14. [Done] Processing Virtual Collection Mappings...\n");

		System.out.println("15. [Start] 'Fixing' the final output ontology...");
		OutputOntologyFixer.fixFinalOntology(owlEntitiesBundle);
		System.out.println("15. [Done] 'Fixing' the final output ontology\n");
		
		System.out.println("----Output ontology generated!----");
		
		return mappedEntitiesBundle; 
	}



}
