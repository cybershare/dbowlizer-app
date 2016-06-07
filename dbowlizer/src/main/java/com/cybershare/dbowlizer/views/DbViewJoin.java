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
import com.cybershare.dbowlizer.dbmodel.DBRelation;

public class DbViewJoin {

	private DBRelation table;
	private DBAttribute column1;
	private DBAttribute column2;
	private DbView view;

	public DbViewJoin(DbView view) {
		super();

		this.view = view;
	}
	
	public String toString()
	{
	   String joinText= "\n";
	   
	   if (table != null)
		   joinText+=" Table: " + table.getRelationName(); 
	  
	   if (column1 != null)
		   joinText+=", Column1:" +column1;
	   
	   if (column2 != null)
		   joinText+=", Column2:" +column2;
	   
	   return joinText;
	}
	
	public DBRelation getTable() {
		return table;
	}
	public void setTable(DBRelation table) {
		this.table = table;
	}
	public DBAttribute getColumn1() {
		return column1;
	}
	public void setColumn1(DBAttribute column1) {
		this.column1 = column1;
	}
	public DBAttribute getColumn2() {
		return column2;
	}
	public void setColumn2(DBAttribute column2) {
		this.column2 = column2;
	}
	public DbView getView() {
		return view;
	}
	public void setView(DbView view) {
		this.view = view;
	}
	
	
	
}
