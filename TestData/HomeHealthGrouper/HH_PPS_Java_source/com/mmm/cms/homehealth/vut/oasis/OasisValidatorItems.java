/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut.oasis;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import com.mmm.cms.homehealth.proto.RecordValidatorFactoryIF;

/**
 * Provides validation items based on the record type.
 * 
 * @author 3M Health Information Systems for CMS Home Health
 */
public class OasisValidatorItems implements RecordValidatorFactoryIF {

    /**
     * Holds the basic OASIS C1 version 2.10 - these items are not HH-PPS
     * version specific, but are specific to the OASIS version.
     */
    public final static RecordItemValidatorIF[] OASIS_ITEMS_C_2_10 = new RecordItemValidatorIF[]{
        new ValidateAssessmentReason(),
        new ValidateBowlIncontinence(),
        new ValidateBowlIncontinenceOstomy(),
        new ValidateCurrentAmbulation(),
        new ValidateCurrentBathing(),
        new ValidateCurrentDressing(),
        new ValidateCurrentToileting(),
        new ValidateCurrentTransferring(),
        new ValidateEpisodeTiming(),
        new ValidateFrequencyPain(),
        new ValidateInjectableMeds(),
        new ValidatePressureUlcer_OASIS_C1(),
        new ValidateStageMostProblematicPressureUlcer(),
        new ValidateStasisUlcer(),
        new ValidateStatusProblematicSurgicalWound(),
        new ValidateTherapyAtHome(),
        new ValidateTherapyNeed(),
        new ValidateUrinaryIncontinence(),
        new ValidateVision(),
        new ValidateWhenDyspneic(),
        new ValidateUniqueDiagnosis() 
                };

    public final static RecordItemValidatorIF[] OASIS_ITEMS_C1_2_11 = OASIS_ITEMS_C_2_10;
    
    public final static RecordItemValidatorIF[] OASIS_ITEMS_C1_2_12 = new RecordItemValidatorIF[]{
        new ValidateAssessmentReason(),
        new ValidateBowlIncontinence(),
        new ValidateBowlIncontinenceOstomy(),
        new ValidateCurrentAmbulation(),
        new ValidateCurrentBathing(),
        new ValidateCurrentDressing(),
        new ValidateCurrentToileting(),
        new ValidateCurrentTransferring(),
        new ValidateEpisodeTiming(),
        new ValidateFrequencyPain(),
        new ValidateInjectableMeds(),
        new ValidatePressureUlcer_OASIS_C1(),
        new ValidateStageMostProblematicPressureUlcer(),
        new ValidateStasisUlcer(),
        new ValidateStatusProblematicSurgicalWound(),
        new ValidateTherapyAtHome(),
        new ValidateTherapyNeed(),
        new ValidateUrinaryIncontinence(),
        new ValidateVision(),
        new ValidateWhenDyspneic(),
        new ValidateUniqueDiagnosis("M1023_OTH_DIAG")
                };
    
        public final static RecordItemValidatorIF[] OASIS_ITEMS_C2_2_20 = new RecordItemValidatorIF[]{
        new ValidateAssessmentReason(),
        new ValidateBowlIncontinence(),
        new ValidateBowlIncontinenceOstomy(),
        new ValidateCurrentAmbulation(),
        new ValidateCurrentBathing(),
        new ValidateCurrentDressing(),
        new ValidateCurrentToileting(),
        new ValidateCurrentTransferring(),
        new ValidateEpisodeTiming(),
        new ValidateFrequencyPain(),
        new ValidateDrugRegimenReview(),
        new ValidateInjectableMeds_OASIS_C2(),
        new ValidatePressureUlcer_OASIS_C2(),
        new ValidateStageMostProblematicPressureUlcer_OASIS_C2(),
        new ValidateStasisUlcer(),
        new ValidateStatusProblematicSurgicalWound(),
        new ValidateTherapyAtHome(),
        new ValidateTherapyNeed(),
        new ValidateUrinaryIncontinence_OASIS_C2(),
        new ValidateVision(),
        new ValidateWhenDyspneic(),
        new ValidateUniqueDiagnosis("M1023_OTH_DIAG")
                };

        public final static RecordItemValidatorIF[] OASIS_ITEMS_D_2_30 = new RecordItemValidatorIF[]{
        new ValidateAssessmentReason(),
        new ValidateBowlIncontinence(),
        new ValidateBowlIncontinenceOstomy_OASIS_D(),
        new ValidateCurrentAmbulation(),
        new ValidateCurrentBathing(),
        new ValidateCurrentDressing(),
        new ValidateCurrentToileting(),
        new ValidateCurrentTransferring(),
        new ValidateEpisodeTiming(),
        new ValidateFrequencyPain(),
        new ValidateDrugRegimenReview_OASIS_D(),
        new ValidateInjectableMeds_OASIS_D(),
        new ValidatePressureUlcer_OASIS_D(),
        new ValidateStageMostProblematicPressureUlcer_OASIS_D(),
        new ValidateStasisUlcer_OASIS_D(),
        new ValidateStatusProblematicSurgicalWound(),
        new ValidateTherapyAtHome(),
        new ValidateTherapyNeed(),
        new ValidateUrinaryIncontinence_OASIS_D(),
        new ValidateVision(),
        new ValidateWhenDyspneic(),
        new ValidateUniqueDiagnosis("M1023_OTH_DIAG")
                };

    @Override
    public RecordItemValidatorIF[] getValidationItems(HomeHealthRecordIF record) {
        switch (record.getRecordType()) {
            case OASIS_C:
                return OASIS_ITEMS_C_2_10;

            case OASIS_C1_ICD_9:
                return OASIS_ITEMS_C1_2_11;
                
            case OASIS_C1_ICD_10:
                return OASIS_ITEMS_C1_2_12;
                
            case OASIS_C2:
                return OASIS_ITEMS_C2_2_20;

            case OASIS_D:
                return OASIS_ITEMS_D_2_30;
                
            case OASIS_B:
            default:
                return null;
        }
    }
    
}
