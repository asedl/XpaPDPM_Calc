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
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C2_IF;
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.util.ValidateUtils;
import java.util.List;

/**
 * Validates Urinary Incontinence for edits: 3060, 4330, 4340
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateUrinaryIncontinence_OASIS_C2 extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_3060, OasisEditsEN.EDIT_4330, OasisEditsEN.EDIT_4340);
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        final String assessmentReason = record.getASSMT_REASON();

        if (ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_1_3_4_5_9_ONLY)) {
            final String urIncontTiming = ((HomeHealthRecord_C2_IF) record).getINCNTNT_TIMING();
            final String urIncont = record.getUR_INCONT();

            // validate the range first
            if (!ValidateUtils.isValidValue(urIncont, ValidateUtils.ARRAY_DOUBLE_0, ValidateUtils.ARRAY_DOUBLE_1, ValidateUtils.ARRAY_DOUBLE_2)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1610_UR_INCONT, urIncont)));
            }

            if (ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_1_3_9_ONLY)) {
                // validate the range first
                if (!ValidateUtils.isValidValue(urIncontTiming, 
                        ValidateUtils.ARRAY_DOUBLE_0,
                        ValidateUtils.ARRAY_DOUBLE_1,
                        ValidateUtils.ARRAY_DOUBLE_2,
                        ValidateUtils.ARRAY_DOUBLE_3,
                        ValidateUtils.ARRAY_DOUBLE_4,
                        ValidateUtils.ARRAY_CARET_VALUES)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1615_INCNTNT_TIMING, urIncontTiming)));
                }

                if (count == 0) {
                    if (ValidateUtils.isValidValue(urIncont, ValidateUtils.ARRAY_DOUBLE_0,  ValidateUtils.ARRAY_DOUBLE_2)
                            && !ValidateUtils.isValidValue(urIncontTiming, ValidateUtils.ARRAY_CARET_VALUES)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4330,
                                new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1615_INCNTNT_TIMING, urIncontTiming)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4330,
                                new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1610_UR_INCONT, urIncont)));

                    } else if (ValidateUtils.isValidValue(urIncont, ValidateUtils.ARRAY_DOUBLE_1)
                            && ValidateUtils.isValidValue(urIncontTiming, ValidateUtils.ARRAY_CARET_VALUES)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4340,
                                new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1615_INCNTNT_TIMING, urIncontTiming)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4340,
                                new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1610_UR_INCONT, urIncont)));

                    }
                }
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
        return "Validates Urinary Incontinence for edits: 3060, 4330, 4340";
    }

}
