/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto.record;



import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.RecordType;
import java.util.Calendar;


/**
 * This represents the data required by the Home Health Grouper in order to
 * effect scoring claim.  The methods within have names similar to the names
 * of the fields represented in the OASIS record.  However, this class does not
 * identify field positions within any persistent storage mechanism. The way
 * the data is stored (text file, DB, XML, spreadsheet, etc) is left to the
 * implementation classes.
 *
 * Aug 2013 - changed the methods names that reflect OASIS-B column names or
 * Abt programming names to reflect OASIS-C column names. Functionally, no 
 * difference, but makes it easier to reference the OASIS-C/C1 column when
 * looking at the method names.
 * 
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface HomeHealthRecordIF {
    
    /**
     * gets the record type represented by this class.
     * 
     * @return non-null RecordType
     */
    RecordType getRecordType();
    
    /**
     * gets the DiagnosisCode by position - 0 thru 17
     * @param index  a zero based index of the Diagnosis code, i.e. 0 thru 17
     * @return one of the diagnosis codes on the record, based on the code's
     * index position in the record
     */
    DiagnosisCodeIF getDiagnosisCode(int index);

    /**
     * gets the Optional Diagnosis Code column 3, by position - 0 thru 5
     * @param index - a zero based index of the Column 3, i.e 0 thru 5
     * @return one of the Column 3 Diagnosis codes on the record
     */
    DiagnosisCodeIF getOptionalDiagnosisCode3(int index);

    /**
     * gets the Optional Diagnosis Code column 4, by position - 0 thru 5
     * @param index - a zero based index of the Column 4, i.e 0 thru 6
     * @return one of the Column 4 Diagnosis codes on the record
     */
    DiagnosisCodeIF getOptionalDiagnosisCode4(int index);

    /**
     * Determines if the supplied diagnostic group is present somewhere on
     * the current record.
     * @param groupNum
     * @param codePosition - the current code position in the record that this
     * method can ignore - i.e. determines if the group is on the record excluding
     * the current code position.  If -1, then it checks all positions.
     * @return true if any diagnosis code on the record is associated with the
     * diagnosis Group number supplied
     */
    boolean isDiagnosticGroupOnRecord(int groupNum, int codePosition);

    /**
     * gets the M0030_START_CARE_DT
     * Format:  YYYYMMDD
     * @return
     */
    Calendar getSTART_CARE_DT();

    /**
     * sets the M0030_START_CARE_DT
     * Format:     YYYYMMDD
     * @param val
     */
    void setSTART_CARE_DT(Calendar val);

    /**
     * Gets the HIPPS code stored in the 
     * 
     * @return
     */
    String getHIPPS_CODE();

    void setHIPPS_CODE(String hipps);

    /**
     * gets the M0090_INFO_COMPLETED_DT
     * Format:     YYYYMMDD
     * @return
     */
    Calendar getINFO_COMPLETED_DT();

    /**
     * sets the M0090_INFO_COMPLETED_DT
     *
     * Format:
     YYYYMMDD
     * @param val
     */
    void setINFO_COMPLETED_DT(Calendar val);

    /**
     * gets the M0100_ASSMT_REASON
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getASSMT_REASON();

    /**
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setASSMT_REASON(String val);

    
    /**
     * gets the M0440_LESION_OPEN_WND
     *
     * Format:
     0=No, 1=Yes
     * @return
     */
    String getLESION_OPEN_WND();

    /**
     *
     * Format:
     0=No, 1=Yes
     * @param val
     */
    void setLESION_OPEN_WND(String val);

    
    /**
     * gets the M0230_PRIMARY_DIAG_ICD
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPRIMARY_DIAG_ICD();

    /**
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPRIMARY_DIAG_ICD(DiagnosisCodeIF val);

    /**
     * gets the M0240_OTH_DIAG1_ICD
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space or "E".
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getOTH_DIAG1_ICD();

    /**
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space or "E".
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setOTH_DIAG1_ICD(DiagnosisCodeIF val);

    /**
     * gets the M0240_OTH_DIAG2_ICD
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space or "E".
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getOTH_DIAG2_ICD();

    /**
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space or "E".
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setOTH_DIAG2_ICD(DiagnosisCodeIF val);

    /**
     * gets the M0240_OTH_DIAG3_ICD
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space or "E".
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getOTH_DIAG3_ICD();

    /**
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space or "E".
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setOTH_DIAG3_ICD(DiagnosisCodeIF val);

    /**
     * gets the M0240_OTH_DIAG4_ICD
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space or "E".
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getOTH_DIAG4_ICD();

    /**
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space or "E".
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setOTH_DIAG4_ICD(DiagnosisCodeIF val);

    /**
     * gets the M0240_OTH_DIAG5_ICD
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space or "E".
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getOTH_DIAG5_ICD();

    /**
     *
     * Format:
     A nonblank value must conform to one of the following two patterns:
   
   PATTERN A
   -Character 1 must be a space or "E".
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.

   PATTERN B
   -Character 1 must be a space.
   -Character 2 must be "V".
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setOTH_DIAG5_ICD(DiagnosisCodeIF val);

    /**
     * gets the M0250_THH_IV_INFUSION
     *
     * Format:
     0=No/Unchecked, 1=Yes/Checked
     * @return
     */
    String getTHH_IV_INFUSION();

    /**
     *
     * Format:
     0=No/Unchecked, 1=Yes/Checked
     * @param val
     */
    void setTHH_IV_INFUSION(String val);

    /**
     * gets the M0250_THH_PAR_NUTRITION
     *
     * Format:
     0=No/Unchecked, 1=Yes/Checked
     * @return
     */
    String getTHH_PAR_NUTRITION();

    /**
     *
     * Format:
     0=No/Unchecked, 1=Yes/Checked
     * @param val
     */
    void setTHH_PAR_NUTRITION(String val);

    /**
     * gets the M0250_THH_ENT_NUTRITION
     *
     * Format:
     0=No/Unchecked, 1=Yes/Checked
     * @return
     */
    String getTHH_ENT_NUTRITION();

    /**
     * Format:
     0=No/Unchecked, 1=Yes/Checked
     * @param val
     */
    void setTHH_ENT_NUTRITION(String val);

    /**
     * gets the M0250_THH_NONE_ABOVE
     *
     * Format:
     0=No/Unchecked, 1=Yes/Checked
     * @return
     */
    String getTHH_NONE_ABOVE();

    /**
     *
     * Format:
     0=No/Unchecked, 1=Yes/Checked
     * @param val
     */
    void setTHH_NONE_ABOVE(String val);

    /**
     * gets the M0390_VISION
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getVISION();

    /**
     *
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setVISION(String val);

    /**
     * gets the M0420_FREQ_PAIN
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getPAIN_FREQ_ACTVTY_MVMT();

    /**
     *
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setPAIN_FREQ_ACTVTY_MVMT(String val);

    /**
     * gets the M0450_NBR_PRSULC_STG1
     *
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @return
     */
    String getNBR_PRSULC_STG1();

    /**
     *
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @param val
     */
    void setNBR_PRSULC_STG1(String val);

    /**
     * gets the M0450_NBR_PRSULC_STG2
     *
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @return
     */
    String getNBR_PRSULC_STG2();

    /**
     *
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @param val
     */
    void setNBR_PRSULC_STG2(String val);

    /**
     * gets the M0450_NBR_PRSULC_STG3
     *
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @return
     */
    String getNBR_PRSULC_STG3();

    /**
     *
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @param val
     */
    void setNBR_PRSULC_STG3(String val);

    /**
     * gets the M0450_NBR_PRSULC_STG4
     *
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @return
     */
    String getNBR_PRSULC_STG4();

    /**
     *
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @param val
     */
    void setNBR_PRSULC_STG4(String val);

    /**
     * gets the M0460_STG_PRBLM_ULCER
     *
     * Format:
     Right justified; pad left with zero.  Any letters must be upper case.
     * @return
     */
    String getSTG_PRBLM_ULCER();

    /**
     *
     * Format:
     Right justified; pad left with zero.  Any letters must be upper case.
     * @param val
     */
    void setSTG_PRBLM_ULCER(String val);

    /**
     * gets the M0468_STASIS_ULCER
     *
     * Format:
     0=No, 1=Yes
     * @return
     */
    String getSTAS_ULCR_PRSNT();

    /**
     *
     * Format:
     0=No, 1=Yes
     * @param val
     */
    void setSTAS_ULCR_PRSNT(String val);

    /**
     * gets the M0470_NBR_STASULC
	 * January 2014 - Changed from "NUM" to "NBR"
     *
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @return
     */
    String getNBR_STAS_ULCR();
    
    /**
	 * January 2014 - Changed from "NUM" to "NBR"
	 * 
     * Format:
     00=Zero, 01=One, 02=Two, 03=Three, 04=Four or more
     * @param val
     */
    void setNBR_STAS_ULCR(String val);

        
    /**
     * gets the M0476_STAT_PRB_STASULC
     *
     * Format:
     Right justified; pad left with zero.  Any letters must be upper case.
     * @return
     */
    String getSTUS_PRBLM_STAS_ULCR();

    /**
     * Format:
     Right justified; pad left with zero.  Any letters must be upper case.
     * @param val
     */
    void setSTUS_PRBLM_STAS_ULCR(String val);

    /**
     * gets the M0482_SURG_WOUND
     *
     * Format:
     0=No, 1=Yes
     * @return
     */
    String getSRGCL_WND_PRSNT();

    /**
     * Format:
     0=No, 1=Yes
     * @param val
     */
    void setSRGCL_WND_PRSNT(String val);

    /**
     * gets the M0488_STAT_PRB_SURGWND
     *
     * Format:
     Right justified; pad left with zero.  Any letters must be upper case.
     * @return
     */
    String getSTUS_PRBLM_SRGCL_WND();

    /**
     * Format:
     Right justified; pad left with zero.  Any letters must be upper case.
     * @param val
     */
    void setSTUS_PRBLM_SRGCL_WND(String val);

    /**
     * gets the M0490_WHEN_DYSPNEIC
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getWHEN_DYSPNEIC();

    /**
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setWHEN_DYSPNEIC(String val);

    /**
     * gets the M0520_UR_INCONT
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getUR_INCONT();

    /**
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setUR_INCONT(String val);

    /**
     * gets the M0540_BWL_INCONT
     *
     * Format:
     Right justified; pad left with zero.  Any letters must be upper case.
     * @return
     */
    String getBWL_INCONT();

    /**
     * Format:
     Right justified; pad left with zero.  Any letters must be upper case.
     * @param val
     */
    void setBWL_INCONT(String val);

    /**
     * gets the M0550_OSTOMY
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getOSTOMY();

    /**
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setOSTOMY(String val);

    /**
     * gets the M0650_CUR_DRESS_UPPER
	 * January 2014 - Changed from "CUR" to "CRNT"
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getCRNT_DRESS_UPPER();

	/**
	 * January 2014 - Changed from "CUR" to "CRNT"
	 * 
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setCRNT_DRESS_UPPER(String val);

    /**
     * gets the M0660_CUR_DRESS_LOWER
	 * January 2014 - Changed from "CUR" to "CRNT"
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getCRNT_DRESS_LOWER();

    /**
	 * January 2014 - Changed from "CUR" to "CRNT"
	 * 
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setCRNT_DRESS_LOWER(String val);

    /**
     * gets the M0670_CUR_BATHING
	 * January 2014 - Changed from "CUR" to "CRNT"
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getCRNT_BATHG();

    /**
	 * January 2014 - Changed from "CUR" to "CRNT"
	 * 
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setCRNT_BATHG(String val);

    /**
     * gets the M0680_CUR_TOILETING
	 * January 2014 - Changed from "CUR" to "CRNT"
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getCRNT_TOILTG();

    /**
	 * January 2014 - Changed from "CUR" to "CRNT"
	 * 
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setCRNT_TOILTG(String val);

    /**
     * gets the M0690_CUR_TRANSFERRING
	 * January 2014 - Changed from "CUR" to "CRNT"
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getCRNT_TRNSFRNG();

    /**
	 * January 2014 - Changed from "CUR" to "CRNT"
	 * 
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setCRNT_TRNSFRNG(String val);

    /**
     * gets the M0700_CUR_AMBULATION
	 * January 2014 - Changed from "CUR" to "CRNT"
     *
     * Format:
     Right justified; pad left with zero.
     * @return
     */
    String getCRNT_AMBLTN();

    /**
     * Format:
     Right justified; pad left with zero.
     * @param val
     */
    void setCRNT_AMBLTN(String val);

    /**
     * gets the M0800_CUR_INJECT_MEDS
	 * January 2014 - Changed from "CUR" to "CRNT"
     *
     * Format:
     Right justified; pad left with zero.  Any letters must be upper case.
     * @return
     */
    String getCRNT_MGMT_INJCTN_MDCTN();

    /**
     * Format:
     Right justified; pad left with zero.  Any letters must be upper case.
     * @param val
     */
    void setCRNT_MGMT_INJCTN_MDCTN(String val);

    /**
     * gets the M0110_EPISODE_TIMING
     *
     * Format:
     Right justified; pad left with zero. Any letters must be upper case.
     * @return
     */
    String getEPISODE_TIMING();

    /**
     * Format:
     Right justified; pad left with zero. Any letters must be upper case.
     * @param val
     */
    void setEPISODE_TIMING(String val);

    /**
     * gets the M0246_PMT_DIAG_ICD_A3
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_A3();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_A3(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_B3
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_B3();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_B3(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_C3
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_C3();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_C3(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_D3
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_D3();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_D3(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_E3
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_E3();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_E3(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_F3
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_F3();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_F3(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_A4
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_A4();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_A4(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_B4
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_B4();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_B4(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_C4
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_C4();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_C4(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_D4
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_D4();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_D4(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_E4
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_E4();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_E4(DiagnosisCodeIF val);

    /**
     * gets the M0246_PMT_DIAG_ICD_F4
     *
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @return
     */
    DiagnosisCodeIF getPMT_DIAG_ICD_F4();

    /**
     * Format:
     A nonblank value must conform as follows:
   -Character 1 must be a space.
   -Character 2 must be a 0 (zero) thru 9.
   -Characters 3 thru 4 must be 0 thru 9.
   -Character 5 must be a decimal point.
   -Characters 6 and 7 must be 0 (zero) thru 9 or space.
   -If character 6 is a space, then character 7 must be a space.
     * @param val
     */
    void setPMT_DIAG_ICD_F4(DiagnosisCodeIF val);

    /**
     * gets the M0826_THER_NEED_NUM
	 * January 2014 - Changed from "NUM" to "NBR"
     *
     * Format:
     Field must be blank or contain a number which is right-justified and zero-filled.
     * @return
     */
    int getTHER_NEED_NBR();

    /**
	 * January 2014 - Changed from "NUM" to "NBR"
	 * 
     * Format:
     Field must be blank or contain a number which is right-justified and zero-filled.
     * @param val
     */
    void setTHER_NEED_NBR(int val);

    /**
     * gets the M0826_THER_NEED_NA
     *
     * Format:
     0=No/Unchecked, 1=Yes/Checked
     * @return
     */
    String getTHER_NEED_NA();

    /**
     * Format:
     0=No/Unchecked, 1=Yes/Checked
     * @param val
     */
    void setTHER_NEED_NA(String val);

    /**
     * Sets the record's fields to their default values
     */
    void setBlank();

    /**
     * Gets the value of a field by name.  If the field is unknown, then
     * the value is null
     * @param fieldName
     * @return
     */
    String getFieldValue(String fieldName);
    
}

