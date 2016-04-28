package com.cybershare.dbowlizer.ontology;

import com.cybershare.dbowlizer.utils.HelperUtils;
import com.cybershare.dbowlizer.dbmodel.Element;

import java.net.URI;
import java.util.Hashtable;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;



public class Individuals {

	private static Hashtable<String, OWLNamedIndividual> individuals = new Hashtable<String, OWLNamedIndividual>();
	
	public static boolean doesIndividualExist(Element element, OWLUtils bundle){
		String individualName = HelperUtils.makeURICompliantFragment(element.getIdentification(), bundle.getBaseIRI());
		String individualIRI = bundle.getIndividualIRI(individualName);
		return individuals.get(individualIRI) != null;
	}
	
	public static OWLNamedIndividual getIndividual(URI uri, OWLUtils bundle){
                return getIndividual(uri.toString(), bundle);
	}
	
	public static OWLNamedIndividual getIndividual(Element element, OWLUtils bundle){

		if(element.getIdentification().startsWith("?"))
			System.out.println("boooo");
		
		String individualName = HelperUtils.makeURICompliantFragment(element.getIdentification(), bundle.getBaseIRI());
		String individualIRI = bundle.getIndividualIRI(individualName);
		
		return getIndividual(individualIRI, bundle);
	}
	
	public static OWLNamedIndividual getIndividual(Element element, OWLUtils bundle, String postfix){
		
		if(element.getIdentification().startsWith("?"))
			System.out.println("boooo");
		
		String individualName = HelperUtils.makeURICompliantFragment(element.getIdentification() + postfix, bundle.getBaseIRI());
		String individualIRI = bundle.getIndividualIRI(individualName);
		
		return getIndividual(individualIRI, bundle);
	}
	
	private static OWLNamedIndividual getIndividual(String uri, OWLUtils bundle){		
		OWLNamedIndividual individual = individuals.get(uri);
		
		if(individual == null){
			individual = bundle.getFactory().getOWLNamedIndividual(IRI.create(uri));
			individuals.put(uri, individual);
		}
		return individual;
	}
}
