/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.homehealth.proto.TreatmentAuthorizationIF;
import com.mmm.cms.util.HexavigesimalCalendarFormat;
import com.mmm.cms.util.HexavigesimalFormat;
import com.mmm.cms.util.IntegerFormat;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ValidateUtils;
import java.util.Calendar;

/**
 * This holds the parts of the OASIS Treatment Authorization code and the
 * getAuthorizationCode() converts those parts into the 18 character string for
 * the Authorization output.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class TreatmentAuthorization implements TreatmentAuthorizationIF {

    /**
     * Hexavigesimal Calendar Formatter
     */
    public static final HexavigesimalCalendarFormat HEXAVIG_CALENDAR_FORMAT;
    /**
     * Hexavigesimal Formatter for numbers
     */
    public static final HexavigesimalFormat HEXAVIG_INTEGER_FORMAT;
    protected boolean blank;

    static {

        HEXAVIG_CALENDAR_FORMAT = new HexavigesimalCalendarFormat();

        HEXAVIG_INTEGER_FORMAT = new HexavigesimalFormat();
        HEXAVIG_INTEGER_FORMAT.setMaximumIntegerDigits(1);
    }

    protected Calendar startOfCareDate;
    protected Calendar infoCompletedDate;
    protected int assessmentReason;
    protected int episodeTiming;
    protected PointsScoringEquationsIF clinicalScore;
    protected PointsScoringEquationsIF functionalScore;

    public TreatmentAuthorization(HomeHealthRecordIF record) {
        this(record, null, null, null);
    }
    
    /**
     * Constructor that sets up the episode timing and dates prior to creating
     * the code
     *
     * @param record
     * @param validator
     * @param clinScore
     * @param funcScore
     */
    public TreatmentAuthorization(HomeHealthRecordIF record,
            HomeHealthRecordValidatorIF validator,
            PointsScoringEquationsIF clinScore,
            PointsScoringEquationsIF funcScore) {

        String timing;

        clinicalScore = clinScore;
        functionalScore = funcScore;

        //-----------------------------------
        // Check for valid record
        //-----------------------------------
        if (record == null) {
            blank = true;
            return;
        }

        timing = record.getEPISODE_TIMING();
        if (clinicalScore == null || functionalScore == null
                || validator != null
                && (!validator.isINFO_COMPLETED_DT_Valid()
                || !validator.isASSMT_REASON_Valid()
                || "1".equals(record.getTHER_NEED_NA())
                || !ValidateUtils.isValidValue(timing, ValidateUtils.ARRAY_DOUBLE_ONE_TWO)
                && !ValidateUtils.UNKNOWN_UK.equals(timing))) {
            blank = true;
            return;
        }

        startOfCareDate = record.getSTART_CARE_DT();
        infoCompletedDate = record.getINFO_COMPLETED_DT();

        assessmentReason = IntegerUtils.parseInt(record.getASSMT_REASON(), 0);

        episodeTiming = IntegerUtils.parseInt(record.getEPISODE_TIMING(), 1);
    }
    
    /**
     * gets the assessment reason
     *
     * @return the assessment reason value
     */
    @Override
    public int getASSMT_REASON() {
        return assessmentReason;
    }

    /**
     * Sets the assessment reason
     *
     * @param M0100_ASSMT_REASON2
     */
    @Override
    public void setASSMT_REASON(int M0100_ASSMT_REASON2) {
        this.assessmentReason = M0100_ASSMT_REASON2;
    }

    /**
     * gets the startOfCareDate
     *
     * @return the start date
     */
    @Override
    public Calendar getSTART_CARE_YR34() {
        return startOfCareDate;
    }

    /**
     * Sets the start date
     *
     * @param startOfCareDate
     */
    @Override
    public void setSTART_CARE_YR34(Calendar M0030_START_CARE_YR34) {
        this.startOfCareDate = M0030_START_CARE_YR34;
    }

    /**
     * gets the infoCompletedDate
     *
     * @return the completed date
     */
    @Override
    public Calendar getINFO_COMPLETED_YR34() {
        return infoCompletedDate;
    }

    /**
     * Sets the completed date
     *
     * @param infoCompletedDate
     */
    @Override
    public void setINFO_COMPLETED_YR34(Calendar M0090_INFO_COMPLETED_YR34) {
        this.infoCompletedDate = M0090_INFO_COMPLETED_YR34;
    }

    /**
     * gets the episodeTiming
     *
     * @return the episode timing
     */
    @Override
    public int getEPISODE_TIMING() {
        return episodeTiming;
    }

    /**
     * Sets the episode timing
     *
     * @param episodeTiming
     */
    @Override
    public void setEPISODE_TIMING(int M0110_EPISODE_TIMING) {
        this.episodeTiming = M0110_EPISODE_TIMING;
    }

    /**
     * gets the ClinicalScore
     *
     * @return clinical scoring values
     */
    @Override
    public PointsScoringEquationsIF getClinicalScore() {
        return clinicalScore;
    }

    /**
     * Sets the clinical scoring values
     *
     * @param clinicalScore
     */
    @Override
    public void setClinicalScore(PointsScoringEquationsIF clinicalScore) {
        this.clinicalScore = clinicalScore;
    }

    /**
     * gets the FunctionalScore
     *
     * @return the functional scoring values
     */
    @Override
    public PointsScoringEquationsIF getFunctionalScore() {
        return functionalScore;
    }

    /**
     * Sets the functional scoring values
     *
     * @param functionalScore
     */
    @Override
    public void setFunctionalScore(PointsScoringEquationsIF functionalScore) {
        this.functionalScore = functionalScore;
    }

    /**
     * gets the Clinical Score number of equation
     *
     * @return the number of scoring equations. If the clinical score is not
     * null, then it returns 4, otherwise 0
     */
    @Override
    public int getClinicalScoreCount() {
        return clinicalScore != null ? 4 : 0;
    }

    /**
     * gets the Functional Score number of equation
     *
     * @return the number of scoring equations. If the functional score is not
     * null, then it returns 4, otherwise 0
     */
    @Override
    public int getFunctionalScoreCount() {
        return functionalScore != null ? 4 : 0;
    }

    /**
     * gets the Clinical score of a specific equation
     *
     * @param equationNumber
     * @return the individual clinical score based on the equation number
     */
    @Override
    public int getClinicalScoreValue(int equationNumber) {
        return getScoreValue(clinicalScore, equationNumber);
    }

    /**
     * gets the Functional score of a specific equation
     *
     * @param equationNumber
     * @return the individual functional score based on the equation number
     */
    @Override
    public int getFunctionalScoreValue(int equationNumber) {
        return getScoreValue(functionalScore, equationNumber);
    }

    /**
     * Generic method for retrieving the equation score for a supplied equation.
     *
     * @param score
     * @param equationNumber
     * @return the score, or 0 if the equation is not found
     */
    protected final int getScoreValue(PointsScoringEquationsIF score,
            int equationNumber) {
        int retval;

        if (score != null) {
            switch (equationNumber) {
                case 1:
                    retval = score.getEarly13AndUnder();
                    break;
                case 2:
                    retval = score.getEarly14Plus();
                    break;
                case 3:
                    retval = score.getLater13AndUnder();
                    break;
                case 4:
                    retval = score.getLater14Plus();
                    break;
				default:
					 retval = 0;
            }
        } else {
			retval = 0;
		}
	
        return retval;
    }

    /**
     * Creates a authorization code base on the following:
     *
     *
     * 1.   Set M0030_YY = startOfCareDate (99) 
     * 2.   Set M0030_DATE_CODE = value returned from searching 
     *   Table 8: Hexavigesimal Code Tables (converting dates and point values 
     *   to letter codes) - Part 1 – DATES in the Appendix with value of 
     *   M0030_START_CARE_MMDD (XX) 
     * 3.   Set M0090_YY = infoCompletedDate (99)
     * 4.   Set M0090_DATE_CODE = value returned from searching 
     *   Table 8: Hexavigesimal Code Tables (converting dates and point values 
     *   to letter codes) - Part 1 – DATES in the Appendix with value of
     *   M0090_INFO_COMPLETED_MMDD (XX)
     * 5.   Set M0100_1 = M0100_ASSMT_REASON2 (X)
     * 6.   If episodeTiming = UK or 01, M0110_1_2 = 1 Else, If episodeTiming =
     * 02, M0110_1_2 = 2 (X) 7.	Set CLIN_SCORE1_CD = value returned from
     * searching Table 8: Hexavigesimal Code Tables (converting dates and point
     * values to letter codes) - Part 2 – POINTS in the Appendix with value of
     * CLIN_SCORE1 (X)
     * 8.   Set FUNC_SCORE1_CD = value returned from searching 
     * Table 8: Hexavigesimal Code Tables (converting dates and point values to
     * letter codes) - Part 2 – POINTS in the Appendix with value of
     * FUNC_SCORE1 (X) 
     * 9.   Set CLIN_SCORE2_CD = value returned from searching
     * Table 8: Hexavigesimal Code Tables (converting dates and point values to
     * letter codes) - Part 2 – POINTS in the Appendix with value of
     * CLIN_SCORE2 (X)
     * 10.  Set FUNC_SCORE2_CD = value returned from searching
     * Table 8: Hexavigesimal Code Tables (converting dates and point values to
     * letter codes) - Part 2 – POINTS in the Appendix with value of
     * FUNC_SCORE2 (X)
     * 11.  Set CLIN_SCORE3_CD = value returned from searching
     * Table 8: Hexavigesimal Code Tables (converting dates and point values to
     * letter codes) - Part 2 – POINTS in the Appendix with value of
     * CLIN_SCORE3 (X)
     * 12.  Set FUNC_SCORE3_CD = value returned from searching
     * Table 8: Hexavigesimal Code Tables (converting dates and point values to
     * letter codes) - Part 2 – POINTS in the Appendix with value of
     * FUNC_SCORE3 (X)
     * 13.  Set CLIN_SCORE4_CD = value returned from searching
     * Table 8: Hexavigesimal Code Tables (converting dates and point values to
     * letter codes) - Part 2 – POINTS in the Appendix with value of
     * CLIN_SCORE4 (X)
     * 14.  Set FUNC_SCORE4_CD = value returned from searching
     * Table 8: Hexavigesimal Code Tables (converting dates and point values to
     * letter codes) - Part 2 – POINTS in the Appendix with value of
     * FUNC_SCORE4 (X)
     *
     * @return non-null 30 character String that may be blank if the authorization can not
     * be determined
     */
    @Override
    public String getAuthorizationCode() {
		String value;
		
        if (blank) {
            value = "                  ";
        } else {
            final StringBuilder buffer = new StringBuilder(18);

            buffer.append(IntegerFormat.INTEGER_FORMAT_DIGITS_2.format(startOfCareDate.get(Calendar.YEAR)
                    % 100));
            buffer.append(HEXAVIG_CALENDAR_FORMAT.format(startOfCareDate));

            buffer.append(IntegerFormat.INTEGER_FORMAT_DIGITS_2.format(infoCompletedDate.get(Calendar.YEAR)
                    % 100));
            buffer.append(HEXAVIG_CALENDAR_FORMAT.format(infoCompletedDate));

            if (assessmentReason > 0) {
                buffer.append(assessmentReason);
            } else {
                buffer.append(" ");
            }

            buffer.append(episodeTiming);

            buffer.append(HEXAVIG_INTEGER_FORMAT.format(clinicalScore.getEarly13AndUnder()));
            buffer.append(HEXAVIG_INTEGER_FORMAT.format(functionalScore.getEarly13AndUnder()));
            buffer.append(HEXAVIG_INTEGER_FORMAT.format(clinicalScore.getEarly14Plus()));
            buffer.append(HEXAVIG_INTEGER_FORMAT.format(functionalScore.getEarly14Plus()));
            buffer.append(HEXAVIG_INTEGER_FORMAT.format(clinicalScore.getLater13AndUnder()));
            buffer.append(HEXAVIG_INTEGER_FORMAT.format(functionalScore.getLater13AndUnder()));
            buffer.append(HEXAVIG_INTEGER_FORMAT.format(clinicalScore.getLater14Plus()));
            buffer.append(HEXAVIG_INTEGER_FORMAT.format(functionalScore.getLater14Plus()));

            value = buffer.toString();
        }
		return value;
    }

    /**
     * gets the authorization code split into an array of Strings for its
     * individual pieces
     *
     * @return The Treatment information as text codes, with each authorization
     * item as a separate array item. This should never return null.
     */
    @Override
    public String[] getAuthorizationCodeParts() {
        final String parts[] = new String[14];
        final String code = getAuthorizationCode();

        parts[0] = code.substring(0, 2);
        parts[1] = code.substring(2, 4);
        parts[2] = code.substring(4, 6);
        parts[3] = code.substring(6, 8);
        parts[4] = code.substring(8, 9);
        parts[5] = code.substring(9, 10);
        parts[6] = code.substring(10, 11);
        parts[7] = code.substring(11, 12);
        parts[8] = code.substring(12, 13);
        parts[9] = code.substring(13, 14);
        parts[10] = code.substring(14, 15);
        parts[11] = code.substring(15, 16);
        parts[12] = code.substring(16, 17);
        parts[13] = code.substring(17);

        return parts;
    }

    /**
     * Same as getAuthorizationCode()
     *
     * @return
     */
    @Override
    public String toString() {
        return getAuthorizationCode();
    }
}
