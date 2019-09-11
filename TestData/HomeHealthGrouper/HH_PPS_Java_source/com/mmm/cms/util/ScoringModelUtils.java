/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.util;

import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;
import com.mmm.cms.homehealth.proto.DataManagerIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.DiagnosisScoringGridIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.HomeHealthScoringModelIF;
import java.util.Collection;

/**
 *
 * @author A0DULZZ
 */
public class ScoringModelUtils {
    
    /**
     * This calculates the primary code score, and if the Other Dx has already
     * been scored, then reduce the primary code by the Other Dx value.
     *
     * @param scoringGrid
     * @param primaryAdjustmentRow
     * @param otherAdjustmentRow
     * @return
     */
    public static int calculatePrimaryAdjustment(DataManagerIF grouperDataManager,
            boolean clinical,
            int equationId, DiagnosisScoringGridIF scoringGrid,
            int primaryAdjustmentRow, int otherAdjustmentRow) {
        int primaryScore;

        // if the other row has already been scored, then reduce the primary
        // value
        if (scoringGrid.isCaseMixAdjustmentRowScored(otherAdjustmentRow)) {
            primaryScore = getCaseMixAdjustmentEquation(grouperDataManager, clinical, primaryAdjustmentRow, equationId)
                    - getCaseMixAdjustmentEquation(grouperDataManager, clinical, otherAdjustmentRow, equationId);
        } else {
            primaryScore = getCaseMixAdjustmentEquation(grouperDataManager, clinical, primaryAdjustmentRow, equationId);
        }

        return primaryScore;
    }

    /**
     * This gets the Case Mix Adjustment table value, by essentially getting the
     * caseMixId (i.e. row) and the equation and returning the value.
     *
     * @param caseMixId
     * @param equationId
     * @return the adjustment or 0 if the case mix id is not found, or the
     * adjustment value is blank
     */
    public static int getCaseMixAdjustmentEquation(DataManagerIF grouperDataManager,
            boolean clinical,
            int caseMixId, int equationId) {
        final CaseMixAdjustmentItemIF caseMix = clinical ? grouperDataManager.getCaseMixAdjustment(caseMixId) : grouperDataManager.getNRSCaseMixAdjustment(caseMixId);
        return caseMix == null ? 0 : caseMix.getAdjustment(equationId);
    }

    
//    /**
//     * Returns a non-null double array of int[6][3]. The values in the array
//     * elements are as follows:
//     * <ul>
//     * <li>0 - not scored</li>
//     * <li>1 - score during first round</li>
//     * <li>2 - score during second round</li>
//     * </ul>
//     *
//     * Stand alone etiology codes are always scored first, then the
//     * manifestation/ etiology pair codes are scored second. Codes that are not
//     * valid for scoring are not evaluated and there for entry in the array will
//     * be 0
//     *
//     * @param record
//     * @return
//     */
//    public static int[][] determineScoreOrder_V3312(HomeHealthRecord_IF record) {
//        DiagnosisCodeIF diagCode;
//        DiagnosisCodeIF prevDiagCode;
//        DiagnosisCodeIF diagCodeOptional;
//        int scoreOrder[][] = new int[6][3];
//
//        // loop through the column 2 codes
//        for (int diagIdx = 0; diagIdx < 6; diagIdx++) {
//            diagCode = record.getDiagnosisCode(diagIdx);
//
//            // for optional V-Codes go across the row to columns 3 & 4
//            if (diagCode.isOptionalPaymentCode()) {
//                // determine if the 4 column is an etiology
//                diagCodeOptional = record.getOptionalDiagnosisCode4(diagIdx);
//                if (diagCodeOptional.isValidForScoring()) {
//                    // since this a secondary code that is valid for
//                    // scoring, then the previous code must its etiology
//                    // and so both are scored second
//                    scoreOrder[diagIdx][2] = 2;
//                    scoreOrder[diagIdx][1] = 2;
//                } else {
//                    // determine if the 3rd column is available for scoring
//                    diagCodeOptional = record.getOptionalDiagnosisCode3(diagIdx);
//                    if (diagCodeOptional.isValidForScoring()) {
//                        scoreOrder[diagIdx][1] = 1;
//                    }
//                }
//            } else if (diagCode.isValidForScoring()) {
//                // if the current code code is a secondary only, determine
//                // whether it earns points or the etiology earns points
//                if (diagCode.isSecondaryOnly()) {
//                    // since this a secondary code that is valid for
//                    // scoring, then the previous code must its etiology
//                    // and so both are scored second
//                    scoreOrder[diagIdx][0] = 2;
//
//                    // August 2011
//                    // now find an etiology code prior to this
//                    // code that this can be paired with
//                    for (int preIdx = diagIdx - 1; preIdx >= 0; preIdx--) {
//                        prevDiagCode = record.getDiagnosisCode(preIdx);
//                        if (diagCode.isValidEtiologyPairing(prevDiagCode)) {
//                            scoreOrder[preIdx][0] = 2;
//                            break;
//                        }
//                    }
//                    // August 2011 - end
//
//                } else {
//                    // this is a etiology so, score it on the first run
//                    scoreOrder[diagIdx][0] = 1;
//                }
//            }
//        }
//
//        return scoreOrder;
//    }

    
//    /**
//     * Returns a non-null double array of int[6][3]. The values in the array
//     * elements are as follows:
//     * <ul>
//     * <li>0 - not scored</li>
//     * <li>1 - score during first round</li>
//     * <li>2 - score during second round</li>
//     * </ul>
//     *
//     * Stand alone etiology codes are always scored first, then the
//     * manifestation/ etiology pair codes are scored second. Codes that are not
//     * valid for scoring are not evaluated and there for entry in the array will
//     * be 0
//     *
//     * @param record
//     * @return
//     */
//
//    public int[][] determineScoreOrder(HomeHealthRecord_IF record) {
//        DiagnosisCodeIF diagCode;
//        DiagnosisCodeIF diagCodeOptional;
//        int scoreOrder[][] = new int[6][3];
//
//        // loop through the column 2 codes
//        for (int diagIdx = 0; diagIdx < 6; diagIdx++) {
//            diagCode = record.getDiagnosisCode(diagIdx);
//
//            // for optional V-Codes go across the row to columns 3 & 4
//            if (diagCode.isOptionalPaymentCode()) {
//                // determine if the 4 column is an etiology
//                diagCodeOptional = record.getOptionalDiagnosisCode4(diagIdx);
//                if (diagCodeOptional.isValidForScoring()) {
//                    // since this a secondary code that is valid for
//                    // scoring, then the previous code must its etiology
//                    // and so both are scored second
//                    scoreOrder[diagIdx][2] = 2;
//                    scoreOrder[diagIdx][1] = 2;
//                } else {
//                    // determine if the 3rd column is available for scoring
//                    diagCodeOptional = record.getOptionalDiagnosisCode3(diagIdx);
//                    if (diagCodeOptional.isValidForScoring()) {
//                        scoreOrder[diagIdx][1] = 1;
//                    }
//                }
//            } else if (diagCode.isValidForScoring()) {
//                // if the current code code is a secondary only, determine
//                // whether it earns points or the etiology earns points
//                if (diagCode.isSecondaryOnly()) {
//                    // since this a secondary code that is valid for
//                    // scoring, then the previous code must its etiology
//                    // and so both are scored second
//                    scoreOrder[diagIdx][0] = 2;
//                    scoreOrder[diagIdx - 1][0] = 2;
//                } else {
//                    // this is a etiology so, score it on the first run
//                    scoreOrder[diagIdx][0] = 1;
//                }
//            }
//        }
//
//        return scoreOrder;
//    }
//
    
    /**
     * Given two codes that are part of a Manifestation/Etiology pair, determine
     * which code has the highest score and remove the value for the code with
     * the lowest score. Tie goes to the Etiology code.
     *
     * August 2011 - Since there may be multiple manifestation codes for an
     * etiology code, if the etiology code wins the contention, it should be
     * marked as having won. So, the next time an etiology/manifestation
     * contention occurs with the same etiology, since the etiology already
     * scored, then the manifestation would automatically win the contention.
     * Basically, ensuring the requirement that a code can not score twice.
     *
     * When an etiology wins the contention, the manifestation code's score
     * should be cleared. When the manifestation wins the contention, the
     * etiology code's score should be clear, but only if the etiology had not
     * won a previous contention.
     *
     * @param etiologyCode
     * @param etiologyIdx
     * @param manifestionCode
     * @param manifestIdx
     * @param scoringGrid
     */
    public static void resolveEtiologyManifestationContention(
            HomeHealthGrouperIF grouper,
            HomeHealthScoringModelIF scoringModel,
            DiagnosisCodeIF etiologyCode, int etiologyIdx,
            DiagnosisCodeIF manifestionCode, int manifestIdx,
            DiagnosisScoringGridIF scoringGrid,
            Collection<HomeHealthEventListenerIF> listeners) {

        // if the etiology has already scored due to winning a contention,
        // so the mani wins the contention this time. So, both codes just 
        // keep their scores - i.e. can not wipe put the etiology score
        // because it scored earlier
        if (scoringGrid.getScoredByWinningContention(etiologyIdx)) {
            ScoringEventFormatter.fireScoringGeneral(listeners, grouper, scoringModel,
                    "Manifestation/Etiology Contention: Previous etiology code ",
                    etiologyCode.getCode(), " already scored due to previous contention.");
            return;
        }

        // the previous code may have been a valid manifestation/
        // etiology pair code, but not a case mix code - i.e. not scored, so
        // the etiology score would be 0
        if (scoringGrid.getTotalScoreForDiagnosis(manifestIdx)
                > scoringGrid.getTotalScoreForDiagnosis(etiologyIdx)) {

            // clear the Etiology score
            scoringGrid.clearScore(etiologyIdx);

            ScoringEventFormatter.fireScoringGeneral(listeners, grouper, scoringModel,
                    "Manifestation/Etiology Contention: Previous etiology code ",
                    etiologyCode.getCode(), " loses score for contention.");
        } else {
            // clear the Manifestion score
            scoringGrid.clearScore(manifestIdx);

            // set the eitology's winning flag
            scoringGrid.setScoredByWinningContention(etiologyIdx, true);

            ScoringEventFormatter.fireScoringGeneral(listeners, grouper, scoringModel,
                    "Manifestation/Etiology Contention: Manifestation code ",
                    manifestionCode.getCode(), " loses score for contention.");
        }
    }



}
