/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3514;

import com.mmm.cms.homehealth.v3414.*;
import java.util.GregorianCalendar;

/**
 * This version is valid from October 1, 2014 to December 31, 2014. There is
 * not grace period and no start window.  
 *
 * @author 3M Health Information Systems
 */
public class GrouperVersion_v3514 extends GrouperVersion_v3414 {

	public GrouperVersion_v3514() {
        super(new GregorianCalendar(2014, 9, 1), // effective October 1, 2014
                new GregorianCalendar(2014, 11, 31), // effective thru December 31, 2014
                null, // effective date window - no window in this version
                "V3514");
	}
}
