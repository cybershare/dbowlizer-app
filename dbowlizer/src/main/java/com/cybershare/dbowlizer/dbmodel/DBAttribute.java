
package com.cybershare.dbowlizer.dbmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */
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
