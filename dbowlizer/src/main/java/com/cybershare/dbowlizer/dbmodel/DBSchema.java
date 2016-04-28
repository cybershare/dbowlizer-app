package com.cybershare.dbowlizer.dbmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */
public class DBSchema extends Element{

    private String schemaName;
    private ArrayList<DBRelation> relations;
    private ArrayList<DBView> views;
    
    public DBSchema(String identification) {
        super(identification);
        relations = new ArrayList<DBRelation>();
        views = new ArrayList<DBView>();
    }
    
    public boolean isSet_relations(){return relations.size() > 0;}
    
    public boolean isSet_views(){return relations.size() > 0;}
    
    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
  
    public void addRelation(DBRelation relation){
        relations.add(relation);
    }
    
    public void addView(DBView view){
        views.add(view);
    }

    public List<DBRelation> getRelations() {
        return relations;
    }

    public List<DBView> getViews() {
        return views;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
