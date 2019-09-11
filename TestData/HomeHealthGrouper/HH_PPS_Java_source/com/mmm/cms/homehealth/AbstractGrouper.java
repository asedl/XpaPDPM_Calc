/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;
import com.mmm.cms.homehealth.proto.DataManagerIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.DiagnosisScoringStatus_EN;
import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.HomeHealthScoringModelIF;
import com.mmm.cms.util.Describable;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.Namable;
import com.mmm.cms.util.ScoringEventFormatter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Holds the basics of a Grouper without any interface applied so that the
 * extending class can apply the interface it requires
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public abstract class AbstractGrouper implements Namable, Describable {

    /**
     * the effective start date for this version
     */
    protected Calendar effectiveDateStart;
    /**
     * the overlap window date for this version
     */
    protected Calendar effectiveDateStartWindow;
    /**
     * the effective through date for this version
     */
    protected Calendar effectiveDateThru;
    /**
     * Reference to the Data manager
     */
    protected DataManagerIF dataManager;
    /**
     * The NRS Scoring module
     */
    protected HomeHealthScoringModelIF nrsScoringModel;
    /**
     * The Clinical Scoring module, equation 1
     */
    protected HomeHealthScoringModelIF clinicalModel_1;
    /**
     * The Clinical Scoring module, equation 2
     */
    protected HomeHealthScoringModelIF clinicalModel_2;
    /**
     * The Clinical Scoring module, equation 3
     */
    protected HomeHealthScoringModelIF clinicalModel_3;
    /**
     * The Clinical Scoring module, equation 4
     */
    protected HomeHealthScoringModelIF clinicalModel_4;
    /**
     * The version name - this can only be set during the constructor
     */
    protected String version;

    /**
     * Constructor that sets the effective start, thru, and window date to the
     * supplied dates. The start and thru dates can not be null but the window
     * can be.
     *
     *
     * @param start - the effective start date
     * @param thru - the effective thru date, inclusive
     * @param window - the start of the overlap window. May be null
     * @param versionName - 5 character string. Can not be null.
     */
    public AbstractGrouper(Calendar start, Calendar thru, Calendar window,
            String versionName) {

        if (start == null || thru == null) {
            throw new IllegalArgumentException("Neither the start date nor the thru date can be null");
        }

        if (versionName == null || versionName.length() != 5) {
            throw new IllegalArgumentException("versionName "
                    + (versionName == null ? " can not be null!" : " of '" + versionName + "' is invalid. Must be a 5 characte string."));
        }

        this.effectiveDateStart = start;
        this.effectiveDateThru = thru;
        this.effectiveDateStartWindow = window == null ? this.effectiveDateStart : window;
        this.version = versionName;
    }

    /**
     * This copies codes from one record to another without any cloning.
     *
     * @param recordDest
     */
    public void copyCodes(HomeHealthRecordIF recordSrc, HomeHealthRecordIF recordDest) {

        recordDest.setPRIMARY_DIAG_ICD(
                recordSrc.getPRIMARY_DIAG_ICD());
        recordDest.setOTH_DIAG1_ICD(
                recordSrc.getOTH_DIAG1_ICD());
        recordDest.setOTH_DIAG2_ICD(
                recordSrc.getOTH_DIAG2_ICD());
        recordDest.setOTH_DIAG3_ICD(
                recordSrc.getOTH_DIAG3_ICD());
        recordDest.setOTH_DIAG4_ICD(
                recordSrc.getOTH_DIAG4_ICD());
        recordDest.setOTH_DIAG5_ICD(
                recordSrc.getOTH_DIAG5_ICD());

        recordDest.setPMT_DIAG_ICD_A3(
                recordSrc.getPMT_DIAG_ICD_A3());
        recordDest.setPMT_DIAG_ICD_B3(
                recordSrc.getPMT_DIAG_ICD_B3());
        recordDest.setPMT_DIAG_ICD_C3(
                recordSrc.getPMT_DIAG_ICD_C3());
        recordDest.setPMT_DIAG_ICD_D3(
                recordSrc.getPMT_DIAG_ICD_D3());
        recordDest.setPMT_DIAG_ICD_E3(
                recordSrc.getPMT_DIAG_ICD_E3());
        recordDest.setPMT_DIAG_ICD_F3(
                recordSrc.getPMT_DIAG_ICD_F3());

        recordDest.setPMT_DIAG_ICD_A4(
                recordSrc.getPMT_DIAG_ICD_A4());
        recordDest.setPMT_DIAG_ICD_B4(
                recordSrc.getPMT_DIAG_ICD_B4());
        recordDest.setPMT_DIAG_ICD_C4(
                recordSrc.getPMT_DIAG_ICD_C4());
        recordDest.setPMT_DIAG_ICD_D4(
                recordSrc.getPMT_DIAG_ICD_D4());
        recordDest.setPMT_DIAG_ICD_E4(
                recordSrc.getPMT_DIAG_ICD_E4());
        recordDest.setPMT_DIAG_ICD_F4(
                recordSrc.getPMT_DIAG_ICD_F4());
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
     * @since V3210
     */
    public int[][] determineScoreOrder(HomeHealthRecordIF record) {
        DiagnosisCodeIF diagCode;
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
                    scoreOrder[diagIdx - 1][0] = 2;
                } else {
                    // this is a etiology so, score it on the first run
                    scoreOrder[diagIdx][0] = 1;
                }
            }
        }

        return scoreOrder;
    }

    /**
     * Each version has an acceptable record type that can be processed, and
     * this returns that class allowing the isValidForVersion() to more
     * appropriately check the validity of processing a specific record.
     *
     * @return non-null Class
     */
    public abstract Class getAcceptableRecordClass();

    /**
     * gets the Standard services case mix adjustment table
     *
     * @return non-null List of CaseMixAdjustment items. If there was an error
     * during initialization, the list may be empty
     */
    public List<CaseMixAdjustmentItemIF> getCaseMixAdjustments() {
        return this.dataManager.getCaseMixAdjustments();
    }

    /**
     * Provides the list of Clinical/Functional codes associated with this
     * version and used for scoring.
     *
     * @return a non-null List of DiagnosisCodeIF
     */
    public List<DiagnosisCodeIF> getClinicalCodes() {
        return this.dataManager.getClinicalCodes();
    }

    public HomeHealthScoringModelIF getClinicalModel_1() {
        return clinicalModel_1;
    }

    public HomeHealthScoringModelIF getClinicalModel_2() {
        return clinicalModel_2;
    }

    public HomeHealthScoringModelIF getClinicalModel_3() {
        return clinicalModel_3;
    }

    public HomeHealthScoringModelIF getClinicalModel_4() {
        return clinicalModel_4;
    }

    /**
     * gets the Non-Routine services case mix adjustment table
     *
     * @return non-null List of CaseMixAdjustment items. If there was an error
     * during initialization, the list may be empty
     */
    public List<CaseMixAdjustmentItemIF> getNRSCaseMixAdjustments() {
        return this.dataManager.getNRSCaseMixAdjustments();
    }

    /**
     * gets the description for this version
     *
     * @return the description of this version
     */
    @Override
    public String getDescription() {
        final StringBuilder buffer = new StringBuilder();
        final SimpleDateFormat dformatter = new SimpleDateFormat("MMM d, yyyy");

        buffer.append(getName());
        buffer.append(" - Effective dates: ");
        buffer.append(dformatter.format(getEffectiveDateStart().getTime()));
        buffer.append(" thru ");
        buffer.append(dformatter.format(getEffectiveDateThru().getTime()));
        if (getEffectiveDateStartWindow() != null
                && getEffectiveDateStartWindow() != getEffectiveDateStart()) {
            buffer.append(" (with effective start window of ");
            buffer.append(dformatter.format(getEffectiveDateStartWindow().getTime()));
            buffer.append(")");
        }

        return buffer.toString();
    }

    /**
     * gets the list of diagnostic Groups associated with the
     * Clinical/Functional diagnosis
     *
     * @return non-null List of Diagnostic Group items. If there was an error
     * during initialization, the list may be empty
     */
    public List<DiagnosticGroupIF> getDiagnosticGroups() {
        return this.dataManager.getDiagnosticGroups();
    }

    /**
     * gets the list of diagnostic Groups associated with the Non-Routine
     * Supplies (NRS) diagnosis
     *
     * @return non-null List of Diagnostic Group items. If there was an error
     * during initialization, the list may be empty
     */
    public List<DiagnosticGroupIF> getDiagnosticGroupsNRS() {
        return this.dataManager.getDiagnosticGroupsNRS();
    }

    public Calendar getEffectiveDateStart() {
        return effectiveDateStart;
    }

    public Calendar getEffectiveDateStartWindow() {
        if (effectiveDateStartWindow == null) {
            effectiveDateStartWindow = this.effectiveDateStart;
        }
        return effectiveDateStartWindow;
    }

    public Calendar getEffectiveDateThru() {
        return effectiveDateThru;
    }

    public DataManagerIF getGrouperDataManager() {
        return dataManager;
    }

    public HomeHealthScoringModelIF getNrsScoringModel() {
        return nrsScoringModel;
    }

    /**
     * gets a list of Diagnosis Codes for Non-routine Services
     *
     * @return
     */
    public List<DiagnosisCodeIF> getNonRoutineCodes() {
        return this.dataManager.getNonRoutineCodes();
    }

    public String getVersion() {
        return version;
    }

    /**
     * The record must be within the effect start and the effective thru dates,
     * and the assessment reason must be appropriate for the completed dates
     *
     *
     * @param record
     * @return true if this version can score the record
     */
    public boolean isValidForVersion(HomeHealthRecordIF record) {
        Calendar date;
        boolean valid = true;
        final Class acceptableRecordClass = getAcceptableRecordClass();

        /*
         * make sure there is a valid record and that this grouper can handle that
         * kind of record
         */
        if (record == null || !acceptableRecordClass.isAssignableFrom(record.getClass())) {
            valid = false;
        } else {

		// if m0826 is 0, score it - possibly
            // based on line 54 and 55 of psuedo code document
            date = record.getINFO_COMPLETED_DT();
            if (date != null) {
                final int tmpInt = IntegerUtils.parseInt(record.getASSMT_REASON(), 0);
                switch (tmpInt) {
                    case 1:
                    case 3:
                        if (date.before(getEffectiveDateStart())) {
                            valid = false;
                        }
                        break;

                    case 4:
                    case 5:
                        if (date.before(getEffectiveDateStartWindow())) {
                            valid = false;
                        }
                        break;

                    default:
                        // Groupers don't handle Assessment Reasons other than
                        // 1, 3, 4 and 5
                        valid = false;
                }

                if (valid) {
                    // ensure that the record it not after the effective thru date
                    valid ^= date.after(getEffectiveDateThru());
                }
            } else {
                valid = false;
            }
        }

        return valid;
    }

    /*
     * Determines if the Diagnosis code is valid for this version.
     *
     * @return true if the code is not null, and the code's value is found
     * within this version.
     */
    public boolean isValidDiagnosisCode(HomeHealthRecordIF record,
            DiagnosisCodeIF code) {
        return code == null ? false : dataManager.getDiagnosisCode(code.getCode()) != null;
    }

    public void setGrouperDataManager(GrouperDataManager grouperDataManager) {
        this.dataManager = grouperDataManager;
    }

    public void setNrsScoringModel(HomeHealthScoringModelIF nrsScoringModel) {
        this.nrsScoringModel = nrsScoringModel;
    }

    public void setClinicalModel_1(HomeHealthScoringModelIF clinicalModel_1) {
        this.clinicalModel_1 = clinicalModel_1;
    }

    public void setClinicalModel_2(HomeHealthScoringModelIF clinicalModel_2) {
        this.clinicalModel_2 = clinicalModel_2;
    }

    public void setClinicalModel_3(HomeHealthScoringModelIF clinicalModel_3) {
        this.clinicalModel_3 = clinicalModel_3;
    }

    public void setClinicalModel_4(HomeHealthScoringModelIF clinicalModel_4) {
        this.clinicalModel_4 = clinicalModel_4;
    }

    /**
     * This should only be done at construction time
     */
    @Override
    public final void setDescription(String desc) {
        //
    }

    /**
     * Get the name of this version
     *
     * @return the name of this version
     */
    @Override
    public String getName() {
        return "HHA PPS Grouper - "
                + getVersion();
    }

    /**
     * This should only be done at construction time
     */
    @Override
    public final void setName(String name) {
        // Can not set the name
    }

    /**
     * This should only be done at construction time
     */
    public final void setVersion(String versionName) {
        this.version = versionName;
    }

    public void setEffectiveDateStart(Calendar effectiveDateStart) {
        this.effectiveDateStart = effectiveDateStart;
    }

    public void setEffectiveDateStartWindow(Calendar effectiveDateStartWindow) {
        this.effectiveDateStartWindow = effectiveDateStartWindow;
    }

    public void setEffectiveDateThru(Calendar effectiveDateThru) {
        this.effectiveDateThru = effectiveDateThru;
    }

    /**
     * Reports the status of a Diagnosis code to the listeners
     *
     * @param recordOasisC
     * @param diagnosisStatus
     * @param scoringModel
     * @param listeners
     */
    public void reportNonScoringDiagnosis(HomeHealthRecordIF recordOasisC, 
            DiagnosisScoringStatus_EN diagnosisStatus[], 
            HomeHealthScoringModelIF scoringModel, 
            Collection<HomeHealthEventListenerIF> listeners) {
        DiagnosisCodeIF diagCode;

        for (int idx = 0; idx < diagnosisStatus.length; idx++) {
            diagCode = recordOasisC.getDiagnosisCode(idx);
            diagnosisStatus[idx] = diagCode.isValidCode()
                    ? diagCode.isValidForScoring() ? DiagnosisScoringStatus_EN.VALID_SCORABLE : DiagnosisScoringStatus_EN.VALID
                    : DiagnosisScoringStatus_EN.INVALID;
            /*
             * report all non-blank codes that are not scored 
             * for whatever reason
             */
            if (!diagCode.isEmpty() && !diagCode.isValidForScoring()) {
                ScoringEventFormatter.fireValidCodeWarning(listeners, (this instanceof HomeHealthGrouperIF ? (HomeHealthGrouperIF) this :  null), 
                        scoringModel, diagCode, idx + 1, diagCode.isValidCode());
            }
        }

    }

}
