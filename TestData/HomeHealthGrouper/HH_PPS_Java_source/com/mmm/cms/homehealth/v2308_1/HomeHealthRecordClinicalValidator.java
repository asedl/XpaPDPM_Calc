/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v2308_1;

import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.util.ScoringEventFormatter;
import java.util.Collection;

/**
 * Based on the AbstractValidator, and provides specific validation for columns
 * M0230, M0240, and M0246x3/4 as they pertain to the Clinical model.
 *
 * Pseudo code lines: 533 thru 665
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HomeHealthRecordClinicalValidator extends AbstractBaseValidator_v2308 {

	/**
	 * Constructor with the required Grouper reference
	 *
	 * @param grouper
	 */
	public HomeHealthRecordClinicalValidator(HomeHealthGrouperIF grouper) {
		super(grouper);
	}

	/**
	 *
	 * Ensure a valid set of codes, and determines which codes to use in
	 * scoring.
	 *
	 * Psuedo code lines: 553 thru 665
	 *
	 * @param record
	 * @return true if the PDX is an allowable Principal code
	 */
	@Override
	public boolean validateDiagnosisCodes(HomeHealthRecordIF record, Collection<HomeHealthEventListenerIF> listeners) {
		boolean valid = true;
		int idx;
		DiagnosisCodeIF code;
		DiagnosisCodeIF tmpCode;

		code = record.getPRIMARY_DIAG_ICD();
		if (code.isSecondaryOnly()) {
			code.setValidForScoring(false);
			valid = false;
		}

		//------------------------------
		// Part 1: Determine if the code is valid or an
		//   optional payment code in order to determine which codes
		//   to use for scoring (pseudo code lines 595 thru 618)
		//------------------------------
		for (idx = 0; idx < 6; idx++) {
			code = record.getDiagnosisCode(idx);
			if (code.isValidForScoring()) {
				// set the options codes as invalid for scoring
				tmpCode = record.getOptionalDiagnosisCode3(idx);
				if (!tmpCode.isEmpty()) {
					tmpCode.setValidForScoring(false);
//					ScoringEventFormatter.fireIssueEvent(listeners, grouper, null, 
//							"Diagnosis Code '" + tmpCode.getCode() + "' at position "
//							+ (idx + 7) + " is NOT valid for scoring because code in position "
//							+ (idx + 1) + " is valid for scoring.");
                                        ScoringEventFormatter.fireScoringCodeEligible(listeners, grouper, null, idx, code);
				}

				tmpCode = record.getOptionalDiagnosisCode4(idx);
				if (!tmpCode.isEmpty()) {
					tmpCode.setValidForScoring(false);
					ScoringEventFormatter.fireIssueEvent(listeners, grouper, null, 
					"Diagnosis Code '" + tmpCode.getCode() + "' at position "
							+ (idx + 13) + " is NOT valid for scoring because code in position "
							+ (idx + 1) + " is valid for scoring.");
				}
			} else {
				// check for being an optional VCode
				if (!code.isOptionalPaymentCode()) {
					tmpCode = record.getOptionalDiagnosisCode3(idx);
					if (!tmpCode.isEmpty()) {
						tmpCode.setValidForScoring(false);
					ScoringEventFormatter.fireIssueEvent(listeners, grouper, null, 
						"Diagnosis Code '" + tmpCode.getCode() + "' at position "
								+ (idx + 7) + " is NOT valid for scoring because code in position "
								+ (idx + 1) + " is NOT an optional payment code.");
					}
					tmpCode = record.getOptionalDiagnosisCode4(idx);
					if (!tmpCode.isEmpty()) {
						tmpCode.setValidForScoring(false);
					ScoringEventFormatter.fireIssueEvent(listeners, grouper, null, 
						"Diagnosis Code '" + tmpCode.getCode() + "' at position "
								+ (idx + 13) + " is NOT valid for scoring because code in position "
								+ (idx + 1) + " is NOT an optional payment code.");
					}
				} else if (record.getOptionalDiagnosisCode4(idx).isValidCode()
						&& !record.getOptionalDiagnosisCode4(idx).isSecondaryOnly()) {
					// the optional column 4 code is not valid
					// so skip it - this is the default indicator on any
					// invalid codes, so nothting to really do
					tmpCode = record.getOptionalDiagnosisCode4(idx);
					if (!tmpCode.isEmpty()) {
						tmpCode.setValidForScoring(false);
					ScoringEventFormatter.fireIssueEvent(listeners, grouper, null, 
						"Diagnosis Code '" + tmpCode.getCode() + "' at position "
								+ (idx + 13) + " is NOT valid for scoring because it is NOT a manifestation code");
					}
				}

				// pseudo code line 614 - 616
				if (record.getOptionalDiagnosisCode3(idx).isValidCode()
						&& record.getOptionalDiagnosisCode3(idx).isSecondaryOnly()) {

					// the options column 3 code is valid
					code.setValidForScoring(false);
					record.getOptionalDiagnosisCode3(idx).setValidForScoring(false);

					valid = false;
				}
			}
		}

		//------------------------------
		// Part 2: Flag acceptable pairing of manifestion diagnosis
		//   in M0240 with etiologies; disqualify others
		//   (pseudo code lines 619 thru 641)
		//------------------------------
		for (idx = 1; idx < 6; idx++) {
			code = record.getDiagnosisCode(idx);

			// only check codes that are still valid to score
			if (code.isValidCode() && code.isSecondaryOnly()) {

				tmpCode = record.getDiagnosisCode(idx - 1);
				// if the previous code is not valid or is an E-code, optional payment code
				// or secondary only code, then this secondary is not valid
				// for scoring.
				if (!tmpCode.isValidCode() || tmpCode.isVCode()
						|| tmpCode.isExternalCauseCode()
						|| tmpCode.isSecondaryOnly()) {
					// do not score this code
					code.setValidForScoring(false);
					valid = false;
				} else {
					// check if the previous code is a valid 
					// etiology pair for the current code
					if ("785.4".equals(code.getCode())) {
						// make sure the previous code is valid
						// and not on the exclusion list
						if (tmpCode.isValidCode() && !code.isEtiologyInPairingList(tmpCode)) {
							code.setValidForScoring(true);
						} else {
							code.setValidForScoring(false);
							valid = false;
						}
					} else if (code.isEtiologyInPairingList(tmpCode)) {
						// the codes on this list are a list of inclusions.
						// so, if the code is found, then it is an acceptable
						// pair and this secondary-only code can be
						// included in the scoring
						code.setValidForScoring(true);

						// the previous code may not be a scorable code
						// unless it is a paired code
						tmpCode.setValidForScoring(true);
					} else {
						// The code does not have an acceptable code pair
						// so don't score this code
						code.setValidForScoring(false);
						valid = false;
					}
				}
			}
		}

		//------------------------------
		// Part 3: Flag acceptable pairing of manifestion diagnosis
		//   in m0246x4 with etiologies; disqualify others
		// (pseudo code lines 643 thru 665)
		//------------------------------
		for (idx = 0; idx < 6; idx++) {
			code = record.getOptionalDiagnosisCode4(idx);

			// only check codes that are still valid to score
			if (code.isValidForScoring() && code.isSecondaryOnly()) {

				tmpCode = record.getOptionalDiagnosisCode3(idx);
				// if the previous code is an E, V, or secondary only
				if (tmpCode.isVCode()
						|| tmpCode.isExternalCauseCode()
						|| tmpCode.isSecondaryOnly()) {
					// do not score this code
					code.setValidForScoring(false);
					valid = false;
				} else {
					// check if the previous code is a valid
					// etiology pair for the current code
					if ("785.4".equals(code.getCode())) {
						// make sure the previous code is valid
						// and not on the exclusion list
						if (tmpCode.isValidCode() && !code.isEtiologyInPairingList(tmpCode)) {
							code.setValidForScoring(true);
						} else {
							code.setValidForScoring(false);
							valid = false;
						}
					} else if (code.isEtiologyInPairingList(tmpCode)) {
						// the codes on this list are a list of inclusions.
						// so, if the code is found, then it is an acceptable
						// pair and this secondary-only code can be
						// included in the scoring
						code.setValidForScoring(true);

						// Although pseudo code line 655 does not make this
						// check, it seems appropriate
						// only score valid casemix codes that are paired
						if (tmpCode.isValidCode()) {
							tmpCode.setValidForScoring(true);
						}
					} else {
						// The code does not have an acceptable code pair
						code.setValidForScoring(false);
						valid = false;
					}
				}
			}
		}

		//------------------------------
		// Part 4: ensure that there are no manifestation codes in
		// column 3
		//------------------------------
		for (idx = 0; idx < 6; idx++) {
			code = record.getOptionalDiagnosisCode3(idx);
			if (code.isSecondaryOnly()) {
				code.setValidForScoring(false);
			}
		}

		return valid;
	}
}
