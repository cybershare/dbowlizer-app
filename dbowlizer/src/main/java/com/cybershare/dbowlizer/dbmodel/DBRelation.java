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

public class DBRelation extends Element{

    private String relationName; //relationName is composed of SchemaName:tableName
    private ArrayList<DBAttribute> attributes;
    private DBPrimaryKey primaryKey;
    private ArrayList<DBForeignKey> foreignKeys;
    private String tableName;

    public DBRelation(String identification) {
        super(identification);
        attributes = new ArrayList<DBAttribute>();
        foreignKeys = new ArrayList<DBForeignKey>();
        setTableName();
    }
    
    public String getTableName() {
		return tableName;
	}

	public void setTableName() {
		String[] splitidentification= getIdentification().split("\\:");
    	this.tableName=splitidentification[splitidentification.length-1];
	}

	public boolean isSet_attributes(){return attributes.size() > 0;}
    
    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }
  
    public void addAttribute(DBAttribute attribute){
        attributes.add(attribute);
    }

    public List<DBAttribute> getAttributes() {
        return attributes;
    }

    public DBPrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(DBPrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<DBForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public void addForeignKeys(DBForeignKey foreignkey) {
        foreignKeys.add(foreignkey);
    }
    
    public boolean areAllAttributesPK(){
		boolean bool=false;
		List<DBAttribute> listDBA=this.attributes;
		int countOfPKs=0;
		for(int i=0; i< listDBA.size();i++){
			if(listDBA.get(i).isPK()){
				countOfPKs++;
			}
		}
		if(countOfPKs==listDBA.size())
			return true;
		return bool;
	}
    
    public boolean areAllAttributesFKnPF(){
		boolean bool=false;
		List<DBAttribute> listDBA=attributes;
		int countOfPKnFKs=0;
		for(int i=0; i< listDBA.size();i++){
			if(listDBA.get(i).isPK()&&listDBA.get(i).isFK()){
				countOfPKnFKs++;
			}
		}
		if(countOfPKnFKs==listDBA.size())
			return true;
		return bool;
	}
    
    
  	public boolean hasOwnPK(){
  		boolean bool=false;
		List<DBAttribute> listDBA=attributes;
		for(int i=0; i<listDBA.size();i++){
			if(listDBA.get(i).isPK()&&!(listDBA.get(i).isFK()))
				return true;
		}
  		return bool;
  	}
  	
    public boolean hasOnePK(){
  		boolean bool=false;
  		List<DBAttribute> listDBA=attributes;
  		int countOfPKs=0;
  		for(int i=0; i< listDBA.size();i++){
  			if(listDBA.get(i).isPK()){
  				countOfPKs++;
  			}
  		}
  		if(countOfPKs==1)
  			return true;
  		return bool;
  	}
    
    public boolean hasOnePKThatIsFK(){
  		boolean bool=false;
  		List<DBAttribute> listDBA=attributes;
  		int countOfPKs=0;
  		for(int i=0; i< listDBA.size();i++){
  			if(listDBA.get(i).isPK()&&listDBA.get(i).isFK()){
  				countOfPKs++;
  			}
  		}
  		if(countOfPKs==1)
  			return true;
  		return bool;
  	}
    
    
    
    
    public boolean hasMoreThanOnePK(){
    	boolean bool=false;
  		List<DBAttribute> listDBA=attributes;
  		int countOfPKs=0;
  		for(int i=0; i< listDBA.size();i++){
  			if(listDBA.get(i).isPK()){
  				countOfPKs++;
  			}
  			if(countOfPKs>=2){
  				return true;
  			}
  		}
  		return bool;	
    }
    
    public int numPkThatareFK(){
  		List<DBAttribute> listDBA=attributes;
  		int countOfPKsnFK=0;
  		for(int i=0; i< listDBA.size();i++){
  			if(listDBA.get(i).isPK()&&listDBA.get(i).isFK()){
  				countOfPKsnFK++;
  			}
  		}
  		return countOfPKsnFK;
  	}
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
