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

package com.cybershare.dbowlizer.db2rdf;

public class XSDs {
private static String xsd="http://www.w3.org/2001/XMLSchema#";


public XSDs(){
	
}
//Annotation based of SQLXML 4.0 
public String getXSDStrings(String columnDataType){	
	if(columnDataType.equals("CHARACTER")||columnDataType.equals("CHARACTER VARYING")||columnDataType.equals("CHARACTER LARGE OBJECT")||columnDataType.equals("CHARACTER LARGE OBJECT")||columnDataType.equals("ENUM")
			||columnDataType.equals("CHAR")||columnDataType.equals("NCHAR")||columnDataType.equals("NTEXT")||columnDataType.equals("NVARCHAR")||columnDataType.equals("VARCHAR")||columnDataType.equals("SYSNAME")
			||columnDataType.equals("TEXT")||columnDataType.equals("UNIQUEIDENTIFIER"))
		return xsd+"string";
	else if(columnDataType.equals("BINARY")||columnDataType.equals("BINARY VARYING")||columnDataType.equals("BINARY LARGE OBJECT"))
		return xsd+"hexBinary";
	else if(columnDataType.equals("NUMERIC")||columnDataType.equals("DECIMAL")||columnDataType.equals("MONEY")||columnDataType.equals("SMALLMONEY"))
		return xsd+"decimal";
	else if(columnDataType.equals("TINYINT"))
		return xsd+"unsignedByte";
	else if(columnDataType.equals("SMALLINT"))
		return xsd+"short";
	else if(columnDataType.equals("INTEGER"))
		return xsd+"integer";
	else if(columnDataType.equals("BIGINT"))
		return xsd+"long";
	else if(columnDataType.equals("FLOAT")||columnDataType.equals("REAL"))
		return xsd+"float";
	else if(columnDataType.equals("DOUBLE PRECISION")||columnDataType.equals("FLOAT UNSIGNED")||columnDataType.contains("FLOAT UNSIGNED"))
		return xsd+"double";
	else if(columnDataType.equals("BOOLEAN")||columnDataType.equals("BIT"))
		return xsd+"boolean";
	else if(columnDataType.equals("DATE"))
		return xsd+"date";
	else if(columnDataType.equals("TIME"))
		return xsd+"time";
	else if(columnDataType.equals("TIMESTAMP")||columnDataType.equals("DATETIME")||columnDataType.equals("SMALLDATETIME"))
		return xsd+"dateTime";
	else if(columnDataType.equals("INT"))
		return xsd+"int";
	else if(columnDataType.equals("INT UNSIGNED"))
		return xsd+"integer";
	else if(columnDataType.equals("IMAGE")||columnDataType.equals("VARBINARY"))
		return xsd+"base64Bianry";
	else return xsd;
	}
}