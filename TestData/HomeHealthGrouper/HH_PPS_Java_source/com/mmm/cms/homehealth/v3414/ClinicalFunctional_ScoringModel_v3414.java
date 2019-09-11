/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3414;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.DiagnosisCode;
import com.mmm.cms.homehealth.DiagnosisCode_Empty;
import com.mmm.cms.homehealth.DiagnosisScoringGrid;
import com.mmm.cms.homehealth.ScoringPoints;
import com.mmm.cms.homehealth.proto.*;
import com.mmm.cms.homehealth.v3312.ManifestationUtils;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ScoringEventFormatter;
import com.mmm.cms.util.ValidateUtils;
import java.util.Collection;

/**
 * This Model provides scoring for the 4 clinical and functional equations
 * defined for Grouper version 2.03.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ClinicalFunctional_ScoringModel_v3414
		implements HomeHealthScoringModelIF {

	private int id;
	protected DataManagerIF grouperDataManager;
	protected HomeHealthGrouperIF grouper;
	private transient String shortName;

	/**
	 * Constructor with references to the Grouper, the Data manager, and the
	 * equation id
	 *
	 * @param grouper
	 * @param grouperDataManager
	 * @param id
	 */
	public ClinicalFunctional_ScoringModel_v3414(HomeHealthGrouperIF grouper,
			DataManagerIF grouperDataManager,
			int equationId) {
		this.grouper = grouper;
		this.grouperDataManager = grouperDataManager;
		this.id = equationId;
		this.shortName = "Equation " + equationId + ": ";
	}

	public ClinicalFunctional_ScoringModel_v3414(HomeHealthGrouperIF grouper,
			DataManagerIF grouperDataManager, int equationId, String shortName) {
		this.id = equationId;
		this.grouperDataManager = grouperDataManager;
		this.grouper = grouper;
		this.shortName = shortName;
	}

	/**
	 * This calculates the primary code score, and if the Other Dx has already
	 * been scored, then reduce the primary code by the Other Dx value.
	 *
	 * @param scoringGrid
	 * @param primaryAdjustmentRow
	 * @param otherAdjustmentRow
	 * @return
	 */
	protected int calculatePrimaryAdjustment(DiagnosisScoringGridIF scoringGrid,
			int primaryAdjustmentRow, int otherAdjustmentRow) {
		int primaryScore;

		// if the other row has already been scored, then reduce the primary
		// value
		if (scoringGrid.isCaseMixAdjustmentRowScored(otherAdjustmentRow)) {
			primaryScore = getCaseMixAdjustmentEquation(primaryAdjustmentRow, id)
					- getCaseMixAdjustmentEquation(otherAdjustmentRow, id);
		} else {
			primaryScore = getCaseMixAdjustmentEquation(primaryAdjustmentRow, id);
		}

		return primaryScore;
	}

	/**
	 * This gets the Case Mix Adjustment table value, by essentially getting the
	 * caseMixId (i.e. row) and the equation and returning the value.
	 *
	 * @param caseMixId
	 * @param id
	 * @return the adjustment or 0 if the case mix id is not found, or the
	 * adjustment value is blank
	 */
	public int getCaseMixAdjustmentEquation(int caseMixId, int equationId) {
		final CaseMixAdjustmentItemIF caseMix = grouperDataManager.getCaseMixAdjustment(caseMixId);
		return caseMix == null ? 0 : caseMix.getAdjustment(equationId);
	}

	/**
	 * @param codeValue
	 * @return the Code with its value being codeValue. If the code is not valid
	 * (found for this version), then the Invalid flag will be set to true and
	 * all other information about the code will be meaningless. This method
	 * should never return null.
	 */
	protected DiagnosisCodeIF getDiagnosisCode(String codeValue) {
		DiagnosisCodeIF code = DiagnosisCode_Empty.DEFAULT;

		// get the diagnosis code from the data manager
		if (codeValue != null) {
			codeValue = codeValue.trim();
			if (!codeValue.isEmpty()) {
				code = grouperDataManager.getDiagnosisCode(codeValue);

				// if the code is not found in the manager, it is
				// considered invalid for this version.  However, this method
				// should never return a null, so create Diagnosis with the code
				// value - by default a Diagnosis code is not a ValidCode, and
				// is it not Valid for scoring
				if (code == null) {
					code = new DiagnosisCode(codeValue);
				}
			}
		}

		return code;
	}

	@Override
	public HomeHealthGrouperIF getGrouper() {
		return grouper;
	}

	@Override
	public String getName() {
		return "Clinical / Functional Scoring Model V3414";
	}

	public String getShortName() {
		return this.shortName;
	}

	/**
	 * This populates the record with all Diagnosis codes that are valid for
	 * this grouper version that are listed on the record. Codes that are found
	 * are marked as Valid, otherwise the code is mark as not Valid, and not
	 * Valid for scoring. This method should be called prior to scoring the
	 *
	 * @param record
	 */
	@Override
	public void populateCodes(HomeHealthRecordIF record) {
		DiagnosisCodeIF tmpCode;

		tmpCode = record.getPRIMARY_DIAG_ICD();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPRIMARY_DIAG_ICD(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getOTH_DIAG1_ICD();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setOTH_DIAG1_ICD(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getOTH_DIAG2_ICD();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setOTH_DIAG2_ICD(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getOTH_DIAG3_ICD();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setOTH_DIAG3_ICD(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getOTH_DIAG4_ICD();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setOTH_DIAG4_ICD(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getOTH_DIAG5_ICD();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setOTH_DIAG5_ICD(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_A3();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_A3(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_B3();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_B3(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_C3();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_C3(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_D3();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_D3(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_E3();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_E3(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_F3();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_F3(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_A4();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_A4(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_B4();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_B4(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_C4();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_C4(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_D4();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_D4(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_E4();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_E4(getDiagnosisCode(tmpCode.getCode()));
		}

		tmpCode = record.getPMT_DIAG_ICD_F4();
		if (tmpCode != DiagnosisCode_Empty.DEFAULT) {
			record.setPMT_DIAG_ICD_F4(getDiagnosisCode(tmpCode.getCode()));
		}
	}

	/**
	 * There is no preprocessing of the HomeHealth Record for this model, and
	 * this does nothing.
	 *
	 * @param record
	 * @param validator
	 */
	@Override
	public void preprocessRecord(HomeHealthRecordIF record,
			HomeHealthRecordValidatorIF validator) {
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
					"Manifestation/Etiology Contention: Manifestation code ",
					manifestionCode.getCode(), " loses score for contention.");
		}
	}

	/**
	 * This is the main scoring module, which scores the clinical information
	 * first and then the functional information.
	 *
	 * @see #scoreClinical(HomeHealthRecordIF record,
	 * HomeHealthRecordValidatorIF validator)
	 * @see #scoreFunctional(HomeHealthRecordIF record,
	 * HomeHealthRecordValidatorIF validator)
	 *
	 * @param record
	 * @param validator
	 * @return ScoringPointsIF that contains the clinical and functional scoring
	 * elements. This will never be null
	 */
	@Override
	public ScoringPointsIF score(HomeHealthRecordIF record,
			HomeHealthRecordValidatorIF validator, int scoreOrder[][]) {
		final ScoringPoints points = new ScoringPoints(2);

		// score the clinical and functional parts, assigning them to the results
		points.setScoreAt(0, scoreClinical(record, validator, scoreOrder));
		points.setScoreAt(1, scoreFunctional(record, validator));

		ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
				"Final equation score (clinical + functional): ",
				Integer.toString(points.getScores()[0] + points.getScores()[1]));

		return points;
	}

	/**
	 * This is the main scoring module, which scores the clinical information
	 * first and then the functional information.
	 *
	 * @see #scoreClinical(HomeHealthRecordIF record,
	 * HomeHealthRecordValidatorIF validator)
	 * @see #scoreFunctional(HomeHealthRecordIF record,
	 * HomeHealthRecordValidatorIF validator)
	 *
	 * @param record
	 * @param validator
	 * @return ScoringPointsIF that contains the clinical and functional scoring
	 * elements. This will never be null
	 */
	@Override
	public ScoringPointsIF score(HomeHealthRecordIF record,
			HomeHealthRecordValidatorIF validator) {
		return score(record, validator, grouper.determineScoreOrder(record));
	}

	/**
	 * This calls score() without the listeners
	 *
	 * @param record
	 * @param validator
	 * @param scoreOrder
	 * @param listeners - always assumed to be null
	 * @return
	 */
	@Override
	public ScoringPointsIF score(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, int[][] scoreOrder, Collection<HomeHealthEventListenerIF> listeners) {
		return score(record, validator, scoreOrder);
	}

	/**
	 * This section covers scoring of Diagnostic groups and Case Mix Adjustment
	 * Rows that do not depend on Primary or Other specific values for the code
	 *
	 * May 2013: updated to use OASIS-C values
	 *
	 * @param record
	 * @param diagCode
	 * @param diagIdx
	 * @param scoringGrid
	 * @throws com.mmm.cms.homehealth.proto.AlreadyScoredException
	 */
	public void scoreAny(HomeHealthRecordIF record,
			DiagnosisCodeIF diagCode, HomeHealthRecordValidatorIF validator,
			int diagIdx, DiagnosisScoringGridIF scoringGrid) throws AlreadyScoredException {

		int tmpScore;

		// determine the Diagnostic Group,
		switch (diagCode.getDiagnosticGroup().getId()) {
			case 1: // Blindness and low vision
				tmpScore = getCaseMixAdjustmentEquation(1, id);
				scoringGrid.addScore(diagCode, diagIdx, 1,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 1, tmpScore);
				break;

			case 2: // Blood disorders
				tmpScore = getCaseMixAdjustmentEquation(2, id);
				scoringGrid.addScore(diagCode, diagIdx, 2,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 2, tmpScore);
				break;

			case 3: // Cancer and selected benign neoplasms
				tmpScore = getCaseMixAdjustmentEquation(3, id);
				scoringGrid.addScore(diagCode, diagIdx, 3,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 3, tmpScore);
				break;

			case 5: // Dysphagia
				if (record.isDiagnosticGroupOnRecord(12, diagIdx)) {
					tmpScore = getCaseMixAdjustmentEquation(6, id);
					scoringGrid.addScore(diagCode, diagIdx, 6,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 6, tmpScore);
				}

				if (validator.isTHERAPIES_Valid()
						&& validator.isINTERNAL_LOGIC_Valid()
						&& "1".equals(record.getTHH_ENT_NUTRITION())) {

					// add Table 5, row 7
					tmpScore = getCaseMixAdjustmentEquation(7, id);
					scoringGrid.addScore(diagCode, diagIdx, 7,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 7, tmpScore);
				}
				break;

			case 6: // Gait Abnormality
				if (validator.isSTGPRSUL_Valid()
                                        && ValidateUtils.isValidValue(
						record.getSTG_PRBLM_ULCER(),
						ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {

					// add Table 5, row 19
					tmpScore = getCaseMixAdjustmentEquation(19, id);
					scoringGrid.addScore(diagCode, diagIdx, 19,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 19, tmpScore);
				}
				break;

			case 7: // Gastrointestinal disorders
				tmpScore = getCaseMixAdjustmentEquation(8, id);
				scoringGrid.addScore(diagCode, diagIdx, 8,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 8, tmpScore);

				if (validator.isOSTOMY_Valid()
						&& ValidateUtils.isValidValue(record.getOSTOMY(),
						ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {

					tmpScore = getCaseMixAdjustmentEquation(9, id);
					scoringGrid.addScore(diagCode, diagIdx, 9,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 9, tmpScore);
				}

				if (record.isDiagnosticGroupOnRecord(10, diagIdx)
						|| record.isDiagnosticGroupOnRecord(11, diagIdx)
						|| record.isDiagnosticGroupOnRecord(12, diagIdx)
						|| record.isDiagnosticGroupOnRecord(13, diagIdx)) {

					tmpScore = getCaseMixAdjustmentEquation(10, id);
					scoringGrid.addScore(diagCode, diagIdx, 10,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 10, tmpScore);
				}

				break;

			case 8: // Heart Disease
			case 9: // Hypertension
				tmpScore = getCaseMixAdjustmentEquation(11, id);
				scoringGrid.addScore(diagCode, diagIdx, 11,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 11, tmpScore);
				break;

			case 10: // Neuro 1 - Brain disorders and paralysis
				if (validator.isCUR_TOILETING_Valid()
                                        && ValidateUtils.isValidValue(
						record.getCRNT_TOILTG(),
						ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR)) {

					tmpScore = getCaseMixAdjustmentEquation(13, id);
					scoringGrid.addScore(diagCode, diagIdx, 13,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 13, tmpScore);
				}

			// fall through to Neuro 2 since the next criteria could
			// also apply to Neuro 1

			case 11: // Neuro 2 - Peripheral neurological disorders
				if (validator.isCUR_DRESS_Valid()
						&& (ValidateUtils.isValidValue(
						record.getCRNT_DRESS_UPPER(),
						ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE) || ValidateUtils.isValidValue(
						record.getCRNT_DRESS_LOWER(),
						ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE))) {

					tmpScore = getCaseMixAdjustmentEquation(14, id);
					scoringGrid.addScore(diagCode, diagIdx, 14,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 14, tmpScore);
				}
				break;

			case 12: // Neuro 3 - Stroke
				// add Table 5, row 15
				tmpScore = getCaseMixAdjustmentEquation(15, id);
				scoringGrid.addScore(diagCode, diagIdx, 15,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 15, tmpScore);

				if (validator.isCUR_DRESS_Valid()
						&& (ValidateUtils.isValidValue(
						record.getCRNT_DRESS_UPPER(),
						ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE) || ValidateUtils.isValidValue(
						record.getCRNT_DRESS_LOWER(),
						ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE))) {

					tmpScore = getCaseMixAdjustmentEquation(16, id);
					scoringGrid.addScore(diagCode, diagIdx, 16,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 16, tmpScore);
				}

				// MODIFIED FOR OASIS-C AMBULATION VALUES
				if (validator.isCUR_AMBULATION_Valid()
						&& ValidateUtils.isValidValue(
						record.getCRNT_AMBLTN(),
						ValidateUtils.ARRAY_DOUBLE_FOUR_FIVE_SIX)) {

					tmpScore = getCaseMixAdjustmentEquation(17, id);
					scoringGrid.addScore(diagCode, diagIdx, 17,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 17, tmpScore);
				}
				break;

			case 13: //Neuro 4 - Multiple Sclerosis
				// MODIFIED FOR OASIS-C BATHING AND AMBULATIONS VALUES
				if (validator.isCUR_BATHING_Valid()&& ValidateUtils.isValidValue(record.getCRNT_BATHG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE_SIX)
						|| validator.isCUR_TOILETING_Valid()&& ValidateUtils.isValidValue(record.getCRNT_TOILTG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR)
						|| validator.isCUR_TRANSFER_Valid()&& ValidateUtils.isValidValue(record.getCRNT_TRNSFRNG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE)
						|| validator.isCUR_AMBULATION_Valid()&& ValidateUtils.isValidValue(record.getCRNT_AMBLTN(), ValidateUtils.ARRAY_DOUBLE_FOUR_FIVE_SIX)) {

					tmpScore = getCaseMixAdjustmentEquation(18, id);
					scoringGrid.addScore(diagCode, diagIdx, 18,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 18, tmpScore);
				}
				break;

			case 14: // Ortho 1 - Leg Disorders
				if (validator.isSTGPRSUL_Valid()
						&& ValidateUtils.isValidValue(
						record.getSTG_PRBLM_ULCER(),
						ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR)) {

					tmpScore = getCaseMixAdjustmentEquation(19, id);
					scoringGrid.addScore(diagCode, diagIdx, 19,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 19, tmpScore);
				}

			// fall through to Ortho 2 since the next criteria could
			// also apply to Ortho 1

			case 15: // Ortho 2 - Other Orthopedic disorders
				if (validator.isINTERNAL_LOGIC_Valid()
						&& validator.isTHERAPIES_Valid()
						&& ("1".equals(record.getTHH_IV_INFUSION())
						|| "1".equals(record.getTHH_PAR_NUTRITION()))) {

					tmpScore = getCaseMixAdjustmentEquation(20, id);
					scoringGrid.addScore(diagCode, diagIdx, 20,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 20, tmpScore);
				}
				break;

			case 16: // Psych 1 - Affective and other psychoses, depression
				tmpScore = getCaseMixAdjustmentEquation(21, id);
				scoringGrid.addScore(diagCode, diagIdx, 21,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 21, tmpScore);
				break;

			case 17: // Psych 2 - Degenerative and other organic psychiatric disorders
				tmpScore = getCaseMixAdjustmentEquation(22, id);
				scoringGrid.addScore(diagCode, diagIdx, 22,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 22, tmpScore);
				break;

			case 18: // Pulmonary disorders
				tmpScore = getCaseMixAdjustmentEquation(23, id);
				scoringGrid.addScore(diagCode, diagIdx, 23,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 23, tmpScore);

				if (validator.isCUR_AMBULATION_Valid()
                                        && ValidateUtils.isValidValue(
						record.getCRNT_AMBLTN(),
						ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE_FOUR_FIVE_SIX)) {

					tmpScore = getCaseMixAdjustmentEquation(24, id);
					scoringGrid.addScore(diagCode, diagIdx, 24,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 24, tmpScore);
				}
				break;

			case 20: // Skin 2 - Ulcers and other skin conditions
				tmpScore = getCaseMixAdjustmentEquation(28, id);
				scoringGrid.addScore(diagCode, diagIdx, 28,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 28, tmpScore);

			// fall through to Skin 1 since the next criteria could
			// also apply to Skin 2

			case 19: // Skin 1 -Traumatic wounds, burns and post-operative complications
				if (validator.isINTERNAL_LOGIC_Valid()
						&& validator.isTHERAPIES_Valid()
						&& ("1".equals(record.getTHH_IV_INFUSION())
						|| "1".equals(record.getTHH_PAR_NUTRITION()))) {

					tmpScore = getCaseMixAdjustmentEquation(27, id);
					scoringGrid.addScore(diagCode, diagIdx, 27,
							tmpScore, true);
					ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
							diagCode, diagIdx + 1, 27, tmpScore);
				}
				break;

			case 21: // Tracheostomy care
				tmpScore = getCaseMixAdjustmentEquation(29, id);
				scoringGrid.addScore(diagCode, diagIdx, 29,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 29, tmpScore);
				break;

			case 22: // Urostomy/Cystostomy Care
				tmpScore = getCaseMixAdjustmentEquation(30, id);
				scoringGrid.addScore(diagCode, diagIdx, 30,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 30, tmpScore);
				break;

			default:
		}
	}

	/**
	 *
	 * September 2012 - Overriding this method in order to add check for
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
	public int scoreClinical(HomeHealthRecordIF record,
			HomeHealthRecordValidatorIF validator, int scoreOrder[][]) {
		DiagnosisCodeIF diagCode;
		DiagnosisCodeIF pairedDiagCode;
		int totalScore;
		final DiagnosisScoringGridIF scoringGrid = new DiagnosisScoringGrid();

		ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this,
				"Clinical Scoring");

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
				 * payment codes
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

		ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this,
				"Clinical Scoring", totalScore);

		return totalScore;
	}

	/**
	 * This overrides in order to check for primary awarding for Dx in position
	 * 2, with the appropriate V-Code in position 1
	 *
	 * @param record
	 * @param diagCode
	 * @param validator
	 * @param diagIdx
	 * @param scoringGrid
	 */
	public void scoreClinicalCode(HomeHealthRecordIF record,
			DiagnosisCodeIF diagCode, HomeHealthRecordValidatorIF validator,
			int diagIdx, DiagnosisScoringGridIF scoringGrid) {

		ScoringEventFormatter.fireScoringGeneral(grouper.getEventListenersList(), grouper, this,
				"Attempting to score Diagnosis ",
				diagCode.getCode(), " (", diagCode.getDiagnosticGroup().getDescription(),
				") at position ", Integer.toString(diagIdx + 1));

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
			 *      position 1 is an awarding payment code
			 *   6) a manifestation in position 3 if the code in position 2 is
			 *      a paired etiology and awarded primary scoring because 
			 *      their is an awarding payment code in position 1
			 *   7) a manifestation in position 3 if the code in position 2 is
			 *      a paired etiology and the manifestation in position 3 is
			 *      awarded primary scoring because their is an awarding payment code 
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
			ScoringEventFormatter.fireDxGroupAlreadyScored(grouper.getEventListenersList(), grouper, this,
					diagCode, diagIdx + 1, e);
		}
	}

	/**
	 * After the first column (historically column 2) has determined that it is
	 * an Optional payment code that can have a column 3 & 4, this method will score
	 * those codes on the same row, and resolve any manifestation/ etiology
	 * contentions.
	 *
	 * @param record
	 * @param validator
	 * @param rowIdx
	 * @param scoringGrid
	 * @param scoreOrder
	 * @param currentPass
	 */
	public void scoreClinicalColumn3_4(HomeHealthRecordIF record,
			HomeHealthRecordValidatorIF validator,
			int rowIdx,
			DiagnosisScoringGridIF scoringGrid,
			int scoreOrder[][],
			int currentPass) {
		DiagnosisCodeIF diagCodeCol_3 = null;

		if (currentPass == scoreOrder[rowIdx][1]) {
			// score column 3 code if valid for scoring
			diagCodeCol_3 = record.getOptionalDiagnosisCode3(rowIdx);
			if (diagCodeCol_3.isValidForScoring()) {
				scoreClinicalCode(record, diagCodeCol_3, validator, rowIdx + 6, scoringGrid);
			}
		}

		if (currentPass == scoreOrder[rowIdx][2]) {
			// score column 4 code if a valid secondary code
			final DiagnosisCodeIF diagCodeCol_4 = record.getOptionalDiagnosisCode4(rowIdx);
			if (diagCodeCol_4.isValidForScoring()
					&& diagCodeCol_4.isSecondaryOnly()) {

				// make sure that the etiology is valid for this secondary.
				scoreClinicalCode(record, diagCodeCol_4, validator, rowIdx + 12, scoringGrid);
				resolveEtiologyManifestationContention(diagCodeCol_3, rowIdx + 6, diagCodeCol_4, rowIdx + 12, scoringGrid);
			}
		}
	}

	/**
	 * Score the functional part of the model, basically things such as CUR_?
	 * and dressing.
	 *
	 * Pseudo code lines: 1492 thru 1517
	 *
	 * @param record
	 * @param validator
	 * @return the functional score
	 */
	public int scoreFunctional(HomeHealthRecordIF record,
			HomeHealthRecordValidatorIF validator) {
		int score = 0;
		int tmpScore;

		ScoringEventFormatter.fireScoringSectionStart(grouper.getEventListenersList(), grouper, this,
				"Functional scoring");

		if (validator.isCUR_DRESS_Valid()
				&& (ValidateUtils.isValidValue(record.getCRNT_DRESS_UPPER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE)
				|| ValidateUtils.isValidValue(record.getCRNT_DRESS_LOWER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE))) {
			tmpScore = getCaseMixAdjustmentEquation(46, id);
			score += tmpScore;
			ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
					"For CUR_DRESS", 46, tmpScore);
		}

		// MODIFIED FOR OASIS-C BATHING VALUES
		if (validator.isCUR_BATHING_Valid()
				&& ValidateUtils.isValidValue(record.getCRNT_BATHG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE_SIX)) {
			tmpScore = getCaseMixAdjustmentEquation(47, id);
			score += tmpScore;
			ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
					"For CUR_BATHING", 47, tmpScore);
		}

		if (validator.isCUR_TOILETING_Valid()
				&& ValidateUtils.isValidValue(record.getCRNT_TOILTG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR)) {
			tmpScore = getCaseMixAdjustmentEquation(48, id);
			score += tmpScore;
			ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
					"For CUR_TOILETING", 48, tmpScore);
		}

		if (validator.isCUR_TRANSFER_Valid()
				&& ValidateUtils.isValidValue(record.getCRNT_TRNSFRNG(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE)) {
			tmpScore = getCaseMixAdjustmentEquation(49, id);
			score += tmpScore;
			ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
					"For CUR_TRANSFER", 49, tmpScore);
		}

		if (validator.isCUR_AMBULATION_Valid()) {

			if (ValidateUtils.isValidValue(record.getCRNT_AMBLTN(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO_THREE)) {
				// MODIFIED FOR OASIS-C AMBULATION VALUES
				tmpScore = getCaseMixAdjustmentEquation(50, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"For CUR_AMBULATION", 50, tmpScore);

			} else if (ValidateUtils.isValidValue(record.getCRNT_AMBLTN(), ValidateUtils.ARRAY_DOUBLE_FOUR_FIVE_SIX)) {
				// MODIFIED FOR OASIS-C AMBULATION VALUES
				tmpScore = getCaseMixAdjustmentEquation(51, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"For CUR_AMBULATION", 51, tmpScore);
			}
		}
		ScoringEventFormatter.fireScoringSectionFinished(grouper.getEventListenersList(), grouper, this,
				"Functional scoring", score);

		return score;
	}

	/**
	 * Scores only those codes that can not be in the primary position
	 *
	 * @param record
	 * @param diagCode
	 * @param diagIdx
	 * @param scoringGrid
	 */
	public void scoreOtherOnly(HomeHealthRecordIF record,
			DiagnosisCodeIF diagCode, int diagIdx, DiagnosisScoringGridIF scoringGrid) throws AlreadyScoredException {
		int tmpScore;

		// score the Dx Groups that have specific Other diagnosis
		// scoring values from their Primary code scoring values.
		// In all cases, the Group can not be scored twice when
		// the code is in an Other position
		switch (diagCode.getDiagnosticGroup().getId()) {
			case 4:  // Diabetes
				tmpScore = getCaseMixAdjustmentEquation(5, id);
				scoringGrid.addScore(diagCode, diagIdx, 5,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 5, tmpScore);

				// no other scoring is require for this other code
				break;

			case 19: // Skin 1 -Traumatic wounds, burns and post-operative complications
				tmpScore = getCaseMixAdjustmentEquation(26, id);
				scoringGrid.addScore(diagCode, diagIdx, 26,
						tmpScore, true);
				ScoringEventFormatter.fireScoringIncreaseCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 26, tmpScore);

				// this Dx Group has some primary or other code related scoring
				break;
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
				ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 4, tmpScore);

				// this Dx Group has some primary or other code related scoring
				break;

			case 10: // Neuro 1 - Brain disorders and paralysis
				// Neuro 1 does not have an explicit "Other" row, so there
				// is nothing to adjust, just use the primary code as is
				tmpScore = getCaseMixAdjustmentEquation(12, id);
				scoringGrid.addScore(diagCode, diagIdx, 12, tmpScore, false);
				ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 12, tmpScore);

				// no other scoring is require for this primary code
				break;

			case 19: // // Skin 1 -Traumatic wounds, burns and post-operative complications
				tmpScore = calculatePrimaryAdjustment(scoringGrid, 25, 26);
				scoringGrid.addScore(diagCode, diagIdx, 25, tmpScore, false);
				ScoringEventFormatter.fireScoringIncreasePrimaryCodeEvent(grouper.getEventListenersList(), grouper, this,
						diagCode, diagIdx + 1, 25, tmpScore);

				// this Dx Group has some primary or other code related scoring
				break;
		}
	}

	/**
	 * This scores the remaining clinical variables.
	 *
	 * Pseudo code lines: 1435 thru 1490
	 *
	 * @param record
	 * @param validator
	 * @return
	 */
	public int scoreRemainingVariables(HomeHealthRecordIF record,
			HomeHealthRecordValidatorIF validator, int currentScore) {

		int score = 0;
		int tmpScore;

		if (validator.isINTERNAL_LOGIC_Valid()
				&& validator.isTHERAPIES_Valid()) {

			if ("1".equals(record.getTHH_IV_INFUSION())
					|| "1".equals(record.getTHH_PAR_NUTRITION())) {

				tmpScore = getCaseMixAdjustmentEquation(31, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"Special Item THH_IV_INFUSION or THH_PAR_NUTRITION", 31, tmpScore);
			}

			if ("1".equals(record.getTHH_ENT_NUTRITION())) {
				tmpScore = getCaseMixAdjustmentEquation(32, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"Special Item THH_ENT_NUTRITION", 32, tmpScore);
			}
		}

		if (ValidateUtils.isValidValue(record.getVISION(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {
			tmpScore = getCaseMixAdjustmentEquation(33, id);
			score += tmpScore;
			ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
					"Special Item VISION", 33, tmpScore);
		}

		// MODIFIED FOR OASIS-C FREQUENCY PAIN VALUES
		if (ValidateUtils.isValidValue(record.getPAIN_FREQ_ACTVTY_MVMT(), ValidateUtils.ARRAY_DOUBLE_THREE_FOUR)) {
			tmpScore = getCaseMixAdjustmentEquation(34, id);
			score += tmpScore;
			ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
					"Special Item FREQ_PAIN", 34, tmpScore);
		}

		if (validator.isNPRSULC3_Valid()
				&& validator.isNPRSULC4_Valid()) {

			int tmpInt1 = 0;
			int tmpInt2 = 0;
			String tmpStr;

			tmpStr = record.getNBR_PRSULC_STG3();
			if (!ValidateUtils.isEmpty(tmpStr)) {
				tmpInt1 = IntegerUtils.parseInt(tmpStr, 0);
			}
			tmpStr = record.getNBR_PRSULC_STG4();
			if (!ValidateUtils.isEmpty(tmpStr)) {
				tmpInt2 = IntegerUtils.parseInt(tmpStr, 0);
			}

			if (tmpInt1 + tmpInt2 >= 2) {
				tmpScore = getCaseMixAdjustmentEquation(35, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"Special Item NBR_PRSULC_STG3 + NBR_PRSULC_STG4", 35, tmpScore);
			}
		}

		if (validator.isSTGPRSUL_Valid()) {
			if (ValidateUtils.isValidValue(record.getSTG_PRBLM_ULCER(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {
				tmpScore = getCaseMixAdjustmentEquation(36, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"Special Item STG_PRBLM_ULCER = 01 or 02", 36, tmpScore);

			} else if (ValidateUtils.isValidValue(record.getSTG_PRBLM_ULCER(), ValidateUtils.ARRAY_DOUBLE_THREE_FOUR)) {
				tmpScore = getCaseMixAdjustmentEquation(37, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"Special Item STG_PRBLM_ULCER = 03 or 04", 37, tmpScore);
			}
		}

		if (validator.isSTATSTASIS_Valid()) {
			final String tmpStr = record.getSTUS_PRBLM_STAS_ULCR();
			if ("02".equals(tmpStr)) {
				tmpScore = getCaseMixAdjustmentEquation(38, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"Special Item STAT_PRB_STASULC = 02", 38, tmpScore);
			} else if ("03".equals(tmpStr)) {
				tmpScore = getCaseMixAdjustmentEquation(39, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"Special Item STAT_PRB_STASULC = 03", 39, tmpScore);
			}
		}

		if (validator.isSTATSURG_Valid()) {
			final String tmpStr = record.getSTUS_PRBLM_SRGCL_WND();
			if ("02".equals(tmpStr)) {
				tmpScore = getCaseMixAdjustmentEquation(40, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"Special Item STAT_PRB_SURGWND = 02", 40, tmpScore);
			} else if ("03".equals(tmpStr)) {
				tmpScore = getCaseMixAdjustmentEquation(41, id);
				score += tmpScore;
				ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
						"Special Item STAT_PRB_SURGWND = 03", 41, tmpScore);
			}
		}

		if (validator.isDYSPNEIC_Valid()
				&& ValidateUtils.isValidValue(record.getWHEN_DYSPNEIC(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR)) {
			tmpScore = getCaseMixAdjustmentEquation(42, id);
			score += tmpScore;
			ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
					"Special Item WHEN_DYSPNEIC = 02,03 or 04", 42, tmpScore);
		}

		if (validator.isBWLINCONT_Valid()
				&& ValidateUtils.isValidValue(record.getBWL_INCONT(), ValidateUtils.ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE)) {
			tmpScore = getCaseMixAdjustmentEquation(43, id);
			score += tmpScore;
			ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
					"Special Item BWL_INCONT = 02,03,04 or 05", 43, tmpScore);
		}

		if (validator.isOSTOMY_Valid()
				&& ValidateUtils.isValidValue(record.getOSTOMY(), ValidateUtils.ARRAY_DOUBLE_ONE_TWO)) {
			tmpScore = getCaseMixAdjustmentEquation(44, id);
			score += tmpScore;
			ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
					"Special Item OSTOMY = 01 or 02", 44, tmpScore);
		}

		// MODIFIED FOR OASIS-C INJECTABLE MEDS VALUES
		if (validator.isCUR_INJECT_MEDS_Valid()
				&& ValidateUtils.isValidValue(record.getCRNT_MGMT_INJCTN_MDCTN(), ValidateUtils.ARRAY_DOUBLE_ZERO_ONE_TWO_THREE)) {
			tmpScore = getCaseMixAdjustmentEquation(45, id);
			score += tmpScore;
			ScoringEventFormatter.fireScoringIncreaseEvent(grouper.getEventListenersList(), grouper, this,
					"Special Item CUR_INJECT_MEDS = 00, 01 or 02", 45, tmpScore);
		}

		return score;
	}

	@Override
	public void setGrouper(HomeHealthGrouperIF grouper) {
		this.grouper = grouper;
	}

	@Override
	public void setName(String name) {
		// can't really do this
	}

	@Override
	public void preprocessRecord(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, Collection<HomeHealthEventListenerIF> listeners) {
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		//this.id = id;
		// can't really do this
	}
	
	
}
