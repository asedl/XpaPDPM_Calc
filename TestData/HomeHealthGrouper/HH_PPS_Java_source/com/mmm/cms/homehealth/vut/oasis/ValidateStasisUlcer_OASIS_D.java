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
import java.util.List;

/**
 * Validates Stasis Ulcer for edits: 3060, 4230, 4220
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateStasisUlcer_OASIS_D extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    @Override
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base
            (
                OasisEditsEN.EDIT_3060,
                OasisEditsEN.EDIT_4220,
                OasisEditsEN.EDIT_4230
            );
    }

    @Override
    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;

        final boolean isRFA_1_3_4_5_only = ValidateUtils.
                isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY);

        final boolean isRFA_1_3_4_5_9_only = ValidateUtils.
                isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_9_ONLY);

        if (isRFA_1_3_4_5_9_only) {
            final String stasPresent = record.getSTAS_ULCR_PRSNT();
            final String numStas = record.getNBR_STAS_ULCR();
            final String statStas = record.getSTUS_PRBLM_STAS_ULCR();

            // edit 4230
            // 01 = patient has both observable and unobservable
            // 02 = patient has observable only
            if (ValidateUtils.isValidValue(stasPresent, ValidateUtils.ARRAY_DOUBLE_1, ValidateUtils.ARRAY_DOUBLE_2)) {
                // if number of stasis ulcers or status of problem stasis ulcer are blank, then flag the error
                if (ValidateUtils.isValidValue(numStas, ValidateUtils.ARRAY_CARET_VALUES)) {
                    if (isRFA_1_3_4_5_only) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4230,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1332_NBR_STAS_ULCR, numStas)));
                    }
                }

                if (ValidateUtils.isValidValue(statStas, ValidateUtils.ARRAY_CARET_VALUES)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4230,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1334_STUS_PRBLM_STAS_ULCR, statStas)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4230,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1330_STAS_ULCR_PRSNT, stasPresent)));
                }
            }

            // edit 4220
            if (ValidateUtils.isValidValue(stasPresent, ValidateUtils.ARRAY_DOUBLE_0, ValidateUtils.ARRAY_DOUBLE_3)) {
                // 00 = patient does not have any stasis ulcers
                // 03 = patient has only an unobservable stasis ulcer
                // if the stasis ulcer is 00 or 03, then the number of stasis
                // ulcers and status of problem stasis ulcer both should be blank
                if (!ValidateUtils.isValidValue(numStas, ValidateUtils.ARRAY_CARET_VALUES)) {
                    if (isRFA_1_3_4_5_only) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4220,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1332_NBR_STAS_ULCR, numStas)));
                    }
                }
                if (!ValidateUtils.isValidValue(statStas, ValidateUtils.ARRAY_CARET_VALUES)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4220,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1334_STUS_PRBLM_STAS_ULCR, statStas)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4220,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1330_STAS_ULCR_PRSNT, stasPresent)));
                }
            }

            if (!ValidateUtils.isValidValue(numStas,
                    ValidateUtils.ARRAY_DOUBLE_1,
                    ValidateUtils.ARRAY_DOUBLE_2,
                    ValidateUtils.ARRAY_DOUBLE_3,
                    ValidateUtils.ARRAY_DOUBLE_4,
                    ValidateUtils.ARRAY_CARET_VALUES)) {
                if (isRFA_1_3_4_5_only) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1332_NBR_STAS_ULCR, numStas)));
                }
            }

            if (!ValidateUtils.isValidValue(statStas,
                    ValidateUtils.ARRAY_DOUBLE_1,
                    ValidateUtils.ARRAY_DOUBLE_2,
                    ValidateUtils.ARRAY_DOUBLE_3,
                    ValidateUtils.ARRAY_CARET_VALUES)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1334_STUS_PRBLM_STAS_ULCR, statStas)));
            }

            if (!ValidateUtils.isValidValue(stasPresent,
                    ValidateUtils.ARRAY_DOUBLE_0,
                    ValidateUtils.ARRAY_DOUBLE_1,
                    ValidateUtils.ARRAY_DOUBLE_2,
                    ValidateUtils.ARRAY_DOUBLE_3)) {
                count++;
                edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                        new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1330_STAS_ULCR_PRSNT, statStas)));
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
        return "Validates Stasis Ulcer for edits: 3060, 4220, 4230";
    }

}
