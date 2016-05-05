
package com.cybershare.dbowlizer.dbmodel;

import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.views.DbView;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at utep.edu>
 */

public class DBAttributeAlias extends Element{

	private String attributeAliasName;
	private DBAttribute attribute;
	private ModelProduct modelProduct;  
	private DbView view;
	private String alias_agg;

	public DBAttributeAlias(String identification, DbView view, ModelProduct modelProduct) {
		super(identification);
		this.view = view;
		this.modelProduct = modelProduct;
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
	
	
	
	public ModelProduct getModelProduct() {
		return modelProduct;
	}

	public void setModelProduct(ModelProduct modelProduct) {
		this.modelProduct = modelProduct;
	}

	public DbView getView() {
		return view;
	}

	public void setView(DbView view) {
		this.view = view;
	}

	public String getAlias_agg() {
		return alias_agg;
	}

	public void setAlias_agg(String alias_agg) {
		this.alias_agg = alias_agg;
	}

	public void addColumn(String columnName){
		if (columnName.equals("0") || columnName.equals("*"))
			this.attribute = new DBAttribute("all");
		else {
			if (modelProduct.getAttributesMap().get(columnName) != null)
				this.attribute = modelProduct.getAttribute(columnName);
			else
				System.out.println(columnName + ":Column in the SELECT statement not found.");
			
		}
	}

	public void addAlias(String aliasName){
		if (aliasName.equals("0") || aliasName.equals("*"))
			this.attributeAliasName = "aliasName";
		else{
			DBAttribute alias_col = modelProduct.getAttributesMap().get(aliasName); 

			if (alias_col != null)
				this.attributeAliasName = alias_col.getColumnName();//Unsure about this
			else
				this.attributeAliasName = aliasName;
		}
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
