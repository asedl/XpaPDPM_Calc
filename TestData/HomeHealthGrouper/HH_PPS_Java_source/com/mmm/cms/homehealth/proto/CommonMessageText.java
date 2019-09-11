/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

/**
 *
 * @author 3M Clinical & Economic Research for CMS Home Health
 */
public interface CommonMessageText {

	/*
	 * Blank values
	 */
	String BLANK_DATE = "        ";
	String BLANK_ICD = "        ";

	/*
	 * Event Values
	 */
	String EVENT_MSG_HEADER = "HH-PPS: ";
	String EVENT_SCORE_MSG_NBR_PRSULC_STG1 = "score added due to NBR_PRSULC_STG1";
	String EVENT_SCORE_MSG_NBR_PRSULC_STG2 = "score added due to NBR_PRSULC_STG2";
	String EVENT_SCORE_MSG_NBR_PRSULC_STG3 = "score added due to NBR_PRSULC_STG3";
	String EVENT_SCORE_MSG_NBR_PRSULC_STG4 = "score added due to NBR_PRSULC_STG4";
	String EVENT_SCORE_MSG_NBR_STASULC = "score added due to NBR_STASULC";
	String EVENT_SCORE_MSG_STAS_ULCR_PRSNT = "score added due to Unobserved STAS_ULCR_PRSNT";
	String EVENT_SCORE_MSG_STAT_PRB_STASULC = "score added due to STAT_PRB_STASULC";
	String EVENT_SCORE_MSG_STAT_PRB_SURGWND = "score added due to STAT_PRB_SURGWND";
	String EVENT_SCORE_MSG_OSTOMY = "score added due to OSTOMY";
	String EVENT_SCORE_MSG_OSTOMY_SKIN = "score added due to OSTOMY and skin score > 0";
	String EVENT_SCORE_MSG_THH_IV_INFUSION = "score added due to THH_IV_INFUSION";
	String EVENT_SCORE_MSG_UNOBS_PRSULC = "score added due to UNOBS_PRSULC";
	String EVENT_SCORE_MSG_UNOBS_STASULC = "score added due to UNOBS_STASULC";
	String EVENT_SCORE_MSG_UR_INCONT = "score added due to UR_INCONT";
	String EVENT_SCORE_MSG_BWL_INCONT = "score added due to BWL_INCONT";
	String EVENT_SCORE_MSG_NSTG_DRSG_or_NSTG_CVRG = "score added due to NSTG_DRSG or NSTG_CVRG";
	String EVENT_SCORE_MSG_POINTS_LOST_ADDED_BACK = "Points Lost added back ";
	String EVENT_SCORE_MSG_THH_IV_INFUSION_or_THH_PAR_NUTRITION = "score added due to THH_IV_INFUSION or THH_PAR_NUTRITION ";
	String EVENT_SCORE_MSG_THH_ENT_NUTRITION = "score added due to THH_ENT_NUTRITION";
	String EVENT_SCORE_MSG_VISION = "score added due to VISION";
	String EVENT_SCORE_MSG_FREQ_PAIN = "score added due to FREQ_PAIN";
	String EVENT_SCORE_MSG_NBR_PRSULC_STG3_and_NBR_PRSULC_STG4 = "score added due to NBR_PRSULC_STG3 + NBR_PRSULC_STG4";
	String EVENT_SCORE_MSG_STG_PRBLM_ULCER = "score added due to STG_PRBLM_ULCER";
	String EVENT_SCORE_MSG_WHEN_DYSPNEIC = "score added due to WHEN_DYSPNEIC";
	String EVENT_SCORE_MSG_CUR_INJECT_MEDS = "score added due to CUR_INJECT_MEDS";
				
	/*
	 * Misc Values
	 */
	String NOT_SUPPORTED = "Not supported yet.";
	String CALCULATE_SPECIAL_ITEMS = "calculate Special Items";
	String NRS_PRE_PROCESS_RECORD = "NRS Pre-process record";
	String MSG_EQUATION = "Equation ";
	String WITH_VALUE_SINGLE_QUOTE = " with value '";
	String DIABETIC_ULCER_SET = "Diabeic Ulcer: Set ";
	
	
}
