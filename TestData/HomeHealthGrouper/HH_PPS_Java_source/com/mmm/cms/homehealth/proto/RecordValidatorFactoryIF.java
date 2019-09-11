/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;

/**
 * Provides a mechanism to look up the validations items required for a specific
 * record, and/or Grouper
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface RecordValidatorFactoryIF {
    
    public RecordItemValidatorIF[] getValidationItems(HomeHealthRecordIF record);

}
