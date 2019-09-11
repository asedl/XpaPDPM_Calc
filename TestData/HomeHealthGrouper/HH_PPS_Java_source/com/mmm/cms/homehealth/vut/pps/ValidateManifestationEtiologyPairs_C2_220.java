/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.vut.pps;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.edits.HH_PPS_OasisC1EditsEN;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.v3312.ManifestationUtils;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.homehealth.vut.oasis.AbstractItemValidator;
import com.mmm.cms.util.ValidateUtils;

/**
 * Validates Manifestation pairings for column 1 and 4, the PDX is not tested;
 * for edits: 70030
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateManifestationEtiologyPairs_C2_220 extends ValidateManifestationEtiologyPairs {

    public ValidateManifestationEtiologyPairs_C2_220() {
    }

    public ValidateManifestationEtiologyPairs_C2_220(String primaryPrefix, String otherPrefix, String optPrefix) {
        super(primaryPrefix, otherPrefix, optPrefix);
    }

    @Override
    public int validate(HomeHealthGrouperIF grouper, HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;
        int idx;
        DiagnosisCodeIF code;
		DiagnosisCodeIF codeAntiPair;
        boolean etiologyPairFound;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            //------------------------------
            // Flag acceptable pairing of manifestion diagnosis with etiologies
            //------------------------------
            for (idx = 0; idx < 6; idx++) { // start from 0
                etiologyPairFound = false;
                code = record.getDiagnosisCode(idx);
                // mimic old logic
                if (idx == 0) {
                    if (!code.getCode().equalsIgnoreCase("I96.")) { // ignore anything but gangrene
                        continue;
                    }
                }

                /*
                 * Since there are now two types of manifestations: HH and MCE,
                 * only check those codes that are secondary and have a Diagnostic 
                 * Group with an id no which means that they are part of HH
                 */
                if (code.getDiagnosticGroup().getId() > 0) {
                    if (code.isSecondaryOnly()) { // manifestation - should find pair to be scored
                        etiologyPairFound = ManifestationUtils.getPairedCode(grouper, record, code, idx) != null;
                        code.setValidForScoring(etiologyPairFound);
                        // Oct 2012 - added to ensure flag was identfied
                        // set the manifestion flag
                        if (!etiologyPairFound) {
                            edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70030,
                                    new HHOasisDataItem(getOtherPrefix() + idx + "_ICD", code.getCode())));
                            count++;
                        }

                    } else if (code.getCode().equalsIgnoreCase("I96.")) { // gangrene - should NOT find pair to be scored
                        // no code first rule for I10 anymore
                        // look up thru all codes
                        codeAntiPair = ManifestationUtils.getExclusionCode(grouper, record, code, idx);
                        etiologyPairFound = codeAntiPair != null;
                        code.setValidForScoring(!etiologyPairFound);
                        if (etiologyPairFound) {
                            edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70030,
                                    new HHOasisDataItem(getOtherPrefix() + idx + "_ICD", code.getCode())));
                            count++;
                        }
                    }
                }

            }
        }

        return count;
    }

}
