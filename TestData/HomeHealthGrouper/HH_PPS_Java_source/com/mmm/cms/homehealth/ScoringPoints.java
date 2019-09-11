/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.ScoringPointsIF;

/**
 * Holds the array integers representing the scores of a set of scoring
 * equations.  The number of equations may differ depending on the Scroring
 * Model that created it.  The main use is to provide scoring values within the
 * scoring models.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class ScoringPoints implements ScoringPointsIF {

    private int scores[];

    /**
     * Constructor that creates the integer array with a specific size
     * @param count
     */
    public ScoringPoints(int count) {
        if (count <= 0) {
            count = 1;
		}

        scores = new int[count];
    }

    /**
     * gets an array of int for scores
     * @return an array of integers representing scoring equations. The number
     * of equations depends on the scoring model that created this.
     */
    @Override
    public int[] getScores() {
        return scores;
    }

    /**
     * Sets the array of integers representing the scoring equations
     * @param scores
     */
    @Override
    public void setScores(int[] scores) {
        this.scores = scores;
    }

    /**
     * Sets the scoring equation at a specific position within the array
     * @param idx
     * @param value
     */
    @Override
    public void setScoreAt(int idx, int value) {
        scores[idx] = value;
    }

}
