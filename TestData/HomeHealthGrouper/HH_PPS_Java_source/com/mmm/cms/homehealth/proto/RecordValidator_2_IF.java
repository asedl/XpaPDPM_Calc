/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import java.util.List;

/**
 * Users a collection of record item validators in order to validate the Home
 * Health Record
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface RecordValidator_2_IF extends HomeHealthRecordValidatorIF{
    
    /**
     * adds a Item validator to the list of validators
     * @param itemValidator 
     */
    void add(RecordItemValidatorIF itemValidator);

    /**
     * returns a non-mutable list of the item validator
     * @return 
     */
    List<RecordItemValidatorIF> getItemValidators();

    /**
     * removes an item validator from this validation
     * @param itemValidator 
     */
    void remove(RecordItemValidatorIF itemValidator);
    
}
