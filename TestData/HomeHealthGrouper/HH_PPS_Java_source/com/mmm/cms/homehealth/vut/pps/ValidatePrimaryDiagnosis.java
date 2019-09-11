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
 * Validates Primary Diagnosis Code for edits: 70000, 70010
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidatePrimaryDiagnosis extends AbstractPPSClinicalItemValidator implements RecordItemValidator_HH_PPS_IF {

    public ValidatePrimaryDiagnosis() {
    }

    public ValidatePrimaryDiagnosis(String primaryPrefix, String otherPrefix, String optPrefix) {
        super(primaryPrefix, otherPrefix, optPrefix);
    }

    
    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(HH_PPS_OasisC1EditsEN.EDIT_70000, HH_PPS_OasisC1EditsEN.EDIT_70010);
    }

    public int validate(HomeHealthGrouperIF grouper, HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            DiagnosisCodeIF code = record.getPRIMARY_DIAG_ICD();

            // check for the primary being blank
            if (code.isEmpty()) {
                DiagnosisCodeIF codeOther;
                
                count++;
                // set all the other codes in the record to not score
                for (int idx = 1; idx < 18;) {
                    codeOther = record.getDiagnosisCode(idx++);
                    if (codeOther != null) {
                        codeOther.setValidForScoring(false);
                    }
                }

                edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70000,
                        new HHOasisDataItem(getPrimaryPrefix(), code.getCode())));
                edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70010,
                        new HHOasisDataItem(getPrimaryPrefix(), code.getCode())));

            // if not blank then check if primary is valid
            } else if (!code.isValidCode()) {
                count++;
                edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70000,
                        new HHOasisDataItem(getPrimaryPrefix(), code.getCode())));
                
            // if primary is valid then check if it's not "E-code"
            } else {
                if (code.isExternalCauseCode()) {
                    count++;
                    /*
                     * check for E-code (External cause code) validation
                     */
                    edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70010,
                            new HHOasisDataItem(getPrimaryPrefix(), code.getCode())));
                    code.setValidForScoring(false);
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
        return "Validates Primary Diagnosis Code for edits: 70000, 70010";
    }

}
