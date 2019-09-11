/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.v4115;

import com.mmm.cms.homehealth.DiagnosisCode;
import com.mmm.cms.homehealth.proto.CodeType_EN;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.v3413.GrouperDataManager_V3413;

/**
 * This class provides the Data management related to the Diagnosis codes, the
 * NRS Diagnosis codes, the Diagnostic Groups, the Etiology Pairs, and the
 * optional Payment Codes.
 *
 * The information is loaded from text files. Refer to the loading methods from
 * the super class.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperDataManager_V4115 extends GrouperDataManager_V3413 {

    public GrouperDataManager_V4115(HomeHealthGrouperIF homeHealthGrouper) {
        super(homeHealthGrouper);
    }

    /**
     * This creates a Diagnosis code allowing any extending class to create its
     * own code implementation for use in its grouper version.
     *
     * @param code
     * @param validCode
     * @param validForScoring
     * @return non-null DiagnosisCodeIF with its code value, valid code flag and
     * valid for scoring flag set
     */
    @Override
    public DiagnosisCodeIF createDiagnosisCode(String code, boolean validCode,
            boolean validForScoring) {
        final DiagnosisCodeIF diag = new DiagnosisCode(code, CodeType_EN.ICD_9, validCode, validForScoring);
        return diag;
    }

    @Override
    protected void onParseDiagnosisCodeAttributes(DiagnosisCodeIF diag, String str) {
        if ("MCE".equalsIgnoreCase(str)) {
            diag.setSecondaryOnly(true);
        } else {
            super.onParseDiagnosisCodeAttributes(diag, str);
        }
    }

}
