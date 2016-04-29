package com.cybershare.dbowlizer.views;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cybershare.dbowlizer.dbmodel.DBAttributeAlias;

public class DbViewParser {
	DbView new_view;


	  public DbViewParser()
	  {
		//#new_view=DbView.new
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
	  public void parseSelectStatement(String selectText)
	  {
		if (findFirstMatchIndex(selectText, "SELECT(\\s`?(\\w+`?\\.`?\\w+`?|\\*|((COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\)))\\sAS\\s`?\\w+`?,?)+") != -1)
		{
			String attText=selectText.replace("SELECT ","").replace("`", "");
			//#puts attText
			String[] attTextArray = attText.split(",");
			for (String att : attTextArray)
			{
				if (findFirstMatchIndex(att, "\\w+\\.\\w+") != -1)
				{
					String columnName="//#{$&}";
					
					String columnAlias=("//#{$'}").trim().replace("AS","").trim();
					DBAttributeAlias newAlias = new DBAttributeAlias("id");//TODO: Add new_view
//					newAlias = DbColumnAlias.new(new_view)
					if (columnName!=null)
					{
						//TODO: Waiting for Erick
//						newAlias.addColumn(columnName)

					}
					if (columnAlias!=null)
					{
						//TODO: Waiting for Erick
//						newAlias.addAlias(columnAlias)
					}	
					new_view.getColumn_alias().add(newAlias);
					
				}
				if (findFirstMatchIndex(att, "(COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\)\\sAS\\s(COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\),?") != -1)
				{
					String aggColumn="//#{$&}";
					//TODO:waiting for erick
//					aggAlias=DbColumnAlias.new(new_view)
					if (findFirstMatchIndex(aggColumn, "(COUNT|AVG|MAX|MIN)\\((0|\\*|\\w+\\.\\w+?)\\)\\sAS") != -1)
					{
						String columnText="//#{$&}";
						String aliasText="//#{$'}";
						if (findFirstMatchIndex(columnText, "(COUNT|AVG|MAX|MIN)") != -1)
						{
							String aggOp="//#{$&}";
							String columnName = ("//#{$'}".replace("(", "").replace(")", "").replace("AS","")).trim();
//							//#puts "columnName:"+ columnName
							if (columnName!=null)
							{
								//TODO:
								//aggAlias.addColumn(columnName)
							}
							//TODO:
//							aggAlias.column_agg=aggOp
						}	
						if (findFirstMatchIndex(aliasText, "(COUNT|AVG|MAX|MIN)") != -1)
						{
							String aliasOp="//#{$&}";
							String aliasName=("//#{$'}").replace("(","").replace(")", "").trim();
//							
							if (aliasName!=null)
							{
								//TODO:
//								aggAlias.addAlias(aliasName)
							}
//							aggAlias.alias_agg=aliasOp
						}	
//						//#puts "aliasText:" + (aliasText.lstrip).rstrip
//
					}	
					//#columnAlias=((("//#{$'}").lstrip).rstrip).replace("AS","").lstrip.rstrip
						//#puts "Rest column:" + columnAlia
					
					//TODO:Uncomment
					//new_view.getColumn_alias().add(aggAlias);
//					
				}
//				
			}
		}
	  }
//	  
//	  public void parseFromStatement(fromText)
//	  {
//		
//		if fromText =~/FROM(\s\(*`?\w+`?,?)+/
//			tableText=((fromText.replace("FROM ","").delete"`").delete"(").delete";"
//			//#puts tableText
//			//#To do: change here for the columns themselves instead of only the name
//			possibleTables=tableText.split(',')
//			//#puts "Possible tables: " + possibleTables.to_s
//			tables=Array.new
//			possibleTables.each do |table|
//				tableName=(table.lstrip).rstrip
//				table=new_view.dbSchema.findTableByName(tableName)
//				if table!= null
//					tables.push(table)
//				} else {
//					puts "Warning: The table in the FROM statement could not be found: " + tableName
//				}
//			}
//			//#if possibleTables.length>1
//			new_view.tables=tables
//			//#} else {
//			//#		new_view.tables.push(tableText)
//			//#}
//		}
//	  }
//
//
//	  
//	  public void parseWhereStatement(whereText)
//		//#To do: change here for the columns themselves instead of only the name
//		if whereText =~/WHERE\s\(?\s?`?\w+`?\.`?\w+`?\s?([\>\<=]|!=)\s?('?(\w+|\d+)'?)\s?\)?,?;?/
//			restText=((((whereText.replace("WHERE ","").delete"`").delete"\;").delete"\(").delete"\)").delete"'"
//			//#puts restText
//			restriction=DbViewRestriction.new(new_view)
//			if restText =~/\w+.\w+/
//				columnText="//#{$&}"
//				if columnText!=null 
//					column=new_view.dbSchema.findColumnByName(columnText)
//					if column!=null
//						restriction.column=column
//					}
//				}
//				remainingText=(("//#{$'}").lstrip).rstrip
//				if remainingText=~/[\>\<=]|!=/
//					restriction.operator="//#{$&}"
//					restriction.value=(("//#{$'}").lstrip).rstrip
//				}
//				new_view.restrictions.push(restriction)
//			}
//		}
//	  }
//	  
//	   public void parseJoinStatement(joinText)
//		
//		if joinText =~/(JOIN\s\(?\s?`?\w+`?\sON\(*`?\w+`?\.`?\w+`?\s?(=)\s*`?\w+`?\.`?\w+`?\)*\s*,?;?)+/
//			//#restText=(((joinText.replace("JOIN ","").delete"`").delete"\;").delete"\(").delete"\)"
//			
//			possibleJoins=joinText.split('JOIN')
//			//#puts "Possible joins: " 
//			possibleJoins.each do |join|
//				if join.length>1
//					newJoin=DbViewJoin.new(new_view)
//					restText=(((join.delete"`").delete"\;").lstrip).rstrip
//					//#puts "join:"+restText
//					tableNamePosition = restText.index('ON')
//					tableName=((restText[0..tableNamePosition-1]).lstrip).rstrip
//					table=new_view.dbSchema.findTableByName(tableName)
//					if table!= null
//						newJoin.table=table
//					} else {
//						puts "The table in the JOIN statement could not be found: " + tableName
//					}
//					columns=((((restText[tableNamePosition+2..restText.length]).lstrip).rstrip).delete"\(").delete"\)"
//					equalPosition=columns.index('=')
//					column1Text=(columns[0..equalPosition-1].lstrip).rstrip
//					column2Text=(columns[equalPosition+1..columns.length].lstrip).rstrip
//					column1=new_view.dbSchema.findColumnByName(column1Text)
//					if column1!=null
//						newJoin.column1=column1
//					} else {
//						puts "The column 1 in the JOIN statement could not be found: " + column1Text
//					}
//					column2=new_view.dbSchema.findColumnByName(column2Text)
//					if column2!=null
//						newJoin.column2=column2
//					} else {
//						puts "The column 2 in the JOIN statement could not be found: " + column2Text
//					}
//					new_view.joins.push(newJoin)
//				}
//			}	
//		}
//	  }
//	  
//	  public void parseGroupByStatement(groupByText)	
//		if groupByText =~/GROUP\sBY(\s\(*`?\w+`?,?)+/
//			columnText=((groupByText.replace("GROUP BY ","").delete"`").delete"(").delete";"
//			//#puts tableText
//			//#To do: change here for the columns themselves instead of only the name
//			possibleColumns=columnText.split(',')
//			//#puts "Possible tables: " + possibleTables.to_s
//			columns=Array.new
//			possibleColumns.each do |column|
//				columnName=(column.lstrip).rstrip
//				column=new_view.dbSchema.findColumnByName(columnName)
//				if column!= null
//					columns.push(column)
//				} else {
//					puts "Warning: The column in the GROUP BY statement could not be found: " + columnName
//				}
//			}
//			//#if possibleTables.length>1
//			new_view.group_by_columns=columns
//			//#} else {
//			//#		new_view.tables.push(tableText)
//			//#}
//		}
//	  }
//
//	  
//	  public void parseView(viewText,dbSchema)
//		newView=DbView.new
//		new_view=newView
//		
//		//#puts "\nTo parse view text: "+viewText
//		new_view.dbSchema=dbSchema
//	    viewName}Position = viewText.index('AS')
//	    parseViewName(viewText[0..viewName}Position-1])
//		//#Parsing the select part of the view.
//		viewSelect}Position = viewText.rindex('FROM')
//	    //#puts "= Select:" + viewText[viewName}Position+3..viewSelect}Position-1]	
//		parseSelectStatement(viewText[viewName}Position+3..viewSelect}Position-1])
//		//#Parsing the from part of the view.
//		wherePosition = viewText.index('WHERE')
//		joinPosition = viewText.index('JOIN')
//		groupByPosition = viewText.index('GROUP BY')
//		//#If there is no where or join keyword, we parse the rest of the view query.
//		if wherePosition==null and joinPosition==null 
//			if groupByPosition==null
//				parseFromStatement(viewText[viewSelect}Position..viewText.length])
//				//#viewFrom}Position = wherePosition
//			} else {
//				parseFromStatement(viewText[viewSelect}Position..groupByPosition-1])
//				parseGroupByStatement(viewText[groupByPosition..viewText.length])
//			}	
//			//#puts "= From:" + viewText[viewSelect}Position..viewText.length]
//			//#parseFromStatement(viewText[viewSelect}Position..viewText.length])
//		}
//		
//		if wherePosition!=null and joinPosition==null 
//			//#puts "= From:" + viewText[viewSelect}Position..wherePosition-1]
//			parseFromStatement(viewText[viewSelect}Position..wherePosition-1])
//			//#puts "= Where:" + viewText[wherePosition..viewText.length]
//			if groupByPosition==null
//				parseWhereStatement(viewText[wherePosition..viewText.length])
//				//#viewFrom}Position = wherePosition
//			} else {
//				parseWhereStatement(viewText[wherePosition..viewText.groupByPosition-1])
//				parseGroupByStatement(viewText[groupByPosition..viewText.length])
//			}	
//		}
//		
//	   	if wherePosition==null and joinPosition!=null 
//			//#puts "= From:" + viewText[viewSelect}Position..joinPosition-1]
//			parseFromStatement(viewText[viewSelect}Position..joinPosition-1])	
//			if groupByPosition==null
//				parseJoinStatement(viewText[joinPosition..viewText.length])
//				
//			} else {
//				parseJoinStatement(viewText[joinPosition..viewText.groupByPosition-1])
//				parseGroupByStatement(viewText[groupByPosition..viewText.length])
//			}	
//			//#puts "= Join:" + viewText[joinPosition..viewText.length]
//			//#parseJoinStatement(viewText[joinPosition..viewText.length])
//		}
//		
//		if wherePosition!=null and joinPosition!=null 
//			//#puts "= From:" + viewText[viewSelect}Position..joinPosition-1]
//			parseFromStatement(viewText[viewSelect}Position..joinPosition-1])	
//			//#puts "= Join:" + viewText[joinPosition..wherePosition-1]
//			parseJoinStatement(viewText[joinPosition..wherePosition-1])
//			//#puts "= Where:" + viewText[wherePosition..viewText.length]
//			//#parseWhereStatement(viewText[wherePosition..viewText.length])
//			if groupByPosition==null
//				parseWhereStatement(viewText[wherePosition..viewText.length])
//				
//			} else {
//				parseWhereStatement(viewText[wherePosition..viewText.groupByPosition-1])
//				parseGroupByStatement(viewText[groupByPosition..viewText.length])
//			}	
//		}
//		//#Given that mysql converts the * from the sql query to a list of columns, it is necessary to check if we are doing a coincidence view (all columns). 
//		//#new_view.check_equality(wherePosition,groupByPosition)
//		new_view.check_all_columns_included_and_add_all_tables()
//	    //#puts new_view
//		return new_view
//	  }
}
