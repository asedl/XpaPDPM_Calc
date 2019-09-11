/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3210;

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
public class HomeHealthRecordClinicalValidator_v3210 extends AbstractBaseValidator_v3210 {

    /**
     * Constructor with the required Grouper reference
     *
     * @param grouper
     */
    public HomeHealthRecordClinicalValidator_v3210(HomeHealthGrouperIF grouper) {
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
        for (idx = 1;
                idx < 6; idx++) {
            code = record.getDiagnosisCode(idx);

            // only check codes that are still valid to score
            if (code.isValidCode()
                    && code.isSecondaryOnly()) {
                tmpCode = record.getDiagnosisCode(idx - 1);
                if (!evaluateManifestionEtiology(code, tmpCode)) {
                    valid = false;
                }
            }
        }
	//------------------------------
        // Part 3: Flag acceptable pairing of manifestion diagnosis
        //   column 4 with etiologies; disqualify others
        //------------------------------
        for (idx = 0;
                idx < 6; idx++) {
            code = record.getOptionalDiagnosisCode4(idx);

            // only check codes that are still valid to score
            if (code.isValidForScoring()
                    && code.isSecondaryOnly()) {
                tmpCode = record.getOptionalDiagnosisCode3(idx);
                if (!evaluateManifestionEtiology(code, tmpCode)) {
                    valid = false;
                }
            }
        }
        return valid;
    }

    protected boolean evaluateManifestionEtiology(DiagnosisCodeIF manifestation, DiagnosisCodeIF etiology) {
        boolean valid = true;

	// if the etiology code is not valid or is an E-code, optional payment code
        // or secondary only code, then the manifestation is not valid
        // for scoring.
        if (!etiology.isValidCode() || etiology.isVCode()
                || etiology.isExternalCauseCode()
                || etiology.isSecondaryOnly()) {
            // do not score the manifestation code
            manifestation.setValidForScoring(false);
            valid = false;

        } else {
            // check if the manifestation code is the
            // gangrene code which has exclusions instead of inclusions
            // for pairing
            if ("785.4".equals(manifestation.getCode())) {
		// The codes on this pair's list are
                // a list of exclusions
                // make sure the previous code is valid
                if (etiology.isValidCode() && !manifestation.isEtiologyInPairingList(etiology)) {
                    manifestation.setValidForScoring(true);
                } else {
                    manifestation.setValidForScoring(false);
                    valid = false;
                }
            } else if (manifestation.isEtiologyInPairingList(etiology)) {
		// the codes on this list are a list of inclusions.
                // so, if the etiology is found, then it is an acceptable
                // pair and this manifestation code can be
                // included in the scoring
                manifestation.setValidForScoring(true);

            } else {
                // The code does not have an acceptable code pair
                manifestation.setValidForScoring(false);
                valid = false;
            }
        }
        return valid;

    }
}
