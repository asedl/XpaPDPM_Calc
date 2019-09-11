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
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.util.ValidateUtils;
import java.util.List;

/**
 * Validates Current Ambulation for edits: 3060
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateCurrentAmbulation extends AbstractFunctionalItemValidator implements RecordItemValidatorIF {

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

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_9_ONLY)) {
            if (!ValidateUtils.isValidValue(record.getCRNT_AMBLTN(), 
                    ValidateUtils.ARRAY_DOUBLE_0, 
                    ValidateUtils.ARRAY_DOUBLE_1, 
                    ValidateUtils.ARRAY_DOUBLE_2, 
                    ValidateUtils.ARRAY_DOUBLE_3, 
                    ValidateUtils.ARRAY_DOUBLE_4, 
                    ValidateUtils.ARRAY_DOUBLE_5, 
                    ValidateUtils.ARRAY_DOUBLE_6)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1860_CRNT_AMBLTN, record.getCRNT_AMBLTN())));
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
        return "Validates Current Ambulation for edits: 3060";
    }

}
