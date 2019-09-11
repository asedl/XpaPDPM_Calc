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
import static com.mmm.cms.util.ValidateUtils.ARRAY_NOT_APPLICABLE;
import static com.mmm.cms.util.ValidateUtils.ARRAY_UNKNOWN_UK;
import java.util.List;

/**
 * Validates Bowl Incontinence for  edits: 3060, 4350
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateBowlIncontinence extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_3060, OasisEditsEN.EDIT_4350);
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        final String assessmentReason = record.getASSMT_REASON();

        if (ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_1_3_4_5_9_ONLY)) {
            final String bowel = record.getBWL_INCONT();

            // if bowl incontenance is active, it must be one of the other valid values
            if (!ValidateUtils.isValidValue(bowel, 
                    ValidateUtils.ARRAY_DOUBLE_0, 
                    ValidateUtils.ARRAY_DOUBLE_1, 
                    ValidateUtils.ARRAY_DOUBLE_2, 
                    ValidateUtils.ARRAY_DOUBLE_3, 
                    ValidateUtils.ARRAY_DOUBLE_4, 
                    ValidateUtils.ARRAY_DOUBLE_5, 
                    ARRAY_NOT_APPLICABLE, ARRAY_UNKNOWN_UK)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1620_BWL_INCONT, bowel)));
            }

            // for assessment reasons 4, 5 or 9 bowel incontinence
            // can not be UK
            if (ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_4_5_9_ONLY)
                    && ValidateUtils.isValidValue(bowel, ARRAY_UNKNOWN_UK)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4350,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1620_BWL_INCONT, bowel)));
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
        return "Validates Bowl Incontinence for edits: 3060";
    }

}
