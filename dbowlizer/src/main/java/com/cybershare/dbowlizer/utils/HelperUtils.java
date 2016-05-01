/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.cybershare.dbowlizer.utils;

import java.net.URI;
import java.security.MessageDigest;
import java.util.Formatter;

/**
 *
 * @author Luis
 */

public class HelperUtils {
    
    public static String remNoneAlphaNum(String param){
        return param.replaceAll("[^a-zA-Z0-9_]", "");
    }
    
    
    public static String SHAsum(byte[] convertme){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return byteArray2Hex(md.digest(convertme));
        }catch(Exception e){e.printStackTrace();}
        return null;
    }
    
    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String hex = formatter.toString();
        formatter.close();
        return hex;
    }
    
    public static String makeURICompliantFragment(String candidateIRIFragment, String baseURI){
        String newIRIFragment = candidateIRIFragment.replaceAll(" ", "-");
        
        //replacing html encoded ampersons
        newIRIFragment = newIRIFragment.replaceAll("&amp;", "and");
        
        if(!baseURI.endsWith("/"))
            baseURI += "/";
        
        try{
            new URI(baseURI + newIRIFragment);
            return newIRIFragment;
        }
        catch(Exception e){
            System.err.println("Offending Name: " + candidateIRIFragment);
            for(int i = 0; i < candidateIRIFragment.length(); i ++)
                System.err.println("character: " + candidateIRIFragment.charAt(i) + ", value: " + candidateIRIFragment.codePointAt(i));
            e.printStackTrace();
        }
        return null;
    }
    
    public static URI isURI(String uriString){
        URI uri = null;
        try{uri = new URI(uriString);}
        catch(Exception e){e.printStackTrace();}
        return uri;
    }
    
    public static String getAttributeDomain(String columnDataType){	
    	if(columnDataType.equals("CHARACTER")||columnDataType.equals("CHARACTER VARYING")||columnDataType.equals("CHARACTER LARGE OBJECT")||columnDataType.equals("CHARACTER LARGE OBJECT")||columnDataType.equals("ENUM")
    			||columnDataType.equals("CHAR")||columnDataType.equals("NCHAR")||columnDataType.equals("NTEXT")||columnDataType.equals("NVARCHAR")||columnDataType.equals("VARCHAR")||columnDataType.equals("SYSNAME")
    			||columnDataType.equals("TEXT")||columnDataType.equals("UNIQUEIDENTIFIER"))
    		return "dbowl_string";
    	else if(columnDataType.equals("BINARY")||columnDataType.equals("BINARY VARYING")||columnDataType.equals("BINARY LARGE OBJECT"))
    		return "dbowl_integer";
    	else if(columnDataType.equals("NUMERIC")||columnDataType.equals("DECIMAL")||columnDataType.equals("MONEY")||columnDataType.equals("SMALLMONEY"))
    		return "dbowl_float";
    	else if(columnDataType.equals("TINYINT"))
    		return "dbowl_integer";
    	else if(columnDataType.equals("SMALLINT"))
    		return "dbowl_integer";
    	else if(columnDataType.equals("INTEGER"))
    		return "dbowl_integer";
    	else if(columnDataType.equals("BIGINT"))
    		return "dbowl_integer";
    	else if(columnDataType.equals("FLOAT")||columnDataType.equals("REAL"))
    		return "dbowl_float";
    	else if(columnDataType.equals("DOUBLE PRECISION")||columnDataType.equals("FLOAT UNSIGNED")||columnDataType.contains("FLOAT UNSIGNED"))
    		return "dbowl_double";
    	else if(columnDataType.equals("BOOLEAN")||columnDataType.equals("BIT"))
    		return "dbowl_string";
    	else if(columnDataType.equals("DATE"))
    		return "dbowl_date";
    	else if(columnDataType.equals("TIME"))
    		return "dbowl_string";
    	else if(columnDataType.equals("TIMESTAMP")||columnDataType.equals("DATETIME")||columnDataType.equals("SMALLDATETIME"))
    		return "dbowl_string";
    	else if(columnDataType.equals("INT"))
    		return "dbowl_integer";
    	else if(columnDataType.equals("INT UNSIGNED"))
    		return "dbowl_integer";
    	else if(columnDataType.equals("IMAGE")||columnDataType.equals("VARBINARY"))
    		return "dbowl_string";
    	else return "dbowl_string";
    	}
    
}
    
