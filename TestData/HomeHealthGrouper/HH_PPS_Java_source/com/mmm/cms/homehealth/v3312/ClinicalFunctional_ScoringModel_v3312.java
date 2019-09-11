/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3312;

import com.mmm.cms.homehealth.DiagnosisScoringGrid;
import com.mmm.cms.homehealth.GrouperDataManager;
import com.mmm.cms.homehealth.proto.DiagnosisScoringGridIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.v3210.ClinicalFunctional_ScoringModel_v3210;
import com.mmm.cms.util.ScoringEventFormatter;

/**
 * This Model provides scoring for the 4 clinical and functional equations
 * defined for Grouper version 2.03.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ClinicalFunctional_ScoringModel_v3312 extends ClinicalFunctional_ScoringModel_v3210 {

	/**
	 * Constructor with references to the Grouper, the Data manager, and the
	 * equation id
	 *
	 * @param grouper
	 * @param grouperDataManager
	 * @param equationId
	 */
	public ClinicalFunctional_ScoringModel_v3312(HomeHealthGrouperIF grouper,
			GrouperDataManager grouperDataManager,
			int equationId) {
		super(grouper, grouperDataManager, equationId);
	}

	@Override
	public String getName() {
		return "Clinical / Functional Scoring Model V3312";
	}

	/**
	 * Given two codes that are part of a Manifestation/Etiology pair, determine
	 * which code has the highest score and remove the value for the code with
	 * the lowest score. Tie goes to the Etiology code.
	 *
	 * August 2011 - Since there may be multiple manifestation codes for an
	 * etiology code, if the etiology code wins the contention, it should be
	 * marked as having won. So, the next time an etiology/manifestation
	 * contention occurs with the same etiology, since the etiology already
	 * scored, then the manifestation would automatically win the contention.
	 * Basically, ensuring the requirement that a code can not score twice.
	 *
	 * When an etiology wins the contention, the manifestation code's score
	 * should be cleared. When the manifestation wins the contention, the
	 * etiology code's score should be clear, but only if the etiology had not
	 * won a previous contention.
	 *
	 * @param etiologyCode
	 * @param etiologyIdx
	 * @param manifestionCode
	 * @param manifestIdx
	 * @param scoringGrid
	 */
	@Override
	public void resolveEtiologyManifestationContention(
			DiagnosisCodeIF etiologyCode, int etiologyIdx,
			DiagnosisCodeIF manifestionCode, int manifestIdx,
			DiagnosisScoringGridIF scoringGrid) {

		// if the etiology has already scored due to winning a contention,
		// so the mani wins the contention this time. So, both codes just 
		// keep their scores - i.e. can not wipe put the etiology score
		// because it scored earlier
		if (scoringGrid.getScoredByWinningContention(etiologyIdx)) {
			ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
					"Manifestation/Etiology Contention: Previous etiology code ",
					etiologyCode.getCode(), " already scored due to previous contention.");
			return;
		}

		// the previous code may have been a valid manifestation/
		// etiology pair code, but not a case mix code - i.e. not scored, so
		// the etiology score would be 0
		if (scoringGrid.getTotalScoreForDiagnosis(manifestIdx)
				> scoringGrid.getTotalScoreForDiagnosis(etiologyIdx)) {

			// clear the Etiology score
			scoringGrid.clearScore(etiologyIdx);

			ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
					"Manifestation/Etiology Contention: Previous etiology code ",
					etiologyCode.getCode(), " loses score for contention.");
		} else {
			// clear the Manifestion score
			scoringGrid.clearScore(manifestIdx);

			// set the eitology's winning flag
			scoringGrid.setScoredByWinningContention(etiologyIdx, true);

			ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
					"Manifestation/Etiology Contention: Manifestation code ", manifestionCode.getCode(),
					" loses score for contention.");
		}
	}

	/**
	 * Clinically scores the record placing values into the pointSE, and
	 * dualPointsSE variables. The scoring is split up into Diagnosis Group
	 * related questions and drill down further.
	 *
	 * Pseudo code lines: no longer appropriate because this simplifies the
	 * logic
	 *
	 * @param record
	 * @param validator
	 * @return
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

				// for optional payment codes go across the row to columns 3 & 4
				if (diagCode.isOptionalPaymentCode()) {
					// need to figure this out first
					scoreClinicalColumn3_4(record, validator, diagIdx, scoringGrid,
							scoreOrder, currentPass);
				} else {
					/*
					 * determine if this code's position is scored on this pass
					 * 
					 * These codes may not be recognized for scoring even
					 * if the score order marks them as scoring in a
					 * specific order
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
							 * August 2011 - changed the way to get the
							 * the paired code
							 */
							pairedDiagCode = ManifestationUtils.getPairedCode(grouper, record, diagCode, diagIdx);
							if (pairedDiagCode != null && pairedDiagCode.isValidForScoring()) {
								/*
								 * if the current score for the etiology
								 * is 0, then score it again to ensure
								 * that the 0 was not due to a previous
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
}
