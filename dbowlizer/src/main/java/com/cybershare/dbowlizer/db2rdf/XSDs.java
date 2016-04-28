package com.cybershare.dbowlizer.db2rdf;

import schemacrawler.schema.ColumnDataType;


/**
 * 
 * @author Eric Camacho <eicamacho at miners.utep.edu>
 */
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
		return xsd+"positiveInteger";
	else if(columnDataType.equals("IMAGE")||columnDataType.equals("VARBINARY"))
		return xsd+"base64Bianry";
	else return xsd;
	}
}