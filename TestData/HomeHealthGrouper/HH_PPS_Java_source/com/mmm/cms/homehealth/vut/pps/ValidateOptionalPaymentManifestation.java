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
 * Validates OptionalPaymentCodes in position x4 is a manifestation code
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateOptionalPaymentManifestation extends AbstractPPSClinicalItemValidator implements RecordItemValidator_HH_PPS_IF {

    public ValidateOptionalPaymentManifestation() {
    }

    public ValidateOptionalPaymentManifestation(String primaryPrefix, String otherPrefix, String optPrefix) {
        super(primaryPrefix, otherPrefix, optPrefix);
    }

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(HH_PPS_OasisC1EditsEN.EDIT_70040, HH_PPS_OasisC1EditsEN.EDIT_70050);
    }

    public int validate(HomeHealthGrouperIF grouper, HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        int idx;
        DiagnosisCodeIF code;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            for (idx = 0; idx < 6; idx++) {
                code = record.getOptionalDiagnosisCode4(idx);
                if (code.isValidCode() && !code.isSecondaryOnly()) {
                    // the optional column 4 code is not valid
                    // so skip it - this is the default indicator on any
                    // invalid codes, so nothting to really do
                    code.setValidForScoring(false);
                    edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70050,
                            new HHOasisDataItem(getPaymentPrefix() + ((char) ('A' + idx)) + "4", code.getCode())));
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
        return "Validates OptionalPaymentCodes for  edits: 70040, 70050";
    }

}
