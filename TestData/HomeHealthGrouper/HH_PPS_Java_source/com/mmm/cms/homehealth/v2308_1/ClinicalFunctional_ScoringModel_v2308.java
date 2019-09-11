/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v2308_1;

import com.mmm.cms.homehealth.ScoringPoints;
import com.mmm.cms.homehealth.DiagnosisCode;
import com.mmm.cms.homehealth.DiagnosisCode_Empty;
import com.mmm.cms.homehealth.GrouperDataManager;
import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.HomeHealthScoringModelIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.ScoringPointsIF;
import com.mmm.cms.util.HomeHealthUtils;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ScoringEventFormatter;
import com.mmm.cms.util.ValidateUtils;
import java.util.Collection;

/**
 * This Model provides scoring for the 4 clinical and functional equations
 * defined for Grouper version 2.03.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ClinicalFunctional_ScoringModel_v2308 implements
        HomeHealthScoringModelIF {

    private GrouperDataManager grouperDataManager;
    private int id;
    private HomeHealthGrouperIF grouper;

    /**
     * Constructor with references to the Grouper, the Data manager, and the
     * equation id
     *
     * @param grouper
     * @param grouperDataManager
     * @param id
     */
    public ClinicalFunctional_ScoringModel_v2308(HomeHealthGrouperIF grouper,
            GrouperDataManager grouperDataManager,
            int equationId) {
        this.grouper = grouper;
        this.grouperDataManager = grouperDataManager;
        this.id = equationId;
    }

    /**
     * This is the main scoring module, which scores the clinical information
     * first and then the functional information.
     *
     * @see #scoreClinical(HomeHealthRecordIF record,
     * HomeHealthRecordValidatorIF validator)
     * @see #scoreFunctional(HomeHealthRecordIF record,
     * HomeHealthRecordValidatorIF validator)
     *
     * @param record
     * @param validator
     * @return ScoringPointsIF that contains the clinical and functional scoring
     * elements. This will never be null
     */
    @Override
    public ScoringPointsIF score(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator) {
        ScoringPoints points;

        points = new ScoringPoints(2);

        // score the clinical and functional parts, assigning them to the results
        points.setScoreAt(0, scoreClinical(record, validator));
        points.setScoreAt(1, scoreFunctional(record, validator));

        return points;
    }

    /**
     * This calls score() without the score order or the listeners which were
     * not implemented in this version
     *
     * @param record
     * @param validator
     * @param scoreOrder - assumed to be null
     * @param listeners - always assumed to be null
     * @return
     */
    @Override
    public ScoringPointsIF score(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, int[][] scoreOrder, Collection<HomeHealthEventListenerIF> listeners) {
        return score(record, validator);
    }

    /**
     * This calls score() without the score order which is not implemented in
     * this version
     *
     * @param record
     * @param validator
     * @param scoreOrder - assumed to be null
     * @return
     */
    @Override
    public ScoringPointsIF score(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, int[][] scoreOrder) {
        return score(record, validator);
    }

    /**
     * There is no preprocessing of the HomeHealth Record for this model, and
     * this method should not be used. If called, this method will throw an
     * Unsupported Operation Exception.
     *
     * @param record
     * @param validator
     */
    @Override
    public void preprocessRecord(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator) {
        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED);
    }

    /**
     * There is no initial scoring of the HomeHealth Record for this model, and
     * this method should not be used. If called, this method will throw an
     * Unsupported Operation Exception.
     *
     * @param record
     * @param validator
     */
    public int[] calculateInitialScore(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator) {
        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED);
    }

    /**
     * Determining the primary diagnosis between the two pairs M0230a/240b or
     * M0246a3/M0246a4. This must be done separately for each equation, because
     * of differences in which codes earn points under each equation, which can
     * determine which diagnoses are recognized for scoring and how many points
     * they earn.
     *
     * Pseudo code lines: 1264 thru 1309
     *
     * @param record
     * @param validator - the validator should have already preformed validation
     * on the current record
     * @param codePoints
     */
    public void determinePrimaryDiagnosis(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int codePoints[]) {

        final DiagnosisCodeIF M0230_PRIMARY_DIAG_ICD;
        final DiagnosisCodeIF M0240_OTH_DIAG1_ICD;
        final DiagnosisCodeIF M0246_PMT_DIAG_ICD_A3;
        final DiagnosisCodeIF M0246_PMT_DIAG_ICD_A4;

        M0230_PRIMARY_DIAG_ICD = record.getPRIMARY_DIAG_ICD();
        M0240_OTH_DIAG1_ICD = record.getOTH_DIAG1_ICD();
        M0246_PMT_DIAG_ICD_A3 = record.getPMT_DIAG_ICD_A3();
        M0246_PMT_DIAG_ICD_A4 = record.getPMT_DIAG_ICD_A4();

        // set nothing to primary at first
        M0230_PRIMARY_DIAG_ICD.setPrimary(false);
        M0240_OTH_DIAG1_ICD.setPrimary(false);
        M0246_PMT_DIAG_ICD_A3.setPrimary(false);
        M0246_PMT_DIAG_ICD_A4.setPrimary(false);

        // first Evaluate M0246
        if (M0246_PMT_DIAG_ICD_A4.isValidForScoring()
                && M0246_PMT_DIAG_ICD_A4.isSecondaryOnly()) {
            // check if A3 is a valid case mix code
            if (M0246_PMT_DIAG_ICD_A3.isValidCode()) {

                // Check if the Diagnosis Groups are the same for both codes
                if (M0246_PMT_DIAG_ICD_A4.getDiagnosticGroup().getId()
                        == M0246_PMT_DIAG_ICD_A3.getDiagnosticGroup().getId()) {

                    // set A3 valid for scoring and not A4
                    M0246_PMT_DIAG_ICD_A3.setPrimary(true);
                } else {
                    // otherwise - score the one with the higher points
                    if (codePoints[12] > codePoints[6]) {
                        M0246_PMT_DIAG_ICD_A4.setPrimary(true);
                    } else {
                        M0246_PMT_DIAG_ICD_A3.setPrimary(true);
                    }
                }
            } else {
                // use A4 for scoring
                M0246_PMT_DIAG_ICD_A4.setPrimary(true);
            }
        } else {
            // check if A3 is a valid case mix code
            if (M0246_PMT_DIAG_ICD_A3.isValidForScoring()) {
                // set A3 valid for scoring and not A4
                M0246_PMT_DIAG_ICD_A3.setPrimary(true);
            } else {
				//-----------------------
                // Evaluation M0230/M0240
                //-----------------------
                // determine if the first secondary code is a manifestion
                if (M0240_OTH_DIAG1_ICD.isSecondaryOnly()) {
                    if (M0230_PRIMARY_DIAG_ICD.isValidCode()) {
                        if (M0230_PRIMARY_DIAG_ICD.getDiagnosticGroup().getId()
                                == M0240_OTH_DIAG1_ICD.getDiagnosticGroup().getId()) {

                            // set the M0230 for scoring as primary
                            M0230_PRIMARY_DIAG_ICD.setPrimary(true);
                        } else {
                            // otherwise - score the one with the higher points
                            if (codePoints[1] > codePoints[0]) {
                                M0240_OTH_DIAG1_ICD.setPrimary(true);
                            } else {
                                M0230_PRIMARY_DIAG_ICD.setPrimary(true);
                            }
                        }
                    } else {
                        // M0240 is used for scoring as primary code
                        M0240_OTH_DIAG1_ICD.setPrimary(true);
                    }

                } else {
                    // set the M0230 for scoring as primary
                    M0230_PRIMARY_DIAG_ICD.setPrimary(true);
                }
            }
        }
    }

    /**
     * Cycle through all diagnosis positions with points. Drop points for
     * non-initial occurrences of any diagnosis groups. Resolve contention
     * within etiology/manifestation pairs when both are casemix diagnoses in
     * different diagnosis groups by retaining the DG with higher score unless
     * that DG has already earned points via a previous occurrence.
     *
     * Pseudo code lines: 1343 thru 1415
     *
     * @param record
     * @param validator
     * @param codePointsSE
     * @param diagGroupsUsed
     * @return
     */
    public boolean[] resolveEtiologyManifestationContention(
            HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int codePointsSE[],
            boolean diagGroupsUsed[]) {
        int diagIdx;
        int groupId;
        boolean pointsLost[];
        DiagnosisCodeIF code;

        pointsLost = new boolean[18];

        code = record.getDiagnosisCode(0);
        groupId = code.getDiagnosticGroup() != null ? (int) code.getDiagnosticGroup().getId() : 0;
        if (groupId > 0 && codePointsSE[0] > 0) {
            diagGroupsUsed[groupId - 1] = true;
        }

        code = record.getDiagnosisCode(6);
        groupId = code.getDiagnosticGroup() != null ? (int) code.getDiagnosticGroup().getId() : 0;
        if (groupId > 0
                && codePointsSE[6] > 0) {
            if (diagGroupsUsed[groupId - 1]) {
                codePointsSE[6] = 0;
                ScoringEventFormatter.fireScoringGeneral(null, grouper, this, "codePointsSE[6] reset to 0");

            } else if (codePointsSE[6] > 0) {
                diagGroupsUsed[groupId - 1] = true;
            }
        }

        code = record.getDiagnosisCode(12);
        groupId = code.getDiagnosticGroup() != null ? (int) code.getDiagnosticGroup().getId() : 0;
        if (groupId > 0 && codePointsSE[12] > 0) {
            if (diagGroupsUsed[groupId - 1]) {
                codePointsSE[12] = 0;
                ScoringEventFormatter.fireScoringGeneral(null, grouper, this, "codePointsSE[12] reset to 0");
            } else {
                if (code.isSecondaryOnly()
                        && codePointsSE[6] > 0) {
                    if (codePointsSE[12]
                            > codePointsSE[6]) {

                        codePointsSE[6] = 0;
						// This next line is not used because in the Abt
                        // DLL, their code is incorrect, and so, in order to
                        // match their results, the next line is taken out
                        pointsLost[6] = true;
                        ScoringEventFormatter.fireScoringGeneral(null, grouper, this, "codePointsSE[6] reset to 0 due to codePointsSE[12] > codePointsSE[6]");
                    } else {
                        codePointsSE[12] = 0;
						// the below line is not part of the original Abt
                        // source code
                        //   pointsLost[12] = true;
                        ScoringEventFormatter.fireScoringGeneral(null, grouper, this, "codePointsSE[12] reset to 0 due to codePointsSE[12] <= codePointsSE[6]");
                    }
                }

                if (codePointsSE[12] > 0) {
                    diagGroupsUsed[groupId - 1] = true;
                }
            }
        }

        // secondary diagnosis only
        for (diagIdx = 1; diagIdx < 6; diagIdx++) {
            code = record.getDiagnosisCode(diagIdx);
            if (code.isValidForScoring()) {
                groupId = code.getDiagnosticGroup() != null ? (int) code.getDiagnosticGroup().getId() : 0;
                if (groupId > 0 && codePointsSE[diagIdx] > 0) {

                    if (diagGroupsUsed[groupId - 1]) {
                        codePointsSE[diagIdx] = 0;
                        ScoringEventFormatter.fireScoringGeneral(null, grouper, this, "codePointsSE[" + diagIdx
                                + "] reset to 0 for diagnosis position "
                                + (diagIdx + 1));

                    } else {
                        if (code.isSecondaryOnly() && codePointsSE[diagIdx - 1] > 0) {

							// determine if the current diagnosis score is greater
                            // than the previous diagnosis score
                            if (codePointsSE[diagIdx] > codePointsSE[diagIdx
                                    - 1]) {
                                codePointsSE[diagIdx - 1] = 0;
								// This next line is not used because in the Abt
                                // DLL, their code is incorrect, and so, in order to
                                // match their results, the next line is taken out
                                pointsLost[diagIdx - 1] = true;
                                ScoringEventFormatter.fireScoringGeneral(null, grouper, this, "codePointsSE[" + (diagIdx
                                        - 1)
                                        + "] reset to 0 for secondary diagnosis position "
                                        + (diagIdx + 1));

                            } else {
                                codePointsSE[diagIdx] = 0;
                                ScoringEventFormatter.fireScoringGeneral(null, grouper, this, "codePointsSE["
                                        + diagIdx
                                        + "] reset to 0 for secondary diagnosis position "
                                        + (diagIdx + 1));
                            }
                        }

                        if (codePointsSE[diagIdx] > 0) {
                            diagGroupsUsed[groupId - 1] = true;
                        }
                    }
                }
            }
        }

        // secondary optional codes - column 3
        for (diagIdx = 7; diagIdx < 12; diagIdx++) {
            code = record.getDiagnosisCode(diagIdx);
            if (code.isValidForScoring()) {
                groupId = (int) code.getDiagnosticGroup().getId();
                if (groupId > 0 && codePointsSE[diagIdx] > 0) {
                    if (diagGroupsUsed[groupId - 1]) {
                        codePointsSE[diagIdx] = 0;
                    } else if (codePointsSE[diagIdx] > 0) {
                        diagGroupsUsed[groupId - 1] = true;
                    }
                }
            }
        }

        // secondary optional codes - column 4
        for (diagIdx = 13; diagIdx < 18; diagIdx++) {
            code = record.getDiagnosisCode(diagIdx);
            if (code.isValidForScoring()) {
                groupId = (int) code.getDiagnosticGroup().getId();

                if (groupId > 0 && diagGroupsUsed[groupId - 1]) {
                    codePointsSE[diagIdx] = 0;
                } else {
                    if (code.isSecondaryOnly() && codePointsSE[diagIdx - 6] > 0) {
                        if (codePointsSE[diagIdx] > codePointsSE[diagIdx - 6]) {
                            codePointsSE[diagIdx - 6] = 0;

							// This next line is not used because in the Abt
                            // DLL, their code is incorrect, and so, in order to
                            // match their results, the next line is taken out
                            pointsLost[diagIdx - 6] = true;

                        } else {
                            codePointsSE[diagIdx] = 0;
                        }
                    } else {
						// this should not have scored to begin with if the
                        // etiology is not a valid code
                        if (!record.getDiagnosisCode(diagIdx
                                - 6).isValidCode()) {
                            codePointsSE[diagIdx] = 0;
                        }
                    }
                }

                if (codePointsSE[diagIdx] > 0) {
                    diagGroupsUsed[groupId - 1] = true;
                }
            }
        }

        return pointsLost;
    }

    /**
     * Recalculate the score of each non-primary diagnosis in casemix variables
     * with different scores for primary vs. other diagnosis by setting that
     * diagnosis’ score to zero and rescoring, giving only the points that are
     * based on being an other diagnosis.
     *
     * Pseudo code lines: 1311 thru 1337
     *
     * @param record
     * @param validator
     * @param codePoints
     * @param dualPointsSE
     */
    public void recalculateNonPrimaryCodes(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int codePoints[],
            int dualPointsSE[]) {
        int idx;
        DiagnosisCodeIF diagCode;
        int groupId;
        int tmpScore;

        // check all diagnosis codes again for non-primary codes
        for (idx = 0; idx < 18; idx++) {
            diagCode = record.getDiagnosisCode(idx);
            if (diagCode.isValidForScoring()) {
                groupId = diagCode.getDiagnosticGroup().getId();

                //  only look at non primary codes that are valid for scoring
                if (!diagCode.isPrimary()) {

                    // check the diagnosis group
                    switch (groupId) {
                        case 4: // diabetes
                            // set the points to case mix 5 values
                            codePoints[idx] = getCaseMixAdjustmentEquation(5, id);
                            ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 5, codePoints[idx]);
                            break;

                        case 10: // Neuro
                            // set points to 0
                            codePoints[idx] -= getCaseMixAdjustmentEquation(12, id);
                            ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this, "For Diagnosis Group 10 - reduced value by from row 12 by "
                                    + getCaseMixAdjustmentEquation(12, id));
                            break;

                        case 19:
                            codePoints[idx] -= getCaseMixAdjustmentEquation(25, id);
                            ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this, "For Diagnosis Group 19 - reduced value by from row 25 by "
                                    + getCaseMixAdjustmentEquation(25, id));

                            // add points to case mix 26 values
                            tmpScore = getCaseMixAdjustmentEquation(26, id);
                            codePoints[idx] += tmpScore;
                            ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 26, tmpScore);

                            break;
                        default:

                    }
                }
            }
        }
    }

    /**
     * This scores the remaining clinical variables.
     *
     * Pseudo code lines: 1435 thru 1490
     *
     * @param record
     * @param validator
     * @param points
     * @return
     */
    public int scoreRemainingVariables(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int[] points) {

        int tmpInt1;
        int tmpInt2;
        int score = 0;
        String tmpStr;
        int tmpScore;

        if (validator.isINTERNAL_LOGIC_Valid()
                && validator.isTHERAPIES_Valid()) {

            if ("1".equals(record.getTHH_IV_INFUSION())
                    || "1".equals(record.getTHH_PAR_NUTRITION())) {
                tmpScore = getCaseMixAdjustmentEquation(31, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_THH_IV_INFUSION_or_THH_PAR_NUTRITION, 31, tmpScore);
            }

            if ("1".equals(record.getTHH_ENT_NUTRITION())) {
                tmpScore = getCaseMixAdjustmentEquation(32, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_THH_ENT_NUTRITION, 32, tmpScore);
            }
        }

        if (ValidateUtils.isValidValue(record.getVISION(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {
            tmpScore = getCaseMixAdjustmentEquation(33, id);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_VISION, 33, tmpScore);
        }

        if (ValidateUtils.isValidValue(record.getPAIN_FREQ_ACTVTY_MVMT(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE)) {
            tmpScore = getCaseMixAdjustmentEquation(34, id);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_FREQ_PAIN, 34, tmpScore);
        }

        if (validator.isNPRSULC3_Valid()
                && validator.isNPRSULC4_Valid()) {

            tmpStr = record.getNBR_PRSULC_STG3();
            if (tmpStr != null && !tmpStr.trim().isEmpty()) {
                try {
                    tmpInt1 = IntegerUtils.parseInt(tmpStr, Integer.MAX_VALUE);
                } catch (Exception e) {
                    tmpInt1 = 0; // remains 0
                }
            } else {
                tmpInt1 = 0;
            }

            tmpStr = record.getNBR_PRSULC_STG4();
            if (tmpStr != null && !tmpStr.trim().isEmpty()) {
                try {
                    tmpInt2 = IntegerUtils.parseInt(tmpStr, Integer.MAX_VALUE);
                } catch (Exception e) {
                    tmpInt2 = 0;
                }
            } else {
                tmpInt2 = 0;
            }

            if (tmpInt1 + tmpInt2 >= 2) {
                tmpScore = getCaseMixAdjustmentEquation(35, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3_and_NBR_PRSULC_STG4, 35, tmpScore);
            }
        }

        if (validator.isSTGPRSUL_Valid()) {
            if (ValidateUtils.isValidValue(record.getSTG_PRBLM_ULCER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {
                tmpScore = getCaseMixAdjustmentEquation(36, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_STG_PRBLM_ULCER, 36, tmpScore);

            } else if (ValidateUtils.isValidValue(record.getSTG_PRBLM_ULCER(), ValidateUtils.ARRAY_DOUBLE_THREE_FOUR)) {
                tmpScore = getCaseMixAdjustmentEquation(37, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_STG_PRBLM_ULCER, 37, tmpScore);
            }
        }

        if (validator.isSTATSTASIS_Valid()) {
            tmpStr = record.getSTUS_PRBLM_STAS_ULCR();
            if (tmpStr.equals("02")) {
                tmpScore = getCaseMixAdjustmentEquation(38, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 38, tmpScore);
            } else if (tmpStr.equals("03")) {
                tmpScore = getCaseMixAdjustmentEquation(39, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 39, tmpScore);
            }
        }

        if (validator.isSTATSURG_Valid()) {
            tmpStr = record.getSTUS_PRBLM_SRGCL_WND();
            if (tmpStr.equals("02")) {
                tmpScore = getCaseMixAdjustmentEquation(40, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_SURGWND, 40, tmpScore);
            } else if (tmpStr.equals("03")) {
                tmpScore = getCaseMixAdjustmentEquation(41, id);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_SURGWND, 41, tmpScore);
            }
        }

        if (validator.isDYSPNEIC_Valid()
                && ValidateUtils.isValidValue(record.getWHEN_DYSPNEIC(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR)) {
            tmpScore = getCaseMixAdjustmentEquation(42, id);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_WHEN_DYSPNEIC, 42, tmpScore);
        }

        if (validator.isBWLINCONT_Valid()
                && ValidateUtils.isValidValue(record.getBWL_INCONT(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE)) {
            tmpScore = getCaseMixAdjustmentEquation(43, id);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_BWL_INCONT, 43, tmpScore);
        }

        if (validator.isOSTOMY_Valid()
                && ValidateUtils.isValidValue(record.getOSTOMY(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {
            tmpScore = getCaseMixAdjustmentEquation(44, id);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_OSTOMY, 44, tmpScore);
        }

        if (validator.isCUR_INJECT_MEDS_Valid()
                && ValidateUtils.isValidValue(record.getCRNT_MGMT_INJCTN_MDCTN(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO)) {
            tmpScore = getCaseMixAdjustmentEquation(45, id);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_CUR_INJECT_MEDS, 45, tmpScore);
        }

        return score;
    }

    /**
     * The name of this version.
     *
     * @return The name of this scoring model
     */
    @Override
    public String getName() {
        return "Clinical / Functional Scoring Model 2308";
    }

    /**
     * This method does nothing - the name can not be reset
     *
     * @param arg0
     */
    @Override
    public void setName(String arg0) {
        // can't really do this
    }

    /**
     * This gets the Case Mix Adjustment table value, by essentially getting the
     * caseMixId (i.e. row) and the equation and returning the value.
     *
     * @param caseMixId
     * @param id
     * @return the adjustment or 0 if the case mix id is not found, or the
     * adjustment value is blank
     */
    public int getCaseMixAdjustmentEquation(int caseMixId, int equationId) {
        final CaseMixAdjustmentItemIF caseMix = getCaseMixAdjustment(caseMixId);
        return caseMix == null ? 0 : caseMix.getAdjustment(equationId);
    }
    /**
     * This variable is used to cache the last value of the case mix adjustment.
     * If the getCaseMixAdjustment() method is asking for the same case mix id,
     * then this variable allow it reduce the calls to th grouperDataManager.
     */
    private transient CaseMixAdjustmentItemIF lastCaseMix = null;

    /**
     * Searches for the case mix object using the id - i.e. the row number, for
     * the set of equations
     *
     * @param caseMixId
     * @return the case mix equation information or null if the id does not
     * exist
     */
    public CaseMixAdjustmentItemIF getCaseMixAdjustment(int caseMixId) {
        if (lastCaseMix == null || lastCaseMix.getId() != caseMixId) {
            lastCaseMix = grouperDataManager.getCaseMixAdjustment(caseMixId);
        }

        return lastCaseMix;
    }

    /**
     * The DiagnosisCodeIF with its value being codeValue.
     *
     * @param codeValue
     * @return the Code with its value being codeValue. If the code is not valid
     * (found for this version), then the Invalid flag will be set to true and
     * all other information about the code will be meaninless. This method
     * should never return null.
     */
    protected DiagnosisCodeIF getDiagnosisCode(String codeValue) {
        DiagnosisCodeIF code = DiagnosisCode_Empty.DEFAULT;

        // get the diagnosis code from the data manager
        if (codeValue != null) {
            codeValue = codeValue.trim();
            if (!codeValue.isEmpty()) {
                code = grouperDataManager.getDiagnosisCode(codeValue);

				// if the code is not found in the manager, it is
                // considered invalid for this version.  However, this method
                // should never return a null, so create Diagnosis with the code
                // value - by default a Diagnosis code is not a ValidCode, and
                // is it not Valid for scoring
                if (code == null) {
                    code = new DiagnosisCode(codeValue);
                }
            }
        }

        return code;
    }

    /**
     * Clinically scores the record placing values into the pointSE, and
     * dualPointsSE variables. The scoring is split up into Diagnosis Group
     * related questions and drill down further.
     *
     * Pseudo code lines: 1149 thru 1490
     *
     * @param record
     * @param validator
     * @return
     */
    public int scoreClinical(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator) {
        int clinicalScore = 0;
        int codePoints[];
        int dualPointsSE[];
        int idx;
        DiagnosisCodeIF diagCode;
        boolean diagGroupsUsed[];
        boolean pointsLost[];
        int diagGroupId;
        boolean Row11C8Flag = false;
        boolean Row11C9Flag = false;
        boolean Row14C10Flag = false;
        boolean Row14C11Flag = false;
        boolean Row19C6Flag = false;
        boolean Row19C14Flag = false;
        boolean Row20C14Flag = false;
        boolean Row20C15Flag = false;
        boolean Row27C19Flag = false;
        boolean Row27C20Flag = false;
        int tmpScore;

        codePoints = new int[18];
        dualPointsSE = new int[18];
        diagGroupsUsed = new boolean[22];
        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "Diagnosis Group scoring");

		// loop through the diagnosis codes and provide
        // a score for each diagnosis and for each equation
        // one pass of the record
        for (idx = 0; idx < 18; idx++) {
            // get the diagnosis code
            diagCode = record.getDiagnosisCode(idx);

			// skip any codes that are not valid for scoring
            // or that does not have a Diagnostic Group, i.e.
            // a valid Etiology code pair that is not a valid
            // Etiology without the Secondary code
            if (!diagCode.isValidForScoring()) {
                if (!diagCode.isValidCode()) {
                    if (!diagCode.isEmpty()) {
                        ScoringEventFormatter.fireValidCodeWarning(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, false);
                    }
                } else {
                    ScoringEventFormatter.fireValidCodeWarning(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, true);
                }

                continue;
            }

            ScoringEventFormatter.fireScoringCodeEligible(grouper.getEventListenersList(), grouper, this, idx + 1, diagCode);

            // set the diagnosis group present indicator
            diagGroupId = (int) diagCode.getDiagnosticGroup().getId();

			// First, determine the Diagnostic Group,
            // then provide the more detailed criteria
            switch (diagGroupId) {
                case 1: // Blindness and low vision
                    // add Table 5, row 1
                    tmpScore = getCaseMixAdjustmentEquation(1, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 1, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;
                    break;

                case 2: // blood disorders
                    // add Table 5, row 2
                    tmpScore = getCaseMixAdjustmentEquation(2, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 2, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;
                    break;

                case 3: // Cancer and Selected benign neoplasms
                    // add Table 5, row 3
                    tmpScore = getCaseMixAdjustmentEquation(3, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 3, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;
                    break;

                case 4: // Diabetes
                    // add Table 5, row 4
                    tmpScore = getCaseMixAdjustmentEquation(4, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 4, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;
                    break;

                case 5: // Dysphagia
                    if (record.isDiagnosticGroupOnRecord(12, -1)) {
                        // add Table 5, row 6
                        tmpScore = getCaseMixAdjustmentEquation(6, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 6, tmpScore);
                        diagGroupsUsed[diagGroupId - 1] = true;
                    }

                    if ("1".equals(record.getTHH_ENT_NUTRITION())
                            && validator.isTHERAPIES_Valid()
                            && validator.isINTERNAL_LOGIC_Valid()) {
                        // add Table 5, row 7
                        tmpScore = getCaseMixAdjustmentEquation(7, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 7, tmpScore);
                        diagGroupsUsed[diagGroupId - 1] = true;
                    }
                    break;

                case 6: // Gain Abnormality
                    if (validator.isSTGPRSUL_Valid() && ValidateUtils.isValidValue(
                            record.getSTG_PRBLM_ULCER(),
                            ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)
                            && !Row19C14Flag) {
                        Row19C6Flag = true;

                        // add Table 5, row 19
                        tmpScore = getCaseMixAdjustmentEquation(19, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 19, tmpScore);

                        // add 19 to the dual Points
                        dualPointsSE[idx] += 19;
                        diagGroupsUsed[diagGroupId - 1] = true;
                    }
                    break;

                case 7: // Gastrointestinal disorders
                    // add Table 5, row 8
                    tmpScore = getCaseMixAdjustmentEquation(8, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 8, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;

                    if (validator.isOSTOMY_Valid()
                            && ValidateUtils.isValidValue(record.getOSTOMY(),
                                    ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {

                        // add Table 5, row 9
                        tmpScore = getCaseMixAdjustmentEquation(9, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 9, tmpScore);
                    }

                    if (record.isDiagnosticGroupOnRecord(10, -1)
                            || record.isDiagnosticGroupOnRecord(11, -1)
                            || record.isDiagnosticGroupOnRecord(12, -1)
                            || record.isDiagnosticGroupOnRecord(13, -1)) {
                        // add Table 5, row 10
                        tmpScore = getCaseMixAdjustmentEquation(10, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 10, tmpScore);
                    }

                    break;

                case 8: // Heart Disease
                    if (!Row11C9Flag) {
                        Row11C8Flag = true;

                        // add Table 5, row 11
                        tmpScore = getCaseMixAdjustmentEquation(11, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 11, tmpScore);

                        // Add 11
                        dualPointsSE[idx] += 11;
                        diagGroupsUsed[diagGroupId - 1] = true;
                    }
                    break;

                case 9: // Hypertension
                    if (!Row11C8Flag) {
                        Row11C9Flag = true;

                        // add Table 5, row 11
                        tmpScore = getCaseMixAdjustmentEquation(11, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 11, tmpScore);

                        // Add 11
                        dualPointsSE[idx] += 11;
                        diagGroupsUsed[diagGroupId - 1] = true;
                    }
                    break;

                case 10: // Neuro 1 - Brain disorders and paralysis
                    // add Table 5, row 12
                    tmpScore = getCaseMixAdjustmentEquation(12, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 12, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;

                    if (validator.isCUR_TOILETING_Valid()
                            && ValidateUtils.isValidValue(
                                    record.getCRNT_TOILTG(),
                                    ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR)) {

                        // add Table 5, row 13
                        tmpScore = getCaseMixAdjustmentEquation(13, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 13, tmpScore);
                    }

                    if (!Row14C11Flag
                            //							&& !validator.isCUR_DRESS_INVLD()
                            && validator.isCUR_DRESS_Valid()
                            && (ValidateUtils.isValidValue(
                                    record.getCRNT_DRESS_UPPER(),
                                    ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE) || ValidateUtils.isValidValue(
                                    record.getCRNT_DRESS_LOWER(),
                                    ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE))) {
                        Row14C10Flag = true;

                        // add Table 5, row 14
                        tmpScore = getCaseMixAdjustmentEquation(14, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 14, tmpScore);

                        // Add 14
                        dualPointsSE[idx] += 14;
                    }

                    break;

                case 11: // Neuro 2 - Peripheral neurological disorders
                    if (!Row14C10Flag
                            //							&& !validator.isCUR_DRESS_INVLD()
                            && validator.isCUR_DRESS_Valid()
                            && (ValidateUtils.isValidValue(
                                    record.getCRNT_DRESS_UPPER(),
                                    ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE) || ValidateUtils.isValidValue(
                                    record.getCRNT_DRESS_LOWER(),
                                    ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE))) {
                        Row14C11Flag = true;

                        // add Table 5, row 14
                        tmpScore = getCaseMixAdjustmentEquation(14, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 14, tmpScore);

                        // Add 14
                        dualPointsSE[idx] += 14;
                        diagGroupsUsed[diagGroupId - 1] = true;
                    }
                    break;

                case 12: // Neuro 3 - stroke
                    // add Table 5, row 15
                    tmpScore = getCaseMixAdjustmentEquation(15, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 15, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;

                    if (validator.isCUR_DRESS_Valid()
                            && (ValidateUtils.isValidValue(
                                    record.getCRNT_DRESS_UPPER(),
                                    ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE) || ValidateUtils.isValidValue(
                                    record.getCRNT_DRESS_LOWER(),
                                    ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE))) {
                        // add Table 5, row 16
                        tmpScore = getCaseMixAdjustmentEquation(16, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 16, tmpScore);
                    }

                    if (validator.isCUR_AMBULATION_Valid()
                            && ValidateUtils.isValidValue(
                                    record.getCRNT_AMBLTN(),
                                    ValidateUtils.ARRAY_DOUBLE_THREE_FOUR_FIVE)) {
                        // add Table 5, row 17
                        tmpScore = getCaseMixAdjustmentEquation(17, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 17, tmpScore);
                    }
                    break;

                case 13: // Neuro 4 - Multiple sclerosis
                    if (validator.isCUR_BATHING_Valid() && ValidateUtils.isValidValue(record.getCRNT_BATHG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE)
                            || validator.isCUR_TOILETING_Valid() && ValidateUtils.isValidValue(record.getCRNT_TOILTG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR)
                            || validator.isCUR_TRANSFER_Valid() && ValidateUtils.isValidValue(record.getCRNT_TRNSFRNG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE)
                            || validator.isCUR_AMBULATION_Valid() && ValidateUtils.isValidValue(record.getCRNT_AMBLTN(), ValidateUtils.ARRAY_DOUBLE_THREE_FOUR_FIVE)) {

                        // add Table 5, row 18
                        tmpScore = getCaseMixAdjustmentEquation(18, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 18, tmpScore);
                        diagGroupsUsed[diagGroupId - 1] = true;
                    }
                    break;

                case 14: //Ortho 1 - Leg Disorders
                    if (!Row19C6Flag
                            //                                                && !validator.isSTGPRSUL_INVLD() 
                            && validator.isSTGPRSUL_Valid()
                            && ValidateUtils.isValidValue(
                                    record.getSTG_PRBLM_ULCER(),
                                    ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {
                        Row19C14Flag = true;

                        // add Table 5, row 19
                        tmpScore = getCaseMixAdjustmentEquation(19, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 19, tmpScore);

                        // Add 19
                        dualPointsSE[idx] += 19;
                        diagGroupsUsed[diagGroupId - 1] = true;
                    }

                    if (!Row20C15Flag && ("1".equals(
                            record.getTHH_IV_INFUSION()) || "1".equals(
                                    record.getTHH_PAR_NUTRITION()))
                            //							&& !validator.isINTERNAL_LOGIC_INVLD()
                            //							&& !validator.isTHERAPIES_INVLD()) {
                            && validator.isINTERNAL_LOGIC_Valid()
                            && validator.isTHERAPIES_Valid()) {
                        Row20C14Flag = true;

                        // add Table 5, row 20
                        tmpScore = getCaseMixAdjustmentEquation(20, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 20, tmpScore);

                        // Add 20
                        dualPointsSE[idx] += 20;
                        diagGroupsUsed[diagGroupId - 1] = true;
                    }
                    break;

                case 15: // Ortho 2 - Other Orthopedic disorders
                    if (!Row20C14Flag && ("1".equals(
                            record.getTHH_IV_INFUSION()) || "1".equals(
                                    record.getTHH_PAR_NUTRITION()))
                            //							&& !validator.isINTERNAL_LOGIC_INVLD()
                            //							&& !validator.isTHERAPIES_INVLD()) {
                            && validator.isINTERNAL_LOGIC_Valid()
                            && validator.isTHERAPIES_Valid()) {

                        Row20C15Flag = true;

                        // add Table 5, row 20
                        tmpScore = getCaseMixAdjustmentEquation(20, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 20, tmpScore);

                        // Add 20
                        dualPointsSE[idx] += 20;
                        diagGroupsUsed[diagGroupId - 1] = true;
                    }
                    break;

                case 16: // Psych 1 - Affective and other psychoses, depression
                    // add Table 5, row 21
                    tmpScore = getCaseMixAdjustmentEquation(21, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 21, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;
                    break;

                case 17: // Psych 2 - Degenerative and other organic psychiatric disorders
                    // add Table 5, row 22
                    tmpScore = getCaseMixAdjustmentEquation(22, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 22, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;
                    break;

                case 18: // Pulmonary disorders
                    // add Table 5, row 23
                    tmpScore = getCaseMixAdjustmentEquation(23, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 23, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;

                    if (validator.isCUR_AMBULATION_Valid()
                            && ValidateUtils.isValidValue(
                                    record.getCRNT_AMBLTN(),
                                    ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR_FIVE)) {
                        // add Table 5, row 24
                        tmpScore = getCaseMixAdjustmentEquation(24, id);
                        codePoints[idx] += tmpScore;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 24, tmpScore);
                    }
                    break;

                case 19: // Skin 1 - Traumatic wounds, burns, and post-operative complications
                    // add Table 5, row 25
                    tmpScore = getCaseMixAdjustmentEquation(25, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 25, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;

                    if (!Row27C20Flag
                            && ("1".equals(record.getTHH_IV_INFUSION())
                            || "1".equals(record.getTHH_PAR_NUTRITION()))
                            //							&& !validator.isINTERNAL_LOGIC_INVLD()
                            //							&& !validator.isTHERAPIES_INVLD()) {
                            && validator.isINTERNAL_LOGIC_Valid()
                            && validator.isTHERAPIES_Valid()) {
                        Row27C19Flag = true;

                        // add Table 5, row 27
                        tmpScore = getCaseMixAdjustmentEquation(27, id);
                        codePoints[idx] += tmpScore;
                        dualPointsSE[idx] += 27;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 27, tmpScore);
                    }
                    break;

                case 20: // Skin 2 - Ulcers and other skin conditions
                    if (!Row27C19Flag
                            && ("1".equals(record.getTHH_IV_INFUSION())
                            || "1".equals(record.getTHH_PAR_NUTRITION()))
                            //							&& !validator.isINTERNAL_LOGIC_INVLD()
                            //							&& !validator.isTHERAPIES_INVLD()) {
                            && validator.isINTERNAL_LOGIC_Valid()
                            && validator.isTHERAPIES_Valid()) {

                        Row27C20Flag = true;

                        // add Table 5, row 27
                        tmpScore = getCaseMixAdjustmentEquation(27, id);
                        codePoints[idx] += tmpScore;
                        dualPointsSE[idx] += 27;
                        ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 27, tmpScore);
                    }

                    // add Table 5, row 28
                    tmpScore = getCaseMixAdjustmentEquation(28, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 28, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;
                    break;

                case 21: // Tracheostomy care
                    // add Table 5, row 29
                    tmpScore = getCaseMixAdjustmentEquation(29, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 29, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;
                    break;

                case 22: // Urostomy / Cystostomy care
                    // add Table 5, row 30
                    tmpScore = getCaseMixAdjustmentEquation(30, id);
                    codePoints[idx] += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, idx + 1, 30, tmpScore);
                    diagGroupsUsed[diagGroupId - 1] = true;
                    break;

                default:
            }
        }

        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this, "Diagnosis Group scoring", codePoints);

		//---------------------------------------------------
        // Determining the primary diagnosis between the two pairs
        // M0230a/240b or M0246a3/M0246a4. This must be done separately
        // for each equation, because of differences in which codes earn
        // points under each equation, which can determine which diagnoses
        // are recognized for scoring and how many points they earn.
        //---------------------------------------------------
        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "Determining primary Diagnosis");
        determinePrimaryDiagnosis(record, validator, codePoints);
        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this, "Determining primary Diagnosis", codePoints);

		//---------------------------------------------------
        // Recalculate the score of each non-primary diagnosis in
        // casemix variables with different scores for primary vs.
        // other diagnosis by setting that diagnosis’ score to zero
        // and rescoring, giving only the points that are based on
        // being an other diagnosis.
        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "Recalculate for non-primary diagnosis");
        recalculateNonPrimaryCodes(record, validator, codePoints, dualPointsSE);
        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this, "Recalculate for non-primary diagnosis", codePoints);

		//---------------------------------------------------
        // Cycle through all diagnosis positions with points. Drop points
        // for non-initial occurrences of any diagnosis groups. Resolve
        // contention within etiology/manifestation pairs when both are
        // casemix diagnoses in different diagnosis groups by retaining the
        // DG with higher score unless that DG has already earned points
        // via a previous occurrence.
        // reset the diagnosis Groups used indicators
        diagGroupsUsed = new boolean[22];
        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "Resolve contention within etiology/manifestation pairs");
        pointsLost = resolveEtiologyManifestationContention(record, validator, codePoints, diagGroupsUsed);
        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this, "Resolve contention within etiology/manifestation pairs", codePoints);

        // Sum all the remaining diagnosis points into the clinical score
        clinicalScore += HomeHealthUtils.sumIntArray(codePoints);

		//--------------------------------------------
        // Recapture the points for any dual-diagnosis casemix variables
        // where the diagnosis originally awarded the points was lost
        // through etiology/manifestation contention.
        // Pseudo code lines: 1416 thru 1434
        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "Recapture the points for any dual-diagnosis");
        for (idx = 0; idx < 18; idx++) {
            // check if points were lost for the diagnosis indicator
            if (pointsLost[idx]) {
                switch (dualPointsSE[idx]) {
                    case 11:
                        if (record.isDiagnosticGroupOnRecord(8, idx)
                                || record.isDiagnosticGroupOnRecord(9, idx)) {
                            clinicalScore += getCaseMixAdjustmentEquation(11, id);
                            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_POINTS_LOST_ADDED_BACK, idx + 1, 11);
                        }
                        break;

                    case 14:
                        if (record.isDiagnosticGroupOnRecord(10, idx)
                                || record.isDiagnosticGroupOnRecord(11, idx)) {
                            clinicalScore += getCaseMixAdjustmentEquation(14, id);
                            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_POINTS_LOST_ADDED_BACK, idx + 1, 14);
                        }
                        break;

                    case 19:
                        if (record.isDiagnosticGroupOnRecord(6, idx)
                                || record.isDiagnosticGroupOnRecord(14, idx)) {
                            clinicalScore += getCaseMixAdjustmentEquation(19, id);
                            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_POINTS_LOST_ADDED_BACK, idx + 1, 19);
                        }
                        break;

                    case 20:
                        if (record.isDiagnosticGroupOnRecord(14, idx)
                                || record.isDiagnosticGroupOnRecord(15, idx)) {
                            clinicalScore += getCaseMixAdjustmentEquation(20, id);
                            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_POINTS_LOST_ADDED_BACK, idx + 1, 20);
                        }
                        break;

                    case 27:
                        if (record.isDiagnosticGroupOnRecord(19, idx)
                                || record.isDiagnosticGroupOnRecord(20, idx)) {
                            clinicalScore += getCaseMixAdjustmentEquation(27, id);
                            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_POINTS_LOST_ADDED_BACK, idx + 1, 27);
                        }
                        break;

                    case 39:
                        if (record.isDiagnosticGroupOnRecord(6, idx)
                                || record.isDiagnosticGroupOnRecord(14, idx)) {
                            clinicalScore += getCaseMixAdjustmentEquation(19, id);
                            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_POINTS_LOST_ADDED_BACK, idx + 1, 19);
                        }

                        if (record.isDiagnosticGroupOnRecord(14, idx)
                                || record.isDiagnosticGroupOnRecord(15, idx)) {
                            clinicalScore += getCaseMixAdjustmentEquation(20, id);
                            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this, CommonMessageText.EVENT_SCORE_MSG_POINTS_LOST_ADDED_BACK, idx + 1, 20);
                        }
                        break;

                    default:
                }
            }
        }
        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this, "Recapture the points for any dual-diagnosis", clinicalScore);

		//---------------------------------------------------
        // Scoring remaining clinical variables – main  model
        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "Scoring remaining clinical variables");
        clinicalScore += scoreRemainingVariables(record, validator, codePoints);
        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this, "Scoring remaining clinical variables", null);

        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this, "Diagnosis Group scoring (final)", clinicalScore);

        return clinicalScore;
    }

    /**
     * Score the functional part of the model
     *
     * Pseudo code lines: 1492 thru 1517
     *
     * @param record
     * @param validator
     * @return the functional score
     */
    public int scoreFunctional(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator) {
        int score = 0;

        if (validator.isCUR_DRESS_Valid()
                && (ValidateUtils.isValidValue(record.getCRNT_DRESS_UPPER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE)
                || ValidateUtils.isValidValue(record.getCRNT_DRESS_LOWER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE))) {
            score += getCaseMixAdjustmentEquation(46, id);
        }

        if (validator.isCUR_BATHING_Valid()
                && ValidateUtils.isValidValue(record.getCRNT_BATHG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE)) {
            score += getCaseMixAdjustmentEquation(47, id);
        }

        if (validator.isCUR_TOILETING_Valid()
                && ValidateUtils.isValidValue(record.getCRNT_TOILTG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR)) {
            score += getCaseMixAdjustmentEquation(48, id);
        }

        if (validator.isCUR_TRANSFER_Valid()
                && ValidateUtils.isValidValue(record.getCRNT_TRNSFRNG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE)) {
            score += getCaseMixAdjustmentEquation(49, id);
        }

        if (validator.isCUR_AMBULATION_Valid()) {
            if (ValidateUtils.isValidValue(record.getCRNT_AMBLTN(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {
                score += getCaseMixAdjustmentEquation(50, id);
            } else if (ValidateUtils.isValidValue(record.getCRNT_AMBLTN(), ValidateUtils.ARRAY_DOUBLE_THREE_FOUR_FIVE)) {
                score += getCaseMixAdjustmentEquation(51, id);
            }
        }

        return score;
    }

    /**
     * This populates the record with all Diagnosis codes that are valid for
     * this grouper version that are listed on the record. Codes that are found
     * are marked as Valid, otherwise the code is mark as not Valid, and not
     * Valid for scoring. This method should be called prior to scoring the
     *
     * @param record
     */
    @Override
    public void populateCodes(HomeHealthRecordIF record) {
        DiagnosisCodeIF tmpCode;

        tmpCode = record.getPRIMARY_DIAG_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPRIMARY_DIAG_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getOTH_DIAG1_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setOTH_DIAG1_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getOTH_DIAG2_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setOTH_DIAG2_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getOTH_DIAG3_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setOTH_DIAG3_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getOTH_DIAG4_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setOTH_DIAG4_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getOTH_DIAG5_ICD();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setOTH_DIAG5_ICD(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_A3();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_A3(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_B3();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_B3(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_C3();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_C3(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_D3();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_D3(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_E3();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_E3(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_F3();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_F3(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_A4();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_A4(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_B4();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_B4(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_C4();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_C4(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_D4();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_D4(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_E4();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_E4(getDiagnosisCode(tmpCode.getCode()));
        }

        tmpCode = record.getPMT_DIAG_ICD_F4();
        if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
            record.setPMT_DIAG_ICD_F4(getDiagnosisCode(tmpCode.getCode()));
        }
    }

    /**
     * Gets the reference to the Grouper
     *
     * @return the Home Health grouper that this model is associated with, null
     * if no grouper
     */
    @Override
    public HomeHealthGrouperIF getGrouper() {
        return grouper;
    }

    /**
     * Sets the Home Health grouper that this model is associated with, null if
     * no grouper
     *
     * @param grouper
     */
    @Override
    public void setGrouper(HomeHealthGrouperIF grouper) {
        this.grouper = grouper;
    }

    @Override
    public void preprocessRecord(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, Collection<HomeHealthEventListenerIF> listeners) {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

}
