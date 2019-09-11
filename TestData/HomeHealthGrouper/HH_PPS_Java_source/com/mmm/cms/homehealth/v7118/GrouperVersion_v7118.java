/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v7118;

import com.mmm.cms.homehealth.hipps.HIPPSCode_2018;
import com.mmm.cms.homehealth.proto.HIPPSCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.v6217.*;
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
public class GrouperVersion_v7118 extends GrouperVersion_v6217 {

   /**
     * Used to pass grouper default to parent grouper
     * 
     * @param effectiveStart
     * @param effectiveThru
     * @param effectiveWindow
     * @param versionName 
     */
    public GrouperVersion_v7118(Calendar effectiveStart, Calendar effectiveThru, Calendar effectiveWindow, String versionName) {
        super(effectiveStart, effectiveThru, effectiveWindow, versionName);
    }
    
    /**
     * Constructor that sets the effective start and thru date
     */
    public GrouperVersion_v7118() {
        super(new GregorianCalendar(2018, 0, 1), // effective January 1, 2018
                new GregorianCalendar(2018, 8, 30), // effective thru September 30, 2018
                null, // effective date window - no window in this version
                "V7118");
    }
    
    /**
     * Provides 2018 Grouping step, timing and clinical/functional severity
     * Hipps Code
     *
     * @param record
     * @param validator
     * @param clinicalScore
     * @param functionalScore
     * @param nrsScore
     * @return
     */
    @Override
    public HIPPSCodeIF createHippsCode(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator,
            PointsScoringEquationsIF clinicalScore, PointsScoringEquationsIF functionalScore, int nrsScore) {
        return new HIPPSCode_2018(record, validator, clinicalScore, functionalScore, nrsScore);
    }

    /**
     * Provides 2018 blank Hipps Code
     *
     * @return
     */
    @Override
    public HIPPSCodeIF createHippsCode() {
        return new HIPPSCode_2018();
    }

}
