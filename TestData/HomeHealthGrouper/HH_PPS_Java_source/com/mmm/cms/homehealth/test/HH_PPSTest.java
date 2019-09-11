/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.test;

import com.mmm.cms.homehealth.pps.HH_PPS;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperFactoryIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import java.io.BufferedReader;
import java.io.Writer;

/**
 * This provides an example testing module for using the HH_PPS class.
 * 
 * @author Tim Gallagher - 3M HIS C&ER for CMS Home Health
 */
public class HH_PPSTest extends CommonTester {

	
	/**
	 *
	 * @param args An array of 1) input file name, and 2) output filename. The
	 * output filename is optional with the default being the input filename
	 * suffixed with "_TESTOUT" before the extension. The extension will be the
	 * same as the input filename.
	 *
	 * Any status of the test or errors are presented to the console.
	 */
	public static void main(String args[]) {
		HH_PPS hhPps;
		HomeHealthRecordIF record;
		String strRecord;
		
		hhPps = HH_PPS.getInstance();
		hhPps.init();
                
//		strRecord = "B1                    C-072009    02.00                                                                                                                                         20100626                                                                                                                     2014010101                                                                                             753.19   401.9    728.87   585.9    577.8    244.9   0001                                                                   00         00       NA              00      00  0000                                   01  02                                                                                                                                                             01                                                                                    0000           Rec 000007019: Code 1AGKS--OASIS 10GV14AA11BFFGAIEH--Version V3414 --Flag 1                                   00   0                                                00    00  0                0300  0101                                    NA                                                                                                                                                                                                                                                                                                                                       %";
		strRecord = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<ASSESSMENT>\n" +
"  <ASMT_SYS_CD>OASIS</ASMT_SYS_CD>\n" +
"  <TRANS_TYPE_CD>1</TRANS_TYPE_CD>\n" +
"  <ITM_SBST_CD>01</ITM_SBST_CD>\n" +
"  <ITM_SET_VRSN_CD>C1-102015</ITM_SET_VRSN_CD>\n" +
"  <SPEC_VRSN_CD>2.12</SPEC_VRSN_CD>\n" +
"  <CORRECTION_NUM>00</CORRECTION_NUM>\n" +
"  <STATE_CD>NY</STATE_CD>\n" +
"  <HHA_AGENCY_ID>1850</HHA_AGENCY_ID>\n" +
"  <NATL_PRVDR_ID>^</NATL_PRVDR_ID>\n" +
"  <SFW_ID>521230861</SFW_ID>\n" +
"  <SFW_NAME>CMS</SFW_NAME>\n" +
"  <SFW_EMAIL_ADR>help@qtso.com</SFW_EMAIL_ADR>\n" +
"  <SFW_PROD_NAME>jHAVEN</SFW_PROD_NAME>\n" +
"  <SFW_PROD_VRSN_CD>1.1.2</SFW_PROD_VRSN_CD>\n" +
"  <ACY_DOC_CD>^</ACY_DOC_CD>\n" +
"  <SUBM_HIPPS_CODE>^</SUBM_HIPPS_CODE>\n" +
"  <SUBM_HIPPS_VERSION>^</SUBM_HIPPS_VERSION>\n" +
"  <M0010_CCN>337042</M0010_CCN>\n" +
"  <M0014_BRANCH_STATE>^</M0014_BRANCH_STATE>\n" +
"  <M0016_BRANCH_ID>P</M0016_BRANCH_ID>\n" +
"  <M0018_PHYSICIAN_ID>^</M0018_PHYSICIAN_ID>\n" +
"  <M0018_PHYSICIAN_UK>1</M0018_PHYSICIAN_UK>\n" +
"  <M0020_PAT_ID>^</M0020_PAT_ID>\n" +
"  <M0030_START_CARE_DT>20161231</M0030_START_CARE_DT>\n" +
"  <M0032_ROC_DT>^</M0032_ROC_DT>\n" +
"  <M0032_ROC_DT_NA>1</M0032_ROC_DT_NA>\n" +
"  <M0040_PAT_FNAME>RFA1 1231</M0040_PAT_FNAME>\n" +
"  <M0040_PAT_MI>^</M0040_PAT_MI>\n" +
"  <M0040_PAT_LNAME>V5216 RFA1</M0040_PAT_LNAME>\n" +
"  <M0040_PAT_SUFFIX>^</M0040_PAT_SUFFIX>\n" +
"  <M0050_PAT_ST>NY</M0050_PAT_ST>\n" +
"  <M0060_PAT_ZIP>01552</M0060_PAT_ZIP>\n" +
"  <M0063_MEDICARE_NUM>999470001D</M0063_MEDICARE_NUM>\n" +
"  <M0063_MEDICARE_NA>0</M0063_MEDICARE_NA>\n" +
"  <M0064_SSN>999470001</M0064_SSN>\n" +
"  <M0064_SSN_UK>0</M0064_SSN_UK>\n" +
"  <M0065_MEDICAID_NUM>999470001</M0065_MEDICAID_NUM>\n" +
"  <M0065_MEDICAID_NA>0</M0065_MEDICAID_NA>\n" +
"  <M0066_PAT_BIRTH_DT>19620601</M0066_PAT_BIRTH_DT>\n" +
"  <M0069_PAT_GENDER>2</M0069_PAT_GENDER>\n" +
"  <M0140_ETHNIC_AI_AN>0</M0140_ETHNIC_AI_AN>\n" +
"  <M0140_ETHNIC_ASIAN>0</M0140_ETHNIC_ASIAN>\n" +
"  <M0140_ETHNIC_BLACK>0</M0140_ETHNIC_BLACK>\n" +
"  <M0140_ETHNIC_HISP>0</M0140_ETHNIC_HISP>\n" +
"  <M0140_ETHNIC_NH_PI>0</M0140_ETHNIC_NH_PI>\n" +
"  <M0140_ETHNIC_WHITE>1</M0140_ETHNIC_WHITE>\n" +
"  <M0150_CPAY_NONE>0</M0150_CPAY_NONE>\n" +
"  <M0150_CPAY_MCARE_FFS>1</M0150_CPAY_MCARE_FFS>\n" +
"  <M0150_CPAY_MCARE_HMO>0</M0150_CPAY_MCARE_HMO>\n" +
"  <M0150_CPAY_MCAID_FFS>0</M0150_CPAY_MCAID_FFS>\n" +
"  <M0150_CPAY_MCAID_HMO>0</M0150_CPAY_MCAID_HMO>\n" +
"  <M0150_CPAY_WRKCOMP>0</M0150_CPAY_WRKCOMP>\n" +
"  <M0150_CPAY_TITLEPGMS>0</M0150_CPAY_TITLEPGMS>\n" +
"  <M0150_CPAY_OTH_GOVT>0</M0150_CPAY_OTH_GOVT>\n" +
"  <M0150_CPAY_PRIV_INS>0</M0150_CPAY_PRIV_INS>\n" +
"  <M0150_CPAY_PRIV_HMO>0</M0150_CPAY_PRIV_HMO>\n" +
"  <M0150_CPAY_SELFPAY>0</M0150_CPAY_SELFPAY>\n" +
"  <M0150_CPAY_OTHER>0</M0150_CPAY_OTHER>\n" +
"  <M0150_CPAY_UK>0</M0150_CPAY_UK>\n" +
"  <M0080_ASSESSOR_DISCIPLINE>01</M0080_ASSESSOR_DISCIPLINE>\n" +
"  <M0090_INFO_COMPLETED_DT>20161231</M0090_INFO_COMPLETED_DT>\n" +
"  <M0100_ASSMT_REASON>01</M0100_ASSMT_REASON>\n" +
"  <M0102_PHYSN_ORDRD_SOCROC_DT>20161231</M0102_PHYSN_ORDRD_SOCROC_DT>\n" +
"  <M0102_PHYSN_ORDRD_SOCROC_DT_NA>0</M0102_PHYSN_ORDRD_SOCROC_DT_NA>\n" +
"  <M0104_PHYSN_RFRL_DT>^</M0104_PHYSN_RFRL_DT>\n" +
"  <M0110_EPISODE_TIMING>02</M0110_EPISODE_TIMING>\n" +
"  <M1000_DC_LTC_14_DA>1</M1000_DC_LTC_14_DA>\n" +
"  <M1000_DC_SNF_14_DA>0</M1000_DC_SNF_14_DA>\n" +
"  <M1000_DC_IPPS_14_DA>0</M1000_DC_IPPS_14_DA>\n" +
"  <M1000_DC_LTCH_14_DA>0</M1000_DC_LTCH_14_DA>\n" +
"  <M1000_DC_IRF_14_DA>0</M1000_DC_IRF_14_DA>\n" +
"  <M1000_DC_PSYCH_14_DA>0</M1000_DC_PSYCH_14_DA>\n" +
"  <M1000_DC_OTH_14_DA>0</M1000_DC_OTH_14_DA>\n" +
"  <M1000_DC_NONE_14_DA>0</M1000_DC_NONE_14_DA>\n" +
"  <M1005_INP_DISCHARGE_DT>20161231</M1005_INP_DISCHARGE_DT>\n" +
"  <M1005_INP_DSCHG_UNKNOWN>0</M1005_INP_DSCHG_UNKNOWN>\n" +
"  <M1011_14_DAY_INP1_ICD>F42.^^^^</M1011_14_DAY_INP1_ICD>\n" +
"  <M1011_14_DAY_INP2_ICD>^</M1011_14_DAY_INP2_ICD>\n" +
"  <M1011_14_DAY_INP3_ICD>^</M1011_14_DAY_INP3_ICD>\n" +
"  <M1011_14_DAY_INP4_ICD>^</M1011_14_DAY_INP4_ICD>\n" +
"  <M1011_14_DAY_INP5_ICD>^</M1011_14_DAY_INP5_ICD>\n" +
"  <M1011_14_DAY_INP6_ICD>^</M1011_14_DAY_INP6_ICD>\n" +
"  <M1017_CHGREG_ICD1>Q71.62^^</M1017_CHGREG_ICD1>\n" +
"  <M1017_CHGREG_ICD2>^</M1017_CHGREG_ICD2>\n" +
"  <M1017_CHGREG_ICD3>^</M1017_CHGREG_ICD3>\n" +
"  <M1017_CHGREG_ICD4>^</M1017_CHGREG_ICD4>\n" +
"  <M1017_CHGREG_ICD5>^</M1017_CHGREG_ICD5>\n" +
"  <M1017_CHGREG_ICD6>^</M1017_CHGREG_ICD6>\n" +
"  <M1017_CHGREG_ICD_NA>0</M1017_CHGREG_ICD_NA>\n" +
"  <M1018_PRIOR_UR_INCON>1</M1018_PRIOR_UR_INCON>\n" +
"  <M1018_PRIOR_CATH>0</M1018_PRIOR_CATH>\n" +
"  <M1018_PRIOR_INTRACT_PAIN>0</M1018_PRIOR_INTRACT_PAIN>\n" +
"  <M1018_PRIOR_IMPR_DECSN>0</M1018_PRIOR_IMPR_DECSN>\n" +
"  <M1018_PRIOR_DISRUPTIVE>0</M1018_PRIOR_DISRUPTIVE>\n" +
"  <M1018_PRIOR_MEM_LOSS>0</M1018_PRIOR_MEM_LOSS>\n" +
"  <M1018_PRIOR_NONE>0</M1018_PRIOR_NONE>\n" +
"  <M1018_PRIOR_NOCHG_14D>0</M1018_PRIOR_NOCHG_14D>\n" +
"  <M1018_PRIOR_UNKNOWN>0</M1018_PRIOR_UNKNOWN>\n" +
"  <M1021_PRIMARY_DIAG_ICD>S43.151A</M1021_PRIMARY_DIAG_ICD>\n" +
"  <M1021_PRIMARY_DIAG_SEVERITY>01</M1021_PRIMARY_DIAG_SEVERITY>\n" +
"  <M1023_OTH_DIAG1_ICD>^</M1023_OTH_DIAG1_ICD>\n" +
"  <M1023_OTH_DIAG1_SEVERITY>^</M1023_OTH_DIAG1_SEVERITY>\n" +
"  <M1023_OTH_DIAG2_ICD>^</M1023_OTH_DIAG2_ICD>\n" +
"  <M1023_OTH_DIAG2_SEVERITY>^</M1023_OTH_DIAG2_SEVERITY>\n" +
"  <M1023_OTH_DIAG3_ICD>^</M1023_OTH_DIAG3_ICD>\n" +
"  <M1023_OTH_DIAG3_SEVERITY>^</M1023_OTH_DIAG3_SEVERITY>\n" +
"  <M1023_OTH_DIAG4_ICD>^</M1023_OTH_DIAG4_ICD>\n" +
"  <M1023_OTH_DIAG4_SEVERITY>^</M1023_OTH_DIAG4_SEVERITY>\n" +
"  <M1023_OTH_DIAG5_ICD>^</M1023_OTH_DIAG5_ICD>\n" +
"  <M1023_OTH_DIAG5_SEVERITY>^</M1023_OTH_DIAG5_SEVERITY>\n" +
"  <M1025_OPT_DIAG_ICD_A3>^</M1025_OPT_DIAG_ICD_A3>\n" +
"  <M1025_OPT_DIAG_ICD_A4>^</M1025_OPT_DIAG_ICD_A4>\n" +
"  <M1025_OPT_DIAG_ICD_B3>^</M1025_OPT_DIAG_ICD_B3>\n" +
"  <M1025_OPT_DIAG_ICD_B4>^</M1025_OPT_DIAG_ICD_B4>\n" +
"  <M1025_OPT_DIAG_ICD_C3>^</M1025_OPT_DIAG_ICD_C3>\n" +
"  <M1025_OPT_DIAG_ICD_C4>^</M1025_OPT_DIAG_ICD_C4>\n" +
"  <M1025_OPT_DIAG_ICD_D3>^</M1025_OPT_DIAG_ICD_D3>\n" +
"  <M1025_OPT_DIAG_ICD_D4>^</M1025_OPT_DIAG_ICD_D4>\n" +
"  <M1025_OPT_DIAG_ICD_E3>^</M1025_OPT_DIAG_ICD_E3>\n" +
"  <M1025_OPT_DIAG_ICD_E4>^</M1025_OPT_DIAG_ICD_E4>\n" +
"  <M1025_OPT_DIAG_ICD_F3>^</M1025_OPT_DIAG_ICD_F3>\n" +
"  <M1025_OPT_DIAG_ICD_F4>^</M1025_OPT_DIAG_ICD_F4>\n" +
"  <M1030_THH_IV_INFUSION>1</M1030_THH_IV_INFUSION>\n" +
"  <M1030_THH_PAR_NUTRITION>0</M1030_THH_PAR_NUTRITION>\n" +
"  <M1030_THH_ENT_NUTRITION>0</M1030_THH_ENT_NUTRITION>\n" +
"  <M1030_THH_NONE_ABOVE>0</M1030_THH_NONE_ABOVE>\n" +
"  <M1033_HOSP_RISK_HSTRY_FALLS>1</M1033_HOSP_RISK_HSTRY_FALLS>\n" +
"  <M1033_HOSP_RISK_WEIGHT_LOSS>0</M1033_HOSP_RISK_WEIGHT_LOSS>\n" +
"  <M1033_HOSP_RISK_MLTPL_HOSPZTN>0</M1033_HOSP_RISK_MLTPL_HOSPZTN>\n" +
"  <M1033_HOSP_RISK_MLTPL_ED_VISIT>0</M1033_HOSP_RISK_MLTPL_ED_VISIT>\n" +
"  <M1033_HOSP_RISK_MNTL_BHV_DCLN>0</M1033_HOSP_RISK_MNTL_BHV_DCLN>\n" +
"  <M1033_HOSP_RISK_COMPLIANCE>0</M1033_HOSP_RISK_COMPLIANCE>\n" +
"  <M1033_HOSP_RISK_5PLUS_MDCTN>0</M1033_HOSP_RISK_5PLUS_MDCTN>\n" +
"  <M1033_HOSP_RISK_CRNT_EXHSTN>0</M1033_HOSP_RISK_CRNT_EXHSTN>\n" +
"  <M1033_HOSP_RISK_OTHR_RISK>0</M1033_HOSP_RISK_OTHR_RISK>\n" +
"  <M1033_HOSP_RISK_NONE_ABOVE>0</M1033_HOSP_RISK_NONE_ABOVE>\n" +
"  <M1034_PTNT_OVRAL_STUS>00</M1034_PTNT_OVRAL_STUS>\n" +
"  <M1036_RSK_SMOKING>1</M1036_RSK_SMOKING>\n" +
"  <M1036_RSK_OBESITY>0</M1036_RSK_OBESITY>\n" +
"  <M1036_RSK_ALCOHOLISM>0</M1036_RSK_ALCOHOLISM>\n" +
"  <M1036_RSK_DRUGS>0</M1036_RSK_DRUGS>\n" +
"  <M1036_RSK_NONE>0</M1036_RSK_NONE>\n" +
"  <M1036_RSK_UNKNOWN>0</M1036_RSK_UNKNOWN>\n" +
"  <M1100_PTNT_LVG_STUTN>02</M1100_PTNT_LVG_STUTN>\n" +
"  <M1200_VISION>01</M1200_VISION>\n" +
"  <M1210_HEARG_ABLTY>00</M1210_HEARG_ABLTY>\n" +
"  <M1220_UNDRSTG_VERBAL_CNTNT>00</M1220_UNDRSTG_VERBAL_CNTNT>\n" +
"  <M1230_SPEECH>00</M1230_SPEECH>\n" +
"  <M1240_FRML_PAIN_ASMT>00</M1240_FRML_PAIN_ASMT>\n" +
"  <M1242_PAIN_FREQ_ACTVTY_MVMT>00</M1242_PAIN_FREQ_ACTVTY_MVMT>\n" +
"  <M1300_PRSR_ULCR_RISK_ASMT>01</M1300_PRSR_ULCR_RISK_ASMT>\n" +
"  <M1302_RISK_OF_PRSR_ULCR>1</M1302_RISK_OF_PRSR_ULCR>\n" +
"  <M1306_UNHLD_STG2_PRSR_ULCR>1</M1306_UNHLD_STG2_PRSR_ULCR>\n" +
"  <M1308_NBR_PRSULC_STG2>01</M1308_NBR_PRSULC_STG2>\n" +
"  <M1308_NBR_PRSULC_STG3>01</M1308_NBR_PRSULC_STG3>\n" +
"  <M1308_NBR_PRSULC_STG4>01</M1308_NBR_PRSULC_STG4>\n" +
"  <M1308_NSTG_DRSG>01</M1308_NSTG_DRSG>\n" +
"  <M1308_NSTG_CVRG>01</M1308_NSTG_CVRG>\n" +
"  <M1308_NSTG_DEEP_TISUE>01</M1308_NSTG_DEEP_TISUE>\n" +
"  <M1320_STUS_PRBLM_PRSR_ULCR>03</M1320_STUS_PRBLM_PRSR_ULCR>\n" +
"  <M1322_NBR_PRSULC_STG1>01</M1322_NBR_PRSULC_STG1>\n" +
"  <M1324_STG_PRBLM_ULCER>03</M1324_STG_PRBLM_ULCER>\n" +
"  <M1330_STAS_ULCR_PRSNT>01</M1330_STAS_ULCR_PRSNT>\n" +
"  <M1332_NBR_STAS_ULCR>01</M1332_NBR_STAS_ULCR>\n" +
"  <M1334_STUS_PRBLM_STAS_ULCR>01</M1334_STUS_PRBLM_STAS_ULCR>\n" +
"  <M1340_SRGCL_WND_PRSNT>00</M1340_SRGCL_WND_PRSNT>\n" +
"  <M1342_STUS_PRBLM_SRGCL_WND>^</M1342_STUS_PRBLM_SRGCL_WND>\n" +
"  <M1350_LESION_OPEN_WND>0</M1350_LESION_OPEN_WND>\n" +
"  <M1400_WHEN_DYSPNEIC>00</M1400_WHEN_DYSPNEIC>\n" +
"  <M1410_RESPTX_OXYGEN>1</M1410_RESPTX_OXYGEN>\n" +
"  <M1410_RESPTX_VENTILATOR>0</M1410_RESPTX_VENTILATOR>\n" +
"  <M1410_RESPTX_AIRPRESS>0</M1410_RESPTX_AIRPRESS>\n" +
"  <M1410_RESPTX_NONE>0</M1410_RESPTX_NONE>\n" +
"  <M1600_UTI>01</M1600_UTI>\n" +
"  <M1610_UR_INCONT>00</M1610_UR_INCONT>\n" +
"  <M1615_INCNTNT_TIMING>^</M1615_INCNTNT_TIMING>\n" +
"  <M1620_BWL_INCONT>00</M1620_BWL_INCONT>\n" +
"  <M1630_OSTOMY>00</M1630_OSTOMY>\n" +
"  <M1700_COG_FUNCTION>00</M1700_COG_FUNCTION>\n" +
"  <M1710_WHEN_CONFUSED>00</M1710_WHEN_CONFUSED>\n" +
"  <M1720_WHEN_ANXIOUS>00</M1720_WHEN_ANXIOUS>\n" +
"  <M1730_STDZ_DPRSN_SCRNG>00</M1730_STDZ_DPRSN_SCRNG>\n" +
"  <M1730_PHQ2_LACK_INTRST>^</M1730_PHQ2_LACK_INTRST>\n" +
"  <M1730_PHQ2_DPRSN>^</M1730_PHQ2_DPRSN>\n" +
"  <M1740_BD_MEM_DEFICIT>0</M1740_BD_MEM_DEFICIT>\n" +
"  <M1740_BD_IMP_DECISN>0</M1740_BD_IMP_DECISN>\n" +
"  <M1740_BD_VERBAL>1</M1740_BD_VERBAL>\n" +
"  <M1740_BD_PHYSICAL>0</M1740_BD_PHYSICAL>\n" +
"  <M1740_BD_SOC_INAPPRO>0</M1740_BD_SOC_INAPPRO>\n" +
"  <M1740_BD_DELUSIONS>0</M1740_BD_DELUSIONS>\n" +
"  <M1740_BD_NONE>0</M1740_BD_NONE>\n" +
"  <M1745_BEH_PROB_FREQ>00</M1745_BEH_PROB_FREQ>\n" +
"  <M1750_REC_PSYCH_NURS>0</M1750_REC_PSYCH_NURS>\n" +
"  <M1800_CRNT_GROOMING>01</M1800_CRNT_GROOMING>\n" +
"  <M1810_CRNT_DRESS_UPPER>00</M1810_CRNT_DRESS_UPPER>\n" +
"  <M1820_CRNT_DRESS_LOWER>00</M1820_CRNT_DRESS_LOWER>\n" +
"  <M1830_CRNT_BATHG>00</M1830_CRNT_BATHG>\n" +
"  <M1840_CRNT_TOILTG>00</M1840_CRNT_TOILTG>\n" +
"  <M1845_CRNT_TOILTG_HYGN>00</M1845_CRNT_TOILTG_HYGN>\n" +
"  <M1850_CRNT_TRNSFRNG>00</M1850_CRNT_TRNSFRNG>\n" +
"  <M1860_CRNT_AMBLTN>00</M1860_CRNT_AMBLTN>\n" +
"  <M1870_CRNT_FEEDING>00</M1870_CRNT_FEEDING>\n" +
"  <M1880_CRNT_PREP_LT_MEALS>00</M1880_CRNT_PREP_LT_MEALS>\n" +
"  <M1890_CRNT_PHONE_USE>00</M1890_CRNT_PHONE_USE>\n" +
"  <M1900_PRIOR_ADLIADL_SELF>00</M1900_PRIOR_ADLIADL_SELF>\n" +
"  <M1900_PRIOR_ADLIADL_AMBLTN>00</M1900_PRIOR_ADLIADL_AMBLTN>\n" +
"  <M1900_PRIOR_ADLIADL_TRNSFR>00</M1900_PRIOR_ADLIADL_TRNSFR>\n" +
"  <M1900_PRIOR_ADLIADL_HSEHOLD>00</M1900_PRIOR_ADLIADL_HSEHOLD>\n" +
"  <M1910_MLT_FCTR_FALL_RISK_ASMT>00</M1910_MLT_FCTR_FALL_RISK_ASMT>\n" +
"  <M2000_DRUG_RGMN_RVW>00</M2000_DRUG_RGMN_RVW>\n" +
"  <M2002_MDCTN_FLWP>^</M2002_MDCTN_FLWP>\n" +
"  <M2010_HIGH_RISK_DRUG_EDCTN>00</M2010_HIGH_RISK_DRUG_EDCTN>\n" +
"  <M2020_CRNT_MGMT_ORAL_MDCTN>00</M2020_CRNT_MGMT_ORAL_MDCTN>\n" +
"  <M2030_CRNT_MGMT_INJCTN_MDCTN>00</M2030_CRNT_MGMT_INJCTN_MDCTN>\n" +
"  <M2040_PRIOR_MGMT_ORAL_MDCTN>00</M2040_PRIOR_MGMT_ORAL_MDCTN>\n" +
"  <M2040_PRIOR_MGMT_INJCTN_MDCTN>00</M2040_PRIOR_MGMT_INJCTN_MDCTN>\n" +
"  <M2102_CARE_TYPE_SRC_ADL>00</M2102_CARE_TYPE_SRC_ADL>\n" +
"  <M2102_CARE_TYPE_SRC_IADL>00</M2102_CARE_TYPE_SRC_IADL>\n" +
"  <M2102_CARE_TYPE_SRC_MDCTN>00</M2102_CARE_TYPE_SRC_MDCTN>\n" +
"  <M2102_CARE_TYPE_SRC_PRCDR>00</M2102_CARE_TYPE_SRC_PRCDR>\n" +
"  <M2102_CARE_TYPE_SRC_EQUIP>00</M2102_CARE_TYPE_SRC_EQUIP>\n" +
"  <M2102_CARE_TYPE_SRC_SPRVSN>00</M2102_CARE_TYPE_SRC_SPRVSN>\n" +
"  <M2102_CARE_TYPE_SRC_ADVCY>00</M2102_CARE_TYPE_SRC_ADVCY>\n" +
"  <M2110_ADL_IADL_ASTNC_FREQ>01</M2110_ADL_IADL_ASTNC_FREQ>\n" +
"  <M2200_THER_NEED_NBR>10</M2200_THER_NEED_NBR>\n" +
"  <M2200_THER_NEED_NA>0</M2200_THER_NEED_NA>\n" +
"  <M2250_PLAN_SMRY_PTNT_SPECF>00</M2250_PLAN_SMRY_PTNT_SPECF>\n" +
"  <M2250_PLAN_SMRY_DBTS_FT_CARE>00</M2250_PLAN_SMRY_DBTS_FT_CARE>\n" +
"  <M2250_PLAN_SMRY_FALL_PRVNT>00</M2250_PLAN_SMRY_FALL_PRVNT>\n" +
"  <M2250_PLAN_SMRY_DPRSN_INTRVTN>00</M2250_PLAN_SMRY_DPRSN_INTRVTN>\n" +
"  <M2250_PLAN_SMRY_PAIN_INTRVTN>00</M2250_PLAN_SMRY_PAIN_INTRVTN>\n" +
"  <M2250_PLAN_SMRY_PRSULC_PRVNT>00</M2250_PLAN_SMRY_PRSULC_PRVNT>\n" +
"  <M2250_PLAN_SMRY_PRSULC_TRTMT>00</M2250_PLAN_SMRY_PRSULC_TRTMT>\n" +
"</ASSESSMENT>";
                /*
		 * If all you want to do is convert the record from a string to a 
		 * Home Health Record, then you can call the following method
		 * right away.
		 */
//		record = hhPps.convertRecord(strRecord);
                ScoringResultsIF sr = hhPps.scoreRecord(strRecord);
	}

    @Override
    public void runTest(BufferedReader bufReader, Writer writer, HomeHealthGrouperFactoryIF grouperFactory, CommandLineParams commandLine) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	
}
