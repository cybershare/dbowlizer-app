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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBAttributeAlias;
import com.cybershare.dbowlizer.dbmodel.DBRelation;

public class DbViewParser {
	DbView new_view;
	private ModelProduct product;
	
	public DbViewParser(ModelProduct product)
	{
		//#new_view=DbView.new
		this.product = product;
		
	}

	public void parseViewName(String tableNameString)
	{
		new_view.setName(tableNameString.trim().replace( "`", "").replace( "'", "").replace( "\"", ""));
		//#puts "\n=== VIEW " + new_view.name+" ==="
	}

	private static int findFirstMatchIndex(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		// Check all occurrences
		while (matcher.find()) {


			return matcher.start();

		}

		return -1;
	}
	
	private static String getCompleteMatchedText(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		// Check all occurrences
		while (matcher.find()) {
			return text.substring(matcher.start(), matcher.end());
		}

		return null;
	}
	
	private static String getTextAfterMatch(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		// Check all occurrences
		while (matcher.find()) {
			return text.substring(matcher.end());
		}

		return null;
	}
	
	public void parseSelectStatement(String selectText)
	{
		
		//if (findFirstMatchIndex(selectText, "SELECT(\\s`?(\\w+`?\\.`?\\w+`?|\\*|((COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\)))\\sAS\\s`?\\w+`?,?)+") != -1)
		if (selectText.contains("SELECT"))
		{
		
			String attText=selectText.replace("SELECT ","").replace("`", "");
			//#puts attText
			
			String[] attTextArray = attText.split(",");
			for (String att : attTextArray)
			{
				att = att.trim();
				if (findFirstMatchIndex(att, "\\w+\\.\\w+.\\w+") != -1)
				{
					
					String columnName = getCompleteMatchedText(att, "\\w+\\.\\w+.\\w+");
					columnName = columnName.substring(0, columnName.indexOf(".")) + ":" + columnName.substring(columnName.indexOf(".") + 1);
					
					//String columnAlias=("//#{$'}").trim().replace("AS","").trim();
					String columnAlias=getTextAfterMatch(att, "\\w+\\.\\w+.\\w+").trim().replace("AS","").trim();
					
					DBAttributeAlias newAlias = new DBAttributeAlias(columnAlias, new_view, product);

					if (columnName!=null)
					{
						newAlias.addColumn(columnName);

					}
					if (columnAlias!=null)
					{
						newAlias.addAlias(columnAlias);
					}	
					new_view.getColumn_alias().add(newAlias);

				}
				
				
				if (findFirstMatchIndex(att, "(COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\)\\sAS\\s(COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\),?") != -1)
				{
					
					String aggColumn = getCompleteMatchedText(att, "(COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\)\\sAS\\s(COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\),?");
					
					DBAttributeAlias aggAlias = new DBAttributeAlias(aggColumn, new_view, product);
					//					
					if (findFirstMatchIndex(aggColumn, "(COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\)\\sAS") != -1)
					{
						
						String columnText = getCompleteMatchedText(aggColumn, "(COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\)\\sAS");
						String aliasText = getTextAfterMatch(aggColumn, "(COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\)\\sAS");
												
						if (findFirstMatchIndex(columnText, "(COUNT|AVG|MAX|MIN)") != -1)
						{
							String aggOp = getCompleteMatchedText(columnText, "(COUNT|AVG|MAX|MIN)");
							String columnName = getTextAfterMatch(columnText, "(COUNT|AVG|MAX|MIN)").replace("(", "").replace(")", "").replace("AS","").trim();
							
							if (columnName!=null)
							{
								aggAlias.addColumn(columnName);
							}
							aggAlias.setAlias_agg(aggOp);
						}	
						if (findFirstMatchIndex(aliasText, "(COUNT|AVG|MAX|MIN)") != -1)
						{
							String aliasOp = getCompleteMatchedText(aliasText, "(COUNT|AVG|MAX|MIN)");
							String aliasName=getTextAfterMatch(aliasText, "(COUNT|AVG|MAX|MIN)").replace("(","").replace(")", "").trim();
														
							if (aliasName!=null)
							{
								aggAlias.addAlias(aliasName);
							}
							aggAlias.setAlias_agg(aliasOp);

						}	

					}	

					new_view.getColumn_alias().add(aggAlias);				
				}		
			}
		}
	}
  
	public void parseFromStatement(String fromText)
	{
		fromText = fromText.trim();
		if (findFirstMatchIndex(fromText, "FROM(\\s\\(*`?\\w+`?,?)+") != -1)
		{
			
			String tableText = fromText.replace("FROM ","").replace("`", "").replace("(", "").replace(";", "");
			//			//#puts tableText
			//			//#To do: change here for the columns themselves instead of only the name
			tableText = tableText.substring(0, tableText.indexOf(".")) + ":" + tableText.substring(tableText.indexOf(".") + 1);
			String[] possibleTables = tableText.split(",");
			
			
			//			//#puts "Possible tables: " + possibleTables.to_s
			ArrayList<DBRelation> tables = new ArrayList<DBRelation>();
			for (String table : possibleTables)
			{
				String tableName = table.trim();
				DBRelation dbRelation = product.getRelationsMap().get(tableName);
				

				if (dbRelation != null)
				{
					tables.add(dbRelation);
				} else {
					System.out.println("Warning: The table in the FROM statement could not be found: " + tableName); 
				}
			}

			new_view.setTables(tables);

		}
	}

	public void parseWhereStatement(String whereText)
	{
		
		//		//#To do: change here for the columns themselves instead of only the name
		if (findFirstMatchIndex(whereText, "WHERE\\s\\(?\\s?`?\\w+`?\\.`?\\w+`?\\.`?\\w+`?\\s?([\\>\\<=]|!=)\\s?('?(\\w+|\\d+)'?)\\s?\\)?,?;?") != -1)
		{
			String restText = whereText.replace("WHERE ","").replace("`","").replace("\\;", "").replace("\\(", "").replace("\\)","").replace("'","");
			//			//#puts restText

			DbViewRestriction restriction = new DbViewRestriction(new_view);

			if (findFirstMatchIndex(restText, "\\w+.\\w+.\\w+") != -1)
			{
				

				String columnText = getCompleteMatchedText(restText, "\\w+.\\w+.\\w+").trim();
				columnText = columnText.substring(0, columnText.indexOf(".")) + ":" + columnText.substring(columnText.indexOf(".") + 1);
				if (columnText!=null)
				{
					DBAttribute column = product.getAttributesMap().get(columnText);
					if (column!=null)
					{
						restriction.setColumn(column);
					}
					else
					{
						System.out.println("Warning: Column not found in WHERE statement: " + columnText);
					}
				}
				String remainingText = getTextAfterMatch(restText, "\\w+.\\w+.\\w+").trim();

				if (findFirstMatchIndex(remainingText, "[\\>\\<=]|!=") != -1)
				{
					String operator = getCompleteMatchedText(remainingText, "[\\>\\<=]|!=").trim();
					String value = getTextAfterMatch(remainingText, "[\\>\\<=]|!=").trim();

					restriction.setOperator(operator);
					restriction.setValue(value);

				}
				new_view.getRestrictions().add(restriction);
			}
		}
	}

	public void parseJoinStatement(String joinText)
	{
		
		//if (findFirstMatchIndex(joinText, "(JOIN\\s\\(?\\s?`?\\w+`?\\sON\\(*`?\\w+`?\\.`?\\w+`?\\s?(=)\\s*`?\\w+`?\\.`?\\w+`?\\)*\\s*,?;?)+") != -1)
		if (findFirstMatchIndex(joinText, "JOIN") != -1)
		{

			String[] possibleJoins = joinText.split("JOIN");
			for (String join : possibleJoins)
			{

				if (join.length()>1)
				{
					DbViewJoin newJoin = new DbViewJoin(new_view );
					String restText = join.replace("`","").replace("\\;","").trim();
					//					//#puts "join:"+restText
					int tableNamePosition = restText.indexOf("ON");
					String tableName = restText.substring(0, tableNamePosition).trim().replace(".", ":");

					DBRelation table = product.getRelationsMap().get(tableName);
					
					
					if (table!= null)
					{
						newJoin.setTable(table);
					} else {
						System.out.println("The table in the JOIN statement could not be found: " + tableName); 
					}
					String columns = restText.substring(tableNamePosition+2).trim().replace("(", "").replace(")", "");

					int equalPosition = columns.indexOf('=');

					String column1Text = columns.substring(0, equalPosition).trim().replaceFirst("\\.", ":");
					String column2Text = columns.substring(equalPosition + 1).trim().replaceFirst("\\.", ":");

					
					
					DBAttribute column1 = product.getAttributesMap().get(column1Text);
					if (column1!=null)
					{
						newJoin.setColumn1(column1);
					} else {
						System.out.println("The column 1 in the JOIN statement could not be found: " + column1Text); 
					}
					DBAttribute column2 = product.getAttributesMap().get(column2Text);
					if (column2!=null)
					{
						newJoin.setColumn2(column2);

					} else {
						System.out.println("The column 2 in the JOIN statement could not be found: " + column2Text); 
					}
					new_view.getJoins().add(newJoin);
				}
			}	
		}
	}
	//	  
	public void parseGroupByStatement(String groupByText)
	{
		if (findFirstMatchIndex(groupByText, "GROUP\\sBY(\\s\\(*`?\\w+`?,?)+") != -1)
		{
			String columnText = groupByText.replace("GROUP BY ","").replace("`","").replace("(","").replace(";","");
			//			//#puts tableText
			//			//#To do: change here for the columns themselves instead of only the name
			String[] possibleColumns = columnText.split(",");
			//			//#puts "Possible tables: " + possibleTables.to_s
			ArrayList<DBAttribute> columns = new ArrayList<DBAttribute>();
			for (String columnStr : possibleColumns)
			{
				String columnName = columnStr.trim();
				columnName = columnName.substring(0, columnName.indexOf(".")) + ":" + columnName.substring(columnName.indexOf(".") + 1);
				DBAttribute column = product.getAttributesMap().get(columnName);
				if (column!= null)
				{
					columns.add(column);
				} else {
					System.out.println("Warning: The column in the GROUP BY statement could not be found: " + columnName); 
				}
			}

			new_view.setGroup_by_columns(columns);

		}
	}

	public DbView parseView(String viewText, String viewName)
	{
		viewName = viewName.substring(viewName.indexOf(":") + 1);
		viewText = viewText.replace("select", "SELECT");
		viewText = viewText.replace("from", "FROM");
		viewText = viewText.replace("where","WHERE");
		viewText = viewText.replace("join","JOIN");
		viewText = viewText.replace("count","COUNT");
		viewText = viewText.replace("avg","AVG");
		viewText = viewText.replace("min","MIN");
		viewText = viewText.replace("max","MAX");
		viewText = viewText.replace("group by","GROUP BY");
		viewText = viewText.replace(" on"," ON");
		
		
		new_view = new DbView();

		new_view.setDbSchema(product);
		int viewNameEndPosition = viewText.indexOf("AS");
		//parseViewName(viewText.substring(0, viewNameEndPosition));
		new_view.setName(viewName);
		//#Parsing the select part of the view.
		int viewSelectPosition = viewText.lastIndexOf("FROM");
		
		parseSelectStatement(viewText.substring(0, viewSelectPosition));

		//#Parsing the from part of the view.
		int wherePosition = viewText.indexOf("WHERE");
		int joinPosition = viewText.indexOf("JOIN");
		int groupByPosition = viewText.indexOf("GROUP BY");
		//#If there is no where or join keyword, we parse the rest of the view query.
		if (wherePosition == -1 && joinPosition == -1)
		{
			if (groupByPosition == -1)
			{
				parseFromStatement(viewText.substring(viewSelectPosition));

			} else {
				parseFromStatement(viewText.substring(viewSelectPosition, groupByPosition));
				parseGroupByStatement(viewText.substring(groupByPosition));

			}

		}

		if (wherePosition!=-1 && joinPosition==-1)
		{
			parseFromStatement(viewText.substring(viewSelectPosition, wherePosition));

			if (groupByPosition==-1)
			{
				parseWhereStatement(viewText.substring(wherePosition));

			} else {
				parseWhereStatement(viewText.substring(wherePosition, groupByPosition));
				parseGroupByStatement(viewText.substring(groupByPosition));

			}	
		}
		//		
		if (wherePosition==-1 && joinPosition!=-1)
		{
			
			parseFromStatement(viewText.substring(viewSelectPosition,joinPosition ));

			if (groupByPosition==-1)
			{
				parseJoinStatement(viewText.substring(joinPosition));

			} else {
				parseJoinStatement(viewText.substring(joinPosition, groupByPosition));
				parseGroupByStatement(viewText.substring(groupByPosition));

			}	

		}

		if (wherePosition!=-1 && joinPosition!=-1)
		{
			parseFromStatement(viewText.substring(viewSelectPosition, joinPosition));	
			parseJoinStatement(viewText.substring(joinPosition, wherePosition));


			if (groupByPosition == -1)
			{
				parseWhereStatement(viewText.substring(wherePosition));

			} else {
				parseWhereStatement(viewText.substring(wherePosition, groupByPosition));
				parseGroupByStatement(viewText.substring(groupByPosition));

			}	
		}
		//		//#Given that mysql converts the * from the sql query to a list of columns, it is necessary to check if we are doing a coincidence view (all columns). 

		new_view.check_all_columns_included_and_add_all_tables();


		return new_view;
	}
}
