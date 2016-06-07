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

import java.util.ArrayList;
import java.util.List;

import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBAttributeAlias;
import com.cybershare.dbowlizer.dbmodel.DBRelation;

import com.cybershare.dbowlizer.dbmodel.Element;

public class DbView {

	private ArrayList<DBAttributeAlias> column_alias;
	private ArrayList<DBRelation> tables;
	private ArrayList<DbViewRestriction> restrictions;
	private ArrayList<DbViewJoin> joins;
	private ArrayList<DBAttribute> group_by_columns;
	private boolean copy;
	private ModelProduct dbSchema;
	private String name;

	public DbView()
	{
		column_alias = new ArrayList<DBAttributeAlias>();
		tables = new ArrayList<DBRelation>();
		restrictions = new ArrayList<DbViewRestriction>();
		joins = new ArrayList<DbViewJoin>();
		group_by_columns = new ArrayList<DBAttribute>();
		copy =false;
	}


	public void check_equality()
	{

		if (tables.size() == 1)
		{
			ArrayList<DBAttribute> columns = new ArrayList<DBAttribute>();
			//#For each alias, we extract the column it cotains.
			for (Element colAlias : column_alias)
			{
				//#puts colAlias.class
				if (colAlias instanceof DBAttributeAlias)
				{
					columns.add(((DBAttributeAlias)colAlias).getAttribute());
				}	
			}
			List<DBAttribute> tableColumns = tables.get(0).getAttributes();

			if (areListsTheSame(columns, tableColumns))	
			{
				copy=true;
			}	
		}	
	}

	private boolean areListsTheSame(List<DBAttribute> list1, List<DBAttribute> tableColumns)
	{
		for (DBAttribute list1Att : list1)
		{
			if (! tableColumns.contains(list1Att))
				return false;
		}

		for (DBAttribute list2Att : tableColumns)
		{
			if (! list1.contains(list2Att))
				return false;
		}

		return true;
	}

	public void check_all_columns_included_and_add_all_tables()
	{
		ArrayList<DBAttribute> view_columns = new ArrayList<DBAttribute>();
		ArrayList<DBRelation> tables_included = new ArrayList<DBRelation>();
		ArrayList<DBAttribute> tables_columns = new ArrayList<DBAttribute>();
		//#For each alias, we extract the column it contains and the table it comes from.
		for (DBAttributeAlias colAlias : column_alias)
		{
			if (colAlias instanceof DBAttributeAlias)
			{
				//TODO: ASK VN
				if (colAlias != null &&  ((DBAttributeAlias)colAlias).getAttribute() != null && !((DBAttributeAlias)colAlias).getAttribute().getIdentification().equals("all"))
				{
					view_columns.add(((DBAttributeAlias) colAlias).getAttribute());
					DBRelation table = null;
					for (DBRelation r : dbSchema.getRelations())
					{
						
						if (r.getAttributes().contains(colAlias.getAttribute()))
						{
							table = r;
						}
					}
					if (tables.contains(table))
					{
						//						//#puts "table already included: " + table.name.to_s

					}else /*if (table != null)*/{
						//					    //#puts "adding table: " + table.name.to_s
						tables.add(table);
					}

				}	
			}
		}

		for (DBRelation currentTable : tables)
		{
			
			for (DBAttribute column : currentTable.getAttributes())
			{
				tables_columns.add(column);
			}	
		}

		if (areListsTheSame(tables_columns, view_columns))
		{
			copy=true;
		}	
	}

	public String toString()
	{
		String viewText="";
		viewText = "\n== VIEW "+name;
		if (column_alias.size()>0)
		{
			for (Element e : column_alias)
			{
				viewText += e.toString();
			}

		}	
		if (tables.size()>0)
		{
			for (DBRelation table : tables)
			{
				viewText += "\n = From Table: "+table.getRelationName();
			}

		}	
		if (joins.size()>0)
		{
			for ( DbViewJoin join : joins)
			{
				viewText += "\n = Join "+join.toString();
			}

		}	
		if (restrictions.size()>0) 
		{
			for (DbViewRestriction restriction : restrictions)
			{
				viewText += "\n = Where Restriction "+restriction.toString();
			}
		}

		if (group_by_columns.size()>0)
		{
			for (DBAttribute column : group_by_columns)
			{
				viewText += "\n = Group By Column: "+column.getColumnName();
			}

		}	
		String tablesName = "";

		for (DBRelation table : tables)
			tablesName+=table.getRelationName()+",";
		if (copy && tables.size()>0)
		{
			viewText+="\n = Contains all the columns from table: "+ tablesName; 
		}
		return viewText;
	}


	public ArrayList<DBAttributeAlias> getColumn_alias() {
		return column_alias;
	}


	public void setColumn_alias(ArrayList<DBAttributeAlias> column_alias) {
		this.column_alias = column_alias;
	}


	public ArrayList<DBRelation> getTables() {
		return tables;
	}


	public void setTables(ArrayList<DBRelation> tables) {
		this.tables = tables;
	}


	public ArrayList<DbViewRestriction> getRestrictions() {
		return restrictions;
	}


	public void setRestrictions(ArrayList<DbViewRestriction> restrictions) {
		this.restrictions = restrictions;
	}


	public ArrayList<DbViewJoin> getJoins() {
		return joins;
	}


	public void setJoins(ArrayList<DbViewJoin> joins) {
		this.joins = joins;
	}


	public ArrayList<DBAttribute> getGroup_by_columns() {
		return group_by_columns;
	}


	public void setGroup_by_columns(ArrayList<DBAttribute> group_by_columns) {
		this.group_by_columns = group_by_columns;
	}


	public boolean isCopy() {
		return copy;
	}


	public void setCopy(boolean copy) {
		this.copy = copy;
	}


	public ModelProduct getDbSchema() {
		return dbSchema;
	}


	public void setDbSchema(ModelProduct dbSchema) {
		this.dbSchema = dbSchema;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
}
