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
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C1_IF;
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import com.mmm.cms.homehealth.v3210.AbstractBaseValidator_v3210;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.util.ValidateUtils;
import static com.mmm.cms.util.ValidateUtils.ARRAY_ZERO_ONE;

import java.util.List;

/**
 * Validates Lesion Open Wound  for edits: 3060
 *
 * @author 3M Health Information Systems for CMS Home Health
 * @deprecated
 */
public class ValidateLesionOpenWound extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

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

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractBaseValidator_v3210.ASSESSMENT_1_3_ONLY)) {
            if (!ValidateUtils.isValidValue(record.getLESION_OPEN_WND(), ARRAY_ZERO_ONE)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                    new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1350_LESION_OPEN_WND, record.getLESION_OPEN_WND())));
            }
        }

        return count;
    }

    /**
     * Gets the description
     * 
     * @return non-null string
     */
    public String getDescription() {
        return "Validates Lesion Open Wound for edits: 3060";
    }

}
