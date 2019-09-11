package com.mmm.cms.homehealth.vut.proto;

import java.io.Serializable;
import java.util.Map;

/**
 * This defines field and field value to each OASIS-C data element within an OASIS-C
 * record, or any collection of fields. 
 * 
 * @author GDIT in collaboration with 3M for CMS Home Health
 *
 */
public interface OasisDataItemIF extends Map.Entry<String, String>, Serializable {	

}
