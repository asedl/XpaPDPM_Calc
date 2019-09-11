/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v5216;

import com.mmm.cms.homehealth.v5115.ClinicalFunctional_ScoringModel_v5115;
import com.mmm.cms.homehealth.v5115.NRS_ScoringModel_v5115;
import com.mmm.cms.homehealth.v5116.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * This version is valid from October 1, 2016 to December 31, 2016 and applies
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
public class GrouperVersion_v5216 extends GrouperVersion_v5116 {

   /**
     * Used to pass grouper default to parent grouper
     * 
     * @param effectiveStart
     * @param effectiveThru
     * @param effectiveWindow
     * @param versionName 
     */
    public GrouperVersion_v5216(Calendar effectiveStart, Calendar effectiveThru, Calendar effectiveWindow, String versionName) {
        super(effectiveStart, effectiveThru, effectiveWindow, versionName);
    }
    
    /**
     * Constructor that sets the effective start and thru date
     */
    public GrouperVersion_v5216() {
        super(new GregorianCalendar(2016, 9, 1), // effective October 1, 2016
                new GregorianCalendar(2016, 11, 31), // effective thru December 31, 2016
                null, // effective date window - no window in this version
                "V5216");
    }

    /**
     * Sets up this version by loading the related Diagnosis code / Group data,
     * and initializing the scoring models: 4 clinical/functional models (one
     * for each equation), and one Non-Routine Supplies model
     *
     * @param props
     * @throws java.lang.Exception
     */
    @Override
    public void init(Properties props) throws Exception {

        dataManager = new GrouperDataManager_v5216(this);
        dataManager.init(props);

        clinicalModel_1 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 1);
        clinicalModel_2 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 2);
        clinicalModel_3 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 3);
        clinicalModel_4 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 4);

        nrsScoringModel = new NRS_ScoringModel_v5115(this, dataManager);
    }
    
    
}
