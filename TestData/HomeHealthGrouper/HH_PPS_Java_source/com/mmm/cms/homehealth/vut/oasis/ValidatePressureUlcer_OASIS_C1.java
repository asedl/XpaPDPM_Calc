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
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ValidateUtils;
import java.util.List;

/**
 * Validates Pressure Ulcers for Stage 1 thru 4, and Non-staging pressure ulcers
 * for edits: 3060, 4130, 4140,4150, 4160, 4170, 4210, 5110, 5120, 5130
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidatePressureUlcer_OASIS_C1 extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

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
     * Creates item validator for the HomeHealthRecord_C1_IF class
     */
    public ValidatePressureUlcer_OASIS_C1() {
        super(HomeHealthRecord_C1_IF.class);
    }

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_3060, OasisEditsEN.EDIT_3010,
                OasisEditsEN.EDIT_4130, OasisEditsEN.EDIT_4140,
                OasisEditsEN.EDIT_4150, OasisEditsEN.EDIT_4160, 
                OasisEditsEN.EDIT_4170, OasisEditsEN.EDIT_4210, 
                OasisEditsEN.EDIT_5110, OasisEditsEN.EDIT_5120, 
                OasisEditsEN.EDIT_5130);
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        final String assessmentReason = record.getASSMT_REASON();

        if (ValidateUtils.isValidValue(assessmentReason, AbstractItemValidator.ASSESSMENT_1_3_4_5_9_ONLY)) {
            if (record instanceof HomeHealthRecord_C1_IF) {
                final HomeHealthRecord_C1_IF recordC = (HomeHealthRecord_C1_IF) record;
                final String UNHLD_STG2_PRSR_ULCR = recordC.getUNHLD_STG2_PRSR_ULCR();
                final String STG_PRBLM_ULCER = recordC.getSTG_PRBLM_ULCER();
                final String NBR_PRSULC_STG1 = recordC.getNBR_PRSULC_STG1();
                final String NBR_PRSULC_STG2 = recordC.getNBR_PRSULC_STG2();
                final String NBR_PRSULC_STG3 = recordC.getNBR_PRSULC_STG3();
                final String NBR_PRSULC_STG4 = recordC.getNBR_PRSULC_STG4();
                final String NSTG_CVRG = recordC.getNSTG_CVRG();
                final String NSTG_DEEP_TISUE = recordC.getNSTG_DEEP_TISUE();
                final String NSTG_DRSG = recordC.getNSTG_DRSG();
                final String STUS_PRBLM_PRSR_ULCR = recordC.getSTUS_PRBLM_PRSR_ULCR();
                final boolean isRFA_1_3_9 = ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_9_ONLY);

                if (!ValidateUtils.isEmpty(UNHLD_STG2_PRSR_ULCR) && !ValidateUtils.isNumeric(UNHLD_STG2_PRSR_ULCR)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1306_UNHLD_STG2_PRSR_ULCR, UNHLD_STG2_PRSR_ULCR)));
                }

                if (!ValidateUtils.isEmptyCaret(NBR_PRSULC_STG2) && !isValueInRange01To99(NBR_PRSULC_STG2)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3010,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2, NBR_PRSULC_STG2)));
                }
                if (!ValidateUtils.isEmptyCaret(NBR_PRSULC_STG3) && !isValueInRange01To99(NBR_PRSULC_STG3)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3010,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3, NBR_PRSULC_STG3)));
                }
                if (!ValidateUtils.isEmptyCaret(NBR_PRSULC_STG4) && !isValueInRange01To99(NBR_PRSULC_STG4)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3010,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4, NBR_PRSULC_STG4)));
                }
                if (!ValidateUtils.isEmptyCaret(NSTG_CVRG) && !isValueInRange01To99(NSTG_CVRG)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3010,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_CVRG, NSTG_CVRG)));
                }
                if (!ValidateUtils.isEmptyCaret(NSTG_DEEP_TISUE) && !isValueInRange01To99(NSTG_DEEP_TISUE)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3010,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE, NSTG_DEEP_TISUE)));
                }

                if (!ValidateUtils.isEmptyCaret(NSTG_DRSG) && !isValueInRange01To99(NSTG_DRSG)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3010,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DRSG, NSTG_DRSG)));
                }
                
                if (isRFA_1_3_9
                        && !ValidateUtils.isValidValue(STUS_PRBLM_PRSR_ULCR, 
                                ValidateUtils.ARRAY_DOUBLE_0, 
                                ValidateUtils.ARRAY_DOUBLE_1, 
                                ValidateUtils.ARRAY_DOUBLE_2, 
                                ValidateUtils.ARRAY_DOUBLE_3, 
                                ValidateUtils.ARRAY_CARET_VALUES, 
                                new String[]{ValidateUtils.NOT_APPLICABLE})) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1320_STUS_PRBLM_PRSR_ULCR, STUS_PRBLM_PRSR_ULCR)));
                }

                if (!ValidateUtils.isValidValue(STG_PRBLM_ULCER, 
                        ValidateUtils.ARRAY_DOUBLE_1, 
                        ValidateUtils.ARRAY_DOUBLE_2, 
                        ValidateUtils.ARRAY_DOUBLE_3, 
                        ValidateUtils.ARRAY_DOUBLE_4,
                        new String[]{ValidateUtils.NOT_APPLICABLE})) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1324_STG_PRBLM_ULCER, STG_PRBLM_ULCER)));
                }

                /*ARRAY_
                 * edit 5110
                 */
                if ("0".equals(UNHLD_STG2_PRSR_ULCR)) {
                    if (!ValidateUtils.isEmpty(NBR_PRSULC_STG2) 
                            || !ValidateUtils.isEmpty(NBR_PRSULC_STG3)
                            || !ValidateUtils.isEmpty(NBR_PRSULC_STG4)
                            || !ValidateUtils.isEmpty(NSTG_CVRG)
                            || !ValidateUtils.isEmpty(NSTG_DEEP_TISUE)
                            || !ValidateUtils.isEmpty(NSTG_DRSG)
                            || !ValidateUtils.isEmpty(STUS_PRBLM_PRSR_ULCR)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5110,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1306_UNHLD_STG2_PRSR_ULCR, UNHLD_STG2_PRSR_ULCR)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5110,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2, NBR_PRSULC_STG2)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5110,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3, NBR_PRSULC_STG3)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5110,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4, NBR_PRSULC_STG4)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5110,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_CVRG, NSTG_CVRG)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5110,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DRSG, NSTG_DRSG)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5110,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE, NSTG_DEEP_TISUE)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5110,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1320_STUS_PRBLM_PRSR_ULCR, STUS_PRBLM_PRSR_ULCR)));
                    }
                } else if ("1".equals(UNHLD_STG2_PRSR_ULCR)) {
                    if (ValidateUtils.isEmpty(NBR_PRSULC_STG2) || edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2)
                            || ValidateUtils.isEmpty(NBR_PRSULC_STG3)|| edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3)
                            || ValidateUtils.isEmpty(NBR_PRSULC_STG4)|| edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4)
                            || ValidateUtils.isEmpty(NSTG_CVRG)|| edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_CVRG)
                            || ValidateUtils.isEmpty(NSTG_DEEP_TISUE)|| edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE)
                            || ValidateUtils.isEmpty(NSTG_DRSG)|| edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DRSG)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5120,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1306_UNHLD_STG2_PRSR_ULCR, UNHLD_STG2_PRSR_ULCR)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5120,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2, NBR_PRSULC_STG2)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5120,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3, NBR_PRSULC_STG3)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5120,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4, NBR_PRSULC_STG4)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5120,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_CVRG, NSTG_CVRG)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5120,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DRSG, NSTG_DRSG)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5120,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE, NSTG_DEEP_TISUE)));

                    } else if (ValidateUtils.isValidValue(NBR_PRSULC_STG2, ValidateUtils.ARRAY_DOUBLE_0)
                            && ValidateUtils.isValidValue(NBR_PRSULC_STG3, ValidateUtils.ARRAY_DOUBLE_0)
                            && ValidateUtils.isValidValue(NBR_PRSULC_STG4, ValidateUtils.ARRAY_DOUBLE_0)
                            && ValidateUtils.isValidValue(NSTG_CVRG, ValidateUtils.ARRAY_DOUBLE_0)
                            && ValidateUtils.isValidValue(NSTG_DRSG, ValidateUtils.ARRAY_DOUBLE_0)
                            && ValidateUtils.isValidValue(NSTG_DEEP_TISUE, ValidateUtils.ARRAY_DOUBLE_0)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5130,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2, NBR_PRSULC_STG2)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5130,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3, NBR_PRSULC_STG3)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5130,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4, NBR_PRSULC_STG4)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5130,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_CVRG, NSTG_CVRG)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5130,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DRSG, NSTG_DRSG)));
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5130,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE, NSTG_DEEP_TISUE)));
                    }
                } else {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1306_UNHLD_STG2_PRSR_ULCR, UNHLD_STG2_PRSR_ULCR)));
                }

                /*
                 * edit 4210
                 */
                if (ValidateUtils.isValidValue(NBR_PRSULC_STG1, ValidateUtils.ARRAY_DOUBLE_0)
                    && (ValidateUtils.isValidValue(NBR_PRSULC_STG2, ValidateUtils.ARRAY_DOUBLE_0) || ValidateUtils.isEmpty(NBR_PRSULC_STG2))
                    && (ValidateUtils.isValidValue(NBR_PRSULC_STG3, ValidateUtils.ARRAY_DOUBLE_0) || ValidateUtils.isEmpty(NBR_PRSULC_STG3))
                    && (ValidateUtils.isValidValue(NBR_PRSULC_STG4, ValidateUtils.ARRAY_DOUBLE_0) || ValidateUtils.isEmpty(NBR_PRSULC_STG4))
                    && !ValidateUtils.NOT_APPLICABLE.equals(STG_PRBLM_ULCER)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4210,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1322_NBR_PRSULC_STG1, NBR_PRSULC_STG1)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4210,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2, NBR_PRSULC_STG2)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4210,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3, NBR_PRSULC_STG3)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4210,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4, NBR_PRSULC_STG4)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4210,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1324_STG_PRBLM_ULCER, STG_PRBLM_ULCER)));
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
                 * edit 4130
                 */
                if (intStage2 == 0 && intStage3 == 0 && intStage4 == 0 && intNonStageCovering == 0
                        && intNonStageDeepTissue == 0
                        && isRFA_1_3_9
                        && !ValidateUtils.NOT_APPLICABLE.equals(STUS_PRBLM_PRSR_ULCR)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4130,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2, NBR_PRSULC_STG2)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4130,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3, NBR_PRSULC_STG3)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4130,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4, NBR_PRSULC_STG4)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4130,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_CVRG, NSTG_CVRG)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4130,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE, NSTG_DEEP_TISUE)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4130,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1320_STUS_PRBLM_PRSR_ULCR, STUS_PRBLM_PRSR_ULCR)));
                }

                /*
                 * edit 4140
                 */
                if ((intStage2 > 0 || intNonStageDeepTissue > 0)
                        && intStage3 == 0 && intStage4 == 0 && intNonStageCovering == 0
                        && isRFA_1_3_9
                        && !"03".equals(STUS_PRBLM_PRSR_ULCR)) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4140,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2, NBR_PRSULC_STG2)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4140,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3, NBR_PRSULC_STG3)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4140,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4, NBR_PRSULC_STG4)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4140,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_CVRG, NSTG_CVRG)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4140,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE, NSTG_DEEP_TISUE)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4140,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1320_STUS_PRBLM_PRSR_ULCR, STUS_PRBLM_PRSR_ULCR)));
                }

                /*
                 * edit 4150
                 */
                if ((intStage3 > 0 || intStage4 > 0)
                        && isRFA_1_3_9
                        && !ValidateUtils.isValidValue(STUS_PRBLM_PRSR_ULCR, 
                                ValidateUtils.ARRAY_DOUBLE_0, 
                                ValidateUtils.ARRAY_DOUBLE_1, 
                                ValidateUtils.ARRAY_DOUBLE_2, 
                                ValidateUtils.ARRAY_DOUBLE_3)) {
                    // if any stage x > 0, then the Status Problem Pressure Ucler
                    // must be 00, 01, 02, or 03, but only when RFA = 1 or 3
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4150,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3, NBR_PRSULC_STG3)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4150,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4, NBR_PRSULC_STG4)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4150,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1320_STUS_PRBLM_PRSR_ULCR, STUS_PRBLM_PRSR_ULCR)));
                }

                /*
                 * edit 4160
                 */
                if (intNonStageCovering > 0 && intStage2 == 0 && intStage3 == 0
                        && intStage4 == 0 && intNonStageDeepTissue == 0
                        && isRFA_1_3_9
                        && !(ValidateUtils.isValidValue(STUS_PRBLM_PRSR_ULCR, ValidateUtils.ARRAY_DOUBLE_2) 
                                    || ValidateUtils.isValidValue(STUS_PRBLM_PRSR_ULCR, ValidateUtils.ARRAY_DOUBLE_3))) {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4160,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2, NBR_PRSULC_STG2)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4160,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3, NBR_PRSULC_STG3)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4160,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4, NBR_PRSULC_STG4)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4160,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_CVRG, NSTG_CVRG)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4160,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE, NSTG_DEEP_TISUE)));
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4160,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1320_STUS_PRBLM_PRSR_ULCR, STUS_PRBLM_PRSR_ULCR)));
                }

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
                    if ("01".equals(STG_PRBLM_ULCER)
                            && !ValidateUtils.isValidValue(NBR_PRSULC_STG1, 
                                    ValidateUtils.ARRAY_DOUBLE_1, 
                                    ValidateUtils.ARRAY_DOUBLE_2, 
                                    ValidateUtils.ARRAY_DOUBLE_3, 
                                    ValidateUtils.ARRAY_DOUBLE_4)) {
                        count++;
                        edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_4170,
                                new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1322_NBR_PRSULC_STG1, NBR_PRSULC_STG1)));
                    }
                } else {
                    count++;
                    edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060,
                            new HHOasisDataItem(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1322_NBR_PRSULC_STG1, NBR_PRSULC_STG1)));
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
     * point and decimal values are not allowed. A sign will not be accepted.<br />
     * <br />
     * The following examples are allowable if the value to be submitted is 
     * equal to [1] and the maximum length is equal to 2: [1], [01]. The 
     * following values are NOT allowed and will lead to a fatal error: [1.], 
     * [1.0], [01.], [01.0], [1.1], [01.1], [1.01], [+1], [-2], [+1.3], [-4.5].<br />
     * <br />
     * NOTE: Additional allowable items is [ 1] for the flat file format due
     * to the phrase above of "Leading zeroes may be included or omitted..." 
     * and the fact that this phrase does not require the value to left 
     * justified.
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
        switch(value.length()) {
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
        return "Validates Pressure Ulcers for Stage 1 thru 4, and Non-staging pressure ulcers for edits: 3060, 4130, 4140,4150, 4160, 4170, 4210, 5110, 5120, 5130";
    }

}