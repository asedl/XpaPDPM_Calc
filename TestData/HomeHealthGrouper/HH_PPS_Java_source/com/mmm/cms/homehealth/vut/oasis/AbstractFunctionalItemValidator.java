/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut.oasis;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.proto.DataValidityFlagIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;

/**
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public abstract class AbstractFunctionalItemValidator extends AbstractItemValidator {

    /**
     * creates with a default record class of HomeHealthRecordIF
     */
    public AbstractFunctionalItemValidator() {
    }
    
    /**
     * creates with a supplied record class. There is no check for this
     * class being a HomeHealthRecordIF extension or not
     * 
     * @param clazz 
     */
    protected AbstractFunctionalItemValidator(Class clazz) {
        super(clazz);
    }

    /**
     * The detailed validator that has no idea about the data validity flag
     * 
     * @param record
     * @param edits
     * @return >= 0 as the number of edits added to the edit list.
     */
    protected abstract int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits);
    
    /**
     * This validates to a lower level method and if there are any invalid
     * issues, it sets the Functional issue flag to true
     * 
     * @param record
     * @param edits
     * @param dataValidity
     * @return 
     */
    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits, DataValidityFlagIF dataValidity) {
        final int count;
        
        count = validate(record, edits);
        if (count > 0) {
            dataValidity.setFunctionalIssue(true);
        }
        return count;
    }
    
}
