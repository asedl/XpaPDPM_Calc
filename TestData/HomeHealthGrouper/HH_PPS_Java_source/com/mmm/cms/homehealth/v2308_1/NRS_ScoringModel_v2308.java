/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v2308_1;

import com.mmm.cms.homehealth.DiagnosisCode;
import com.mmm.cms.homehealth.DiagnosisCode_Empty;
import com.mmm.cms.homehealth.GrouperDataManager;
import com.mmm.cms.homehealth.ScoringPoints;
import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.HomeHealthScoringModelIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_B_IF;
import com.mmm.cms.homehealth.proto.ScoringPointsIF;
import com.mmm.cms.util.HomeHealthUtils;
import com.mmm.cms.util.ScoringEventFormatter;
import com.mmm.cms.util.ValidateUtils;
import java.util.Collection;

/**
 * This provides scoring based on the Non-Routine Supplies model for the Home
 * Health Grouper, version 2.03
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class NRS_ScoringModel_v2308 implements HomeHealthScoringModelIF {

    static String SET_TO_0 = " set to 0";
    /**
     * A reference to the Grouper's data manager
     */
    final private GrouperDataManager grouperDataManager;
    /**
     * A reference to the Grouper version that is requesting the scoring. Used
     * mostly for reporting during the scoring process.
     */
    private HomeHealthGrouperIF grouper;
    public final static String SHORTNAME = "NRS Equation";

    /**
     * Constructor that initialized with the Grouper and data manager references
     *
     * @param grouper
     * @param dataManager
     */
    public NRS_ScoringModel_v2308(HomeHealthGrouperIF grouper,
            GrouperDataManager dataManager) {
        this.grouper = grouper;
        grouperDataManager = dataManager;
    }

    /**
     * This is the main scoring module, which scores the non routine supplies
     * information.
     *
     * @param record
     * @param validator
     * @return ScoringPointsIF that contains the clinical and functional scoring
     * elements. This will never be null
     */
    @Override
    public ScoringPointsIF score(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator) {

        int score;
        ScoringPoints scorePoints;
        int codePoints[];
        int skinConditionScore;
        int tmpScore;

        scorePoints = new ScoringPoints(1);

        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this,
                "Main scoring module");
		//----------------------------------------------
        // Setting the NRS diagnosis group flag for Diabetic Ulcers,
        // identified by primary diagnosis and first other diagnosis and
        // turning off the other non-pressure, non-stasis ulcer flag:
        preprocessRecord(record, validator);

		//---------------------------------------
        // Calculate maximum possible score for each of the 18 diagnoses to
        // support choice of scoring variable when an etiology and a
        // manifestation are both NRS casemix variables.
        // special case for primaries and ulcers
        codePoints = calculateInitialScore(record, validator);

		//-----------------------------
        // POSSIBLY REDETERMINE THE PRIMARY DIAGNOSIS
        //-----------------------------
        determinePrimaryDiagnosis(record, validator, codePoints);

		//-----------------------------
        // Recalculate the scores of non-primary diagnoses in NRS casemix
        // variables with different scores for primary vs. other diagnosis by
        // setting that diagnosis’ score to zero and rescoring, giving only the
        // NRS points that are based on being an other diagnosis.
        recalculateNonPrimaryCodes(record, validator, codePoints, null);

		//-----------------------------
        // Cycle through all diagnosis positions with points. Drop points for
        // non-initial occurrences of any NRS diagnosis groups. Resolve
        // contention within etiology/manifestation pairs when both are
        // casemix diagnoses in different diagnosis groups by retaining the
        // DG with higher score unless that DG has already earned points via
        // a previous occurrence.
        // any points lost at this point are not recaptured
        resolveEtiologyManifestationContention(record, validator, codePoints, null);

		//--------------------------------------------
        // Scoring remaining Selected Skin Conditions variables – NRS  model
        score = scoreRemainingVariables(record, validator, codePoints);

        // sum up the skin condition score
        skinConditionScore = score + HomeHealthUtils.sumIntArray(codePoints);

        if (validator.isOSTOMY_Valid()) {
            final String tmpStr = record.getOSTOMY();
            if ("01".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(43);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_OSTOMY, 43, tmpScore);
                if (skinConditionScore > 0) {
                    tmpScore = getCaseMixAdjustmentEquation(45);
                    score += tmpScore;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_OSTOMY_SKIN, 45, tmpScore);
                }
            } else if ("02".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(44);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_OSTOMY, 44, tmpScore);
                if (skinConditionScore > 0) {
                    score += getCaseMixAdjustmentEquation(46);
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            CommonMessageText.EVENT_SCORE_MSG_OSTOMY_SKIN, 46, tmpScore);
                }
            }
        }

        if (validator.isTHERAPIES_Valid()
                && validator.isINTERNAL_LOGIC_Valid()
                && "1".equals(record.getTHH_IV_INFUSION())) {
            tmpScore = getCaseMixAdjustmentEquation(47);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                    CommonMessageText.EVENT_SCORE_MSG_THH_IV_INFUSION, 47, tmpScore);
        }

        if (validator.isUR_INCONT_Valid()
                && "02".equals(record.getUR_INCONT())) {
            tmpScore = getCaseMixAdjustmentEquation(48);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                    CommonMessageText.EVENT_SCORE_MSG_UR_INCONT, 48, tmpScore);
        }

        if (validator.isBWLINCONT_Valid()) {
            final String tmpStr = record.getBWL_INCONT();
            if (tmpStr.equals("04") || tmpStr.equals("05")) {
                tmpScore = getCaseMixAdjustmentEquation(49);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_BWL_INCONT, 49, tmpScore);
            }
        }

        // Sum all the remaining diagnonsis points into the NRS score
        score += HomeHealthUtils.sumIntArray(codePoints);

        scorePoints.setScoreAt(0, score);

        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this,
                "Main scoring module", score);

        return scorePoints;
    }

    /**
     * This calls score() without the scoreOrder or listeners, which were not
     * implemented in this version
     *
     * @param record
     * @param validator
     * @param scoreOrder - always assumed to be null
     * @param listeners - always assumed to be null
     * @return
     */
    @Override
    public ScoringPointsIF score(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, int[][] scoreOrder, Collection<HomeHealthEventListenerIF> listeners) {
        return score(record, validator);
    }

    /**
     * This calls score() without the scoreOrder which was not implemented in
     * this version
     *
     * @param record
     * @param validator
     * @param scoreOrder - always assumed to be null
     * @return
     */
    @Override
    public ScoringPointsIF score(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, int[][] scoreOrder) {
        return score(record, validator);
    }

    /**
     * This preprocesses the record to adjust the diagnostic groups based on the
     * presence of an Ulcer or Diabetic Ulcer on the record.
     *
     * Pseudo code lines: 1530 thru 1575
     *
     * @param record
     * @param validator
     */
    @Override
    public void preprocessRecord(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator) {
        DiagnosisCodeIF primaryCode;

        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "Pre-process record");

        // check the primary code first
        primaryCode = record.getPRIMARY_DIAG_ICD();

		// check if the code is used for scoring and if it
        // is a diabetic ulcer
        if (primaryCode.isValidForScoring() && primaryCode.isDiabeticUlcer()) {

            setUlcerDiagnosisScoring(record, primaryCode);
        }

        // check the optoinal primary code
        primaryCode = record.getPMT_DIAG_ICD_A3();

		// check if the code is used for scoring and if it
        // is a diabetic ulcer
        if (primaryCode.isValidForScoring() && primaryCode.isDiabeticUlcer()) {
            setUlcerDiagnosisScoring(record, primaryCode);
        }

        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this,
                "Pre-process record", 0);
    }

    private void setUlcerDiagnosisScoring(HomeHealthRecordIF record,
            DiagnosisCodeIF primaryCode) {

        DiagnosisCodeIF code;

        // get second code and check that code is an ulcer related
        code = record.getOTH_DIAG1_ICD();
        if (code.isUlcer()) {
            primaryCode.setDiagnosticGroup(grouperDataManager.getNRSDiagnosticGroup(3));
            code.setDiagnosticGroup(grouperDataManager.getNRSDiagnosticGroup(0));
            ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                    CommonMessageText.DIABETIC_ULCER_SET + primaryCode.getCode()
                    + " Group to 3 and " + code.getCode() + " to 0.");
        } else if (code.isOptionalPaymentCode()) {
            // get the optional code and check that code is an ulcer related
            code = record.getPMT_DIAG_ICD_B3();
            if (code.isUlcer()) {
                primaryCode.setDiagnosticGroup(grouperDataManager.getNRSDiagnosticGroup(3));
                code.setDiagnosticGroup(grouperDataManager.getNRSDiagnosticGroup(0));
                ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.DIABETIC_ULCER_SET + primaryCode.getCode()
                        + " Group to 3 and " + code.getCode() + " to 0.");
            } else {
				// mark the diabetic ulcer not valid for scoring because
                // there is not a paring Ulcer code associated with it
                primaryCode.setValidForScoring(false);
                ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.DIABETIC_ULCER_SET + primaryCode.getCode()
                        + " to NOT score.");
            }
        } else {
			// mark the diabetic ulcer not valid for scoring because
            // there is not a paring Ulcer code associated with it
            primaryCode.setValidForScoring(false);
            ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
                    CommonMessageText.DIABETIC_ULCER_SET + primaryCode.getCode()
                    + " to NOT score due to non-optional VCode.");
        }

    }

    private String getCodePointsPositionHeader(int idx, DiagnosisCodeIF code) {
        return "Diagnosis code '" + code.getCode() + "' at position " + (++idx) + " ";
    }

    /**
     * Calculate maximum possible score for each of the 18 diagnoses to support
     * choice of scoring variable when an etiology and a manifestation are both
     * NRS casemix variables. special case for primaries and ulcers
     *
     * Pseudo code lines: 1521 thru 1574
     *
     * @param record
     * @param validator
     * @return int array with score per diagnosis
     */
    public int[] calculateInitialScore(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator) {
        DiagnosisCodeIF code;
        int idx;
        int codePoints[];
        boolean nrsDiagGroupsUsed[];
        int diagGroupId;

        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "calculateInitialScore");

        // initialize to 0
        codePoints = new int[18];
        nrsDiagGroupsUsed = new boolean[22];

        code = record.getPRIMARY_DIAG_ICD();
        if (code.isValidForScoring() && code.getDiagnosticGroup().getId() == 3) {
            codePoints[0] += getCaseMixAdjustmentEquation(5);
            nrsDiagGroupsUsed[2] = true;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                    getCodePointsPositionHeader(0, code), 5, codePoints[0]);
        }

        code = record.getPMT_DIAG_ICD_A3();
        if (code.isValidForScoring() && code.getDiagnosticGroup().getId() == 3) {
            codePoints[6] += getCaseMixAdjustmentEquation(5);
            nrsDiagGroupsUsed[2] = true;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                    getCodePointsPositionHeader(6, code), 5, codePoints[6]);
        }

        for (idx = 0; idx < codePoints.length; idx++) {
            code = record.getDiagnosisCode(idx);

            // skip any that are not valid for scoring
            if (!code.isValidForScoring()) {
                if (!code.isValidCode()) {
                    if (!code.isEmpty()) {
                        ScoringEventFormatter.fireScoringWarning(grouper.getEventListenersList(), grouper, this,
                                "Diagnosis code '"
                                + code.getCode()
                                + "' at position " + (idx + 1)
                                + " is NOT a valid code.");
                    }
                } else {
                    ScoringEventFormatter.fireScoringWarning(grouper.getEventListenersList(), grouper, this,
                            "Diagnosis code '" + code.getCode()
                            + "' at position " + (idx + 1)
                            + " is valid code BUT NOT valid for scoring.");
                }
                continue;
            }

            // get the codes diagnosis group,
            diagGroupId = code.getDiagnosticGroup().getId();
            switch (diagGroupId) {
                case 1:
                    codePoints[idx] += getCaseMixAdjustmentEquation(1);
                    nrsDiagGroupsUsed[0] = true;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(idx, code), idx, 1);
                    break;

                case 2:
                    codePoints[idx] += getCaseMixAdjustmentEquation(3);
                    nrsDiagGroupsUsed[1] = true;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(idx, code), idx, 3);
                    break;

                case 4:
                    codePoints[idx] += getCaseMixAdjustmentEquation(6);
                    nrsDiagGroupsUsed[3] = true;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(idx, code), idx, 6);
                    break;

                case 5:
                    codePoints[idx] += getCaseMixAdjustmentEquation(8);
                    nrsDiagGroupsUsed[4] = true;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(idx, code), idx, 8);
                    break;

                case 6:
					// ensure that the Special Diabetic Ulcer group
                    // is not also used on this record
                    if (!nrsDiagGroupsUsed[2]) {
                        codePoints[idx] += getCaseMixAdjustmentEquation(10);
                        nrsDiagGroupsUsed[5] = true;
                        ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                getCodePointsPositionHeader(idx, code), idx, 10);
                    }
                    break;

                case 7:
                    codePoints[idx] += getCaseMixAdjustmentEquation(11);
                    nrsDiagGroupsUsed[6] = true;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(idx, code), idx, 11);
                    break;

                case 8:
                    codePoints[idx] += getCaseMixAdjustmentEquation(13);
                    nrsDiagGroupsUsed[7] = true;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(idx, code), idx, 13);
                    break;

                case 9:
                    codePoints[idx] += getCaseMixAdjustmentEquation(15);
                    nrsDiagGroupsUsed[8] = true;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(idx, code), idx, 15);
                    break;

                case 10:
                    codePoints[idx] += getCaseMixAdjustmentEquation(17);
                    nrsDiagGroupsUsed[9] = true;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(idx, code), idx, 17);
                    break;

                case 11:
                    codePoints[idx] += getCaseMixAdjustmentEquation(18);
                    nrsDiagGroupsUsed[10] = true;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(idx, code), idx, 18);
                    break;

                case 12:
                    codePoints[idx] += getCaseMixAdjustmentEquation(19);
                    nrsDiagGroupsUsed[11] = true;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(idx, code), idx, 19);
                    break;

                default:

            }
        }

        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this,
                "calculateInitialScore", codePoints);

        return codePoints;
    }

    /**
     * Determine the primary diagnosis between the two pairs M0230a240b or
     * M0246x3/M0246x4
     *
     * Pseudo code lines: 1600 thru 1643
     *
     * @param record
     * @param validator
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
                    // otherwise - allow both to score
                    M0246_PMT_DIAG_ICD_A3.setPrimary(true);
                    M0246_PMT_DIAG_ICD_A4.setPrimary(true);

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
                            // either can be scored as primary - based on points
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
     * non-initial occurrences of any NRS diagnosis groups. Resolve contention
     * within etiology/manifestation pairs when both are casemix diagnoses in
     * different diagnosis groups by retaining the DG with higher score unless
     * that DG has already earned points via a previous occurrence. Any points
     * lost at this point are not recaptured
     *
     * Pseudo code lines: 1683 thru 1762
     *
     * @param record
     * @param validator
     * @param codePoints
     * @param diagGroupsUsed
     * @return
     */
    public boolean[] resolveEtiologyManifestationContention(
            HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int[] codePoints,
            boolean diagGroupsUsed[]) {
        boolean nrsDiagGroupsUsed[];
        boolean pointsLost[];
        int idx;
        int scoreIdx;
        DiagnosisCodeIF code;
        DiagnosisCodeIF tmpCode;
        int diagGroupId;

        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "resolve Etiology Manifestation Contention");

        nrsDiagGroupsUsed = new boolean[22];
        pointsLost = new boolean[18];

        code = record.getPRIMARY_DIAG_ICD();
        if (code.isValidCode()) {
            diagGroupId = (int) code.getDiagnosticGroup().getId();
            if (diagGroupId > 0 && codePoints[0] > 0) {
                nrsDiagGroupsUsed[diagGroupId - 1] = true;
            }
        }

        code = record.getPMT_DIAG_ICD_A3();
        if (code.isValidCode()) {
            diagGroupId = (int) code.getDiagnosticGroup().getId();
            if (diagGroupId > 0 && codePoints[6] > 0) {
                if (nrsDiagGroupsUsed[diagGroupId - 1]) {
                    codePoints[6] = 0;
                    ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                            getCodePointsPositionHeader(6, code) + SET_TO_0, 7, 0);
                } else {
                    nrsDiagGroupsUsed[diagGroupId - 1] = true;
                }
            }
        }

        code = record.getPMT_DIAG_ICD_A4();
        if (code.isValidCode()) {
            diagGroupId = (int) code.getDiagnosticGroup().getId();
            if (diagGroupId > 0 && codePoints[12] > 0) {
                if (nrsDiagGroupsUsed[diagGroupId - 1]) {
                    codePoints[12] = 0;
                } else {
                    if (code.isSecondaryOnly() && codePoints[6] > 0) {
                        if (codePoints[12] > codePoints[6]) {
                            codePoints[6] = 0;
                            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                    getCodePointsPositionHeader(6, code) + SET_TO_0, 12, 0);
                            nrsDiagGroupsUsed[diagGroupId - 1] = true;
                            pointsLost[6] = true;
                        } else {
                            codePoints[12] = 0;
                            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                    getCodePointsPositionHeader(12, code) + SET_TO_0, 12, 0);
                            pointsLost[12] = true;
                        }
                    }

                    if (codePoints[12] > 0) {
                        nrsDiagGroupsUsed[diagGroupId - 1] = true;
                    }
                }
            }
        }

        for (idx = 1; idx < 6; idx++) {
            code = record.getDiagnosisCode(idx);
            if (code.isValidCode()) {

                diagGroupId = (int) code.getDiagnosticGroup().getId();

                if (diagGroupId > 0 && codePoints[idx] > 0) {
                    if (nrsDiagGroupsUsed[diagGroupId - 1]) {
                        codePoints[idx] = 0;
                        ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                getCodePointsPositionHeader(idx, code) + SET_TO_0, idx, 0);
                    } else {
                        if (code.isSecondaryOnly() && codePoints[idx - 1] > 0) {
                            if (codePoints[idx] > codePoints[idx - 1]) {
                                codePoints[idx - 1] = 0;
                                tmpCode = record.getDiagnosisCode(idx - 1);
                                if (tmpCode != null
                                        && tmpCode.getDiagnosticGroup().getId() > 0) {
                                    nrsDiagGroupsUsed[(int) tmpCode.getDiagnosticGroup().getId()
                                            - 1] = true;
                                }
                                pointsLost[idx - 1] = true;
                                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                        getCodePointsPositionHeader(idx - 1, code) + SET_TO_0, idx, 0);
                            } else {
                                codePoints[idx] = 0;
                                pointsLost[idx] = true;
                                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                        getCodePointsPositionHeader(idx, code) + SET_TO_0, idx, 0);
                            }
                        }

                        if (codePoints[idx] > 0) {
                            nrsDiagGroupsUsed[diagGroupId - 1] = true;
                        }
                    }
                }
            }
        }

        // loop through the column 3 codes, in psuedo code as 8 thru 12
        for (idx = 1, scoreIdx = 7; idx < 6; idx++, scoreIdx++) {
            code = record.getOptionalDiagnosisCode3(idx);
            if (code.isValidCode()) {

                diagGroupId = (int) code.getDiagnosticGroup().getId();

                if (diagGroupId > 0) {
                    if (nrsDiagGroupsUsed[diagGroupId - 1]) {
                        codePoints[scoreIdx] = 0;
                        ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                getCodePointsPositionHeader(scoreIdx, code) + SET_TO_0, scoreIdx, 0);
                    } else if (codePoints[scoreIdx] > 0) {
                        nrsDiagGroupsUsed[diagGroupId - 1] = true;
                    }
                }
            }
        }

        // loop through the column 4 codes, in psuedo code as 14 thru 18
        for (idx = 1, scoreIdx = 13; idx < 6; idx++, scoreIdx++) {
            code = record.getOptionalDiagnosisCode4(idx);
            if (code.isValidCode()) {

                diagGroupId = (int) code.getDiagnosticGroup().getId();

                if (diagGroupId > 0) {
                    if (nrsDiagGroupsUsed[diagGroupId - 1]) {
                        codePoints[scoreIdx] = 0;
                    } else {
                        if (code.isSecondaryOnly() && codePoints[scoreIdx - 6]
                                > 0) {
                            if (codePoints[scoreIdx] > codePoints[scoreIdx - 6]) {
                                codePoints[scoreIdx - 6] = 0;
                                tmpCode = record.getDiagnosisCode(idx
                                        - 6);
                                if (tmpCode != null
                                        && tmpCode.getDiagnosticGroup().getId() > 0) {
                                    nrsDiagGroupsUsed[(int) tmpCode.getDiagnosticGroup().getId()
                                            - 1] = true;
                                }
                                pointsLost[scoreIdx - 6] = true;
                                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                        getCodePointsPositionHeader(scoreIdx - 6, code) + SET_TO_0, scoreIdx, 0);

                            } else {
                                codePoints[scoreIdx] = 0;
                                pointsLost[scoreIdx] = true;
                                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                        getCodePointsPositionHeader(scoreIdx, code) + SET_TO_0, scoreIdx, 0);
                            }
                        }

                        if (codePoints[scoreIdx] > 0) {
                            nrsDiagGroupsUsed[diagGroupId - 1] = true;
                        }
                    }
                }
            }
        }

        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this,
                "resolve Etiology Manifestation Contention", codePoints);

        return pointsLost;
    }

    /**
     * Pseudo code lines: 1645 thru 1681
     *
     * @param record
     * @param validator
     * @param codePoints
     * @param dualPoints
     */
    public void recalculateNonPrimaryCodes(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int codePoints[],
            int dualPoints[]) {
        DiagnosisCodeIF code;
        int idx;
        int groupId;

        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "recalculate Non-Primary Codes");

        for (idx = 0; idx < codePoints.length; idx++) {
            code = record.getDiagnosisCode(idx);

            // only update non-primary codes
            if (code.isValidForScoring() && !code.isPrimary()) {

                groupId = code.getDiagnosticGroup().getId();
                switch (groupId) {
                    case 1:
                        codePoints[idx] = getCaseMixAdjustmentEquation(2);
                        ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                getCodePointsPositionHeader(idx, code), idx, 2);
                        break;

                    case 2:
                        codePoints[idx] = getCaseMixAdjustmentEquation(4);
                        ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                getCodePointsPositionHeader(idx, code), idx, 4);
                        break;

                    case 4:
                        codePoints[idx] = getCaseMixAdjustmentEquation(7);
                        ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                getCodePointsPositionHeader(idx, code), idx, 7);
                        break;

                    case 5:
                        codePoints[idx] = getCaseMixAdjustmentEquation(9);
                        ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                getCodePointsPositionHeader(idx, code), idx, 9);
                        break;

                    case 7:
                        codePoints[idx] = getCaseMixAdjustmentEquation(12);
                        ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                getCodePointsPositionHeader(idx, code), idx, 12);
                        break;

                    case 8:
                        codePoints[idx] = getCaseMixAdjustmentEquation(14);
                        ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                getCodePointsPositionHeader(idx, code), idx, 14);
                        break;

                    case 9:
                        codePoints[idx] = getCaseMixAdjustmentEquation(16);
                        ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                                getCodePointsPositionHeader(idx, code), idx, 16);
                        break;
                    default:

                }
            }
        }
        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this,
                "recalculate Non-Primary Codes", codePoints);

    }

    /**
     * This scores the remaining clinical variables.
     *
     * Pseudo code lines: 1764 thru 1833
     *
     * @param record
     * @param validator
     * @param points
     * @return
     */
    public int scoreRemainingVariables(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator, int[] points) {

        int score = 0;
        int tmpScore;

        ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, CommonMessageText.CALCULATE_SPECIAL_ITEMS);

        if (validator.isNPRSULC1_Valid()) {
            if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG1(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {
                tmpScore = getCaseMixAdjustmentEquation(20);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG1, 20, tmpScore);
            } else if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG1(), ValidateUtils.ARRAY_DOUBLE_THREE_FOUR)) {
                tmpScore = getCaseMixAdjustmentEquation(21);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG1, 21, tmpScore);
            }
        }

        if (validator.isNPRSULC2_Valid()) {
            final String tmpStr = record.getNBR_PRSULC_STG2();

            if ("01".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(22);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 22, tmpScore);
            } else if ("02".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(23);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 23, tmpScore);
            } else if ("03".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(24);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 24, tmpScore);
            } else if ("04".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(25);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG2, 25, tmpScore);
            }
        }

        if (validator.isNPRSULC3_Valid()) {
            final String tmpStr = record.getNBR_PRSULC_STG3();
            if ("01".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(26);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 26, tmpScore);
            } else if ("02".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(27);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 27, tmpScore);
            } else if ("03".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(28);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 28, tmpScore);
            } else if ("04".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(29);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG3, 29, tmpScore);
            }
        }

        if (validator.isNPRSULC4_Valid()) {
            final String tmpStr = record.getNBR_PRSULC_STG4();
            if ("01".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(30);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG4, 30, tmpScore);
            } else if ("02".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(31);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG4, 31, tmpScore);
            } else if ("03".equals(tmpStr) || "04".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(32);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_PRSULC_STG4, 32, tmpScore);
            }
        }

        if (validator.isUNOBS_PRSULC_Valid()
                && "1".equals(((HomeHealthRecord_B_IF) record).getUNOBS_PRSULC())) {
            tmpScore = getCaseMixAdjustmentEquation(33);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                    CommonMessageText.EVENT_SCORE_MSG_UNOBS_PRSULC, 33, tmpScore);
        }

        if (validator.isNBR_STASULC_Valid()) {
            final String tmpStr = record.getNBR_STAS_ULCR();
            if ("02".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(34);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_STASULC, 34, tmpScore);
            } else if ("03".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(35);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_STASULC, 35, tmpScore);
            } else if ("04".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(36);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_NBR_STASULC, 36, tmpScore);
            }
        }

        if (validator.isUNOBS_STASULC_Valid()
                && "1".equals(((HomeHealthRecord_B_IF) record).getUNOBS_STASULC())) {
            tmpScore = getCaseMixAdjustmentEquation(37);
            score += tmpScore;
            ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                    CommonMessageText.EVENT_SCORE_MSG_UNOBS_STASULC, 37, tmpScore);
        }

        if (validator.isSTATSTASIS_Valid()) {
            final String tmpStr = record.getSTUS_PRBLM_STAS_ULCR();
            if ("01".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(38);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 38, tmpScore);
            } else if ("02".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(39);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 39, tmpScore);
            } else if ("03".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(40);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_STASULC, 40, tmpScore);
            }
        }

        if (validator.isSTATSURG_Valid()) {
            final String tmpStr = record.getSTUS_PRBLM_SRGCL_WND();
            if ("02".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(41);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_SURGWND, 41, tmpScore);
            } else if ("03".equals(tmpStr)) {
                tmpScore = getCaseMixAdjustmentEquation(42);
                score += tmpScore;
                ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
                        CommonMessageText.EVENT_SCORE_MSG_STAT_PRB_SURGWND, 42, tmpScore);
            }
        }

        ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this,
                CommonMessageText.CALCULATE_SPECIAL_ITEMS, score);

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

        record.setPRIMARY_DIAG_ICD(getDiagnosisCode(
                record.getPRIMARY_DIAG_ICD().getCode()));
        record.setOTH_DIAG1_ICD(getDiagnosisCode(
                record.getOTH_DIAG1_ICD().getCode()));
        record.setOTH_DIAG2_ICD(getDiagnosisCode(
                record.getOTH_DIAG2_ICD().getCode()));
        record.setOTH_DIAG3_ICD(getDiagnosisCode(
                record.getOTH_DIAG3_ICD().getCode()));
        record.setOTH_DIAG4_ICD(getDiagnosisCode(
                record.getOTH_DIAG4_ICD().getCode()));
        record.setOTH_DIAG5_ICD(getDiagnosisCode(
                record.getOTH_DIAG5_ICD().getCode()));

        record.setPMT_DIAG_ICD_A3(getDiagnosisCode(
                record.getPMT_DIAG_ICD_A3().getCode()));
        record.setPMT_DIAG_ICD_B3(getDiagnosisCode(
                record.getPMT_DIAG_ICD_B3().getCode()));
        record.setPMT_DIAG_ICD_C3(getDiagnosisCode(
                record.getPMT_DIAG_ICD_C3().getCode()));
        record.setPMT_DIAG_ICD_D3(getDiagnosisCode(
                record.getPMT_DIAG_ICD_D3().getCode()));
        record.setPMT_DIAG_ICD_E3(getDiagnosisCode(
                record.getPMT_DIAG_ICD_E3().getCode()));
        record.setPMT_DIAG_ICD_F3(getDiagnosisCode(
                record.getPMT_DIAG_ICD_F3().getCode()));

        record.setPMT_DIAG_ICD_A4(getDiagnosisCode(
                record.getPMT_DIAG_ICD_A4().getCode()));
        record.setPMT_DIAG_ICD_B4(getDiagnosisCode(
                record.getPMT_DIAG_ICD_B4().getCode()));
        record.setPMT_DIAG_ICD_C4(getDiagnosisCode(
                record.getPMT_DIAG_ICD_C4().getCode()));
        record.setPMT_DIAG_ICD_D4(getDiagnosisCode(
                record.getPMT_DIAG_ICD_D4().getCode()));
        record.setPMT_DIAG_ICD_E4(getDiagnosisCode(
                record.getPMT_DIAG_ICD_E4().getCode()));
        record.setPMT_DIAG_ICD_F4(getDiagnosisCode(
                record.getPMT_DIAG_ICD_F4().getCode()));
    }

    /**
     * Gets the name of this scoring module
     *
     * @return The name of this scoring model
     */
    @Override
    public String getName() {
        return "Non-Routine Supplies Scoring Model 2308";
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
     * @return the adjustment or 0 if the case mix id is not found, or the
     * adjustment value is blank
     */
    public int getCaseMixAdjustmentEquation(int caseMixId) {
        CaseMixAdjustmentItemIF nrsCasemix;

        nrsCasemix = grouperDataManager.getNRSCaseMixAdjustment(caseMixId);
        // get the first equation - that is all this case mix has
        return nrsCasemix != null ? nrsCasemix.getEarly13AndUnder() : 0;
    }

    /**
     * Gets an NRS related Diagnosis code.
     *
     * @param codeValue
     * @return the Icd9Code with its value being codeValue. If the code is not
     * valid (found for this version), then the Invalid flag will be set to true
     * and all other information about the code will be meaninless. This method
     * should never return null.
     */
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

    /**
     * Gets the Grouper reference
     *
     * @return reference to the grouper this model belongs to
     */
    @Override
    public HomeHealthGrouperIF getGrouper() {
        return grouper;
    }

    /**
     * Sets the reference to the Grouper this model belongs to
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
        return 1;
    }

    @Override
    public void setId(int id) {
        // can't really do this
    }
}
