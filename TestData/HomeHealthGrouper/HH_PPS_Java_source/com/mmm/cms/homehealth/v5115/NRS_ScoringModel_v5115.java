/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.v5115;

import com.mmm.cms.homehealth.proto.DataManagerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.v4115.NRS_ScoringModel_v4115;

/**
 * Provides Non-Routine Supplies scoring
 * 
 * @author 3M Health Information Systems for CMS Home Health
 */
public class NRS_ScoringModel_v5115 
    extends  NRS_ScoringModel_v4115 {

    public NRS_ScoringModel_v5115(HomeHealthGrouperIF grouper, DataManagerIF dataManager) {
        super(grouper, dataManager);
        setUseOptionalLogic(false);
    }

    @Override
    public String getName() {
        return "Non-Routine Supplies Scoring Model V5115";
    }
    
}
