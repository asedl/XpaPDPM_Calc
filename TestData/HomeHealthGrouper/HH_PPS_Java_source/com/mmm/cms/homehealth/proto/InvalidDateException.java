/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;



/**
 * Exception used when the date is not a valid format.
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class InvalidDateException extends Exception {

    /**
     * Constructor with a message
     * @param message
     */
    public InvalidDateException(String message) {
        super(message);
    }

}
