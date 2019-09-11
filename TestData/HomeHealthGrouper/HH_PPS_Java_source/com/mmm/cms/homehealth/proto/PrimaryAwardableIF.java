/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

/**
 * Helps indicate that a code allows subsequent codes to score as a primary
 * code, a concept introduced in v3413
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 * @since V3413
 */
public interface PrimaryAwardableIF {

    /**
     * gets the flag indicating that this Code allows the Diabetes, Neuro 1
     * and Skin 1 primary code in position 2
     * Introduced for V3413
     * @return
     */
    boolean isPrimaryAwardableCode();
    
    /**
     * if the code is null or the code is not a V-Code (I-9) or Z-Code (I-10), and the parameter value
     * is true, then this throws an IllegalStateException because the value can
     * not be set to true unless it is a V/Z-Code.
     * 
     * Introduced for V3413
     * 
     * @param primaryAwardableCode 
     * @throws IllegalStateException 
     */
    void setPrimaryAwardableCode(boolean primaryAwardableCode) throws IllegalStateException;    
}
