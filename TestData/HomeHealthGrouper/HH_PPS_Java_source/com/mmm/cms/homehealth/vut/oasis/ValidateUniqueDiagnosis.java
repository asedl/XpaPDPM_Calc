/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut.oasis;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditsEN;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.util.ValidateUtils;
import java.util.List;

/**
 * Validates Existing Codes for edits: 5000
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateUniqueDiagnosis extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    public ValidateUniqueDiagnosis() {
    }

    public ValidateUniqueDiagnosis(String otherPrefix) {
        super(otherPrefix);
    }

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_5000);
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        DiagnosisCodeIF otherCode;
        DiagnosisCodeIF currentCode;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            // check only code 1 through 5 in this outter loop
            // the inner loop will check against the 6th code
            for (int idx = 0; idx < 5 && count == 0; ) {
                currentCode = record.getDiagnosisCode(idx++);

                // only check valid codes
                if (currentCode.isValidCode()) {
                    for (int j = idx; j < 6;) {
                        // if the current code is duplicated in subsequent positions
                        // then mark the subsequent positions as not valid for
                        // scoring, but allow the current code's to scoring
                        // indicator to remain
                        otherCode = record.getDiagnosisCode(j++);
                        if (otherCode.isValidCode() && currentCode.equals(otherCode)) {
                            // set the code to not score
                            otherCode.setValidForScoring(false);

                            edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_5000,
                                    new HHOasisDataItem(getOtherPrefix() + j + "_ICD", otherCode.getCode())));

                            // indicate that a duplicate was found
                            count++;
                            break;
                        }
                    }
                }
            }
        }

        return count;
    }

    /**
     * Gets the description
     *
     * @return non-null string
     */
    public String getDescription() {
        return "Validates Existing Codes for edits: 5000";
    }

}
