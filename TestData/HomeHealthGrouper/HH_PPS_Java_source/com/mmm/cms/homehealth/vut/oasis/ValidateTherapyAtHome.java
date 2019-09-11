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
import static com.mmm.cms.util.ValidateUtils.ARRAY_ZERO_ONE;
import java.util.List;

/**
 * Validates therapy at home, IV infusion, Par Nutrition and Enteral Nutrition 
 * for edits: 3060, 3880, 3890
 * 
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateTherapyAtHome extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     * 
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_3060, OasisEditsEN.EDIT_3880, OasisEditsEN.EDIT_3890);
    }

    /**
     * Validates therapy at home, IV infusion, Par Nutrition and Enteral Nutrition
     * 
     * @param record
     * @param edits
     * @return 
     */
    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            if (!ValidateUtils.isValidValue(record.getTHH_IV_INFUSION(), ARRAY_ZERO_ONE)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_IV_INFUSION, record.getTHH_IV_INFUSION())));
            }

            if (!ValidateUtils.isValidValue(record.getTHH_PAR_NUTRITION(), ARRAY_ZERO_ONE)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_PAR_NUTRITION, record.getTHH_PAR_NUTRITION())));
            }

            if (!ValidateUtils.isValidValue(record.getTHH_ENT_NUTRITION(), ARRAY_ZERO_ONE)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_ENT_NUTRITION, record.getTHH_ENT_NUTRITION())));
            }

            if (!ValidateUtils.isValidValue(record.getTHH_NONE_ABOVE(), ARRAY_ZERO_ONE)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_NONE_ABOVE, record.getTHH_NONE_ABOVE())));
            }

            if ("1".equals(record.getTHH_NONE_ABOVE())) {
                if ("1".equals(record.getTHH_IV_INFUSION())
                        || "1".equals(record.getTHH_PAR_NUTRITION())
                        || "1".equals(record.getTHH_ENT_NUTRITION())) {
                    count += 4;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3890,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_NONE_ABOVE, record.getTHH_NONE_ABOVE())));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3890,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_IV_INFUSION, record.getTHH_IV_INFUSION())));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3890,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_PAR_NUTRITION, record.getTHH_PAR_NUTRITION())));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3890,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_ENT_NUTRITION, record.getTHH_ENT_NUTRITION())));
                }
            } else if ("0".equals(record.getTHH_NONE_ABOVE())) {
                if ("0".equals(record.getTHH_IV_INFUSION())
                        && "0".equals(record.getTHH_PAR_NUTRITION())
                        && "0".equals(record.getTHH_ENT_NUTRITION())) {
                    count += 4;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3880,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_NONE_ABOVE, record.getTHH_NONE_ABOVE())));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3880,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_IV_INFUSION, record.getTHH_IV_INFUSION())));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3880,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_PAR_NUTRITION, record.getTHH_PAR_NUTRITION())));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3880,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_ENT_NUTRITION, record.getTHH_ENT_NUTRITION())));
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
        return "Validates therapy at home, IV infusion, Par Nutrition and Enteral Nutrition for edits: 3060, 3880, 3890";
    }

}
