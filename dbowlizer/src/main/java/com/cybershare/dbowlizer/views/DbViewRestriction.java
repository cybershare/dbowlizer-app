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
package com.cybershare.dbowlizer.views;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;

public class DbViewRestriction {
	DbView view;
	DBAttribute column;
	String operator;
	String value;
	
	
	
	public DbViewRestriction(DbView view) {
		super();
		this.view = view;
		
	}

	public String toString()
	{
	   String aliasText= "\n";
	   
	   if (column != null)
		   aliasText+=" Column: " + column.getColumnName(); 
	  
	   if (operator != null)
		   aliasText+=", operator:" +operator;
	   
	   if (value != null)
		   aliasText+=", value:" +value;
	   
	   return aliasText;
	}
	
	public DbView getView() {
		return view;
	}
	public void setView(DbView view) {
		this.view = view;
	}
	public DBAttribute getColumn() {
		return column;
	}
	public void setColumn(DBAttribute column) {
		this.column = column;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
