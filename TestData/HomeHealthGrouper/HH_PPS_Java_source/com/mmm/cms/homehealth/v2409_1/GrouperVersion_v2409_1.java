/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
*/
package com.mmm.cms.homehealth.v2409_1;

import com.mmm.cms.homehealth.v2308_1.GrouperVersion_v2308_1;
import java.util.GregorianCalendar;

/**
 * This version is effective from October 1, 2009 thru December 31, 2009
 * NOTE: OASIS-C records are to be used after December
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class GrouperVersion_v2409_1 extends GrouperVersion_v2308_1 {

   /**
     * Constructor that sets the effective start and thru dates to
     * October 1, 2009 and December 31, 2009 inclusive
     */
    public GrouperVersion_v2409_1() {
        // effective start October 1, 2009
        // effective thru December 31, 2009 - OASIS-C records are to be used
        //  after December
        // no window
        super(new GregorianCalendar(2009, 9, 1), new GregorianCalendar(2009, 11, 31), null);
        setVersion("V2409");
    }

    /**
     * Gets the name of this Grouper version
     *
     * @return The name of this version, plus version number
     */
    @Override
    public String getName() {
        return "HHA PPS Grouper - October 2009 ICD-9-CM codes - " + getVersion();
    }

    /**
     * Gets the descriptions of this version
     *
     * @return the description of this version
     */
    @Override
    public String getDescription() {
        return "Provides the version 2.03 scoring logic for codes valid between October 1, 2009 thru December 31, 2009 inclusive.";
    }


}
