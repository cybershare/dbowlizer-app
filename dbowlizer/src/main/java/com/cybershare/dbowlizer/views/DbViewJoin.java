package com.cybershare.dbowlizer.views;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBRelation;

public class DbViewJoin {

	private DBRelation table;
	private DBAttribute column1;
	private DBAttribute column2;
	private DbView view;

	public DbViewJoin(DBRelation table, DBAttribute column1, DBAttribute column2, DbView view) {
		super();
		this.table = table;
		this.column1 = column1;
		this.column2 = column2;
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
