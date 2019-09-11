package com.mmm.cms.util;

import com.mmm.cms.homehealth.proto.HIPPSCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.homehealth.proto.TreatmentAuthorizationIF;

/**
 * This utility class consolidates formatting of scoring results to return to
 * the DLL, or to output to a file.
 *
 * @author 3M Health Information Systems for CMS Home Health
 * @version %version: 6 %
 */
public class ScoringResultsFormatter {

	protected final static IntegerFormat RECORD_NUMBER_FORMAT;
    static {
        RECORD_NUMBER_FORMAT = new IntegerFormat('0');
        RECORD_NUMBER_FORMAT.setMinimumIntegerDigits(9);
        RECORD_NUMBER_FORMAT.setMaximumIntegerDigits(9);
    }
	
	public static String formatContinousScore(ScoringResultsIF scoringResult) {
		final String str;

		if (scoringResult == null) {
			str = ScoringResultsIF.DEFAULT_BLANK_VALUE;
		} else {
			// build a string to hold the results
			final StringBuilder buffer = new StringBuilder(30);
			String scoreParts[];

			scoreParts = formatScoringParts(scoringResult);

			buffer.append(scoreParts[0]);
			buffer.append(scoreParts[1]);
			buffer.append(scoreParts[2]);
			buffer.append(scoreParts[3]);

			str = buffer.toString();
		}
		return str;
	}

	/**
	 * this formats the score with the following format (example and without
	 * indent):
	 *
	 * Rec 000000001: Code 2BHKS--OASIS 08LV10AA11EHILCKGH--Version V3110 --Flag
	 * 1
	 *
	 * @param scoringResult
	 * @param recNum
	 * @return
	 */
	public static String formatTestingScore(ScoringResultsIF scoringResult,
			int recNum) {
		return formatTestingScore(scoringResult, recNum, false, false);
	}

	/**
	 * this formats the score with the following format (example and without
	 * indent):
	 *
	 * Rec 000000001: Code 2BHKS--OASIS 08LV10AA11EHILCKGH--Version V3110 --Flag
	 * 1
	 *
	 * @param scoringResult
	 * @param recNum
	 * @return
	 */
	public static String formatTestingScore(ScoringResultsIF scoringResult,
			int recNum, boolean hideTAC, boolean hideVersion) {
		// build a string to hold the results
		final StringBuilder buffer = new StringBuilder(30);
		String scoreParts[];

		scoreParts = formatScoringParts(scoringResult);

		buffer.append(formatRecordNumber(recNum, true));
		buffer.append(": Code ");
		buffer.append(scoreParts[0]);
		if (!hideTAC) {
			buffer.append("--OASIS ");
			buffer.append(scoreParts[1]);
		}
		if (!hideVersion) {
			buffer.append("--Version ");
			buffer.append(scoreParts[2]);
		}
		buffer.append(" --Flag ");
		buffer.append(scoreParts[3]);

		return buffer.toString();
	}

	/**
	 * This returns an array of strings with the data as:
	 *
	 * string[0] = hipps code string[1] = treatment authorization code string[2]
	 * = grouper version string[3] = flag
	 *
	 * If the scoringResult is null, then all the elements are empty strings
	 *
	 * @param scoringResult
	 * @return a non-null String array with non-null values for each element
	 */
	public static String[] formatScoringParts(ScoringResultsIF scoringResult) {
		final String scores[] = new String[4];
		HIPPSCodeIF hipps;

		if (scoringResult == null) {

			scores[0] = HIPPSCodeIF.DEFAULT_BLANK_VALUE;
			scores[1] = TreatmentAuthorizationIF.DEFAULT_BLANK_VALUE;
			scores[2] = HomeHealthGrouperIF.DEFAULT_BLANK_VALUE;
			scores[3] = " ";

		} else {
			// always set the version
			scores[2] = scoringResult.getGrouperVersion();

			// only set the flags if there is a version associated
			if (scores[2] == null || scores[2].trim().isEmpty()) {
				scores[3] = " ";
			} else if (scoringResult.getValidityFlag() == null) {
				scores[3] = " ";
			} else {
				scores[3] = scoringResult.getValidityFlag().getValidityFlag();
			}

			// don't provide any score if the hipps code is blank
			hipps = scoringResult.getHIPPSCode();
			if (hipps != null && hipps.getGroupingStep() > 0) {
				scores[0] = hipps.getCode();
				scores[1] = scoringResult.getTreatmentAuthorization() != null
						? scoringResult.getTreatmentAuthorization().getAuthorizationCode()
						: TreatmentAuthorizationIF.DEFAULT_BLANK_VALUE;
			} else {
				scores[0] = HIPPSCodeIF.DEFAULT_BLANK_VALUE;
				scores[1] = TreatmentAuthorizationIF.DEFAULT_BLANK_VALUE;
			}
		}

		return scores;
	}

	/**
	 * Provides a standard record number format optionally with preceeding text
	 *
	 * @param recNum
	 * @return
	 */
	public static String formatRecordNumber(int recNum, boolean includeText) {
		return includeText ? "Rec " + RECORD_NUMBER_FORMAT.format(recNum) : RECORD_NUMBER_FORMAT.format(recNum);
	}
}
