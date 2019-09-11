/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth.proto;

import java.util.List;

/**
 * Holds a list of codes that can be used to pair with the owning code.  The
 * list can be set as an inclusionary or exclusionary list, but can only be  
 * set once, preferably during the constructor of the implimennting class.
 * By default the implementing class should set the inclusionary flag to true.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface EtiologyPairingListIF extends List<DiagnosisCodeIF> {

    /**
     * flag determining if the list has changed.
     *
     * @return true if the list has changed without being reorganized
     */
    boolean isDirty();

    /**
     * flag determining if the list is an Inclusionary list (which should be
     * the default) or an exclusionary list
     *
     * @return true if the list is an Inclusionary list
     */
    boolean isInclusionary();

    /**
     * sets the dirty flag and is useful if the list organized outside of this
     * implementation.
     * 
     * @param dirty
     */
    void setDirty(boolean dirty);

    /**
     * Sets the flag to indicate that the list is an Inclusionary or 
     * Exclusionary list.  This flag should be able to be set once there are
     * codes in the list and should throw an IllegalStateException.  
     * 
     * @param bool
     */
    void setInclusionary(boolean bool) throws IllegalStateException;

    /**
     * This determines if the supplied etiology code can be paired with the
     * owning manifestation code.  If this pairing list is an inclusionary
     * list and this method returns true, then that would be the same as
     * isContained() == true. If the pairing list is an exclusionary list and
     * this method returns true, then that would be the same as
     * isContained() == false.
     *
     * @param code
     * @return true if the etiology code can be paired with the current code
     * (which should be a manifestation code)
     */
    boolean isValidEtiologyPairing(DiagnosisCodeIF etiologyCode);

}

