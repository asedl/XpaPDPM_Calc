/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3312;

import com.mmm.cms.homehealth.v3210.*;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.util.ScoringEventFormatter;
import java.util.Collection;

/**
 * Based on the AbstractValidator, and provides specific validation for columns
 * of diagnosis codes as they pertain to the Clinical model.
 *
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HomeHealthRecordClinicalValidator_v3312 extends AbstractBaseValidator_v3210 {

	/**
	 * Constructor with the required Grouper reference
	 *
	 * @param grouper
	 */
	public HomeHealthRecordClinicalValidator_v3312(HomeHealthGrouperIF grouper) {
		super(grouper);
	}

	/**
	 * Ensure a valid set of codes, and determines which codes to use in
	 * scoring.
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

		if (!isValidPrincipalDiagnosisCode(record)) {
			code = record.getPRIMARY_DIAG_ICD();
			code.setValidForScoring(false);
			valid = false;
		}

		//------------------------------
		// Part 1: Determine if the code is valid or an
		//   optional payment code in order to determine which codes
		//   to use for scoring
		//------------------------------
		for (idx = 0; idx < 6; idx++) {
			code = record.getDiagnosisCode(idx);
			if (code.isValidForScoring()) {
				// set the options codes as invalid for scoring
				tmpCode = record.getOptionalDiagnosisCode3(idx);
				if (!tmpCode.isEmpty()) {
					tmpCode.setValidForScoring(false);
					ScoringEventFormatter.fireIssueEvent(listeners, grouper, null,
							"Diagnosis Code '" + tmpCode.getCode() + "' at position "
							+ (idx + 7) + " is NOT valid for scoring because code in position "
							+ (idx + 1) + " is valid for scoring.");
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
					// set the payment codes to not be scorable
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
				} else {
					tmpCode = record.getOptionalDiagnosisCode4(idx);
					if (tmpCode.isValidCode()
							&& !tmpCode.isSecondaryOnly()
							&& !tmpCode.isEmpty()) {
						// the optional column 4 code is not valid
						// so skip it - this is the default indicator on any
						// invalid codes, so nothting to really do
						tmpCode.setValidForScoring(false);
						ScoringEventFormatter.fireIssueEvent(listeners, grouper, null,
								"Diagnosis Code '" + tmpCode.getCode() + "' at position "
								+ (idx + 13) + " is NOT valid for scoring because it is NOT a manifestation code");
					}

					// make sure that column 3 is not a secondary only code
					tmpCode = record.getOptionalDiagnosisCode3(idx);
					if (tmpCode.isSecondaryOnly()) {
						// column 3 code is not valid for scoring
						tmpCode.setValidForScoring(false);
						ScoringEventFormatter.fireIssueEvent(listeners, grouper, null,
								"Diagnosis Code '" + tmpCode.getCode() + "' at position "
								+ (idx + 7) + " is NOT valid for scoring because it is NOT an etiology code");
						valid = false;
					}
				}
			}
		}

		//------------------------------
		// Part 2: Flag acceptable pairing of manifestion diagnosis
		//   with etiologies; disqualify others
		//------------------------------
		for (idx = 1; idx < 6; idx++) {
			code = record.getDiagnosisCode(idx);

			// only check codes that are still valid
			if (code.isValidCode()
					&& code.isSecondaryOnly()) {
				// set the valid for scoring based on whether there
				// is an exceptable pairing code found for this code
				code.setValidForScoring(ManifestationUtils.getPairedCode(grouper, record, code, idx) != null);
			}
		}

		//------------------------------
		// Part 3: Flag acceptable pairing of manifestion diagnosis
		//   column 4 with etiologies; disqualify others
		//------------------------------
		for (idx = 0; idx < 6; idx++) {
			code = record.getOptionalDiagnosisCode4(idx);

			// only check codes that are still valid to score
			if (code.isValidForScoring()
					&& code.isSecondaryOnly()) {
				tmpCode = record.getOptionalDiagnosisCode3(idx);
				if (!code.isValidEtiologyPairing(tmpCode)) {
					code.setValidForScoring(false);
					valid = false;
				}
			}
		}

		return valid;
	}
}