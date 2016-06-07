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

package com.cybershare.dbowlizer.ontology;

import com.cybershare.dbowlizer.utils.Settings;
import com.cybershare.dbowlizer.vocabulary.RelationalToModel;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;

public class OWLUtils {
    

   private Settings settings;
   private OWLOntology ontology; 
   private OWLDataFactory factory;
   private OWLOntologyManager manager;
   private String baseIRI;
   private String importsOntology;
   private File file;
   
   public OWLUtils(Settings settings) {
        this.settings = settings;    
              
        this.file = new File(settings.getOntologyFile()); 
        settings.addOntologyName("relational-to-ontology-mapping-dbwolizer.owl");
      
        this.baseIRI = settings.getBaseIRI();
        
        this.importsOntology = settings.getImportsOntology();
        
        if(!this.baseIRI.endsWith("/"))
            this.baseIRI = this.baseIRI + "/";
        
        this.manager = OWLManager.createOWLOntologyManager();
        this.factory = manager.getOWLDataFactory();
        
        createNewOntology("relational-model-individuals.owl");
        
    }
    
    public String getIndividualIRI(String individualName){
	return baseIRI + individualName;
     }
    
    //Add a list of axioms into the ontology
    public void addAxioms(List<OWLAxiom> axioms){
        AddAxiom addAxiomChange;
		for(OWLAxiom anAxiom : axioms){
	            addAxiomChange = new AddAxiom(ontology, anAxiom);
		    manager.applyChange(addAxiomChange);
		}
   }
   
   public void createNewOntology(String name){
   
        //IRI ontologyIRI = IRI.create(baseIRI.toString() + name);
        IRI ontologyIRI = IRI.create(baseIRI.toString());
        IRI physicalIRI = IRI.create(settings.getOutputDir() + name);
        
        SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, physicalIRI);
        manager.addIRIMapper(mapper);
        
        try{
            this.ontology = manager.createOntology(ontologyIRI);
        }
        catch(OWLOntologyCreationException e){e.printStackTrace();}
        
        this.importRelationalModelComplexOntology();
        
   }
   
   private void importRelationalModelComplexOntology(){
	   RelationalToModel vocabulary_relationalToModel = new RelationalToModel(this);
	   IRI relationalModelComplexOntologyIRI = IRI.create(this.importsOntology);
	   OWLImportsDeclaration relationalModelImportDeclaration = this.factory.getOWLImportsDeclaration(relationalModelComplexOntologyIRI);
	   AddImport addRelationalModelImport = new AddImport(ontology, relationalModelImportDeclaration);
	   this.manager.applyChange(addRelationalModelImport);
   }
   
   
   public void saveOntology(){
        try {
            System.setProperty("file.encoding","UTF-8");
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            try {
                charset.set(null,null);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(OWLUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(OWLUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                manager.saveOntology(ontology, IRI.create(file.toURI()));
                //add permisions here
            } catch (OWLOntologyStorageException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException ex) {
                Logger.getLogger(OWLUtils.class.getName()).log(Level.SEVERE, null, ex);
	} catch (SecurityException ex) {
           Logger.getLogger(OWLUtils.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   
   //Getters and Setters
    public OWLOntology getOntology() {
        return ontology;
    }

    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    public OWLDataFactory getFactory() {
        return factory;
    }

    public void setFactory(OWLDataFactory factory) {
        this.factory = factory;
    }
    
    public String getBaseIRI() {
        return baseIRI;
    }
    
    public void setBaseIRI(String baseIRI) {
        this.baseIRI = baseIRI;
    }
    
    public OWLOntologyManager getManager() {
        return this.manager;
    }
    
}
