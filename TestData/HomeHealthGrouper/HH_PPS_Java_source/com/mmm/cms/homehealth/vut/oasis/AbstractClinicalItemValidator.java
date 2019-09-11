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
public abstract class AbstractClinicalItemValidator extends AbstractItemValidator {

    private String otherPrefix = "M1022_OTH_DIAG";

    /**
     * creates with a default record class of HomeHealthRecordIF
     */
    public AbstractClinicalItemValidator() {
    }
    
    /**
     * creates with a supplied record class. There is no check for this
     * class being a HomeHealthRecordIF extension or not
     * 
     * @param clazz 
     */
    protected AbstractClinicalItemValidator(Class clazz) {
        super(clazz);
    }

    /**
     * creates with a certain prefix for the Other codes, the default is: "M1022_OTH_DIAG"
     * @param otherPrefix 
     */
    public AbstractClinicalItemValidator(String otherPrefix) {
        this.otherPrefix = otherPrefix;
    }
    

    public String getOtherPrefix() {
        return otherPrefix;
    }

    public void setOtherPrefix(String otherPrefix) {
        this.otherPrefix = otherPrefix;
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
     * issues, it sets the Clinical issue flag to true
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
            dataValidity.setClinicalIssue(true);
        }
        return count;
    }
    
}
