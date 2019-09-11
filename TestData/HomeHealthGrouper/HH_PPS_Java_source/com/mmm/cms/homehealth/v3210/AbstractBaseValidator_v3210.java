/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3210;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisDataItemIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditTypeEN;
import com.mmm.cms.homehealth.vut.proto.OasisValidationEditIF;
import com.mmm.cms.homehealth.DataValidityFlag;
import com.mmm.cms.homehealth.proto.DataValidityFlagIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ScoringEventFormatter;
import com.mmm.cms.util.ValidateUtils;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This validator provides the common validation for the Home Health Record that
 * are performed for both the Clinical and the Non-Routine Supplies models
 *
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public abstract class AbstractBaseValidator_v3210 implements
        HomeHealthRecordValidatorIF {

    public final static String[] ASSESSMENT_REASON_VALUES_ALL = new String[]{"01", "03", "04", "05", "06", "07", "08", "09"};
    public final static String[] ASSESSMENT_REASON_VALUES = new String[]{"01", "03", "04", "05"};
    public final static String[] ASSESSMENT_1_3_ONLY = new String[]{"01", "03"};
    public final static String[] ASSESSMENT_4_5_ONLY = new String[]{"04", "05"};
    public final static String blank4 = "    ";
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
     * List of listeners to report events to
     */
//    protected List<HomeHealthEventListenerIF> listeners;
    /**
     * Reference to the Grouper
     */
    protected transient HomeHealthGrouperIF grouper;
    protected int[] diagnosisScoringStatus;

    /**
     * @param grouper
     */
    public AbstractBaseValidator_v3210(HomeHealthGrouperIF grouper) {
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

    /**
     * Since this is a historic validation, this will always return null
     * @param record
     * @param listeners
     * @return always null
     */
    @Override
    public CollectionValidationEditsIF validate(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
        DiagnosisCodeIF code;
        int idx;

        // check for clinical issues on the record
        // re-use Oasis-B
        if (!validateASSMT_REASON(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Assessment Reason", record.getASSMT_REASON());
        }

        //-----
        // Diagnosis code validations - start
        //-----
        if (!validateExistingCodes(record, listeners)) {
            dataValidityFlag.setClinicalIssue(true);
        }

        if (!validatePRIMARY_DIAG_ICD(record, listeners)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Primary Diagnosis", record.getPRIMARY_DIAG_ICD().getCode());
        }

        if (!validatePAYMENT_ECodes(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Payment Diagnosis E-codes", "<various>");
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
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Other Diagnosis are not unique", "<various>");
        }

        //-----
        // Diagnosis code validations - end
        //-----
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

        if (!validateFREQ_PAIN(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Frequent Pain", record.getPAIN_FREQ_ACTVTY_MVMT());
        }

        if (!validateNPRSULC(record, listeners)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Number of Pressure Ulcers (4 stages)",
                    record.getNBR_PRSULC_STG1() + ", "
                    + record.getNBR_PRSULC_STG2() + ", "
                    + record.getNBR_PRSULC_STG3() + ", "
                    + record.getNBR_PRSULC_STG4());
        }

        if (!validateSTGPRSUL(record, listeners)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Stage of Most Problematic Pressure Ulcer", record.getSTG_PRBLM_ULCER());
        }

        if (!validateNBR_STASULC(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Number of Stasis Ulcers", record.getNBR_STAS_ULCR());
        }

        if (!validateUNOBS_STASULC(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Unobserved Stasis Ulcer", record.getSTAS_ULCR_PRSNT());
        }

        if (!validateSTATSTASIS(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Status Stasis Ulcer", record.getSTUS_PRBLM_STAS_ULCR());
        }

        if (!validateSTATSURG(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Status Surgical Wound", record.getSTUS_PRBLM_SRGCL_WND());
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

        //----------------------------------------
        // FUNCTIONAL DOMAIN VALIDATION
        //----------------------------------------
        if (!validateCUR_INJECT_MEDS(record)) {
            dataValidityFlag.setClinicalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null, "Current Injectable Meds", record.getCRNT_MGMT_INJCTN_MDCTN());
        }

        // check for functional issues on the record
        if (!validateCUR_DRESS(record)) {
            dataValidityFlag.setFunctionalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null, "Current Dressing (Upper & Lower)", record.getCRNT_DRESS_UPPER()
                    + ", "
                    + record.getCRNT_DRESS_LOWER());
        }

        if (!validateCUR_BATHING(record)) {
            dataValidityFlag.setFunctionalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null, "Current Bathing", record.getCRNT_BATHG());
        }

        if (!validateCUR_TOILETING(record)) {
            dataValidityFlag.setFunctionalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null, "Current Toileting", record.getCRNT_TOILTG());
        }

        if (!validateCUR_TRANSFERRING(record)) {
            dataValidityFlag.setFunctionalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null, "Current Transferring", record.getCRNT_TRNSFRNG());
        }

        if (!validateCUR_AMBULATION(record)) {
            dataValidityFlag.setFunctionalIssue(true);
            ScoringEventFormatter.fireFunctionalIssueEvent(listeners, grouper, null, "Current Ambulation", record.getCRNT_AMBLTN());
        }

        //--------------------------------------------------
        // SERVICE DOMAIN VALIDATION
        //--------------------------------------------------
        // check for service issues on the record
        if (!validateTHER_NEED(record)) {
            dataValidityFlag.setServiceIssue(true);
            ScoringEventFormatter.fireServiceIssueEvent(listeners, grouper, null, "Therapy Need and Number of Therapies",
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

        return null;
    }

    /**
     * Entry to higher level validate for the Oasis record. It calls all the
     * clinical, functional and service domain validation.
     *
     * This method assumes that the diagnosis codes have been populate by the
     * current grouper
     *
     * @param record
     * @return true if a valid record
     */
    @Override
    public boolean validate(HomeHealthRecordIF record) {
        validate(record, null);
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
    // SAME AS VALIDATE ITEM
    public boolean validateASSMT_REASON(HomeHealthRecordIF record) {
        if (!ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES_ALL)) {
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
    // SAME AS VALIDATE EXISTING CODES ITEM
    public boolean validateExistingCodes(
            HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
        DiagnosisCodeIF code;
        boolean valid = true;

        for (int idx = 0; idx < 6; idx++) {
            code = record.getDiagnosisCode(idx);

            // Blank codes are acceptable, but invalid codes
            // are not.
            if (!code.isValidCode() && !code.isEmpty()) {
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Invalid Diagnosis code", code.getCode());
                valid = false;
            }
        }

        for (int idx = 0; idx < 6; idx++) {
            code = record.getOptionalDiagnosisCode3(idx);
            // Blank codes are acceptable, but invalid codes
            // are not.
            if (!code.isValidCode() && !code.isEmpty()) {
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Invalid Diagnosis code", code.getCode());
                valid = false;
            }

            code = record.getOptionalDiagnosisCode4(idx);
            // Blank codes are acceptable, but invalid codes
            // are not.
            if (!code.isValidCode() && !code.isEmpty()) {
                ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Invalid Diagnosis code", code.getCode());
                valid = false;
            }
        }

        return valid;
    }

    /**
     * This checks for E-codes in the optional columns
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
                    if (otherCode.isValidCode()
                            && currentCode.equals(otherCode)) {
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
    public boolean validatePRIMARY_DIAG_ICD(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
        boolean valid = true;
        DiagnosisCodeIF code = record.getPRIMARY_DIAG_ICD();

        // check for E-code validation
        if (code.isExternalCauseCode()) {
            code.setValidForScoring(false);
            valid = false;
        }

        // check for the primary being blank
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
    public boolean validateTHH(HomeHealthRecordIF record) {
        boolean valid;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
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

            valid = !(this.THERAPIES_INVLD || this.INTERNAL_LOGIC_INVLD);
        } else {
            valid = true;
        }

        return valid;
    }

    /**
     * Oasis-C M1200_VISION Vision
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    public boolean validateVISION(HomeHealthRecordIF record) {

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)
                && !ValidateUtils.isValidValue(record.getVISION(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO)) {
            this.VISION_INVLD = true;
        }
        return !this.VISION_INVLD;
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
    public boolean validateFREQ_PAIN(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES) && !ValidateUtils.isValidValue(record.getPAIN_FREQ_ACTVTY_MVMT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
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
    public boolean validateLESION_OPEN_WND(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            if (!ValidateUtils.isValidValue(record.getLESION_OPEN_WND(), ValidateUtils.ARRAY_ZERO_ONE)) {
                this.LESION_INVLD = true;
            }
        }
        return !this.LESION_INVLD;
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
    public boolean validateNPRSULC(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
        if (record instanceof HomeHealthRecord_C_IF) {
            HomeHealthRecord_C_IF recordC = (HomeHealthRecord_C_IF) record;
            int intStage1;
            int intStage2;
            int intStage3;
            int intStage4;
            int intNonStageCovering;
            int intNonStageDeepTissue;
            String tmpStr;

            if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
                // convert the string values to numbers
                // stage 1 must be 0 thru 4
                tmpStr = recordC.getNBR_PRSULC_STG1();
                if (!ValidateUtils.isEmpty(tmpStr)) {
                    intStage1 = IntegerUtils.parseInt(tmpStr, -1);
                    if (intStage1 < 0 || intStage1 > 4) {
                        // value is not in range
                        this.NPRSULC1_INVLD = true;
                        ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "NBR_PRSULC_STG1", "Value not in range: " + tmpStr);
                    }
                } else {
                    this.NPRSULC1_INVLD = true;
                }

                tmpStr = recordC.getNBR_PRSULC_STG2();
                if (!ValidateUtils.isEmpty(tmpStr)) {
                    intStage2 = IntegerUtils.parseInt(tmpStr, -2);
                    if (intStage2 == -2) {
                        this.NPRSULC2_INVLD = true;
                    }
                } else {
                    intStage2 = -1;
                }

                tmpStr = recordC.getNBR_PRSULC_STG3();
                if (!ValidateUtils.isEmpty(tmpStr)) {
                    intStage3 = IntegerUtils.parseInt(tmpStr, -2);
                    if (intStage3 == -2) {
                        this.NPRSULC3_INVLD = true;
                    }
                } else {
                    intStage3 = -1;
                }

                tmpStr = recordC.getNBR_PRSULC_STG4();
                if (!ValidateUtils.isEmpty(tmpStr)) {
                    intStage4 = IntegerUtils.parseInt(tmpStr, -2);
                    if (intStage4 == -2) {
                        this.NPRSULC4_INVLD = true;
                    }
                } else {
                    intStage4 = -1;
                }

                tmpStr = recordC.getNSTG_CVRG();
                if (!ValidateUtils.isEmpty(tmpStr)) {
                    intNonStageCovering = IntegerUtils.parseInt(tmpStr, -2);
                    if (intNonStageCovering == -2) {
                        this.UNOBS_PRSULC_INVLD = true;
                    }
                } else {
                    intNonStageCovering = -1;
                }

                tmpStr = recordC.getNSTG_DEEP_TISUE();
                if (!ValidateUtils.isEmpty(tmpStr)) {
                    intNonStageDeepTissue = IntegerUtils.parseInt(tmpStr, -2);
                    if (intNonStageDeepTissue == -2) {
                        this.UNOBS_PRSULC_INVLD = true;
                    }
                } else {
                    intNonStageDeepTissue = -1;
                }

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
                tmpStr = recordC.getUNHLD_STG2_PRSR_ULCR();

                if ("0".equals(tmpStr)) {
                    // number pressure ulcers stage 2 should not be filled in
                    // and the SOC/ROC values should be blank only for RFA 4 & 5
                    if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                            && (intStage2 >= 0 || !ValidateUtils.isEmpty(recordC.getNBR_STG2_AT_SOC_ROC()))) {
                        this.NPRSULC2_INVLD = true;
                    }
                    // number pressure ulcers stage 3 should not be filled in
                    // and the SOC/ROC values should be blank only for RFA 4 & 5
                    if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                            && (intStage3 >= 0 || !ValidateUtils.isEmpty(recordC.getNBR_STG3_AT_SOC_ROC()))) {
                        this.NPRSULC3_INVLD = true;
                    }
                    // number pressure ulcers stage 4 should not be filled in
                    // and the SOC/ROC values should be blank only for RFA 4 & 5
                    if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                            && (intStage4 >= 0 || !ValidateUtils.isEmpty(recordC.getNBR_STG4_AT_SOC_ROC()))) {
                        this.NPRSULC4_INVLD = true;
                    }
                    // validate that the rest of the information about these uclers
                    // is also blank for RFA 1, 3, 4, 5
                    if (!ValidateUtils.isEmpty(recordC.getNSTG_DEEP_TISUE())) {
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }

                    // validate that the rest of the information about these uclers
                    // is also blank for RFA 1, 3, 4, 5
                    if (!ValidateUtils.isEmpty(recordC.getNSTG_DRSG())
                            || intNonStageCovering >= 0) {
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                        this.UNOBS_PRSULC_INVLD = true;
                    }

                    // validate that the rest of the information about these uclers
                    // is also blank for RFA 1, 3 only
                    if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)
                            && (!ValidateUtils.isEmpty(recordC.getPRSR_ULCR_LNGTH())
                            || !ValidateUtils.isEmpty(recordC.getPRSR_ULCR_WDTH())
                            || !ValidateUtils.isEmpty(recordC.getPRSR_ULCR_DEPTH())
                            || !ValidateUtils.isEmpty(recordC.getSTUS_PRBLM_PRSR_ULCR()))) {
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }

                    // validate that the rest of the information about these uclers
                    // is also blank for RFA 4, 5 only
                    if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                            && (!ValidateUtils.isEmpty(recordC.getNSTG_DRSG_SOC_ROC())
                            || !ValidateUtils.isEmpty(recordC.getNSTG_CVRG_SOC_ROC())
                            || !ValidateUtils.isEmpty(recordC.getNSTG_DEEP_TISSUE_SOC_ROC()))) {
                        this.NPRSULC2_INVLD = true;
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }

                } else if ("1".equals(tmpStr)) {

                    boolean atLeastOne = false;
                    // number pressure ulcers stage 2 should be greater than
                    // or equal to at start of care/resumption of care
                    if (intStage2 > 0) {
                        atLeastOne = true;

                        // The Number Stage 2 at Start of Care or Resume
                        // of Care, must be less then or equal to the Number
                        // of Stage 2 Ulcers - a blank would be considered
                        // less then
                        // only during RFA 4, 5
                        if (!ValidateUtils.SPACE_2.equals(recordC.getNBR_STG2_AT_SOC_ROC())
                                && ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                                && IntegerUtils.parseInt(recordC.getNBR_STG2_AT_SOC_ROC(), 9999) > intStage2) {
                            this.NPRSULC2_INVLD = true;
                            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "NBR_PRSULC_STG2_AT_SOC_ROC", "Value out of range: " + recordC.getNBR_STG2_AT_SOC_ROC());
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
                        // only during RFA 4, 5
                        if (!ValidateUtils.SPACE_2.equals(recordC.getNBR_STG3_AT_SOC_ROC())
                                && ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                                && IntegerUtils.parseInt(recordC.getNBR_STG3_AT_SOC_ROC(), 9999) > intStage3) {
                            this.NPRSULC3_INVLD = true;
                            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "NBR_PRSULC_STG3_AT_SOC_ROC", "Value out of range: " + recordC.getNBR_STG3_AT_SOC_ROC());
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
                        // only during RFA 4, 5
                        if (!ValidateUtils.SPACE_2.equals(recordC.getNBR_STG4_AT_SOC_ROC())
                                && ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                                && IntegerUtils.parseInt(recordC.getNBR_STG4_AT_SOC_ROC(), 9999) > intStage4) {
                            this.NPRSULC4_INVLD = true;
                            ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "NBR_PRSULC_STG4_AT_SOC_ROC", "Value out of range: " + recordC.getNBR_STG4_AT_SOC_ROC());
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
                    try {
                        if ("00".compareTo(recordC.getNSTG_DRSG()) < 0
                                || "00".compareTo(recordC.getNSTG_CVRG()) < 0
                                || "00".compareTo(recordC.getNSTG_DEEP_TISUE()) < 0) {
                            atLeastOne = true;
                        } else if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                                && (ValidateUtils.DOUBLE_ZERO.compareTo(recordC.getNSTG_DRSG_SOC_ROC()) < 0
                                || ValidateUtils.DOUBLE_ZERO.compareTo(recordC.getNSTG_CVRG_SOC_ROC()) < 0
                                || ValidateUtils.DOUBLE_ZERO.compareTo(recordC.getNSTG_DEEP_TISSUE_SOC_ROC())
                                < 0)) {
                            atLeastOne = true;
                        }
                    } catch (NullPointerException e) {
                        Logger.getLogger(getClass().getName()).log(Level.INFO, " What?");
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

                // check the ulcer sizes for RFA 1 and 3
                if (ValidateUtils.isValidValue(recordC.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)) {
                    // If M1308_NBR_PRSULC_STG3 is greater than zero (0) or
                    // M1308_NBR_PRSULC_STG4 is greater than zero (0) or
                    // M1308_NSTG_CVRG is greater than zero (0), then
                    // M1310_PRSR_ULCR_LNGTH through M1314_PRSR_ULCR_DPTH cannot be
                    //blank
                    if ((intStage3 > 0 || intStage4 > 0 || intNonStageCovering > 0)
                            && (blank4.equals(recordC.getPRSR_ULCR_LNGTH())
                            || blank4.equals(recordC.getPRSR_ULCR_WDTH())
                            || blank4.equals(recordC.getPRSR_ULCR_DEPTH()))) {
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }

                    // If M1308_NBR_PRSULC_STG3 is equal to zero (0) and
                    // M1308_NBR_PRSULC_STG4 is equal to zero (0), and
                    // M1308_NSTG_CVRG is equal to zero (0), then
                    // M1310_PRSR_ULCR_LNGTH through M1314_PRSR_ULCR_DPTH must be
                    // blank.
                    if (intStage3 == 0 && intStage4 == 0 && intNonStageCovering == 0
                            && (!blank4.equals(recordC.getPRSR_ULCR_LNGTH())
                            || !blank4.equals(recordC.getPRSR_ULCR_WDTH())
                            || !blank4.equals(recordC.getPRSR_ULCR_DEPTH()))) {
                        this.NPRSULC3_INVLD = true;
                        this.NPRSULC4_INVLD = true;
                    }
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
    public boolean validateSTGPRSUL(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
        int tmpInt;
        String tmpStr;
        final String problemUlcer;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            problemUlcer = record.getSTG_PRBLM_ULCER();

            // check the value of the Stage of Most Problematic Pressure Ulcer
            if ("01".equals(problemUlcer)) {
                // number of pressure Ulcer stage 1 must be > 0
                tmpStr = record.getNBR_PRSULC_STG1();
                if (!ValidateUtils.isEmpty(tmpStr)) {
                    try {
                        tmpInt = IntegerUtils.parseInt(tmpStr, 10, Integer.MAX_VALUE, false);
                        if (tmpInt <= 0) {
                            this.STGPRSUL_INVLD = true;
                        }
                    } catch (NumberFormatException e) {
                        ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "NBR_PRSULC_STG1", e.toString());
                        this.STGPRSUL_INVLD = true;
                    }
                } else {
                    this.STGPRSUL_INVLD = true;
                }

            } else if ("02".equals(problemUlcer)) {
                // number of pressure Ulcer stage 2 must be > 0
                tmpStr = record.getNBR_PRSULC_STG2();
                if (!ValidateUtils.isEmpty(tmpStr)) {
                    try {
                        tmpInt = IntegerUtils.parseInt(tmpStr, 10, Integer.MAX_VALUE, false);
                        if (tmpInt <= 0) {
                            this.STGPRSUL_INVLD = true;
                        }
                    } catch (NumberFormatException e) {
                        ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "NBR_PRSULC_STG2", e.toString());
                        this.STGPRSUL_INVLD = true;
                    }
                } else {
                    this.STGPRSUL_INVLD = true;
                }
            } else if ("03".equals(problemUlcer)) {
                // number of pressure Ulcer stage 3 must be > 0
                tmpStr = record.getNBR_PRSULC_STG3();
                if (!ValidateUtils.isEmpty(tmpStr)) {
                    try {
                        tmpInt = IntegerUtils.parseInt(tmpStr, 10, Integer.MAX_VALUE, false);
                        if (tmpInt <= 0) {
                            this.STGPRSUL_INVLD = true;
                        }
                    } catch (NumberFormatException e) {
                        ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "NBR_PRSULC_STG3", e.toString());
                        this.STGPRSUL_INVLD = true;
                    }
                } else {
                    this.STGPRSUL_INVLD = true;
                }
            } else if ("04".equals(problemUlcer)) {
                // number of pressure Ulcer stage 4 must be > 0
                tmpStr = record.getNBR_PRSULC_STG4();
                if (!ValidateUtils.isEmpty(tmpStr)) {
                    try {
                        tmpInt = IntegerUtils.parseInt(tmpStr,10, Integer.MAX_VALUE, false);
                        if (tmpInt <= 0) {
                            this.STGPRSUL_INVLD = true;
                        }
                    } catch (NumberFormatException e) {
                        ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "NBR_PRSULC_STG4", e.toString());
                        this.STGPRSUL_INVLD = true;
                    }
                } else {
                    this.STGPRSUL_INVLD = true;
                }
            } else if (ValidateUtils.DOUBLE_ZERO.equals(record.getNBR_PRSULC_STG1())
                    && ValidateUtils.DOUBLE_ZERO.equals(record.getNBR_PRSULC_STG2())
                    && ValidateUtils.DOUBLE_ZERO.equals(record.getNBR_PRSULC_STG3())
                    && ValidateUtils.DOUBLE_ZERO.equals(record.getNBR_PRSULC_STG4())
                    && !ValidateUtils.NOT_APPLICABLE.equals(problemUlcer)) {
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
    public boolean validateNBR_STASULC(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            final String str = record.getSTAS_ULCR_PRSNT();
            final String numStasisUlcers = record.getNBR_STAS_ULCR();

            if (!ValidateUtils.isValidValue(numStasisUlcers, ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)
                    && !ValidateUtils.SPACE_2.equals(numStasisUlcers)) {
                this.NBR_STASULC_INVLD = true;
            } else {

                // 01 = patient has both observable and unobservable
                // 02 = patient has observable only
                if ("01".equals(str) || "02".equals(str)) {
                    // if number of stasis ulcers or status of problem stasis ulcer are blank, then flag the error
                    if (ValidateUtils.SPACE_2.equals(numStasisUlcers)
                            || ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_STAS_ULCR())) {
                        this.NBR_STASULC_INVLD = true;
                    }
                } else {
                    // 00 = patient does not have any stasis ulcers
                    // 03 = patient has only an unobservable stasis ulcer
                    // if the stasis ulcer is 00 or 03, then the number of stasis
                    // ulcers and status of problem stasis ulcer both should be blank
                    if ((ValidateUtils.DOUBLE_ZERO.equals(str) || "03".equals(str))
                            && (!ValidateUtils.SPACE_2.equals(numStasisUlcers)
                            || !ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_STAS_ULCR()))) {
                        this.NBR_STASULC_INVLD = true;
                    }
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
                } else {
                    // 00 = patient does not have any stasis ulcers
                    // 03 = patient has only an unobservable stasis ulcer
                    // the stasis ulcer is 00 or 03, then the number of stasis
                    // ulcers and status of problem stasis ulcer both should be blank
                    if ((ValidateUtils.DOUBLE_ZERO.equals(str) || "03".equals(str))
                            && (!ValidateUtils.SPACE_2.equals(record.getNBR_STAS_ULCR())
                            || !ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_STAS_ULCR()))) {
                        this.UNOBS_STASULC_INVLD = true;
                    }
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
    public boolean validateSTATSTASIS(HomeHealthRecordIF record) {

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {

            if (!ValidateUtils.isValidValue(record.getSTUS_PRBLM_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)
                    && !ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_STAS_ULCR())) {
                this.STATSTASIS_INVLD = true;
            } else {
                final String tmpStr = record.getSTAS_ULCR_PRSNT();
                // 01 = patient has both observable and unobservable
                // 02 = patient has observable only
                // if the stasis Ulcer indicator is observable
                if ("01".equals(tmpStr) || "02".equals(tmpStr)) {
                    // the status of the problem ulcer should be indicated
                    if (!ValidateUtils.isValidValue(record.getSTUS_PRBLM_STAS_ULCR(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)
                            || ValidateUtils.SPACE_2.equals(record.getNBR_STAS_ULCR())) {

                        this.STATSTASIS_INVLD = true;
                    }

                } else if ((ValidateUtils.DOUBLE_ZERO.equals(tmpStr) || "03".equals(tmpStr))
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
    public boolean validateSTATSURG(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            final String tmpStr = record.getSRGCL_WND_PRSNT();
            if ("01".equals(tmpStr)) {
                // if there is an observable surgical wound, then
                // the status can not be blank
                if (ValidateUtils.SPACE_2.equals(record.getSTUS_PRBLM_SRGCL_WND())) {
                    this.STATSURG_INVLD = true;
                }

            } else if ((ValidateUtils.DOUBLE_ZERO.equals(tmpStr) || "02".equals(tmpStr))
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
    public boolean validateWHEN_DYSPNEIC(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)
                && !ValidateUtils.isValidValue(record.getWHEN_DYSPNEIC(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
            this.DYSPNEIC_INVLD = true;
        }
        return !this.DYSPNEIC_INVLD;
    }

    /**
     * M1610_UR_INCONT Urinary Incontinence or Urinary Catheter
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    public boolean validateUR_INCONT(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            // validate the range first - if range is not valid, skip the rest
            if (!ValidateUtils.isValidValue(record.getUR_INCONT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO)) {
                this.UR_INCONT_INVLD = true;
            }

            if (!this.UR_INCONT_INVLD
                    && ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)) {
                // Nov 19, 2009 update from Errata
                // Edits #1 and 2 are only valid for RFA's 01, 03, and 09 as
                // M1615_INCNTNT_TIMING is not active for RFA's 04 and 05.
                // *2. If M1610_UR_INCONT = 01, then M1615_INCNTNT_TIMING
                // cannot be blank.
                if ("01".equals(record.getUR_INCONT())) {
                    if (ValidateUtils.SPACE_2.equals(((HomeHealthRecord_C_IF) record).getINCNTNT_TIMING())) {
                        this.UR_INCONT_INVLD = true;
                    }
                } else {
                    // *1. If M1610_UR_INCONT = 00 or 02, then M1615_INCNTNT_TIMING
                    // must be skipped (blank).
                    if (!ValidateUtils.SPACE_2.equals(((HomeHealthRecord_C_IF) record).getINCNTNT_TIMING())) {
                        this.UR_INCONT_INVLD = true;
                    }
                }
            }
        }
        return !this.UR_INCONT_INVLD;
    }

    /**
     * M1620_BWL_INCONT Bowel Incontinence Frequency
     *
     * Only checked for Assessment Reason 04, 05
     *
     * @param record
     * @return
     */
    public boolean validateBWL_INCONT(HomeHealthRecordIF record) {
        // for assessment reasons 4 and 5, bowel incontinence
        // can not be UK - i.e. is must be one of the other valid values
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_4_5_ONLY)
                && !ValidateUtils.isValidValue(record.getBWL_INCONT(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE, ValidateUtils.ARRAY_NOT_APPLICABLE)) {
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
    public boolean validateCUR_INJECT_MEDS(HomeHealthRecordIF record) {
        String tmpInjectMeds = record.getCRNT_MGMT_INJCTN_MDCTN();

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_1_3_ONLY)) {
            final String tmpRgmn = ((HomeHealthRecord_C_IF) record).getDRUG_RGMN_RVW();

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
    public boolean validateCUR_TOILETING(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)
                && !ValidateUtils.isValidValue(record.getCRNT_TOILTG(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR)) {
            this.CUR_TOILETING_INVLD = true;
        }

        return !this.CUR_TOILETING_INVLD;
    }

    /**
     * M1850_CUR_TRNSFRNG Current: Transferring
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    public boolean validateCUR_TRANSFERRING(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)
                && !ValidateUtils.isValidValue(record.getCRNT_TRNSFRNG(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE)) {
            this.CUR_TRANSFER_INVLD = true;
        }

        return !this.CUR_TRANSFER_INVLD;
    }

    /**
     * M1860_CRNT_AMBLTN Current: Ambulation
     *
     * Only checked for Assessment Reason 01, 03, 04, 05
     *
     * @param record
     * @return
     */
    public boolean validateCUR_AMBULATION(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)
                && !ValidateUtils.isValidValue(record.getCRNT_AMBLTN(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE_SIX)) {
            this.CUR_AMBULATION_INVLD = true;
        }
        return !this.CUR_AMBULATION_INVLD;
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
    public boolean validateTHER_NEED(HomeHealthRecordIF record) {
        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES)) {
            final String tmpStr = record.getTHER_NEED_NA();
            if ("1".equals(tmpStr)) {
                /*
                 * Therapy Need Number is an int, so an invalid or
                 * blank value would be -1, and entered number would
                 * be >= 0
                 */
                if (record.getTHER_NEED_NBR() >= 0) {
                    this.THER_NEED_INVLD = true;
                }

            } else if ("0".equals(tmpStr)
                    && (record.getTHER_NEED_NBR() < 0
                    || record.getTHER_NEED_NBR() > 999)) {
                // ensure the therapy count is in the valid range
                this.THER_NEED_INVLD = true;
            }
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
        DiagnosisCodeIF code = record.getPRIMARY_DIAG_ICD();

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

    @Override
    public String toString() {
        return "Validator: " + getClass().getSimpleName()
                + "{"
                + "ASSMT_REASON_INVLD=" + ASSMT_REASON_INVLD
                + ", INFO_COMPLETED_DT_INVLD=" + INFO_COMPLETED_DT_INVLD
                + ", THERAPIES_INVLD=" + THERAPIES_INVLD
                + ", INTERNAL_LOGIC_INVLD=" + INTERNAL_LOGIC_INVLD
                + ", VISION_INVLD=" + VISION_INVLD
                + ", PAIN_INVLD=" + PAIN_INVLD
                + ", LESION_INVLD=" + LESION_INVLD
                + ", NPRSULC1_INVLD=" + NPRSULC1_INVLD
                + ", NPRSULC2_INVLD=" + NPRSULC2_INVLD
                + ", NPRSULC3_INVLD=" + NPRSULC3_INVLD + 
                ", NPRSULC4_INVLD=" + NPRSULC4_INVLD + 
                ", UNOBS_PRSULC_INVLD=" + UNOBS_PRSULC_INVLD + 
                ", STGPRSUL_INVLD=" + STGPRSUL_INVLD + 
                ", NBR_STASULC_INVLD=" + NBR_STASULC_INVLD + 
                ", UNOBS_STASULC_INVLD=" + UNOBS_STASULC_INVLD + 
                ", STATSTASIS_INVLD=" + STATSTASIS_INVLD + 
                ", STATSURG_INVLD=" + STATSURG_INVLD + 
                ", DYSPNEIC_INVLD=" + DYSPNEIC_INVLD + 
                ", UR_INCONT_INVLD=" + UR_INCONT_INVLD + 
                ", BWLINCONT_INVLD=" + BWLINCONT_INVLD + 
                ", OSTOMY_INVLD=" + OSTOMY_INVLD + 
                ", CUR_DRESS_INVLD=" + CUR_DRESS_INVLD + 
                ", CUR_BATHING_INVLD=" + CUR_BATHING_INVLD + 
                ", CUR_TOILETING_INVLD=" + CUR_TOILETING_INVLD + 
                ", CUR_TRANSFER_INVLD=" + CUR_TRANSFER_INVLD + 
                ", CUR_AMBULATION_INVLD=" + CUR_AMBULATION_INVLD + 
                ", CUR_INJECT_MEDS_INVLD=" + CUR_INJECT_MEDS_INVLD + 
                ", THER_NEED_INVLD=" + THER_NEED_INVLD + 
                ", dataValidityFlag=" + dataValidityFlag + 
//                ", listeners=" + listeners + 
                ", grouper=" + grouper + 
                ", diagnosisScoringStatus=" + diagnosisScoringStatus + '}';
    }

    class MyOasisValidationEdit implements OasisValidationEditIF {

        private OasisEditIF oasisEdit;
        private OasisDataItemIF oasisDataItem;
        
        public MyOasisValidationEdit(String message, String dataKey, String dataValue) {
            this.oasisEdit = new MyOasisEditIF(message);
            this.oasisDataItem = new MyOasisDataItemIF(dataKey, dataValue);
        }

        public OasisEditIF getEdit() {
            return this.oasisEdit;
        }

        public OasisDataItemIF getOasisDataItem() {
            return this.oasisDataItem;
        }

        public void setEdit(OasisEditIF oeif) {

        }

        public void setOasisDataItem(OasisDataItemIF odiif) {

        }
    }

    class MyOasisEditIF implements OasisEditIF {

        final private String message;

        public MyOasisEditIF(String message) {
            this.message = message;
        }

        public long getId() {
            return 90000;
        }

        public String getDescription() {
            return "Historical Edit: " + message;
        }

        public OasisEditTypeEN getType() {
            return OasisEditTypeEN.INFORMATION;
        }

        public Level getServerityLevel() {
            return Level.WARNING;
        }

        public String getVersionActiveTo() {
            return "3210";
        }
    }

    class MyOasisDataItemIF implements OasisDataItemIF {

        final private String key;
        private String value;

        public MyOasisDataItemIF(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
        
        public String setValue(String value) {
            this.value = value;
            return value;
        }

    }
    
}
