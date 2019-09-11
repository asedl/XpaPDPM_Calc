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
 * This holds a list of Codes that can be paired with the owning code.
 *
 * This has been deprecated because it is specific to ICD-9.  Use the
 * EtiologyPairingIF instead.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface EtiologyPairingIF {

    /**
     * gets the list of etiology pairs - only valid when the code is
     * flagged as a secondary only
     *
     * @return a List of diagnosis codes that are valid as the second part
     * of an Allowable Etiology Pair (taken from the original Table 3, Part 1 -
     * acceptable etiology/manifestation pairs).  This list should never be
     * null, but may be empty
     */
     EtiologyPairingListIF getEtiologyPairCodes();

    /**
     * Sets the Allowable Etiology Pair codes for this current code.  The codes
     * in the list should be the second code in the pairs of codes as originally
     * defined in Table 3, Part 1.
     *
     * only valid when the code is flagged as a secondary only
     *
     * @param pairs
     */
     void setEtiologyPairCodes(List<DiagnosisCodeIF> pairs);

    /**
     * check to determine if the code is on the list of code pairs
     * for this diagnosis.
     *
     * @param code
     * @return true if the code is allowed as a pair, otherwise false.  If the
     * this diagnosis does not have a list of pairs, then it also returns false.
     */
     boolean isEtiologyInPairingList(DiagnosisCodeIF etiologyCode);


    /**
     * This determines if the supplied etiology code can be paired with the
     * owning manifestation code.  This is based on a pairing list.  If the
     * pairing list is an inclusionary list and this method returns true, then 
     * that would be the same as isEtiologyInPairingList() == true. If the 
     * pairing list is an exclusionary list and this method returns true, then
     * that would be the same as isEtiologyInPairingList() == false.
     * 
     * @param etiologyCode
     * @param code
     * @return true if the etiology code can be paired with the current code 
     * (which should be a manifestation code)
     */
     boolean isValidEtiologyPairing(DiagnosisCodeIF etiologyCode);

}

