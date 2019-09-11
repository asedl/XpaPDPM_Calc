/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut.pps;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.proto.DataValidityFlagIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;

/**
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public abstract class AbstractPPSManifestationItemValidator extends AbstractPPSClinicalItemValidator {

    /**
     * creates with a default record class of HomeHealthRecordIF
     */
    public AbstractPPSManifestationItemValidator() {
    }

    /**
     * used to set the Payment diagnosis field name prefix and the 
     * other diagnosis field name prefix.
     * 
     * @param primaryPrefix
     * @param otherPrefix
     * @param optPrefix 
     */
    public AbstractPPSManifestationItemValidator(String primaryPrefix, String otherPrefix, 
            String optPrefix) {
        super(primaryPrefix, otherPrefix, optPrefix);
    }

    /**
     * creates with a supplied record class. There is no check for this class
     * being a HomeHealthRecordIF extension or not
     *
     * @param clazz
     */
    protected AbstractPPSManifestationItemValidator(Class clazz) {
        super(clazz);
    }

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
    @Override
    public int validate(HomeHealthGrouperIF grouper, HomeHealthRecordIF record, CollectionValidationEditsIF edits, DataValidityFlagIF dataValidity) {
        final int count;

        count = validate(grouper, record, edits);
        if (count > 0) {
            dataValidity.setManifestationSequenceIssue(true);
        }
        return count;
    }

}
