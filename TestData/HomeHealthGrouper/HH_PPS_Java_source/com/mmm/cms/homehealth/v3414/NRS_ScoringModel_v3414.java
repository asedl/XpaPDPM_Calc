/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3414;

import com.mmm.cms.homehealth.DiagnosisCode;
import com.mmm.cms.homehealth.DiagnosisCode_Empty;
import com.mmm.cms.homehealth.ScoringPoints;
import com.mmm.cms.homehealth.proto.AlreadyScoredException;
import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.DataManagerIF;
import com.mmm.cms.homehealth.proto.DiagnosisScoringGridIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.proto.ScoringPointsIF;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ScoringEventFormatter;
import com.mmm.cms.util.ValidateUtils;

/**
 * This provides scoring based on the Non-Routine Supplies model for the Home
 * Health Grouper, version 3413
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class NRS_ScoringModel_v3414 extends ClinicalFunctional_ScoringModel_v3414 {

    /**
     * Constructor that initialized with the Grouper and data manager references
     *
     * @param grouper
     * @param dataManager
     */
    public NRS_ScoringModel_v3414(HomeHealthGrouperIF grouper,
            DataManagerIF dataManager) {
        super(grouper, dataManager, 0, "NRS Equation");
    }

    /**
     * This is the main scoring module, which scores the clinical information
     * first and then the functional information.
     *
     * @see #scoreClinical(HomeHealthRecordIF record,
     * HomeHealthRecordValidatorIF validator)
     *
     * @param record
     * @param validator
     * @return ScoringPointsIF that contains the clinical and functional scoring
     * elements. This will never be null
     */
    @Override
    public ScoringPointsIF score(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int scoreOrder[][]) {
        final ScoringPoints points = new ScoringPoints(1);

        preprocessRecord(record, validator);

        // score the clinical
        points.setScoreAt(0, scoreClinical(record, validator, scoreOrder));

        // display info
        ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                "Final NRS equation score: ", Integer.toString(points.getScores()[0]));

        return points;
    }

    /**
     * This preprocesses the record to adjust the scorable Diabetic Ulcer code.
     * If the primary code is Diabetic, and the secondary code is Ulcer, then
     * score the Diabetic and not the Ulcer. Otherwise do not score the Diabetic
     * code.
     *
     * If any Diabetic code is in any other position besides the primary
     * position it should be ignored.
     *
     * Pseudo code lines: 1530 thru 1575
     *
     * Nov 2012 - Ensure same diabetic ulcer logic applies when the payment code in
     * position 1 is an Primary Awarding payment code. In such a case, then the
     * diabetic ulcer can be in position 2 with the ulcer in position 3
     *
     * @param record
     * @param validator
     */
    @Override
    public void preprocessRecord(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator) {
        int ulcerIndex;
        DiagnosisCodeIF code;
        DiagnosisCodeIF primaryCode;

        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this,
                CommonMessageText.NRS_PRE_PROCESS_RECORD);

        // check the primary code first
        primaryCode = record.getPRIMARY_DIAG_ICD();

        // check if the code is an optional payment code
        // and that the A3 is on the pairing list
        if (primaryCode.isOptionalPaymentCode() && primaryCode.isValidEtiologyPairing(record.getPMT_DIAG_ICD_A3())) {
            primaryCode = record.getPMT_DIAG_ICD_A3();
            ulcerIndex = 1;
        } else if (primaryCode.isPrimaryAwardableCode()) {
            // the second code is considered primary
            primaryCode = record.getOTH_DIAG1_ICD();
            ulcerIndex = 2;
        } else {
            ulcerIndex = 1;
        }

        // check for primary code being a diabetic ulcer    
        if (primaryCode.isDiabeticUlcer()) {

            // get second code and check that code is used for scoring
            // and that it is an ulcer - if it is not, then
            // do not score the primary code
            code = record.getDiagnosisCode(ulcerIndex);

            // check for a VCode or not to determine if the combo code
            // should be this or column 3
            if (code.isOptionalPaymentCode()) {
                // check column 3 instead
                code = record.getDiagnosisCode(ulcerIndex + 6);
                if (code.isUlcer()) {
                    // still score the primary, but don't score the
                    // secondary
                    code.setValidForScoring(false);
                } else {
                    // do not score the primary code because of the
                    // missing Ulcer code in column 3
                    primaryCode.setValidForScoring(false);
                    ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                            "Set PRIMARY_DIAG_ICD not valid for scoring due to missing Ulcer code in column 3");
                }
            } else if (!code.isValidForScoring() || !code.isUlcer()) {
                // do not score the primary code because of the
                // missing Ulcer code
                primaryCode.setValidForScoring(false);
                ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                        "Set PRIMARY_DIAG_ICD not valid for scoring due to missing Ulcer code");
            } else if (code.isUlcer()) {
                // still score the primary, but don't score the
                // secondary
                code.setValidForScoring(false);
            }
        }

        // if a Diabetic code is found in any other position besides the primary,
        // as identified above, set it to not score
        for (int idx = ulcerIndex; idx < 6; idx++) {
            code = record.getDiagnosisCode(idx);
            if (code.isDiabeticUlcer()) {
                code.setValidForScoring(false);
                ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                        "Set DIAG code ", code.getCode(), " not valid for scoring due previous Diabetic Ulcer code");
            }
        }

        // column 3
        // if a Diabetic code is found in any other position besides the primary,
        // as identified above, set it to not score
        for (int idx = ulcerIndex; idx < 6; idx++) {
            code = record.getOptionalDiagnosisCode3(idx);
            if (code.isDiabeticUlcer()) {
                code.setValidForScoring(false);
                ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                        "Set DIAG code ", code.getCode(), " not valid for scoring due previous Diabetic Ulcer code");
            }
        }

        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this,
                CommonMessageText.NRS_PRE_PROCESS_RECORD, 0);
    }

    /**
     * Scores the Primary only code with Primary only CaseMix values
     *
     * @param record
     * @param diagCode
     * @param diagIdx
     * @param scoringGrid
     * @throws com.mmm.cms.homehealth.proto.AlreadyScoredException
     */
    @Override
    public void scorePrimaryOnly(HomeHealthRecordIF record,
            DiagnosisCodeIF diagCode, int diagIdx, DiagnosisScoringGridIF scoringGrid) throws AlreadyScoredException {
        int tmpScore;

        //----------------------------------------------------
        // Primary codes
        //
        // if the code is a
        // potential primary code (an etiology in position index 0 or 6, or a
        // manifestation in position 6 or 12), then we can sometimes score the
        // code differently then when it is in an Other position.
        // Because of the 2 pass logic, in some cases, we
        // can score some Diagnostic Groups twice, but with a different
        // case mix adjustment row. When scoring the same Group twice,
        // the previous score from pass 1, must be substracted from the
        // the primary code score, so as not to over count the scoring.
        // This effects only a couple Dx Groups that have Primary code specific
        // values.
        switch (diagCode.getDiagnosticGroup().getId()) {
            case 1: // Anal Fissure, fistula and abscess
                tmpScore = calculatePrimaryAdjustment(scoringGrid, 1, 2);
                scoringGrid.setScore(diagCode, diagIdx, 1, tmpScore, false);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode,
                        diagIdx + 1, 1, tmpScore);
                break;

            case 2: // Cellulitis and abscess
                tmpScore = calculatePrimaryAdjustment(scoringGrid, 3, 4);
                scoringGrid.setScore(diagCode, diagIdx, 3, tmpScore, false);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode,
                        diagIdx + 1, 3, tmpScore);
                break;

            case 3: // Diabetic Ulcer
                tmpScore = getCaseMixAdjustmentEquation(5, 0);
                scoringGrid.addScore(diagCode, diagIdx, 5, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode,
                        diagIdx + 1, 5, tmpScore);
                break;

            case 4: // Gangrene
                tmpScore = calculatePrimaryAdjustment(scoringGrid, 6, 7);
                scoringGrid.setScore(diagCode, diagIdx, 6, tmpScore, false);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode,
                        diagIdx + 1, 6, tmpScore);
                break;

            case 5: // Malignant neoplasms of skin
                tmpScore = calculatePrimaryAdjustment(scoringGrid, 8, 9);
                scoringGrid.setScore(diagCode, diagIdx, 8, tmpScore, false);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode,
                        diagIdx + 1, 8, tmpScore);
                break;

            case 7: // Other infections of skin and subcutaneous tissue
                tmpScore = calculatePrimaryAdjustment(scoringGrid, 11, 12);
                scoringGrid.setScore(diagCode, diagIdx, 11, tmpScore, false);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode,
                        diagIdx + 1, 11, tmpScore);
                break;

            case 8: // Post-operative Complications
                tmpScore = calculatePrimaryAdjustment(scoringGrid, 13, 14);
                scoringGrid.setScore(diagCode, diagIdx, 13, tmpScore, false);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 13, tmpScore);
                break;

            case 9: // Traumatic wounds, burns and post-operative complications
                tmpScore = calculatePrimaryAdjustment(scoringGrid, 15, 16);
                scoringGrid.setScore(diagCode, diagIdx, 15, tmpScore, false);
                ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode,
                        diagIdx + 1, 15, tmpScore);
                break;

            default:
                ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                        "Diagnosis code '", diagCode.getCode(),
                        "' valid for scoring, but Diagnosis Group ",
                        Integer.toString(diagCode.getDiagnosticGroup().getId()),
                        " not scorable for Primary-Only diagnosis.");
        }
    }

    /**
     * Scores only those codes that can not be in the primary position
     *
     * @param record
     * @param diagCode
     * @param diagIdx
     * @param scoringGrid
     */
    @Override
    public void scoreOtherOnly(HomeHealthRecordIF record,
            DiagnosisCodeIF diagCode, int diagIdx, DiagnosisScoringGridIF scoringGrid) throws AlreadyScoredException {
        int tmpScore;

        switch (diagCode.getDiagnosticGroup().getId()) {
            case 1: // Anal Fissure, fistula and abscess
                tmpScore = getCaseMixAdjustmentEquation(2, 0);
                scoringGrid.addScore(diagCode, diagIdx, 2, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 2, tmpScore);
                break;

            case 2: // Cellulitis and abscess
                tmpScore = getCaseMixAdjustmentEquation(4, 0);
                scoringGrid.addScore(diagCode, diagIdx, 4, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 4, tmpScore);
                break;

            case 4: // Gangrene
                tmpScore = getCaseMixAdjustmentEquation(7, 0);
                scoringGrid.addScore(diagCode, diagIdx, 7, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 7, tmpScore);
                break;

            case 5: // Malignant neoplasms of skin
                tmpScore = getCaseMixAdjustmentEquation(9, 0);
                scoringGrid.addScore(diagCode, diagIdx, 9, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 9, tmpScore);
                break;

            case 7: //Other infections of skin and subcutaneous tissue
                tmpScore = getCaseMixAdjustmentEquation(12, 0);
                scoringGrid.addScore(diagCode, diagIdx, 12, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 12, tmpScore);
                break;

            case 8: // Post-operative Complications
                tmpScore = getCaseMixAdjustmentEquation(14, 0);
                scoringGrid.addScore(diagCode, diagIdx, 14, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 14, tmpScore);
                break;

            case 9: // Traumatic wounds, burns and post-operative complications
                tmpScore = getCaseMixAdjustmentEquation(16, 0);
                scoringGrid.addScore(diagCode, diagIdx, 16, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 16, tmpScore);
                break;

            default:
                ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                        "Diagnosis code '", diagCode.getCode(),
                        "' valid for scoring, but Diagnosis Group ",
                        Integer.toString(diagCode.getDiagnosticGroup().getId()),
                        " not scorable for Other-Only diagnosis.");
        }
    }

    /**
     * This section covers scoring of Diagnostic groups and Case Mix Adjustment
     * Rows that do not depend on Primary or Other specific values for the code
     *
     * @param record
     * @param diagCode
     * @param diagIdx
     * @param scoringGrid
     * @throws com.mmm.cms.homehealth.proto.AlreadyScoredException
     */
    @Override
    public void scoreAny(HomeHealthRecordIF record,
            DiagnosisCodeIF diagCode, HomeHealthRecordValidatorIF validator,
            int diagIdx, DiagnosisScoringGridIF scoringGrid) throws AlreadyScoredException {
        int tmpScore;

        switch (diagCode.getDiagnosticGroup().getId()) {
            case 6: // Non-pressure and non-stasis ulcers (other than diabetic)
                // ensure that the Special Diabetic Ulcer group
                // is not also used on this record
                if (!record.isDiagnosticGroupOnRecord(3, diagIdx)) {
                    tmpScore = getCaseMixAdjustmentEquation(10, 0);
                    scoringGrid.addScore(diagCode, diagIdx, 10, tmpScore, true);
                    ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                            diagCode, diagIdx + 1, 10, tmpScore);
                }
                break;

            case 10: // payment code, Cystostomy Care
                tmpScore = getCaseMixAdjustmentEquation(17, 0);
                scoringGrid.addScore(diagCode, diagIdx, 17, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 17, tmpScore);
                break;

            case 11: // payment code, Tracheostomy Care
                tmpScore = getCaseMixAdjustmentEquation(18, 0);
                scoringGrid.addScore(diagCode, diagIdx, 18, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 18, tmpScore);
                break;

            case 12: // payment code, Urostomy Care
                tmpScore = getCaseMixAdjustmentEquation(19, 0);
                scoringGrid.addScore(diagCode, diagIdx, 19, tmpScore, true);
                ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
                        diagCode, diagIdx + 1, 19, tmpScore);
                break;

            default:
        }
    }

    /**
     * This scores the remaining clinical variables.
     *
     * Pseudo code lines: 1764 thru 1833
     *
     * @param record
     * @param validator
     * @return
     */
    @Override
    public int scoreRemainingVariables(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int currentScore) {

        int score = 0;
        String tmpStr;
        int tmpInt;
        int tmpScore;

        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this,
                CommonMessageText.CALCULATE_SPECIAL_ITEMS);

        //---------------------------------------------
        // Score the skin condition related items first
        //---------------------------------------------
        if (validator.isNPRSULC1_Valid()) {
            if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG1(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {
                tmpScore = getCaseMixAdjustmentEquation(20, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG1, 20, tmpScore);
            } else if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG1(), ValidateUtils.ARRAY_DOUBLE_THREE_FOUR)) {
                tmpScore = getCaseMixAdjustmentEquation(21, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG1, 21, tmpScore);
            }
        }

        if (validator.isNPRSULC2_Valid()) {
            tmpStr = record.getNBR_PRSULC_STG2();
            if (tmpStr != null && tmpStr.trim().length() > 0) {
                tmpInt = IntegerUtils.parseInt(tmpStr, Integer.MAX_VALUE);

                if (1 == tmpInt) {
                    tmpScore = getCaseMixAdjustmentEquation(22, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 22, tmpScore);
                } else if (2 == tmpInt) {
                    tmpScore = getCaseMixAdjustmentEquation(23, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 23, tmpScore);
                } else if (3 == tmpInt) {
                    tmpScore = getCaseMixAdjustmentEquation(24, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 24, tmpScore);
                } else if (4 <= tmpInt) {
                    // any value 4 or above 
                    tmpScore = getCaseMixAdjustmentEquation(25, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 25, tmpScore);
                }
            }
        }

        if (validator.isNPRSULC3_Valid()) {
            tmpStr = record.getNBR_PRSULC_STG3();
            if (tmpStr != null && tmpStr.trim().length() > 0) {
                tmpInt = IntegerUtils.parseInt(tmpStr, Integer.MAX_VALUE);

                if (1 == tmpInt) {
                    tmpScore = getCaseMixAdjustmentEquation(26, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 26, tmpScore);
                } else if (2 == tmpInt) {
                    tmpScore = getCaseMixAdjustmentEquation(27, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 27, tmpScore);
                } else if (3 == tmpInt) {
                    tmpScore = getCaseMixAdjustmentEquation(28, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 28, tmpScore);
                } else if (4 <= tmpInt) {
                    // any value 4 or above 
                    tmpScore = getCaseMixAdjustmentEquation(29, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 29, tmpScore);
                }
            }
        }

        if (validator.isNPRSULC4_Valid()) {
            tmpStr = record.getNBR_PRSULC_STG4();
            if (tmpStr != null && tmpStr.trim().length() > 0) {
                tmpInt = IntegerUtils.parseInt(tmpStr, Integer.MAX_VALUE);

                if (1 == tmpInt) {
                    tmpScore = getCaseMixAdjustmentEquation(30, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG4, 30, tmpScore);
                } else if (2 == tmpInt) {
                    tmpScore = getCaseMixAdjustmentEquation(31, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG4, 31, tmpScore);
                } else if (3 <= tmpInt) {
                    // any value 4 or above 
                    tmpScore = getCaseMixAdjustmentEquation(32, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG4, 32, tmpScore);
                }
            }
        }

        /*
         * Non-staging pressure ulcer is concerned with Dressing and Covering
         */
        if (validator.isUNOBS_PRSULC_Valid()
                && ("00".compareTo(((HomeHealthRecord_C_IF) record).getNSTG_DRSG()) < 0
                || "00".compareTo(((HomeHealthRecord_C_IF) record).getNSTG_CVRG()) < 0)) {
            tmpScore = getCaseMixAdjustmentEquation(33, 0);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                    CommonMessageText.EVENT_SCORE_MSG_NSTG_DRSG_or_NSTG_CVRG, 33, tmpScore);
        }

        if (validator.isNBR_STASULC_Valid()) {
            tmpStr = record.getNBR_STAS_ULCR();
            if ("02".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(34, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_STASULC, 34, tmpScore);
            } else if ("03".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(35, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_STASULC, 35, tmpScore);
            } else if ("04".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(36, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_STASULC, 36, tmpScore);
            }
        }

        /* 
         * STAS_ULCR_PRSNT
         * 0 - No
         * 1 - Yes, patient has BOTH observable and unobservable stasis ulcers
         * 2 - Yes, patient has observable stasis ulcers ONLY
         * 3 - Yes, patient has unobservable stasis ulcers ONLY (known but not 
         *      observable due to non-removable dressing)
         */
        if (validator.isUNOBS_STASULC_Valid()) {
            String str = record.getSTAS_ULCR_PRSNT();
            if ("01".equals(str) || "03".equals(str)) {
                tmpScore = getCaseMixAdjustmentEquation(37, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAS_ULCR_PRSNT, 37, tmpScore);
            }
        }

        if (validator.isSTATSTASIS_Valid()) {
            tmpStr = record.getSTUS_PRBLM_STAS_ULCR();
            if ("01".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(38, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 38, tmpScore);
            } else if ("02".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(39, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 39, tmpScore);
            } else if ("03".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(40, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 40, tmpScore);
            }
        }

        if (validator.isSTATSURG_Valid()) {
            tmpStr = record.getSTUS_PRBLM_SRGCL_WND();
            if ("02".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(41, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_SURGWND, 41, tmpScore);
            } else if ("03".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(42, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_SURGWND, 42, tmpScore);
            }
        }

        if (validator.isOSTOMY_Valid()) {
            tmpStr = record.getOSTOMY();
            if ("01".equals(tmpStr)) {
                // only score row 45 if there have already been scores
                // for rows 1-42
                if (currentScore > 0 || score > 0) {
                    tmpScore = getCaseMixAdjustmentEquation(45, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_OSTOMY_SKIN, 45, tmpScore);
                }

                tmpScore = getCaseMixAdjustmentEquation(43, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_OSTOMY, 43, tmpScore);
            } else if ("02".equals(tmpStr)) {
                // only score row 46 if there have already been scores
                // for rows 1-42
                if (currentScore > 0 || score > 0) {
                    tmpScore = getCaseMixAdjustmentEquation(46, 0);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_OSTOMY_SKIN, 46, tmpScore);
                }

                tmpScore = getCaseMixAdjustmentEquation(44, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_OSTOMY, 44, tmpScore);
            }
        }

        if (validator.isTHERAPIES_Valid()
                && validator.isINTERNAL_LOGIC_Valid()
                && "1".equals(record.getTHH_IV_INFUSION())) {
            tmpScore = getCaseMixAdjustmentEquation(47, 0);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                    CommonMessageText.EVENT_SCORE_MSG_THH_IV_INFUSION, 47, tmpScore);
        }

        if (validator.isUR_INCONT_Valid()
                && "02".equals(record.getUR_INCONT())) {
            tmpScore = getCaseMixAdjustmentEquation(48, 0);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                    CommonMessageText.EVENT_SCORE_MSG_UR_INCONT, 48, tmpScore);
        }

        if (validator.isBWLINCONT_Valid()) {
            tmpStr = record.getBWL_INCONT();
            if ("04".equals(tmpStr) || "05".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(49, 0);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_BWL_INCONT, 49, tmpScore);
            }
        }

        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this,
                CommonMessageText.CALCULATE_SPECIAL_ITEMS, score);

        return score;
    }

    /**
     * Gets the name of this scoring module
     *
     * @return The name of this scoring model
     */
    @Override
    public String getName() {
        return "Non-Routine Supplies Scoring Model v3414";
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
    @Override
    public int getCaseMixAdjustmentEquation(int caseMixId, int equationId) {
        CaseMixAdjustmentItemIF nrsCasemix = grouperDataManager.getNRSCaseMixAdjustment(caseMixId);

        // get the first equation - that is all this case mix has
        return nrsCasemix != null ? nrsCasemix.getEarly13AndUnder() : 0;
    }

    /**
     * Gets an NRS related Diagnosis code.
     *
     * @param codeValue
     * @return the Code with its value being codeValue. If the code is not valid
     * (found for this version), then the Invalid flag will be set to true and
     * all other information about the code will be meaningless. This method
     * should never return null.
     */
    @Override
    protected DiagnosisCodeIF getDiagnosisCode(String codeValue) {
        DiagnosisCodeIF code = DiagnosisCode_Empty.DEFAULT;

        // get the diagnosis code from the data manager
        if (codeValue != null) {
            codeValue = codeValue.trim();
            if (!codeValue.isEmpty()) {
                code = grouperDataManager.getNRSDiagnosisCode(codeValue);

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
}
