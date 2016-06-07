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
package com.cybershare.dbowlizer.build;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBAttributeAlias;
import com.cybershare.dbowlizer.dbmodel.DBAttributeDomain;
import com.cybershare.dbowlizer.dbmodel.DBAttributeRestriction;
import com.cybershare.dbowlizer.dbmodel.DBCandidateKey;
import com.cybershare.dbowlizer.dbmodel.DBForeignKey;
import com.cybershare.dbowlizer.dbmodel.DBPrimaryKey;
import com.cybershare.dbowlizer.dbmodel.DBQuery;
import com.cybershare.dbowlizer.dbmodel.DBQueryCondition;
import com.cybershare.dbowlizer.dbmodel.DBQueryJoin;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.dbmodel.DBSchema;
import com.cybershare.dbowlizer.dbmodel.DBView;


public class ModelProduct {
    
    private HashMap<String, DBSchema> schemas;
    private HashMap<String, DBRelation> relations;
    private HashMap<String, DBAttribute> attributes;
    private HashMap<String, DBAttributeAlias> aliases;
    private HashMap<String, DBView> views;
    private HashMap<String, DBQuery> queries;
    private HashMap<String, DBQueryCondition> conditions;
    private HashMap<String, DBQueryJoin> joins;
    private HashMap<String, DBCandidateKey> candidatekeys;
    private HashMap<String, DBPrimaryKey> primarykeys;
    private HashMap<String, DBForeignKey> foreignkeys;
    private HashMap<String, DBAttributeDomain> attributedomains;
    private HashMap<String, DBAttributeRestriction> attributerestrictions;

    public ModelProduct(){
        this.schemas = new  HashMap <String, DBSchema>();
        this.relations = new HashMap <String, DBRelation>();
        this.attributes = new HashMap <String, DBAttribute>();
        this.aliases = new HashMap <String, DBAttributeAlias>();
        this.views = new HashMap <String, DBView>();
        this.queries = new HashMap <String, DBQuery>();
        this.conditions = new HashMap <String, DBQueryCondition>();
        this.joins = new HashMap <String, DBQueryJoin>();
        this.candidatekeys = new HashMap <String, DBCandidateKey>();
        this.primarykeys = new HashMap <String, DBPrimaryKey>();
        this.foreignkeys = new HashMap <String, DBForeignKey>();
        this.attributedomains = new HashMap <String, DBAttributeDomain>();
        this.attributerestrictions = new HashMap <String, DBAttributeRestriction>();
    }
    
    //Get Lists
    public List<DBSchema> getSchemas(){return new ArrayList<DBSchema>(schemas.values());}
    public List<DBRelation> getRelations(){return new ArrayList<DBRelation>(relations.values());}
    public List<DBAttributeAlias> getAliases(){return new ArrayList<DBAttributeAlias>(aliases.values());}
    public List<DBAttribute> getAttributes(){return new ArrayList<DBAttribute>(attributes.values());}
    public List<DBView> getViews(){return new ArrayList<DBView>(views.values());}
    public List<DBQuery> getQueries(){return new ArrayList<DBQuery>(queries.values());}
    public List<DBQueryCondition> getConditionQueries(){return new ArrayList<DBQueryCondition>(conditions.values());}
    public List<DBQueryJoin> getJoinQueries(){return new ArrayList<DBQueryJoin>(joins.values());}
    public List<DBCandidateKey> getCandidateKeys(){return new ArrayList<DBCandidateKey>(candidatekeys.values());}
    public List<DBPrimaryKey> getPrimaryKeys(){return new ArrayList<DBPrimaryKey>(primarykeys.values());}
    public List<DBForeignKey> getForeignKeys(){return new ArrayList<DBForeignKey>(foreignkeys.values());}
    public List<DBAttributeDomain> getAttributeDomains(){return new ArrayList<DBAttributeDomain>(attributedomains.values());}
    public List<DBAttributeRestriction> getAttributeRestrictions(){return new ArrayList<DBAttributeRestriction>(attributerestrictions.values());}
    
    //Get HashMaps
    public HashMap<String, DBRelation> getRelationsMap(){return new HashMap<String, DBRelation>(relations);}
    public HashMap<String, DBAttribute> getAttributesMap(){return new HashMap<String, DBAttribute>(attributes);}
    //Get each or insert if object does not exist
    public DBSchema getSchema(String key){
        DBSchema object = this.schemas.get(key);
        if(object == null){
            object = new DBSchema(key);
            this.schemas.put(key,object);
        }
        return object;
    }
    
    public DBRelation getRelation(String key){
        DBRelation object = this.relations.get(key);
        if(object == null){
            object = new DBRelation(key);
            this.relations.put(key,object);
        }
        return object;
    }
    
    public DBView getView(String key){
        DBView object = this.views.get(key);
        if(object == null){
            object = new DBView(key);
            this.views.put(key,object);
        }
        return object;
    }
    
    public DBQuery getQuery(String key){
        DBQuery object = this.queries.get(key);
        if(object == null){
            object = new DBQuery(key);
            this.queries.put(key,object);
        }
        return object;
    }
    
    public DBQueryCondition getConditionQuery(String key){
        DBQueryCondition object = this.conditions.get(key);
        if(object == null){
            object = new DBQueryCondition(key);
            this.conditions.put(key,object);
        }
        return object;
    }
    
    public DBQueryJoin getJoinQuery(String key){
        DBQueryJoin object = this.joins.get(key);
        if(object == null){
            object = new DBQueryJoin(key);
            this.joins.put(key,object);
        }
        return object;
    }
    
    public DBAttribute getAttribute(String key){
        DBAttribute object = this.attributes.get(key);
        if(object == null){
            object = new DBAttribute(key);
            this.attributes.put(key,object);
        }
        return object;   
    }
    
    public DBAttributeAlias getAttributeAlias(String key){
        DBAttributeAlias object = this.aliases.get(key);
        if(object == null){
            object = new DBAttributeAlias(key, null, this);
            this.aliases.put(key,object);
        }
        return object;   
    }
 
    public DBCandidateKey getCandidateKey(String key){
        DBCandidateKey object = this.candidatekeys.get(key);
        if(object == null){
            object = new DBCandidateKey(key);
            this.candidatekeys.put(key,object);
        }
        return object;   
    }
    
    public DBPrimaryKey getPrimaryKey(String key){
        DBPrimaryKey object = this.primarykeys.get(key);
        if(object == null){
            object = new DBPrimaryKey(key);
            this.primarykeys.put(key,object);
        }
        return object;   
    }
    
    public DBForeignKey getForeignKey(String key){
        DBForeignKey object = this.foreignkeys.get(key);
        if(object == null){
            object = new DBForeignKey(key);
            this.foreignkeys.put(key,object);
        }
        return object;   
    }
    
    public DBAttributeDomain getAttributeDomain(String key){
        DBAttributeDomain object = this.attributedomains.get(key);
        if(object == null){
            object = new DBAttributeDomain(key);
            this.attributedomains.put(key,object);
        }
        return object;   
    }
    
    public DBAttributeRestriction getAttributeRestriction(String key){
        DBAttributeRestriction object = this.attributerestrictions.get(key);
        if(object == null){
            object = new DBAttributeRestriction(key);
            this.attributerestrictions.put(key,object);
        }
        return object;   
    }
    
}
