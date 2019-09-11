/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3414;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.AbstractGrouper_2;
import com.mmm.cms.homehealth.hipps.HIPPSCode;
import com.mmm.cms.homehealth.HomeHealthEventNotifier;
import com.mmm.cms.homehealth.PointsScoringEquations;
import com.mmm.cms.homehealth.ScoringResults;
import com.mmm.cms.homehealth.TreatmentAuthorization;
import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;
import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.DiagnosisScoringStatus_EN;
import com.mmm.cms.homehealth.proto.HIPPSCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.proto.ScoringPointsIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.proto.TreatmentAuthorizationIF;
import com.mmm.cms.homehealth.v3413.GrouperDataManager_V3413;
import com.mmm.cms.homehealth.v3413.HomeHealthRecordClinicalValidator_v3413;
import com.mmm.cms.util.ScoringEventFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * This version is valid from January 1, 2014 to September 30, 2014. There is
 * not grace period and no start window. In previous versions, the OASIS-C
 * record was mapped back to the OASIS-B record after validation but prior to
 * scoring. This version removes this mapping and scores directly on the OASIS-C
 * record.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperVersion_v3414 extends AbstractGrouper_2 implements
		HomeHealthGrouperIF {

	HomeHealthEventNotifier eventNotifier;

	/**
	 * Constructor that sets the effective start and thru date to January 1,
	 */
	public GrouperVersion_v3414() {
		this(new GregorianCalendar(2014, 0, 1), // effective January 1, 2014
				new GregorianCalendar(2014, 8, 30), // effective thru September 30, 2014
				null, // effective date window - no window in this version
				"V3414");
	}

	/**
	 * Constructor that sets the effective start, thru, and window date to the
	 * supplied dates. The start and thru dates can not be null but the window
	 * can be.
	 *
	 *
	 * @param start - the effective start date
	 * @param thru - the effective thru date, inclusive
	 * @param window - the start of the overlap window. May be null
	 * @param versionName - 5 character string. Can not be null.
	 */
	public GrouperVersion_v3414(Calendar start, Calendar thru, Calendar window,
			String versionName) {
		super(start, thru, window, versionName);

		eventNotifier = new HomeHealthEventNotifier();
	}

	/**
	 * This copies codes from one record to another without any cloning.
	 *
	 * @param recordDest
	 */
	public void copyCodes(HomeHealthRecordIF recordSrc, HomeHealthRecordIF recordDest) {

		recordDest.setPRIMARY_DIAG_ICD(
				recordSrc.getPRIMARY_DIAG_ICD());
		recordDest.setOTH_DIAG1_ICD(
				recordSrc.getOTH_DIAG1_ICD());
		recordDest.setOTH_DIAG2_ICD(
				recordSrc.getOTH_DIAG2_ICD());
		recordDest.setOTH_DIAG3_ICD(
				recordSrc.getOTH_DIAG3_ICD());
		recordDest.setOTH_DIAG4_ICD(
				recordSrc.getOTH_DIAG4_ICD());
		recordDest.setOTH_DIAG5_ICD(
				recordSrc.getOTH_DIAG5_ICD());

		recordDest.setPMT_DIAG_ICD_A3(
				recordSrc.getPMT_DIAG_ICD_A3());
		recordDest.setPMT_DIAG_ICD_B3(
				recordSrc.getPMT_DIAG_ICD_B3());
		recordDest.setPMT_DIAG_ICD_C3(
				recordSrc.getPMT_DIAG_ICD_C3());
		recordDest.setPMT_DIAG_ICD_D3(
				recordSrc.getPMT_DIAG_ICD_D3());
		recordDest.setPMT_DIAG_ICD_E3(
				recordSrc.getPMT_DIAG_ICD_E3());
		recordDest.setPMT_DIAG_ICD_F3(
				recordSrc.getPMT_DIAG_ICD_F3());

		recordDest.setPMT_DIAG_ICD_A4(
				recordSrc.getPMT_DIAG_ICD_A4());
		recordDest.setPMT_DIAG_ICD_B4(
				recordSrc.getPMT_DIAG_ICD_B4());
		recordDest.setPMT_DIAG_ICD_C4(
				recordSrc.getPMT_DIAG_ICD_C4());
		recordDest.setPMT_DIAG_ICD_D4(
				recordSrc.getPMT_DIAG_ICD_D4());
		recordDest.setPMT_DIAG_ICD_E4(
				recordSrc.getPMT_DIAG_ICD_E4());
		recordDest.setPMT_DIAG_ICD_F4(
				recordSrc.getPMT_DIAG_ICD_F4());
	}

	/**
	 * gets the list of diagnostic Groups associated with the
	 * Clinical/Functional diagnosis
	 *
	 * @return non-null List of Diagnostic Group items. If there was an error
	 * during initialization, the list may be empty
	 */
	@Override
	public List<DiagnosticGroupIF> getDiagnosticGroups() {
		return this.dataManager.getDiagnosticGroups();
	}

	/**
	 * gets the list of diagnostic Groups associated with the Non-Routine
	 * Supplies (NRS) diagnosis
	 *
	 * @return non-null List of Diagnostic Group items. If there was an error
	 * during initialization, the list may be empty
	 */
	@Override
	public List<DiagnosticGroupIF> getDiagnosticGroupsNRS() {
		return this.dataManager.getDiagnosticGroupsNRS();
	}

	/**
	 * gets the Standard services case mix adjustment table
	 *
	 * @return non-null List of CaseMixAdjustment items. If there was an error
	 * during initialization, the list may be empty
	 */
	@Override
	public List<CaseMixAdjustmentItemIF> getCaseMixAdjustments() {
		return this.dataManager.getCaseMixAdjustments();
	}

	/**
	 * Provides the validator for the Clinical portion of the scoring
	 *
	 * @return HomeHealthRecordValidatorIF
	 */
	@Override
	public HomeHealthRecordValidatorIF getClinicalValidator() {
		return new HomeHealthRecordClinicalValidator_v3413(this);
	}

	@Override
	public Class getAcceptableRecordClass() {
		return HomeHealthRecord_C_IF.class;
	}
	
	/**
	 * Provides the list of Clinical/Functional codes associated with this
	 * version and used for scoring.
	 *
	 * @return a non-null List of DiagnosisCodeIF
	 */
	@Override
	public List<DiagnosisCodeIF> getClinicalCodes() {
		return this.dataManager.getClinicalCodes();
	}

	/**
	 * Get the name of this version
	 *
	 * @return the name of this version
	 */
	@Override
	public String getName() {
		return "HHA PPS Grouper - October 2010 ICD-9-CM codes, "
				+ getVersion();
	}

	/**
	 * gets the Non-Routine services case mix adjustment table
	 *
	 * @return non-null List of CaseMixAdjustment items. If there was an error
	 * during initialization, the list may be empty
	 */
	@Override
	public List<CaseMixAdjustmentItemIF> getNRSCaseMixAdjustments() {
		return this.dataManager.getNRSCaseMixAdjustments();
	}

	/**
	 * Provides the validator for the Non-Routine Supplies portion of the
	 * scoring
	 *
	 * @return HomeHealthRecordValidatorIF
	 */
	@Override
	public HomeHealthRecordValidatorIF getNRSValidator() {
		return new HomeHealthRecordClinicalValidator_v3413(this);
	}

	/**
	 * gets a list of Diagnosis Codes for Non-routine Services
	 *
	 * @return
	 */
	@Override
	public List<DiagnosisCodeIF> getNonRoutineCodes() {
		return this.dataManager.getNonRoutineCodes();
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

		this.dataManager = new GrouperDataManager_V3413(this);
		this.dataManager.init(props);

		clinicalModel_1 = new ClinicalFunctional_ScoringModel_v3414(this, dataManager, 1);
		clinicalModel_2 = new ClinicalFunctional_ScoringModel_v3414(this, dataManager, 2);
		clinicalModel_3 = new ClinicalFunctional_ScoringModel_v3414(this, dataManager, 3);
		clinicalModel_4 = new ClinicalFunctional_ScoringModel_v3414(this, dataManager, 4);

		nrsScoringModel = new NRS_ScoringModel_v3414(this, dataManager);
	}

	/*
	 * Determines if the Diagnosis code is valid for this version.
	 *
	 * @return true if the code is not null, and the code's value is found
	 * within this version.
	 */
	@Override
	public boolean isValidDiagnosisCode(HomeHealthRecordIF record,
			DiagnosisCodeIF code) {
		return code == null ? false : dataManager.getDiagnosisCode(code.getCode()) != null;
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
		HomeHealthRecordValidatorIF validator = getClinicalValidator();

		clinicalModel_1.populateCodes(record);
		validator.validate(record);

		return validator;
	}

	@Override
	public  synchronized ScoringResultsIF score(HomeHealthRecordIF record, boolean validateDates, CollectionValidationEditsIF validationEdits, Collection<HomeHealthEventListenerIF> listeners) {
		try {
			if (listeners != null) {
				for (HomeHealthEventListenerIF listener : listeners) {
					addEventListener(listener);
				}
			}
			return score(record, validateDates);
		} finally {
			if (listeners != null) {
				for (HomeHealthEventListenerIF listener : listeners) {
					removeEventListener(listener);
				}
			}
		}
	}

	/**
	 * When scoring a recordOasisC, the data can either come from a OASIS-B or
	 * an OASIS-C source. The OASIS-C source needs to be converted to an OASIS-B
	 * prior to scoring. Then the parent scoring can be called.
	 *
	 * @param recordOasisC
	 * @param validateDates
	 * @return a non-null Scoring Result that contains the scores for clinical /
	 * functional, and for NRS as well as the OASIS treatments and flags.
	 */
	@Override
	public ScoringResultsIF score(HomeHealthRecordIF recordOasisC,
			boolean validateDates) {
		final HIPPSCodeIF hippsCode;
		PointsScoringEquationsIF clinicalScore;
		PointsScoringEquationsIF functionalScore;
		HomeHealthRecordValidatorIF validator;
		HomeHealthRecordValidatorIF nrsValidator;
		int nrsScore;
		ScoringResultsIF scoringResult;
		TreatmentAuthorizationIF treatment;
		ScoringPointsIF scoringPoints;
		int scoreOrder[][];

		ScoringEventFormatter.fireScoringRecord(this.eventNotifier.getEventListenersList(), this, "Pre-scoring: ", recordOasisC);

		// if the recordOasisC's dates need to be validated, then determine
		// that first.
		if (validateDates && !isValidForVersion(recordOasisC)) {
			ScoringEventFormatter.fireScoringGeneral(this.eventNotifier.getEventListenersList(), this, null, ": record not valid for this version");

			// since the recordOasisC is not valid to score for this version,
			// create the empty scoring results
			scoringResult = new ScoringResults(recordOasisC);

		} else if (!(recordOasisC instanceof HomeHealthRecord_C_IF)) {
			// make sure that the recordOasisC can be used as a
			// OasisC recordOasisC - if not then don't score it
			ScoringEventFormatter.fireScoringGeneral(this.eventNotifier.getEventListenersList(), this, null, ": Invalid record format for this version");

			// since the recordOasisC is not valid to score for this version,
			// create the empty scoring results
			scoringResult = new ScoringResults(recordOasisC);
		} else {
			final DiagnosisScoringStatus_EN diagnosisStatus[] = new DiagnosisScoringStatus_EN[18];
			final DiagnosisScoringStatus_EN nrsDiagnosisStatus[] = new DiagnosisScoringStatus_EN[18];

			// initialize the scoring variables
			clinicalScore = new PointsScoringEquations();
			functionalScore = new PointsScoringEquations();

			//----------------------------------------------------
			// populate the code for the Clinical/Functional Model
			// this only needs to be done one for all of the
			// clinical models
			ScoringEventFormatter.fireScoringGeneral(this.eventNotifier.getEventListenersList(), this, null, "populating codes for Clinical/Functional scoring");
			clinicalModel_1.populateCodes(recordOasisC);
			ScoringEventFormatter.fireScoringRecord(this.eventNotifier.getEventListenersList(), this, "Post-populating record: ", recordOasisC);

			// validate the recordOasisC
			ScoringEventFormatter.fireScoringGeneral(this.eventNotifier.getEventListenersList(), this, null, "Validating record");
			validator = getClinicalValidator();
//			Iterator<HomeHealthEventListenerIF> iterator = this.eventNotifier.getEventListenersList().iterator();
//			while (iterator.hasNext()) {
//				validator.addEventListener(iterator.next());
//			}
//			validator.validate(recordOasisC);
			validator.validate(recordOasisC, this.eventNotifier.getEventListenersList());
			ScoringEventFormatter.fireScoringGeneral(this.eventNotifier.getEventListenersList(), this, null, "Validating record - Done");

			// report all non-blank the codes that are not scored for whatever
			DiagnosisCodeIF diagCode;

			for (int idx = 0; idx < diagnosisStatus.length; idx++) {
				diagCode = recordOasisC.getDiagnosisCode(idx);
				diagnosisStatus[idx] = diagCode.isValidCode()
						? diagCode.isValidForScoring() ? DiagnosisScoringStatus_EN.VALID_SCORABLE : DiagnosisScoringStatus_EN.VALID
						: DiagnosisScoringStatus_EN.INVALID;
                // report all non-blank the codes that are not scored for whatever
				// reason
				if (!diagCode.isEmpty() && !diagCode.isValidForScoring()) {
					ScoringEventFormatter.fireValidCodeWarning(this.getEventListenersList(), this, clinicalModel_1, diagCode, idx + 1, diagCode.isValidCode());
				}
			}

			
			// Determine when the code should be scored - not at all
			// pass 1 or pass 2
			scoreOrder = determineScoreOrder(recordOasisC);

			// provide the scoring for each equation
			// the clinical scoring provides two values, one for clinical
			// and one for functional
			// score equation 1
			scoringPoints = clinicalModel_1.score(recordOasisC, validator, scoreOrder);
			clinicalScore.setEarly13AndUnder(scoringPoints.getScores()[0]);
			functionalScore.setEarly13AndUnder(scoringPoints.getScores()[1]);

			// score equation 2
			scoringPoints = clinicalModel_2.score(recordOasisC, validator, scoreOrder);
			clinicalScore.setEarly14Plus(scoringPoints.getScores()[0]);
			functionalScore.setEarly14Plus(scoringPoints.getScores()[1]);

			// score equation 3
			scoringPoints = clinicalModel_3.score(recordOasisC, validator, scoreOrder);
			clinicalScore.setLater13AndUnder(scoringPoints.getScores()[0]);
			functionalScore.setLater13AndUnder(scoringPoints.getScores()[1]);

			// score equation 4
			scoringPoints = clinicalModel_4.score(recordOasisC, validator, scoreOrder);
			clinicalScore.setLater14Plus(scoringPoints.getScores()[0]);
			functionalScore.setLater14Plus(scoringPoints.getScores()[1]);

			//----------------------------------------------------
			// provide the Non-Routine Supplies score
			// populate the codes for the NRS Model
			ScoringEventFormatter.fireScoringGeneral(this.eventNotifier.getEventListenersList(), this, null, "populating codes for Non-Routine Supplies scoring");
			nrsScoringModel.populateCodes(recordOasisC);
			ScoringEventFormatter.fireScoringRecord(this.eventNotifier.getEventListenersList(), this, "Post-populate with NRS Diagnosis: ", recordOasisC);

			// validate the recordOasisC for NRS
			ScoringEventFormatter.fireScoringGeneral(this.eventNotifier.getEventListenersList(), this, null, "Validating NRS record");
			nrsValidator = getNRSValidator();
//			iterator = this.eventNotifier.getEventListenersList().iterator();
//			while (iterator.hasNext()) {
//				nrsValidator.addEventListener(iterator.next());
//			}
//			nrsValidator.validate(recordOasisC);
			nrsValidator.validate(recordOasisC, this.eventNotifier.getEventListenersList());
                        
			ScoringEventFormatter.fireScoringGeneral(this.eventNotifier.getEventListenersList(), this, null, "Validating NRS record - Done");

			for (int idx = 0; idx < 6; idx++) {
				diagCode = recordOasisC.getDiagnosisCode(idx);
				if (!diagCode.isEmpty() && !diagCode.isValidForScoring()) {
					ScoringEventFormatter.fireValidCodeWarning(getEventListenersList(), this, nrsScoringModel, diagCode, idx + 1, diagCode.isValidCode());
				}
			}

			for (int idx = 0; idx < nrsDiagnosisStatus.length; idx++) {
				diagCode = recordOasisC.getDiagnosisCode(idx);
				nrsDiagnosisStatus[idx] = diagCode.isValidCode()
						? diagCode.isValidForScoring() ? DiagnosisScoringStatus_EN.VALID_SCORABLE : DiagnosisScoringStatus_EN.VALID
						: DiagnosisScoringStatus_EN.INVALID;
			}

			// Determine when the code should be scored - not at all
			// pass 1 or pass 2
			scoreOrder = determineScoreOrder(recordOasisC);

			// the NRS model returns only one item
			scoringPoints = nrsScoringModel.score(recordOasisC, nrsValidator, scoreOrder);
			nrsScore = scoringPoints.getScores()[0];

			// create the HIPPS code based on the scoring
			hippsCode = createHippsCode(recordOasisC, validator, clinicalScore, functionalScore, nrsScore);
			if (hippsCode.getCode().trim().isEmpty()) {
				// create a blank Oasis treatment
				treatment = new TreatmentAuthorization(recordOasisC, (HomeHealthRecordValidatorIF) null, null, null);
			} else {
				treatment = new TreatmentAuthorization(recordOasisC, validator, clinicalScore, functionalScore);
			}

			scoringResult = new ScoringResults(hippsCode, getVersion(),
					validator.getDataValidityFlag(), treatment,
					validator, nrsValidator);
			scoringResult.setDiagnosisScoringStatus(diagnosisStatus);
			scoringResult.setNrsDiagnosisScoringStatus(nrsDiagnosisStatus);
			
		}

		return scoringResult;
	}

	@Override
	public void addEventListener(HomeHealthEventListenerIF listener) {
		eventNotifier.addEventListener(listener);
	}

	@Override
	public Iterator<HomeHealthEventListenerIF> getEventListeners() {
		return eventNotifier.getEventListeners();
	}

	@Override
	public List<HomeHealthEventListenerIF> getEventListenersList() {
		return eventNotifier.getEventListenersList();
	}

	@Override
	public void notifyEventListeners(HomeHealthEventIF event) {
		eventNotifier.notifyEventListeners(event);
	}

	@Override
	public void removeEventListener(HomeHealthEventListenerIF listener) {
		eventNotifier.removeEventListener(listener);
	}

	@Override
	public void removeEventListeners() {
		eventNotifier.removeEventListeners();
	}

	@Override
	public int getListenerCount() {
		return eventNotifier.getListenerCount();
	}
}
