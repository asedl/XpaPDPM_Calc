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
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_D_IF;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.util.ValidateUtils;
import java.util.List;

/**
 * Validates Current Injectable Meds for edits: 3060, 4440, 4450, 4490
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateInjectableMeds_OASIS_D extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base
        (
                OasisEditsEN.EDIT_3060,
                OasisEditsEN.EDIT_4441,
                OasisEditsEN.EDIT_4431,
                OasisEditsEN.EDIT_4490
        );
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;

        // get all involved values
        final String assessmentReason = record.getASSMT_REASON();
        final String tmpInjectMeds = record.getCRNT_MGMT_INJCTN_MDCTN();
        final String drugRegimenReview = ((HomeHealthRecord_D_IF) record).getDRUG_RGMN_RVW();

        if (ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            // check all valid values for inject meds
            if (!ValidateUtils.isValidValue(tmpInjectMeds,
                    ValidateUtils.ARRAY_DOUBLE_0,
                    ValidateUtils.ARRAY_DOUBLE_1,
                    ValidateUtils.ARRAY_DOUBLE_2,
                    ValidateUtils.ARRAY_DOUBLE_3,
                    ValidateUtils.ARRAY_NOT_APPLICABLE,
                    ValidateUtils.ARRAY_CARET_VALUES)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN, tmpInjectMeds)));
            }
            
            // edit 4490
            if (ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_4_5_9_ONLY)
                    && ValidateUtils.isValidValue(tmpInjectMeds, ValidateUtils.ARRAY_CARET_VALUES)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4490,
                        new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN, tmpInjectMeds)));
            }
            
            // edit 4431
            if ((ValidateUtils.isValidValue(drugRegimenReview, ValidateUtils.ARRAY_DOUBLE_1)
                    && ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_1_3_ONLY))
                    && ValidateUtils.isValidValue(tmpInjectMeds, ValidateUtils.ARRAY_CARET_VALUES)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4431,
                        new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M2001_DRUG_RGMN_RVW, drugRegimenReview)));
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4431,
                        new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN, tmpInjectMeds)));
            }

            // edit 4441
            if ((ValidateUtils.isValidValue(drugRegimenReview, ValidateUtils.ARRAY_DOUBLE_9)
                    && ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_1_3_ONLY))
                    && !ValidateUtils.isValidValue(tmpInjectMeds, ValidateUtils.ARRAY_CARET_VALUES)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4441,
                        new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M2001_DRUG_RGMN_RVW, drugRegimenReview)));
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4441,
                        new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN, tmpInjectMeds)));
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
        return "Validates Current Injectable Meds for  edits: 3060, 4440, 4450, 4490";
    }

}
