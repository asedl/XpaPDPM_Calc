/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.v5115;

import com.mmm.cms.homehealth.DiagnosisCode;
import com.mmm.cms.homehealth.proto.CodeType_EN;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.v4115.GrouperDataManager_V4115;

/**
 * Data manager for V5115 which is the same as V4115 except for ensuring the
 * creation of ICD-10 code types
 * 
 * @author 3M HIS Clinical & Economic Research
 */
public class GrouperDataManager_v5115 extends GrouperDataManager_V4115 {

    public GrouperDataManager_v5115(HomeHealthGrouperIF homeHealthGrouper) {
        super(homeHealthGrouper);
    }

    /**
     * Creates a code of type ICD-10
     * 
     * @param code
     * @param validCode
     * @param validForScoring
     * @return  non-null DiagnosisCodeIF identified as ICD-10.  The parameters 
     * are passed through to the created object
     */
    @Override
    public DiagnosisCodeIF createDiagnosisCode(String code, boolean validCode, boolean validForScoring) {
        final DiagnosisCodeIF diag = new DiagnosisCode(code, CodeType_EN.ICD_10, validCode, validForScoring);
        return diag;
    }
    
}
