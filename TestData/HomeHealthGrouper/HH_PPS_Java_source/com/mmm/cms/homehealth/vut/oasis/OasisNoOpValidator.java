/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.vut.oasis;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import com.mmm.cms.homehealth.proto.RecordValidatorFactoryIF;

/**
 * Regardless of the record type, this always returns a empty array.
 *
 * @author 3M Health Information Systems for CMS Home Health 
 */
public class OasisNoOpValidator implements RecordValidatorFactoryIF {

    public final static RecordItemValidatorIF EMPTY_ITEM_VALIDATOR[] = new RecordItemValidatorIF[0];
    
    public RecordItemValidatorIF[] getValidationItems(HomeHealthRecordIF record) {
        return EMPTY_ITEM_VALIDATOR;
    }
    
}
