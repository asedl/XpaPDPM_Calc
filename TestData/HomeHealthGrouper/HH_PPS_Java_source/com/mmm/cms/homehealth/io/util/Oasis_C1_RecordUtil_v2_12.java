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
import com.mmm.cms.homehealth.io.record.Oasis_C1_Record_2_12;
import static com.mmm.cms.homehealth.io.HomeHealthRecordUtil.formatDxCode_C1;
import static com.mmm.cms.homehealth.io.HomeHealthRecordUtil.parseDxCode;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.util.OasisCalendarFormatter;
import java.text.ParseException;

/**
 * Converts data to/from OASIS C1 2.12 flat file format
 * 
 * @author 3M Health Information Systems for CMS Home Health
 */
public class Oasis_C1_RecordUtil_v2_12
        extends AbstractRecordConverter
        implements OasisRecordConverterIF {

    final static String OASIS_ITEM_ASMT_SYS_CD = "ASMT_SYS_CD";
    final static String OASIS_ITEM_TRANS_TYPE_CD = "TRANS_TYPE_CD";
    final static String OASIS_ITEM_ITM_SBST_CD = "ITM_SBST_CD";
    final static String OASIS_ITEM_ITM_SET_VRSN_CD = "ITM_SET_VRSN_CD";
    final static String OASIS_ITEM_SPEC_VRSN_CD = "SPEC_VRSN_CD";
    final static String OASIS_ITEM_CORRECTION_NUM = "CORRECTION_NUM";
    final static String OASIS_ITEM_STATE_CD = "STATE_CD";
    final static String OASIS_ITEM_HHA_AGENCY_ID = "HHA_AGENCY_ID";
    final static String OASIS_ITEM_NATL_PRVDR_ID = "NATL_PRVDR_ID";
    final static String OASIS_ITEM_SFW_ID = "SFW_ID";
    final static String OASIS_ITEM_SFW_NAME = "SFW_NAME";
    final static String OASIS_ITEM_SFW_EMAIL_ADR = "SFW_EMAIL_ADR";
    final static String OASIS_ITEM_SFW_PROD_NAME = "SFW_PROD_NAME";
    final static String OASIS_ITEM_SFW_PROD_VRSN_CD = "SFW_PROD_VRSN_CD";
    final static String OASIS_ITEM_ACY_DOC_CD = "ACY_DOC_CD";
    final static String OASIS_ITEM_SUBM_HIPPS_CODE = "SUBM_HIPPS_CODE";
    final static String OASIS_ITEM_SUBM_HIPPS_VERSION = "SUBM_HIPPS_VERSION";
    final static String OASIS_ITEM_M0010_CCN = "M0010_CCN";
    final static String OASIS_ITEM_M0014_BRANCH_STATE = "M0014_BRANCH_STATE";
    final static String OASIS_ITEM_M0016_BRANCH_ID = "M0016_BRANCH_ID";
    final static String OASIS_ITEM_M0018_PHYSICIAN_ID = "M0018_PHYSICIAN_ID";
    final static String OASIS_ITEM_M0018_PHYSICIAN_UK = "M0018_PHYSICIAN_UK";
    final static String OASIS_ITEM_M0020_PAT_ID = "M0020_PAT_ID";
    final static String OASIS_ITEM_M0030_START_CARE_DT = "M0030_START_CARE_DT";
    final static String OASIS_ITEM_M0032_ROC_DT = "M0032_ROC_DT";
    final static String OASIS_ITEM_M0032_ROC_DT_NA = "M0032_ROC_DT_NA";
    final static String OASIS_ITEM_M0040_PAT_FNAME = "M0040_PAT_FNAME";
    final static String OASIS_ITEM_M0040_PAT_MI = "M0040_PAT_MI";
    final static String OASIS_ITEM_M0040_PAT_LNAME = "M0040_PAT_LNAME";
    final static String OASIS_ITEM_M0040_PAT_SUFFIX = "M0040_PAT_SUFFIX";
    final static String OASIS_ITEM_M0050_PAT_ST = "M0050_PAT_ST";
    final static String OASIS_ITEM_M0060_PAT_ZIP = "M0060_PAT_ZIP";
    final static String OASIS_ITEM_M0063_MEDICARE_NUM = "M0063_MEDICARE_NUM";
    final static String OASIS_ITEM_M0063_MEDICARE_NA = "M0063_MEDICARE_NA";
    final static String OASIS_ITEM_M0064_SSN = "M0064_SSN";
    final static String OASIS_ITEM_M0064_SSN_UK = "M0064_SSN_UK";
    final static String OASIS_ITEM_M0065_MEDICAID_NUM = "M0065_MEDICAID_NUM";
    final static String OASIS_ITEM_M0065_MEDICAID_NA = "M0065_MEDICAID_NA";
    final static String OASIS_ITEM_M0066_PAT_BIRTH_DT = "M0066_PAT_BIRTH_DT";
    final static String OASIS_ITEM_M0069_PAT_GENDER = "M0069_PAT_GENDER";
    final static String OASIS_ITEM_M0140_ETHNIC_AI_AN = "M0140_ETHNIC_AI_AN";
    final static String OASIS_ITEM_M0140_ETHNIC_ASIAN = "M0140_ETHNIC_ASIAN";
    final static String OASIS_ITEM_M0140_ETHNIC_BLACK = "M0140_ETHNIC_BLACK";
    final static String OASIS_ITEM_M0140_ETHNIC_HISP = "M0140_ETHNIC_HISP";
    final static String OASIS_ITEM_M0140_ETHNIC_NH_PI = "M0140_ETHNIC_NH_PI";
    final static String OASIS_ITEM_M0140_ETHNIC_WHITE = "M0140_ETHNIC_WHITE";
    final static String OASIS_ITEM_M0150_CPAY_NONE = "M0150_CPAY_NONE";
    final static String OASIS_ITEM_M0150_CPAY_MCARE_FFS = "M0150_CPAY_MCARE_FFS";
    final static String OASIS_ITEM_M0150_CPAY_MCARE_HMO = "M0150_CPAY_MCARE_HMO";
    final static String OASIS_ITEM_M0150_CPAY_MCAID_FFS = "M0150_CPAY_MCAID_FFS";
    final static String OASIS_ITEM_M0150_CPAY_MCAID_HMO = "M0150_CPAY_MCAID_HMO";
    final static String OASIS_ITEM_M0150_CPAY_WRKCOMP = "M0150_CPAY_WRKCOMP";
    final static String OASIS_ITEM_M0150_CPAY_TITLEPGMS = "M0150_CPAY_TITLEPGMS";
    final static String OASIS_ITEM_M0150_CPAY_OTH_GOVT = "M0150_CPAY_OTH_GOVT";
    final static String OASIS_ITEM_M0150_CPAY_PRIV_INS = "M0150_CPAY_PRIV_INS";
    final static String OASIS_ITEM_M0150_CPAY_PRIV_HMO = "M0150_CPAY_PRIV_HMO";
    final static String OASIS_ITEM_M0150_CPAY_SELFPAY = "M0150_CPAY_SELFPAY";
    final static String OASIS_ITEM_M0150_CPAY_OTHER = "M0150_CPAY_OTHER";
    final static String OASIS_ITEM_M0150_CPAY_UK = "M0150_CPAY_UK";
    final static String OASIS_ITEM_M0080_ASSESSOR_DISCIPLINE = "M0080_ASSESSOR_DISCIPLINE";
    final static String OASIS_ITEM_M0090_INFO_COMPLETED_DT = "M0090_INFO_COMPLETED_DT";
    final static String OASIS_ITEM_M0100_ASSMT_REASON = "M0100_ASSMT_REASON";
    final static String OASIS_ITEM_M0102_PHYSN_ORDRD_SOCROC_DT = "M0102_PHYSN_ORDRD_SOCROC_DT";
    final static String OASIS_ITEM_M0102_PHYSN_ORDRD_SOCROC_DT_NA = "M0102_PHYSN_ORDRD_SOCROC_DT_NA";
    final static String OASIS_ITEM_M0104_PHYSN_RFRL_DT = "M0104_PHYSN_RFRL_DT";
    final static String OASIS_ITEM_M0110_EPISODE_TIMING = "M0110_EPISODE_TIMING";
    final static String OASIS_ITEM_M1000_DC_LTC_14_DA = "M1000_DC_LTC_14_DA";
    final static String OASIS_ITEM_M1000_DC_SNF_14_DA = "M1000_DC_SNF_14_DA";
    final static String OASIS_ITEM_M1000_DC_IPPS_14_DA = "M1000_DC_IPPS_14_DA";
    final static String OASIS_ITEM_M1000_DC_LTCH_14_DA = "M1000_DC_LTCH_14_DA";
    final static String OASIS_ITEM_M1000_DC_IRF_14_DA = "M1000_DC_IRF_14_DA";
    final static String OASIS_ITEM_M1000_DC_PSYCH_14_DA = "M1000_DC_PSYCH_14_DA";
    final static String OASIS_ITEM_M1000_DC_OTH_14_DA = "M1000_DC_OTH_14_DA";
    final static String OASIS_ITEM_M1000_DC_NONE_14_DA = "M1000_DC_NONE_14_DA";
    final static String OASIS_ITEM_M1005_INP_DISCHARGE_DT = "M1005_INP_DISCHARGE_DT";
    final static String OASIS_ITEM_M1005_INP_DSCHG_UNKNOWN = "M1005_INP_DSCHG_UNKNOWN";
    final static String OASIS_ITEM_ITEM_FILLER_001 = "ITEM_FILLER_001";
    final static String OASIS_ITEM_ITEM_FILLER_002 = "ITEM_FILLER_002";
    final static String OASIS_ITEM_ITEM_FILLER_003 = "ITEM_FILLER_003";
    final static String OASIS_ITEM_ITEM_FILLER_004 = "ITEM_FILLER_004";
    final static String OASIS_ITEM_ITEM_FILLER_005 = "ITEM_FILLER_005";
    final static String OASIS_ITEM_ITEM_FILLER_006 = "ITEM_FILLER_006";
    final static String OASIS_ITEM_ITEM_FILLER_007 = "ITEM_FILLER_007";
    final static String OASIS_ITEM_ITEM_FILLER_008 = "ITEM_FILLER_008";
    final static String OASIS_ITEM_ITEM_FILLER_009 = "ITEM_FILLER_009";
    final static String OASIS_ITEM_ITEM_FILLER_010 = "ITEM_FILLER_010";
    final static String OASIS_ITEM_ITEM_FILLER_011 = "ITEM_FILLER_011";
    final static String OASIS_ITEM_ITEM_FILLER_012 = "ITEM_FILLER_012";
    final static String OASIS_ITEM_ITEM_FILLER_013 = "ITEM_FILLER_013";
    final static String OASIS_ITEM_ITEM_FILLER_014 = "ITEM_FILLER_014";
    final static String OASIS_ITEM_ITEM_FILLER_015 = "ITEM_FILLER_015";
    final static String OASIS_ITEM_ITEM_FILLER_016 = "ITEM_FILLER_016";
    final static String OASIS_ITEM_ITEM_FILLER_017 = "ITEM_FILLER_017";
    final static String OASIS_ITEM_ITEM_FILLER_018 = "ITEM_FILLER_018";
    final static String OASIS_ITEM_ITEM_FILLER_019 = "ITEM_FILLER_019";
    final static String OASIS_ITEM_M1018_PRIOR_UR_INCON = "M1018_PRIOR_UR_INCON";
    final static String OASIS_ITEM_M1018_PRIOR_CATH = "M1018_PRIOR_CATH";
    final static String OASIS_ITEM_M1018_PRIOR_INTRACT_PAIN = "M1018_PRIOR_INTRACT_PAIN";
    final static String OASIS_ITEM_M1018_PRIOR_IMPR_DECSN = "M1018_PRIOR_IMPR_DECSN";
    final static String OASIS_ITEM_M1018_PRIOR_DISRUPTIVE = "M1018_PRIOR_DISRUPTIVE";
    final static String OASIS_ITEM_M1018_PRIOR_MEM_LOSS = "M1018_PRIOR_MEM_LOSS";
    final static String OASIS_ITEM_M1018_PRIOR_NONE = "M1018_PRIOR_NONE";
    final static String OASIS_ITEM_M1018_PRIOR_NOCHG_14D = "M1018_PRIOR_NOCHG_14D";
    final static String OASIS_ITEM_M1018_PRIOR_UNKNOWN = "M1018_PRIOR_UNKNOWN";
    final static String OASIS_ITEM_ITEM_FILLER_020 = "ITEM_FILLER_020";
    final static String OASIS_ITEM_ITEM_FILLER_021 = "ITEM_FILLER_021";
    final static String OASIS_ITEM_ITEM_FILLER_022 = "ITEM_FILLER_022";
    final static String OASIS_ITEM_ITEM_FILLER_023 = "ITEM_FILLER_023";
    final static String OASIS_ITEM_ITEM_FILLER_024 = "ITEM_FILLER_024";
    final static String OASIS_ITEM_ITEM_FILLER_025 = "ITEM_FILLER_025";
    final static String OASIS_ITEM_ITEM_FILLER_026 = "ITEM_FILLER_026";
    final static String OASIS_ITEM_ITEM_FILLER_027 = "ITEM_FILLER_027";
    final static String OASIS_ITEM_ITEM_FILLER_028 = "ITEM_FILLER_028";
    final static String OASIS_ITEM_ITEM_FILLER_029 = "ITEM_FILLER_029";
    final static String OASIS_ITEM_ITEM_FILLER_030 = "ITEM_FILLER_030";
    final static String OASIS_ITEM_ITEM_FILLER_031 = "ITEM_FILLER_031";
    final static String OASIS_ITEM_ITEM_FILLER_032 = "ITEM_FILLER_032";
    final static String OASIS_ITEM_ITEM_FILLER_033 = "ITEM_FILLER_033";
    final static String OASIS_ITEM_ITEM_FILLER_034 = "ITEM_FILLER_034";
    final static String OASIS_ITEM_ITEM_FILLER_035 = "ITEM_FILLER_035";
    final static String OASIS_ITEM_ITEM_FILLER_036 = "ITEM_FILLER_036";
    final static String OASIS_ITEM_ITEM_FILLER_037 = "ITEM_FILLER_037";
    final static String OASIS_ITEM_ITEM_FILLER_038 = "ITEM_FILLER_038";
    final static String OASIS_ITEM_ITEM_FILLER_039 = "ITEM_FILLER_039";
    final static String OASIS_ITEM_ITEM_FILLER_040 = "ITEM_FILLER_040";
    final static String OASIS_ITEM_ITEM_FILLER_041 = "ITEM_FILLER_041";
    final static String OASIS_ITEM_ITEM_FILLER_042 = "ITEM_FILLER_042";
    final static String OASIS_ITEM_ITEM_FILLER_043 = "ITEM_FILLER_043";
    final static String OASIS_ITEM_M1030_THH_IV_INFUSION = "M1030_THH_IV_INFUSION";
    final static String OASIS_ITEM_M1030_THH_PAR_NUTRITION = "M1030_THH_PAR_NUTRITION";
    final static String OASIS_ITEM_M1030_THH_ENT_NUTRITION = "M1030_THH_ENT_NUTRITION";
    final static String OASIS_ITEM_M1030_THH_NONE_ABOVE = "M1030_THH_NONE_ABOVE";
    final static String OASIS_ITEM_ITEM_FILLER_044 = "ITEM_FILLER_044";
    final static String OASIS_ITEM_ITEM_FILLER_045 = "ITEM_FILLER_045";
    final static String OASIS_ITEM_ITEM_FILLER_046 = "ITEM_FILLER_046";
    final static String OASIS_ITEM_ITEM_FILLER_047 = "ITEM_FILLER_047";
    final static String OASIS_ITEM_ITEM_FILLER_048 = "ITEM_FILLER_048";
    final static String OASIS_ITEM_ITEM_FILLER_049 = "ITEM_FILLER_049";
    final static String OASIS_ITEM_ITEM_FILLER_050 = "ITEM_FILLER_050";
    final static String OASIS_ITEM_M1034_PTNT_OVRAL_STUS = "M1034_PTNT_OVRAL_STUS";
    final static String OASIS_ITEM_M1036_RSK_SMOKING = "M1036_RSK_SMOKING";
    final static String OASIS_ITEM_M1036_RSK_OBESITY = "M1036_RSK_OBESITY";
    final static String OASIS_ITEM_M1036_RSK_ALCOHOLISM = "M1036_RSK_ALCOHOLISM";
    final static String OASIS_ITEM_M1036_RSK_DRUGS = "M1036_RSK_DRUGS";
    final static String OASIS_ITEM_M1036_RSK_NONE = "M1036_RSK_NONE";
    final static String OASIS_ITEM_M1036_RSK_UNKNOWN = "M1036_RSK_UNKNOWN";
    final static String OASIS_ITEM_ITEM_FILLER_051 = "ITEM_FILLER_051";
    final static String OASIS_ITEM_ITEM_FILLER_052 = "ITEM_FILLER_052";
    final static String OASIS_ITEM_ITEM_FILLER_053 = "ITEM_FILLER_053";
    final static String OASIS_ITEM_ITEM_FILLER_054 = "ITEM_FILLER_054";
    final static String OASIS_ITEM_M1100_PTNT_LVG_STUTN = "M1100_PTNT_LVG_STUTN";
    final static String OASIS_ITEM_M1200_VISION = "M1200_VISION";
    final static String OASIS_ITEM_M1210_HEARG_ABLTY = "M1210_HEARG_ABLTY";
    final static String OASIS_ITEM_M1220_UNDRSTG_VERBAL_CNTNT = "M1220_UNDRSTG_VERBAL_CNTNT";
    final static String OASIS_ITEM_M1230_SPEECH = "M1230_SPEECH";
    final static String OASIS_ITEM_M1240_FRML_PAIN_ASMT = "M1240_FRML_PAIN_ASMT";
    final static String OASIS_ITEM_M1242_PAIN_FREQ_ACTVTY_MVMT = "M1242_PAIN_FREQ_ACTVTY_MVMT";
    final static String OASIS_ITEM_M1300_PRSR_ULCR_RISK_ASMT = "M1300_PRSR_ULCR_RISK_ASMT";
    final static String OASIS_ITEM_M1302_RISK_OF_PRSR_ULCR = "M1302_RISK_OF_PRSR_ULCR";
    final static String OASIS_ITEM_M1306_UNHLD_STG2_PRSR_ULCR = "M1306_UNHLD_STG2_PRSR_ULCR";
    final static String OASIS_ITEM_M1307_OLDST_STG2_AT_DSCHRG = "M1307_OLDST_STG2_AT_DSCHRG";
    final static String OASIS_ITEM_M1307_OLDST_STG2_ONST_DT = "M1307_OLDST_STG2_ONST_DT";
    final static String OASIS_ITEM_M1308_NBR_PRSULC_STG2 = "M1308_NBR_PRSULC_STG2";
    final static String OASIS_ITEM_ITEM_FILLER_055 = "ITEM_FILLER_055";
    final static String OASIS_ITEM_M1308_NBR_PRSULC_STG3 = "M1308_NBR_PRSULC_STG3";
    final static String OASIS_ITEM_ITEM_FILLER_056 = "ITEM_FILLER_056";
    final static String OASIS_ITEM_M1308_NBR_PRSULC_STG4 = "M1308_NBR_PRSULC_STG4";
    final static String OASIS_ITEM_ITEM_FILLER_057 = "ITEM_FILLER_057";
    final static String OASIS_ITEM_M1308_NSTG_DRSG = "M1308_NSTG_DRSG";
    final static String OASIS_ITEM_ITEM_FILLER_058 = "ITEM_FILLER_058";
    final static String OASIS_ITEM_M1308_NSTG_CVRG = "M1308_NSTG_CVRG";
    final static String OASIS_ITEM_ITEM_FILLER_059 = "ITEM_FILLER_059";
    final static String OASIS_ITEM_M1308_NSTG_DEEP_TISUE = "M1308_NSTG_DEEP_TISUE";
    final static String OASIS_ITEM_ITEM_FILLER_060 = "ITEM_FILLER_060";
    final static String OASIS_ITEM_ITEM_FILLER_061 = "ITEM_FILLER_061";
    final static String OASIS_ITEM_ITEM_FILLER_062 = "ITEM_FILLER_062";
    final static String OASIS_ITEM_ITEM_FILLER_063 = "ITEM_FILLER_063";
    final static String OASIS_ITEM_M1320_STUS_PRBLM_PRSR_ULCR = "M1320_STUS_PRBLM_PRSR_ULCR";
    final static String OASIS_ITEM_M1322_NBR_PRSULC_STG1 = "M1322_NBR_PRSULC_STG1";
    final static String OASIS_ITEM_M1324_STG_PRBLM_ULCER = "M1324_STG_PRBLM_ULCER";
    final static String OASIS_ITEM_M1330_STAS_ULCR_PRSNT = "M1330_STAS_ULCR_PRSNT";
    final static String OASIS_ITEM_M1332_NBR_STAS_ULCR = "M1332_NBR_STAS_ULCR";
    final static String OASIS_ITEM_M1334_STUS_PRBLM_STAS_ULCR = "M1334_STUS_PRBLM_STAS_ULCR";
    final static String OASIS_ITEM_M1340_SRGCL_WND_PRSNT = "M1340_SRGCL_WND_PRSNT";
    final static String OASIS_ITEM_M1342_STUS_PRBLM_SRGCL_WND = "M1342_STUS_PRBLM_SRGCL_WND";
    final static String OASIS_ITEM_M1350_LESION_OPEN_WND = "M1350_LESION_OPEN_WND";
    final static String OASIS_ITEM_M1400_WHEN_DYSPNEIC = "M1400_WHEN_DYSPNEIC";
    final static String OASIS_ITEM_M1410_RESPTX_OXYGEN = "M1410_RESPTX_OXYGEN";
    final static String OASIS_ITEM_M1410_RESPTX_VENTILATOR = "M1410_RESPTX_VENTILATOR";
    final static String OASIS_ITEM_M1410_RESPTX_AIRPRESS = "M1410_RESPTX_AIRPRESS";
    final static String OASIS_ITEM_M1410_RESPTX_NONE = "M1410_RESPTX_NONE";
    final static String OASIS_ITEM_M1500_SYMTM_HRT_FAILR_PTNTS = "M1500_SYMTM_HRT_FAILR_PTNTS";
    final static String OASIS_ITEM_M1510_HRT_FAILR_NO_ACTN = "M1510_HRT_FAILR_NO_ACTN";
    final static String OASIS_ITEM_M1510_HRT_FAILR_PHYSN_CNTCT = "M1510_HRT_FAILR_PHYSN_CNTCT";
    final static String OASIS_ITEM_M1510_HRT_FAILR_ER_TRTMT = "M1510_HRT_FAILR_ER_TRTMT";
    final static String OASIS_ITEM_M1510_HRT_FAILR_PHYSN_TRTMT = "M1510_HRT_FAILR_PHYSN_TRTMT";
    final static String OASIS_ITEM_M1510_HRT_FAILR_CLNCL_INTRVTN = "M1510_HRT_FAILR_CLNCL_INTRVTN";
    final static String OASIS_ITEM_M1510_HRT_FAILR_CARE_PLAN_CHG = "M1510_HRT_FAILR_CARE_PLAN_CHG";
    final static String OASIS_ITEM_M1600_UTI = "M1600_UTI";
    final static String OASIS_ITEM_M1610_UR_INCONT = "M1610_UR_INCONT";
    final static String OASIS_ITEM_M1615_INCNTNT_TIMING = "M1615_INCNTNT_TIMING";
    final static String OASIS_ITEM_M1620_BWL_INCONT = "M1620_BWL_INCONT";
    final static String OASIS_ITEM_M1630_OSTOMY = "M1630_OSTOMY";
    final static String OASIS_ITEM_M1700_COG_FUNCTION = "M1700_COG_FUNCTION";
    final static String OASIS_ITEM_M1710_WHEN_CONFUSED = "M1710_WHEN_CONFUSED";
    final static String OASIS_ITEM_M1720_WHEN_ANXIOUS = "M1720_WHEN_ANXIOUS";
    final static String OASIS_ITEM_M1730_STDZ_DPRSN_SCRNG = "M1730_STDZ_DPRSN_SCRNG";
    final static String OASIS_ITEM_M1730_PHQ2_LACK_INTRST = "M1730_PHQ2_LACK_INTRST";
    final static String OASIS_ITEM_M1730_PHQ2_DPRSN = "M1730_PHQ2_DPRSN";
    final static String OASIS_ITEM_M1740_BD_MEM_DEFICIT = "M1740_BD_MEM_DEFICIT";
    final static String OASIS_ITEM_M1740_BD_IMP_DECISN = "M1740_BD_IMP_DECISN";
    final static String OASIS_ITEM_M1740_BD_VERBAL = "M1740_BD_VERBAL";
    final static String OASIS_ITEM_M1740_BD_PHYSICAL = "M1740_BD_PHYSICAL";
    final static String OASIS_ITEM_M1740_BD_SOC_INAPPRO = "M1740_BD_SOC_INAPPRO";
    final static String OASIS_ITEM_M1740_BD_DELUSIONS = "M1740_BD_DELUSIONS";
    final static String OASIS_ITEM_M1740_BD_NONE = "M1740_BD_NONE";
    final static String OASIS_ITEM_M1745_BEH_PROB_FREQ = "M1745_BEH_PROB_FREQ";
    final static String OASIS_ITEM_M1750_REC_PSYCH_NURS = "M1750_REC_PSYCH_NURS";
    final static String OASIS_ITEM_M1800_CRNT_GROOMING = "M1800_CRNT_GROOMING";
    final static String OASIS_ITEM_M1810_CRNT_DRESS_UPPER = "M1810_CRNT_DRESS_UPPER";
    final static String OASIS_ITEM_M1820_CRNT_DRESS_LOWER = "M1820_CRNT_DRESS_LOWER";
    final static String OASIS_ITEM_M1830_CRNT_BATHG = "M1830_CRNT_BATHG";
    final static String OASIS_ITEM_M1840_CRNT_TOILTG = "M1840_CRNT_TOILTG";
    final static String OASIS_ITEM_M1845_CRNT_TOILTG_HYGN = "M1845_CRNT_TOILTG_HYGN";
    final static String OASIS_ITEM_M1850_CRNT_TRNSFRNG = "M1850_CRNT_TRNSFRNG";
    final static String OASIS_ITEM_M1860_CRNT_AMBLTN = "M1860_CRNT_AMBLTN";
    final static String OASIS_ITEM_M1870_CRNT_FEEDING = "M1870_CRNT_FEEDING";
    final static String OASIS_ITEM_M1880_CRNT_PREP_LT_MEALS = "M1880_CRNT_PREP_LT_MEALS";
    final static String OASIS_ITEM_M1890_CRNT_PHONE_USE = "M1890_CRNT_PHONE_USE";
    final static String OASIS_ITEM_M1900_PRIOR_ADLIADL_SELF = "M1900_PRIOR_ADLIADL_SELF";
    final static String OASIS_ITEM_M1900_PRIOR_ADLIADL_AMBLTN = "M1900_PRIOR_ADLIADL_AMBLTN";
    final static String OASIS_ITEM_M1900_PRIOR_ADLIADL_TRNSFR = "M1900_PRIOR_ADLIADL_TRNSFR";
    final static String OASIS_ITEM_M1900_PRIOR_ADLIADL_HSEHOLD = "M1900_PRIOR_ADLIADL_HSEHOLD";
    final static String OASIS_ITEM_M1910_MLT_FCTR_FALL_RISK_ASMT = "M1910_MLT_FCTR_FALL_RISK_ASMT";
    final static String OASIS_ITEM_M2000_DRUG_RGMN_RVW = "M2000_DRUG_RGMN_RVW";
    final static String OASIS_ITEM_M2002_MDCTN_FLWP = "M2002_MDCTN_FLWP";
    final static String OASIS_ITEM_M2004_MDCTN_INTRVTN = "M2004_MDCTN_INTRVTN";
    final static String OASIS_ITEM_M2010_HIGH_RISK_DRUG_EDCTN = "M2010_HIGH_RISK_DRUG_EDCTN";
    final static String OASIS_ITEM_M2015_DRUG_EDCTN_INTRVTN = "M2015_DRUG_EDCTN_INTRVTN";
    final static String OASIS_ITEM_M2020_CRNT_MGMT_ORAL_MDCTN = "M2020_CRNT_MGMT_ORAL_MDCTN";
    final static String OASIS_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN = "M2030_CRNT_MGMT_INJCTN_MDCTN";
    final static String OASIS_ITEM_M2040_PRIOR_MGMT_ORAL_MDCTN = "M2040_PRIOR_MGMT_ORAL_MDCTN";
    final static String OASIS_ITEM_M2040_PRIOR_MGMT_INJCTN_MDCTN = "M2040_PRIOR_MGMT_INJCTN_MDCTN";
    final static String OASIS_ITEM_ITEM_FILLER_064 = "ITEM_FILLER_064";
    final static String OASIS_ITEM_ITEM_FILLER_065 = "ITEM_FILLER_065";
    final static String OASIS_ITEM_ITEM_FILLER_066 = "ITEM_FILLER_066";
    final static String OASIS_ITEM_ITEM_FILLER_067 = "ITEM_FILLER_067";
    final static String OASIS_ITEM_ITEM_FILLER_068 = "ITEM_FILLER_068";
    final static String OASIS_ITEM_ITEM_FILLER_069 = "ITEM_FILLER_069";
    final static String OASIS_ITEM_ITEM_FILLER_070 = "ITEM_FILLER_070";
    final static String OASIS_ITEM_M2110_ADL_IADL_ASTNC_FREQ = "M2110_ADL_IADL_ASTNC_FREQ";
    final static String OASIS_ITEM_M2200_THER_NEED_NBR = "M2200_THER_NEED_NBR";
    final static String OASIS_ITEM_M2200_THER_NEED_NA = "M2200_THER_NEED_NA";
    final static String OASIS_ITEM_M2250_PLAN_SMRY_PTNT_SPECF = "M2250_PLAN_SMRY_PTNT_SPECF";
    final static String OASIS_ITEM_M2250_PLAN_SMRY_DBTS_FT_CARE = "M2250_PLAN_SMRY_DBTS_FT_CARE";
    final static String OASIS_ITEM_M2250_PLAN_SMRY_FALL_PRVNT = "M2250_PLAN_SMRY_FALL_PRVNT";
    final static String OASIS_ITEM_M2250_PLAN_SMRY_DPRSN_INTRVTN = "M2250_PLAN_SMRY_DPRSN_INTRVTN";
    final static String OASIS_ITEM_M2250_PLAN_SMRY_PAIN_INTRVTN = "M2250_PLAN_SMRY_PAIN_INTRVTN";
    final static String OASIS_ITEM_M2250_PLAN_SMRY_PRSULC_PRVNT = "M2250_PLAN_SMRY_PRSULC_PRVNT";
    final static String OASIS_ITEM_M2250_PLAN_SMRY_PRSULC_TRTMT = "M2250_PLAN_SMRY_PRSULC_TRTMT";
    final static String OASIS_ITEM_M2300_EMER_USE_AFTR_LAST_ASMT = "M2300_EMER_USE_AFTR_LAST_ASMT";
    final static String OASIS_ITEM_M2310_ECR_MEDICATION = "M2310_ECR_MEDICATION";
    final static String OASIS_ITEM_M2310_ECR_INJRY_BY_FALL = "M2310_ECR_INJRY_BY_FALL";
    final static String OASIS_ITEM_M2310_ECR_RSPRTRY_INFCTN = "M2310_ECR_RSPRTRY_INFCTN";
    final static String OASIS_ITEM_M2310_ECR_RSPRTRY_OTHR = "M2310_ECR_RSPRTRY_OTHR";
    final static String OASIS_ITEM_M2310_ECR_HRT_FAILR = "M2310_ECR_HRT_FAILR";
    final static String OASIS_ITEM_M2310_ECR_CRDC_DSRTHM = "M2310_ECR_CRDC_DSRTHM";
    final static String OASIS_ITEM_M2310_ECR_MI_CHST_PAIN = "M2310_ECR_MI_CHST_PAIN";
    final static String OASIS_ITEM_M2310_ECR_OTHR_HRT_DEASE = "M2310_ECR_OTHR_HRT_DEASE";
    final static String OASIS_ITEM_M2310_ECR_STROKE_TIA = "M2310_ECR_STROKE_TIA";
    final static String OASIS_ITEM_M2310_ECR_HYPOGLYC = "M2310_ECR_HYPOGLYC";
    final static String OASIS_ITEM_M2310_ECR_GI_PRBLM = "M2310_ECR_GI_PRBLM";
    final static String OASIS_ITEM_M2310_ECR_DHYDRTN_MALNTR = "M2310_ECR_DHYDRTN_MALNTR";
    final static String OASIS_ITEM_M2310_ECR_UTI = "M2310_ECR_UTI";
    final static String OASIS_ITEM_M2310_ECR_CTHTR_CMPLCTN = "M2310_ECR_CTHTR_CMPLCTN";
    final static String OASIS_ITEM_M2310_ECR_WND_INFCTN_DTRORTN = "M2310_ECR_WND_INFCTN_DTRORTN";
    final static String OASIS_ITEM_M2310_ECR_UNCNTLD_PAIN = "M2310_ECR_UNCNTLD_PAIN";
    final static String OASIS_ITEM_M2310_ECR_MENTL_BHVRL_PRBLM = "M2310_ECR_MENTL_BHVRL_PRBLM";
    final static String OASIS_ITEM_M2310_ECR_DVT_PULMNRY = "M2310_ECR_DVT_PULMNRY";
    final static String OASIS_ITEM_M2310_ECR_OTHER = "M2310_ECR_OTHER";
    final static String OASIS_ITEM_M2310_ECR_UNKNOWN = "M2310_ECR_UNKNOWN";
    final static String OASIS_ITEM_M2400_INTRVTN_SMRY_DBTS_FT = "M2400_INTRVTN_SMRY_DBTS_FT";
    final static String OASIS_ITEM_M2400_INTRVTN_SMRY_FALL_PRVNT = "M2400_INTRVTN_SMRY_FALL_PRVNT";
    final static String OASIS_ITEM_M2400_INTRVTN_SMRY_DPRSN = "M2400_INTRVTN_SMRY_DPRSN";
    final static String OASIS_ITEM_M2400_INTRVTN_SMRY_PAIN_MNTR = "M2400_INTRVTN_SMRY_PAIN_MNTR";
    final static String OASIS_ITEM_M2400_INTRVTN_SMRY_PRSULC_PRVN = "M2400_INTRVTN_SMRY_PRSULC_PRVN";
    final static String OASIS_ITEM_M2400_INTRVTN_SMRY_PRSULC_WET = "M2400_INTRVTN_SMRY_PRSULC_WET";
    final static String OASIS_ITEM_M2410_INPAT_FACILITY = "M2410_INPAT_FACILITY";
    final static String OASIS_ITEM_M2420_DSCHRG_DISP = "M2420_DSCHRG_DISP";
    final static String OASIS_ITEM_M2430_HOSP_MED = "M2430_HOSP_MED";
    final static String OASIS_ITEM_M2430_HOSP_INJRY_BY_FALL = "M2430_HOSP_INJRY_BY_FALL";
    final static String OASIS_ITEM_M2430_HOSP_RSPRTRY_INFCTN = "M2430_HOSP_RSPRTRY_INFCTN";
    final static String OASIS_ITEM_M2430_HOSP_RSPRTRY_OTHR = "M2430_HOSP_RSPRTRY_OTHR";
    final static String OASIS_ITEM_M2430_HOSP_HRT_FAILR = "M2430_HOSP_HRT_FAILR";
    final static String OASIS_ITEM_M2430_HOSP_CRDC_DSRTHM = "M2430_HOSP_CRDC_DSRTHM";
    final static String OASIS_ITEM_M2430_HOSP_MI_CHST_PAIN = "M2430_HOSP_MI_CHST_PAIN";
    final static String OASIS_ITEM_M2430_HOSP_OTHR_HRT_DEASE = "M2430_HOSP_OTHR_HRT_DEASE";
    final static String OASIS_ITEM_M2430_HOSP_STROKE_TIA = "M2430_HOSP_STROKE_TIA";
    final static String OASIS_ITEM_M2430_HOSP_HYPOGLYC = "M2430_HOSP_HYPOGLYC";
    final static String OASIS_ITEM_M2430_HOSP_GI_PRBLM = "M2430_HOSP_GI_PRBLM";
    final static String OASIS_ITEM_M2430_HOSP_DHYDRTN_MALNTR = "M2430_HOSP_DHYDRTN_MALNTR";
    final static String OASIS_ITEM_M2430_HOSP_UR_TRACT = "M2430_HOSP_UR_TRACT";
    final static String OASIS_ITEM_M2430_HOSP_CTHTR_CMPLCTN = "M2430_HOSP_CTHTR_CMPLCTN";
    final static String OASIS_ITEM_M2430_HOSP_WND_INFCTN = "M2430_HOSP_WND_INFCTN";
    final static String OASIS_ITEM_M2430_HOSP_PAIN = "M2430_HOSP_PAIN";
    final static String OASIS_ITEM_M2430_HOSP_MENTL_BHVRL_PRBLM = "M2430_HOSP_MENTL_BHVRL_PRBLM";
    final static String OASIS_ITEM_M2430_HOSP_DVT_PULMNRY = "M2430_HOSP_DVT_PULMNRY";
    final static String OASIS_ITEM_M2430_HOSP_SCHLD_TRTMT = "M2430_HOSP_SCHLD_TRTMT";
    final static String OASIS_ITEM_M2430_HOSP_OTHER = "M2430_HOSP_OTHER";
    final static String OASIS_ITEM_M2430_HOSP_UK = "M2430_HOSP_UK";
    final static String OASIS_ITEM_ITEM_FILLER_071 = "ITEM_FILLER_071";
    final static String OASIS_ITEM_ITEM_FILLER_072 = "ITEM_FILLER_072";
    final static String OASIS_ITEM_ITEM_FILLER_073 = "ITEM_FILLER_073";
    final static String OASIS_ITEM_ITEM_FILLER_074 = "ITEM_FILLER_074";
    final static String OASIS_ITEM_ITEM_FILLER_075 = "ITEM_FILLER_075";
    final static String OASIS_ITEM_ITEM_FILLER_076 = "ITEM_FILLER_076";
    final static String OASIS_ITEM_ITEM_FILLER_077 = "ITEM_FILLER_077";
    final static String OASIS_ITEM_M0903_LAST_HOME_VISIT = "M0903_LAST_HOME_VISIT";
    final static String OASIS_ITEM_M0906_DC_TRAN_DTH_DT = "M0906_DC_TRAN_DTH_DT";
    final static String OASIS_ITEM_CONTROL_ITEMS_FILLER = "CONTROL_ITEMS_FILLER";
    final static String OASIS_ITEM_M1011_14_DAY_INP1_ICD = "M1011_14_DAY_INP1_ICD";
    final static String OASIS_ITEM_M1011_14_DAY_INP2_ICD = "M1011_14_DAY_INP2_ICD";
    final static String OASIS_ITEM_M1011_14_DAY_INP3_ICD = "M1011_14_DAY_INP3_ICD";
    final static String OASIS_ITEM_M1011_14_DAY_INP4_ICD = "M1011_14_DAY_INP4_ICD";
    final static String OASIS_ITEM_M1011_14_DAY_INP5_ICD = "M1011_14_DAY_INP5_ICD";
    final static String OASIS_ITEM_M1011_14_DAY_INP6_ICD = "M1011_14_DAY_INP6_ICD";
    final static String OASIS_ITEM_M1011_14_DAY_INP_NA = "M1011_14_DAY_INP_NA";
    final static String OASIS_ITEM_M1017_CHGREG_ICD1 = "M1017_CHGREG_ICD1";
    final static String OASIS_ITEM_M1017_CHGREG_ICD2 = "M1017_CHGREG_ICD2";
    final static String OASIS_ITEM_M1017_CHGREG_ICD3 = "M1017_CHGREG_ICD3";
    final static String OASIS_ITEM_M1017_CHGREG_ICD4 = "M1017_CHGREG_ICD4";
    final static String OASIS_ITEM_M1017_CHGREG_ICD5 = "M1017_CHGREG_ICD5";
    final static String OASIS_ITEM_M1017_CHGREG_ICD6 = "M1017_CHGREG_ICD6";
    final static String OASIS_ITEM_M1017_CHGREG_ICD_NA = "M1017_CHGREG_ICD_NA";
    final static String OASIS_ITEM_M1021_PRIMARY_DIAG_ICD = "M1021_PRIMARY_DIAG_ICD";
    final static String OASIS_ITEM_M1021_PRIMARY_DIAG_SEVERITY = "M1021_PRIMARY_DIAG_SEVERITY";
    final static String OASIS_ITEM_M1023_OTH_DIAG1_ICD = "M1023_OTH_DIAG1_ICD";
    final static String OASIS_ITEM_M1023_OTH_DIAG1_SEVERITY = "M1023_OTH_DIAG1_SEVERITY";
    final static String OASIS_ITEM_M1023_OTH_DIAG2_ICD = "M1023_OTH_DIAG2_ICD";
    final static String OASIS_ITEM_M1023_OTH_DIAG2_SEVERITY = "M1023_OTH_DIAG2_SEVERITY";
    final static String OASIS_ITEM_M1023_OTH_DIAG3_ICD = "M1023_OTH_DIAG3_ICD";
    final static String OASIS_ITEM_M1023_OTH_DIAG3_SEVERITY = "M1023_OTH_DIAG3_SEVERITY";
    final static String OASIS_ITEM_M1023_OTH_DIAG4_ICD = "M1023_OTH_DIAG4_ICD";
    final static String OASIS_ITEM_M1023_OTH_DIAG4_SEVERITY = "M1023_OTH_DIAG4_SEVERITY";
    final static String OASIS_ITEM_M1023_OTH_DIAG5_ICD = "M1023_OTH_DIAG5_ICD";
    final static String OASIS_ITEM_M1023_OTH_DIAG5_SEVERITY = "M1023_OTH_DIAG5_SEVERITY";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_A3 = "M1025_OPT_DIAG_ICD_A3";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_A4 = "M1025_OPT_DIAG_ICD_A4";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_B3 = "M1025_OPT_DIAG_ICD_B3";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_B4 = "M1025_OPT_DIAG_ICD_B4";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_C3 = "M1025_OPT_DIAG_ICD_C3";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_C4 = "M1025_OPT_DIAG_ICD_C4";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_D3 = "M1025_OPT_DIAG_ICD_D3";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_D4 = "M1025_OPT_DIAG_ICD_D4";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_E3 = "M1025_OPT_DIAG_ICD_E3";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_E4 = "M1025_OPT_DIAG_ICD_E4";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_F3 = "M1025_OPT_DIAG_ICD_F3";
    final static String OASIS_ITEM_M1025_OPT_DIAG_ICD_F4 = "M1025_OPT_DIAG_ICD_F4";
    final static String OASIS_ITEM_M1033_HOSP_RISK_HSTRY_FALLS = "M1033_HOSP_RISK_HSTRY_FALLS";
    final static String OASIS_ITEM_M1033_HOSP_RISK_WEIGHT_LOSS = "M1033_HOSP_RISK_WEIGHT_LOSS";
    final static String OASIS_ITEM_M1033_HOSP_RISK_MLTPL_HOSPZTN = "M1033_HOSP_RISK_MLTPL_HOSPZTN";
    final static String OASIS_ITEM_M1033_HOSP_RISK_MLTPL_ED_VISIT = "M1033_HOSP_RISK_MLTPL_ED_VISIT";
    final static String OASIS_ITEM_M1033_HOSP_RISK_MNTL_BHV_DCLN = "M1033_HOSP_RISK_MNTL_BHV_DCLN";
    final static String OASIS_ITEM_M1033_HOSP_RISK_COMPLIANCE = "M1033_HOSP_RISK_COMPLIANCE";
    final static String OASIS_ITEM_M1033_HOSP_RISK_5PLUS_MDCTN = "M1033_HOSP_RISK_5PLUS_MDCTN";
    final static String OASIS_ITEM_M1033_HOSP_RISK_CRNT_EXHSTN = "M1033_HOSP_RISK_CRNT_EXHSTN";
    final static String OASIS_ITEM_M1033_HOSP_RISK_OTHR_RISK = "M1033_HOSP_RISK_OTHR_RISK";
    final static String OASIS_ITEM_M1033_HOSP_RISK_NONE_ABOVE = "M1033_HOSP_RISK_NONE_ABOVE";
    final static String OASIS_ITEM_M1041_IN_INFLNZ_SEASON = "M1041_IN_INFLNZ_SEASON";
    final static String OASIS_ITEM_M1046_INFLNZ_RECD_CRNT_SEASON = "M1046_INFLNZ_RECD_CRNT_SEASON";
    final static String OASIS_ITEM_M1051_PVX_RCVD_AGNCY = "M1051_PVX_RCVD_AGNCY";
    final static String OASIS_ITEM_M1056_PVX_RSN_NOT_RCVD_AGNCY = "M1056_PVX_RSN_NOT_RCVD_AGNCY";
    final static String OASIS_ITEM_M1309_NBR_NEW_WRS_PRSULC_STG2 = "M1309_NBR_NEW_WRS_PRSULC_STG2";
    final static String OASIS_ITEM_M1309_NBR_NEW_WRS_PRSULC_STG3 = "M1309_NBR_NEW_WRS_PRSULC_STG3";
    final static String OASIS_ITEM_M1309_NBR_NEW_WRS_PRSULC_STG4 = "M1309_NBR_NEW_WRS_PRSULC_STG4";
    final static String OASIS_ITEM_M2102_CARE_TYPE_SRC_ADL = "M2102_CARE_TYPE_SRC_ADL";
    final static String OASIS_ITEM_M2102_CARE_TYPE_SRC_IADL = "M2102_CARE_TYPE_SRC_IADL";
    final static String OASIS_ITEM_M2102_CARE_TYPE_SRC_MDCTN = "M2102_CARE_TYPE_SRC_MDCTN";
    final static String OASIS_ITEM_M2102_CARE_TYPE_SRC_PRCDR = "M2102_CARE_TYPE_SRC_PRCDR";
    final static String OASIS_ITEM_M2102_CARE_TYPE_SRC_EQUIP = "M2102_CARE_TYPE_SRC_EQUIP";
    final static String OASIS_ITEM_M2102_CARE_TYPE_SRC_SPRVSN = "M2102_CARE_TYPE_SRC_SPRVSN";
    final static String OASIS_ITEM_M2102_CARE_TYPE_SRC_ADVCY = "M2102_CARE_TYPE_SRC_ADVCY";
    final static String OASIS_ITEM_M1309_NBR_NEW_WRS_PRSULC_NSTG = "M1309_NBR_NEW_WRS_PRSULC_NSTG";
    final static String OASIS_ITEM_ASMT_ITEMS_FILLER = "ASMT_ITEMS_FILLER";
    final static String OASIS_ITEM_LEGACY_ITEMS_FILLER = "LEGACY_ITEMS_FILLER";
    final static String OASIS_ITEM_C_CCN_NUM = "C_CCN_NUM";
    final static String OASIS_ITEM_CALCULATED_ITEMS_FILLER = "CALCULATED_ITEMS_FILLER";
    final static String OASIS_ITEM_HHA_ASMT_INT_ID = "HHA_ASMT_INT_ID";
    final static String OASIS_ITEM_ORIG_ASMT_INT_ID = "ORIG_ASMT_INT_ID";
    final static String OASIS_ITEM_RES_INT_ID = "RES_INT_ID";
    final static String OASIS_ITEM_ASMT_EFF_DATE = "ASMT_EFF_DATE";
    final static String OASIS_ITEM_CALC_FILLER_001 = "CALC_FILLER_001";
    final static String OASIS_ITEM_FAC_INT_ID = "FAC_INT_ID";
    final static String OASIS_ITEM_SUBMISSION_ID = "SUBMISSION_ID";
    final static String OASIS_ITEM_SUBMISSION_DATE = "SUBMISSION_DATE";
    final static String OASIS_ITEM_SUBMISSION_COMPLETE_DATE = "SUBMISSION_COMPLETE_DATE";
    final static String OASIS_ITEM_SUBMITTING_USER_ID = "SUBMITTING_USER_ID";
    final static String OASIS_ITEM_RES_MATCH_CRITERIA = "RES_MATCH_CRITERIA";
    final static String OASIS_ITEM_RESIDENT_AGE = "RESIDENT_AGE";
    final static String OASIS_ITEM_BIRTHDATE_SUBM_IND = "BIRTHDATE_SUBM_IND";
    final static String OASIS_ITEM_CALC_HIPPS_CODE = "CALC_HIPPS_CODE";
    final static String OASIS_ITEM_CALC_HIPPS_VERSION = "CALC_HIPPS_VERSION";
    final static String OASIS_ITEM_DATA_END_INDICATOR = "DATA_END_INDICATOR";
    final static String OASIS_ITEM_CR = "CR";
    final static String OASIS_ITEM_LF = "LF";

    public Oasis_C1_RecordUtil_v2_12() {
        this("20151001", "20161231"); // this converter ends by 20161231
    }

    protected Oasis_C1_RecordUtil_v2_12(String startDate, String endDate) {
        super(startDate, endDate, 3256, "2.12");
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


    /**
     * This calls convertToHomeHealthRec(String strRecord, int recNum, boolean skipPassthru)
     * with the supplied parameter and false for the passthru
     * 
     * @param strRecord
     * @param recNum
     * @return non-null record
     * @throws ParseException 
     */
    @Override
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum) throws ParseException {
        return convertToHomeHealthRec(strRecord, recNum, false);
    }
    
    
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum, boolean skipPassthru) throws ParseException {
        Oasis_C1_Record_2_12 oasisRecord;
        oasisRecord = new Oasis_C1_Record_2_12();

// validate that this is an Body record
        if (strRecord == null) {
            throw new ParseException("OASIS record string can not be null", 0);
        } else if (strRecord.length() < 3256) {
            throw new ParseException("Unknown record due to invalid length of "
                    + strRecord.length() + ", should be at least 3256 characters.", 0);
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
        // ITEM_FILLER_020 - Filler: replaces M1020_PRIMARY_DIAG_ICD
        // ITEM_FILLER_021 - Filler: replaces M1020_PRIMARY_DIAG_SEVERITY
        // ITEM_FILLER_022 - Filler: replaces M1022_OTH_DIAG1_ICD
        // ITEM_FILLER_023 - Filler: replaces M1022_OTH_DIAG1_SEVERITY
        // ITEM_FILLER_024 - Filler: replaces M1022_OTH_DIAG2_ICD
        // ITEM_FILLER_025 - Filler: replaces M1022_OTH_DIAG2_SEVERITY
        // ITEM_FILLER_026 - Filler: replaces M1022_OTH_DIAG3_ICD
        // ITEM_FILLER_027 - Filler: replaces M1022_OTH_DIAG3_SEVERITY
        // ITEM_FILLER_028 - Filler: replaces M1022_OTH_DIAG4_ICD
        // ITEM_FILLER_029 - Filler: replaces M1022_OTH_DIAG4_SEVERITY
        // ITEM_FILLER_030 - Filler: replaces M1022_OTH_DIAG5_ICD
        // ITEM_FILLER_031 - Filler: replaces M1022_OTH_DIAG5_SEVERITY
        // ITEM_FILLER_032 - Filler: replaces M1024_PMT_DIAG_ICD_A3
        // ITEM_FILLER_033 - Filler: replaces M1024_PMT_DIAG_ICD_B3
        // ITEM_FILLER_034 - Filler: replaces M1024_PMT_DIAG_ICD_C3
        // ITEM_FILLER_035 - Filler: replaces M1024_PMT_DIAG_ICD_D3
        // ITEM_FILLER_036 - Filler: replaces M1024_PMT_DIAG_ICD_E3
        // ITEM_FILLER_037 - Filler: replaces M1024_PMT_DIAG_ICD_F3
        // ITEM_FILLER_038 - Filler: replaces M1024_PMT_DIAG_ICD_A4
        // ITEM_FILLER_039 - Filler: replaces M1024_PMT_DIAG_ICD_B4
        // ITEM_FILLER_040 - Filler: replaces M1024_PMT_DIAG_ICD_C4
        // ITEM_FILLER_041 - Filler: replaces M1024_PMT_DIAG_ICD_D4
        // ITEM_FILLER_042 - Filler: replaces M1024_PMT_DIAG_ICD_E4
        // ITEM_FILLER_043 - Filler: replaces M1024_PMT_DIAG_ICD_F4
        oasisRecord.setTHH_IV_INFUSION(strRecord.substring(742, 743));   // M1030_THH_IV_INFUSION - Therapies received at home: intravenous, infusion
        oasisRecord.setTHH_PAR_NUTRITION(strRecord.substring(743, 744));   // M1030_THH_PAR_NUTRITION - Therapies received at home: parenteral nutrition
        oasisRecord.setTHH_ENT_NUTRITION(strRecord.substring(744, 745));   // M1030_THH_ENT_NUTRITION - Therapies received at home: enteral nutrition
        oasisRecord.setTHH_NONE_ABOVE(strRecord.substring(745, 746));   // M1030_THH_NONE_ABOVE - Therapies received at home: none of the above
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
        oasisRecord.setVISION(strRecord.substring(770, 772));   // M1200_VISION - Sensory status: vision
        // M1210_HEARG_ABLTY - Ability to hear
        // M1220_UNDRSTG_VERBAL_CNTNT - Understanding verbal content in patient's language
        // M1230_SPEECH - Sensory status: speech
        // M1240_FRML_PAIN_ASMT - Has patient had a formal validated pain assessment
        oasisRecord.setPAIN_FREQ_ACTVTY_MVMT(strRecord.substring(780, 782));   // M1242_PAIN_FREQ_ACTVTY_MVMT - Freq of pain interfering with pt activity/movement
        // M1300_PRSR_ULCR_RISK_ASMT - Was patient assessed for risk of developing PUs
        // M1302_RISK_OF_PRSR_ULCR - Does this patient have a risk of developing PUs
        oasisRecord.setUNHLD_STG2_PRSR_ULCR(strRecord.substring(785, 786));   // M1306_UNHLD_STG2_PRSR_ULCR - Patient has 1+ unhealed PU at stage 2 or higher
        // M1307_OLDST_STG2_AT_DSCHRG - Status of oldest stage 2 pressure ulcer at disch
        // M1307_OLDST_STG2_ONST_DT - Date of onset of oldest stage 2 pressure ulcer
        oasisRecord.setNBR_PRSULC_STG2(strRecord.substring(796, 798));   // M1308_NBR_PRSULC_STG2 - Number of stage 2 pressure ulcers
        // ITEM_FILLER_055 - Filler: replaces M1308_NBR_STG2_AT_SOC_ROC
        oasisRecord.setNBR_PRSULC_STG3(strRecord.substring(800, 802));   // M1308_NBR_PRSULC_STG3 - Number of stage 3 pressure ulcers
        // ITEM_FILLER_056 - Filler: replaces M1308_NBR_STG3_AT_SOC_ROC
        oasisRecord.setNBR_PRSULC_STG4(strRecord.substring(804, 806));   // M1308_NBR_PRSULC_STG4 - Number of stage 4 pressure ulcers
        // ITEM_FILLER_057 - Filler: replaces M1308_NBR_STG4_AT_SOC_ROC
        oasisRecord.setNSTG_DRSG(strRecord.substring(808, 810));   // M1308_NSTG_DRSG - Unstageable: non-removable dressing/device
        // ITEM_FILLER_058 - Filler: replaces M1308_NSTG_DRSG_SOC_ROC
        oasisRecord.setNSTG_CVRG(strRecord.substring(812, 814));   // M1308_NSTG_CVRG - Unstageable: coverage by slough or eschar
        // ITEM_FILLER_059 - Filler: replaces M1308_NSTG_CVRG_SOC_ROC
        oasisRecord.setNSTG_DEEP_TISUE(strRecord.substring(816, 818));   // M1308_NSTG_DEEP_TISUE - Unstageable: suspect deep tissue injury
        // ITEM_FILLER_060 - Filler: replaces M1308_NSTG_DEEP_TISUE_SOC_ROC
        // ITEM_FILLER_061 - Filler: replaces M1310_PRSR_ULCR_LNGTH
        // ITEM_FILLER_062 - Filler: replaces M1312_PRSR_ULCR_WDTH
        // ITEM_FILLER_063 - Filler: replaces M1314_PRSR_ULCR_DEPTH
        oasisRecord.setSTUS_PRBLM_PRSR_ULCR(strRecord.substring(832, 834));   // M1320_STUS_PRBLM_PRSR_ULCR - Status of most problematic pressure ulcer
        oasisRecord.setNBR_PRSULC_STG1(strRecord.substring(834, 836));   // M1322_NBR_PRSULC_STG1 - Number of stage 1 pressure ulcers
        oasisRecord.setSTG_PRBLM_ULCER(strRecord.substring(836, 838));   // M1324_STG_PRBLM_ULCER - Stage of most problematic pressure ulcer
        oasisRecord.setSTAS_ULCR_PRSNT(strRecord.substring(838, 840));   // M1330_STAS_ULCR_PRSNT - Does this patient have a stasis ulcer
        oasisRecord.setNBR_STAS_ULCR(strRecord.substring(840, 842));   // M1332_NBR_STAS_ULCR - Number of stasis ulcers
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
        oasisRecord.setCRNT_DRESS_UPPER(strRecord.substring(897, 899));   // M1810_CRNT_DRESS_UPPER - Current: dress upper body
        oasisRecord.setCRNT_DRESS_LOWER(strRecord.substring(899, 901));   // M1820_CRNT_DRESS_LOWER - Current: dress lower body
        oasisRecord.setCRNT_BATHG(strRecord.substring(901, 903));   // M1830_CRNT_BATHG - Current: bathing
        oasisRecord.setCRNT_TOILTG(strRecord.substring(903, 905));   // M1840_CRNT_TOILTG - Current: toileting
        // M1845_CRNT_TOILTG_HYGN - Current: toileting hygiene
        oasisRecord.setCRNT_TRNSFRNG(strRecord.substring(907, 909));   // M1850_CRNT_TRNSFRNG - Current: transferring
        oasisRecord.setCRNT_AMBLTN(strRecord.substring(909, 911));   // M1860_CRNT_AMBLTN - Current: ambulation
        // M1870_CRNT_FEEDING - Current: feeding
        // M1880_CRNT_PREP_LT_MEALS - Current: prepare light meals
        // M1890_CRNT_PHONE_USE - Current: telephone use
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
        // ITEM_FILLER_064 - Filler: replaces M2100_CARE_TYPE_SRC_ADL
        // ITEM_FILLER_065 - Filler: replaces M2100_CARE_TYPE_SRC_IADL
        // ITEM_FILLER_066 - Filler: replaces M2100_CARE_TYPE_SRC_MDCTN
        // ITEM_FILLER_067 - Filler: replaces M2100_CARE_TYPE_SRC_PRCDR
        // ITEM_FILLER_068 - Filler: replaces M2100_CARE_TYPE_SRC_EQUIP
        // ITEM_FILLER_069 - Filler: replaces M2100_CARE_TYPE_SRC_SPRVSN
        // ITEM_FILLER_070 - Filler: replaces M2100_CARE_TYPE_SRC_ADVCY
        // M2110_ADL_IADL_ASTNC_FREQ - How often recv non-HHA caregiver ADL/IADL assist
        oasisRecord.setTHER_NEED_NBR(HomeHealthRecordUtil.parseTherapyNeedNumber_C1(strRecord.substring(960, 963), 0));   // M2200_THER_NEED_NBR - Therapy need: number of visits indicated
        oasisRecord.setTHER_NEED_NA(strRecord.substring(963, 964));   // M2200_THER_NEED_NA - Therapy need: not applicable
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
        // M2310_ECR_HYPOGLYC - Emergent care reason: hypoglycemia/hyperglycemia
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
        // M2430_HOSP_RSPRTRY_INFCTN - Hospitalized: respiratory infection
        // M2430_HOSP_RSPRTRY_OTHR - Hospitalized: other respiratory
        // M2430_HOSP_HRT_FAILR - Hospitalized: heart failure
        // M2430_HOSP_CRDC_DSRTHM - Hospitalized: cardiac dysrhythmia
        // M2430_HOSP_MI_CHST_PAIN - Hospitalized: myocardial infarction or chest pain
        // M2430_HOSP_OTHR_HRT_DEASE - Hospitalized: other heart disease
        // M2430_HOSP_STROKE_TIA - Hospitalized: stroke (CVA) or TIA
        // M2430_HOSP_HYPOGLYC - Hospitalized: hypoglycemia/hyperglycemia
        // M2430_HOSP_GI_PRBLM - Hospitalized: GI bleed/obstruct/constip/impact
        // M2430_HOSP_DHYDRTN_MALNTR - Hospitalized: dehydration, malnutrition
        // M2430_HOSP_UR_TRACT - Hospitalized: urinary tract infection
        // M2430_HOSP_CTHTR_CMPLCTN - Hospitalized: IV catheter infect/complic
        // M2430_HOSP_WND_INFCTN - Hospitalized: wound infect/deterioration
        // M2430_HOSP_PAIN - Hospitalized: uncontrolled pain
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
        oasisRecord.setPRIMARY_DIAG_ICD(parseDxCode(strRecord.substring(1358, 1366)));   // M1021_PRIMARY_DIAG_ICD - Primary diagnosis ICD code
        // M1021_PRIMARY_DIAG_SEVERITY - Primary diagnosis severity rating
        oasisRecord.setOTH_DIAG1_ICD(parseDxCode(strRecord.substring(1368, 1376)));   // M1023_OTH_DIAG1_ICD - Other diagnosis 1: ICD code
        // M1023_OTH_DIAG1_SEVERITY - Other diagnosis 1: severity rating
        oasisRecord.setOTH_DIAG2_ICD(parseDxCode(strRecord.substring(1378, 1386)));   // M1023_OTH_DIAG2_ICD - Other diagnosis 2: ICD code
        // M1023_OTH_DIAG2_SEVERITY - Other diagnosis 2: severity rating
        oasisRecord.setOTH_DIAG3_ICD(parseDxCode(strRecord.substring(1388, 1396)));   // M1023_OTH_DIAG3_ICD - Other diagnosis 3: ICD code
        // M1023_OTH_DIAG3_SEVERITY - Other diagnosis 3: severity rating
        oasisRecord.setOTH_DIAG4_ICD(parseDxCode(strRecord.substring(1398, 1406)));   // M1023_OTH_DIAG4_ICD - Other diagnosis 4: ICD code
        // M1023_OTH_DIAG4_SEVERITY - Other diagnosis 4: severity rating
        oasisRecord.setOTH_DIAG5_ICD(parseDxCode(strRecord.substring(1408, 1416)));   // M1023_OTH_DIAG5_ICD - Other diagnosis 5: ICD code
        // M1023_OTH_DIAG5_SEVERITY - Other diagnosis 5: severity rating
        //oasisRecord.setPMT_DIAG_ICD_A3(parseDxCode(strRecord.substring(1418, 1426)));   // M1025_OPT_DIAG_ICD_A3 - Optional diagnosis: primary, column 3
        //oasisRecord.setPMT_DIAG_ICD_A4(parseDxCode(strRecord.substring(1426, 1434)));   // M1025_OPT_DIAG_ICD_A4 - Optional diagnosis: primary, column 4
        //oasisRecord.setPMT_DIAG_ICD_B3(parseDxCode(strRecord.substring(1434, 1442)));   // M1025_OPT_DIAG_ICD_B3 - Optional diagnosis: first secondary, column 3
        //oasisRecord.setPMT_DIAG_ICD_B4(parseDxCode(strRecord.substring(1442, 1450)));   // M1025_OPT_DIAG_ICD_B4 - Optional diagnosis: first secondary, column 4
        //oasisRecord.setPMT_DIAG_ICD_C3(parseDxCode(strRecord.substring(1450, 1458)));   // M1025_OPT_DIAG_ICD_C3 - Optional diagnosis: second secondary, column 3
        //oasisRecord.setPMT_DIAG_ICD_C4(parseDxCode(strRecord.substring(1458, 1466)));   // M1025_OPT_DIAG_ICD_C4 - Optional diagnosis: second secondary, column 4
        //oasisRecord.setPMT_DIAG_ICD_D3(parseDxCode(strRecord.substring(1466, 1474)));   // M1025_OPT_DIAG_ICD_D3 - Optional diagnosis: third secondary, column 3
        //oasisRecord.setPMT_DIAG_ICD_D4(parseDxCode(strRecord.substring(1474, 1482)));   // M1025_OPT_DIAG_ICD_D4 - Optional diagnosis: third secondary, column 4
        //oasisRecord.setPMT_DIAG_ICD_E3(parseDxCode(strRecord.substring(1482, 1490)));   // M1025_OPT_DIAG_ICD_E3 - Optional diagnosis: fourth secondary, column 3
        //oasisRecord.setPMT_DIAG_ICD_E4(parseDxCode(strRecord.substring(1490, 1498)));   // M1025_OPT_DIAG_ICD_E4 - Optional diagnosis: fourth secondary, column 4
        //oasisRecord.setPMT_DIAG_ICD_F3(parseDxCode(strRecord.substring(1498, 1506)));   // M1025_OPT_DIAG_ICD_F3 - Optional diagnosis: fifth secondary, column 3
        //oasisRecord.setPMT_DIAG_ICD_F4(parseDxCode(strRecord.substring(1506, 1514)));   // M1025_OPT_DIAG_ICD_F4 - Optional diagnosis: fifth secondary, column 4
        // M1033_HOSP_RISK_HSTRY_FALLS - Hosp risk: 2+ falls or injury fall in past year
        // M1033_HOSP_RISK_WEIGHT_LOSS - Hosp risk: unintentional weight loss
        // M1033_HOSP_RISK_MLTPL_HOSPZTN - Hosp risk: 2+ hospitalizations in past 12 months
        // M1033_HOSP_RISK_MLTPL_ED_VISIT - Hosp risk: 2+ emergcy dept visits in past 6 months
        // M1033_HOSP_RISK_MNTL_BHV_DCLN - Hosp risk: decline mental/emotional/behav status
        // M1033_HOSP_RISK_COMPLIANCE - Hosp risk: difficulty with medical instructions
        // M1033_HOSP_RISK_5PLUS_MDCTN - Hosp risk: taking five or more medications
        // M1033_HOSP_RISK_CRNT_EXHSTN - Hosp risk: current exhaustion
        // M1033_HOSP_RISK_OTHR_RISK - Hosp risk: other risk(s) not listed
        // M1033_HOSP_RISK_NONE_ABOVE - Hosp risk: none of the above
        // M1041_IN_INFLNZ_SEASON - Does episode include Oct 1 thru Mar 31
        // M1046_INFLNZ_RECD_CRNT_SEASON - Did patient receive influenza vaccine
        // M1051_PVX_RCVD_AGNCY - Was pneumococcal vaccine received
        // M1056_PVX_RSN_NOT_RCVD_AGNCY - If pneumococcal vacc not received, state reason
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
        // M1309_NBR_NEW_WRS_PRSULC_NSTG - Number of new or worsening unstageable
        // ASMT_ITEMS_FILLER - Assessment items filler
        // LEGACY_ITEMS_FILLER - Discontinued OASIS-B1 and C items.
        // C_CCN_NUM - Calculated Facility CMS Certification Number (CCN)
        // CALCULATED_ITEMS_FILLER - Calculated items filler
        // HHA_ASMT_INT_ID - Assessment internal ID
        // ORIG_ASMT_INT_ID - Original assessment ID
        // RES_INT_ID - Resident internal ID
        // ASMT_EFF_DATE - Effective date
        // CALC_FILLER_001 - Filler: replaces BRANCH_IDENTIFIER
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

        return oasisRecord;
    }

    public StringBuilder convertFromHomeHealthRecDelimeted(HomeHealthRecordIF oasisRecord, String delimiter) {
        return  convertFromHomeHealthToFlatRecord((HomeHealthRecord_C_IF) oasisRecord, delimiter);
    }

    
    /**
     * 
     * @param oasisRecord
     * @param delimiter
     * @return 
     */
    public StringBuilder convertFromHomeHealthToFlatRecord(HomeHealthRecord_C_IF oasisRecord, String delimiter) {
        final StringBuilder buffer = new StringBuilder(getRecordLength());

        if (delimiter == null) {
            delimiter = "";
        }
        
        buffer.append("          ");   // ASMT_SYS_CD - Assessment system code
        buffer.append(delimiter);
        buffer.append(" ");   // TRANS_TYPE_CD - Transaction type code
        buffer.append(delimiter);
        buffer.append("   ");   // ITM_SBST_CD - Item subset code
        buffer.append(delimiter);
        buffer.append("          ");   // ITM_SET_VRSN_CD - Item set version code
        buffer.append(delimiter);
        buffer.append("          ");   // SPEC_VRSN_CD - Specifications version code
        buffer.append(delimiter);
        buffer.append("  ");   // CORRECTION_NUM - Correction number
        buffer.append(delimiter);
        buffer.append("  ");   // STATE_CD - Agency's state postal code
        buffer.append(delimiter);
        buffer.append("                ");   // HHA_AGENCY_ID - Assigned agency submission ID
        buffer.append(delimiter);
        buffer.append("          ");   // NATL_PRVDR_ID - Agency National Provider ID (NPI)
        buffer.append(delimiter);
        buffer.append("         ");   // SFW_ID - Software vendor federal employer tax ID
        buffer.append(delimiter);
        buffer.append("                              ");   // SFW_NAME - Software vendor company name
        buffer.append(delimiter);
        buffer.append("                                                  ");   // SFW_EMAIL_ADR - Software vendor email address
        buffer.append(delimiter);
        buffer.append("                                                  ");   // SFW_PROD_NAME - Software product name
        buffer.append(delimiter);
        buffer.append("                    ");   // SFW_PROD_VRSN_CD - Software product version code
        buffer.append(delimiter);
        buffer.append("                    ");   // ACY_DOC_CD - Document ID code (agency use)
        buffer.append(delimiter);
        buffer.append("     ");   // SUBM_HIPPS_CODE - HIPPS group code: submitted
        buffer.append(delimiter);
        buffer.append("     ");   // SUBM_HIPPS_VERSION - HIPPS version code: submitted
        buffer.append(delimiter);
        buffer.append("      ");   // M0010_CCN - Facility CMS certification number (CCN)
        buffer.append(delimiter);
        buffer.append("  ");   // M0014_BRANCH_STATE - Branch state
        buffer.append(delimiter);
        buffer.append("          ");   // M0016_BRANCH_ID - Branch ID
        buffer.append(delimiter);
        buffer.append("          ");   // M0018_PHYSICIAN_ID - Attending physician National Provider ID (NPI)
        buffer.append(delimiter);
        buffer.append(" ");   // M0018_PHYSICIAN_UK - Attending physician NPI: Unknown
        buffer.append(delimiter);
        buffer.append("                    ");   // M0020_PAT_ID - Patient ID number
        buffer.append(delimiter);
        OasisCalendarFormatter.format(oasisRecord.getSTART_CARE_DT(), buffer);   // M0030_START_CARE_DT - Start of care date
        buffer.append(delimiter);
        buffer.append("        ");   // M0032_ROC_DT - Resumption of care date
        buffer.append(delimiter);
        buffer.append(" ");   // M0032_ROC_DT_NA - No resumption of care date
        buffer.append(delimiter);
        buffer.append("            ");   // M0040_PAT_FNAME - Patient's first name
        buffer.append(delimiter);
        buffer.append(" ");   // M0040_PAT_MI - Patient's middle initial
        buffer.append(delimiter);
        buffer.append("                  ");   // M0040_PAT_LNAME - Patient's last name
        buffer.append(delimiter);
        buffer.append("   ");   // M0040_PAT_SUFFIX - Patient's suffix
        buffer.append(delimiter);
        buffer.append("  ");   // M0050_PAT_ST - Patient state of residence
        buffer.append(delimiter);
        buffer.append("           ");   // M0060_PAT_ZIP - Patient zip code
        buffer.append(delimiter);
        buffer.append("            ");   // M0063_MEDICARE_NUM - Medicare number, including suffix
        buffer.append(delimiter);
        buffer.append(" ");   // M0063_MEDICARE_NA - No Medicare number
        buffer.append(delimiter);
        buffer.append("         ");   // M0064_SSN - Patient's Social Security number
        buffer.append(delimiter);
        buffer.append(" ");   // M0064_SSN_UK - No Social Security number
        buffer.append(delimiter);
        buffer.append("              ");   // M0065_MEDICAID_NUM - Medicaid number
        buffer.append(delimiter);
        buffer.append(" ");   // M0065_MEDICAID_NA - No Medicaid number
        buffer.append(delimiter);
        buffer.append("        ");   // M0066_PAT_BIRTH_DT - Date of birth
        buffer.append(delimiter);
        buffer.append(" ");   // M0069_PAT_GENDER - Gender
        buffer.append(delimiter);
        buffer.append(" ");   // M0140_ETHNIC_AI_AN - Ethnicity: American Indian or Alaska Native
        buffer.append(delimiter);
        buffer.append(" ");   // M0140_ETHNIC_ASIAN - Ethnicity: Asian
        buffer.append(delimiter);
        buffer.append(" ");   // M0140_ETHNIC_BLACK - Ethnicity: Black or African American
        buffer.append(delimiter);
        buffer.append(" ");   // M0140_ETHNIC_HISP - Ethnicity: Hispanic or Latino
        buffer.append(delimiter);
        buffer.append(" ");   // M0140_ETHNIC_NH_PI - Ethnicity: Native Hawaiian/Pacific Islander
        buffer.append(delimiter);
        buffer.append(" ");   // M0140_ETHNIC_WHITE - Ethnicity: White
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_NONE - Payment sources: no charge for current services
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_MCARE_FFS - Payment sources: Medicare fee-for-service
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_MCARE_HMO - Payment sources: Medicare HMO/managed care
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_MCAID_FFS - Payment sources: Medicaid fee-for-service
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_MCAID_HMO - Payment sources: Medicaid HMO/managed care
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_WRKCOMP - Payment sources: worker's compensation
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_TITLEPGMS - Payment sources: title programs
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_OTH_GOVT - Payment sources: other government
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_PRIV_INS - Payment sources: private insurance
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_PRIV_HMO - Payment sources: private HMO/managed care
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_SELFPAY - Payment sources: self-pay
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_OTHER - Payment sources: other
        buffer.append(delimiter);
        buffer.append(" ");   // M0150_CPAY_UK - Payment sources: unknown
        buffer.append(delimiter);
        buffer.append("  ");   // M0080_ASSESSOR_DISCIPLINE - Discipline of person completing assessment
        buffer.append(delimiter);
        OasisCalendarFormatter.format(oasisRecord.getINFO_COMPLETED_DT(), buffer);   // M0090_INFO_COMPLETED_DT - Date assessment completed
        buffer.append(delimiter);
        buffer.append(oasisRecord.getASSMT_REASON());   // M0100_ASSMT_REASON - Reason for assessment
        buffer.append(delimiter);
        buffer.append("        ");   // M0102_PHYSN_ORDRD_SOCROC_DT - Physician ordered SOC/ROC date
        buffer.append(delimiter);
        buffer.append(" ");   // M0102_PHYSN_ORDRD_SOCROC_DT_NA - Physician ordered SOC/ROC date - NA
        buffer.append(delimiter);
        buffer.append("        ");   // M0104_PHYSN_RFRL_DT - Physician date of referral
        buffer.append(delimiter);
        buffer.append(oasisRecord.getEPISODE_TIMING());   // M0110_EPISODE_TIMING - Episode timing
        buffer.append(delimiter);
        buffer.append(" ");   // M1000_DC_LTC_14_DA - Past 14 days: disch from LTC NH
        buffer.append(delimiter);
        buffer.append(" ");   // M1000_DC_SNF_14_DA - Past 14 days: disch from skilled nursing facility
        buffer.append(delimiter);
        buffer.append(" ");   // M1000_DC_IPPS_14_DA - Past 14 days: disch from short stay acute hospital
        buffer.append(delimiter);
        buffer.append(" ");   // M1000_DC_LTCH_14_DA - Past 14 days: disch from long term care hospital
        buffer.append(delimiter);
        buffer.append(" ");   // M1000_DC_IRF_14_DA - Past 14 days: disch from inpatient rehab facility
        buffer.append(delimiter);
        buffer.append(" ");   // M1000_DC_PSYCH_14_DA - Past 14 days: disch from psych hospital or unit
        buffer.append(delimiter);
        buffer.append(" ");   // M1000_DC_OTH_14_DA - Past 14 days: disch from other
        buffer.append(delimiter);
        buffer.append(" ");   // M1000_DC_NONE_14_DA - Past 14 days: not disch from inpatient facility
        buffer.append(delimiter);
        buffer.append("        ");   // M1005_INP_DISCHARGE_DT - Most recent inpatient discharge date
        buffer.append(delimiter);
        buffer.append(" ");   // M1005_INP_DSCHG_UNKNOWN - Inpatient discharge date unknown
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_001 - Filler: replaces M1010_14_DAY_INP1_ICD
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_002 - Filler: replaces M1010_14_DAY_INP2_ICD
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_003 - Filler: replaces M1010_14_DAY_INP3_ICD
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_004 - Filler: replaces M1010_14_DAY_INP4_ICD
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_005 - Filler: replaces M1010_14_DAY_INP5_ICD
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_006 - Filler: replaces M1010_14_DAY_INP6_ICD
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_007 - Filler: replaces M1012_INP_PRCDR1_ICD
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_008 - Filler: replaces M1012_INP_PRCDR2_ICD
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_009 - Filler: replaces M1012_INP_PRCDR3_ICD
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_010 - Filler: replaces M1012_INP_PRCDR4_ICD
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_011 - Filler: replaces M1012_INP_NA_ICD
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_012 - Filler: replaces M1012_INP_UK_ICD
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_013 - Filler: replaces M1016_CHGREG_ICD1
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_014 - Filler: replaces M1016_CHGREG_ICD2
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_015 - Filler: replaces M1016_CHGREG_ICD3
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_016 - Filler: replaces M1016_CHGREG_ICD4
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_017 - Filler: replaces M1016_CHGREG_ICD5
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_018 - Filler: replaces M1016_CHGREG_ICD6
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_019 - Filler: replaces M1016_CHGREG_ICD_NA
        buffer.append(delimiter);
        buffer.append(" ");   // M1018_PRIOR_UR_INCON - Prior condition: urinary incontinence
        buffer.append(delimiter);
        buffer.append(" ");   // M1018_PRIOR_CATH - Prior condition: indwelling/suprapubic catheter
        buffer.append(delimiter);
        buffer.append(" ");   // M1018_PRIOR_INTRACT_PAIN - Prior condition: intractable pain
        buffer.append(delimiter);
        buffer.append(" ");   // M1018_PRIOR_IMPR_DECSN - Prior condition: impaired decision-making
        buffer.append(delimiter);
        buffer.append(" ");   // M1018_PRIOR_DISRUPTIVE - Prior condition: disruptive/inappropriate behav
        buffer.append(delimiter);
        buffer.append(" ");   // M1018_PRIOR_MEM_LOSS - Prior condition: memory loss, supervision required
        buffer.append(delimiter);
        buffer.append(" ");   // M1018_PRIOR_NONE - Prior condition: none of the above
        buffer.append(delimiter);
        buffer.append(" ");   // M1018_PRIOR_NOCHG_14D - Prior condition: no inpt disch/no change regimen
        buffer.append(delimiter);
        buffer.append(" ");   // M1018_PRIOR_UNKNOWN - Prior condition: unknown
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_020 - Filler: replaces M1020_PRIMARY_DIAG_ICD
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_021 - Filler: replaces M1020_PRIMARY_DIAG_SEVERITY
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_022 - Filler: replaces M1022_OTH_DIAG1_ICD
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_023 - Filler: replaces M1022_OTH_DIAG1_SEVERITY
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_024 - Filler: replaces M1022_OTH_DIAG2_ICD
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_025 - Filler: replaces M1022_OTH_DIAG2_SEVERITY
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_026 - Filler: replaces M1022_OTH_DIAG3_ICD
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_027 - Filler: replaces M1022_OTH_DIAG3_SEVERITY
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_028 - Filler: replaces M1022_OTH_DIAG4_ICD
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_029 - Filler: replaces M1022_OTH_DIAG4_SEVERITY
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_030 - Filler: replaces M1022_OTH_DIAG5_ICD
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_031 - Filler: replaces M1022_OTH_DIAG5_SEVERITY
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_032 - Filler: replaces M1024_PMT_DIAG_ICD_A3
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_033 - Filler: replaces M1024_PMT_DIAG_ICD_B3
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_034 - Filler: replaces M1024_PMT_DIAG_ICD_C3
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_035 - Filler: replaces M1024_PMT_DIAG_ICD_D3
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_036 - Filler: replaces M1024_PMT_DIAG_ICD_E3
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_037 - Filler: replaces M1024_PMT_DIAG_ICD_F3
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_038 - Filler: replaces M1024_PMT_DIAG_ICD_A4
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_039 - Filler: replaces M1024_PMT_DIAG_ICD_B4
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_040 - Filler: replaces M1024_PMT_DIAG_ICD_C4
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_041 - Filler: replaces M1024_PMT_DIAG_ICD_D4
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_042 - Filler: replaces M1024_PMT_DIAG_ICD_E4
        buffer.append(delimiter);
        buffer.append("       ");   // ITEM_FILLER_043 - Filler: replaces M1024_PMT_DIAG_ICD_F4
        buffer.append(delimiter);
        buffer.append(oasisRecord.getTHH_IV_INFUSION());   // M1030_THH_IV_INFUSION - Therapies received at home: intravenous, infusion
        buffer.append(delimiter);
        buffer.append(oasisRecord.getTHH_PAR_NUTRITION());   // M1030_THH_PAR_NUTRITION - Therapies received at home: parenteral nutrition
        buffer.append(delimiter);
        buffer.append(oasisRecord.getTHH_ENT_NUTRITION());   // M1030_THH_ENT_NUTRITION - Therapies received at home: enteral nutrition
        buffer.append(delimiter);
        buffer.append(oasisRecord.getTHH_NONE_ABOVE());   // M1030_THH_NONE_ABOVE - Therapies received at home: none of the above
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_044 - Filler: replaces M1032_HOSP_RISK_RCNT_DCLN
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_045 - Filler: replaces M1032_HOSP_RISK_MLTPL_HOSPZTN
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_046 - Filler: replaces M1032_HOSP_RISK_HSTRY_FALLS
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_047 - Filler: replaces M1032_HOSP_RISK_5PLUS_MDCTN
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_048 - Filler: replaces M1032_HOSP_RISK_FRAILTY
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_049 - Filler: replaces M1032_HOSP_RISK_OTHR
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_050 - Filler: replaces M1032_HOSP_RISK_NONE_ABOVE
        buffer.append(delimiter);
        buffer.append("  ");   // M1034_PTNT_OVRAL_STUS - Patient's overall status
        buffer.append(delimiter);
        buffer.append(" ");   // M1036_RSK_SMOKING - High risk factor: smoking
        buffer.append(delimiter);
        buffer.append(" ");   // M1036_RSK_OBESITY - High risk factor: obesity
        buffer.append(delimiter);
        buffer.append(" ");   // M1036_RSK_ALCOHOLISM - High risk factor: alcoholism
        buffer.append(delimiter);
        buffer.append(" ");   // M1036_RSK_DRUGS - High risk factor: drugs
        buffer.append(delimiter);
        buffer.append(" ");   // M1036_RSK_NONE - High risk factor: none of the above
        buffer.append(delimiter);
        buffer.append(" ");   // M1036_RSK_UNKNOWN - High risk factor: unknown
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_051 - Filler: replaces M1040_INFLNZ_RCVD_AGNCY
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_052 - Filler: replaces M1045_INFLNZ_RSN_NOT_RCVD
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_053 - Filler: replaces M1050_PPV_RCVD_AGNCY
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_054 - Filler: replaces M1055_PPV_RSN_NOT_RCVD_AGNCY
        buffer.append(delimiter);
        buffer.append("  ");   // M1100_PTNT_LVG_STUTN - Patient living situation
        buffer.append(delimiter);
        buffer.append(oasisRecord.getVISION());   // M1200_VISION - Sensory status: vision
        buffer.append(delimiter);
        buffer.append("  ");   // M1210_HEARG_ABLTY - Ability to hear
        buffer.append(delimiter);
        buffer.append("  ");   // M1220_UNDRSTG_VERBAL_CNTNT - Understanding verbal content in patient's language
        buffer.append(delimiter);
        buffer.append("  ");   // M1230_SPEECH - Sensory status: speech
        buffer.append(delimiter);
        buffer.append("  ");   // M1240_FRML_PAIN_ASMT - Has patient had a formal validated pain assessment
        buffer.append(delimiter);
        buffer.append(oasisRecord.getPAIN_FREQ_ACTVTY_MVMT());   // M1242_PAIN_FREQ_ACTVTY_MVMT - Freq of pain interfering with pt activity/movement
        buffer.append(delimiter);
        buffer.append("  ");   // M1300_PRSR_ULCR_RISK_ASMT - Was patient assessed for risk of developing PUs
        buffer.append(delimiter);
        buffer.append(" ");   // M1302_RISK_OF_PRSR_ULCR - Does this patient have a risk of developing PUs
        buffer.append(delimiter);
        buffer.append(oasisRecord.getUNHLD_STG2_PRSR_ULCR());   // M1306_UNHLD_STG2_PRSR_ULCR - Patient has 1+ unhealed PU at stage 2 or higher
        buffer.append(delimiter);
        buffer.append("  ");   // M1307_OLDST_STG2_AT_DSCHRG - Status of oldest stage 2 pressure ulcer at disch
        buffer.append(delimiter);
        buffer.append("        ");   // M1307_OLDST_STG2_ONST_DT - Date of onset of oldest stage 2 pressure ulcer
        buffer.append(delimiter);
        buffer.append(oasisRecord.getNBR_PRSULC_STG2());   // M1308_NBR_PRSULC_STG2 - Number of stage 2 pressure ulcers
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_055 - Filler: replaces M1308_NBR_STG2_AT_SOC_ROC
        buffer.append(delimiter);
        buffer.append(oasisRecord.getNBR_PRSULC_STG3());   // M1308_NBR_PRSULC_STG3 - Number of stage 3 pressure ulcers
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_056 - Filler: replaces M1308_NBR_STG3_AT_SOC_ROC
        buffer.append(delimiter);
        buffer.append(oasisRecord.getNBR_PRSULC_STG4());   // M1308_NBR_PRSULC_STG4 - Number of stage 4 pressure ulcers
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_057 - Filler: replaces M1308_NBR_STG4_AT_SOC_ROC
        buffer.append(delimiter);
        buffer.append(oasisRecord.getNSTG_DRSG());   // M1308_NSTG_DRSG - Unstageable: non-removable dressing/device
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_058 - Filler: replaces M1308_NSTG_DRSG_SOC_ROC
        buffer.append(delimiter);
        buffer.append(oasisRecord.getNSTG_CVRG());   // M1308_NSTG_CVRG - Unstageable: coverage by slough or eschar
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_059 - Filler: replaces M1308_NSTG_CVRG_SOC_ROC
        buffer.append(delimiter);
        buffer.append(oasisRecord.getNSTG_DEEP_TISUE());   // M1308_NSTG_DEEP_TISUE - Unstageable: suspect deep tissue injury
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_060 - Filler: replaces M1308_NSTG_DEEP_TISUE_SOC_ROC
        buffer.append(delimiter);
        buffer.append("    ");   // ITEM_FILLER_061 - Filler: replaces M1310_PRSR_ULCR_LNGTH
        buffer.append(delimiter);
        buffer.append("    ");   // ITEM_FILLER_062 - Filler: replaces M1312_PRSR_ULCR_WDTH
        buffer.append(delimiter);
        buffer.append("    ");   // ITEM_FILLER_063 - Filler: replaces M1314_PRSR_ULCR_DEPTH
        buffer.append(delimiter);
        buffer.append(oasisRecord.getSTUS_PRBLM_PRSR_ULCR());   // M1320_STUS_PRBLM_PRSR_ULCR - Status of most problematic pressure ulcer
        buffer.append(delimiter);
        buffer.append(oasisRecord.getNBR_PRSULC_STG1());   // M1322_NBR_PRSULC_STG1 - Number of stage 1 pressure ulcers
        buffer.append(delimiter);
        buffer.append(oasisRecord.getSTG_PRBLM_ULCER());   // M1324_STG_PRBLM_ULCER - Stage of most problematic pressure ulcer
        buffer.append(delimiter);
        buffer.append(oasisRecord.getSTAS_ULCR_PRSNT());   // M1330_STAS_ULCR_PRSNT - Does this patient have a stasis ulcer
        buffer.append(delimiter);
        buffer.append(oasisRecord.getNBR_STAS_ULCR());   // M1332_NBR_STAS_ULCR - Number of stasis ulcers
        buffer.append(delimiter);
        buffer.append(oasisRecord.getSTUS_PRBLM_STAS_ULCR());   // M1334_STUS_PRBLM_STAS_ULCR - Status of most problematic stasis ulcer
        buffer.append(delimiter);
        buffer.append(oasisRecord.getSRGCL_WND_PRSNT());   // M1340_SRGCL_WND_PRSNT - Does this patient have a surgical wound
        buffer.append(delimiter);
        buffer.append(oasisRecord.getSTUS_PRBLM_SRGCL_WND());   // M1342_STUS_PRBLM_SRGCL_WND - Status of most problematic surgical wound
        buffer.append(delimiter);
        buffer.append(oasisRecord.getLESION_OPEN_WND());   // M1350_LESION_OPEN_WND - Has skin lesion or open wound
        buffer.append(delimiter);
        buffer.append(oasisRecord.getWHEN_DYSPNEIC());   // M1400_WHEN_DYSPNEIC - When dyspneic
        buffer.append(delimiter);
        buffer.append(" ");   // M1410_RESPTX_OXYGEN - Respiratory treatments: oxygen
        buffer.append(delimiter);
        buffer.append(" ");   // M1410_RESPTX_VENTILATOR - Respiratory treatments: ventilator
        buffer.append(delimiter);
        buffer.append(" ");   // M1410_RESPTX_AIRPRESS - Respiratory treatments: airway pressure
        buffer.append(delimiter);
        buffer.append(" ");   // M1410_RESPTX_NONE - Respiratory treatments: none of the above
        buffer.append(delimiter);
        buffer.append("  ");   // M1500_SYMTM_HRT_FAILR_PTNTS - Symptoms in heart failure patients
        buffer.append(delimiter);
        buffer.append(" ");   // M1510_HRT_FAILR_NO_ACTN - Heart failure follow up:  no action
        buffer.append(delimiter);
        buffer.append(" ");   // M1510_HRT_FAILR_PHYSN_CNTCT - Heart failure follow up:  physician contacted
        buffer.append(delimiter);
        buffer.append(" ");   // M1510_HRT_FAILR_ER_TRTMT - Heart failure follow up:  ER treatment advised
        buffer.append(delimiter);
        buffer.append(" ");   // M1510_HRT_FAILR_PHYSN_TRTMT - Heart failure follow up:  phys-ordered treatmnt
        buffer.append(delimiter);
        buffer.append(" ");   // M1510_HRT_FAILR_CLNCL_INTRVTN - Heart failure follow up: pt educ/other clinical
        buffer.append(delimiter);
        buffer.append(" ");   // M1510_HRT_FAILR_CARE_PLAN_CHG - Heart failure follow up: change in care plan
        buffer.append(delimiter);
        buffer.append("  ");   // M1600_UTI - Treated for urinary tract infection past 14 days
        buffer.append(delimiter);
        buffer.append(oasisRecord.getUR_INCONT());   // M1610_UR_INCONT - Urinary incontinence or urinary catheter present
        buffer.append(delimiter);
        buffer.append(oasisRecord.getINCNTNT_TIMING());   // M1615_INCNTNT_TIMING - When urinary incontinence occurs
        buffer.append(delimiter);
        buffer.append(oasisRecord.getBWL_INCONT());   // M1620_BWL_INCONT - Bowel incontinence frequency
        buffer.append(delimiter);
        buffer.append(oasisRecord.getOSTOMY());   // M1630_OSTOMY - Ostomy for bowel elimination
        buffer.append(delimiter);
        buffer.append("  ");   // M1700_COG_FUNCTION - Cognitive functioning
        buffer.append(delimiter);
        buffer.append("  ");   // M1710_WHEN_CONFUSED - When confused (reported or observed)
        buffer.append(delimiter);
        buffer.append("  ");   // M1720_WHEN_ANXIOUS - When anxious (reported or observed)
        buffer.append(delimiter);
        buffer.append("  ");   // M1730_STDZ_DPRSN_SCRNG - Screened for depression using validated tool
        buffer.append(delimiter);
        buffer.append("  ");   // M1730_PHQ2_LACK_INTRST - PHQ2: little interest or pleasure in doing things
        buffer.append(delimiter);
        buffer.append("  ");   // M1730_PHQ2_DPRSN - PHQ2: feeling down, depressed or hopeless
        buffer.append(delimiter);
        buffer.append(" ");   // M1740_BD_MEM_DEFICIT - Behavior demonstrated: memory deficit
        buffer.append(delimiter);
        buffer.append(" ");   // M1740_BD_IMP_DECISN - Behavior demonstrated: impaired decision-making
        buffer.append(delimiter);
        buffer.append(" ");   // M1740_BD_VERBAL - Behavior demonstrated: verbal disruption
        buffer.append(delimiter);
        buffer.append(" ");   // M1740_BD_PHYSICAL - Behavior demonstrated: physical aggression
        buffer.append(delimiter);
        buffer.append(" ");   // M1740_BD_SOC_INAPPRO - Behavior demonstrated: socially inappropriate
        buffer.append(delimiter);
        buffer.append(" ");   // M1740_BD_DELUSIONS - Behavior demonstrated: delusions
        buffer.append(delimiter);
        buffer.append(" ");   // M1740_BD_NONE - Behavior demonstrated: none of the above
        buffer.append(delimiter);
        buffer.append("  ");   // M1745_BEH_PROB_FREQ - Frequency of behavior problems
        buffer.append(delimiter);
        buffer.append(" ");   // M1750_REC_PSYCH_NURS - Receives psychiatric nursing
         buffer.append(delimiter);
       buffer.append("  ");   // M1800_CRNT_GROOMING - Current: grooming
         buffer.append(delimiter);
       buffer.append(oasisRecord.getCRNT_DRESS_UPPER());   // M1810_CRNT_DRESS_UPPER - Current: dress upper body
         buffer.append(delimiter);
       buffer.append(oasisRecord.getCRNT_DRESS_LOWER());   // M1820_CRNT_DRESS_LOWER - Current: dress lower body
         buffer.append(delimiter);
       buffer.append(oasisRecord.getCRNT_BATHG());   // M1830_CRNT_BATHG - Current: bathing
        buffer.append(delimiter);
        buffer.append(oasisRecord.getCRNT_TOILTG());   // M1840_CRNT_TOILTG - Current: toileting
        buffer.append(delimiter);
        buffer.append("  ");   // M1845_CRNT_TOILTG_HYGN - Current: toileting hygiene
        buffer.append(delimiter);
        buffer.append(oasisRecord.getCRNT_TRNSFRNG());   // M1850_CRNT_TRNSFRNG - Current: transferring
        buffer.append(delimiter);
        buffer.append(oasisRecord.getCRNT_AMBLTN());   // M1860_CRNT_AMBLTN - Current: ambulation
        buffer.append(delimiter);
        buffer.append("  ");   // M1870_CRNT_FEEDING - Current: feeding
        buffer.append(delimiter);
        buffer.append("  ");   // M1880_CRNT_PREP_LT_MEALS - Current: prepare light meals
        buffer.append(delimiter);
        buffer.append("  ");   // M1890_CRNT_PHONE_USE - Current: telephone use
        buffer.append(delimiter);
        buffer.append("  ");   // M1900_PRIOR_ADLIADL_SELF - Prior functioning ADL/IADL: self-care
        buffer.append(delimiter);
        buffer.append("  ");   // M1900_PRIOR_ADLIADL_AMBLTN - Prior functioning ADL/IADL: ambulation
        buffer.append(delimiter);
        buffer.append("  ");   // M1900_PRIOR_ADLIADL_TRNSFR - Prior functioning ADL/IADL: transfer
        buffer.append(delimiter);
        buffer.append("  ");   // M1900_PRIOR_ADLIADL_HSEHOLD - Prior functioning ADL/IADL: household tasks
        buffer.append(delimiter);
        buffer.append("  ");   // M1910_MLT_FCTR_FALL_RISK_ASMT - Has patient had a multi-factor fall risk asmt
        buffer.append(delimiter);
        buffer.append(oasisRecord.getDRUG_RGMN_RVW());   // M2000_DRUG_RGMN_RVW - Drug regimen review
        buffer.append(delimiter);
        buffer.append(" ");   // M2002_MDCTN_FLWP - Medication follow-up
        buffer.append(delimiter);
        buffer.append("  ");   // M2004_MDCTN_INTRVTN - Medication intervention
        buffer.append(delimiter);
        buffer.append("  ");   // M2010_HIGH_RISK_DRUG_EDCTN - Patient/caregiver high risk drug education
        buffer.append(delimiter);
        buffer.append("  ");   // M2015_DRUG_EDCTN_INTRVTN - Patient/caregiver drug education intervention
         buffer.append(delimiter);
       buffer.append("  ");   // M2020_CRNT_MGMT_ORAL_MDCTN - Current: management of oral medications
        buffer.append(delimiter);
        buffer.append(oasisRecord.getCRNT_MGMT_INJCTN_MDCTN());   // M2030_CRNT_MGMT_INJCTN_MDCTN - Current: management of injectable medications
        buffer.append(delimiter);
        buffer.append("  ");   // M2040_PRIOR_MGMT_ORAL_MDCTN - Prior med mgmt: oral medications
        buffer.append(delimiter);
        buffer.append("  ");   // M2040_PRIOR_MGMT_INJCTN_MDCTN - Prior med mgmt: injectable medications
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_064 - Filler: replaces M2100_CARE_TYPE_SRC_ADL
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_065 - Filler: replaces M2100_CARE_TYPE_SRC_IADL
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_066 - Filler: replaces M2100_CARE_TYPE_SRC_MDCTN
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_067 - Filler: replaces M2100_CARE_TYPE_SRC_PRCDR
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_068 - Filler: replaces M2100_CARE_TYPE_SRC_EQUIP
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_069 - Filler: replaces M2100_CARE_TYPE_SRC_SPRVSN
        buffer.append(delimiter);
        buffer.append("  ");   // ITEM_FILLER_070 - Filler: replaces M2100_CARE_TYPE_SRC_ADVCY
        buffer.append(delimiter);
        buffer.append("  ");   // M2110_ADL_IADL_ASTNC_FREQ - How often recv non-HHA caregiver ADL/IADL assist
        buffer.append(delimiter);
        buffer.append(oasisRecord.getTHER_NEED_NBR());   // M2200_THER_NEED_NBR - Therapy need: number of visits indicated
        buffer.append(delimiter);
        buffer.append(oasisRecord.getTHER_NEED_NA());   // M2200_THER_NEED_NA - Therapy need: not applicable
        buffer.append(delimiter);
        buffer.append("  ");   // M2250_PLAN_SMRY_PTNT_SPECF - Plan of care synopsis: patient specific parameters
        buffer.append(delimiter);
        buffer.append("  ");   // M2250_PLAN_SMRY_DBTS_FT_CARE - Plan of care synopsis: diabetic foot care
        buffer.append(delimiter);
        buffer.append("  ");   // M2250_PLAN_SMRY_FALL_PRVNT - Plan of care synopsis: falls prevention
        buffer.append(delimiter);
        buffer.append("  ");   // M2250_PLAN_SMRY_DPRSN_INTRVTN - Plan of care synopsis: depression interventions
        buffer.append(delimiter);
        buffer.append("  ");   // M2250_PLAN_SMRY_PAIN_INTRVTN - Plan of care synopsis: pain interventions
        buffer.append(delimiter);
        buffer.append("  ");   // M2250_PLAN_SMRY_PRSULC_PRVNT - Plan of care synopsis: PU prevention
        buffer.append(delimiter);
        buffer.append("  ");   // M2250_PLAN_SMRY_PRSULC_TRTMT - Plan of care synopsis: PU moist treatment
        buffer.append(delimiter);
        buffer.append("  ");   // M2300_EMER_USE_AFTR_LAST_ASMT - Emergent care: use since previous OASIS
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_MEDICATION - Emergent care reason: medication
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_INJRY_BY_FALL - Emergent care reason: injury caused by fall
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_RSPRTRY_INFCTN - Emergent care reason: respiratory infection
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_RSPRTRY_OTHR - Emergent care reason: respiratory other
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_HRT_FAILR - Emergent care reason: heart failure
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_CRDC_DSRTHM - Emergent care reason: cardiac dysrhythmia
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_MI_CHST_PAIN - Emergent care reason: myocard infarct/chest pain
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_OTHR_HRT_DEASE - Emergent care reason: other heart disease
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_STROKE_TIA - Emergent care reason: stroke (CVA) or TIA
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_HYPOGLYC - Emergent care reason: hypoglycemia/hyperglycemia
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_GI_PRBLM - Emergent care: GI bleed/obstruct/constip/impact
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_DHYDRTN_MALNTR - Emergent care reason: dehydration, malnutrition
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_UTI - Emergent care reason: urinary tract infection
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_CTHTR_CMPLCTN - Emergent care reason: IV catheter infect/complic
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_WND_INFCTN_DTRORTN - Emergent care reason: wound infect/deterioration
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_UNCNTLD_PAIN - Emergent care reason: uncontrolled pain
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_MENTL_BHVRL_PRBLM - Emergent care reason: acute mental/behav problem
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_DVT_PULMNRY - Emergent care reason: deep vein thromb/pulm embol
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_OTHER - Emergent care reason: other than above
        buffer.append(delimiter);
        buffer.append(" ");   // M2310_ECR_UNKNOWN - Emergent care reason: unknown
        buffer.append(delimiter);
        buffer.append("  ");   // M2400_INTRVTN_SMRY_DBTS_FT - Intervention synopsis: diabetic foot care
        buffer.append(delimiter);
        buffer.append("  ");   // M2400_INTRVTN_SMRY_FALL_PRVNT - Intervention synopsis: falls prevention
        buffer.append(delimiter);
        buffer.append("  ");   // M2400_INTRVTN_SMRY_DPRSN - Intervention synopsis: depression intervention
        buffer.append(delimiter);
        buffer.append("  ");   // M2400_INTRVTN_SMRY_PAIN_MNTR - Intervention synopsis: monitor and mitigate pain
        buffer.append(delimiter);
        buffer.append("  ");   // M2400_INTRVTN_SMRY_PRSULC_PRVN - Intervention synopsis: prevent pressure ulcers
        buffer.append(delimiter);
        buffer.append("  ");   // M2400_INTRVTN_SMRY_PRSULC_WET - Intervention synopsis: PU moist wound treatment
        buffer.append(delimiter);
        buffer.append("  ");   // M2410_INPAT_FACILITY - Inpatient facility
        buffer.append(delimiter);
        buffer.append("  ");   // M2420_DSCHRG_DISP - Discharge disposition
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_MED - Hospitalized: medication
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_INJRY_BY_FALL - Hospitalized: injury caused by fall
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_RSPRTRY_INFCTN - Hospitalized: respiratory infection
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_RSPRTRY_OTHR - Hospitalized: other respiratory
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_HRT_FAILR - Hospitalized: heart failure
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_CRDC_DSRTHM - Hospitalized: cardiac dysrhythmia
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_MI_CHST_PAIN - Hospitalized: myocardial infarction or chest pain
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_OTHR_HRT_DEASE - Hospitalized: other heart disease
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_STROKE_TIA - Hospitalized: stroke (CVA) or TIA
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_HYPOGLYC - Hospitalized: hypoglycemia/hyperglycemia
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_GI_PRBLM - Hospitalized: GI bleed/obstruct/constip/impact
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_DHYDRTN_MALNTR - Hospitalized: dehydration, malnutrition
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_UR_TRACT - Hospitalized: urinary tract infection
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_CTHTR_CMPLCTN - Hospitalized: IV catheter infect/complic
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_WND_INFCTN - Hospitalized: wound infect/deterioration
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_PAIN - Hospitalized: uncontrolled pain
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_MENTL_BHVRL_PRBLM - Hospitalized: acute mental/behav problem
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_DVT_PULMNRY - Hospitalized: deep vein thromb/pulm embol
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_SCHLD_TRTMT - Hospitalized: scheduled treatment or procedure
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_OTHER - Hospitalized: other
        buffer.append(delimiter);
        buffer.append(" ");   // M2430_HOSP_UK - Hospitalized: UK
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_071 - Filler: replaces M2440_NH_THERAPY
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_072 - Filler: replaces M2440_NH_RESPITE
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_073 - Filler: replaces M2440_NH_HOSPICE
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_074 - Filler: replaces M2440_NH_PERMANENT
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_075 - Filler: replaces M2440_NH_UNSAFE_HOME
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_076 - Filler: replaces M2440_NH_OTHER
        buffer.append(delimiter);
        buffer.append(" ");   // ITEM_FILLER_077 - Filler: replaces M2440_NH_UNKNOWN
        buffer.append(delimiter);
        buffer.append("        ");   // M0903_LAST_HOME_VISIT - Date of last home visit
        buffer.append(delimiter);
        buffer.append("        ");   // M0906_DC_TRAN_DTH_DT - Discharge, transfer, death date
        buffer.append(delimiter);
        buffer.append("                                                                                                                                                                                                        ");   // CONTROL_ITEMS_FILLER - Control items filler
        buffer.append(delimiter);
        buffer.append("        ");   // M1011_14_DAY_INP1_ICD - Inpatient stay within last 14 days: ICD code 1
        buffer.append(delimiter);
        buffer.append("        ");   // M1011_14_DAY_INP2_ICD - Inpatient stay within last 14 days: ICD code 2
        buffer.append(delimiter);
        buffer.append("        ");   // M1011_14_DAY_INP3_ICD - Inpatient stay within last 14 days: ICD code 3
        buffer.append(delimiter);
        buffer.append("        ");   // M1011_14_DAY_INP4_ICD - Inpatient stay within last 14 days: ICD code 4
        buffer.append(delimiter);
        buffer.append("        ");   // M1011_14_DAY_INP5_ICD - Inpatient stay within last 14 days: ICD code 5
        buffer.append(delimiter);
        buffer.append("        ");   // M1011_14_DAY_INP6_ICD - Inpatient stay within last 14 days: ICD code 6
        buffer.append(delimiter);
        buffer.append(" ");   // M1011_14_DAY_INP_NA - Inpatient stay within last 14 days: not applicable
        buffer.append(delimiter);
        buffer.append("        ");   // M1017_CHGREG_ICD1 - Regimen change in past 14 days: ICD code 1
        buffer.append(delimiter);
        buffer.append("        ");   // M1017_CHGREG_ICD2 - Regimen change in past 14 days: ICD code 2
        buffer.append(delimiter);
        buffer.append("        ");   // M1017_CHGREG_ICD3 - Regimen change in past 14 days: ICD code 3
        buffer.append(delimiter);
        buffer.append("        ");   // M1017_CHGREG_ICD4 - Regimen change in past 14 days: ICD code 4
        buffer.append(delimiter);
        buffer.append("        ");   // M1017_CHGREG_ICD5 - Regimen change in past 14 days: ICD code 5
        buffer.append(delimiter);
        buffer.append("        ");   // M1017_CHGREG_ICD6 - Regimen change in past 14 days: ICD code 6
        buffer.append(delimiter);
        buffer.append(" ");   // M1017_CHGREG_ICD_NA - Regimen change in past 14 days: not applicable
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPRIMARY_DIAG_ICD(), 8));   // M1021_PRIMARY_DIAG_ICD - Primary diagnosis ICD code
        buffer.append(delimiter);
        buffer.append("  ");   // M1021_PRIMARY_DIAG_SEVERITY - Primary diagnosis severity rating
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG1_ICD(), 8));   // M1023_OTH_DIAG1_ICD - Other diagnosis 1: ICD code
        buffer.append(delimiter);
        buffer.append("  ");   // M1023_OTH_DIAG1_SEVERITY - Other diagnosis 1: severity rating
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG2_ICD(), 8));   // M1023_OTH_DIAG2_ICD - Other diagnosis 2: ICD code
        buffer.append(delimiter);
        buffer.append("  ");   // M1023_OTH_DIAG2_SEVERITY - Other diagnosis 2: severity rating
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG3_ICD(), 8));   // M1023_OTH_DIAG3_ICD - Other diagnosis 3: ICD code
        buffer.append(delimiter);
        buffer.append("  ");   // M1023_OTH_DIAG3_SEVERITY - Other diagnosis 3: severity rating
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG4_ICD(), 8));   // M1023_OTH_DIAG4_ICD - Other diagnosis 4: ICD code
        buffer.append(delimiter);
        buffer.append("  ");   // M1023_OTH_DIAG4_SEVERITY - Other diagnosis 4: severity rating
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getOTH_DIAG5_ICD(), 8));   // M1023_OTH_DIAG5_ICD - Other diagnosis 5: ICD code
        buffer.append(delimiter);
        buffer.append("  ");   // M1023_OTH_DIAG5_SEVERITY - Other diagnosis 5: severity rating
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_A3(), 8));   // M1025_OPT_DIAG_ICD_A3 - Optional diagnosis: primary, column 3
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_A4(), 8));   // M1025_OPT_DIAG_ICD_A4 - Optional diagnosis: primary, column 4
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_B3(), 8));   // M1025_OPT_DIAG_ICD_B3 - Optional diagnosis: first secondary, column 3
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_B4(), 8));   // M1025_OPT_DIAG_ICD_B4 - Optional diagnosis: first secondary, column 4
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_C3(), 8));   // M1025_OPT_DIAG_ICD_C3 - Optional diagnosis: second secondary, column 3
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_C4(), 8));   // M1025_OPT_DIAG_ICD_C4 - Optional diagnosis: second secondary, column 4
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_D3(), 8));   // M1025_OPT_DIAG_ICD_D3 - Optional diagnosis: third secondary, column 3
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_D4(), 8));   // M1025_OPT_DIAG_ICD_D4 - Optional diagnosis: third secondary, column 4
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_E3(), 8));   // M1025_OPT_DIAG_ICD_E3 - Optional diagnosis: fourth secondary, column 3
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_E4(), 8));   // M1025_OPT_DIAG_ICD_E4 - Optional diagnosis: fourth secondary, column 4
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_F3(), 8));   // M1025_OPT_DIAG_ICD_F3 - Optional diagnosis: fifth secondary, column 3
        buffer.append(delimiter);
        buffer.append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_F4(), 8));   // M1025_OPT_DIAG_ICD_F4 - Optional diagnosis: fifth secondary, column 4
        buffer.append(delimiter);
        buffer.append(" ");   // M1033_HOSP_RISK_HSTRY_FALLS - Hosp risk: 2+ falls or injury fall in past year
        buffer.append(delimiter);
        buffer.append(" ");   // M1033_HOSP_RISK_WEIGHT_LOSS - Hosp risk: unintentional weight loss
        buffer.append(delimiter);
        buffer.append(" ");   // M1033_HOSP_RISK_MLTPL_HOSPZTN - Hosp risk: 2+ hospitalizations in past 12 months
        buffer.append(delimiter);
        buffer.append(" ");   // M1033_HOSP_RISK_MLTPL_ED_VISIT - Hosp risk: 2+ emergcy dept visits in past 6 months
        buffer.append(delimiter);
        buffer.append(" ");   // M1033_HOSP_RISK_MNTL_BHV_DCLN - Hosp risk: decline mental/emotional/behav status
        buffer.append(delimiter);
        buffer.append(" ");   // M1033_HOSP_RISK_COMPLIANCE - Hosp risk: difficulty with medical instructions
        buffer.append(delimiter);
        buffer.append(" ");   // M1033_HOSP_RISK_5PLUS_MDCTN - Hosp risk: taking five or more medications
        buffer.append(delimiter);
        buffer.append(" ");   // M1033_HOSP_RISK_CRNT_EXHSTN - Hosp risk: current exhaustion
        buffer.append(delimiter);
        buffer.append(" ");   // M1033_HOSP_RISK_OTHR_RISK - Hosp risk: other risk(s) not listed
        buffer.append(delimiter);
        buffer.append(" ");   // M1033_HOSP_RISK_NONE_ABOVE - Hosp risk: none of the above
        buffer.append(delimiter);
        buffer.append(" ");   // M1041_IN_INFLNZ_SEASON - Does episode include Oct 1 thru Mar 31
        buffer.append(delimiter);
        buffer.append("  ");   // M1046_INFLNZ_RECD_CRNT_SEASON - Did patient receive influenza vaccine
        buffer.append(delimiter);
        buffer.append(" ");   // M1051_PVX_RCVD_AGNCY - Was pneumococcal vaccine received
        buffer.append(delimiter);
        buffer.append("  ");   // M1056_PVX_RSN_NOT_RCVD_AGNCY - If pneumococcal vacc not received, state reason
        buffer.append(delimiter);
        buffer.append("  ");   // M1309_NBR_NEW_WRS_PRSULC_STG2 - Number of new or worsening stage 2
        buffer.append(delimiter);
        buffer.append("  ");   // M1309_NBR_NEW_WRS_PRSULC_STG3 - Number of new or worsening stage 3
        buffer.append(delimiter);
        buffer.append("  ");   // M1309_NBR_NEW_WRS_PRSULC_STG4 - Number of new or worsening stage 4
        buffer.append(delimiter);
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_ADL - Care mgmt, types/sources: ADL
        buffer.append(delimiter);
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_IADL - Care mgmt, types/sources: IADL
        buffer.append(delimiter);
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_MDCTN - Care mgmt, types/sources: med admin
        buffer.append(delimiter);
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_PRCDR - Care mgmt, types/sources: med procs tx
        buffer.append(delimiter);
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_EQUIP - Care mgmt, types/sources: equipment
        buffer.append(delimiter);
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_SPRVSN - Care mgmt, types/sources: supervision and safety
        buffer.append(delimiter);
        buffer.append("  ");   // M2102_CARE_TYPE_SRC_ADVCY - Care mgmt, types/sources: advocacy or facilitation
        buffer.append(delimiter);
        buffer.append("  ");   // M1309_NBR_NEW_WRS_PRSULC_NSTG - Number of new or worsening unstageable
        buffer.append(delimiter);
        buffer.append("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ");   // ASMT_ITEMS_FILLER - Assessment items filler
        buffer.append(delimiter);
        buffer.append("                                                                                                                                                                                                                                                                                                                                                              ");   // LEGACY_ITEMS_FILLER - Discontinued OASIS-B1 and C items.
        buffer.append(delimiter);
        buffer.append("            ");   // C_CCN_NUM - Calculated Facility CMS Certification Number (CCN)
        buffer.append(delimiter);
        buffer.append("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ");   // CALCULATED_ITEMS_FILLER - Calculated items filler
        buffer.append(delimiter);
        buffer.append("               ");   // HHA_ASMT_INT_ID - Assessment internal ID
        buffer.append(delimiter);
        buffer.append("               ");   // ORIG_ASMT_INT_ID - Original assessment ID
        buffer.append(delimiter);
        buffer.append("          ");   // RES_INT_ID - Resident internal ID
        buffer.append(delimiter);
        buffer.append("        ");   // ASMT_EFF_DATE - Effective date
        buffer.append(delimiter);
        buffer.append("          ");   // CALC_FILLER_001 - Filler: replaces BRANCH_IDENTIFIER
        buffer.append(delimiter);
        buffer.append("          ");   // FAC_INT_ID - Facility internal ID
        buffer.append(delimiter);
        buffer.append("               ");   // SUBMISSION_ID - Submission ID
        buffer.append(delimiter);
        buffer.append("        ");   // SUBMISSION_DATE - Submission date
        buffer.append(delimiter);
        buffer.append("        ");   // SUBMISSION_COMPLETE_DATE - Submission processing completion date
        buffer.append(delimiter);
        buffer.append("                              ");   // SUBMITTING_USER_ID - Submitter user ID
        buffer.append(delimiter);
        buffer.append("  ");   // RES_MATCH_CRITERIA - Resident matching criteria
        buffer.append(delimiter);
        buffer.append("   ");   // RESIDENT_AGE - Age of resident on the effective date
        buffer.append(delimiter);
        buffer.append(" ");   // BIRTHDATE_SUBM_IND - Birth date submit code
        buffer.append(delimiter);
        buffer.append("     ");   // CALC_HIPPS_CODE - HIPPS group code: recalculated
        buffer.append(delimiter);
        buffer.append("     ");   // CALC_HIPPS_VERSION - HIPPS version code: recalculated
        buffer.append(delimiter);
        buffer.append("%");   // DATA_END_INDICATOR - End of data terminator code
        buffer.append("\r");   // CR - Carriage return (ASCII 013)
       buffer.append("\n");   // LF - Line feed character (ASCII 010)

        return buffer;
    }

}
