/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut;

import com.mmm.cms.homehealth.vut.proto.OasisDataItemIF;
import com.mmm.cms.homehealth.vut.proto.OasisValidatableRecordIF;
import static com.mmm.cms.homehealth.io.HomeHealthRecordUtil.formatDxCode_C1;
import static com.mmm.cms.homehealth.io.HomeHealthRecordUtil.formatTherapyNeedNum;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C1_IF;
import com.mmm.cms.util.OasisCalendarFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This provides on Oasis Validatable Record that is based on a Home Health
 * record used within the HH-PPS. Though the values within this record may be
 * changeable, making changes to the value in this record have no effect on the
 * original Home Health Record.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HHOasisValidatableRecord implements OasisValidatableRecordIF {

    Map<String, OasisDataItemIF> oasisRecord;

    public HHOasisValidatableRecord(HomeHealthRecord_C1_IF homeHealthRecord) {
        createOasisRecordValueMap(homeHealthRecord);
    }

    /**
     * Always returns 59
     *
     * @return 59
     */
    @Override
    public int size() {
        return this.oasisRecord.size();
    }

    /**
     * Always return false
     *
     * @return false
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Always returns false
     *
     * @return false
     */
    @Override
    public boolean isModifiable() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return get((String) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return value instanceof String ? oasisRecord.containsValue(new HHOasisDataItem("", (String) value)) : oasisRecord.containsValue(value);
    }

    @Override
    public OasisDataItemIF get(Object object) {
        return this.oasisRecord.get(object);
    }

    private void createOasisRecordValueMap(HomeHealthRecord_C1_IF homeHealthRecord) {
        final StringBuilder buffer = new StringBuilder();
        this.oasisRecord = new HashMap<String, OasisDataItemIF>();
        this.oasisRecord.put("M0030_START_CARE_DT", new HHOasisDataItem("M0030_START_CARE_DT",
                OasisCalendarFormatter.format(homeHealthRecord.getSTART_CARE_DT(), buffer).toString()));   // M0030_START_CARE_DT - Start of care date
        buffer.setLength(0);
        this.oasisRecord.put("M0090_INFO_COMPLETED_DT", new HHOasisDataItem("M0090_INFO_COMPLETED_DT",
                OasisCalendarFormatter.format(homeHealthRecord.getINFO_COMPLETED_DT(), buffer).toString()));  // M0090_INFO_COMPLETED_DT - Date assessment completed
        this.oasisRecord.put("M0100_ASSMT_REASON", new HHOasisDataItem("M0100_ASSMT_REASON", homeHealthRecord.getASSMT_REASON()));   // M0100_ASSMT_REASON - Reason for assessment
        this.oasisRecord.put("M0110_EPISODE_TIMING", new HHOasisDataItem("M0110_EPISODE_TIMING", homeHealthRecord.getEPISODE_TIMING()));   // M0110_EPISODE_TIMING - Episode timing
        this.oasisRecord.put("M1030_THH_IV_INFUSION", new HHOasisDataItem("M1030_THH_IV_INFUSION", homeHealthRecord.getTHH_IV_INFUSION()));   // M1030_THH_IV_INFUSION - Therapies received at home: intravenous, infusion
        this.oasisRecord.put("M1030_THH_PAR_NUTRITION", new HHOasisDataItem("M1030_THH_PAR_NUTRITION", homeHealthRecord.getTHH_PAR_NUTRITION()));   // M1030_THH_PAR_NUTRITION - Therapies received at home: parenteral nutrition
        this.oasisRecord.put("M1030_THH_ENT_NUTRITION", new HHOasisDataItem("M1030_THH_ENT_NUTRITION", homeHealthRecord.getTHH_ENT_NUTRITION()));   // M1030_THH_ENT_NUTRITION - Therapies received at home: enteral nutrition
        this.oasisRecord.put("M1030_THH_NONE_ABOVE", new HHOasisDataItem("M1030_THH_NONE_ABOVE", homeHealthRecord.getTHH_NONE_ABOVE()));   // M1030_THH_NONE_ABOVE - Therapies received at home: none of the above
        this.oasisRecord.put("M1200_VISION", new HHOasisDataItem("M1200_VISION", homeHealthRecord.getVISION()));   // M1200_VISION - Sensory status: vision
        this.oasisRecord.put("M1242_PAIN_FREQ_ACTVTY_MVMT", new HHOasisDataItem("M1242_PAIN_FREQ_ACTVTY_MVMT", homeHealthRecord.getPAIN_FREQ_ACTVTY_MVMT()));   // M1242_PAIN_FREQ_ACTVTY_MVMT - Freq of pain interfering with pt activity/movement
        this.oasisRecord.put("M1306_UNHLD_STG2_PRSR_ULCR", new HHOasisDataItem("M1306_UNHLD_STG2_PRSR_ULCR", homeHealthRecord.getUNHLD_STG2_PRSR_ULCR()));   // M1306_UNHLD_STG2_PRSR_ULCR - Patient has 1+ unhealed PU at stage 2 or higher
        this.oasisRecord.put("M1308_NBR_PRSULC_STG2", new HHOasisDataItem("M1308_NBR_PRSULC_STG2", homeHealthRecord.getNBR_PRSULC_STG2()));   // M1308_NBR_PRSULC_STG2 - No. pressure ulcers - stage 2
        this.oasisRecord.put("M1308_NBR_PRSULC_STG3", new HHOasisDataItem("M1308_NBR_PRSULC_STG3", homeHealthRecord.getNBR_PRSULC_STG3()));   // M1308_NBR_PRSULC_STG3 - No. pressure ulcers - stage 3
        this.oasisRecord.put("M1308_NBR_PRSULC_STG4", new HHOasisDataItem("M1308_NBR_PRSULC_STG4", homeHealthRecord.getNBR_PRSULC_STG4()));   // M1308_NBR_PRSULC_STG4 - No. pressure ulcers - stage 4
        this.oasisRecord.put("M1308_NSTG_DRSG", new HHOasisDataItem("M1308_NSTG_DRSG", homeHealthRecord.getNSTG_DRSG()));   // M1308_NSTG_DRSG - Unstageable: non-removable dressing/device
        this.oasisRecord.put("M1308_NSTG_CVRG", new HHOasisDataItem("M1308_NSTG_CVRG", homeHealthRecord.getNSTG_CVRG()));   // M1308_NSTG_CVRG - Unstageable: coverage by slough or eschar
        this.oasisRecord.put("M1308_NSTG_DEEP_TISUE", new HHOasisDataItem("M1308_NSTG_DEEP_TISUE", homeHealthRecord.getNSTG_DEEP_TISUE()));   // M1308_NSTG_DEEP_TISUE - Unstageable: suspect deep tissue injury
        this.oasisRecord.put("M1320_STUS_PRBLM_PRSR_ULCR", new HHOasisDataItem("M1320_STUS_PRBLM_PRSR_ULCR", homeHealthRecord.getSTUS_PRBLM_PRSR_ULCR()));   // M1320_STUS_PRBLM_PRSR_ULCR - Status of most problematic pressure ulcer
        this.oasisRecord.put("M1322_NBR_PRSULC_STG1", new HHOasisDataItem("M1322_NBR_PRSULC_STG1", homeHealthRecord.getNBR_PRSULC_STG1()));   // M1322_NBR_PRSULC_STG1 - No. pressure ulcers - stage 1
        this.oasisRecord.put("M1324_STG_PRBLM_ULCER", new HHOasisDataItem("M1324_STG_PRBLM_ULCER", homeHealthRecord.getSTG_PRBLM_ULCER()));   // M1324_STG_PRBLM_ULCER - Stage of most problematic pressure ulcer
        this.oasisRecord.put("M1330_STAS_ULCR_PRSNT", new HHOasisDataItem("M1330_STAS_ULCR_PRSNT", homeHealthRecord.getSTAS_ULCR_PRSNT()));   // M1330_STAS_ULCR_PRSNT - Does this patient have a stasis ulcer
        this.oasisRecord.put("M1332_NUM_STAS_ULCR", new HHOasisDataItem("M1332_NUM_STAS_ULCR", homeHealthRecord.getNBR_STAS_ULCR()));   // M1332_NUM_STAS_ULCR - No. stasis ulcers
        this.oasisRecord.put("M1334_STUS_PRBLM_STAS_ULCR", new HHOasisDataItem("M1334_STUS_PRBLM_STAS_ULCR", homeHealthRecord.getSTUS_PRBLM_STAS_ULCR()));   // M1334_STUS_PRBLM_STAS_ULCR - Status of most problematic stasis ulcer
        this.oasisRecord.put("M1340_SRGCL_WND_PRSNT", new HHOasisDataItem("M1340_SRGCL_WND_PRSNT", homeHealthRecord.getSRGCL_WND_PRSNT()));   // M1340_SRGCL_WND_PRSNT - Does this patient have a surgical wound
        this.oasisRecord.put("M1342_STUS_PRBLM_SRGCL_WND", new HHOasisDataItem("M1342_STUS_PRBLM_SRGCL_WND", homeHealthRecord.getSTUS_PRBLM_SRGCL_WND()));   // M1342_STUS_PRBLM_SRGCL_WND - Status of most problematic surgical wound
        this.oasisRecord.put("M1350_LESION_OPEN_WND", new HHOasisDataItem("M1350_LESION_OPEN_WND", homeHealthRecord.getLESION_OPEN_WND()));   // M1350_LESION_OPEN_WND - Has skin lesion or open wound
        this.oasisRecord.put("M1400_WHEN_DYSPNEIC", new HHOasisDataItem("M1400_WHEN_DYSPNEIC", homeHealthRecord.getWHEN_DYSPNEIC()));   // M1400_WHEN_DYSPNEIC - When dyspneic
        this.oasisRecord.put("M1610_UR_INCONT", new HHOasisDataItem("M1610_UR_INCONT", homeHealthRecord.getUR_INCONT()));   // M1610_UR_INCONT - Urinary incontinence or urinary catheter present
        this.oasisRecord.put("M1615_INCNTNT_TIMING", new HHOasisDataItem("M1615_INCNTNT_TIMING", homeHealthRecord.getINCNTNT_TIMING()));   // M1615_INCNTNT_TIMING - When urinary incontinence occurs
        this.oasisRecord.put("M1620_BWL_INCONT", new HHOasisDataItem("M1620_BWL_INCONT", homeHealthRecord.getBWL_INCONT()));   // M1620_BWL_INCONT - Bowel incontinence frequency
        this.oasisRecord.put("M1630_OSTOMY", new HHOasisDataItem("M1630_OSTOMY", homeHealthRecord.getOSTOMY()));   // M1630_OSTOMY - Ostomy for bowel elimination
        this.oasisRecord.put("M1810_CUR_DRESS_UPPER", new HHOasisDataItem("M1810_CUR_DRESS_UPPER", homeHealthRecord.getCRNT_DRESS_UPPER()));   // M1810_CUR_DRESS_UPPER - Current: dress upper body
        this.oasisRecord.put("M1820_CUR_DRESS_LOWER", new HHOasisDataItem("M1820_CUR_DRESS_LOWER", homeHealthRecord.getCRNT_DRESS_LOWER()));   // M1820_CUR_DRESS_LOWER - Current: dress lower body
        this.oasisRecord.put("M1830_CRNT_BATHG", new HHOasisDataItem("M1830_CRNT_BATHG", homeHealthRecord.getCRNT_BATHG()));   // M1830_CRNT_BATHG - Current: bathing
        this.oasisRecord.put("M1840_CUR_TOILTG", new HHOasisDataItem("M1840_CUR_TOILTG", homeHealthRecord.getCRNT_TOILTG()));   // M1840_CUR_TOILTG - Current: toileting
        this.oasisRecord.put("M1850_CUR_TRNSFRNG", new HHOasisDataItem("M1850_CUR_TRNSFRNG", homeHealthRecord.getCRNT_TRNSFRNG()));   // M1850_CUR_TRNSFRNG - Current: transferring
        this.oasisRecord.put("M1860_CRNT_AMBLTN", new HHOasisDataItem("M1860_CRNT_AMBLTN", homeHealthRecord.getCRNT_AMBLTN()));   // M1860_CRNT_AMBLTN - Current: ambulation
        this.oasisRecord.put("M2000_DRUG_RGMN_RVW", new HHOasisDataItem("M2000_DRUG_RGMN_RVW", homeHealthRecord.getDRUG_RGMN_RVW()));   // M2000_DRUG_RGMN_RVW - Drug regimen review
        this.oasisRecord.put("M2030_CRNT_MGMT_INJCTN_MDCTN", new HHOasisDataItem("M2030_CRNT_MGMT_INJCTN_MDCTN", homeHealthRecord.getCRNT_MGMT_INJCTN_MDCTN()));   // M2030_CRNT_MGMT_INJCTN_MDCTN - Current: management of injectable medications
        this.oasisRecord.put("M2200_THER_NEED_NUM", new HHOasisDataItem("M2200_THER_NEED_NUM", formatTherapyNeedNum(homeHealthRecord.getTHER_NEED_NBR())));    // M2200_THER_NEED_NUM - Therapy need: number of visits
        this.oasisRecord.put("M2200_THER_NEED_NA", new HHOasisDataItem("M2200_THER_NEED_NA", homeHealthRecord.getTHER_NEED_NA()));   // M2200_THER_NEED_NA - Therapy need: not applicable
        this.oasisRecord.put("M1021_PRIMARY_DIAG_ICD", new HHOasisDataItem("M1021_PRIMARY_DIAG_ICD", formatDxCode_C1(homeHealthRecord.getPRIMARY_DIAG_ICD())));   // M1021_PRIMARY_DIAG_ICD - Primary diagnosis ICD code
        this.oasisRecord.put("M1023_OTH_DIAG1_ICD", new HHOasisDataItem("M1023_OTH_DIAG1_ICD", formatDxCode_C1(homeHealthRecord.getOTH_DIAG1_ICD())));   // M1023_OTH_DIAG1_ICD - Other diagnosis 1: ICD code
        this.oasisRecord.put("M1023_OTH_DIAG2_ICD", new HHOasisDataItem("M1023_OTH_DIAG2_ICD", formatDxCode_C1(homeHealthRecord.getOTH_DIAG2_ICD())));   // M1023_OTH_DIAG2_ICD - Other diagnosis 2: ICD code
        this.oasisRecord.put("M1023_OTH_DIAG3_ICD", new HHOasisDataItem("M1023_OTH_DIAG3_ICD", formatDxCode_C1(homeHealthRecord.getOTH_DIAG3_ICD())));   // M1023_OTH_DIAG3_ICD - Other diagnosis 3: ICD code
        this.oasisRecord.put("M1023_OTH_DIAG4_ICD", new HHOasisDataItem("M1023_OTH_DIAG4_ICD", formatDxCode_C1(homeHealthRecord.getOTH_DIAG4_ICD())));   // M1023_OTH_DIAG4_ICD - Other diagnosis 4: ICD code
        this.oasisRecord.put("M1023_OTH_DIAG5_ICD", new HHOasisDataItem("M1023_OTH_DIAG5_ICD", formatDxCode_C1(homeHealthRecord.getOTH_DIAG5_ICD())));   // M1023_OTH_DIAG5_ICD - Other diagnosis 5: ICD code
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_A3", new HHOasisDataItem("M1025_PMT_DIAG_ICD_A3", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_A3())));   // M1025_PMT_DIAG_ICD_A3 - Case mix diagnosis: primary, column 3
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_A4", new HHOasisDataItem("M1025_PMT_DIAG_ICD_A4", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_A4())));   // M1025_PMT_DIAG_ICD_A4 - Case mix diagnosis: primary, column 4
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_B3", new HHOasisDataItem("M1025_PMT_DIAG_ICD_B3", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_B3())));   // M1025_PMT_DIAG_ICD_B3 - Case mix diagnosis: first secondary, column 3
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_B4", new HHOasisDataItem("M1025_PMT_DIAG_ICD_B4", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_B4())));   // M1025_PMT_DIAG_ICD_B4 - Case mix diagnosis: first secondary, column 4
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_C3", new HHOasisDataItem("M1025_PMT_DIAG_ICD_C3", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_C3())));   // M1025_PMT_DIAG_ICD_C3 - Case mix diagnosis: second secondary, column 3
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_C4", new HHOasisDataItem("M1025_PMT_DIAG_ICD_C4", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_C4())));   // M1025_PMT_DIAG_ICD_C4 - Case mix diagnosis: second secondary, column 4
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_D3", new HHOasisDataItem("M1025_PMT_DIAG_ICD_D3", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_D3())));   // M1025_PMT_DIAG_ICD_D3 - Case mix diagnosis: third secondary, column 3
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_D4", new HHOasisDataItem("M1025_PMT_DIAG_ICD_D4", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_D4())));   // M1025_PMT_DIAG_ICD_D4 - Case mix diagnosis: third secondary, column 4
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_E3", new HHOasisDataItem("M1025_PMT_DIAG_ICD_E3", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_E3())));   // M1025_PMT_DIAG_ICD_E3 - Case mix diagnosis: fourth secondary, column 3
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_E4", new HHOasisDataItem("M1025_PMT_DIAG_ICD_E4", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_E4())));   // M1025_PMT_DIAG_ICD_E4 - Case mix diagnosis: fourth secondary, column 4
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_F3", new HHOasisDataItem("M1025_PMT_DIAG_ICD_F3", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_F3())));   // M1025_PMT_DIAG_ICD_F3 - Case mix diagnosis: fifth secondary, column 3
        this.oasisRecord.put("M1025_PMT_DIAG_ICD_F4", new HHOasisDataItem("M1025_PMT_DIAG_ICD_F4", formatDxCode_C1(homeHealthRecord.getPMT_DIAG_ICD_F4())));   // M1025_PMT_DIAG_ICD_F4 - Case mix diagnosis: fifth secondary, column 4
    }

    /**
     * Does nothing
     *
     * @param key
     * @param value
     * @return null
     */
    @Override
    public OasisDataItemIF put(String key, OasisDataItemIF value) {
        return null;
    }

    /**
     * Does nothing
     *
     * @param key
     * @return null
     */
    @Override
    public OasisDataItemIF remove(Object key) {
        return null;
    }

    /**
     * Does nothing
     *
     * @param m
     */
    @Override
    public void putAll(Map<? extends String, ? extends OasisDataItemIF> m) {
    }

    /**
     * Does nothing
     */
    @Override
    public void clear() {
    }

    @Override
    public Set<String> keySet() {
        return oasisRecord.keySet();
    }

    @Override
    public Collection<OasisDataItemIF> values() {
        return oasisRecord.values();
    }

    @Override
    public Set<Entry<String, OasisDataItemIF>> entrySet() {
        return oasisRecord.entrySet();
    }
}
