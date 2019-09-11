/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v2308_1;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.DataValidityFlag;
import com.mmm.cms.homehealth.proto.DataValidityFlagIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_B_IF;
import com.mmm.cms.util.ScoringEventFormatter;
import com.mmm.cms.util.ValidateUtils;

import java.util.Collection;

/**
 * This validator provides the common validation for the Home Health Record that
 * are performed for both the Clinical and the Non-Routine Supplies models for
 * the Grouper version 2.03
 *
 *
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public abstract class AbstractBaseValidator_v2308 implements
        HomeHealthRecordValidatorIF {

    public final static String[] ASSESSMENT_REASON_VALUES = new String[]{"01", "03", "04", "05"};
    public final static String[] ASSESSMENT_1_3_ONLY = new String[]{"01", "03"};
    public final static String[] ASSESSMENT_4_5_ONLY = new String[]{"04", "05"};
    /**
     * Indicates if ASSMT_REASON is invalid
     */
    protected boolean ASSMT_REASON_INVLD;
    /**
     * Indicates if INFO_COMPLETED_DT is invalid
     */
    protected boolean INFO_COMPLETED_DT_INVLD;
    /**
     * Indicates if THERAPIES is invalid
     */
    protected boolean THERAPIES_INVLD;
    /**
     * Indicates if INTERNAL_LOGIC is invalid
     */
    protected boolean INTERNAL_LOGIC_INVLD;
    /**
     * Indicates if VISION is invalid
     */
    protected boolean VISION_INVLD;
    /**
     * Indicates if PAIN is invalid
     */
    protected boolean PAIN_INVLD;
    /**
     * Indicates if LESION is invalid
     */
    protected boolean LESION_INVLD;
    /**
     * Indicates if NPRSULC1 is invalid
     */
    protected boolean NPRSULC1_INVLD;
    /**
     * Indicates if NPRSULC2 is invalid
     */
    protected boolean NPRSULC2_INVLD;
    /**
     * Indicates if NPRSULC3 is invalid
     */
    protected boolean NPRSULC3_INVLD;
    /**
     * Indicates if NPRSULC4 is invalid
     */
    protected boolean NPRSULC4_INVLD;
    /**
     * Indicates if UNOBS_PRSULC is invalid
     */
    protected boolean UNOBS_PRSULC_INVLD;
    /**
     * Indicates if STGPRSUL is invalid
     */
    protected boolean STGPRSUL_INVLD;
    /**
     * Indicates if NBR_STASULC is invalid
     */
    protected boolean NBR_STASULC_INVLD;
    /**
     * Indicates if UNOBS_STASULC is invalid
     */
    protected boolean UNOBS_STASULC_INVLD;
    /**
     * Indicates if STATSTASIS is invalid
     */
    protected boolean STATSTASIS_INVLD;
    /**
     * Indicates if STATSURG is invalid
     */
    protected boolean STATSURG_INVLD;
    /**
     * Indicates if DYSPNEIC is invalid
     */
    protected boolean DYSPNEIC_INVLD;
    /**
     * Indicates if UR_INCONT is invalid
     */
    protected boolean UR_INCONT_INVLD;
    /**
     * Indicates if BWLINCONT is invalid
     */
    protected boolean BWLINCONT_INVLD;
    /**
     * Indicates if OSTOMY is invalid
     */
    protected boolean OSTOMY_INVLD;
    /**
     * Indicates if CUR_DRESS is invalid
     */
    protected boolean CUR_DRESS_INVLD;
    /**
     * Indicates if CUR_BATHING is invalid
     */
    protected boolean CUR_BATHING_INVLD;
    /**
     * Indicates if CUR_TOILETING is invalid
     */
    protected boolean CUR_TOILETING_INVLD;
    /**
     * Indicates if CUR_TRANSFER is invalid
     */
    protected boolean CUR_TRANSFER_INVLD;
    /**
     * Indicates if CUR_AMBULATION is invalid
     */
    protected boolean CUR_AMBULATION_INVLD;
    /**
     * Indicates if CUR_INJECT_MEDS is invalid
     */
    protected boolean CUR_INJECT_MEDS_INVLD;
    /**
     * Indicates if THER_NEED is invalid
     */
    protected boolean THER_NEED_INVLD;
    /**
     * the overall data validity information
     */
    protected DataValidityFlagIF dataValidityFlag;
    /**
     * Reference to the Grouper
     */
    protected HomeHealthGrouperIF grouper;
    protected int[] diagnosisScoringStatus;

    /**
     * @param grouper
     */
    public AbstractBaseValidator_v2308(HomeHealthGrouperIF grouper) {
        this.grouper = grouper;
        dataValidityFlag = new DataValidityFlag();
        diagnosisScoringStatus = new int[18];
    }

    /**
     * gets the DataValidityFlag information
     *
     * @return the data validity flag object - this will never be null
     */
    @Override
    public DataValidityFlagIF getDataValidityFlag() {
        return dataValidityFlag;
    }

    /**
     * sets the data validity flag object - should not be null
     *
     * @param dataValidityFlag
     */
    @Override
    public void setDataValidityFlag(DataValidityFlagIF dataValidityFlag) {
        if (dataValidityFlag == null) {
            throw new IllegalArgumentException("dataValidityFlag parameter can not be null");
        }
        this.dataValidityFlag = dataValidityFlag;
    }

    /**
     * gets the ClinicalDomainDataIssueFlag
     *
     * @return
     */
    @Override
    public final boolean isClinicalDomainDataIssueFlag() {
        return dataValidityFlag.isClinicalIssue();
    }

    /**
     * gets the FunctionalDomainDataIssueFlag
     *
     * @return
     */
    @Override
    public final boolean isFunctionalDomainDataIssueFlag() {
        return dataValidityFlag.isFunctionalIssue();
    }

    /**
     * gets the ManifestationSequenceIssueFlag
     *
     * @return
     */
    @Override
    public final boolean isManifestationSequenceIssueFlag() {
        return dataValidityFlag.isManifestationSequenceIssue();
    }

    /**
     * gets the ServiceDomainDataIssueFlag
     *
     * @return
     */
    @Override
    public final boolean isServiceDomainDataIssueFlag() {
        return dataValidityFlag.isServiceIssue();
    }
    
    @Override
    public CollectionValidationEditsIF validate(HomeHealthRecordIF recordIF, Collection<HomeHealthEventListenerIF> listeners) {
        final CollectionValidationEditsIF edits = null;
        DiagnosisCodeIF code;
        int idx;
        HomeHealthRecord_B_IF record;

        if (recordIF instanceof HomeHealthRecord_B_IF) {
            record = (HomeHealthRecord_B_IF) recordIF;

            // check for clinical issues on the record
            if (!validateASSMT_REASON(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Assessment Reason", record.getASSMT_REASON());
            }

            if (!validateExistingCodes(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Primary Diagnosis", record.getPRIMARY_DIAG_ICD().getCode());
            }

            if (!validatePRIMARY_DIAG_ICD(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Primary Diagnosis", record.getPRIMARY_DIAG_ICD().getCode());
            }

            if (!validatePAYMENT_ECodes(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Payment Codes E-codes", "<various>");
            }

            if (!validateTHH(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Therapies Received at Home",
                        record.getTHH_IV_INFUSION() + ", "
                        + record.getTHH_PAR_NUTRITION() + ", "
                        + record.getTHH_ENT_NUTRITION() + ", "
                        + record.getTHH_NONE_ABOVE());

            }
            if (!validateVISION(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Vision", record.getVISION());
            }

            if (!validateLESION_OPEN_WND(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Open Lesion Wound", record.getLESION_OPEN_WND());
            }

            if (!validateFREQ_PAIN(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Frequent Pain", record.getPAIN_FREQ_ACTVTY_MVMT());
            }

            if (!validateNPRSULC(record, listeners)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Number of Pressure Ulcers (4 Stages)",
                        record.getNBR_PRSULC_STG1() + ", "
                        + record.getNBR_PRSULC_STG2() + ", "
                        + record.getNBR_PRSULC_STG3() + ", "
                        + record.getNBR_PRSULC_STG4() + ", "
                        + record.getUNOBS_PRSULC());
            }

            if (!validateSTGPRSUL(record, listeners)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Stage of Problem Ulcer", record.getSTG_PRBLM_ULCER());
            }

            if (!validateNBR_STASULC(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Number of Stasis Ulcers", record.getNBR_STAS_ULCR());
            }

            if (!validateUNOBS_STASULC(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Unobserved Stasis Ulcers", record.getUNOBS_STASULC());
            }

            if (!validateSTATSTASIS(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Status of Stasis Ulcers", record.getSTUS_PRBLM_STAS_ULCR());
            }

            if (!validateSTATSURG(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Status of Surgical Wound", record.getSTUS_PRBLM_SRGCL_WND());
            }

            if (!validateWHEN_DYSPNEIC(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "When Dyspneic", record.getWHEN_DYSPNEIC());
            }

            if (!validateUR_INCONT(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Urinary Incontinence", record.getUR_INCONT());
            }

            if (!validateBWL_INCONT(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Bowl Incontinence", record.getBWL_INCONT());
            }

            if (!validateBWL_INCONT_OSTOMY(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Bowl Incontinence Ostomy", record.getOSTOMY());
            }

            if (!validateCUR_INJECT_MEDS(record)) {
                dataValidityFlag.setClinicalIssue(true);
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Current Injectable Meds", record.getCRNT_MGMT_INJCTN_MDCTN());
            }

            // check for functional issues on the record
            if (!validateCUR_DRESS(record)) {
                dataValidityFlag.setFunctionalIssue(true);
                ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null,
                        "Current Dressing (Upper & Lower)",
                        record.getCRNT_DRESS_UPPER() + ", " + record.getCRNT_DRESS_LOWER());
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
                        "Current Tranferring", record.getCRNT_TRNSFRNG());
            }

            if (!validateCUR_AMBULATION(record)) {
                dataValidityFlag.setFunctionalIssue(true);
                ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null,
                        "Current Ambulation", record.getCRNT_AMBLTN());
            }

            // check for service issues on the record
            if (!validateTHER_NEED(record)) {
                dataValidityFlag.setServiceIssue(true);
                ScoringEventFormatter.fireServiceIssueEvent(listeners, grouper, null,
                        "Therapy Need and Number of Therapies",
                        record.getTHER_NEED_NBR() + ", " + record.getTHER_NEED_NA());
            }

            // check for manifestation issues on the record
            if (!isValidPrincipalDiagnosisCode(record)) {
                dataValidityFlag.setManifestationSequenceIssue(true);
                ScoringEventFormatter.fireManifestationIssueEvent(listeners, grouper, null,
                        "Primary Diagnosis", record.getPRIMARY_DIAG_ICD().getCode());
            }

            if (!validateDiagnosisCodes(record, listeners)) {
                dataValidityFlag.setManifestationSequenceIssue(true);
                ScoringEventFormatter.fireManifestationIssueEvent(listeners, grouper, null,
                        "All Diagnosis", "<various>");
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
        }
        
        return edits;
    }
    
    
    /**
     * Entry to higher level validate for the Oasis record. It calls all the
     * clinical, functional and service domain validation.
     *
     * This method assumes that the diagnosis codes have been populate by the
     * current grouper
     *
     * @param recordIF
     * @return
     */
    @Override
    public boolean validate(HomeHealthRecordIF recordIF) {
        validate(recordIF, null);
        return dataValidityFlag.isIssuePresent() == false;
    }


    /*------------------------------------------------------------
     * CLINICAL DOMAIN VALIDATIONS
     * -------------------------------------------------------- */
    /**
     * Pseudo code lines: no corresponding line
     *
     * @param record
     * @return
     */
    public boolean validateASSMT_REASON(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            this.ASSMT_REASON_INVLD = true;
        }
        return !this.ASSMT_REASON_INVLD;
    }

    /**
     * Ensures that all non-blank codes are valid ICD-9-Codes within the record.
     * Note that if all the codes are blank, this method will return true, i.e.
     * valid. This does not indicate which diagnosis invalid.
     *
     * Added as part of the increased specificity validation of the record
     * pertaining to the ICD-9-Codes
     *
     * @param record
     * @return true if all the codes are blank and/or valid ICD-9-Codes
     */
    public boolean validateExistingCodes(
            HomeHealthRecordIF record) {

        int idx = 6;
        DiagnosisCodeIF code;
        boolean valid = true;

        while (idx-- > 0) {
            code = record.getDiagnosisCode(idx);

			// Blank codes are acceptable, but invalid codes
            // are not.
            if (!code.isValidCode() && !code.isEmpty()) {
                valid = false;
                break;
            }

            code = record.getOptionalDiagnosisCode3(idx);
			// Blank codes are acceptable, but invalid codes
            // are not.
            if (!code.isValidCode() && !code.isEmpty()) {
                valid = false;
                break;
            }

            code = record.getOptionalDiagnosisCode4(idx);
			// Blank codes are acceptable, but invalid codes
            // are not.
            if (!code.isValidCode() && !code.isEmpty()) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    /**
     * Ensure the primary codes is not an E-Code
     *
     * Pseudo code lines: 563 thru 565, and 679
     *
     * @param record
     * @return
     */
    public boolean validatePRIMARY_DIAG_ICD(HomeHealthRecordIF record) {
        final DiagnosisCodeIF code = record.getPRIMARY_DIAG_ICD();
        boolean valid = true;

        if (code.isExternalCauseCode()) {
            code.setValidForScoring(false);
            valid = false;
        }
        return valid;
    }

    /**
     * This checks for E-codes in the optional columns
     *
     * Pseudo code lines: 563 thru 565, and 679
     *
     * @param record
     * @return
     */
    public boolean validatePAYMENT_ECodes(HomeHealthRecordIF record) {
        DiagnosisCodeIF code;
        int idx = 0;
        boolean valid = true;

        while (idx < 6) {
            // look at the "colum 3" codes
            code = record.getOptionalDiagnosisCode3(idx);
            if (code.isExternalCauseCode()) {
                valid = false;
                code.setValidForScoring(valid);
            }
            // look at the "colum 4" codes
            code = record.getOptionalDiagnosisCode4(idx);
            if (code.isExternalCauseCode()) {
                valid = false;
                code.setValidForScoring(valid);
            }
            idx++;
        }

        return valid;
    }

    /**
     * Validate the valid values for M0250 and the logic between the M250 values
     *
     * Pseudo code lines: 781 thru 806
     *
     * @param record
     * @return
     */
    public boolean validateTHH(HomeHealthRecordIF record) {

        if (!ValidateUtils.isValidValue(record.getTHH_IV_INFUSION(), ValidateUtils.ARRAY_ZERO_ONE)) {
            this.THERAPIES_INVLD = true;
        }

        if (!ValidateUtils.isValidValue(record.getTHH_PAR_NUTRITION(), ValidateUtils.ARRAY_ZERO_ONE)) {
            this.THERAPIES_INVLD = true;
        }

        if (!ValidateUtils.isValidValue(record.getTHH_ENT_NUTRITION(), ValidateUtils.ARRAY_ZERO_ONE)) {
            this.THERAPIES_INVLD = true;
        }

        if (!ValidateUtils.isValidValue(record.getTHH_NONE_ABOVE(), ValidateUtils.ARRAY_ZERO_ONE)) {
            this.THERAPIES_INVLD = true;
        }

        if ("1".equals(record.getTHH_NONE_ABOVE())
                && ("1".equals(record.getTHH_IV_INFUSION())
                || "1".equals(record.getTHH_PAR_NUTRITION())
                || "1".equals(record.getTHH_ENT_NUTRITION()))) {
            this.INTERNAL_LOGIC_INVLD = true;
        }

        if ("0".equals(record.getTHH_NONE_ABOVE())
                && "0".equals(record.getTHH_IV_INFUSION())
                && "0".equals(record.getTHH_PAR_NUTRITION())
                && "0".equals(record.getTHH_ENT_NUTRITION())) {
            this.INTERNAL_LOGIC_INVLD = true;
        }

        return !(this.THERAPIES_INVLD || this.INTERNAL_LOGIC_INVLD);
    }

    /**
     * Pseudo code lines: 808 thru 810
     *
     * @param record
     * @return
     */
    public boolean validateVISION(HomeHealthRecordIF record) {

        if (!ValidateUtils.isValidValue(record.getVISION(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO)) {
            this.VISION_INVLD = true;
        }
        return !this.VISION_INVLD;
    }

    /**
     * Pseudo code lines: 812 thru 814
     *
     * @param record
     * @return
     */
    public boolean validateFREQ_PAIN(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getPAIN_FREQ_ACTVTY_MVMT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)) {
            this.PAIN_INVLD = true;
        }
        return !this.PAIN_INVLD;
    }

    /**
     * Pseudo code lines: 816 thru 818
     *
     * @param record
     * @return
     */
    public boolean validateLESION_OPEN_WND(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getLESION_OPEN_WND(), ValidateUtils.ARRAY_ZERO_ONE)) {
            this.LESION_INVLD = true;
        }
        return !this.LESION_INVLD;
    }

    /**
     * Pseudo code lines: 820 thru 924
     *
     * @param record
     * @return
     */
    public boolean validateNPRSULC(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
        // check that the assessment reason is not 4 or 5
        if (!ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)) {

            // check for an open lesion and ulcer is indicated
            if ("1".equals(record.getLESION_OPEN_WND())
                    && "1".equals(((HomeHealthRecord_B_IF) record).getPRESS_ULCER())) {

                // check m0450a
                if (!ValidateUtils.isValidValue(record.getNBR_PRSULC_STG1(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC1_INVLD = true;
                }

                // check m0450b
                if (!ValidateUtils.isValidValue(record.getNBR_PRSULC_STG2(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC2_INVLD = true;
                }

                // check m0450c
                if (!ValidateUtils.isValidValue(record.getNBR_PRSULC_STG3(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC3_INVLD = true;
                }

                // check m0450d
                if (!ValidateUtils.isValidValue(record.getNBR_PRSULC_STG4(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC4_INVLD = true;
                }

                // check m0450e
                if (!ValidateUtils.isValidValue(((HomeHealthRecord_B_IF) record).getUNOBS_PRSULC(), ValidateUtils.ARRAY_ZERO_ONE)) {
                    this.UNOBS_PRSULC_INVLD = true;
                }
            } else {
				// check for either lesion or ucler not indicated (i.e. not = 1)
                // but the ulcer stage is indicated
                // check m0450a
                if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG1(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC1_INVLD = true;
                }

                // check m0450b
                if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG2(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC2_INVLD = true;
                }

                // check m0450c
                if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG3(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC3_INVLD = true;
                }

                // check m0450d
                if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG4(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC4_INVLD = true;
                }

                // check m0450e
                if (ValidateUtils.isValidValue(((HomeHealthRecord_B_IF) record).getUNOBS_PRSULC(), ValidateUtils.ARRAY_ZERO_ONE)) {
                    this.UNOBS_PRSULC_INVLD = true;
                }
            }
        } else if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)) {
			// the assessment reason IS 4 or 5

            // check if the lesion is open
            if ("1".equals(record.getLESION_OPEN_WND())) {

				// but the ulcer stage is not indicated
                // check m0450a
                if (!ValidateUtils.isValidValue(record.getNBR_PRSULC_STG1(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR, ValidateUtils.ARRAY_SPACE_2)) {
                    this.NPRSULC1_INVLD = true;
                }

                // check m0450b
                if (!ValidateUtils.isValidValue(record.getNBR_PRSULC_STG2(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR, ValidateUtils.ARRAY_SPACE_2)) {
                    this.NPRSULC2_INVLD = true;
                }

                // check m0450c
                if (!ValidateUtils.isValidValue(record.getNBR_PRSULC_STG3(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR, ValidateUtils.ARRAY_SPACE_2)) {
                    this.NPRSULC3_INVLD = true;
                }

                // check m0450d
                if (!ValidateUtils.isValidValue(record.getNBR_PRSULC_STG4(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR, ValidateUtils.ARRAY_SPACE_2)) {
                    this.NPRSULC4_INVLD = true;
                }

                // check m0450e
                if (!" ".equals(((HomeHealthRecord_B_IF) record).getUNOBS_PRSULC())
                        && !ValidateUtils.isValidValue(((HomeHealthRecord_B_IF) record).getUNOBS_PRSULC(), ValidateUtils.ARRAY_ZERO_ONE)) {
                    this.UNOBS_PRSULC_INVLD = true;
                }

            } else if (!"1".equals(record.getLESION_OPEN_WND())) { // non-lesion
                // but the ulcer stage is indicated
                // check m0450a
                if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG1(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC1_INVLD = true;
                }

                // check m0450b
                if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG2(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC2_INVLD = true;
                }

                // check m0450c
                if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG3(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC3_INVLD = true;
                }

                // check m0450d
                if (ValidateUtils.isValidValue(record.getNBR_PRSULC_STG4(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NPRSULC4_INVLD = true;
                }

                // check m0450e
                if (ValidateUtils.isValidValue(((HomeHealthRecord_B_IF) record).getUNOBS_PRSULC(), ValidateUtils.ARRAY_ZERO_ONE)) {
                    this.UNOBS_PRSULC_INVLD = true;
                }
            }
        }

        return !(this.NPRSULC1_INVLD
                || this.NPRSULC2_INVLD
                || this.NPRSULC3_INVLD
                || this.NPRSULC4_INVLD
                || this.UNOBS_PRSULC_INVLD);
    }

    /**
     * Pseudo code lines: 925 thru 965
     *
     * @param record
     * @return
     */
    public boolean validateSTGPRSUL(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
        // check that the assessment reason is not 4 or 5
        if (!ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)) {

            // check for an open lesion and ulcer is indicated
            if ("1".equals(record.getLESION_OPEN_WND())
                    && "1".equals(((HomeHealthRecord_B_IF) record).getPRESS_ULCER())) {

				// check for the stage problem is not either na or invalid value
                // NOTE: Should this also check for no blank?
                if (!ValidateUtils.isValidValue(record.getSTG_PRBLM_ULCER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
                    this.STGPRSUL_INVLD = true;
                }

				// check for an NBR stage indicator but no stage problem ulcer
                // indicator
                if ((ValidateUtils.isValidValue(record.getNBR_PRSULC_STG1(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)
                        || ValidateUtils.isValidValue(record.getNBR_PRSULC_STG2(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)
                        || ValidateUtils.isValidValue(record.getNBR_PRSULC_STG3(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)
                        || ValidateUtils.isValidValue(record.getNBR_PRSULC_STG4(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR))
                        && !ValidateUtils.isValidValue(record.getSTG_PRBLM_ULCER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {
                    this.STGPRSUL_INVLD = true;
                }

            } else {
				// either the lesion or the ucler are not indicated

                // check if stage problem is not applicable or valid value
                if (ValidateUtils.isValidValue(record.getSTG_PRBLM_ULCER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
                    this.STGPRSUL_INVLD = true;
                }
            }
        } else {
			// the assessment reason IS 4 or 5

            // check for an open lesion and ulcer is indicated
            if ("1".equals(record.getLESION_OPEN_WND())) {

                // check for the stage problem not either blank, na or invalid value
                if (!ValidateUtils.isValidValue(record.getSTG_PRBLM_ULCER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR, ValidateUtils.ARRAY_SPACE_2, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
                    this.STGPRSUL_INVLD = true;
                }

				// check for an NBR stage indicator but no stage problem ulcer
                // indicator
                if ((ValidateUtils.isValidValue(record.getNBR_PRSULC_STG1(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)
                        || ValidateUtils.isValidValue(record.getNBR_PRSULC_STG2(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)
                        || ValidateUtils.isValidValue(record.getNBR_PRSULC_STG3(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)
                        || ValidateUtils.isValidValue(record.getNBR_PRSULC_STG4(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR))
                        && !ValidateUtils.isValidValue(record.getSTG_PRBLM_ULCER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {
                    this.STGPRSUL_INVLD = true;
                }

            } else {
				// open lesion not indicated

                // check for the stage problem either na or invalid value
                if (ValidateUtils.isValidValue(record.getSTG_PRBLM_ULCER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
                    this.STGPRSUL_INVLD = true;
                }
            }
        }

        return !(this.STGPRSUL_INVLD);
    }

    /**
     * Pseudo code lines: 967 thru 987
     *
     * @param record
     * @return
     */
    public boolean validateNBR_STASULC(HomeHealthRecordIF record) {

        // check that the assessment reason is not 4 or 5
        if (!ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)) {

            // check for an open lesion and ulcer is indicated
            if ("1".equals(record.getLESION_OPEN_WND())
                    && "1".equals(record.getSTAS_ULCR_PRSNT())) {

                // check the NBR STASULCER
                if (!ValidateUtils.isValidValue(record.getNBR_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NBR_STASULC_INVLD = true;
                }
            } else {
				// either the lesion or the ucler are not indicated

                // check the NBR STASULCER
                if (ValidateUtils.isValidValue(record.getNBR_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NBR_STASULC_INVLD = true;
                }
            }
        } else {
			// the assessment reason IS 4 or 5

            // check for an open lesion is indicated
            if ("1".equals(record.getLESION_OPEN_WND())) {

                // check the NBR STASULCER
                if (!ValidateUtils.isValidValue(record.getNBR_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR, ValidateUtils.ARRAY_SPACE_2)) {
                    this.NBR_STASULC_INVLD = true;
                }
            } else {
				// either the lesion or the ucler are not indicated

                // check the NBR STASULCER
                if (ValidateUtils.isValidValue(record.getNBR_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
                    this.NBR_STASULC_INVLD = true;
                }
            }
        }

        return !this.NBR_STASULC_INVLD;
    }

    /**
     * Pseudo code lines: 989 thru 1011
     *
     * @param record
     * @return
     */
    public boolean validateUNOBS_STASULC(HomeHealthRecordIF record) {

        // check that the assessment reason is not 4 or 5
        if (!ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)) {

            // check for an open lesion and ulcer is indicated
            if ("1".equals(record.getLESION_OPEN_WND())
                    && "1".equals(record.getSTAS_ULCR_PRSNT())) {

                // check the UNOB STASULCER
                if (!ValidateUtils.isValidValue(((HomeHealthRecord_B_IF) record).getUNOBS_STASULC(), ValidateUtils.ARRAY_ZERO_ONE)) {
                    this.UNOBS_STASULC_INVLD = true;
                }
            } else {
				// either the lesion or the ucler are not indicated

                // check the UNOB STASULCER
                if (ValidateUtils.isValidValue(((HomeHealthRecord_B_IF) record).getUNOBS_STASULC(), ValidateUtils.ARRAY_ZERO_ONE)) {
                    this.UNOBS_STASULC_INVLD = true;
                }
            }
        } else {
			// the assessment reason IS 4 or 5

            // check for an open lesion is indicated
            if ("1".equals(record.getLESION_OPEN_WND())) {

                // check the UNOB STASULCER
                if (!ValidateUtils.isValidValue(((HomeHealthRecord_B_IF) record).getUNOBS_STASULC(), ValidateUtils.ARRAY_ZERO_ONE, ValidateUtils.ARRAY_SPACE_1)) {
                    this.UNOBS_STASULC_INVLD = true;
                }
            } else {
				// either the lesion or the ucler are not indicated

                // check the UNOB STASULCER
                if (ValidateUtils.isValidValue(((HomeHealthRecord_B_IF) record).getUNOBS_STASULC(), ValidateUtils.ARRAY_ZERO_ONE)) {
                    this.UNOBS_STASULC_INVLD = true;
                }
            }
        }

        return !this.UNOBS_STASULC_INVLD;

    }

    /**
     * Pseudo code lines: 1012 thru 1035
     *
     * @param record
     * @return
     */
    public boolean validateSTATSTASIS(HomeHealthRecordIF record) {

        // check that the assessment reason is not 4 or 5
        if (!ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)) {

            // check for an open lesion and ulcer is indicated
            if ("1".equals(record.getLESION_OPEN_WND())
                    && "1".equals(record.getSTAS_ULCR_PRSNT())
                    && ValidateUtils.isValidValue(record.getNBR_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {

                // check the PRB STASULCER
                if (!ValidateUtils.isValidValue(record.getSTUS_PRBLM_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {
                    this.STATSTASIS_INVLD = true;
                }
            } else {
				// either the lesion or the ucler are not indicated

                // check the PRB STASULCER
                if (ValidateUtils.isValidValue(record.getSTUS_PRBLM_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {
                    this.STATSTASIS_INVLD = true;
                }
            }
        } else {
			// the assessment reason IS 4 or 5

            // check for an open lesion is indicated
            if ("1".equals(record.getLESION_OPEN_WND())
                    && ValidateUtils.isValidValue(record.getNBR_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {

                // check the PRB STASULCER
                if (!ValidateUtils.isValidValue(record.getSTUS_PRBLM_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {
                    this.STATSTASIS_INVLD = true;
                }
            } else {
				// either the lesion or the ucler are not indicated

                // check the PRB STASULCER
                if (ValidateUtils.isValidValue(record.getSTUS_PRBLM_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {
                    this.STATSTASIS_INVLD = true;
                }
            }
        }

        return !this.STATSTASIS_INVLD;
    }

    /**
     * Pseudo code lines: 1037 thru 1057
     *
     * @param record
     * @return
     */
    public boolean validateSTATSURG(HomeHealthRecordIF record) {
        // check that the assessment reason is not 4 or 5
        if (!ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)) {

            // check for an open lesion and ulcer is indicated
            if ("1".equals(record.getLESION_OPEN_WND())
                    && "1".equals(record.getSRGCL_WND_PRSNT())) {

				// check the NBR surgical
                // check the PRB STASULCER
                if (ValidateUtils.isValidValue(((HomeHealthRecord_B_IF) record).getNBR_SURGWND(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)
                        && !ValidateUtils.isValidValue(record.getSTUS_PRBLM_SRGCL_WND(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE)) {
                    this.STATSURG_INVLD = true;
                }
            } else {
				// either the lesion or the ucler are not indicated
                // check the PRB STASULCER
                if (ValidateUtils.isValidValue(record.getSTUS_PRBLM_SRGCL_WND(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
                    this.STATSURG_INVLD = true;
                }
            }
        } else {
			// the assessment reason IS 4 or 5

            // check for an open lesion is indicated
            if ("1".equals(record.getLESION_OPEN_WND())) {
                // check the PRB STASULCER
                if (!ValidateUtils.isValidValue(record.getSTUS_PRBLM_SRGCL_WND(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE, ValidateUtils.ARRAY_SPACE_2, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
                    this.STATSURG_INVLD = true;
                }
            } else {
				// the lesion is not indicated

                // check the PRB STAT
                if (ValidateUtils.isValidValue(record.getSTUS_PRBLM_SRGCL_WND(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE)) {
                    this.STATSURG_INVLD = true;
                }
            }
        }

        return !this.STATSURG_INVLD;
    }

    /**
     * Pseudo code lines: 1059 thru 1062
     *
     * @param record
     * @return
     */
    public boolean validateWHEN_DYSPNEIC(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getWHEN_DYSPNEIC(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
            this.DYSPNEIC_INVLD = true;
        }
        return !this.DYSPNEIC_INVLD;
    }

    /**
     * Pseudo code lines: 1064 thru 1067
     *
     * @param record
     * @return
     */
    public boolean validateUR_INCONT(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getUR_INCONT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO)) {
            this.UR_INCONT_INVLD = true;
        }
        return !this.UR_INCONT_INVLD;
    }

    /**
     * Pseudo code lines: 1069 thru 1073
     *
     * @param record
     * @return
     */
    public boolean validateBWL_INCONT(HomeHealthRecordIF record) {
        // check that the assessment reason is 01 or 03
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)) {

            if (!ValidateUtils.isValidValue(record.getBWL_INCONT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE, ValidateUtils.ARRAY_NOT_APPLICABLE, ValidateUtils.ARRAY_UNKNOWN_UK)) {
                this.BWLINCONT_INVLD = true;
            }
        } else if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                && !ValidateUtils.isValidValue(record.getBWL_INCONT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
            this.BWLINCONT_INVLD = true;
        }

        return !this.BWLINCONT_INVLD;
    }

    /**
     * Pseudo code lines: 1080 thru 1090
     *
     * @param record
     * @return
     */
    public boolean validateBWL_INCONT_OSTOMY(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getBWL_INCONT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE, ValidateUtils.ARRAY_UNKNOWN_UK)) {

            if (!ValidateUtils.DOUBLE_ZERO.equals(record.getOSTOMY())) {
                this.OSTOMY_INVLD = true;
            }

        } else if (ValidateUtils.NOT_APPLICABLE.equals(record.getBWL_INCONT())) {

            if (!ValidateUtils.isValidValue(record.getOSTOMY(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {
                this.OSTOMY_INVLD = true;
            }

        } else if (!ValidateUtils.isValidValue(record.getOSTOMY(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO)) {
            this.OSTOMY_INVLD = true;
        }

        return !this.OSTOMY_INVLD;
    }

    /**
     * Pseudo code lines: 1092 thru 1095
     *
     * @param record
     * @return
     */
    public boolean validateCUR_INJECT_MEDS(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getCRNT_MGMT_INJCTN_MDCTN(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
            this.CUR_INJECT_MEDS_INVLD = true;
        }

        return !CUR_INJECT_MEDS_INVLD;
    }


    /*------------------------------------------------------------
     * FUNCTIONAL DOMAIN VALIDATIONS
     * -------------------------------------------------------- */
    /**
     * Pseudo code lines: 1098 thru 1111
     *
     * @param record
     * @return
     */
    public boolean validateCUR_DRESS(HomeHealthRecordIF record) {
        if (ValidateUtils.DOUBLE_ZERO.equals(record.getCRNT_DRESS_LOWER())
                && !ValidateUtils.isValidValue(record.getCRNT_DRESS_UPPER(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)) {
            this.CUR_DRESS_INVLD = true;
        } else if (ValidateUtils.DOUBLE_ZERO.equals(record.getCRNT_DRESS_UPPER())
                && !ValidateUtils.isValidValue(record.getCRNT_DRESS_LOWER(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)) {
            this.CUR_DRESS_INVLD = true;
        }

        return !this.CUR_DRESS_INVLD;
    }

    /**
     * Pseudo code lines: 1113 thru 1116
     *
     * @param record
     * @return
     */
    public boolean validateCUR_BATHING(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getCRNT_BATHG(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE)) {
            this.CUR_BATHING_INVLD = true;
        }

        return !this.CUR_BATHING_INVLD;
    }

    /**
     * Pseudo code lines: 1118 thru 1121
     *
     * @param record
     * @return
     */
    public boolean validateCUR_TOILETING(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getCRNT_TOILTG(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
            this.CUR_TOILETING_INVLD = true;
        }

        return !this.CUR_TOILETING_INVLD;
    }

    /**
     * Pseudo code lines: 1123 thru 1126
     *
     * @param record
     * @return
     */
    public boolean validateCUR_TRANSFERRING(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getCRNT_TRNSFRNG(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE)) {
            this.CUR_TRANSFER_INVLD = true;
        }

        return !this.CUR_TRANSFER_INVLD;
    }

    /**
     * Pseudo code lines: 1128 thru 1131
     *
     * @param record
     * @return
     */
    public boolean validateCUR_AMBULATION(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getCRNT_AMBLTN(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE)) {
            this.CUR_AMBULATION_INVLD = true;
        }

        return !this.CUR_AMBULATION_INVLD;
    }


    /*------------------------------------------------------------
     * SERVICE DOMAIN VALIDATIONS
     * -------------------------------------------------------- */
    /**
     * Pseudo code lines: 1135 thru 1147
     *
     * @param record
     * @return
     */
    public boolean validateTHER_NEED(HomeHealthRecordIF record) {
        final String therapyNeedNA = record.getTHER_NEED_NA();

        if (therapyNeedNA.equals("1")) {
            /*
             * M0826_THER_NEED_NUM is an int, so an invalid or
             * blank value would be -1, and entered number would
             * be >= 0
             */
            if (record.getTHER_NEED_NBR() >= 0) {
                this.THER_NEED_INVLD = true;
            }
        } else if (therapyNeedNA.equals("0")
                && (record.getTHER_NEED_NBR() < 0
                || record.getTHER_NEED_NBR() > 999)) {
            // ensure the therapy count is in the valid range
            this.THER_NEED_INVLD = true;
        }

        return !this.THER_NEED_INVLD;
    }

    /*------------------------------------------------------------
     * MANIFESTATION DOMAIN VALIDATIONS
     * -------------------------------------------------------- */
    /*
     * @return true if the code is not an E-code, and is not flagged as
     * secondary only
     *
     * @param record
     * @return
     */
    public boolean isValidPrincipalDiagnosisCode(HomeHealthRecordIF record) {
        final DiagnosisCodeIF code = record.getPRIMARY_DIAG_ICD();

        // (a) validate the Principal Diagnosis
        return code == null || code.isEmpty() || code.isSecondaryOnly() || code.isExternalCauseCode() ? false : true;
    }

    /**
     * Ensure a valid set of codes, and determines which codes to use in
     * scoring. This method is specific to the Model that is scoring.
     *
     * @param record
     * @return true if the PDX is an allowable Principal code
     */
    public abstract boolean validateDiagnosisCodes(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners);

    /**
     * Get the value of diagnosisScoringStatus
     *
     * @return the value of diagnosisScoringStatus
     */
    @Override
    public int[] getDiagnosisScoringStatus() {
        return diagnosisScoringStatus;
    }

    /**
     * Set the value of diagnosisScoringStatus
     *
     * @param diagnosisScoringStatus new value of diagnosisScoringStatus
     */
    @Override
    public void setDiagnosisScoringStatus(int[] diagnosisScoringStatus) {
        this.diagnosisScoringStatus = diagnosisScoringStatus;
    }

    @Override
    public boolean isCUR_BATHING_Valid() {
        return !CUR_BATHING_INVLD;
    }

    @Override
    public boolean isCUR_TOILETING_Valid() {
        return !CUR_TOILETING_INVLD;
    }

    @Override
    public boolean isCUR_TRANSFER_Valid() {
        return !CUR_TRANSFER_INVLD;
    }

    @Override
    public boolean isCUR_AMBULATION_Valid() {
        return !CUR_AMBULATION_INVLD;
    }

    @Override
    public boolean isCUR_INJECT_MEDS_Valid() {
        return !CUR_INJECT_MEDS_INVLD;
    }

    @Override
    public boolean isTHER_NEED_Valid() {
        return !THER_NEED_INVLD;
    }

    @Override
    public boolean isDYSPNEIC_Valid() {
        return !DYSPNEIC_INVLD;
    }

    @Override
    public boolean isUR_INCONT_Valid() {
        return !UR_INCONT_INVLD;
    }

    @Override
    public boolean isBWLINCONT_Valid() {
        return !BWLINCONT_INVLD;
    }

    @Override
    public boolean isOSTOMY_Valid() {
        return !OSTOMY_INVLD;
    }

    @Override
    public boolean isCUR_DRESS_Valid() {
        return !CUR_DRESS_INVLD;
    }

    @Override
    public boolean isSTGPRSUL_Valid() {
        return !STGPRSUL_INVLD;
    }

    @Override
    public boolean isNBR_STASULC_Valid() {
        return !NBR_STASULC_INVLD;
    }

    @Override
    public boolean isUNOBS_STASULC_Valid() {
        return !UNOBS_STASULC_INVLD;
    }

    @Override
    public boolean isSTATSTASIS_Valid() {
        return !STATSTASIS_INVLD;
    }

    @Override
    public boolean isSTATSURG_Valid() {
        return !STATSURG_INVLD;
    }

    @Override
    public boolean isNPRSULC1_Valid() {
        return !NPRSULC1_INVLD;
    }

    @Override
    public boolean isNPRSULC2_Valid() {
        return !NPRSULC2_INVLD;
    }

    @Override
    public boolean isNPRSULC3_Valid() {
        return !NPRSULC3_INVLD;
    }

    @Override
    public boolean isNPRSULC4_Valid() {
        return !NPRSULC4_INVLD;
    }

    @Override
    public boolean isUNOBS_PRSULC_Valid() {
        return !UNOBS_PRSULC_INVLD;
    }

    @Override
    public boolean isASSMT_REASON_Valid() {
        return !ASSMT_REASON_INVLD;
    }

    @Override
    public boolean isINFO_COMPLETED_DT_Valid() {
        return !INFO_COMPLETED_DT_INVLD;
    }

    @Override
    public boolean isINTERNAL_LOGIC_Valid() {
        return !INTERNAL_LOGIC_INVLD;
    }

    @Override
    public boolean isTHERAPIES_Valid() {
        return !THERAPIES_INVLD;
    }

    @Override
    public boolean isVISION_Valid() {
        return !VISION_INVLD;
    }

    @Override
    public boolean isPAIN_Valid() {
        return !PAIN_INVLD;
    }

    @Override
    public boolean isLESION_Valid() {
        return !LESION_INVLD;
    }

}
