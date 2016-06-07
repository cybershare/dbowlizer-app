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
