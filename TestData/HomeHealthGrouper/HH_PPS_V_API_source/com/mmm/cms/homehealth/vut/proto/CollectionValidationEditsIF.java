package com.mmm.cms.homehealth.vut.proto;

import java.util.List;

/**
 * This defines a collection of validation errors.  This collection may 
 * be empty. 
 *  
 * @author GDIT in collaboration with 3M for CMS Home Health
 *
 */
public interface CollectionValidationEditsIF extends List<OasisValidationEditIF>  {
	
	/**
	 * gets a list of validation edits based on the edit type enum. 
	 * 
	 * @param edit - if this parameter is null, then it returns all the edit.  If this
	 * parameter is not null, then it returns all the edit pertaining to the 
	 * specific edit type.
	 * 
	 * @return list of OasisValidationErrorIF, if null, then it is empty. If not null, then
	 * there is at least 1 error in the list. 
	 */
	List<OasisValidationEditIF> getEdits(OasisEditIF edit);
	
	/**
	 * gets a list of validation edits based on the edit type enum. 
	 * 
	 * @param edit - if this parameter is null, then it returns all the edits.  If this
	 * parameter is not null, then it returns all the edits pertaining to the 
	 * specific variable name.
	 * 
	 * @param variableName
	 * 
	 * @return list of OasisValidationErrorIF, if null, then it is empty. If not null, then
	 * there is at least 1 error in the list. 
	 */
	List<OasisValidationEditIF> getEdits(String variableName);
	
	/**
	 * gets the flag that the edit type has at least one edit with list
	 * 
	 * @param edit
	 * @return true if there is at least 1 edit
	 */
	boolean isEditPresent(OasisEditIF edit);
	
	/**
	 * gets the flag that the variableName has at least one edit with list
	 * 
	 * @param variableName
	 * @return true if there is at least 1 edit for the variableName
	 */
	boolean isEditPresent(String variableName);

}
