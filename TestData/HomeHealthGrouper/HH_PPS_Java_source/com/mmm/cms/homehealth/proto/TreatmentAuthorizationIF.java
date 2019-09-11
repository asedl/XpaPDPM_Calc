/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;


import java.util.Calendar;

/**
 * This represents the 18 character code used to report the Oasis Treatment
 * and Authorization for the Grouper results.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface TreatmentAuthorizationIF {

    String DEFAULT_BLANK_VALUE = "                  ";

    /**
     *
     */
    int EPISODE_TIMIMG_EARLY = 1;
    /**
     *
     */
    int EPISODE_TIMIMG_LATE = 2;
    /**
     *
     */
    int EPISODE_TIMIMG_UNKNOWN = 0;

    /**
     * gets the start date of the care
     * @return The start date of care
     */
    Calendar getSTART_CARE_YR34();

    /**
     * Sets the start date of care
     * @param date
     */
    void setSTART_CARE_YR34(Calendar date);

    /**
     * gets the completion date of care
     * @return The completion date of care
     */
    Calendar getINFO_COMPLETED_YR34();

    /**
     * Sets the completion date of care
     * @param date
     */
    void setINFO_COMPLETED_YR34(Calendar date);

    /**
     * gets the assessment reason
     * @return The Assessment Reason
     */
    int getASSMT_REASON();

    /**
     * Sets the assessment Reason
     * @param val
     */
    void setASSMT_REASON(int val);

    /**
     * gets the episode timing indicator
     * @return The Episode timing indicator -  0 for unknown, 1 for early, 2 for late
     */
    int getEPISODE_TIMING();

    /**
     * Sets the Episode timing
     * @param val - must be one of the following: 0 for unknown, 1 for early,
     * 2 for late
     */
    void setEPISODE_TIMING(int val);

    /**
     * gets the number of clinical score, i.e. equations
     * @return The number of clinical scoring values available
     */
    int getClinicalScoreCount();

    /**
     * @return The number functional scoring values available
     */
    int getFunctionalScoreCount();

    /**
     * gets the clinicial scores
     * @return PointsScoringEquationsIF for the clinical score.  This may be null.
     */
    PointsScoringEquationsIF getClinicalScore();

    /**
     * Sets the clinical score.
     * @param score - This may be null.
     */
    void setClinicalScore(PointsScoringEquationsIF score);

    /**
     * gets the clinical score value by equation number
     * @param equationNumber
     * @return using the equation number, return the value of that clinical equation.  If
     * the clinical score is not available, returns 0
     */
    int getClinicalScoreValue(int equationNumber);

    /**
     * gets the functional score
     * @return PointsScoringEquationsIF for the functional score.  This may be null.
     */
    PointsScoringEquationsIF getFunctionalScore();

    /**
     * Sets the functional score.
     * @param score - This may be null.
     */
    void setFunctionalScore(PointsScoringEquationsIF score);

    /**
     * gets the functional score value for an equation
     * @param equationNumber
     * @return using the equation number, return the value of that functional equation.  If
     * the functional score is not available, returns 0
     */
    int getFunctionalScoreValue(int equationNumber);

    /**
     * gets the 18 character treatment authorization code
     * @return The Treatment information as text code performing any conversions
     * as needed. This format may be version specific, and the returned String
     * should always be the same length although it may include all spaces.
     */
    String getAuthorizationCode();

    /**
     * gets the treatment authorization code split into its parts as an array
     * of Strings.
     *
     * @return The Treatment information as text codes, with each authorization
     * item as a separate array item.  This should never return null.
     */
    String[] getAuthorizationCodeParts();
}
