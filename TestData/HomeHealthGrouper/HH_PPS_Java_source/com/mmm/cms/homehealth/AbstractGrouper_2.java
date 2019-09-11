/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth;


import com.mmm.cms.homehealth.hipps.HIPPSCode;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.HIPPSCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import java.util.Calendar;
import java.util.Collection;

/**
 * Overrides the original Abstract Grouper
 * 
 * @author 3M Clinical & Economic Research for CMS Home Health
 */
public abstract class AbstractGrouper_2 extends AbstractGrouper {

    public AbstractGrouper_2(Calendar start, Calendar thru, Calendar window, String versionName) {
        super(start, thru, window, versionName);
    }

    /**
     * Returns a non-null double array of int[6][3]. The values in the array
     * elements are as follows:
     * <ul>
     * <li>0 - not scored</li>
     * <li>1 - score during first round</li>
     * <li>2 - score during second round</li>
     * </ul>
     *
     * Stand alone etiology codes are always scored first, then the
     * manifestation/ etiology pair codes are scored second. Codes that are not
     * valid for scoring are not evaluated and there for entry in the array will
     * be 0
     *
     * @param record
     * @return
     */
    @Override
    public int[][] determineScoreOrder(HomeHealthRecordIF record) {
        DiagnosisCodeIF diagCode;
        DiagnosisCodeIF prevDiagCode;
        DiagnosisCodeIF diagCodeOptional;
        int scoreOrder[][] = new int[6][3];

        // loop through the column 2 codes
        for (int diagIdx = 0; diagIdx < 6; diagIdx++) {
            diagCode = record.getDiagnosisCode(diagIdx);

            // for optional V-Codes go across the row to columns 3 & 4
            if (diagCode.isOptionalPaymentCode()) {
                // determine if the 4 column is an etiology
                diagCodeOptional = record.getOptionalDiagnosisCode4(diagIdx);
                if (diagCodeOptional.isValidForScoring()) {
                    // since this a secondary code that is valid for
                    // scoring, then the previous code must its etiology
                    // and so both are scored second
                    scoreOrder[diagIdx][2] = 2;
                    scoreOrder[diagIdx][1] = 2;
                } else {
                    // determine if the 3rd column is available for scoring
                    diagCodeOptional = record.getOptionalDiagnosisCode3(diagIdx);
                    if (diagCodeOptional.isValidForScoring()) {
                        scoreOrder[diagIdx][1] = 1;
                    }
                }
            } else if (diagCode.isValidForScoring()) {
                // if the current code code is a secondary only, determine
                // whether it earns points or the etiology earns points
                if (diagCode.isSecondaryOnly()) {
                    // since this a secondary code that is valid for
                    // scoring, then the previous code must its etiology
                    // and so both are scored second
                    scoreOrder[diagIdx][0] = 2;

                    // August 2011
                    // now find an etiology code prior to this
                    // code that this can be paired with
                    for (int preIdx = diagIdx - 1; preIdx >= 0; preIdx--) {
                        prevDiagCode = record.getDiagnosisCode(preIdx);
                        if (diagCode.isValidEtiologyPairing(prevDiagCode)) {
                            scoreOrder[preIdx][0] = 2;
                            break;
                        }
                    }
                    // August 2011 - end

                } else {
                    // this is a etiology so, score it on the first run
                    scoreOrder[diagIdx][0] = 1;
                }
            }
        }

        return scoreOrder;
    }
    
    public abstract ScoringResultsIF score(HomeHealthRecordIF recordOasisC,
            boolean validateDates, CollectionValidationEditsIF validationEdits,
            Collection<HomeHealthEventListenerIF> listeners);

    /**
     * When scoring a recordOasisC, the data can either come from a OASIS-B or
     * an OASIS-C source. The OASIS-C source needs to be converted to an OASIS-B
     * prior to scoring. Then the parent scoring can be called.
     *
     * @param recordOasisC
     * @param validateDates
     * @return a non-null Scoring Result that contains the scores for clinical /
     * functional, and for NRS as well as the OASIS treatments and flags.
     * @deprecated
     */
    public ScoringResultsIF score(HomeHealthRecordIF recordOasisC,
            boolean validateDates) {
        return score(recordOasisC, validateDates, null, null);
    }

    public HIPPSCodeIF createHippsCode() {
        return new HIPPSCode();
    }
    
    public HIPPSCodeIF createHippsCode(HomeHealthRecordIF record,
			HomeHealthRecordValidatorIF validator,
			PointsScoringEquationsIF clinicalScore,
			PointsScoringEquationsIF functionalScore,
			int nrsScore) {
        return new HIPPSCode(record, validator, clinicalScore, functionalScore, nrsScore);
    }
    
    
}
