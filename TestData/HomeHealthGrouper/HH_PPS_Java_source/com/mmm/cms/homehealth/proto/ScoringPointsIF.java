/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;



/**
 * Holds points accumulated by the Scoring Model.  It is up to the model
 * to determine how many scores should be held within.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface ScoringPointsIF {

    /**
     * gets the int array of scores.  Should be only as many ints are the scoring
     * provided, i.e. as of version 2.03 clinical/functional has 4, NRS has 1
     *
     * @return an array of integers representing scoring equations. The number
     * of equations depends on the scoring model that created this.
     */
    int[] getScores();

    /**
     * Sets the array of integers representing the scoring equations
     * @param scores
     */
    void setScores(int scores[]);

    /**
     * Sets the scoring equation at a specific position within the array
     * @param idx
     * @param value
     */
    void setScoreAt(int idx, int value);
}
