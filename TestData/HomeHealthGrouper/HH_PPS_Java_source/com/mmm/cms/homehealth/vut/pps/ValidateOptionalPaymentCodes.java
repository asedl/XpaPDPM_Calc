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
 * Validates OptionalPaymentCodes for edits: 70040, 70050
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateOptionalPaymentCodes extends AbstractPPSClinicalItemValidator implements RecordItemValidator_HH_PPS_IF {

    public ValidateOptionalPaymentCodes() {
    }

    public ValidateOptionalPaymentCodes(String primaryPrefix, String otherPrefix, String optPrefix) {
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
        DiagnosisCodeIF tmpCode;

        if (ValidateUtils.isValidValue(record.getASSMT_REASON(), AbstractItemValidator.ASSESSMENT_1_3_4_5_ONLY)) {
            /*
             * Due to the Primary awarding and the paired association of payment
             * type codes, column 3 and 4 are no longer relavent and so there is
             * no need to provide "edit" information, just the historic events
             */
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
                    }

                    tmpCode = record.getOptionalDiagnosisCode4(idx);
                    if (!tmpCode.isEmpty()) {
                        tmpCode.setValidForScoring(false);
                    }
                } else {
                    // check for being an optional payment code
                    if (code.isOptionalPaymentCode()) {
                        tmpCode = record.getOptionalDiagnosisCode4(idx);
                        if (tmpCode.isValidCode() && !tmpCode.isSecondaryOnly()) {
                            // the optional column 4 code is not valid
                            // so skip it - this is the default indicator on any
                            // invalid codes, so nothting to really do
                            tmpCode.setValidForScoring(false);
                            edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70050,
                                    new HHOasisDataItem(getPaymentPrefix() + ((char) ('A' + idx)) + "4", tmpCode.getCode())));
                        }

                        /*
                         * In version 3413, optional payment codes are paired with  
                         * a specific set of Dx codes.  If the pairing is not valid
                         * then the column 3 code can not score.  Blank codes are
                         * not checked.
                         */
                        tmpCode = record.getOptionalDiagnosisCode3(idx);
                        if (!tmpCode.isEmpty() && !code.isValidEtiologyPairing(tmpCode)) {
                            tmpCode.setValidForScoring(false);
                            edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70040,
                                    new HHOasisDataItem(getPaymentPrefix() + ((char) ('A' + idx)) + "3", tmpCode.getCode())));
                        }
                    } else {
                        // set the payment codes to not be scorable
                        tmpCode = record.getOptionalDiagnosisCode3(idx);
                        if (!tmpCode.isEmpty()) {
                            tmpCode.setValidForScoring(false);
                            edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70040,
                                    new HHOasisDataItem(getPaymentPrefix() + ((char) ('A' + idx)) + "3", tmpCode.getCode())));
                        }

                        tmpCode = record.getOptionalDiagnosisCode4(idx);
                        if (!tmpCode.isEmpty()) {
                            tmpCode.setValidForScoring(false);

                            edits.add(new OasisValidationEdit(HH_PPS_OasisC1EditsEN.EDIT_70040,
                                    new HHOasisDataItem(getPaymentPrefix() + ((char) ('A' + idx)) + "4", tmpCode.getCode())));
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
        return "Validates OptionalPaymentCodes for  edits: 70040, 70050";
    }

}
