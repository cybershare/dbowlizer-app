
package com.cybershare.dbowlizer.dbmodel;

/**
 *
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */
public interface Visitor {
    public void visit(DBRelation dbrelation);
    public void visit(DBAttribute dbattribute);
    public void visit(DBQuery dbquery);
    public void visit(DBQueryCondition dbquerycondition);
    public void visit(DBQueryJoin dbqueryjoin);
    public void visit(DBAttributeAlias attributealias);
    public void visit(DBView dbview);
    public void visit(DBCandidateKey dbcandidatekey);
    public void visit(DBPrimaryKey dbprimarykey);
    public void visit(DBForeignKey dbforeignkey);
    public void visit(DBSchema dbschema);
    public void visit(DBAttributeDomain dbattributedomain);
    public void visit(DBAttributeRestriction dbattributerestriction);
}
