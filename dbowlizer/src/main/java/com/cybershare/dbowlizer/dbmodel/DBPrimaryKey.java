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

public class DBPrimaryKey extends Element{

    private String primaryKeyName;
    private ArrayList<DBAttribute> attributes;

    public DBPrimaryKey(String identification) {
        super(identification);
        attributes = new ArrayList<DBAttribute>();
    }
    
    public boolean isSet_attributes(){return attributes.size() > 0;}
    
    public int getAttributeCount(){
    	return attributes.size();
    }
    
    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public void addAttribute(DBAttribute attribute){
        attributes.add(attribute);
    }

    public List<DBAttribute> getAttributes() {
        return attributes;
    }
    
    /* PK Classification methods */
    
    /* A primary key whose attributes are not part of a foreign key. */
    public boolean isIndependent_PK(){
        for (DBAttribute db_attribute: attributes){
            if (db_attribute.isFK())
                return false;
        }
       return true;
    }
    
    /* Database Primary Foreign Key composed only by attributes that are 
    also part of two foreign keys. */
    public boolean isBinaryDependent_PK(){
        boolean value = false;
        String relationName = null;
        if (attributes.size() == 2){
            for (DBAttribute temp : attributes){
                if(temp.isFK()){
                    if (relationName == null){
                        relationName = temp.getReferencedRelationName();
                    }
                    else if (relationName != temp.getReferencedRelationName()){
                        value = true;
                    }
                }
            }
        }
        return value;
    }
    
    /* Primary Key composed only by foreign key attributes referring to more than two relations */
    public boolean isMultipleDependent_PK(){
        boolean value = false;
        ArrayList<String> relationNames = new ArrayList<String>();
        int rCounter = 0;
        if (attributes.size() > 2){
            for (DBAttribute temp : attributes){
                if(temp.isFK()){
                    if (relationNames == null){
                        relationNames.add(temp.getReferencedRelationName());
                        rCounter++;
                    }
                    else if (!relationNames.contains(temp.getReferencedRelationName())){
                        relationNames.add(temp.getReferencedRelationName());
                        rCounter++;
                    }
                }
                else{
                    return false;
                }
            }
        }
        
        if(rCounter > 2)
            return true;
        else
            return value;
    }
    
    /* Database Primary Foreign Key composed by foreign key attributes that 
    reference two relation plus at least one non-foreign key attribute. */
    public boolean isPartiallyBinaryDependent_PK(){
        boolean value = false;
        boolean noFK_Found = false;
        ArrayList<String> relationNames = new ArrayList<String>();
        int rCounter = 0;
        if (attributes.size() > 2){
            for (DBAttribute temp : attributes){
                if(temp.isFK()){
                    if (relationNames == null){
                        relationNames.add(temp.getReferencedRelationName());
                        rCounter++;
                    }
                    else if (!relationNames.contains(temp.getReferencedRelationName())){
                        relationNames.add(temp.getReferencedRelationName());
                        rCounter++;
                    }
                }
                else{
                    noFK_Found = true;
                }
            }
        }
        
        if(rCounter == 2 && noFK_Found)
            return true;
        else
            return value;
    }
    
    /* Database Primary Key composed by foreign key attributes that refer to 
       than two relations plus at least one non-foreign key attribute. */
    public boolean isPartiallyMultipleDependent_PK(){
        boolean value = false;
        boolean noFK_Found = false;
        ArrayList<String> relationNames = new ArrayList<String>();
        int rCounter = 0;
        if (attributes.size() > 2){
            for (DBAttribute temp : attributes){
                if(temp.isFK()){
                    if (relationNames == null){
                        relationNames.add(temp.getReferencedRelationName());
                        rCounter++;
                    }
                    else if (!relationNames.contains(temp.getReferencedRelationName())){
                        relationNames.add(temp.getReferencedRelationName());
                        rCounter++;
                    }
                }
                else{
                    noFK_Found = true;
                }
            }
        }
        
        if(rCounter > 2 && noFK_Found)
            return true;
        else
            return value;  
    }
    
    /* Primary Key composed by foreign key attributes that refer to only one relation plus at least one non-foreign key attribute. */
    public boolean isPartiallySingleDependent_PK(){
        boolean value = false;
        boolean noFK_Found = false;
        String relationName = null;
        for (DBAttribute temp : this.attributes){
            if(temp.isFK()){
                if (relationName == null){
                    relationName = temp.getReferencedRelationName();
                    value = true;
                }
                else if (relationName == temp.getReferencedRelationName()){
                    value = true;
                }
                else{
                    return false;
                }
            }
            else{
                noFK_Found = true;
            }
        }
        if (value && noFK_Found){
            return true;
        }
        else{
            return false;
        }
        
    }
   
    /* Primary Key composed only by foreign key attribute(s) that refer to only one relation. */
    public boolean isSingleDependent_PK(){
        boolean value = false;
        String relationName = null;
        for (DBAttribute temp : this.attributes){
            if(temp.isFK()){
                if (relationName == null){
                    relationName = temp.getReferencedRelationName();
                    value = true;
                }
                else if (relationName == temp.getReferencedRelationName()){
                    value = true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        return value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
