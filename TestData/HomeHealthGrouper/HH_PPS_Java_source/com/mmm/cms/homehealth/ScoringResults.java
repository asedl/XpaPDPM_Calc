/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;


import com.mmm.cms.homehealth.hipps.HIPPSCode;
import com.mmm.cms.homehealth.proto.DataValidityFlagIF;
import com.mmm.cms.homehealth.proto.DiagnosisScoringStatus_EN;
import com.mmm.cms.homehealth.proto.HIPPSCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.proto.TreatmentAuthorizationIF;
import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import java.io.Serializable;
import java.util.Collection;

/**
 * Scoring Results holds the collection of HIPPS code, grouper version Data
 * validity flag, OASIS Treatment Authorization, and the validator used to check
 * the record. The main use of this class is to provide a means of returning
 * detailed scoring information from the Grouper.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ScoringResults implements ScoringResultsIF, Serializable {

    private HIPPSCodeIF hippsCode;
    private String grouperVersion;
    private DataValidityFlagIF validityFlag;
    private TreatmentAuthorizationIF treatmentAuthorization;
    private HomeHealthRecordValidatorIF clinicalValidator;
    private HomeHealthRecordValidatorIF nrsValidator;
    private CollectionValidationEditsIF validationEdits;
    private Collection<HomeHealthEventIF> scoringEvents;
    private DiagnosisScoringStatus_EN diagnosisScoringStatus[];
    private DiagnosisScoringStatus_EN nrsDiagnosisScoringStatus[];
    private Exception exception;

    public ScoringResults() {
        this.hippsCode = new HIPPSCode();
        this.grouperVersion = "     ";
        this.validityFlag = new DataValidityFlag();
        this.treatmentAuthorization = new TreatmentAuthorization(null, null, null, null);
    }

    /**
     * Constructor to build an empty Scoring Results
     *
     * @param record
     */
    public ScoringResults(HomeHealthRecordIF record) {
        this.hippsCode = new HIPPSCode();
        this.grouperVersion = "     ";
        this.validityFlag = new DataValidityFlag();
        this.treatmentAuthorization = new TreatmentAuthorization(record, null, null, null);
    }

    /**
     * Constructor to set all the internal references
     *
     * @param hippsCode
     * @param grouperVersion
     * @param dataValidityFlag
     * @param treatmentAuthorization
     * @param clinValidator - the Clinical Validator
     * @param nValidator - the NRS Validator
     */
    public ScoringResults(HIPPSCodeIF hippsCode,
            String grouperVersion,
            DataValidityFlagIF dataValidityFlag,
            TreatmentAuthorizationIF treatmentAuthorization,
            HomeHealthRecordValidatorIF clinValidator,
            HomeHealthRecordValidatorIF nValidator) {

        this.hippsCode = hippsCode;
        this.grouperVersion = grouperVersion;
        if (dataValidityFlag != null) {
            this.validityFlag = dataValidityFlag;
        } else {
            this.validityFlag = new DataValidityFlag();
        }
        this.treatmentAuthorization = treatmentAuthorization;
        this.clinicalValidator = clinValidator;
        this.nrsValidator = nValidator;
    }
    
    public ScoringResults(CollectionValidationEditsIF validationEdits, 
            Collection<HomeHealthEventIF> events, 
            HIPPSCodeIF hippsCode, 
            String grouperVersion, 
            DataValidityFlagIF dataValidityFlag, 
            TreatmentAuthorizationIF treatmentAuthorization, 
            DiagnosisScoringStatus_EN diagnosisScoringStatus[],
            DiagnosisScoringStatus_EN nrsDiagnosisScoringStatus[]) {
        this(hippsCode, grouperVersion, dataValidityFlag, treatmentAuthorization, null, null);
        this.validationEdits = validationEdits;
        this.scoringEvents = events;
        this.diagnosisScoringStatus = diagnosisScoringStatus;
        this.nrsDiagnosisScoringStatus = nrsDiagnosisScoringStatus;
    }
    
    public ScoringResults(ScoringResultsIF scoringResults) {
        this(scoringResults.getHIPPSCode(), scoringResults.getGrouperVersion(),
                scoringResults.getValidityFlag(),
                scoringResults.getTreatmentAuthorization(), 
                scoringResults.getClinicalValidator(),
                scoringResults.getNrsValidator());
    }
    
    /**
     * Get the value of nrsValidator
     *
     * @return the value of nrsValidator
     */
    @Override
    public HomeHealthRecordValidatorIF getNrsValidator() {
        return nrsValidator;
    }

    /**
     * Set the value of nrsValidator
     *
     * @param nrsValidator new value of nrsValidator
     */
    @Override
    public void setNrsValidator(HomeHealthRecordValidatorIF nrsValidator) {
        this.nrsValidator = nrsValidator;
    }

    /**
     * gets the Data Validity flag
     *
     * @return a non-null DataValidityFlagIF
     */
    @Override
    public DataValidityFlagIF getValidityFlag() {
        return validityFlag;
    }

    /**
     * Sets the validity flag
     *
     * @param dataValidityFlag
     */
    public void setValidityFlag(DataValidityFlagIF dataValidityFlag) {
        this.validityFlag = dataValidityFlag;
    }

    /**
     * gets the Grouper version
     *
     * @return a non-null String. Should not be blank.
     */
    @Override
    public String getGrouperVersion() {
        return grouperVersion;
    }

    /**
     * Sets the reference grouper version
     *
     * @param grouperVersion
     */
    public void setGrouperVersion(String grouperVersion) {
        this.grouperVersion = grouperVersion;
    }

    /**
     * gets the HIPPS code
     *
     * @return a non-null HIPPSCodeIF object
     */
    @Override
    public HIPPSCodeIF getHIPPSCode() {
        return hippsCode;
    }

    /**
     * Sets the HIPPS code object
     *
     * @param hippsCode
     */
    public void setHIPPSCode(HIPPSCodeIF hippsCode) {
        this.hippsCode = hippsCode;
    }

    /**
     * getst the Treatment Authorization
     *
     * @return a non-null TreatmentAuthorizationIF
     */
    @Override
    public TreatmentAuthorizationIF getTreatmentAuthorization() {
        return treatmentAuthorization;
    }

    /**
     * Sets the treatment Authorization object
     *
     * @param treatmentAuthorization
     */
    public void setTreatmentAuthorization(TreatmentAuthorizationIF treatmentAuthorization) {
        this.treatmentAuthorization = treatmentAuthorization;
    }

    /**
     * gets the validator used
     *
     * @return The HomeHealthRecordValidatorIF used on the record during the
     * scoring, this may be null
     */
    @Override
    public HomeHealthRecordValidatorIF getClinicalValidator() {
        return clinicalValidator;
    }

    /**
     * Sets the HomeHealthRecordValidatorIF that was used on the record during
     * the scoring.
     *
     * @param validator
     */
    @Override
    public void setClinicalValidator(HomeHealthRecordValidatorIF validator) {
        this.clinicalValidator = validator;
    }
    
    @Override
    public DiagnosisScoringStatus_EN[] getDiagnosisScoringStatus() {
        return diagnosisScoringStatus;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public DiagnosisScoringStatus_EN[] getNrsDiagnosisScoringStatus() {
        return nrsDiagnosisScoringStatus;
    }

    @Override
    public Collection<HomeHealthEventIF> getScoringEvents() {
        return scoringEvents;
    }
    
    @Override
    public CollectionValidationEditsIF getValidationEdits() {
        return this.validationEdits;
    }

    @Override
    public void setDiagnosisScoringStatus(DiagnosisScoringStatus_EN[] diagnosisScoringStatus) {
        this.diagnosisScoringStatus = diagnosisScoringStatus;
    }

    @Override
    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void setNrsDiagnosisScoringStatus(DiagnosisScoringStatus_EN[] nrsDiagnosisScoringStatus) {
        this.nrsDiagnosisScoringStatus = nrsDiagnosisScoringStatus;
    }

    @Override
    public void setScoringEvents(Collection<HomeHealthEventIF> events) {
        this.scoringEvents = events;
    }
    
    @Override
    public void setValidationEdits(CollectionValidationEditsIF validationEdits) {
       this.validationEdits = validationEdits;
    }

	@Override
	public String toString() {
		return "ScoringResults{" + "hippsCode=" + hippsCode + ", grouperVersion=" + grouperVersion + ", validityFlag=" + validityFlag + ", treatmentAuthorization=" + treatmentAuthorization + ", clinicalValidator=" + clinicalValidator + ", nrsValidator=" + nrsValidator + ", validationEdits=" + validationEdits + ", scoringEvents=" + scoringEvents + ", diagnosisScoringStatus=" + diagnosisScoringStatus + ", nrsDiagnosisScoringStatus=" + nrsDiagnosisScoringStatus + ", exception=" + exception + '}';
	}
    
    
}
