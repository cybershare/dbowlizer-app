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

package com.cybershare.dbowlizer.dbmodel;

public class DBCandidateKey extends Element{

    private String candidateKeyName;
    private DBAttribute keyAttribute;

    public DBCandidateKey(String identification) {
        super(identification);
    }

    public DBAttribute getKeyAttribute() {
        return keyAttribute;
    }

    public void setKeyAttribute(DBAttribute keyAttribute) {
        this.keyAttribute = keyAttribute;
    }
    
    public String getCandidateKeyName() {
        return candidateKeyName;
    }

    public void setCandidateKeyName(String candidateKeyName) {
        this.candidateKeyName = candidateKeyName;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
