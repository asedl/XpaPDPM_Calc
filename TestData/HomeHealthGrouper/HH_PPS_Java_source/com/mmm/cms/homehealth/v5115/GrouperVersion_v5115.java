/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v5115;

import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.v4115.GrouperVersion_v4115;
import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Properties;

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
public class GrouperVersion_v5115 extends GrouperVersion_v4115 {

    /**
     * Used to pass grouper default to parent grouper
     * 
     * @param effectiveStart
     * @param effectiveThru
     * @param effectiveWindow
     * @param versionName 
     */
    public GrouperVersion_v5115(Calendar effectiveStart, Calendar effectiveThru, Calendar effectiveWindow, String versionName) {
        super(effectiveStart, effectiveThru, effectiveWindow, versionName);
    }

    /**
     * Constructor that sets the effective start and thru date
     */
    public GrouperVersion_v5115() {
        super(new GregorianCalendar(2015, 9, 1), // effective October 1, 2015
                new GregorianCalendar(2015, 11, 31), // effective thru Dec 31, 2015
                null, // effective date window - no window in this version
                "V5115");
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

        dataManager = new GrouperDataManager_v5115(this);
        dataManager.init(props);

        clinicalModel_1 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 1);
        clinicalModel_2 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 2);
        clinicalModel_3 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 3);
        clinicalModel_4 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 4);

        nrsScoringModel = new NRS_ScoringModel_v5115(this, dataManager);
    }

    @Override
    public ScoringResultsIF score(HomeHealthRecordIF recordOasisC, 
            boolean validateDates, CollectionValidationEditsIF validationEdits, 
            Collection<HomeHealthEventListenerIF> listeners) {
        
        return super.score(recordOasisC, validateDates, validationEdits, listeners);
    }

}
