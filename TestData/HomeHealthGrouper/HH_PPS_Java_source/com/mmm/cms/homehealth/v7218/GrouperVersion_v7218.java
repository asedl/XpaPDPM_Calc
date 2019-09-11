/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v7218;

import com.mmm.cms.homehealth.v7118.GrouperVersion_v7118;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This version is valid from October 1, 2017 to December 31, 2017 and applies
 * to I-10 codes using the OASIS-C1 data formats; there is no grace period and
 * no start window; the HomeHealthEventNotifierIF methods are present, but do
 * nothing.
 * 
 * Changes in logic:
 * 
 * 1) See readme.txt
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperVersion_v7218 extends GrouperVersion_v7118 {

   /**
     * Used to pass grouper default to parent grouper
     * o
     * @param effectiveStart
     * @param effectiveThru
     * @param effectiveWindow
     * @param versionName 
     */
    public GrouperVersion_v7218(Calendar effectiveStart, Calendar effectiveThru, Calendar effectiveWindow, String versionName) {
        super(effectiveStart, effectiveThru, effectiveWindow, versionName);
    }
    
    /**
     * Constructor that sets the effective start and thru date
     */
    public GrouperVersion_v7218() {
        super(new GregorianCalendar(2018, 9, 1), // effective October 1, 2018
                new GregorianCalendar(2018, 11, 31), // effective thru December 31, 2018
                null, // effective date window - no window in this version
                "V7218");
    }
    
}
