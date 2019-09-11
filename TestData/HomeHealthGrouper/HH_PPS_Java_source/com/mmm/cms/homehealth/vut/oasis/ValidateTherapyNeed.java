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
 * Validates Therapy Need for edits: 3060, 4510, 4520
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateTherapyNeed extends AbstractServiceItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_3060, OasisEditsEN.EDIT_4510, OasisEditsEN.EDIT_4520);
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            final String tmpStr = record.getTHER_NEED_NA();
            final int needNumber = record.getTHER_NEED_NBR();

            if ("1".equals(tmpStr)) {
                /*
                 * M0826_THER_NEED_NBR is an int, so an invalid or
                 * blank value would be -1, and entered number would
                 * be >= 0
                 */
                if (needNumber >= 0) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4520,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M2200_THER_NEED_NBR, Integer.toString(record.getTHER_NEED_NBR()))));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4520,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M2200_THER_NEED_NA, tmpStr)));
                }
            } else if ("0".equals(tmpStr)) {
                if (needNumber < 0 || needNumber > 999) {
                    // ensure the therapy count is in the valid range
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4510,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M2200_THER_NEED_NBR, Integer.toString(record.getTHER_NEED_NBR()))));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4510,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M2200_THER_NEED_NA, tmpStr)));
                }
            } else {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M2200_THER_NEED_NA, tmpStr)));
            }

            /*
             * negative 1 is the value of empty, anything less is an error value
             */
            if (needNumber < -1) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M2200_THER_NEED_NBR, Integer.toString(needNumber))));
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
        return "Validates Therapy Need for edits: 3060, 4510, 4520";
    }

}
