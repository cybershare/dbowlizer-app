package com.cybershare.dbowlizer.vocabulary;

import com.cybershare.dbowlizer.ontology.OWLUtils;

/**
 * 
 * @author Luis Garnica <lagarnicachavira at miners.utep.edu>
 */

public abstract class Vocabulary {
    protected OWLUtils bundle;

    public Vocabulary(OWLUtils bundle) {
        this.bundle = bundle;
    }
    
    public abstract String getNamespace(); 
}
