/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut.oasis;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditsEN;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import static com.mmm.cms.homehealth.v3210.AbstractBaseValidator_v3210.ASSESSMENT_REASON_VALUES_ALL;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.util.ValidateUtils;

import java.util.List;

/**
 * Validates Assessment Reason for edits: 3060
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateAssessmentReason extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_3060);
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;

        if (!ValidateUtils.isValidValue(record.getASSMT_REASON(), ASSESSMENT_REASON_VALUES_ALL)) {
            count++;
            edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060, new HHOasisDataItem("M0100_ASSMT_REASON", record.getASSMT_REASON())));
        }

        return count;
    }

    /**
     * Gets the description
     *
     * @return non-null string
     */
    public String getDescription() {
        return "Validates Assessment Reason for edits: 3060";
    }

}
