/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3211;

import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_B_IF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.v3210.GrouperVersion_v3210;
import java.util.GregorianCalendar;

/**
 * This version is valid between Oct 1, 2011 to Sept 30, 2012
 * 
 * @see com.mmm.cms.homehealth.v3210.GrouperVersion_v3210
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class GrouperVersion_v3211 extends GrouperVersion_v3210  implements
        HomeHealthGrouperIF {

    /**
     * Constructor that sets the effective start and thru date to
     * Oct 1, 2011 thru Dec 31, 2011.
     */
    public GrouperVersion_v3211() {
        super(new GregorianCalendar(2011, 9, 1), new GregorianCalendar(2011, 11, 31),
                null, "V3211");
    }

    /**
     * Get the name of this version
     * @return the name of this version
     */
    @Override
    public String getName() {
        return "HHA PPS Grouper - October 2011 ICD-9-CM codes, " +
                getVersion();
    }

}


