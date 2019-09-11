/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v8219;

import com.mmm.cms.homehealth.v8119.GrouperVersion_v8119;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This version is valid from January 1, 2018 to September 30, 2018 and applies
 * to I-10 codes using the OASIS-C2 data formats; there is no grace period and
 * no start window; the HomeHealthEventNotifierIF methods are present, but do
 * nothing.
 * 
 * Changes in logic:
 * 
 * 1) See readme.txt
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperVersion_v8219 extends GrouperVersion_v8119 {

   /**
     * Used to pass grouper default to parent grouper
     * 
     * @param effectiveStart
     * @param effectiveThru
     * @param effectiveWindow
     * @param versionName 
     */
    public GrouperVersion_v8219(Calendar effectiveStart, Calendar effectiveThru, Calendar effectiveWindow, String versionName) {
        super(effectiveStart, effectiveThru, effectiveWindow, versionName);
    }
    
    /**
     * Constructor that sets the effective start and thru date
     */
    public GrouperVersion_v8219() {
        super(new GregorianCalendar(2019, 9, 1), // effective October 1, 2019
                new GregorianCalendar(2019, 11, 31), // effective thru December 31, 2019
                null, // effective date window - no window in this version
                "V8219");
    }
    
}
