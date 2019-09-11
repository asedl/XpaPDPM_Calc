/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth.io.record;

import com.mmm.cms.homehealth.HomeHealthRecord;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.homehealth.proto.RecordType;
import com.mmm.cms.util.OasisCalendarFormatter;

/**
 * This class holds the information from an OASIS-C record that pertains
 * to Home Health scoring/grouping
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class Oasis_C_Record extends HomeHealthRecord  implements HomeHealthRecord_C_IF {

    private String DRUG_REGIMEN_REVIEW;
    private String NSTG_DRSG = "  ";
    private String NSTG_CVRG = "  ";
    private String NSTG_DEEP_TISSUE = "  ";
    private String STATUS_PROBLEM_PRESSURE_ULCER;
    private String UNHEALED_STAGE2_PRESSURE_ULCER;
    protected String NSTG_DRSG_SOC_ROC;
    protected String NSTG_CVRG_SOC_ROC;
    protected String NSTG_DEEP_TISSUE_SOC_ROC;
    protected String NBR_STG2_AT_SOC_ROC;
    protected String NBR_STG3_AT_SOC_ROC;
    protected String NBR_STG4_AT_SOC_ROC;
    protected String PRSR_ULCR_LNGTH;
    protected String PRSR_ULCR_WDTH;
    protected String PRSR_ULCR_DEPTH;
    protected String INCNTNT_TIMING;

    public Oasis_C_Record() {
    }

    public RecordType getRecordType() {
        return RecordType.OASIS_C;
    }


   /**
     * Get the value of NSTG_DEEP_TISSUE
     *
     * @return the value of NSTG_DEEP_TISSUE
     */
    @Override
    public String getNSTG_DEEP_TISUE() {
        return NSTG_DEEP_TISSUE;
    }

    /**
     * Set the value of NSTG_DEEP_TISSUE
     *
     * @param NSTG_DEEP_TISSUE new value of NSTG_DEEP_TISSUE
     */
    @Override
    public void setNSTG_DEEP_TISUE(String NSTG_DEEP_TISSUE) {
        this.NSTG_DEEP_TISSUE = NSTG_DEEP_TISSUE;
    }


    /**
     * Get the value of NSTG_CVRG
     *
     * @return the value of NSTG_CVRG
     */
    @Override
    public String getNSTG_CVRG() {
        return NSTG_CVRG;
    }

    /**
     * Set the value of NSTG_CVRG
     *
     * @param NSTG_CVRG new value of NSTG_CVRG
     */
    @Override
    public void setNSTG_CVRG(String NSTG_CVRG) {
        this.NSTG_CVRG = NSTG_CVRG;
    }

    /**
     * Get the value of NSTG_DRSG
     *
     * @return the value of NSTG_DRSG
     */
    @Override
    public String getNSTG_DRSG() {
        return NSTG_DRSG;
    }

    /**
     * Set the value of NSTG_DRSG
     *
     * @param NSTG_DRSG new value of NSTG_DRSG
     */
    @Override
    public void setNSTG_DRSG(String NSTG_DRSG) {
        this.NSTG_DRSG = NSTG_DRSG;
    }


    /**
     * Get the value of DRUG_REGIMEN_REVIEW
     *
     * @return the value of DRUG_REGIMEN_REVIEW
     */
    @Override
    public String getDRUG_RGMN_RVW() {
        return DRUG_REGIMEN_REVIEW;
    }

    /**
     * Set the value of DRUG_REGIMEN_REVIEW
     *
     * @param DRUG_REGIMEN_REVIEW new value of DRUG_REGIMEN_REVIEW
     */
    @Override
    public void setDRUG_RGMN_RVW(String DRUG_REGIMEN_REVIEW) {
        this.DRUG_REGIMEN_REVIEW = DRUG_REGIMEN_REVIEW;
    }

    /**
     * Get the value of STATUS_PROBLEM_PRESSURE_ULCER
     *
     * @return the value of STATUS_PROBLEM_PRESSURE_ULCER
     */
    @Override
    public String getSTUS_PRBLM_PRSR_ULCR() {
        return STATUS_PROBLEM_PRESSURE_ULCER;
    }

    /**
     * Set the value of STATUS_PROBLEM_PRESSURE_ULCER
     *
     * @param STATUS_PROBLEM_PRESSURE_ULCER new value of STATUS_PROBLEM_PRESSURE_ULCER
     */
    @Override
    public void setSTUS_PRBLM_PRSR_ULCR(
            String STATUS_PROBLEM_PRESSURE_ULCER) {
        this.STATUS_PROBLEM_PRESSURE_ULCER = STATUS_PROBLEM_PRESSURE_ULCER;
    }

    /**
     * Get the value of UNHEALED_STAGE2_PRESSURE_ULCER
     *
     * @return the value of UNHEALED_STAGE2_PRESSURE_ULCER
     */
    @Override
    public String getUNHLD_STG2_PRSR_ULCR() {
        return UNHEALED_STAGE2_PRESSURE_ULCER;
    }

    /**
     * Set the value of UNHEALED_STAGE2_PRESSURE_ULCER
     *
     * @param UNHEALED_STAGE2_PRESSURE_ULCER new value of UNHEALED_STAGE2_PRESSURE_ULCER
     */
    @Override
    public void setUNHLD_STG2_PRSR_ULCR(
            String UNHEALED_STAGE2_PRESSURE_ULCER) {
        this.UNHEALED_STAGE2_PRESSURE_ULCER = UNHEALED_STAGE2_PRESSURE_ULCER;
    }


    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(2400);

        buffer.append("M0030_START_CARE_DT=");
		OasisCalendarFormatter.format(getSTART_CARE_DT(), buffer);

        buffer.append(",M0090_INFO_COMPLETED_DT=");
		OasisCalendarFormatter.format(getINFO_COMPLETED_DT(), buffer);

        buffer.append(",M0100_ASSMT_REASON=");
        buffer.append(getASSMT_REASON());
        buffer.append(",M1020_PRIMARY_DIAG_ICD=");
        buffer.append(getPRIMARY_DIAG_ICD());
        buffer.append(",M1022_OTH_DIAG1_ICD=");
        buffer.append(getOTH_DIAG1_ICD());
        buffer.append(",M1022_OTH_DIAG2_ICD=");
        buffer.append(getOTH_DIAG2_ICD());
        buffer.append(",M1022_OTH_DIAG3_ICD=");
        buffer.append(getOTH_DIAG3_ICD());
        buffer.append(",M1022_OTH_DIAG4_ICD=");
        buffer.append(getOTH_DIAG4_ICD());
        buffer.append(",M1022_OTH_DIAG5_ICD=");
        buffer.append(getOTH_DIAG5_ICD());
        buffer.append(",M1030_THH_IV_INFUSION=");
        buffer.append(getTHH_IV_INFUSION());
        buffer.append(",M1030_THH_PAR_NUTRITION=");
        buffer.append(getTHH_PAR_NUTRITION());
        buffer.append(",M1030_THH_ENT_NUTRITION=");
        buffer.append(getTHH_ENT_NUTRITION());
        buffer.append(",M1030_THH_NONE_ABOVE=");
        buffer.append(getTHH_NONE_ABOVE());
        buffer.append(",M1200_VISION=");
        buffer.append(getVISION());
        buffer.append(",M1242_FREQ_PAIN=");
        buffer.append(getPAIN_FREQ_ACTVTY_MVMT());
        buffer.append(",M1350_LESION_OPEN_WND=");
        buffer.append(getLESION_OPEN_WND());
        buffer.append(",M1322_NBR_PRSULC_STG1=");
        buffer.append(getNBR_PRSULC_STG1());
        buffer.append(",M1306_UNHLD_STG2_PRSR_ULCR=");
        buffer.append(getUNHEALED_STAGE2_PRESSURE_ULCER());
        buffer.append(",M1308_NBR_PRSULC_STG2=");
        buffer.append(getNBR_PRSULC_STG2());
        buffer.append(",M1308_NBR_STG2_AT_SOC_ROC=");
        buffer.append(getNBR_STG2_AT_SOC_ROC());
        buffer.append(",M1308_NBR_PRSULC_STG3=");
        buffer.append(getNBR_PRSULC_STG3());
        buffer.append(",M1308_NBR_STG3_AT_SOC_ROC=");
        buffer.append(getNBR_STG3_AT_SOC_ROC());
        buffer.append(",M1308_NBR_PRSULC_STG4=");
        buffer.append(getNBR_PRSULC_STG4());
        buffer.append(",M1308_NBR_STG4_AT_SOC_ROC=");
        buffer.append(getNBR_STG4_AT_SOC_ROC());
        buffer.append(",M1324_STG_PRBLM_ULCER=");
        buffer.append(getSTG_PRBLM_ULCER());
        buffer.append(",M1330_STASIS_ULCER=");
        buffer.append(getSTAS_ULCR_PRSNT());
        buffer.append(",M1332_NBR_STASULC=");
        buffer.append(getNBR_STAS_ULCR());
        buffer.append(",M1334_STAT_PRB_STASULC=");
        buffer.append(getSTUS_PRBLM_STAS_ULCR());
        buffer.append(",M1340_SRGCL_WND_PRSNT=");
        buffer.append(getSRGCL_WND_PRSNT());
        buffer.append(",M1342_STAT_PRB_SURGWND=");
        buffer.append(getSTUS_PRBLM_SRGCL_WND());
        buffer.append(",M1400_WHEN_DYSPNEIC=");
        buffer.append(getWHEN_DYSPNEIC());
        buffer.append(",M1610_UR_INCONT=");
        buffer.append(getUR_INCONT());
        buffer.append(",M1620_BWL_INCONT=");
        buffer.append(getBWL_INCONT());
        buffer.append(",M1630_OSTOMY=");
        buffer.append(getOSTOMY());
        buffer.append(",M1810_CUR_DRESS_UPPER=");
        buffer.append(getCRNT_DRESS_UPPER());
        buffer.append(",M1820_CUR_DRESS_LOWER=");
        buffer.append(getCRNT_DRESS_LOWER());
        buffer.append(",M1830_CUR_BATHING=");
        buffer.append(getCRNT_BATHG());
        buffer.append(",M1840_CUR_TOILETING=");
        buffer.append(getCRNT_TOILTG());
        buffer.append(",M1850_CUR_TRANSFERRING=");
        buffer.append(getCRNT_TRNSFRNG());
        buffer.append(",M1860_CUR_AMBULATION=");
        buffer.append(getCRNT_AMBLTN());
        buffer.append(",M2030_CUR_INJECT_MEDS=");
        buffer.append(getCRNT_MGMT_INJCTN_MDCTN());
        buffer.append(",M0110_EPISODE_TIMING=");
        buffer.append(getEPISODE_TIMING());
        buffer.append(",M1024_PMT_DIAG_ICD_A3=");
        buffer.append(getPMT_DIAG_ICD_A3());
        buffer.append(",M1024_PMT_DIAG_ICD_B3=");
        buffer.append(getPMT_DIAG_ICD_B3());
        buffer.append(",M1024_PMT_DIAG_ICD_C3=");
        buffer.append(getPMT_DIAG_ICD_C3());
        buffer.append(",M1024_PMT_DIAG_ICD_D3=");
        buffer.append(getPMT_DIAG_ICD_D3());
        buffer.append(",M1024_PMT_DIAG_ICD_E3=");
        buffer.append(getPMT_DIAG_ICD_E3());
        buffer.append(",M1024_PMT_DIAG_ICD_F3=");
        buffer.append(getPMT_DIAG_ICD_F3());
        buffer.append(",M1024_PMT_DIAG_ICD_A4=");
        buffer.append(getPMT_DIAG_ICD_A4());
        buffer.append(",M1024_PMT_DIAG_ICD_B4=");
        buffer.append(getPMT_DIAG_ICD_B4());
        buffer.append(",M1024_PMT_DIAG_ICD_C4=");
        buffer.append(getPMT_DIAG_ICD_C4());
        buffer.append(",M1024_PMT_DIAG_ICD_D4=");
        buffer.append(getPMT_DIAG_ICD_D4());
        buffer.append(",M1024_PMT_DIAG_ICD_E4=");
        buffer.append(getPMT_DIAG_ICD_E4());
        buffer.append(",M1024_PMT_DIAG_ICD_F4=");
        buffer.append(getPMT_DIAG_ICD_F4());
        buffer.append(",M2000_DRUG_RGMN_RVW=");
        buffer.append(getDRUG_RGMN_RVW());
        buffer.append(",M2200_THER_NEED_NUM=");
        buffer.append(getTHER_NEED_NBR());
        buffer.append(",M2200_THER_NEED_NA=");
        buffer.append(getTHER_NEED_NA());

        buffer.append(",M1308_NSTG_DRSG=");
        buffer.append(NSTG_DRSG);
        buffer.append(",M1308_NSTG_DRSG_SOC_ROC=");
        buffer.append(getNSTG_DRSG_SOC_ROC());
        buffer.append(",M1308_NSTG_CVRG=");
        buffer.append(NSTG_CVRG);
        buffer.append(",M1308_NSTG_CVRG_SOC_ROC=");
        buffer.append(getNSTG_CVRG_SOC_ROC());
        buffer.append(",M1308_NSTG_DEEP_TISSUE=");
        buffer.append(NSTG_DEEP_TISSUE);
        buffer.append(",M1308_NSTG_DEEP_TISSUE_SOC_ROC=");
        buffer.append(NSTG_DEEP_TISSUE_SOC_ROC);
        buffer.append(",M1310_PRSR_ULCR_LNGTH=");
        buffer.append(PRSR_ULCR_LNGTH);
        buffer.append(",M1312_PRSR_ULCR_WDTH=");
        buffer.append(PRSR_ULCR_WDTH);
        buffer.append(",M1314_PRSR_ULCR_DEPTH=");
        buffer.append(PRSR_ULCR_DEPTH);
        buffer.append(",M1615_INCNTNT_TIMING=");
        buffer.append(INCNTNT_TIMING);
		
        return buffer.toString();
    }

    @Override
    public String getNSTG_DRSG_SOC_ROC() {
        return NSTG_DRSG_SOC_ROC;
    }

    @Override
    public void setNSTG_DRSG_SOC_ROC(String str) {
        NSTG_DRSG_SOC_ROC = str;
    }

    @Override
    public String getNSTG_CVRG_SOC_ROC() {
        return NSTG_CVRG_SOC_ROC;
    }

    @Override
    public void setNSTG_CVRG_SOC_ROC(String str) {
        NSTG_CVRG_SOC_ROC = str;
    }

    @Override
    public String getNSTG_DEEP_TISSUE_SOC_ROC() {
        return NSTG_DEEP_TISSUE_SOC_ROC;
    }

    @Override
    public void setNSTG_DEEP_TISSUE_SOC_ROC(String str) {
        NSTG_DEEP_TISSUE_SOC_ROC = str;
    }

    public String getSTATUS_PROBLEM_PRESSURE_ULCER() {
        return STATUS_PROBLEM_PRESSURE_ULCER;
    }

    public void setSTATUS_PROBLEM_PRESSURE_ULCER(String STATUS_PROBLEM_PRESSURE_ULCER) {
        this.STATUS_PROBLEM_PRESSURE_ULCER = STATUS_PROBLEM_PRESSURE_ULCER;
    }

    public String getUNHEALED_STAGE2_PRESSURE_ULCER() {
        return UNHEALED_STAGE2_PRESSURE_ULCER;
    }

    public void setUNHEALED_STAGE2_PRESSURE_ULCER(String UNHEALED_STAGE2_PRESSURE_ULCER) {
        this.UNHEALED_STAGE2_PRESSURE_ULCER = UNHEALED_STAGE2_PRESSURE_ULCER;
    }

    @Override
    public String getNBR_STG2_AT_SOC_ROC() {
        return NBR_STG2_AT_SOC_ROC;
    }

    @Override
    public void setNBR_STG2_AT_SOC_ROC(String NBR_STG2_AT_SOC_ROC) {
        this.NBR_STG2_AT_SOC_ROC = NBR_STG2_AT_SOC_ROC;
    }

    @Override
    public String getNBR_STG3_AT_SOC_ROC() {
        return NBR_STG3_AT_SOC_ROC;
    }

    @Override
    public void setNBR_STG3_AT_SOC_ROC(String NBR_STG3_AT_SOC_ROC) {
        this.NBR_STG3_AT_SOC_ROC = NBR_STG3_AT_SOC_ROC;
    }

    @Override
    public String getNBR_STG4_AT_SOC_ROC() {
        return NBR_STG4_AT_SOC_ROC;
    }

    @Override
    public void setNBR_STG4_AT_SOC_ROC(String NBR_STG4_AT_SOC_ROC) {
        this.NBR_STG4_AT_SOC_ROC = NBR_STG4_AT_SOC_ROC;
    }

    @Override
    public String getPRSR_ULCR_DEPTH() {
        return PRSR_ULCR_DEPTH;
    }

    @Override
    public void setPRSR_ULCR_DEPTH(String PRSR_ULCR_DEPTH) {
        this.PRSR_ULCR_DEPTH = PRSR_ULCR_DEPTH;
    }

    @Override
    public String getPRSR_ULCR_LNGTH() {
        return PRSR_ULCR_LNGTH;
    }

    @Override
    public void setPRSR_ULCR_LNGTH(String PRSR_ULCR_LNGTH) {
        this.PRSR_ULCR_LNGTH = PRSR_ULCR_LNGTH;
    }

    @Override
    public String getPRSR_ULCR_WDTH() {
        return PRSR_ULCR_WDTH;
    }

    @Override
    public void setPRSR_ULCR_WDTH(String PRSR_ULCR_WDTH) {
        this.PRSR_ULCR_WDTH = PRSR_ULCR_WDTH;
    }

    /**
     * Get the value of INCNTNT_TIMING
     *
     * @return the value of INCNTNT_TIMING
     */
    @Override
    public String getINCNTNT_TIMING() {
        return INCNTNT_TIMING;
    }

    /**
     * Set the value of INCNTNT_TIMING
     *
     * @param INCNTNT_TIMING new value of INCNTNT_TIMING
     */
    @Override
    public void setINCNTNT_TIMING(String INCNTNT_TIMING) {
        this.INCNTNT_TIMING = INCNTNT_TIMING;
    }
    
}
