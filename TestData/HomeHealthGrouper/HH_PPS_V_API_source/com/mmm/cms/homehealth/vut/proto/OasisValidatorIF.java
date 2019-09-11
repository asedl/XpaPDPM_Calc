package com.mmm.cms.homehealth.vut.proto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * This defines how clients interact with the Validation Utility directly. In
 * general, implementations of the interface should be thread safe and may
 * require initialization prior to performing validation.
 * 
 * @author GDIT in collaboration with 3M for CMS Home Health
 * 
 */
public interface OasisValidatorIF {

	/**
	 * gets the validation version identifier
	 * @return non-null identifier
	 */
	String getVersion();
	
	/**
	 * validates an XML formatted OASIS-C1 string
	 * 
	 * @param asmtString
	 * @return CollectionValidationErrorsIF.  If null, then there are no errors. If 
	 * not null, then there is at least one error.
	 */
	CollectionValidationEditsIF validateAsmtString(String asmtString);

	/**
	 * validates an XML formatted OASIS-C1 string read from the input stream.  This method 
	 * will not close the stream.
	 * 
	 * @param asmtIS
	 * @return CollectionValidationErrorsIF.  If null, then there are no errors. If 
	 * not null, then there is at least one error.
	 */
	CollectionValidationEditsIF validateAsmtString(InputStream asmtIS);
	
	/**
	 * validates an OasisRecordIF.
	 * If the OasisRecordIF is not a complete record, the validation may or may not validate
	 * on the missing items.
	 * 
	 * @param oasisRecord
	 * @return
	 */
	CollectionValidationEditsIF validateAsmtString(OasisValidatableRecordIF oasisRecord);

	/**
	 * provides a convenience method for printing errors in a consistent way.
	 * 
	 * @param validationErrors
	 * @return
	 */
	List<String> writeResultsToArray(
			CollectionValidationEditsIF validationErrors);

	/**
	 * Describe the location of the Spec XML files 
	 * Name = spec.xml.folder, Value = user defined path to folder 
	 * Name = icd.flag, Value = true (perform ICD-9/10 look up), false
	 * 
	 * examples:
	 *   spec.xml.folder=c:/Program Files/VUT
	 *   icd.flag=false
	 *   
	 * @throws IOException if the spec xml folder can not be found or read properly
	 */
	void init(Properties properties) throws IOException;

}
