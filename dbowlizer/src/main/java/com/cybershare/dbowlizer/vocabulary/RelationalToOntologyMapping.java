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

package com.cybershare.dbowlizer.vocabulary;

import com.cybershare.dbowlizer.ontology.OWLUtils;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class RelationalToOntologyMapping extends Vocabulary {

    private static final String NAMESPACE = "http://ontology.cybershare.utep.edu/resource/dbowl/";
    
    //OWL Classes
    private static final String OWLClass_EntityConceptMapping = NAMESPACE + "EntityConceptMapping";
    private static final String OWLClass_VirtualConceptMapping = NAMESPACE + "VirtualConceptMapping";
    private static final String OWLClass_VirtualPropertyMapping = NAMESPACE + "VirtualPropertyMapping";
    private static final String OWLClass_VirtualCollectionMapping = NAMESPACE + "VirtualCollectionMapping";
    private static final String OWLClass_RelationConceptMapping = NAMESPACE + "RelationConceptMapping";
    private static final String OWLClass_UnaryObjectPropertyMapping = NAMESPACE + "UnaryObjectPropertyMapping";
    private static final String OWLClass_BinaryObjectPropertyMapping = NAMESPACE + "BinaryObjectPropertyMapping";
    private static final String OWLClass_DatatypePropertyMapping = NAMESPACE + "DatatypePropertyMapping";
    private static final String OWLClass_TransitiveObjectPropertyMapping = NAMESPACE + "TransitiveObjectPropertyMapping";
    private static final String OWLClass_FunctionalObjectPropertyMapping = NAMESPACE + "FunctionalObjectPropertyMapping";
    private static final String OWLClass_EnnumerationPropertyRangeMapping = NAMESPACE + "EnnumerationPropertyRangeMapping";
    private static final String OWLClass_SymmetricObjectPropertyMapping = NAMESPACE + "SymmetricObjectPropertyMapping";
    private static final String OWLClass_AsymmetricObjectPropertyMapping = NAMESPACE + "AsymmetricObjectPropertyMapping";
    private static final String OWLClass_InverseFunctionalObjectPropertyMapping = NAMESPACE + "InverseFunctionalObjectPropertyMapping";
    private static final String OWLClass_ReflexiveObjectPropertyMapping = NAMESPACE + "ReflexiveObjectPropertyMapping";
    private static final String OWLClass_IrreflexiveObjectPropertyMapping = NAMESPACE + "IrreflexiveObjectPropertyMapping";
    private static final String OWLClass_FunctionalDatatypePropertyMapping = NAMESPACE + "FunctionalDatatypePropertyMapping";
    
    
    //OWL Object Properties
    private static final String ObjectProperty_isCollectionOfMapping = NAMESPACE + "isCollectionOfMapping";
    private static final String ObjectProperty_isGroupingAttributeMapping = NAMESPACE + "isGroupingAttributeMapping";
    private static final String ObjectProperty_exactly1CardinalityRestriction = NAMESPACE + "exactly1CardinalityRestriction";
    private static final String ObjectProperty_min1CardinalityRestriction = NAMESPACE + "min1CardinalityRestriction";
    private static final String ObjectProperty_max1CardinalityRestriction = NAMESPACE + "max1CardinalityRestriction";
    private static final String ObjectProperty_existsSomeObjectPropertyRestriction = NAMESPACE + "existsSomeObjectPropertyRestriction";
    private static final String ObjectProperty_onlyFromPropertyRestriction = NAMESPACE + "onlyFromPropertyRestriction";
    private static final String ObjectProperty_subClassOfMapping = NAMESPACE + "subClassOfMapping";
    private static final String ObjectProperty_superClassOfMapping = NAMESPACE + "superClassOfMapping";
    private static final String ObjectProperty_equivalentClassMapping = NAMESPACE + "equivalentClassMapping";
    private static final String ObjectProperty_hasPartMapping = NAMESPACE + "hasPartMapping";
    private static final String ObjectProperty_hasRangeMapping = NAMESPACE + "hasRangeMapping";
    private static final String ObjectProperty_hasDomainMapping = NAMESPACE + "hasDomainMapping";
    private static final String ObjectProperty_hasInversePropertyMapping = NAMESPACE + "hasInversePropertMapping";
    private static final String ObjectProperty_hasDefaultValueMapping = NAMESPACE + "hasDefaultValueMapping";
    private static final String ObjectProperty_hasObjectPropertyMapping = NAMESPACE + "hasObjectPropertyMapping";
    private static final String ObjectProperty_hasSubClassOfRangeMapping = NAMESPACE + "hasSubClassOfRangeMapping";
    private static final String ObjectProperty_hasSubClassOfDomainMapping = NAMESPACE + "hasSubClassOfDomainMapping";
    
    //OWL Data Properties
    private static final String DataProperty_hasAggregateFunctionMapping = NAMESPACE + "hasAggregateFunctionMapping";
    
    
    //IRI Getters
    public OWLClass getOWLClass_EntityConceptMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_EntityConceptMapping));}
    public OWLClass getOWLClass_VirtualConceptMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_VirtualConceptMapping));}
    public OWLClass getOWLClass_VirtualPropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_VirtualPropertyMapping));}
    public OWLClass getOWLClass_VirtualCollectionMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_VirtualCollectionMapping));}
    public OWLClass getOWLClass_RelationConceptMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_RelationConceptMapping));}
    public OWLClass getOWLClass_UnaryObjectPropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_UnaryObjectPropertyMapping));}
    public OWLClass getOWLClass_BinaryObjectPropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_BinaryObjectPropertyMapping));}
    public OWLClass getOWLClass_DatatypePropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_DatatypePropertyMapping));}
    public OWLClass getOWLClass_TransitiveObjectPropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_TransitiveObjectPropertyMapping));}
    public OWLClass getOWLClass_FunctionalObjectPropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_FunctionalObjectPropertyMapping));}
    public OWLClass getOWLClass_EnnumerationPropertyRangeMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_EnnumerationPropertyRangeMapping));}
    public OWLClass getOWLClass_SymmetricObjectPropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_SymmetricObjectPropertyMapping));}
    public OWLClass getOWLClass_AsymmetricObjectPropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_AsymmetricObjectPropertyMapping));}
    public OWLClass getOWLClass_InverseFunctionalObjectPropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_InverseFunctionalObjectPropertyMapping));}
    public OWLClass getOWLClass_ReflexiveObjectPropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_ReflexiveObjectPropertyMapping));}
    public OWLClass getOWLClass_IrreflexiveObjectPropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_IrreflexiveObjectPropertyMapping));}
    public OWLClass getOWLClass_FunctionalDatatypePropertyMapping(){return this.bundle.getFactory().getOWLClass(IRI.create(OWLClass_FunctionalDatatypePropertyMapping));}
    
    
    
    public OWLObjectProperty getObjectProperty_isCollectionOfMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_isCollectionOfMapping));}
    public OWLObjectProperty getObjectProperty_isGroupingAttributeMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_isGroupingAttributeMapping));}
    public OWLObjectProperty getObjectProperty_exactly1CardinalityRestriction(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_exactly1CardinalityRestriction));}
    public OWLObjectProperty getObjectProperty_min1CardinalityRestriction(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_min1CardinalityRestriction));}
    public OWLObjectProperty getObjectProperty_max1CardinalityRestriction(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_max1CardinalityRestriction));}
    public OWLObjectProperty getObjectProperty_existsSomeObjectPropertyRestriction(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_existsSomeObjectPropertyRestriction));}
    public OWLObjectProperty getObjectProperty_onlyFromPropertyRestriction(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_onlyFromPropertyRestriction));}
    public OWLObjectProperty getObjectProperty_subClassOfMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_subClassOfMapping));}
    public OWLObjectProperty getObjectProperty_superClassOfMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_superClassOfMapping));}
    public OWLObjectProperty getObjectProperty_equivalentClassMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_equivalentClassMapping));}
    public OWLObjectProperty getObjectProperty_hasPartMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasPartMapping));}
    public OWLObjectProperty getObjectProperty_hasRangeMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasRangeMapping));}
    public OWLObjectProperty getObjectProperty_hasDomainMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasDomainMapping));}
    public OWLObjectProperty getObjectProperty_hasInversePropertyMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasInversePropertyMapping));}
    public OWLObjectProperty getObjectProperty_hasDefaultValueMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasDefaultValueMapping));}
    public OWLObjectProperty getObjectProperty_hasObjectPropertyMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasObjectPropertyMapping));}
    public OWLObjectProperty getObjectProperty_hasSubClassOfRangeMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasSubClassOfRangeMapping));}
    public OWLObjectProperty getObjectProperty_hasSubClassOfDomainMapping(){return this.bundle.getFactory().getOWLObjectProperty(IRI.create(ObjectProperty_hasSubClassOfDomainMapping));}
    
    public OWLDataProperty getDataProperty_hasAggregateFunctionMapping(){return this.bundle.getFactory().getOWLDataProperty(IRI.create(DataProperty_hasAggregateFunctionMapping ));}
    
    public RelationalToOntologyMapping(OWLUtils bundle) {
        super(bundle);
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

}
