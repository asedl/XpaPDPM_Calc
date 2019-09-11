/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.CodeType_EN;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.EtiologyPairingListIF;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a blank code that can not be altered.
 *
 * Since so many records include blank items this class helps to save memory by
 * providing a single non-changing code for use when processing those blank
 * codes
 *
 * @author 3M Health Information Systems for CMS Home Health
 *
 */
public class DiagnosisCode_Empty implements DiagnosisCodeIF {

    public final static DiagnosisCode_Empty DEFAULT = new DiagnosisCode_Empty();

    protected DiagnosisCode_Empty() {
        // this is an unchangable, singleton object
    }

    @Override
    public void setDiabeticUlcer(boolean diabeticUlcer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEtiologyPairCodes(List<DiagnosisCodeIF> pairs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrimary(boolean primary) {
        if (primary) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, CommonMessageText.EVENT_MSG_HEADER + "DiagnosisCode_Empty - trying to set as Primary Diagnosis - this is not allowed");
        }
    }

    @Override
    public void setSecondaryOnly(boolean bool) {
        if (bool) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, CommonMessageText.EVENT_MSG_HEADER + "DiagnosisCode_Empty - trying to set as Secondary Only Diagnosis - this is not allowed");
        }
    }

    @Override
    public void setUlcer(boolean ulcer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCode(String code) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCodeType(CodeType_EN type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDescription(String description) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDiagnosticGroup(DiagnosticGroupIF diagnosticGroup) {
        throw new UnsupportedOperationException();
    }

    public void setGrouper(HomeHealthGrouperIF grouper) {
        throw new UnsupportedOperationException();
    }

    public void setOptionalVCode(boolean bool) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOptionalPaymentCode(boolean bool) {
        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED);
    }

    @Override
    public void setValidCode(boolean validCode) {
        throw new UnsupportedOperationException();
    }

    /**
     * Does nothing
     *
     * @param validForScoring
     */
    @Override
    public void setValidForScoring(boolean validForScoring) {
    }

    @Override
    public DiagnosisCodeIF clone() throws CloneNotSupportedException {
        return this;
    }

    @Override
    public EtiologyPairingListIF getEtiologyPairCodes() {
        return null;
    }

    @Override
    public boolean isDiabeticUlcer() {
        return false;
    }

    @Override
    public boolean isPrimary() {
        return false;
    }

    @Override
    public boolean isSecondaryOnly() {
        return false;
    }

    @Override
    public boolean isUlcer() {
        return false;
    }

    /**
     * Always returns false
     *
     * @param code
     * @return false
     */
    @Override
    public boolean isEtiologyInPairingList(DiagnosisCodeIF code) {
        return false;
    }

    /**
     * Always returns false
     *
     * @param etiologyCode
     * @return true if the codes can be paired
     */
    @Override
    public boolean isValidEtiologyPairing(DiagnosisCodeIF etiologyCode) {
        return false;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public CodeType_EN getCodeType() {
        return CodeType_EN.ICD_9;
    }

    @Override
    public DiagnosticGroupIF getDiagnosticGroup() {
        return DiagnosticGroup.GROUP_UNKNOWN;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isExternalCauseCode() {
        return false;
    }

    @Override
    public boolean isOptionalPaymentCode() {
        return false;
    }

    @Override
    public boolean isVCode() {
        return false;
    }

    @Override
    public boolean isValidCode() {
        return false;
    }

    @Override
    public boolean isValidForScoring() {
        return false;
    }

    @Override
    public String getDescription() {
        return "";
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "";
    }

    /**
     * Always returns false
     *
     * @return false
     */
    @Override
    public boolean isPrimaryAwardableCode() {
        return false;
    }

    /**
     * does nothing
     *
     * @param primaryAwardableVCode
     * @throws IllegalStateException
     */
    @Override
    public void setPrimaryAwardableCode(boolean primaryAwardableVCode) throws IllegalStateException {
    }

    /**
     * Always returns 0
     *
     * @return 0
     */
    @Override
    public int getOffset() {
        return 0;
    }

    /**
     * does nothing
     *
     * @param position
     */
    @Override
    public void setOffset(int position) {
    }
    
    @Override
    public int compareTo(Object obj) {
        final int diff;
        if (obj instanceof DiagnosisCodeIF) {
            final DiagnosisCodeIF other = (DiagnosisCodeIF) obj;

            diff = "".compareTo(other.getCode());
        } else {
            diff = 1;
        }
        return diff;
    }


}
