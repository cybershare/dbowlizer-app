
package com.cybershare.dbowlizer.dbmodel;

import com.cybershare.dbowlizer.build.ModelProduct;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */

public class DBAttributeAlias extends Element{

    private String attributeAliasName;
    private DBAttribute attribute;
    private ModelProduct modelProduct;                            

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
    public void addColumn(String columnName, ModelProduct modelProduct){
      if (columnName.equals("0") || columnName.equals("*"))
         this.attribute = new DBAttribute("all");
       else {
         if (modelProduct.getAttribute(columnName) != null)
           this.attribute = modelProduct.getAttribute(columnName);
         else
           System.out.println("Column not found");
       }
     }
 
     public void addAlias(String aliasName){
       if (aliasName.equals("0") || aliasName.equals("*"))
         this.attributeAliasName = "aliasName";
       else{
         //TODO: check this part
         if (modelProduct.getAttribute(aliasName) != null)
           this.attributeAliasName = aliasName;//Unsure about this
       }
     }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
