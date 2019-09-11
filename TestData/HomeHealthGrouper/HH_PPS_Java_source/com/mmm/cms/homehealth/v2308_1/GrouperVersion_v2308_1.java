/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v2308_1;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.GrouperDataManager;
import com.mmm.cms.homehealth.hipps.HIPPSCode;
import com.mmm.cms.homehealth.HomeHealthEventNotifier;
import com.mmm.cms.homehealth.PointsScoringEquations;
import com.mmm.cms.homehealth.ScoringResults;
import com.mmm.cms.homehealth.TreatmentAuthorization;
import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_B_IF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C1_IF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.proto.ScoringPointsIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.proto.TreatmentAuthorizationIF;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ScoringEventFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

/**
 * This class uses the 2.03 version scoring logic by extending the
 * GrouperVersion 2.03 class, but encapsulates the Diagnosis Code set for valid
 * codes between January 1, 2008 and September 30, 2008, inclusive.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperVersion_v2308_1 extends HomeHealthEventNotifier implements
		HomeHealthGrouperIF {

	/**
	 * the effective start date for this version
	 */
	private Calendar effectiveDateStart;
	/**
	 * the overlap window date for this version
	 */
	private Calendar effectiveDateStartWindow;
	/**
	 * the effective through date for this version
	 */
	private Calendar effectiveDateThru;
	/**
	 * Reference to the Data manager
	 */
	private GrouperDataManager grouperDataManager;
	/**
	 * The NRS Scoring module
	 */
	protected NRS_ScoringModel_v2308 nrsScoringModel;
	/**
	 * The Clinical Scoring module, equation 1
	 */
	protected ClinicalFunctional_ScoringModel_v2308 clinicalModel_1;
	/**
	 * The Clinical Scoring module, equation 2
	 */
	protected ClinicalFunctional_ScoringModel_v2308 clinicalModel_2;
	/**
	 * The Clinical Scoring module, equation 3
	 */
	protected ClinicalFunctional_ScoringModel_v2308 clinicalModel_3;
	/**
	 * The Clinical Scoring module, equation 4
	 */
	protected ClinicalFunctional_ScoringModel_v2308 clinicalModel_4;
	protected String version;

	/**
	 * Constructor that sets the effective start and thru date to January 1,
	 */
	public GrouperVersion_v2308_1() {

		this(new GregorianCalendar(2008, 0, 1), // effective January 1, 2008
				new GregorianCalendar(2008, 8, 30), // effective date window December 27, 2007 which is prior to actual
				// effective date
				new GregorianCalendar(2007, 11, 27));  // effective thru September 30, 2008
	}

	/**
	 * Constructor that sets the effective start, thru, and window date to the
	 * supplied dates. The start and thru dates can not be null but the window
	 * can be.
	 *
	 * @param start
	 * @param thru
	 * @param window
	 */
	public GrouperVersion_v2308_1(Calendar start, Calendar thru, Calendar window) {

		if (start == null || thru == null) {
			throw new IllegalArgumentException("Neither the start date or the thru date can be null");
		}

		this.effectiveDateStart = start;
		this.effectiveDateThru = thru;
		this.effectiveDateStartWindow = window == null ? start : window;

		this.version = "V2308";
	}

	/**
	 * Returns a non-null double array of int[6][3]. The values in the array
	 * elements are as follows:
	 * <ul>
	 * <li>0 - not scored</li>
	 * <li>1 - score during first round</li>
	 * <li>2 - score during second round</li>
	 * </ul>
	 *
	 * Stand alone etiology codes are always scored first, then the
	 * manifestation/ etiology pair codes are scored second. Codes that are not
	 * valid for scoring are not evaluated and there for entry in the array will
	 * be 0
	 *
	 * @param record
	 * @return
	 */
	@Override
	public int[][] determineScoreOrder(HomeHealthRecordIF record) {
		DiagnosisCodeIF diagCode;
		DiagnosisCodeIF diagCodeOptional;
		int scoreOrder[][] = new int[6][3];

		// loop through the column 2 codes
		for (int diagIdx = 0; diagIdx < 6; diagIdx++) {
			diagCode = record.getDiagnosisCode(diagIdx);

			// for optional payment code go across the row to columns 3 & 4
			if (diagCode.isOptionalPaymentCode()) {
				// determine if the 4 column is an etiology
				diagCodeOptional = record.getOptionalDiagnosisCode4(diagIdx);
				if (diagCodeOptional.isValidForScoring()) {
					// since this a secondary code that is valid for
					// scoring, then the previous code must its etiology
					// and so both are scored second
					scoreOrder[diagIdx][2] = 2;
					scoreOrder[diagIdx][1] = 2;
				} else {
					// determine if the 3rd column is available for scoring
					diagCodeOptional = record.getOptionalDiagnosisCode3(diagIdx);
					if (diagCodeOptional.isValidForScoring()) {
						scoreOrder[diagIdx][1] = 1;
					}
				}
			} else if (diagCode.isValidForScoring()) {
				// if the current code code is a secondary only, determine
				// whether it earns points or the etiology earns points
				if (diagCode.isSecondaryOnly()) {
					// since this a secondary code that is valid for
					// scoring, then the previous code must its etiology
					// and so both are scored second
					scoreOrder[diagIdx][0] = 2;
					scoreOrder[diagIdx - 1][0] = 2;
				} else {
					// this is a etiology so, score it on the first run
					scoreOrder[diagIdx][0] = 1;
				}
			}
		}

		return scoreOrder;
	}

	/**
	 * gets the list of diagnostic Groups associated with the standard services
	 * diagnosis
	 *
	 * @return non-null List of Diagnostic Groups items. If there was an error
	 * during initialization, the list may be empty
	 */
	@Override
	public List<DiagnosticGroupIF> getDiagnosticGroups() {
		return this.grouperDataManager.getDiagnosticGroups();
	}

	@Override
	public List<DiagnosticGroupIF> getDiagnosticGroupsNRS() {
		return this.grouperDataManager.getDiagnosticGroupsNRS();
	}

	/**
	 * Get the name of this version
	 *
	 * @return the name of this version
	 */
	@Override
	public String getName() {
		return "HHA PPS Grouper - Pre October 2008 ICD-9-CM codes, "
				+ getVersion();
	}

	/**
	 * Empty method - you can reset the name
	 *
	 * @param name
	 */
	@Override
	public void setName(String name) {
		// you really can't set the name
	}

	/**
	 * gets the version identifier
	 *
	 * @return
	 */
	@Override
	public String getVersion() {
		return this.version;
	}

	/**
	 * gets the effective start date
	 *
	 * @return
	 */
	@Override
	public Calendar getEffectiveDateStart() {
		return effectiveDateStart;
	}

	/**
	 * gets the effective through date
	 *
	 * @return
	 */
	@Override
	public Calendar getEffectiveDateThru() {
		return effectiveDateThru;
	}

	/**
	 * Validate the record.
	 *
	 * The record must be within the effect start and the effective thru dates,
	 * and the assessment reason must be appropriate for the completed dates
	 *
	 * According to the Readme203.pdf, Source of Data, item 2, page 6: The
	 * Therapy threshold item (M0825 & M0826) is NOT equal to NA. This means
	 * that the cases for which the HIPPS codes is not needed will not be
	 * classified. (This would include, for example, assessments for
	 * non-Medicare/non-Medicaid patients, or Medicare assessments that are not
	 * the basis for casemix classifications for the Medicare episode.) It also
	 * means that the Medicare assessments with M0825 or M0826 coded NA in error
	 * will not be classified. Records with M0825 or M0826 left blank WILL be
	 * classified, with that item treated as invalid data.
	 *
	 *
	 * Specification for Version 02.03: Transition Issues
	 *
	 * The Grouper Version 02.n applies to Medicare home health payment episodes
	 * starting January 1, 2008, or later. Since episode start date is not
	 * recorded on the OASIS, the effective date of Grouper 02.03 is based on
	 * assessment completion date (M0090_INFO_COMPLETED_DATE). If reason for
	 * assessment (M0100_ASSMT_REASON) = 01 or 03 (start/resumption of care),
	 * Grouper 02.03 will be effective for assessments with
	 * M0090_INFO_COMPLETED_DATE on or after 2008-01-01.
	 *
	 * To allow for completion of recertification assessments during the 5-day
	 * window before the next episode begins, if reason for assessment
	 * (M0100_ASSMT_REASON) = 04 or 05 (recertification/other followup), Grouper
	 * 02.03 will be effective for assessments with M0090_INFO_COMPLETED_DATE on
	 * or after 2007-12-27.
	 *
	 * NB: The effective date of the Final Rule, CMS-1541-FC, is January 1,
	 * 2008. Therefore, the logic above does not cover ALL situations, and in
	 * some cases it is anticipated that providers or RHHIs may need to develop
	 * workarounds.
	 *
	 * For example, an assessment with M0100_ASSMT_REASON=04 or 05, M0090
	 * assessment completed date = 12/27/2007, and episode start date =
	 * 12/31/2007 would need to use Grouper Version 01.06, but the software will
	 * produce a Grouper 02.03 classification because the assessment date is
	 * on/after 12/27/2007. In these cases, a workaround will be necessary.
	 * Guidance has been posted on the CMS Web site,
	 * http://www.cms.hhs.gov/HomeHealthPPS/.
	 *
	 *
	 * @param record
	 * @return true if this version can score the record
	 */
	@Override
	public boolean isValidForVersion(HomeHealthRecordIF record) {
		Calendar date;
		boolean valid;
		final Class acceptableRecordClass = getAcceptableRecordClass();

		/*
		 * make sure there is a valid record and that this grouper can handle that
		 * kind of record
		 */
		if (record == null || !acceptableRecordClass.isAssignableFrom(record.getClass())) {
			valid = false;
		} else {
			// based on line 54 and 55 of psuedo code document
			date = record.getINFO_COMPLETED_DT();
			if (date != null) {
				final int tmpInt = IntegerUtils.parseInt(record.getASSMT_REASON(), 0);
				switch (tmpInt) {
					case 1:
					case 3:
						valid = date.before(effectiveDateStart) ? false : true;
						break;

					case 4:
					case 5:
						valid = date.before(effectiveDateStartWindow) ? false : true;
						break;

					default:
						valid = false;
				}

				// ensure that the record it not after the effective thru date
				if (valid) {
					valid = !date.after(effectiveDateThru);
				}
			} else {
				valid = false;
			}
		}
		return valid;
	}

	/**
	 * gets the description for this version
	 *
	 * @return the description of this version
	 */
	@Override
	public String getDescription() {
		return "Provides the version 2.03 scoring logic for codes valid between January 1, 2008 thru September 30, 2008 inclusive.";
	}

	/**
	 * Empty method - you can not reset the description
	 *
	 * @param arg0
	 */
	@Override
	public void setDescription(String arg0) {
		// can't really do this
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

		grouperDataManager = new GrouperDataManager(this);
		grouperDataManager.init(props);

		clinicalModel_1 = new ClinicalFunctional_ScoringModel_v2308(this, grouperDataManager, 1);
		clinicalModel_2 = new ClinicalFunctional_ScoringModel_v2308(this, grouperDataManager, 2);
		clinicalModel_3 = new ClinicalFunctional_ScoringModel_v2308(this, grouperDataManager, 3);
		clinicalModel_4 = new ClinicalFunctional_ScoringModel_v2308(this, grouperDataManager, 4);

		nrsScoringModel = new NRS_ScoringModel_v2308(this, grouperDataManager);
	}

	/**
	 * This is the main scoring module for the Oasis record. It uses a validator
	 * to verify the diagnosis codes and other variables on the record are valid
	 * for scoring with this version. It uses the 4 clinical/functional and NRS
	 * models to perform the scoring on the record, return the Scoring results.
	 *
	 * Pseudo code lines 553 thru 1971
	 *
	 * @return ScoringResultsIF representing the score. This will never be null
	 *
	 */
	@Override
	public ScoringResultsIF score(HomeHealthRecordIF record,
			boolean validateDates) {
		PointsScoringEquationsIF clinicalScore;
		PointsScoringEquationsIF functionalScore;
		HomeHealthRecordValidatorIF validator;
		HomeHealthRecordValidatorIF nrsValidator;
		HIPPSCode hippsCode;
		int nrsScore;
		ScoringResultsIF scoringResult;
		TreatmentAuthorizationIF treatment;
		ScoringPointsIF scoringPoints;

		ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this, "Pre-scoring: ", record);

		// if the record's dates need to be validated, then determine
		// that first.
		if (validateDates && !isValidForVersion(record)) {
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), ": record not valid for this version");

			// since the record is not valid to score for this version,
			// create the empty scoring results
			scoringResult = new ScoringResults(record);
		} else if (record instanceof HomeHealthRecord_C_IF || record instanceof HomeHealthRecord_C1_IF) {
			// check to make sure that the record is not an OASIS-C
			// record with a Jan 1, 2010 date - if OASIS-C record,
			// then don't score it.
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), ": Invalid record format for this version");

			// since the record is not valid to score for this version,
			// create the empty scoring results
			scoringResult = new ScoringResults(record);
		} else {
			// initialize the scoring variables
			clinicalScore = new PointsScoringEquations();
			functionalScore = new PointsScoringEquations();

			//----------------------------------------------------
			// populate the code for the Clinical/Functional Model
			// this only needs to be done one for all of the
			// clinical models
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "populating codes for Clinical/Functional scoring");
			clinicalModel_1.populateCodes(record);
			ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this, "Post-populating record: ", record);

			// validate the record
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "Validating record");
			validator = getClinicalValidator();
//			final Iterator<HomeHealthEventListenerIF> iterator = getEventListeners();
//			while (iterator.hasNext()) {
//				validator.addEventListener(iterator.next());
//			}
//			validator.validate(record);
			validator.validate(record, getEventListenersList());

			// provide the scoring for each equation
			// the clinical scoring provides two values, one for clinical
			// and one for functional
			// score equation 1
			scoringPoints = clinicalModel_1.score(record, validator);
			clinicalScore.setEarly13AndUnder(scoringPoints.getScores()[0]);
			functionalScore.setEarly13AndUnder(scoringPoints.getScores()[1]);

			// score equation 2
			scoringPoints = clinicalModel_2.score(record, validator);
			clinicalScore.setEarly14Plus(scoringPoints.getScores()[0]);
			functionalScore.setEarly14Plus(scoringPoints.getScores()[1]);

			// score equation 3
			scoringPoints = clinicalModel_3.score(record, validator);
			clinicalScore.setLater13AndUnder(scoringPoints.getScores()[0]);
			functionalScore.setLater13AndUnder(scoringPoints.getScores()[1]);

			// score equation 4
			scoringPoints = clinicalModel_4.score(record, validator);
			clinicalScore.setLater14Plus(scoringPoints.getScores()[0]);
			functionalScore.setLater14Plus(scoringPoints.getScores()[1]);

			//----------------------------------------------------
			// provide the Non-Routine Supplies score
			// populate the codes for the NRS Model
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "populating codes for Non-Routine Supplies scoring");
			nrsScoringModel.populateCodes(record);
			nrsValidator = getNRSValidator();
			nrsValidator.validate(record);
			ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this, "Post-populate with NRS Diagnosis: ", record);

			// the NRS model returns only one item
			scoringPoints = nrsScoringModel.score(record, nrsValidator);
			nrsScore = scoringPoints.getScores()[0];

			// create the HIPPS code based on the scoring
			hippsCode = new HIPPSCode(record, validator, clinicalScore, functionalScore, nrsScore);

			if (hippsCode.getCode().trim().isEmpty()) {
				// create a blank Oasis treatment
				treatment = new TreatmentAuthorization(record, (HomeHealthRecordValidatorIF) null, null, null);
			} else {
				treatment = new TreatmentAuthorization(record, validator, clinicalScore, functionalScore);
			}
			scoringResult = new ScoringResults(hippsCode, getVersion(),
					validator.getDataValidityFlag(), treatment,
					validator, nrsValidator);

		}

		return scoringResult;
	}

	/**
	 * gets the Standard services case mix adjustment table
	 *
	 * @return non-null List of CaseMixAdjustment items. If there was an error
	 * during initialization, the list may be empty
	 */
	@Override
	public List<CaseMixAdjustmentItemIF> getCaseMixAdjustments() {
		return this.grouperDataManager.getCaseMixAdjustments();
	}

	/**
	 * Provides the list of Clinical/Functional codes associated with this
	 * version and used for scoring.
	 *
	 * @return a non-null List of DiagnosisCodeIF
	 */
	@Override
	public List<DiagnosisCodeIF> getClinicalCodes() {
		return this.grouperDataManager.getClinicalCodes();
	}

	/**
	 * Provides the validator for the Clinical portion of the scoring
	 *
	 * @return HomeHealthRecordValidatorIF
	 */
	@Override
	public HomeHealthRecordValidatorIF getClinicalValidator() {
		return new HomeHealthRecordClinicalValidator(this);
	}

	/**
	 * gets a list of Diagnosis Codes for Non-routine Services
	 *
	 * @return
	 */
	@Override
	public List<DiagnosisCodeIF> getNonRoutineCodes() {
		return this.grouperDataManager.getNonRoutineCodes();
	}

	/**
	 * gets the Non-Routine services case mix adjustment table
	 *
	 * @return non-null List of CaseMixAdjustment items. If there was an error
	 * during initialization, the list may be empty
	 */
	@Override
	public List<CaseMixAdjustmentItemIF> getNRSCaseMixAdjustments() {
		return this.grouperDataManager.getNRSCaseMixAdjustments();
	}

	/**
	 * Provides the validator for the Non-Routine Supplies portion of the
	 * scoring
	 *
	 * @return HomeHealthRecordValidatorIF
	 */
	@Override
	public HomeHealthRecordValidatorIF getNRSValidator() {
		return new HomeHealthRecordNRSValidator(this);
	}

	/*
	 * Determines if the Diagnosis code is valid for this version.
	 *
	 *
	 * @return true if the code is not null, and the code's value is found
	 * within this version.
	 */
	@Override
	public boolean isValidDiagnosisCode(HomeHealthRecordIF record,
			DiagnosisCodeIF code) {
		return code == null ? false : grouperDataManager.getDiagnosisCode(code.getCode()) != null;
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
		HomeHealthRecordClinicalValidator validator = new HomeHealthRecordClinicalValidator(this);

		clinicalModel_1.populateCodes(record);
		validator.validate(record);

		return validator;
	}

	/**
	 * Sets the version string - traditionally this should be exactly 5
	 * characters long and never null
	 *
	 * @param version
	 */
	@Override
	public void setVersion(String version) {
		if (version == null) {
			throw new IllegalArgumentException("setVersion() - version can not be null.");
		}

		this.version = version;
	}

	@Override
	public ScoringResultsIF score(HomeHealthRecordIF record, boolean validateDates, CollectionValidationEditsIF validationEdits, Collection<HomeHealthEventListenerIF> listeners) {
		return score(record, validateDates);
	}

	@Override
	public void setEffectiveDateStart(Calendar effectiveDateStart) {
		this.effectiveDateStart = effectiveDateStart;
	}

	@Override
	public void setEffectiveDateThru(Calendar effectiveDateThru) {
		this.effectiveDateThru = effectiveDateThru;
	}

	@Override
	public Class getAcceptableRecordClass() {
		return HomeHealthRecord_B_IF.class;
	}

	@Override
	public void setEffectiveDateStartWindow(Calendar effectiveDateStartWindow) {
		this.effectiveDateStartWindow = effectiveDateStartWindow;
	}

	@Override
	public Calendar getEffectiveDateStartWindow() {
		return this.effectiveDateStartWindow;
	}

    @Override
    public GrouperDataManager getGrouperDataManager() {
        return grouperDataManager;
    }

}
