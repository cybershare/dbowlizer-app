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

import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.views.DbView;


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
