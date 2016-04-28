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
