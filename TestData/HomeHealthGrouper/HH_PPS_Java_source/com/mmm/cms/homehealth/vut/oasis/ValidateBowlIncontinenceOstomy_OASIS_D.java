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
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_D_IF;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.util.ValidateUtils;
import static com.mmm.cms.util.ValidateUtils.ARRAY_UNKNOWN_UK;
import static com.mmm.cms.util.ValidateUtils.NOT_APPLICABLE;
import java.util.List;

/**
 * Validates Bowl Incontinence Ostomy for edits: 3060, 4370
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateBowlIncontinenceOstomy_OASIS_D extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    @Override
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(
                OasisEditsEN.EDIT_3060,
                OasisEditsEN.EDIT_4360,
                OasisEditsEN.EDIT_4370);
    }

    @Override
    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        final String assessmentReason = record.getASSMT_REASON();

        if (ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            final String bwlIncont = record.getBWL_INCONT();
            final String ostomy = record.getOSTOMY();

            if (ValidateUtils.isValidValue(ostomy, ValidateUtils.ARRAY_DOUBLE_0, 
                    ValidateUtils.ARRAY_DOUBLE_1, 
                    ValidateUtils.ARRAY_DOUBLE_2)) {
                if (NOT_APPLICABLE.equals(bwlIncont)) {
                    if (!ValidateUtils.isValidValue(ostomy, ValidateUtils.ARRAY_DOUBLE_1, ValidateUtils.ARRAY_DOUBLE_2)) {
                        // if bowl incontinence is NA, then the Ostomy
                        // must be 01 or 02
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4370,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1620_BWL_INCONT, bwlIncont)));
                    }
                } else if (ValidateUtils.isValidValue(bwlIncont, 
                        ValidateUtils.ARRAY_DOUBLE_0, ValidateUtils.ARRAY_DOUBLE_1, ValidateUtils.ARRAY_DOUBLE_2,
                        ValidateUtils.ARRAY_DOUBLE_3, ValidateUtils.ARRAY_DOUBLE_4, ValidateUtils.ARRAY_DOUBLE_5,
                        ARRAY_UNKNOWN_UK)
                        && !ValidateUtils.isValidValue(ostomy, ValidateUtils.ARRAY_DOUBLE_0)) {
                    // if the bowl incontinence is 00-05, or UK, then Ostomy
                    // must be 00
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4360,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1620_BWL_INCONT, bwlIncont)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4360,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1630_OSTOMY, ostomy)));
                }
            } else {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1630_OSTOMY, ostomy)));
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
        return "Validates Bowl Incontinence Ostomy for edits: 3060, 4370";
    }

}
