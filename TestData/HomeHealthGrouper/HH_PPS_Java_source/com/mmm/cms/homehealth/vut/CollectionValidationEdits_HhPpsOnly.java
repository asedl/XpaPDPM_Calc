/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut;

import com.mmm.cms.homehealth.vut.proto.OasisValidationEditIF;
import com.mmm.cms.homehealth.proto.edits.HH_PPS_OasisC1EditsEN;

/**
 * Implementation of the VUT edit interface that allow inclusion of HH-PPS
 * edits only - i.e. Ids >= 70000
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class CollectionValidationEdits_HhPpsOnly extends CollectionValidationEdits {

    @Override
    public boolean add(OasisValidationEditIF newEdit) {
       return newEdit.getEdit().getId() >= HH_PPS_OasisC1EditsEN.EDIT_70000.getId() ? super.add(newEdit) : false;
    }
    
}
