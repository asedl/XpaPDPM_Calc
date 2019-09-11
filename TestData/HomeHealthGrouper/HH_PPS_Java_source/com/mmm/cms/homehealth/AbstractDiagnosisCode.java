/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.CodeType_EN;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;

/**
 * Provides the base information about a Diagnosis or Procedure code, including
 * the code value, type, and associated Group.
 *
 * @author 3M HIS
 */
public abstract class AbstractDiagnosisCode implements DiagnosisCodeIF, Cloneable {

    /**
     * an Empty string of length 0
     */
    public final static String EMPTY_CODE = "";
    private String code;
    private String description;
    private CodeType_EN codeType;
    private DiagnosticGroupIF diagnosticGroup;
    private boolean validForScoring;
    private boolean validCode;
    private boolean optionalPaymentCode;

    /**
     * Constructs the code with an empty code value
     */
    public AbstractDiagnosisCode() {
        this.code = EMPTY_CODE;
        this.diagnosticGroup = DiagnosticGroup.GROUP_UNKNOWN;
    }

    /**
     * This method will bypass the code and type validation, assuming that the
     * extended class knows what type it is and also ensure that the code value
     * is "interned" in order to reduce the number of code string objects are
     * created and used.
     *
     * @param code
     * @param codeType
     */
    public AbstractDiagnosisCode(final String code, final CodeType_EN codeType) {
        this.code = code.intern().toUpperCase(); // OVK: 6/15/16 PBI-164243
        this.codeType = codeType;
        this.diagnosticGroup = DiagnosticGroup.GROUP_UNKNOWN;
    }

    /**
     * @param code
     * @param codeType
     * @param validCode
     * @param validForScoring
     */
    public AbstractDiagnosisCode(final String code, final CodeType_EN codeType, final boolean validCode,
            final boolean validForScoring) {
        this.code = code.intern().toUpperCase(); // OVK: 6/15/16 PBI-164243
        this.codeType = codeType;
        this.validCode = validCode;
        this.validForScoring = validForScoring;
        this.diagnosticGroup = DiagnosticGroup.GROUP_UNKNOWN;
    }

    /**
     * Clones the code
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public DiagnosisCodeIF clone() throws CloneNotSupportedException {
        return (DiagnosisCodeIF) super.clone();
    }

    /**
     * hash code based on the code's value
     *
     * @return
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }

    /**
     * Compares this code's value with another code's value
     *
     * @param obj
     * @return true if the values are the same
     */
    @Override
    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }

    @Override
    public int compareTo(Object obj) {
        final int diff;
        if (obj instanceof DiagnosisCodeIF) {
            final DiagnosisCodeIF other = (DiagnosisCodeIF) obj;
            final String otherCode = other.getCode();

            if (this.code == null) {
                diff = otherCode != null ? -1 : 0;
            } else {
                diff = this.code.compareTo(otherCode);
            }
        } else {
            diff = 1;
        }
        return diff;
    }

    /**
     * Get the value of description
     *
     * @return the value of description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Set the value of description
     *
     * @param description new value of description
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the value of code
     *
     * @return the value of code
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * Set the value of code but ensures that the code contains only alpha-
     * numeric or decimal characters, or is empty; Codes within this class are
     * not representations of the the way the code is stored but instead it is a
     * usable code value or empty.
     *
     * @param code new value of code
     */
    @Override
    public void setCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Code value can not be null");
        } else {
            final char chars[] = code.toCharArray();
            for (char curChar : chars) {
                if (curChar == '.' || Character.isLetterOrDigit(curChar)) {
                    continue;
                } else {
                    throw new IllegalArgumentException("Code value can not be illegal character of '" + curChar + "'. Full code value = '" + code + "'");
                }
            }
        }

        this.code = code;
    }

    /**
     * Sets the code type - ICD-9 or ICD-10
     *
     * @param type valid code type
     */
    @Override
    public void setCodeType(CodeType_EN type) {
        codeType = type;
    }

    /**
     * gets the code type
     *
     * @return the code type
     */
    @Override
    public CodeType_EN getCodeType() {
        return codeType;
    }

    /**
     * Get the value of diagnosticGroup
     *
     * @return the value of diagnosticGroup
     */
    @Override
    public DiagnosticGroupIF getDiagnosticGroup() {
        return diagnosticGroup;
    }

    /**
     * Set the value of diagnosticGroup
     *
     * @param diagnosticGroup new value of diagnosticGroup
     */
    @Override
    public void setDiagnosticGroup(DiagnosticGroupIF diagnosticGroup) {
        this.diagnosticGroup = diagnosticGroup;
    }

    /**
     * determines if the code is an E-code
     *
     * @return true if the first character in the code value is 'E'
     */
    @Override
    public boolean isExternalCauseCode() {
        boolean bool;

        if (code.isEmpty()) {
            bool = false;
        } else {
            if (this.codeType == CodeType_EN.ICD_9) {
                bool = 'E' == code.charAt(0);
            } else if (this.codeType == CodeType_EN.ICD_10) {
                switch (code.charAt(0)) {
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                        bool = true;
                        break;
                    default:
                        bool = false;
                        break;
                }
            } else {
                bool = false;
            }
        }

        return bool;
    }

    /**
     * determine if the code is a V-code
     *
     * @return true if the first character in the code value is 'V'
     * @deprecated as a way to determine Optional Payment or Primary Awarding
     */
    @Override
    public boolean isVCode() {
        boolean bool;
        if (code.isEmpty()) {
            bool = false;
        } else if (this.codeType == CodeType_EN.ICD_9) {
            bool = 'V' == code.charAt(0);
        } else {
            bool = 'Z' == code.charAt(0);
        }
        return bool;
    }

    /**
     * Get the value of validForScoring
     *
     * @return the value of validForScoring
     */
    @Override
    public boolean isValidForScoring() {
        return validForScoring;
    }

    /**
     * Set the value of validForScoring
     *
     * @param validForScoring new value of validForScoring
     */
    @Override
    public void setValidForScoring(boolean validForScoring) {
        this.validForScoring = validForScoring;
    }

    /**
     * determines if the code is blank
     *
     * @return true if the code includes only spaces
     */
    @Override
    public boolean isEmpty() {
        boolean isblank = true;

        if (code != null && code != EMPTY_CODE) {
            int idx = 0;
            final char chars[] = code.toCharArray();

            while (idx < chars.length) {
                if (chars[idx++] != ' ') {
                    isblank = false;
                    break;
                }
            }
        }
        return isblank;
    }

    /**
     * Get the value of validCode
     *
     * @return the value of validCode
     */
    @Override
    public boolean isValidCode() {
        return validCode;
    }

    /**
     * Set the value of validCode
     *
     * @param validCode new value of validCode
     */
    @Override
    public void setValidCode(boolean validCode) {
        this.validCode = validCode;
    }

    /**
     * determines if the code is an optional V-code
     *
     * @return true if this code is set to be an optional V-Code
     */
    @Override
    public boolean isOptionalPaymentCode() {
        return optionalPaymentCode;
    }

    /**
     * Sets the optional payment code flag - Originally used for V-Codes to
     * indicate that the payment codes are to be used.
     *
     * @param bool
     */
    @Override
    public void setOptionalPaymentCode(boolean bool) {
        optionalPaymentCode = bool;
    }

    /**
     * Returns only the code value. To get a full definition of the code, use
     * toStringFinest()
     *
     * @return code value, which may be blank
     */
    @Override
    public String toString() {
        return code;
    }

    /**
     * Returns detailed information about this code. Often used in debugging or
     * logging with "FINEST"
     *
     * @return Details string about this code
     */
    public String toStringFinest() {
        return "AbstractDiagnosisCode{" + "code=" + code
                + ", description=" + description
                + ", codeType=" + codeType
                + ", diagnosticGroup=" + diagnosticGroup
                + ", validForScoring=" + validForScoring
                + ", validCode=" + validCode
                + ", optionalPaymentCode=" + optionalPaymentCode + '}';
    }
}
