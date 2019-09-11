/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.util.Describable;
import com.mmm.cms.util.Namable;
import java.util.List;

/**
 * Defines validator for a specific item for a specific criteria for a Home 
 * Health Record.  The validator must be thread safe in that it does not hold
 * any data from one validate request to the next that would conflict with the
 * validation of either record.
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface RecordItemValidatorIF extends Describable, Namable {
    
    /**
     * This returns the list of edits that this item validator is associated
     * with.
     * 
     * @return non-null list, and should have at least one OasisEditIF in it
     */
    List<OasisEditIF> getEditIdsUsed();
    
    /**
     * validates the record on a specific criteria, reporting the edits to the 
     * CollectionValidationEditsIF collection and optionally reporting to the 
     * DataValidityFlagIF
     * 
     * @param record
     * @param edits
     * @param dataValidity
     * @return the number of edits added to the list. Since the list may not
     * keep duplicate edit entries, the number may not reflect the number of edits
     * retained by the CollectionValidationEditsIF
     */
    int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits, DataValidityFlagIF dataValidity);
    
    /**
     * This gets the record type interface, as a Class, that this validator is associated
     * with. Not all validators will work for all record, so this helps to organize
     * the records.
     * 
     * @return non-null class
     */
    Class getRecordIFType();
}
