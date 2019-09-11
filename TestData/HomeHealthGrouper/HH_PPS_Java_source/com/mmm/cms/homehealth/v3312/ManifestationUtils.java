/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.v3312;

import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.EtiologyPairingListIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;

/**
 * This supplies utility methods for helping with the new Manifestation logic.
 *
 * @author Tim Gallagher, 3M HIS, Clinical & Economic Research
 */
public class ManifestationUtils {

    /**
     * This will find the code that should be paired with the supplied
     * manifestation code.
     *
     * There are slightly different pairing criteria between standard
     * manifestation codes (inclusionary codes) and gangrene codes
     * (exclusionary) codes.
     *
     * For Inclusionary Manifestation/Etiology pairing (normal) (Optional
     * payment codes followed by x3/x4 has no logic change whatsoever)
     *
     * If you come across a Manifestation code: <ol> <li>go up (column 1 only)
     * and check for the closest matching Etiology code (you can go up all the
     * way to row 1 if necessary)</li> <li>if you find a matching code, Man/Eti
     * can score (using contention).</li> <li>if you do not find a matching
     * code, Man does not score</li> </ol>
     *
     * For Exclusionary Manifestation/Etiology pairing (Gangrene) (Optional
     * payment codes followed by x3/x4 has no logic change whatsoever)
     *
     * If you come across a Gangrene code: <ol> <li>go up (column 1 only) until
     * you find the closest non-Manifestation code</li> <li>if you find a
     * non-Man code, <ol> <li>) if code is a payment code or E-code, Gangrene
     * does not score (anti-pair) <li>) if code is on gangrene exclusion list,
     * Gangrene does not score (anti-pair) <li>) if code is not on exclusion
     * pair, Gangrene can score (using contention) </ol></li> <li>if you do not
     * find a non-Man code (i.e. run out), Gangrene does not score</li> </ol>
     *
     * @param record
     * @param diagCode
     * @param diagIdx
     * @return
     */
    // new in v6117 and OASIS C2 v2.20
    public static DiagnosisCodeIF getExclusionCode(HomeHealthGrouperIF grouper,
            HomeHealthRecordIF record, DiagnosisCodeIF diagCode,
            int diagIdx) {
        DiagnosisCodeIF foundPairingCode = null;
        EtiologyPairingListIF pairList = diagCode.getEtiologyPairCodes();
        
        if(pairList == null) {
            return null;
        }

        if (pairList.isInclusionary()) {
            return null; // don't check Inclusionary Manifestation
        } else {
            /*
			 * Manifestation Exclusionary Pairs - i.e. Gangrene Finding a
			 * pairing code for exclusionary codes means finding the first
			 * non-Manifestation code previous to this code and going no
			 * further. Then check on the code to determine if it is a valid
			 * pair. Refer to isValidEtiologyPairing() for more information.
             */

            for (int i = 0; i < 6; i++) { // 6 DXs total
                if (i == diagIdx) { // skip itself
                    continue;
                }
                DiagnosisCodeIF dx = record.getDiagnosisCode(i);
                
                if (pairList.contains(dx)) {
                    foundPairingCode = dx;
                    foundPairingCode.setOffset(i);
                    break;
                }
            }
        }
        return foundPairingCode;
    }

    public static DiagnosisCodeIF getPairedCode(HomeHealthGrouperIF grouper,
            HomeHealthRecordIF record, DiagnosisCodeIF diagCode,
            int diagIdx) {
        DiagnosisCodeIF foundPairingCode = null;
        DiagnosisCodeIF prevDiagCode;
        int preIdx;

        if (diagCode.getEtiologyPairCodes().isInclusionary()) {
            /*
			 * Manifestation Inclusionary pairing now find an etiology code
			 * prior to this code that this can be paired with
             */
            for (preIdx = diagIdx - 1; preIdx >= 0; preIdx--) {
                prevDiagCode = record.getDiagnosisCode(preIdx);
                if (diagCode.isValidEtiologyPairing(prevDiagCode)) {
                    foundPairingCode = prevDiagCode;
                    foundPairingCode.setOffset(preIdx);
                    break;
                }
            }
        } else {
            /*
			 * Manifestation Exclusionary Pairs - i.e. Gangrene Finding a
			 * pairing code for exclusionary codes means finding the first
			 * non-Manifestation code previous to this code and going no
			 * further. Then check on the code to determine if it is a valid
			 * pair. Refer to isValidEtiologyPairing() for more information.
             */
            for (preIdx = diagIdx - 1; preIdx >= 0; preIdx--) {
                prevDiagCode = record.getDiagnosisCode(preIdx);

                // skip secondary only codes
                if (!prevDiagCode.isSecondaryOnly()) {
                    if (diagCode.isValidEtiologyPairing(prevDiagCode)) {
                        foundPairingCode = prevDiagCode;
                        foundPairingCode.setOffset(preIdx);
                    }
                    break;
                }
            }
        }
        return foundPairingCode;
    }
}
