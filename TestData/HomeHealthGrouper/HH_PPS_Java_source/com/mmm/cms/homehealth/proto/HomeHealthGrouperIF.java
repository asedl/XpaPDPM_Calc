/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.util.Describable;
import com.mmm.cms.util.Initializable;
import com.mmm.cms.util.Namable;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Describes the high level class that will score the Home Health Record.
 * It manages all its own information about valid Diagnosis, Groups,
 * Optional Payment Codes, etc.  It should organize the information, the Validation
 * and scoring models to perform the detailed scoring process. 
 *
 * NOTE: the HomeHealthEventNotifierIF is scheduled for removal in 2015
 * because it promotes thread unsafe design
 * @author 3M Health Information Systems  for CMS Home Health
 * 
 */
public interface HomeHealthGrouperIF extends Namable, Describable, Initializable, 
        Serializable, HomeHealthEventNotifierIF {

    String DEFAULT_BLANK_VALUE = "     ";

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
     */
    int[][] determineScoreOrder(HomeHealthRecordIF record);
    
    /**
     * gets the list of diagnostic Groups associated with the
     * Clinical/Functional diagnosis
     *
     * @return non-null List of Diagnostic Groups items.  If there was an error
     * during initialization, the list may be empty
     */
    List<DiagnosticGroupIF> getDiagnosticGroups();

    /**
     * gets the list of diagnostic Groups associated with the
     * Non-routine services (NRS) diagnosis
     *
     * @return non-null List of Diagnostic Groups items.  If there was an error
     * during initialization, the list may be empty
     */
    List<DiagnosticGroupIF> getDiagnosticGroupsNRS();

    /**
     * gets the routine services case mix adjustment table.  This table will
     * have 4 equations for each case mix.
     * @return non-null List of CaseMixAdjustment items.  If there was an error
     * during initialization, the list may be empty
     */
    List<CaseMixAdjustmentItemIF> getCaseMixAdjustments();

    /**
     * Provides the validator for the Clinical portion of the scoring
     * @return HomeHealthRecordValidatorIF
     */
    HomeHealthRecordValidatorIF getClinicalValidator();

    /**
     * Provides the list of Clinical/Functional codes associated with this
     * version and used for scoring.
     *
     * @return a non-null List of DiagnosisCodeIF
     */
    List<DiagnosisCodeIF> getClinicalCodes();

    /**
     * gets the effective start date
     * @return The date this version starts being valid
     */
    Calendar getEffectiveDateStart();

    /**
     * sets the effective start date
	 * @param calendar - non null Calendar
     */
    void setEffectiveDateStart(Calendar calendar);

	/**
     * gets the effective thru date
     * @return The last day this version is valid
     */
    Calendar getEffectiveDateThru();

	/**
     * sets the effective thru date
	 * @param calendar - non null Calendar
     */
    void setEffectiveDateThru(Calendar calendar);

    /**
     * Provides the list of Non-Routine codes associated with this version and used for
     * scoring.
     *
     * @return a non-null List of DiagnosisCodeIF
     */
    List<DiagnosisCodeIF> getNonRoutineCodes();

    /**
     * Provides the validator for the Non-Routine Supplies portion of the scoring
     * @return HomeHealthRecordValidatorIF
     */
    HomeHealthRecordValidatorIF getNRSValidator();

    /**
     * gets the Non-Routine services case mix adjustment table.  This table has
     * only one equation associated with it.
     * @return non-null List of CaseMixAdjustment items.  If there was an error
     * during initialization, the list may be empty
     */
    List<CaseMixAdjustmentItemIF> getNRSCaseMixAdjustments();

    /**
     * gets the version number id
     * @return The version number of the grouper
     */
    String getVersion();

    /**
     * Determines if the Diagnosis code is valid for this grouper
     * @param record
     * @param code
     * @return True if the code is valid for the grouper version and the
     * effective data of the code
     */
    boolean isValidDiagnosisCode(HomeHealthRecordIF record, DiagnosisCodeIF code);

    /**
     * This checks for proper dates, and assessment types to ensure that the
     * record is valid for the specific implementation of the Grouper
     * version.
     * 
     * @param record
     * @return true if the record is valid for this version of the grouper
     * @throws InvalidDateException
     */
    boolean isValidForVersion(HomeHealthRecordIF record) throws InvalidDateException;
    
    /**
     * This provides a way to determine if the codes on the record are valid for
     * the version and to validate the record.
     * scoring
     * @param record
     * @return the validator used to validate this record
     */
    HomeHealthRecordValidatorIF populateValidateClinicalCodes(HomeHealthRecordIF record);

    /**
     * Provides the overall grouping/scoring for the Oasis record
     *
     * @param record
     * @param validateDates - if true then is calls isValidForVersion() prior to
     * scoring.  If isValidForVersion() is called, and returns false, the record
     * will not be scored.
     * @return the results of the scoring - it will never be null
     * @deprecated - Scheduled for removal in 2015 - this method 
     */
    ScoringResultsIF score(HomeHealthRecordIF record, boolean validateDates);

    /**
     * Scores the record using the supplied validation information (if non-null)
     * instead of using the built in validation capabilities.
     * 
     * @param record
     * @param validateDates
     * @param validationEdits - if non-null, then this information is used 
     * during scoring as the validation information, otherwise, if null, the
     * built in validation is performed.
     * @param listener - can be null, otherwise list of listeners for notified of events
     * 
     * @return non-null scoring details which includes the traditional HIPPS,
     * Treatment Authorization, version name, and flags, plus the Validation
     * information (either the one supplied on the method call or the results
     * of the built in validation) and scoring process information 
     * (e.g. what item scored what value)
     */
    ScoringResultsIF score(HomeHealthRecordIF record, boolean validateDates,
            CollectionValidationEditsIF validationEdits, Collection<HomeHealthEventListenerIF> listeners);
    
    /**
     * Sets the version identifier for this grouper.  Traditionally, this
     * string should be exactly 5 characters long.
     *
     * @param version
     */
    void setVersion(String version);

	/**
	 * Each version has an acceptable record type that can be processed, and this
	 * returns that class allowing the isValidForVersion() to more appropriately
	 * check the validity of processing a specific record.
	 * 
	 * @return non-null Class
	 */
	Class getAcceptableRecordClass();

	/**
	 * Gets the effect start date window, which may be the same as the effective
	 * start date.
	 * @return non-null Calendar
	 */
	Calendar getEffectiveDateStartWindow();
	
	/**
	 * Sets the effective start date window.
	 * @param window - non-null Calendar
	 */
	void setEffectiveDateStartWindow(Calendar window);
        
        public DataManagerIF getGrouperDataManager();
}
