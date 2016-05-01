package com.cybershare.dbowlizer.vocabulary;

import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */

public class RelationalToModel extends Vocabulary {
    
    private static final String NAMESPACE = "http://ontology.cybershare.utep.edu/resource/dbowl/";
    
    //OWL Classes OWLClass_ClassName
    private static final String OWLClass_DBSchema = NAMESPACE + "DBSchema";
    private static final String OWLClass_DBRelation = NAMESPACE + "DBRelation";
    private static final String OWLClass_DBView = NAMESPACE + "DBView";
    private static final String OWLClass_DBJoinView = NAMESPACE + "DBJoinView";
    private static final String OWLClass_DBJoinSelectionView = NAMESPACE + "DBJoinSelectionView";
    private static final String OWLClass_DBJoinProjectionView = NAMESPACE + "DBJoinProjectionView";
    private static final String OWLClass_DBJoinProjectionSelectionView = NAMESPACE + "DBJoinPojectionSelectionView";
    private static final String OWLClass_DBQuery = NAMESPACE + "DBQuery";
    private static final String OWLClass_DBQueryCondition = NAMESPACE + "DBQueryCondition";
    private static final String OWLClass_DBQueryJoin = NAMESPACE + "DBQueryJoin";
    private static final String OWLClass_DBAttributeAlias = NAMESPACE + "DBAttributeAlias";
    private static final String OWLClass_DBAttribute = NAMESPACE + "DBAttribute";
    private static final String OWLClass_DBAttributeDomain = NAMESPACE + "DBAttributeDomain";
    private static final String OWLClass_DBAttributeRestriction = NAMESPACE + "DBAttributeRestriction";
    private static final String OWLClass_DBCandidateKey = NAMESPACE + "DBCandidateKey";
    private static final String OWLClass_DBPrimaryKey = NAMESPACE + "DBPrimaryKey";
    private static final String OWLClass_DBForeignKey = NAMESPACE + "DBForeignKey";
    private static final String OWLClass_DBPrimaryForeignKey = NAMESPACE + "DBPrimaryForeignKey";
    private static final String OWLClass_DBKey = NAMESPACE + "DBKey";
    private static final String OWLClass_DBCompositeAttribute = NAMESPACE + "DBCompositeAttribute";
    private static final String OWLClass_DBNonKeyAttribute = NAMESPACE + "DBNonKeyAttribute";
    private static final String OWLClass_DBCandidateKeyAttribute = NAMESPACE + "DBCandidateKeyAttribute";
    private static final String OWLClass_DBPrimaryKeyAttribute = NAMESPACE + "DBPrimaryKeyAttribute";
    private static final String OWLClass_DBForeignKeyAttribute = NAMESPACE + "DBForeignKeyAttribute";
    private static final String OWLClass_DBIndependentPrimaryKey = NAMESPACE + "DBIndependentPrimaryKey";
    private static final String OWLClass_DBNonPrimaryForeignKeyAttribute = NAMESPACE + "DBNonPrimaryForeignKeyAttribute";
    private static final String OWLClass_DBSingleDependentPrimaryKey = NAMESPACE + "DBSingleDependentPrimaryKey";
    private static final String OWLClass_DBMultipleDependentPrimaryKey = NAMESPACE + "DBMultipleDependentPrimaryKey";
    private static final String OWLClass_DBPartiallyMultipleDependentPrimaryKey = NAMESPACE + "DBPartiallyMultipleDependentPrimaryKey";
    private static final String OWLClass_DBBinaryDependentPrimaryKey = NAMESPACE + "DBBinaryDependentPrimaryKey";
    private static final String OWLClass_DBPartiallyBinaryDependentPrimaryKey = NAMESPACE + "DBPartiallyBinaryDependentPrimaryKey";
    private static final String OWLClass_DBPartiallySingleDependentPrimaryKey = NAMESPACE + "DBPartiallySingleDependentPrimaryKey";
    private static final String OWLClass_DBNonPrimaryForeignKey = NAMESPACE + "DBNonPrimaryForeignKey";
    
    //Object Properties ObjectProperty_hasSomething
    private static final String ObjectProperty_hasPart = NAMESPACE + "hasPart";
    private static final String ObjectProperty_isPartOf = NAMESPACE + "isPartOf";
    private static final String ObjectProperty_isRelatedTo = NAMESPACE + "isRelatedTo";
    private static final String ObjectProperty_hasUpdateAction = NAMESPACE + "hasUpdateAction";
    private static final String ObjectProperty_hasDeleteAction = NAMESPACE + "hasDeleteAction";
    private static final String ObjectProperty_hasAttribute = NAMESPACE + "hasAttribute";
    private static final String ObjectProperty_references = NAMESPACE + "references";
    private static final String ObjectProperty_hasAttributeRestriction = NAMESPACE + "hasAttributeRestriction";
    private static final String ObjectProperty_hasAttributeDomain = NAMESPACE + "hasAttributeDomain";
    private static final String ObjectProperty_hasGroupByAttribute = NAMESPACE + "hasGroupByAttribute";
    private static final String ObjectProperty_isEquivalentTo = NAMESPACE + "isEquivalentTo";
    private static final String ObjectProperty_hasMember = NAMESPACE + "hasMember";
    private static final String ObjectProperty_hasCount = NAMESPACE + "hasCount";
    
    //Data Properties DataProperty_hasSomething
    private static final String DataProperty_hasDefaultValue = NAMESPACE + "hasDefaultValue"; 
    private static final String DataProperty_hasAllowedValue = NAMESPACE + "hasAllowedValue";
    private static final String DataProperty_hasAliasName = NAMESPACE + "hasAliasName";
    private static final String DataProperty_hasAggregateFunction = NAMESPACE + "hasAggregateFunction";
    private static final String DataProperty_hasOperator = NAMESPACE + "hasOperator";
    private static final String DataProperty_hasValue = NAMESPACE + "hasValue";

    public RelationalToModel(OWLUtils bundle) {
        super(bundle);
    }
    
    //IRI Getters
    public OWLClass getOWLClass_DBSchema(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBSchema));}
    public OWLClass getOWLClass_DBRelation(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBRelation));}
    public OWLClass getOWLClass_DBView(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBView));}
    public OWLClass getOWLClass_DBJoinView(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBJoinView));}
    public OWLClass getOWLClass_DBJoinSelectionView(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBJoinSelectionView));}
    public OWLClass getOWLClass_DBJoinProjectionView(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBJoinProjectionView));}
    public OWLClass getOWLClass_DBJoinProjectionSelectionView(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBJoinProjectionSelectionView));}
    public OWLClass getOWLClass_DBQuery(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBQuery));}
    public OWLClass getOWLClass_DBQueryCondition(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBQueryCondition));}
    public OWLClass getOWLClass_DBQueryJoin(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBQueryJoin));}
    public OWLClass getOWLClass_DBAttributeAlias(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBAttributeAlias));}
    public OWLClass getOWLClass_DBAttribute(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBAttribute));}
    public OWLClass getOWLClass_DBAttributeDomain(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBAttributeDomain));}
    public OWLClass getOWLClass_DBAttributeRestriction(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBAttributeRestriction));}
    public OWLClass getOWLClass_DBCandidateKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBCandidateKey));}
    public OWLClass getOWLClass_DBPrimaryKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBPrimaryKey));}
    public OWLClass getOWLClass_DBForeignKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBForeignKey));}
    public OWLClass getOWLClass_DBPrimaryForeignKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBPrimaryForeignKey));}
    public OWLClass getOWLClass_DBKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBKey));}
    public OWLClass getOWLClass_DBCompositeAttribute(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBCompositeAttribute));}
    public OWLClass getOWLClass_DBNonKeyAttribute(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBNonKeyAttribute));}
    public OWLClass getOWLClass_DBCandidateKeyAttribute(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBCandidateKeyAttribute));}
    public OWLClass getOWLClass_DBPrimaryKeyAttribute(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBPrimaryKeyAttribute));}
    public OWLClass getOWLClass_DBForeignKeyAttribute(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBForeignKeyAttribute));}
    public OWLClass getOWLClass_DBIndependentPrimaryKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBIndependentPrimaryKey));}
    public OWLClass getOWLClass_DBNonPrimaryForeignKeyAttribute(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBNonPrimaryForeignKeyAttribute));}
    public OWLClass getOWLClass_DBSingleDependentPrimaryKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBSingleDependentPrimaryKey));}
    public OWLClass getOWLClass_DBMultipleDependentPrimaryKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBMultipleDependentPrimaryKey));} 
    public OWLClass getOWLClass_DBPartiallyMultipleDependentPrimaryKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBPartiallyMultipleDependentPrimaryKey));}
    public OWLClass getOWLClass_DBBinaryDependentPrimaryKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBBinaryDependentPrimaryKey));}  
    public OWLClass getOWLClass_DBPartiallyBinaryDependentPrimaryKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBPartiallyBinaryDependentPrimaryKey));} 
    public OWLClass getOWLClass_DBPartiallySingleDependentPrimaryKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBPartiallySingleDependentPrimaryKey));}
    public OWLClass getOWLClass_DBNonPrimaryForeignKey(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DBNonPrimaryForeignKey));}
    
    public OWLObjectProperty getObjectProperty_hasPart(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasPart));}
    public OWLObjectProperty getObjectProperty_isPartOf(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_isPartOf));}
    public OWLObjectProperty getObjectProperty_isRelatedTo(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_isRelatedTo));}
    public OWLObjectProperty getObjectProperty_hasUpdateAction(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasUpdateAction));}
    public OWLObjectProperty getObjectProperty_hasDeleteAction(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasDeleteAction));}
    public OWLObjectProperty getObjectProperty_hasAttribute(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasAttribute));}
    public OWLObjectProperty getObjectProperty_references(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_references));}
    public OWLObjectProperty getObjectProperty_hasAttributeRestriction(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasAttributeRestriction));}
    public OWLObjectProperty getObjectProperty_hasAttributeDomain(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasAttributeDomain));}
    public OWLObjectProperty getObjectProperty_hasGroupByAttribute(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasGroupByAttribute));}
    public OWLObjectProperty getObjectProperty_isEquivalentTo(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_isEquivalentTo));}
    public OWLObjectProperty getObjectProperty_hasMember(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasMember));}
    public OWLObjectProperty getObjectProperty_hasCount(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasCount));}
    
    public OWLDataProperty getDataProperty_hasDefaultValue(){return this.bundle.getFactory().getOWLDataProperty(IRI.create(DataProperty_hasDefaultValue));}
    public OWLDataProperty getDataProperty_hasAllowedValue(){return this.bundle.getFactory().getOWLDataProperty(IRI.create(DataProperty_hasAllowedValue));}
    public OWLDataProperty getDataProperty_hasAliasName(){return this.bundle.getFactory().getOWLDataProperty(IRI.create(DataProperty_hasAliasName));}
    public OWLDataProperty getDataProperty_hasAggregateFunction(){return this.bundle.getFactory().getOWLDataProperty(IRI.create(DataProperty_hasAggregateFunction));}
    public OWLDataProperty getDataProperty_hasOperator(){return this.bundle.getFactory().getOWLDataProperty(IRI.create(DataProperty_hasOperator));}
    public OWLDataProperty getDataProperty_hasValue(){return this.bundle.getFactory().getOWLDataProperty(IRI.create(DataProperty_hasValue));}
    
    
    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

}
