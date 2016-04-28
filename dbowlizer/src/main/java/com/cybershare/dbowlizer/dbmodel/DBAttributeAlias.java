
package com.cybershare.dbowlizer.dbmodel;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */

public class DBAttributeAlias extends Element{

    private String attributeAliasName;
    private DBAttribute attribute;

    public DBAttributeAlias(String identification) {
        super(identification);
    }

    public String getAttributeAliasName() {
        return attributeAliasName;
    }

    public void setAttributeAliasName(String attributeAliasName) {
        this.attributeAliasName = attributeAliasName;
    }

    public DBAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(DBAttribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
