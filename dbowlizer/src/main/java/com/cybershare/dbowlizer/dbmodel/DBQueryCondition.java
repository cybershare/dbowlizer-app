
package com.cybershare.dbowlizer.dbmodel;


/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */
public class DBQueryCondition extends Element{

    private String queryConditionName;
    private DBAttribute attribute;
    private String value;
    private String operator;

    public DBQueryCondition(String identification) {
        super(identification);
    }

    public String getQueryConditionName() {
        return queryConditionName;
    }

    public void setQueryConditionName(String queryConditionName) {
        this.queryConditionName = queryConditionName;
    }

    public DBAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(DBAttribute attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
