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
