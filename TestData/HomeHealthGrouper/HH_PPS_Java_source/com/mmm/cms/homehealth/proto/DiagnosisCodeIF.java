/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

import com.mmm.cms.util.Describable;


/**
 * Holds the information specific to the ICD-9 Diagnosis codes (as opposed to
 * a procedure code) that supports Home Health scoring.
 *
 * Dec 2010 - 3M - Refactored this class to allow method definitions to be
 * more ala carte for the transition to I-10 coding.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface DiagnosisCodeIF extends Describable,
         CodePositionalIF, CodeUlcerIF, PrimaryAwardableIF,
         OptionalPaymentCodeIF,
        EtiologyPairingIF, Cloneable, Comparable {

//    public static final int TYPE_ICD_9_CM_DIAGNOSIS = 1;

    /**
     * creates an exact copy of this object that can be manipulated separately
     * without effecting this object
     *
     * @return creates an exact copy of the code, and should never be null
     * @throws java.lang.CloneNotSupportedException
     */
     DiagnosisCodeIF clone() throws CloneNotSupportedException;

    /**
     * Gets the code value
     * @return
     */
    String getCode();

    /**
     * gets the code type
     *
     * @return The code type integer indicator
     */
    CodeType_EN getCodeType();

    /**
     * gets the Diagnostic Group indicator
     * @return The Diagnostic Group indicator
     */
    DiagnosticGroupIF getDiagnosticGroup();

    /**
     * Gets the 0 based position of the code within the record's Diagnosis
     * "grid".
     * Introduced in V3312 
     * 
     * @return
     */
    int getOffset();
	
    /**
     * Codes can be blank, and this will return true if it is
     *
     * @return
     */
    boolean isEmpty();

    /**
     * This checks the current code value for a starting 'E'.  Blanks
     * are not considered 'E' codes
     *
     * @return true if the code begins with an 'E'
     */
    boolean isExternalCauseCode();

    /**
     * gets the valid ICD-9-CM code indicator. This would be specific to the
     * grouper version and its code listing
     *
     * @return true if the code is valid for the grouper version.  By default
     * all codes should be considered invalid.
     */
    boolean isValidCode();

    /**
     * This value is used to determine if a code is used in the
     * scoring or should be skipped.  Codes that are blank should
     * automatically be considered not valid for scoring
     *
     * @return true if this code can be used in scoring
     */
    boolean isValidForScoring();

    /**
     * This checks the current code value for a starting 'V'.  Blanks
     * are not considered 'V' codes
     *
     * @return true if the code begins with an 'V'
	 * @deprecated - should use isOptionalPayment() or isPrimaryAwarding() instead
     */
    boolean isVCode();

    /**
     * Sets the code value
     * @param code
     */
    void setCode(String code);

    /**
     * Sets the code type - must be either 1 for diagnosis or 2 for procedure
     * @param type
     */
    void setCodeType(CodeType_EN type);

    /**
     * sets the Diagnostic Group indicator
     * @param dg
     */
    void setDiagnosticGroup(DiagnosticGroupIF dg);


    /**
     * Sets the 0 based position of the code within the record's Diagnosis
     * grid
     * 
     * Introduced for V3312
     *
     * @param position
     */
    void setOffset(int position);

    /**
     * sets the valid for scoring flag
     * @param bool
     */
    void setValidForScoring(boolean bool);

    /**
     * Sets the valid code flag
     * @param bool
     */
    void setValidCode(boolean bool);

}
