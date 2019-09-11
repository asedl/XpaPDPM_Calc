/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.vut;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * Provides an empty iterator when an iterator is required but checking for a
 * null iterator is impractical
 *
 * @author Tim Gallagher, 3M Clinical & Economic Research for CMS Home Health
 */
public class EmptyIterator implements Iterator, ListIterator {

    public final static EmptyIterator INSTANCE = new EmptyIterator();

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }

    @Override
    public void remove() {
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public Object previous() {
        return null;
    }

    @Override
    public int nextIndex() {
        return -1;
    }

    @Override
    public int previousIndex() {
        return -1;
    }

    @Override
    public void set(Object e) {
        
    }

    @Override
    public void add(Object e) {
        
    }
    
    
}
