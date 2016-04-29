package com.cybershare.dbowlizer.views;

import com.cybershare.dbowlizer.dbmodel.DBAttribute;

public class DbViewRestriction {
	DbView view;
	DBAttribute column;
	String operator;
	String value;
	
	
	
	public DbViewRestriction(DbView view, DBAttribute column, String operator, String value) {
		super();
		this.view = view;
		this.column = column;
		this.operator = operator;
		this.value = value;
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
