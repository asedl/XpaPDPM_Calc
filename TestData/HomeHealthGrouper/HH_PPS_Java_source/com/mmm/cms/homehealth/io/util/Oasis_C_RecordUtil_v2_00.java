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
import com.mmm.cms.homehealth.io.record.Oasis_C_Record;
import static com.mmm.cms.homehealth.io.HomeHealthRecordUtil.formatDiagnosisCodeI9;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.util.OasisCalendarFormatter;
import java.text.ParseException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class Oasis_C_RecordUtil_v2_00
        extends AbstractRecordConverter
        implements OasisRecordConverterIF {

    public Oasis_C_RecordUtil_v2_00() {
        super("20100101", "20141231", 1446, "C-");
    }

    @Override
    protected String getRecordDate(String record) {
        return record.substring(301, 309);
    }

    @Override
    protected String getVersionCD(String record) {
        return record.substring(22, 24);
    }

    @Override
    public StringBuilder convertFromHomeHealthRec(
            HomeHealthRecordIF homeHealthRecord) {
        return convertFromHomeHealthRecDelimeted((HomeHealthRecord_C_IF) homeHealthRecord, "");
    }

    /**
     * this casts the object to a HomeHealthRecord_C_IF object
     * 
     * @param homeHealthRecord
     * @param delimiter
     * @return non-null StringBuilder, but may be empty
     */
    public StringBuilder convertFromHomeHealthRecDelimeted(
            HomeHealthRecordIF homeHealthRecord,
            String delimiter) {

        return convertFromHomeHealthRecDelimeted(
                (HomeHealthRecord_C_IF) homeHealthRecord,
                delimiter);
    }

    /**
     * converts HomeHealthRecord_C_IF to flat file format with a delimiter 
     * between values
     * 
     * @param homeHealthRecord
     * @param delimiter - if null, then the values will run together
     * @return non-null StringBuilder, but may be empty
     */
    public StringBuilder convertFromHomeHealthRecDelimeted(
            HomeHealthRecord_C_IF homeHealthRecord,
            String delimiter) {
        StringBuilder buffer = new StringBuilder(1450);

        if (delimiter == null) {
            delimiter = "";
        }
        buffer.append("B1"); // rec id
        buffer.append(delimiter);
        buffer.append("  "); // rec_type
        buffer.append(delimiter);
        buffer.append("        "); // lock_date
        buffer.append(delimiter);
        buffer.append("00");  // correction num
        buffer.append(delimiter);
        buffer.append("        ");  // acy_doc_cd
        buffer.append(delimiter);
        buffer.append("C-072009    "); // version_cd1
        buffer.append(delimiter);
        buffer.append("02.00");  // version_cd2
        buffer.append(delimiter);
        buffer.append("         "); // SFTW_ID
        buffer.append(delimiter);
        buffer.append("     "); // SFT_VER
        buffer.append(delimiter);
        buffer.append("                "); //  hha_agency_id
        buffer.append(delimiter);
        buffer.append("              "); // pat_id
        buffer.append(delimiter);
        buffer.append("  "); // st_code
        buffer.append(delimiter);
        buffer.append("    "); // st_err_cnt
        buffer.append(delimiter);
        buffer.append(" "); // st_cor
        buffer.append(delimiter);
        buffer.append(" "); // st_pmt_cor
        buffer.append(delimiter);
        buffer.append(" "); //  st_key_cor
        buffer.append(delimiter);
        buffer.append(" "); // st_delete
        buffer.append(delimiter);
        buffer.append(" "); // mc_cor
        buffer.append(delimiter);
        buffer.append(" "); // mc_pmt_cor
        buffer.append(delimiter);
        buffer.append(" "); // mc_key_cor
        buffer.append(delimiter);
        buffer.append("                    "); // mask_version_cd
        buffer.append(delimiter);
        buffer.append("       "); // cnt_filler
        buffer.append(delimiter);
        buffer.append("      "); // m0010_ccn
        buffer.append(delimiter);
        buffer.append("               "); // item_filler12
        buffer.append(delimiter);
        buffer.append("  "); // m0014_branch_state
        buffer.append(delimiter);
        buffer.append("          "); // m0016_branch_id
        buffer.append(delimiter);
        buffer.append("                    "); // m0020_pat_id
        buffer.append(delimiter);
        OasisCalendarFormatter.format(homeHealthRecord.getSTART_CARE_DT(), buffer);  // m0030_start_care_dt
        buffer.append(delimiter);
        buffer.append("        "); // m0032_roc_dt
        buffer.append(delimiter);
        buffer.append(" "); // m0032_roc_dt_na
        buffer.append(delimiter);
        buffer.append("            "); // m0040_pat_fname
        buffer.append(delimiter);
        buffer.append(" "); // m0040_pat_mi
        buffer.append(delimiter);
        buffer.append("                  "); // m0040_pat_lname
        buffer.append(delimiter);
        buffer.append("   "); // m0040_pat_sufix
        buffer.append(delimiter);
        buffer.append("  "); // m0060_pat_st
        buffer.append(delimiter);
        buffer.append("           "); // m0060_pat_zip
        buffer.append(delimiter);
        buffer.append("            "); // m0063_medicare_num
        buffer.append(delimiter);
        buffer.append(" "); // m0063_medicare_na
        buffer.append(delimiter);
        buffer.append("         "); // m0064_ssn
        buffer.append(delimiter);
        buffer.append(" "); // m0064_ssn_uk
        buffer.append(delimiter);
        buffer.append("              "); // m0065_medicaid_num
        buffer.append(delimiter);
        buffer.append(" "); // m0065_medicaid_na
        buffer.append(delimiter);
        buffer.append("        "); // m0066_pat_birth_dt
        buffer.append(delimiter);
        buffer.append(" "); // item_filler1
        buffer.append(delimiter);
        buffer.append(" "); // m0069_pat_gender
        buffer.append(delimiter);
        buffer.append("          "); // m0018_physician_id
        buffer.append(delimiter);
        buffer.append(" "); // m0018_physician_uk
        buffer.append(delimiter);
        buffer.append("  "); // m0080_assessor_discipline
        buffer.append(delimiter);
        OasisCalendarFormatter.format(homeHealthRecord.getINFO_COMPLETED_DT(), buffer); // m0090_info_completed_dt
        buffer.append(delimiter);
        buffer.append(homeHealthRecord.getASSMT_REASON()); // m0100_assmt_reason
        buffer.append(delimiter);
        buffer.append(" "); // m0140_ethnic_ai_an
        buffer.append(delimiter);
        buffer.append(" "); // m0140_ethnic_asian
        buffer.append(delimiter);
        buffer.append(" "); // m0140_ethnic_black
        buffer.append(delimiter);
        buffer.append(" "); // m0140_ethnic_hisp
        buffer.append(delimiter);
        buffer.append(" "); // m0140_ethnic_nh_pi
        buffer.append(delimiter);
        buffer.append(" "); // m0140_ethnic_white
        buffer.append(delimiter);
        buffer.append(" "); // item_filler13
        buffer.append(delimiter);
        buffer.append(" "); // m0150_cpay_none
        buffer.append(delimiter);
        buffer.append(" "); // m0150_cpay_mcare_ffs
        buffer.append(delimiter);
        buffer.append(" "); // m0150_cpay_mcare_hmo
        buffer.append(delimiter);
        buffer.append(" "); // m0150_cpay_mcaid_ffs
        buffer.append(delimiter);
        buffer.append(" "); // m0150_cpay_mcaid_hmo
        buffer.append(delimiter);
        buffer.append(" "); // mo150_cpay_wrkcomp
        buffer.append(delimiter);
        buffer.append(" "); // m0150_cpay_titlegms
        buffer.append(delimiter);
        buffer.append(" "); // m0150_cpay_oth_govt
        buffer.append(delimiter);
        buffer.append(" "); // m0150_cpay_priv_ins
        buffer.append(delimiter);
        buffer.append(" "); // m0105_cpay_priv_hmo
        buffer.append(delimiter);
        buffer.append(" "); // m0150_cpay_selfpay
        buffer.append(delimiter);
        buffer.append(" "); // m0105_cpay_other
        buffer.append(delimiter);
        buffer.append(" "); // m0150_cpay_uk
        buffer.append(delimiter);
        buffer.append("      "); // item_filler2
        buffer.append(delimiter);
        buffer.append("     "); // item_filler3
        buffer.append(delimiter);
        buffer.append("        "); // m1005_inp_discharge_dt
        buffer.append(delimiter);
        buffer.append(" "); // m1005_inp_dschg_unknown
        buffer.append(delimiter);
        buffer.append("       "); // m1010_14_day_inp1_icd
        buffer.append(delimiter);
        buffer.append("       "); // m1010_14_day_inp2_icd
        buffer.append(delimiter);
        buffer.append(" "); // item_filler14
        buffer.append(delimiter);
        buffer.append("       "); // m1016_chgreg_icd1
        buffer.append(delimiter);
        buffer.append("       "); // m1016_chgreg_icd2
        buffer.append(delimiter);
        buffer.append("       "); // m1016_chgreg_icd3
        buffer.append(delimiter);
        buffer.append("       "); // m1016_chgreg_icd4
        buffer.append(delimiter);
        buffer.append(" "); // m1018_prior_ur_incon
        buffer.append(delimiter);
        buffer.append(" "); // m1018_prior_cath
        buffer.append(delimiter);
        buffer.append(" "); // m1018_prior_intract_pain
        buffer.append(delimiter);
        buffer.append(" "); // m1018_prior_impr_decsn
        buffer.append(delimiter);
        buffer.append(" "); // m1018_prior_distruptive
        buffer.append(delimiter);
        buffer.append(" "); // m1018_prior_mem_loss
        buffer.append(delimiter);
        buffer.append(" "); // m1018_prior_none
        buffer.append(delimiter);
        buffer.append(" "); // m1018_prior_nochr_14d
        buffer.append(delimiter);
        buffer.append(" "); // m1018_prior_unknown
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPRIMARY_DIAG_ICD())); // m1020_primary_diag_icd
        buffer.append(delimiter);
        buffer.append("  "); // m1020_primary_diag_severity
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getOTH_DIAG1_ICD())); // m1022_oth_diag1_icd
        buffer.append(delimiter);
        buffer.append("  "); // m1022_oth_diag1_severity
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getOTH_DIAG2_ICD())); // m1022_oth_diag2_icd
        buffer.append(delimiter);
        buffer.append("  "); // m1022_oth_diag2_severity
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getOTH_DIAG3_ICD())); // m1022_oth_diag3_icd
        buffer.append(delimiter);
        buffer.append("  "); // m1022_oth_diag3_severity
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getOTH_DIAG4_ICD())); // m1022_oth_diag4_icd
        buffer.append(delimiter);
        buffer.append("  "); // m1022_oth_diag4_severity
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getOTH_DIAG5_ICD())); // m1022_oth_diag5_icd
        buffer.append(delimiter);
        buffer.append("  "); // m1022_oth_diag5_severity
        buffer.append(delimiter);
        buffer.append(homeHealthRecord.getTHH_IV_INFUSION()); // m1030_thh_iv_infusion
        buffer.append(delimiter);
        buffer.append(homeHealthRecord.getTHH_PAR_NUTRITION()); // m1030_thh_par_nutrition
        buffer.append(delimiter);
        buffer.append(homeHealthRecord.getTHH_ENT_NUTRITION()); // m1030_thh_ent_nutrition
        buffer.append(delimiter);
        buffer.append(homeHealthRecord.getTHH_NONE_ABOVE()); // m1030_thh_none_above
        buffer.append(delimiter);
        buffer.append("      "); // item_filler15
        buffer.append(delimiter);
        buffer.append(" "); // m1036_rsk_smoking
        buffer.append(delimiter);
        buffer.append(" "); // m1036_risk_obesity
        buffer.append(delimiter);
        buffer.append(" "); // m1036_rsk_alcholism
        buffer.append(delimiter);
        buffer.append(" "); // m1036_rsk_drugs
        buffer.append(delimiter);
        buffer.append(" "); // m1036_rsk_none
        buffer.append(delimiter);
        buffer.append(" "); // m1036_rsk_unknown
        buffer.append(delimiter);
        buffer.append("  "); // item_filler16
        buffer.append(delimiter);
        buffer.append("     "); // item_filler4
        buffer.append(delimiter);
        buffer.append("            "); // item_filler5
        buffer.append(delimiter);
        buffer.append("             "); // item_filler6
        buffer.append(delimiter);
        buffer.append("                       "); // item_filler17
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getVISION(), 2, '0'));
        buffer.append(delimiter);
        buffer.append("  "); // item_filler18
        buffer.append(delimiter);
        buffer.append("  "); // m1230_speech
        buffer.append(delimiter);
        buffer.append("     "); // item_filler19
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNBR_PRSULC_STG1(), 2, '0')); //  m1322_nbr_prsulc_stg1
        buffer.append(delimiter);
        buffer.append("       "); // item_filler20
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getSTG_PRBLM_ULCER(), 2, '0')); //  m1324_stg_prblm_ulcer
        buffer.append(delimiter);
        buffer.append("              "); // item_filler22
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getWHEN_DYSPNEIC(), 2, '0')); // m1400_when_dyspneic
        buffer.append(delimiter);
        buffer.append(" "); // m1410_resptx_oxygen
        buffer.append(delimiter);
        buffer.append(" "); // m1410_resptx_ventilator
        buffer.append(delimiter);
        buffer.append(" "); // m1410_resptx_airpress
        buffer.append(delimiter);
        buffer.append(" "); // m1410_resptx_none
        buffer.append(delimiter);
        buffer.append("  "); // m1600_uti
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getUR_INCONT(), 2, '0')); // m1610_ur_incont
        buffer.append(delimiter);
        buffer.append("  "); // item_filler21
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getBWL_INCONT(), 2, '0')); // m1610_bwl_incont
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getOSTOMY(), 2, '0')); // m1610_ostomy
        buffer.append(delimiter);
        buffer.append("  "); // m1700_cog_function
        buffer.append(delimiter);
        buffer.append("  "); // m1710_when_confused
        buffer.append(delimiter);
        buffer.append("  "); // m1720_when_anxious
        buffer.append(delimiter);
        buffer.append("      "); // item_filler23
        buffer.append(delimiter);
        buffer.append("       "); // item_filler7
        buffer.append(delimiter);
        buffer.append(" "); // m1740_bd_mem_deficit
        buffer.append(delimiter);
        buffer.append(" "); // m1740_bd_imp_decisn
        buffer.append(delimiter);
        buffer.append(" "); // m1740_bd_verbal
        buffer.append(delimiter);
        buffer.append(" "); // m1740_bd_physical
        buffer.append(delimiter);
        buffer.append(" "); // m1740_bd_soc_inappro
        buffer.append(delimiter);
        buffer.append(" "); // m1740_bd_delusions
        buffer.append(delimiter);
        buffer.append(" "); // m1740_bd_none
        buffer.append(delimiter);
        buffer.append("  "); // m1745_beh_prob_freq
        buffer.append(delimiter);
        buffer.append(" "); // m1750_rec_psych_nurs
        buffer.append(delimiter);
        buffer.append("  "); // item_filler24
        buffer.append(delimiter);
        buffer.append("  "); // m1800_cur_grooming
        buffer.append(delimiter);
        buffer.append("  "); // item_filler25
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getCRNT_DRESS_UPPER(), 2, '0'));  // m181_cur_dress_upper
        buffer.append(delimiter);
        buffer.append("  "); // item_filler26
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getCRNT_DRESS_LOWER(), 2, '0'));  // m181_cur_dress_upper
        buffer.append(delimiter);
        buffer.append("                  "); // item_filler27
        buffer.append(delimiter);
        buffer.append("  "); // m1870_cur_feeding
        buffer.append(delimiter);
        buffer.append("  "); // item_filler28
        buffer.append(delimiter);
        buffer.append("  "); // m1880_cur_prep_lt_meals
        buffer.append(delimiter);
        buffer.append("                  "); // item_filler29
        buffer.append(delimiter);
        buffer.append("  "); // m180_cur_phone_use
        buffer.append(delimiter);
        buffer.append("                     "); // item_filler30
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_medication
        buffer.append(delimiter);
        buffer.append("     "); // item_filler31
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_hypoglyc
        buffer.append(delimiter);
        buffer.append("  "); // item_filler32
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_unknown
        buffer.append(delimiter);
        buffer.append("  "); // m2410_inpat_facility
        buffer.append(delimiter);
        buffer.append("       "); // item_filler33
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_med
        buffer.append(delimiter);
        buffer.append("   "); // item_filler34
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_hypoglyc
        buffer.append(delimiter);
        buffer.append("     "); // item_filler35
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_ur_tract
        buffer.append(delimiter);
        buffer.append(" "); // item_filler36
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_dvt_pulmnry
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_pain
        buffer.append(delimiter);
        buffer.append("  "); // item_filler37
        buffer.append(delimiter);
        buffer.append(" "); // m2440_nh_therapy
        buffer.append(delimiter);
        buffer.append(" "); // m2440_nh_respite
        buffer.append(delimiter);
        buffer.append(" "); // m2440_nh_hospice
        buffer.append(delimiter);
        buffer.append(" "); // m2440_nh_permanent
        buffer.append(delimiter);
        buffer.append(" "); // m2440_nh_unsafe_home
        buffer.append(delimiter);
        buffer.append(" "); // m2440_nh_other
        buffer.append(delimiter);
        buffer.append(" "); // m2440_nh_unknown
        buffer.append(delimiter);
        buffer.append("        "); // m0903_last_home_visit
        buffer.append(delimiter);
        buffer.append("        "); // m0906_dc_tran_dth_dt
        buffer.append(delimiter);
        buffer.append("  "); // item_filler8
        buffer.append(delimiter);
        buffer.append("  "); // item_filler9
        buffer.append(delimiter);
        buffer.append("  "); // item_filler38
        buffer.append(delimiter);
        buffer.append(" "); // m1000_dc_snf_14_da
        buffer.append(delimiter);
        buffer.append("  "); // item_filler39
        buffer.append(delimiter);
        buffer.append(" "); // m1000_dc_none_14_da
        buffer.append(delimiter);
        buffer.append("              "); // item_filler10
        buffer.append(delimiter);
        buffer.append("          "); // natl_prov_id
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getEPISODE_TIMING(), 2, '0')); // m0110_episode_timing
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_A3())); // m1024_pmt_diag_icd_a3
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_B3())); // m1024_pmt_diag_icd_b3
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_C3())); // m1024_pmt_diag_icd_c3
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_D3())); // m1024_pmt_diag_icd_d3
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_E3())); // m1024_pmt_diag_icd_e3
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_F3())); // m1024_pmt_diag_icd_f3
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_A4())); // m1024_pmt_diag_icd_a4
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_B4())); // m1024_pmt_diag_icd_b4
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_C4())); // m1024_pmt_diag_icd_c4
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_D4())); // m1024_pmt_diag_icd_d4
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_E4())); // m1024_pmt_diag_icd_e4
        buffer.append(delimiter);
        buffer.append(formatDiagnosisCodeI9(homeHealthRecord.getPMT_DIAG_ICD_F4())); // m1024_pmt_diag_icd_f4
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(Integer.toString(homeHealthRecord.getTHER_NEED_NBR()), 3, '0')); // m2200_ther_need_num
        buffer.append(delimiter);
        buffer.append(homeHealthRecord.getTHER_NEED_NA()); // m2200_ther_need_na
        buffer.append(delimiter);
        buffer.append(" "); // item_filler40
        buffer.append(delimiter);
        buffer.append("        "); // m0102_physn_ordrd_socroc_dt
        buffer.append(delimiter);
        buffer.append(" "); // m0102_physn_ordrd_socroc_dt_na
        buffer.append(delimiter);
        buffer.append("        "); // m0104_physn_rfrl_dt
        buffer.append(delimiter);
        buffer.append(" "); // m1000_dc_ltc_14_da
        buffer.append(delimiter);
        buffer.append(" "); // m1000_dc_ipps_14_da
        buffer.append(delimiter);
        buffer.append(" "); // m1000_dc_ltch_14_da
        buffer.append(delimiter);
        buffer.append(" "); // m1000_dc_irf_14_da
        buffer.append(delimiter);
        buffer.append(" "); // m1000_dc_physn_14_da
        buffer.append(delimiter);
        buffer.append(" "); // m1000_dc_oth_14_da
        buffer.append(delimiter);
        buffer.append("       "); // m1010_day_inp3_icd
        buffer.append(delimiter);
        buffer.append("       "); // m1010_day_inp4_icd
        buffer.append(delimiter);
        buffer.append("       "); // m1010_day_inp5_icd
        buffer.append(delimiter);
        buffer.append("       "); // m1010_day_inp6_icd
        buffer.append(delimiter);
        buffer.append("       "); // m1012_inp_prcdr1_icd
        buffer.append(delimiter);
        buffer.append("       "); // m1012_inp_prcdr2_icd
        buffer.append(delimiter);
        buffer.append("       "); // m1012_inp_prcdr3_icd
        buffer.append(delimiter);
        buffer.append("       "); // m1012_inp_prcdr4_icd
        buffer.append(delimiter);
        buffer.append(" "); // m1012_inp_na_icd
        buffer.append(delimiter);
        buffer.append(" "); //m1012_inp_uk_icd
        buffer.append(delimiter);
        buffer.append("       "); // m1016_chgreg_icd5
        buffer.append(delimiter);
        buffer.append("       "); // m1016_chgreg_icd5
        buffer.append(delimiter);
        buffer.append(" "); // m1016_chgreg_icd_na
        buffer.append(delimiter);
        buffer.append(" "); // m1032_hosp_risk_rcnt_dcln
        buffer.append(delimiter);
        buffer.append(" "); // m1032_hosp_rsk_mltpl_hospztn
        buffer.append(delimiter);
        buffer.append(" "); // m1032_hosp_risk_hstry_falls
        buffer.append(delimiter);
        buffer.append(" "); // m1032_hosp_risk_5plus_mdctn
        buffer.append(delimiter);
        buffer.append(" "); // m1032_hosp_risk_frailty
        buffer.append(delimiter);
        buffer.append(" "); // m1032_hosp_risk_othr
        buffer.append(delimiter);
        buffer.append(" "); // m1032_hosp_risk_none_above
        buffer.append(delimiter);
        buffer.append("  "); // m1034_ptnt_ovral_stus
        buffer.append(delimiter);
        buffer.append("  "); // m1040_inflnz_rcvd_agncy
        buffer.append(delimiter);
        buffer.append("  "); // m1045_inflnz_rsn_not_rcvd
        buffer.append(delimiter);
        buffer.append(" "); // m1050_ppv_rcvd_agncy
        buffer.append(delimiter);
        buffer.append("  "); // m1055_ppv_rsn_not_rcvd_agncy
        buffer.append(delimiter);
        buffer.append("  "); // m1100_ptnt_lvg_stutn
        buffer.append(delimiter);
        buffer.append("  "); // m1210_hearg_ablty
        buffer.append(delimiter);
        buffer.append("  "); // m1220_undrstg_verbal_cntnt
        buffer.append(delimiter);
        buffer.append("  "); // m1240_frml_pain_asmt
        buffer.append(delimiter);
        buffer.append("  "); // m1242_pain_frew_actvty_mvmt
        buffer.append(delimiter);
        buffer.append("  "); // m1300_prsr_ulcr_risk_asmt
        buffer.append(delimiter);
        buffer.append(" "); // m1302_risk_of_prsr_ulcr
        buffer.append(delimiter);
        buffer.append(homeHealthRecord.getUNHLD_STG2_PRSR_ULCR());  // m1306_unhld_stg2_prsr_ulcr
        buffer.append(delimiter);
        buffer.append("        "); // m1307_oldst_stg_onst_dt
        buffer.append(delimiter);
        buffer.append("  "); // m1307_oldt_stg2_at_dschrg
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNBR_PRSULC_STG2(), 2, '0')); // m1308_nbr_prsulc_stg2
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNBR_STG2_AT_SOC_ROC(), 2, '0')); // m1308_nbr_stg2_at_soc_roc
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNBR_PRSULC_STG3(), 2, '0')); // m1308_nbr_prsulc_stg3
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNBR_STG3_AT_SOC_ROC(), 2, '0')); // m1308_nbr_stg3_at_soc_roc
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNBR_PRSULC_STG4(), 2, '0')); // m1308_nbr_prsulc_stg4
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNBR_STG4_AT_SOC_ROC(), 2, '0')); // m1308_nbr_stg4_at_soc_roc
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNSTG_DRSG(), 2, '0')); // m1308_nstg_drsg
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNSTG_DRSG_SOC_ROC(), 2, '0')); // m1308_nstg_drsg_soc_roc
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNSTG_CVRG(), 2, '0')); // m1308_nstg_cvrg
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNSTG_CVRG_SOC_ROC(), 2, '0')); // m1308_nstg_cvrg_soc_roc
       buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNSTG_DEEP_TISUE(), 2, '0')); // m1308_nstg_deep_tissue
         buffer.append(delimiter);
       buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNSTG_DEEP_TISSUE_SOC_ROC(), 2, '0')); // m1308_nstg_deep_tissue_soc_roc
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getPRSR_ULCR_LNGTH(), 4, '0')); // m1310_prsr_ulcr_lngth
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getPRSR_ULCR_WDTH(), 4, '0')); // m1308_prsr_ulcr_wdth
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getPRSR_ULCR_DEPTH(), 4, '0')); // m1308_prsr_ulcr_depth
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getSTUS_PRBLM_PRSR_ULCR(), 2, '0')); // m1320_stus_prblm_prsr_ulcr
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getSTAS_ULCR_PRSNT(), 2, '0')); // m1330_stat_ulcr_prsnt
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getNBR_STAS_ULCR(), 2, '0')); // m1332_num_stas_ulcr
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getSTUS_PRBLM_STAS_ULCR(), 2, '0')); // m1334_stus_prbl_stas_ulcr
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getSRGCL_WND_PRSNT(), 2, '0')); // m1340_srgcl_wnd_prsnt
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getSTUS_PRBLM_SRGCL_WND(), 2, '0')); // m1342_stus_prblm_srgcl_wnd
        buffer.append(delimiter);
        buffer.append(homeHealthRecord.getLESION_OPEN_WND()); // m1350_lesion_open_wnd
        buffer.append(delimiter);
        buffer.append("  "); // m1500_symtm_hrt_failr_prnts
        buffer.append(delimiter);
        buffer.append(" "); // m1510_hrt_failr_no_actn
        buffer.append(delimiter);
        buffer.append(" "); // m1510_hrt_failr_physn_cntct
        buffer.append(delimiter);
        buffer.append(" "); // m1510_hrt_failr_er_trtmt
        buffer.append(delimiter);
        buffer.append(" "); // m1510_hrt_failr_physn_trtmt
        buffer.append(delimiter);
        buffer.append(" "); // m1510_hrt_failr_clncl_intrvnt
        buffer.append(delimiter);
        buffer.append(" "); // m1510_hrt_failr_care_plan_chg
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getINCNTNT_TIMING(), 2, '0')); // m1615_incntnt_timing
        buffer.append(delimiter);
        buffer.append("  "); // m1730_stdz_dprsn_scrng
        buffer.append(delimiter);
        buffer.append("  "); // m1730_phq2_lack_intrst
        buffer.append(delimiter);
        buffer.append("  "); // m1730_phq2_dprsn
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getCRNT_BATHG(), 2, '0')); // m1830_crnt_bathg
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getCRNT_TOILTG(), 2, '0')); // m1840_crnt_toiltg
        buffer.append(delimiter);
        buffer.append("  "); // m1845_cur_toiltg_hygn
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getCRNT_TRNSFRNG(), 2, '0')); // m1850_crnt_trnsfrng
        buffer.append(delimiter);
        buffer.append(HomeHealthRecordUtil.justifyRight(homeHealthRecord.getCRNT_AMBLTN(), 2, '0')); // m1860_crnt_amltn
        buffer.append(delimiter);
        buffer.append("     "); // subm_hipps_code
        buffer.append(delimiter);
        buffer.append("     "); // item_filler11
        buffer.append(delimiter);
        buffer.append("     "); // subm_hipps_version
        buffer.append(delimiter);
        buffer.append("  "); // m1900_prior_adliadl_self
        buffer.append(delimiter);
        buffer.append("  "); // m1900_prior_adliadl_ambltn
        buffer.append(delimiter);
        buffer.append("  "); // m1900_prior_adliadl_trnsfr
        buffer.append(delimiter);
        buffer.append("  "); // m1900_prior_adliadl_hsehold
        buffer.append(delimiter);
        buffer.append("  "); // m1910_mlt_fctr_fall_risk_asmt
        buffer.append(delimiter);
        buffer.append("  "); // m2000_drgu_rgmn_rvw
        buffer.append(delimiter);
        buffer.append(" "); // m2002_mdctn_flwp
        buffer.append(delimiter);
        buffer.append("  "); // m2004_mdctn_intrvtn
        buffer.append(delimiter);
        buffer.append("  "); // m2010_high_risk_drug_edctn
        buffer.append(delimiter);
        buffer.append("  "); // m2015_drug_edctn_intrvtn
        buffer.append(delimiter);
        buffer.append("  "); // m2020_crnt_mgmt_oral_mdctn
        buffer.append(delimiter);
        buffer.append("  "); // m2030_crnt_mgmt_injctn_mdctn
        buffer.append(delimiter);
        buffer.append("  "); // m2040_prior_mgmt_oral_mdctn
        buffer.append(delimiter);
        buffer.append("  "); // m2040_prior_mgmt_injctn_mdctn
        buffer.append(delimiter);
        buffer.append("  "); // m2100_care_type_src_adl
        buffer.append(delimiter);
        buffer.append("  "); // m2100_care_type_src_iadl
        buffer.append(delimiter);
        buffer.append("  "); // m2100_care_type_src_mdctn
        buffer.append(delimiter);
        buffer.append("  "); // m2100_care_type_scr_prcdr
        buffer.append(delimiter);
        buffer.append("  "); // m2100_care_type_src_equip
        buffer.append(delimiter);
        buffer.append("  "); // m2100_care_type_src_sprvsn
        buffer.append(delimiter);
        buffer.append("  "); // m2100_care_type_src_advcy
        buffer.append(delimiter);
        buffer.append("  "); // m2110_adl_iadl_astnc_freq
        buffer.append(delimiter);
        buffer.append("  "); // m2250_plan_smry_ptnt_specf
        buffer.append(delimiter);
        buffer.append("  "); // m2250_plan_smry_dbts_ft_care
        buffer.append(delimiter);
        buffer.append("  "); // m2250_plan_smry_fall_prvnt
        buffer.append(delimiter);
        buffer.append("  "); // m2250_plan_smry_dprsn_intrvtn
        buffer.append(delimiter);
        buffer.append("  "); // m2250_plan_smry_pain_intrvtn
        buffer.append(delimiter);
        buffer.append("  "); // m2250_plan_smry_prsulc_prvnt
        buffer.append(delimiter);
        buffer.append("  "); // m2250_plan_smry_prsulc_trtmt
        buffer.append(delimiter);
        buffer.append("  "); // m2300_emer_us_after_last_asmt
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_injry_by_fall
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_rsprtry_infctn
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_rsprtry_othr
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_hrt_failr
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_crdc_dsrthm
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_mi_crst_pain
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_othr_hrt_dease
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_stroke_tia
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_gi_prblm
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_dhydrtn_malntr
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_uti
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_cthtr_cmplctn
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_wnd_infctn_dtrortn
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_uncntld_pain
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_mentl_bhvrl_prblm
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_dvt_pulmnry
        buffer.append(delimiter);
        buffer.append(" "); // m2310_ecr_other
        buffer.append(delimiter);
        buffer.append("  "); // m2400_intrvtn_smry_dbts_ft
        buffer.append(delimiter);
        buffer.append("  "); // m2400_intrvnt_smry_fall_prvnt
        buffer.append(delimiter);
        buffer.append("  "); // m2400_intrvtn_smry_dpsrsn
        buffer.append(delimiter);
        buffer.append("  "); // m2400_intrvtn_smry_pain_mntr
        buffer.append(delimiter);
        buffer.append("  "); // m2400_intrvtn_smry_prsulc_prvn
        buffer.append(delimiter);
        buffer.append("  "); // m2400_intrvtn_smry_prsulc_wet
        buffer.append(delimiter);
        buffer.append("  "); // m2420_dscharg_disp
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_injry_by_fall
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_rsprtry_infctn
        buffer.append(delimiter);
        buffer.append(" "); // n2430_hosp_rsprtry_othr
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_hrt_failr
        buffer.append(delimiter);
        buffer.append(" "); // n2430_hosp_crdc_dsrthm
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_mi_chst_pain
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_othr_hrt_dease
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_stroke_tia
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_gi_prblm
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_dhydrtn_malntr
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_cthtr_cmplctn
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_wnd_infctn
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_mentl_bhvrl_prblm
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_schld_trtmt
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_other
        buffer.append(delimiter);
        buffer.append(" "); // m2430_hosp_uk
        buffer.append(delimiter);
        buffer.append("                                                                                                                                         "); // item_filler41
        buffer.append(delimiter);
        buffer.append("                                                    "); // cms_use1
        buffer.append(delimiter);
        buffer.append("      "); // item_filler2
        buffer.append(delimiter);
        buffer.append("                                              "); // cms_use2
        buffer.append(delimiter);
        buffer.append("   "); // item_filler3
        buffer.append(delimiter);
        buffer.append("%"); // date_end
        buffer.append('\r'); // CR
        buffer.append('\n'); // line feed

        return buffer;
    }

    /**
     * Converts an OASIS string record to an OASIS-C related Home Health record
     * using the generic Home Health Record interface. Since much of the OASIS
     * information is not needed for Home Health grouping, this method can
     * ignore them or store them with the record using the
     * <code>skipPassthru</code> parameter.
     *
     * @param strRecord
     * @param recNum
     * @param skipPassthru
     * @return
     * @throws java.text.ParseException
     */
    @Override
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord,
            int recNum, boolean skipPassthru) throws ParseException {
        final Logger logger = Logger.getLogger(getClass().getName());
        final Oasis_C_Record oasisRecord = new Oasis_C_Record();
        String tmpStr;
        Calendar calendar;

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

        //Format: YYYYMMDD
        try {
            calendar = OasisCalendarFormatter.parse(strRecord.substring(176, 184));
            oasisRecord.setSTART_CARE_DT(calendar);
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "HH-PPS: Record: {0}: Invalid Or missing M0030_START_CARE_DT of ''{1}''", new Object[]{recNum, strRecord.substring(176, 184)});
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "HH-PPS: Record: {0}: Invalid Or missing M0030_START_CARE_DT of ''{1}''", new Object[]{recNum, strRecord.substring(176, 184)});
        }

        //Format: YYYYMMDD
        try {
            calendar = OasisCalendarFormatter.parse(strRecord.substring(301, 309));
            oasisRecord.setINFO_COMPLETED_DT(calendar);
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "HH-PPS: Record: {0}: Invalid Or missing M0090_INFO_COMPLETED_DT of ''{1}''", new Object[]{recNum, strRecord.substring(301, 309)});
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "HH-PPS: Record: {0}: Invalid Or missing M0090_INFO_COMPLETED_DT of ''{1}''", new Object[]{recNum, strRecord.substring(301, 309)});
        }

        //Format: 99
        oasisRecord.setASSMT_REASON(strRecord.substring(309, 311).intern());

        //Format: XX99.XX
        tmpStr = strRecord.substring(403, 410);
        oasisRecord.setPRIMARY_DIAG_ICD(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: XX99.XX
        tmpStr = strRecord.substring(412, 419);
        oasisRecord.setOTH_DIAG1_ICD(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: XX99.XX
        tmpStr = strRecord.substring(421, 428);
        oasisRecord.setOTH_DIAG2_ICD(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: XX99.XX
        tmpStr = strRecord.substring(430, 437);
        oasisRecord.setOTH_DIAG3_ICD(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: XX99.XX
        tmpStr = strRecord.substring(439, 446);
        oasisRecord.setOTH_DIAG4_ICD(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: XX99.XX
        tmpStr = strRecord.substring(448, 455);
        oasisRecord.setOTH_DIAG5_ICD(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X
        oasisRecord.setTHH_IV_INFUSION(strRecord.substring(457, 458).intern());

        //Format: X
        oasisRecord.setTHH_PAR_NUTRITION(strRecord.substring(458, 459).intern());

        //Format: X
        oasisRecord.setTHH_ENT_NUTRITION(strRecord.substring(459, 460).intern());

        //Format: X
        oasisRecord.setTHH_NONE_ABOVE(strRecord.substring(460, 461).intern());

        // Format: XX
        oasisRecord.setVISION(strRecord.substring(528, 530).intern());

        // Format: XX
        oasisRecord.setNBR_PRSULC_STG1(strRecord.substring(539, 541).intern());

        //Format: XX
        oasisRecord.setSTG_PRBLM_ULCER(strRecord.substring(548, 550).intern());

        // Format: XX
        oasisRecord.setWHEN_DYSPNEIC(strRecord.substring(564, 566).intern());

        // Format: XX
        oasisRecord.setUR_INCONT(strRecord.substring(572, 574).intern());

        // Format: XX
        oasisRecord.setBWL_INCONT(strRecord.substring(576, 578).intern());

        // Format: XX
        oasisRecord.setOSTOMY(strRecord.substring(578, 580).intern());

        // Format: XX
        oasisRecord.setCRNT_DRESS_UPPER(strRecord.substring(615, 617).intern());

        // Format: XX
        oasisRecord.setCRNT_DRESS_LOWER(strRecord.substring(619, 621).intern());

        //Format: XX
        oasisRecord.setEPISODE_TIMING(strRecord.substring(778, 780).intern());

        //Format: X999.XX
        tmpStr = strRecord.substring(780, 787);
        oasisRecord.setPMT_DIAG_ICD_A3(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(787, 794);
        oasisRecord.setPMT_DIAG_ICD_B3(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(794, 801);
        oasisRecord.setPMT_DIAG_ICD_C3(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(801, 808);
        oasisRecord.setPMT_DIAG_ICD_D3(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(808, 815);
        oasisRecord.setPMT_DIAG_ICD_E3(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(815, 822);
        oasisRecord.setPMT_DIAG_ICD_F3(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(822, 829);
        oasisRecord.setPMT_DIAG_ICD_A4(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(829, 836);
        oasisRecord.setPMT_DIAG_ICD_B4(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(836, 843);
        oasisRecord.setPMT_DIAG_ICD_C4(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(843, 850);
        oasisRecord.setPMT_DIAG_ICD_D4(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(850, 857);
        oasisRecord.setPMT_DIAG_ICD_E4(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: X999.XX
        tmpStr = strRecord.substring(857, 864);
        oasisRecord.setPMT_DIAG_ICD_F4(HomeHealthRecordUtil.parseDxCode(tmpStr));

        //Format: 999
        // Therapy Need Number
        tmpStr = strRecord.substring(864, 867);
        try {
            if ("   ".equals(tmpStr)) {
                oasisRecord.setTHER_NEED_NBR(-1);
                logger.log(Level.SEVERE, "HH-PPS: Record: {0}: missing M2200_THER_NEED_NUM of ''  ''", recNum);

            } else {
                oasisRecord.setTHER_NEED_NBR(HomeHealthRecordUtil.parseTherapyNeedNumber_C1(tmpStr, 0));
            }
        } catch (NumberFormatException e) {
            oasisRecord.setTHER_NEED_NBR(-1);
            logger.log(Level.SEVERE, "HH-PPS: Record: {0}: Invalid Or missing M2200_THER_NEED_NUM of ''{1}''", new Object[]{recNum, tmpStr});
        }

        //Format: 9
        oasisRecord.setTHER_NEED_NA(strRecord.substring(867, 868).intern());

        // Format: XX
        oasisRecord.setPAIN_FREQ_ACTVTY_MVMT(strRecord.substring(989, 991).intern());

        // Format: X - used in the OASIS-B scoring modules - same as the
        oasisRecord.setUNHLD_STG2_PRSR_ULCR(strRecord.substring(994, 995).intern());

        // Format: XX
        oasisRecord.setNBR_PRSULC_STG2(strRecord.substring(1005, 1007).intern());

        // Format: XX
        oasisRecord.setNBR_STG2_AT_SOC_ROC(strRecord.substring(1007, 1009).intern());

        // Format: XX
        oasisRecord.setNBR_PRSULC_STG3(strRecord.substring(1009, 1011).intern());

        // Format: XX
        oasisRecord.setNBR_STG3_AT_SOC_ROC(strRecord.substring(1011, 1013).intern());

        // Format: XX
        oasisRecord.setNBR_PRSULC_STG4(strRecord.substring(1013, 1015).intern());

        // Format: XX
        oasisRecord.setNBR_STG4_AT_SOC_ROC(strRecord.substring(1015, 1017).intern());

        // Format: XX
        oasisRecord.setNSTG_DRSG(strRecord.substring(1017, 1019).intern());

        // Format: XX
        oasisRecord.setNSTG_DRSG_SOC_ROC(strRecord.substring(1019, 1021).intern());

        // Format: XX
        oasisRecord.setNSTG_CVRG(strRecord.substring(1021, 1023).intern());

        // Format: XX
        oasisRecord.setNSTG_CVRG_SOC_ROC(strRecord.substring(1023, 1025).intern());

        // Format: XX
        oasisRecord.setNSTG_DEEP_TISUE(strRecord.substring(1025, 1027).intern());

        // Format: XX
        oasisRecord.setNSTG_DEEP_TISSUE_SOC_ROC(strRecord.substring(1027, 1029).intern());

        // Head To Toe Length Of Stage III Or IV Pu With Largest Area - 4 1030 1033
        oasisRecord.setPRSR_ULCR_LNGTH(strRecord.substring(1029, 1033).intern());

        //Width At Right Angles Of Stage III Or IV Pu With Largest Area - 4 1034 1037
        oasisRecord.setPRSR_ULCR_WDTH(strRecord.substring(1033, 1037).intern());

        // Depth Of Stage III Or IV Pu With Largest Area - 4 1038 1041
        oasisRecord.setPRSR_ULCR_DEPTH(strRecord.substring(1037, 1041).intern());

        // Format: XX
        oasisRecord.setSTUS_PRBLM_PRSR_ULCR(strRecord.substring(1041, 1043).intern());

        //Format: X
        oasisRecord.setSTAS_ULCR_PRSNT(strRecord.substring(1043, 1045).intern());

        //Format: XX
        oasisRecord.setNBR_STAS_ULCR(strRecord.substring(1045, 1047).intern());

        //Format: XX
        oasisRecord.setSTUS_PRBLM_STAS_ULCR(strRecord.substring(1047, 1049).intern());

        //Format: X
        oasisRecord.setSRGCL_WND_PRSNT(strRecord.substring(1049, 1051).intern());

        //Format: XX
        oasisRecord.setSTUS_PRBLM_SRGCL_WND(strRecord.substring(1051, 1053).intern());

        /**
         * Format: X
         *
         * @deprecated - OASIS-C no longer used this variable for validation of
         * Ulcers
         */
        oasisRecord.setLESION_OPEN_WND(strRecord.substring(1053, 1054).intern());

        // Format: XX
        oasisRecord.setINCNTNT_TIMING(strRecord.substring(1062, 1064).intern());

        //Format: XX
        oasisRecord.setCRNT_BATHG(strRecord.substring(1070, 1072).intern());

        //Format: XX
        oasisRecord.setCRNT_TOILTG(strRecord.substring(1072, 1074).intern());

        //Format: XX
        oasisRecord.setCRNT_TRNSFRNG(strRecord.substring(1076, 1078).intern());

        //Format: XX
        oasisRecord.setCRNT_AMBLTN(strRecord.substring(1078, 1080).intern());

        //Format: XX
        oasisRecord.setCRNT_MGMT_INJCTN_MDCTN(strRecord.substring(1116, 1118).intern());

        //Format: XX
        oasisRecord.setDRUG_RGMN_RVW(strRecord.substring(1105, 1107).intern());

        return oasisRecord;
    }

//    @Override
//    public StringBuilder toHeaderOasisRecDelimeted(String delimiter) {
//        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED);
//    }
//
    public HomeHealthRecordIF convertToHomeHealthRec(
            String M0030_START_CARE_DT,
            String M0090_INFO_COMPLETED_DT,
            String M0100_ASSMT_REASON,
            String M0110_EPISODE_TIMING,
            String M1020_PRIMARY_DIAG_ICD,
            String M1022_OTH_DIAG1_ICD,
            String M1022_OTH_DIAG2_ICD,
            String M1022_OTH_DIAG3_ICD,
            String M1022_OTH_DIAG4_ICD,
            String M1022_OTH_DIAG5_ICD,
            String M1024_PMT_DIAG_ICD_A3,
            String M1024_PMT_DIAG_ICD_B3,
            String M1024_PMT_DIAG_ICD_C3,
            String M1024_PMT_DIAG_ICD_D3,
            String M1024_PMT_DIAG_ICD_E3,
            String M1024_PMT_DIAG_ICD_F3,
            String M1024_PMT_DIAG_ICD_A4,
            String M1024_PMT_DIAG_ICD_B4,
            String M1024_PMT_DIAG_ICD_C4,
            String M1024_PMT_DIAG_ICD_D4,
            String M1024_PMT_DIAG_ICD_E4,
            String M1024_PMT_DIAG_ICD_F4,
            String M1030_THH_IV_INFUSION,
            String M1030_THH_PAR_NUTRITION,
            String M1030_THH_ENT_NUTRITION,
            String M1030_THH_NONE_ABOVE,
            String M1200_VISION,
            String M1242_PAIN_FREQ_ACTVTY_MVMT,
            String M1306_UNHLD_STG2_PRSR_ULCR,
            String M1308_NBR_PRSULC_STG2,
            String M1308_NBR_STG2_AT_SOC_ROC,
            String M1308_NBR_PRSULC_STG3,
            String M1308_NBR_STG3_AT_SOC_ROC,
            String M1308_NBR_PRSULC_STG4,
            String M1308_NBR_STG4_AT_SOC_ROC,
            String M1308_NSTG_DRSG,
            String M1308_NSTG_DRSG_SOC_ROC,
            String M1308_NSTG_CVRG,
            String M1308_NSTG_CVRG_SOC_ROC,
            String M1308_NSTG_DEEP_TISUE,
            String M1310_PRSR_ULCR_LNGTH,
            String M1312_PRSR_ULCR_WDTH,
            String M1314_PRSR_ULCR_DEPTH,
            String M1320_STUS_PRBLM_PRSR_ULCR,
            String M1322_NBR_PRSULC_STG1,
            String M1324_STG_PRBLM_ULCER,
            String M1330_STAS_ULCR_PRSNT,
            String M1334_STUS_PRBLM_STAS_ULCR,
            String M1340_SRGCL_WND_PRSNT,
            String M1342_STUS_PRBLM_SRGCL_WND,
            String M1350_LESION_OPEN_WND,
            String M1400_WHEN_DYSPNEIC,
            String M1610_UR_INCONT,
            String M1615_INCNTNT_TIMING,
            String M1620_BWL_INCONT,
            String M1630_OSTOMY,
            String M1830_CRNT_BATHG,
            String M1860_CRNT_AMBLTN,
            String M2000_DRUG_RGMN_RVW,
            String M2030_CRNT_MGMT_INJCTN_MDCTN,
            String M2200_THER_NEED_NA) throws ParseException {
        Oasis_C1_Record_2_11 oasisRecord;
        oasisRecord = new Oasis_C1_Record_2_11();

        oasisRecord.setSTART_CARE_DT(OasisCalendarFormatter.parse(M0030_START_CARE_DT));
        oasisRecord.setINFO_COMPLETED_DT(OasisCalendarFormatter.parse(M0090_INFO_COMPLETED_DT));
        oasisRecord.setASSMT_REASON(M0100_ASSMT_REASON);
        oasisRecord.setEPISODE_TIMING(M0110_EPISODE_TIMING);
        oasisRecord.setPRIMARY_DIAG_ICD(HomeHealthRecordUtil.parseDxCode(M1020_PRIMARY_DIAG_ICD));
        oasisRecord.setOTH_DIAG1_ICD(HomeHealthRecordUtil.parseDxCode(M1022_OTH_DIAG1_ICD));
        oasisRecord.setOTH_DIAG2_ICD(HomeHealthRecordUtil.parseDxCode(M1022_OTH_DIAG2_ICD));
        oasisRecord.setOTH_DIAG3_ICD(HomeHealthRecordUtil.parseDxCode(M1022_OTH_DIAG3_ICD));
        oasisRecord.setOTH_DIAG4_ICD(HomeHealthRecordUtil.parseDxCode(M1022_OTH_DIAG4_ICD));
        oasisRecord.setOTH_DIAG5_ICD(HomeHealthRecordUtil.parseDxCode(M1022_OTH_DIAG5_ICD));
        oasisRecord.setPMT_DIAG_ICD_A3(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_A3));
        oasisRecord.setPMT_DIAG_ICD_B3(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_B3));
        oasisRecord.setPMT_DIAG_ICD_C3(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_C3));
        oasisRecord.setPMT_DIAG_ICD_D3(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_D3));
        oasisRecord.setPMT_DIAG_ICD_E3(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_E3));
        oasisRecord.setPMT_DIAG_ICD_F3(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_F3));
        oasisRecord.setPMT_DIAG_ICD_A4(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_A4));
        oasisRecord.setPMT_DIAG_ICD_B4(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_B4));
        oasisRecord.setPMT_DIAG_ICD_C4(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_C4));
        oasisRecord.setPMT_DIAG_ICD_D4(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_D4));
        oasisRecord.setPMT_DIAG_ICD_E4(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_E4));
        oasisRecord.setPMT_DIAG_ICD_F4(HomeHealthRecordUtil.parseDxCode(M1024_PMT_DIAG_ICD_F4));
        oasisRecord.setTHH_IV_INFUSION(M1030_THH_IV_INFUSION);
        oasisRecord.setTHH_PAR_NUTRITION(M1030_THH_PAR_NUTRITION);
        oasisRecord.setTHH_ENT_NUTRITION(M1030_THH_ENT_NUTRITION);
        oasisRecord.setTHH_NONE_ABOVE(M1030_THH_NONE_ABOVE);
        oasisRecord.setVISION(M1200_VISION);
        oasisRecord.setPAIN_FREQ_ACTVTY_MVMT(M1242_PAIN_FREQ_ACTVTY_MVMT);
        oasisRecord.setUNHLD_STG2_PRSR_ULCR(M1306_UNHLD_STG2_PRSR_ULCR);
        oasisRecord.setNBR_PRSULC_STG2(M1308_NBR_PRSULC_STG2);
        oasisRecord.setNBR_STG2_AT_SOC_ROC(M1308_NBR_STG2_AT_SOC_ROC);
        oasisRecord.setNBR_PRSULC_STG3(M1308_NBR_PRSULC_STG3);
        oasisRecord.setNBR_STG3_AT_SOC_ROC(M1308_NBR_STG3_AT_SOC_ROC);
        oasisRecord.setNBR_PRSULC_STG4(M1308_NBR_PRSULC_STG4);
        oasisRecord.setNBR_STG4_AT_SOC_ROC(M1308_NBR_STG4_AT_SOC_ROC);
        oasisRecord.setNSTG_DRSG(M1308_NSTG_DRSG);
        oasisRecord.setNSTG_DRSG_SOC_ROC(M1308_NSTG_DRSG_SOC_ROC);
        oasisRecord.setNSTG_CVRG(M1308_NSTG_CVRG);
        oasisRecord.setNSTG_CVRG_SOC_ROC(M1308_NSTG_CVRG_SOC_ROC);
        oasisRecord.setNSTG_DEEP_TISUE(M1308_NSTG_DEEP_TISUE);
        oasisRecord.setPRSR_ULCR_LNGTH(M1310_PRSR_ULCR_LNGTH);
        oasisRecord.setPRSR_ULCR_WDTH(M1312_PRSR_ULCR_WDTH);
        oasisRecord.setPRSR_ULCR_DEPTH(M1314_PRSR_ULCR_DEPTH);
        oasisRecord.setSTUS_PRBLM_PRSR_ULCR(M1320_STUS_PRBLM_PRSR_ULCR);
        oasisRecord.setNBR_PRSULC_STG1(M1322_NBR_PRSULC_STG1);
        oasisRecord.setSTG_PRBLM_ULCER(M1324_STG_PRBLM_ULCER);
        oasisRecord.setSTAS_ULCR_PRSNT(M1330_STAS_ULCR_PRSNT);
        oasisRecord.setSTUS_PRBLM_STAS_ULCR(M1334_STUS_PRBLM_STAS_ULCR);
        oasisRecord.setSRGCL_WND_PRSNT(M1340_SRGCL_WND_PRSNT);
        oasisRecord.setSTUS_PRBLM_SRGCL_WND(M1342_STUS_PRBLM_SRGCL_WND);
        oasisRecord.setLESION_OPEN_WND(M1350_LESION_OPEN_WND);
        oasisRecord.setWHEN_DYSPNEIC(M1400_WHEN_DYSPNEIC);
        oasisRecord.setUR_INCONT(M1610_UR_INCONT);
        oasisRecord.setINCNTNT_TIMING(M1615_INCNTNT_TIMING);
        oasisRecord.setBWL_INCONT(M1620_BWL_INCONT);
        oasisRecord.setOSTOMY(M1630_OSTOMY);
        oasisRecord.setCRNT_BATHG(M1830_CRNT_BATHG);
        oasisRecord.setCRNT_AMBLTN(M1860_CRNT_AMBLTN);
        oasisRecord.setDRUG_RGMN_RVW(M2000_DRUG_RGMN_RVW);
        oasisRecord.setCRNT_MGMT_INJCTN_MDCTN(M2030_CRNT_MGMT_INJCTN_MDCTN);
        oasisRecord.setTHER_NEED_NA(M2200_THER_NEED_NA);

        return oasisRecord;
    }
}
