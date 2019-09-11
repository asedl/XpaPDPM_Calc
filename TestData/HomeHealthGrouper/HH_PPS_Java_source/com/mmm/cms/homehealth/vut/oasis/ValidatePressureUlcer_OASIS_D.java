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
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ValidateUtils;
import java.util.List;

/**
 * Validates Pressure Ulcers for Stage 1 thru 4, and Non-staging pressure ulcers
 * for edits: 3060, 4131, 4141,4151, 4161, 4170, 4211, 5110, 5121, 5131
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidatePressureUlcer_OASIS_D extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    static final String validNumberOf[];

    static {
        validNumberOf = new String[110];
        // allow "01" and "1 " as values
        for (int idx = 0; idx < 10; idx++) {
            validNumberOf[idx] = "0" + idx;
            validNumberOf[idx + 10] = idx + " ";
        }
        for (int idx = 20; idx < validNumberOf.length; idx++) {
            validNumberOf[idx] = Integer.toString(idx);
        }
    }

    /**
     * Creates item validator for the HomeHealthRecord_C2_IF class
     */
    public ValidatePressureUlcer_OASIS_D() {
        super(HomeHealthRecord_D_IF.class);
    }

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(
                OasisEditsEN.EDIT_3060,
                OasisEditsEN.EDIT_3010,
                OasisEditsEN.EDIT_4170,
                OasisEditsEN.EDIT_4211,
                OasisEditsEN.EDIT_5111,
                OasisEditsEN.EDIT_5122,
                OasisEditsEN.EDIT_5131,
                OasisEditsEN.EDIT_5660,
                OasisEditsEN.EDIT_5680
        );
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        final String assessmentReason = record.getASSMT_REASON();

        if (ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_1_3_4_5_9_ONLY)) {
            if (record instanceof HomeHealthRecord_D_IF) {
                final HomeHealthRecord_D_IF recordD = (HomeHealthRecord_D_IF) record;
                final String UNHLD_STG2_PRSR_ULCR = recordD.getUNHLD_STG2_PRSR_ULCR();
                final String STG_PRBLM_ULCER = recordD.getSTG_PRBLM_ULCER();
                final String NBR_PRSULC_STG1 = recordD.getNBR_PRSULC_STG1();
                final String NBR_PRSULC_STG2 = recordD.getNBR_PRSULC_STG2();
                final String NBR_PRSULC_STG3 = recordD.getNBR_PRSULC_STG3();
                final String NBR_PRSULC_STG4 = recordD.getNBR_PRSULC_STG4();
                final String NSTG_CVRG = recordD.getNSTG_CVRG();
                final String NSTG_DEEP_TISUE = recordD.getNSTG_DEEP_TISUE();
                final String NSTG_DRSG = recordD.getNSTG_DRSG();
                final boolean isRFA_1_3_4_5_only = ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY);
                final boolean isRFA_9_only = ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_9_ONLY);

                if (!ValidateUtils.isEmpty(UNHLD_STG2_PRSR_ULCR) && !ValidateUtils.isNumeric(UNHLD_STG2_PRSR_ULCR)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1306_UNHLD_STG2_PRSR_ULCR, UNHLD_STG2_PRSR_ULCR)));
                }

                if (!ValidateUtils.isEmptyCaret(NBR_PRSULC_STG2) && !isValueInRange01To99(NBR_PRSULC_STG2)
                        && !ValidateUtils.isValidValue(NBR_PRSULC_STG2, ValidateUtils.ARRAY_DASH_VALUES)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3100,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG2_A1, NBR_PRSULC_STG2)));
                }
                if (!ValidateUtils.isEmptyCaret(NBR_PRSULC_STG3) && !isValueInRange01To99(NBR_PRSULC_STG3)
                        && !ValidateUtils.isValidValue(NBR_PRSULC_STG3, ValidateUtils.ARRAY_DASH_VALUES)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3100,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG3_B1, NBR_PRSULC_STG3)));
                }
                if (!ValidateUtils.isEmptyCaret(NBR_PRSULC_STG4) && !isValueInRange01To99(NBR_PRSULC_STG4)
                        && !ValidateUtils.isValidValue(NBR_PRSULC_STG4, ValidateUtils.ARRAY_DASH_VALUES)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3100,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG4_C1, NBR_PRSULC_STG4)));
                }
                if (!ValidateUtils.isEmptyCaret(NSTG_CVRG) && !isValueInRange01To99(NSTG_CVRG)
                        && !ValidateUtils.isValidValue(NSTG_CVRG, ValidateUtils.ARRAY_DASH_VALUES)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3100,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_CVRG_E1, NSTG_CVRG)));
                }
                if (!ValidateUtils.isEmptyCaret(NSTG_DEEP_TISUE) && !isValueInRange01To99(NSTG_DEEP_TISUE)
                        && !ValidateUtils.isValidValue(NSTG_DEEP_TISUE, ValidateUtils.ARRAY_DASH_VALUES)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3100,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DEEP_TSUE_F1, NSTG_DEEP_TISUE)));
                }

                if (!ValidateUtils.isEmptyCaret(NSTG_DRSG) && !isValueInRange01To99(NSTG_DRSG)
                        && !ValidateUtils.isValidValue(NSTG_DRSG, ValidateUtils.ARRAY_DASH_VALUES)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3100,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DRSG_D1, NSTG_DRSG)));
                }

                if (!ValidateUtils.isValidValue(STG_PRBLM_ULCER,
                        ValidateUtils.ARRAY_DOUBLE_1,
                        ValidateUtils.ARRAY_DOUBLE_2,
                        ValidateUtils.ARRAY_DOUBLE_3,
                        ValidateUtils.ARRAY_DOUBLE_4,
                        new String[]{ValidateUtils.NOT_APPLICABLE})) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1324_STG_PRBLM_ULCER, STG_PRBLM_ULCER)));
                }

                /*
            * Edit 5660
                 */
                if (isRFA_1_3_4_5_only) {
                    if (ValidateUtils.isValidValue(NBR_PRSULC_STG2, ValidateUtils.ARRAY_DASH_VALUES)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5660,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG2_A1, NBR_PRSULC_STG2)));
                    }
                    if (ValidateUtils.isValidValue(NBR_PRSULC_STG3, ValidateUtils.ARRAY_DASH_VALUES)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5660,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG3_B1, NBR_PRSULC_STG3)));
                    }
                    if (ValidateUtils.isValidValue(NBR_PRSULC_STG4, ValidateUtils.ARRAY_DASH_VALUES)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5660,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG4_C1, NBR_PRSULC_STG4)));
                    }
                    if (ValidateUtils.isValidValue(NSTG_CVRG, ValidateUtils.ARRAY_DASH_VALUES)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5660,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_CVRG_E1, NSTG_CVRG)));
                    }
                    if (ValidateUtils.isValidValue(NSTG_DEEP_TISUE, ValidateUtils.ARRAY_DASH_VALUES)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5660,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DEEP_TSUE_F1, NSTG_DEEP_TISUE)));
                    }
                    if (ValidateUtils.isValidValue(NSTG_DRSG, ValidateUtils.ARRAY_DASH_VALUES)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5660,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DRSG_D1, NSTG_DRSG)));
                    }
                }

                /*ARRAY_
                 * edit 5111
                 */
                if (ValidateUtils.isValidValue(UNHLD_STG2_PRSR_ULCR, ValidateUtils.ARRAY_DOUBLE_0)) {
                    if (!ValidateUtils.isEmpty(NBR_PRSULC_STG2)
                            || !ValidateUtils.isEmpty(NBR_PRSULC_STG3)
                            || !ValidateUtils.isEmpty(NBR_PRSULC_STG4)
                            || !ValidateUtils.isEmpty(NSTG_CVRG)
                            || !ValidateUtils.isEmpty(NSTG_DEEP_TISUE)
                            || !ValidateUtils.isEmpty(NSTG_DRSG)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5111,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1306_UNHLD_STG2_PRSR_ULCR, UNHLD_STG2_PRSR_ULCR)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5111,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG2_A1, NBR_PRSULC_STG2)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5111,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG3_B1, NBR_PRSULC_STG3)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5111,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG4_C1, NBR_PRSULC_STG4)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5111,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_CVRG_E1, NSTG_CVRG)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5111,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DRSG_D1, NSTG_DRSG)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5111,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DEEP_TSUE_F1, NSTG_DEEP_TISUE)));
                    }
                }
                // Edit 5122
                if (ValidateUtils.isValidValue(UNHLD_STG2_PRSR_ULCR, ValidateUtils.ARRAY_DOUBLE_1)) {
                    if (ValidateUtils.isEmpty(NBR_PRSULC_STG2) || edits.isEditPresent(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG2_A1)
                            || ValidateUtils.isEmpty(NBR_PRSULC_STG3) || edits.isEditPresent(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG3_B1)
                            || ValidateUtils.isEmpty(NBR_PRSULC_STG4) || edits.isEditPresent(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG4_C1)
                            || ValidateUtils.isEmpty(NSTG_CVRG) || edits.isEditPresent(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_CVRG_E1)
                            || ValidateUtils.isEmpty(NSTG_DEEP_TISUE) || edits.isEditPresent(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DEEP_TSUE_F1)
                            || ValidateUtils.isEmpty(NSTG_DRSG) || edits.isEditPresent(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DRSG_D1)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5122,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1306_UNHLD_STG2_PRSR_ULCR, UNHLD_STG2_PRSR_ULCR)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5122,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG2_A1, NBR_PRSULC_STG2)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5122,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG3_B1, NBR_PRSULC_STG3)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5122,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG4_C1, NBR_PRSULC_STG4)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5122,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_CVRG_E1, NSTG_CVRG)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5122,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DRSG_D1, NSTG_DRSG)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5122,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DEEP_TSUE_F1, NSTG_DEEP_TISUE)));

                    }
                }
                // Edit 5131
                if (ValidateUtils.isValidValue(NBR_PRSULC_STG2, ValidateUtils.ARRAY_DOUBLE_0)
                        && ValidateUtils.isValidValue(NBR_PRSULC_STG3, ValidateUtils.ARRAY_DOUBLE_0)
                        && ValidateUtils.isValidValue(NBR_PRSULC_STG4, ValidateUtils.ARRAY_DOUBLE_0)
                        && ValidateUtils.isValidValue(NSTG_CVRG, ValidateUtils.ARRAY_DOUBLE_0)
                        && ValidateUtils.isValidValue(NSTG_DRSG, ValidateUtils.ARRAY_DOUBLE_0)
                        && ValidateUtils.isValidValue(NSTG_DEEP_TISUE, ValidateUtils.ARRAY_DOUBLE_0)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5131,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG2_A1, NBR_PRSULC_STG2)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5131,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG3_B1, NBR_PRSULC_STG3)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5131,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG4_C1, NBR_PRSULC_STG4)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5131,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_CVRG_E1, NSTG_CVRG)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5131,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DRSG_D1, NSTG_DRSG)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5131,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NSTG_DEEP_TSUE_F1, NSTG_DEEP_TISUE)));
                }

                if (!ValidateUtils.isValidValue(UNHLD_STG2_PRSR_ULCR, ValidateUtils.ARRAY_DOUBLE_0, ValidateUtils.ARRAY_DOUBLE_1)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1306_UNHLD_STG2_PRSR_ULCR, UNHLD_STG2_PRSR_ULCR)));
                }

                /*
            Edit 5680 ([V2.30.1]-New edit. Replaces edit -4211):
            (a) If M0100_ASSMT_REASON=[01,03,04,05] and M1322_NBR_PRSULC_STG1 = [00], and if M1311_NBR_PRSULC_STG2_A1,
                M1311_NBR_PRSULC_STG3_B1 and M1311_NBR_PRSULC_STG4_C1 are all equal to [00,^],
                then M1324_STG_PRBLM_ULCER must be equal to [NA].
            (b) If M0100_ASSMT_REASON=[09], and if M1311_NBR_PRSULC_STG2_A1, M1311_NBR_PRSULC_STG3_B1 and M1311_NBR_PRSULC_STG4_C1
                are all equal to [00,-,^], then M1324_STG_PRBLM_ULCER must be equal to [1, NA].  
                 */
 /* (a) */
                if ((ValidateUtils.isValidValue(NBR_PRSULC_STG1, ValidateUtils.ARRAY_DOUBLE_0) && isRFA_1_3_4_5_only)
                        && (ValidateUtils.isValidValue(NBR_PRSULC_STG2, ValidateUtils.ARRAY_DOUBLE_0) || ValidateUtils.isEmpty(NBR_PRSULC_STG2))
                        && (ValidateUtils.isValidValue(NBR_PRSULC_STG3, ValidateUtils.ARRAY_DOUBLE_0) || ValidateUtils.isEmpty(NBR_PRSULC_STG3))
                        && (ValidateUtils.isValidValue(NBR_PRSULC_STG4, ValidateUtils.ARRAY_DOUBLE_0) || ValidateUtils.isEmpty(NBR_PRSULC_STG4))
                        && !ValidateUtils.NOT_APPLICABLE.equals(STG_PRBLM_ULCER)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5680,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1322_NBR_PRSULC_STG1, NBR_PRSULC_STG1)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5680,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG2_A1, NBR_PRSULC_STG2)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5680,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG3_B1, NBR_PRSULC_STG3)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5680,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG4_C1, NBR_PRSULC_STG4)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5680,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1324_STG_PRBLM_ULCER, STG_PRBLM_ULCER)));
                }
                /* (b) */
                if (isRFA_9_only
                        && (ValidateUtils.isValidValue(NBR_PRSULC_STG2, ValidateUtils.ARRAY_DOUBLE_0)
                        || ValidateUtils.isEmpty(NBR_PRSULC_STG2) || ValidateUtils.isValidValue(NBR_PRSULC_STG2, ValidateUtils.ARRAY_DASH_VALUES))
                        && (ValidateUtils.isValidValue(NBR_PRSULC_STG3, ValidateUtils.ARRAY_DOUBLE_0)
                        || ValidateUtils.isEmpty(NBR_PRSULC_STG3) || ValidateUtils.isValidValue(NBR_PRSULC_STG3, ValidateUtils.ARRAY_DASH_VALUES))
                        && (ValidateUtils.isValidValue(NBR_PRSULC_STG4, ValidateUtils.ARRAY_DOUBLE_0)
                        || ValidateUtils.isEmpty(NBR_PRSULC_STG4) || ValidateUtils.isValidValue(NBR_PRSULC_STG4, ValidateUtils.ARRAY_DASH_VALUES))
                        && !(ValidateUtils.NOT_APPLICABLE.equals(STG_PRBLM_ULCER)
                        || ValidateUtils.isValidValue(STG_PRBLM_ULCER, ValidateUtils.ARRAY_DOUBLE_1))) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5680,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1322_NBR_PRSULC_STG1, NBR_PRSULC_STG1)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5680,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG2_A1, NBR_PRSULC_STG2)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5680,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG3_B1, NBR_PRSULC_STG3)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5680,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1311_NBR_PRSULC_STG4_C1, NBR_PRSULC_STG4)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5680,
                            new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1324_STG_PRBLM_ULCER, STG_PRBLM_ULCER)));
                }

                /*
                 * convert the strings to number to make it easier to check 
                 * greater than, because the greater than check no longer works
                 * with a string since "^ " (i.e. empty) is actually greater
                 * than "00"
                 */
                final int intStage2 = ValidateUtils.isEmpty(NBR_PRSULC_STG2) ? -1 : IntegerUtils.parseInt(NBR_PRSULC_STG2, -1);
                final int intStage3 = ValidateUtils.isEmpty(NBR_PRSULC_STG3) ? -1 : IntegerUtils.parseInt(NBR_PRSULC_STG3, -1);
                final int intStage4 = ValidateUtils.isEmpty(NBR_PRSULC_STG4) ? -1 : IntegerUtils.parseInt(NBR_PRSULC_STG4, -1);
                final int intNonStageCovering = ValidateUtils.isEmpty(NSTG_CVRG) ? -1 : IntegerUtils.parseInt(NSTG_CVRG, -1);
                final int intNonStageDeepTissue = ValidateUtils.isEmpty(NSTG_DEEP_TISUE) ? -1 : IntegerUtils.parseInt(NSTG_DEEP_TISUE, -1);

                /*
                 * edit 4170
                 * edit 3060 for stage 1
                 */
                if (ValidateUtils.isValidValue(NBR_PRSULC_STG1,
                        ValidateUtils.ARRAY_DOUBLE_0,
                        ValidateUtils.ARRAY_DOUBLE_1,
                        ValidateUtils.ARRAY_DOUBLE_2,
                        ValidateUtils.ARRAY_DOUBLE_3,
                        ValidateUtils.ARRAY_DOUBLE_4)) {
                    if (ValidateUtils.isValidValue(STG_PRBLM_ULCER, ValidateUtils.ARRAY_DOUBLE_1)
                            && !ValidateUtils.isValidValue(NBR_PRSULC_STG1,
                                    ValidateUtils.ARRAY_DOUBLE_1,
                                    ValidateUtils.ARRAY_DOUBLE_2,
                                    ValidateUtils.ARRAY_DOUBLE_3,
                                    ValidateUtils.ARRAY_DOUBLE_4)) {
                        if (isRFA_1_3_4_5_only) {
                            count++;
                            edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4170,
                                    new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1322_NBR_PRSULC_STG1, NBR_PRSULC_STG1)));
                            edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4170,
                                    new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1324_STG_PRBLM_ULCER, STG_PRBLM_ULCER)));
                        }
                    }
                } else {
                    if (isRFA_1_3_4_5_only) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                                new HHOasisDataItem(HomeHealthRecord_D_IF.OASIS_D_ITEM_M1322_NBR_PRSULC_STG1, NBR_PRSULC_STG1)));
                    }
                }
            }
        }

        return count;
    }

    /**
     * This validates the numeric value range based on the OASIS specification
     * of: <br />
     * FORMATTING OF POSITIVE INTEGER NUMERIC ITEMS<br />
     * Only positive integer values and the special values (if any) that are
     * listed in the "Item Values" table of the Detailed Data Specifications
     * Report will be accepted for this item. Leading zeroes may be included or
     * omitted from the submitted value as long as the resulting length of the
     * string does not exceed the allowed maximum length for the item. A decimal
     * point and decimal values are not allowed. A sign will not be
     * accepted.<br />
     * <br />
     * The following examples are allowable if the value to be submitted is
     * equal to [1] and the maximum length is equal to 2: [1], [01]. The
     * following values are NOT allowed and will lead to a fatal error: [1.],
     * [1.0], [01.], [01.0], [1.1], [01.1], [1.01], [+1], [-2], [+1.3],
     * [-4.5].<br />
     * <br />
     * NOTE: Additional allowable items is [ 1] for the flat file format due to
     * the phrase above of "Leading zeroes may be included or omitted..." and
     * the fact that this phrase does not require the value to left justified.
     *
     * @param value
     * @return
     */
    private boolean isValueInRange01To99(String value) {
//        byte bytes[] = value.getBytes();
//        boolean valid = false;
//        
//        if (bytes.length == 2) {
//            if (Character.isDigit(bytes[0])) {
//               valid = Character.isDigit(bytes[1]) || bytes[1] == ' ';
//            } else if (bytes[0] == ' ') {
//                valid = Character.isDigit(bytes[1]) ;
//            }
//        } else {
//            valid = bytes.length == 1 && Character.isDigit(bytes[0]);
//        }
//            
//        return valid;
        boolean valid = false;
        switch (value.length()) {
            case 2:
                char char1 = value.charAt(0);
                char char2 = value.charAt(1);
                if (char1 == ' ') {
                    valid = Character.isDigit(char2);
                } else if (Character.isDigit(char1)) {
                    valid = Character.isDigit(char2) || char2 == ' ';
                }
                break;

            case 1:
                valid = Character.isDigit(value.charAt(0));
        }

        return valid;
    }

    /**
     * Gets the description
     *
     * @return non-null string
     */
    public String getDescription() {
        return "Validates Pressure Ulcers for Stage 1 thru 4, and Non-staging pressure ulcers for edits: 3060, 4131, 4141,4151, 4161, 4170, 4211, 5110, 5121, 5131";
    }

}
