/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 *
 * Generated from the Oasis data set for
 * table Body_Record_Definition
 */
package com.mmm.cms.homehealth.io.record;

import com.mmm.cms.homehealth.HomeHealthRecord;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_B_IF;
import com.mmm.cms.homehealth.proto.RecordType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents an OASIS record as an extension of the HomeHealthRecord. Anything
 * that is not used by the HomeHealth Grouper is not given a specific place
 * holder, but instead identified as a pass thru variable. Given this, the pass
 * thru variables are chunks of character strings that may contain several OASIS
 * fields within, but they are not individually identified. Instead they are
 * generically named "passThru_??" where the ?? represent the order number of
 * the pass through data.
 *
 * For more information of where these passThru variables are read or stored in
 * the OASIS record along with the Home Health Record information, please see
 * Oasis_B_RecordUtil conversion routines.
 *
 */
public class Oasis_B_Record implements HomeHealthRecord_B_IF {

    private HomeHealthRecord homeHealthRecord;
    private String PRESS_ULCER = HomeHealthRecord.oneSpace;
    private String UNOBS_PRSULC = HomeHealthRecord.oneSpace;
    private String UNOBS_STASULC = HomeHealthRecord.oneSpace;
    private String NBR_SURGWND = HomeHealthRecord.oneSpace;

    private transient StringBuilder strRecord;
    private String passThru_1;
    private String passThru_2;
    private String passThru_3;
    private String passThru_4;
    private String passThru_5;
    private String passThru_6;
    private String passThru_7;
    private String passThru_8;
    private String passThru_9;
    private String passThru_10;
    private String passThru_11;
    private String passThru_12;
    private String passThru_13;
    private String passThru_14;
    private String passThru_15;
    private String passThru_16;
    private String passThru_17;
    private String passThru_18;
    private String passThru_19;
    private String passThru_20;
    private String passThru_21;
    private String passThru_22;
    private String passThru_23;
    private String passThru_24;
    private String passThru_25;
    private String passThru_26;
    private int testRecordId;
    private transient HashMap<String, Method> getMethodsTable;

    public Oasis_B_Record(HomeHealthRecord homeHealthRecord) {
        this.homeHealthRecord = homeHealthRecord;
        getMethodsTable = new HashMap<String, Method>();
    }

    public Oasis_B_Record() {
        this(new HomeHealthRecord());
    }

    public RecordType getRecordType() {
        return RecordType.OASIS_B;
    }
    
    /**
     * gets the PRESS_ULCER
     *
     * Format: 0=No, 1=Yes
     *
     * @return
     */
    @Override
    public String getPRESS_ULCER() {
        return PRESS_ULCER;
    }

    /**
     * Format: 0=No, 1=Yes
     *
     * @param val
     */
    @Override
    public void setPRESS_ULCER(String val) {
        PRESS_ULCER = val;
    }

    
    /**
     * gets the UNOBS_PRSULC
     *
     * Format: 0=No, 1=Yes
     *
     * @return
     */
    @Override
    public String getUNOBS_PRSULC() {
        return UNOBS_PRSULC;
    }

    /**
     * Format: 0=No, 1=Yes
     *
     * @param val
     */
    @Override
    public void setUNOBS_PRSULC(String val) {
        UNOBS_PRSULC = val;
    }

    /**
     * gets the UNOBS_STASULC
     *
     * Format: 0=No, 1=Yes
     *
     * @return
     */
    @Override
    public String getUNOBS_STASULC() {
        return UNOBS_STASULC;
    }

    /**
     * Format: 0=No, 1=Yes
     *
     * @param val
     */
    @Override
    public void setUNOBS_STASULC(String val) {
        UNOBS_STASULC = val;
    }

    /**
     * gets the NBR_SURGWND
     *
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @return
     */
    @Override
    public String getNBR_SURGWND() {
        return NBR_SURGWND;
    }

    /**
     * Format: 00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     *
     * @param val
     */
    @Override
    public void setNBR_SURGWND(String val) {
        NBR_SURGWND = val;
    }

    
    
    /**
     * This method is used this class needs to provide the default values for
     * the values that are ignored by the HomeHealth Grouper. All but the first
     * pass thru will be spaces. The first pass thru will begin with B1, which
     * cooresponds with this record type.
     */
    public void setPassThruDefaults() {
        if (strRecord == null) {
            // build a large string that we can extract from laters
            strRecord = new StringBuilder();
            strRecord.append("B1");
            for (int idx = 0; idx < 150; idx++) {
                strRecord.append("          ");
            }
        }

        passThru_1 = strRecord.substring(0, 176);
        passThru_2 = strRecord.substring(184, 301);
        passThru_3 = strRecord.substring(311, 403);
        passThru_4 = strRecord.substring(410, 412);
        passThru_5 = strRecord.substring(419, 421);
        passThru_6 = strRecord.substring(428, 430);
        passThru_7 = strRecord.substring(437, 439);
        passThru_8 = strRecord.substring(446, 448);
        passThru_9 = strRecord.substring(455, 457);
        passThru_10 = strRecord.substring(461, 528);
        passThru_11 = strRecord.substring(530, 534);
        passThru_12 = strRecord.substring(536, 537);
        passThru_13 = strRecord.substring(550, 552);
        passThru_14 = strRecord.substring(561, 562);
        passThru_15 = strRecord.substring(566, 572);
        passThru_16 = strRecord.substring(574, 576);
        passThru_17 = strRecord.substring(580, 615);
        passThru_18 = strRecord.substring(617, 619);
        passThru_19 = strRecord.substring(621, 623);
        passThru_20 = strRecord.substring(625, 627);
        passThru_21 = strRecord.substring(629, 631);
        passThru_22 = strRecord.substring(633, 635);
        passThru_23 = strRecord.substring(637, 675);
        passThru_24 = strRecord.substring(677, 778);
        passThru_25 = strRecord.substring(868, 1080);
        passThru_26 = strRecord.substring(1085, 1445);
    }

    /**
     * Get the value of testRecordId
     *
     * @return the value of testRecordId
     */
    public int getTestRecordId() {
        return testRecordId;
    }

    /**
     * Set the value of testRecordId
     *
     * @param testRecordId new value of testRecordId
     */
    public void setTestRecordId(int testRecordId) {
        this.testRecordId = testRecordId;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_1() {
        return passThru_1;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_1(String val) {
        passThru_1 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_2() {
        return passThru_2;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_2(String val) {
        passThru_2 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_3() {
        return passThru_3;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_3(String val) {
        passThru_3 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_4() {
        return passThru_4;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_4(String val) {
        passThru_4 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_5() {
        return passThru_5;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_5(String val) {
        passThru_5 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_6() {
        return passThru_6;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_6(String val) {
        passThru_6 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_7() {
        return passThru_7;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_7(String val) {
        passThru_7 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_8() {
        return passThru_8;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_8(String val) {
        passThru_8 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_9() {
        return passThru_9;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_9(String val) {
        passThru_9 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_10() {
        return passThru_10;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_10(String val) {
        passThru_10 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_11() {
        return passThru_11;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_11(String val) {
        passThru_11 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_12() {
        return passThru_12;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_12(String val) {
        passThru_12 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_13() {
        return passThru_13;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_13(String val) {
        passThru_13 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_14() {
        return passThru_14;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_14(String val) {
        passThru_14 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_15() {
        return passThru_15;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_15(String val) {
        passThru_15 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_16() {
        return passThru_16;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_16(String val) {
        passThru_16 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_17() {
        return passThru_17;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_17(String val) {
        passThru_17 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_18() {
        return passThru_18;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_18(String val) {
        passThru_18 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_19() {
        return passThru_19;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_19(String val) {
        passThru_19 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_20() {
        return passThru_20;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_20(String val) {
        passThru_20 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_21() {
        return passThru_21;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_21(String val) {
        passThru_21 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_22() {
        return passThru_22;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_22(String val) {
        passThru_22 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_23() {
        return passThru_23;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_23(String val) {
        passThru_23 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_24() {
        return passThru_24;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_24(String val) {
        passThru_24 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_25() {
        return passThru_25;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_25(String val) {
        passThru_25 = val;
    }

    /**
     * PassThru
     *
     * @return
     */
    public String getPassThru_26() {
        return passThru_26;
    }

    /**
     * PassThru
     *
     * @param val
     */
    public void setPassThru_26(String val) {
        passThru_26 = val;
    }

    /**
     * Internal proxy to the homeHealthRecord.setHIPPS_Code();
     *
     * @param hipps
     */
    @Override
    public void setHIPPS_CODE(String hipps) {
        homeHealthRecord.setHIPPS_CODE(hipps);
    }

    @Override
    public void setTHER_NEED_NBR(int val) {
        homeHealthRecord.setTHER_NEED_NBR(val);
    }

    @Override
    public void setTHER_NEED_NA(String val) {
        homeHealthRecord.setTHER_NEED_NA(val);
    }

    @Override
    public void setCRNT_MGMT_INJCTN_MDCTN(String val) {
        homeHealthRecord.setCRNT_MGMT_INJCTN_MDCTN(val);
    }

    @Override
    public void setCRNT_AMBLTN(String val) {
        homeHealthRecord.setCRNT_AMBLTN(val);
    }

    @Override
    public void setCRNT_TRNSFRNG(String val) {
        homeHealthRecord.setCRNT_TRNSFRNG(val);
    }

    @Override
    public void setCRNT_TOILTG(String val) {
        homeHealthRecord.setCRNT_TOILTG(val);
    }

    @Override
    public void setCRNT_BATHG(String val) {
        homeHealthRecord.setCRNT_BATHG(val);
    }

    @Override
    public void setCRNT_DRESS_LOWER(String val) {
        homeHealthRecord.setCRNT_DRESS_LOWER(val);
    }

    @Override
    public void setCRNT_DRESS_UPPER(String val) {
        homeHealthRecord.setCRNT_DRESS_UPPER(val);
    }

    @Override
    public void setOSTOMY(String val) {
        homeHealthRecord.setOSTOMY(val);
    }

    @Override
    public void setBWL_INCONT(String val) {
        homeHealthRecord.setBWL_INCONT(val);
    }

    @Override
    public void setUR_INCONT(String val) {
        homeHealthRecord.setUR_INCONT(val);
    }

    @Override
    public void setWHEN_DYSPNEIC(String val) {
        homeHealthRecord.setWHEN_DYSPNEIC(val);
    }

    @Override
    public void setSTUS_PRBLM_SRGCL_WND(String val) {
        homeHealthRecord.setSTUS_PRBLM_SRGCL_WND(val);
    }

    @Override
    public void setSRGCL_WND_PRSNT(String val) {
        homeHealthRecord.setSRGCL_WND_PRSNT(val);
    }

    @Override
    public void setSTUS_PRBLM_STAS_ULCR(String val) {
        homeHealthRecord.setSTUS_PRBLM_STAS_ULCR(val);
    }

    @Override
    public void setNBR_STAS_ULCR(String val) {
        homeHealthRecord.setNBR_STAS_ULCR(val);
    }

    @Override
    public void setSTAS_ULCR_PRSNT(String val) {
        homeHealthRecord.setSTAS_ULCR_PRSNT(val);
    }

    @Override
    public void setSTG_PRBLM_ULCER(String val) {
        homeHealthRecord.setSTG_PRBLM_ULCER(val);
    }

    @Override
    public void setNBR_PRSULC_STG4(String val) {
        homeHealthRecord.setNBR_PRSULC_STG4(val);
    }

    @Override
    public void setNBR_PRSULC_STG3(String val) {
        homeHealthRecord.setNBR_PRSULC_STG3(val);
    }

    @Override
    public void setNBR_PRSULC_STG2(String val) {
        homeHealthRecord.setNBR_PRSULC_STG2(val);
    }

    @Override
    public void setNBR_PRSULC_STG1(String val) {
        homeHealthRecord.setNBR_PRSULC_STG1(val);
    }

    @Override
    public void setLESION_OPEN_WND(String val) {
        homeHealthRecord.setLESION_OPEN_WND(val);
    }

    @Override
    public void setPAIN_FREQ_ACTVTY_MVMT(String val) {
        homeHealthRecord.setPAIN_FREQ_ACTVTY_MVMT(val);
    }

    @Override
    public void setVISION(String val) {
        homeHealthRecord.setVISION(val);
    }

    @Override
    public void setTHH_PAR_NUTRITION(String val) {
        homeHealthRecord.setTHH_PAR_NUTRITION(val);
    }

    @Override
    public void setTHH_NONE_ABOVE(String val) {
        homeHealthRecord.setTHH_NONE_ABOVE(val);
    }

    @Override
    public void setTHH_IV_INFUSION(String val) {
        homeHealthRecord.setTHH_IV_INFUSION(val);
    }

    @Override
    public void setTHH_ENT_NUTRITION(String val) {
        homeHealthRecord.setTHH_ENT_NUTRITION(val);
    }

    @Override
    public void setPMT_DIAG_ICD_F4(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_F4(val);
    }

    @Override
    public void setPMT_DIAG_ICD_F3(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_F3(val);
    }

    @Override
    public void setPMT_DIAG_ICD_E4(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_E4(val);
    }

    @Override
    public void setPMT_DIAG_ICD_E3(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_E3(val);
    }

    @Override
    public void setPMT_DIAG_ICD_D4(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_D4(val);
    }

    @Override
    public void setPMT_DIAG_ICD_D3(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_D3(val);
    }

    @Override
    public void setPMT_DIAG_ICD_C4(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_C4(val);
    }

    @Override
    public void setPMT_DIAG_ICD_C3(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_C3(val);
    }

    @Override
    public void setPMT_DIAG_ICD_B4(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_B4(val);
    }

    @Override
    public void setPMT_DIAG_ICD_B3(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_B3(val);
    }

    @Override
    public void setPMT_DIAG_ICD_A4(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_A4(val);
    }

    @Override
    public void setPMT_DIAG_ICD_A3(DiagnosisCodeIF val) {
        homeHealthRecord.setPMT_DIAG_ICD_A3(val);
    }

    @Override
    public void setOTH_DIAG5_ICD(DiagnosisCodeIF val) {
        homeHealthRecord.setOTH_DIAG5_ICD(val);
    }

    @Override
    public void setOTH_DIAG4_ICD(DiagnosisCodeIF val) {
        homeHealthRecord.setOTH_DIAG4_ICD(val);
    }

    @Override
    public void setOTH_DIAG3_ICD(DiagnosisCodeIF val) {
        homeHealthRecord.setOTH_DIAG3_ICD(val);
    }

    @Override
    public void setOTH_DIAG2_ICD(DiagnosisCodeIF val) {
        homeHealthRecord.setOTH_DIAG2_ICD(val);
    }

    @Override
    public void setOTH_DIAG1_ICD(DiagnosisCodeIF val) {
        homeHealthRecord.setOTH_DIAG1_ICD(val);
    }

    @Override
    public void setPRIMARY_DIAG_ICD(DiagnosisCodeIF val) {
        homeHealthRecord.setPRIMARY_DIAG_ICD(val);
    }

    @Override
    public void setEPISODE_TIMING(String val) {
        homeHealthRecord.setEPISODE_TIMING(val);
    }

    @Override
    public void setASSMT_REASON(String val) {
        homeHealthRecord.setASSMT_REASON(val);
    }

    @Override
    public void setINFO_COMPLETED_DT(Calendar val) {
        homeHealthRecord.setINFO_COMPLETED_DT(val);
    }

    @Override
    public void setSTART_CARE_DT(Calendar val) {
        homeHealthRecord.setSTART_CARE_DT(val);
    }

    @Override
    public void setBlank() {
        homeHealthRecord.setBlank();
    }

    @Override
    public String getHIPPS_CODE() {
        return homeHealthRecord.getHIPPS_CODE();
    }

    @Override
    public boolean isDiagnosticGroupOnRecord(int groupNum, int codePosition) {
        return homeHealthRecord.isDiagnosticGroupOnRecord(groupNum, codePosition);
    }

    @Override
    public DiagnosisCodeIF getOptionalDiagnosisCode4(int index) {
        return homeHealthRecord.getOptionalDiagnosisCode4(index);
    }

    @Override
    public DiagnosisCodeIF getOptionalDiagnosisCode3(int index) {
        return homeHealthRecord.getOptionalDiagnosisCode3(index);
    }

    @Override
    public int getTHER_NEED_NBR() {
        return homeHealthRecord.getTHER_NEED_NBR();
    }

    @Override
    public String getTHER_NEED_NA() {
        return homeHealthRecord.getTHER_NEED_NA();
    }

    @Override
    public String getCRNT_MGMT_INJCTN_MDCTN() {
        return homeHealthRecord.getCRNT_MGMT_INJCTN_MDCTN();
    }

    @Override
    public String getCRNT_AMBLTN() {
        return homeHealthRecord.getCRNT_AMBLTN();
    }

    @Override
    public String getCRNT_TRNSFRNG() {
        return homeHealthRecord.getCRNT_TRNSFRNG();
    }

    @Override
    public String getCRNT_TOILTG() {
        return homeHealthRecord.getCRNT_TOILTG();
    }

    @Override
    public String getCRNT_BATHG() {
        return homeHealthRecord.getCRNT_BATHG();
    }

    @Override
    public String getCRNT_DRESS_LOWER() {
        return homeHealthRecord.getCRNT_DRESS_LOWER();
    }

    @Override
    public String getCRNT_DRESS_UPPER() {
        return homeHealthRecord.getCRNT_DRESS_UPPER();
    }

    @Override
    public String getOSTOMY() {
        return homeHealthRecord.getOSTOMY();
    }

    @Override
    public String getBWL_INCONT() {
        return homeHealthRecord.getBWL_INCONT();
    }

    @Override
    public String getUR_INCONT() {
        return homeHealthRecord.getUR_INCONT();
    }

    @Override
    public String getWHEN_DYSPNEIC() {
        return homeHealthRecord.getWHEN_DYSPNEIC();
    }

    @Override
    public String getSTUS_PRBLM_SRGCL_WND() {
        return homeHealthRecord.getSTUS_PRBLM_SRGCL_WND();
    }

    @Override
    public String getSRGCL_WND_PRSNT() {
        return homeHealthRecord.getSRGCL_WND_PRSNT();
    }

    @Override
    public String getSTUS_PRBLM_STAS_ULCR() {
        return homeHealthRecord.getSTUS_PRBLM_STAS_ULCR();
    }

    @Override
    public String getNBR_STAS_ULCR() {
        return homeHealthRecord.getNBR_STAS_ULCR();
    }

    @Override
    public String getSTAS_ULCR_PRSNT() {
        return homeHealthRecord.getSTAS_ULCR_PRSNT();
    }

    @Override
    public String getSTG_PRBLM_ULCER() {
        return homeHealthRecord.getSTG_PRBLM_ULCER();
    }

    @Override
    public String getNBR_PRSULC_STG4() {
        return homeHealthRecord.getNBR_PRSULC_STG4();
    }

    @Override
    public String getNBR_PRSULC_STG3() {
        return homeHealthRecord.getNBR_PRSULC_STG3();
    }

    @Override
    public String getNBR_PRSULC_STG2() {
        return homeHealthRecord.getNBR_PRSULC_STG2();
    }

    @Override
    public String getNBR_PRSULC_STG1() {
        return homeHealthRecord.getNBR_PRSULC_STG1();
    }

    @Override
    public String getLESION_OPEN_WND() {
        return homeHealthRecord.getLESION_OPEN_WND();
    }

    @Override
    public String getPAIN_FREQ_ACTVTY_MVMT() {
        return homeHealthRecord.getPAIN_FREQ_ACTVTY_MVMT();
    }

    @Override
    public String getVISION() {
        return homeHealthRecord.getVISION();
    }

    @Override
    public String getTHH_PAR_NUTRITION() {
        return homeHealthRecord.getTHH_PAR_NUTRITION();
    }

    @Override
    public String getTHH_NONE_ABOVE() {
        return homeHealthRecord.getTHH_NONE_ABOVE();
    }

    @Override
    public String getTHH_IV_INFUSION() {
        return homeHealthRecord.getTHH_IV_INFUSION();
    }

    @Override
    public String getTHH_ENT_NUTRITION() {
        return homeHealthRecord.getTHH_ENT_NUTRITION();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_F4() {
        return homeHealthRecord.getPMT_DIAG_ICD_F4();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_F3() {
        return homeHealthRecord.getPMT_DIAG_ICD_F3();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_E4() {
        return homeHealthRecord.getPMT_DIAG_ICD_E4();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_E3() {
        return homeHealthRecord.getPMT_DIAG_ICD_E3();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_D4() {
        return homeHealthRecord.getPMT_DIAG_ICD_D4();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_D3() {
        return homeHealthRecord.getPMT_DIAG_ICD_D3();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_C4() {
        return homeHealthRecord.getPMT_DIAG_ICD_C4();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_C3() {
        return homeHealthRecord.getPMT_DIAG_ICD_C3();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_B4() {
        return homeHealthRecord.getPMT_DIAG_ICD_B4();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_B3() {
        return homeHealthRecord.getPMT_DIAG_ICD_B3();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_A4() {
        return homeHealthRecord.getPMT_DIAG_ICD_A4();
    }

    @Override
    public DiagnosisCodeIF getPMT_DIAG_ICD_A3() {
        return homeHealthRecord.getPMT_DIAG_ICD_A3();
    }

    @Override
    public DiagnosisCodeIF getOTH_DIAG5_ICD() {
        return homeHealthRecord.getOTH_DIAG5_ICD();
    }

    @Override
    public DiagnosisCodeIF getOTH_DIAG4_ICD() {
        return homeHealthRecord.getOTH_DIAG4_ICD();
    }

    @Override
    public DiagnosisCodeIF getOTH_DIAG3_ICD() {
        return homeHealthRecord.getOTH_DIAG3_ICD();
    }

    @Override
    public DiagnosisCodeIF getOTH_DIAG2_ICD() {
        return homeHealthRecord.getOTH_DIAG2_ICD();
    }

    @Override
    public DiagnosisCodeIF getOTH_DIAG1_ICD() {
        return homeHealthRecord.getOTH_DIAG1_ICD();
    }

    @Override
    public DiagnosisCodeIF getPRIMARY_DIAG_ICD() {
        return homeHealthRecord.getPRIMARY_DIAG_ICD();
    }

    @Override
    public String getEPISODE_TIMING() {
        return homeHealthRecord.getEPISODE_TIMING();
    }

    @Override
    public String getASSMT_REASON() {
        return homeHealthRecord.getASSMT_REASON();
    }

    @Override
    public Calendar getINFO_COMPLETED_DT() {
        return homeHealthRecord.getINFO_COMPLETED_DT();
    }

    @Override
    public Calendar getSTART_CARE_DT() {
        return homeHealthRecord.getSTART_CARE_DT();
    }

    @Override
    public DiagnosisCodeIF getDiagnosisCode(int index) {
        return homeHealthRecord.getDiagnosisCode(index);
    }

    @Override
    public String toString() {
        return homeHealthRecord.toString();
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
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
            }

        } catch (NoSuchMethodException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        }

        return value;
    }
}
