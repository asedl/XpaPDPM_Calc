package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.EtiologyPairingListIF;
import com.mmm.cms.util.DiagnosisCodeComparator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * An list of Etiology pair related codes. The list can be an inclusionary list
 * or an exclusionary list.
 */
public class EtiologyPairsList extends ArrayList<DiagnosisCodeIF>
		implements EtiologyPairingListIF {

	/**
	 * if the object is dirty, then it is sorted prior to searching.
	 */
	private boolean dirty;
	/**
	 * Indicates if the list is inclusionary (i.e. that is should be used to
	 * check that a code is included) or exclusionary (i.e. that the codes in
	 * the list should not be pair with the code owning this list)
	 */
	private boolean inclusionary;

	public EtiologyPairsList() {
		super(20);
		inclusionary = true;
	}

	public EtiologyPairsList(boolean inclusionary) {
		super(20);
		this.inclusionary = inclusionary;
	}

	@Override
	public boolean add(DiagnosisCodeIF dxCode) {
		dirty = true;
		return super.add(dxCode);
	}

	@Override
	public void add(int index, DiagnosisCodeIF element) {
		dirty = true;
		super.add(index, element);
	}

	@Override
	public boolean addAll(int index, Collection<? extends DiagnosisCodeIF> collection) {
		dirty = true;
		return super.addAll(index, collection);
	}

	/**
	 * Determines if a code is on the list
	 *
	 * @param code
	 * @return true if the code is on the list
	 */
	public boolean contains(DiagnosisCodeIF code) {
		int idx;
		if (dirty) {
			Collections.sort(this, DiagnosisCodeComparator.codeComparator);
			dirty = false;
		}
		idx = Collections.binarySearch(this, code, DiagnosisCodeComparator.codeComparator);
		return idx >= 0;
	}

	/**
	 * Gets the "change" flag
	 *
	 * @return true if the list has recently changed
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Gets the list type - inclusionary, or exclusionary
	 *
	 * @return true if the list is for included codes, false if the list is for
	 * exclusion codes.
	 */
	@Override
	public boolean isInclusionary() {
		return inclusionary;
	}

	/**
	 * This determines if the supplied etiology code can be paired with the
	 * owning manifestation code.
	 *
	 * If the "etiology code" is an Optional Payment code, a valid code that
	 * does not belong to a Diagnosis Group, or is actually a manifestation only
	 * code, this will will always return false, regardless if this is and
	 * inclusionary or exclusionary list.
	 *
	 * Give that the "etiology code" passes the criteria above, if this pairing
	 * list is an inclusionary list and this method returns true, then that
	 * would be the same as isContained() == true. If the pairing list is an
	 * exclusionary list and this method returns true, then that would be the
	 * same as isContained() == false.
	 *
	 * @param code
	 * @return true if the etiology code can be paired with the current code
	 * (which should be a manifestation code)
	 */
	@Override
	public boolean isValidEtiologyPairing(DiagnosisCodeIF etiologyCode) {
		boolean isValid;

		/*
		 * if an inclusionary list, then the code must be contained within it
		 */
		if (inclusionary) {
			isValid = contains(etiologyCode);
		} else {
			/*
			 * if an exclusionary list, then the code must NOT be contained within
			 * it, and it can not be a payment, e-code or other manifestation
			 */
			if (etiologyCode.isVCode()
					|| etiologyCode.isExternalCauseCode()
					|| etiologyCode.isSecondaryOnly()) {
				isValid = false;
			} else {
				// not on the list
				isValid = !contains(etiologyCode);
			}
		}
		return isValid;
	}

	@Override
	public DiagnosisCodeIF remove(int index) {
		dirty = true;
		return super.remove(index);
	}

	@Override
	public boolean remove(Object obj) {
		dirty = true;
		return super.remove(obj);
	}

	@Override
	public DiagnosisCodeIF set(int index, DiagnosisCodeIF element) {
		dirty = true;
		return super.set(index, element);
	}

	/**
	 * sets the dirty flag. If the flag is true, then the next time the list is
	 * accessed it will be re-organized.
	 *
	 * @param dirty
	 */
	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * sets the inclusionary flag for this list. If the new value is different
	 * from the current value and there are codes in the list, then this will
	 * throw the IllegalStateException. The Diagnosis codes definitions should
	 * be checked to be consistent, since a code can only one type of pairing
	 * list and not a combination of the two types of lists.
	 *
	 * @param bool
	 * @throws java.lang.IllegalStateException
	 */
	@Override
	public void setInclusionary(boolean bool) throws IllegalStateException {
		if (bool != inclusionary && size() > 0) {
			throw new IllegalStateException("Inclusionary flag can not be reset after codes have been added to the list. Please check the data for valid code Inclusion/exclusion definitions.");
		}
		inclusionary = bool;
	}
}
