/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.v5216;

import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.v5115.GrouperDataManager_v5115;

/**
 * Data manager for V5115 which is the same as V4115 except for ensuring the
 * creation of ICD-10 code types
 * 
 * @author 3M HIS Clinical & Economic Research
 */
public class GrouperDataManager_v5216 extends GrouperDataManager_v5115 {

    public GrouperDataManager_v5216(HomeHealthGrouperIF homeHealthGrouper) {
        super(homeHealthGrouper);
    }

    @Override
    protected void onParseDiagnosisCodeAttributes(DiagnosisCodeIF diag, String str) {
        if ("G".equalsIgnoreCase(str)) {
            diag.setSecondaryOnly(true);
        } else {
            super.onParseDiagnosisCodeAttributes(diag, str);
        }
    }

    
}
