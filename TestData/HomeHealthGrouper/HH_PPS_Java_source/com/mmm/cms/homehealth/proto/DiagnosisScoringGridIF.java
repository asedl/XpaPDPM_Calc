/*
 *  This is an unpublished work containing 3M confidential and
 *  proprietary information. Disclosure or reproduction without the
 *  written  authorization of 3M is prohibited.  If publication occurs,
 *  the following notice applies:
 *
 *  Copyright (C) 1998-2003, 3M All rights reserved.
 *
 */

package com.mmm.cms.homehealth.proto;


/**
 * This provides a holder for the scores for the diagnosis codes.  It is 
 * responsible to ensure that no Case Mix Adjustment row or Diagnostic
 * Group is scored more than once.  It also tracks when an etiology
 * has scored by winning a etiology/manifestation contention.
 *
 * @author  3M HIS, Tim Gallagher
 */
public interface DiagnosisScoringGridIF {

    /**
     * Add a score to the grid. This performs a check on the caseMixAdjustmentRow
     * to make sure the row has not already scored for the diagnostic
     * group associated with the code.
     *
     * @param diagnosisCode
     * @param diagnosisIdx
     * @param caseMixAdjustmentRow
     * @param score
     * @param checkDiagnosticGroup - tells the scoring grid to check (true)
     *  duplicate entries for DiagnosticGroup, or not to check (false)
     * @throws com.mmm.cms.homehealth.proto.AlreadyScoredException
     */
    void addScore(DiagnosisCodeIF diagnosisCode, int diagnosisIdx,
            int caseMixAdjustmentRow, int score, boolean checkDiagnosticGroup) throws AlreadyScoredException;

    /**
     * removes all the current scores so that this object can be reused
     * @param diagnosisIdx
     */
    void clearScore(int diagnosisIdx);

    /**
     * gets the flag indicating that the diagnosis code scored due to 
     * winning a etiology/manifestation contention
     * 
     * @param diagnosisIdx - zero based index of the diagnosis code
     * @return
     */
    boolean getScoredByWinningContention(int diagnosisIdx);

    /**
     * Returns the summed score of all the items scored
     * @return
     */
    int getTotalScore();

    /**
     * gets the total score for a single diagnosis
     * @param diagnosisIdx
     * @return
     */
    int getTotalScoreForDiagnosis(int diagnosisIdx);

    /**
     * Returns true when the case mix adjustment row has already been scored
     * @param rowId
     * @return
     */
    boolean isCaseMixAdjustmentRowScored(int rowId);

    /**
     * Returns true when the diagnostic group has already scored
     *
     * @param diagnosisIdx
     * @param groupId
     * @return
     */
    boolean isDiagnosticGroupScored(int diagnosisIdx, int groupId);

    /**
     *
     * @param diagnosisCode
     * @param diagnosisIdx
     * @param caseMixAdjustmentRow
     * @param score
     * @param checkDiagnosticGroup - tells the scoring grid to check (true)
     *  duplicate entries for DiagnosticGroup, or not to check (false)
     * @throws com.mmm.cms.homehealth.proto.AlreadyScoredException
     */
    void setScore(DiagnosisCodeIF diagnosisCode, int diagnosisIdx,
            int caseMixAdjustmentRow, int score, boolean checkDiagnosticGroup) throws AlreadyScoredException;

    /**
     * sets the flag indicating that the diagnosis code scored due to
     * winning a etiology/manifestation contention
     *
     * @param diagnosisIdx - zero based index of the diagnosis code
     * @param flag - true if the score was based on winning a contention
     * @return
     */
    void setScoredByWinningContention(int diagnosisIdx, boolean flag);


}
