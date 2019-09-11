/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v5115;

import com.mmm.cms.homehealth.proto.DataManagerIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.v4115.ClinicalFunctional_ScoringModel_v4115;

/**
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ClinicalFunctional_ScoringModel_v5115 
        extends ClinicalFunctional_ScoringModel_v4115 {

    public ClinicalFunctional_ScoringModel_v5115(HomeHealthGrouperIF grouper, DataManagerIF grouperDataManager, int equationId) {
        super(grouper, grouperDataManager, equationId);
        setUseOptionalLogic(false);
    }

    public ClinicalFunctional_ScoringModel_v5115(HomeHealthGrouperIF grouper, DataManagerIF grouperDataManager, int equationId, String shortName) {
        super(grouper, grouperDataManager, equationId, shortName);
        setUseOptionalLogic(false);
    }

    @Override
    public String getName() {
        return "Clinical / Functional Scoring Model V5115";
    }
}
