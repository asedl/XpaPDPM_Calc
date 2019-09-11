/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.io.record;

import com.mmm.cms.homehealth.proto.RecordType;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_D_IF;
import com.mmm.cms.util.OasisCalendarFormatter;

/**
 * OASIS-C2 2.20 Record
 * This is the same as the OASIS C1 2.12, except for the name of the 
 * PRESSURE ULCER fields
 * 
 * @author 3M Health Information Systems for CMS Home Health
 */
public class Oasis_D_Record_2_30 extends Oasis_C_Record implements HomeHealthRecord_D_IF {

    @Override
    public RecordType getRecordType() {
        return RecordType.OASIS_D;
    }
    
    /**
     * Displays the HH-PPS values used for OASIS D 2.30
     * 
     * @return non-null String
     */
    @Override
    public String toString() {

        final StringBuilder buffer = new StringBuilder(2000);

        buffer.append("M0030_START_CARE_DT=");
        OasisCalendarFormatter.format(getSTART_CARE_DT(), buffer);

        buffer.append(",M0090_INFO_COMPLETED_DT=");
        OasisCalendarFormatter.format(getINFO_COMPLETED_DT(), buffer);

        buffer.append(",M0100_ASSMT_REASON=");
        buffer.append(getASSMT_REASON());
        buffer.append(",M1021_PRIMARY_DIAG_ICD=");
        buffer.append(getPRIMARY_DIAG_ICD());
        buffer.append(",M1023_OTH_DIAG1_ICD=");
        buffer.append(getOTH_DIAG1_ICD());
        buffer.append(",M1023_OTH_DIAG2_ICD=");
        buffer.append(getOTH_DIAG2_ICD());
        buffer.append(",M1023_OTH_DIAG3_ICD=");
        buffer.append(getOTH_DIAG3_ICD());
        buffer.append(",M1023_OTH_DIAG4_ICD=");
        buffer.append(getOTH_DIAG4_ICD());
        buffer.append(",M1023_OTH_DIAG5_ICD=");
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
        buffer.append(",M1242_PAIN_FREQ_ACTVTY_MVMT=");
        buffer.append(getPAIN_FREQ_ACTVTY_MVMT());
        buffer.append(",M1306_UNHLD_STG2_PRSR_ULCR=");
        buffer.append(getUNHEALED_STAGE2_PRESSURE_ULCER());
        buffer.append(",M1322_NBR_PRSULC_STG1=");   // fix per CMS
        buffer.append(getNBR_PRSULC_STG1());
        buffer.append(",M1311_NBR_PRSULC_STG2_A1="); // change for c2 v6117
        buffer.append(getNBR_PRSULC_STG2());
        buffer.append(",M1311_NBR_PRSULC_STG3_B1="); // change for c2 v6117
        buffer.append(getNBR_PRSULC_STG3());
        buffer.append(",M1311_NBR_PRSULC_STG4_C1="); // change for c2 v6117
        buffer.append(getNBR_PRSULC_STG4());
        buffer.append(",M1324_STG_PRBLM_ULCER=");
        buffer.append(getSTG_PRBLM_ULCER());
        buffer.append(",M1330_STAS_ULCR_PRSNT=");
        buffer.append(getSTAS_ULCR_PRSNT());
        buffer.append(",M1332_NBR_STAS_ULCR=");
        buffer.append(getNBR_STAS_ULCR());
        buffer.append(",M1334_STUS_PRBLM_STAS_ULCR=");
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
        buffer.append(",M1810_CRNT_DRESS_UPPER=");
        buffer.append(getCRNT_DRESS_UPPER());
        buffer.append(",M1820_CRNT_DRESS_LOWER=");
        buffer.append(getCRNT_DRESS_LOWER());
        buffer.append(",M1830_CRNT_BATHING=");
        buffer.append(getCRNT_BATHG());
        buffer.append(",M1840_CRNT_TOILETING=");
        buffer.append(getCRNT_TOILTG());
        buffer.append(",M1850_CRNT_TRANSFERRING=");
        buffer.append(getCRNT_TRNSFRNG());
        buffer.append(",M1860_CRNT_AMBULATION=");
        buffer.append(getCRNT_AMBLTN());
        buffer.append(",M2030_CRNT_MGMT_INJCTN_MDCTN=");
        buffer.append(getCRNT_MGMT_INJCTN_MDCTN());
        buffer.append(",M0110_EPISODE_TIMING=");
        buffer.append(getEPISODE_TIMING());
        buffer.append(",M1311_NSTG_DRSG_D1="); // change for c2 v6117
        buffer.append(getNSTG_DRSG());
        buffer.append(",M1311_NSTG_CVRG_E1="); // change for c2 v6117
        buffer.append(getNSTG_CVRG());
        buffer.append(",M1311_NSTG_DEEP_TSUE_F1="); // change for c2 v6117
        buffer.append(getNSTG_DEEP_TISUE());
        buffer.append(",M2001_DRUG_RGMN_RVW="); // change for c2 v6117
        buffer.append(getDRUG_RGMN_RVW());        
        buffer.append(",M2200_THER_NEED_NUM=");
        buffer.append(getTHER_NEED_NBR());
        buffer.append(",M2200_THER_NEED_NA=");
        buffer.append(getTHER_NEED_NA());

        return buffer.toString();
    }

}
