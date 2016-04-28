
package com.cybershare.dbowlizer.dbmodel;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
public class DBCandidateKey extends Element{

    private String candidateKeyName;
    private DBAttribute keyAttribute;

    public DBCandidateKey(String identification) {
        super(identification);
    }

    public DBAttribute getKeyAttribute() {
        return keyAttribute;
    }

    public void setKeyAttribute(DBAttribute keyAttribute) {
        this.keyAttribute = keyAttribute;
    }
    
    public String getCandidateKeyName() {
        return candidateKeyName;
    }

    public void setCandidateKeyName(String candidateKeyName) {
        this.candidateKeyName = candidateKeyName;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
