/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.io.util;

import com.mmm.cms.homehealth.io.AbstractRecordConverter;
import com.mmm.cms.homehealth.io.HomeHealthRecordUtil;
import com.mmm.cms.homehealth.io.OasisRecordConverterIF;
import com.mmm.cms.homehealth.io.record.Oasis_C1_Record_2_11;
import static com.mmm.cms.homehealth.io.HomeHealthRecordUtil.formatDxCode_C1;
import static com.mmm.cms.homehealth.io.HomeHealthRecordUtil.formatTherapyNeedNum;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.util.OasisCalendarFormatter;
import java.text.ParseException;

/**
 * Converts a flat file record to Oasis_C1_Record
 * @author 3M Health Information Systems for CMS Home Health
 */
public class Oasis_C1_RecordUtil_v2_11 
        extends AbstractRecordConverter
        implements OasisRecordConverterIF {

    public Oasis_C1_RecordUtil_v2_11() {
        this("20150101", "20150930");
    }

    public Oasis_C1_RecordUtil_v2_11(String startDate, String endDate) {
        super(startDate, endDate, 3256, "2.11");
    }
    
    /**
     * The M0090 date is at location: 435-442, inclusive
     * 
     * @param record
     * @return 
     */
    @Override
    protected String getRecordDate(String record) {
        return record.substring(434, 442);
    }

    /**
     * The version CD is at location: 25-34, inclusive
     * @param record
     * @return 
     */
    @Override
    protected String getVersionCD(String record) {
        return record.substring(24, 34);
    }

    @Override
    public StringBuilder convertFromHomeHealthRec(HomeHealthRecordIF homeHealthRecord) {
        return convertFromHomeHealthToFlatRecord((HomeHealthRecord_C_IF) homeHealthRecord);
    }

    @Override
    public StringBuilder convertFromHomeHealthRecDelimeted(HomeHealthRecordIF homeHealthRecord, String delimiter) {
        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum) throws ParseException {
        return convertToHomeHealthRec(strRecord, recNum, true);
    }

    public HomeHealthRecordIF convertToHomeHealthRec(
            String M0030_START_CARE_DT,
            String M0090_INFO_COMPLETED_DT,
            String M0100_ASSMT_REASON,
            String M0110_EPISODE_TIMING,
            String M1030_THH_IV_INFUSION,
            String M1030_THH_PAR_NUTRITION,
            String M1030_THH_ENT_NUTRITION,
            String M1030_THH_NONE_ABOVE,
            String M1200_VISION,
            String M1242_PAIN_FREQ_ACTVTY_MVMT,
            String M1306_UNHLD_STG2_PRSR_ULCR,
            String M1308_NBR_PRSULC_STG2,
            String M1308_NBR_PRSULC_STG3,
            String M1308_NBR_PRSULC_STG4,
            String M1308_NSTG_DRSG,
            String M1308_NSTG_CVRG,
            String M1308_NSTG_DEEP_TISUE,
            String M1320_STUS_PRBLM_PRSR_ULCR,
            String M1322_NBR_PRSULC_STG1,
            String M1324_STG_PRBLM_ULCER,
            String M1330_STAS_ULCR_PRSNT,
            String M1332_NBR_STAS_ULCR,
            String M1334_STUS_PRBLM_STAS_ULCR,
            String M1340_SRGCL_WND_PRSNT,
            String M1342_STUS_PRBLM_SRGCL_WND,
            String M1350_LESION_OPEN_WND,
            String M1400_WHEN_DYSPNEIC,
            String M1610_UR_INCONT,
            String M1615_INCNTNT_TIMING,
            String M1620_BWL_INCONT,
            String M1630_OSTOMY,
            String M1810_CRNT_DRESS_UPPER,
            String M1820_CRNT_DRESS_LOWER,
            String M1830_CRNT_BATHG,
            String M1840_CRNT_TOILTG,
            String M1850_CRNT_TRNSFRNG,
            String M1860_CRNT_AMBLTN,
            String M2000_DRUG_RGMN_RVW,
            String M2030_CRNT_MGMT_INJCTN_MDCTN,
            String M2200_THER_NEED_NBR,
            String M2200_THER_NEED_NA,
            String M1021_PRIMARY_DIAG_ICD,
            String M1023_OTH_DIAG1_ICD,
            String M1023_OTH_DIAG2_ICD,
            String M1023_OTH_DIAG3_ICD,
            String M1023_OTH_DIAG4_ICD,
            String M1023_OTH_DIAG5_ICD,
            String M1025_PMT_DIAG_ICD_A3,
            String M1025_PMT_DIAG_ICD_A4,
            String M1025_PMT_DIAG_ICD_B3,
            String M1025_PMT_DIAG_ICD_B4,
            String M1025_PMT_DIAG_ICD_C3,
            String M1025_PMT_DIAG_ICD_C4,
            String M1025_PMT_DIAG_ICD_D3,
            String M1025_PMT_DIAG_ICD_D4,
            String M1025_PMT_DIAG_ICD_E3,
            String M1025_PMT_DIAG_ICD_E4,
            String M1025_PMT_DIAG_ICD_F3,
            String M1025_PMT_DIAG_ICD_F4) throws ParseException {
        final Oasis_C1_Record_2_11 oasisRecord = new Oasis_C1_Record_2_11();

        oasisRecord.setSTART_CARE_DT(OasisCalendarFormatter.parse(M0030_START_CARE_DT));
        oasisRecord.setINFO_COMPLETED_DT(OasisCalendarFormatter.parse(M0090_INFO_COMPLETED_DT));
        oasisRecord.setASSMT_REASON(M0100_ASSMT_REASON);
        oasisRecord.setEPISODE_TIMING(M0110_EPISODE_TIMING);
        oasisRecord.setTHH_IV_INFUSION(M1030_THH_IV_INFUSION);
        oasisRecord.setTHH_PAR_NUTRITION(M1030_THH_PAR_NUTRITION);
        oasisRecord.setTHH_ENT_NUTRITION(M1030_THH_ENT_NUTRITION);
        oasisRecord.setTHH_NONE_ABOVE(M1030_THH_NONE_ABOVE);
        oasisRecord.setVISION(M1200_VISION);
        oasisRecord.setPAIN_FREQ_ACTVTY_MVMT(M1242_PAIN_FREQ_ACTVTY_MVMT);
        oasisRecord.setUNHLD_STG2_PRSR_ULCR(M1306_UNHLD_STG2_PRSR_ULCR);
        oasisRecord.setNBR_PRSULC_STG2(M1308_NBR_PRSULC_STG2);
        oasisRecord.setNBR_PRSULC_STG3(M1308_NBR_PRSULC_STG3);
        oasisRecord.setNBR_PRSULC_STG4(M1308_NBR_PRSULC_STG4);
        oasisRecord.setNSTG_DRSG(M1308_NSTG_DRSG);
        oasisRecord.setNSTG_CVRG(M1308_NSTG_CVRG);
        oasisRecord.setNSTG_DEEP_TISUE(M1308_NSTG_DEEP_TISUE);
        oasisRecord.setSTUS_PRBLM_PRSR_ULCR(M1320_STUS_PRBLM_PRSR_ULCR);
        oasisRecord.setNBR_PRSULC_STG1(M1322_NBR_PRSULC_STG1);
        oasisRecord.setSTG_PRBLM_ULCER(M1324_STG_PRBLM_ULCER);
        oasisRecord.setSTAS_ULCR_PRSNT(M1330_STAS_ULCR_PRSNT);
        oasisRecord.setNBR_STAS_ULCR(M1332_NBR_STAS_ULCR);
        oasisRecord.setSTUS_PRBLM_STAS_ULCR(M1334_STUS_PRBLM_STAS_ULCR);
        oasisRecord.setSRGCL_WND_PRSNT(M1340_SRGCL_WND_PRSNT);
        oasisRecord.setSTUS_PRBLM_SRGCL_WND(M1342_STUS_PRBLM_SRGCL_WND);
        oasisRecord.setLESION_OPEN_WND(M1350_LESION_OPEN_WND);
        oasisRecord.setWHEN_DYSPNEIC(M1400_WHEN_DYSPNEIC);
        oasisRecord.setUR_INCONT(M1610_UR_INCONT);
        oasisRecord.setINCNTNT_TIMING(M1615_INCNTNT_TIMING);
        oasisRecord.setBWL_INCONT(M1620_BWL_INCONT);
        oasisRecord.setOSTOMY(M1630_OSTOMY);
        oasisRecord.setCRNT_DRESS_UPPER(M1810_CRNT_DRESS_UPPER);
        oasisRecord.setCRNT_DRESS_LOWER(M1820_CRNT_DRESS_LOWER);
        oasisRecord.setCRNT_BATHG(M1830_CRNT_BATHG);
        oasisRecord.setCRNT_TOILTG(M1840_CRNT_TOILTG);
        oasisRecord.setCRNT_TRNSFRNG(M1850_CRNT_TRNSFRNG);
        oasisRecord.setCRNT_AMBLTN(M1860_CRNT_AMBLTN);
        oasisRecord.setDRUG_RGMN_RVW(M2000_DRUG_RGMN_RVW);
        oasisRecord.setCRNT_MGMT_INJCTN_MDCTN(M2030_CRNT_MGMT_INJCTN_MDCTN);
        oasisRecord.setTHER_NEED_NBR(HomeHealthRecordUtil.parseTherapyNeedNumber_C1(M2200_THER_NEED_NBR, -1));
        oasisRecord.setTHER_NEED_NA(M2200_THER_NEED_NA);
        oasisRecord.setPRIMARY_DIAG_ICD(HomeHealthRecordUtil.parseDxCode(M1021_PRIMARY_DIAG_ICD));
        oasisRecord.setOTH_DIAG1_ICD(HomeHealthRecordUtil.parseDxCode(M1023_OTH_DIAG1_ICD));
        oasisRecord.setOTH_DIAG2_ICD(HomeHealthRecordUtil.parseDxCode(M1023_OTH_DIAG2_ICD));
        oasisRecord.setOTH_DIAG3_ICD(HomeHealthRecordUtil.parseDxCode(M1023_OTH_DIAG3_ICD));
        oasisRecord.setOTH_DIAG4_ICD(HomeHealthRecordUtil.parseDxCode(M1023_OTH_DIAG4_ICD));
        oasisRecord.setOTH_DIAG5_ICD(HomeHealthRecordUtil.parseDxCode(M1023_OTH_DIAG5_ICD));
        oasisRecord.setPMT_DIAG_ICD_A3(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_A3));
        oasisRecord.setPMT_DIAG_ICD_A4(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_A4));
        oasisRecord.setPMT_DIAG_ICD_B3(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_B3));
        oasisRecord.setPMT_DIAG_ICD_B4(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_B4));
        oasisRecord.setPMT_DIAG_ICD_C3(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_C3));
        oasisRecord.setPMT_DIAG_ICD_C4(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_C4));
        oasisRecord.setPMT_DIAG_ICD_D3(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_D3));
        oasisRecord.setPMT_DIAG_ICD_D4(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_D4));
        oasisRecord.setPMT_DIAG_ICD_E3(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_E3));
        oasisRecord.setPMT_DIAG_ICD_E4(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_E4));
        oasisRecord.setPMT_DIAG_ICD_F3(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_F3));
        oasisRecord.setPMT_DIAG_ICD_F4(HomeHealthRecordUtil.parseDxCode(M1025_PMT_DIAG_ICD_F4));

        return oasisRecord;
    }

    /**
     * GENERATED CODE BELOW
     *
     * @param strRecord - this should be an OASIS-C1 flat file format
     * @param recNum - the current record number being processed
     * @param skipPassthru - no longer used
     *
     * @return HomeHealthRecordIF - this will be extended to an implementation
     * of HomeHealthRecord_C1_IF
     */
    @Override
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum, boolean skipPassthru) throws ParseException {
        Oasis_C1_Record_2_11 oasisRecord = new Oasis_C1_Record_2_11();

        // validate that this is an Body record
        if (strRecord == null) {
            throw new ParseException("OASIS record string can not be null", 0);
        } else if (!isRecordConvertable(strRecord)) {
            if (strRecord.length() < getRecordLength()) {
                throw new ParseException("Unknown record due to invalid length of "
                        + strRecord.length() + ", should be at least " + getRecordLength() + "  characters.", 0);
            } else if (strRecord.charAt(0) != 'B' || strRecord.charAt(1) != '1') {
                throw new ParseException("Unknown record - not OASIS-C record - ID characters: \""
                        + strRecord.substring(0, 2) + "\"", 0);
            } else {
                throw new ParseException("Unknown record due to invalid dates: \""
                        + strRecord.substring(0, 2) + "\"", 0);
            }
        }

		// ASMT_SYS_CD - Assessment system code
        // TRANS_TYPE_CD - Transaction type code
        // ITM_SBST_CD - Item subset code
        // ITM_SET_VRSN_CD - Item set version code
        // SPEC_VRSN_CD - Specifications version code
        // CORRECTION_NUM - Correction number
        // STATE_CD - Agency's state postal code
        // HHA_AGENCY_ID - Assigned agency submission ID
        // NATL_PRVDR_ID - Agency National Provider ID (NPI)
        // SFW_ID - Software vendor federal employer tax ID
        // SFW_NAME - Software vendor company name
        // SFW_EMAIL_ADR - Software vendor email address
        // SFW_PROD_NAME - Software product name
        // SFW_PROD_VRSN_CD - Software product version code
        // ACY_DOC_CD - Document ID code (agency use)
        // SUBM_HIPPS_CODE - HIPPS group code: submitted
        // SUBM_HIPPS_VERSION - HIPPS version code: submitted
        // M0010_CCN - Facility CMS certification number (CCN)
        // M0014_BRANCH_STATE - Branch state
        // M0016_BRANCH_ID - Branch ID
        // M0018_PHYSICIAN_ID - Attending physician National Provider ID (NPI)
        // M0018_PHYSICIAN_UK - Attending physician NPI: Unknown
        // M0020_PAT_ID - Patient ID number
        oasisRecord.setSTART_CARE_DT(OasisCalendarFormatter.parse(strRecord.substring(302, 310)));   // M0030_START_CARE_DT - Start of care date
        // M0032_ROC_DT - Resumption of care date
        // M0032_ROC_DT_NA - No resumption of care date
        // M0040_PAT_FNAME - Patient's first name
        // M0040_PAT_MI - Patient's middle initial
        // M0040_PAT_LNAME - Patient's last name
        // M0040_PAT_SUFFIX - Patient's suffix
        // M0050_PAT_ST - Patient state of residence
        // M0060_PAT_ZIP - Patient zip code
        // M0063_MEDICARE_NUM - Medicare number, including suffix
        // M0063_MEDICARE_NA - No Medicare number
        // M0064_SSN - Patient's Social Security number
        // M0064_SSN_UK - No Social Security number
        // M0065_MEDICAID_NUM - Medicaid number
        // M0065_MEDICAID_NA - No Medicaid number
        // M0066_PAT_BIRTH_DT - Date of birth
        // M0069_PAT_GENDER - Gender
        // M0140_ETHNIC_AI_AN - Ethnicity: American Indian or Alaska Native
        // M0140_ETHNIC_ASIAN - Ethnicity: Asian
        // M0140_ETHNIC_BLACK - Ethnicity: Black or African American
        // M0140_ETHNIC_HISP - Ethnicity: Hispanic or Latino
        // M0140_ETHNIC_NH_PI - Ethnicity: Native Hawaiian/Pacific Islander
        // M0140_ETHNIC_WHITE - Ethnicity: White
        // M0150_CPAY_NONE - Payment sources: no charge for current services
        // M0150_CPAY_MCARE_FFS - Payment sources: Medicare fee-for-service
        // M0150_CPAY_MCARE_HMO - Payment sources: Medicare HMO/managed care
        // M0150_CPAY_MCAID_FFS - Payment sources: Medicaid fee-for-service
        // M0150_CPAY_MCAID_HMO - Payment sources: Medicaid HMO/managed care
        // M0150_CPAY_WRKCOMP - Payment sources: worker's compensation
        // M0150_CPAY_TITLEPGMS - Payment sources: title programs
        // M0150_CPAY_OTH_GOVT - Payment sources: other government
        // M0150_CPAY_PRIV_INS - Payment sources: private insurance
        // M0150_CPAY_PRIV_HMO - Payment sources: private HMO/managed care
        // M0150_CPAY_SELFPAY - Payment sources: self-pay
        // M0150_CPAY_OTHER - Payment sources: other
        // M0150_CPAY_UK - Payment sources: unknown
        // M0080_ASSESSOR_DISCIPLINE - Discipline of person completing assessment
        oasisRecord.setINFO_COMPLETED_DT(OasisCalendarFormatter.parse(strRecord.substring(434, 442)));   // M0090_INFO_COMPLETED_DT - Date assessment completed
        oasisRecord.setASSMT_REASON(strRecord.substring(442, 444).intern());   // M0100_ASSMT_REASON - Reason for assessment
        // M0102_PHYSN_ORDRD_SOCROC_DT - Physician ordered SOC/ROC date
        // M0102_PHYSN_ORDRD_SOCROC_DT_NA - Physician ordered SOC/ROC date - NA
        // M0104_PHYSN_RFRL_DT - Physician date of referral
        oasisRecord.setEPISODE_TIMING(strRecord.substring(461, 463).intern());   // M0110_EPISODE_TIMING - Episode timing
        // M1000_DC_LTC_14_DA - Past 14 days: disch from LTC NH
        // M1000_DC_SNF_14_DA - Past 14 days: disch from skilled nursing facility
        // M1000_DC_IPPS_14_DA - Past 14 days: disch from short stay acute hospital
        // M1000_DC_LTCH_14_DA - Past 14 days: disch from long term care hospital
        // M1000_DC_IRF_14_DA - Past 14 days: disch from inpatient rehab facility
        // M1000_DC_PSYCH_14_DA - Past 14 days: disch from psych hospital or unit
        // M1000_DC_OTH_14_DA - Past 14 days: disch from other
        // M1000_DC_NONE_14_DA - Past 14 days: not disch from inpatient facility
        // M1005_INP_DISCHARGE_DT - Most recent inpatient discharge date
        // M1005_INP_DSCHG_UNKNOWN - Inpatient discharge date unknown
        // ITEM_FILLER_001 - Filler: replaces M1010_14_DAY_INP1_ICD
        // ITEM_FILLER_002 - Filler: replaces M1010_14_DAY_INP2_ICD
        // ITEM_FILLER_003 - Filler: replaces M1010_14_DAY_INP3_ICD
        // ITEM_FILLER_004 - Filler: replaces M1010_14_DAY_INP4_ICD
        // ITEM_FILLER_005 - Filler: replaces M1010_14_DAY_INP5_ICD
        // ITEM_FILLER_006 - Filler: replaces M1010_14_DAY_INP6_ICD
        // ITEM_FILLER_007 - Filler: replaces M1012_INP_PRCDR1_ICD
        // ITEM_FILLER_008 - Filler: replaces M1012_INP_PRCDR2_ICD
        // ITEM_FILLER_009 - Filler: replaces M1012_INP_PRCDR3_ICD
        // ITEM_FILLER_010 - Filler: replaces M1012_INP_PRCDR4_ICD
        // ITEM_FILLER_011 - Filler: replaces M1012_INP_NA_ICD
        // ITEM_FILLER_012 - Filler: replaces M1012_INP_UK_ICD
        // ITEM_FILLER_013 - Filler: replaces M1016_CHGREG_ICD1
        // ITEM_FILLER_014 - Filler: replaces M1016_CHGREG_ICD2
        // ITEM_FILLER_015 - Filler: replaces M1016_CHGREG_ICD3
        // ITEM_FILLER_016 - Filler: replaces M1016_CHGREG_ICD4
        // ITEM_FILLER_017 - Filler: replaces M1016_CHGREG_ICD5
        // ITEM_FILLER_018 - Filler: replaces M1016_CHGREG_ICD6
        // ITEM_FILLER_019 - Filler: replaces M1016_CHGREG_ICD_NA
        // M1018_PRIOR_UR_INCON - Prior condition: urinary incontinence
        // M1018_PRIOR_CATH - Prior condition: indwelling/suprapubic catheter
        // M1018_PRIOR_INTRACT_PAIN - Prior condition: intractable pain
        // M1018_PRIOR_IMPR_DECSN - Prior condition: impaired decision-making
        // M1018_PRIOR_DISRUPTIVE - Prior condition: disruptive/inappropriate behav
        // M1018_PRIOR_MEM_LOSS - Prior condition: memory loss, supervision required
        // M1018_PRIOR_NONE - Prior condition: none of the above
        // M1018_PRIOR_NOCHG_14D - Prior condition: no inpt disch/no change regimen
        // M1018_PRIOR_UNKNOWN - Prior condition: unknown
        oasisRecord.setPRIMARY_DIAG_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(604, 611)));   // M1020_PRIMARY_DIAG_ICD - Primary diagnosis ICD code
        // M1020_PRIMARY_DIAG_SEVERITY - Primary diagnosis severity rating
        oasisRecord.setOTH_DIAG1_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(613, 620)));   // M1022_OTH_DIAG1_ICD - Other diagnosis 1: ICD code
        // M1022_OTH_DIAG1_SEVERITY - Other diagnosis 1: severity rating
        oasisRecord.setOTH_DIAG2_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(622, 629)));   // M1022_OTH_DIAG2_ICD - Other diagnosis 2: ICD code
        // M1022_OTH_DIAG2_SEVERITY - Other diagnosis 2: severity rating
        oasisRecord.setOTH_DIAG3_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(631, 638)));   // M1022_OTH_DIAG3_ICD - Other diagnosis 3: ICD code
        // M1022_OTH_DIAG3_SEVERITY - Other diagnosis 3: severity rating
        oasisRecord.setOTH_DIAG4_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(640, 647)));   // M1022_OTH_DIAG4_ICD - Other diagnosis 4: ICD code
        // M1022_OTH_DIAG4_SEVERITY - Other diagnosis 4: severity rating
        oasisRecord.setOTH_DIAG5_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(649, 656)));   // M1022_OTH_DIAG5_ICD - Other diagnosis 5: ICD code
        // M1022_OTH_DIAG5_SEVERITY - Other diagnosis 5: severity rating
        oasisRecord.setPMT_DIAG_ICD_A3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(658, 665)));   // M1024_PMT_DIAG_ICD_A3 - Case mix diagnosis: primary, column 3
        oasisRecord.setPMT_DIAG_ICD_B3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(665, 672)));   // M1024_PMT_DIAG_ICD_B3 - Case mix diagnosis: first secondary, column 3
        oasisRecord.setPMT_DIAG_ICD_C3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(672, 679)));   // M1024_PMT_DIAG_ICD_C3 - Case mix diagnosis: second secondary, column 3
        oasisRecord.setPMT_DIAG_ICD_D3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(679, 686)));   // M1024_PMT_DIAG_ICD_D3 - Case mix diagnosis: third secondary, column 3
        oasisRecord.setPMT_DIAG_ICD_E3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(686, 693)));   // M1024_PMT_DIAG_ICD_E3 - Case mix diagnosis: fourth secondary, column 3
        oasisRecord.setPMT_DIAG_ICD_F3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(693, 700)));   // M1024_PMT_DIAG_ICD_F3 - Case mix diagnosis: fifth secondary, column 3
        oasisRecord.setPMT_DIAG_ICD_A4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(700, 707)));   // M1024_PMT_DIAG_ICD_A4 - Case mix diagnosis: primary, column 4
        oasisRecord.setPMT_DIAG_ICD_B4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(707, 714)));   // M1024_PMT_DIAG_ICD_B4 - Case mix diagnosis: first secondary, column 4
        oasisRecord.setPMT_DIAG_ICD_C4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(714, 721)));   // M1024_PMT_DIAG_ICD_C4 - Case mix diagnosis: second secondary, column 4
        oasisRecord.setPMT_DIAG_ICD_D4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(721, 728)));   // M1024_PMT_DIAG_ICD_D4 - Case mix diagnosis: third secondary, column 4
        oasisRecord.setPMT_DIAG_ICD_E4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(728, 735)));   // M1024_PMT_DIAG_ICD_E4 - Case mix diagnosis: fourth secondary, column 4
        oasisRecord.setPMT_DIAG_ICD_F4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(735, 742)));   // M1024_PMT_DIAG_ICD_F4 - Case mix diagnosis: fifth secondary, column 4
        oasisRecord.setTHH_IV_INFUSION(strRecord.substring(742, 743).intern());   // M1030_THH_IV_INFUSION - Therapies received at home: intravenous, infusion
        oasisRecord.setTHH_PAR_NUTRITION(strRecord.substring(743, 744).intern());   // M1030_THH_PAR_NUTRITION - Therapies received at home: parenteral nutrition
        oasisRecord.setTHH_ENT_NUTRITION(strRecord.substring(744, 745).intern());   // M1030_THH_ENT_NUTRITION - Therapies received at home: enteral nutrition
        oasisRecord.setTHH_NONE_ABOVE(strRecord.substring(745, 746).intern());   // M1030_THH_NONE_ABOVE - Therapies received at home: none of the above
        // ITEM_FILLER_044 - Filler: replaces M1032_HOSP_RISK_RCNT_DCLN
        // ITEM_FILLER_045 - Filler: replaces M1032_HOSP_RISK_MLTPL_HOSPZTN
        // ITEM_FILLER_046 - Filler: replaces M1032_HOSP_RISK_HSTRY_FALLS
        // ITEM_FILLER_047 - Filler: replaces M1032_HOSP_RISK_5PLUS_MDCTN
        // ITEM_FILLER_048 - Filler: replaces M1032_HOSP_RISK_FRAILTY
        // ITEM_FILLER_049 - Filler: replaces M1032_HOSP_RISK_OTHR
        // ITEM_FILLER_050 - Filler: replaces M1032_HOSP_RISK_NONE_ABOVE
        // M1034_PTNT_OVRAL_STUS - Patient's overall status
        // M1036_RSK_SMOKING - High risk factor: smoking
        // M1036_RSK_OBESITY - High risk factor: obesity
        // M1036_RSK_ALCOHOLISM - High risk factor: alcoholism
        // M1036_RSK_DRUGS - High risk factor: drugs
        // M1036_RSK_NONE - High risk factor: none of the above
        // M1036_RSK_UNKNOWN - High risk factor: unknown
        // ITEM_FILLER_051 - Filler: replaces M1040_INFLNZ_RCVD_AGNCY
        // ITEM_FILLER_052 - Filler: replaces M1045_INFLNZ_RSN_NOT_RCVD
        // ITEM_FILLER_053 - Filler: replaces M1050_PPV_RCVD_AGNCY
        // ITEM_FILLER_054 - Filler: replaces M1055_PPV_RSN_NOT_RCVD_AGNCY
        // M1100_PTNT_LVG_STUTN - Patient living situation
        oasisRecord.setVISION(strRecord.substring(770, 772).intern());   // M1200_VISION - Sensory status: vision
        // M1210_HEARG_ABLTY - Ability to hear
        // M1220_UNDRSTG_VERBAL_CNTNT - Understanding verbal content in patient's language
        // M1230_SPEECH - Sensory status: speech
        // M1240_FRML_PAIN_ASMT - Has patient had a formal validated pain assessment
        oasisRecord.setPAIN_FREQ_ACTVTY_MVMT(strRecord.substring(780, 782).intern());   // M1242_PAIN_FREQ_ACTVTY_MVMT - Freq of pain interfering with pt activity/movement
        // M1300_PRSR_ULCR_RISK_ASMT - Was patient assessed for risk of developing pus
        // M1302_RISK_OF_PRSR_ULCR - Does this patient have a risk of developing pus
        oasisRecord.setUNHLD_STG2_PRSR_ULCR(strRecord.substring(785, 786).intern());   // M1306_UNHLD_STG2_PRSR_ULCR - Patient has 1+ unhealed PU at stage 2 or higher
        // M1307_OLDST_STG2_AT_DSCHRG - Status of oldest stage 2 pressure ulcer at disch
        // M1307_OLDST_STG2_ONST_DT - Date of onset of oldest stage 2 pressure ulcer
        oasisRecord.setNBR_PRSULC_STG2(strRecord.substring(796, 798).intern());   // M1308_NBR_PRSULC_STG2 - No. pressure ulcers - stage 2
        // ITEM_FILLER_055 - Filler: replaces M1308_NBR_STG2_AT_SOC_ROC
        oasisRecord.setNBR_PRSULC_STG3(strRecord.substring(800, 802).intern());   // M1308_NBR_PRSULC_STG3 - No. pressure ulcers - stage 3
        // ITEM_FILLER_056 - Filler: replaces M1308_NBR_STG3_AT_SOC_ROC
        oasisRecord.setNBR_PRSULC_STG4(strRecord.substring(804, 806).intern());   // M1308_NBR_PRSULC_STG4 - No. pressure ulcers - stage 4
        // ITEM_FILLER_057 - Filler: replaces M1308_NBR_STG4_AT_SOC_ROC
        oasisRecord.setNSTG_DRSG(strRecord.substring(808, 810).intern());   // M1308_NSTG_DRSG - Unstageable: non-removable dressing/device
        // ITEM_FILLER_058 - Filler: replaces M1308_NSTG_DRSG_SOC_ROC
        oasisRecord.setNSTG_CVRG(strRecord.substring(812, 814).intern());   // M1308_NSTG_CVRG - Unstageable: coverage by slough or eschar
        // ITEM_FILLER_059 - Filler: replaces M1308_NSTG_CVRG_SOC_ROC
        oasisRecord.setNSTG_DEEP_TISUE(strRecord.substring(816, 818).intern());   // M1308_NSTG_DEEP_TISUE - Unstageable: suspect deep tissue injury
        // ITEM_FILLER_060 - Filler: replaces M1308_NSTG_DEEP_TISUE_SOC_ROC
        // ITEM_FILLER_061 - Filler: replaces M1310_PRSR_ULCR_LNGTH
        // ITEM_FILLER_062 - Filler: replaces M1312_PRSR_ULCR_WDTH
        // ITEM_FILLER_063 - Filler: replaces M1314_PRSR_ULCR_DEPTH
        oasisRecord.setSTUS_PRBLM_PRSR_ULCR(strRecord.substring(832, 834).intern());   // M1320_STUS_PRBLM_PRSR_ULCR - Status of most problematic pressure ulcer
        oasisRecord.setNBR_PRSULC_STG1(strRecord.substring(834, 836).intern());   // M1322_NBR_PRSULC_STG1 - No. pressure ulcers - stage 1
        oasisRecord.setSTG_PRBLM_ULCER(strRecord.substring(836, 838).intern());   // M1324_STG_PRBLM_ULCER - Stage of most problematic pressure ulcer
        oasisRecord.setSTAS_ULCR_PRSNT(strRecord.substring(838, 840).intern());   // M1330_STAS_ULCR_PRSNT - Does this patient have a stasis ulcer
        oasisRecord.setNBR_STAS_ULCR(strRecord.substring(840, 842).intern());   // M1332_NBR_STAS_ULCR - No. stasis ulcers
        oasisRecord.setSTUS_PRBLM_STAS_ULCR(strRecord.substring(842, 844).intern());   // M1334_STUS_PRBLM_STAS_ULCR - Status of most problematic stasis ulcer
        oasisRecord.setSRGCL_WND_PRSNT(strRecord.substring(844, 846).intern());   // M1340_SRGCL_WND_PRSNT - Does this patient have a surgical wound
        oasisRecord.setSTUS_PRBLM_SRGCL_WND(strRecord.substring(846, 848).intern());   // M1342_STUS_PRBLM_SRGCL_WND - Status of most problematic surgical wound
        oasisRecord.setLESION_OPEN_WND(strRecord.substring(848, 849).intern());   // M1350_LESION_OPEN_WND - Has skin lesion or open wound
        oasisRecord.setWHEN_DYSPNEIC(strRecord.substring(849, 851).intern());   // M1400_WHEN_DYSPNEIC - When dyspneic
        // M1410_RESPTX_OXYGEN - Respiratory treatments: oxygen
        // M1410_RESPTX_VENTILATOR - Respiratory treatments: ventilator
        // M1410_RESPTX_AIRPRESS - Respiratory treatments: airway pressure
        // M1410_RESPTX_NONE - Respiratory treatments: none of the above
        // M1500_SYMTM_HRT_FAILR_PTNTS - Symptoms in heart failure patients
        // M1510_HRT_FAILR_NO_ACTN - Heart failure follow up:  no action
        // M1510_HRT_FAILR_PHYSN_CNTCT - Heart failure follow up:  physician contacted
        // M1510_HRT_FAILR_ER_TRTMT - Heart failure follow up:  ER treatment advised
        // M1510_HRT_FAILR_PHYSN_TRTMT - Heart failure follow up:  phys-ordered treatmnt
        // M1510_HRT_FAILR_CLNCL_INTRVTN - Heart failure follow up: pt educ/other clinical
        // M1510_HRT_FAILR_CARE_PLAN_CHG - Heart failure follow up: change in care plan
        // M1600_UTI - Treated for urinary tract infection past 14 days
        oasisRecord.setUR_INCONT(strRecord.substring(865, 867).intern());   // M1610_UR_INCONT - Urinary incontinence or urinary catheter present
        oasisRecord.setINCNTNT_TIMING(strRecord.substring(867, 869).intern());   // M1615_INCNTNT_TIMING - When urinary incontinence occurs
        oasisRecord.setBWL_INCONT(strRecord.substring(869, 871).intern());   // M1620_BWL_INCONT - Bowel incontinence frequency
        oasisRecord.setOSTOMY(strRecord.substring(871, 873).intern());   // M1630_OSTOMY - Ostomy for bowel elimination
        // M1700_COG_FUNCTION - Cognitive functioning
        // M1710_WHEN_CONFUSED - When confused (reported or observed)
        // M1720_WHEN_ANXIOUS - When anxious (reported or observed)
        // M1730_STDZ_DPRSN_SCRNG - Screened for depression using validated tool
        // M1730_PHQ2_LACK_INTRST - PHQ2: little interest or pleasure in doing things
        // M1730_PHQ2_DPRSN - PHQ2: feeling down, depressed or hopeless
        // M1740_BD_MEM_DEFICIT - Behavior demonstrated: memory deficit
        // M1740_BD_IMP_DECISN - Behavior demonstrated: impaired decision-making
        // M1740_BD_VERBAL - Behavior demonstrated: verbal disruption
        // M1740_BD_PHYSICAL - Behavior demonstrated: physical aggression
        // M1740_BD_SOC_INAPPRO - Behavior demonstrated: socially inappropriate
        // M1740_BD_DELUSIONS - Behavior demonstrated: delusions
        // M1740_BD_NONE - Behavior demonstrated: none of the above
        // M1745_BEH_PROB_FREQ - Frequency of behavior problems
        // M1750_REC_PSYCH_NURS - Receives psychiatric nursing
        // M1800_CRNT_GROOMING - Current: grooming
        oasisRecord.setCRNT_DRESS_UPPER(strRecord.substring(897, 899).intern());   // M1810_CRNT_DRESS_UPPER - Current: dress upper body
        oasisRecord.setCRNT_DRESS_LOWER(strRecord.substring(899, 901).intern());   // M1820_CRNT_DRESS_LOWER - Current: dress lower body
        oasisRecord.setCRNT_BATHG(strRecord.substring(901, 903).intern());   // M1830_CRNT_BATHG - Current: bathing
        oasisRecord.setCRNT_TOILTG(strRecord.substring(903, 905).intern());   // M1840_CRNT_TOILTG - Current: toileting
        // M1845_CRNT_TOILTG_HYGN - Current: toileting hygiene
        oasisRecord.setCRNT_TRNSFRNG(strRecord.substring(907, 909).intern());   // M1850_CRNT_TRNSFRNG - Current: transferring
        oasisRecord.setCRNT_AMBLTN(strRecord.substring(909, 911).intern());   // M1860_CRNT_AMBLTN - Current: ambulation
        // M1870_CRNT_FEEDING - Current: feeding
        // M1880_CRNT_PREP_LT_MEALS - Current: prepare light meals
        // M1890_CRNT_PHONE_USE - Current: telephone use
        // M1900_PRIOR_ADLIADL_SELF - Prior functioning ADL/IADL: self-care
        // M1900_PRIOR_ADLIADL_AMBLTN - Prior functioning ADL/IADL: ambulation
        // M1900_PRIOR_ADLIADL_TRNSFR - Prior functioning ADL/IADL: transfer
        // M1900_PRIOR_ADLIADL_HSEHOLD - Prior functioning ADL/IADL: household tasks
        // M1910_MLT_FCTR_FALL_RISK_ASMT - Has patient had a multi-factor fall risk asmt
        oasisRecord.setDRUG_RGMN_RVW(strRecord.substring(927, 929).intern());   // M2000_DRUG_RGMN_RVW - Drug regimen review
        // M2002_MDCTN_FLWP - Medication follow-up
        // M2004_MDCTN_INTRVTN - Medication intervention
        // M2010_HIGH_RISK_DRUG_EDCTN - Patient/caregiver high risk drug education
        // M2015_DRUG_EDCTN_INTRVTN - Patient/caregiver drug education intervention
        // M2020_CRNT_MGMT_ORAL_MDCTN - Current: management of oral medications
        oasisRecord.setCRNT_MGMT_INJCTN_MDCTN(strRecord.substring(938, 940).intern());   // M2030_CRNT_MGMT_INJCTN_MDCTN - Current: management of injectable medications
        // M2040_PRIOR_MGMT_ORAL_MDCTN - Prior med mgmt: oral medications
        // M2040_PRIOR_MGMT_INJCTN_MDCTN - Prior med mgmt: injectable medications
        // ITEM_FILLER_064 - Filler: replaces M2100_CARE_TYPE_SRC_ADL
        // ITEM_FILLER_065 - Filler: replaces M2100_CARE_TYPE_SRC_IADL
        // ITEM_FILLER_066 - Filler: replaces M2100_CARE_TYPE_SRC_MDCTN
        // ITEM_FILLER_067 - Filler: replaces M2100_CARE_TYPE_SRC_PRCDR
        // ITEM_FILLER_068 - Filler: replaces M2100_CARE_TYPE_SRC_EQUIP
        // ITEM_FILLER_069 - Filler: replaces M2100_CARE_TYPE_SRC_SPRVSN
        // ITEM_FILLER_070 - Filler: replaces M2100_CARE_TYPE_SRC_ADVCY
        // M2110_ADL_IADL_ASTNC_FREQ - How often recv ADL or IADL assistance from any
        oasisRecord.setTHER_NEED_NBR(HomeHealthRecordUtil.parseTherapyNeedNumber_C1(strRecord.substring(960, 963), -1));   // M2200_THER_NEED_NBR - Therapy need: number of visits
        oasisRecord.setTHER_NEED_NA(strRecord.substring(963, 964).intern());   // M2200_THER_NEED_NA - Therapy need: not applicable
        // M2250_PLAN_SMRY_PTNT_SPECF - Plan of care synopsis: patient specific parameters
        // M2250_PLAN_SMRY_DBTS_FT_CARE - Plan of care synopsis: diabetic foot care
        // M2250_PLAN_SMRY_FALL_PRVNT - Plan of care synopsis: falls prevention
        // M2250_PLAN_SMRY_DPRSN_INTRVTN - Plan of care synopsis: depression interventions
        // M2250_PLAN_SMRY_PAIN_INTRVTN - Plan of care synopsis: pain interventions
        // M2250_PLAN_SMRY_PRSULC_PRVNT - Plan of care synopsis: PU prevention
        // M2250_PLAN_SMRY_PRSULC_TRTMT - Plan of care synopsis: PU moist treatment
        // M2300_EMER_USE_AFTR_LAST_ASMT - Emergent care: use since previous OASIS
        // M2310_ECR_MEDICATION - Emergent care reason: medication
        // M2310_ECR_INJRY_BY_FALL - Emergent care reason: injury caused by fall
        // M2310_ECR_RSPRTRY_INFCTN - Emergent care reason: respiratory infection
        // M2310_ECR_RSPRTRY_OTHR - Emergent care reason: respiratory other
        // M2310_ECR_HRT_FAILR - Emergent care reason: heart failure
        // M2310_ECR_CRDC_DSRTHM - Emergent care reason: cardiac dysrhythmia
        // M2310_ECR_MI_CHST_PAIN - Emergent care reason: myocard infarct/chest pain
        // M2310_ECR_OTHR_HRT_DEASE - Emergent care reason: other heart disease
        // M2310_ECR_STROKE_TIA - Emergent care reason: stroke (CVA) or TIA
        // M2310_ECR_HYPOGLYC - Emergent care reason: hypoglycemia
        // M2310_ECR_GI_PRBLM - Emergent care: GI bleed/obstruct/constip/impact
        // M2310_ECR_DHYDRTN_MALNTR - Emergent care reason: dehydration, malnutrition
        // M2310_ECR_UTI - Emergent care reason: urinary tract infection
        // M2310_ECR_CTHTR_CMPLCTN - Emergent care reason: IV catheter infect/complic
        // M2310_ECR_WND_INFCTN_DTRORTN - Emergent care reason: wound infect/deterioration
        // M2310_ECR_UNCNTLD_PAIN - Emergent care reason: uncontrolled pain
        // M2310_ECR_MENTL_BHVRL_PRBLM - Emergent care reason: acute mental/behav problem
        // M2310_ECR_DVT_PULMNRY - Emergent care reason: deep vein thromb/pulm embol
        // M2310_ECR_OTHER - Emergent care reason: other than above
        // M2310_ECR_UNKNOWN - Emergent care reason: unknown
        // M2400_INTRVTN_SMRY_DBTS_FT - Intervention synopsis: diabetic foot care
        // M2400_INTRVTN_SMRY_FALL_PRVNT - Intervention synopsis: falls prevention
        // M2400_INTRVTN_SMRY_DPRSN - Intervention synopsis: depression intervention
        // M2400_INTRVTN_SMRY_PAIN_MNTR - Intervention synopsis: monitor and mitigate pain
        // M2400_INTRVTN_SMRY_PRSULC_PRVN - Intervention synopsis: prevent pressure ulcers
        // M2400_INTRVTN_SMRY_PRSULC_WET - Intervention synopsis: PU moist wound treatment
        // M2410_INPAT_FACILITY - Inpatient facility
        // M2420_DSCHRG_DISP - Discharge disposition
        // M2430_HOSP_MED - Hospitalized: medication
        // M2430_HOSP_INJRY_BY_FALL - Hospitalized: injury caused by fall
        // M2430_HOSP_RSPRTRY_INFCTN - Hospitalized: respiratory
        // M2430_HOSP_RSPRTRY_OTHR - Hospitalized: other respiratory
        // M2430_HOSP_HRT_FAILR - Hospitalized: heart failure
        // M2430_HOSP_CRDC_DSRTHM - Hospitalized: cardiac dysrhythmia
        // M2430_HOSP_MI_CHST_PAIN - Hospitalized: myocardial infarction or chest pain
        // M2430_HOSP_OTHR_HRT_DEASE - Hospitalized: other heart disease
        // M2430_HOSP_STROKE_TIA - Hospitalized: stroke (CVA) or TIA
        // M2430_HOSP_HYPOGLYC - Hospitalized: hypoglycemia
        // M2430_HOSP_GI_PRBLM - Hospitalized: GI bleed/obstruct/constip/impact
        // M2430_HOSP_DHYDRTN_MALNTR - Hospitalized: dehydration, malnutrition
        // M2430_HOSP_UR_TRACT - Hospitalized: urinary tract infection
        // M2430_HOSP_CTHTR_CMPLCTN - Hospitalized: IV catheter infect/complic
        // M2430_HOSP_WND_INFCTN - Hospitalized: wound infect/deterioration
        // M2430_HOSP_PAIN - Hospitalized: pain
        // M2430_HOSP_MENTL_BHVRL_PRBLM - Hospitalized: acute mental/behav problem
        // M2430_HOSP_DVT_PULMNRY - Hospitalized: deep vein thromb/pulm embol
        // M2430_HOSP_SCHLD_TRTMT - Hospitalized: scheduled treatment or procedure
        // M2430_HOSP_OTHER - Hospitalized: other
        // M2430_HOSP_UK - Hospitalized: UK
        // ITEM_FILLER_071 - Filler: replaces M2440_NH_THERAPY
        // ITEM_FILLER_072 - Filler: replaces M2440_NH_RESPITE
        // ITEM_FILLER_073 - Filler: replaces M2440_NH_HOSPICE
        // ITEM_FILLER_074 - Filler: replaces M2440_NH_PERMANENT
        // ITEM_FILLER_075 - Filler: replaces M2440_NH_UNSAFE_HOME
        // ITEM_FILLER_076 - Filler: replaces M2440_NH_OTHER
        // ITEM_FILLER_077 - Filler: replaces M2440_NH_UNKNOWN
        // M0903_LAST_HOME_VISIT - Date of last home visit
        // M0906_DC_TRAN_DTH_DT - Discharge, transfer, death date
        // CONTROL_ITEMS_FILLER - Control items filler
        // M1011_14_DAY_INP1_ICD - Inpatient stay within last 14 days: ICD code 1
        // M1011_14_DAY_INP2_ICD - Inpatient stay within last 14 days: ICD code 2
        // M1011_14_DAY_INP3_ICD - Inpatient stay within last 14 days: ICD code 3
        // M1011_14_DAY_INP4_ICD - Inpatient stay within last 14 days: ICD code 4
        // M1011_14_DAY_INP5_ICD - Inpatient stay within last 14 days: ICD code 5
        // M1011_14_DAY_INP6_ICD - Inpatient stay within last 14 days: ICD code 6
        // M1011_14_DAY_INP_NA - Inpatient stay within last 14 days: not applicable
        // M1017_CHGREG_ICD1 - Regimen change in past 14 days: ICD code 1
        // M1017_CHGREG_ICD2 - Regimen change in past 14 days: ICD code 2
        // M1017_CHGREG_ICD3 - Regimen change in past 14 days: ICD code 3
        // M1017_CHGREG_ICD4 - Regimen change in past 14 days: ICD code 4
        // M1017_CHGREG_ICD5 - Regimen change in past 14 days: ICD code 5
        // M1017_CHGREG_ICD6 - Regimen change in past 14 days: ICD code 6
        // M1017_CHGREG_ICD_NA - Regimen change in past 14 days: not applicable
//		oasisRecord.setPRIMARY_DIAG_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1358, 1366)));   // M1021_PRIMARY_DIAG_ICD - Primary diagnosis ICD code
//		// M1021_PRIMARY_DIAG_SEVERITY - Primary diagnosis severity rating
//		oasisRecord.setOTH_DIAG1_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1368, 1376)));   // M1023_OTH_DIAG1_ICD - Other diagnosis 1: ICD code
//		// M1023_OTH_DIAG1_SEVERITY - Other diagnosis 1: severity rating
//		oasisRecord.setOTH_DIAG2_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1378, 1386)));   // M1023_OTH_DIAG2_ICD - Other diagnosis 2: ICD code
//		// M1023_OTH_DIAG2_SEVERITY - Other diagnosis 2: severity rating
//		oasisRecord.setOTH_DIAG3_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1388, 1396)));   // M1023_OTH_DIAG3_ICD - Other diagnosis 3: ICD code
//		// M1023_OTH_DIAG3_SEVERITY - Other diagnosis 3: severity rating
//		oasisRecord.setOTH_DIAG4_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1398, 1406)));   // M1023_OTH_DIAG4_ICD - Other diagnosis 4: ICD code
//		// M1023_OTH_DIAG4_SEVERITY - Other diagnosis 4: severity rating
//		oasisRecord.setOTH_DIAG5_ICD(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1408, 1416)));   // M1023_OTH_DIAG5_ICD - Other diagnosis 5: ICD code
//		// M1023_OTH_DIAG5_SEVERITY - Other diagnosis 5: severity rating
//		oasisRecord.setPMT_DIAG_ICD_A3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1418, 1426)));   // M1025_PMT_DIAG_ICD_A3 - Case mix diagnosis: primary, column 3
//		oasisRecord.setPMT_DIAG_ICD_A4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1426, 1434)));   // M1025_PMT_DIAG_ICD_A4 - Case mix diagnosis: primary, column 4
//		oasisRecord.setPMT_DIAG_ICD_B3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1434, 1442)));   // M1025_PMT_DIAG_ICD_B3 - Case mix diagnosis: first secondary, column 3
//		oasisRecord.setPMT_DIAG_ICD_B4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1442, 1450)));   // M1025_PMT_DIAG_ICD_B4 - Case mix diagnosis: first secondary, column 4
//		oasisRecord.setPMT_DIAG_ICD_C3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1450, 1458)));   // M1025_PMT_DIAG_ICD_C3 - Case mix diagnosis: second secondary, column 3
//		oasisRecord.setPMT_DIAG_ICD_C4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1458, 1466)));   // M1025_PMT_DIAG_ICD_C4 - Case mix diagnosis: second secondary, column 4
//		oasisRecord.setPMT_DIAG_ICD_D3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1466, 1474)));   // M1025_PMT_DIAG_ICD_D3 - Case mix diagnosis: third secondary, column 3
//		oasisRecord.setPMT_DIAG_ICD_D4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1474, 1482)));   // M1025_PMT_DIAG_ICD_D4 - Case mix diagnosis: third secondary, column 4
//		oasisRecord.setPMT_DIAG_ICD_E3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1482, 1490)));   // M1025_PMT_DIAG_ICD_E3 - Case mix diagnosis: fourth secondary, column 3
//		oasisRecord.setPMT_DIAG_ICD_E4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1490, 1498)));   // M1025_PMT_DIAG_ICD_E4 - Case mix diagnosis: fourth secondary, column 4
//		oasisRecord.setPMT_DIAG_ICD_F3(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1498, 1506)));   // M1025_PMT_DIAG_ICD_F3 - Case mix diagnosis: fifth secondary, column 3
//		oasisRecord.setPMT_DIAG_ICD_F4(HomeHealthRecordUtil.parseDxCode(strRecord.substring(1506, 1514)));   // M1025_PMT_DIAG_ICD_F4 - Case mix diagnosis: fifth secondary, column 4
        // M1033_HOSP_RISK_HSTRY_FALLS - Hosp risk: 2+ falls or injury fall in past year
        // M1033_HOSP_RISK_WEIGHT_LOSS - Hosp risk: unintentional weight loss
        // M1033_HOSP_RISK_MLTPL_HOSPZTN - Hosp risk: 2+ hospitalizations in past 12 months
        // M1033_HOSP_RISK_MLTPL_ED_VISIT - Hosp risk: 2+ emergcy dept visits in past 6 months
        // M1033_HOSP_RISK_MNTL_BHV_DCLN - Hosp risk: decline mental/emotional/behav status
        // M1033_HOSP_RISK_COMPLIANCE - Hosp risk: difficulty with medical instructions
        // M1033_HOSP_RISK_6PLUS_MDCTN - Hosp risk: taking six or more medications
        // M1033_HOSP_RISK_CURR_EXHSTN - Hosp risk: current exhaustion
        // M1033_HOSP_RISK_OTHR - Hosp risk: other
        // M1033_HOSP_RISK_NONE_ABOVE - Hosp risk: none of the above
        // M1041_IN_INFLNZ_SEASON - Does episode include Oct 1 thru Mar 31
        // M1046_INFLNZ_RECD_CUR_SEASON - Did patient receive influenza vaccine
        // M1051_PPV_RCVD_AGNCY - Was pneumococcal vaccine received
        // M1056_PPV_RSN_NOT_RCVD_AGNCY - If pneumococcal vacc not received, state reason
        // M1309_NBR_NEW_WRS_PRSULC_STG2 - Number of new or worsening stage 2
        // M1309_NBR_NEW_WRS_PRSULC_STG3 - Number of new or worsening stage 3
        // M1309_NBR_NEW_WRS_PRSULC_STG4 - Number of new or worsening stage 4
        // M2102_CARE_TYPE_SRC_ADL - Care mgmt, types/sources: ADL
        // M2102_CARE_TYPE_SRC_IADL - Care mgmt, types/sources: IADL
        // M2102_CARE_TYPE_SRC_MDCTN - Care mgmt, types/sources: med admin
        // M2102_CARE_TYPE_SRC_PRCDR - Care mgmt, types/sources: med procs tx
        // M2102_CARE_TYPE_SRC_EQUIP - Care mgmt, types/sources: equipment
        // M2102_CARE_TYPE_SRC_SPRVSN - Care mgmt, types/sources: supervision and safety
        // M2102_CARE_TYPE_SRC_ADVCY - Care mgmt, types/sources: advocacy or facilitation
        // ASMT_ITEMS_FILLER - Assessment items filler
        // LEGACY_ITEMS_FILLER - Discontinued OASIS-B1 and C items.
        // CALCULATED_ITEMS_FILLER - Calculated items filler
        // HHA_ASMT_INT_ID - Assessment internal ID
        // ORIG_ASMT_INT_ID - Original assessment ID
        // RES_INT_ID - Resident internal ID
        // ASMT_EFF_DATE - Effective date
        // BRANCH_IDENTIFIER - Branch internal ID
        // FAC_INT_ID - Facility internal ID
        // SUBMISSION_ID - Submission ID
        // SUBMISSION_DATE - Submission date
        // SUBMISSION_COMPLETE_DATE - Submission processing completion date
        // SUBMITTING_USER_ID - Submitter user ID
        // RES_MATCH_CRITERIA - Resident matching criteria
        // RESIDENT_AGE - Age of resident on the effective date
        // BIRTHDATE_SUBM_IND - Birth date submit code
        // CALC_HIPPS_CODE - HIPPS group code: recalculated
        // CALC_HIPPS_VERSION - HIPPS version code: recalculated
        // DATA_END_INDICATOR - End of data terminator code
        // CR - Carriage return (ASCII 013)
        // LF - Line feed character (ASCII 010)

        postProcessRecord(oasisRecord);

        return oasisRecord;
    }

    public StringBuilder convertFromHomeHealthToFlatRecord(HomeHealthRecord_C_IF oasisRecord) {
        StringBuilder buffer = new StringBuilder(3260);

        buffer.append("OASIS     ");   // ASMT_SYS_CD - Assessment system code
        buffer.append(" ");   // TRANS_TYPE_CD - Transaction type code
        buffer.append("   ");   // ITM_SBST_CD - Item subset code
        buffer.append("C1-102014 ");   // ITM_SET_VRSN_CD - Item set version code
        buffer.append("2.11      ");   // SPEC_VRSN_CD - Specifications version code
        buffer.append("  ");   // CORRECTION_NUM - Correction number
        buffer.append("  ");   // STATE_CD - Agency's state postal code
        buffer.append("                ");   // HHA_AGENCY_ID - Assigned agency submission ID
        buffer.append("          ");   // NATL_PRVDR_ID - Agency National Provider ID (NPI)
        buffer.append("         ");   // SFW_ID - Software vendor federal employer tax ID
        buffer.append("                              ");   // SFW_NAME - Software vendor company name
        buffer.append("                                                  ");   // SFW_EMAIL_ADR - Software vendor email address
        buffer.append("                                                  ");   // SFW_PROD_NAME - Software product name
        buffer.append("                    ");   // SFW_PROD_VRSN_CD - Software product version code
        buffer.append("                    ");   // ACY_DOC_CD - Document ID code (agency use)
        buffer.append("     ");   // SUBM_HIPPS_CODE - HIPPS group code: submitted
        buffer.append("     ");   // SUBM_HIPPS_VERSION - HIPPS version code: submitted
        buffer.append("      ");   // M0010_CCN - Facility CMS certification number (CCN)
        buffer.append("  ");   // M0014_BRANCH_STATE - Branch state
        buffer.append("          ");   // M0016_BRANCH_ID - Branch ID
        buffer.append("          ");   // M0018_PHYSICIAN_ID - Attending physician National Provider ID (NPI)
        buffer.append(" ");   // M0018_PHYSICIAN_UK - Attending physician NPI: Unknown
        buffer.append("                    ");   // M0020_PAT_ID - Patient ID number
        OasisCalendarFormatter.format(oasisRecord.getSTART_CARE_DT(), buffer);   // M0030_START_CARE_DT - Start of care date
        buffer.append("        ");   // M0032_ROC_DT - Resumption of care date
        buffer.append(" ");   // M0032_ROC_DT_NA - No resumption of care date
        buffer.append("            ");   // M0040_PAT_FNAME - Patient's first name
        buffer.append(" ");   // M0040_PAT_MI - Patient's middle initial
        buffer.append("                  ");   // M0040_PAT_LNAME - Patient's last name
        buffer.append("   ");   // M0040_PAT_SUFFIX - Patient's suffix
        buffer.append("  ");   // M0050_PAT_ST - Patient state of residence
        buffer.append("           ");   // M0060_PAT_ZIP - Patient zip code
        buffer.append("            ");   // M0063_MEDICARE_NUM - Medicare number, including suffix
        buffer.append(" ");   // M0063_MEDICARE_NA - No Medicare number
        buffer.append("         ");   // M0064_SSN - Patient's Social Security number
        buffer.append(" ");   // M0064_SSN_UK - No Social Security number
        buffer.append("              ");   // M0065_MEDICAID_NUM - Medicaid number
        buffer.append(" ");   // M0065_MEDICAID_NA - No Medicaid number
        buffer.append("        ");   // M0066_PAT_BIRTH_DT - Date of birth
        buffer.append(" ");   // M0069_PAT_GENDER - Gender
        buffer.append(" ");   // M0140_ETHNIC_AI_AN - Ethnicity: American Indian or Alaska Native
        buffer.append(" ");   // M0140_ETHNIC_ASIAN - Ethnicity: Asian
        buffer.append(" ");   // M0140_ETHNIC_BLACK - Ethnicity: Black or African American
        buffer.append(" ");   // M0140_ETHNIC_HISP - Ethnicity: Hispanic or Latino
        buffer.append(" ");   // M0140_ETHNIC_NH_PI - Ethnicity: Native Hawaiian/Pacific Islander
        buffer.append(" ");   // M0140_ETHNIC_WHITE - Ethnicity: White
        buffer.append(" ");   // M0150_CPAY_NONE - Payment sources: no charge for current services
        buffer.append(" ");   // M0150_CPAY_MCARE_FFS - Payment sources: Medicare fee-for-service
        buffer.append(" ");   // M0150_CPAY_MCARE_HMO - Payment sources: Medicare HMO/managed care
        buffer.append(" ");   // M0150_CPAY_MCAID_FFS - Payment sources: Medicaid fee-for-service
        buffer.append(" ");   // M0150_CPAY_MCAID_HMO - Payment sources: Medicaid HMO/managed care
        buffer.append(" ");   // M0150_CPAY_WRKCOMP - Payment sources: worker's compensation
        buffer.append(" ");   // M0150_CPAY_TITLEPGMS - Payment sources: title programs
        buffer.append(" ");   // M0150_CPAY_OTH_GOVT - Payment sources: other government
        buffer.append(" ");   // M0150_CPAY_PRIV_INS - Payment sources: private insurance
        buffer.append(" ");   // M0150_CPAY_PRIV_HMO - Payment sources: private HMO/managed care
        buffer.append(" ");   // M0150_CPAY_SELFPAY - Payment sources: self-pay
        buffer.append(" ");   // M0150_CPAY_OTHER - Payment sources: other
        buffer.append(" ");   // M0150_CPAY_UK - Payment sources: unknown
        buffer.append("  ");   // M0080_ASSESSOR_DISCIPLINE - Discipline of person completing assessment
        OasisCalendarFormatter.format(oasisRecord.getINFO_COMPLETED_DT(), buffer);   // M0090_INFO_COMPLETED_DT - Date assessment completed
        buffer.append(oasisRecord.getASSMT_REASON());   // M0100_ASSMT_REASON - Reason for assessment
        buffer.append("        ");   // M0102_PHYSN_ORDRD_SOCROC_DT - Physician ordered SOC/ROC date
        buffer.append(" ");   // M0102_PHYSN_ORDRD_SOCROC_DT_NA - Physician ordered SOC/ROC date - NA
        buffer.append("        ");   // M0104_PHYSN_RFRL_DT - Physician date of referral
        buffer.append(oasisRecord.getEPISODE_TIMING());   // M0110_EPISODE_TIMING - Episode timing
        buffer.append(" ");   // M1000_DC_LTC_14_DA - Past 14 days: disch from LTC NH
        buffer.append(" ");   // M1000_DC_SNF_14_DA - Past 14 days: disch from skilled nursing facility
        buffer.append(" ");   // M1000_DC_IPPS_14_DA - Past 14 days: disch from short stay acute hospital
        buffer.append(" ");   // M1000_DC_LTCH_14_DA - Past 14 days: disch from long term care hospital
        buffer.append(" ");   // M1000_DC_IRF_14_DA - Past 14 days: disch from inpatient rehab facility
        buffer.append(" ");   // M1000_DC_PSYCH_14_DA - Past 14 days: disch from psych hospital or unit
        buffer.append(" ");   // M1000_DC_OTH_14_DA - Past 14 days: disch from other
        buffer.append(" ");   // M1000_DC_NONE_14_DA - Past 14 days: not disch from inpatient facility
        buffer.append("        ");   // M1005_INP_DISCHARGE_DT - Most recent inpatient discharge date
        buffer.append(" ");   // M1005_INP_DSCHG_UNKNOWN - Inpatient discharge date unknown
        buffer.append("       ");   // ITEM_FILLER_001 - Filler: replaces M1010_14_DAY_INP1_ICD
        buffer.append("       ");   // ITEM_FILLER_002 - Filler: replaces M1010_14_DAY_INP2_ICD
        buffer.append("       ");   // ITEM_FILLER_003 - Filler: replaces M1010_14_DAY_INP3_ICD
        buffer.append("       ");   // ITEM_FILLER_004 - Filler: replaces M1010_14_DAY_INP4_ICD
        buffer.append("       ");   // ITEM_FILLER_005 - Filler: replaces M1010_14_DAY_INP5_ICD
        buffer.append("       ");   // ITEM_FILLER_006 - Filler: replaces M1010_14_DAY_INP6_ICD
        buffer.append("       ");   // ITEM_FILLER_007 - Filler: replaces M1012_INP_PRCDR1_ICD
        buffer.append("       ");   // ITEM_FILLER_008 - Filler: replaces M1012_INP_PRCDR2_ICD
        buffer.append("       ");   // ITEM_FILLER_009 - Filler: replaces M1012_INP_PRCDR3_ICD
        buffer.append("       ");   // ITEM_FILLER_010 - Filler: replaces M1012_INP_PRCDR4_ICD
        buffer.append(" ");   // ITEM_FILLER_011 - Filler: replaces M1012_INP_NA_ICD
        buffer.append(" ");   // ITEM_FILLER_012 - Filler: replaces M1012_INP_UK_ICD
        buffer.append("       ");   // ITEM_FILLER_013 - Filler: replaces M1016_CHGREG_ICD1
        buffer.append("       ");   // ITEM_FILLER_014 - Filler: replaces M1016_CHGREG_ICD2
        buffer.append("       ");   // ITEM_FILLER_015 - Filler: replaces M1016_CHGREG_ICD3
        buffer.append("       ");   // ITEM_FILLER_016 - Filler: replaces M1016_CHGREG_ICD4
        buffer.append("       ");   // ITEM_FILLER_017 - Filler: replaces M1016_CHGREG_ICD5
        buffer.append("       ");   // ITEM_FILLER_018 - Filler: replaces M1016_CHGREG_ICD6
        buffer.append(" ");   // ITEM_FILLER_019 - Filler: replaces M1016_CHGREG_ICD_NA
        buffer.append(" ");   // M1018_PRIOR_UR_INCON - Prior condition: urinary incontinence
        buffer.append(" ");   // M1018_PRIOR_CATH - Prior condition: indwelling/suprapubic catheter
        buffer.append(" ");   // M1018_PRIOR_INTRACT_PAIN - Prior condition: intractable pain
        buffer.append(" ");   // M1018_PRIOR_IMPR_DECSN - Prior condition: impaired decision-making
        buffer.append(" ");   // M1018_PRIOR_DISRUPTIVE - Prior condition: disruptive/inappropriate behav
        buffer.append(" ");   // M1018_PRIOR_MEM_LOSS - Prior condition: memory loss, supervision required
        buffer.append(" ");   // M1018_PRIOR_NONE - Prior condition: none of the above
        buffer.append(" ");   // M1018_PRIOR_NOCHG_14D - Prior condition: no inpt disch/no change regimen
        buffer.append(" ");   // M1018_PRIOR_UNKNOWN - Prior condition: unknown
        buffer.append("       ");   // ITEM_FILLER_020 - Filler: replaces M1020_PRIMARY_DIAG_ICD
        buffer.append("  ");   // ITEM_FILLER_021 - Filler: replaces M1020_PRIMARY_DIAG_SEVERITY
        buffer.append("       ");   // ITEM_FILLER_022 - Filler: replaces M1022_OTH_DIAG1_ICD
        buffer.append("  ");   // ITEM_FILLER_023 - Filler: replaces M1022_OTH_DIAG1_SEVERITY
        buffer.append("       ");   // ITEM_FILLER_024 - Filler: replaces M1022_OTH_DIAG2_ICD
        buffer.append("  ");   // ITEM_FILLER_025 - Filler: replaces M1022_OTH_DIAG2_SEVERITY
        buffer.append("       ");   // ITEM_FILLER_026 - Filler: replaces M1022_OTH_DIAG3_ICD
        buffer.append("  ");   // ITEM_FILLER_027 - Filler: replaces M1022_OTH_DIAG3_SEVERITY
        buffer.append("       ");   // ITEM_FILLER_028 - Filler: replaces M1022_OTH_DIAG4_ICD
        buffer.append("  ");   // ITEM_FILLER_029 - Filler: replaces M1022_OTH_DIAG4_SEVERITY
        buffer.append("       ");   // ITEM_FILLER_030 - Filler: replaces M1022_OTH_DIAG5_ICD
        buffer.append("  ");   // ITEM_FILLER_031 - Filler: replaces M1022_OTH_DIAG5_SEVERITY
        buffer.append("       ");   // ITEM_FILLER_032 - Filler: replaces M1024_PMT_DIAG_ICD_A3
        buffer.append("       ");   // ITEM_FILLER_033 - Filler: replaces M1024_PMT_DIAG_ICD_B3
        buffer.append("       ");   // ITEM_FILLER_034 - Filler: replaces M1024_PMT_DIAG_ICD_C3
        buffer.append("       ");   // ITEM_FILLER_035 - Filler: replaces M1024_PMT_DIAG_ICD_D3
        buffer.append("       ");   // ITEM_FILLER_036 - Filler: replaces M1024_PMT_DIAG_ICD_E3
        buffer.append("       ");   // ITEM_FILLER_037 - Filler: replaces M1024_PMT_DIAG_ICD_F3
        buffer.append("       ");   // ITEM_FILLER_038 - Filler: replaces M1024_PMT_DIAG_ICD_A4
        buffer.append("       ");   // ITEM_FILLER_039 - Filler: replaces M1024_PMT_DIAG_ICD_B4
        buffer.append("       ");   // ITEM_FILLER_040 - Filler: replaces M1024_PMT_DIAG_ICD_C4
        buffer.append("       ");   // ITEM_FILLER_041 - Filler: replaces M1024_PMT_DIAG_ICD_D4
        buffer.append("       ");   // ITEM_FILLER_042 - Filler: replaces M1024_PMT_DIAG_ICD_E4
        buffer.append("       ");   // ITEM_FILLER_043 - Filler: replaces M1024_PMT_DIAG_ICD_F4
        buffer.append(oasisRecord.getTHH_IV_INFUSION());   // M1030_THH_IV_INFUSION - Therapies received at home: intravenous, infusion
        buffer.append(oasisRecord.getTHH_PAR_NUTRITION());   // M1030_THH_PAR_NUTRITION - Therapies received at home: parenteral nutrition
        buffer.append(oasisRecord.getTHH_ENT_NUTRITION());   // M1030_THH_ENT_NUTRITION - Therapies received at home: enteral nutrition
        buffer.append(oasisRecord.getTHH_NONE_ABOVE());   // M1030_THH_NONE_ABOVE - Therapies received at home: none of the above
        buffer.append(" ");   // ITEM_FILLER_044 - Filler: replaces M1032_HOSP_RISK_RCNT_DCLN
        buffer.append(" ");   // ITEM_FILLER_045 - Filler: replaces M1032_HOSP_RISK_MLTPL_HOSPZTN
        buffer.append(" ");   // ITEM_FILLER_046 - Filler: replaces M1032_HOSP_RISK_HSTRY_FALLS
        buffer.append(" ");   // ITEM_FILLER_047 - Filler: replaces M1032_HOSP_RISK_5PLUS_MDCTN
        buffer.append(" ");   // ITEM_FILLER_048 - Filler: replaces M1032_HOSP_RISK_FRAILTY
        buffer.append(" ");   // ITEM_FILLER_049 - Filler: replaces M1032_HOSP_RISK_OTHR
        buffer.append(" ");   // ITEM_FILLER_050 - Filler: replaces M1032_HOSP_RISK_NONE_ABOVE
        buffer.append("  ");   // M1034_PTNT_OVRAL_STUS - Patient's overall status
        buffer.append(" ");   // M1036_RSK_SMOKING - High risk factor: smoking
        buffer.append(" ");   // M1036_RSK_OBESITY - High risk factor: obesity
        buffer.append(" ");   // M1036_RSK_ALCOHOLISM - High risk factor: alcoholism
        buffer.append(" ");   // M1036_RSK_DRUGS - High risk factor: drugs
        buffer.append(" ");   // M1036_RSK_NONE - High risk factor: none of the above
        buffer.append(" ");   // M1036_RSK_UNKNOWN - High risk factor: unknown
        buffer.append("  ");   // ITEM_FILLER_051 - Filler: replaces M1040_INFLNZ_RCVD_AGNCY
        buffer.append("  ");   // ITEM_FILLER_052 - Filler: replaces M1045_INFLNZ_RSN_NOT_RCVD
        buffer.append(" ");   // ITEM_FILLER_053 - Filler: replaces M1050_PPV_RCVD_AGNCY
        buffer.append("  ");   // ITEM_FILLER_054 - Filler: replaces M1055_PPV_RSN_NOT_RCVD_AGNCY
        buffer.append("  ");   // M1100_PTNT_LVG_STUTN - Patient living situation
        buffer.append(oasisRecord.getVISION());   // M1200_VISION - Sensory status: vision
        buffer.append("  ");   // M1210_HEARG_ABLTY - Ability to hear
        buffer.append("  ");   // M1220_UNDRSTG_VERBAL_CNTNT - Understanding verbal content in patient's language
        buffer.append("  ");   // M1230_SPEECH - Sensory status: speech
        buffer.append("  ");   // M1240_FRML_PAIN_ASMT - Has patient had a formal validated pain assessment
        buffer.append(oasisRecord.getPAIN_FREQ_ACTVTY_MVMT());   // M1242_PAIN_FREQ_ACTVTY_MVMT - Freq of pain interfering with pt activity/movement
        buffer.append("  ");   // M1300_PRSR_ULCR_RISK_ASMT - Was patient assessed for risk of developing pus
        buffer.append(" ");   // M1302_RISK_OF_PRSR_ULCR - Does this patient have a risk of developing pus
        buffer.append(oasisRecord.getUNHLD_STG2_PRSR_ULCR());   // M1306_UNHLD_STG2_PRSR_ULCR - Patient has 1+ unhealed PU at stage 2 or higher
        buffer.append("  ");   // M1307_OLDST_STG2_AT_DSCHRG - Status of oldest stage 2 pressure ulcer at disch
        buffer.append("        ");   // M1307_OLDST_STG2_ONST_DT - Date of onset of oldest stage 2 pressure ulcer
        buffer.append(oasisRecord.getNBR_PRSULC_STG2());   // M1308_NBR_PRSULC_STG2 - No. pressure ulcers - stage 2
        buffer.append("  ");   // ITEM_FILLER_055 - Filler: replaces M1308_NBR_STG2_AT_SOC_ROC
        buffer.append(oasisRecord.getNBR_PRSULC_STG3());   // M1308_NBR_PRSULC_STG3 - No. pressure ulcers - stage 3
        buffer.append("  ");   // ITEM_FILLER_056 - Filler: replaces M1308_NBR_STG3_AT_SOC_ROC
        buffer.append(oasisRecord.getNBR_PRSULC_STG4());   // M1308_NBR_PRSULC_STG4 - No. pressure ulcers - stage 4
        buffer.append("  ");   // ITEM_FILLER_057 - Filler: replaces M1308_NBR_STG4_AT_SOC_ROC
        buffer.append(oasisRecord.getNSTG_DRSG());   // M1308_NSTG_DRSG - Unstageable: non-removable dressing/device
        buffer.append("  ");   // ITEM_FILLER_058 - Filler: replaces M1308_NSTG_DRSG_SOC_ROC
        buffer.append(oasisRecord.getNSTG_CVRG());   // M1308_NSTG_CVRG - Unstageable: coverage by slough or eschar
        buffer.append("  ");   // ITEM_FILLER_059 - Filler: replaces M1308_NSTG_CVRG_SOC_ROC
        buffer.append(oasisRecord.getNSTG_DEEP_TISUE());   // M1308_NSTG_DEEP_TISUE - Unstageable: suspect deep tissue injury
        buffer.append("  ");   // ITEM_FILLER_060 - Filler: replaces M1308_NSTG_DEEP_TISUE_SOC_ROC
        buffer.append("    ");   // ITEM_FILLER_061 - Filler: replaces M1310_PRSR_ULCR_LNGTH
        buffer.append("    ");   // ITEM_FILLER_062 - Filler: replaces M1312_PRSR_ULCR_WDTH
        buffer.append("    ");   // ITEM_FILLER_063 - Filler: replaces M1314_PRSR_ULCR_DEPTH
        buffer.append(oasisRecord.getSTUS_PRBLM_PRSR_ULCR());   // M1320_STUS_PRBLM_PRSR_ULCR - Status of most problematic pressure ulcer
        buffer.append(oasisRecord.getNBR_PRSULC_STG1());   // M1322_NBR_PRSULC_STG1 - No. pressure ulcers - stage 1
        buffer.append(oasisRecord.getSTG_PRBLM_ULCER());   // M1324_STG_PRBLM_ULCER - Stage of most problematic pressure ulcer
        buffer.append(oasisRecord.getSTAS_ULCR_PRSNT());   // M1330_STAS_ULCR_PRSNT - Does this patient have a stasis ulcer
        buffer.append(oasisRecord.getNBR_STAS_ULCR());   // M1332_NBR_STAS_ULCR - No. stasis ulcers
        buffer.append(oasisRecord.getSTUS_PRBLM_STAS_ULCR());   // M1334_STUS_PRBLM_STAS_ULCR - Status of most problematic stasis ulcer
        buffer.append(oasisRecord.getSRGCL_WND_PRSNT());   // M1340_SRGCL_WND_PRSNT - Does this patient have a surgical wound
        buffer.append(oasisRecord.getSTUS_PRBLM_SRGCL_WND());   // M1342_STUS_PRBLM_SRGCL_WND - Status of most problematic surgical wound
        buffer.append(oasisRecord.getLESION_OPEN_WND());   // M1350_LESION_OPEN_WND - Has skin lesion or open wound
        buffer.append(oasisRecord.getWHEN_DYSPNEIC());   // M1400_WHEN_DYSPNEIC - When dyspneic
        buffer.append(" ");   // M1410_RESPTX_OXYGEN - Respiratory treatments: oxygen
        buffer.append(" ");   // M1410_RESPTX_VENTILATOR - Respiratory treatments: ventilator
        buffer.append(" ");   // M1410_RESPTX_AIRPRESS - Respiratory treatments: airway pressure
        buffer.append(" ");   // M1410_RESPTX_NONE - Respiratory treatments: none of the above
        buffer.append("  ");   // M1500_SYMTM_HRT_FAILR_PTNTS - Symptoms in heart failure patients
        buffer.append(" ");   // M1510_HRT_FAILR_NO_ACTN - Heart failure follow up:  no action
        buffer.append(" ");   // M1510_HRT_FAILR_PHYSN_CNTCT - Heart failure follow up:  physician contacted
        buffer.append(" ");   // M1510_HRT_FAILR_ER_TRTMT - Heart failure follow up:  ER treatment advised
        buffer.append(" ");   // M1510_HRT_FAILR_PHYSN_TRTMT - Heart failure follow up:  phys-ordered treatmnt
        buffer.append(" ");   // M1510_HRT_FAILR_CLNCL_INTRVTN - Heart failure follow up: pt educ/other clinical
        buffer.append(" ");   // M1510_HRT_FAILR_CARE_PLAN_CHG - Heart failure follow up: change in care plan
        buffer.append("  ");   // M1600_UTI - Treated for urinary tract infection past 14 days
        buffer.append(oasisRecord.getUR_INCONT());   // M1610_UR_INCONT - Urinary incontinence or urinary catheter present
        buffer.append(oasisRecord.getINCNTNT_TIMING());   // M1615_INCNTNT_TIMING - When urinary incontinence occurs
        buffer.append(oasisRecord.getBWL_INCONT());   // M1620_BWL_INCONT - Bowel incontinence frequency
        buffer.append(oasisRecord.getOSTOMY());   // M1630_OSTOMY - Ostomy for bowel elimination
        buffer.append("  ");   // M1700_COG_FUNCTION - Cognitive functioning
        buffer.append("  ");   // M1710_WHEN_CONFUSED - When confused (reported or observed)
        buffer.append("  ");   // M1720_WHEN_ANXIOUS - When anxious (reported or observed)
        buffer.append("  ");   // M1730_STDZ_DPRSN_SCRNG - Screened for depression using validated tool
        buffer.append("  ");   // M1730_PHQ2_LACK_INTRST - PHQ2: little interest or pleasure in doing things
        buffer.append("  ");   // M1730_PHQ2_DPRSN - PHQ2: feeling down, depressed or hopeless
        buffer.append(" ");   // M1740_BD_MEM_DEFICIT - Behavior demonstrated: memory deficit
        buffer.append(" ");   // M1740_BD_IMP_DECISN - Behavior demonstrated: impaired decision-making
        buffer.append(" ");   // M1740_BD_VERBAL - Behavior demonstrated: verbal disruption
        buffer.append(" ");   // M1740_BD_PHYSICAL - Behavior demonstrated: physical aggression
        buffer.append(" ");   // M1740_BD_SOC_INAPPRO - Behavior demonstrated: socially inappropriate
        buffer.append(" ");   // M1740_BD_DELUSIONS - Behavior demonstrated: delusions
        buffer.append(" ");   // M1740_BD_NONE - Behavior demonstrated: none of the above
        buffer.append("  ");   // M1745_BEH_PROB_FREQ - Frequency of behavior problems
        buffer.append(" ");   // M1750_REC_PSYCH_NURS - Receives psychiatric nursing
        buffer.append("  ");   // M1800_CRNT_GROOMING - Current: grooming
        buffer.append(oasisRecord.getCRNT_DRESS_UPPER());   // M1810_CRNT_DRESS_UPPER - Current: dress upper body
        buffer.append(oasisRecord.getCRNT_DRESS_LOWER());   // M1820_CRNT_DRESS_LOWER - Current: dress lower body
        buffer.append(oasisRecord.getCRNT_BATHG());   // M1830_CRNT_BATHG - Current: bathing
        buffer.append(oasisRecord.getCRNT_TOILTG());   // M1840_CRNT_TOILTG - Current: toileting
        buffer.append("  ");   // M1845_CRNT_TOILTG_HYGN - Current: toileting hygiene
        buffer.append(oasisRecord.getCRNT_TRNSFRNG());   // M1850_CRNT_TRNSFRNG - Current: transferring
        buffer.append(oasisRecord.getCRNT_AMBLTN());   // M1860_CRNT_AMBLTN - Current: ambulation
        buffer.append("  ");   // M1870_CRNT_FEEDING - Current: feeding
        buffer.append("  ");   // M1880_CRNT_PREP_LT_MEALS - Current: prepare light meals
        buffer.append("  ");   // M1890_CRNT_PHONE_USE - Current: telephone use
        buffer.append("  ");   // M1900_PRIOR_ADLIADL_SELF - Prior functioning ADL/IADL: self-care
        buffer.append("  ");   // M1900_PRIOR_ADLIADL_AMBLTN - Prior functioning ADL/IADL: ambulation
        buffer.append("  ");   // M1900_PRIOR_ADLIADL_TRNSFR - Prior functioning ADL/IADL: transfer
        buffer.append("  ");   // M1900_PRIOR_ADLIADL_HSEHOLD - Prior functioning ADL/IADL: household tasks
        buffer.append("  ");   // M1910_MLT_FCTR_FALL_RISK_ASMT - Has patient had a multi-factor fall risk asmt
        buffer.append(oasisRecord.getDRUG_RGMN_RVW());   // M2000_DRUG_RGMN_RVW - Drug regimen review
        buffer.append(" ");   // M2002_MDCTN_FLWP - Medication follow-up
        buffer.append("  ");   // M2004_MDCTN_INTRVTN - Medication intervention
        buffer.append("  ");   // M2010_HIGH_RISK_DRUG_EDCTN - Patient/caregiver high risk drug education
        buffer.append("  ");   // M2015_DRUG_EDCTN_INTRVTN - Patient/caregiver drug education intervention
        buffer.append("  ");   // M2020_CRNT_MGMT_ORAL_MDCTN - Current: management of oral medications
        buffer.append(oasisRecord.getCRNT_MGMT_INJCTN_MDCTN());   // M2030_CRNT_MGMT_INJCTN_MDCTN - Current: management of injectable medications
        buffer.append("  ");   // M2040_PRIOR_MGMT_ORAL_MDCTN - Prior med mgmt: oral medications
        buffer.append("  ");   // M2040_PRIOR_MGMT_INJCTN_MDCTN - Prior med mgmt: injectable medications
        buffer.append("  ");   // ITEM_FILLER_064 - Filler: replaces M2100_CARE_TYPE_SRC_ADL
        buffer.append("  ");   // ITEM_FILLER_065 - Filler: replaces M2100_CARE_TYPE_SRC_IADL
        buffer.append("  ");   // ITEM_FILLER_066 - Filler: replaces M2100_CARE_TYPE_SRC_MDCTN
        buffer.append("  ");   // ITEM_FILLER_067 - Filler: replaces M2100_CARE_TYPE_SRC_PRCDR
        buffer.append("  ");   // ITEM_FILLER_068 - Filler: replaces M2100_CARE_TYPE_SRC_EQUIP
        buffer.append("  ");   // ITEM_FILLER_069 - Filler: replaces M2100_CARE_TYPE_SRC_SPRVSN
        buffer.append("  ");   // ITEM_FILLER_070 - Filler: replaces M2100_CARE_TYPE_SRC_ADVCY
        buffer.append("  ");   // M2110_ADL_IADL_ASTNC_FREQ - How often recv ADL or IADL assistance from any
        buffer.append(formatTherapyNeedNum(oasisRecord.getTHER_NEED_NBR()));   // M2200_THER_NEED_NBR - Therapy need: number of visits
        buffer.append(oasisRecord.getTHER_NEED_NA());   // M2200_THER_NEED_NA - Therapy need: not applicable
        buffer.append("  ");   // M2250_PLAN_SMRY_PTNT_SPECF - Plan of care synopsis: patient specific parameters
        buffer.append("  ");   // M2250_PLAN_SMRY_DBTS_FT_CARE - Plan of care synopsis: diabetic foot care
        buffer.append("  ");   // M2250_PLAN_SMRY_FALL_PRVNT - Plan of care synopsis: falls prevention
        buffer.append("  ");   // M2250_PLAN_SMRY_DPRSN_INTRVTN - Plan of care synopsis: depression interventions
        buffer.append("  ");   // M2250_PLAN_SMRY_PAIN_INTRVTN - Plan of care synopsis: pain interventions
        buffer.append("  ");   // M2250_PLAN_SMRY_PRSULC_PRVNT - Plan of care synopsis: PU prevention
        buffer.append("  ");   // M2250_PLAN_SMRY_PRSULC_TRTMT - Plan of care synopsis: PU moist treatment
        buffer.append("  ");   // M2300_EMER_USE_AFTR_LAST_ASMT - Emergent care: use since previous OASIS
        buffer.append(" ");   // M2310_ECR_MEDICATION - Emergent care reason: medication
        buffer.append(" ");   // M2310_ECR_INJRY_BY_FALL - Emergent care reason: injury caused by fall
        buffer.append(" ");   // M2310_ECR_RSPRTRY_INFCTN - Emergent care reason: respiratory infection
        buffer.append(" ");   // M2310_ECR_RSPRTRY_OTHR - Emergent care reason: respiratory other
        buffer.append(" ");   // M2310_ECR_HRT_FAILR - Emergent care reason: heart failure
        buffer.append(" ");   // M2310_ECR_CRDC_DSRTHM - Emergent care reason: cardiac dysrhythmia
        buffer.append(" ");   // M2310_ECR_MI_CHST_PAIN - Emergent care reason: myocard infarct/chest pain
        buffer.append(" ");   // M2310_ECR_OTHR_HRT_DEASE - Emergent care reason: other heart disease
        buffer.append(" ");   // M2310_ECR_STROKE_TIA - Emergent care reason: stroke (CVA) or TIA
        buffer.append(" ");   // M2310_ECR_HYPOGLYC - Emergent care reason: hypoglycemia
        buffer.append(" ");   // M2310_ECR_GI_PRBLM - Emergent care: GI bleed/obstruct/constip/impact
        buffer.append(" ");   // M2310_ECR_DHYDRTN_MALNTR - Emergent care reason: dehydration, malnutrition
        buffer.append(" ");   // M2310_ECR_UTI - Emergent care reason: urinary tract infection
        buffer.append(" ");   // M2310_ECR_CTHTR_CMPLCTN - Emergent care reason: IV catheter infect/complic
        buffer.append(" ");   // M2310_ECR_WND_INFCTN_DTRORTN - Emergent care reason: wound infect/deterioration
        buffer.append(" ");   // M2310_ECR_UNCNTLD_PAIN - Emergent care reason: uncontrolled pain
        buffer.append(" ");   // M2310_ECR_MENTL_BHVRL_PRBLM - Emergent care reason: acute mental/behav problem
        buffer.append(" ");   // M2310_ECR_DVT_PULMNRY - Emergent care reason: deep vein thromb/pulm embol
        buffer.append(" ");   // M2310_ECR_OTHER - Emergent care reason: other than above
        buffer.append(" ");   // M2310_ECR_UNKNOWN - Emergent care reason: unknown
        buffer.append("  ");   // M2400_INTRVTN_SMRY_DBTS_FT - Intervention synopsis: diabetic foot care
        buffer.append("  ");   // M2400_INTRVTN_SMRY_FALL_PRVNT - Intervention synopsis: falls prevention
        buffer.append("  ");   // M2400_INTRVTN_SMRY_DPRSN - Intervention synopsis: depression intervention
        buffer.append("  ");   // M2400_INTRVTN_SMRY_PAIN_MNTR - Intervention synopsis: monitor and mitigate pain
        buffer.append("  ");   // M2400_INTRVTN_SMRY_PRSULC_PRVN - Intervention synopsis: prevent pressure ulcers
        buffer.append("  ");   // M2400_INTRVTN_SMRY_PRSULC_WET - Intervention synopsis: PU moist wound treatment
        buffer.append("  ");   // M2410_INPAT_FACILITY - Inpatient facility
        buffer.append("  ");   // M2420_DSCHRG_DISP - Discharge disposition
        buffer.append(" ");   // M2430_HOSP_MED - Hospitalized: medication
        buffer.append(" ");   // M2430_HOSP_INJRY_BY_FALL - Hospitalized: injury caused by fall
        buffer.append(" ");   // M2430_HOSP_RSPRTRY_INFCTN - Hospitalized: respiratory
        buffer.append(" ");   // M2430_HOSP_RSPRTRY_OTHR - Hospitalized: other respiratory
        buffer.append(" ");   // M2430_HOSP_HRT_FAILR - Hospitalized: heart failure
        buffer.append(" ");   // M2430_HOSP_CRDC_DSRTHM - Hospitalized: cardiac dysrhythmia
        buffer.append(" ");   // M2430_HOSP_MI_CHST_PAIN - Hospitalized: myocardial infarction or chest pain
        buffer.append(" ");   // M2430_HOSP_OTHR_HRT_DEASE - Hospitalized: other heart disease
        buffer.append(" ");   // M2430_HOSP_STROKE_TIA - Hospitalized: stroke (CVA) or TIA
        buffer.append(" ");   // M2430_HOSP_HYPOGLYC - Hospitalized: hypoglycemia
        buffer.append(" ");   // M2430_HOSP_GI_PRBLM - Hospitalized: GI bleed/obstruct/constip/impact
        buffer.append(" ");   // M2430_HOSP_DHYDRTN_MALNTR - Hospitalized: dehydration, malnutrition
        buffer.append(" ");   // M2430_HOSP_UR_TRACT - Hospitalized: urinary tract infection
        buffer.append(" ");   // M2430_HOSP_CTHTR_CMPLCTN - Hospitalized: IV catheter infect/complic
        buffer.append(" ");   // M2430_HOSP_WND_INFCTN - Hospitalized: wound infect/deterioration
        buffer.append(" ");   // M2430_HOSP_PAIN - Hospitalized: pain
        buffer.append(" ");   // M2430_HOSP_MENTL_BHVRL_PRBLM - Hospitalized: acute mental/behav problem
        buffer.append(" ");   // M2430_HOSP_DVT_PULMNRY - Hospitalized: deep vein thromb/pulm embol
        buffer.append(" ");   // M2430_HOSP_SCHLD_TRTMT - Hospitalized: scheduled treatment or procedure
        buffer.append(" ");   // M2430_HOSP_OTHER - Hospitalized: other
        buffer.append(" ");   // M2430_HOSP_UK - Hospitalized: UK
        buffer.append(" ");   // ITEM_FILLER_071 - Filler: replaces M2440_NH_THERAPY
        buffer.append(" ");   // ITEM_FILLER_072 - Filler: replaces M2440_NH_RESPITE
        buffer.append(" ");   // ITEM_FILLER_073 - Filler: replaces M2440_NH_HOSPICE
        buffer.append(" ");   // ITEM_FILLER_074 - Filler: replaces M2440_NH_PERMANENT
        buffer.append(" ");   // ITEM_FILLER_075 - Filler: replaces M2440_NH_UNSAFE_HOME
        buffer.append(" ");   // ITEM_FILLER_076 - Filler: replaces M2440_NH_OTHER
        buffer.append(" ");   // ITEM_FILLER_077 - Filler: replaces M2440_NH_UNKNOWN
        buffer.append("        ");   // M0903_LAST_HOME_VISIT - Date of last home visit
        buffer.append("        ");   // M0906_DC_TRAN_DTH_DT - Discharge, transfer, death date
        buffer.append("                                                                                                                                                                                                        ");   // CONTROL_ITEMS_FILLER - Control items filler
        buffer.append("        ");   // M1011_14_DAY_INP1_ICD - Inpatient stay within last 14 days: ICD code 1
        buffer.append("        ");   // M1011_14_DAY_INP2_ICD - Inpatient stay within last 14 days: ICD code 2
        buffer.append("        ");   // M1011_14_DAY_INP3_ICD - Inpatient stay within last 14 days: ICD code 3
        buffer.append("        ");   // M1011_14_DAY_INP4_ICD - Inpatient stay within last 14 days: ICD code 4
        buffer.append("        ");   // M1011_14_DAY_INP5_ICD - Inpatient stay within last 14 days: ICD code 5
        buffer.append("        ");   // M1011_14_DAY_INP6_ICD - Inpatient stay within last 14 days: ICD code 6
        buffer.append(" ");   // M1011_14_DAY_INP_NA - Inpatient stay within last 14 days: not applicable
        buffer.append("        ");   // M1017_CHGREG_ICD1 - Regimen change in past 14 days: ICD code 1
        buffer.append("        ");   // M1017_CHGREG_ICD2 - Regimen change in past 14 days: ICD code 2
        buffer.append("        ");   // M1017_CHGREG_ICD3 - Regimen change in past 14 days: ICD code 3
        buffer.append("        ");   // M1017_CHGREG_ICD4 - Regimen change in past 14 days: ICD code 4
        buffer.append("        ");   // M1017_CHGREG_ICD5 - Regimen change in past 14 days: ICD code 5
        buffer.append("        ");   // M1017_CHGREG_ICD6 - Regimen change in past 14 days: ICD code 6
        buffer.append(" ");   // M1017_CHGREG_ICD_NA - Regimen change in past 14 days: not applicable
        buffer.append(formatDxCode_C1(oasisRecord.getPRIMARY_DIAG_ICD()));   // M1021_PRIMARY_DIAG_ICD - Primary diagnosis ICD code
        buffer.append("  ");   // M1021_PRIMARY_DIAG_SEVERITY - Primary diagnosis severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG1_ICD()));   // M1023_OTH_DIAG1_ICD - Other diagnosis 1: ICD code
        buffer.append("  ");   // M1023_OTH_DIAG1_SEVERITY - Other diagnosis 1: severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG2_ICD()));   // M1023_OTH_DIAG2_ICD - Other diagnosis 2: ICD code
        buffer.append("  ");   // M1023_OTH_DIAG2_SEVERITY - Other diagnosis 2: severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG3_ICD()));   // M1023_OTH_DIAG3_ICD - Other diagnosis 3: ICD code
        buffer.append("  ");   // M1023_OTH_DIAG3_SEVERITY - Other diagnosis 3: severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG4_ICD()));   // M1023_OTH_DIAG4_ICD - Other diagnosis 4: ICD code
        buffer.append("  ");   // M1023_OTH_DIAG4_SEVERITY - Other diagnosis 4: severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG5_ICD()));   // M1023_OTH_DIAG5_ICD - Other diagnosis 5: ICD code
        buffer.append("  ");   // M1023_OTH_DIAG5_SEVERITY - Other diagnosis 5: severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_A3()));   // M1025_PMT_DIAG_ICD_A3 - Case mix diagnosis: primary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_A4()));   // M1025_PMT_DIAG_ICD_A4 - Case mix diagnosis: primary, column 4
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_B3()));   // M1025_PMT_DIAG_ICD_B3 - Case mix diagnosis: first secondary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_B4()));   // M1025_PMT_DIAG_ICD_B4 - Case mix diagnosis: first secondary, column 4
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_C3()));   // M1025_PMT_DIAG_ICD_C3 - Case mix diagnosis: second secondary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_C4()));   // M1025_PMT_DIAG_ICD_C4 - Case mix diagnosis: second secondary, column 4
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_D3()));   // M1025_PMT_DIAG_ICD_D3 - Case mix diagnosis: third secondary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_D4()));   // M1025_PMT_DIAG_ICD_D4 - Case mix diagnosis: third secondary, column 4
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_E3()));   // M1025_PMT_DIAG_ICD_E3 - Case mix diagnosis: fourth secondary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_E4()));   // M1025_PMT_DIAG_ICD_E4 - Case mix diagnosis: fourth secondary, column 4
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_F3()));   // M1025_PMT_DIAG_ICD_F3 - Case mix diagnosis: fifth secondary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_F4()));   // M1025_PMT_DIAG_ICD_F4 - Case mix diagnosis: fifth secondary, column 4
        buffer.append(" ");   // M1033_HOSP_RISK_HSTRY_FALLS - Hosp risk: 2+ falls or injury fall in past year
        buffer.append(" ");   // M1033_HOSP_RISK_WEIGHT_LOSS - Hosp risk: unintentional weight loss
        buffer.append(" ");   // M1033_HOSP_RISK_MLTPL_HOSPZTN - Hosp risk: 2+ hospitalizations in past 12 months
        buffer.append(" ");   // M1033_HOSP_RISK_MLTPL_ED_VISIT - Hosp risk: 2+ emergcy dept visits in past 6 months
        buffer.append(" ");   // M1033_HOSP_RISK_MNTL_BHV_DCLN - Hosp risk: decline mental/emotional/behav status
        buffer.append(" ");   // M1033_HOSP_RISK_COMPLIANCE - Hosp risk: difficulty with medical instructions
        buffer.append(" ");   // M1033_HOSP_RISK_6PLUS_MDCTN - Hosp risk: taking six or more medications
        buffer.append(" ");   // M1033_HOSP_RISK_CURR_EXHSTN - Hosp risk: current exhaustion
        buffer.append(" ");   // M1033_HOSP_RISK_OTHR - Hosp risk: other
        buffer.append(" ");   // M1033_HOSP_RISK_NONE_ABOVE - Hosp risk: none of the above
        buffer.append(" ");   // M1041_IN_INFLNZ_SEASON - Does episode include Oct 1 thru Mar 31
        buffer.append("  ");   // M1046_INFLNZ_RECD_CUR_SEASON - Did patient receive influenza vaccine
        buffer.append(" ");   // M1051_PPV_RCVD_AGNCY - Was pneumococcal vaccine received
        buffer.append("  ");   // M1056_PPV_RSN_NOT_RCVD_AGNCY - If pneumococcal vacc not received, state reason
        buffer.append("  ");   // M1309_NBR_NEW_WRS_PRSULC_STG2 - Number of new or worsening stage 2
        buffer.append("  ");   // M1309_NBR_NEW_WRS_PRSULC_STG3 - Number of new or worsening stage 3
        buffer.append("  ");   // M1309_NBR_NEW_WRS_PRSULC_STG4 - Number of new or worsening stage 4
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_ADL - Care mgmt, types/sources: ADL
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_IADL - Care mgmt, types/sources: IADL
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_MDCTN - Care mgmt, types/sources: med admin
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_PRCDR - Care mgmt, types/sources: med procs tx
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_EQUIP - Care mgmt, types/sources: equipment
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_SPRVSN - Care mgmt, types/sources: supervision and safety
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_ADVCY - Care mgmt, types/sources: advocacy or facilitation
        buffer.append("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      ");   // ASMT_ITEMS_FILLER - Assessment items filler
        buffer.append("                                                                                                                                                                                                                                                                                                                                                              ");   // LEGACY_ITEMS_FILLER - Discontinued OASIS-B1 and C items.
        buffer.append("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ");   // CALCULATED_ITEMS_FILLER - Calculated items filler
        buffer.append("               ");   // HHA_ASMT_INT_ID - Assessment internal ID
        buffer.append("               ");   // ORIG_ASMT_INT_ID - Original assessment ID
        buffer.append("          ");   // RES_INT_ID - Resident internal ID
        buffer.append("        ");   // ASMT_EFF_DATE - Effective date
        buffer.append("          ");   // BRANCH_IDENTIFIER - Branch internal ID
        buffer.append("          ");   // FAC_INT_ID - Facility internal ID
        buffer.append("               ");   // SUBMISSION_ID - Submission ID
        buffer.append("        ");   // SUBMISSION_DATE - Submission date
        buffer.append("        ");   // SUBMISSION_COMPLETE_DATE - Submission processing completion date
        buffer.append("                              ");   // SUBMITTING_USER_ID - Submitter user ID
        buffer.append("  ");   // RES_MATCH_CRITERIA - Resident matching criteria
        buffer.append("   ");   // RESIDENT_AGE - Age of resident on the effective date
        buffer.append(" ");   // BIRTHDATE_SUBM_IND - Birth date submit code
        buffer.append("     ");   // CALC_HIPPS_CODE - HIPPS group code: recalculated
        buffer.append("     ");   // CALC_HIPPS_VERSION - HIPPS version code: recalculated
        buffer.append("%");   // DATA_END_INDICATOR - End of data terminator code
        buffer.append("\r");   // CR - Carriage return (ASCII 013)
        buffer.append("\n");   // LF - Line feed character (ASCII 010)

        return buffer;
    }

    public String getStartDate() {
        return dateRanger.getStartDate();
    }

    public String getEndDate() {
        return dateRanger.getEndDate();
    }

    @Override
    public void setStartDate(String date) {
        dateRanger.setStartDate(date);
    }

    @Override
    public void setEndDate(String date) throws IllegalArgumentException {
        dateRanger.setEndDate(date);
    }

    /**
     * If the value of the field is not '^', then this normalizes some fields
     * that may not actually be the valid length but are valid values.
     *
     * The following fields are allowed to have values shorter than the defined
     * length
     * <ul><li>M1308_NBR_PRSULC_STG2</li>
     * <li>M1308_NBR_PRSULC_STG3</li>
     * <li>M1308_NBR_PRSULC_STG4</li>
     * <li>M1308_NSTG_DRSG</li>
     * <li>M1308_NSTG_CVRG</li>
     * <li>M1308_NSTG_DEEP_TISUE</li>
     * <li>M2200_THER_NEED_NBR - this has already been converted to an int, so
     * no other processing</li>
     * </ul>
     *
     * @param oasisRecord
     */
    public void postProcessRecord(Oasis_C1_Record_2_11 oasisRecord) {
        HomeHealthRecordUtil.justifyOasisCValues(oasisRecord);
    }

}
