/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import java.util.Collection;

/**
 * Holds the HIPPS, OASIS Treatment Authorization, Version and Flag
 * information accumulated during the Grouper scoring process.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface ScoringResultsIF {

    String DEFAULT_BLANK_VALUE = HIPPSCodeIF.DEFAULT_BLANK_VALUE +
                                                    TreatmentAuthorizationIF.DEFAULT_BLANK_VALUE +
                                                    HomeHealthGrouperIF.DEFAULT_BLANK_VALUE +
                                                    " ";

    /**
     * gets the Hipps code
     * @return a non-null HIPPSCodeIF object
     */
    HIPPSCodeIF getHIPPSCode();

    /**
     * gets the data validity flag
     * @return a non-null DataValidityFlagIF
     */
    DataValidityFlagIF getValidityFlag();

    /**
     * gets the treatment authorization code
     * @return a non-null TreatmentAuthorizationIF
     */
    TreatmentAuthorizationIF getTreatmentAuthorization();

    /**
     * gets the version of the Grouper that performed the scoring
     * @return a non-null String.  Should not be blank.
     */
    String getGrouperVersion();

    /**
     * gets the validator that was used to validate the record
     * @return The HomeHealthRecordValidatorIF used on the record during
     * the scoring of Clinical/Functional issues.
     */
    HomeHealthRecordValidatorIF getClinicalValidator();

    /**
     * Sets the HomeHealthRecordValidatorIF that was used on the record
     * during the scoring of Clinical/Functional issues.
     * 
     * @param validator
     */
    void setClinicalValidator(HomeHealthRecordValidatorIF validator);

    /**
     * gets the validator that was used to validate the record
     * @return The HomeHealthRecordValidatorIF used on the record during
     * the scoring of NRS.
     */
    HomeHealthRecordValidatorIF getNrsValidator();

    /**
     * gets the status of a code as it pertains to scoring.
     *
     * @return
     */
    DiagnosisScoringStatus_EN[] getDiagnosisScoringStatus();

    /**
     * gets any exception that happened during to scoring that possibly stopped
     * the scoring from working properly or at all.  If there is no exception
     * during scoring, then this value will be null.
     * 
     * @return 
     */
    Exception getException();
    
    /**
     * gets the status of a code as it pertains to scoring.
     *
     * @return
     */
    DiagnosisScoringStatus_EN[] getNrsDiagnosisScoringStatus();

    /**
     * gets the List of Home Health events; may be null
     * @return non-empty List or null
     */
    Collection<HomeHealthEventIF> getScoringEvents();
    
    /**
     * gets the collection of validation edits that should be applied to a
     * scored record in order for the record to score properly - non-HH PPS 
     * related edits, such as payor code, may not be part of this collection, 
     * but may be.
     * 
     * @return null if validation was not applied to a scored record, and a 
     * non-collection if the validation was applied, however, the collection
     * may be empty if there were no edits.
     */
    CollectionValidationEditsIF getValidationEdits();

    /**
     * sets the status of a code as it pertains to scoring.
     *
     * @return
     */
    void setDiagnosisScoringStatus(DiagnosisScoringStatus_EN diagnosisScoringStatus[]);

    /**
     * sets any exception that happened during to scoring that possibly stopped
     * the scoring from working properly or at all.
     * 
     * @param e 
     */
    void setException(Exception e);
    
    /**
     * sets the status of a code as it pertains to scoring.
     *
     * @return
     */
    void setNrsDiagnosisScoringStatus(DiagnosisScoringStatus_EN diagnosisScoringStatus[]);
    
    /**
     * Sets the HomeHealthRecordValidatorIF that was used on the record
     * during the scoring of NRS.
     *
     * @param validator
     */
    void setNrsValidator(HomeHealthRecordValidatorIF validator);

    /**
     * sets the List of Home Health scoring events
     * @param events - may be null
     */
    void setScoringEvents(Collection<HomeHealthEventIF> events);
    
    /**
     * Sets the validation edits
     * 
     * @param validationEdits 
     */
    void setValidationEdits(CollectionValidationEditsIF validationEdits);

}
