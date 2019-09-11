/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3210;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.GrouperDataManager;
import com.mmm.cms.homehealth.hipps.HIPPSCode;
import com.mmm.cms.homehealth.HomeHealthEventNotifier;
import com.mmm.cms.homehealth.PointsScoringEquations;
import com.mmm.cms.homehealth.ScoringResults;
import com.mmm.cms.homehealth.TreatmentAuthorization;
import com.mmm.cms.homehealth.io.HomeHealthRecordUtil;
import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;
import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.DiagnosisScoringStatus_EN;
import com.mmm.cms.homehealth.proto.HomeHealthScoringModelIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.proto.ScoringPointsIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.proto.TreatmentAuthorizationIF;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ScoringEventFormatter;
import java.text.SimpleDateFormat;
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
public class GrouperVersion_v3210 extends HomeHealthEventNotifier implements
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
	protected HomeHealthScoringModelIF nrsScoringModel;
	/**
	 * The Clinical Scoring module, equation 1
	 */
	protected HomeHealthScoringModelIF clinicalModel_1;
	/**
	 * The Clinical Scoring module, equation 2
	 */
	protected HomeHealthScoringModelIF clinicalModel_2;
	/**
	 * The Clinical Scoring module, equation 3
	 */
	protected HomeHealthScoringModelIF clinicalModel_3;
	/**
	 * The Clinical Scoring module, equation 4
	 */
	protected HomeHealthScoringModelIF clinicalModel_4;
	/**
	 * The version name - this can only be set during the constructor
	 */
	protected String version;
	/**
	 * flag indicating mapping OASIS-C values to OASIS-B values for grouping
	 * purposes.
	 */
	private boolean mapValues;

	/**
	 * Constructor that sets the effective start and thru date to January 1,
	 */
	public GrouperVersion_v3210() {
		this(new GregorianCalendar(2010, 9, 1), // effective January 1, 2010
				new GregorianCalendar(2011, 8, 30), // effective thru September 30, 2011
				null, // effective date window - no window in this version
				"V3210");
	}

	/**
	 * Constructor that sets the effective start, thru, and window date to the
	 * supplied dates. The start and thru dates can not be null but the window
	 * can be.
	 *
	 * The mapValues indicator is set to true by default.
	 *
	 * @param start - the effective start date
	 * @param thru - the effective thru date, inclusive
	 * @param window - the start of the overlap window. May be null
	 * @param versionName - 5 character string. Can not be null.
	 */
	public GrouperVersion_v3210(Calendar start, Calendar thru, Calendar window,
			String versionName) {

		if (start == null || thru == null) {
			throw new IllegalArgumentException("Neither the start date or the thru date can be null");
		}

		if (versionName == null || versionName.length() != 5) {
			throw new IllegalArgumentException("versionName "
					+ (versionName == null ? " can not be null!" : " of '" + versionName + "' is invalid. Must be a 5 characte string."));
		}

		this.effectiveDateStart = start;
		this.effectiveDateThru = thru;
		this.effectiveDateStartWindow = window != null ? window : this.effectiveDateStart;
		this.mapValues = true;
		this.version = versionName;
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
	 * @since V3210
	 */
	@Override
	public int[][] determineScoreOrder(HomeHealthRecordIF record) {
		DiagnosisCodeIF diagCode;
		DiagnosisCodeIF diagCodeOptional;
		int scoreOrder[][] = new int[6][3];

		// loop through the column 2 codes
		for (int diagIdx = 0; diagIdx < 6; diagIdx++) {
			diagCode = record.getDiagnosisCode(diagIdx);

			// for optional payment codes go across the row to columns 3 & 4
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
	 * gets the list of diagnostic Groups associated with the
	 * Clinical/Functional diagnosis
	 *
	 * @return non-null List of Disgnostic Group items. If there was an error
	 * during initialization, the list may be empty
	 */
	@Override
	public List<DiagnosticGroupIF> getDiagnosticGroups() {
		return this.grouperDataManager.getDiagnosticGroups();
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
		return this.grouperDataManager.getDiagnosticGroupsNRS();
	}

	/**
	 * gets the description for this version
	 *
	 * @return the description of this version
	 */
	@Override
	public String getDescription() {
		StringBuilder buffer = new StringBuilder();
		SimpleDateFormat dformatter = new SimpleDateFormat("MMM d, yyyy");

		buffer.append(getName());
		buffer.append(" - Effective dates: ");
		buffer.append(dformatter.format(getEffectiveDateStart().getTime()));
		buffer.append(" thru ");
		buffer.append(dformatter.format(getEffectiveDateThru().getTime()));
		if (this.effectiveDateStartWindow != null
				&& this.effectiveDateStartWindow != this.effectiveDateStart) {
			buffer.append(" (with effective start window of ");
			buffer.append(dformatter.format(this.effectiveDateStartWindow.getTime()));
			buffer.append(")");
		}

		return buffer.toString();
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

	public HomeHealthScoringModelIF getClinicalModel_1() {
		return clinicalModel_1;
	}

	public HomeHealthScoringModelIF getClinicalModel_2() {
		return clinicalModel_2;
	}

	public HomeHealthScoringModelIF getClinicalModel_3() {
		return clinicalModel_3;
	}

	public HomeHealthScoringModelIF getClinicalModel_4() {
		return clinicalModel_4;
	}

	/**
	 * Provides the validator for the Clinical portion of the scoring
	 *
	 * @return HomeHealthRecordValidatorIF
	 */
	@Override
	public HomeHealthRecordValidatorIF getClinicalValidator() {
		return new HomeHealthRecordClinicalValidator_v3210(this);
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

	public GrouperDataManager getGrouperDataManager() {
		return grouperDataManager;
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
		return this.grouperDataManager.getNRSCaseMixAdjustments();
	}

	public HomeHealthScoringModelIF getNrsScoringModel() {
		return nrsScoringModel;
	}

	/**
	 * Provides the validator for the Non-Routine Supplies portion of the
	 * scoring
	 *
	 * @return HomeHealthRecordValidatorIF
	 */
	@Override
	public HomeHealthRecordValidatorIF getNRSValidator() {
		return new HomeHealthRecordClinicalValidator_v3210(this);
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
	 * gets the version identifier
	 *
	 * @return
	 */
	@Override
	public String getVersion() {
		return this.version;
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

		clinicalModel_1 = new ClinicalFunctional_ScoringModel_v3210(this, grouperDataManager, 1);
		clinicalModel_2 = new ClinicalFunctional_ScoringModel_v3210(this, grouperDataManager, 2);
		clinicalModel_3 = new ClinicalFunctional_ScoringModel_v3210(this, grouperDataManager, 3);
		clinicalModel_4 = new ClinicalFunctional_ScoringModel_v3210(this, grouperDataManager, 4);

		nrsScoringModel = new NRS_ScoringModel_v3210(this, grouperDataManager);

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
			if (valid == true) {
				valid = !date.after(effectiveDateThru);
			}
		} else {
			valid = false;
		}

		return valid;
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
		HomeHealthRecordValidatorIF validator = getClinicalValidator();

		clinicalModel_1.populateCodes(record);
		validator.validate(record);

		return validator;
	}

	/**
	 * Calls score() without the validations edits or listeners
	 *
	 * @param record
	 * @param validateDates
	 * @param validationEdits
	 * @param listeners
	 * @return
	 */
	@Override
	public synchronized ScoringResultsIF score(HomeHealthRecordIF record, boolean validateDates, CollectionValidationEditsIF validationEdits, Collection<HomeHealthEventListenerIF> listeners) {
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
		final HIPPSCode hippsCode;
		PointsScoringEquationsIF clinicalScore;
		PointsScoringEquationsIF functionalScore;
		HomeHealthRecordValidatorIF validator;
		HomeHealthRecordValidatorIF nrsValidator;
		int nrsScore;
		ScoringResultsIF scoringResult;
		TreatmentAuthorizationIF treatment;
		ScoringPointsIF scoringPoints;
		HomeHealthRecordIF recordOasisB;
		int scoreOrder[][];

		ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this,
				"Pre-scoring: ", recordOasisC);

		// if the recordOasisC's dates need to be validated, then determine
		// that first.
		if (validateDates && !isValidForVersion(recordOasisC)) {
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), ": record not valid for this version");

			// since the recordOasisC is not valid to score for this version,
			// create the empty scoring results
			scoringResult = new ScoringResults(recordOasisC);

		} else if (!(recordOasisC instanceof HomeHealthRecord_C_IF)) {
			// make sure that the recordOasisC can be used as a
			// OasisC recordOasisC - if not then don't score it
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), ": Invalid record format for this version");

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
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "populating codes for Clinical/Functional scoring");
			clinicalModel_1.populateCodes(recordOasisC);
			ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this,
					"Post-populating record: ", recordOasisC);

			// validate the recordOasisC
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "Validating record");
			validator = getClinicalValidator();
			validator.validate(recordOasisC, getEventListenersList());
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "Validating record - Done");

			// report all non-blank the codes that are not scored for whatever
			DiagnosisCodeIF diagCode;

			// convert/map the incoming recordOasisC to the Oasis-B values,
			// using the non-destructive conversion, so that it can be scored
			// as before.
			recordOasisB = HomeHealthRecordUtil.convertToHomeHealthRecord_OasisB((HomeHealthRecord_C_IF) recordOasisC, this.mapValues);
			ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this,
					"Post-converting record to Oasis-B values: ", recordOasisB);

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
			scoreOrder = determineScoreOrder(recordOasisB);

			// provide the scoring for each equation
			// the clinical scoring provides two values, one for clinical
			// and one for functional
			// score equation 1
			scoringPoints = clinicalModel_1.score(recordOasisB, validator, scoreOrder);
			clinicalScore.setEarly13AndUnder(scoringPoints.getScores()[0]);
			functionalScore.setEarly13AndUnder(scoringPoints.getScores()[1]);

			// score equation 2
			scoringPoints = clinicalModel_2.score(recordOasisB, validator, scoreOrder);
			clinicalScore.setEarly14Plus(scoringPoints.getScores()[0]);
			functionalScore.setEarly14Plus(scoringPoints.getScores()[1]);

			// score equation 3
			scoringPoints = clinicalModel_3.score(recordOasisB, validator, scoreOrder);
			clinicalScore.setLater13AndUnder(scoringPoints.getScores()[0]);
			functionalScore.setLater13AndUnder(scoringPoints.getScores()[1]);

			// score equation 4
			scoringPoints = clinicalModel_4.score(recordOasisB, validator, scoreOrder);
			clinicalScore.setLater14Plus(scoringPoints.getScores()[0]);
			functionalScore.setLater14Plus(scoringPoints.getScores()[1]);

			//----------------------------------------------------
			// provide the Non-Routine Supplies score
			// populate the codes for the NRS Model
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "populating codes for Non-Routine Supplies scoring");
			nrsScoringModel.populateCodes(recordOasisC);
			ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this,
					"Post-populate with NRS Diagnosis: ", recordOasisC);

			// validate the recordOasisC for NRS
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "Validating NRS record");
			nrsValidator = getNRSValidator();
			nrsValidator.validate(recordOasisC, getEventListenersList());
                        
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "Validating NRS record - Done");

			// since the recordOasisB was convert for the clinical scoring it
			// still has the clinical Dx code info.  So, replace the codes
			// with the NRS codes
			copyCodes(recordOasisC, recordOasisB);

			for (int idx = 0; idx < nrsDiagnosisStatus.length; idx++) {
				diagCode = recordOasisC.getDiagnosisCode(idx);
				nrsDiagnosisStatus[idx] = diagCode.isValidCode()
						? diagCode.isValidForScoring() ? DiagnosisScoringStatus_EN.VALID_SCORABLE : DiagnosisScoringStatus_EN.VALID
						: DiagnosisScoringStatus_EN.INVALID;
				// report all non-blank the codes that are not scored for whatever
				// reason
				if (!diagCode.isEmpty() && !diagCode.isValidForScoring()) {
					ScoringEventFormatter.fireValidCodeWarning(this.getEventListenersList(), this, nrsScoringModel, diagCode, idx + 1, diagCode.isValidCode());
				}
			}

			// Determine when the code should be scored - not at all
			// pass 1 or pass 2
			scoreOrder = determineScoreOrder(recordOasisB);

			// the NRS model returns only one item
			scoringPoints = nrsScoringModel.score(recordOasisB, nrsValidator, scoreOrder);
			nrsScore = scoringPoints.getScores()[0];

			// create the HIPPS code based on the scoring
			hippsCode = new HIPPSCode(recordOasisC, validator, clinicalScore, functionalScore, nrsScore);
			if (hippsCode.getCode().trim().isEmpty()) {
				// create a blank Oasis treatment
				treatment = new TreatmentAuthorization(recordOasisC, (HomeHealthRecordValidatorIF) null, null, null);
			} else {
				treatment = new TreatmentAuthorization(recordOasisC, validator, clinicalScore, functionalScore);
			}

			scoringResult = new ScoringResults(hippsCode, getVersion(),
					validator.getDataValidityFlag(), treatment,
					validator,
					nrsValidator);
			scoringResult.setDiagnosisScoringStatus(diagnosisStatus);
			scoringResult.setNrsDiagnosisScoringStatus(nrsDiagnosisStatus);
		}

		return scoringResult;
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

	public void setClinicalModel_1(ClinicalFunctional_ScoringModel_v3210 clinicalModel_1) {
		this.clinicalModel_1 = clinicalModel_1;
	}

	public void setClinicalModel_2(ClinicalFunctional_ScoringModel_v3210 clinicalModel_2) {
		this.clinicalModel_2 = clinicalModel_2;
	}

	public void setClinicalModel_3(ClinicalFunctional_ScoringModel_v3210 clinicalModel_3) {
		this.clinicalModel_3 = clinicalModel_3;
	}

	public void setClinicalModel_4(ClinicalFunctional_ScoringModel_v3210 clinicalModel_4) {
		this.clinicalModel_4 = clinicalModel_4;
	}

	public void setGrouperDataManager(GrouperDataManager grouperDataManager) {
		this.grouperDataManager = grouperDataManager;
	}

	/**
	 * Emtpy method - you can reset the name
	 *
	 * @param name
	 */
	@Override
	public void setName(String name) {
		// you really can't set the name
	}

	public void setNrsScoringModel(NRS_ScoringModel_v3210 nrsScoringModel) {
		this.nrsScoringModel = nrsScoringModel;
	}

	/**
	 * The version name can not be reset This method does nothing.
	 *
	 * @param version
	 */
	@Override
	public void setVersion(String version) {
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
		return HomeHealthRecord_C_IF.class;
	}

	@Override
	public void setEffectiveDateStartWindow(Calendar effectiveDateStartWindow) {
		this.effectiveDateStartWindow = effectiveDateStartWindow;
	}

	@Override
	public Calendar getEffectiveDateStartWindow() {
		return this.effectiveDateStartWindow;
	}

	
}
