/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.proto;

import com.mmm.cms.util.Initializable;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Defines access to the low level elements of codes, groups and casemix.
 * 
 * @author 3M HIS Clinical & Economic Research for CMS Home Health
 */
public interface DataManagerIF extends Initializable, Serializable {

	/**
	 * This creates a Diagnosis code allowing any extending class to create its
	 * own code implementation for use in its grouper version.
	 *
	 * @param code
	 * @param validCode
	 * @param validForScoring
	 * @return non-null DiagnosisCodeIF with its code value, valid code flag and
	 * valid for scoring flag set
	 */
	DiagnosisCodeIF createDiagnosisCode(String code, boolean validCode, boolean validForScoring);

	/**
	 * This creates a Diagnostic Group allowing any extending class to create
	 * its own group implementation for use in its grouper version.
	 *
	 * @param id
	 * @return non-null DiagnosticGroupIF with its ID set
	 */
	DiagnosticGroupIF createDiagnosticGroup(int id);

	/**
	 * This will search the case mix table for the case mix id, and return the
	 * case mix adjustment item
	 *
	 * @param caseMixId
	 * @return Case mix adjustment item if found. null if not found
	 */
	CaseMixAdjustmentItemIF getCaseMixAdjustment(int caseMixId);

	/**
	 * gets the Standard services case mix adjustment table
	 *
	 * @return non-null List of CaseMixAdjustment items. If there was an error
	 * during initialization, the list may be empty
	 */
	List<CaseMixAdjustmentItemIF> getCaseMixAdjustments();

	/**
	 * Gets all the Clinical/Functional codes that are sorted by code value.
	 *
	 * @return
	 */
	List<DiagnosisCodeIF> getClinicalCodes();

	/**
	 * This is a proxy to getDiagnosisCodeBase
	 *
	 * @see #getDiagnosisCodeBase(HashMap codes, String value)
	 * @param value
	 * @return Diagnosis code or null if not a valid code
	 */
	DiagnosisCodeIF getDiagnosisCode(String value);

	/**
	 * Searches for the Diagnostic Group object based on its ID
	 *
	 * @param id
	 * @return DiagnosticGroupIF or null if the ID is not valid
	 */
	DiagnosticGroupIF getDiagnosticGroup(long id);

	/**
	 * gets the list of diagnostic Groups associated with the
	 * Clinical/Functional diagnosis
	 *
	 * @return non-null List of Diagnostic Groups items. If there was an error
	 * during initialization, the list may be empty
	 */
	List<DiagnosticGroupIF> getDiagnosticGroups();

	/**
	 * gets the list of diagnostic Groups associated with the Non-routine
	 * supplies diagnosis
	 *
	 * @return non-null List of Diagnostic Groups items. If there was an error
	 * during initialization, the list may be empty
	 */
	List<DiagnosticGroupIF> getDiagnosticGroupsNRS();

	/**
	 * Get the value of homeHealthGrouper
	 *
	 * @return the value of homeHealthGrouper
	 */
	HomeHealthGrouperIF getHomeHealthGrouper();

	/**
	 * This will search the case mix table for the case mix id, and return the
	 * case mix adjustment item
	 *
	 * @param caseMixId
	 * @return Case mix adjustment item if found. null if not found
	 */
	CaseMixAdjustmentItemIF getNRSCaseMixAdjustment(int caseMixId);

	/**
	 * gets the Non-routines services case mix adjustment table
	 *
	 * @return non-null List of CaseMixAdjustment items. If there was an error
	 * during initialization, the list may be empty
	 */
	List<CaseMixAdjustmentItemIF> getNRSCaseMixAdjustments();

	/**
	 * This is a proxy to getDiagnosisCodeBase
	 *
	 * @see #getDiagnosisCodeBase(HashMap codes, String value)
	 * @param value
	 * @return NRS Diagnosis code or null if not a valid code
	 */
	DiagnosisCodeIF getNRSDiagnosisCode(String value);

	/**
	 * Searches for the Non-Routine Supplies Diagnostic Group based on the ID
	 *
	 * @param id
	 * @return DiagnosticGroup or null if the id is not found
	 */
	DiagnosticGroupIF getNRSDiagnosticGroup(long id);

	/**
	 * Gets all the
	 *
	 * @return
	 */
	List<DiagnosisCodeIF> getNonRoutineCodes();

	Pattern getSplitPattern();

	boolean isIncludeDescriptions();

	void setIncludeDescriptions(boolean includeDescriptions);
	
}
