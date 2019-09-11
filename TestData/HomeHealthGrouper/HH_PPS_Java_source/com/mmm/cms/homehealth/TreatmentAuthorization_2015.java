/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import static com.mmm.cms.homehealth.TreatmentAuthorization.HEXAVIG_CALENDAR_FORMAT;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordValidatorIF;
import com.mmm.cms.homehealth.proto.PointsScoringEquationsIF;
import com.mmm.cms.util.IntegerFormat;
import java.util.Calendar;

/**
 * Changes the TAC to use A=0, B=1, C=2, etc instead of the original A=0 or 1,
 * B=1, C=2, etc
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class TreatmentAuthorization_2015 extends TreatmentAuthorization {

    public TreatmentAuthorization_2015(HomeHealthRecordIF record) {
        super(record);
    }

    public TreatmentAuthorization_2015(HomeHealthRecordIF record, HomeHealthRecordValidatorIF validator, PointsScoringEquationsIF clinScore, PointsScoringEquationsIF funcScore) {
        super(record, validator, clinScore, funcScore);
    }

    /**
     * Creates a authorization code base on the following:
     *
     *
     * 1. Set M0030_YY = startOfCareDate (99) 2. Set M0030_DATE_CODE = value
     * returned from searching Table 8: Hexavigesimal Code Tables (converting
     * dates and point values to letter codes) - Part 1 – DATES in the Appendix
     * with value of M0030_START_CARE_MMDD (XX) 3. Set M0090_YY =
     * infoCompletedDate (99) 4. Set M0090_DATE_CODE = value returned from
     * searching Table 8: Hexavigesimal Code Tables (converting dates and point
     * values to letter codes) - Part 1 – DATES in the Appendix with value of
     * M0090_INFO_COMPLETED_MMDD (XX) 5. Set M0100_1 = M0100_ASSMT_REASON2 (X)
     * 6. If episodeTiming = UK or 01, M0110_1_2 = 1 Else, If episodeTiming =
     * 02, M0110_1_2 = 2 (X) 7.	Set CLIN_SCORE1_CD = value returned from
     * searching Table 8: Hexavigesimal Code Tables (converting dates and point
     * values to letter codes) - Part 2 – POINTS in the Appendix with value of
     * CLIN_SCORE1 (X) 8. Set FUNC_SCORE1_CD = value returned from searching
     * Table 8: Hexavigesimal Code Tables (converting dates and point values to
     * letter codes) - Part 2 – POINTS in the Appendix with value of FUNC_SCORE1
     * (X) 9. Set CLIN_SCORE2_CD = value returned from searching Table 8:
     * Hexavigesimal Code Tables (converting dates and point values to letter
     * codes) - Part 2 – POINTS in the Appendix with value of CLIN_SCORE2 (X)
     * 10. Set FUNC_SCORE2_CD = value returned from searching Table 8:
     * Hexavigesimal Code Tables (converting dates and point values to letter
     * codes) - Part 2 – POINTS in the Appendix with value of FUNC_SCORE2 (X)
     * 11. Set CLIN_SCORE3_CD = value returned from searching Table 8:
     * Hexavigesimal Code Tables (converting dates and point values to letter
     * codes) - Part 2 – POINTS in the Appendix with value of CLIN_SCORE3 (X)
     * 12. Set FUNC_SCORE3_CD = value returned from searching Table 8:
     * Hexavigesimal Code Tables (converting dates and point values to letter
     * codes) - Part 2 – POINTS in the Appendix with value of FUNC_SCORE3 (X)
     * 13. Set CLIN_SCORE4_CD = value returned from searching Table 8:
     * Hexavigesimal Code Tables (converting dates and point values to letter
     * codes) - Part 2 – POINTS in the Appendix with value of CLIN_SCORE4 (X)
     * 14. Set FUNC_SCORE4_CD = value returned from searching Table 8:
     * Hexavigesimal Code Tables (converting dates and point values to letter
     * codes) - Part 2 – POINTS in the Appendix with value of FUNC_SCORE4 (X)
     *
     * @return non-null 30 character String that may be blank if the
     * authorization can not be determined
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

            buffer.append(getNewHexavigesimal(clinicalScore.getEarly13AndUnder()));
            buffer.append(getNewHexavigesimal(functionalScore.getEarly13AndUnder()));
            buffer.append(getNewHexavigesimal(clinicalScore.getEarly14Plus()));
            buffer.append(getNewHexavigesimal(functionalScore.getEarly14Plus()));
            buffer.append(getNewHexavigesimal(clinicalScore.getLater13AndUnder()));
            buffer.append(getNewHexavigesimal(functionalScore.getLater13AndUnder()));
            buffer.append(getNewHexavigesimal(clinicalScore.getLater14Plus()));
            buffer.append(getNewHexavigesimal(functionalScore.getLater14Plus()));

            value = buffer.toString();
        }
        return value;
    }

    private char getNewHexavigesimal(int value) {
        return value >= 25 ? 'Z' : (char) ('A' + value);
    }

}
