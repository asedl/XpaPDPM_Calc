/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3110_1;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.v2308_1.AbstractBaseValidator_v2308;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ScoringEventFormatter;
import com.mmm.cms.util.ValidateUtils;
import java.util.Collection;

/**
 * This validator builds on the v2308 validator to provide the common validation
 * for the Home Health Record with OASIS-C related values, that are performed
 * for both the Clinical and the Non-Routine Supplies models.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public abstract class AbstractBaseValidator_v3110 extends AbstractBaseValidator_v2308 {

    public final static String blank4 = "    ";

    public AbstractBaseValidator_v3110(HomeHealthGrouperIF grouper) {
        super(grouper);
    }

    /**
     * Entry to higher level validate for the Oasis record. It calls all the
     * clinical, functional and service domain validation.
     *
     * This method assumes that the diagnosis codes have been populate by the
     * current grouper
     *
     * @param record
     * @return
     */
    @Override
    public CollectionValidationEditsIF validate(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
        final CollectionValidationEditsIF edits = null;
        DiagnosisCodeIF code;
        int idx;

		// check for clinical issues on the record
        // re-use Oasis-B
        if (!validateASSMT_REASON(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Assessment Reason", record.getASSMT_REASON());
        }

		//-----
        // Diagnosis code validations - start
        //-----
        if (!validateExistingCodes(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Primary Diagnosis", record.getPRIMARY_DIAG_ICD().getCode());
        }

        if (!validatePRIMARY_DIAG_ICD(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Primary Diagnosis", record.getPRIMARY_DIAG_ICD().getCode());
        }

        if (!validatePAYMENT_ECodes(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Payment Diagnosis E-codes", "<various>");
        }

		// Nov 19, 2009 update from Errata
        // Notes:
        // 2. The edit stating where an ICD is not blank all prior ICDs within
        //  this item must not be blank is designed to assist the vendors in
        //  designing their systems; however, the State System will eliminate
        //  any embedded blank ICDs by shifting the subsequent non-blank ICDs
        //  into the prior blank items. Secondary ICD codes will not be moved
        // to Primary ICD codes.
        //if (!validateOtherDiagnosisSeries(record)) {
        //            dataValidityFlag.setClinicalIssue(true);
        //            fireClinicalIssueEvent("Other Diagnosis not in a series", "<various>");
        //        }
        if (!validateDiagnosisUnique(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Other Diagnosis are not unique", "<various>");
        }

		//-----
        // Diagnosis code validations - end
        //-----
        if (!validateTHH(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Therapies Received at Home",
                    record.getTHH_IV_INFUSION() + ", "
                    + record.getTHH_PAR_NUTRITION() + ", "
                    + record.getTHH_ENT_NUTRITION() + ", "
                    + record.getTHH_NONE_ABOVE());

        }
        if (!validateVISION(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Vision", record.getVISION());
        }

        if (!validateFREQ_PAIN(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Frequent Pain", record.getPAIN_FREQ_ACTVTY_MVMT());
        }

        if (!validateNPRSULC(record, listeners)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Number of Pressure Ulcers (4 stages)",
                    record.getNBR_PRSULC_STG1() + ", "
                    + record.getNBR_PRSULC_STG2() + ", "
                    + record.getNBR_PRSULC_STG3() + ", "
                    + record.getNBR_PRSULC_STG4());
        }

        if (!validateSTGPRSUL(record, listeners)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Stage of Most Problematic Pressure Ulcer", record.getSTG_PRBLM_ULCER());
        }

        if (!validateNBR_STASULC(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Number of Stasis Ulcers", record.getNBR_STAS_ULCR());
        }

        if (!validateUNOBS_STASULC(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Unobserved Stasis Ulcer",
                    "stat_ulcr_prsnt=" + record.getSTAS_ULCR_PRSNT()
                    + "num_stas_ulcr=" + record.getNBR_STAS_ULCR()
                    + "stus_prblm_stas_ulcr=" + record.getSTUS_PRBLM_STAS_ULCR());
        }

        if (!validateSTATSTASIS(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Status Stasis Ulcer", record.getSTUS_PRBLM_STAS_ULCR());
        }

        if (!validateSTATSURG(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Status Surgical Wound", record.getSTUS_PRBLM_SRGCL_WND());
        }

        if (!validateWHEN_DYSPNEIC(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "When Dyspneic", record.getWHEN_DYSPNEIC());
        }

        if (!validateUR_INCONT(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Urinary Incontinence", record.getUR_INCONT());
        }

        if (!validateBWL_INCONT(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Bowl Incontinence", record.getBWL_INCONT());
        }

        if (!validateBWL_INCONT_OSTOMY(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                    "Bowl Incontence Ostomy", record.getOSTOMY());
        }

		//----------------------------------------
        // FUNCTIONAL DOMAIN VALIDATION
        //----------------------------------------
        if (!validateCUR_INJECT_MEDS(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null,
                    "Current Injectable Meds", record.getCRNT_MGMT_INJCTN_MDCTN());
        }

        // check for functional issues on the record
        if (!validateCUR_DRESS(record)) {
            dataValidityFlag.setFunctionalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null,
                    "Current Dressing (Upper & Lower)", record.getCRNT_DRESS_UPPER()
                    + ", "
                    + record.getCRNT_DRESS_LOWER());
        }

        if (!validateCUR_BATHING(record)) {
            dataValidityFlag.setFunctionalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null,
                    "Current Bathing", record.getCRNT_BATHG());
        }

        if (!validateCUR_TOILETING(record)) {
            dataValidityFlag.setFunctionalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null,
                    "Current Toileting", record.getCRNT_TOILTG());
        }

        if (!validateCUR_TRANSFERRING(record)) {
            dataValidityFlag.setFunctionalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null,
                    "Current Transferring", record.getCRNT_TRNSFRNG());
        }

        if (!validateCUR_AMBULATION(record)) {
            dataValidityFlag.setFunctionalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null,
                    "Current Ambulation", record.getCRNT_AMBLTN());
        }

		//--------------------------------------------------
        // SERVICE DOMAIN VALIDATION
        //--------------------------------------------------
        // check for service issues on the record
        if (!validateTHER_NEED(record)) {
            dataValidityFlag.setServiceIssue(true);
            ScoringEventFormatter.fireServiceIssueEvent(listeners, grouper, null,
                    "Therapy Need and Number of Therapies",
                    record.getTHER_NEED_NBR() + ", "
                    + record.getTHER_NEED_NA());
        }

		//---------------------------------------------------
        // MANIFESTATION DOMAIN VALIDATION
        //---------------------------------------------------
        // check for manifestation issues on the record
        if (!isValidPrincipalDiagnosisCode(record)) {
            dataValidityFlag.setManifestationSequenceIssue(true);
            ScoringEventFormatter.fireManifestationIssueEvent(listeners, grouper, null,
                    "Primary Diagnosis", record.getPRIMARY_DIAG_ICD().getCode());
        }

        if (!validateDiagnosisCodes(record, listeners)) {
            dataValidityFlag.setManifestationSequenceIssue(true);
            ScoringEventFormatter.fireManifestationIssueEvent(listeners, grouper, null,
                    "All Diagnosis Codes", "<various>");
        }

        // set the scoring information from record
        for (idx = 0; idx < 18; idx++) {
            code = record.getDiagnosisCode(idx);
            if (code.isValidForScoring()) {
                diagnosisScoringStatus[idx] = 2;
            } else if (code.isValidCode() || code.isOptionalPaymentCode()) {
                diagnosisScoringStatus[idx] = 1;
            } else {
                diagnosisScoringStatus[idx] = 0;
            }
        }
        return edits;
    }

    
    /**
     * From OASIS-C spec: *2. If M1022_OTH_DIAG1_ICD is blank, then
     * M1022_OTH_DIAG2_ICD must be blank.
     *
     * Similar validation above is repeated for diagnosis codes through
     * OTH_DIAG5_ICD.
     *
     * Once a blank is encountered, the rest of the codes in this column, if any
     * are marked as not valid for scoring
     *
     * @param record
     * @return
     */
    public boolean validateOtherDiagnosisSeries(HomeHealthRecordIF record) {
        DiagnosisCodeIF previousCode;
        DiagnosisCodeIF currentCode;
        boolean valid = true;

        for (int idx = 1; idx < 5;) {
            previousCode = record.getDiagnosisCode(idx++);
            currentCode = record.getDiagnosisCode(idx);

			// if the previous code is blank and the current code it not
            // then the codes were not enterred serially
            if (previousCode.isEmpty() && !currentCode.isEmpty()) {
				// mark the current and the rest of the codes
                // in this first column as unscorable because of the
                // preceding blank
                currentCode.setValidForScoring(false);
                while (++idx < 6) {
                    currentCode = record.getDiagnosisCode(idx);
                    currentCode.setValidForScoring(false);
                }
                valid = false;
                break;
            }
        }
        return valid;
    }

    /**
     * From OASIS-C spec: *1. If M1022_OTH_DIAG2_ICD is not blank, it cannot be
     * equal to M1020_PRIMARY_DIAG_ICD, M1022_OTH_DIAG1_ICD,
     * M1022_OTH_DIAG3_ICD, M1022_OTH_DIAG4_ICD, or M1022_OTH_DIAG5_ICD.
     *
     * Similar validation above is repeated for diagnosis codes through
     * OTH_DIAG5_ICD.
     *
     * The codes are checked from top to bottom. If a code is a duplicated in
     * subsequent positions, then the subsequent codes are marked as not valid
     * for scoring. However, the first code will still be able to score.
     *
     * @param record
     * @return
     */
    public boolean validateDiagnosisUnique(HomeHealthRecordIF record) {
        DiagnosisCodeIF otherCode;
        DiagnosisCodeIF currentCode;
        boolean valid = true;

		// check only code 1 through 5 in this outter loop
        // the inner loop will check against the 6th code
        for (int idx = 0; idx < 5;) {
            currentCode = record.getDiagnosisCode(idx++);

            // only check valid codes
            if (currentCode.isValidCode()) {
                for (int j = idx; j < 6;) {
					// if the current code is duplicated in subsequent positions
                    // then mark the subsequent positions as not valid for
                    // scoring, but allow the current code's to scoring
                    // indicator to remain
                    otherCode = record.getDiagnosisCode(j++);
                    if (currentCode.equals(otherCode)) {
                        // set the code to not score
                        otherCode.setValidForScoring(false);

                        // indicate that a duplicate was found
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }

    /**
     * If the super.validatePRIMARY_DIAG_ICD() is false, then check for the
     * primary being blank. If the primary is blank, then set all the rest of
     * the codes to not score.
     *
     * @param record
     * @return
     */
    @Override
    public boolean validatePRIMARY_DIAG_ICD(HomeHealthRecordIF record) {
        boolean valid;
        // check for E-code validation
        valid = super.validatePRIMARY_DIAG_ICD(record);

        // check for the primary being blank
        DiagnosisCodeIF code = record.getPRIMARY_DIAG_ICD();
        if (code.isEmpty()) {
            // set all the other codes in the record to not score
            for (int idx = 1; idx < 18;) {
                code = record.getDiagnosisCode(idx++);
                if (code != null) {
                    code.setValidForScoring(false);
                }
            }
            valid = false;
        }

        return valid;
    }

    /**
     * Oasis-C 1030_THH values. Therapies Received at Home
     *
     * If the Therapies are invalid, it sets the Internal_Logic_Invld = true
     * Therapies_Invld = true
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * param record return true if there are no errors
     */
    @Override
    public boolean validateTHH(HomeHealthRecordIF record) {
        return ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES) ? super.validateTHH(record) : true;
    }

    /**
     * Oasis-C M1200_VISION Vision
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateVISION(HomeHealthRecordIF record) {
        return ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES) ? super.validateVISION(record) : true;
    }

    /**
     * Oasis-C M1242_PAIN_FREQ_ACTVTY_MVMT Frequent pain during activity or
     * movement
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateFREQ_PAIN(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)
                && !ValidateUtils.isValidValue(record.getPAIN_FREQ_ACTVTY_MVMT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
            this.PAIN_INVLD = true;
        }
        return !this.PAIN_INVLD;
    }

    /**
     * Oasis-C M1350_LESION_OPEN_WND Lesion Open Wound
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     * @deprecated - OASIS-C no longer use this variable for validation of
     * Ulcers
     */
    @Override
    public boolean validateLESION_OPEN_WND(HomeHealthRecordIF record) {
        return ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES) ? super.validateLESION_OPEN_WND(record) : true;
    }

    /**
     * Logic updated Aug 31, 2009
     *
     * M1322_NBR_PRSULC_STG1 No. Pressure Ulcers -Stage 1
     *
     * M1308_NBR_PRSULC_STG2 No. Pressure Ulcers -Stage 2
     *
     * M1308_NBR_PRSULC_STG3 No. Pressure Ulcers -Stage 3
     *
     * M1308_NBR_PRSULC_STG4 No. Pressure Ulcers -Stage 4
     *
     * Only checked for Assessment Reason 01, 03, 04, 05 - Some associated
     * fields within this validation are only available during Assessment Reason
     * 1 & 3
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateNPRSULC(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
        if (record instanceof HomeHealthRecord_C_IF) {
            HomeHealthRecord_C_IF recordC = (HomeHealthRecord_C_IF) record;
            int intStage1;
            int intStage2;
            int intStage3;
            int intStage4;
            int intNonStageCovering;
            int intNonStageDeepTissue;
            int tmpInt;

            if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
				// convert the string values to numbers

                // stage 1 must be 0 thru 4
                intStage1 = IntegerUtils.parseInt(recordC.getNBR_PRSULC_STG1().trim(), -1);
                if (intStage1 < 0 || intStage1 > 4) {
                    // value is not in range
                    this.NPRSULC1_INVLD = true;
                    ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                            "NBR_PRSULC_STG1", "Value out of range: " + recordC.getNBR_PRSULC_STG1());
                }

                intStage2 = IntegerUtils.parseInt(recordC.getNBR_PRSULC_STG2().trim(), -1);
                intStage3 = IntegerUtils.parseInt(recordC.getNBR_PRSULC_STG3().trim(), -1);
                intStage4 = IntegerUtils.parseInt(recordC.getNBR_PRSULC_STG4().trim(), -1);
                intNonStageCovering = IntegerUtils.parseInt(recordC.getNSTG_CVRG().trim(), -1);
                intNonStageDeepTissue = IntegerUtils.parseInt(recordC.getNSTG_DEEP_TISUE().trim(), -1);

                // Stage 1,2,3,4
                if ((intStage3 > 0 || intStage4 > 0)
                        && ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)
                        && !ValidateUtils.isValidValue(recordC.getSTUS_PRBLM_PRSR_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)) {
					// if any stage x > 0, then the Status Problem Pressure Ucler
                    // must be 00, 01, 02, or 03, but only when RFA = 1 or 3
                    this.NPRSULC3_INVLD = true;
                    this.NPRSULC4_INVLD = true;
                }

				// if the unhealed stage 2 pressure ulcer indicator is
                // 0, then no other stage indicators should be used
                if ("0".equals(recordC.getUNHLD_STG2_PRSR_ULCR())) {
					// number pressure ulcers stage 2 should not be filled in
                    // and the SOC/ROC values should be blank only for RFA 4 & 5
                    if (intStage2 >= 0
                            || ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                            && !recordC.getNBR_STG2_AT_SOC_ROC().trim().isEmpty()) {
                        this.NPRSULC2_INVLD = true;
                    }

					// number pressure ulcers stage 3 should not be filled in
                    // and the SOC/ROC values should be blank only for RFA 4 & 5
                    if (intStage3 >= 0
                            || ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                            && !recordC.getNBR_STG3_AT_SOC_ROC().trim().isEmpty()) {
                        this.NPRSULC3_INVLD = true;
                    }

					// number pressure ulcers stage 4 should not be filled in
                    // and the SOC/ROC values should be blank only for RFA 4 & 5
                    if (intStage4 >= 0
                            || ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                            && !recordC.getNBR_STG4_AT_SOC_ROC().trim().isEmpty()) {
                        this.NPRSULC4_INVLD = true;
                    }

					// validate that the rest of the information about these uclers
                    // is also blank for RFA 1, 3, 4, 5
                    if (!recordC.getNSTG_DEEP_TISUE().trim().isEmpty()) {
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }

					// validate that the rest of the information about these uclers
                    // is also blank for RFA 1, 3, 4, 5
                    if (!recordC.getNSTG_DRSG().trim().isEmpty()
                            || intNonStageCovering >= 0) {
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                        this.UNOBS_PRSULC_INVLD = true;
                    }

					// validate that the rest of the information about these uclers
                    // is also blank for RFA 1, 3 only
                    if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)
                            && !recordC.getPRSR_ULCR_LNGTH().trim().isEmpty()
                            || !recordC.getPRSR_ULCR_WDTH().trim().isEmpty()
                            || !recordC.getPRSR_ULCR_DEPTH().trim().isEmpty()
                            || !recordC.getSTUS_PRBLM_PRSR_ULCR().trim().isEmpty()) {
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }

					// validate that the rest of the information about these uclers
                    // is also blank for RFA 4, 5 only
                    if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                            && (!recordC.getNSTG_DRSG_SOC_ROC().trim().isEmpty()
                            || !recordC.getNSTG_CVRG_SOC_ROC().trim().isEmpty()
                            || !recordC.getNSTG_DEEP_TISSUE_SOC_ROC().trim().isEmpty())) {
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }

                } else if ("1".equals(recordC.getUNHLD_STG2_PRSR_ULCR())) {
                    boolean atLeastOne = false;

					// number pressure ulcers stage 2 should be greater than
                    // or equal to at start of care/resumption of care
                    if (intStage2 > 0) {
                        atLeastOne = true;

						// The Number Stage 2 at Start of Care or Resume
                        // of Care, must be less then or equal to the Number
                        // of Stage 2 Ulcers - a blank would be considered
                        // less then
                        if (!ValidateUtils.SPACE_2.equals(recordC.getNBR_STG2_AT_SOC_ROC())) {
                            try {
                                // only during RFA 4, 5
                                // update - 7/2015
                                tmpInt = IntegerUtils.parseInt(recordC.getNBR_STG2_AT_SOC_ROC(), Integer.MAX_VALUE);
                                if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY) && tmpInt > intStage2) {
                                    this.NPRSULC2_INVLD = true;
                                }
                            } catch (NumberFormatException e) {
                                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                                        "NBR_PRSULC_STG2_AT_SOC_ROC", e.toString());
                                this.NPRSULC2_INVLD = true;
                            }
                        }
                    } else if (intStage2 < 0) {
                        // since the unhealed is 1, this field can not be skipped
                        this.NPRSULC2_INVLD = true;
                    }

					// number pressure ulcers stage 3 should be greater than
                    // or equal to at start of care/resumption of care
                    if (intStage3 > 0) {
                        atLeastOne = true;

						// The Number Stage 3 at Start of Care or Resume
                        // of Care, must be less then or equal to the Number
                        // of Stage 3 Ulcers - a blank would be considered
                        // less then
                        if (!ValidateUtils.SPACE_2.equals(recordC.getNBR_STG3_AT_SOC_ROC())) {
                            try {
                                // only during RFA 4, 5
                                tmpInt = IntegerUtils.parseInt(recordC.getNBR_STG3_AT_SOC_ROC(), Integer.MAX_VALUE);
                                if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY) && tmpInt > intStage3) {
                                    this.NPRSULC3_INVLD = true;
                                }
                            } catch (NumberFormatException e) {
                                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                                        "NBR_PRSULC_STG3_AT_SOC_ROC", e.toString());
                                this.NPRSULC3_INVLD = true;
                            }
                        }
                    } else if (intStage3 < 0) {
                        // since the unhealed is 1, this field can not be skipped
                        this.NPRSULC3_INVLD = true;
                    }

					// number pressure ulcers stage 4 should be greater than
                    // or equal to at start of care/resumption of care
                    if (intStage4 > 0) {
                        atLeastOne = true;

						// The Number Stage 4 at Start of Care or Resume
                        // of Care, must be less then or equal to the Number
                        // of Stage 4 Ulcers - a blank would be considered
                        // less then
                        if (!ValidateUtils.SPACE_2.equals(recordC.getNBR_STG4_AT_SOC_ROC())) {
                            try {
                                // only during RFA 4, 5
                                tmpInt = IntegerUtils.parseInt(recordC.getNBR_STG4_AT_SOC_ROC(), Integer.MAX_VALUE);
                                if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY) && tmpInt > intStage4) {
                                    this.NPRSULC4_INVLD = true;
                                }
                            } catch (NumberFormatException e) {
                                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                                        "NBR_PRSULC_STG4_AT_SOC_ROC", e.toString());
                                this.NPRSULC4_INVLD = true;
                            }
                        }
                    } else if (intStage4 < 0) {
                        // since the unhealed is 1, this field can not be skipped
                        this.NPRSULC4_INVLD = true;
                    }

					// At least one of the Stages above or the fields
                    // listed must be greater than 0, or this section is invalid
                    // NOTE: the compareTo() returns < 0 if the String on the
                    // left is less than the string on the right. So, "00" would
                    // be less than "01" thru "99" even as a string. Also, "  "
                    // would be less then "00".  So, if all the values are "00"
                    // or "  ", using a compare with "00" would result in the left
                    // side being greater then the right.
                    // some items are only valid for RFA 4 & 5
                    // check for at least one item > 0 for RFAs 1, 3, 4, 5
                    // else
                    // check for at least on item > 0 for RFAs 4 and 5 only
                    if (ValidateUtils.DOUBLE_ZERO.compareTo(recordC.getNSTG_DRSG()) < 0
                            || ValidateUtils.DOUBLE_ZERO.compareTo(recordC.getNSTG_CVRG()) < 0
                            || ValidateUtils.DOUBLE_ZERO.compareTo(recordC.getNSTG_DEEP_TISUE()) < 0) {
                        atLeastOne = true;
                    } else if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                            && (ValidateUtils.DOUBLE_ZERO.compareTo(recordC.getNSTG_DRSG_SOC_ROC()) < 0
                            || ValidateUtils.DOUBLE_ZERO.compareTo(recordC.getNSTG_CVRG_SOC_ROC()) < 0
                            || ValidateUtils.DOUBLE_ZERO.compareTo(recordC.getNSTG_DEEP_TISSUE_SOC_ROC())
                            < 0)) {
                        atLeastOne = true;
                    }

                    // validate that at least one item is > 0
                    if (!atLeastOne) {
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }

					//-------------------------------------------
                    // Additional edits added to the OASIS-C record
                    // specification as of October 6, 2009
                    //-------------------------------------------
                    if (intStage2 == 0 && intStage3 == 0 && intStage4 == 0 && intNonStageDeepTissue == 0) {
						// If Stage 2, 3, 4 and NSTG_CVRG are 0, then the
                        // Status of Most Problematic Pressure Ulcer must NA
                        if (intNonStageCovering == 0) {
                            if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)
                                    && !ValidateUtils.NOT_APPLICABLE.equals(recordC.getSTUS_PRBLM_PRSR_ULCR())) {
                                this.NPRSULC2_INVLD = true;
                                this.NPRSULC3_INVLD = true;
                                this.NPRSULC4_INVLD = true;
                            }
                        } else if (intNonStageCovering > 0
                                && ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)
                                && !ValidateUtils.isValidValue(recordC.getSTUS_PRBLM_PRSR_ULCR(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE)) {
							// If Stage 2, 3, and 4 are 0, and NSTG_CVRG greater
                            // than 0, then the 
                            // Status of Most Problematic Pressure Ulcer must
                            // be 02 or 03
                            this.NPRSULC2_INVLD = true;
                            this.NPRSULC3_INVLD = true;
                            this.NPRSULC4_INVLD = true;
                            this.UNOBS_PRSULC_INVLD = true;
                        }
                    } else if ((intStage2 > 0 || intNonStageDeepTissue > 0)
                            && intStage3 == 0 && intStage4 == 0
                            && intNonStageCovering == 0
                            && ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)
                            && !"03".equals(recordC.getSTUS_PRBLM_PRSR_ULCR())) {
						// if the Number of Stage 2 Pressure Ulcers is > 0
                        // and the Number of Stage 3 and 4, and Non Stagable due
                        // to Convering is 0, then the Status of Problematic Pressuure
                        // Ulcer must be 3
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }

					// if the Number of Stage 3 or 4 > 0, then the status
                    // of the Problematic Pressure Ulcer must be 00, 01, 02, or
                    // 03
                    if ((intStage3 > 0 || intStage4 > 0)
                            && ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)
                            && !ValidateUtils.isValidValue(recordC.getSTUS_PRBLM_PRSR_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)) {
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }

					// comparison of some values but
                    // only for RFA 4 & 5
                    if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)) {

						// Unstageable Due To Non-removable Dressing Or Device At
                        // SOC/ROC  must be less then or equal to Unstageable Due
                        // To Non-removable Dressing Or Device
                        if (recordC.getNSTG_DRSG_SOC_ROC().compareTo(recordC.getNSTG_DRSG())
                                > 0) {
                            this.NPRSULC2_INVLD = true;
                            this.NPRSULC3_INVLD = true;
                            this.NPRSULC4_INVLD = true;
                            this.UNOBS_PRSULC_INVLD = true;
                        }

						// Nonstageable Due To Non-removable Dressing Or Device At
                        // SOC/ROC must be less then or equal to Unstageable Due
                        // To Coverage by Slough or Eschar
                        if (recordC.getNSTG_CVRG_SOC_ROC().compareTo(recordC.getNSTG_CVRG())
                                > 0) {
                            this.NPRSULC2_INVLD = true;
                            this.NPRSULC3_INVLD = true;
                            this.NPRSULC4_INVLD = true;
                            this.UNOBS_PRSULC_INVLD = true;
                        }

						// Unstageable Due To Suspected Deep Tissue Injury In
                        // Evolution At SOC/ROC must be less than or equal to
                        // Unstageable Due To Suspected Deep Tissue Injury In
                        // Evolution
                        if (recordC.getNSTG_DEEP_TISSUE_SOC_ROC().compareTo(recordC.getNSTG_DEEP_TISUE())
                                > 0) {
                            this.NPRSULC2_INVLD = true;
                            this.NPRSULC3_INVLD = true;
                            this.NPRSULC4_INVLD = true;
                        }
                    }
                } else {
                    // the field UNHLD_STG2_PRSR_ULCR is blank or some other junk
                    this.NPRSULC2_INVLD = true;
                    this.NPRSULC3_INVLD = true;
                    this.NPRSULC4_INVLD = true;
                    this.UNOBS_PRSULC_INVLD = true;
                }

                /*
                 * check the ulcer sizes for RFA 1 and 3
                 * 
                 * If M1308_NBR_PRSULC_STG3 is greater than zero (0) or
                 * M1308_NBR_PRSULC_STG4 is greater than zero (0) or
                 * M1308_NSTG_CVRG is greater than zero (0), then
                 * M1310_PRSR_ULCR_LNGTH through M1314_PRSR_ULCR_DPTH cannot be
                 * blank
                 */
                if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)
                        && (intStage3 > 0 || intStage4 > 0 || intNonStageCovering > 0)
                        && (blank4.equals(recordC.getPRSR_ULCR_LNGTH())
                        || blank4.equals(recordC.getPRSR_ULCR_WDTH())
                        || blank4.equals(recordC.getPRSR_ULCR_DEPTH()))) {
                    this.NPRSULC3_INVLD = true;
                    this.NPRSULC4_INVLD = true;
                }

                /*
                 * If M1308_NBR_PRSULC_STG3 is equal to zero (0) and
                 * M1308_NBR_PRSULC_STG4 is equal to zero (0), and
                 * M1308_NSTG_CVRG is equal to zero (0), then
                 * M1310_PRSR_ULCR_LNGTH through M1314_PRSR_ULCR_DPTH must be
                 * blank.
                 */
                if (intStage3 == 0 && intStage4 == 0 && intNonStageCovering == 0
                        && (!blank4.equals(recordC.getPRSR_ULCR_LNGTH())
                        || !blank4.equals(recordC.getPRSR_ULCR_WDTH())
                        || !blank4.equals(recordC.getPRSR_ULCR_DEPTH()))) {
                    this.NPRSULC3_INVLD = true;
                    this.NPRSULC4_INVLD = true;
                }
            }
        }
		// the Unobserved flag may not be set, but should be if the
        // stages have issues
        this.UNOBS_PRSULC_INVLD = this.UNOBS_PRSULC_INVLD
                || this.NPRSULC2_INVLD
                || this.NPRSULC3_INVLD
                || this.NPRSULC4_INVLD;
        return !(this.NPRSULC1_INVLD
                || this.NPRSULC2_INVLD
                || this.NPRSULC3_INVLD
                || this.NPRSULC4_INVLD
                || this.UNOBS_PRSULC_INVLD);
    }

    /**
     * Oasis-C M1324_STG_PRBLM_ULCER Stage of Most Problematic Pressure Ulcer
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateSTGPRSUL(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
        int tmpInt;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            // check the value of the Stage of Most Problematic Pressure Ulcer
            if ("01".equals(record.getSTG_PRBLM_ULCER())) {

                // number of pressure Ulcer stage 1 must be > 0
                tmpInt = IntegerUtils.parseInt(record.getNBR_PRSULC_STG1(), -1);
                if (tmpInt <= 0) {
                    this.STGPRSUL_INVLD = true;
                    ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                            "NBR_PRSULC_STG1", "Value out of range: " + record.getNBR_PRSULC_STG1());
                }

            } else if ("02".equals(record.getSTG_PRBLM_ULCER())) {

                // number of pressure Ulcer stage 2 must be > 0
                tmpInt = IntegerUtils.parseInt(record.getNBR_PRSULC_STG2(), -1);
                if (tmpInt <= 0) {
                    this.STGPRSUL_INVLD = true;
                    ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                            "NBR_PRSULC_STG2", "Value out of range: " + record.getNBR_PRSULC_STG2());
                }

            } else if ("03".equals(record.getSTG_PRBLM_ULCER())) {
                // number of pressure Ulcer stage 3 must be > 0
                tmpInt = IntegerUtils.parseInt(record.getNBR_PRSULC_STG3(), -1);
                if (tmpInt <= 0) {
                    this.STGPRSUL_INVLD = true;
                    ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                            "NBR_PRSULC_STG3", "Value out of range: " + record.getNBR_PRSULC_STG3());
                }

            } else if ("04".equals(record.getSTG_PRBLM_ULCER())) {
                // number of pressure Ulcer stage 4 must be > 0
                tmpInt = IntegerUtils.parseInt(record.getNBR_PRSULC_STG4(), -1);
                if (tmpInt <= 0) {
                    this.STGPRSUL_INVLD = true;
                    ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null,
                            "NBR_PRSULC_STG4", "Value out of range: " + record.getNBR_PRSULC_STG4());
                }
            } else if (ValidateUtils.DOUBLE_ZERO.equals(record.getNBR_PRSULC_STG1())
                    && ValidateUtils.DOUBLE_ZERO.equals(record.getNBR_PRSULC_STG2())
                    && ValidateUtils.DOUBLE_ZERO.equals(record.getNBR_PRSULC_STG3())
                    && ValidateUtils.DOUBLE_ZERO.equals(record.getNBR_PRSULC_STG4())
                    && !ValidateUtils.NOT_APPLICABLE.equals(record.getSTG_PRBLM_ULCER())) {
				// if the all the Stages are 0, then the Stage of most 
                // Problematic Pressure Ulcer must be NA
                this.STGPRSUL_INVLD = true;
            }
        }

        return !(this.STGPRSUL_INVLD);
    }

    /**
     * Oasis-C M1332_NUM_STAS_ULCER No. Stasis Ulcers
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateNBR_STASULC(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            String str = record.getSTAS_ULCR_PRSNT();

            if (!ValidateUtils.isValidValue(record.getNBR_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)
                    && !ValidateUtils.SPACE_2.equals(record.getNBR_STAS_ULCR())) {
                this.NBR_STASULC_INVLD = true;
            } else {

				// 01 = patient has both observable and unobservable
                // 02 = patient has observable only
                if ("01".equals(str) || "02".equals(str)) {
                    // if number of stasis ulcers or status of problem stasis ulcer are blank, then flag the error
                    if (ValidateUtils.SPACE_2.equals(record.getNBR_STAS_ULCR())
                            || ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_STAS_ULCR())) {
                        this.NBR_STASULC_INVLD = true;
                    }
                } else if (("00".equals(str) || "03".equals(str))
                        && (!ValidateUtils.SPACE_2.equals(record.getNBR_STAS_ULCR())
                        || !ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_STAS_ULCR()))) {
					// 00 = patient does not have any stasis ulcers
                    // 03 = patient has only an unobservable stasis ulcer
                    // the stasis ulcer is 00 or 03, then the number of stasis
                    // ulcers and status of problem stasis ulcer both should be blank
                    this.NBR_STASULC_INVLD = true;
                }
            }
        }

        return !this.NBR_STASULC_INVLD;
    }

    /**
     * Oasis-C M1330_STAS_ULCR_PRSNT Does This Patient Have A Stasis Ulcer
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateUNOBS_STASULC(
            HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            String str = record.getSTAS_ULCR_PRSNT();

            if (!ValidateUtils.isValidValue(str, ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)) {
                this.UNOBS_STASULC_INVLD = true;
            } else {

				// 01 = patient has both observable and unobservable
                // 02 = patient has observable only
                if ("01".equals(str) || "02".equals(str)) {
                    // if number of stasis ulcers or status of problem stasis ulcer are blank, then flag the error
                    if (ValidateUtils.SPACE_2.equals(record.getNBR_STAS_ULCR())
                            || ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_STAS_ULCR())) {
                        this.UNOBS_STASULC_INVLD = true;
                    }
                } else if (("00".equals(str) || "03".equals(str))
                        && !ValidateUtils.SPACE_2.equals(record.getNBR_STAS_ULCR())
                        || !ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_STAS_ULCR())) {
					// 00 = patient does not have any stasis ulcers
                    // 03 = patient has only an unobservable stasis ulcer
                    // the stasis ulcer is 00 or 03, then the number of stasis
                    // ulcers and status of problem stasis ulcer both should be blank
                    this.UNOBS_STASULC_INVLD = true;
                }
            }
        }

        return !this.UNOBS_STASULC_INVLD;
    }

    /**
     * Oasis-C M1334_STUS_PRBLM_STAS_ULCER Status Of Most Problematic Stasis
     * Ulcer
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateSTATSTASIS(HomeHealthRecordIF record) {

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            String str = record.getSTAS_ULCR_PRSNT();

            if (!ValidateUtils.isValidValue(record.getSTUS_PRBLM_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)
                    && !ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_STAS_ULCR())) {
                this.STATSTASIS_INVLD = true;
            } else {

				// 01 = patient has both observable and unobservable
                // 02 = patient has observable only
                // if the stasis Ulcer indicator is observable
                if ("01".equals(str) || "02".equals(str)) {
                    // the status of the problem ulcer should be indicated
                    if (!ValidateUtils.isValidValue(record.getSTUS_PRBLM_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)
                            || ValidateUtils.SPACE_2.equals(record.getNBR_STAS_ULCR())) {

                        this.STATSTASIS_INVLD = true;
                    }

                } else if ((ValidateUtils.DOUBLE_ZERO.equals(str) || "03".equals(str))
                        && (!ValidateUtils.SPACE_2.equals(record.getNBR_STAS_ULCR())
                        || !ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_STAS_ULCR()))) {
					// 00 = patient does not have any stasis ulcers
                    // 03 = patient has only an unobservable stasis ulcer
                    // no or unobservable stasis ulcer, so the status should be skipped
                    this.STATSTASIS_INVLD = true;
                }
            }
        }
        return !this.STATSTASIS_INVLD;
    }

    /**
     * M1342_STUS_PRBLM_SRGCL_WND Status Of Most Problematic Surgical Wound
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateSTATSURG(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {

            if ("01".equals(record.getSRGCL_WND_PRSNT())) {
				// if there is an observable surgical wound, then
                // the status can not be blank
                if (ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_SRGCL_WND())) {
                    this.STATSURG_INVLD = true;
                }

            } else if ((ValidateUtils.DOUBLE_ZERO.equals(record.getSRGCL_WND_PRSNT())
                    || "02".equals(record.getSRGCL_WND_PRSNT()))
                    && !ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_SRGCL_WND())) {
				// if there is no or unobservable surgical wound,
                // then the status must be blank
                this.STATSURG_INVLD = true;
            }
        }

        return !this.STATSURG_INVLD;
    }

    /**
     * M1400_WHEN_DYSPNEIC When Dyspneic
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateWHEN_DYSPNEIC(HomeHealthRecordIF record) {
        return ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES) ? super.validateWHEN_DYSPNEIC(record) : true;
    }

    /**
     * M1610_UR_INCONT Urinary Incontinence or Urinary Catheter
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateUR_INCONT(HomeHealthRecordIF record) {
		// Nov 19, 2009 update from Errata
        // Edits #1 and 2 are only valid for RFA's 01, 03, and 09 as
        // M1615_INCNTNT_TIMING is not active for RFA's 04 and 05.
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)
                && super.validateUR_INCONT(record)) {
			// *2. If M1610_UR_INCONT = 01, then M1615_INCNTNT_TIMING
            // cannot be blank.
            if ("01".equals(record.getUR_INCONT())) {
                if (ValidateUtils.SPACE_2.equals(((HomeHealthRecord_C_IF) record).getINCNTNT_TIMING())) {
                    this.UR_INCONT_INVLD = true;
                }
            } else if (!ValidateUtils.SPACE_2.equals(((HomeHealthRecord_C_IF) record).getINCNTNT_TIMING())) {
				// *1. If M1610_UR_INCONT = 00 or 02, then M1615_INCNTNT_TIMING
                // must be skipped (blank).
                this.UR_INCONT_INVLD = true;
            }
        }
        return !super.UR_INCONT_INVLD;
    }

    /**
     * M1620_BWL_INCONT Bowel Incontinence Frequency
     *
     * Only checked for Assessment Reason 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateBWL_INCONT(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                && !ValidateUtils.isValidValue(record.getBWL_INCONT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
			// for assessment reasons 4 and 5, bowel incontinence
            // can not be UK - i.e. is must be one of the other valid values
            this.BWLINCONT_INVLD = true;
        }

        return !this.BWLINCONT_INVLD;
    }

    /**
     * M1630_OSTOMY Ostomy for Bowel Elimination
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateBWL_INCONT_OSTOMY(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            if (ValidateUtils.NOT_APPLICABLE.equals(record.getBWL_INCONT())) {
				// if bowl incontinence is NA, then the Ostomy
                // must be 01 or 02
                if (!"01".equals(record.getOSTOMY())
                        && !"02".equals(record.getOSTOMY())) {
                    this.OSTOMY_INVLD = true;
                }

            } else if (ValidateUtils.isValidValue(record.getBWL_INCONT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE, ValidateUtils.ARRAY_UNKNOWN_UK)
                    && !ValidateUtils.DOUBLE_ZERO.equals(record.getOSTOMY())) {
				// if the bowl incontinence is 00-05, or UK, then Ostomy
                // must be 00
                this.OSTOMY_INVLD = true;
            }
        }

        return !this.OSTOMY_INVLD;
    }


    /*------------------------------------------------------------
     * FUNCTIONAL DOMAIN VALIDATIONS
     * -------------------------------------------------------- */
    /**
     * M1810_CUR_DRESS_UPPER Current: Dress Upper Body and M1820_CUR_DRESS_LOWER
     * Current: Dress Lower Body
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateCUR_DRESS(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            if (!ValidateUtils.isValidValue(record.getCRNT_DRESS_UPPER(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)) {
                this.CUR_DRESS_INVLD = true;
            } else if (!ValidateUtils.isValidValue(record.getCRNT_DRESS_LOWER(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)) {
                this.CUR_DRESS_INVLD = true;
            }
        }

        return !this.CUR_DRESS_INVLD;
    }

    /**
     * M1830_CRNT_BATHG Current: Bathing
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateCUR_BATHING(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)
                && !ValidateUtils.isValidValue(record.getCRNT_BATHG(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE_SIX)) {
            this.CUR_BATHING_INVLD = true;
        }

        return !this.CUR_BATHING_INVLD;
    }

    /**
     * M1840_CUR_TOILTG Current: Toileting
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateCUR_TOILETING(HomeHealthRecordIF record) {
        return ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES) ? super.validateCUR_TOILETING(record) : true;
    }

    /**
     * M1850_CUR_TRNSFRNG Current: Transferring
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateCUR_TRANSFERRING(HomeHealthRecordIF record) {
        return ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES) ? super.validateCUR_TRANSFERRING(record) : true;
    }

    /**
     * M1860_CRNT_AMBLTN Current: Ambulation
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateCUR_AMBULATION(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)
                && !ValidateUtils.isValidValue(record.getCRNT_AMBLTN(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE_SIX)) {
            this.CUR_AMBULATION_INVLD = true;
        }
        return !this.CUR_AMBULATION_INVLD;
    }

    /**
     * M2030_CRNT_MGMT_INJCTN_MDCTN Current: Management Of Injectable
     * Medications
     *
     * Only checked for Assessment Reason 01, 03, 04, and 05 - because the
     * related field Drug Regimen Review is only valid during 01 and 03, the
     * validation is split
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateCUR_INJECT_MEDS(HomeHealthRecordIF record) {
        String tmpInjectMeds = record.getCRNT_MGMT_INJCTN_MDCTN();

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)) {
            String tmpRgmn = ((HomeHealthRecord_C_IF) record).getDRUG_RGMN_RVW();

            // when the drug regimen is 0, 1, or 2
            if (ValidateUtils.isValidValue(tmpRgmn, ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO)) {
				// then injectables can not be blank but must be
                // one of the other valid values
                if (!ValidateUtils.isValidValue(tmpInjectMeds, ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
                    this.CUR_INJECT_MEDS_INVLD = true;
                }

            } else if (ValidateUtils.NOT_APPLICABLE.equals(tmpRgmn)
                    && !ValidateUtils.SPACE_2.equals(record.getCRNT_MGMT_INJCTN_MDCTN())) {
                // if the drug regimen is NA then the injectables must be blank
                this.CUR_INJECT_MEDS_INVLD = true;
            }
        } else if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                && !ValidateUtils.isValidValue(tmpInjectMeds, ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
            // injectables can not be blank but one of the other valid values
            this.CUR_INJECT_MEDS_INVLD = true;
        }
        return !CUR_INJECT_MEDS_INVLD;
    }

    /*------------------------------------------------------------
     * SERVICE DOMAIN VALIDATIONS
     * -------------------------------------------------------- */
    /**
     * M2200_THER_NEED_NUM Therapy Need: Number of Visits
     *
     * M2200_THER_NEED_NA Therapy Need: Not Applicable
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    @Override
    public boolean validateTHER_NEED(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            if ("1".equals(record.getTHER_NEED_NA())) {

                /*
                 * Therapy Need Number is an int, so an invalid or
                 * blank value would be -1, and entered number would
                 * be >= 0
                 */
                if (record.getTHER_NEED_NBR() >= 0) {
                    this.THER_NEED_INVLD = true;
                }

            } else if ("0".equals(record.getTHER_NEED_NA())
                    && (record.getTHER_NEED_NBR() < 0
                    || record.getTHER_NEED_NBR() > 999)) {
                // ensure the therapy count is in the valid range
                this.THER_NEED_INVLD = true;
            }
        }

        return !this.THER_NEED_INVLD;
    }
}
