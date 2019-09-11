/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3413;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.DiagnosisScoringGrid;
import com.mmm.cms.homehealth.GrouperDataManager;
import com.mmm.cms.homehealth.proto.*;
import com.mmm.cms.homehealth.v3312.ClinicalFunctional_ScoringModel_v3312;
import com.mmm.cms.homehealth.v3312.ManifestationUtils;
import com.mmm.cms.util.ScoringEventFormatter;

/**
 * This Model provides scoring for the 4 clinical and functional equations
 * defined for Grouper version 2.03.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ClinicalFunctional_ScoringModel_v3413 extends ClinicalFunctional_ScoringModel_v3312 {

	/**
	 * Constructor with references to the Grouper, the Data manager, and the
	 * equation id
	 *
	 * @param grouper
	 * @param grouperDataManager
	 * @param equationId
	 */
	public ClinicalFunctional_ScoringModel_v3413(HomeHealthGrouperIF grouper,
			GrouperDataManager grouperDataManager,
			int equationId) {
		super(grouper, grouperDataManager, equationId);
	}

	@Override
	public String getName() {
		return "Clinical / Functional Scoring Model V3413";
	}

	/**
	 *
	 * September 2012 - Overriding this method in order to added check for
	 * optional payment code pairings prior to scoring related codes.
	 *
	 * Though this "override" duplicates much the super implementation of this
	 * method, it is worth looking at the super implementation for more info
	 *
	 * @param record
	 * @param validator
	 * @return score
	 * @see
	 * ClinicalFunctional_ScoringModel_v3312#scoreClinial(HomeHealthRecordIF
	 * record, HomeHealthRecordValidatorIF validator, int scoreOrder[][])
	 */
	@Override
	public int scoreClinical(HomeHealthRecordIF record,
			HomeHealthRecordValidatorIF validator, int scoreOrder[][]) {
		DiagnosisCodeIF diagCode;
		DiagnosisCodeIF pairedDiagCode;
		int totalScore;
		final DiagnosisScoringGridIF scoringGrid = new DiagnosisScoringGrid();

		ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this, "Clinical Scoring");

		// make two passes over the codes and only score the code if its
		// score order matches the currentPass indicator
		// pass 1 will score the stand alone etiology codes
		// pass 2 will score the manifestation/etiology codes
		for (int currentPass = 1; currentPass <= 2; currentPass++) {
			// loop through the column 2 codes - if the code is a VCode
			// then move to the 3rd and 4th columns to score
			for (int diagIdx = 0; diagIdx < 6; diagIdx++) {
				diagCode = record.getDiagnosisCode(diagIdx);

				/*
				 * There is a new set of optional payment codes in this version: primary code
				 * pairings as well as payment code pairing and standard scoring
				 * payment code
				 */
				if (diagCode.isVCode() && diagCode.isOptionalPaymentCode()) {
					/*
					 * for optional payment codes go across the row to columns 3 & 4
					 * column 3 is now paired with payment code in column 1, so check
					 * that the pairing is correct before scoring. The payment codes
					 * are mutually exclusive in that a payment code can not be both a
					 * primary Awarding code and a Payment code at the same
					 * time.
					 */
					if (diagCode.isEtiologyInPairingList(record.getOptionalDiagnosisCode3(diagIdx))) {
						scoreClinicalColumn3_4(record, validator, diagIdx, scoringGrid,
								scoreOrder, currentPass);
					}
				} else {

					/*
					 * determine if this code's position is scored on this pass
					 * 
					 * These codes may not be recognized for scoring even if
					 * the score order marks them as scoring in a specific
					 * order
					 *
					 * Codes that are secondary codes that can not be pair
					 * with an etiology, or are excluded due to
					 *
					 */
					if (currentPass == scoreOrder[diagIdx][0]
							&& diagCode.isValidForScoring()) {
						// Now score the code
						scoreClinicalCode(record, diagCode, validator, diagIdx, scoringGrid);

						// if the current code is a secondary only, determine
						// whether it earns points or the etiology earns points
						if (diagCode.isSecondaryOnly()) {
							/*
							 * August 2011 - changed the way to get the the
							 * paired code
							 */
							pairedDiagCode = ManifestationUtils.getPairedCode(grouper, record, diagCode, diagIdx);
							if (pairedDiagCode != null && pairedDiagCode.isValidForScoring()) {
								/*
								 * if the current score for the etiology is
								 * 0, then score it again to ensure that the
								 * 0 was not due to a previous
								 * etiology/manifestation contention
								 */
								if (scoringGrid.getTotalScoreForDiagnosis(pairedDiagCode.getOffset()) == 0) {
									scoreClinicalCode(record, pairedDiagCode, validator, pairedDiagCode.getOffset(), scoringGrid);
								}

								// now compare the scores
								resolveEtiologyManifestationContention(
										pairedDiagCode, pairedDiagCode.getOffset(),
										diagCode, diagIdx, scoringGrid);
							}
						}
					}
				}
			}
		}

		// add up the score and return it
		totalScore = scoringGrid.getTotalScore();

		// score the remaining Clinical related variables
		totalScore += scoreRemainingVariables(record, validator, totalScore);

		ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this, "Clinical Scoring", totalScore);

		return totalScore;
	}

	/**
	 * This overrides in order to check for primary awarding for Dx in position
	 * 2, with the appropriate optional payment code in position 1
	 *
	 * @param record
	 * @param diagCode
	 * @param validator
	 * @param diagIdx
	 * @param scoringGrid
	 */
	@Override
	public void scoreClinicalCode(HomeHealthRecordIF record,
			DiagnosisCodeIF diagCode, HomeHealthRecordValidatorIF validator,
			int diagIdx, DiagnosisScoringGridIF scoringGrid) {

		if (grouper.getListenerCount() > 0) {
			ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
					"Attempting to score Diagnosis ", diagCode.getCode(), " (",
					diagCode.getDiagnosticGroup().getDescription(), ") at position ", Integer.toString(diagIdx + 1));
		}

		try {
			/*
			 * determine if this is a primary code position primary code 
			 * positions are:
			 *   1) non-manifestion DX codes in position 1 
			 *   2) non-manifestion DX in position 7
			 *   3) a manifestation in position 2
			 *   4) a manifestation in postition 12 
			 * 
			 *  New for this version are:
			 *   5) non-manifestion DX codes in position 2 if the code in 
			 *      position 1 is an awarding code
			 *   6) a manifestation in position 3 if the code in position 2 is
			 *      a paired etiology and awarded primary scoring because 
			 *      their is an awarding code in position 1
			 *   7) a manifestation in position 3 if the code in position 2 is
			 *      a paired etiology and the manifestation in position 3 is
			 *      awarded primary scoring because their is an awarding code 
			 *      in position 1
			 */
			if (diagIdx == 0
					|| diagIdx == 6
					|| diagCode.isSecondaryOnly() && (diagIdx == 1 || diagIdx == 12)) {
				// logic items 1 thru 4
				scorePrimaryOnly(record, diagCode, diagIdx, scoringGrid);

			} else if (diagIdx == 1
					&& record.getPRIMARY_DIAG_ICD().isPrimaryAwardableCode()
					&& diagCode.getDiagnosticGroup().isAlternatePrimaryScorable()) {
				// logic item 5
				scorePrimaryOnly(record, diagCode, diagIdx, scoringGrid);

			} else if (diagIdx == 2
					&& diagCode.isSecondaryOnly()
					&& record.getPRIMARY_DIAG_ICD().isPrimaryAwardableCode()
					&& record.getOTH_DIAG1_ICD().getDiagnosticGroup().isAlternatePrimaryScorable()) {
				// logic item 6
				scorePrimaryOnly(record, diagCode, diagIdx, scoringGrid);

			} else if (diagIdx == 2
					&& diagCode.isSecondaryOnly()
					&& record.getPRIMARY_DIAG_ICD().isPrimaryAwardableCode()
					&& diagCode.getDiagnosticGroup().isAlternatePrimaryScorable()) {
				// logic item 7
				scorePrimaryOnly(record, diagCode, diagIdx, scoringGrid);

			} else {
				// all other code conditions
				scoreOtherOnly(record, diagCode, diagIdx, scoringGrid);
			}
			// all codes are scored as "any"
			scoreAny(record, diagCode, validator, diagIdx, scoringGrid);

		} catch (AlreadyScoredException e) {
			ScoringEventFormatter.fireDxGroupAlreadyScored(grouper.getEventListenersList(), grouper, this, diagCode, diagIdx + 1, e);
		}
	}

	/**
	 * Scores the Primary only code with Primary only CaseMix values.
	 *
	 * @param record
	 * @param diagCode
	 * @param diagIdx
	 * @param scoringGrid
	 * @throws com.mmm.cms.homehealth.proto.AlreadyScoredException
	 */
	@Override
	public void scorePrimaryOnly(HomeHealthRecordIF record,
			DiagnosisCodeIF diagCode, int diagIdx,
			DiagnosisScoringGridIF scoringGrid) throws AlreadyScoredException {

		//----------------------------------------------------
		// Primary codes
		//
		// if the code is a
		// potential primary code (an etiology in position index 0 or 6, or a
		// manifestation in position 2 or 12), then we can sometimes score the
		// code differently then when it is in an Other position.
		// Because of the 2 pass logic, in some cases, we
		// can score some Diagnostic Groups twice, but with a different
		// case mix adjustment row. When scoring the same Group twice,
		// the previous score from pass 1, must be substracted from the
		// the primary code score, so as not to over count the scoring.
		// This effects only a couple Dx Groups that have Primary code specific
		// values.
		int tmpScore;

		// score the code as a primary code
		switch (diagCode.getDiagnosticGroup().getId()) {
			case 4:  // Diabetes
				tmpScore = calculatePrimaryAdjustment(scoringGrid, 4, 5);
				scoringGrid.addScore(diagCode, diagIdx, 4, tmpScore, false);
				ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, diagIdx + 1, 4, tmpScore);

				// this Dx Group has some primary or other code related scoring
				break;

			case 10: // Neuro 1 - Brain disorders and paralysis
				// Neuro 1 does not have an explicit "Other" row, so there
				// is nothing to adjust, just use the primary code as is
				tmpScore = getCaseMixAdjustmentEquation(12, super.getEquationId());
				scoringGrid.addScore(diagCode, diagIdx, 12, tmpScore, false);
				ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, diagIdx + 1, 12, tmpScore);

				// no other scoring is require for this primary code
				break;

			case 19: // // Skin 1 -Traumatic wounds, burns and post-operative complications
				tmpScore = calculatePrimaryAdjustment(scoringGrid, 25, 26);
				scoringGrid.addScore(diagCode, diagIdx, 25, tmpScore, false);
				ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this, diagCode, diagIdx + 1, 25, tmpScore);

				// this Dx Group has some primary or other code related scoring
				break;

			default:
		}
	}
}
