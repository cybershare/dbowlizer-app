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

public class DBForeignKey extends Element{

    private String foreignKeyName;
    private DBAttribute attribute;
    private DBPrimaryKey referencedKey;

    public DBForeignKey(String identification) {
        super(identification);
    }

    public String getForeignKeyName() {
        return foreignKeyName;
    }

    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    public DBAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(DBAttribute attribute) {
        this.attribute = attribute;
    }

    public DBPrimaryKey getReferencedKey() {
        return referencedKey;
    }

    public void setReferencedKey(DBPrimaryKey referencedKey) {
        this.referencedKey = referencedKey;
    }
    
    /* FK Classification methods */
    
    /*Foreign Key whose attributes are not part of the relation's primary key.*/
    public boolean isNonPrimaryFK(){
        if (!this.attribute.isPK()){
            return true;
        }
        return false;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
