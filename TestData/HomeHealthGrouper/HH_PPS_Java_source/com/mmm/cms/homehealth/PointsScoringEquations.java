/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;

/**
 * This holds scoring for the clinical/functional models which is concerned
 * scoring based on the number of episodes and the timing of the submission.
 * In earlier versions, these were simply equation 1 thru 4, but have been
 * named in this version as:
 * 
 * <ul>
 *     <li>early13AndUnder</li>
 *     <li>early14Plus</li>
 *     <li>later13AndUnder</li>
 *     <li>later14Plus</li>
 * </ul>
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class PointsScoringEquations implements PointsScoringEquationsIF {

    private int early13AndUnder;
    private int early14Plus;
    private int later13AndUnder;
    private int later14Plus;

    /**
     * Constructor setting the scores to a default of 0
     */
    public PointsScoringEquations() {
    }

    /**
     * Adds all the points from the scoring equation to this scoring equation
     * @param points
     */
    @Override
    public void add(PointsScoringEquationsIF points) {
        if (points != null) {
            early13AndUnder += points.getEarly13AndUnder();
            early14Plus += points.getEarly14Plus();
            later13AndUnder += points.getLater13AndUnder();
            later14Plus += points.getLater14Plus();
        }
    }

    /**
     * Get the value of equation1
     *
     * @return the value of equation1
     */
    @Override
    public int getEarly13AndUnder() {
        return early13AndUnder;
    }

    /**
     * Set the value of equation1
     *
     * @param equation1 new value of equation1
     */
    @Override
    public void setEarly13AndUnder(int equation1) {
        this.early13AndUnder = equation1;
    }

    /**
     * Adds the value to equation1
     *
     * @param equation
     */
    @Override
    public void addToEarly13AndUnder(int equation) {
        this.early13AndUnder += equation;
    }

    /**
     * Get the value of equation2
     *
     * @return the value of equation2
     */
    @Override
    public int getEarly14Plus() {
        return early14Plus;
    }

    /**
     * Set the value of equation2
     *
     * @param equation2 new value of equation2
     */
    @Override
    public void setEarly14Plus(int equation2) {
        this.early14Plus = equation2;
    }

    /**
     * Adds the value to equation2
     *
     * @param equation
     */
    @Override
    public void addToEarly14Plus(int equation) {
        this.early14Plus += equation;
    }

    /**
     * Get the value of equation3
     *
     * @return the value of equation3
     */
    @Override
    public int getLater13AndUnder() {
        return later13AndUnder;
    }

    /**
     * Set the value of equation3
     *
     * @param equation3 new value of equation3
     */
    @Override
    public void setLater13AndUnder(int equation3) {
        this.later13AndUnder = equation3;
    }

    /**
     * Adds the value to equation3
     *
     * @param equation
     */
    @Override
    public void addToLater13AndUnder(int equation) {
        this.later13AndUnder += equation;
    }

    /**
     * Get the value of equation4
     *
     * @return the value of equation4
     */
    @Override
    public int getLater14Plus() {
        return later14Plus;
    }

    /**
     * Set the value of equation4
     *
     * @param equation
     */
    @Override
    public void setLater14Plus(int equation) {
        this.later14Plus = equation;
    }

    /**
     * Adds the value to equation4
     *
     * @param equation
     */
    @Override
    public void addToLater14Plus(int equation) {
        this.later14Plus += equation;
    }

    /**
     * Returns the equation value at the 0-based index
     * @param idx
     * @return equation value or 0
     */
    @Override
    public int getEquationValue(int idx) {
		int value;
		
        switch (idx) {
            case 0:
                value = early13AndUnder;
				break;
				
            case 1:
                value = early14Plus;
				break;
            case 2:
                value = later13AndUnder;
				break;
            case 3:
                value = later14Plus;
				break;
				
			default: 
				value = 0;
        }
        return value;
    }

    /**
     * Sets the equation value at the 0-based index, with the value
     * @param idx
     */
    @Override
    public void setEquationValue(int idx, int value) {
        switch (idx) {
            case 0:
                early13AndUnder = value;
                break;

            case 1:
                early14Plus = value;
                break;

            case 2:
                later13AndUnder = value;
                break;

            case 3:
                later14Plus = value;
                break;
				
			default:

        }
    }

}
