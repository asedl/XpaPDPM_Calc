/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut.pps;

import com.mmm.cms.homehealth.vut.oasis.AbstractItemValidator;
import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.proto.DataValidityFlagIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;

/**
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public abstract class AbstractPPSClinicalItemValidator extends AbstractItemValidator {

    private String primaryPrefix = "M1020_PRIMARY_DIAG_ICD";
    private String otherPrefix = "M1022_OTH_DIAG";
    private String paymentPrefix = "M1024_PMT_DIAG_ICD_";

    /**
     * creates with a default record class of HomeHealthRecordIF
     */
    public AbstractPPSClinicalItemValidator() {
    }

    /**
     * used to set the Payment diagnosis field name prefix and the 
     * other diagnosis field name prefix.
     * 
     * @param primaryPrefix
     * @param otherPrefix
     * @param optPrefix 
     */
    public AbstractPPSClinicalItemValidator(String primaryPrefix, String otherPrefix, 
            String optPrefix) {
        this.primaryPrefix = primaryPrefix;
        this.otherPrefix = otherPrefix;
        this.paymentPrefix = optPrefix;
    }

    public String getPrimaryPrefix() {
        return primaryPrefix;
    }
  
    protected String getPaymentPrefix() {
        return paymentPrefix;
    }

    protected String getOtherPrefix() {
        return otherPrefix;
    }

    /**
     * creates with a supplied record class. There is no check for this class
     * being a HomeHealthRecordIF extension or not
     *
     * @param clazz
     */
    protected AbstractPPSClinicalItemValidator(Class clazz) {
        super(clazz);
    }

    /**
     * The detailed validator that has no idea about the data validity flag
     *
     * @param grouper
     * @param record
     * @param edits
     * @return >= 0 as the number of edits added to the edit list.
     */
    protected abstract int validate(HomeHealthGrouperIF grouper, HomeHealthRecordIF record, CollectionValidationEditsIF edits);

    /**
     * This validates to a lower level method and if there are any invalid
     * issues, it sets the Clinical issue flag to true; if there are any 
     * Manifestation issues, it sets the Manifestation issue flag to true.
     *
     * @param grouper - the version associated with the record
     * @param record
     * @param edits
     * @param dataValidity
     * @return number of edits items added to the list
     */
    public int validate(HomeHealthGrouperIF grouper, HomeHealthRecordIF record, CollectionValidationEditsIF edits, DataValidityFlagIF dataValidity) {
        final int count;

        count = validate(grouper, record, edits);
        if (count > 0) {
            dataValidity.setClinicalIssue(true);
        }
        return count;
    }

}
