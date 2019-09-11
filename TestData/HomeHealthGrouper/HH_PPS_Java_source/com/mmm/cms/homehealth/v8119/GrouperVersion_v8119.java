/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v8119;

import com.mmm.cms.homehealth.hipps.HIPPSCode_2019;
import com.mmm.cms.homehealth.proto.HIPPSCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_D_IF;
import com.mmm.cms.homehealth.v7218.GrouperVersion_v7218;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

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
public class GrouperVersion_v8119 extends GrouperVersion_v7218 {

   /**
     * Used to pass grouper default to parent grouper
     * 
     * @param effectiveStart
     * @param effectiveThru
     * @param effectiveWindow
     * @param versionName 
     */
    public GrouperVersion_v8119(Calendar effectiveStart, Calendar effectiveThru, Calendar effectiveWindow, String versionName) {
        super(effectiveStart, effectiveThru, effectiveWindow, versionName);
    }
    
    /**
     * Constructor that sets the effective start and thru date
     */
    public GrouperVersion_v8119() {
        super(new GregorianCalendar(2019, 0, 1), // effective January 1, 2019
                new GregorianCalendar(2019, 8, 30), // effective thru September 30, 2019
                null, // effective date window - no window in this version
                "V8119");
    }
    
    /**
     * Provides 2019 Grouping step, timing and clinical/functional severity
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
        return new HIPPSCode_2019(record, validator, clinicalScore, functionalScore, nrsScore);
    }

    /**
     * Provides 2019 blank Hipps Code
     *
     * @return
     */
    @Override
    public HIPPSCodeIF createHippsCode() {
        return new HIPPSCode_2019();
    }

    @Override
    public Class getAcceptableRecordClass() {
        return HomeHealthRecord_D_IF.class;
    }

    @Override
    public void init(Properties props) throws Exception {
        super.init(props);
        nrsScoringModel = new NRS_ScoringModel_v8119(this, dataManager);
    }
    
}
