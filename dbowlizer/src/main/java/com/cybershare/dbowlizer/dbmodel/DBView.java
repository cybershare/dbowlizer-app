/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cybershare.dbowlizer.dbmodel;


/**
 * 
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
public class DBView extends Element{
  private String viewName;
  private String definition;

  public DBView(String identification) {
    super(identification);
  }

  public String getViewName() {
    return viewName;
  }

  public void setViewName(String relationName) {
    this.viewName = relationName;
  }

  public String getDefinition() {
    return definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
