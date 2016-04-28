
package com.cybershare.dbowlizer.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cybershare.dbowlizer.db2rdf.R2RMLfactory;
import com.cybershare.dbowlizer.build.Builder;
import com.cybershare.dbowlizer.build.Director;
import com.cybershare.dbowlizer.build.ModelProduct;
import com.cybershare.dbowlizer.dbmodel.DBAttribute;
import com.cybershare.dbowlizer.dbmodel.DBCandidateKey;
import com.cybershare.dbowlizer.dbmodel.DBForeignKey;
import com.cybershare.dbowlizer.dbmodel.DBPrimaryKey;
import com.cybershare.dbowlizer.dbmodel.DBRelation;
import com.cybershare.dbowlizer.dbmodel.DBSchema;
import com.cybershare.dbowlizer.dbmodel.DBView;
import com.cybershare.dbowlizer.generate.MappedEntitiesBundle;
import com.cybershare.dbowlizer.generate.OutputOntologyGenerator;
import com.cybershare.dbowlizer.ontology.OWLUtils;
import com.cybershare.dbowlizer.ontology.OWLVisitor;
import com.cybershare.dbowlizer.utils.DriverSelector;
import com.cybershare.dbowlizer.utils.Settings;

/**
 *
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
public class Service {

    /**
     * @param args the command line arguments
     */
	
	public static void main(String[] args){
		
	    //Instantiate JSON Parser
        JSONParser parser = new JSONParser();
        Object obj = null;
		String path = "C:/Users/Luis/Documents/Git Projects/dbowlizer-app/dbowlizer/src/main/java/com/cybershare/dbowlizer/json";
		try {
			obj = parser.parse(new FileReader(path +  "/test.json"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject jsonInput =  (JSONObject)obj;
		JSONObject dbsettings = (JSONObject)jsonInput.get("dbsettings");
		if (dbsettings != null){
			String driver = dbsettings.get("driver").toString();
			String host = dbsettings.get("host").toString();
			String port = dbsettings.get("port").toString();
			String dbname = dbsettings.get("dbname").toString();
			String user = dbsettings.get("user").toString();
			String password = dbsettings.get("password").toString();
		}

	}
    
    public static void service(Settings settings) {
        
        /* Database Connector */
        DriverSelector selector = new DriverSelector(settings.getDriver());
        selector.setDataSource(settings.getHost(), settings.getPort(), settings.getDbname(), null);
        final Connection connection = selector.getConnection(settings.getUser(), settings.getPassword());
        
        //setup the empty product
        ModelProduct product = new ModelProduct();
        
        //setup the builder of the product
        Builder builder = new Builder(product);
        
        //setup the builder director
        Director director = new Director(builder);
        
        
        //get the data source and pass to the director
        director.construct(connection, selector);
        
        //create visitor to convert model product to axioms
        OWLUtils bundle = new OWLUtils(settings);
        OWLVisitor visitor = new OWLVisitor(bundle);
        
        //visit models and contruct axioms  
        for(DBAttribute attribute : product.getAttributes())
            visitor.visit(attribute);
        
        for(DBRelation relation : product.getRelations())
            visitor.visit(relation);
        
        for(DBSchema schema : product.getSchemas())
            visitor.visit(schema);
        
        for(DBPrimaryKey primarykey: product.getPrimaryKeys())
            visitor.visit(primarykey);
        
        for(DBForeignKey foreignkey : product.getForeignKeys())
            visitor.visit(foreignkey);
        
        for(DBCandidateKey candidatekey: product.getCandidateKeys())
            visitor.visit(candidatekey);
        
        for(DBView view : product.getViews())
            visitor.visit(view);
        
        
        //dump resulting ontology
        bundle.saveOntology();
        
        OutputOntologyGenerator oPOG = new OutputOntologyGenerator(bundle, product, settings);
        MappedEntitiesBundle mappedEntitiesBundle = oPOG.createMappingOutputOntology();
        
        R2RMLfactory r2rmlFactory= new R2RMLfactory(product,mappedEntitiesBundle, settings);
        r2rmlFactory.startProduction();
        
    }
    
}
