/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth.proto;

/**
 * Provides methods for identifying codes with ulcers or diabetic ulcers
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface CodeUlcerIF {

    /**
     * gets the Diabetic Ulcer indicator
     * @return true if this code is flagged as relating to Diabetic Ulcer
     */
    boolean isDiabeticUlcer();

    /**
     * gets the Ulcer indicator
     * @return true if this code is flagged as relating to Ulcer
     */
    boolean isUlcer();

    /**
     * Sets the relating to Diabetic Ulcer flag
     * @param bool
     */
    void setDiabeticUlcer(boolean bool);

    /**
     * Sets the relating to Ulcer flag
     * @param bool
     */
    void setUlcer(boolean bool);

}

