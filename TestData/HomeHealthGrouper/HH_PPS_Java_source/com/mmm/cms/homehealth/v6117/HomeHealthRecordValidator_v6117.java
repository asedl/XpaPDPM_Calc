/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v6117;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C1_IF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C2_IF;
import com.mmm.cms.homehealth.v4115.HomeHealthRecordValidator_v4115;

/**
 * This Validator is different from the previous due to the OASIS-C1 record
 * specs defining the edit/error numbers and the use of the VUT for the actual
 * validation. For historical reasons, the similar methods are used for "valid"
 * indicator, however, the "is...INVLD" methods (which have been deprecated)
 * have been disabled in favor of the "is...Valid" methods.
 *
 * In order to determine valid items, this will check the
 * CollectionValidationEditsIF items to determine the error id and the
 * associated OASIS-C1 item.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HomeHealthRecordValidator_v6117 extends HomeHealthRecordValidator_v4115 implements HomeHealthRecordValidatorIF {


    /**
     * Constructor with the required Grouper reference
     *
     * @param grouper
     */
    public HomeHealthRecordValidator_v6117(HomeHealthGrouperIF grouper) {
        super(grouper);
    }

    /**
     * Constructor with the required Grouper reference
     *
     * @param grouper
     * @param edits - this validator uses the supplied edits instead of
     * re-validating
     *
     */
    public HomeHealthRecordValidator_v6117(HomeHealthGrouperIF grouper,
            CollectionValidationEditsIF edits) {
        super(grouper, edits);
    }

    /**
     * gets if assessment reason is valid
     * 
     * @return true if there are no edit for ASSMT REASON
     */
    @Override
    public boolean isASSMT_REASON_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M0100_ASSMT_REASON)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M0100_ASSMT_REASON)
                ;
    }

    /**
     * This is not really necessary since the record has already been converted,
     * therefore the date was valid.
     *
     * @return true is there are no edit items for INFO COMPLETE
     */
    @Override
    public boolean isINFO_COMPLETED_DT_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M0090_INFO_COMPLETED_DT)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M0090_INFO_COMPLETED_DT)
                ;
    }

    @Override
    public boolean isTHERAPIES_Valid() {

        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_IV_INFUSION)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_PAR_NUTRITION)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_ENT_NUTRITION)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1030_THH_NONE_ABOVE)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1030_THH_IV_INFUSION)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1030_THH_PAR_NUTRITION)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1030_THH_ENT_NUTRITION)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1030_THH_NONE_ABOVE)
               
                ;
    }

    /**
     * @return
     */
    @Override
    public boolean isVISION_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1200_VISION)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1200_VISION)
                ;
    }

    @Override
    public boolean isPAIN_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1242_PAIN_FREQ_ACTVTY_MVMT)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1242_PAIN_FREQ_ACTVTY_MVMT)
                ;
    }

    @Override
    public boolean isLESION_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1350_LESION_OPEN_WND)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1350_LESION_OPEN_WND)
                ;
    }

    @Override
    public boolean isNPRSULC1_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1322_NBR_PRSULC_STG1)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1322_NBR_PRSULC_STG1)
                ;
    }

    @Override
    public boolean isNPRSULC2_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1311_NBR_PRSULC_STG2_A1)
                ;
    }

    @Override
    public boolean isNPRSULC3_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1311_NBR_PRSULC_STG3_B1)
                ;
    }

    @Override
    public boolean isNPRSULC4_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1311_NBR_PRSULC_STG4_C1)
                ;
    }

    @Override
    public boolean isUNOBS_PRSULC_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_CVRG)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DRSG)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE)
                &&
               !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1311_NSTG_CVRG_E1)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1311_NSTG_DRSG_D1)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1311_NSTG_DEEP_TSUE_F1)
                ;
    }

    @Override
    public boolean isSTGPRSUL_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1324_STG_PRBLM_ULCER)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1324_STG_PRBLM_ULCER)
                ;
    }

    @Override
    public boolean isNBR_STASULC_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1332_NBR_STAS_ULCR)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1332_NBR_STAS_ULCR)
                ;
    }

    @Override
    public boolean isUNOBS_STASULC_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1330_STAS_ULCR_PRSNT)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1330_STAS_ULCR_PRSNT)
                ;
    }

    @Override
    public boolean isSTATSTASIS_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1334_STUS_PRBLM_STAS_ULCR)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1334_STUS_PRBLM_STAS_ULCR)
                ;
    }

    @Override
    public boolean isSTATSURG_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1340_SRGCL_WND_PRSNT)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1340_SRGCL_WND_PRSNT)
                ;
    }

    @Override
    public boolean isDYSPNEIC_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1400_WHEN_DYSPNEIC)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1400_WHEN_DYSPNEIC)
                ;
    }

    @Override
    public boolean isUR_INCONT_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1610_UR_INCONT)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1615_INCNTNT_TIMING)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1610_UR_INCONT)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1615_INCNTNT_TIMING)
                ;
    }

    @Override
    public boolean isBWLINCONT_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1620_BWL_INCONT)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1620_BWL_INCONT)
                ;
    }

    @Override
    public boolean isOSTOMY_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1630_OSTOMY)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1630_OSTOMY)
                ;
    }

    @Override
    public boolean isCUR_DRESS_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1810_CRNT_DRESS_UPPER)
                && !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1820_CRNT_DRESS_LOWER)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1810_CRNT_DRESS_UPPER)
                && !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1820_CRNT_DRESS_LOWER)
                ;
    }

    @Override
    public boolean isCUR_BATHING_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1830_CRNT_BATHG)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1830_CRNT_BATHG)
                ;
    }

    @Override
    public boolean isCUR_TOILETING_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1840_CRNT_TOILTG)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1840_CRNT_TOILTG)
                ;
    }

    @Override
    public boolean isCUR_TRANSFER_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1850_CRNT_TRNSFRNG)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1850_CRNT_TRNSFRNG)
                ;
    }

    @Override
    public boolean isCUR_AMBULATION_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M1860_CRNT_AMBLTN)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M1860_CRNT_AMBLTN)
                ;
    }

    /**
     *
     * @return @deprecated
     */
    @Override
    public boolean isCUR_INJECT_MEDS_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN)
                ;
    }

    @Override
    public boolean isTHER_NEED_Valid() {
        return !this.edits.isEditPresent(HomeHealthRecord_C1_IF.OASIS_C1_ITEM_M2200_THER_NEED_NBR)
                &&
                !this.edits.isEditPresent(HomeHealthRecord_C2_IF.OASIS_C2_ITEM_M2200_THER_NEED_NBR)
                ;
    }
    
}
