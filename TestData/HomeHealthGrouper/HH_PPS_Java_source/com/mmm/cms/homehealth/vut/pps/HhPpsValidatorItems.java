/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut.pps;

import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.RecordItemValidator_HH_PPS_IF;

/**
 * Holds the list of validators that are grouper specific
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class HhPpsValidatorItems  {
    
    private static HhPpsValidatorItems myInstance;
    
    private HhPpsValidatorItems() {
    }
    
    public static HhPpsValidatorItems getInstance() {
        if (myInstance == null) {
            myInstance = new HhPpsValidatorItems();
        }
        return myInstance;
    }
    
    /**
     * Holds the basic OASIS C1 version 2.10 - these items are not HH-PPS
     * version specific, but are specific to the OASIS version.
     */
    public final static RecordItemValidator_HH_PPS_IF[] HHPPS_ITEMS_C_2_10 = new RecordItemValidator_HH_PPS_IF[] {
        new ValidateExistingCodes(),
        new ValidateOptionalPaymentCodes(),
        new ValidateManifestationAsPrimaryDiagnosis(),
        new ValidateOptionalPaymentManifestation(),
        new ValidatePrimaryDiagnosis(),
        new ValidateManifestationEtiologyPairs()
    };

    /**
     * Holds the basic OASIS C1 version 2.11 - these items are not HH-PPS
     * version specific, but are specific to the OASIS version.
     */
    public final static RecordItemValidator_HH_PPS_IF[] HHPPS_ITEMS_C1_2_11 = HHPPS_ITEMS_C_2_10;
 
    /**
     * Holds the basic OASIS C1 version 2.12 - these items are not HH-PPS
     * version specific, but are specific to the OASIS version.
     */
    public final static RecordItemValidator_HH_PPS_IF[] HHPPS_ITEMS_C1_2_12 = new RecordItemValidator_HH_PPS_IF[] {
        new ValidateExistingCodes("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_"),
        new ValidateManifestationAsPrimaryDiagnosis("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_"),
        new ValidatePrimaryDiagnosis("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_"),
        new ValidateManifestationEtiologyPairs("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_")
    };
    
    /**
     * Holds the basic OASIS C1 version 2.20 - these items are not HH-PPS
     * version specific, but are specific to the OASIS version.
     */
    public final static RecordItemValidator_HH_PPS_IF[] HHPPS_ITEMS_C2_2_20 = new RecordItemValidator_HH_PPS_IF[] {
        new ValidateExistingCodes("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_"),
        new ValidateManifestationAsPrimaryDiagnosis("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_"),
        new ValidatePrimaryDiagnosis("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_"),
        new ValidateManifestationEtiologyPairs_C2_220("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_")
    };

    /**
     * Holds the basic OASIS C1 version 2.30 - these items are not HH-PPS
     * version specific, but are specific to the OASIS version.
     */
    public final static RecordItemValidator_HH_PPS_IF[] HHPPS_ITEMS_D_2_30 = new RecordItemValidator_HH_PPS_IF[] {
        new ValidateExistingCodes("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_"),
        new ValidateManifestationAsPrimaryDiagnosis("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_"),
        new ValidatePrimaryDiagnosis("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_"),
        new ValidateManifestationEtiologyPairs_C2_220("M1021_PRIMARY_DIAG_ICD", "M1023_OTH_DIAG", "M1025_OPT_DIAG_ICD_")
    };

    
    public RecordItemValidator_HH_PPS_IF[] getValidationItems(HomeHealthGrouperIF grouper, HomeHealthRecordIF record) {
        switch (record.getRecordType()) {
            case OASIS_C:
                return HHPPS_ITEMS_C_2_10;

            case OASIS_C1_ICD_9:
                return HHPPS_ITEMS_C1_2_11;
                
            case OASIS_C1_ICD_10:
                return HHPPS_ITEMS_C1_2_12;
                
            case OASIS_C2:
                return HHPPS_ITEMS_C2_2_20;

            case OASIS_D:
                return HHPPS_ITEMS_D_2_30;
                
            case OASIS_B:
            default:
                return null;
        }
    }
}


