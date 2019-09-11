/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;



/**
 * This holds a single set of scoring values based on the scoring equations
 * for the combinations of:
 *
 *      Episode Timing (M0110)  - UK or 01 as Early, and 02 as Later
 *      Therapy Visits (M0826)   - two main groups of 0-13, and 14+
 *
 * These combine to create 4 separate values, or equations for scoring the
 * case mix.
 *
 * The "equations" are also referred to as a number in the following combination
 * of Episode Timing and Therapy Visits
 *
 *      Early Time:
 *          0-13 Visits = 1
 *          14+ Visits  = 2
 *
 *      Later Time:
 *          0-13 Visits = 3
 *          14+ Visits  = 4
 *
 * The values of each equation can be referred to within either by their name
 * or by their equation number.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface PointsScoringEquationsIF {

    /**
     * Adds the value of the equations from points
     * to the corresponding equations in this object.
     *
     * @param points
     */
    void add(PointsScoringEquationsIF points);

    /**
     * Adds the value to the Early timing, 13 and under visits
     * Also referred as equation 1
     *
     * @param equation
     */
    void addToEarly13AndUnder(int equation);

    /**
     * Adds the value to Early timing, 14 and more visits
     * Also referred as equation 2
     *
     * @param equation
     */
    void addToEarly14Plus(int equation);

    /**
     * Adds the value to the Later timing, 13 and under visits
     * Also referred as equation 3
     *
     * @param equation
     */
    void addToLater13AndUnder(int equation);

    /**
     * Adds the value to Later timing, 14 and more visits
     * Also referred as equation 4
     *
     * @param equation
     */
    void addToLater14Plus(int equation);

    /**
     * Get the value of Early timing, 13 and under visits
     * Also referred as equation 1
     *
     * @return the value of equation1
     */
    int getEarly13AndUnder();

    /**
     * Get the value of Early timing, 14 and more visits
     * Also referred as equation 2
     *
     * @return the value of equation2
     */
    int getEarly14Plus();

    /**
     * Get the value of Later timing, 13 and under visits
     * Also referred as equation 3
     *
     * @return the value of equation3
     */
    int getLater13AndUnder();

    /**
     * Get the value of Later timing, 14 and more visits
     * Also referred as equation 4
     *
     * @return the value of equation4
     */
    int getLater14Plus();

    /**
     * Set the value of Early timing, 13 and under visits
     * Also referred as equation 1
     *
     * @param equation1 new value of equation1
     */
    void setEarly13AndUnder(int equation1);

    /**
     * Set the value of Early timing, 14 and more visits.
     * Also referred as equation 2
     *
     * @param equation2 new value of equation2
     */
    void setEarly14Plus(int equation2);

    /**
     * Set the value of Later timing, 13 and under visits
     * Also referred as equation 3
     *
     * @param equation3 new value of equation3
     */
    void setLater13AndUnder(int equation3);

    /**
     * Set the value of Later timing, 14 and more visits
     * Also referred as equation 4
     *
     * @param equation
     */
    void setLater14Plus(int equation);

    /**
     * the value of the equation at that index.
     * @param idx - a 0 based index of equations
     * @return the value of the equation at that index.
     */
    int getEquationValue(int idx);

    /**
     * @param idx - a 0 based index of equations
     * @param value - the new value
     */
    void setEquationValue(int idx, int value);

}
