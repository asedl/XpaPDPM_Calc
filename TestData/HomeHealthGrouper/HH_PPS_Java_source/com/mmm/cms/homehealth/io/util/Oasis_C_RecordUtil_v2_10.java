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
import com.mmm.cms.homehealth.io.record.Oasis_C_Record;
import static com.mmm.cms.homehealth.io.HomeHealthRecordUtil.formatDxCode_C1;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.util.OasisCalendarFormatter;
import java.text.ParseException;

/**
 * This Utility converts between the OASIS-C formatted data and the internal
 * HomeHealtRecord_C_IF data formats.
 *
 * It can be used in conjunction with the OasisRecordFactory, which queries this
 * utility to determine if a specific OASIS record format can be converted using
 * this utility.
 *
 * @see OasisReaderFactory
 * @author 3M Health Information Systems for CMS Home Health
 *
 */
public class Oasis_C_RecordUtil_v2_10
    extends AbstractRecordConverter
    implements OasisRecordConverterIF {

    protected final static int OASIS_C_FLAT_RECORD_LENGTH = 3256;
    public final static String OASIS_ITEM_ASMT_SYS_CD = "ASMT_SYS_CD";
    public final static String OASIS_ITEM_TRANS_TYPE_CD = "TRANS_TYPE_CD";
    public final static String OASIS_ITEM_ITM_SBST_CD = "ITM_SBST_CD";
    public final static String OASIS_ITEM_ITM_SET_VRSN_CD = "ITM_SET_VRSN_CD";
    public final static String OASIS_ITEM_SPEC_VRSN_CD = "SPEC_VRSN_CD";
    public final static String OASIS_ITEM_CORRECTION_NUM = "CORRECTION_NUM";
    public final static String OASIS_ITEM_STATE_CD = "STATE_CD";
    public final static String OASIS_ITEM_HHA_AGENCY_ID = "HHA_AGENCY_ID";
    public final static String OASIS_ITEM_NATL_PRVDR_ID = "NATL_PRVDR_ID";
    public final static String OASIS_ITEM_SFW_ID = "SFW_ID";
    public final static String OASIS_ITEM_SFW_NAME = "SFW_NAME";
    public final static String OASIS_ITEM_SFW_EMAIL_ADR = "SFW_EMAIL_ADR";
    public final static String OASIS_ITEM_SFW_PROD_NAME = "SFW_PROD_NAME";
    public final static String OASIS_ITEM_SFW_PROD_VRSN_CD = "SFW_PROD_VRSN_CD";
    public final static String OASIS_ITEM_ACY_DOC_CD = "ACY_DOC_CD";
    public final static String OASIS_ITEM_SUBM_HIPPS_CODE = "SUBM_HIPPS_CODE";
    public final static String OASIS_ITEM_SUBM_HIPPS_VERSION = "SUBM_HIPPS_VERSION";
    public final static String OASIS_ITEM_M0010_CCN = "M0010_CCN";
    public final static String OASIS_ITEM_M0014_BRANCH_STATE = "M0014_BRANCH_STATE";
    public final static String OASIS_ITEM_M0016_BRANCH_ID = "M0016_BRANCH_ID";
    public final static String OASIS_ITEM_M0018_PHYSICIAN_ID = "M0018_PHYSICIAN_ID";
    public final static String OASIS_ITEM_M0018_PHYSICIAN_UK = "M0018_PHYSICIAN_UK";
    public final static String OASIS_ITEM_M0020_PAT_ID = "M0020_PAT_ID";
    public final static String OASIS_ITEM_M0030_START_CARE_DT = "M0030_START_CARE_DT";
    public final static String OASIS_ITEM_M0032_ROC_DT = "M0032_ROC_DT";
    public final static String OASIS_ITEM_M0032_ROC_DT_NA = "M0032_ROC_DT_NA";
    public final static String OASIS_ITEM_M0040_PAT_FNAME = "M0040_PAT_FNAME";
    public final static String OASIS_ITEM_M0040_PAT_MI = "M0040_PAT_MI";
    public final static String OASIS_ITEM_M0040_PAT_LNAME = "M0040_PAT_LNAME";
    public final static String OASIS_ITEM_M0040_PAT_SUFFIX = "M0040_PAT_SUFFIX";
    public final static String OASIS_ITEM_M0050_PAT_ST = "M0050_PAT_ST";
    public final static String OASIS_ITEM_M0060_PAT_ZIP = "M0060_PAT_ZIP";
    public final static String OASIS_ITEM_M0063_MEDICARE_NUM = "M0063_MEDICARE_NUM";
    public final static String OASIS_ITEM_M0063_MEDICARE_NA = "M0063_MEDICARE_NA";
    public final static String OASIS_ITEM_M0064_SSN = "M0064_SSN";
    public final static String OASIS_ITEM_M0064_SSN_UK = "M0064_SSN_UK";
    public final static String OASIS_ITEM_M0065_MEDICAID_NUM = "M0065_MEDICAID_NUM";
    public final static String OASIS_ITEM_M0065_MEDICAID_NA = "M0065_MEDICAID_NA";
    public final static String OASIS_ITEM_M0066_PAT_BIRTH_DT = "M0066_PAT_BIRTH_DT";
    public final static String OASIS_ITEM_M0069_PAT_GENDER = "M0069_PAT_GENDER";
    public final static String OASIS_ITEM_M0140_ETHNIC_AI_AN = "M0140_ETHNIC_AI_AN";
    public final static String OASIS_ITEM_M0140_ETHNIC_ASIAN = "M0140_ETHNIC_ASIAN";
    public final static String OASIS_ITEM_M0140_ETHNIC_BLACK = "M0140_ETHNIC_BLACK";
    public final static String OASIS_ITEM_M0140_ETHNIC_HISP = "M0140_ETHNIC_HISP";
    public final static String OASIS_ITEM_M0140_ETHNIC_NH_PI = "M0140_ETHNIC_NH_PI";
    public final static String OASIS_ITEM_M0140_ETHNIC_WHITE = "M0140_ETHNIC_WHITE";
    public final static String OASIS_ITEM_M0150_CPAY_NONE = "M0150_CPAY_NONE";
    public final static String OASIS_ITEM_M0150_CPAY_MCARE_FFS = "M0150_CPAY_MCARE_FFS";
    public final static String OASIS_ITEM_M0150_CPAY_MCARE_HMO = "M0150_CPAY_MCARE_HMO";
    public final static String OASIS_ITEM_M0150_CPAY_MCAID_FFS = "M0150_CPAY_MCAID_FFS";
    public final static String OASIS_ITEM_M0150_CPAY_MCAID_HMO = "M0150_CPAY_MCAID_HMO";
    public final static String OASIS_ITEM_M0150_CPAY_WRKCOMP = "M0150_CPAY_WRKCOMP";
    public final static String OASIS_ITEM_M0150_CPAY_TITLEPGMS = "M0150_CPAY_TITLEPGMS";
    public final static String OASIS_ITEM_M0150_CPAY_OTH_GOVT = "M0150_CPAY_OTH_GOVT";
    public final static String OASIS_ITEM_M0150_CPAY_PRIV_INS = "M0150_CPAY_PRIV_INS";
    public final static String OASIS_ITEM_M0150_CPAY_PRIV_HMO = "M0150_CPAY_PRIV_HMO";
    public final static String OASIS_ITEM_M0150_CPAY_SELFPAY = "M0150_CPAY_SELFPAY";
    public final static String OASIS_ITEM_M0150_CPAY_OTHER = "M0150_CPAY_OTHER";
    public final static String OASIS_ITEM_M0150_CPAY_UK = "M0150_CPAY_UK";
    public final static String OASIS_ITEM_M0080_ASSESSOR_DISCIPLINE = "M0080_ASSESSOR_DISCIPLINE";
    public final static String OASIS_ITEM_M0090_INFO_COMPLETED_DT = "M0090_INFO_COMPLETED_DT";
    public final static String OASIS_ITEM_M0100_ASSMT_REASON = "M0100_ASSMT_REASON";
    public final static String OASIS_ITEM_M0102_PHYSN_ORDRD_SOCROC_DT = "M0102_PHYSN_ORDRD_SOCROC_DT";
    public final static String OASIS_ITEM_M0102_PHYSN_ORDRD_SOCROC_DT_NA = "M0102_PHYSN_ORDRD_SOCROC_DT_NA";
    public final static String OASIS_ITEM_M0104_PHYSN_RFRL_DT = "M0104_PHYSN_RFRL_DT";
    public final static String OASIS_ITEM_M0110_EPISODE_TIMING = "M0110_EPISODE_TIMING";
    public final static String OASIS_ITEM_M1000_DC_LTC_14_DA = "M1000_DC_LTC_14_DA";
    public final static String OASIS_ITEM_M1000_DC_SNF_14_DA = "M1000_DC_SNF_14_DA";
    public final static String OASIS_ITEM_M1000_DC_IPPS_14_DA = "M1000_DC_IPPS_14_DA";
    public final static String OASIS_ITEM_M1000_DC_LTCH_14_DA = "M1000_DC_LTCH_14_DA";
    public final static String OASIS_ITEM_M1000_DC_IRF_14_DA = "M1000_DC_IRF_14_DA";
    public final static String OASIS_ITEM_M1000_DC_PSYCH_14_DA = "M1000_DC_PSYCH_14_DA";
    public final static String OASIS_ITEM_M1000_DC_OTH_14_DA = "M1000_DC_OTH_14_DA";
    public final static String OASIS_ITEM_M1000_DC_NONE_14_DA = "M1000_DC_NONE_14_DA";
    public final static String OASIS_ITEM_M1005_INP_DISCHARGE_DT = "M1005_INP_DISCHARGE_DT";
    public final static String OASIS_ITEM_M1005_INP_DSCHG_UNKNOWN = "M1005_INP_DSCHG_UNKNOWN";
    public final static String OASIS_ITEM_M1010_14_DAY_INP1_ICD = "M1010_14_DAY_INP1_ICD";
    public final static String OASIS_ITEM_M1010_14_DAY_INP2_ICD = "M1010_14_DAY_INP2_ICD";
    public final static String OASIS_ITEM_M1010_14_DAY_INP3_ICD = "M1010_14_DAY_INP3_ICD";
    public final static String OASIS_ITEM_M1010_14_DAY_INP4_ICD = "M1010_14_DAY_INP4_ICD";
    public final static String OASIS_ITEM_M1010_14_DAY_INP5_ICD = "M1010_14_DAY_INP5_ICD";
    public final static String OASIS_ITEM_M1010_14_DAY_INP6_ICD = "M1010_14_DAY_INP6_ICD";
    public final static String OASIS_ITEM_M1012_INP_PRCDR1_ICD = "M1012_INP_PRCDR1_ICD";
    public final static String OASIS_ITEM_M1012_INP_PRCDR2_ICD = "M1012_INP_PRCDR2_ICD";
    public final static String OASIS_ITEM_M1012_INP_PRCDR3_ICD = "M1012_INP_PRCDR3_ICD";
    public final static String OASIS_ITEM_M1012_INP_PRCDR4_ICD = "M1012_INP_PRCDR4_ICD";
    public final static String OASIS_ITEM_M1012_INP_NA_ICD = "M1012_INP_NA_ICD";
    public final static String OASIS_ITEM_M1012_INP_UK_ICD = "M1012_INP_UK_ICD";
    public final static String OASIS_ITEM_M1016_CHGREG_ICD1 = "M1016_CHGREG_ICD1";
    public final static String OASIS_ITEM_M1016_CHGREG_ICD2 = "M1016_CHGREG_ICD2";
    public final static String OASIS_ITEM_M1016_CHGREG_ICD3 = "M1016_CHGREG_ICD3";
    public final static String OASIS_ITEM_M1016_CHGREG_ICD4 = "M1016_CHGREG_ICD4";
    public final static String OASIS_ITEM_M1016_CHGREG_ICD5 = "M1016_CHGREG_ICD5";
    public final static String OASIS_ITEM_M1016_CHGREG_ICD6 = "M1016_CHGREG_ICD6";
    public final static String OASIS_ITEM_M1016_CHGREG_ICD_NA = "M1016_CHGREG_ICD_NA";
    public final static String OASIS_ITEM_M1018_PRIOR_UR_INCON = "M1018_PRIOR_UR_INCON";
    public final static String OASIS_ITEM_M1018_PRIOR_CATH = "M1018_PRIOR_CATH";
    public final static String OASIS_ITEM_M1018_PRIOR_INTRACT_PAIN = "M1018_PRIOR_INTRACT_PAIN";
    public final static String OASIS_ITEM_M1018_PRIOR_IMPR_DECSN = "M1018_PRIOR_IMPR_DECSN";
    public final static String OASIS_ITEM_M1018_PRIOR_DISRUPTIVE = "M1018_PRIOR_DISRUPTIVE";
    public final static String OASIS_ITEM_M1018_PRIOR_MEM_LOSS = "M1018_PRIOR_MEM_LOSS";
    public final static String OASIS_ITEM_M1018_PRIOR_NONE = "M1018_PRIOR_NONE";
    public final static String OASIS_ITEM_M1018_PRIOR_NOCHG_14D = "M1018_PRIOR_NOCHG_14D";
    public final static String OASIS_ITEM_M1018_PRIOR_UNKNOWN = "M1018_PRIOR_UNKNOWN";
    public final static String OASIS_ITEM_M1020_PRIMARY_DIAG_ICD = "M1020_PRIMARY_DIAG_ICD";
    public final static String OASIS_ITEM_M1020_PRIMARY_DIAG_SEVERITY = "M1020_PRIMARY_DIAG_SEVERITY";
    public final static String OASIS_ITEM_M1022_OTH_DIAG1_ICD = "M1022_OTH_DIAG1_ICD";
    public final static String OASIS_ITEM_M1022_OTH_DIAG1_SEVERITY = "M1022_OTH_DIAG1_SEVERITY";
    public final static String OASIS_ITEM_M1022_OTH_DIAG2_ICD = "M1022_OTH_DIAG2_ICD";
    public final static String OASIS_ITEM_M1022_OTH_DIAG2_SEVERITY = "M1022_OTH_DIAG2_SEVERITY";
    public final static String OASIS_ITEM_M1022_OTH_DIAG3_ICD = "M1022_OTH_DIAG3_ICD";
    public final static String OASIS_ITEM_M1022_OTH_DIAG3_SEVERITY = "M1022_OTH_DIAG3_SEVERITY";
    public final static String OASIS_ITEM_M1022_OTH_DIAG4_ICD = "M1022_OTH_DIAG4_ICD";
    public final static String OASIS_ITEM_M1022_OTH_DIAG4_SEVERITY = "M1022_OTH_DIAG4_SEVERITY";
    public final static String OASIS_ITEM_M1022_OTH_DIAG5_ICD = "M1022_OTH_DIAG5_ICD";
    public final static String OASIS_ITEM_M1022_OTH_DIAG5_SEVERITY = "M1022_OTH_DIAG5_SEVERITY";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_A3 = "M1024_PMT_DIAG_ICD_A3";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_B3 = "M1024_PMT_DIAG_ICD_B3";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_C3 = "M1024_PMT_DIAG_ICD_C3";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_D3 = "M1024_PMT_DIAG_ICD_D3";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_E3 = "M1024_PMT_DIAG_ICD_E3";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_F3 = "M1024_PMT_DIAG_ICD_F3";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_A4 = "M1024_PMT_DIAG_ICD_A4";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_B4 = "M1024_PMT_DIAG_ICD_B4";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_C4 = "M1024_PMT_DIAG_ICD_C4";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_D4 = "M1024_PMT_DIAG_ICD_D4";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_E4 = "M1024_PMT_DIAG_ICD_E4";
    public final static String OASIS_ITEM_M1024_PMT_DIAG_ICD_F4 = "M1024_PMT_DIAG_ICD_F4";
    public final static String OASIS_ITEM_M1030_THH_IV_INFUSION = "M1030_THH_IV_INFUSION";
    public final static String OASIS_ITEM_M1030_THH_PAR_NUTRITION = "M1030_THH_PAR_NUTRITION";
    public final static String OASIS_ITEM_M1030_THH_ENT_NUTRITION = "M1030_THH_ENT_NUTRITION";
    public final static String OASIS_ITEM_M1030_THH_NONE_ABOVE = "M1030_THH_NONE_ABOVE";
    public final static String OASIS_ITEM_M1032_HOSP_RISK_RCNT_DCLN = "M1032_HOSP_RISK_RCNT_DCLN";
    public final static String OASIS_ITEM_M1032_HOSP_RISK_MLTPL_HOSPZTN = "M1032_HOSP_RISK_MLTPL_HOSPZTN";
    public final static String OASIS_ITEM_M1032_HOSP_RISK_HSTRY_FALLS = "M1032_HOSP_RISK_HSTRY_FALLS";
    public final static String OASIS_ITEM_M1032_HOSP_RISK_5PLUS_MDCTN = "M1032_HOSP_RISK_5PLUS_MDCTN";
    public final static String OASIS_ITEM_M1032_HOSP_RISK_FRAILTY = "M1032_HOSP_RISK_FRAILTY";
    public final static String OASIS_ITEM_M1032_HOSP_RISK_OTHR = "M1032_HOSP_RISK_OTHR";
    public final static String OASIS_ITEM_M1032_HOSP_RISK_NONE_ABOVE = "M1032_HOSP_RISK_NONE_ABOVE";
    public final static String OASIS_ITEM_M1034_PTNT_OVRAL_STUS = "M1034_PTNT_OVRAL_STUS";
    public final static String OASIS_ITEM_M1036_RSK_SMOKING = "M1036_RSK_SMOKING";
    public final static String OASIS_ITEM_M1036_RSK_OBESITY = "M1036_RSK_OBESITY";
    public final static String OASIS_ITEM_M1036_RSK_ALCOHOLISM = "M1036_RSK_ALCOHOLISM";
    public final static String OASIS_ITEM_M1036_RSK_DRUGS = "M1036_RSK_DRUGS";
    public final static String OASIS_ITEM_M1036_RSK_NONE = "M1036_RSK_NONE";
    public final static String OASIS_ITEM_M1036_RSK_UNKNOWN = "M1036_RSK_UNKNOWN";
    public final static String OASIS_ITEM_M1040_INFLNZ_RCVD_AGNCY = "M1040_INFLNZ_RCVD_AGNCY";
    public final static String OASIS_ITEM_M1045_INFLNZ_RSN_NOT_RCVD = "M1045_INFLNZ_RSN_NOT_RCVD";
    public final static String OASIS_ITEM_M1050_PPV_RCVD_AGNCY = "M1050_PPV_RCVD_AGNCY";
    public final static String OASIS_ITEM_M1055_PPV_RSN_NOT_RCVD_AGNCY = "M1055_PPV_RSN_NOT_RCVD_AGNCY";
    public final static String OASIS_ITEM_M1100_PTNT_LVG_STUTN = "M1100_PTNT_LVG_STUTN";
    public final static String OASIS_ITEM_M1200_VISION = "M1200_VISION";
    public final static String OASIS_ITEM_M1210_HEARG_ABLTY = "M1210_HEARG_ABLTY";
    public final static String OASIS_ITEM_M1220_UNDRSTG_VERBAL_CNTNT = "M1220_UNDRSTG_VERBAL_CNTNT";
    public final static String OASIS_ITEM_M1230_SPEECH = "M1230_SPEECH";
    public final static String OASIS_ITEM_M1240_FRML_PAIN_ASMT = "M1240_FRML_PAIN_ASMT";
    public final static String OASIS_ITEM_M1242_PAIN_FREQ_ACTVTY_MVMT = "M1242_PAIN_FREQ_ACTVTY_MVMT";
    public final static String OASIS_ITEM_M1300_PRSR_ULCR_RISK_ASMT = "M1300_PRSR_ULCR_RISK_ASMT";
    public final static String OASIS_ITEM_M1302_RISK_OF_PRSR_ULCR = "M1302_RISK_OF_PRSR_ULCR";
    public final static String OASIS_ITEM_M1306_UNHLD_STG2_PRSR_ULCR = "M1306_UNHLD_STG2_PRSR_ULCR";
    public final static String OASIS_ITEM_M1307_OLDST_STG2_AT_DSCHRG = "M1307_OLDST_STG2_AT_DSCHRG";
    public final static String OASIS_ITEM_M1307_OLDST_STG2_ONST_DT = "M1307_OLDST_STG2_ONST_DT";
    public final static String OASIS_ITEM_M1308_NBR_PRSULC_STG2 = "M1308_NBR_PRSULC_STG2";
    public final static String OASIS_ITEM_M1308_NBR_STG2_AT_SOC_ROC = "M1308_NBR_STG2_AT_SOC_ROC";
    public final static String OASIS_ITEM_M1308_NBR_PRSULC_STG3 = "M1308_NBR_PRSULC_STG3";
    public final static String OASIS_ITEM_M1308_NBR_STG3_AT_SOC_ROC = "M1308_NBR_STG3_AT_SOC_ROC";
    public final static String OASIS_ITEM_M1308_NBR_PRSULC_STG4 = "M1308_NBR_PRSULC_STG4";
    public final static String OASIS_ITEM_M1308_NBR_STG4_AT_SOC_ROC = "M1308_NBR_STG4_AT_SOC_ROC";
    public final static String OASIS_ITEM_M1308_NSTG_DRSG = "M1308_NSTG_DRSG";
    public final static String OASIS_ITEM_M1308_NSTG_DRSG_SOC_ROC = "M1308_NSTG_DRSG_SOC_ROC";
    public final static String OASIS_ITEM_M1308_NSTG_CVRG = "M1308_NSTG_CVRG";
    public final static String OASIS_ITEM_M1308_NSTG_CVRG_SOC_ROC = "M1308_NSTG_CVRG_SOC_ROC";
    public final static String OASIS_ITEM_M1308_NSTG_DEEP_TISUE = "M1308_NSTG_DEEP_TISUE";
    public final static String OASIS_ITEM_M1308_NSTG_DEEP_TISUE_SOC_ROC = "M1308_NSTG_DEEP_TISUE_SOC_ROC";
    public final static String OASIS_ITEM_M1310_PRSR_ULCR_LNGTH = "M1310_PRSR_ULCR_LNGTH";
    public final static String OASIS_ITEM_M1312_PRSR_ULCR_WDTH = "M1312_PRSR_ULCR_WDTH";
    public final static String OASIS_ITEM_M1314_PRSR_ULCR_DEPTH = "M1314_PRSR_ULCR_DEPTH";
    public final static String OASIS_ITEM_M1320_STUS_PRBLM_PRSR_ULCR = "M1320_STUS_PRBLM_PRSR_ULCR";
    public final static String OASIS_ITEM_M1322_NBR_PRSULC_STG1 = "M1322_NBR_PRSULC_STG1";
    public final static String OASIS_ITEM_M1324_STG_PRBLM_ULCER = "M1324_STG_PRBLM_ULCER";
    public final static String OASIS_ITEM_M1330_STAS_ULCR_PRSNT = "M1330_STAS_ULCR_PRSNT";
    public final static String OASIS_ITEM_M1332_NUM_STAS_ULCR = "M1332_NUM_STAS_ULCR";
    public final static String OASIS_ITEM_M1334_STUS_PRBLM_STAS_ULCR = "M1334_STUS_PRBLM_STAS_ULCR";
    public final static String OASIS_ITEM_M1340_SRGCL_WND_PRSNT = "M1340_SRGCL_WND_PRSNT";
    public final static String OASIS_ITEM_M1342_STUS_PRBLM_SRGCL_WND = "M1342_STUS_PRBLM_SRGCL_WND";
    public final static String OASIS_ITEM_M1350_LESION_OPEN_WND = "M1350_LESION_OPEN_WND";
    public final static String OASIS_ITEM_M1400_WHEN_DYSPNEIC = "M1400_WHEN_DYSPNEIC";
    public final static String OASIS_ITEM_M1410_RESPTX_OXYGEN = "M1410_RESPTX_OXYGEN";
    public final static String OASIS_ITEM_M1410_RESPTX_VENTILATOR = "M1410_RESPTX_VENTILATOR";
    public final static String OASIS_ITEM_M1410_RESPTX_AIRPRESS = "M1410_RESPTX_AIRPRESS";
    public final static String OASIS_ITEM_M1410_RESPTX_NONE = "M1410_RESPTX_NONE";
    public final static String OASIS_ITEM_M1500_SYMTM_HRT_FAILR_PTNTS = "M1500_SYMTM_HRT_FAILR_PTNTS";
    public final static String OASIS_ITEM_M1510_HRT_FAILR_NO_ACTN = "M1510_HRT_FAILR_NO_ACTN";
    public final static String OASIS_ITEM_M1510_HRT_FAILR_PHYSN_CNTCT = "M1510_HRT_FAILR_PHYSN_CNTCT";
    public final static String OASIS_ITEM_M1510_HRT_FAILR_ER_TRTMT = "M1510_HRT_FAILR_ER_TRTMT";
    public final static String OASIS_ITEM_M1510_HRT_FAILR_PHYSN_TRTMT = "M1510_HRT_FAILR_PHYSN_TRTMT";
    public final static String OASIS_ITEM_M1510_HRT_FAILR_CLNCL_INTRVTN = "M1510_HRT_FAILR_CLNCL_INTRVTN";
    public final static String OASIS_ITEM_M1510_HRT_FAILR_CARE_PLAN_CHG = "M1510_HRT_FAILR_CARE_PLAN_CHG";
    public final static String OASIS_ITEM_M1600_UTI = "M1600_UTI";
    public final static String OASIS_ITEM_M1610_UR_INCONT = "M1610_UR_INCONT";
    public final static String OASIS_ITEM_M1615_INCNTNT_TIMING = "M1615_INCNTNT_TIMING";
    public final static String OASIS_ITEM_M1620_BWL_INCONT = "M1620_BWL_INCONT";
    public final static String OASIS_ITEM_M1630_OSTOMY = "M1630_OSTOMY";
    public final static String OASIS_ITEM_M1700_COG_FUNCTION = "M1700_COG_FUNCTION";
    public final static String OASIS_ITEM_M1710_WHEN_CONFUSED = "M1710_WHEN_CONFUSED";
    public final static String OASIS_ITEM_M1720_WHEN_ANXIOUS = "M1720_WHEN_ANXIOUS";
    public final static String OASIS_ITEM_M1730_STDZ_DPRSN_SCRNG = "M1730_STDZ_DPRSN_SCRNG";
    public final static String OASIS_ITEM_M1730_PHQ2_LACK_INTRST = "M1730_PHQ2_LACK_INTRST";
    public final static String OASIS_ITEM_M1730_PHQ2_DPRSN = "M1730_PHQ2_DPRSN";
    public final static String OASIS_ITEM_M1740_BD_MEM_DEFICIT = "M1740_BD_MEM_DEFICIT";
    public final static String OASIS_ITEM_M1740_BD_IMP_DECISN = "M1740_BD_IMP_DECISN";
    public final static String OASIS_ITEM_M1740_BD_VERBAL = "M1740_BD_VERBAL";
    public final static String OASIS_ITEM_M1740_BD_PHYSICAL = "M1740_BD_PHYSICAL";
    public final static String OASIS_ITEM_M1740_BD_SOC_INAPPRO = "M1740_BD_SOC_INAPPRO";
    public final static String OASIS_ITEM_M1740_BD_DELUSIONS = "M1740_BD_DELUSIONS";
    public final static String OASIS_ITEM_M1740_BD_NONE = "M1740_BD_NONE";
    public final static String OASIS_ITEM_M1745_BEH_PROB_FREQ = "M1745_BEH_PROB_FREQ";
    public final static String OASIS_ITEM_M1750_REC_PSYCH_NURS = "M1750_REC_PSYCH_NURS";
    public final static String OASIS_ITEM_M1800_CUR_GROOMING = "M1800_CUR_GROOMING";
    public final static String OASIS_ITEM_M1810_CUR_DRESS_UPPER = "M1810_CUR_DRESS_UPPER";
    public final static String OASIS_ITEM_M1820_CUR_DRESS_LOWER = "M1820_CUR_DRESS_LOWER";
    public final static String OASIS_ITEM_M1830_CRNT_BATHG = "M1830_CRNT_BATHG";
    public final static String OASIS_ITEM_M1840_CUR_TOILTG = "M1840_CUR_TOILTG";
    public final static String OASIS_ITEM_M1845_CUR_TOILTG_HYGN = "M1845_CUR_TOILTG_HYGN";
    public final static String OASIS_ITEM_M1850_CUR_TRNSFRNG = "M1850_CUR_TRNSFRNG";
    public final static String OASIS_ITEM_M1860_CRNT_AMBLTN = "M1860_CRNT_AMBLTN";
    public final static String OASIS_ITEM_M1870_CUR_FEEDING = "M1870_CUR_FEEDING";
    public final static String OASIS_ITEM_M1880_CUR_PREP_LT_MEALS = "M1880_CUR_PREP_LT_MEALS";
    public final static String OASIS_ITEM_M1890_CUR_PHONE_USE = "M1890_CUR_PHONE_USE";
    public final static String OASIS_ITEM_M1900_PRIOR_ADLIADL_SELF = "M1900_PRIOR_ADLIADL_SELF";
    public final static String OASIS_ITEM_M1900_PRIOR_ADLIADL_AMBLTN = "M1900_PRIOR_ADLIADL_AMBLTN";
    public final static String OASIS_ITEM_M1900_PRIOR_ADLIADL_TRNSFR = "M1900_PRIOR_ADLIADL_TRNSFR";
    public final static String OASIS_ITEM_M1900_PRIOR_ADLIADL_HSEHOLD = "M1900_PRIOR_ADLIADL_HSEHOLD";
    public final static String OASIS_ITEM_M1910_MLT_FCTR_FALL_RISK_ASMT = "M1910_MLT_FCTR_FALL_RISK_ASMT";
    public final static String OASIS_ITEM_M2000_DRUG_RGMN_RVW = "M2000_DRUG_RGMN_RVW";
    public final static String OASIS_ITEM_M2002_MDCTN_FLWP = "M2002_MDCTN_FLWP";
    public final static String OASIS_ITEM_M2004_MDCTN_INTRVTN = "M2004_MDCTN_INTRVTN";
    public final static String OASIS_ITEM_M2010_HIGH_RISK_DRUG_EDCTN = "M2010_HIGH_RISK_DRUG_EDCTN";
    public final static String OASIS_ITEM_M2015_DRUG_EDCTN_INTRVTN = "M2015_DRUG_EDCTN_INTRVTN";
    public final static String OASIS_ITEM_M2020_CRNT_MGMT_ORAL_MDCTN = "M2020_CRNT_MGMT_ORAL_MDCTN";
    public final static String OASIS_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN = "M2030_CRNT_MGMT_INJCTN_MDCTN";
    public final static String OASIS_ITEM_M2040_PRIOR_MGMT_ORAL_MDCTN = "M2040_PRIOR_MGMT_ORAL_MDCTN";
    public final static String OASIS_ITEM_M2040_PRIOR_MGMT_INJCTN_MDCTN = "M2040_PRIOR_MGMT_INJCTN_MDCTN";
    public final static String OASIS_ITEM_M2100_CARE_TYPE_SRC_ADL = "M2100_CARE_TYPE_SRC_ADL";
    public final static String OASIS_ITEM_M2100_CARE_TYPE_SRC_IADL = "M2100_CARE_TYPE_SRC_IADL";
    public final static String OASIS_ITEM_M2100_CARE_TYPE_SRC_MDCTN = "M2100_CARE_TYPE_SRC_MDCTN";
    public final static String OASIS_ITEM_M2100_CARE_TYPE_SRC_PRCDR = "M2100_CARE_TYPE_SRC_PRCDR";
    public final static String OASIS_ITEM_M2100_CARE_TYPE_SRC_EQUIP = "M2100_CARE_TYPE_SRC_EQUIP";
    public final static String OASIS_ITEM_M2100_CARE_TYPE_SRC_SPRVSN = "M2100_CARE_TYPE_SRC_SPRVSN";
    public final static String OASIS_ITEM_M2100_CARE_TYPE_SRC_ADVCY = "M2100_CARE_TYPE_SRC_ADVCY";
    public final static String OASIS_ITEM_M2110_ADL_IADL_ASTNC_FREQ = "M2110_ADL_IADL_ASTNC_FREQ";
    public final static String OASIS_ITEM_M2200_THER_NEED_NUM = "M2200_THER_NEED_NUM";
    public final static String OASIS_ITEM_M2200_THER_NEED_NA = "M2200_THER_NEED_NA";
    public final static String OASIS_ITEM_M2250_PLAN_SMRY_PTNT_SPECF = "M2250_PLAN_SMRY_PTNT_SPECF";
    public final static String OASIS_ITEM_M2250_PLAN_SMRY_DBTS_FT_CARE = "M2250_PLAN_SMRY_DBTS_FT_CARE";
    public final static String OASIS_ITEM_M2250_PLAN_SMRY_FALL_PRVNT = "M2250_PLAN_SMRY_FALL_PRVNT";
    public final static String OASIS_ITEM_M2250_PLAN_SMRY_DPRSN_INTRVTN = "M2250_PLAN_SMRY_DPRSN_INTRVTN";
    public final static String OASIS_ITEM_M2250_PLAN_SMRY_PAIN_INTRVTN = "M2250_PLAN_SMRY_PAIN_INTRVTN";
    public final static String OASIS_ITEM_M2250_PLAN_SMRY_PRSULC_PRVNT = "M2250_PLAN_SMRY_PRSULC_PRVNT";
    public final static String OASIS_ITEM_M2250_PLAN_SMRY_PRSULC_TRTMT = "M2250_PLAN_SMRY_PRSULC_TRTMT";
    public final static String OASIS_ITEM_M2300_EMER_USE_AFTR_LAST_ASMT = "M2300_EMER_USE_AFTR_LAST_ASMT";
    public final static String OASIS_ITEM_M2310_ECR_MEDICATION = "M2310_ECR_MEDICATION";
    public final static String OASIS_ITEM_M2310_ECR_INJRY_BY_FALL = "M2310_ECR_INJRY_BY_FALL";
    public final static String OASIS_ITEM_M2310_ECR_RSPRTRY_INFCTN = "M2310_ECR_RSPRTRY_INFCTN";
    public final static String OASIS_ITEM_M2310_ECR_RSPRTRY_OTHR = "M2310_ECR_RSPRTRY_OTHR";
    public final static String OASIS_ITEM_M2310_ECR_HRT_FAILR = "M2310_ECR_HRT_FAILR";
    public final static String OASIS_ITEM_M2310_ECR_CRDC_DSRTHM = "M2310_ECR_CRDC_DSRTHM";
    public final static String OASIS_ITEM_M2310_ECR_MI_CHST_PAIN = "M2310_ECR_MI_CHST_PAIN";
    public final static String OASIS_ITEM_M2310_ECR_OTHR_HRT_DEASE = "M2310_ECR_OTHR_HRT_DEASE";
    public final static String OASIS_ITEM_M2310_ECR_STROKE_TIA = "M2310_ECR_STROKE_TIA";
    public final static String OASIS_ITEM_M2310_ECR_HYPOGLYC = "M2310_ECR_HYPOGLYC";
    public final static String OASIS_ITEM_M2310_ECR_GI_PRBLM = "M2310_ECR_GI_PRBLM";
    public final static String OASIS_ITEM_M2310_ECR_DHYDRTN_MALNTR = "M2310_ECR_DHYDRTN_MALNTR";
    public final static String OASIS_ITEM_M2310_ECR_UTI = "M2310_ECR_UTI";
    public final static String OASIS_ITEM_M2310_ECR_CTHTR_CMPLCTN = "M2310_ECR_CTHTR_CMPLCTN";
    public final static String OASIS_ITEM_M2310_ECR_WND_INFCTN_DTRORTN = "M2310_ECR_WND_INFCTN_DTRORTN";
    public final static String OASIS_ITEM_M2310_ECR_UNCNTLD_PAIN = "M2310_ECR_UNCNTLD_PAIN";
    public final static String OASIS_ITEM_M2310_ECR_MENTL_BHVRL_PRBLM = "M2310_ECR_MENTL_BHVRL_PRBLM";
    public final static String OASIS_ITEM_M2310_ECR_DVT_PULMNRY = "M2310_ECR_DVT_PULMNRY";
    public final static String OASIS_ITEM_M2310_ECR_OTHER = "M2310_ECR_OTHER";
    public final static String OASIS_ITEM_M2310_ECR_UNKNOWN = "M2310_ECR_UNKNOWN";
    public final static String OASIS_ITEM_M2400_INTRVTN_SMRY_DBTS_FT = "M2400_INTRVTN_SMRY_DBTS_FT";
    public final static String OASIS_ITEM_M2400_INTRVTN_SMRY_FALL_PRVNT = "M2400_INTRVTN_SMRY_FALL_PRVNT";
    public final static String OASIS_ITEM_M2400_INTRVTN_SMRY_DPRSN = "M2400_INTRVTN_SMRY_DPRSN";
    public final static String OASIS_ITEM_M2400_INTRVTN_SMRY_PAIN_MNTR = "M2400_INTRVTN_SMRY_PAIN_MNTR";
    public final static String OASIS_ITEM_M2400_INTRVTN_SMRY_PRSULC_PRVN = "M2400_INTRVTN_SMRY_PRSULC_PRVN";
    public final static String OASIS_ITEM_M2400_INTRVTN_SMRY_PRSULC_WET = "M2400_INTRVTN_SMRY_PRSULC_WET";
    public final static String OASIS_ITEM_M2410_INPAT_FACILITY = "M2410_INPAT_FACILITY";
    public final static String OASIS_ITEM_M2420_DSCHRG_DISP = "M2420_DSCHRG_DISP";
    public final static String OASIS_ITEM_M2430_HOSP_MED = "M2430_HOSP_MED";
    public final static String OASIS_ITEM_M2430_HOSP_INJRY_BY_FALL = "M2430_HOSP_INJRY_BY_FALL";
    public final static String OASIS_ITEM_M2430_HOSP_RSPRTRY_INFCTN = "M2430_HOSP_RSPRTRY_INFCTN";
    public final static String OASIS_ITEM_M2430_HOSP_RSPRTRY_OTHR = "M2430_HOSP_RSPRTRY_OTHR";
    public final static String OASIS_ITEM_M2430_HOSP_HRT_FAILR = "M2430_HOSP_HRT_FAILR";
    public final static String OASIS_ITEM_M2430_HOSP_CRDC_DSRTHM = "M2430_HOSP_CRDC_DSRTHM";
    public final static String OASIS_ITEM_M2430_HOSP_MI_CHST_PAIN = "M2430_HOSP_MI_CHST_PAIN";
    public final static String OASIS_ITEM_M2430_HOSP_OTHR_HRT_DEASE = "M2430_HOSP_OTHR_HRT_DEASE";
    public final static String OASIS_ITEM_M2430_HOSP_STROKE_TIA = "M2430_HOSP_STROKE_TIA";
    public final static String OASIS_ITEM_M2430_HOSP_HYPOGLYC = "M2430_HOSP_HYPOGLYC";
    public final static String OASIS_ITEM_M2430_HOSP_GI_PRBLM = "M2430_HOSP_GI_PRBLM";
    public final static String OASIS_ITEM_M2430_HOSP_DHYDRTN_MALNTR = "M2430_HOSP_DHYDRTN_MALNTR";
    public final static String OASIS_ITEM_M2430_HOSP_UR_TRACT = "M2430_HOSP_UR_TRACT";
    public final static String OASIS_ITEM_M2430_HOSP_CTHTR_CMPLCTN = "M2430_HOSP_CTHTR_CMPLCTN";
    public final static String OASIS_ITEM_M2430_HOSP_WND_INFCTN = "M2430_HOSP_WND_INFCTN";
    public final static String OASIS_ITEM_M2430_HOSP_PAIN = "M2430_HOSP_PAIN";
    public final static String OASIS_ITEM_M2430_HOSP_MENTL_BHVRL_PRBLM = "M2430_HOSP_MENTL_BHVRL_PRBLM";
    public final static String OASIS_ITEM_M2430_HOSP_DVT_PULMNRY = "M2430_HOSP_DVT_PULMNRY";
    public final static String OASIS_ITEM_M2430_HOSP_SCHLD_TRTMT = "M2430_HOSP_SCHLD_TRTMT";
    public final static String OASIS_ITEM_M2430_HOSP_OTHER = "M2430_HOSP_OTHER";
    public final static String OASIS_ITEM_M2430_HOSP_UK = "M2430_HOSP_UK";
    public final static String OASIS_ITEM_M2440_NH_THERAPY = "M2440_NH_THERAPY";
    public final static String OASIS_ITEM_M2440_NH_RESPITE = "M2440_NH_RESPITE";
    public final static String OASIS_ITEM_M2440_NH_HOSPICE = "M2440_NH_HOSPICE";
    public final static String OASIS_ITEM_M2440_NH_PERMANENT = "M2440_NH_PERMANENT";
    public final static String OASIS_ITEM_M2440_NH_UNSAFE_HOME = "M2440_NH_UNSAFE_HOME";
    public final static String OASIS_ITEM_M2440_NH_OTHER = "M2440_NH_OTHER";
    public final static String OASIS_ITEM_M2440_NH_UNKNOWN = "M2440_NH_UNKNOWN";
    public final static String OASIS_ITEM_M0903_LAST_HOME_VISIT = "M0903_LAST_HOME_VISIT";
    public final static String OASIS_ITEM_M0906_DC_TRAN_DTH_DT = "M0906_DC_TRAN_DTH_DT";
    public final static String OASIS_ITEM_CONTROL_ITEMS_FILLER = "CONTROL_ITEMS_FILLER";
    public final static String OASIS_ITEM_ASMT_ITEMS_FILLER = "ASMT_ITEMS_FILLER";
    public final static String OASIS_ITEM_LEGACY_ITEMS_FILLER = "LEGACY_ITEMS_FILLER";
    public final static String OASIS_ITEM_CALCULATED_ITEMS_FILLER = "CALCULATED_ITEMS_FILLER";
    public final static String OASIS_ITEM_HHA_ASMT_INT_ID = "HHA_ASMT_INT_ID";
    public final static String OASIS_ITEM_ORIG_ASMT_INT_ID = "ORIG_ASMT_INT_ID";
    public final static String OASIS_ITEM_RES_INT_ID = "RES_INT_ID";
    public final static String OASIS_ITEM_ASMT_EFF_DATE = "ASMT_EFF_DATE";
    public final static String OASIS_ITEM_BRANCH_IDENTIFIER = "BRANCH_IDENTIFIER";
    public final static String OASIS_ITEM_FAC_INT_ID = "FAC_INT_ID";
    public final static String OASIS_ITEM_SUBMISSION_ID = "SUBMISSION_ID";
    public final static String OASIS_ITEM_SUBMISSION_DATE = "SUBMISSION_DATE";
    public final static String OASIS_ITEM_SUBMISSION_COMPLETE_DATE = "SUBMISSION_COMPLETE_DATE";
    public final static String OASIS_ITEM_SUBMITTING_USER_ID = "SUBMITTING_USER_ID";
    public final static String OASIS_ITEM_RES_MATCH_CRITERIA = "RES_MATCH_CRITERIA";
    public final static String OASIS_ITEM_RESIDENT_AGE = "RESIDENT_AGE";
    public final static String OASIS_ITEM_BIRTHDATE_SUBM_IND = "BIRTHDATE_SUBM_IND";
    public final static String OASIS_ITEM_CALC_HIPPS_CODE = "CALC_HIPPS_CODE";
    public final static String OASIS_ITEM_CALC_HIPPS_VERSION = "CALC_HIPPS_VERSION";
    public final static String OASIS_ITEM_DATA_END_INDICATOR = "DATA_END_INDICATOR";
    public final static String OASIS_ITEM_CR = "CR";
    public final static String OASIS_ITEM_LF = "LF";

    public Oasis_C_RecordUtil_v2_10() {
        super("20100101", "20141231", OASIS_C_FLAT_RECORD_LENGTH, "2.10");
    }

    /**
     * get the M0090 date at location: 435-442, inclusive
     * @param record
     * @return 
     */
    @Override
    protected String getRecordDate(String record) {
        return record.substring(434, 442);
    }

    /**
     * gets the version CD at location: 25-34, inclusive
     * @param record
     * @return 
     */
    @Override
    protected String getVersionCD(String record) {
        return record.substring(24, 34);
    }

    public StringBuilder convertFromHomeHealthRecDelimeted(
            HomeHealthRecordIF homeHealthRecord,
            String delimiter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum, boolean skipPassthru) throws ParseException {
        final Oasis_C_Record oasisRecord = new Oasis_C_Record();

        // validate that this is an Body record
        if (strRecord == null) {
            throw new ParseException("OASIS record string can not be null", 0);
        } else if (!isRecordConvertable(strRecord)) {
            if (strRecord.length() < OASIS_C_FLAT_RECORD_LENGTH) {
                throw new ParseException("Unknown record due to invalid length of "
                        + strRecord.length() + ", should be at least " + OASIS_C_FLAT_RECORD_LENGTH + "  characters.", 0);
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
        oasisRecord.setASSMT_REASON(strRecord.substring(442, 444));   // M0100_ASSMT_REASON - Reason for assessment
        // M0102_PHYSN_ORDRD_SOCROC_DT - Physician ordered SOC/ROC date
        // M0102_PHYSN_ORDRD_SOCROC_DT_NA - Physician ordered SOC/ROC date - NA
        // M0104_PHYSN_RFRL_DT - Physician date of referral
        oasisRecord.setEPISODE_TIMING(strRecord.substring(461, 463));   // M0110_EPISODE_TIMING - Episode timing
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
        // M1010_14_DAY_INP1_ICD - Inpatient stay within last 14 days: ICD code 1
        // M1010_14_DAY_INP2_ICD - Inpatient stay within last 14 days: ICD code 2
        // M1010_14_DAY_INP3_ICD - Inpatient stay within last 14 days: ICD code 3
        // M1010_14_DAY_INP4_ICD - Inpatient stay within last 14 days: ICD code 4
        // M1010_14_DAY_INP5_ICD - Inpatient stay within last 14 days: ICD code 5
        // M1010_14_DAY_INP6_ICD - Inpatient stay within last 14 days: ICD code 6
        // M1012_INP_PRCDR1_ICD - Inpatient ICD procedure1 code
        // M1012_INP_PRCDR2_ICD - Inpatient ICD procedure2 code
        // M1012_INP_PRCDR3_ICD - Inpatient ICD procedure3 code
        // M1012_INP_PRCDR4_ICD - Inpatient ICD procedure4 code
        // M1012_INP_NA_ICD - Inpatient ICD procedure code - NA
        // M1012_INP_UK_ICD - Inpatient ICD procedure code - UK
        // M1016_CHGREG_ICD1 - Regimen change in past 14 days: ICD code 1
        // M1016_CHGREG_ICD2 - Regimen change in past 14 days: ICD code 2
        // M1016_CHGREG_ICD3 - Regimen change in past 14 days: ICD code 3
        // M1016_CHGREG_ICD4 - Regimen change in past 14 days: ICD code 4
        // M1016_CHGREG_ICD5 - Regimen change in past 14 days: ICD code 5
        // M1016_CHGREG_ICD6 - Regimen change in past 14 days: ICD code 6
        // M1016_CHGREG_ICD_NA - Regimen change in past 14 days: not applicable
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
        oasisRecord.setTHH_IV_INFUSION(strRecord.substring(742, 743));   // M1030_THH_IV_INFUSION - Therapies received at home: intravenous, infusion
        oasisRecord.setTHH_PAR_NUTRITION(strRecord.substring(743, 744));   // M1030_THH_PAR_NUTRITION - Therapies received at home: parenteral nutrition
        oasisRecord.setTHH_ENT_NUTRITION(strRecord.substring(744, 745));   // M1030_THH_ENT_NUTRITION - Therapies received at home: enteral nutrition
        oasisRecord.setTHH_NONE_ABOVE(strRecord.substring(745, 746));   // M1030_THH_NONE_ABOVE - Therapies received at home: none of the above
        // M1032_HOSP_RISK_RCNT_DCLN - Hosp risk: decline mental/emotional/behav status
        // M1032_HOSP_RISK_MLTPL_HOSPZTN - Hosp risk: 2+ hospitalizations in past 12 months
        // M1032_HOSP_RISK_HSTRY_FALLS - Hosp risk: 2+ falls or injury fall in past year
        // M1032_HOSP_RISK_5PLUS_MDCTN - Hosp risk: taking five or more medications
        // M1032_HOSP_RISK_FRAILTY - Hosp risk: frailty indicators
        // M1032_HOSP_RISK_OTHR - Hosp risk: other
        // M1032_HOSP_RISK_NONE_ABOVE - Hosp risk: none of the above
        // M1034_PTNT_OVRAL_STUS - Patient's overall status
        // M1036_RSK_SMOKING - High risk factor: smoking
        // M1036_RSK_OBESITY - High risk factor: obesity
        // M1036_RSK_ALCOHOLISM - High risk factor: alcoholism
        // M1036_RSK_DRUGS - High risk factor: drugs
        // M1036_RSK_NONE - High risk factor: none of the above
        // M1036_RSK_UNKNOWN - High risk factor: unknown
        // M1040_INFLNZ_RCVD_AGNCY - Was influenza vaccine received
        // M1045_INFLNZ_RSN_NOT_RCVD - If influenza vaccine not received, state reason
        // M1050_PPV_RCVD_AGNCY - Was pneumococcal vaccine received
        // M1055_PPV_RSN_NOT_RCVD_AGNCY - If pneumococcal vacc not received, state reason
        // M1100_PTNT_LVG_STUTN - Patient living situation
        oasisRecord.setVISION(strRecord.substring(770, 772));   // M1200_VISION - Sensory status: vision
        // M1210_HEARG_ABLTY - Ability to hear
        // M1220_UNDRSTG_VERBAL_CNTNT - Understanding verbal content in patient's language
        // M1230_SPEECH - Sensory status: speech
        // M1240_FRML_PAIN_ASMT - Has patient had a formal pain assessment
        oasisRecord.setPAIN_FREQ_ACTVTY_MVMT(strRecord.substring(780, 782));   // M1242_PAIN_FREQ_ACTVTY_MVMT - Freq of pain interfering with pt activity/movement
        // M1300_PRSR_ULCR_RISK_ASMT - Was patient assessed for risk of developing pus
        // M1302_RISK_OF_PRSR_ULCR - Does this patient have a risk of developing pus
        oasisRecord.setUNHLD_STG2_PRSR_ULCR(strRecord.substring(785, 786));   // M1306_UNHLD_STG2_PRSR_ULCR - Patient has 1+ unhealed PU at stage 2 or higher
        // M1307_OLDST_STG2_AT_DSCHRG - Status of oldest stage 2 pressure ulcer at disch
        // M1307_OLDST_STG2_ONST_DT - Date of onset of oldest stage 2 pressure ulcer
        oasisRecord.setNBR_PRSULC_STG2(strRecord.substring(796, 798));   // M1308_NBR_PRSULC_STG2 - No. pressure ulcers - stage 2
        oasisRecord.setNBR_STG2_AT_SOC_ROC(strRecord.substring(798, 800));   // M1308_NBR_STG2_AT_SOC_ROC - Number pu stage 2 at SOC/ROC
        oasisRecord.setNBR_PRSULC_STG3(strRecord.substring(800, 802));   // M1308_NBR_PRSULC_STG3 - No. pressure ulcers - stage 3
        oasisRecord.setNBR_STG3_AT_SOC_ROC(strRecord.substring(802, 804));   // M1308_NBR_STG3_AT_SOC_ROC - Number PU stage 3 at SOC/ROC
        oasisRecord.setNBR_PRSULC_STG4(strRecord.substring(804, 806));   // M1308_NBR_PRSULC_STG4 - No. pressure ulcers - stage 4
        oasisRecord.setNBR_STG4_AT_SOC_ROC(strRecord.substring(806, 808));   // M1308_NBR_STG4_AT_SOC_ROC - Number PU stage 4 at SOC/ROC
        oasisRecord.setNSTG_DRSG(strRecord.substring(808, 810));   // M1308_NSTG_DRSG - Unstageable: non-removable dressing/device
        oasisRecord.setNSTG_DRSG_SOC_ROC(strRecord.substring(810, 812));   // M1308_NSTG_DRSG_SOC_ROC - Unstageable: non-removable dressing/device-SOC/ROC
        oasisRecord.setNSTG_CVRG(strRecord.substring(812, 814));   // M1308_NSTG_CVRG - Unstageable: coverage by slough or eschar
        oasisRecord.setNSTG_CVRG_SOC_ROC(strRecord.substring(814, 816));   // M1308_NSTG_CVRG_SOC_ROC - Unstageable: coverage by slough or eschar-SOC/ROC
        oasisRecord.setNSTG_DEEP_TISUE(strRecord.substring(816, 818));   // M1308_NSTG_DEEP_TISUE - Unstageable: suspect deep tissue injury
        oasisRecord.setNSTG_DEEP_TISSUE_SOC_ROC(strRecord.substring(818, 820));// M1308_NSTG_DEEP_TISUE_SOC_ROC - Unstageable: suspect deep tissue injury-SOC/ROC
        oasisRecord.setPRSR_ULCR_LNGTH(strRecord.substring(820, 824));// M1310_PRSR_ULCR_LNGTH - Length of stage 3 or 4 PU with largest area
        oasisRecord.setPRSR_ULCR_WDTH(strRecord.substring(824, 828));// M1312_PRSR_ULCR_WDTH - Width of stage 3 or 4 PU with largest area
        oasisRecord.setPRSR_ULCR_DEPTH(strRecord.substring(828, 832));// M1314_PRSR_ULCR_DEPTH - Depth of stage 3 or 4 PU with largest area
        oasisRecord.setSTUS_PRBLM_PRSR_ULCR(strRecord.substring(832, 834));   // M1320_STUS_PRBLM_PRSR_ULCR - Status of most problematic pressure ulcer
        oasisRecord.setNBR_PRSULC_STG1(strRecord.substring(834, 836));   // M1322_NBR_PRSULC_STG1 - No. pressure ulcers - stage 1
        oasisRecord.setSTG_PRBLM_ULCER(strRecord.substring(836, 838));   // M1324_STG_PRBLM_ULCER - Stage of most problematic pressure ulcer
        oasisRecord.setSTAS_ULCR_PRSNT(strRecord.substring(838, 840));   // M1330_STAS_ULCR_PRSNT - Does this patient have a stasis ulcer
        oasisRecord.setNBR_STAS_ULCR(strRecord.substring(840, 842));// M1332_NUM_STAS_ULCR - No. stasis ulcers
        oasisRecord.setSTUS_PRBLM_STAS_ULCR(strRecord.substring(842, 844));   // M1334_STUS_PRBLM_STAS_ULCR - Status of most problematic stasis ulcer
        oasisRecord.setSRGCL_WND_PRSNT(strRecord.substring(844, 846));   // M1340_SRGCL_WND_PRSNT - Does this patient have a surgical wound
        oasisRecord.setSTUS_PRBLM_SRGCL_WND(strRecord.substring(846, 848));   // M1342_STUS_PRBLM_SRGCL_WND - Status of most problematic surgical wound
        oasisRecord.setLESION_OPEN_WND(strRecord.substring(848, 849));   // M1350_LESION_OPEN_WND - Has skin lesion or open wound
        oasisRecord.setWHEN_DYSPNEIC(strRecord.substring(849, 851));   // M1400_WHEN_DYSPNEIC - When dyspneic
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
        oasisRecord.setUR_INCONT(strRecord.substring(865, 867));   // M1610_UR_INCONT - Urinary incontinence or urinary catheter present
        oasisRecord.setINCNTNT_TIMING(strRecord.substring(867, 869));   // M1615_INCNTNT_TIMING - When urinary incontinence occurs
        oasisRecord.setBWL_INCONT(strRecord.substring(869, 871));   // M1620_BWL_INCONT - Bowel incontinence frequency
        oasisRecord.setOSTOMY(strRecord.substring(871, 873));   // M1630_OSTOMY - Ostomy for bowel elimination
        // M1700_COG_FUNCTION - Cognitive functioning
        // M1710_WHEN_CONFUSED - When confused (reported or observed)
        // M1720_WHEN_ANXIOUS - When anxious (reported or observed)
        // M1730_STDZ_DPRSN_SCRNG - Has the patient been screened for depression
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
        // M1800_CUR_GROOMING - Current: grooming
        oasisRecord.setCRNT_DRESS_UPPER(strRecord.substring(897, 899));// M1810_CUR_DRESS_UPPER - Current: dress upper body
        oasisRecord.setCRNT_DRESS_LOWER(strRecord.substring(899, 901));// M1820_CUR_DRESS_LOWER - Current: dress lower body
        oasisRecord.setCRNT_BATHG(strRecord.substring(901, 903));   // M1830_CRNT_BATHG - Current: bathing
        oasisRecord.setCRNT_TOILTG(strRecord.substring(903, 905));// M1840_CUR_TOILTG - Current: toileting
        // M1845_CUR_TOILTG_HYGN - Current: toileting hygiene
        oasisRecord.setCRNT_TRNSFRNG(strRecord.substring(907, 909));// M1850_CUR_TRNSFRNG - Current: transferring
        oasisRecord.setCRNT_AMBLTN(strRecord.substring(909, 911));   // M1860_CRNT_AMBLTN - Current: ambulation
        // M1870_CUR_FEEDING - Current: feeding
        // M1880_CUR_PREP_LT_MEALS - Current: prepare light meals
        // M1890_CUR_PHONE_USE - Current: telephone use
        // M1900_PRIOR_ADLIADL_SELF - Prior functioning ADL/IADL: self-care
        // M1900_PRIOR_ADLIADL_AMBLTN - Prior functioning ADL/IADL: ambulation
        // M1900_PRIOR_ADLIADL_TRNSFR - Prior functioning ADL/IADL: transfer
        // M1900_PRIOR_ADLIADL_HSEHOLD - Prior functioning ADL/IADL: household tasks
        // M1910_MLT_FCTR_FALL_RISK_ASMT - Has patient had a multi-factor fall risk asmt
        oasisRecord.setDRUG_RGMN_RVW(strRecord.substring(927, 929));   // M2000_DRUG_RGMN_RVW - Drug regimen review
        // M2002_MDCTN_FLWP - Medication follow-up
        // M2004_MDCTN_INTRVTN - Medication intervention
        // M2010_HIGH_RISK_DRUG_EDCTN - Patient/caregiver high risk drug education
        // M2015_DRUG_EDCTN_INTRVTN - Patient/caregiver drug education intervention
        // M2020_CRNT_MGMT_ORAL_MDCTN - Current: management of oral medications
        oasisRecord.setCRNT_MGMT_INJCTN_MDCTN(strRecord.substring(938, 940));   // M2030_CRNT_MGMT_INJCTN_MDCTN - Current: management of injectable medications
        // M2040_PRIOR_MGMT_ORAL_MDCTN - Prior med mgmt: oral medications
        // M2040_PRIOR_MGMT_INJCTN_MDCTN - Prior med mgmt: injectable medications
        // M2100_CARE_TYPE_SRC_ADL - Care mgmt, types/sources: ADL
        // M2100_CARE_TYPE_SRC_IADL - Care mgmt, types/sources: IADL
        // M2100_CARE_TYPE_SRC_MDCTN - Care mgmt, types/sources: med admin
        // M2100_CARE_TYPE_SRC_PRCDR - Care mgmt, types/sources: med procs tx
        // M2100_CARE_TYPE_SRC_EQUIP - Care mgmt, types/sources: equipment
        // M2100_CARE_TYPE_SRC_SPRVSN - Care mgmt, types/sources: supervision and safety
        // M2100_CARE_TYPE_SRC_ADVCY - Care mgmt, types/sources: advocacy or facilitation
        // M2110_ADL_IADL_ASTNC_FREQ - How often recv ADL or IADL assistance from any
        oasisRecord.setTHER_NEED_NBR(HomeHealthRecordUtil.parseTherapyNeedNumber_C1(strRecord.substring(960, 963), -1));// M2200_THER_NEED_NUM - Therapy need: number of visits
        oasisRecord.setTHER_NEED_NA(strRecord.substring(963, 964));   // M2200_THER_NEED_NA - Therapy need: not applicable
        // M2250_PLAN_SMRY_PTNT_SPECF - Plan of care synopsis: patient specific parameters
        // M2250_PLAN_SMRY_DBTS_FT_CARE - Plan of care synopsis: diabetic foot care
        // M2250_PLAN_SMRY_FALL_PRVNT - Plan of care synopsis: falls prevention
        // M2250_PLAN_SMRY_DPRSN_INTRVTN - Plan of care synopsis: depression interventions
        // M2250_PLAN_SMRY_PAIN_INTRVTN - Plan of care synopsis: pain interventions
        // M2250_PLAN_SMRY_PRSULC_PRVNT - Plan of care synopsis: PU prevention
        // M2250_PLAN_SMRY_PRSULC_TRTMT - Plan of care synopsis: PU moist treatment
        // M2300_EMER_USE_AFTR_LAST_ASMT - Emergent care: use since last OASIS
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
        // M2440_NH_THERAPY - Admitted to nursing home: therapy
        // M2440_NH_RESPITE - Admitted to nursing home: respite
        // M2440_NH_HOSPICE - Admitted to nursing home: hospice
        // M2440_NH_PERMANENT - Admitted to nursing home: permanent placement
        // M2440_NH_UNSAFE_HOME - Admitted to nursing home: unsafe at home
        // M2440_NH_OTHER - Admitted to nursing home: other
        // M2440_NH_UNKNOWN - Admitted to nursing home: unknown
        // M0903_LAST_HOME_VISIT - Date of last home visit
        // M0906_DC_TRAN_DTH_DT - Discharge, transfer, death date
        // CONTROL_ITEMS_FILLER - Control items filler
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

        /*
         * convvert any caret prefixed values to be space prefixed
         */
        postProcessRecord(oasisRecord);

        return oasisRecord;
    }

    public StringBuilder convertFromHomeHealthToFlatRecord(HomeHealthRecord_C_IF oasisRecord) {
        StringBuilder buffer = new StringBuilder(1450);

        buffer.append("          ");   // ASMT_SYS_CD - Assessment system code
        buffer.append(" ");   // TRANS_TYPE_CD - Transaction type code
        buffer.append("   ");   // ITM_SBST_CD - Item subset code
        buffer.append("          ");   // ITM_SET_VRSN_CD - Item set version code
        buffer.append("          ");   // SPEC_VRSN_CD - Specifications version code
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
        buffer.append("       ");   // M1010_14_DAY_INP1_ICD - Inpatient stay within last 14 days: ICD code 1
        buffer.append("       ");   // M1010_14_DAY_INP2_ICD - Inpatient stay within last 14 days: ICD code 2
        buffer.append("       ");   // M1010_14_DAY_INP3_ICD - Inpatient stay within last 14 days: ICD code 3
        buffer.append("       ");   // M1010_14_DAY_INP4_ICD - Inpatient stay within last 14 days: ICD code 4
        buffer.append("       ");   // M1010_14_DAY_INP5_ICD - Inpatient stay within last 14 days: ICD code 5
        buffer.append("       ");   // M1010_14_DAY_INP6_ICD - Inpatient stay within last 14 days: ICD code 6
        buffer.append("       ");   // M1012_INP_PRCDR1_ICD - Inpatient ICD procedure1 code
        buffer.append("       ");   // M1012_INP_PRCDR2_ICD - Inpatient ICD procedure2 code
        buffer.append("       ");   // M1012_INP_PRCDR3_ICD - Inpatient ICD procedure3 code
        buffer.append("       ");   // M1012_INP_PRCDR4_ICD - Inpatient ICD procedure4 code
        buffer.append(" ");   // M1012_INP_NA_ICD - Inpatient ICD procedure code - NA
        buffer.append(" ");   // M1012_INP_UK_ICD - Inpatient ICD procedure code - UK
        buffer.append("       ");   // M1016_CHGREG_ICD1 - Regimen change in past 14 days: ICD code 1
        buffer.append("       ");   // M1016_CHGREG_ICD2 - Regimen change in past 14 days: ICD code 2
        buffer.append("       ");   // M1016_CHGREG_ICD3 - Regimen change in past 14 days: ICD code 3
        buffer.append("       ");   // M1016_CHGREG_ICD4 - Regimen change in past 14 days: ICD code 4
        buffer.append("       ");   // M1016_CHGREG_ICD5 - Regimen change in past 14 days: ICD code 5
        buffer.append("       ");   // M1016_CHGREG_ICD6 - Regimen change in past 14 days: ICD code 6
        buffer.append(" ");   // M1016_CHGREG_ICD_NA - Regimen change in past 14 days: not applicable
        buffer.append(" ");   // M1018_PRIOR_UR_INCON - Prior condition: urinary incontinence
        buffer.append(" ");   // M1018_PRIOR_CATH - Prior condition: indwelling/suprapubic catheter
        buffer.append(" ");   // M1018_PRIOR_INTRACT_PAIN - Prior condition: intractable pain
        buffer.append(" ");   // M1018_PRIOR_IMPR_DECSN - Prior condition: impaired decision-making
        buffer.append(" ");   // M1018_PRIOR_DISRUPTIVE - Prior condition: disruptive/inappropriate behav
        buffer.append(" ");   // M1018_PRIOR_MEM_LOSS - Prior condition: memory loss, supervision required
        buffer.append(" ");   // M1018_PRIOR_NONE - Prior condition: none of the above
        buffer.append(" ");   // M1018_PRIOR_NOCHG_14D - Prior condition: no inpt disch/no change regimen
        buffer.append(" ");   // M1018_PRIOR_UNKNOWN - Prior condition: unknown
        buffer.append(formatDxCode_C1(oasisRecord.getPRIMARY_DIAG_ICD()));   // M1020_PRIMARY_DIAG_ICD - Primary diagnosis ICD code
        buffer.append("  ");   // M1020_PRIMARY_DIAG_SEVERITY - Primary diagnosis severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG1_ICD()));   // M1022_OTH_DIAG1_ICD - Other diagnosis 1: ICD code
        buffer.append("  ");   // M1022_OTH_DIAG1_SEVERITY - Other diagnosis 1: severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG2_ICD()));   // M1022_OTH_DIAG2_ICD - Other diagnosis 2: ICD code
        buffer.append("  ");   // M1022_OTH_DIAG2_SEVERITY - Other diagnosis 2: severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG3_ICD()));   // M1022_OTH_DIAG3_ICD - Other diagnosis 3: ICD code
        buffer.append("  ");   // M1022_OTH_DIAG3_SEVERITY - Other diagnosis 3: severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG4_ICD()));   // M1022_OTH_DIAG4_ICD - Other diagnosis 4: ICD code
        buffer.append("  ");   // M1022_OTH_DIAG4_SEVERITY - Other diagnosis 4: severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG5_ICD()));   // M1022_OTH_DIAG5_ICD - Other diagnosis 5: ICD code
        buffer.append("  ");   // M1022_OTH_DIAG5_SEVERITY - Other diagnosis 5: severity rating
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_A3()));   // M1024_PMT_DIAG_ICD_A3 - Case mix diagnosis: primary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_B3()));   // M1024_PMT_DIAG_ICD_B3 - Case mix diagnosis: first secondary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_C3()));   // M1024_PMT_DIAG_ICD_C3 - Case mix diagnosis: second secondary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_D3()));   // M1024_PMT_DIAG_ICD_D3 - Case mix diagnosis: third secondary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_E3()));   // M1024_PMT_DIAG_ICD_E3 - Case mix diagnosis: fourth secondary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_F3()));   // M1024_PMT_DIAG_ICD_F3 - Case mix diagnosis: fifth secondary, column 3
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_A4()));   // M1024_PMT_DIAG_ICD_A4 - Case mix diagnosis: primary, column 4
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_B4()));   // M1024_PMT_DIAG_ICD_B4 - Case mix diagnosis: first secondary, column 4
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_C4()));   // M1024_PMT_DIAG_ICD_C4 - Case mix diagnosis: second secondary, column 4
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_D4()));   // M1024_PMT_DIAG_ICD_D4 - Case mix diagnosis: third secondary, column 4
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_E4()));   // M1024_PMT_DIAG_ICD_E4 - Case mix diagnosis: fourth secondary, column 4
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_F4()));   // M1024_PMT_DIAG_ICD_F4 - Case mix diagnosis: fifth secondary, column 4
        buffer.append(oasisRecord.getTHH_IV_INFUSION());   // M1030_THH_IV_INFUSION - Therapies received at home: intravenous, infusion
        buffer.append(oasisRecord.getTHH_PAR_NUTRITION());   // M1030_THH_PAR_NUTRITION - Therapies received at home: parenteral nutrition
        buffer.append(oasisRecord.getTHH_ENT_NUTRITION());   // M1030_THH_ENT_NUTRITION - Therapies received at home: enteral nutrition
        buffer.append(oasisRecord.getTHH_NONE_ABOVE());   // M1030_THH_NONE_ABOVE - Therapies received at home: none of the above
        buffer.append(" ");   // M1032_HOSP_RISK_RCNT_DCLN - Hosp risk: decline mental/emotional/behav status
        buffer.append(" ");   // M1032_HOSP_RISK_MLTPL_HOSPZTN - Hosp risk: 2+ hospitalizations in past 12 months
        buffer.append(" ");   // M1032_HOSP_RISK_HSTRY_FALLS - Hosp risk: 2+ falls or injury fall in past year
        buffer.append(" ");   // M1032_HOSP_RISK_5PLUS_MDCTN - Hosp risk: taking five or more medications
        buffer.append(" ");   // M1032_HOSP_RISK_FRAILTY - Hosp risk: frailty indicators
        buffer.append(" ");   // M1032_HOSP_RISK_OTHR - Hosp risk: other
        buffer.append(" ");   // M1032_HOSP_RISK_NONE_ABOVE - Hosp risk: none of the above
        buffer.append("  ");   // M1034_PTNT_OVRAL_STUS - Patient's overall status
        buffer.append(" ");   // M1036_RSK_SMOKING - High risk factor: smoking
        buffer.append(" ");   // M1036_RSK_OBESITY - High risk factor: obesity
        buffer.append(" ");   // M1036_RSK_ALCOHOLISM - High risk factor: alcoholism
        buffer.append(" ");   // M1036_RSK_DRUGS - High risk factor: drugs
        buffer.append(" ");   // M1036_RSK_NONE - High risk factor: none of the above
        buffer.append(" ");   // M1036_RSK_UNKNOWN - High risk factor: unknown
        buffer.append("  ");   // M1040_INFLNZ_RCVD_AGNCY - Was influenza vaccine received
        buffer.append("  ");   // M1045_INFLNZ_RSN_NOT_RCVD - If influenza vaccine not received, state reason
        buffer.append(" ");   // M1050_PPV_RCVD_AGNCY - Was pneumococcal vaccine received
        buffer.append("  ");   // M1055_PPV_RSN_NOT_RCVD_AGNCY - If pneumococcal vacc not received, state reason
        buffer.append("  ");   // M1100_PTNT_LVG_STUTN - Patient living situation
        buffer.append(oasisRecord.getVISION());   // M1200_VISION - Sensory status: vision
        buffer.append("  ");   // M1210_HEARG_ABLTY - Ability to hear
        buffer.append("  ");   // M1220_UNDRSTG_VERBAL_CNTNT - Understanding verbal content in patient's language
        buffer.append("  ");   // M1230_SPEECH - Sensory status: speech
        buffer.append("  ");   // M1240_FRML_PAIN_ASMT - Has patient had a formal pain assessment
        buffer.append(oasisRecord.getPAIN_FREQ_ACTVTY_MVMT());   // M1242_PAIN_FREQ_ACTVTY_MVMT - Freq of pain interfering with pt activity/movement
        buffer.append("  ");   // M1300_PRSR_ULCR_RISK_ASMT - Was patient assessed for risk of developing pus
        buffer.append(" ");   // M1302_RISK_OF_PRSR_ULCR - Does this patient have a risk of developing pus
        buffer.append(oasisRecord.getUNHLD_STG2_PRSR_ULCR());   // M1306_UNHLD_STG2_PRSR_ULCR - Patient has 1+ unhealed PU at stage 2 or higher
        buffer.append("  ");   // M1307_OLDST_STG2_AT_DSCHRG - Status of oldest stage 2 pressure ulcer at disch
        buffer.append("        ");   // M1307_OLDST_STG2_ONST_DT - Date of onset of oldest stage 2 pressure ulcer
        buffer.append(oasisRecord.getNBR_PRSULC_STG2());   // M1308_NBR_PRSULC_STG2 - No. pressure ulcers - stage 2
        buffer.append(oasisRecord.getNBR_STG2_AT_SOC_ROC());   // M1308_NBR_STG2_AT_SOC_ROC - Number pu stage 2 at SOC/ROC
        buffer.append(oasisRecord.getNBR_PRSULC_STG3());   // M1308_NBR_PRSULC_STG3 - No. pressure ulcers - stage 3
        buffer.append(oasisRecord.getNBR_STG3_AT_SOC_ROC());   // M1308_NBR_STG3_AT_SOC_ROC - Number PU stage 3 at SOC/ROC
        buffer.append(oasisRecord.getNBR_PRSULC_STG4());   // M1308_NBR_PRSULC_STG4 - No. pressure ulcers - stage 4
        buffer.append(oasisRecord.getNBR_STG4_AT_SOC_ROC());   // M1308_NBR_STG4_AT_SOC_ROC - Number PU stage 4 at SOC/ROC
        buffer.append(oasisRecord.getNSTG_DRSG());   // M1308_NSTG_DRSG - Unstageable: non-removable dressing/device
        buffer.append(oasisRecord.getNSTG_DRSG_SOC_ROC());   // M1308_NSTG_DRSG_SOC_ROC - Unstageable: non-removable dressing/device-SOC/ROC
        buffer.append(oasisRecord.getNSTG_CVRG());   // M1308_NSTG_CVRG - Unstageable: coverage by slough or eschar
        buffer.append(oasisRecord.getNSTG_CVRG_SOC_ROC());   // M1308_NSTG_CVRG_SOC_ROC - Unstageable: coverage by slough or eschar-SOC/ROC
        buffer.append(oasisRecord.getNSTG_DEEP_TISUE());   // M1308_NSTG_DEEP_TISUE - Unstageable: suspect deep tissue injury
        buffer.append("  ");   // M1308_NSTG_DEEP_TISUE_SOC_ROC - Unstageable: suspect deep tissue injury-SOC/ROC
        buffer.append("    ");   // M1310_PRSR_ULCR_LNGTH - Length of stage 3 or 4 PU with largest area
        buffer.append("    ");   // M1312_PRSR_ULCR_WDTH - Width of stage 3 or 4 PU with largest area
        buffer.append("    ");   // M1314_PRSR_ULCR_DEPTH - Depth of stage 3 or 4 PU with largest area
        buffer.append(oasisRecord.getSTUS_PRBLM_PRSR_ULCR());   // M1320_STUS_PRBLM_PRSR_ULCR - Status of most problematic pressure ulcer
        buffer.append(oasisRecord.getNBR_PRSULC_STG1());   // M1322_NBR_PRSULC_STG1 - No. pressure ulcers - stage 1
        buffer.append(oasisRecord.getSTG_PRBLM_ULCER());   // M1324_STG_PRBLM_ULCER - Stage of most problematic pressure ulcer
        buffer.append(oasisRecord.getSTAS_ULCR_PRSNT());   // M1330_STAS_ULCR_PRSNT - Does this patient have a stasis ulcer
        buffer.append("  ");   // M1332_NUM_STAS_ULCR - No. stasis ulcers
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
        buffer.append("  ");   // M1730_STDZ_DPRSN_SCRNG - Has the patient been screened for depression
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
        buffer.append("  ");   // M1800_CUR_GROOMING - Current: grooming
        buffer.append("  ");   // M1810_CUR_DRESS_UPPER - Current: dress upper body
        buffer.append("  ");   // M1820_CUR_DRESS_LOWER - Current: dress lower body
        buffer.append(oasisRecord.getCRNT_BATHG());   // M1830_CRNT_BATHG - Current: bathing
        buffer.append("  ");   // M1840_CUR_TOILTG - Current: toileting
        buffer.append("  ");   // M1845_CUR_TOILTG_HYGN - Current: toileting hygiene
        buffer.append("  ");   // M1850_CUR_TRNSFRNG - Current: transferring
        buffer.append(oasisRecord.getCRNT_AMBLTN());   // M1860_CRNT_AMBLTN - Current: ambulation
        buffer.append("  ");   // M1870_CUR_FEEDING - Current: feeding
        buffer.append("  ");   // M1880_CUR_PREP_LT_MEALS - Current: prepare light meals
        buffer.append("  ");   // M1890_CUR_PHONE_USE - Current: telephone use
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
        buffer.append("  ");   // M2100_CARE_TYPE_SRC_ADL - Care mgmt, types/sources: ADL
        buffer.append("  ");   // M2100_CARE_TYPE_SRC_IADL - Care mgmt, types/sources: IADL
        buffer.append("  ");   // M2100_CARE_TYPE_SRC_MDCTN - Care mgmt, types/sources: med admin
        buffer.append("  ");   // M2100_CARE_TYPE_SRC_PRCDR - Care mgmt, types/sources: med procs tx
        buffer.append("  ");   // M2100_CARE_TYPE_SRC_EQUIP - Care mgmt, types/sources: equipment
        buffer.append("  ");   // M2100_CARE_TYPE_SRC_SPRVSN - Care mgmt, types/sources: supervision and safety
        buffer.append("  ");   // M2100_CARE_TYPE_SRC_ADVCY - Care mgmt, types/sources: advocacy or facilitation
        buffer.append("  ");   // M2110_ADL_IADL_ASTNC_FREQ - How often recv ADL or IADL assistance from any
        buffer.append("   ");   // M2200_THER_NEED_NUM - Therapy need: number of visits
        buffer.append(oasisRecord.getTHER_NEED_NA());   // M2200_THER_NEED_NA - Therapy need: not applicable
        buffer.append("  ");   // M2250_PLAN_SMRY_PTNT_SPECF - Plan of care synopsis: patient specific parameters
        buffer.append("  ");   // M2250_PLAN_SMRY_DBTS_FT_CARE - Plan of care synopsis: diabetic foot care
        buffer.append("  ");   // M2250_PLAN_SMRY_FALL_PRVNT - Plan of care synopsis: falls prevention
        buffer.append("  ");   // M2250_PLAN_SMRY_DPRSN_INTRVTN - Plan of care synopsis: depression interventions
        buffer.append("  ");   // M2250_PLAN_SMRY_PAIN_INTRVTN - Plan of care synopsis: pain interventions
        buffer.append("  ");   // M2250_PLAN_SMRY_PRSULC_PRVNT - Plan of care synopsis: PU prevention
        buffer.append("  ");   // M2250_PLAN_SMRY_PRSULC_TRTMT - Plan of care synopsis: PU moist treatment
        buffer.append("  ");   // M2300_EMER_USE_AFTR_LAST_ASMT - Emergent care: use since last OASIS
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
        buffer.append(" ");   // M2440_NH_THERAPY - Admitted to nursing home: therapy
        buffer.append(" ");   // M2440_NH_RESPITE - Admitted to nursing home: respite
        buffer.append(" ");   // M2440_NH_HOSPICE - Admitted to nursing home: hospice
        buffer.append(" ");   // M2440_NH_PERMANENT - Admitted to nursing home: permanent placement
        buffer.append(" ");   // M2440_NH_UNSAFE_HOME - Admitted to nursing home: unsafe at home
        buffer.append(" ");   // M2440_NH_OTHER - Admitted to nursing home: other
        buffer.append(" ");   // M2440_NH_UNKNOWN - Admitted to nursing home: unknown
        buffer.append("        ");   // M0903_LAST_HOME_VISIT - Date of last home visit
        buffer.append("        ");   // M0906_DC_TRAN_DTH_DT - Discharge, transfer, death date
        buffer.append("                                                                                                                                                                                                        ");   // CONTROL_ITEMS_FILLER - Control items filler
        buffer.append("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ");   // ASMT_ITEMS_FILLER - Assessment items filler
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
        buffer.append(" ");   // DATA_END_INDICATOR - End of data terminator code
        buffer.append("\r");   // CR - Carriage return (ASCII 013)
        buffer.append("\n");   // LF - Line feed character (ASCII 010)

        return buffer;
    }

    public StringBuilder convertFromHomeHealthToXmlRecord(HomeHealthRecord_C_IF oasisRecord) {
        StringBuilder buffer = new StringBuilder(3000);

        buffer.append("<ASSESSMENT>");
        buffer.append("  <ASMT_SYS_CD>OASIS</ASMT_SYS_CD>");
        buffer.append("  <TRANS_TYPE_CD>1</TRANS_TYPE_CD>");
        buffer.append("  <ITM_SBST_CD>01</ITM_SBST_CD>");
        buffer.append("  <ITM_SET_VRSN_CD>C1-102014</ITM_SET_VRSN_CD>");
        buffer.append("  <SPEC_VRSN_CD>2.11</SPEC_VRSN_CD>");

        buffer.append("  <CORRECTION_NUM>").append("</CORRECTION_NUM>");   // CORRECTION_NUM - Correction number
        buffer.append("  <STATE_CD>").append("</STATE_CD>");   // STATE_CD - Agency's state postal code
        buffer.append("  <HHA_AGENCY_ID>").append("</HHA_AGENCY_ID>");   // HHA_AGENCY_ID - Assigned agency submission ID
        buffer.append("  <NATL_PRVDR_ID>").append("</NATL_PRVDR_ID>");   // NATL_PRVDR_ID - Agency National Provider ID (NPI)
        buffer.append("  <SFW_ID>").append("</SFW_ID>");   // SFW_ID - Software vendor federal employer tax ID
        buffer.append("  <SFW_NAME>").append("</SFW_NAME>");   // SFW_NAME - Software vendor company name
        buffer.append("  <SFW_EMAIL_ADR>").append("</SFW_EMAIL_ADR>");   // SFW_EMAIL_ADR - Software vendor email address
        buffer.append("  <SFW_PROD_NAME>").append("</SFW_PROD_NAME>");   // SFW_PROD_NAME - Software product name
        buffer.append("  <SFW_PROD_VRSN_CD>").append("</SFW_PROD_VRSN_CD>");   // SFW_PROD_VRSN_CD - Software product version code
        buffer.append("  <ACY_DOC_CD>").append("</ACY_DOC_CD>");   // ACY_DOC_CD - Document ID code (agency use)
        buffer.append("  <SUBM_HIPPS_CODE>").append("</SUBM_HIPPS_CODE>");   // SUBM_HIPPS_CODE - HIPPS group code: submitted
        buffer.append("  <SUBM_HIPPS_VERSION>").append("</SUBM_HIPPS_VERSION>");   // SUBM_HIPPS_VERSION - HIPPS version code: submitted
        buffer.append("  <M0010_CCN>").append("</M0010_CCN>");   // M0010_CCN - Facility CMS certification number (CCN)
        buffer.append("  <M0014_BRANCH_STATE>").append("</M0014_BRANCH_STATE>");   // M0014_BRANCH_STATE - Branch state
        buffer.append("  <M0016_BRANCH_ID>").append("</M0016_BRANCH_ID>");   // M0016_BRANCH_ID - Branch ID
        buffer.append("  <M0018_PHYSICIAN_ID>").append("</M0018_PHYSICIAN_ID>");   // M0018_PHYSICIAN_ID - Attending physician National Provider ID (NPI)
        buffer.append("  <M0018_PHYSICIAN_UK>").append("</M0018_PHYSICIAN_UK>");   // M0018_PHYSICIAN_UK - Attending physician NPI: Unknown
        buffer.append("  <M0020_PAT_ID>").append("</M0020_PAT_ID>");   // M0020_PAT_ID - Patient ID number
        buffer.append("  <M0030_START_CARE_DT>");
        OasisCalendarFormatter.format(oasisRecord.getSTART_CARE_DT(), buffer).append("</M0030_START_CARE_DT>");   // M0030_START_CARE_DT - Start of care date
        buffer.append("  <M0032_ROC_DT>").append("</M0032_ROC_DT>");   // M0032_ROC_DT - Resumption of care date
        buffer.append("  <M0032_ROC_DT_NA>").append("</M0032_ROC_DT_NA>");   // M0032_ROC_DT_NA - No resumption of care date
        buffer.append("  <M0040_PAT_FNAME>").append("</M0040_PAT_FNAME>");   // M0040_PAT_FNAME - Patient's first name
        buffer.append("  <M0040_PAT_MI>").append("</M0040_PAT_MI>");   // M0040_PAT_MI - Patient's middle initial
        buffer.append("  <M0040_PAT_LNAME>").append("</M0040_PAT_LNAME>");   // M0040_PAT_LNAME - Patient's last name
        buffer.append("  <M0040_PAT_SUFFIX>").append("</M0040_PAT_SUFFIX>");   // M0040_PAT_SUFFIX - Patient's suffix
        buffer.append("  <M0050_PAT_ST>").append("</M0050_PAT_ST>");   // M0050_PAT_ST - Patient state of residence
        buffer.append("  <M0060_PAT_ZIP>").append("</M0060_PAT_ZIP>");   // M0060_PAT_ZIP - Patient zip code
        buffer.append("  <M0063_MEDICARE_NUM>").append("</M0063_MEDICARE_NUM>");   // M0063_MEDICARE_NUM - Medicare number, including suffix
        buffer.append("  <M0063_MEDICARE_NA>").append("</M0063_MEDICARE_NA>");   // M0063_MEDICARE_NA - No Medicare number
        buffer.append("  <M0064_SSN>").append("</M0064_SSN>");   // M0064_SSN - Patient's Social Security number
        buffer.append("  <M0064_SSN_UK>").append("</M0064_SSN_UK>");   // M0064_SSN_UK - No Social Security number
        buffer.append("  <M0065_MEDICAID_NUM>").append("</M0065_MEDICAID_NUM>");   // M0065_MEDICAID_NUM - Medicaid number
        buffer.append("  <M0065_MEDICAID_NA>").append("</M0065_MEDICAID_NA>");   // M0065_MEDICAID_NA - No Medicaid number
        buffer.append("  <M0066_PAT_BIRTH_DT>").append("</M0066_PAT_BIRTH_DT>");   // M0066_PAT_BIRTH_DT - Date of birth
        buffer.append("  <M0069_PAT_GENDER>").append("</M0069_PAT_GENDER>");   // M0069_PAT_GENDER - Gender
        buffer.append("  <M0140_ETHNIC_AI_AN>").append("</M0140_ETHNIC_AI_AN>");   // M0140_ETHNIC_AI_AN - Ethnicity: American Indian or Alaska Native
        buffer.append("  <M0140_ETHNIC_ASIAN>").append("</M0140_ETHNIC_ASIAN>");   // M0140_ETHNIC_ASIAN - Ethnicity: Asian
        buffer.append("  <M0140_ETHNIC_BLACK>").append("</M0140_ETHNIC_BLACK>");   // M0140_ETHNIC_BLACK - Ethnicity: Black or African American
        buffer.append("  <M0140_ETHNIC_HISP>").append("</M0140_ETHNIC_HISP>");   // M0140_ETHNIC_HISP - Ethnicity: Hispanic or Latino
        buffer.append("  <M0140_ETHNIC_NH_PI>").append("</M0140_ETHNIC_NH_PI>");   // M0140_ETHNIC_NH_PI - Ethnicity: Native Hawaiian/Pacific Islander
        buffer.append("  <M0140_ETHNIC_WHITE>").append("</M0140_ETHNIC_WHITE>");   // M0140_ETHNIC_WHITE - Ethnicity: White
        buffer.append("  <M0150_CPAY_NONE>").append("</M0150_CPAY_NONE>");   // M0150_CPAY_NONE - Payment sources: no charge for current services
        buffer.append("  <M0150_CPAY_MCARE_FFS>").append("</M0150_CPAY_MCARE_FFS>");   // M0150_CPAY_MCARE_FFS - Payment sources: Medicare fee-for-service
        buffer.append("  <M0150_CPAY_MCARE_HMO>").append("</M0150_CPAY_MCARE_HMO>");   // M0150_CPAY_MCARE_HMO - Payment sources: Medicare HMO/managed care
        buffer.append("  <M0150_CPAY_MCAID_FFS>").append("</M0150_CPAY_MCAID_FFS>");   // M0150_CPAY_MCAID_FFS - Payment sources: Medicaid fee-for-service
        buffer.append("  <M0150_CPAY_MCAID_HMO>").append("</M0150_CPAY_MCAID_HMO>");   // M0150_CPAY_MCAID_HMO - Payment sources: Medicaid HMO/managed care
        buffer.append("  <M0150_CPAY_WRKCOMP>").append("</M0150_CPAY_WRKCOMP>");   // M0150_CPAY_WRKCOMP - Payment sources: worker's compensation
        buffer.append("  <M0150_CPAY_TITLEPGMS>").append("</M0150_CPAY_TITLEPGMS>");   // M0150_CPAY_TITLEPGMS - Payment sources: title programs
        buffer.append("  <M0150_CPAY_OTH_GOVT>").append("</M0150_CPAY_OTH_GOVT>");   // M0150_CPAY_OTH_GOVT - Payment sources: other government
        buffer.append("  <M0150_CPAY_PRIV_INS>").append("</M0150_CPAY_PRIV_INS>");   // M0150_CPAY_PRIV_INS - Payment sources: private insurance
        buffer.append("  <M0150_CPAY_PRIV_HMO>").append("</M0150_CPAY_PRIV_HMO>");   // M0150_CPAY_PRIV_HMO - Payment sources: private HMO/managed care
        buffer.append("  <M0150_CPAY_SELFPAY>").append("</M0150_CPAY_SELFPAY>");   // M0150_CPAY_SELFPAY - Payment sources: self-pay
        buffer.append("  <M0150_CPAY_OTHER>").append("</M0150_CPAY_OTHER>");   // M0150_CPAY_OTHER - Payment sources: other
        buffer.append("  <M0150_CPAY_UK>").append("</M0150_CPAY_UK>");   // M0150_CPAY_UK - Payment sources: unknown
        buffer.append("  <M0080_ASSESSOR_DISCIPLINE>").append("</M0080_ASSESSOR_DISCIPLINE>");   // M0080_ASSESSOR_DISCIPLINE - Discipline of person completing assessment
        buffer.append("  <M0090_INFO_COMPLETED_DT>");
        OasisCalendarFormatter.format(oasisRecord.getINFO_COMPLETED_DT(), buffer).append("</M0090_INFO_COMPLETED_DT>");   // M0090_INFO_COMPLETED_DT - Date assessment completed
        buffer.append("  <M0100_ASSMT_REASON>").append(oasisRecord.getASSMT_REASON()).append("</M0100_ASSMT_REASON>");   // M0100_ASSMT_REASON - Reason for assessment
        buffer.append("  <M0102_PHYSN_ORDRD_SOCROC_DT>").append("</M0102_PHYSN_ORDRD_SOCROC_DT>");   // M0102_PHYSN_ORDRD_SOCROC_DT - Physician ordered SOC/ROC date
        buffer.append("  <M0102_PHYSN_ORDRD_SOCROC_DT_NA>").append("</M0102_PHYSN_ORDRD_SOCROC_DT_NA>");   // M0102_PHYSN_ORDRD_SOCROC_DT_NA - Physician ordered SOC/ROC date - NA
        buffer.append("  <M0104_PHYSN_RFRL_DT>").append("</M0104_PHYSN_RFRL_DT>");   // M0104_PHYSN_RFRL_DT - Physician date of referral
        buffer.append("  <M0110_EPISODE_TIMING>").append(oasisRecord.getEPISODE_TIMING()).append("</M0110_EPISODE_TIMING>");   // M0110_EPISODE_TIMING - Episode timing
        buffer.append("  <M1000_DC_LTC_14_DA>").append("</M1000_DC_LTC_14_DA>");   // M1000_DC_LTC_14_DA - Past 14 days: disch from LTC NH
        buffer.append("  <M1000_DC_SNF_14_DA>").append("</M1000_DC_SNF_14_DA>");   // M1000_DC_SNF_14_DA - Past 14 days: disch from skilled nursing facility
        buffer.append("  <M1000_DC_IPPS_14_DA>").append("</M1000_DC_IPPS_14_DA>");   // M1000_DC_IPPS_14_DA - Past 14 days: disch from short stay acute hospital
        buffer.append("  <M1000_DC_LTCH_14_DA>").append("</M1000_DC_LTCH_14_DA>");   // M1000_DC_LTCH_14_DA - Past 14 days: disch from long term care hospital
        buffer.append("  <M1000_DC_IRF_14_DA>").append("</M1000_DC_IRF_14_DA>");   // M1000_DC_IRF_14_DA - Past 14 days: disch from inpatient rehab facility
        buffer.append("  <M1000_DC_PSYCH_14_DA>").append("</M1000_DC_PSYCH_14_DA>");   // M1000_DC_PSYCH_14_DA - Past 14 days: disch from psych hospital or unit
        buffer.append("  <M1000_DC_OTH_14_DA>").append("</M1000_DC_OTH_14_DA>");   // M1000_DC_OTH_14_DA - Past 14 days: disch from other
        buffer.append("  <M1000_DC_NONE_14_DA>").append("</M1000_DC_NONE_14_DA>");   // M1000_DC_NONE_14_DA - Past 14 days: not disch from inpatient facility
        buffer.append("  <M1005_INP_DISCHARGE_DT>").append("</M1005_INP_DISCHARGE_DT>");   // M1005_INP_DISCHARGE_DT - Most recent inpatient discharge date
        buffer.append("  <M1005_INP_DSCHG_UNKNOWN>").append("</M1005_INP_DSCHG_UNKNOWN>");   // M1005_INP_DSCHG_UNKNOWN - Inpatient discharge date unknown
        buffer.append("  <M1010_14_DAY_INP1_ICD>").append("</M1010_14_DAY_INP1_ICD>");   // M1010_14_DAY_INP1_ICD - Inpatient stay within last 14 days: ICD code 1
        buffer.append("  <M1010_14_DAY_INP2_ICD>").append("</M1010_14_DAY_INP2_ICD>");   // M1010_14_DAY_INP2_ICD - Inpatient stay within last 14 days: ICD code 2
        buffer.append("  <M1010_14_DAY_INP3_ICD>").append("</M1010_14_DAY_INP3_ICD>");   // M1010_14_DAY_INP3_ICD - Inpatient stay within last 14 days: ICD code 3
        buffer.append("  <M1010_14_DAY_INP4_ICD>").append("</M1010_14_DAY_INP4_ICD>");   // M1010_14_DAY_INP4_ICD - Inpatient stay within last 14 days: ICD code 4
        buffer.append("  <M1010_14_DAY_INP5_ICD>").append("</M1010_14_DAY_INP5_ICD>");   // M1010_14_DAY_INP5_ICD - Inpatient stay within last 14 days: ICD code 5
        buffer.append("  <M1010_14_DAY_INP6_ICD>").append("</M1010_14_DAY_INP6_ICD>");   // M1010_14_DAY_INP6_ICD - Inpatient stay within last 14 days: ICD code 6
        buffer.append("  <M1012_INP_PRCDR1_ICD>").append("</M1012_INP_PRCDR1_ICD>");   // M1012_INP_PRCDR1_ICD - Inpatient ICD procedure1 code
        buffer.append("  <M1012_INP_PRCDR2_ICD>").append("</M1012_INP_PRCDR2_ICD>");   // M1012_INP_PRCDR2_ICD - Inpatient ICD procedure2 code
        buffer.append("  <M1012_INP_PRCDR3_ICD>").append("</M1012_INP_PRCDR3_ICD>");   // M1012_INP_PRCDR3_ICD - Inpatient ICD procedure3 code
        buffer.append("  <M1012_INP_PRCDR4_ICD>").append("</M1012_INP_PRCDR4_ICD>");   // M1012_INP_PRCDR4_ICD - Inpatient ICD procedure4 code
        buffer.append("  <M1012_INP_NA_ICD>").append("</M1012_INP_NA_ICD>");   // M1012_INP_NA_ICD - Inpatient ICD procedure code - NA
        buffer.append("  <M1012_INP_UK_ICD>").append("</M1012_INP_UK_ICD>");   // M1012_INP_UK_ICD - Inpatient ICD procedure code - UK
        buffer.append("  <M1016_CHGREG_ICD1>").append("</M1016_CHGREG_ICD1>");   // M1016_CHGREG_ICD1 - Regimen change in past 14 days: ICD code 1
        buffer.append("  <M1016_CHGREG_ICD2>").append("</M1016_CHGREG_ICD2>");   // M1016_CHGREG_ICD2 - Regimen change in past 14 days: ICD code 2
        buffer.append("  <M1016_CHGREG_ICD3>").append("</M1016_CHGREG_ICD3>");   // M1016_CHGREG_ICD3 - Regimen change in past 14 days: ICD code 3
        buffer.append("  <M1016_CHGREG_ICD4>").append("</M1016_CHGREG_ICD4>");   // M1016_CHGREG_ICD4 - Regimen change in past 14 days: ICD code 4
        buffer.append("  <M1016_CHGREG_ICD5>").append("</M1016_CHGREG_ICD5>");   // M1016_CHGREG_ICD5 - Regimen change in past 14 days: ICD code 5
        buffer.append("  <M1016_CHGREG_ICD6>").append("</M1016_CHGREG_ICD6>");   // M1016_CHGREG_ICD6 - Regimen change in past 14 days: ICD code 6
        buffer.append("  <M1016_CHGREG_ICD_NA>").append("</M1016_CHGREG_ICD_NA>");   // M1016_CHGREG_ICD_NA - Regimen change in past 14 days: not applicable
        buffer.append("  <M1018_PRIOR_UR_INCON>").append("</M1018_PRIOR_UR_INCON>");   // M1018_PRIOR_UR_INCON - Prior condition: urinary incontinence
        buffer.append("  <M1018_PRIOR_CATH>").append("</M1018_PRIOR_CATH>");   // M1018_PRIOR_CATH - Prior condition: indwelling/suprapubic catheter
        buffer.append("  <M1018_PRIOR_INTRACT_PAIN>").append("</M1018_PRIOR_INTRACT_PAIN>");   // M1018_PRIOR_INTRACT_PAIN - Prior condition: intractable pain
        buffer.append("  <M1018_PRIOR_IMPR_DECSN>").append("</M1018_PRIOR_IMPR_DECSN>");   // M1018_PRIOR_IMPR_DECSN - Prior condition: impaired decision-making
        buffer.append("  <M1018_PRIOR_DISRUPTIVE>").append("</M1018_PRIOR_DISRUPTIVE>");   // M1018_PRIOR_DISRUPTIVE - Prior condition: disruptive/inappropriate behav
        buffer.append("  <M1018_PRIOR_MEM_LOSS>").append("</M1018_PRIOR_MEM_LOSS>");   // M1018_PRIOR_MEM_LOSS - Prior condition: memory loss, supervision required
        buffer.append("  <M1018_PRIOR_NONE>").append("</M1018_PRIOR_NONE>");   // M1018_PRIOR_NONE - Prior condition: none of the above
        buffer.append("  <M1018_PRIOR_NOCHG_14D>").append("</M1018_PRIOR_NOCHG_14D>");   // M1018_PRIOR_NOCHG_14D - Prior condition: no inpt disch/no change regimen
        buffer.append("  <M1018_PRIOR_UNKNOWN>").append("</M1018_PRIOR_UNKNOWN>");   // M1018_PRIOR_UNKNOWN - Prior condition: unknown
        buffer.append("  <M1020_PRIMARY_DIAG_ICD>").append(formatDxCode_C1(oasisRecord.getPRIMARY_DIAG_ICD())).append("</M1020_PRIMARY_DIAG_ICD>");   // M1020_PRIMARY_DIAG_ICD - Primary diagnosis ICD code
        buffer.append("  <M1020_PRIMARY_DIAG_SEVERITY>").append("</M1020_PRIMARY_DIAG_SEVERITY>");   // M1020_PRIMARY_DIAG_SEVERITY - Primary diagnosis severity rating
        buffer.append("  <M1022_OTH_DIAG1_ICD>").append(formatDxCode_C1(oasisRecord.getOTH_DIAG1_ICD())).append("</M1022_OTH_DIAG1_ICD>");   // M1022_OTH_DIAG1_ICD - Other diagnosis 1: ICD code
        buffer.append("  <M1022_OTH_DIAG1_SEVERITY>").append("</M1022_OTH_DIAG1_SEVERITY>");   // M1022_OTH_DIAG1_SEVERITY - Other diagnosis 1: severity rating
        buffer.append("  <M1022_OTH_DIAG2_ICD>").append(formatDxCode_C1(oasisRecord.getOTH_DIAG2_ICD())).append("</M1022_OTH_DIAG2_ICD>");   // M1022_OTH_DIAG2_ICD - Other diagnosis 2: ICD code
        buffer.append("  <M1022_OTH_DIAG2_SEVERITY>").append("</M1022_OTH_DIAG2_SEVERITY>");   // M1022_OTH_DIAG2_SEVERITY - Other diagnosis 2: severity rating
        buffer.append("  <M1022_OTH_DIAG3_ICD>").append(formatDxCode_C1(oasisRecord.getOTH_DIAG3_ICD())).append("</M1022_OTH_DIAG3_ICD>");   // M1022_OTH_DIAG3_ICD - Other diagnosis 3: ICD code
        buffer.append("  <M1022_OTH_DIAG3_SEVERITY>").append("</M1022_OTH_DIAG3_SEVERITY>");   // M1022_OTH_DIAG3_SEVERITY - Other diagnosis 3: severity rating
        buffer.append("  <M1022_OTH_DIAG4_ICD>").append(formatDxCode_C1(oasisRecord.getOTH_DIAG4_ICD())).append("</M1022_OTH_DIAG4_ICD>");   // M1022_OTH_DIAG4_ICD - Other diagnosis 4: ICD code
        buffer.append("  <M1022_OTH_DIAG4_SEVERITY>").append("</M1022_OTH_DIAG4_SEVERITY>");   // M1022_OTH_DIAG4_SEVERITY - Other diagnosis 4: severity rating
        buffer.append("  <M1022_OTH_DIAG5_ICD>").append(formatDxCode_C1(oasisRecord.getOTH_DIAG5_ICD())).append("</M1022_OTH_DIAG5_ICD>");   // M1022_OTH_DIAG5_ICD - Other diagnosis 5: ICD code
        buffer.append("  <M1022_OTH_DIAG5_SEVERITY>").append("</M1022_OTH_DIAG5_SEVERITY>");   // M1022_OTH_DIAG5_SEVERITY - Other diagnosis 5: severity rating
        buffer.append("  <M1024_PMT_DIAG_ICD_A3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_A3())).append("</M1024_PMT_DIAG_ICD_A3>");   // M1024_PMT_DIAG_ICD_A3 - Case mix diagnosis: primary, column 3
        buffer.append("  <M1024_PMT_DIAG_ICD_B3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_B3())).append("</M1024_PMT_DIAG_ICD_B3>");   // M1024_PMT_DIAG_ICD_B3 - Case mix diagnosis: first secondary, column 3
        buffer.append("  <M1024_PMT_DIAG_ICD_C3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_C3())).append("</M1024_PMT_DIAG_ICD_C3>");   // M1024_PMT_DIAG_ICD_C3 - Case mix diagnosis: second secondary, column 3
        buffer.append("  <M1024_PMT_DIAG_ICD_D3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_D3())).append("</M1024_PMT_DIAG_ICD_D3>");   // M1024_PMT_DIAG_ICD_D3 - Case mix diagnosis: third secondary, column 3
        buffer.append("  <M1024_PMT_DIAG_ICD_E3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_E3())).append("</M1024_PMT_DIAG_ICD_E3>");   // M1024_PMT_DIAG_ICD_E3 - Case mix diagnosis: fourth secondary, column 3
        buffer.append("  <M1024_PMT_DIAG_ICD_F3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_F3())).append("</M1024_PMT_DIAG_ICD_F3>");   // M1024_PMT_DIAG_ICD_F3 - Case mix diagnosis: fifth secondary, column 3
        buffer.append("  <M1024_PMT_DIAG_ICD_A4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_A4())).append("</M1024_PMT_DIAG_ICD_A4>");   // M1024_PMT_DIAG_ICD_A4 - Case mix diagnosis: primary, column 4
        buffer.append("  <M1024_PMT_DIAG_ICD_B4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_B4())).append("</M1024_PMT_DIAG_ICD_B4>");   // M1024_PMT_DIAG_ICD_B4 - Case mix diagnosis: first secondary, column 4
        buffer.append("  <M1024_PMT_DIAG_ICD_C4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_C4())).append("</M1024_PMT_DIAG_ICD_C4>");   // M1024_PMT_DIAG_ICD_C4 - Case mix diagnosis: second secondary, column 4
        buffer.append("  <M1024_PMT_DIAG_ICD_D4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_D4())).append("</M1024_PMT_DIAG_ICD_D4>");   // M1024_PMT_DIAG_ICD_D4 - Case mix diagnosis: third secondary, column 4
        buffer.append("  <M1024_PMT_DIAG_ICD_E4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_E4())).append("</M1024_PMT_DIAG_ICD_E4>");   // M1024_PMT_DIAG_ICD_E4 - Case mix diagnosis: fourth secondary, column 4
        buffer.append("  <M1024_PMT_DIAG_ICD_F4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_F4())).append("</M1024_PMT_DIAG_ICD_F4>");   // M1024_PMT_DIAG_ICD_F4 - Case mix diagnosis: fifth secondary, column 4
        buffer.append("  <M1030_THH_IV_INFUSION>").append(oasisRecord.getTHH_IV_INFUSION()).append("</M1030_THH_IV_INFUSION>");   // M1030_THH_IV_INFUSION - Therapies received at home: intravenous, infusion
        buffer.append("  <M1030_THH_PAR_NUTRITION>").append(oasisRecord.getTHH_PAR_NUTRITION()).append("</M1030_THH_PAR_NUTRITION>");   // M1030_THH_PAR_NUTRITION - Therapies received at home: parenteral nutrition
        buffer.append("  <M1030_THH_ENT_NUTRITION>").append(oasisRecord.getTHH_ENT_NUTRITION()).append("</M1030_THH_ENT_NUTRITION>");   // M1030_THH_ENT_NUTRITION - Therapies received at home: enteral nutrition
        buffer.append("  <M1030_THH_NONE_ABOVE>").append(oasisRecord.getTHH_NONE_ABOVE()).append("</M1030_THH_NONE_ABOVE>");   // M1030_THH_NONE_ABOVE - Therapies received at home: none of the above
        buffer.append("  <M1032_HOSP_RISK_RCNT_DCLN>").append("</M1032_HOSP_RISK_RCNT_DCLN>");   // M1032_HOSP_RISK_RCNT_DCLN - Hosp risk: decline mental/emotional/behav status
        buffer.append("  <M1032_HOSP_RISK_MLTPL_HOSPZTN>").append("</M1032_HOSP_RISK_MLTPL_HOSPZTN>");   // M1032_HOSP_RISK_MLTPL_HOSPZTN - Hosp risk: 2+ hospitalizations in past 12 months
        buffer.append("  <M1032_HOSP_RISK_HSTRY_FALLS>").append("</M1032_HOSP_RISK_HSTRY_FALLS>");   // M1032_HOSP_RISK_HSTRY_FALLS - Hosp risk: 2+ falls or injury fall in past year
        buffer.append("  <M1032_HOSP_RISK_5PLUS_MDCTN>").append("</M1032_HOSP_RISK_5PLUS_MDCTN>");   // M1032_HOSP_RISK_5PLUS_MDCTN - Hosp risk: taking five or more medications
        buffer.append("  <M1032_HOSP_RISK_FRAILTY>").append("</M1032_HOSP_RISK_FRAILTY>");   // M1032_HOSP_RISK_FRAILTY - Hosp risk: frailty indicators
        buffer.append("  <M1032_HOSP_RISK_OTHR>").append("</M1032_HOSP_RISK_OTHR>");   // M1032_HOSP_RISK_OTHR - Hosp risk: other
        buffer.append("  <M1032_HOSP_RISK_NONE_ABOVE>").append("</M1032_HOSP_RISK_NONE_ABOVE>");   // M1032_HOSP_RISK_NONE_ABOVE - Hosp risk: none of the above
        buffer.append("  <M1034_PTNT_OVRAL_STUS>").append("</M1034_PTNT_OVRAL_STUS>");   // M1034_PTNT_OVRAL_STUS - Patient's overall status
        buffer.append("  <M1036_RSK_SMOKING>").append("</M1036_RSK_SMOKING>");   // M1036_RSK_SMOKING - High risk factor: smoking
        buffer.append("  <M1036_RSK_OBESITY>").append("</M1036_RSK_OBESITY>");   // M1036_RSK_OBESITY - High risk factor: obesity
        buffer.append("  <M1036_RSK_ALCOHOLISM>").append("</M1036_RSK_ALCOHOLISM>");   // M1036_RSK_ALCOHOLISM - High risk factor: alcoholism
        buffer.append("  <M1036_RSK_DRUGS>").append("</M1036_RSK_DRUGS>");   // M1036_RSK_DRUGS - High risk factor: drugs
        buffer.append("  <M1036_RSK_NONE>").append("</M1036_RSK_NONE>");   // M1036_RSK_NONE - High risk factor: none of the above
        buffer.append("  <M1036_RSK_UNKNOWN>").append("</M1036_RSK_UNKNOWN>");   // M1036_RSK_UNKNOWN - High risk factor: unknown
        buffer.append("  <M1040_INFLNZ_RCVD_AGNCY>").append("</M1040_INFLNZ_RCVD_AGNCY>");   // M1040_INFLNZ_RCVD_AGNCY - Was influenza vaccine received
        buffer.append("  <M1045_INFLNZ_RSN_NOT_RCVD>").append("</M1045_INFLNZ_RSN_NOT_RCVD>");   // M1045_INFLNZ_RSN_NOT_RCVD - If influenza vaccine not received, state reason
        buffer.append("  <M1050_PPV_RCVD_AGNCY>").append("</M1050_PPV_RCVD_AGNCY>");   // M1050_PPV_RCVD_AGNCY - Was pneumococcal vaccine received
        buffer.append("  <M1055_PPV_RSN_NOT_RCVD_AGNCY>").append("</M1055_PPV_RSN_NOT_RCVD_AGNCY>");   // M1055_PPV_RSN_NOT_RCVD_AGNCY - If pneumococcal vacc not received, state reason
        buffer.append("  <M1100_PTNT_LVG_STUTN>").append("</M1100_PTNT_LVG_STUTN>");   // M1100_PTNT_LVG_STUTN - Patient living situation
        buffer.append("  <M1200_VISION>").append(oasisRecord.getVISION()).append("</M1200_VISION>");   // M1200_VISION - Sensory status: vision
        buffer.append("  <M1210_HEARG_ABLTY>").append("</M1210_HEARG_ABLTY>");   // M1210_HEARG_ABLTY - Ability to hear
        buffer.append("  <M1220_UNDRSTG_VERBAL_CNTNT>").append("</M1220_UNDRSTG_VERBAL_CNTNT>");   // M1220_UNDRSTG_VERBAL_CNTNT - Understanding verbal content in patient's language
        buffer.append("  <M1230_SPEECH>").append("</M1230_SPEECH>");   // M1230_SPEECH - Sensory status: speech
        buffer.append("  <M1240_FRML_PAIN_ASMT>").append("</M1240_FRML_PAIN_ASMT>");   // M1240_FRML_PAIN_ASMT - Has patient had a formal pain assessment
        buffer.append("  <M1242_PAIN_FREQ_ACTVTY_MVMT>").append(oasisRecord.getPAIN_FREQ_ACTVTY_MVMT()).append("</M1242_PAIN_FREQ_ACTVTY_MVMT>");   // M1242_PAIN_FREQ_ACTVTY_MVMT - Freq of pain interfering with pt activity/movement
        buffer.append("  <M1300_PRSR_ULCR_RISK_ASMT>").append("</M1300_PRSR_ULCR_RISK_ASMT>");   // M1300_PRSR_ULCR_RISK_ASMT - Was patient assessed for risk of developing pus
        buffer.append("  <M1302_RISK_OF_PRSR_ULCR>").append("</M1302_RISK_OF_PRSR_ULCR>");   // M1302_RISK_OF_PRSR_ULCR - Does this patient have a risk of developing pus
        buffer.append("  <M1306_UNHLD_STG2_PRSR_ULCR>").append(oasisRecord.getUNHLD_STG2_PRSR_ULCR()).append("</M1306_UNHLD_STG2_PRSR_ULCR>");   // M1306_UNHLD_STG2_PRSR_ULCR - Patient has 1+ unhealed PU at stage 2 or higher
        buffer.append("  <M1307_OLDST_STG2_AT_DSCHRG>").append("</M1307_OLDST_STG2_AT_DSCHRG>");   // M1307_OLDST_STG2_AT_DSCHRG - Status of oldest stage 2 pressure ulcer at disch
        buffer.append("  <M1307_OLDST_STG2_ONST_DT>").append("</M1307_OLDST_STG2_ONST_DT>");   // M1307_OLDST_STG2_ONST_DT - Date of onset of oldest stage 2 pressure ulcer
        buffer.append("  <M1308_NBR_PRSULC_STG2>").append(oasisRecord.getNBR_PRSULC_STG2()).append("</M1308_NBR_PRSULC_STG2>");   // M1308_NBR_PRSULC_STG2 - No. pressure ulcers - stage 2
        buffer.append("  <M1308_NBR_STG2_AT_SOC_ROC>").append(oasisRecord.getNBR_STG2_AT_SOC_ROC()).append("</M1308_NBR_STG2_AT_SOC_ROC>");   // M1308_NBR_STG2_AT_SOC_ROC - Number pu stage 2 at SOC/ROC
        buffer.append("  <M1308_NBR_PRSULC_STG3>").append(oasisRecord.getNBR_PRSULC_STG3()).append("</M1308_NBR_PRSULC_STG3>");   // M1308_NBR_PRSULC_STG3 - No. pressure ulcers - stage 3
        buffer.append("  <M1308_NBR_STG3_AT_SOC_ROC>").append(oasisRecord.getNBR_STG3_AT_SOC_ROC()).append("</M1308_NBR_STG3_AT_SOC_ROC>");   // M1308_NBR_STG3_AT_SOC_ROC - Number PU stage 3 at SOC/ROC
        buffer.append("  <M1308_NBR_PRSULC_STG4>").append(oasisRecord.getNBR_PRSULC_STG4()).append("</M1308_NBR_PRSULC_STG4>");   // M1308_NBR_PRSULC_STG4 - No. pressure ulcers - stage 4
        buffer.append("  <M1308_NBR_STG4_AT_SOC_ROC>").append(oasisRecord.getNBR_STG4_AT_SOC_ROC()).append("</M1308_NBR_STG4_AT_SOC_ROC>");   // M1308_NBR_STG4_AT_SOC_ROC - Number PU stage 4 at SOC/ROC
        buffer.append("  <M1308_NSTG_DRSG>").append(oasisRecord.getNSTG_DRSG()).append("</M1308_NSTG_DRSG>");   // M1308_NSTG_DRSG - Unstageable: non-removable dressing/device
        buffer.append("  <M1308_NSTG_DRSG_SOC_ROC>").append(oasisRecord.getNSTG_DRSG_SOC_ROC()).append("</M1308_NSTG_DRSG_SOC_ROC>");   // M1308_NSTG_DRSG_SOC_ROC - Unstageable: non-removable dressing/device-SOC/ROC
        buffer.append("  <M1308_NSTG_CVRG>").append(oasisRecord.getNSTG_CVRG()).append("</M1308_NSTG_CVRG>");   // M1308_NSTG_CVRG - Unstageable: coverage by slough or eschar
        buffer.append("  <M1308_NSTG_CVRG_SOC_ROC>").append(oasisRecord.getNSTG_CVRG_SOC_ROC()).append("</M1308_NSTG_CVRG_SOC_ROC>");   // M1308_NSTG_CVRG_SOC_ROC - Unstageable: coverage by slough or eschar-SOC/ROC
        buffer.append("  <M1308_NSTG_DEEP_TISUE>").append(oasisRecord.getNSTG_DEEP_TISUE()).append("</M1308_NSTG_DEEP_TISUE>");   // M1308_NSTG_DEEP_TISUE - Unstageable: suspect deep tissue injury
        buffer.append("  <M1308_NSTG_DEEP_TISUE_SOC_ROC>").append("</M1308_NSTG_DEEP_TISUE_SOC_ROC>");   // M1308_NSTG_DEEP_TISUE_SOC_ROC - Unstageable: suspect deep tissue injury-SOC/ROC
        buffer.append("  <M1310_PRSR_ULCR_LNGTH>").append("</M1310_PRSR_ULCR_LNGTH>");   // M1310_PRSR_ULCR_LNGTH - Length of stage 3 or 4 PU with largest area
        buffer.append("  <M1312_PRSR_ULCR_WDTH>").append("</M1312_PRSR_ULCR_WDTH>");   // M1312_PRSR_ULCR_WDTH - Width of stage 3 or 4 PU with largest area
        buffer.append("  <M1314_PRSR_ULCR_DEPTH>").append("</M1314_PRSR_ULCR_DEPTH>");   // M1314_PRSR_ULCR_DEPTH - Depth of stage 3 or 4 PU with largest area
        buffer.append("  <M1320_STUS_PRBLM_PRSR_ULCR>").append(oasisRecord.getSTUS_PRBLM_PRSR_ULCR()).append("</M1320_STUS_PRBLM_PRSR_ULCR>");   // M1320_STUS_PRBLM_PRSR_ULCR - Status of most problematic pressure ulcer
        buffer.append("  <M1322_NBR_PRSULC_STG1>").append(oasisRecord.getNBR_PRSULC_STG1()).append("</M1322_NBR_PRSULC_STG1>");   // M1322_NBR_PRSULC_STG1 - No. pressure ulcers - stage 1
        buffer.append("  <M1324_STG_PRBLM_ULCER>").append(oasisRecord.getSTG_PRBLM_ULCER()).append("</M1324_STG_PRBLM_ULCER>");   // M1324_STG_PRBLM_ULCER - Stage of most problematic pressure ulcer
        buffer.append("  <M1330_STAS_ULCR_PRSNT>").append(oasisRecord.getSTAS_ULCR_PRSNT()).append("</M1330_STAS_ULCR_PRSNT>");   // M1330_STAS_ULCR_PRSNT - Does this patient have a stasis ulcer
        buffer.append("  <M1332_NUM_STAS_ULCR>").append("</M1332_NUM_STAS_ULCR>");   // M1332_NUM_STAS_ULCR - No. stasis ulcers
        buffer.append("  <M1334_STUS_PRBLM_STAS_ULCR>").append(oasisRecord.getSTUS_PRBLM_STAS_ULCR()).append("</M1334_STUS_PRBLM_STAS_ULCR>");   // M1334_STUS_PRBLM_STAS_ULCR - Status of most problematic stasis ulcer
        buffer.append("  <M1340_SRGCL_WND_PRSNT>").append(oasisRecord.getSRGCL_WND_PRSNT()).append("</M1340_SRGCL_WND_PRSNT>");   // M1340_SRGCL_WND_PRSNT - Does this patient have a surgical wound
        buffer.append("  <M1342_STUS_PRBLM_SRGCL_WND>").append(oasisRecord.getSTUS_PRBLM_SRGCL_WND()).append("</M1342_STUS_PRBLM_SRGCL_WND>");   // M1342_STUS_PRBLM_SRGCL_WND - Status of most problematic surgical wound
        buffer.append("  <M1350_LESION_OPEN_WND>").append(oasisRecord.getLESION_OPEN_WND()).append("</M1350_LESION_OPEN_WND>");   // M1350_LESION_OPEN_WND - Has skin lesion or open wound
        buffer.append("  <M1400_WHEN_DYSPNEIC>").append(oasisRecord.getWHEN_DYSPNEIC()).append("</M1400_WHEN_DYSPNEIC>");   // M1400_WHEN_DYSPNEIC - When dyspneic
        buffer.append("  <M1410_RESPTX_OXYGEN>").append("</M1410_RESPTX_OXYGEN>");   // M1410_RESPTX_OXYGEN - Respiratory treatments: oxygen
        buffer.append("  <M1410_RESPTX_VENTILATOR>").append("</M1410_RESPTX_VENTILATOR>");   // M1410_RESPTX_VENTILATOR - Respiratory treatments: ventilator
        buffer.append("  <M1410_RESPTX_AIRPRESS>").append("</M1410_RESPTX_AIRPRESS>");   // M1410_RESPTX_AIRPRESS - Respiratory treatments: airway pressure
        buffer.append("  <M1410_RESPTX_NONE>").append("</M1410_RESPTX_NONE>");   // M1410_RESPTX_NONE - Respiratory treatments: none of the above
        buffer.append("  <M1500_SYMTM_HRT_FAILR_PTNTS>").append("</M1500_SYMTM_HRT_FAILR_PTNTS>");   // M1500_SYMTM_HRT_FAILR_PTNTS - Symptoms in heart failure patients
        buffer.append("  <M1510_HRT_FAILR_NO_ACTN>").append("</M1510_HRT_FAILR_NO_ACTN>");   // M1510_HRT_FAILR_NO_ACTN - Heart failure follow up:  no action
        buffer.append("  <M1510_HRT_FAILR_PHYSN_CNTCT>").append("</M1510_HRT_FAILR_PHYSN_CNTCT>");   // M1510_HRT_FAILR_PHYSN_CNTCT - Heart failure follow up:  physician contacted
        buffer.append("  <M1510_HRT_FAILR_ER_TRTMT>").append("</M1510_HRT_FAILR_ER_TRTMT>");   // M1510_HRT_FAILR_ER_TRTMT - Heart failure follow up:  ER treatment advised
        buffer.append("  <M1510_HRT_FAILR_PHYSN_TRTMT>").append("</M1510_HRT_FAILR_PHYSN_TRTMT>");   // M1510_HRT_FAILR_PHYSN_TRTMT - Heart failure follow up:  phys-ordered treatmnt
        buffer.append("  <M1510_HRT_FAILR_CLNCL_INTRVTN>").append("</M1510_HRT_FAILR_CLNCL_INTRVTN>");   // M1510_HRT_FAILR_CLNCL_INTRVTN - Heart failure follow up: pt educ/other clinical
        buffer.append("  <M1510_HRT_FAILR_CARE_PLAN_CHG>").append("</M1510_HRT_FAILR_CARE_PLAN_CHG>");   // M1510_HRT_FAILR_CARE_PLAN_CHG - Heart failure follow up: change in care plan
        buffer.append("  <M1600_UTI>").append("</M1600_UTI>");   // M1600_UTI - Treated for urinary tract infection past 14 days
        buffer.append("  <M1610_UR_INCONT>").append(oasisRecord.getUR_INCONT()).append("</M1610_UR_INCONT>");   // M1610_UR_INCONT - Urinary incontinence or urinary catheter present
        buffer.append("  <M1615_INCNTNT_TIMING>").append(oasisRecord.getINCNTNT_TIMING()).append("</M1615_INCNTNT_TIMING>");   // M1615_INCNTNT_TIMING - When urinary incontinence occurs
        buffer.append("  <M1620_BWL_INCONT>").append(oasisRecord.getBWL_INCONT()).append("</M1620_BWL_INCONT>");   // M1620_BWL_INCONT - Bowel incontinence frequency
        buffer.append("  <M1630_OSTOMY>").append(oasisRecord.getOSTOMY()).append("</M1630_OSTOMY>");   // M1630_OSTOMY - Ostomy for bowel elimination
        buffer.append("  <M1700_COG_FUNCTION>").append("</M1700_COG_FUNCTION>");   // M1700_COG_FUNCTION - Cognitive functioning
        buffer.append("  <M1710_WHEN_CONFUSED>").append("</M1710_WHEN_CONFUSED>");   // M1710_WHEN_CONFUSED - When confused (reported or observed)
        buffer.append("  <M1720_WHEN_ANXIOUS>").append("</M1720_WHEN_ANXIOUS>");   // M1720_WHEN_ANXIOUS - When anxious (reported or observed)
        buffer.append("  <M1730_STDZ_DPRSN_SCRNG>").append("</M1730_STDZ_DPRSN_SCRNG>");   // M1730_STDZ_DPRSN_SCRNG - Has the patient been screened for depression
        buffer.append("  <M1730_PHQ2_LACK_INTRST>").append("</M1730_PHQ2_LACK_INTRST>");   // M1730_PHQ2_LACK_INTRST - PHQ2: little interest or pleasure in doing things
        buffer.append("  <M1730_PHQ2_DPRSN>").append("</M1730_PHQ2_DPRSN>");   // M1730_PHQ2_DPRSN - PHQ2: feeling down, depressed or hopeless
        buffer.append("  <M1740_BD_MEM_DEFICIT>").append("</M1740_BD_MEM_DEFICIT>");   // M1740_BD_MEM_DEFICIT - Behavior demonstrated: memory deficit
        buffer.append("  <M1740_BD_IMP_DECISN>").append("</M1740_BD_IMP_DECISN>");   // M1740_BD_IMP_DECISN - Behavior demonstrated: impaired decision-making
        buffer.append("  <M1740_BD_VERBAL>").append("</M1740_BD_VERBAL>");   // M1740_BD_VERBAL - Behavior demonstrated: verbal disruption
        buffer.append("  <M1740_BD_PHYSICAL>").append("</M1740_BD_PHYSICAL>");   // M1740_BD_PHYSICAL - Behavior demonstrated: physical aggression
        buffer.append("  <M1740_BD_SOC_INAPPRO>").append("</M1740_BD_SOC_INAPPRO>");   // M1740_BD_SOC_INAPPRO - Behavior demonstrated: socially inappropriate
        buffer.append("  <M1740_BD_DELUSIONS>").append("</M1740_BD_DELUSIONS>");   // M1740_BD_DELUSIONS - Behavior demonstrated: delusions
        buffer.append("  <M1740_BD_NONE>").append("</M1740_BD_NONE>");   // M1740_BD_NONE - Behavior demonstrated: none of the above
        buffer.append("  <M1745_BEH_PROB_FREQ>").append("</M1745_BEH_PROB_FREQ>");   // M1745_BEH_PROB_FREQ - Frequency of behavior problems
        buffer.append("  <M1750_REC_PSYCH_NURS>").append("</M1750_REC_PSYCH_NURS>");   // M1750_REC_PSYCH_NURS - Receives psychiatric nursing
        buffer.append("  <M1800_CUR_GROOMING>").append("</M1800_CUR_GROOMING>");   // M1800_CUR_GROOMING - Current: grooming
        buffer.append("  <M1810_CUR_DRESS_UPPER>").append("</M1810_CUR_DRESS_UPPER>");   // M1810_CUR_DRESS_UPPER - Current: dress upper body
        buffer.append("  <M1820_CUR_DRESS_LOWER>").append("</M1820_CUR_DRESS_LOWER>");   // M1820_CUR_DRESS_LOWER - Current: dress lower body
        buffer.append("  <M1830_CRNT_BATHG>").append(oasisRecord.getCRNT_BATHG()).append("</M1830_CRNT_BATHG>");   // M1830_CRNT_BATHG - Current: bathing
        buffer.append("  <M1840_CUR_TOILTG>").append("</M1840_CUR_TOILTG>");   // M1840_CUR_TOILTG - Current: toileting
        buffer.append("  <M1845_CUR_TOILTG_HYGN>").append("</M1845_CUR_TOILTG_HYGN>");   // M1845_CUR_TOILTG_HYGN - Current: toileting hygiene
        buffer.append("  <M1850_CUR_TRNSFRNG>").append("</M1850_CUR_TRNSFRNG>");   // M1850_CUR_TRNSFRNG - Current: transferring
        buffer.append("  <M1860_CRNT_AMBLTN>").append(oasisRecord.getCRNT_AMBLTN()).append("</M1860_CRNT_AMBLTN>");   // M1860_CRNT_AMBLTN - Current: ambulation
        buffer.append("  <M1870_CUR_FEEDING>").append("</M1870_CUR_FEEDING>");   // M1870_CUR_FEEDING - Current: feeding
        buffer.append("  <M1880_CUR_PREP_LT_MEALS>").append("</M1880_CUR_PREP_LT_MEALS>");   // M1880_CUR_PREP_LT_MEALS - Current: prepare light meals
        buffer.append("  <M1890_CUR_PHONE_USE>").append("</M1890_CUR_PHONE_USE>");   // M1890_CUR_PHONE_USE - Current: telephone use
        buffer.append("  <M1900_PRIOR_ADLIADL_SELF>").append("</M1900_PRIOR_ADLIADL_SELF>");   // M1900_PRIOR_ADLIADL_SELF - Prior functioning ADL/IADL: self-care
        buffer.append("  <M1900_PRIOR_ADLIADL_AMBLTN>").append("</M1900_PRIOR_ADLIADL_AMBLTN>");   // M1900_PRIOR_ADLIADL_AMBLTN - Prior functioning ADL/IADL: ambulation
        buffer.append("  <M1900_PRIOR_ADLIADL_TRNSFR>").append("</M1900_PRIOR_ADLIADL_TRNSFR>");   // M1900_PRIOR_ADLIADL_TRNSFR - Prior functioning ADL/IADL: transfer
        buffer.append("  <M1900_PRIOR_ADLIADL_HSEHOLD>").append("</M1900_PRIOR_ADLIADL_HSEHOLD>");   // M1900_PRIOR_ADLIADL_HSEHOLD - Prior functioning ADL/IADL: household tasks
        buffer.append("  <M1910_MLT_FCTR_FALL_RISK_ASMT>").append("</M1910_MLT_FCTR_FALL_RISK_ASMT>");   // M1910_MLT_FCTR_FALL_RISK_ASMT - Has patient had a multi-factor fall risk asmt
        buffer.append("  <M2000_DRUG_RGMN_RVW>").append(oasisRecord.getDRUG_RGMN_RVW()).append("</M2000_DRUG_RGMN_RVW>");   // M2000_DRUG_RGMN_RVW - Drug regimen review
        buffer.append("  <M2002_MDCTN_FLWP>").append("</M2002_MDCTN_FLWP>");   // M2002_MDCTN_FLWP - Medication follow-up
        buffer.append("  <M2004_MDCTN_INTRVTN>").append("</M2004_MDCTN_INTRVTN>");   // M2004_MDCTN_INTRVTN - Medication intervention
        buffer.append("  <M2010_HIGH_RISK_DRUG_EDCTN>").append("</M2010_HIGH_RISK_DRUG_EDCTN>");   // M2010_HIGH_RISK_DRUG_EDCTN - Patient/caregiver high risk drug education
        buffer.append("  <M2015_DRUG_EDCTN_INTRVTN>").append("</M2015_DRUG_EDCTN_INTRVTN>");   // M2015_DRUG_EDCTN_INTRVTN - Patient/caregiver drug education intervention
        buffer.append("  <M2020_CRNT_MGMT_ORAL_MDCTN>").append("</M2020_CRNT_MGMT_ORAL_MDCTN>");   // M2020_CRNT_MGMT_ORAL_MDCTN - Current: management of oral medications
        buffer.append("  <M2030_CRNT_MGMT_INJCTN_MDCTN>").append(oasisRecord.getCRNT_MGMT_INJCTN_MDCTN()).append("</M2030_CRNT_MGMT_INJCTN_MDCTN>");   // M2030_CRNT_MGMT_INJCTN_MDCTN - Current: management of injectable medications
        buffer.append("  <M2040_PRIOR_MGMT_ORAL_MDCTN>").append("</M2040_PRIOR_MGMT_ORAL_MDCTN>");   // M2040_PRIOR_MGMT_ORAL_MDCTN - Prior med mgmt: oral medications
        buffer.append("  <M2040_PRIOR_MGMT_INJCTN_MDCTN>").append("</M2040_PRIOR_MGMT_INJCTN_MDCTN>");   // M2040_PRIOR_MGMT_INJCTN_MDCTN - Prior med mgmt: injectable medications
        buffer.append("  <M2100_CARE_TYPE_SRC_ADL>").append("</M2100_CARE_TYPE_SRC_ADL>");   // M2100_CARE_TYPE_SRC_ADL - Care mgmt, types/sources: ADL
        buffer.append("  <M2100_CARE_TYPE_SRC_IADL>").append("</M2100_CARE_TYPE_SRC_IADL>");   // M2100_CARE_TYPE_SRC_IADL - Care mgmt, types/sources: IADL
        buffer.append("  <M2100_CARE_TYPE_SRC_MDCTN>").append("</M2100_CARE_TYPE_SRC_MDCTN>");   // M2100_CARE_TYPE_SRC_MDCTN - Care mgmt, types/sources: med admin
        buffer.append("  <M2100_CARE_TYPE_SRC_PRCDR>").append("</M2100_CARE_TYPE_SRC_PRCDR>");   // M2100_CARE_TYPE_SRC_PRCDR - Care mgmt, types/sources: med procs tx
        buffer.append("  <M2100_CARE_TYPE_SRC_EQUIP>").append("</M2100_CARE_TYPE_SRC_EQUIP>");   // M2100_CARE_TYPE_SRC_EQUIP - Care mgmt, types/sources: equipment
        buffer.append("  <M2100_CARE_TYPE_SRC_SPRVSN>").append("</M2100_CARE_TYPE_SRC_SPRVSN>");   // M2100_CARE_TYPE_SRC_SPRVSN - Care mgmt, types/sources: supervision and safety
        buffer.append("  <M2100_CARE_TYPE_SRC_ADVCY>").append("</M2100_CARE_TYPE_SRC_ADVCY>");   // M2100_CARE_TYPE_SRC_ADVCY - Care mgmt, types/sources: advocacy or facilitation
        buffer.append("  <M2110_ADL_IADL_ASTNC_FREQ>").append("</M2110_ADL_IADL_ASTNC_FREQ>");   // M2110_ADL_IADL_ASTNC_FREQ - How often recv ADL or IADL assistance from any
        buffer.append("  <M2200_THER_NEED_NUM>").append("</M2200_THER_NEED_NUM>");   // M2200_THER_NEED_NUM - Therapy need: number of visits
        buffer.append("  <M2200_THER_NEED_NA>").append(oasisRecord.getTHER_NEED_NA()).append("</M2200_THER_NEED_NA>");   // M2200_THER_NEED_NA - Therapy need: not applicable
        buffer.append("  <M2250_PLAN_SMRY_PTNT_SPECF>").append("</M2250_PLAN_SMRY_PTNT_SPECF>");   // M2250_PLAN_SMRY_PTNT_SPECF - Plan of care synopsis: patient specific parameters
        buffer.append("  <M2250_PLAN_SMRY_DBTS_FT_CARE>").append("</M2250_PLAN_SMRY_DBTS_FT_CARE>");   // M2250_PLAN_SMRY_DBTS_FT_CARE - Plan of care synopsis: diabetic foot care
        buffer.append("  <M2250_PLAN_SMRY_FALL_PRVNT>").append("</M2250_PLAN_SMRY_FALL_PRVNT>");   // M2250_PLAN_SMRY_FALL_PRVNT - Plan of care synopsis: falls prevention
        buffer.append("  <M2250_PLAN_SMRY_DPRSN_INTRVTN>").append("</M2250_PLAN_SMRY_DPRSN_INTRVTN>");   // M2250_PLAN_SMRY_DPRSN_INTRVTN - Plan of care synopsis: depression interventions
        buffer.append("  <M2250_PLAN_SMRY_PAIN_INTRVTN>").append("</M2250_PLAN_SMRY_PAIN_INTRVTN>");   // M2250_PLAN_SMRY_PAIN_INTRVTN - Plan of care synopsis: pain interventions
        buffer.append("  <M2250_PLAN_SMRY_PRSULC_PRVNT>").append("</M2250_PLAN_SMRY_PRSULC_PRVNT>");   // M2250_PLAN_SMRY_PRSULC_PRVNT - Plan of care synopsis: PU prevention
        buffer.append("  <M2250_PLAN_SMRY_PRSULC_TRTMT>").append("</M2250_PLAN_SMRY_PRSULC_TRTMT>");   // M2250_PLAN_SMRY_PRSULC_TRTMT - Plan of care synopsis: PU moist treatment
        buffer.append("  <M2300_EMER_USE_AFTR_LAST_ASMT>").append("</M2300_EMER_USE_AFTR_LAST_ASMT>");   // M2300_EMER_USE_AFTR_LAST_ASMT - Emergent care: use since last OASIS
        buffer.append("  <M2310_ECR_MEDICATION>").append("</M2310_ECR_MEDICATION>");   // M2310_ECR_MEDICATION - Emergent care reason: medication
        buffer.append("  <M2310_ECR_INJRY_BY_FALL>").append("</M2310_ECR_INJRY_BY_FALL>");   // M2310_ECR_INJRY_BY_FALL - Emergent care reason: injury caused by fall
        buffer.append("  <M2310_ECR_RSPRTRY_INFCTN>").append("</M2310_ECR_RSPRTRY_INFCTN>");   // M2310_ECR_RSPRTRY_INFCTN - Emergent care reason: respiratory infection
        buffer.append("  <M2310_ECR_RSPRTRY_OTHR>").append("</M2310_ECR_RSPRTRY_OTHR>");   // M2310_ECR_RSPRTRY_OTHR - Emergent care reason: respiratory other
        buffer.append("  <M2310_ECR_HRT_FAILR>").append("</M2310_ECR_HRT_FAILR>");   // M2310_ECR_HRT_FAILR - Emergent care reason: heart failure
        buffer.append("  <M2310_ECR_CRDC_DSRTHM>").append("</M2310_ECR_CRDC_DSRTHM>");   // M2310_ECR_CRDC_DSRTHM - Emergent care reason: cardiac dysrhythmia
        buffer.append("  <M2310_ECR_MI_CHST_PAIN>").append("</M2310_ECR_MI_CHST_PAIN>");   // M2310_ECR_MI_CHST_PAIN - Emergent care reason: myocard infarct/chest pain
        buffer.append("  <M2310_ECR_OTHR_HRT_DEASE>").append("</M2310_ECR_OTHR_HRT_DEASE>");   // M2310_ECR_OTHR_HRT_DEASE - Emergent care reason: other heart disease
        buffer.append("  <M2310_ECR_STROKE_TIA>").append("</M2310_ECR_STROKE_TIA>");   // M2310_ECR_STROKE_TIA - Emergent care reason: stroke (CVA) or TIA
        buffer.append("  <M2310_ECR_HYPOGLYC>").append("</M2310_ECR_HYPOGLYC>");   // M2310_ECR_HYPOGLYC - Emergent care reason: hypoglycemia
        buffer.append("  <M2310_ECR_GI_PRBLM>").append("</M2310_ECR_GI_PRBLM>");   // M2310_ECR_GI_PRBLM - Emergent care: GI bleed/obstruct/constip/impact
        buffer.append("  <M2310_ECR_DHYDRTN_MALNTR>").append("</M2310_ECR_DHYDRTN_MALNTR>");   // M2310_ECR_DHYDRTN_MALNTR - Emergent care reason: dehydration, malnutrition
        buffer.append("  <M2310_ECR_UTI>").append("</M2310_ECR_UTI>");   // M2310_ECR_UTI - Emergent care reason: urinary tract infection
        buffer.append("  <M2310_ECR_CTHTR_CMPLCTN>").append("</M2310_ECR_CTHTR_CMPLCTN>");   // M2310_ECR_CTHTR_CMPLCTN - Emergent care reason: IV catheter infect/complic
        buffer.append("  <M2310_ECR_WND_INFCTN_DTRORTN>").append("</M2310_ECR_WND_INFCTN_DTRORTN>");   // M2310_ECR_WND_INFCTN_DTRORTN - Emergent care reason: wound infect/deterioration
        buffer.append("  <M2310_ECR_UNCNTLD_PAIN>").append("</M2310_ECR_UNCNTLD_PAIN>");   // M2310_ECR_UNCNTLD_PAIN - Emergent care reason: uncontrolled pain
        buffer.append("  <M2310_ECR_MENTL_BHVRL_PRBLM>").append("</M2310_ECR_MENTL_BHVRL_PRBLM>");   // M2310_ECR_MENTL_BHVRL_PRBLM - Emergent care reason: acute mental/behav problem
        buffer.append("  <M2310_ECR_DVT_PULMNRY>").append("</M2310_ECR_DVT_PULMNRY>");   // M2310_ECR_DVT_PULMNRY - Emergent care reason: deep vein thromb/pulm embol
        buffer.append("  <M2310_ECR_OTHER>").append("</M2310_ECR_OTHER>");   // M2310_ECR_OTHER - Emergent care reason: other than above
        buffer.append("  <M2310_ECR_UNKNOWN>").append("</M2310_ECR_UNKNOWN>");   // M2310_ECR_UNKNOWN - Emergent care reason: unknown
        buffer.append("  <M2400_INTRVTN_SMRY_DBTS_FT>").append("</M2400_INTRVTN_SMRY_DBTS_FT>");   // M2400_INTRVTN_SMRY_DBTS_FT - Intervention synopsis: diabetic foot care
        buffer.append("  <M2400_INTRVTN_SMRY_FALL_PRVNT>").append("</M2400_INTRVTN_SMRY_FALL_PRVNT>");   // M2400_INTRVTN_SMRY_FALL_PRVNT - Intervention synopsis: falls prevention
        buffer.append("  <M2400_INTRVTN_SMRY_DPRSN>").append("</M2400_INTRVTN_SMRY_DPRSN>");   // M2400_INTRVTN_SMRY_DPRSN - Intervention synopsis: depression intervention
        buffer.append("  <M2400_INTRVTN_SMRY_PAIN_MNTR>").append("</M2400_INTRVTN_SMRY_PAIN_MNTR>");   // M2400_INTRVTN_SMRY_PAIN_MNTR - Intervention synopsis: monitor and mitigate pain
        buffer.append("  <M2400_INTRVTN_SMRY_PRSULC_PRVN>").append("</M2400_INTRVTN_SMRY_PRSULC_PRVN>");   // M2400_INTRVTN_SMRY_PRSULC_PRVN - Intervention synopsis: prevent pressure ulcers
        buffer.append("  <M2400_INTRVTN_SMRY_PRSULC_WET>").append("</M2400_INTRVTN_SMRY_PRSULC_WET>");   // M2400_INTRVTN_SMRY_PRSULC_WET - Intervention synopsis: PU moist wound treatment
        buffer.append("  <M2410_INPAT_FACILITY>").append("</M2410_INPAT_FACILITY>");   // M2410_INPAT_FACILITY - Inpatient facility
        buffer.append("  <M2420_DSCHRG_DISP>").append("</M2420_DSCHRG_DISP>");   // M2420_DSCHRG_DISP - Discharge disposition
        buffer.append("  <M2430_HOSP_MED>").append("</M2430_HOSP_MED>");   // M2430_HOSP_MED - Hospitalized: medication
        buffer.append("  <M2430_HOSP_INJRY_BY_FALL>").append("</M2430_HOSP_INJRY_BY_FALL>");   // M2430_HOSP_INJRY_BY_FALL - Hospitalized: injury caused by fall
        buffer.append("  <M2430_HOSP_RSPRTRY_INFCTN>").append("</M2430_HOSP_RSPRTRY_INFCTN>");   // M2430_HOSP_RSPRTRY_INFCTN - Hospitalized: respiratory
        buffer.append("  <M2430_HOSP_RSPRTRY_OTHR>").append("</M2430_HOSP_RSPRTRY_OTHR>");   // M2430_HOSP_RSPRTRY_OTHR - Hospitalized: other respiratory
        buffer.append("  <M2430_HOSP_HRT_FAILR>").append("</M2430_HOSP_HRT_FAILR>");   // M2430_HOSP_HRT_FAILR - Hospitalized: heart failure
        buffer.append("  <M2430_HOSP_CRDC_DSRTHM>").append("</M2430_HOSP_CRDC_DSRTHM>");   // M2430_HOSP_CRDC_DSRTHM - Hospitalized: cardiac dysrhythmia
        buffer.append("  <M2430_HOSP_MI_CHST_PAIN>").append("</M2430_HOSP_MI_CHST_PAIN>");   // M2430_HOSP_MI_CHST_PAIN - Hospitalized: myocardial infarction or chest pain
        buffer.append("  <M2430_HOSP_OTHR_HRT_DEASE>").append("</M2430_HOSP_OTHR_HRT_DEASE>");   // M2430_HOSP_OTHR_HRT_DEASE - Hospitalized: other heart disease
        buffer.append("  <M2430_HOSP_STROKE_TIA>").append("</M2430_HOSP_STROKE_TIA>");   // M2430_HOSP_STROKE_TIA - Hospitalized: stroke (CVA) or TIA
        buffer.append("  <M2430_HOSP_HYPOGLYC>").append("</M2430_HOSP_HYPOGLYC>");   // M2430_HOSP_HYPOGLYC - Hospitalized: hypoglycemia
        buffer.append("  <M2430_HOSP_GI_PRBLM>").append("</M2430_HOSP_GI_PRBLM>");   // M2430_HOSP_GI_PRBLM - Hospitalized: GI bleed/obstruct/constip/impact
        buffer.append("  <M2430_HOSP_DHYDRTN_MALNTR>").append("</M2430_HOSP_DHYDRTN_MALNTR>");   // M2430_HOSP_DHYDRTN_MALNTR - Hospitalized: dehydration, malnutrition
        buffer.append("  <M2430_HOSP_UR_TRACT>").append("</M2430_HOSP_UR_TRACT>");   // M2430_HOSP_UR_TRACT - Hospitalized: urinary tract infection
        buffer.append("  <M2430_HOSP_CTHTR_CMPLCTN>").append("</M2430_HOSP_CTHTR_CMPLCTN>");   // M2430_HOSP_CTHTR_CMPLCTN - Hospitalized: IV catheter infect/complic
        buffer.append("  <M2430_HOSP_WND_INFCTN>").append("</M2430_HOSP_WND_INFCTN>");   // M2430_HOSP_WND_INFCTN - Hospitalized: wound infect/deterioration
        buffer.append("  <M2430_HOSP_PAIN>").append("</M2430_HOSP_PAIN>");   // M2430_HOSP_PAIN - Hospitalized: pain
        buffer.append("  <M2430_HOSP_MENTL_BHVRL_PRBLM>").append("</M2430_HOSP_MENTL_BHVRL_PRBLM>");   // M2430_HOSP_MENTL_BHVRL_PRBLM - Hospitalized: acute mental/behav problem
        buffer.append("  <M2430_HOSP_DVT_PULMNRY>").append("</M2430_HOSP_DVT_PULMNRY>");   // M2430_HOSP_DVT_PULMNRY - Hospitalized: deep vein thromb/pulm embol
        buffer.append("  <M2430_HOSP_SCHLD_TRTMT>").append("</M2430_HOSP_SCHLD_TRTMT>");   // M2430_HOSP_SCHLD_TRTMT - Hospitalized: scheduled treatment or procedure
        buffer.append("  <M2430_HOSP_OTHER>").append("</M2430_HOSP_OTHER>");   // M2430_HOSP_OTHER - Hospitalized: other
        buffer.append("  <M2430_HOSP_UK>").append("</M2430_HOSP_UK>");   // M2430_HOSP_UK - Hospitalized: UK
        buffer.append("  <M2440_NH_THERAPY>").append("</M2440_NH_THERAPY>");   // M2440_NH_THERAPY - Admitted to nursing home: therapy
        buffer.append("  <M2440_NH_RESPITE>").append("</M2440_NH_RESPITE>");   // M2440_NH_RESPITE - Admitted to nursing home: respite
        buffer.append("  <M2440_NH_HOSPICE>").append("</M2440_NH_HOSPICE>");   // M2440_NH_HOSPICE - Admitted to nursing home: hospice
        buffer.append("  <M2440_NH_PERMANENT>").append("</M2440_NH_PERMANENT>");   // M2440_NH_PERMANENT - Admitted to nursing home: permanent placement
        buffer.append("  <M2440_NH_UNSAFE_HOME>").append("</M2440_NH_UNSAFE_HOME>");   // M2440_NH_UNSAFE_HOME - Admitted to nursing home: unsafe at home
        buffer.append("  <M2440_NH_OTHER>").append("</M2440_NH_OTHER>");   // M2440_NH_OTHER - Admitted to nursing home: other
        buffer.append("  <M2440_NH_UNKNOWN>").append("</M2440_NH_UNKNOWN>");   // M2440_NH_UNKNOWN - Admitted to nursing home: unknown
        buffer.append("  <M0903_LAST_HOME_VISIT>").append("</M0903_LAST_HOME_VISIT>");   // M0903_LAST_HOME_VISIT - Date of last home visit
        buffer.append("  <M0906_DC_TRAN_DTH_DT>").append("</M0906_DC_TRAN_DTH_DT>");   // M0906_DC_TRAN_DTH_DT - Discharge, transfer, death date
        buffer.append("  <CONTROL_ITEMS_FILLER>").append("</CONTROL_ITEMS_FILLER>");   // CONTROL_ITEMS_FILLER - Control items filler
        buffer.append("  <ASMT_ITEMS_FILLER>").append("</ASMT_ITEMS_FILLER>");   // ASMT_ITEMS_FILLER - Assessment items filler
        buffer.append("  <LEGACY_ITEMS_FILLER>").append("</LEGACY_ITEMS_FILLER>");   // LEGACY_ITEMS_FILLER - Discontinued OASIS-B1 and C items.
        buffer.append("  <CALCULATED_ITEMS_FILLER>").append("</CALCULATED_ITEMS_FILLER>");   // CALCULATED_ITEMS_FILLER - Calculated items filler
        buffer.append("  <HHA_ASMT_INT_ID>").append("</HHA_ASMT_INT_ID>");   // HHA_ASMT_INT_ID - Assessment internal ID
        buffer.append("  <ORIG_ASMT_INT_ID>").append("</ORIG_ASMT_INT_ID>");   // ORIG_ASMT_INT_ID - Original assessment ID
        buffer.append("  <RES_INT_ID>").append("</RES_INT_ID>");   // RES_INT_ID - Resident internal ID
        buffer.append("  <ASMT_EFF_DATE>").append("</ASMT_EFF_DATE>");   // ASMT_EFF_DATE - Effective date
        buffer.append("  <BRANCH_IDENTIFIER>").append("</BRANCH_IDENTIFIER>");   // BRANCH_IDENTIFIER - Branch internal ID
        buffer.append("  <FAC_INT_ID>").append("</FAC_INT_ID>");   // FAC_INT_ID - Facility internal ID
        buffer.append("  <SUBMISSION_ID>").append("</SUBMISSION_ID>");   // SUBMISSION_ID - Submission ID
        buffer.append("  <SUBMISSION_DATE>").append("</SUBMISSION_DATE>");   // SUBMISSION_DATE - Submission date
        buffer.append("  <SUBMISSION_COMPLETE_DATE>").append("</SUBMISSION_COMPLETE_DATE>");   // SUBMISSION_COMPLETE_DATE - Submission processing completion date
        buffer.append("  <SUBMITTING_USER_ID>").append("</SUBMITTING_USER_ID>");   // SUBMITTING_USER_ID - Submitter user ID
        buffer.append("  <RES_MATCH_CRITERIA>").append("</RES_MATCH_CRITERIA>");   // RES_MATCH_CRITERIA - Resident matching criteria
        buffer.append("  <RESIDENT_AGE>").append("</RESIDENT_AGE>");   // RESIDENT_AGE - Age of resident on the effective date
        buffer.append("  <BIRTHDATE_SUBM_IND>").append("</BIRTHDATE_SUBM_IND>");   // BIRTHDATE_SUBM_IND - Birth date submit code
        buffer.append("  <CALC_HIPPS_CODE>").append("</CALC_HIPPS_CODE>");   // CALC_HIPPS_CODE - HIPPS group code: recalculated
        buffer.append("  <CALC_HIPPS_VERSION>").append("</CALC_HIPPS_VERSION>");   // CALC_HIPPS_VERSION - HIPPS version code: recalculated

        buffer.append("</ASSESSMENT>");

        return buffer;
    }

    /**
     * This will remove the carets in the OASIS-C XML/Extended version and
     * convert them to spaces allowing the validation and scoring to work with
     * consistent data for the OASIS-C record.  
     * 
     * Further, some variables are allowed to be less then
     * the expected length, so this will also right justify those items
     *
     * @param record
     */
    public static void postProcessRecord(HomeHealthRecordIF record) {
        final Oasis_C_Record oasisRecord = (Oasis_C_Record) record;

        /*
         * due to edit -3100 of the spec some values may be less length than
         * the field requires so they need to be adjusted
        */
        HomeHealthRecordUtil.justifyOasisCValues(oasisRecord);
        
        
        // convet the Oasis C 2.10 values to Oasis C 2.00
        oasisRecord.setNBR_PRSULC_STG2(convertCaretSpace(oasisRecord.getNBR_PRSULC_STG2(), 2));   // M1308_NBR_PRSULC_STG2 - No. pressure ulcers - stage 2
        oasisRecord.setNBR_STG2_AT_SOC_ROC(convertCaretSpace(oasisRecord.getNBR_STG2_AT_SOC_ROC(), 2));   // M1308_NBR_STG2_AT_SOC_ROC - Number pu stage 2 at SOC/ROC
        oasisRecord.setNBR_PRSULC_STG3(convertCaretSpace(oasisRecord.getNBR_PRSULC_STG3(), 2));   // M1308_NBR_PRSULC_STG3 - No. pressure ulcers - stage 3
        oasisRecord.setNBR_STG3_AT_SOC_ROC(convertCaretSpace(oasisRecord.getNBR_STG3_AT_SOC_ROC(), 2));   // M1308_NBR_STG3_AT_SOC_ROC - Number PU stage 3 at SOC/ROC
        oasisRecord.setNBR_PRSULC_STG4(convertCaretSpace(oasisRecord.getNBR_PRSULC_STG4(), 2));   // M1308_NBR_PRSULC_STG4 - No. pressure ulcers - stage 4
        oasisRecord.setNBR_STG4_AT_SOC_ROC(convertCaretSpace(oasisRecord.getNBR_STG4_AT_SOC_ROC(), 2));   // M1308_NBR_STG4_AT_SOC_ROC - Number PU stage 4 at SOC/ROC
        oasisRecord.setNSTG_DRSG(convertCaretSpace(oasisRecord.getNSTG_DRSG(), 2));   // M1308_NSTG_DRSG - Unstageable: non-removable dressing/device
        oasisRecord.setNSTG_DRSG_SOC_ROC(convertCaretSpace(oasisRecord.getNSTG_DRSG_SOC_ROC(), 2));   // M1308_NSTG_DRSG_SOC_ROC - Unstageable: non-removable dressing/device-SOC/ROC
        oasisRecord.setNSTG_CVRG(convertCaretSpace(oasisRecord.getNSTG_CVRG(), 2));   // M1308_NSTG_CVRG - Unstageable: coverage by slough or eschar
        oasisRecord.setNSTG_CVRG_SOC_ROC(convertCaretSpace(oasisRecord.getNSTG_CVRG_SOC_ROC(), 2));   // M1308_NSTG_CVRG_SOC_ROC - Unstageable: coverage by slough or eschar-SOC/ROC
        oasisRecord.setNSTG_DEEP_TISUE(convertCaretSpace(oasisRecord.getNSTG_DEEP_TISUE(), 2));   // M1308_NSTG_DEEP_TISUE - Unstageable: suspect deep tissue injury
        oasisRecord.setNSTG_DEEP_TISSUE_SOC_ROC(convertCaretSpace(oasisRecord.getNSTG_DEEP_TISSUE_SOC_ROC(), 2));// M1308_NSTG_DEEP_TISUE_SOC_ROC - Unstageable: suspect deep tissue injury-SOC/ROC
        oasisRecord.setPRSR_ULCR_LNGTH(convertCaretSpace(oasisRecord.getPRSR_ULCR_LNGTH(), 4));// M1310_PRSR_ULCR_LNGTH - Length of stage 3 or 4 PU with largest area
        oasisRecord.setPRSR_ULCR_WDTH(convertCaretSpace(oasisRecord.getPRSR_ULCR_WDTH(), 4));// M1312_PRSR_ULCR_WDTH - Width of stage 3 or 4 PU with largest area
        oasisRecord.setPRSR_ULCR_DEPTH(convertCaretSpace(oasisRecord.getPRSR_ULCR_DEPTH(), 4));// M1314_PRSR_ULCR_DEPTH - Depth of stage 3 or 4 PU with largest area
        oasisRecord.setSTUS_PRBLM_PRSR_ULCR(convertCaretSpace(oasisRecord.getSTUS_PRBLM_PRSR_ULCR(), 2));   // M1320_STUS_PRBLM_PRSR_ULCR - Status of most problematic pressure ulcer
        oasisRecord.setNBR_STAS_ULCR(convertCaretSpace(oasisRecord.getNBR_STAS_ULCR(), 2));// M1332_NUM_STAS_ULCR - No. stasis ulcers
        oasisRecord.setSTUS_PRBLM_STAS_ULCR(convertCaretSpace(oasisRecord.getSTUS_PRBLM_STAS_ULCR(), 2));   // M1334_STUS_PRBLM_STAS_ULCR - Status of most problematic stasis ulcer		
        oasisRecord.setSTUS_PRBLM_SRGCL_WND(convertCaretSpace(oasisRecord.getSTUS_PRBLM_SRGCL_WND(), 2));   // M1342_STUS_PRBLM_SRGCL_WND - Status of most problematic surgical wound
        oasisRecord.setINCNTNT_TIMING(convertCaretSpace(oasisRecord.getINCNTNT_TIMING(), 2));   // M1615_INCNTNT_TIMING - When urinary incontinence occurs
        oasisRecord.setCRNT_MGMT_INJCTN_MDCTN(convertCaretSpace(oasisRecord.getCRNT_MGMT_INJCTN_MDCTN(), 2));   // M2030_CRNT_MGMT_INJCTN_MDCTN - Current: management of injectable medications
        
    }

    /**
     * Checks to see if the first character is a caret and if so, it creates a
     * new string replacing the caret with a space, otherwise it returns the
     * original string
     *
     * @param str
     * @return original string or new string if the string starts with a caret
     */
    final public static String convertCaretSpace(String str, int length) {
        if (str == null || str.charAt(0) == '^') {
            char chars[] = new char[length];
            for (int idx = 0; idx < length; ++idx) {
                chars[idx] = ' ';
            }
            str = new String(chars);
        }
        return str;
    }

}
