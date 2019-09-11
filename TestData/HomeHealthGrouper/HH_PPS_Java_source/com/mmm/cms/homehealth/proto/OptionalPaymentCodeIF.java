/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth.proto;

/**
 * Indicates that the code is an optional payment code. The original term
 * "Optional V-Code" refers to a specific type of "Payment code".
 * This interface has been deprecated for the more general term "Payment Code"
 * as the system evolves from ICD-9 code to ICD-10 codes
 *
 * @author 3M Health Information Systems  for CMS Home Health
 * 
 */
public interface OptionalPaymentCodeIF {

    /**
     * gets the optional payment code indicator.
     * Note that these types of codes are not considered valid case mix codes.
     *
     * @return true if the code is an optional payment code, otherwise false
     */
    boolean isOptionalPaymentCode();

    /**
     * sets the optional payment code indicator
     *
     * @param bool
     */
    void setOptionalPaymentCode(boolean bool);

}

