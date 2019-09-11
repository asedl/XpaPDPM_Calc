/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto.record;




/**
 * This represents the data required by the Home Health Grouper in order to
 * effect scoring claim.  The methods within have names similar to the names
 * of the fields represented in the OASIS record.  However, this class does not
 * identify field positions within any persistent storage mechanism. The way
 * the data is stored (text file, DB, XML, spreadsheet, etc) is left to the
 * implementation classes.
 *
 * Aug 2013 - changed the methods names that reflect OASIS-B column names or
 * Abt programming names to reflect OASIS-C column names. Functionally, no 
 * difference, but makes it easier to reference the OASIS-C/C1 column when
 * looking at the method names.
 * 
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface HomeHealthRecord_B_IF extends HomeHealthRecordIF{

    /**
     * gets the M0450_UNOBS_PRSULC
     *
     * Format:
     0=No, 1=Yes
     * @return
     */
    String getUNOBS_PRSULC();

    /**
     *
     * Format:
     0=No, 1=Yes
     * @param val
     */
    void setUNOBS_PRSULC(String val);

    /**
     * gets the M0474_UNOBS_STASULC
     *
     * Format:
     0=No, 1=Yes
     * @return
     */
    String getUNOBS_STASULC();

    /**
     * Format:
     0=No, 1=Yes
     * @param val
     */
    void setUNOBS_STASULC(String val);

    /**
     * gets the M0484_NBR_SURGWND
     *
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @return
     */
    String getNBR_SURGWND();

    /**
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @param val
     */
    void setNBR_SURGWND(String val);

    
    /**
     * gets the PRESS_ULCER
     * 
     * OASIS-B = M0455
     * OASIS-C = 1306
     * OASIS-C1 = 1306
     *
     * Format: 0=No, 1=Yes
     *
     * @return
     */
    String getPRESS_ULCER();

    /**
     * Sets the PRESS_ULCER
     * 
     * OASIS-B = M0455
     * OASIS-C = 1306
     * OASIS-C1 = 1306
     *
     * Format: 0=No, 1=Yes
     *
     * @param val
     */
    void setPRESS_ULCER(String val); 
    
}

