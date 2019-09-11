package com.mmm.cms.homehealth.vut.proto;

/**
 * This associates an edit item  with a specific OASIS
 * data element (and value)  If there is an edit enum associated with this
 * object, then there must be an Oasis Data item associated as well,
 * because an error without the Oasis Data item is not useful. 
 *  
 * @author GDIT in collaboration with 3M for CMS Home Health
 *
 */
public interface OasisValidationEditIF {
	
	/**
	 * gets the validation error
	 * 
	 * @return non-null OasisValiationErrorEN
	 */
	OasisEditIF getEdit();
	
	/**
	 * gets the Oasis Date Item 
	 * 
	 * @return non-null OasisDataItemIF 
	 */
	OasisDataItemIF getOasisDataItem();
	
	/**
	 * sets the validation edit type
	 * @param validationError - can not be null
	 */
	void setEdit(OasisEditIF validationError);
	
	/**
	 * sets the Oasis Data item
	 * @param dataItem - can not be null
	 */
	void setOasisDataItem(OasisDataItemIF dataItem);

}
