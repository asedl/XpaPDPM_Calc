/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut.oasis;

import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public abstract class AbstractItemValidator {
    public static final String[] ASSESSMENT_1_3_9_ONLY = new String[]{"01", "03", "09"};
    public static final String[] ASSESSMENT_1_3_4_5_9_ONLY = new String[]{"01", "03", "04", "05", "09"};
    public static final String[] ASSESSMENT_1_3_ONLY = new String[]{"01", "03"};
    public static final String[] ASSESSMENT_1_3_4_5_ONLY = new String[]{"01", "03", "04", "05"};
    public static final String[] ASSESSMENT_4_5_9_ONLY = new String[]{"04", "05", "09"};
    public static final String[] ASSESSMENT_9_ONLY = new String[]{"09"};

    private List<OasisEditIF> oasisEdits;
    private final Class recordClass;

    /**
     * creates with a default record class of HomeHealthRecordIF
     */
    public AbstractItemValidator() {
        recordClass = HomeHealthRecordIF.class;
    }
    
    /**
     * creates with a supplied record class. There is no check for this
     * class being a HomeHealthRecordIF extension or not
     * 
     * @param clazz 
     */
    protected AbstractItemValidator(Class clazz) {
        clazz.getSimpleName();
        recordClass = clazz;
    }

    /**
     * This provides creating an array for the edits and keeps track of the 
     * array list so that extended classes do not have to repeat it.
     * 
     * @param edits - non-null OasisEditIF objects, usually enums, but don't 
     * have to be.
     * 
     * @return 
     */
    final protected List<OasisEditIF> getEditIdsUsed_base(OasisEditIF ... edits) {
        if (edits == null) {
            throw new IllegalArgumentException("OasisEditIF edits can not be null.");
        }
        if (this.oasisEdits == null) {
            this.oasisEdits = Arrays.asList(edits);
        }
        return this.oasisEdits;
    }
    
    /**
     * returns the default class of HomeHealthRecordIF
     * @return HomeHealthRecordIF class
     */
    public Class getRecordIFType() {
        return recordClass;
    }
    
    /**
     * Does nothing - description can not be set after creation
     * @param description 
     */
    public void setDescription(String description) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Does nothing - name can not be set after creation
     * @param name 
     */
    public void setName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Basic name using the class name
     * @return 
     */
    public String getName() {
        return getClass().getSimpleName();
    }   
}
