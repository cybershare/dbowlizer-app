/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cybershare.dbowlizer.dbmodel;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
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
