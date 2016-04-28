
package com.cybershare.dbowlizer.dbmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */

public class DBQueryJoin extends Element{

    private String queryJoinName;
    private DBRelation relation;
    private ArrayList<DBAttribute> attributes;


    public DBQueryJoin(String identification) {
        super(identification);
        attributes = new ArrayList<DBAttribute>();
    }

    public String getQueryJoinName() {
        return queryJoinName;
    }

    public void setQueryJoinName(String queryJoinName) {
        this.queryJoinName = queryJoinName;
    }

    public DBRelation getRelation() {
        return relation;
    }

    public void setRelation(DBRelation relation) {
        this.relation = relation;
    }
    
    public void addAttribute(DBAttribute attribute){
        attributes.add(attribute);
    }

    public List<DBAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
