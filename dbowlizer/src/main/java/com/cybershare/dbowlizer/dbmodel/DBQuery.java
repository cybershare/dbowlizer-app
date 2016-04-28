
package com.cybershare.dbowlizer.dbmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */

public class DBQuery extends Element{

    private String queryName;
    private ArrayList<DBAttributeAlias> aliases;
    private ArrayList<DBRelation> relations;
    private ArrayList<DBQueryCondition> conditions;
    private ArrayList<DBQueryJoin> joins;

    public DBQuery(String identification) {
        super(identification);
        relations = new ArrayList<DBRelation>();
        conditions = new ArrayList<DBQueryCondition>();
        joins = new ArrayList<DBQueryJoin>();
        aliases = new ArrayList<DBAttributeAlias>();
    }

    public boolean isSet_aliases(){return aliases.size() > 0;}
    public boolean isSet_relations(){return relations.size() > 0;}
    public boolean isSet_conditions(){return conditions.size() > 0;}
    public boolean isSet_joins(){return joins.size() > 0;}

    
    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }
    
    public void addRelation(DBRelation relation){
        relations.add(relation);
    }
    
    public List<DBRelation> getRelations() {
        return relations;
    }
    
    public void addCondition(DBQueryCondition condition){
        this.conditions.add(condition);
    }
    
    public List<DBQueryCondition> getConditions() {
        return conditions;
    }
    
    public void addJoin(DBQueryJoin join){
        this.joins.add(join);
    }
    
    public List<DBQueryJoin> getJoins() {
        return joins;
    }
    
    public void addAlias(DBAttributeAlias alias){
        this.aliases.add(alias);
    }
    
    public List<DBAttributeAlias> getAliases() {
        return aliases;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
