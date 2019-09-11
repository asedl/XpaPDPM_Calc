/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

/**
 * This indicates the Record type for the HomeHealthRecord.
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public enum RecordType {

    UNKNOWN ("Unknown"),
    OASIS_B ("OASIS B"),
    OASIS_C ("OASIS C"),
    OASIS_C1_ICD_9 ("OASIS C1 ICD-9"),
    OASIS_C1_ICD_10 ("OASIS C1 ICD-10"),
    OASIS_C2 ("OASIS C2"),
    OASIS_D ("OASIS D");
    
    final private String description;
    
    RecordType(String desc) {
        this.description = desc;
    }
    
    public String getDescription() {
        return description;
    }
    
}
