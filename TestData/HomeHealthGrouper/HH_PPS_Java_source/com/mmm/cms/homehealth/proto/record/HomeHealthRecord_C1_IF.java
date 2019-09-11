/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto.record;

/**
 * This interface describes an OASIS-C1 record
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public interface HomeHealthRecord_C1_IF extends HomeHealthRecordIF {

	String VALUE_SPEC_VRSN_CD = "2.11";
	String OASIS_C1_ITEM_ASMT_SYS_CD = "ASMT_SYS_CD";
	String OASIS_C1_ITEM_TRANS_TYPE_CD = "TRANS_TYPE_CD";
	String OASIS_C1_ITEM_ITM_SBST_CD = "ITM_SBST_CD";
	String OASIS_C1_ITEM_ITM_SET_VRSN_CD = "ITM_SET_VRSN_CD";
	String OASIS_C1_ITEM_SPEC_VRSN_CD = "SPEC_VRSN_CD";
	String OASIS_C1_ITEM_CORRECTION_NUM = "CORRECTION_NUM";
	String OASIS_C1_ITEM_STATE_CD = "STATE_CD";
	String OASIS_C1_ITEM_HHA_AGENCY_ID = "HHA_AGENCY_ID";
	String OASIS_C1_ITEM_NATL_PRVDR_ID = "NATL_PRVDR_ID";
	String OASIS_C1_ITEM_SFW_ID = "SFW_ID";
	String OASIS_C1_ITEM_SFW_NAME = "SFW_NAME";
	String OASIS_C1_ITEM_SFW_EMAIL_ADR = "SFW_EMAIL_ADR";
	String OASIS_C1_ITEM_SFW_PROD_NAME = "SFW_PROD_NAME";
	String OASIS_C1_ITEM_SFW_PROD_VRSN_CD = "SFW_PROD_VRSN_CD";
	String OASIS_C1_ITEM_ACY_DOC_CD = "ACY_DOC_CD";
	String OASIS_C1_ITEM_SUBM_HIPPS_CODE = "SUBM_HIPPS_CODE";
	String OASIS_C1_ITEM_SUBM_HIPPS_VERSION = "SUBM_HIPPS_VERSION";
	String OASIS_C1_ITEM_M0010_CCN = "M0010_CCN";
	String OASIS_C1_ITEM_M0014_BRANCH_STATE = "M0014_BRANCH_STATE";
	String OASIS_C1_ITEM_M0016_BRANCH_ID = "M0016_BRANCH_ID";
	String OASIS_C1_ITEM_M0018_PHYSICIAN_ID = "M0018_PHYSICIAN_ID";
	String OASIS_C1_ITEM_M0018_PHYSICIAN_UK = "M0018_PHYSICIAN_UK";
	String OASIS_C1_ITEM_M0020_PAT_ID = "M0020_PAT_ID";
	String OASIS_C1_ITEM_M0030_START_CARE_DT = "M0030_START_CARE_DT";
	String OASIS_C1_ITEM_M0032_ROC_DT = "M0032_ROC_DT";
	String OASIS_C1_ITEM_M0032_ROC_DT_NA = "M0032_ROC_DT_NA";
	String OASIS_C1_ITEM_M0040_PAT_FNAME = "M0040_PAT_FNAME";
	String OASIS_C1_ITEM_M0040_PAT_MI = "M0040_PAT_MI";
	String OASIS_C1_ITEM_M0040_PAT_LNAME = "M0040_PAT_LNAME";
	String OASIS_C1_ITEM_M0040_PAT_SUFFIX = "M0040_PAT_SUFFIX";
	String OASIS_C1_ITEM_M0050_PAT_ST = "M0050_PAT_ST";
	String OASIS_C1_ITEM_M0060_PAT_ZIP = "M0060_PAT_ZIP";
	String OASIS_C1_ITEM_M0063_MEDICARE_NUM = "M0063_MEDICARE_NUM";
	String OASIS_C1_ITEM_M0063_MEDICARE_NA = "M0063_MEDICARE_NA";
	String OASIS_C1_ITEM_M0064_SSN = "M0064_SSN";
	String OASIS_C1_ITEM_M0064_SSN_UK = "M0064_SSN_UK";
	String OASIS_C1_ITEM_M0065_MEDICAID_NUM = "M0065_MEDICAID_NUM";
	String OASIS_C1_ITEM_M0065_MEDICAID_NA = "M0065_MEDICAID_NA";
	String OASIS_C1_ITEM_M0066_PAT_BIRTH_DT = "M0066_PAT_BIRTH_DT";
	String OASIS_C1_ITEM_M0069_PAT_GENDER = "M0069_PAT_GENDER";
	String OASIS_C1_ITEM_M0140_ETHNIC_AI_AN = "M0140_ETHNIC_AI_AN";
	String OASIS_C1_ITEM_M0140_ETHNIC_ASIAN = "M0140_ETHNIC_ASIAN";
	String OASIS_C1_ITEM_M0140_ETHNIC_BLACK = "M0140_ETHNIC_BLACK";
	String OASIS_C1_ITEM_M0140_ETHNIC_HISP = "M0140_ETHNIC_HISP";
	String OASIS_C1_ITEM_M0140_ETHNIC_NH_PI = "M0140_ETHNIC_NH_PI";
	String OASIS_C1_ITEM_M0140_ETHNIC_WHITE = "M0140_ETHNIC_WHITE";
	String OASIS_C1_ITEM_M0150_CPAY_NONE = "M0150_CPAY_NONE";
	String OASIS_C1_ITEM_M0150_CPAY_MCARE_FFS = "M0150_CPAY_MCARE_FFS";
	String OASIS_C1_ITEM_M0150_CPAY_MCARE_HMO = "M0150_CPAY_MCARE_HMO";
	String OASIS_C1_ITEM_M0150_CPAY_MCAID_FFS = "M0150_CPAY_MCAID_FFS";
	String OASIS_C1_ITEM_M0150_CPAY_MCAID_HMO = "M0150_CPAY_MCAID_HMO";
	String OASIS_C1_ITEM_M0150_CPAY_WRKCOMP = "M0150_CPAY_WRKCOMP";
	String OASIS_C1_ITEM_M0150_CPAY_TITLEPGMS = "M0150_CPAY_TITLEPGMS";
	String OASIS_C1_ITEM_M0150_CPAY_OTH_GOVT = "M0150_CPAY_OTH_GOVT";
	String OASIS_C1_ITEM_M0150_CPAY_PRIV_INS = "M0150_CPAY_PRIV_INS";
	String OASIS_C1_ITEM_M0150_CPAY_PRIV_HMO = "M0150_CPAY_PRIV_HMO";
	String OASIS_C1_ITEM_M0150_CPAY_SELFPAY = "M0150_CPAY_SELFPAY";
	String OASIS_C1_ITEM_M0150_CPAY_OTHER = "M0150_CPAY_OTHER";
	String OASIS_C1_ITEM_M0150_CPAY_UK = "M0150_CPAY_UK";
	String OASIS_C1_ITEM_M0080_ASSESSOR_DISCIPLINE = "M0080_ASSESSOR_DISCIPLINE";
	String OASIS_C1_ITEM_M0090_INFO_COMPLETED_DT = "M0090_INFO_COMPLETED_DT";
	String OASIS_C1_ITEM_M0100_ASSMT_REASON = "M0100_ASSMT_REASON";
	String OASIS_C1_ITEM_M0102_PHYSN_ORDRD_SOCROC_DT = "M0102_PHYSN_ORDRD_SOCROC_DT";
	String OASIS_C1_ITEM_M0102_PHYSN_ORDRD_SOCROC_DT_NA = "M0102_PHYSN_ORDRD_SOCROC_DT_NA";
	String OASIS_C1_ITEM_M0104_PHYSN_RFRL_DT = "M0104_PHYSN_RFRL_DT";
	String OASIS_C1_ITEM_M0110_EPISODE_TIMING = "M0110_EPISODE_TIMING";
	String OASIS_C1_ITEM_M1000_DC_LTC_14_DA = "M1000_DC_LTC_14_DA";
	String OASIS_C1_ITEM_M1000_DC_SNF_14_DA = "M1000_DC_SNF_14_DA";
	String OASIS_C1_ITEM_M1000_DC_IPPS_14_DA = "M1000_DC_IPPS_14_DA";
	String OASIS_C1_ITEM_M1000_DC_LTCH_14_DA = "M1000_DC_LTCH_14_DA";
	String OASIS_C1_ITEM_M1000_DC_IRF_14_DA = "M1000_DC_IRF_14_DA";
	String OASIS_C1_ITEM_M1000_DC_PSYCH_14_DA = "M1000_DC_PSYCH_14_DA";
	String OASIS_C1_ITEM_M1000_DC_OTH_14_DA = "M1000_DC_OTH_14_DA";
	String OASIS_C1_ITEM_M1000_DC_NONE_14_DA = "M1000_DC_NONE_14_DA";
	String OASIS_C1_ITEM_M1005_INP_DISCHARGE_DT = "M1005_INP_DISCHARGE_DT";
	String OASIS_C1_ITEM_M1005_INP_DSCHG_UNKNOWN = "M1005_INP_DSCHG_UNKNOWN";
	String OASIS_C1_ITEM_ITEM_FILLER_001 = "ITEM_FILLER_001";
	String OASIS_C1_ITEM_ITEM_FILLER_002 = "ITEM_FILLER_002";
	String OASIS_C1_ITEM_ITEM_FILLER_003 = "ITEM_FILLER_003";
	String OASIS_C1_ITEM_ITEM_FILLER_004 = "ITEM_FILLER_004";
	String OASIS_C1_ITEM_ITEM_FILLER_005 = "ITEM_FILLER_005";
	String OASIS_C1_ITEM_ITEM_FILLER_006 = "ITEM_FILLER_006";
	String OASIS_C1_ITEM_ITEM_FILLER_007 = "ITEM_FILLER_007";
	String OASIS_C1_ITEM_ITEM_FILLER_008 = "ITEM_FILLER_008";
	String OASIS_C1_ITEM_ITEM_FILLER_009 = "ITEM_FILLER_009";
	String OASIS_C1_ITEM_ITEM_FILLER_010 = "ITEM_FILLER_010";
	String OASIS_C1_ITEM_ITEM_FILLER_011 = "ITEM_FILLER_011";
	String OASIS_C1_ITEM_ITEM_FILLER_012 = "ITEM_FILLER_012";
	String OASIS_C1_ITEM_ITEM_FILLER_013 = "ITEM_FILLER_013";
	String OASIS_C1_ITEM_ITEM_FILLER_014 = "ITEM_FILLER_014";
	String OASIS_C1_ITEM_ITEM_FILLER_015 = "ITEM_FILLER_015";
	String OASIS_C1_ITEM_ITEM_FILLER_016 = "ITEM_FILLER_016";
	String OASIS_C1_ITEM_ITEM_FILLER_017 = "ITEM_FILLER_017";
	String OASIS_C1_ITEM_ITEM_FILLER_018 = "ITEM_FILLER_018";
	String OASIS_C1_ITEM_ITEM_FILLER_019 = "ITEM_FILLER_019";
	String OASIS_C1_ITEM_M1018_PRIOR_UR_INCON = "M1018_PRIOR_UR_INCON";
	String OASIS_C1_ITEM_M1018_PRIOR_CATH = "M1018_PRIOR_CATH";
	String OASIS_C1_ITEM_M1018_PRIOR_INTRACT_PAIN = "M1018_PRIOR_INTRACT_PAIN";
	String OASIS_C1_ITEM_M1018_PRIOR_IMPR_DECSN = "M1018_PRIOR_IMPR_DECSN";
	String OASIS_C1_ITEM_M1018_PRIOR_DISRUPTIVE = "M1018_PRIOR_DISRUPTIVE";
	String OASIS_C1_ITEM_M1018_PRIOR_MEM_LOSS = "M1018_PRIOR_MEM_LOSS";
	String OASIS_C1_ITEM_M1018_PRIOR_NONE = "M1018_PRIOR_NONE";
	String OASIS_C1_ITEM_M1018_PRIOR_NOCHG_14D = "M1018_PRIOR_NOCHG_14D";
	String OASIS_C1_ITEM_M1018_PRIOR_UNKNOWN = "M1018_PRIOR_UNKNOWN";
	String OASIS_C1_ITEM_ITEM_FILLER_020 = "ITEM_FILLER_020";
	String OASIS_C1_ITEM_ITEM_FILLER_021 = "ITEM_FILLER_021";
	String OASIS_C1_ITEM_ITEM_FILLER_022 = "ITEM_FILLER_022";
	String OASIS_C1_ITEM_ITEM_FILLER_023 = "ITEM_FILLER_023";
	String OASIS_C1_ITEM_ITEM_FILLER_024 = "ITEM_FILLER_024";
	String OASIS_C1_ITEM_ITEM_FILLER_025 = "ITEM_FILLER_025";
	String OASIS_C1_ITEM_ITEM_FILLER_026 = "ITEM_FILLER_026";
	String OASIS_C1_ITEM_ITEM_FILLER_027 = "ITEM_FILLER_027";
	String OASIS_C1_ITEM_ITEM_FILLER_028 = "ITEM_FILLER_028";
	String OASIS_C1_ITEM_ITEM_FILLER_029 = "ITEM_FILLER_029";
	String OASIS_C1_ITEM_ITEM_FILLER_030 = "ITEM_FILLER_030";
	String OASIS_C1_ITEM_ITEM_FILLER_031 = "ITEM_FILLER_031";
	String OASIS_C1_ITEM_ITEM_FILLER_032 = "ITEM_FILLER_032";
	String OASIS_C1_ITEM_ITEM_FILLER_033 = "ITEM_FILLER_033";
	String OASIS_C1_ITEM_ITEM_FILLER_034 = "ITEM_FILLER_034";
	String OASIS_C1_ITEM_ITEM_FILLER_035 = "ITEM_FILLER_035";
	String OASIS_C1_ITEM_ITEM_FILLER_036 = "ITEM_FILLER_036";
	String OASIS_C1_ITEM_ITEM_FILLER_037 = "ITEM_FILLER_037";
	String OASIS_C1_ITEM_ITEM_FILLER_038 = "ITEM_FILLER_038";
	String OASIS_C1_ITEM_ITEM_FILLER_039 = "ITEM_FILLER_039";
	String OASIS_C1_ITEM_ITEM_FILLER_040 = "ITEM_FILLER_040";
	String OASIS_C1_ITEM_ITEM_FILLER_041 = "ITEM_FILLER_041";
	String OASIS_C1_ITEM_ITEM_FILLER_042 = "ITEM_FILLER_042";
	String OASIS_C1_ITEM_ITEM_FILLER_043 = "ITEM_FILLER_043";
	String OASIS_C1_ITEM_M1030_THH_IV_INFUSION = "M1030_THH_IV_INFUSION";
	String OASIS_C1_ITEM_M1030_THH_PAR_NUTRITION = "M1030_THH_PAR_NUTRITION";
	String OASIS_C1_ITEM_M1030_THH_ENT_NUTRITION = "M1030_THH_ENT_NUTRITION";
	String OASIS_C1_ITEM_M1030_THH_NONE_ABOVE = "M1030_THH_NONE_ABOVE";
	String OASIS_C1_ITEM_ITEM_FILLER_044 = "ITEM_FILLER_044";
	String OASIS_C1_ITEM_ITEM_FILLER_045 = "ITEM_FILLER_045";
	String OASIS_C1_ITEM_ITEM_FILLER_046 = "ITEM_FILLER_046";
	String OASIS_C1_ITEM_ITEM_FILLER_047 = "ITEM_FILLER_047";
	String OASIS_C1_ITEM_ITEM_FILLER_048 = "ITEM_FILLER_048";
	String OASIS_C1_ITEM_ITEM_FILLER_049 = "ITEM_FILLER_049";
	String OASIS_C1_ITEM_ITEM_FILLER_050 = "ITEM_FILLER_050";
	String OASIS_C1_ITEM_M1034_PTNT_OVRAL_STUS = "M1034_PTNT_OVRAL_STUS";
	String OASIS_C1_ITEM_M1036_RSK_SMOKING = "M1036_RSK_SMOKING";
	String OASIS_C1_ITEM_M1036_RSK_OBESITY = "M1036_RSK_OBESITY";
	String OASIS_C1_ITEM_M1036_RSK_ALCOHOLISM = "M1036_RSK_ALCOHOLISM";
	String OASIS_C1_ITEM_M1036_RSK_DRUGS = "M1036_RSK_DRUGS";
	String OASIS_C1_ITEM_M1036_RSK_NONE = "M1036_RSK_NONE";
	String OASIS_C1_ITEM_M1036_RSK_UNKNOWN = "M1036_RSK_UNKNOWN";
	String OASIS_C1_ITEM_ITEM_FILLER_051 = "ITEM_FILLER_051";
	String OASIS_C1_ITEM_ITEM_FILLER_052 = "ITEM_FILLER_052";
	String OASIS_C1_ITEM_ITEM_FILLER_053 = "ITEM_FILLER_053";
	String OASIS_C1_ITEM_ITEM_FILLER_054 = "ITEM_FILLER_054";
	String OASIS_C1_ITEM_M1100_PTNT_LVG_STUTN = "M1100_PTNT_LVG_STUTN";
	String OASIS_C1_ITEM_M1200_VISION = "M1200_VISION";
	String OASIS_C1_ITEM_M1210_HEARG_ABLTY = "M1210_HEARG_ABLTY";
	String OASIS_C1_ITEM_M1220_UNDRSTG_VERBAL_CNTNT = "M1220_UNDRSTG_VERBAL_CNTNT";
	String OASIS_C1_ITEM_M1230_SPEECH = "M1230_SPEECH";
	String OASIS_C1_ITEM_M1240_FRML_PAIN_ASMT = "M1240_FRML_PAIN_ASMT";
	String OASIS_C1_ITEM_M1242_PAIN_FREQ_ACTVTY_MVMT = "M1242_PAIN_FREQ_ACTVTY_MVMT";
	String OASIS_C1_ITEM_M1300_PRSR_ULCR_RISK_ASMT = "M1300_PRSR_ULCR_RISK_ASMT";
	String OASIS_C1_ITEM_M1302_RISK_OF_PRSR_ULCR = "M1302_RISK_OF_PRSR_ULCR";
	String OASIS_C1_ITEM_M1306_UNHLD_STG2_PRSR_ULCR = "M1306_UNHLD_STG2_PRSR_ULCR";
	String OASIS_C1_ITEM_M1307_OLDST_STG2_AT_DSCHRG = "M1307_OLDST_STG2_AT_DSCHRG";
	String OASIS_C1_ITEM_M1307_OLDST_STG2_ONST_DT = "M1307_OLDST_STG2_ONST_DT";
	String OASIS_C1_ITEM_M1308_NBR_PRSULC_STG2 = "M1308_NBR_PRSULC_STG2";
	String OASIS_C1_ITEM_ITEM_FILLER_055 = "ITEM_FILLER_055";
	String OASIS_C1_ITEM_M1308_NBR_PRSULC_STG3 = "M1308_NBR_PRSULC_STG3";
	String OASIS_C1_ITEM_ITEM_FILLER_056 = "ITEM_FILLER_056";
	String OASIS_C1_ITEM_M1308_NBR_PRSULC_STG4 = "M1308_NBR_PRSULC_STG4";
	String OASIS_C1_ITEM_ITEM_FILLER_057 = "ITEM_FILLER_057";
	String OASIS_C1_ITEM_M1308_NSTG_DRSG = "M1308_NSTG_DRSG";
	String OASIS_C1_ITEM_ITEM_FILLER_058 = "ITEM_FILLER_058";
	String OASIS_C1_ITEM_M1308_NSTG_CVRG = "M1308_NSTG_CVRG";
	String OASIS_C1_ITEM_ITEM_FILLER_059 = "ITEM_FILLER_059";
	String OASIS_C1_ITEM_M1308_NSTG_DEEP_TISUE = "M1308_NSTG_DEEP_TISUE";
	String OASIS_C1_ITEM_ITEM_FILLER_060 = "ITEM_FILLER_060";
	String OASIS_C1_ITEM_ITEM_FILLER_061 = "ITEM_FILLER_061";
	String OASIS_C1_ITEM_ITEM_FILLER_062 = "ITEM_FILLER_062";
	String OASIS_C1_ITEM_ITEM_FILLER_063 = "ITEM_FILLER_063";
	String OASIS_C1_ITEM_M1320_STUS_PRBLM_PRSR_ULCR = "M1320_STUS_PRBLM_PRSR_ULCR";
	String OASIS_C1_ITEM_M1322_NBR_PRSULC_STG1 = "M1322_NBR_PRSULC_STG1";
	String OASIS_C1_ITEM_M1324_STG_PRBLM_ULCER = "M1324_STG_PRBLM_ULCER";
	String OASIS_C1_ITEM_M1330_STAS_ULCR_PRSNT = "M1330_STAS_ULCR_PRSNT";
	String OASIS_C1_ITEM_M1332_NBR_STAS_ULCR = "M1332_NBR_STAS_ULCR";
	String OASIS_C1_ITEM_M1334_STUS_PRBLM_STAS_ULCR = "M1334_STUS_PRBLM_STAS_ULCR";
	String OASIS_C1_ITEM_M1340_SRGCL_WND_PRSNT = "M1340_SRGCL_WND_PRSNT";
	String OASIS_C1_ITEM_M1342_STUS_PRBLM_SRGCL_WND = "M1342_STUS_PRBLM_SRGCL_WND";
	String OASIS_C1_ITEM_M1350_LESION_OPEN_WND = "M1350_LESION_OPEN_WND";
	String OASIS_C1_ITEM_M1400_WHEN_DYSPNEIC = "M1400_WHEN_DYSPNEIC";
	String OASIS_C1_ITEM_M1410_RESPTX_OXYGEN = "M1410_RESPTX_OXYGEN";
	String OASIS_C1_ITEM_M1410_RESPTX_VENTILATOR = "M1410_RESPTX_VENTILATOR";
	String OASIS_C1_ITEM_M1410_RESPTX_AIRPRESS = "M1410_RESPTX_AIRPRESS";
	String OASIS_C1_ITEM_M1410_RESPTX_NONE = "M1410_RESPTX_NONE";
	String OASIS_C1_ITEM_M1500_SYMTM_HRT_FAILR_PTNTS = "M1500_SYMTM_HRT_FAILR_PTNTS";
	String OASIS_C1_ITEM_M1510_HRT_FAILR_NO_ACTN = "M1510_HRT_FAILR_NO_ACTN";
	String OASIS_C1_ITEM_M1510_HRT_FAILR_PHYSN_CNTCT = "M1510_HRT_FAILR_PHYSN_CNTCT";
	String OASIS_C1_ITEM_M1510_HRT_FAILR_ER_TRTMT = "M1510_HRT_FAILR_ER_TRTMT";
	String OASIS_C1_ITEM_M1510_HRT_FAILR_PHYSN_TRTMT = "M1510_HRT_FAILR_PHYSN_TRTMT";
	String OASIS_C1_ITEM_M1510_HRT_FAILR_CLNCL_INTRVTN = "M1510_HRT_FAILR_CLNCL_INTRVTN";
	String OASIS_C1_ITEM_M1510_HRT_FAILR_CARE_PLAN_CHG = "M1510_HRT_FAILR_CARE_PLAN_CHG";
	String OASIS_C1_ITEM_M1600_UTI = "M1600_UTI";
	String OASIS_C1_ITEM_M1610_UR_INCONT = "M1610_UR_INCONT";
	String OASIS_C1_ITEM_M1615_INCNTNT_TIMING = "M1615_INCNTNT_TIMING";
	String OASIS_C1_ITEM_M1620_BWL_INCONT = "M1620_BWL_INCONT";
	String OASIS_C1_ITEM_M1630_OSTOMY = "M1630_OSTOMY";
	String OASIS_C1_ITEM_M1700_COG_FUNCTION = "M1700_COG_FUNCTION";
	String OASIS_C1_ITEM_M1710_WHEN_CONFUSED = "M1710_WHEN_CONFUSED";
	String OASIS_C1_ITEM_M1720_WHEN_ANXIOUS = "M1720_WHEN_ANXIOUS";
	String OASIS_C1_ITEM_M1730_STDZ_DPRSN_SCRNG = "M1730_STDZ_DPRSN_SCRNG";
	String OASIS_C1_ITEM_M1730_PHQ2_LACK_INTRST = "M1730_PHQ2_LACK_INTRST";
	String OASIS_C1_ITEM_M1730_PHQ2_DPRSN = "M1730_PHQ2_DPRSN";
	String OASIS_C1_ITEM_M1740_BD_MEM_DEFICIT = "M1740_BD_MEM_DEFICIT";
	String OASIS_C1_ITEM_M1740_BD_IMP_DECISN = "M1740_BD_IMP_DECISN";
	String OASIS_C1_ITEM_M1740_BD_VERBAL = "M1740_BD_VERBAL";
	String OASIS_C1_ITEM_M1740_BD_PHYSICAL = "M1740_BD_PHYSICAL";
	String OASIS_C1_ITEM_M1740_BD_SOC_INAPPRO = "M1740_BD_SOC_INAPPRO";
	String OASIS_C1_ITEM_M1740_BD_DELUSIONS = "M1740_BD_DELUSIONS";
	String OASIS_C1_ITEM_M1740_BD_NONE = "M1740_BD_NONE";
	String OASIS_C1_ITEM_M1745_BEH_PROB_FREQ = "M1745_BEH_PROB_FREQ";
	String OASIS_C1_ITEM_M1750_REC_PSYCH_NURS = "M1750_REC_PSYCH_NURS";
	String OASIS_C1_ITEM_M1800_CRNT_GROOMING = "M1800_CRNT_GROOMING";
	String OASIS_C1_ITEM_M1810_CRNT_DRESS_UPPER = "M1810_CRNT_DRESS_UPPER";
	String OASIS_C1_ITEM_M1820_CRNT_DRESS_LOWER = "M1820_CRNT_DRESS_LOWER";
	String OASIS_C1_ITEM_M1830_CRNT_BATHG = "M1830_CRNT_BATHG";
	String OASIS_C1_ITEM_M1840_CRNT_TOILTG = "M1840_CRNT_TOILTG";
	String OASIS_C1_ITEM_M1845_CRNT_TOILTG_HYGN = "M1845_CRNT_TOILTG_HYGN";
	String OASIS_C1_ITEM_M1850_CRNT_TRNSFRNG = "M1850_CRNT_TRNSFRNG";
	String OASIS_C1_ITEM_M1860_CRNT_AMBLTN = "M1860_CRNT_AMBLTN";
	String OASIS_C1_ITEM_M1870_CRNT_FEEDING = "M1870_CRNT_FEEDING";
	String OASIS_C1_ITEM_M1880_CRNT_PREP_LT_MEALS = "M1880_CRNT_PREP_LT_MEALS";
	String OASIS_C1_ITEM_M1890_CRNT_PHONE_USE = "M1890_CRNT_PHONE_USE";
	String OASIS_C1_ITEM_M1900_PRIOR_ADLIADL_SELF = "M1900_PRIOR_ADLIADL_SELF";
	String OASIS_C1_ITEM_M1900_PRIOR_ADLIADL_AMBLTN = "M1900_PRIOR_ADLIADL_AMBLTN";
	String OASIS_C1_ITEM_M1900_PRIOR_ADLIADL_TRNSFR = "M1900_PRIOR_ADLIADL_TRNSFR";
	String OASIS_C1_ITEM_M1900_PRIOR_ADLIADL_HSEHOLD = "M1900_PRIOR_ADLIADL_HSEHOLD";
	String OASIS_C1_ITEM_M1910_MLT_FCTR_FALL_RISK_ASMT = "M1910_MLT_FCTR_FALL_RISK_ASMT";
	String OASIS_C1_ITEM_M2000_DRUG_RGMN_RVW = "M2000_DRUG_RGMN_RVW";
	String OASIS_C1_ITEM_M2002_MDCTN_FLWP = "M2002_MDCTN_FLWP";
	String OASIS_C1_ITEM_M2004_MDCTN_INTRVTN = "M2004_MDCTN_INTRVTN";
	String OASIS_C1_ITEM_M2010_HIGH_RISK_DRUG_EDCTN = "M2010_HIGH_RISK_DRUG_EDCTN";
	String OASIS_C1_ITEM_M2015_DRUG_EDCTN_INTRVTN = "M2015_DRUG_EDCTN_INTRVTN";
	String OASIS_C1_ITEM_M2020_CRNT_MGMT_ORAL_MDCTN = "M2020_CRNT_MGMT_ORAL_MDCTN";
	String OASIS_C1_ITEM_M2030_CRNT_MGMT_INJCTN_MDCTN = "M2030_CRNT_MGMT_INJCTN_MDCTN";
	String OASIS_C1_ITEM_M2040_PRIOR_MGMT_ORAL_MDCTN = "M2040_PRIOR_MGMT_ORAL_MDCTN";
	String OASIS_C1_ITEM_M2040_PRIOR_MGMT_INJCTN_MDCTN = "M2040_PRIOR_MGMT_INJCTN_MDCTN";
	String OASIS_C1_ITEM_ITEM_FILLER_064 = "ITEM_FILLER_064";
	String OASIS_C1_ITEM_ITEM_FILLER_065 = "ITEM_FILLER_065";
	String OASIS_C1_ITEM_ITEM_FILLER_066 = "ITEM_FILLER_066";
	String OASIS_C1_ITEM_ITEM_FILLER_067 = "ITEM_FILLER_067";
	String OASIS_C1_ITEM_ITEM_FILLER_068 = "ITEM_FILLER_068";
	String OASIS_C1_ITEM_ITEM_FILLER_069 = "ITEM_FILLER_069";
	String OASIS_C1_ITEM_ITEM_FILLER_070 = "ITEM_FILLER_070";
	String OASIS_C1_ITEM_M2110_ADL_IADL_ASTNC_FREQ = "M2110_ADL_IADL_ASTNC_FREQ";
	String OASIS_C1_ITEM_M2200_THER_NEED_NBR = "M2200_THER_NEED_NBR";
	String OASIS_C1_ITEM_M2200_THER_NEED_NA = "M2200_THER_NEED_NA";
	String OASIS_C1_ITEM_M2250_PLAN_SMRY_PTNT_SPECF = "M2250_PLAN_SMRY_PTNT_SPECF";
	String OASIS_C1_ITEM_M2250_PLAN_SMRY_DBTS_FT_CARE = "M2250_PLAN_SMRY_DBTS_FT_CARE";
	String OASIS_C1_ITEM_M2250_PLAN_SMRY_FALL_PRVNT = "M2250_PLAN_SMRY_FALL_PRVNT";
	String OASIS_C1_ITEM_M2250_PLAN_SMRY_DPRSN_INTRVTN = "M2250_PLAN_SMRY_DPRSN_INTRVTN";
	String OASIS_C1_ITEM_M2250_PLAN_SMRY_PAIN_INTRVTN = "M2250_PLAN_SMRY_PAIN_INTRVTN";
	String OASIS_C1_ITEM_M2250_PLAN_SMRY_PRSULC_PRVNT = "M2250_PLAN_SMRY_PRSULC_PRVNT";
	String OASIS_C1_ITEM_M2250_PLAN_SMRY_PRSULC_TRTMT = "M2250_PLAN_SMRY_PRSULC_TRTMT";
	String OASIS_C1_ITEM_M2300_EMER_USE_AFTR_LAST_ASMT = "M2300_EMER_USE_AFTR_LAST_ASMT";
	String OASIS_C1_ITEM_M2310_ECR_MEDICATION = "M2310_ECR_MEDICATION";
	String OASIS_C1_ITEM_M2310_ECR_INJRY_BY_FALL = "M2310_ECR_INJRY_BY_FALL";
	String OASIS_C1_ITEM_M2310_ECR_RSPRTRY_INFCTN = "M2310_ECR_RSPRTRY_INFCTN";
	String OASIS_C1_ITEM_M2310_ECR_RSPRTRY_OTHR = "M2310_ECR_RSPRTRY_OTHR";
	String OASIS_C1_ITEM_M2310_ECR_HRT_FAILR = "M2310_ECR_HRT_FAILR";
	String OASIS_C1_ITEM_M2310_ECR_CRDC_DSRTHM = "M2310_ECR_CRDC_DSRTHM";
	String OASIS_C1_ITEM_M2310_ECR_MI_CHST_PAIN = "M2310_ECR_MI_CHST_PAIN";
	String OASIS_C1_ITEM_M2310_ECR_OTHR_HRT_DEASE = "M2310_ECR_OTHR_HRT_DEASE";
	String OASIS_C1_ITEM_M2310_ECR_STROKE_TIA = "M2310_ECR_STROKE_TIA";
	String OASIS_C1_ITEM_M2310_ECR_HYPOGLYC = "M2310_ECR_HYPOGLYC";
	String OASIS_C1_ITEM_M2310_ECR_GI_PRBLM = "M2310_ECR_GI_PRBLM";
	String OASIS_C1_ITEM_M2310_ECR_DHYDRTN_MALNTR = "M2310_ECR_DHYDRTN_MALNTR";
	String OASIS_C1_ITEM_M2310_ECR_UTI = "M2310_ECR_UTI";
	String OASIS_C1_ITEM_M2310_ECR_CTHTR_CMPLCTN = "M2310_ECR_CTHTR_CMPLCTN";
	String OASIS_C1_ITEM_M2310_ECR_WND_INFCTN_DTRORTN = "M2310_ECR_WND_INFCTN_DTRORTN";
	String OASIS_C1_ITEM_M2310_ECR_UNCNTLD_PAIN = "M2310_ECR_UNCNTLD_PAIN";
	String OASIS_C1_ITEM_M2310_ECR_MENTL_BHVRL_PRBLM = "M2310_ECR_MENTL_BHVRL_PRBLM";
	String OASIS_C1_ITEM_M2310_ECR_DVT_PULMNRY = "M2310_ECR_DVT_PULMNRY";
	String OASIS_C1_ITEM_M2310_ECR_OTHER = "M2310_ECR_OTHER";
	String OASIS_C1_ITEM_M2310_ECR_UNKNOWN = "M2310_ECR_UNKNOWN";
	String OASIS_C1_ITEM_M2400_INTRVTN_SMRY_DBTS_FT = "M2400_INTRVTN_SMRY_DBTS_FT";
	String OASIS_C1_ITEM_M2400_INTRVTN_SMRY_FALL_PRVNT = "M2400_INTRVTN_SMRY_FALL_PRVNT";
	String OASIS_C1_ITEM_M2400_INTRVTN_SMRY_DPRSN = "M2400_INTRVTN_SMRY_DPRSN";
	String OASIS_C1_ITEM_M2400_INTRVTN_SMRY_PAIN_MNTR = "M2400_INTRVTN_SMRY_PAIN_MNTR";
	String OASIS_C1_ITEM_M2400_INTRVTN_SMRY_PRSULC_PRVN = "M2400_INTRVTN_SMRY_PRSULC_PRVN";
	String OASIS_C1_ITEM_M2400_INTRVTN_SMRY_PRSULC_WET = "M2400_INTRVTN_SMRY_PRSULC_WET";
	String OASIS_C1_ITEM_M2410_INPAT_FACILITY = "M2410_INPAT_FACILITY";
	String OASIS_C1_ITEM_M2420_DSCHRG_DISP = "M2420_DSCHRG_DISP";
	String OASIS_C1_ITEM_M2430_HOSP_MED = "M2430_HOSP_MED";
	String OASIS_C1_ITEM_M2430_HOSP_INJRY_BY_FALL = "M2430_HOSP_INJRY_BY_FALL";
	String OASIS_C1_ITEM_M2430_HOSP_RSPRTRY_INFCTN = "M2430_HOSP_RSPRTRY_INFCTN";
	String OASIS_C1_ITEM_M2430_HOSP_RSPRTRY_OTHR = "M2430_HOSP_RSPRTRY_OTHR";
	String OASIS_C1_ITEM_M2430_HOSP_HRT_FAILR = "M2430_HOSP_HRT_FAILR";
	String OASIS_C1_ITEM_M2430_HOSP_CRDC_DSRTHM = "M2430_HOSP_CRDC_DSRTHM";
	String OASIS_C1_ITEM_M2430_HOSP_MI_CHST_PAIN = "M2430_HOSP_MI_CHST_PAIN";
	String OASIS_C1_ITEM_M2430_HOSP_OTHR_HRT_DEASE = "M2430_HOSP_OTHR_HRT_DEASE";
	String OASIS_C1_ITEM_M2430_HOSP_STROKE_TIA = "M2430_HOSP_STROKE_TIA";
	String OASIS_C1_ITEM_M2430_HOSP_HYPOGLYC = "M2430_HOSP_HYPOGLYC";
	String OASIS_C1_ITEM_M2430_HOSP_GI_PRBLM = "M2430_HOSP_GI_PRBLM";
	String OASIS_C1_ITEM_M2430_HOSP_DHYDRTN_MALNTR = "M2430_HOSP_DHYDRTN_MALNTR";
	String OASIS_C1_ITEM_M2430_HOSP_UR_TRACT = "M2430_HOSP_UR_TRACT";
	String OASIS_C1_ITEM_M2430_HOSP_CTHTR_CMPLCTN = "M2430_HOSP_CTHTR_CMPLCTN";
	String OASIS_C1_ITEM_M2430_HOSP_WND_INFCTN = "M2430_HOSP_WND_INFCTN";
	String OASIS_C1_ITEM_M2430_HOSP_PAIN = "M2430_HOSP_PAIN";
	String OASIS_C1_ITEM_M2430_HOSP_MENTL_BHVRL_PRBLM = "M2430_HOSP_MENTL_BHVRL_PRBLM";
	String OASIS_C1_ITEM_M2430_HOSP_DVT_PULMNRY = "M2430_HOSP_DVT_PULMNRY";
	String OASIS_C1_ITEM_M2430_HOSP_SCHLD_TRTMT = "M2430_HOSP_SCHLD_TRTMT";
	String OASIS_C1_ITEM_M2430_HOSP_OTHER = "M2430_HOSP_OTHER";
	String OASIS_C1_ITEM_M2430_HOSP_UK = "M2430_HOSP_UK";
	String OASIS_C1_ITEM_ITEM_FILLER_071 = "ITEM_FILLER_071";
	String OASIS_C1_ITEM_ITEM_FILLER_072 = "ITEM_FILLER_072";
	String OASIS_C1_ITEM_ITEM_FILLER_073 = "ITEM_FILLER_073";
	String OASIS_C1_ITEM_ITEM_FILLER_074 = "ITEM_FILLER_074";
	String OASIS_C1_ITEM_ITEM_FILLER_075 = "ITEM_FILLER_075";
	String OASIS_C1_ITEM_ITEM_FILLER_076 = "ITEM_FILLER_076";
	String OASIS_C1_ITEM_ITEM_FILLER_077 = "ITEM_FILLER_077";
	String OASIS_C1_ITEM_M0903_LAST_HOME_VISIT = "M0903_LAST_HOME_VISIT";
	String OASIS_C1_ITEM_M0906_DC_TRAN_DTH_DT = "M0906_DC_TRAN_DTH_DT";
	String OASIS_C1_ITEM_CONTROL_ITEMS_FILLER = "CONTROL_ITEMS_FILLER";
	String OASIS_C1_ITEM_M1011_14_DAY_INP1_ICD = "M1011_14_DAY_INP1_ICD";
	String OASIS_C1_ITEM_M1011_14_DAY_INP2_ICD = "M1011_14_DAY_INP2_ICD";
	String OASIS_C1_ITEM_M1011_14_DAY_INP3_ICD = "M1011_14_DAY_INP3_ICD";
	String OASIS_C1_ITEM_M1011_14_DAY_INP4_ICD = "M1011_14_DAY_INP4_ICD";
	String OASIS_C1_ITEM_M1011_14_DAY_INP5_ICD = "M1011_14_DAY_INP5_ICD";
	String OASIS_C1_ITEM_M1011_14_DAY_INP6_ICD = "M1011_14_DAY_INP6_ICD";
	String OASIS_C1_ITEM_M1011_14_DAY_INP_NA = "M1011_14_DAY_INP_NA";
	String OASIS_C1_ITEM_M1017_CHGREG_ICD1 = "M1017_CHGREG_ICD1";
	String OASIS_C1_ITEM_M1017_CHGREG_ICD2 = "M1017_CHGREG_ICD2";
	String OASIS_C1_ITEM_M1017_CHGREG_ICD3 = "M1017_CHGREG_ICD3";
	String OASIS_C1_ITEM_M1017_CHGREG_ICD4 = "M1017_CHGREG_ICD4";
	String OASIS_C1_ITEM_M1017_CHGREG_ICD5 = "M1017_CHGREG_ICD5";
	String OASIS_C1_ITEM_M1017_CHGREG_ICD6 = "M1017_CHGREG_ICD6";
	String OASIS_C1_ITEM_M1017_CHGREG_ICD_NA = "M1017_CHGREG_ICD_NA";
	String OASIS_C1_ITEM_M1020_PRIMARY_DIAG_ICD = "M1020_PRIMARY_DIAG_ICD";
	String OASIS_C1_ITEM_M1020_PRIMARY_DIAG_SEVERITY = "M1020_PRIMARY_DIAG_SEVERITY";
	String OASIS_C1_ITEM_M1022_OTH_DIAG1_ICD = "M1022_OTH_DIAG1_ICD";
	String OASIS_C1_ITEM_M1022_OTH_DIAG1_SEVERITY = "M1022_OTH_DIAG1_SEVERITY";
	String OASIS_C1_ITEM_M1022_OTH_DIAG2_ICD = "M1022_OTH_DIAG2_ICD";
	String OASIS_C1_ITEM_M1022_OTH_DIAG2_SEVERITY = "M1022_OTH_DIAG2_SEVERITY";
	String OASIS_C1_ITEM_M1022_OTH_DIAG3_ICD = "M1022_OTH_DIAG3_ICD";
	String OASIS_C1_ITEM_M1022_OTH_DIAG3_SEVERITY = "M1022_OTH_DIAG3_SEVERITY";
	String OASIS_C1_ITEM_M1022_OTH_DIAG4_ICD = "M1022_OTH_DIAG4_ICD";
	String OASIS_C1_ITEM_M1022_OTH_DIAG4_SEVERITY = "M1022_OTH_DIAG4_SEVERITY";
	String OASIS_C1_ITEM_M1022_OTH_DIAG5_ICD = "M1022_OTH_DIAG5_ICD";
	String OASIS_C1_ITEM_M1022_OTH_DIAG5_SEVERITY = "M1022_OTH_DIAG5_SEVERITY";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_A3 = "M1024_PMT_DIAG_ICD_A3";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_A4 = "M1024_PMT_DIAG_ICD_A4";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_B3 = "M1024_PMT_DIAG_ICD_B3";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_B4 = "M1024_PMT_DIAG_ICD_B4";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_C3 = "M1024_PMT_DIAG_ICD_C3";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_C4 = "M1024_PMT_DIAG_ICD_C4";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_D3 = "M1024_PMT_DIAG_ICD_D3";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_D4 = "M1024_PMT_DIAG_ICD_D4";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_E3 = "M1024_PMT_DIAG_ICD_E3";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_E4 = "M1024_PMT_DIAG_ICD_E4";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_F3 = "M1024_PMT_DIAG_ICD_F3";
	String OASIS_C1_ITEM_M1024_PMT_DIAG_ICD_F4 = "M1024_PMT_DIAG_ICD_F4";
	String OASIS_C1_ITEM_M1033_HOSP_RISK_HSTRY_FALLS = "M1033_HOSP_RISK_HSTRY_FALLS";
	String OASIS_C1_ITEM_M1033_HOSP_RISK_WEIGHT_LOSS = "M1033_HOSP_RISK_WEIGHT_LOSS";
	String OASIS_C1_ITEM_M1033_HOSP_RISK_MLTPL_HOSPZTN = "M1033_HOSP_RISK_MLTPL_HOSPZTN";
	String OASIS_C1_ITEM_M1033_HOSP_RISK_MLTPL_ED_VISIT = "M1033_HOSP_RISK_MLTPL_ED_VISIT";
	String OASIS_C1_ITEM_M1033_HOSP_RISK_MNTL_BHV_DCLN = "M1033_HOSP_RISK_MNTL_BHV_DCLN";
	String OASIS_C1_ITEM_M1033_HOSP_RISK_COMPLIANCE = "M1033_HOSP_RISK_COMPLIANCE";
	String OASIS_C1_ITEM_M1033_HOSP_RISK_6PLUS_MDCTN = "M1033_HOSP_RISK_6PLUS_MDCTN";
	String OASIS_C1_ITEM_M1033_HOSP_RISK_CURR_EXHSTN = "M1033_HOSP_RISK_CURR_EXHSTN";
	String OASIS_C1_ITEM_M1033_HOSP_RISK_OTHR = "M1033_HOSP_RISK_OTHR";
	String OASIS_C1_ITEM_M1033_HOSP_RISK_NONE_ABOVE = "M1033_HOSP_RISK_NONE_ABOVE";
	String OASIS_C1_ITEM_M1041_IN_INFLNZ_SEASON = "M1041_IN_INFLNZ_SEASON";
	String OASIS_C1_ITEM_M1046_INFLNZ_RECD_CUR_SEASON = "M1046_INFLNZ_RECD_CUR_SEASON";
	String OASIS_C1_ITEM_M1051_PPV_RCVD_AGNCY = "M1051_PPV_RCVD_AGNCY";
	String OASIS_C1_ITEM_M1056_PPV_RSN_NOT_RCVD_AGNCY = "M1056_PPV_RSN_NOT_RCVD_AGNCY";
	String OASIS_C1_ITEM_M1309_NBR_NEW_WRS_PRSULC_STG2 = "M1309_NBR_NEW_WRS_PRSULC_STG2";
	String OASIS_C1_ITEM_M1309_NBR_NEW_WRS_PRSULC_STG3 = "M1309_NBR_NEW_WRS_PRSULC_STG3";
	String OASIS_C1_ITEM_M1309_NBR_NEW_WRS_PRSULC_STG4 = "M1309_NBR_NEW_WRS_PRSULC_STG4";
	String OASIS_C1_ITEM_M2102_CARE_TYPE_SRC_ADL = "M2102_CARE_TYPE_SRC_ADL";
	String OASIS_C1_ITEM_M2102_CARE_TYPE_SRC_IADL = "M2102_CARE_TYPE_SRC_IADL";
	String OASIS_C1_ITEM_M2102_CARE_TYPE_SRC_MDCTN = "M2102_CARE_TYPE_SRC_MDCTN";
	String OASIS_C1_ITEM_M2102_CARE_TYPE_SRC_PRCDR = "M2102_CARE_TYPE_SRC_PRCDR";
	String OASIS_C1_ITEM_M2102_CARE_TYPE_SRC_EQUIP = "M2102_CARE_TYPE_SRC_EQUIP";
	String OASIS_C1_ITEM_M2102_CARE_TYPE_SRC_SPRVSN = "M2102_CARE_TYPE_SRC_SPRVSN";
	String OASIS_C1_ITEM_M2102_CARE_TYPE_SRC_ADVCY = "M2102_CARE_TYPE_SRC_ADVCY";
	String OASIS_C1_ITEM_ASMT_ITEMS_FILLER = "ASMT_ITEMS_FILLER";
	String OASIS_C1_ITEM_LEGACY_ITEMS_FILLER = "LEGACY_ITEMS_FILLER";
	String OASIS_C1_ITEM_CALCULATED_ITEMS_FILLER = "CALCULATED_ITEMS_FILLER";
	String OASIS_C1_ITEM_HHA_ASMT_INT_ID = "HHA_ASMT_INT_ID";
	String OASIS_C1_ITEM_ORIG_ASMT_INT_ID = "ORIG_ASMT_INT_ID";
	String OASIS_C1_ITEM_RES_INT_ID = "RES_INT_ID";
	String OASIS_C1_ITEM_ASMT_EFF_DATE = "ASMT_EFF_DATE";
	String OASIS_C1_ITEM_BRANCH_IDENTIFIER = "BRANCH_IDENTIFIER";
	String OASIS_C1_ITEM_FAC_INT_ID = "FAC_INT_ID";
	String OASIS_C1_ITEM_SUBMISSION_ID = "SUBMISSION_ID";
	String OASIS_C1_ITEM_SUBMISSION_DATE = "SUBMISSION_DATE";
	String OASIS_C1_ITEM_SUBMISSION_COMPLETE_DATE = "SUBMISSION_COMPLETE_DATE";
	String OASIS_C1_ITEM_SUBMITTING_USER_ID = "SUBMITTING_USER_ID";
	String OASIS_C1_ITEM_RES_MATCH_CRITERIA = "RES_MATCH_CRITERIA";
	String OASIS_C1_ITEM_RESIDENT_AGE = "RESIDENT_AGE";
	String OASIS_C1_ITEM_BIRTHDATE_SUBM_IND = "BIRTHDATE_SUBM_IND";
	String OASIS_C1_ITEM_CALC_HIPPS_CODE = "CALC_HIPPS_CODE";
	String OASIS_C1_ITEM_CALC_HIPPS_VERSION = "CALC_HIPPS_VERSION";
	String OASIS_C1_ITEM_DATA_END_INDICATOR = "DATA_END_INDICATOR";
	String OASIS_C1_ITEM_CR = "CR";
	String OASIS_C1_ITEM_LF = "LF";

	/**
	 * gets Unstageable Due To Non-removable Dressing or Device
	 *
	 * @return
	 */
	String getNSTG_DRSG();

	/**
	 * sets Unstageable Due To Non-removable Dressing or Device
	 */
	void setNSTG_DRSG(String str);

	/**
	 * gets Unstageable Due To Coverage By Slough Or Eschar
	 *
	 * @return
	 */
	String getNSTG_CVRG();

	/*
	 * sets Unstageable Due To Coverage By Slough Or Eschar
	 */
	void setNSTG_CVRG(String str);

	/**
	 * gets Unstageable Due To Suspected Deep Tissue Injury In Evolution
	 *
	 * @return
	 */
	String getNSTG_DEEP_TISUE();

	/**
	 * sets Unstageable Due To Suspected Deep Tissue Injury In Evolution
	 */
	void setNSTG_DEEP_TISUE(String str);

	/**
	 * gets Drug Regimen Review
	 *
	 * @return
	 */
	String getDRUG_RGMN_RVW();

	/**
	 * sets Drug Regimen Review
	 */
	void setDRUG_RGMN_RVW(String str);

	/**
	 * gets Status of most Problematic Pressure Ulcer
	 *
	 * @return
	 */
	String getSTUS_PRBLM_PRSR_ULCR();

	/**
	 * gets Status of most Problematic Pressure Ulcer
	 */
	void setSTUS_PRBLM_PRSR_ULCR(String str);

	/**
	 * gets Patient Has At Least 1 Unhealed PU At Stage 2 Or Higher
	 *
	 * @return
	 */
	String getUNHLD_STG2_PRSR_ULCR();

	/**
	 * sets Patient Has At Least 1 Unhealed PU At Stage 2 Or Higher
	 */
	void setUNHLD_STG2_PRSR_ULCR(String str);

	/**
	 * get When Urinary Incontinence Occurs
	 */
	String getINCNTNT_TIMING();

	/**
	 * set When Urinary Incontinence Occurs
	 */
	void setINCNTNT_TIMING(String val);
}
