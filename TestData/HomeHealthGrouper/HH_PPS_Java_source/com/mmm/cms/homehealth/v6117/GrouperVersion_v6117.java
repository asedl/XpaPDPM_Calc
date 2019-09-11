/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v6117;

import com.mmm.cms.homehealth.hipps.HIPPSCode_2017;
import com.mmm.cms.homehealth.proto.HIPPSCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C2_IF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.v5115.ClinicalFunctional_ScoringModel_v5115;
import com.mmm.cms.homehealth.v5115.NRS_ScoringModel_v5115;
import com.mmm.cms.homehealth.v5216.*;
import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * This version is valid from Jan 1, 2017 to September 30, 2017 and applies to
 * I-10 codes using the OASIS-C2 data formats; there is no grace period and no
 * start window; the HomeHealthEventNotifierIF methods are present, but do
 * nothing.
 *
 * Changes in logic:
 *
 * 1) readme.txt
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperVersion_v6117 extends GrouperVersion_v5216 {

    /**
     * Used to pass grouper default to parent grouper
     *
     * @param effectiveStart
     * @param effectiveThru
     * @param effectiveWindow
     * @param versionName
     */
    public GrouperVersion_v6117(Calendar effectiveStart, Calendar effectiveThru, Calendar effectiveWindow, String versionName) {
        super(effectiveStart, effectiveThru, effectiveWindow, versionName);
    }

    /**
     * Constructor that sets the effective start and thru date
     */
    public GrouperVersion_v6117() {
        super(new GregorianCalendar(2017, 0, 1), // effective January 1, 2017
                new GregorianCalendar(2017, 8, 30), // effective thru September 30, 2017
                null, // effective date window - no window in this version
                "V6117");
    }

    /**
     * Provides 2017 Grouping step, timing and clinical/functional severity
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
        return new HIPPSCode_2017(record, validator, clinicalScore, functionalScore, nrsScore);
    }

    /**
     * Provides 2017 blank Hipps Code
     *
     * @return
     */
    @Override
    public HIPPSCodeIF createHippsCode() {
        return new HIPPSCode_2017();
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

        dataManager = new GrouperDataManager_v6117(this);
        dataManager.init(props);

        clinicalModel_1 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 1);
        clinicalModel_2 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 2);
        clinicalModel_3 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 3);
        clinicalModel_4 = new ClinicalFunctional_ScoringModel_v5115(this, dataManager, 4);

        nrsScoringModel = new NRS_ScoringModel_v5115(this, dataManager);
    }

    @Override
    public Class getAcceptableRecordClass() {
        return HomeHealthRecord_C2_IF.class;
    }

    /**
     * Provides the validator for the Clinical portion of the scoring
     *
     * @return HomeHealthRecordValidatorIF
     */
    @Override
    public HomeHealthRecordValidatorIF getClinicalValidator() {
        return new HomeHealthRecordValidator_v6117(this);
    }

    /**
     * Provides the validator for the Clinical portion of the scoring using the
     * provided validation edits. If the validation edits are non-null this will
     * effectively bypass the internal validation.
     *
     * @return HomeHealthRecordValidatorIF
     * @param validationEdits - this will take the supplied edits and add them
     * to the new validator
     */
    @Override
    public HomeHealthRecordValidatorIF getClinicalValidator(CollectionValidationEditsIF validationEdits) {
        return new HomeHealthRecordValidator_v6117(this, validationEdits);
    }

    /**
     * Provides the validator for the Non-Routine Supplies portion of the
     * scoring
     *
     * @return HomeHealthRecordValidatorIF
     */
    @Override
    public HomeHealthRecordValidatorIF getNRSValidator() {
        return new HomeHealthRecordValidator_v6117(this);
    }

    /**
     * Provides the validator for the Non-Routine Supplies portion of the
     * scoring using the provided validation edits. If the validation edits are
     * non-null this will effectively bypass the internal validation.
     *
     * @return HomeHealthRecordValidatorIF
     * @param validationEdits - this will take the supplied edits and add them
     * to the new validator
     */
    @Override
    public HomeHealthRecordValidatorIF getNRSValidator(CollectionValidationEditsIF validationEdits) {
        return new HomeHealthRecordValidator_v6117(this, validationEdits);
    }

    
    
}
