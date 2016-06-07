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

import eu.optique.api.mapping.TriplesMap;

public class TriplesNode{
private TriplesMap triplesMap;
private String nameOfTriplesMap;


	public TriplesNode(TriplesMap trip, String name){
		triplesMap=trip;
		nameOfTriplesMap=name;
	}
	
	public TriplesNode(){
		
	}
	
	public TriplesMap getTriplesMap() {
		return triplesMap;
	}
	
	public void setTriplesMap(TriplesMap triplesMap) {
		this.triplesMap = triplesMap;
	}
	
	public String getNameOfTriplesMap() {
		return nameOfTriplesMap;
	}
	
	public void setNameOfTriplesMap(String nameOfTriplesMap) {
		this.nameOfTriplesMap = nameOfTriplesMap;
	}

}
