/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.vut.pps;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.edits.HH_PPS_OasisC1EditsEN;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.RecordItemValidator_HH_PPS_IF;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.homehealth.vut.oasis.AbstractItemValidator;
import com.mmm.cms.util.ValidateUtils;
import java.util.List;

/**
 * Validates Existing Codes for edits: 70000
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateExistingCodes extends AbstractPPSClinicalItemValidator implements RecordItemValidator_HH_PPS_IF {

    public ValidateExistingCodes() {
    }

    public ValidateExistingCodes(String primaryPrefix, String otherPrefix, String optPrefix) {
        super(primaryPrefix, otherPrefix, optPrefix);
    }

    
    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(HH_PPS_OasisC1EditsEN.EDIT_70000);
    }

    public int validate(HomeHealthGrouperIF grouper, HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        DiagnosisCodeIF code;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            for (int idx = 0; idx < 6; idx++) {
                code = record.getDiagnosisCode(idx);

                // Blank codes are acceptable, but invalid codes
                // are not.
                if (!code.isValidCode() && !code.isEmpty()) {
                    count++;
//                    ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Invalid Diagnosis code", code.getCode());
                    edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70000,
                            new HHOasisDataItem(idx == 0 ? getPrimaryPrefix() : getOtherPrefix() + idx + "_ICD", code.getCode())));
                }
            }

            for (int idx = 0; idx < 6; idx++) {
                code = record.getOptionalDiagnosisCode3(idx);
                // Blank codes are acceptable, but invalid codes
                // are not.
                if (code != null && !code.isValidCode() && !code.isEmpty()) {
                    count++;
//                    ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Invalid Diagnosis code", code.getCode());
                    edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70000,
                            new HHOasisDataItem(getPaymentPrefix() + ('A' + idx) + "3", code.getCode())));
                }

                code = record.getOptionalDiagnosisCode4(idx);
                // Blank codes are acceptable, but invalid codes
                // are not.
                if (code != null && !code.isValidCode() && !code.isEmpty()) {
                    count++;
//                    ScoringEventFormatter.fireClinicalIssueEvent(listeners, grouper, null, "Invalid Diagnosis code", code.getCode());
                    edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70000,
                            new HHOasisDataItem(getPaymentPrefix() + ('A' + idx) + "4", code.getCode())));
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
        return "Validates Existing Codes for edits: 70000";
    }

}
