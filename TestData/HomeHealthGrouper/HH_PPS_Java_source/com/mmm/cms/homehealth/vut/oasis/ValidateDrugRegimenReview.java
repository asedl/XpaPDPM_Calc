/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut.oasis;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditsEN;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C2_IF;
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.util.ValidateUtils;

import java.util.List;

/**
 * Validates Episode Timing for edits: 3060
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateDrugRegimenReview extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_3060, OasisEditsEN.EDIT_4431);
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;

        final String assessmentReason = record.getASSMT_REASON();
        final String drugRegemine = ((HomeHealthRecord_C2_IF) record).getDRUG_RGMN_RVW();
        final String injectMeds = record.getCRNT_MGMT_INJCTN_MDCTN();

        if (ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_1_3_ONLY)) {

            // check for valid values OASIS C2 v2.20
            if (!ValidateUtils.isValidValue(drugRegemine,
                    ValidateUtils.ARRAY_DOUBLE_0,
                    ValidateUtils.ARRAY_DOUBLE_1,
                    ValidateUtils.ARRAY_DOUBLE_9,
                    ValidateUtils.ARRAY_DASH_VALUES)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M2001_DRUG_RGMN_RVW, drugRegemine)));
            }

            // check for rule 4431 OASIS C2 v2.20
            if (ValidateUtils.isValidValue(drugRegemine,
                    ValidateUtils.ARRAY_DOUBLE_1)) {
                if (ValidateUtils.isValidValue(injectMeds,
                        ValidateUtils.ARRAY_CARET_VALUES)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4431,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN, injectMeds)));
                }
            }

            // check for rule 4441 OASIS C2 v2.20
            if (ValidateUtils.isValidValue(drugRegemine,
                    ValidateUtils.ARRAY_DOUBLE_9)) {
                if (!ValidateUtils.isValidValue(injectMeds,
                        ValidateUtils.ARRAY_CARET_VALUES)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4441,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN, injectMeds)));
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
        return "Validates Drug Regimen Review for edits: 3060";
    }

}
