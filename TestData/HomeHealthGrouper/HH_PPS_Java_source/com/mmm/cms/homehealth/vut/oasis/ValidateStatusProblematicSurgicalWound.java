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
 * Validates Status Problematic Surgical Wound for edits: 3060, 4240, 4250
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateStatusProblematicSurgicalWound extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_3060, OasisEditsEN.EDIT_4240, OasisEditsEN.EDIT_4250);
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_9_ONLY)) {
            final String surgPresent = record.getSRGCL_WND_PRSNT();
            final String statSurg = record.getSTUS_PRBLM_SRGCL_WND();

            if (!ValidateUtils.isValidValue(statSurg, 
                    ValidateUtils.ARRAY_DOUBLE_0, 
                    ValidateUtils.ARRAY_DOUBLE_1, 
                    ValidateUtils.ARRAY_DOUBLE_2, 
                    ValidateUtils.ARRAY_DOUBLE_3,
                    ValidateUtils.ARRAY_CARET_VALUES)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1342_STUS_PRBLM_SRGCL_WND, statSurg)));
            }

            if (ValidateUtils.isValidValue(surgPresent, ValidateUtils.ARRAY_DOUBLE_1)) {
                // if there is an observable surgical wound, then
                // the status can not be blank/^
                if (ValidateUtils.isValidValue(statSurg, ValidateUtils.ARRAY_CARET_VALUES)) {
                count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4250,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1340_SRGCL_WND_PRSNT, surgPresent)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4250,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1342_STUS_PRBLM_SRGCL_WND, surgPresent)));
                }

            } else if (ValidateUtils.isValidValue(surgPresent, ValidateUtils.ARRAY_DOUBLE_0, ValidateUtils.ARRAY_DOUBLE_2)) {
                if (!ValidateUtils.isValidValue(statSurg, ValidateUtils.ARRAY_CARET_VALUES)) {
                    // if there is no or unobservable surgical wound,
                    // then the status must be blank
                count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4240,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1340_SRGCL_WND_PRSNT, surgPresent)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4240,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1342_STUS_PRBLM_SRGCL_WND, surgPresent)));
                }
            } else {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1340_SRGCL_WND_PRSNT, surgPresent)));
            }
        }

        return count;
    }

    /**
     * Gets the description
     * 
     * @return non-null string
     */
    @Override
    public String getDescription() {
        return "Validates Status Problematic Surgical Wound for edits: 3060, 4240, 4250";
    }

}
