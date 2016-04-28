

package com.cybershare.dbowlizer.dbmodel;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */

public class DBAttributeDomain extends Element{

    private String attributeDomainName;

    public DBAttributeDomain(String identification) {
        super(identification);
    }
    
    
    public String getAttributeDomainName() {
        return attributeDomainName;
    }

    public void setAttributeDomainName(String attributeDomainName) {
        this.attributeDomainName = attributeDomainName;
    }
  
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
