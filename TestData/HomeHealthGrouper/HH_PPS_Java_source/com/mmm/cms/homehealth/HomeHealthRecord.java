/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.RecordType;
import com.mmm.cms.util.IntegerFormat;
import com.mmm.cms.util.OasisCalendarFormatter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This implementation of HomeHealthRecordIF uses the OASIS variable names. It
 * holds only the information needed for scoring the home health record, i.e. it
 * does not hold any extra OASIS information.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HomeHealthRecord implements HomeHealthRecordIF {

    private transient HashMap<String, Method> getMethodsTable;
    public static final String oneSpace = " ";
    public static final String twoSpace = "  ";
    private Calendar START_CARE_DT;
    private Calendar INFO_COMPLETED_DT;
    private String ASSMT_REASON = twoSpace;
    private DiagnosisCodeIF PRIMARY_DIAG_ICD;
    private DiagnosisCodeIF OTH_DIAG1_ICD;
    private DiagnosisCodeIF OTH_DIAG2_ICD;
    private DiagnosisCodeIF OTH_DIAG3_ICD;
    private DiagnosisCodeIF OTH_DIAG4_ICD;
    private DiagnosisCodeIF OTH_DIAG5_ICD;
    private String THH_IV_INFUSION = oneSpace;
    private String THH_PAR_NUTRITION = oneSpace;
    private String THH_ENT_NUTRITION = oneSpace;
    private String THH_NONE_ABOVE = oneSpace;
    private String VISION = twoSpace;
    private String FREQ_PAIN = twoSpace;
    private String LESION_OPEN_WND = oneSpace;
    private String NBR_PRSULC_STG1 = twoSpace;
    private String NBR_PRSULC_STG2 = twoSpace;
    private String NBR_PRSULC_STG3 = twoSpace;
    private String NBR_PRSULC_STG4 = twoSpace;
    private String STG_PRBLM_ULCER = twoSpace;
    private String STASIS_ULCER = oneSpace;
    private String NBR_STASULC = twoSpace;
    private String STAT_PRB_STASULC = twoSpace;
    private String SURG_WOUND = oneSpace;
    private String STAT_PRB_SURGWND = twoSpace;
    private String WHEN_DYSPNEIC = twoSpace;
    private String UR_INCONT = twoSpace;
    private String BWL_INCONT = twoSpace;
    private String OSTOMY = twoSpace;
    private String CUR_DRESS_UPPER = twoSpace;
    private String CUR_DRESS_LOWER = twoSpace;
    private String CUR_BATHING = twoSpace;
    private String CUR_TOILETING = twoSpace;
    private String CUR_TRANSFERRING = twoSpace;
    private String CUR_AMBULATION = twoSpace;
    private String CUR_INJECT_MEDS = twoSpace;
    private String EPISODE_TIMING = twoSpace;
    private DiagnosisCodeIF PMT_DIAG_ICD_A3;
    private DiagnosisCodeIF PMT_DIAG_ICD_B3;
    private DiagnosisCodeIF PMT_DIAG_ICD_C3;
    private DiagnosisCodeIF PMT_DIAG_ICD_D3;
    private DiagnosisCodeIF PMT_DIAG_ICD_E3;
    private DiagnosisCodeIF PMT_DIAG_ICD_F3;
    private DiagnosisCodeIF PMT_DIAG_ICD_A4;
    private DiagnosisCodeIF PMT_DIAG_ICD_B4;
    private DiagnosisCodeIF PMT_DIAG_ICD_C4;
    private DiagnosisCodeIF PMT_DIAG_ICD_D4;
    private DiagnosisCodeIF PMT_DIAG_ICD_E4;
    private DiagnosisCodeIF PMT_DIAG_ICD_F4;
    private int THER_NEED_NUM;
    private String THER_NEED_NA = oneSpace;
    private String HIPPS_CODE;  // MAY NOT NEED THIS HERE

    /**
     * Constructor with the dirty flag set to true
     */
    public HomeHealthRecord() {
        getMethodsTable = new HashMap<String, Method>();
    }

    public RecordType getRecordType() {
        return RecordType.UNKNOWN;
    }

    /**
     * This will get any Diagnosis code in any of the fields M0230, M0240,
     * M0246x3, M0246x4 with an continuous zero-based index from 0 to 17
     *
     * @param index
     * @return a DiagnosisCodeIF object. May be null if the index is out of
     * range.
     */
    @Override
    public DiagnosisCodeIF getDiagnosisCode(int index) {
        DiagnosisCodeIF code;

        switch (index) {
            case 0:
                code = PRIMARY_DIAG_ICD;
                break;

            case 1:
                code = OTH_DIAG1_ICD;
                break;

            case 2:
                code = OTH_DIAG2_ICD;
                break;

            case 3:
                code = OTH_DIAG3_ICD;
                break;

            case 4:
                code = OTH_DIAG4_ICD;
                break;

            case 5:
                code = OTH_DIAG5_ICD;
                break;

            case 6:
                code = PMT_DIAG_ICD_A3;
                break;

            case 7:
                code = PMT_DIAG_ICD_B3;
                break;

            case 8:
                code = PMT_DIAG_ICD_C3;
                break;

            case 9:
                code = PMT_DIAG_ICD_D3;
                break;

            case 10:
                code = PMT_DIAG_ICD_E3;
                break;

            case 11:
                code = PMT_DIAG_ICD_F3;
                break;

            case 12:
                code = PMT_DIAG_ICD_A4;
                break;

            case 13:
                code = PMT_DIAG_ICD_B4;
                break;

            case 14:
                code = PMT_DIAG_ICD_C4;
                break;

            case 15:
                code = PMT_DIAG_ICD_D4;
                break;

            case 16:
                code = PMT_DIAG_ICD_E4;
                break;

            case 17:
                code = PMT_DIAG_ICD_F4;
                break;

            default:
                code = null;
                break;
        }

        return code;
    }

    /**
     * This will get any Diagnosis code in any of the fields M0246x3 with an
     * continous zero-based index from 0 to 5
     *
     * @param index
     * @return a DiagnosisCodeIF object. May be null if the index is out of
     * range.
     */
    @Override
    public DiagnosisCodeIF getOptionalDiagnosisCode3(int index) {
        DiagnosisCodeIF code;

        switch (index) {
            case 0:
                code = PMT_DIAG_ICD_A3;
                break;

            case 1:
                code = PMT_DIAG_ICD_B3;
                break;

            case 2:
                code = PMT_DIAG_ICD_C3;
                break;

            case 3:
                code = PMT_DIAG_ICD_D3;
                break;

            case 4:
                code = PMT_DIAG_ICD_E3;
                break;

            case 5:
                code = PMT_DIAG_ICD_F3;
                break;

            default:
                code = null;
                break;
        }
        return code;
    }

    /**
     * This will get any Diagnosis code in any of the fields M0246x4 with an
     * continous zero-based index from 0 to 5
     *
     * @param index
     * @return a DiagnosisCodeIF object. May be null if the index is out of
     * range.
     */
    @Override
    public DiagnosisCodeIF getOptionalDiagnosisCode4(int index) {
        DiagnosisCodeIF code;

        switch (index) {
            case 0:
                code = PMT_DIAG_ICD_A4;
                break;

            case 1:
                code = PMT_DIAG_ICD_B4;
                break;

            case 2:
                code = PMT_DIAG_ICD_C4;
                break;

            case 3:
                code = PMT_DIAG_ICD_D4;
                break;

            case 4:
                code = PMT_DIAG_ICD_E4;
                break;

            case 5:
                code = PMT_DIAG_ICD_F4;
                break;

            default:
                code = null;
                break;
        }
        return code;
    }

    /**
     * @param groupNum
     * @param codePosition
     * @return true if the group number is represented by a code somewhere on
     * the record
     */
    @Override
    public boolean isDiagnosticGroupOnRecord(int groupNum, int codePosition) {
        DiagnosisCodeIF diagCode;
        int idx = 18;
        boolean isPresent = false;

        while (idx-- > 0) {
            // skip the code position provided so it is not counted
            // twice
            if (idx != codePosition) {
                // get the diagnosis group indicator
                diagCode = getDiagnosisCode(idx);
                if (diagCode != null && diagCode.isValidForScoring()
                        && groupNum == diagCode.getDiagnosticGroup().getId()) {
                    isPresent = true;
                    break;
                }
            }
        }
        return isPresent;
    }

    /**
     * gets the START_CARE_DT
     *
     * Format: YYYYMMDD
     *
     * @return
     */
    @Override
    public Calendar getSTART_CARE_DT() {
        return START_CARE_DT;
    }

    /**
     * Format: YYYYMMDD
     *
     * @param val
     */
    @Override
    public void setSTART_CARE_DT(Calendar val) {
        START_CARE_DT = val;
    }

    /**
     * gets the INFO_COMPLETED_DT
     *
     * Format: YYYYMMDD
     *
     * @return
     */
    @Override
    public Calendar getINFO_COMPLETED_DT() {
        return INFO_COMPLETED_DT;
    }

    /**
     * Format: YYYYMMDD
     *
     * @param val
     */
    @Override
    public void setINFO_COMPLETED_DT(Calendar val) {
        INFO_COMPLETED_DT = val;
    }

    /**
     * gets the ASSMT_REASON
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getASSMT_REASON() {
        return ASSMT_REASON;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setASSMT_REASON(String val) {
        ASSMT_REASON = val;
    }

    /**
     * gets the PRIMARY_DIAG_ICD
     *
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space. -Character 2 must be a 0 (zero)
     * thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a
     * decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPRIMARY_DIAG_ICD() {
        return PRIMARY_DIAG_ICD;
    }

    /**
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space. -Character 2 must be a 0 (zero)
     * thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a
     * decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPRIMARY_DIAG_ICD(DiagnosisCodeIF val) {
        PRIMARY_DIAG_ICD = val;
    }

    /**
     * gets the OTH_DIAG1_ICD
     *
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space or "E". -Character 2 must be a 0
     * (zero) thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must
     * be a decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space.
     * -If character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getOTH_DIAG1_ICD() {
        return OTH_DIAG1_ICD;
    }

    /**
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space or "E". -Character 2 must be a 0
     * (zero) thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must
     * be a decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space.
     * -If character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setOTH_DIAG1_ICD(DiagnosisCodeIF val) {
        OTH_DIAG1_ICD = val;
    }

    /**
     * gets the OTH_DIAG2_ICD
     *
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space or "E". -Character 2 must be a 0
     * (zero) thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must
     * be a decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space.
     * -If character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getOTH_DIAG2_ICD() {
        return OTH_DIAG2_ICD;
    }

    /**
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space or "E". -Character 2 must be a 0
     * (zero) thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must
     * be a decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space.
     * -If character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setOTH_DIAG2_ICD(DiagnosisCodeIF val) {
        OTH_DIAG2_ICD = val;
    }

    /**
     * gets the OTH_DIAG3_ICD
     *
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space or "E". -Character 2 must be a 0
     * (zero) thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must
     * be a decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space.
     * -If character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getOTH_DIAG3_ICD() {
        return OTH_DIAG3_ICD;
    }

    /**
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space or "E". -Character 2 must be a 0
     * (zero) thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must
     * be a decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space.
     * -If character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setOTH_DIAG3_ICD(DiagnosisCodeIF val) {
        OTH_DIAG3_ICD = val;
    }

    /**
     * gets the OTH_DIAG4_ICD
     *
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space or "E". -Character 2 must be a 0
     * (zero) thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must
     * be a decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space.
     * -If character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getOTH_DIAG4_ICD() {
        return OTH_DIAG4_ICD;
    }

    /**
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space or "E". -Character 2 must be a 0
     * (zero) thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must
     * be a decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space.
     * -If character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setOTH_DIAG4_ICD(DiagnosisCodeIF val) {
        OTH_DIAG4_ICD = val;
    }

    /**
     * gets the OTH_DIAG5_ICD
     *
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space or "E". -Character 2 must be a 0
     * (zero) thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must
     * be a decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space.
     * -If character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getOTH_DIAG5_ICD() {
        return OTH_DIAG5_ICD;
    }

    /**
     * Format: A nonblank value must conform to one of the following two
     * patterns:
     *
     * PATTERN A -Character 1 must be a space or "E". -Character 2 must be a 0
     * (zero) thru 9. -Characters 3 thru 4 must be 0 thru 9. -Character 5 must
     * be a decimal point. -Characters 6 and 7 must be 0 (zero) thru 9 or space.
     * -If character 6 is a space, then character 7 must be a space.
     *
     * PATTERN B -Character 1 must be a space. -Character 2 must be "V".
     * -Characters 3 thru 4 must be 0 thru 9. -Character 5 must be a decimal
     * point. -Characters 6 and 7 must be 0 (zero) thru 9 or space. -If
     * character 6 is a space, then character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setOTH_DIAG5_ICD(DiagnosisCodeIF val) {
        OTH_DIAG5_ICD = val;
    }

    /**
     * gets the THH_IV_INFUSION
     *
     * Format: 0=No/Unchecked, 1=Yes/Checked
     *
     * @return
     */
    @Override
    public String getTHH_IV_INFUSION() {
        return THH_IV_INFUSION;
    }

    /**
     * Format: 0=No/Unchecked, 1=Yes/Checked
     *
     * @param val
     */
    @Override
    public void setTHH_IV_INFUSION(String val) {
        THH_IV_INFUSION = val;
    }

    /**
     * gets the THH_PAR_NUTRITION
     *
     * Format: 0=No/Unchecked, 1=Yes/Checked
     *
     * @return
     */
    @Override
    public String getTHH_PAR_NUTRITION() {
        return THH_PAR_NUTRITION;
    }

    /**
     * Format: 0=No/Unchecked, 1=Yes/Checked
     *
     * @param val
     */
    @Override
    public void setTHH_PAR_NUTRITION(String val) {
        THH_PAR_NUTRITION = val;
    }

    /**
     * gets the THH_ENT_NUTRITION
     *
     * Format: 0=No/Unchecked, 1=Yes/Checked
     *
     * @return
     */
    @Override
    public String getTHH_ENT_NUTRITION() {
        return THH_ENT_NUTRITION;
    }

    /**
     * Format: 0=No/Unchecked, 1=Yes/Checked
     *
     * @param val
     */
    @Override
    public void setTHH_ENT_NUTRITION(String val) {
        THH_ENT_NUTRITION = val;
    }

    /**
     * gets the THH_NONE_ABOVE
     *
     * Format: 0=No/Unchecked, 1=Yes/Checked
     *
     * @return
     */
    @Override
    public String getTHH_NONE_ABOVE() {
        return THH_NONE_ABOVE;
    }

    /**
     * Format: 0=No/Unchecked, 1=Yes/Checked
     *
     * @param val
     */
    @Override
    public void setTHH_NONE_ABOVE(String val) {
        THH_NONE_ABOVE = val;
    }

    /**
     * gets the VISION
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getVISION() {
        return VISION;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setVISION(String val) {
        VISION = val;
    }

    /**
     * gets the FREQ_PAIN
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getPAIN_FREQ_ACTVTY_MVMT() {
        return FREQ_PAIN;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setPAIN_FREQ_ACTVTY_MVMT(String val) {
        FREQ_PAIN = val;
    }

    /**
     * gets the LESION_OPEN_WND
     *
     * Format: 0=No, 1=Yes
     *
     * @return
     */
    @Override
    public String getLESION_OPEN_WND() {
        return LESION_OPEN_WND;
    }

    /**
     * Format: 0=No, 1=Yes
     *
     * @param val
     */
    @Override
    public void setLESION_OPEN_WND(String val) {
        LESION_OPEN_WND = val;
    }

    /**
     * gets the NBR_PRSULC_STG1
     *
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @return
     */
    @Override
    public String getNBR_PRSULC_STG1() {
        return NBR_PRSULC_STG1;
    }

    /**
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @param val
     */
    @Override
    public void setNBR_PRSULC_STG1(String val) {
        NBR_PRSULC_STG1 = val;
    }

    /**
     * gets the NBR_PRSULC_STG2
     *
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @return
     */
    @Override
    public String getNBR_PRSULC_STG2() {
        return NBR_PRSULC_STG2;
    }

    /**
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @param val
     */
    @Override
    public void setNBR_PRSULC_STG2(String val) {
        NBR_PRSULC_STG2 = val;
    }

    /**
     * gets the NBR_PRSULC_STG3
     *
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @return
     */
    @Override
    public String getNBR_PRSULC_STG3() {
        return NBR_PRSULC_STG3;
    }

    /**
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @param val
     */
    @Override
    public void setNBR_PRSULC_STG3(String val) {
        NBR_PRSULC_STG3 = val;
    }

    /**
     * gets the NBR_PRSULC_STG4
     *
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @return
     */
    @Override
    public String getNBR_PRSULC_STG4() {
        return NBR_PRSULC_STG4;
    }

    /**
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @param val
     */
    @Override
    public void setNBR_PRSULC_STG4(String val) {
        NBR_PRSULC_STG4 = val;
    }

    /**
     * gets the STG_PRBLM_ULCER
     *
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @return
     */
    @Override
    public String getSTG_PRBLM_ULCER() {
        return STG_PRBLM_ULCER;
    }

    /**
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @param val
     */
    @Override
    public void setSTG_PRBLM_ULCER(String val) {
        STG_PRBLM_ULCER = val;
    }

    /**
     * gets the STASIS_ULCER
     *
     * Format: 0=No, 1=Yes
     *
     * @return
     */
    @Override
    public String getSTAS_ULCR_PRSNT() {
        return STASIS_ULCER;
    }

    /**
     * Format: 0=No, 1=Yes
     *
     * @param val
     */
    @Override
    public void setSTAS_ULCR_PRSNT(String val) {
        STASIS_ULCER = val;
    }

    /**
     * gets the NBR_STASULC
     *
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @return
     */
    @Override
    public String getNBR_STAS_ULCR() {
        return NBR_STASULC;
    }

    /**
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @param val
     */
    @Override
    public void setNBR_STAS_ULCR(String val) {
        NBR_STASULC = val;
    }

    /**
     * gets the STAT_PRB_STASULC
     *
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @return
     */
    @Override
    public String getSTUS_PRBLM_STAS_ULCR() {
        return STAT_PRB_STASULC;
    }

    /**
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @param val
     */
    @Override
    public void setSTUS_PRBLM_STAS_ULCR(String val) {
        STAT_PRB_STASULC = val;
    }

    /**
     * gets the SURG_WOUND
     *
     * Format: 0=No, 1=Yes
     *
     * @return
     */
    @Override
    public String getSRGCL_WND_PRSNT() {
        return SURG_WOUND;
    }

    /**
     * Format: 0=No, 1=Yes
     *
     * @param val
     */
    @Override
    public void setSRGCL_WND_PRSNT(String val) {
        SURG_WOUND = val;
    }

    /**
     * gets the STAT_PRB_SURGWND
     *
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @return
     */
    @Override
    public String getSTUS_PRBLM_SRGCL_WND() {
        return STAT_PRB_SURGWND;
    }

    /**
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @param val
     */
    @Override
    public void setSTUS_PRBLM_SRGCL_WND(String val) {
        STAT_PRB_SURGWND = val;
    }

    /**
     * gets the WHEN_DYSPNEIC
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getWHEN_DYSPNEIC() {
        return WHEN_DYSPNEIC;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setWHEN_DYSPNEIC(String val) {
        WHEN_DYSPNEIC = val;
    }

    /**
     * gets the UR_INCONT
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getUR_INCONT() {
        return UR_INCONT;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setUR_INCONT(String val) {
        UR_INCONT = val;
    }

    /**
     * gets the BWL_INCONT
     *
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @return
     */
    @Override
    public String getBWL_INCONT() {
        return BWL_INCONT;
    }

    /**
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @param val
     */
    @Override
    public void setBWL_INCONT(String val) {
        BWL_INCONT = val;
    }

    /**
     * gets the tM0550_OSTOMY
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getOSTOMY() {
        return OSTOMY;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setOSTOMY(String val) {
        OSTOMY = val;
    }

    /**
     * gets the CUR_DRESS_UPPER
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getCRNT_DRESS_UPPER() {
        return CUR_DRESS_UPPER;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setCRNT_DRESS_UPPER(String val) {
        CUR_DRESS_UPPER = val;
    }

    /**
     * gets the CUR_DRESS_LOWER
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getCRNT_DRESS_LOWER() {
        return CUR_DRESS_LOWER;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setCRNT_DRESS_LOWER(String val) {
        CUR_DRESS_LOWER = val;
    }

    /**
     * gets the CUR_BATHING
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getCRNT_BATHG() {
        return CUR_BATHING;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setCRNT_BATHG(String val) {
        CUR_BATHING = val;
    }

    /**
     * gets the CUR_TOILETING
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getCRNT_TOILTG() {
        return CUR_TOILETING;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setCRNT_TOILTG(String val) {
        CUR_TOILETING = val;
    }

    /**
     * gets the CUR_TRANSFERRING
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getCRNT_TRNSFRNG() {
        return CUR_TRANSFERRING;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setCRNT_TRNSFRNG(String val) {
        CUR_TRANSFERRING = val;
    }

    /**
     * gets the CUR_AMBULATION
     *
     * Format: Right justified; pad left with zero.
     *
     * @return
     */
    @Override
    public String getCRNT_AMBLTN() {
        return CUR_AMBULATION;
    }

    /**
     * Format: Right justified; pad left with zero.
     *
     * @param val
     */
    @Override
    public void setCRNT_AMBLTN(String val) {
        CUR_AMBULATION = val;
    }

    /**
     * gets the CUR_INJECT_MEDS
     *
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @return
     */
    @Override
    public String getCRNT_MGMT_INJCTN_MDCTN() {
        return CUR_INJECT_MEDS;
    }

    /**
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @param val
     */
    @Override
    public void setCRNT_MGMT_INJCTN_MDCTN(String val) {
        CUR_INJECT_MEDS = val;
    }

    /**
     * gets the EPISODE_TIMING
     *
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @return
     */
    @Override
    public String getEPISODE_TIMING() {
        return EPISODE_TIMING;
    }

    /**
     * Format: Right justified; pad left with zero. Any letters must be upper
     * case.
     *
     * @param val
     */
    @Override
    public void setEPISODE_TIMING(String val) {
        EPISODE_TIMING = val;
    }

    /**
     * gets the PMT_DIAG_ICD_A3
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_A3() {
        return PMT_DIAG_ICD_A3;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_A3(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_A3 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_B3
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_B3() {
        return PMT_DIAG_ICD_B3;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_B3(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_B3 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_C3
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_C3() {
        return PMT_DIAG_ICD_C3;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_C3(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_C3 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_D3
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_D3() {
        return PMT_DIAG_ICD_D3;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_D3(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_D3 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_E3
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_E3() {
        return PMT_DIAG_ICD_E3;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_E3(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_E3 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_F3
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_F3() {
        return PMT_DIAG_ICD_F3;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_F3(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_F3 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_A4
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_A4() {
        return PMT_DIAG_ICD_A4;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_A4(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_A4 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_B4
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_B4() {
        return PMT_DIAG_ICD_B4;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_B4(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_B4 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_C4
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_C4() {
        return PMT_DIAG_ICD_C4;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_C4(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_C4 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_D4
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_D4() {
        return PMT_DIAG_ICD_D4;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_D4(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_D4 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_E4
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_E4() {
        return PMT_DIAG_ICD_E4;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_E4(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_E4 = val;
    }

    /**
     * gets the PMT_DIAG_ICD_F4
     *
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @return
     */
    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_F4() {
        return PMT_DIAG_ICD_F4;
    }

    /**
     * Format: A nonblank value must conform as follows: -Character 1 must be a
     * space. -Character 2 must be a 0 (zero) thru 9. -Characters 3 thru 4 must
     * be 0 thru 9. -Character 5 must be a decimal point. -Characters 6 and 7
     * must be 0 (zero) thru 9 or space. -If character 6 is a space, then
     * character 7 must be a space.
     *
     * @param val
     */
    @Override
    public void setPMT_DIAG_ICD_F4(DiagnosisCodeIF val) {
        PMT_DIAG_ICD_F4 = val;
    }

    /**
     * gets the THER_NEED_NUM
     *
     * Format: Field must be blank or contain a number which is right-justified
     * and zero-filled.
     *
     * @return
     */
    @Override
    public int getTHER_NEED_NBR() {
        return THER_NEED_NUM;
    }

    /**
     * Format: Field must be blank or contain a number which is right-justified
     * and zero-filled.
     *
     * @param val
     */
    @Override
    public void setTHER_NEED_NBR(int val) {
        THER_NEED_NUM = val;
    }

    /**
     * gets the THER_NEED_NA
     *
     * Format: 0=No/Unchecked, 1=Yes/Checked
     *
     * @return
     */
    @Override
    public String getTHER_NEED_NA() {
        return THER_NEED_NA;
    }

    /**
     * Format: 0=No/Unchecked, 1=Yes/Checked
     *
     * @param val
     */
    @Override
    public void setTHER_NEED_NA(String val) {
        THER_NEED_NA = val;
    }

    /**
     * gets the HIPPS_CODE
     *
     * Format: If not blank, must be upper case alphanumeric.
     *
     * @return
     */
    @Override
    public String getHIPPS_CODE() {
        return HIPPS_CODE;
    }

    /**
     * Format: If not blank, must be upper case alphanumeric.
     *
     * @param val
     */
    @Override
    public void setHIPPS_CODE(String val) {
        HIPPS_CODE = val;
    }

    /**
     * A string describing the values contained on this record.
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();

        buffer.append("M0030_START_CARE_DT=");
        OasisCalendarFormatter.format(START_CARE_DT, buffer);
        buffer.append(",M0090_INFO_COMPLETED_DT=");
        OasisCalendarFormatter.format(INFO_COMPLETED_DT, buffer);
        buffer.append(",M0100_ASSMT_REASON=");
        buffer.append(ASSMT_REASON);
        buffer.append(",M0230_PRIMARY_DIAG_ICD=");
        buffer.append(PRIMARY_DIAG_ICD);
        buffer.append(",M0240_OTH_DIAG1_ICD=");
        buffer.append(OTH_DIAG1_ICD);
        buffer.append(",M0240_OTH_DIAG2_ICD=");
        buffer.append(OTH_DIAG2_ICD);
        buffer.append(",M0240_OTH_DIAG3_ICD=");
        buffer.append(OTH_DIAG3_ICD);
        buffer.append(",M0240_OTH_DIAG4_ICD=");
        buffer.append(OTH_DIAG4_ICD);
        buffer.append(",M0240_OTH_DIAG5_ICD=");
        buffer.append(OTH_DIAG5_ICD);
        buffer.append(",M0250_THH_IV_INFUSION=");
        buffer.append(THH_IV_INFUSION);
        buffer.append(",M0250_THH_PAR_NUTRITION=");
        buffer.append(THH_PAR_NUTRITION);
        buffer.append(",M0250_THH_ENT_NUTRITION=");
        buffer.append(THH_ENT_NUTRITION);
        buffer.append(",M0250_THH_NONE_ABOVE=");
        buffer.append(THH_NONE_ABOVE);
        buffer.append(",M0390_VISION=");
        buffer.append(VISION);
        buffer.append(",M0420_FREQ_PAIN=");
        buffer.append(FREQ_PAIN);
        buffer.append(",M0440_LESION_OPEN_WND=");
        buffer.append(LESION_OPEN_WND);
        buffer.append(",M0450_NBR_PRSULC_STG1=");
        buffer.append(NBR_PRSULC_STG1);
        buffer.append(",M0450_NBR_PRSULC_STG2=");
        buffer.append(NBR_PRSULC_STG2);
        buffer.append(",M0450_NBR_PRSULC_STG3=");
        buffer.append(NBR_PRSULC_STG3);
        buffer.append(",M0450_NBR_PRSULC_STG4=");
        buffer.append(NBR_PRSULC_STG4);
        buffer.append(",M0460_STG_PRBLM_ULCER=");
        buffer.append(STG_PRBLM_ULCER);
        buffer.append(",M0468_STASIS_ULCER=");
        buffer.append(STASIS_ULCER);
        buffer.append(",M0470_NBR_STASULC=");
        buffer.append(NBR_STASULC);
        buffer.append(",M0476_STAT_PRB_STASULC=");
        buffer.append(STAT_PRB_STASULC);
        buffer.append(",M0482_SURG_WOUND=");
        buffer.append(SURG_WOUND);
        buffer.append(",M0488_STAT_PRB_SURGWND=");
        buffer.append(STAT_PRB_SURGWND);
        buffer.append(",M0490_WHEN_DYSPNEIC=");
        buffer.append(WHEN_DYSPNEIC);
        buffer.append(",M0520_UR_INCONT=");
        buffer.append(UR_INCONT);
        buffer.append(",M0540_BWL_INCONT=");
        buffer.append(BWL_INCONT);
        buffer.append(",M0550_OSTOMY=");
        buffer.append(OSTOMY);
        buffer.append(",M0650_CUR_DRESS_UPPER=");
        buffer.append(CUR_DRESS_UPPER);
        buffer.append(",M0660_CUR_DRESS_LOWER=");
        buffer.append(CUR_DRESS_LOWER);
        buffer.append(",M0670_CUR_BATHING=");
        buffer.append(CUR_BATHING);
        buffer.append(",M0680_CUR_TOILETING=");
        buffer.append(CUR_TOILETING);
        buffer.append(",M0690_CUR_TRANSFERRING=");
        buffer.append(CUR_TRANSFERRING);
        buffer.append(",M0700_CUR_AMBULATION=");
        buffer.append(CUR_AMBULATION);
        buffer.append(",M0800_CUR_INJECT_MEDS=");
        buffer.append(CUR_INJECT_MEDS);
        buffer.append(",M0110_EPISODE_TIMING=");
        buffer.append(EPISODE_TIMING);
        buffer.append(",M0246_PMT_DIAG_ICD_A3=");
        buffer.append(PMT_DIAG_ICD_A3);
        buffer.append(",M0246_PMT_DIAG_ICD_B3=");
        buffer.append(PMT_DIAG_ICD_B3);
        buffer.append(",M0246_PMT_DIAG_ICD_C3=");
        buffer.append(PMT_DIAG_ICD_C3);
        buffer.append(",M0246_PMT_DIAG_ICD_D3=");
        buffer.append(PMT_DIAG_ICD_D3);
        buffer.append(",M0246_PMT_DIAG_ICD_E3=");
        buffer.append(PMT_DIAG_ICD_E3);
        buffer.append(",M0246_PMT_DIAG_ICD_F3=");
        buffer.append(PMT_DIAG_ICD_F3);
        buffer.append(",M0246_PMT_DIAG_ICD_A4=");
        buffer.append(PMT_DIAG_ICD_A4);
        buffer.append(",M0246_PMT_DIAG_ICD_B4=");
        buffer.append(PMT_DIAG_ICD_B4);
        buffer.append(",M0246_PMT_DIAG_ICD_C4=");
        buffer.append(PMT_DIAG_ICD_C4);
        buffer.append(",M0246_PMT_DIAG_ICD_D4=");
        buffer.append(PMT_DIAG_ICD_D4);
        buffer.append(",M0246_PMT_DIAG_ICD_E4=");
        buffer.append(PMT_DIAG_ICD_E4);
        buffer.append(",M0246_PMT_DIAG_ICD_F4=");
        buffer.append(PMT_DIAG_ICD_F4);
        buffer.append(",M0826_THER_NEED_NUM=");
        buffer.append(THER_NEED_NUM);
        buffer.append(",M0826_THER_NEED_NA=");
        buffer.append(THER_NEED_NA);

        return buffer.toString();
    }

    /**
     * Sets the record's fields to their default values
     */
    @Override
    public void setBlank() {
        START_CARE_DT = new GregorianCalendar();
        INFO_COMPLETED_DT = new GregorianCalendar();
        PRIMARY_DIAG_ICD = new DiagnosisCode();
        OTH_DIAG1_ICD = new DiagnosisCode();
        OTH_DIAG2_ICD = new DiagnosisCode();
        OTH_DIAG3_ICD = new DiagnosisCode();
        OTH_DIAG4_ICD = new DiagnosisCode();
        OTH_DIAG5_ICD = new DiagnosisCode();

        ASSMT_REASON
                = THH_IV_INFUSION
                = THH_PAR_NUTRITION
                = THH_ENT_NUTRITION
                = THH_NONE_ABOVE
                = VISION
                = FREQ_PAIN
                = LESION_OPEN_WND
                = NBR_PRSULC_STG1
                = NBR_PRSULC_STG2
                = NBR_PRSULC_STG3
                = NBR_PRSULC_STG4
                = STG_PRBLM_ULCER
                = STASIS_ULCER
                = NBR_STASULC
                = STAT_PRB_STASULC
                = SURG_WOUND
                = STAT_PRB_SURGWND
                = WHEN_DYSPNEIC
                = UR_INCONT
                = BWL_INCONT
                = OSTOMY
                = CUR_DRESS_UPPER
                = CUR_DRESS_LOWER
                = CUR_BATHING
                = CUR_TOILETING
                = CUR_TRANSFERRING
                = CUR_AMBULATION
                = CUR_INJECT_MEDS
                = EPISODE_TIMING
                = THER_NEED_NA = "";

        PMT_DIAG_ICD_A3 = new DiagnosisCode();
        PMT_DIAG_ICD_B3 = new DiagnosisCode();
        PMT_DIAG_ICD_C3 = new DiagnosisCode();
        PMT_DIAG_ICD_D3 = new DiagnosisCode();
        PMT_DIAG_ICD_E3 = new DiagnosisCode();
        PMT_DIAG_ICD_F3 = new DiagnosisCode();
        PMT_DIAG_ICD_A4 = new DiagnosisCode();
        PMT_DIAG_ICD_B4 = new DiagnosisCode();
        PMT_DIAG_ICD_C4 = new DiagnosisCode();
        PMT_DIAG_ICD_D4 = new DiagnosisCode();
        PMT_DIAG_ICD_E4 = new DiagnosisCode();
        PMT_DIAG_ICD_F4 = new DiagnosisCode();
        THER_NEED_NUM = 0;
    }

    @Override
    public String getFieldValue(String fieldName) {
        Method method;
        String value = null;

        try {
            method = this.getClass().getMethod("get" + fieldName, (Class[]) null);
            Object obj;

            getMethodsTable.put(fieldName, method);
            try {
                obj = method.invoke(this, (Object[]) null);
                if (obj != null) {
                    if (obj instanceof String) {
                        value = (String) obj;
                    } else {
                        value = obj.toString();
                    }
                }
            } catch (IllegalAccessException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
            }

        } catch (NoSuchMethodException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        }

        return value;
    }
}
