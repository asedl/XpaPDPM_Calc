/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.AlreadyScoredException;
import com.mmm.cms.homehealth.proto.DiagnosisScoringGridIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import java.util.ArrayList;
import java.util.List;

/**
 * This holds a series of Scoring Items (see inner class below) for all the
 * diagnosis codes that are scored. Using ScoringItem allows this object to
 * track each case mix row that a Diagnosis code scores on. Determining the
 * total score for a Diagnosis code requires looking at all the ScoreItems in
 * this object.
 *
 * The job of this object is to keep track of diagnosis code scores and prevents
 * a Diagnosis Group or a Case Mix Adjustment row from scoring more than once.
 * The reason for the introduction of this new object is to allow the scoring
 * modules remove complicated code related to tracking when a Diagnosis Group or
 * Case Mix Adjustment row has already scored. The scoring module can use a
 * simplified logic to determine if the Diagnosis code is eligible for scoring.
 * When the value is added to the Diagnosis Scoring Grid, the grid will
 * determine if score is allowed based on previously scored Groups or Case Mix
 * rows.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class DiagnosisScoringGrid implements DiagnosisScoringGridIF {

	private ScoringItem scores[];
	private transient int totalScore;
	private boolean scoredByWinningContention[];

	public DiagnosisScoringGrid() {
		scores = new ScoringItem[0];
		scoredByWinningContention = new boolean[18];
	}

	/**
	 * Adds scoring information about a specific position in the scoring grid
	 * ensuring that the case mix row is not scored twice, optionally checking
	 * that the diagnostic group is not scored twice.
	 *
	 * @param diagnosisCode
	 * @param diagnosisIdx
	 * @param caseMixAdjustmentRow
	 * @param score
	 * @param checkDiagnosticGroup
	 * @throws com.mmm.cms.homehealth.proto.AlreadyScoredException
	 */
	@Override
	public void addScore(DiagnosisCodeIF diagnosisCode, int diagnosisIdx,
			int caseMixAdjustmentRow, int score, boolean checkDiagnosticGroup) throws
			AlreadyScoredException {
		int diagnosticGroupId = diagnosisCode.getDiagnosticGroup().getId();
		boolean scoredDxGroup;
		boolean scoredRow;

		// Make sure that the row and diagnostic group have not already been
		// scored by another code already
		scoredDxGroup = checkDiagnosticGroup ? isDiagnosticGroupScored(diagnosisIdx, diagnosticGroupId) : false;
		scoredRow = isCaseMixAdjustmentRowScored(caseMixAdjustmentRow);
		if (scoredDxGroup || scoredRow) {

			// another code has already scored the row or diagnostic group
			throw new AlreadyScoredException(
					scoredDxGroup ? diagnosticGroupId : 0,
					scoredRow ? caseMixAdjustmentRow : 0,
					diagnosisCode);
		} else {
			// increase the array and add up the score
			ScoringItem tmpScores[] = new ScoringItem[scores.length + 1];
			System.arraycopy(scores, 0, tmpScores, 0, scores.length);
			tmpScores[scores.length] = new ScoringItem(diagnosisIdx, diagnosticGroupId, caseMixAdjustmentRow, score);
			scores = tmpScores;
			this.totalScore += score;
		}
	}

	@Override
	public void clearScore(int diagnosisIdx) {
		// first remove any scoring for the diagnosisIdx, by creating
		// a new list
		int idx = this.scores.length;
		if (idx > 0) {
			List<ScoringItem> tmpScores = new ArrayList<ScoringItem>();
			ScoringItem item;

			// while removing the item, rescore the total
			this.totalScore = 0;
			while (idx-- > 0) {
				item = this.scores[idx];
				if (item.getDiagnosisIdx() != diagnosisIdx) {
					tmpScores.add(item);
					this.totalScore += item.getScore();
				}
			}

			if (tmpScores.size() > 0) {
				scores = new ScoringItem[tmpScores.size()];
				tmpScores.toArray(scores);
			} else {
				scores = new ScoringItem[0];
			}
		}
	}

	/**
	 * gets the flag for winning contention
	 *
	 * @param diagnosisIdx
	 * @return
	 */
	@Override
	public boolean getScoredByWinningContention(int diagnosisIdx) {
		return scoredByWinningContention[diagnosisIdx];
	}

	/**
	 * gets the total score by adding all the individual items
	 *
	 * @return
	 */
	@Override
	public int getTotalScore() {
		return this.totalScore;
	}

	@Override
	public int getTotalScoreForDiagnosis(int diagnosisIdx) {
		int score = 0;
		ScoringItem item;
		int idx = scores.length;

		while (idx-- > 0) {
			item = scores[idx];
			if (diagnosisIdx == item.getDiagnosisIdx()) {
				score += item.getScore();
			}
		}
		return score;
	}

	/**
	 * This determines if the Diagnosis Group has already scored or not.
	 *
	 * @param diagnosisIdx
	 * @param groupId
	 * @return
	 */
	@Override
	public boolean isDiagnosticGroupScored(int diagnosisIdx, int groupId) {
		ScoringItem item;
		int idx = scores.length;
		boolean isScored = false;

		while (idx-- > 0) {
			item = scores[idx];
			if (item.getDiagnosisIdx() != diagnosisIdx && item.getDiagnosticGroupId() == groupId) {
				isScored = true;
				break;
			}
		}
		return isScored;
	}

	/**
	 * Determines if the Case Mix Adjustment row has already been scored
	 *
	 * @param caseMixAdjustmentRow
	 * @return true if the row has been scored
	 */
	@Override
	public boolean isCaseMixAdjustmentRowScored(int caseMixAdjustmentRow) {
		ScoringItem item;
		int idx = scores.length;
		boolean isScored = false;

		while (idx-- > 0) {
			item = scores[idx];
			if (item.getCaseMixAdjustmentRow() == caseMixAdjustmentRow) {
				isScored = true;
				break;
			}
		}
		return isScored;
	}

	@Override
	public void setScore(DiagnosisCodeIF diagnosisCode, int diagnosisIdx,
			int caseMixAdjustmentRow, int score, boolean checkDiagnosticGroup)
			throws AlreadyScoredException {

		clearScore(diagnosisIdx);
		addScore(diagnosisCode, diagnosisIdx, caseMixAdjustmentRow, score, checkDiagnosticGroup);
	}

	@Override
	public void setScoredByWinningContention(int diagnosisIdx, boolean scoredByWinningContention) {
		this.scoredByWinningContention[diagnosisIdx] = scoredByWinningContention;
	}

	/**
	 * This holds a scoring element relating the score to a Diagnostic Group,
	 * Case Mix Adjustment row and Diagnosis position.
	 *
	 */
	class ScoringItem {

		protected int score;
		protected int diagnosticGroupId;
		protected int caseMixAdjustmentRow;
		protected int diagnosisIdx;

		/**
		 *
		 * @param score
		 * @param diagnosisIdx
		 * @param diagnosticGroupId
		 * @param caseMixAdjustmentRow
		 */
		public ScoringItem(int diagnosisIdx,
				int diagnosticGroupId,
				int caseMixAdjustmentRow,
				int score) {
			this.score = score;
			this.diagnosticGroupId = diagnosticGroupId;
			this.caseMixAdjustmentRow = caseMixAdjustmentRow;
			this.diagnosisIdx = diagnosisIdx;
		}

		/**
		 * Get the value of diagnosisIdx
		 *
		 * @return the value of diagnosisIdx
		 */
		public int getDiagnosisIdx() {
			return diagnosisIdx;
		}

		/**
		 * Set the value of diagnosisIdx
		 *
		 * @param diagnosisIdx new value of diagnosisIdx
		 */
		public void setDiagnosisIdx(int diagnosisIdx) {
			this.diagnosisIdx = diagnosisIdx;
		}

		/**
		 * Get the value of caseMixAdjustmentRow
		 *
		 * @return the value of caseMixAdjustmentRow
		 */
		public int getCaseMixAdjustmentRow() {
			return caseMixAdjustmentRow;
		}

		/**
		 * Set the value of caseMixAdjustmentRow
		 *
		 * @param caseMixAdjustmentRow new value of caseMixAdjustmentRow
		 */
		public void setCaseMixAdjustmentRow(int caseMixAdjustmentRow) {
			this.caseMixAdjustmentRow = caseMixAdjustmentRow;
		}

		/**
		 * Get the value of diagnosticGroupId
		 *
		 * @return the value of diagnosticGroupId
		 */
		public int getDiagnosticGroupId() {
			return diagnosticGroupId;
		}

		/**
		 * Set the value of diagnosticGroupId
		 *
		 * @param diagnosticGroupId new value of diagnosticGroupId
		 */
		public void setDiagnosticGroupId(int diagnosticGroupId) {
			this.diagnosticGroupId = diagnosticGroupId;
		}

		/**
		 * Get the value of score
		 *
		 * @return the value of score
		 */
		public int getScore() {
			return score;
		}

		/**
		 * Set the value of score
		 *
		 * @param score new value of score
		 */
		public void setScore(int score) {
			this.score = score;
		}
	}
}
