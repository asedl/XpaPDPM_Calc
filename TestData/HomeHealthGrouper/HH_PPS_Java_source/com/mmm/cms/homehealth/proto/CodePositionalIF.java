/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth.proto;

/**
 * This provides code position indicators such as primary or secondary only
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface CodePositionalIF {

    /**
     * gets the secondary only indicator
     * @return true if the code can only be used as a secondary code
     */
    boolean isSecondaryOnly();

    /**
     * sets the secondary only flag
     * @param bool
     */
    void setSecondaryOnly(boolean bool);

    /**
     * Indicates if the code is considered the primary code
     * in the case mix.
     *
     * The default should be false.
     *
     * @return true if the code is considered primary
     */
    boolean isPrimary();

    /**
     * Sets the Primary Diagnosis indicator
     * @param bool - true or false
     */
    void setPrimary(boolean bool);

}

