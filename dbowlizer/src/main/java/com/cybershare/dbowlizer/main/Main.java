
package com.cybershare.dbowlizer.main;

import java.sql.Connection;

import org.json.simple.JSONObject;

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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /* Load configuration settings */
        Settings settings = new Settings("/schema2owl.config.properties");
        settings.setUuid("312312-312312-312312");
        
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
        
        //visit models and construct axioms  
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
        
        
        //dump relational-model ontology
        bundle.saveOntology();
        
        OutputOntologyGenerator oPOG = new OutputOntologyGenerator(bundle, product, settings);
        MappedEntitiesBundle mappedEntitiesBundle = oPOG.createMappingOutputOntology();
        
        R2RMLfactory r2rmlFactory= new R2RMLfactory(product,mappedEntitiesBundle, settings);
        r2rmlFactory.startProduction();
        
        //Output names on a json
        JSONObject jsonObject = new JSONObject();
    	int i=0;
        for(String ontologyName: settings.getOntologyNames()){
        	jsonObject.put("Ontology-Link-" +  i, settings.getOutputURL() + settings.getUuid() + "/" + ontologyName);
        	i++;
        }
        i = 0;
        for(String mappingName: settings.getMappingNames()){
        	jsonObject.put("Mapping-Link-" + i, settings.getOutputURL() + settings.getUuid() + "/" + mappingName);
        	i++;
        }
        
        System.out.println(jsonObject.toJSONString());

    }

}
