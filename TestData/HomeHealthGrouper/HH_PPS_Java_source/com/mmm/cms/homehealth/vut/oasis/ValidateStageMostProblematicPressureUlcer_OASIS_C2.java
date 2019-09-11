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
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ValidateUtils;
import java.util.List;

/**
 * Validates Stage Most Problematic Pressure Ulcer for edits: 3060,
 * 4170, 4181, 4191, 4201
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateStageMostProblematicPressureUlcer_OASIS_C2 extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_3060, OasisEditsEN.EDIT_4170,
                OasisEditsEN.EDIT_4181, OasisEditsEN.EDIT_4191, OasisEditsEN.EDIT_4201);
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_9_ONLY)) {
            final String problemUlcer = record.getSTG_PRBLM_ULCER();
            String tmpStr;

            // check the value of the Stage of Most Problematic Pressure Ulcer
            if ("01".equals(problemUlcer)) {
                // number of pressure Ulcer stage 1 must be > 0
                tmpStr = record.getNBR_PRSULC_STG1();
//                if (ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES) || ValidateUtils.DOUBLE_ZERO.equals(tmpStr)) {
                if (ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES) || IntegerUtils.parseInt(tmpStr, 0) <= 0) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4170,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1324_STG_PRBLM_ULCER, problemUlcer)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4170,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1322_NBR_PRSULC_STG1, tmpStr)));
                }
            } else if ("02".equals(problemUlcer)) {
                // number of pressure Ulcer stage 2 must be > 0
                tmpStr = record.getNBR_PRSULC_STG2();
//                if (ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES) || ValidateUtils.DOUBLE_ZERO.equals(tmpStr)) {
                if (ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES) || IntegerUtils.parseInt(tmpStr, 0) <= 0) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4181,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1324_STG_PRBLM_ULCER, problemUlcer)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4181,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1311_NBR_PRSULC_STG2_A1, tmpStr)));
                }
            } else if ("03".equals(problemUlcer)) {
                // number of pressure Ulcer stage 3 must be > 0
                tmpStr = record.getNBR_PRSULC_STG3();
//                if (ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES) || ValidateUtils.DOUBLE_ZERO.equals(tmpStr)) {
                if (ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES) || IntegerUtils.parseInt(tmpStr, 0) <= 0) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4191,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1324_STG_PRBLM_ULCER, problemUlcer)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4191,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1311_NBR_PRSULC_STG3_B1, tmpStr)));
                }
            } else if ("04".equals(problemUlcer)) {
                // number of pressure Ulcer stage 4 must be > 0
                tmpStr = record.getNBR_PRSULC_STG4();
//                if (ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES) || ValidateUtils.DOUBLE_ZERO.equals(tmpStr)) {
                if (ValidateUtils.isValidValue(tmpStr, ValidateUtils.ARRAY_CARET_VALUES) || IntegerUtils.parseInt(tmpStr, 0) <= 0) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4201,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1324_STG_PRBLM_ULCER, problemUlcer)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4201,
                            new HHOasisDataItem(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1311_NBR_PRSULC_STG4_C1, tmpStr)));
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
        return "Validates Stage Most Problematic Pressure Ulcer for edits: 3060, 4170, 4181, 4191, 4201\n";
    }

}
