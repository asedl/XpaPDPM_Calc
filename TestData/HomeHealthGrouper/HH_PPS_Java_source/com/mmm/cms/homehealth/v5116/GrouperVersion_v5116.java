/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v5116;

import com.mmm.cms.homehealth.hipps.HIPPSCode_2016;
import com.mmm.cms.homehealth.proto.HIPPSCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.v5115.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This version is valid from October 1, 2015 to September 30, 2016 and applies
 * to I-10 codes using the OASIS-C1 data formats; there is no grace period and
 * no start window; the HomeHealthEventNotifierIF methods are present, but do
 * nothing.
 *
 * Changes in logic:
 *
 * 1) since I-10
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperVersion_v5116 extends GrouperVersion_v5115 {

    /**
     * Used to pass grouper default to parent grouper
     *
     * @param effectiveStart
     * @param effectiveThru
     * @param effectiveWindow
     * @param versionName
     */
    public GrouperVersion_v5116(Calendar effectiveStart, Calendar effectiveThru, Calendar effectiveWindow, String versionName) {
        super(effectiveStart, effectiveThru, effectiveWindow, versionName);
    }

    /**
     * Constructor that sets the effective start and thru date
     */
    public GrouperVersion_v5116() {
        super(new GregorianCalendar(2016, 0, 1), // effective January 1, 2016
                new GregorianCalendar(2016, 8, 30), // effective thru September 30, 2016
                null, // effective date window - no window in this version
                "V5116");
    }

    /**
     * Provides 2016 Grouping step, timing and clinical/functional severity
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
        return new HIPPSCode_2016(record, validator, clinicalScore, functionalScore, nrsScore);
    }

    /**
     * Provides 2016 blank Hipps Code
     *
     * @return
     */
    @Override
    public HIPPSCodeIF createHippsCode() {
        return new HIPPSCode_2016();
    }

}
