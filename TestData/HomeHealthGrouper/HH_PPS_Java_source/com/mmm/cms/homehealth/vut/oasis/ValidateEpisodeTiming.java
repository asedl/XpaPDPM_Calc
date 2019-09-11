/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut.oasis;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditsEN;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import com.mmm.cms.homehealth.vut.HHOasisDataItem;
import com.mmm.cms.homehealth.vut.OasisValidationEdit;
import com.mmm.cms.util.ValidateUtils;

import java.util.List;

/**
 * Validates Episode Timing for edits: 3060
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class ValidateEpisodeTiming extends AbstractClinicalItemValidator implements RecordItemValidatorIF {

    /**
     * returns the edit list
     *
     * @return non-null list of edits for this validator
     */
    public List<OasisEditIF> getEditIdsUsed() {
        return super.getEditIdsUsed_base(OasisEditsEN.EDIT_3060);
    }

    public int validate(HomeHealthRecordIF record, CollectionValidationEditsIF edits) {
        int count = 0;

        if (!ValidateUtils.isValidValue(record.getEPISODE_TIMING(), 
                    ValidateUtils.ARRAY_DOUBLE_1, 
                    ValidateUtils.ARRAY_DOUBLE_2, 
                    ValidateUtils.ARRAY_UNKNOWN_UK, 
                    ValidateUtils.ARRAY_NOT_APPLICABLE)) {
            count++;
            edits.add(new OasisValidationEdit(OasisEditsEN.EDIT_3060, new HHOasisDataItem("M0110_EPISODE_TIMING", record.getEPISODE_TIMING())));
        }

        return count;
    }

    /**
     * Gets the description
     *
     * @return non-null string
     */
    public String getDescription() {
        return "Validates Episode Timing for edits: 3060";
    }

}
