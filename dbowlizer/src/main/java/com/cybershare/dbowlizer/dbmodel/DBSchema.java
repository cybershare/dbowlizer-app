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
package com.cybershare.dbowlizer.dbmodel;

import java.util.ArrayList;
import java.util.List;

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
