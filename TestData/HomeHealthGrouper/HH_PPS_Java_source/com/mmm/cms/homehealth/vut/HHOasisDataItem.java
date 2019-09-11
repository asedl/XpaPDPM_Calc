/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut;

import com.mmm.cms.homehealth.vut.proto.OasisDataItemIF;

/**
 * This provides a read only name of an OASIS-C1 data item and its
 * read only value 
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class HHOasisDataItem implements OasisDataItemIF {
    
    /**
     * Key of the item
     */
    final private String key;
    /**
     * Value of the item
     */
    final private String value;

    public HHOasisDataItem(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    /**
     * This method does not actually do anything.  The key can only be set
     * within the constructor
     * 
     * @param key 
     */
    public void setKey(String key) {
    }

    /**
     * This method does not actually do anything.  The value can only be set
     * within the constructor.
     * 
     * @param value
     * @return the parameter value
     */
    @Override
    public String setValue(String value) {
        return value;
    }

	@Override
	public String toString() {
		return "HHOasisDataItem{" + "key=" + key + ", value=" + value + '}';
	}
    
}
