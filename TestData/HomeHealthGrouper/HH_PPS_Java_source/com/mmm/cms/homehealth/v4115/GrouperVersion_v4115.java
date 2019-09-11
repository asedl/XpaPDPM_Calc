/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v4115;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisValidationEditIF;
import com.mmm.cms.homehealth.AbstractGrouper_2;
import com.mmm.cms.homehealth.hipps.HIPPSCode_2015;
import com.mmm.cms.homehealth.PointsScoringEquations;
import com.mmm.cms.homehealth.ScoringEventCollector;
import com.mmm.cms.homehealth.ScoringResults;
import com.mmm.cms.homehealth.TreatmentAuthorization_2015;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.DiagnosisScoringStatus_EN;
import com.mmm.cms.homehealth.proto.edits.HH_PPS_OasisC1EditsEN;
import com.mmm.cms.homehealth.proto.HIPPSCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C1_IF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.proto.ScoringPointsIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.proto.TreatmentAuthorizationIF;
import com.mmm.cms.util.ScoringEventFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * This version is valid from January 1, 2015 to September 30, 2015 and applies
 * to I-9 codes using the OASIS-C1 data formats; there is no grace period and no
 * start window; the HomeHealthEventNotifierIF methods are present, but do
 * nothing.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperVersion_v4115 extends AbstractGrouper_2 implements HomeHealthGrouperIF {

    /**
     * Constructor that sets the effective start and thru date
     */
    public GrouperVersion_v4115() {
        super(new GregorianCalendar(2015, 0, 1), // effective January 1, 2015
                new GregorianCalendar(2015, 8, 30), // effective thru September 30, 2015
                null, // effective date window - no window in this version
                "V4115");
    }

    /**
     * Allows extended classes to construct with their own date ranges.
     *
     * @param effectiveStart
     * @param effectiveThru
     * @param effectiveWindow
     * @param versionName
     */
    protected GrouperVersion_v4115(Calendar effectiveStart, Calendar effectiveThru,
            Calendar effectiveWindow, String versionName) {
        super(effectiveStart, effectiveThru, effectiveWindow, versionName);
    }

    @Override
    public Class getAcceptableRecordClass() {
        return HomeHealthRecord_C1_IF.class;
    }

    /**
     * Provides the validator for the Clinical portion of the scoring
     *
     * @return HomeHealthRecordValidatorIF
     */
    @Override
    public HomeHealthRecordValidatorIF getClinicalValidator() {
        return new HomeHealthRecordValidator_v4115(this);
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
    public HomeHealthRecordValidatorIF getClinicalValidator(CollectionValidationEditsIF validationEdits) {
        return new HomeHealthRecordValidator_v4115(this, validationEdits);
    }

    /**
     * Provides the validator for the Non-Routine Supplies portion of the
     * scoring
     *
     * @return HomeHealthRecordValidatorIF
     */
    @Override
    public HomeHealthRecordValidatorIF getNRSValidator() {
        return new HomeHealthRecordValidator_v4115(this);
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
    public HomeHealthRecordValidatorIF getNRSValidator(CollectionValidationEditsIF validationEdits) {
        return new HomeHealthRecordValidator_v4115(this, validationEdits);
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

        dataManager = new GrouperDataManager_V4115(this);
        dataManager.init(props);

        clinicalModel_1 = new ClinicalFunctional_ScoringModel_v4115(this, dataManager, 1);
        clinicalModel_2 = new ClinicalFunctional_ScoringModel_v4115(this, dataManager, 2);
        clinicalModel_3 = new ClinicalFunctional_ScoringModel_v4115(this, dataManager, 3);
        clinicalModel_4 = new ClinicalFunctional_ScoringModel_v4115(this, dataManager, 4);

        nrsScoringModel = new NRS_ScoringModel_v4115(this, dataManager);
    }

    /**
     * This method is not very efficient for scoring a record, since it
     * populates the codes into the record each time, prior to validating them.
     * However, it should give sufficient information about the which codes were
     * used for scoring.
     *
     * @param record
     * @return HomeHealthRecordValidatorIF, this value will never be null.
     */
    @Override
    public HomeHealthRecordValidatorIF populateValidateClinicalCodes(
            HomeHealthRecordIF record) {
        final HomeHealthRecordValidatorIF validator = getClinicalValidator();

        clinicalModel_1.populateCodes(record);
        validator.validate(record);

        return validator;
    }

    @Override
    public ScoringResultsIF score(HomeHealthRecordIF recordOasisC,
            boolean validateDates, CollectionValidationEditsIF validationEdits,
            Collection<HomeHealthEventListenerIF> listeners) {

        CollectionValidationEditsIF validationEdits_Results;
        HomeHealthRecordValidatorIF validator;
        HomeHealthRecordValidatorIF nrsValidator;
        int nrsScore;
        ScoringPointsIF scoringPoints;
        int scoreOrder[][];
        ScoringResultsIF scoringResult;
        boolean eventCollectorFound;
        final boolean listenersCreated;
        ScoringEventCollector scoringEventCollector = null;
        DiagnosisCodeIF diagCode;
        List<OasisValidationEditIF> editList;

        /*
         * If there are no listeners or they do not include the scoring event 
         * collector, then either create it or add it.
         */
        if (listeners == null) {
            listenersCreated = true;
            listeners = new ArrayList();
        } else {
            listenersCreated = false;
            for (HomeHealthEventListenerIF listener : listeners) {
                if (listener instanceof ScoringEventCollector) {
                    scoringEventCollector = (ScoringEventCollector) listener;
                    break;
                }
            }
        }

        if (scoringEventCollector == null) {
            scoringEventCollector = new ScoringEventCollector();
            listeners.add(scoringEventCollector);
            eventCollectorFound = false;
        } else {
            eventCollectorFound = true;
        }

        ScoringEventFormatter.fireScoringRecord(listeners, this, "Pre-scoring: ", recordOasisC);

		// if the recordOasisC's dates need to be validated, then determine
        // that first.
        if (validateDates && !isValidForVersion(recordOasisC)) {
            ScoringEventFormatter.fireScoringGeneral(listeners, this, null, ": record not valid for this version");

			// since the recordOasisC is not valid to score for this version,
            // create the empty scoring results
            scoringResult = new ScoringResults(recordOasisC);

        } else if (recordOasisC instanceof HomeHealthRecord_C_IF) {
            HIPPSCodeIF hippsCode;
            TreatmentAuthorizationIF treatment;
            final DiagnosisScoringStatus_EN diagnosisStatus[] = new DiagnosisScoringStatus_EN[6];
            final DiagnosisScoringStatus_EN nrsDiagnosisStatus[] = new DiagnosisScoringStatus_EN[6];

            // initialize the scoring variables
            final PointsScoringEquationsIF clinicalScore = new PointsScoringEquations();
            final PointsScoringEquationsIF functionalScore = new PointsScoringEquations();

			//----------------------------------------------------
            // populate the code for the Clinical/Functional Model
            // this only needs to be done one for all of the
            // clinical models
            ScoringEventFormatter.fireScoringGeneral(listeners, this, null, "populating codes for Clinical/Functional scoring");
            clinicalModel_1.populateCodes(recordOasisC);
            ScoringEventFormatter.fireScoringRecord(listeners, this, "Post-populating record: ", recordOasisC);

            // validate the recordOasisC
            ScoringEventFormatter.fireScoringGeneral(listeners, this, null, "Validating record");

            validator = getClinicalValidator(validationEdits);
            validationEdits_Results = validator.validate(recordOasisC, listeners);

            ScoringEventFormatter.fireScoringGeneral(listeners, this, null, "Validating record - Done");

            reportNonScoringDiagnosis(recordOasisC, diagnosisStatus, clinicalModel_1, listeners);

            /*
             * If the primary code is a manifestation, do not score the record.
             * So, check for the Mani in the PDX edit
             */
            editList = validationEdits_Results.getEdits(HH_PPS_OasisC1EditsEN.EDIT_70020);
            if (editList == null || editList.isEmpty()) {
				// Determine when the code should be scored - not at all
                // pass 1 or pass 2
                scoreOrder = determineScoreOrder(recordOasisC);

                reportPrimaryCodeSelection(listeners, recordOasisC, scoreOrder);

				// provide the scoring for each equation
                // the clinical scoring provides two values, one for clinical
                // and one for functional
                // score equation 1
                scoringPoints = clinicalModel_1.score(recordOasisC, validator, scoreOrder, listeners);
                clinicalScore.setEarly13AndUnder(scoringPoints.getScores()[0]);
                functionalScore.setEarly13AndUnder(scoringPoints.getScores()[1]);

                // score equation 2
                scoringPoints = clinicalModel_2.score(recordOasisC, validator, scoreOrder, listeners);
                clinicalScore.setEarly14Plus(scoringPoints.getScores()[0]);
                functionalScore.setEarly14Plus(scoringPoints.getScores()[1]);

                // score equation 3
                scoringPoints = clinicalModel_3.score(recordOasisC, validator, scoreOrder, listeners);
                clinicalScore.setLater13AndUnder(scoringPoints.getScores()[0]);
                functionalScore.setLater13AndUnder(scoringPoints.getScores()[1]);

                // score equation 4
                scoringPoints = clinicalModel_4.score(recordOasisC, validator, scoreOrder, listeners);
                clinicalScore.setLater14Plus(scoringPoints.getScores()[0]);
                functionalScore.setLater14Plus(scoringPoints.getScores()[1]);

				//----------------------------------------------------
                // provide the Non-Routine Supplies score
                // populate the codes for the NRS Model
                ScoringEventFormatter.fireScoringGeneral(listeners, this, null, "populating codes for Non-Routine Supplies scoring");
                nrsScoringModel.populateCodes(recordOasisC);
                ScoringEventFormatter.fireScoringRecord(listeners, this, "Post-populate with NRS Diagnosis: ", recordOasisC);

                // validate the recordOasisC for NRS
                ScoringEventFormatter.fireScoringGeneral(listeners, this, null, "Validating NRS record");

                /*
                 * Since the validation is the same for Clinical as it is for NRS,
                 * just reuse the Clinical edits - no need to re-validate
                 */
                if (validationEdits == null) {
                    nrsValidator = getNRSValidator();
                    nrsValidator.validate(recordOasisC, listeners);
                } else {
                    nrsValidator = getNRSValidator(((HomeHealthRecordValidator_v4115) validator).getEdits());
                }

                ScoringEventFormatter.fireScoringGeneral(listeners, this, null, "Validating NRS record - Done");

                reportNonScoringDiagnosis(recordOasisC, nrsDiagnosisStatus, nrsScoringModel, listeners);

				// Determine when the code should be scored - not at all
                // pass 1 or pass 2
                scoreOrder = determineScoreOrder(recordOasisC);
                reportPrimaryCodeSelection(listeners, recordOasisC, scoreOrder);

                // the NRS model returns only one item
                scoringPoints = nrsScoringModel.score(recordOasisC, nrsValidator, scoreOrder, listeners);
                nrsScore = scoringPoints.getScores()[0];

                // create the HIPPS code based on the scoring
                hippsCode = createHippsCode(recordOasisC, validator, clinicalScore, functionalScore, nrsScore);
            } else {
                // default these to blanks
                hippsCode = createHippsCode();
            }

            if (hippsCode.getCode().trim().isEmpty()) {
                // create a blank Oasis treatment
                treatment = new TreatmentAuthorization_2015(recordOasisC);
            } else {
                treatment = new TreatmentAuthorization_2015(recordOasisC, validator, clinicalScore, functionalScore);
            }

            /*
             * though the validation edits may come from the parameter, then are
             * set in the validators (both have the same edits)
             */
            scoringResult = new ScoringResults(
                    ((HomeHealthRecordValidator_v4115) validator).getEdits(), // validationEdits,
                    scoringEventCollector.getEvents(),
                    hippsCode,
                    getVersion(),
                    validator.getDataValidityFlag(),
                    treatment,
                    diagnosisStatus,
                    nrsDiagnosisStatus);

        } else {
			// make sure that the recordOasisC can be used as a
            // OasisC recordOasisC - if not then don't score it
            ScoringEventFormatter.fireScoringGeneral(listeners, this, null, ": Invalid record format for this version");

			// since the recordOasisC is not valid to score for this version,
            // create the empty scoring results
            scoringResult = new ScoringResults(recordOasisC);
        }

        // remove the scoring event collector
        if (!listenersCreated && !eventCollectorFound) {
            listeners.remove(scoringEventCollector);
        }

        return scoringResult;
    }

    /**
     * Not used in this version - this is not thread safe
     *
     * @param listener
     * @deprecated
     */
    @Override
    public void addEventListener(HomeHealthEventListenerIF listener) {
        // not used in this version
    }

    /**
     * Not used in this version - this is not thread safe
     *
     * @return
     * @deprecated
     */
    @Override
    public Iterator<HomeHealthEventListenerIF> getEventListeners() {
        return null;
    }

    /**
     * Not used in this version - this is not thread safe
     *
     * @return
     * @deprecated
     */
    @Override
    public List<HomeHealthEventListenerIF> getEventListenersList() {
        return null;
    }

    /**
     * Not used in this version - this is not thread safe
     *
     * @param event
     * @deprecated
     */
    @Override
    public void notifyEventListeners(HomeHealthEventIF event) {
        // not used in this version
    }

    /**
     * Not used in this version - this is not thread safe
     *
     * @deprecated
     */
    @Override
    public void removeEventListeners() {
        // not used in this version
    }

    /**
     * Not used in this version - this is not thread safe
     *
     * @param listener
     * @deprecated
     */
    @Override
    public void removeEventListener(HomeHealthEventListenerIF listener) {
        // not used in this version
    }

    /**
     * Not used in this version - this is not thread safe
     *
     * @return
     * @deprecated
     */
    @Override
    public int getListenerCount() {
        return 0;
    }

    public void reportPrimaryCodeSelection(Collection<HomeHealthEventListenerIF> listeners, HomeHealthRecordIF recordOasisC, int scoreOrder[][]) {
        int idx;

        idx = recordOasisC.getPRIMARY_DIAG_ICD().isPrimaryAwardableCode() ? 1 : 0;

        if (scoreOrder[idx][0] > 0) {
            ScoringEventFormatter.fireScoringGeneral(listeners, this, null,
                    "Primary Code identified for Scoring: ", recordOasisC.getDiagnosisCode(idx).getCode(),
                    " in position ", Integer.toString(idx + 1));
        }
        if (scoreOrder[++idx][0] == 2) {
            ScoringEventFormatter.fireScoringGeneral(listeners, this, null,
                    "Primary Code identified for Scoring: ", recordOasisC.getDiagnosisCode(idx).getCode(),
                    " in position ", Integer.toString(idx + 1));
        }
    }

    @Override
    public HIPPSCodeIF createHippsCode() {
        return new HIPPSCode_2015();
    }

    @Override
    public HIPPSCodeIF createHippsCode(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, PointsScoringEquationsIF clinicalScore, PointsScoringEquationsIF functionalScore, int nrsScore) {
        return new HIPPSCode_2015(record, validator, clinicalScore, functionalScore, nrsScore); //To change body of generated methods, choose Tools | Templates.
    }

}
