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
import com.mmm.cms.homehealth.v3312.ManifestationUtils;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.homehealth.vut.oasis.AbstractItemValidator;
import com.mmm.cms.util.ValidateUtils;
import java.util.List;

/**
 * Validates Manifestation pairings for column 1 and 4, the PDX is not tested;
 * for  edits: 70030
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateManifestationEtiologyPairs extends AbstractPPSManifestationItemValidator implements RecordItemValidator_HH_PPS_IF {

    public ValidateManifestationEtiologyPairs() {
    }

    public ValidateManifestationEtiologyPairs(String primaryPrefix, String otherPrefix, String optPrefix) {
        super(primaryPrefix, otherPrefix, optPrefix);
    }

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(HH_PPS_OasisC1EditsEN.EDIT_70030);
    }

    
    public int validate(HomeHealthGrouperIF grouper, HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        int idx;
        DiagnosisCodeIF code;
        boolean etiologyPairFound;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            //------------------------------
            // Flag acceptable pairing of manifestion diagnosis with etiologies
            //------------------------------
            for (idx = 1; idx < 6; idx++) {
                code = record.getDiagnosisCode(idx);

                /*
                 * Since there are now two types of manifestations: HH and MCE,
                 * only check those codes that are secondary and have a Diagnostic 
                 * Group with an id no which means that they are part of HH
                 */
                if (code.isSecondaryOnly() && code.getDiagnosticGroup().getId() > 0) {
                    // set the valid for scoring based on whether there
                    // is an exceptable pairing code found for this code
                    etiologyPairFound = ManifestationUtils.getPairedCode(grouper, record, code, idx) != null;
                    code.setValidForScoring(etiologyPairFound);

                    // Oct 2012 - added to ensure flag was identfied
                    // set the manifestion flag
                    if (!etiologyPairFound) {
                        edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70030,
                                new HHOasisDataItem(getOtherPrefix() + idx + "_ICD", code.getCode())));
                        count++;
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
        return "Validates OptionalPaymentCodes for  edits: 70030";
    }

}
