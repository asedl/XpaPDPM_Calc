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
import com.mmm.cms.util.Identifiable;
import com.mmm.cms.util.Namable;
import java.util.Collection;

/**
 * This represents a single model for scoring a HomeHealth record.
 * Implementations of this class should not have to worry about validating
 * the record, and only focus on scoring it.
 *
 * A Reference to the Grouper that created this model at run-time to ensure
 * any reporting through the Notify Events methods can use the Grouper instead
 * of implementing a internal version of the notification process.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface HomeHealthScoringModelIF extends Namable, Identifiable {

    /**
     * gets a reference to the Grouper which created this model
     * @return A reference to Grouper which created this model at runtime
     */
    HomeHealthGrouperIF getGrouper();

    /**
     * Sets the Grouper which created this model at run time.
     * @param grouper
     */
    void setGrouper(HomeHealthGrouperIF grouper);

    /**
     * This is the overall scoring routine.  All scoring should be summed here
     * in order to return a single value.
     *
     * @param record
     * @param validator
     * @return The scoring object which may hold scores per type and equation,
     * but is dependent on the scoring model
     * @deprecated scheduled for removal in 2015
     */
    ScoringPointsIF score(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator);

    /**
     * Used to perform any pre-scoring, such as any changes to the data due
     * to multi-code relationships; this is a convenience method that is the same
     * as calling preprocessRecord(record, validator, null)
     *
     * @param record
     * @param validator
     * @see HomeHealthScoringModelIF.preprocessRecord(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, Collection<HomeHealthEventListenerIF> listeners)
     */
    void preprocessRecord(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator);

    /**
     * Used to perform any pre-scoring, such as any changes to the data due
     * to multi-code relationships.
     *
     * @param record
     * @param validator
     * @param listeners - can be null, otherwise list of listeners for notified of events
     */
    void preprocessRecord(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator,
            Collection<HomeHealthEventListenerIF> listeners);

    
    /**
     * This populates the record with Diagnosis codes that are associated with
     * this grouper.  Codes that are part of the grouper are marked as Valid
     * and those that are not are marked as not Valid
     *
     * @param record
     */
    void populateCodes(HomeHealthRecordIF record);

    /**
     * This is the overall scoring routine.  All scoring should be summed here
     * in order to return a single value.
     *
     * @param record
     * @param validator
     * @param scoreOrder - array of int[6][3] determining the scoring order
     * of a diagnosis code in the related position.
     * 
     * @return The scoring object which may hold scores per type and equation,
     * but is dependent on the scoring model
     * @deprecated - use score(..., listeners) instead with the listeners = null
     */
    ScoringPointsIF score(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator,
            int [][] scoreOrder);

    /**
     * This is the overall scoring routine.  All scoring should be summed here
     * in order to return a single value.
     *
     * @param record
     * @param validator
     * @param scoreOrder - array of int[6][3] determining the scoring order
     * of a diagnosis code in the related position.
     * @param listeners - can be null, otherwise list of listeners for notified of events
     * 
     * @return The scoring object which may hold scores per type and equation,
     * but is dependent on the scoring model
     */
    ScoringPointsIF score(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator,
            int [][] scoreOrder, Collection<HomeHealthEventListenerIF> listeners);
    
    
}
