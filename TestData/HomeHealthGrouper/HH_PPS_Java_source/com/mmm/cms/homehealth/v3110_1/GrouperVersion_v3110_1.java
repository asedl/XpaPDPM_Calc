/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3110_1;

import com.mmm.cms.homehealth.hipps.HIPPSCode;
import com.mmm.cms.homehealth.PointsScoringEquations;
import com.mmm.cms.homehealth.ScoringResults;
import com.mmm.cms.homehealth.TreatmentAuthorization;
import com.mmm.cms.homehealth.io.HomeHealthRecordUtil;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.proto.ScoringPointsIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.proto.TreatmentAuthorizationIF;
import com.mmm.cms.homehealth.v2308_1.GrouperVersion_v2308_1;
import com.mmm.cms.util.ScoringEventFormatter;
import java.util.GregorianCalendar;

/**
 * This version is effective from January 1, 2010 thru Sept 30, 2010. It
 * provides processing for Oasis-C related data. However, since it uses the
 * version V2409's logic for scoring, it also provides a mapping of the data
 * backwards to be compatible with that versions accepted data values.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperVersion_v3110_1 extends GrouperVersion_v2308_1 {

	private boolean mapValues;

	/**
	 * Constructor that sets the effective start to January 1, 2010 and
	 * effective thru September 30, 2010. Also sets the default mapping value,
	 * i.e. mapping Oasis-C values to Oasis-B values prior to scoring, to true,
	 * eg. perform mapping.
	 */
	public GrouperVersion_v3110_1() {
		// no window
		super(new GregorianCalendar(2010, 0, 1), new GregorianCalendar(2010, 8, 30), null);
		setVersion("V3110");

		mapValues = true;
	}

	/**
	 * Gets the name of this Grouper version
	 *
	 * @return The name of this version, plus version number
	 */
	@Override
	public String getName() {
		return "HHA PPS Grouper - January 1, 2010 - " + getVersion();
	}

	/**
	 * Gets the descriptions of this version
	 *
	 * @return the description of this version
	 */
	@Override
	public String getDescription() {
		return "Provides the version 2.03 scoring logic for codes valid between January 1, 2010 thru September 30, 2010 inclusive.";
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

		PointsScoringEquationsIF clinicalScore;
		PointsScoringEquationsIF functionalScore;
		HomeHealthRecordValidatorIF validator;
		HomeHealthRecordValidatorIF nrsValidator;
		HIPPSCode hippsCode;
		int nrsScore;
		ScoringResultsIF scoringResult;
		TreatmentAuthorizationIF treatment;
		ScoringPointsIF scoringPoints;
		HomeHealthRecordIF recordOasisB;

		ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this, "Pre-scoring: ", recordOasisC);

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
			// initialize the scoring variables
			clinicalScore = new PointsScoringEquations();
			functionalScore = new PointsScoringEquations();

			//----------------------------------------------------
			// populate the code for the Clinical/Functional Model
			// this only needs to be done one for all of the
			// clinical models
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "populating codes for Clinical/Functional scoring");
			this.clinicalModel_1.populateCodes(recordOasisC);
			ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this, "Post-populating record: ", recordOasisC);

			// validate the recordOasisC
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "Validating record");
			validator = getClinicalValidator();
			validator.validate(recordOasisC, getEventListenersList());

			// convert/map the incoming recordOasisC to the Oasis-B values,
			// using the non-destructive conversion, so that it can be scored
			// as before.
			recordOasisB = HomeHealthRecordUtil.convertToHomeHealthRecord_OasisB((HomeHealthRecord_C_IF) recordOasisC, this.mapValues);
			ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this, "Post-converting record to Oasis-B values: ", recordOasisB);

			// provide the scoring for each equation
			// the clinical scoring provides two values, one for clinical
			// and one for functional
			// score equation 1
			scoringPoints = this.clinicalModel_1.score(recordOasisB, validator);
			clinicalScore.setEarly13AndUnder(scoringPoints.getScores()[0]);
			functionalScore.setEarly13AndUnder(scoringPoints.getScores()[1]);

			// score equation 2
			scoringPoints = this.clinicalModel_2.score(recordOasisB, validator);
			clinicalScore.setEarly14Plus(scoringPoints.getScores()[0]);
			functionalScore.setEarly14Plus(scoringPoints.getScores()[1]);

			// score equation 3
			scoringPoints = this.clinicalModel_3.score(recordOasisB, validator);
			clinicalScore.setLater13AndUnder(scoringPoints.getScores()[0]);
			functionalScore.setLater13AndUnder(scoringPoints.getScores()[1]);

			// score equation 4
			scoringPoints = this.clinicalModel_4.score(recordOasisB, validator);
			clinicalScore.setLater14Plus(scoringPoints.getScores()[0]);
			functionalScore.setLater14Plus(scoringPoints.getScores()[1]);

			//----------------------------------------------------
			// provide the Non-Routine Supplies score
			// populate the codes for the NRS Model
			ScoringEventFormatter.fireScoringGeneral(getEventListenersList(), this, null,
					getName(), "populating codes for Non-Routine Supplies scoring");
			this.nrsScoringModel.populateCodes(recordOasisC);
			nrsValidator = getNRSValidator();
			nrsValidator.validate(recordOasisC, getEventListenersList());
			ScoringEventFormatter.fireScoringRecord(getEventListenersList(), this, "Post-populate with NRS Diagnosis: ", recordOasisC);

			// since the recordOasisB was convert for the clinical scoring it
			// still has the clinical Dx code info.  So, replace the codes
			// with the NRS codes
			copyCodes(recordOasisC, recordOasisB);

			// the NRS model returns only one item
			scoringPoints = this.nrsScoringModel.score(recordOasisB, nrsValidator);
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
					validator, nrsValidator);
		}

		return scoringResult;
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

	@Override
	public Class getAcceptableRecordClass() {
		return HomeHealthRecord_C_IF.class;
	}

	/**
	 * Provides the validator for the Clinical portion of the scoring
	 *
	 * @return HomeHealthRecordValidatorIF
	 */
	@Override
	public HomeHealthRecordValidatorIF getClinicalValidator() {
		return new HomeHealthRecordClinicalValidator_v3110_1(this);
	}

	@Override
	public HomeHealthRecordValidatorIF getNRSValidator() {
		return new HomeHealthRecordNRSValidator_3110(this);
	}
}
