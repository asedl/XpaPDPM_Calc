/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.vut.pps;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.proto.DataValidityFlagIF;
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
 * Validates Manifestation is not in the Primary Diagnosis position;
 * for  edits: 70020
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateManifestationAsPrimaryDiagnosis extends AbstractPPSManifestationItemValidator implements RecordItemValidator_HH_PPS_IF {

    public ValidateManifestationAsPrimaryDiagnosis() {
    }

    public ValidateManifestationAsPrimaryDiagnosis(String primaryPrefix, String otherPrefix, String optPrefix) {
        super(primaryPrefix, otherPrefix, optPrefix);
    }

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(HH_PPS_OasisC1EditsEN.EDIT_70020);
    }

    /**
     * This adds a clinical issue flag if this edit occurs - the parents class
     * already handles the manifestation issue indicator
     * 
     * @param grouper
     * @param record
     * @param edits
     * @param dataValidity
     * @return 
     */
    @Override
    public int validate(HomeHealthGrouperIF grouper, HomeHealthRecordIF record, CollectionValidationEditsIF edits, DataValidityFlagIF dataValidity) {
        final int count;
        
        count = super.validate(grouper, record, edits, dataValidity);
        if (count > 0) {
            dataValidity.setClinicalIssue(true);
        } 
        return count;
    }

    public int validate(HomeHealthGrouperIF grouper, HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        DiagnosisCodeIF code;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
                code = record.getPRIMARY_DIAG_ICD();
                if (code.isSecondaryOnly()) {
                    count++;
                    /*
                     * Secondary/Manifestation codes can not be in the primary position
                     */
                    edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70020,
                            new HHOasisDataItem(getPrimaryPrefix(), code.getCode())));
                    code.setValidForScoring(false);
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
        return "Validates OptionalPaymentCodes for  edits: 70020";
    }

}
