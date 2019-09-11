/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto.record;


/**
 * This is a marker to identify a record built from an OASIS-C record string
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface HomeHealthRecord_C_IF extends HomeHealthRecordIF {

    /**
     * gets Unstageable Due To Non-removable Dressing or Device
     * @return
     */
    String getNSTG_DRSG();

    /**
     * sets Unstageable Due To Non-removable Dressing or Device
     */
    void setNSTG_DRSG(String str);

    /**
     * Unstageable Due To Non-removable Dressing Or Device At SOC/ROC
     * @return
     */
    String getNSTG_DRSG_SOC_ROC();

    /**
     * Unstageable Due To Non-removable Dressing Or Device At SOC/ROC
     * @param str
     */
    void setNSTG_DRSG_SOC_ROC(String str);

    /**
     * gets Unstageable Due To Coverage By Slough Or Eschar
     * @return
     */
    String getNSTG_CVRG();

    /*
     * sets Unstageable Due To Coverage By Slough Or Eschar
     */
    void setNSTG_CVRG(String str);

    /**
     * Unstageable Due To Coverage By Slough Or Eschar At SOC/ROC
     * @return
     */
    String getNSTG_CVRG_SOC_ROC();

    /**
     * Unstageable Due To Coverage By Slough Or Eschar At SOC/ROC
     * @param str
     */
    void setNSTG_CVRG_SOC_ROC(String str);

    /**
     * gets Unstageable Due To Suspected Deep Tissue Injury In Evolution
     * @return
     */
    String getNSTG_DEEP_TISUE();

    /**
     * sets Unstageable Due To Suspected Deep Tissue Injury In Evolution
     */
    void setNSTG_DEEP_TISUE(String str);

    /**
     * Unstageable Due To Suspected Deep Tissue Injury In Evolution At SOC/ROC
     * @return
     */
    String getNSTG_DEEP_TISSUE_SOC_ROC();

    /**
     * Unstageable Due To Suspected Deep Tissue Injury In Evolution At SOC/ROC
     * @param str
     */
    void setNSTG_DEEP_TISSUE_SOC_ROC(String str);

    /**
     * gets Drug Regimen Review
     * @return
     */
    String getDRUG_RGMN_RVW();

    /**
     * sets Drug Regimen Review
     */
    void setDRUG_RGMN_RVW(String str);

    /**
     * gets Status of most Problematic Pressure Ulcer
     * @return
     */
    String getSTUS_PRBLM_PRSR_ULCR();

    /**
     * gets Status of most Problematic Pressure Ulcer
     */
    void setSTUS_PRBLM_PRSR_ULCR(String str);

    /**
     * gets Patient Has At Least 1 Unhealed PU At Stage 2 Or Higher
     * @return
     */
    String getUNHLD_STG2_PRSR_ULCR();

    /**
     * sets Patient Has At Least 1 Unhealed PU At Stage 2 Or Higher
     */
    void setUNHLD_STG2_PRSR_ULCR(String str);

	/**
	 *Number PU Stage 2 At SOC/ROC - 2 1008 1009 
	 * @return 
	 */
    String getNBR_STG2_AT_SOC_ROC();

	/**
	 *
	 * @param val 
	 */
    void setNBR_STG2_AT_SOC_ROC(String val);

    /**
	 * Number PU Stage 3 At SOC/ROC - 2 1012 1013
	 * @return 
	 */
    String getNBR_STG3_AT_SOC_ROC();

	/**
	 * 
	 * @param val 
	 */
    void setNBR_STG3_AT_SOC_ROC(String val);

    /**
	 * Number PU Stage 4 At SOC/ROC - 2 1016 1017
	 * @return 
	 */
    String getNBR_STG4_AT_SOC_ROC();

	/**
	 * 
	 * @param val 
	 */
    void setNBR_STG4_AT_SOC_ROC(String val);

    /**
	 * Head To Toe Length Of Stage III Or IV Pu With Largest Area - 4 1030 1033
	 * @return 
	 */
    String getPRSR_ULCR_LNGTH();

	/**
	 * 
	 * @param val 
	 */
    void setPRSR_ULCR_LNGTH(String val);

    /**
	 * Width At Right Angles Of Stage III Or IV Pu With Largest Area - 4 1034 1037
	 * @return 
	 */
    String getPRSR_ULCR_WDTH();

	/**
	 * 
	 * @param val 
	 */
    void setPRSR_ULCR_WDTH(String val);

    /**
	 * Depth Of Stage III Or IV Pu With Largest Area - 4 1038 1041
	 * @return 
	 */
    String getPRSR_ULCR_DEPTH();

	/**
	 * 
	 * @param val 
	 */
    void setPRSR_ULCR_DEPTH(String val);

    /**
	 * get When Urinary Incontinence Occurs
	 * @return 
	 */
    String getINCNTNT_TIMING();

    /**
	 * set When Urinary Incontinence Occurs
	 * @param val 
	 */
    void setINCNTNT_TIMING(String val);

}

