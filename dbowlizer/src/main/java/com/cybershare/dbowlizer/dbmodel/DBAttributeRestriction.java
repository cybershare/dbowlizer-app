

package com.cybershare.dbowlizer.dbmodel;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */
public class DBAttributeRestriction extends Element{

    private String attributeRestrictionName;

    public DBAttributeRestriction(String identification) {
        super(identification);
    }
    
    
    public String getAttributeRestrictionName() {
        return attributeRestrictionName;
    }

    public void setAttributeRestrictionName(String attributeRestrictionName) {
        this.attributeRestrictionName = attributeRestrictionName;
    }
  
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
