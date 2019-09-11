/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.vut;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.vut.proto.OasisValidationEditIF;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This is just an empty list of edits which is used for times when the OASIS
 * record is assumed to be correct, and therefore the validation is bypassed
 * without errors.
 *
 * @author Tim Gallagher, 3M Clinical & Economic Research for CMS Home Health
 */
public class CollectionValidationEdits_Empty implements CollectionValidationEditsIF {

    public final static CollectionValidationEdits_Empty INSTANCE = new CollectionValidationEdits_Empty();

    @Override
    public List<OasisValidationEditIF> getEdits(OasisEditIF oeen) {
        return null;
    }

    @Override
    public List<OasisValidationEditIF> getEdits(String string) {
        return null;
    }

    @Override
    public boolean isEditPresent(OasisEditIF editType) {
        return false;
    }

    @Override
    public boolean isEditPresent(String dataItemName) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<OasisValidationEditIF> iterator() {
        return EmptyIterator.INSTANCE;
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return a;
    }

    @Override
    public boolean add(OasisValidationEditIF e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override

    public boolean addAll(Collection<? extends OasisValidationEditIF> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends OasisValidationEditIF> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public OasisValidationEditIF get(int index) {
        return null;
    }

    @Override
    public OasisValidationEditIF set(int index, OasisValidationEditIF element) {
        return null;
    }

    @Override
    public void add(int index, OasisValidationEditIF element) {

    }

    @Override
    public OasisValidationEditIF remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return -1;
    }

    @Override
    public ListIterator<OasisValidationEditIF> listIterator() {
        return EmptyIterator.INSTANCE;
    }

    @Override
    public ListIterator<OasisValidationEditIF> listIterator(int index) {
        return EmptyIterator.INSTANCE;
    }

    @Override
    public List<OasisValidationEditIF> subList(int fromIndex, int toIndex) {
        return null;
    }

}
