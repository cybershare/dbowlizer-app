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

import com.cybershare.dbowlizer.build.ModelProduct;

public class DBAttribute extends Element {

    private boolean PK = false; //Primary Key Attribute
    private boolean FK = false; //Foreign Key Attribute
    private boolean NN = true; //Not Null Attribute
    private boolean UN = false; //Unique Attribute
    private String datatype = null;
    private String defaultvalue = null;
    private DBAttributeDomain domain;
    private String referencedRelationName = null; //Only applies if the attribute is a foreign key
    private ArrayList<DBAttributeRestriction> restrictions;
    private String columnName;
    
    public DBAttribute(String identification) {
        super(identification);
        restrictions = new ArrayList<DBAttributeRestriction>();
        setColumnName();
    }

    public String getColumnName() {
		return columnName;
	}

	public DBAttributeDomain getDomain() {
        return domain;
    }

    public void setDomain(DBAttributeDomain domain) {
        this.domain = domain;
    }
    
    public List<DBAttributeRestriction> getAttributeRestrictions() {
        return restrictions;
    }

    public void addRestriction(DBAttributeRestriction attributerestriction){
        restrictions.add(attributerestriction);
    }
    
    public void setRestrictions(ArrayList<DBAttributeRestriction> restrictions) {
        this.restrictions = restrictions;
    }
    

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    public String getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }    

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public boolean isPK() {
        return PK;
    }

    public void setPK(boolean PK) {
        this.PK = PK;
    }

    public boolean isFK() {
        return FK;
    }

    public void setFK(boolean FK) {
        this.FK = FK;
    }

    public boolean isNN() {
        return NN;
    }

    public void setNN(boolean NN) {
        this.NN = NN;
    }

    public boolean isUN() {
        return UN;
    }

    public void setUN(boolean UN) {
        this.UN = UN;
    }
    
    
    public void setColumnName(){
    	String[] splitidentification= getIdentification().split("\\.");
    	this.columnName=splitidentification[splitidentification.length-1];
    }
    
    //Only applies if the attribute is a foreign key to reference other relations
    public String getReferencedRelationName() {
        return referencedRelationName;
    }

    public void setReferencedRelationName(String referencedRelationName) {
        this.referencedRelationName = referencedRelationName;
    }
   
    public boolean is_nonKeyAttribute (){
        return !(PK || FK || UN);
    }
    
    public boolean isCandidateKey(){
        return (!PK && UN);
    }

}
