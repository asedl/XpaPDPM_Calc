/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.io;

import com.mmm.cms.homehealth.io.record.Oasis_C_Record;
import com.mmm.cms.homehealth.io.util.Oasis_C_RecordUtil_v2_10;
import static com.mmm.cms.homehealth.io.HomeHealthRecordUtil.formatDxCode_C1;
import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.util.DateRanger;
import com.mmm.cms.util.OasisCalendarFormatter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 * This class parses an XML file building a list of Home Health Record IF
 * objects for various OASIS xml formats.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class OasisXMLConverter implements OasisRecordConverterIF {

    public final static String TAG_ASSESSMENT = "ASSESSMENT";
    public final static String TAG_SPEC_VRSN_CD = "SPEC_VRSN_CD";
    public final static String TAG_SPEC_VRSN_CD_VALUE_OASIS_C = "2.10";
    public final static String TAG_SPEC_VRSN_CD_VALUE_OASIS_C1_2_10A = "02.00";
    public final static String TAG_SPEC_VRSN_CD_VALUE_OASIS_C1_2_10A_OTHER = "2.00";
    public final static String TAG_SPEC_VRSN_CD_VALUE_OASIS_C1_2_11 = "2.11";
    public final static String TAG_SPEC_VRSN_CD_VALUE_OASIS_C1_2_12 = "2.12";
    public final static String TAG_SPEC_VRSN_CD_VALUE_OASIS_C2_2_20 = "2.20";
    public final static String TAG_SPEC_VRSN_CD_VALUE_OASIS_C2_2_21 = "2.21";
    public final static String TAG_SPEC_VRSN_CD_VALUE_OASIS_D_2_30 = "2.30";
    public final static String TAG_SPEC_VRSN_CD_VALUE_OASIS_DEFAULT = "DEFAULT";
    public final static String TAG_ASMT_SYS_CD = "ASMT_SYS_CD";
    public final static String TAG_TRANS_TYPE_CD = "TRANS_TYPE_CD";
    public final static String TAG_ITM_SBST_CD = "ITM_SBST_CD";
    public final static String TAG_ITM_SET_VRSN_CD = "ITM_SET_VRSN_CD";

    protected DateRanger dateRanger;

    public OasisXMLConverter() {
        this(new ArrayList());
    }

    public OasisXMLConverter(List<HomeHealthRecordIF> recordList) {
        this(recordList, "20120101", "20191231");
    }

    public OasisXMLConverter(List<HomeHealthRecordIF> recordList, String startDate, String endDate) {
        if (recordList == null) {
            throw new IllegalArgumentException("HomeHealthXMLHandler.init - parameter 'recordList' can not be null");
        }

        this.dateRanger = new DateRanger(startDate, endDate);
    }

    //---------------------------------
    // DOCUMENT READING
    //---------------------------------
    public int readWith(InputStream inputStream, OasisXmlHandler xmlHandler) {

        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser;

        try {
            saxParser = saxFactory.newSAXParser();
            saxParser.parse(inputStream, xmlHandler);

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        } catch (SAXException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
        }

        return xmlHandler.getRecords().size();
    }

    /**
     * Determines if the string can be converted to a HomeHealthRecordIF, such
     * as an OASIS-C or OASIS-C1-ICD9
     *
     * @param record
     * @return true if the string has a high probability of being converted to
     * either OASIS-C or OASIS-C1, although this test is not extensive and there
     * may be errors during actually conversion.
     */
    @Override
    public boolean isRecordConvertable(String record) {
        boolean convertable = false;

        /*
         * The string must begin with '<xml' and
         * OASIS-C1 does not have a tag for PRSR_ULCR_LNGTH
         */
        if (record.contains("<?xml")) {
            final int idx = record.indexOf(Oasis_C_RecordUtil_v2_10.OASIS_ITEM_M0090_INFO_COMPLETED_DT);

            if (idx >= 0) {
                // get the MOO90 date and compare to the date range
                final int endTagIdx = record.indexOf(">", idx) + 1;
                final int nextAmpIdx = record.indexOf("<", endTagIdx);
                final String date = record.substring(endTagIdx, nextAmpIdx).trim();

                convertable = dateRanger.isDateWithinRange(date)
                        && (isOasisC_Convertable(record)
                        || isOasisC1_Convertable(record));
            }
        }

        return convertable;
    }

    /**
     * Determines if the record contains tags that are specific to OASIS-C, such
     * as the Ulcer size items
     *
     * @param record
     * @return
     */
    protected boolean isOasisC_Convertable(String record) {
        return record.contains("PRSR_ULCR_LNGTH");
    }

    /**
     * Determines if the record contains tags that are specific to OASIS-C1 or
     * does not contain any OASIS-C specific items
     *
     * @param record
     * @return
     */
    protected boolean isOasisC1_Convertable(String record) {
        return !isOasisC_Convertable(record);
    }

    @Override
    public StringBuilder convertFromHomeHealthRec(HomeHealthRecordIF homeHealthRecord) {
        final StringBuilder buffer = new StringBuilder();

        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        buffer.append(convertFromHomeHealthToXmlRecord((HomeHealthRecord_C_IF) homeHealthRecord));

        return buffer;
    }

    @Override
    public StringBuilder convertFromHomeHealthRecDelimeted(HomeHealthRecordIF homeHealthRecord, String delimiter) {
        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED);
    }

    @Override
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum) throws ParseException {
        return convertToHomeHealthRec(strRecord, recNum, false);
    }

    /**
     *
     * @param strRecord
     * @param recNum
     * @param skipPassthru - ignored
     * @return
     * @throws ParseException - if there is 0 records, or more than one record
     */
    @Override
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum, boolean skipPassthru) throws ParseException {
        return convertToHomeHealthRec(strRecord, recNum, skipPassthru, null);
    }
    
    /**
     *
     * @param strRecord
     * @param recNum
     * @param skipPassthru - ignored
     * @param xmlHandler - if non-null, it will be used to handle the XML events
     * @return
     * @throws ParseException - if there is 0 records, or more than one record
     */
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord, int recNum, boolean skipPassthru,
            OasisXmlHandler xmlHandler) throws ParseException {
        HomeHealthRecordIF record;
        InputStream inputStream;
        int count;
        
        // ensure there is an XML handler
        if (xmlHandler == null) {
            xmlHandler = new OasisXmlHandler();
        }

        inputStream = new ByteArrayInputStream(strRecord.getBytes());
        count = readWith(inputStream, xmlHandler);
        switch (count) {
            case 1:
                record = xmlHandler.getRecords().get(0);
                break;
            case 0:
                throw new ParseException("No record found", 0);
            default:
                throw new ParseException("Too many records in string: count = " + count, 0);
        }

        /* 
         * this is specifically for OASIS-C and OASIS-C1, which might
         * actually be extending OASIS-C
         */
        if (record.getClass() == Oasis_C_Record.class) {
            Oasis_C_RecordUtil_v2_10.postProcessRecord(record);
        }

        /*
         * this is for OASIS-C and its extending classes
         * justify any values that came in as less than required length
         * due to edit -3100 of spec
         */
        if (record instanceof Oasis_C_Record) {
            HomeHealthRecordUtil.justifyOasisCValues((Oasis_C_Record) record);
        }

        return record;
    }

    public StringBuilder convertFromHomeHealthToXmlRecord(HomeHealthRecord_C_IF oasisRecord) {
        StringBuilder buffer = new StringBuilder(3000);

        buffer.append("<ASSESSMENT>").append("\n");
        buffer.append("  <ASMT_SYS_CD>OASIS</ASMT_SYS_CD>").append("\n");
        buffer.append("  <TRANS_TYPE_CD>1</TRANS_TYPE_CD>").append("\n");
        buffer.append("  <ITM_SBST_CD>01</ITM_SBST_CD>").append("\n");
        buffer.append("  <ITM_SET_VRSN_CD>C1-102014</ITM_SET_VRSN_CD>").append("\n");
        buffer.append("  <SPEC_VRSN_CD>2.11</SPEC_VRSN_CD>").append("\n");

        buffer.append("  <CORRECTION_NUM>").append("</CORRECTION_NUM>").append("\n");   // CORRECTION_NUM - Correction number
        buffer.append("  <STATE_CD>").append("</STATE_CD>").append("\n");   // STATE_CD - Agency's state postal code
        buffer.append("  <HHA_AGENCY_ID>").append("</HHA_AGENCY_ID>").append("\n");   // HHA_AGENCY_ID - Assigned agency submission ID
        buffer.append("  <NATL_PRVDR_ID>").append("</NATL_PRVDR_ID>").append("\n");   // NATL_PRVDR_ID - Agency National Provider ID (NPI)
        buffer.append("  <SFW_ID>").append("</SFW_ID>").append("\n");   // SFW_ID - Software vendor federal employer tax ID
        buffer.append("  <SFW_NAME>").append("</SFW_NAME>").append("\n");   // SFW_NAME - Software vendor company name
        buffer.append("  <SFW_EMAIL_ADR>").append("</SFW_EMAIL_ADR>").append("\n");   // SFW_EMAIL_ADR - Software vendor email address
        buffer.append("  <SFW_PROD_NAME>").append("</SFW_PROD_NAME>").append("\n");   // SFW_PROD_NAME - Software product name
        buffer.append("  <SFW_PROD_VRSN_CD>").append("</SFW_PROD_VRSN_CD>").append("\n");   // SFW_PROD_VRSN_CD - Software product version code
        buffer.append("  <ACY_DOC_CD>").append("</ACY_DOC_CD>").append("\n");   // ACY_DOC_CD - Document ID code (agency use)
        buffer.append("  <SUBM_HIPPS_CODE>").append("</SUBM_HIPPS_CODE>").append("\n");   // SUBM_HIPPS_CODE - HIPPS group code: submitted
        buffer.append("  <SUBM_HIPPS_VERSION>").append("</SUBM_HIPPS_VERSION>").append("\n");   // SUBM_HIPPS_VERSION - HIPPS version code: submitted
        buffer.append("  <M0010_CCN>").append("</M0010_CCN>").append("\n");   // M0010_CCN - Facility CMS certification number (CCN)
        buffer.append("  <M0014_BRANCH_STATE>").append("</M0014_BRANCH_STATE>").append("\n");   // M0014_BRANCH_STATE - Branch state
        buffer.append("  <M0016_BRANCH_ID>").append("</M0016_BRANCH_ID>").append("\n");   // M0016_BRANCH_ID - Branch ID
        buffer.append("  <M0018_PHYSICIAN_ID>").append("</M0018_PHYSICIAN_ID>").append("\n");   // M0018_PHYSICIAN_ID - Attending physician National Provider ID (NPI)
        buffer.append("  <M0018_PHYSICIAN_UK>").append("</M0018_PHYSICIAN_UK>").append("\n");   // M0018_PHYSICIAN_UK - Attending physician NPI: Unknown
        buffer.append("  <M0020_PAT_ID>").append("</M0020_PAT_ID>").append("\n");   // M0020_PAT_ID - Patient ID number
        buffer.append("  <M0030_START_CARE_DT>");
        OasisCalendarFormatter.format(oasisRecord.getSTART_CARE_DT(), buffer).append("</M0030_START_CARE_DT>").append("\n");   // M0030_START_CARE_DT - Start of care date
        buffer.append("  <M0032_ROC_DT>").append("</M0032_ROC_DT>").append("\n");   // M0032_ROC_DT - Resumption of care date
        buffer.append("  <M0032_ROC_DT_NA>").append("</M0032_ROC_DT_NA>").append("\n");   // M0032_ROC_DT_NA - No resumption of care date
        buffer.append("  <M0040_PAT_FNAME>").append("</M0040_PAT_FNAME>").append("\n");   // M0040_PAT_FNAME - Patient's first name
        buffer.append("  <M0040_PAT_MI>").append("</M0040_PAT_MI>").append("\n");   // M0040_PAT_MI - Patient's middle initial
        buffer.append("  <M0040_PAT_LNAME>").append("</M0040_PAT_LNAME>").append("\n");   // M0040_PAT_LNAME - Patient's last name
        buffer.append("  <M0040_PAT_SUFFIX>").append("</M0040_PAT_SUFFIX>").append("\n");   // M0040_PAT_SUFFIX - Patient's suffix
        buffer.append("  <M0050_PAT_ST>").append("</M0050_PAT_ST>").append("\n");   // M0050_PAT_ST - Patient state of residence
        buffer.append("  <M0060_PAT_ZIP>").append("</M0060_PAT_ZIP>").append("\n");   // M0060_PAT_ZIP - Patient zip code
        buffer.append("  <M0063_MEDICARE_NUM>").append("</M0063_MEDICARE_NUM>").append("\n");   // M0063_MEDICARE_NUM - Medicare number, including suffix
        buffer.append("  <M0063_MEDICARE_NA>").append("</M0063_MEDICARE_NA>").append("\n");   // M0063_MEDICARE_NA - No Medicare number
        buffer.append("  <M0064_SSN>").append("</M0064_SSN>").append("\n");   // M0064_SSN - Patient's Social Security number
        buffer.append("  <M0064_SSN_UK>").append("</M0064_SSN_UK>").append("\n");   // M0064_SSN_UK - No Social Security number
        buffer.append("  <M0065_MEDICAID_NUM>").append("</M0065_MEDICAID_NUM>").append("\n");   // M0065_MEDICAID_NUM - Medicaid number
        buffer.append("  <M0065_MEDICAID_NA>").append("</M0065_MEDICAID_NA>").append("\n");   // M0065_MEDICAID_NA - No Medicaid number
        buffer.append("  <M0066_PAT_BIRTH_DT>").append("</M0066_PAT_BIRTH_DT>").append("\n");   // M0066_PAT_BIRTH_DT - Date of birth
        buffer.append("  <M0069_PAT_GENDER>").append("</M0069_PAT_GENDER>").append("\n");   // M0069_PAT_GENDER - Gender
        buffer.append("  <M0140_ETHNIC_AI_AN>").append("</M0140_ETHNIC_AI_AN>").append("\n");   // M0140_ETHNIC_AI_AN - Ethnicity: American Indian or Alaska Native
        buffer.append("  <M0140_ETHNIC_ASIAN>").append("</M0140_ETHNIC_ASIAN>").append("\n");   // M0140_ETHNIC_ASIAN - Ethnicity: Asian
        buffer.append("  <M0140_ETHNIC_BLACK>").append("</M0140_ETHNIC_BLACK>").append("\n");   // M0140_ETHNIC_BLACK - Ethnicity: Black or African American
        buffer.append("  <M0140_ETHNIC_HISP>").append("</M0140_ETHNIC_HISP>").append("\n");   // M0140_ETHNIC_HISP - Ethnicity: Hispanic or Latino
        buffer.append("  <M0140_ETHNIC_NH_PI>").append("</M0140_ETHNIC_NH_PI>").append("\n");   // M0140_ETHNIC_NH_PI - Ethnicity: Native Hawaiian/Pacific Islander
        buffer.append("  <M0140_ETHNIC_WHITE>").append("</M0140_ETHNIC_WHITE>").append("\n");   // M0140_ETHNIC_WHITE - Ethnicity: White
        buffer.append("  <M0150_CPAY_NONE>").append("</M0150_CPAY_NONE>").append("\n");   // M0150_CPAY_NONE - Payment sources: no charge for current services
        buffer.append("  <M0150_CPAY_MCARE_FFS>").append("</M0150_CPAY_MCARE_FFS>").append("\n");   // M0150_CPAY_MCARE_FFS - Payment sources: Medicare fee-for-service
        buffer.append("  <M0150_CPAY_MCARE_HMO>").append("</M0150_CPAY_MCARE_HMO>").append("\n");   // M0150_CPAY_MCARE_HMO - Payment sources: Medicare HMO/managed care
        buffer.append("  <M0150_CPAY_MCAID_FFS>").append("</M0150_CPAY_MCAID_FFS>").append("\n");   // M0150_CPAY_MCAID_FFS - Payment sources: Medicaid fee-for-service
        buffer.append("  <M0150_CPAY_MCAID_HMO>").append("</M0150_CPAY_MCAID_HMO>").append("\n");   // M0150_CPAY_MCAID_HMO - Payment sources: Medicaid HMO/managed care
        buffer.append("  <M0150_CPAY_WRKCOMP>").append("</M0150_CPAY_WRKCOMP>").append("\n");   // M0150_CPAY_WRKCOMP - Payment sources: worker's compensation
        buffer.append("  <M0150_CPAY_TITLEPGMS>").append("</M0150_CPAY_TITLEPGMS>").append("\n");   // M0150_CPAY_TITLEPGMS - Payment sources: title programs
        buffer.append("  <M0150_CPAY_OTH_GOVT>").append("</M0150_CPAY_OTH_GOVT>").append("\n");   // M0150_CPAY_OTH_GOVT - Payment sources: other government
        buffer.append("  <M0150_CPAY_PRIV_INS>").append("</M0150_CPAY_PRIV_INS>").append("\n");   // M0150_CPAY_PRIV_INS - Payment sources: private insurance
        buffer.append("  <M0150_CPAY_PRIV_HMO>").append("</M0150_CPAY_PRIV_HMO>").append("\n");   // M0150_CPAY_PRIV_HMO - Payment sources: private HMO/managed care
        buffer.append("  <M0150_CPAY_SELFPAY>").append("</M0150_CPAY_SELFPAY>").append("\n");   // M0150_CPAY_SELFPAY - Payment sources: self-pay
        buffer.append("  <M0150_CPAY_OTHER>").append("</M0150_CPAY_OTHER>").append("\n");   // M0150_CPAY_OTHER - Payment sources: other
        buffer.append("  <M0150_CPAY_UK>").append("</M0150_CPAY_UK>").append("\n");   // M0150_CPAY_UK - Payment sources: unknown
        buffer.append("  <M0080_ASSESSOR_DISCIPLINE>").append("</M0080_ASSESSOR_DISCIPLINE>").append("\n");   // M0080_ASSESSOR_DISCIPLINE - Discipline of person completing assessment
        buffer.append("  <M0090_INFO_COMPLETED_DT>");
        OasisCalendarFormatter.format(oasisRecord.getINFO_COMPLETED_DT(), buffer).append("</M0090_INFO_COMPLETED_DT>").append("\n");   // M0090_INFO_COMPLETED_DT - Date assessment completed
        buffer.append("  <M0100_ASSMT_REASON>").append(oasisRecord.getASSMT_REASON()).append("</M0100_ASSMT_REASON>").append("\n");   // M0100_ASSMT_REASON - Reason for assessment
        buffer.append("  <M0102_PHYSN_ORDRD_SOCROC_DT>").append("</M0102_PHYSN_ORDRD_SOCROC_DT>").append("\n");   // M0102_PHYSN_ORDRD_SOCROC_DT - Physician ordered SOC/ROC date
        buffer.append("  <M0102_PHYSN_ORDRD_SOCROC_DT_NA>").append("</M0102_PHYSN_ORDRD_SOCROC_DT_NA>").append("\n");   // M0102_PHYSN_ORDRD_SOCROC_DT_NA - Physician ordered SOC/ROC date - NA
        buffer.append("  <M0104_PHYSN_RFRL_DT>").append("</M0104_PHYSN_RFRL_DT>").append("\n");   // M0104_PHYSN_RFRL_DT - Physician date of referral
        buffer.append("  <M0110_EPISODE_TIMING>").append(oasisRecord.getEPISODE_TIMING()).append("</M0110_EPISODE_TIMING>").append("\n");   // M0110_EPISODE_TIMING - Episode timing
        buffer.append("  <M1000_DC_LTC_14_DA>").append("</M1000_DC_LTC_14_DA>").append("\n");   // M1000_DC_LTC_14_DA - Past 14 days: disch from LTC NH
        buffer.append("  <M1000_DC_SNF_14_DA>").append("</M1000_DC_SNF_14_DA>").append("\n");   // M1000_DC_SNF_14_DA - Past 14 days: disch from skilled nursing facility
        buffer.append("  <M1000_DC_IPPS_14_DA>").append("</M1000_DC_IPPS_14_DA>").append("\n");   // M1000_DC_IPPS_14_DA - Past 14 days: disch from short stay acute hospital
        buffer.append("  <M1000_DC_LTCH_14_DA>").append("</M1000_DC_LTCH_14_DA>").append("\n");   // M1000_DC_LTCH_14_DA - Past 14 days: disch from long term care hospital
        buffer.append("  <M1000_DC_IRF_14_DA>").append("</M1000_DC_IRF_14_DA>").append("\n");   // M1000_DC_IRF_14_DA - Past 14 days: disch from inpatient rehab facility
        buffer.append("  <M1000_DC_PSYCH_14_DA>").append("</M1000_DC_PSYCH_14_DA>").append("\n");   // M1000_DC_PSYCH_14_DA - Past 14 days: disch from psych hospital or unit
        buffer.append("  <M1000_DC_OTH_14_DA>").append("</M1000_DC_OTH_14_DA>").append("\n");   // M1000_DC_OTH_14_DA - Past 14 days: disch from other
        buffer.append("  <M1000_DC_NONE_14_DA>").append("</M1000_DC_NONE_14_DA>").append("\n");   // M1000_DC_NONE_14_DA - Past 14 days: not disch from inpatient facility
        buffer.append("  <M1005_INP_DISCHARGE_DT>").append("</M1005_INP_DISCHARGE_DT>").append("\n");   // M1005_INP_DISCHARGE_DT - Most recent inpatient discharge date
        buffer.append("  <M1005_INP_DSCHG_UNKNOWN>").append("</M1005_INP_DSCHG_UNKNOWN>").append("\n");   // M1005_INP_DSCHG_UNKNOWN - Inpatient discharge date unknown
        buffer.append("  <M1018_PRIOR_UR_INCON>").append("</M1018_PRIOR_UR_INCON>").append("\n");   // M1018_PRIOR_UR_INCON - Prior condition: urinary incontinence
        buffer.append("  <M1018_PRIOR_CATH>").append("</M1018_PRIOR_CATH>").append("\n");   // M1018_PRIOR_CATH - Prior condition: indwelling/suprapubic catheter
        buffer.append("  <M1018_PRIOR_INTRACT_PAIN>").append("</M1018_PRIOR_INTRACT_PAIN>").append("\n");   // M1018_PRIOR_INTRACT_PAIN - Prior condition: intractable pain
        buffer.append("  <M1018_PRIOR_IMPR_DECSN>").append("</M1018_PRIOR_IMPR_DECSN>").append("\n");   // M1018_PRIOR_IMPR_DECSN - Prior condition: impaired decision-making
        buffer.append("  <M1018_PRIOR_DISRUPTIVE>").append("</M1018_PRIOR_DISRUPTIVE>").append("\n");   // M1018_PRIOR_DISRUPTIVE - Prior condition: disruptive/inappropriate behav
        buffer.append("  <M1018_PRIOR_MEM_LOSS>").append("</M1018_PRIOR_MEM_LOSS>").append("\n");   // M1018_PRIOR_MEM_LOSS - Prior condition: memory loss, supervision required
        buffer.append("  <M1018_PRIOR_NONE>").append("</M1018_PRIOR_NONE>").append("\n");   // M1018_PRIOR_NONE - Prior condition: none of the above
        buffer.append("  <M1018_PRIOR_NOCHG_14D>").append("</M1018_PRIOR_NOCHG_14D>").append("\n");   // M1018_PRIOR_NOCHG_14D - Prior condition: no inpt disch/no change regimen
        buffer.append("  <M1018_PRIOR_UNKNOWN>").append("</M1018_PRIOR_UNKNOWN>").append("\n");   // M1018_PRIOR_UNKNOWN - Prior condition: unknown
        buffer.append("  <M1030_THH_IV_INFUSION>").append(oasisRecord.getTHH_IV_INFUSION()).append("</M1030_THH_IV_INFUSION>").append("\n");   // M1030_THH_IV_INFUSION - Therapies received at home: intravenous, infusion
        buffer.append("  <M1030_THH_PAR_NUTRITION>").append(oasisRecord.getTHH_PAR_NUTRITION()).append("</M1030_THH_PAR_NUTRITION>").append("\n");   // M1030_THH_PAR_NUTRITION - Therapies received at home: parenteral nutrition
        buffer.append("  <M1030_THH_ENT_NUTRITION>").append(oasisRecord.getTHH_ENT_NUTRITION()).append("</M1030_THH_ENT_NUTRITION>").append("\n");   // M1030_THH_ENT_NUTRITION - Therapies received at home: enteral nutrition
        buffer.append("  <M1030_THH_NONE_ABOVE>").append(oasisRecord.getTHH_NONE_ABOVE()).append("</M1030_THH_NONE_ABOVE>").append("\n");   // M1030_THH_NONE_ABOVE - Therapies received at home: none of the above
        buffer.append("  <M1034_PTNT_OVRAL_STUS>").append("</M1034_PTNT_OVRAL_STUS>").append("\n");   // M1034_PTNT_OVRAL_STUS - Patient's overall status
        buffer.append("  <M1036_RSK_SMOKING>").append("</M1036_RSK_SMOKING>").append("\n");   // M1036_RSK_SMOKING - High risk factor: smoking
        buffer.append("  <M1036_RSK_OBESITY>").append("</M1036_RSK_OBESITY>").append("\n");   // M1036_RSK_OBESITY - High risk factor: obesity
        buffer.append("  <M1036_RSK_ALCOHOLISM>").append("</M1036_RSK_ALCOHOLISM>").append("\n");   // M1036_RSK_ALCOHOLISM - High risk factor: alcoholism
        buffer.append("  <M1036_RSK_DRUGS>").append("</M1036_RSK_DRUGS>").append("\n");   // M1036_RSK_DRUGS - High risk factor: drugs
        buffer.append("  <M1036_RSK_NONE>").append("</M1036_RSK_NONE>").append("\n");   // M1036_RSK_NONE - High risk factor: none of the above
        buffer.append("  <M1036_RSK_UNKNOWN>").append("</M1036_RSK_UNKNOWN>").append("\n");   // M1036_RSK_UNKNOWN - High risk factor: unknown
        buffer.append("  <M1100_PTNT_LVG_STUTN>").append("</M1100_PTNT_LVG_STUTN>").append("\n");   // M1100_PTNT_LVG_STUTN - Patient living situation
        buffer.append("  <M1200_VISION>").append(oasisRecord.getVISION()).append("</M1200_VISION>").append("\n");   // M1200_VISION - Sensory status: vision
        buffer.append("  <M1210_HEARG_ABLTY>").append("</M1210_HEARG_ABLTY>").append("\n");   // M1210_HEARG_ABLTY - Ability to hear
        buffer.append("  <M1220_UNDRSTG_VERBAL_CNTNT>").append("</M1220_UNDRSTG_VERBAL_CNTNT>").append("\n");   // M1220_UNDRSTG_VERBAL_CNTNT - Understanding verbal content in patient's language
        buffer.append("  <M1230_SPEECH>").append("</M1230_SPEECH>").append("\n");   // M1230_SPEECH - Sensory status: speech
        buffer.append("  <M1240_FRML_PAIN_ASMT>").append("</M1240_FRML_PAIN_ASMT>").append("\n");   // M1240_FRML_PAIN_ASMT - Has patient had a formal validated pain assessment
        buffer.append("  <M1242_PAIN_FREQ_ACTVTY_MVMT>").append(oasisRecord.getPAIN_FREQ_ACTVTY_MVMT()).append("</M1242_PAIN_FREQ_ACTVTY_MVMT>").append("\n");   // M1242_PAIN_FREQ_ACTVTY_MVMT - Freq of pain interfering with pt activity/movement
        buffer.append("  <M1300_PRSR_ULCR_RISK_ASMT>").append("</M1300_PRSR_ULCR_RISK_ASMT>").append("\n");   // M1300_PRSR_ULCR_RISK_ASMT - Was patient assessed for risk of developing pus
        buffer.append("  <M1302_RISK_OF_PRSR_ULCR>").append("</M1302_RISK_OF_PRSR_ULCR>").append("\n");   // M1302_RISK_OF_PRSR_ULCR - Does this patient have a risk of developing pus
        buffer.append("  <M1306_UNHLD_STG2_PRSR_ULCR>").append(oasisRecord.getUNHLD_STG2_PRSR_ULCR()).append("</M1306_UNHLD_STG2_PRSR_ULCR>").append("\n");   // M1306_UNHLD_STG2_PRSR_ULCR - Patient has 1+ unhealed PU at stage 2 or higher
        buffer.append("  <M1307_OLDST_STG2_AT_DSCHRG>").append("</M1307_OLDST_STG2_AT_DSCHRG>").append("\n");   // M1307_OLDST_STG2_AT_DSCHRG - Status of oldest stage 2 pressure ulcer at disch
        buffer.append("  <M1307_OLDST_STG2_ONST_DT>").append("</M1307_OLDST_STG2_ONST_DT>").append("\n");   // M1307_OLDST_STG2_ONST_DT - Date of onset of oldest stage 2 pressure ulcer
        buffer.append("  <M1311_NBR_PRSULC_STG2_A1>").append(oasisRecord.getNBR_PRSULC_STG2()).append("</M1311_NBR_PRSULC_STG2_A1>").append("\n");  // M1311_NBR_PRSULC_STG2_A1 - Number of Stage 2 Pressure Ulcers
        buffer.append("  <M1311_NBR_PRSULC_STG3_B1>").append(oasisRecord.getNBR_PRSULC_STG3()).append("</M1311_NBR_PRSULC_STG3_B1>").append("\n");  // M1311_NBR_PRSULC_STG3_B1 - Number of Stage 3 Pressure Ulcers
        buffer.append("  <M1311_NBR_PRSULC_STG4_C1>").append(oasisRecord.getNBR_PRSULC_STG4()).append("</M1311_NBR_PRSULC_STG4_C1>").append("\n");   // M1311_NBR_PRSULC_STG4_C1 - Number of Stage 4 Pressure Ulcers
        buffer.append("  <M1311_NSTG_DRSG_D1>").append(oasisRecord.getNSTG_DRSG()).append("</M1311_NSTG_DRSG_D1>").append("\n");   // M1311_NSTG_DRSG_D1 - Num unstage pressure ulcer non-remov dress
        buffer.append("  <M1311_NSTG_CVRG_E1>").append(oasisRecord.getNSTG_CVRG()).append("</M1311_NSTG_CVRG_E1>").append("\n");   // M1311_NSTG_CVRG_E1 - Unstageable: coverage by slough or eschar
        buffer.append("  <M1311_NSTG_DEEP_TSUE_F1>").append(oasisRecord.getNSTG_DEEP_TISUE()).append("</M1311_NSTG_DEEP_TSUE_F1>").append("\n");   // M1311_NSTG_DEEP_TSUE_F1 - Unstageable: suspect deep tissue injury
        buffer.append("  <M1320_STUS_PRBLM_PRSR_ULCR>").append(oasisRecord.getSTUS_PRBLM_PRSR_ULCR()).append("</M1320_STUS_PRBLM_PRSR_ULCR>").append("\n");   // M1320_STUS_PRBLM_PRSR_ULCR - Status of most problematic pressure ulcer
        buffer.append("  <M1322_NBR_PRSULC_STG1>").append(oasisRecord.getNBR_PRSULC_STG1()).append("</M1322_NBR_PRSULC_STG1>").append("\n");   // M1322_NBR_PRSULC_STG1 - No. pressure ulcers - stage 1
        buffer.append("  <M1324_STG_PRBLM_ULCER>").append(oasisRecord.getSTG_PRBLM_ULCER()).append("</M1324_STG_PRBLM_ULCER>").append("\n");   // M1324_STG_PRBLM_ULCER - Stage of most problematic pressure ulcer
        buffer.append("  <M1330_STAS_ULCR_PRSNT>").append(oasisRecord.getSTAS_ULCR_PRSNT()).append("</M1330_STAS_ULCR_PRSNT>").append("\n");   // M1330_STAS_ULCR_PRSNT - Does this patient have a stasis ulcer
        buffer.append("  <M1332_NBR_STAS_ULCR>").append(oasisRecord.getNBR_STAS_ULCR()).append("</M1332_NBR_STAS_ULCR>").append("\n");   // M1332_NBR_STAS_ULCR - No. stasis ulcers
        buffer.append("  <M1334_STUS_PRBLM_STAS_ULCR>").append(oasisRecord.getSTUS_PRBLM_STAS_ULCR()).append("</M1334_STUS_PRBLM_STAS_ULCR>").append("\n");   // M1334_STUS_PRBLM_STAS_ULCR - Status of most problematic stasis ulcer
        buffer.append("  <M1340_SRGCL_WND_PRSNT>").append(oasisRecord.getSRGCL_WND_PRSNT()).append("</M1340_SRGCL_WND_PRSNT>").append("\n");   // M1340_SRGCL_WND_PRSNT - Does this patient have a surgical wound
        buffer.append("  <M1342_STUS_PRBLM_SRGCL_WND>").append(oasisRecord.getSTUS_PRBLM_SRGCL_WND()).append("</M1342_STUS_PRBLM_SRGCL_WND>").append("\n");   // M1342_STUS_PRBLM_SRGCL_WND - Status of most problematic surgical wound
        buffer.append("  <M1350_LESION_OPEN_WND>").append(oasisRecord.getLESION_OPEN_WND()).append("</M1350_LESION_OPEN_WND>").append("\n");   // M1350_LESION_OPEN_WND - Has skin lesion or open wound
        buffer.append("  <M1400_WHEN_DYSPNEIC>").append(oasisRecord.getWHEN_DYSPNEIC()).append("</M1400_WHEN_DYSPNEIC>").append("\n");   // M1400_WHEN_DYSPNEIC - When dyspneic
        buffer.append("  <M1410_RESPTX_OXYGEN>").append("</M1410_RESPTX_OXYGEN>").append("\n");   // M1410_RESPTX_OXYGEN - Respiratory treatments: oxygen
        buffer.append("  <M1410_RESPTX_VENTILATOR>").append("</M1410_RESPTX_VENTILATOR>").append("\n");   // M1410_RESPTX_VENTILATOR - Respiratory treatments: ventilator
        buffer.append("  <M1410_RESPTX_AIRPRESS>").append("</M1410_RESPTX_AIRPRESS>").append("\n");   // M1410_RESPTX_AIRPRESS - Respiratory treatments: airway pressure
        buffer.append("  <M1410_RESPTX_NONE>").append("</M1410_RESPTX_NONE>").append("\n");   // M1410_RESPTX_NONE - Respiratory treatments: none of the above
        buffer.append("  <M1500_SYMTM_HRT_FAILR_PTNTS>").append("</M1500_SYMTM_HRT_FAILR_PTNTS>").append("\n");   // M1500_SYMTM_HRT_FAILR_PTNTS - Symptoms in heart failure patients
        buffer.append("  <M1510_HRT_FAILR_NO_ACTN>").append("</M1510_HRT_FAILR_NO_ACTN>").append("\n");   // M1510_HRT_FAILR_NO_ACTN - Heart failure follow up:  no action
        buffer.append("  <M1510_HRT_FAILR_PHYSN_CNTCT>").append("</M1510_HRT_FAILR_PHYSN_CNTCT>").append("\n");   // M1510_HRT_FAILR_PHYSN_CNTCT - Heart failure follow up:  physician contacted
        buffer.append("  <M1510_HRT_FAILR_ER_TRTMT>").append("</M1510_HRT_FAILR_ER_TRTMT>").append("\n");   // M1510_HRT_FAILR_ER_TRTMT - Heart failure follow up:  ER treatment advised
        buffer.append("  <M1510_HRT_FAILR_PHYSN_TRTMT>").append("</M1510_HRT_FAILR_PHYSN_TRTMT>").append("\n");   // M1510_HRT_FAILR_PHYSN_TRTMT - Heart failure follow up:  phys-ordered treatmnt
        buffer.append("  <M1510_HRT_FAILR_CLNCL_INTRVTN>").append("</M1510_HRT_FAILR_CLNCL_INTRVTN>").append("\n");   // M1510_HRT_FAILR_CLNCL_INTRVTN - Heart failure follow up: pt educ/other clinical
        buffer.append("  <M1510_HRT_FAILR_CARE_PLAN_CHG>").append("</M1510_HRT_FAILR_CARE_PLAN_CHG>").append("\n");   // M1510_HRT_FAILR_CARE_PLAN_CHG - Heart failure follow up: change in care plan
        buffer.append("  <M1600_UTI>").append("</M1600_UTI>").append("\n");   // M1600_UTI - Treated for urinary tract infection past 14 days
        buffer.append("  <M1610_UR_INCONT>").append(oasisRecord.getUR_INCONT()).append("</M1610_UR_INCONT>").append("\n");   // M1610_UR_INCONT - Urinary incontinence or urinary catheter present
        buffer.append("  <M1615_INCNTNT_TIMING>").append(oasisRecord.getINCNTNT_TIMING()).append("</M1615_INCNTNT_TIMING>").append("\n");   // M1615_INCNTNT_TIMING - When urinary incontinence occurs
        buffer.append("  <M1620_BWL_INCONT>").append(oasisRecord.getBWL_INCONT()).append("</M1620_BWL_INCONT>").append("\n");   // M1620_BWL_INCONT - Bowel incontinence frequency
        buffer.append("  <M1630_OSTOMY>").append(oasisRecord.getOSTOMY()).append("</M1630_OSTOMY>").append("\n");   // M1630_OSTOMY - Ostomy for bowel elimination
        buffer.append("  <M1700_COG_FUNCTION>").append("</M1700_COG_FUNCTION>").append("\n");   // M1700_COG_FUNCTION - Cognitive functioning
        buffer.append("  <M1710_WHEN_CONFUSED>").append("</M1710_WHEN_CONFUSED>").append("\n");   // M1710_WHEN_CONFUSED - When confused (reported or observed)
        buffer.append("  <M1720_WHEN_ANXIOUS>").append("</M1720_WHEN_ANXIOUS>").append("\n");   // M1720_WHEN_ANXIOUS - When anxious (reported or observed)
        buffer.append("  <M1730_STDZ_DPRSN_SCRNG>").append("</M1730_STDZ_DPRSN_SCRNG>").append("\n");   // M1730_STDZ_DPRSN_SCRNG - Screened for depression using validated tool
        buffer.append("  <M1730_PHQ2_LACK_INTRST>").append("</M1730_PHQ2_LACK_INTRST>").append("\n");   // M1730_PHQ2_LACK_INTRST - PHQ2: little interest or pleasure in doing things
        buffer.append("  <M1730_PHQ2_DPRSN>").append("</M1730_PHQ2_DPRSN>").append("\n");   // M1730_PHQ2_DPRSN - PHQ2: feeling down, depressed or hopeless
        buffer.append("  <M1740_BD_MEM_DEFICIT>").append("</M1740_BD_MEM_DEFICIT>").append("\n");   // M1740_BD_MEM_DEFICIT - Behavior demonstrated: memory deficit
        buffer.append("  <M1740_BD_IMP_DECISN>").append("</M1740_BD_IMP_DECISN>").append("\n");   // M1740_BD_IMP_DECISN - Behavior demonstrated: impaired decision-making
        buffer.append("  <M1740_BD_VERBAL>").append("</M1740_BD_VERBAL>").append("\n");   // M1740_BD_VERBAL - Behavior demonstrated: verbal disruption
        buffer.append("  <M1740_BD_PHYSICAL>").append("</M1740_BD_PHYSICAL>").append("\n");   // M1740_BD_PHYSICAL - Behavior demonstrated: physical aggression
        buffer.append("  <M1740_BD_SOC_INAPPRO>").append("</M1740_BD_SOC_INAPPRO>").append("\n");   // M1740_BD_SOC_INAPPRO - Behavior demonstrated: socially inappropriate
        buffer.append("  <M1740_BD_DELUSIONS>").append("</M1740_BD_DELUSIONS>").append("\n");   // M1740_BD_DELUSIONS - Behavior demonstrated: delusions
        buffer.append("  <M1740_BD_NONE>").append("</M1740_BD_NONE>").append("\n");   // M1740_BD_NONE - Behavior demonstrated: none of the above
        buffer.append("  <M1745_BEH_PROB_FREQ>").append("</M1745_BEH_PROB_FREQ>").append("\n");   // M1745_BEH_PROB_FREQ - Frequency of behavior problems
        buffer.append("  <M1750_REC_PSYCH_NURS>").append("</M1750_REC_PSYCH_NURS>").append("\n");   // M1750_REC_PSYCH_NURS - Receives psychiatric nursing
        buffer.append("  <M1800_CRNT_GROOMING>").append("</M1800_CRNT_GROOMING>").append("\n");   // M1800_CRNT_GROOMING - Current: grooming
        buffer.append("  <M1810_CRNT_DRESS_UPPER>").append(oasisRecord.getCRNT_DRESS_UPPER()).append("</M1810_CRNT_DRESS_UPPER>").append("\n");   // M1810_CRNT_DRESS_UPPER - Current: dress upper body
        buffer.append("  <M1820_CRNT_DRESS_LOWER>").append(oasisRecord.getCRNT_DRESS_LOWER()).append("</M1820_CRNT_DRESS_LOWER>").append("\n");   // M1820_CRNT_DRESS_LOWER - Current: dress lower body
        buffer.append("  <M1830_CRNT_BATHG>").append(oasisRecord.getCRNT_BATHG()).append("</M1830_CRNT_BATHG>").append("\n");   // M1830_CRNT_BATHG - Current: bathing
        buffer.append("  <M1840_CRNT_TOILTG>").append(oasisRecord.getCRNT_TOILTG()).append("</M1840_CRNT_TOILTG>").append("\n");   // M1840_CRNT_TOILTG - Current: toileting
        buffer.append("  <M1845_CRNT_TOILTG_HYGN>").append("</M1845_CRNT_TOILTG_HYGN>").append("\n");   // M1845_CRNT_TOILTG_HYGN - Current: toileting hygiene
        buffer.append("  <M1850_CRNT_TRNSFRNG>").append(oasisRecord.getCRNT_TRNSFRNG()).append("</M1850_CRNT_TRNSFRNG>").append("\n");   // M1850_CRNT_TRNSFRNG - Current: transferring
        buffer.append("  <M1860_CRNT_AMBLTN>").append(oasisRecord.getCRNT_AMBLTN()).append("</M1860_CRNT_AMBLTN>").append("\n");   // M1860_CRNT_AMBLTN - Current: ambulation
        buffer.append("  <M1870_CRNT_FEEDING>").append("</M1870_CRNT_FEEDING>").append("\n");   // M1870_CRNT_FEEDING - Current: feeding
        buffer.append("  <M1880_CRNT_PREP_LT_MEALS>").append("</M1880_CRNT_PREP_LT_MEALS>").append("\n");   // M1880_CRNT_PREP_LT_MEALS - Current: prepare light meals
        buffer.append("  <M1890_CRNT_PHONE_USE>").append("</M1890_CRNT_PHONE_USE>").append("\n");   // M1890_CRNT_PHONE_USE - Current: telephone use
        buffer.append("  <M1900_PRIOR_ADLIADL_SELF>").append("</M1900_PRIOR_ADLIADL_SELF>").append("\n");   // M1900_PRIOR_ADLIADL_SELF - Prior functioning ADL/IADL: self-care
        buffer.append("  <M1900_PRIOR_ADLIADL_AMBLTN>").append("</M1900_PRIOR_ADLIADL_AMBLTN>").append("\n");   // M1900_PRIOR_ADLIADL_AMBLTN - Prior functioning ADL/IADL: ambulation
        buffer.append("  <M1900_PRIOR_ADLIADL_TRNSFR>").append("</M1900_PRIOR_ADLIADL_TRNSFR>").append("\n");   // M1900_PRIOR_ADLIADL_TRNSFR - Prior functioning ADL/IADL: transfer
        buffer.append("  <M1900_PRIOR_ADLIADL_HSEHOLD>").append("</M1900_PRIOR_ADLIADL_HSEHOLD>").append("\n");   // M1900_PRIOR_ADLIADL_HSEHOLD - Prior functioning ADL/IADL: household tasks
        buffer.append("  <M1910_MLT_FCTR_FALL_RISK_ASMT>").append("</M1910_MLT_FCTR_FALL_RISK_ASMT>").append("\n");   // M1910_MLT_FCTR_FALL_RISK_ASMT - Has patient had a multi-factor fall risk asmt
        buffer.append("  <M2001_DRUG_RGMN_RVW>").append(oasisRecord.getDRUG_RGMN_RVW()).append("</M2001_DRUG_RGMN_RVW>").append("\n");   // M2000_DRUG_RGMN_RVW - Drug regimen review
        buffer.append("  <M2002_MDCTN_FLWP>").append("</M2002_MDCTN_FLWP>").append("\n");   // M2002_MDCTN_FLWP - Medication follow-up
        buffer.append("  <M2004_MDCTN_INTRVTN>").append("</M2004_MDCTN_INTRVTN>").append("\n");   // M2004_MDCTN_INTRVTN - Medication intervention
        buffer.append("  <M2010_HIGH_RISK_DRUG_EDCTN>").append("</M2010_HIGH_RISK_DRUG_EDCTN>").append("\n");   // M2010_HIGH_RISK_DRUG_EDCTN - Patient/caregiver high risk drug education
        buffer.append("  <M2015_DRUG_EDCTN_INTRVTN>").append("</M2015_DRUG_EDCTN_INTRVTN>").append("\n");   // M2015_DRUG_EDCTN_INTRVTN - Patient/caregiver drug education intervention
        buffer.append("  <M2020_CRNT_MGMT_ORAL_MDCTN>").append("</M2020_CRNT_MGMT_ORAL_MDCTN>").append("\n");   // M2020_CRNT_MGMT_ORAL_MDCTN - Current: management of oral medications
        buffer.append("  <M2030_CRNT_MGMT_INJCTN_MDCTN>").append(oasisRecord.getCRNT_MGMT_INJCTN_MDCTN()).append("</M2030_CRNT_MGMT_INJCTN_MDCTN>").append("\n");   // M2030_CRNT_MGMT_INJCTN_MDCTN - Current: management of injectable medications
        buffer.append("  <M2040_PRIOR_MGMT_ORAL_MDCTN>").append("</M2040_PRIOR_MGMT_ORAL_MDCTN>").append("\n");   // M2040_PRIOR_MGMT_ORAL_MDCTN - Prior med mgmt: oral medications
        buffer.append("  <M2040_PRIOR_MGMT_INJCTN_MDCTN>").append("</M2040_PRIOR_MGMT_INJCTN_MDCTN>").append("\n");   // M2040_PRIOR_MGMT_INJCTN_MDCTN - Prior med mgmt: injectable medications
        buffer.append("  <M2110_ADL_IADL_ASTNC_FREQ>").append("</M2110_ADL_IADL_ASTNC_FREQ>").append("\n");   // M2110_ADL_IADL_ASTNC_FREQ - How often recv ADL or IADL assistance from any
        buffer.append("  <M2200_THER_NEED_NBR>").append(oasisRecord.getTHER_NEED_NBR()).append("</M2200_THER_NEED_NBR>").append("\n");   // M2200_THER_NEED_NBR - Therapy need: number of visits
        buffer.append("  <M2200_THER_NEED_NA>").append(oasisRecord.getTHER_NEED_NA()).append("</M2200_THER_NEED_NA>").append("\n");   // M2200_THER_NEED_NA - Therapy need: not applicable
        buffer.append("  <M2250_PLAN_SMRY_PTNT_SPECF>").append("</M2250_PLAN_SMRY_PTNT_SPECF>").append("\n");   // M2250_PLAN_SMRY_PTNT_SPECF - Plan of care synopsis: patient specific parameters
        buffer.append("  <M2250_PLAN_SMRY_DBTS_FT_CARE>").append("</M2250_PLAN_SMRY_DBTS_FT_CARE>").append("\n");   // M2250_PLAN_SMRY_DBTS_FT_CARE - Plan of care synopsis: diabetic foot care
        buffer.append("  <M2250_PLAN_SMRY_FALL_PRVNT>").append("</M2250_PLAN_SMRY_FALL_PRVNT>").append("\n");   // M2250_PLAN_SMRY_FALL_PRVNT - Plan of care synopsis: falls prevention
        buffer.append("  <M2250_PLAN_SMRY_DPRSN_INTRVTN>").append("</M2250_PLAN_SMRY_DPRSN_INTRVTN>").append("\n");   // M2250_PLAN_SMRY_DPRSN_INTRVTN - Plan of care synopsis: depression interventions
        buffer.append("  <M2250_PLAN_SMRY_PAIN_INTRVTN>").append("</M2250_PLAN_SMRY_PAIN_INTRVTN>").append("\n");   // M2250_PLAN_SMRY_PAIN_INTRVTN - Plan of care synopsis: pain interventions
        buffer.append("  <M2250_PLAN_SMRY_PRSULC_PRVNT>").append("</M2250_PLAN_SMRY_PRSULC_PRVNT>").append("\n");   // M2250_PLAN_SMRY_PRSULC_PRVNT - Plan of care synopsis: PU prevention
        buffer.append("  <M2250_PLAN_SMRY_PRSULC_TRTMT>").append("</M2250_PLAN_SMRY_PRSULC_TRTMT>").append("\n");   // M2250_PLAN_SMRY_PRSULC_TRTMT - Plan of care synopsis: PU moist treatment
        buffer.append("  <M2300_EMER_USE_AFTR_LAST_ASMT>").append("</M2300_EMER_USE_AFTR_LAST_ASMT>").append("\n");   // M2300_EMER_USE_AFTR_LAST_ASMT - Emergent care: use since previous OASIS
        buffer.append("  <M2310_ECR_MEDICATION>").append("</M2310_ECR_MEDICATION>").append("\n");   // M2310_ECR_MEDICATION - Emergent care reason: medication
        buffer.append("  <M2310_ECR_INJRY_BY_FALL>").append("</M2310_ECR_INJRY_BY_FALL>").append("\n");   // M2310_ECR_INJRY_BY_FALL - Emergent care reason: injury caused by fall
        buffer.append("  <M2310_ECR_RSPRTRY_INFCTN>").append("</M2310_ECR_RSPRTRY_INFCTN>").append("\n");   // M2310_ECR_RSPRTRY_INFCTN - Emergent care reason: respiratory infection
        buffer.append("  <M2310_ECR_RSPRTRY_OTHR>").append("</M2310_ECR_RSPRTRY_OTHR>").append("\n");   // M2310_ECR_RSPRTRY_OTHR - Emergent care reason: respiratory other
        buffer.append("  <M2310_ECR_HRT_FAILR>").append("</M2310_ECR_HRT_FAILR>").append("\n");   // M2310_ECR_HRT_FAILR - Emergent care reason: heart failure
        buffer.append("  <M2310_ECR_CRDC_DSRTHM>").append("</M2310_ECR_CRDC_DSRTHM>").append("\n");   // M2310_ECR_CRDC_DSRTHM - Emergent care reason: cardiac dysrhythmia
        buffer.append("  <M2310_ECR_MI_CHST_PAIN>").append("</M2310_ECR_MI_CHST_PAIN>").append("\n");   // M2310_ECR_MI_CHST_PAIN - Emergent care reason: myocard infarct/chest pain
        buffer.append("  <M2310_ECR_OTHR_HRT_DEASE>").append("</M2310_ECR_OTHR_HRT_DEASE>").append("\n");   // M2310_ECR_OTHR_HRT_DEASE - Emergent care reason: other heart disease
        buffer.append("  <M2310_ECR_STROKE_TIA>").append("</M2310_ECR_STROKE_TIA>").append("\n");   // M2310_ECR_STROKE_TIA - Emergent care reason: stroke (CVA) or TIA
        buffer.append("  <M2310_ECR_HYPOGLYC>").append("</M2310_ECR_HYPOGLYC>").append("\n");   // M2310_ECR_HYPOGLYC - Emergent care reason: hypoglycemia
        buffer.append("  <M2310_ECR_GI_PRBLM>").append("</M2310_ECR_GI_PRBLM>").append("\n");   // M2310_ECR_GI_PRBLM - Emergent care: GI bleed/obstruct/constip/impact
        buffer.append("  <M2310_ECR_DHYDRTN_MALNTR>").append("</M2310_ECR_DHYDRTN_MALNTR>").append("\n");   // M2310_ECR_DHYDRTN_MALNTR - Emergent care reason: dehydration, malnutrition
        buffer.append("  <M2310_ECR_UTI>").append("</M2310_ECR_UTI>").append("\n");   // M2310_ECR_UTI - Emergent care reason: urinary tract infection
        buffer.append("  <M2310_ECR_CTHTR_CMPLCTN>").append("</M2310_ECR_CTHTR_CMPLCTN>").append("\n");   // M2310_ECR_CTHTR_CMPLCTN - Emergent care reason: IV catheter infect/complic
        buffer.append("  <M2310_ECR_WND_INFCTN_DTRORTN>").append("</M2310_ECR_WND_INFCTN_DTRORTN>").append("\n");   // M2310_ECR_WND_INFCTN_DTRORTN - Emergent care reason: wound infect/deterioration
        buffer.append("  <M2310_ECR_UNCNTLD_PAIN>").append("</M2310_ECR_UNCNTLD_PAIN>").append("\n");   // M2310_ECR_UNCNTLD_PAIN - Emergent care reason: uncontrolled pain
        buffer.append("  <M2310_ECR_MENTL_BHVRL_PRBLM>").append("</M2310_ECR_MENTL_BHVRL_PRBLM>").append("\n");   // M2310_ECR_MENTL_BHVRL_PRBLM - Emergent care reason: acute mental/behav problem
        buffer.append("  <M2310_ECR_DVT_PULMNRY>").append("</M2310_ECR_DVT_PULMNRY>").append("\n");   // M2310_ECR_DVT_PULMNRY - Emergent care reason: deep vein thromb/pulm embol
        buffer.append("  <M2310_ECR_OTHER>").append("</M2310_ECR_OTHER>").append("\n");   // M2310_ECR_OTHER - Emergent care reason: other than above
        buffer.append("  <M2310_ECR_UNKNOWN>").append("</M2310_ECR_UNKNOWN>").append("\n");   // M2310_ECR_UNKNOWN - Emergent care reason: unknown
        buffer.append("  <M2400_INTRVTN_SMRY_DBTS_FT>").append("</M2400_INTRVTN_SMRY_DBTS_FT>").append("\n");   // M2400_INTRVTN_SMRY_DBTS_FT - Intervention synopsis: diabetic foot care
        buffer.append("  <M2400_INTRVTN_SMRY_FALL_PRVNT>").append("</M2400_INTRVTN_SMRY_FALL_PRVNT>").append("\n");   // M2400_INTRVTN_SMRY_FALL_PRVNT - Intervention synopsis: falls prevention
        buffer.append("  <M2400_INTRVTN_SMRY_DPRSN>").append("</M2400_INTRVTN_SMRY_DPRSN>").append("\n");   // M2400_INTRVTN_SMRY_DPRSN - Intervention synopsis: depression intervention
        buffer.append("  <M2400_INTRVTN_SMRY_PAIN_MNTR>").append("</M2400_INTRVTN_SMRY_PAIN_MNTR>").append("\n");   // M2400_INTRVTN_SMRY_PAIN_MNTR - Intervention synopsis: monitor and mitigate pain
        buffer.append("  <M2400_INTRVTN_SMRY_PRSULC_PRVN>").append("</M2400_INTRVTN_SMRY_PRSULC_PRVN>").append("\n");   // M2400_INTRVTN_SMRY_PRSULC_PRVN - Intervention synopsis: prevent pressure ulcers
        buffer.append("  <M2400_INTRVTN_SMRY_PRSULC_WET>").append("</M2400_INTRVTN_SMRY_PRSULC_WET>").append("\n");   // M2400_INTRVTN_SMRY_PRSULC_WET - Intervention synopsis: PU moist wound treatment
        buffer.append("  <M2410_INPAT_FACILITY>").append("</M2410_INPAT_FACILITY>").append("\n");   // M2410_INPAT_FACILITY - Inpatient facility
        buffer.append("  <M2420_DSCHRG_DISP>").append("</M2420_DSCHRG_DISP>").append("\n");   // M2420_DSCHRG_DISP - Discharge disposition
        buffer.append("  <M2430_HOSP_MED>").append("</M2430_HOSP_MED>").append("\n");   // M2430_HOSP_MED - Hospitalized: medication
        buffer.append("  <M2430_HOSP_INJRY_BY_FALL>").append("</M2430_HOSP_INJRY_BY_FALL>").append("\n");   // M2430_HOSP_INJRY_BY_FALL - Hospitalized: injury caused by fall
        buffer.append("  <M2430_HOSP_RSPRTRY_INFCTN>").append("</M2430_HOSP_RSPRTRY_INFCTN>").append("\n");   // M2430_HOSP_RSPRTRY_INFCTN - Hospitalized: respiratory
        buffer.append("  <M2430_HOSP_RSPRTRY_OTHR>").append("</M2430_HOSP_RSPRTRY_OTHR>").append("\n");   // M2430_HOSP_RSPRTRY_OTHR - Hospitalized: other respiratory
        buffer.append("  <M2430_HOSP_HRT_FAILR>").append("</M2430_HOSP_HRT_FAILR>").append("\n");   // M2430_HOSP_HRT_FAILR - Hospitalized: heart failure
        buffer.append("  <M2430_HOSP_CRDC_DSRTHM>").append("</M2430_HOSP_CRDC_DSRTHM>").append("\n");   // M2430_HOSP_CRDC_DSRTHM - Hospitalized: cardiac dysrhythmia
        buffer.append("  <M2430_HOSP_MI_CHST_PAIN>").append("</M2430_HOSP_MI_CHST_PAIN>").append("\n");   // M2430_HOSP_MI_CHST_PAIN - Hospitalized: myocardial infarction or chest pain
        buffer.append("  <M2430_HOSP_OTHR_HRT_DEASE>").append("</M2430_HOSP_OTHR_HRT_DEASE>").append("\n");   // M2430_HOSP_OTHR_HRT_DEASE - Hospitalized: other heart disease
        buffer.append("  <M2430_HOSP_STROKE_TIA>").append("</M2430_HOSP_STROKE_TIA>").append("\n");   // M2430_HOSP_STROKE_TIA - Hospitalized: stroke (CVA) or TIA
        buffer.append("  <M2430_HOSP_HYPOGLYC>").append("</M2430_HOSP_HYPOGLYC>").append("\n");   // M2430_HOSP_HYPOGLYC - Hospitalized: hypoglycemia
        buffer.append("  <M2430_HOSP_GI_PRBLM>").append("</M2430_HOSP_GI_PRBLM>").append("\n");   // M2430_HOSP_GI_PRBLM - Hospitalized: GI bleed/obstruct/constip/impact
        buffer.append("  <M2430_HOSP_DHYDRTN_MALNTR>").append("</M2430_HOSP_DHYDRTN_MALNTR>").append("\n");   // M2430_HOSP_DHYDRTN_MALNTR - Hospitalized: dehydration, malnutrition
        buffer.append("  <M2430_HOSP_UR_TRACT>").append("</M2430_HOSP_UR_TRACT>").append("\n");   // M2430_HOSP_UR_TRACT - Hospitalized: urinary tract infection
        buffer.append("  <M2430_HOSP_CTHTR_CMPLCTN>").append("</M2430_HOSP_CTHTR_CMPLCTN>").append("\n");   // M2430_HOSP_CTHTR_CMPLCTN - Hospitalized: IV catheter infect/complic
        buffer.append("  <M2430_HOSP_WND_INFCTN>").append("</M2430_HOSP_WND_INFCTN>").append("\n");   // M2430_HOSP_WND_INFCTN - Hospitalized: wound infect/deterioration
        buffer.append("  <M2430_HOSP_PAIN>").append("</M2430_HOSP_PAIN>").append("\n");   // M2430_HOSP_PAIN - Hospitalized: pain
        buffer.append("  <M2430_HOSP_MENTL_BHVRL_PRBLM>").append("</M2430_HOSP_MENTL_BHVRL_PRBLM>").append("\n");   // M2430_HOSP_MENTL_BHVRL_PRBLM - Hospitalized: acute mental/behav problem
        buffer.append("  <M2430_HOSP_DVT_PULMNRY>").append("</M2430_HOSP_DVT_PULMNRY>").append("\n");   // M2430_HOSP_DVT_PULMNRY - Hospitalized: deep vein thromb/pulm embol
        buffer.append("  <M2430_HOSP_SCHLD_TRTMT>").append("</M2430_HOSP_SCHLD_TRTMT>").append("\n");   // M2430_HOSP_SCHLD_TRTMT - Hospitalized: scheduled treatment or procedure
        buffer.append("  <M2430_HOSP_OTHER>").append("</M2430_HOSP_OTHER>").append("\n");   // M2430_HOSP_OTHER - Hospitalized: other
        buffer.append("  <M2430_HOSP_UK>").append("</M2430_HOSP_UK>").append("\n");   // M2430_HOSP_UK - Hospitalized: UK
        buffer.append("  <M0903_LAST_HOME_VISIT>").append("</M0903_LAST_HOME_VISIT>").append("\n");   // M0903_LAST_HOME_VISIT - Date of last home visit
        buffer.append("  <M0906_DC_TRAN_DTH_DT>").append("</M0906_DC_TRAN_DTH_DT>").append("\n");   // M0906_DC_TRAN_DTH_DT - Discharge, transfer, death date
        buffer.append("  <CONTROL_ITEMS_FILLER>").append("</CONTROL_ITEMS_FILLER>").append("\n");   // CONTROL_ITEMS_FILLER - Control items filler
        buffer.append("  <M1011_14_DAY_INP1_ICD>").append("</M1011_14_DAY_INP1_ICD>").append("\n");   // M1011_14_DAY_INP1_ICD - Inpatient stay within last 14 days: ICD code 1
        buffer.append("  <M1011_14_DAY_INP2_ICD>").append("</M1011_14_DAY_INP2_ICD>").append("\n");   // M1011_14_DAY_INP2_ICD - Inpatient stay within last 14 days: ICD code 2
        buffer.append("  <M1011_14_DAY_INP3_ICD>").append("</M1011_14_DAY_INP3_ICD>").append("\n");   // M1011_14_DAY_INP3_ICD - Inpatient stay within last 14 days: ICD code 3
        buffer.append("  <M1011_14_DAY_INP4_ICD>").append("</M1011_14_DAY_INP4_ICD>").append("\n");   // M1011_14_DAY_INP4_ICD - Inpatient stay within last 14 days: ICD code 4
        buffer.append("  <M1011_14_DAY_INP5_ICD>").append("</M1011_14_DAY_INP5_ICD>").append("\n");   // M1011_14_DAY_INP5_ICD - Inpatient stay within last 14 days: ICD code 5
        buffer.append("  <M1011_14_DAY_INP6_ICD>").append("</M1011_14_DAY_INP6_ICD>").append("\n");   // M1011_14_DAY_INP6_ICD - Inpatient stay within last 14 days: ICD code 6
        buffer.append("  <M1011_14_DAY_INP_NA>").append("</M1011_14_DAY_INP_NA>").append("\n");   // M1011_14_DAY_INP_NA - Inpatient stay within last 14 days: not applicable
        buffer.append("  <M1017_CHGREG_ICD1>").append("</M1017_CHGREG_ICD1>").append("\n");   // M1017_CHGREG_ICD1 - Regimen change in past 14 days: ICD code 1
        buffer.append("  <M1017_CHGREG_ICD2>").append("</M1017_CHGREG_ICD2>").append("\n");   // M1017_CHGREG_ICD2 - Regimen change in past 14 days: ICD code 2
        buffer.append("  <M1017_CHGREG_ICD3>").append("</M1017_CHGREG_ICD3>").append("\n");   // M1017_CHGREG_ICD3 - Regimen change in past 14 days: ICD code 3
        buffer.append("  <M1017_CHGREG_ICD4>").append("</M1017_CHGREG_ICD4>").append("\n");   // M1017_CHGREG_ICD4 - Regimen change in past 14 days: ICD code 4
        buffer.append("  <M1017_CHGREG_ICD5>").append("</M1017_CHGREG_ICD5>").append("\n");   // M1017_CHGREG_ICD5 - Regimen change in past 14 days: ICD code 5
        buffer.append("  <M1017_CHGREG_ICD6>").append("</M1017_CHGREG_ICD6>").append("\n");   // M1017_CHGREG_ICD6 - Regimen change in past 14 days: ICD code 6
        buffer.append("  <M1017_CHGREG_ICD_NA>").append("</M1017_CHGREG_ICD_NA>").append("\n");   // M1017_CHGREG_ICD_NA - Regimen change in past 14 days: not applicable
        buffer.append("  <M1021_PRIMARY_DIAG_ICD>").append(formatDxCode_C1(oasisRecord.getPRIMARY_DIAG_ICD())).append("</M1021_PRIMARY_DIAG_ICD>").append("\n");   // M1021_PRIMARY_DIAG_ICD - Primary diagnosis ICD code
        buffer.append("  <M1021_PRIMARY_DIAG_SEVERITY>").append("</M1021_PRIMARY_DIAG_SEVERITY>").append("\n");   // M1021_PRIMARY_DIAG_SEVERITY - Primary diagnosis severity rating
        buffer.append("  <M1023_OTH_DIAG1_ICD>").append(formatDxCode_C1(oasisRecord.getOTH_DIAG1_ICD())).append("</M1023_OTH_DIAG1_ICD>").append("\n");   // M1023_OTH_DIAG1_ICD - Other diagnosis 1: ICD code
        buffer.append("  <M1023_OTH_DIAG1_SEVERITY>").append("</M1023_OTH_DIAG1_SEVERITY>").append("\n");   // M1023_OTH_DIAG1_SEVERITY - Other diagnosis 1: severity rating
        buffer.append("  <M1023_OTH_DIAG2_ICD>").append(formatDxCode_C1(oasisRecord.getOTH_DIAG2_ICD())).append("</M1023_OTH_DIAG2_ICD>").append("\n");   // M1023_OTH_DIAG2_ICD - Other diagnosis 2: ICD code
        buffer.append("  <M1023_OTH_DIAG2_SEVERITY>").append("</M1023_OTH_DIAG2_SEVERITY>").append("\n");   // M1023_OTH_DIAG2_SEVERITY - Other diagnosis 2: severity rating
        buffer.append("  <M1023_OTH_DIAG3_ICD>").append(formatDxCode_C1(oasisRecord.getOTH_DIAG3_ICD())).append("</M1023_OTH_DIAG3_ICD>").append("\n");   // M1023_OTH_DIAG3_ICD - Other diagnosis 3: ICD code
        buffer.append("  <M1023_OTH_DIAG3_SEVERITY>").append("</M1023_OTH_DIAG3_SEVERITY>").append("\n");   // M1023_OTH_DIAG3_SEVERITY - Other diagnosis 3: severity rating
        buffer.append("  <M1023_OTH_DIAG4_ICD>").append(formatDxCode_C1(oasisRecord.getOTH_DIAG4_ICD())).append("</M1023_OTH_DIAG4_ICD>").append("\n");   // M1023_OTH_DIAG4_ICD - Other diagnosis 4: ICD code
        buffer.append("  <M1023_OTH_DIAG4_SEVERITY>").append("</M1023_OTH_DIAG4_SEVERITY>").append("\n");   // M1023_OTH_DIAG4_SEVERITY - Other diagnosis 4: severity rating
        buffer.append("  <M1023_OTH_DIAG5_ICD>").append(formatDxCode_C1(oasisRecord.getOTH_DIAG5_ICD())).append("</M1023_OTH_DIAG5_ICD>").append("\n");   // M1023_OTH_DIAG5_ICD - Other diagnosis 5: ICD code
        buffer.append("  <M1023_OTH_DIAG5_SEVERITY>").append("</M1023_OTH_DIAG5_SEVERITY>").append("\n");   // M1023_OTH_DIAG5_SEVERITY - Other diagnosis 5: severity rating
        buffer.append("  <M1025_PMT_DIAG_ICD_A3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_A3())).append("</M1025_PMT_DIAG_ICD_A3>").append("\n");   // M1025_PMT_DIAG_ICD_A3 - Case mix diagnosis: primary, column 3
        buffer.append("  <M1025_PMT_DIAG_ICD_A4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_A4())).append("</M1025_PMT_DIAG_ICD_A4>").append("\n");   // M1025_PMT_DIAG_ICD_A4 - Case mix diagnosis: primary, column 4
        buffer.append("  <M1025_PMT_DIAG_ICD_B3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_B3())).append("</M1025_PMT_DIAG_ICD_B3>").append("\n");   // M1025_PMT_DIAG_ICD_B3 - Case mix diagnosis: first secondary, column 3
        buffer.append("  <M1025_PMT_DIAG_ICD_B4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_B4())).append("</M1025_PMT_DIAG_ICD_B4>").append("\n");   // M1025_PMT_DIAG_ICD_B4 - Case mix diagnosis: first secondary, column 4
        buffer.append("  <M1025_PMT_DIAG_ICD_C3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_C3())).append("</M1025_PMT_DIAG_ICD_C3>").append("\n");   // M1025_PMT_DIAG_ICD_C3 - Case mix diagnosis: second secondary, column 3
        buffer.append("  <M1025_PMT_DIAG_ICD_C4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_C4())).append("</M1025_PMT_DIAG_ICD_C4>").append("\n");   // M1025_PMT_DIAG_ICD_C4 - Case mix diagnosis: second secondary, column 4
        buffer.append("  <M1025_PMT_DIAG_ICD_D3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_D3())).append("</M1025_PMT_DIAG_ICD_D3>").append("\n");   // M1025_PMT_DIAG_ICD_D3 - Case mix diagnosis: third secondary, column 3
        buffer.append("  <M1025_PMT_DIAG_ICD_D4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_D4())).append("</M1025_PMT_DIAG_ICD_D4>").append("\n");   // M1025_PMT_DIAG_ICD_D4 - Case mix diagnosis: third secondary, column 4
        buffer.append("  <M1025_PMT_DIAG_ICD_E3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_E3())).append("</M1025_PMT_DIAG_ICD_E3>").append("\n");   // M1025_PMT_DIAG_ICD_E3 - Case mix diagnosis: fourth secondary, column 3
        buffer.append("  <M1025_PMT_DIAG_ICD_E4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_E4())).append("</M1025_PMT_DIAG_ICD_E4>").append("\n");   // M1025_PMT_DIAG_ICD_E4 - Case mix diagnosis: fourth secondary, column 4
        buffer.append("  <M1025_PMT_DIAG_ICD_F3>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_F3())).append("</M1025_PMT_DIAG_ICD_F3>").append("\n");   // M1025_PMT_DIAG_ICD_F3 - Case mix diagnosis: fifth secondary, column 3
        buffer.append("  <M1025_PMT_DIAG_ICD_F4>").append(formatDxCode_C1(oasisRecord.getPMT_DIAG_ICD_F4())).append("</M1025_PMT_DIAG_ICD_F4>").append("\n");   // M1025_PMT_DIAG_ICD_F4 - Case mix diagnosis: fifth secondary, column 4
        buffer.append("  <M1033_HOSP_RISK_HSTRY_FALLS>").append("</M1033_HOSP_RISK_HSTRY_FALLS>").append("\n");   // M1033_HOSP_RISK_HSTRY_FALLS - Hosp risk: 2+ falls or injury fall in past year
        buffer.append("  <M1033_HOSP_RISK_WEIGHT_LOSS>").append("</M1033_HOSP_RISK_WEIGHT_LOSS>").append("\n");   // M1033_HOSP_RISK_WEIGHT_LOSS - Hosp risk: unintentional weight loss
        buffer.append("  <M1033_HOSP_RISK_MLTPL_HOSPZTN>").append("</M1033_HOSP_RISK_MLTPL_HOSPZTN>").append("\n");   // M1033_HOSP_RISK_MLTPL_HOSPZTN - Hosp risk: 2+ hospitalizations in past 12 months
        buffer.append("  <M1033_HOSP_RISK_MLTPL_ED_VISIT>").append("</M1033_HOSP_RISK_MLTPL_ED_VISIT>").append("\n");   // M1033_HOSP_RISK_MLTPL_ED_VISIT - Hosp risk: 2+ emergcy dept visits in past 6 months
        buffer.append("  <M1033_HOSP_RISK_MNTL_BHV_DCLN>").append("</M1033_HOSP_RISK_MNTL_BHV_DCLN>").append("\n");   // M1033_HOSP_RISK_MNTL_BHV_DCLN - Hosp risk: decline mental/emotional/behav status
        buffer.append("  <M1033_HOSP_RISK_COMPLIANCE>").append("</M1033_HOSP_RISK_COMPLIANCE>").append("\n");   // M1033_HOSP_RISK_COMPLIANCE - Hosp risk: difficulty with medical instructions
        buffer.append("  <M1033_HOSP_RISK_6PLUS_MDCTN>").append("</M1033_HOSP_RISK_6PLUS_MDCTN>").append("\n");   // M1033_HOSP_RISK_6PLUS_MDCTN - Hosp risk: taking six or more medications
        buffer.append("  <M1033_HOSP_RISK_CURR_EXHSTN>").append("</M1033_HOSP_RISK_CURR_EXHSTN>").append("\n");   // M1033_HOSP_RISK_CURR_EXHSTN - Hosp risk: current exhaustion
        buffer.append("  <M1033_HOSP_RISK_OTHR>").append("</M1033_HOSP_RISK_OTHR>").append("\n");   // M1033_HOSP_RISK_OTHR - Hosp risk: other
        buffer.append("  <M1033_HOSP_RISK_NONE_ABOVE>").append("</M1033_HOSP_RISK_NONE_ABOVE>").append("\n");   // M1033_HOSP_RISK_NONE_ABOVE - Hosp risk: none of the above
        buffer.append("  <M1041_IN_INFLNZ_SEASON>").append("</M1041_IN_INFLNZ_SEASON>").append("\n");   // M1041_IN_INFLNZ_SEASON - Does episode include Oct 1 thru Mar 31
        buffer.append("  <M1046_INFLNZ_RECD_CUR_SEASON>").append("</M1046_INFLNZ_RECD_CUR_SEASON>").append("\n");   // M1046_INFLNZ_RECD_CUR_SEASON - Did patient receive influenza vaccine
        buffer.append("  <M1051_PPV_RCVD_AGNCY>").append("</M1051_PPV_RCVD_AGNCY>").append("\n");   // M1051_PPV_RCVD_AGNCY - Was pneumococcal vaccine received
        buffer.append("  <M1056_PPV_RSN_NOT_RCVD_AGNCY>").append("</M1056_PPV_RSN_NOT_RCVD_AGNCY>").append("\n");   // M1056_PPV_RSN_NOT_RCVD_AGNCY - If pneumococcal vacc not received, state reason
        buffer.append("  <M1309_NBR_NEW_WRS_PRSULC_STG2>").append("</M1309_NBR_NEW_WRS_PRSULC_STG2>").append("\n");   // M1309_NBR_NEW_WRS_PRSULC_STG2 - Number of new or worsening stage 2
        buffer.append("  <M1309_NBR_NEW_WRS_PRSULC_STG3>").append("</M1309_NBR_NEW_WRS_PRSULC_STG3>").append("\n");   // M1309_NBR_NEW_WRS_PRSULC_STG3 - Number of new or worsening stage 3
        buffer.append("  <M1309_NBR_NEW_WRS_PRSULC_STG4>").append("</M1309_NBR_NEW_WRS_PRSULC_STG4>").append("\n");   // M1309_NBR_NEW_WRS_PRSULC_STG4 - Number of new or worsening stage 4
        buffer.append("  <M2102_CARE_TYPE_SRC_ADL>").append("</M2102_CARE_TYPE_SRC_ADL>").append("\n");   // M2102_CARE_TYPE_SRC_ADL - Care mgmt, types/sources: ADL
        buffer.append("  <M2102_CARE_TYPE_SRC_IADL>").append("</M2102_CARE_TYPE_SRC_IADL>").append("\n");   // M2102_CARE_TYPE_SRC_IADL - Care mgmt, types/sources: IADL
        buffer.append("  <M2102_CARE_TYPE_SRC_MDCTN>").append("</M2102_CARE_TYPE_SRC_MDCTN>").append("\n");   // M2102_CARE_TYPE_SRC_MDCTN - Care mgmt, types/sources: med admin
        buffer.append("  <M2102_CARE_TYPE_SRC_PRCDR>").append("</M2102_CARE_TYPE_SRC_PRCDR>").append("\n");   // M2102_CARE_TYPE_SRC_PRCDR - Care mgmt, types/sources: med procs tx
        buffer.append("  <M2102_CARE_TYPE_SRC_EQUIP>").append("</M2102_CARE_TYPE_SRC_EQUIP>").append("\n");   // M2102_CARE_TYPE_SRC_EQUIP - Care mgmt, types/sources: equipment
        buffer.append("  <M2102_CARE_TYPE_SRC_SPRVSN>").append("</M2102_CARE_TYPE_SRC_SPRVSN>").append("\n");   // M2102_CARE_TYPE_SRC_SPRVSN - Care mgmt, types/sources: supervision and safety
        buffer.append("  <M2102_CARE_TYPE_SRC_ADVCY>").append("</M2102_CARE_TYPE_SRC_ADVCY>").append("\n");   // M2102_CARE_TYPE_SRC_ADVCY - Care mgmt, types/sources: advocacy or facilitation
        buffer.append("  <ASMT_ITEMS_FILLER>").append("</ASMT_ITEMS_FILLER>").append("\n");   // ASMT_ITEMS_FILLER - Assessment items filler
        buffer.append("  <LEGACY_ITEMS_FILLER>").append("</LEGACY_ITEMS_FILLER>").append("\n");   // LEGACY_ITEMS_FILLER - Discontinued OASIS-B1 and C items.
        buffer.append("  <CALCULATED_ITEMS_FILLER>").append("</CALCULATED_ITEMS_FILLER>").append("\n");   // CALCULATED_ITEMS_FILLER - Calculated items filler
        buffer.append("  <HHA_ASMT_INT_ID>").append("</HHA_ASMT_INT_ID>").append("\n");   // HHA_ASMT_INT_ID - Assessment internal ID
        buffer.append("  <ORIG_ASMT_INT_ID>").append("</ORIG_ASMT_INT_ID>").append("\n");   // ORIG_ASMT_INT_ID - Original assessment ID
        buffer.append("  <RES_INT_ID>").append("</RES_INT_ID>").append("\n");   // RES_INT_ID - Resident internal ID
        buffer.append("  <ASMT_EFF_DATE>").append("</ASMT_EFF_DATE>").append("\n");   // ASMT_EFF_DATE - Effective date
        buffer.append("  <BRANCH_IDENTIFIER>").append("</BRANCH_IDENTIFIER>").append("\n");   // BRANCH_IDENTIFIER - Branch internal ID
        buffer.append("  <FAC_INT_ID>").append("</FAC_INT_ID>").append("\n");   // FAC_INT_ID - Facility internal ID
        buffer.append("  <SUBMISSION_ID>").append("</SUBMISSION_ID>").append("\n");   // SUBMISSION_ID - Submission ID
        buffer.append("  <SUBMISSION_DATE>").append("</SUBMISSION_DATE>").append("\n");   // SUBMISSION_DATE - Submission date
        buffer.append("  <SUBMISSION_COMPLETE_DATE>").append("</SUBMISSION_COMPLETE_DATE>").append("\n");   // SUBMISSION_COMPLETE_DATE - Submission processing completion date
        buffer.append("  <SUBMITTING_USER_ID>").append("</SUBMITTING_USER_ID>").append("\n");   // SUBMITTING_USER_ID - Submitter user ID
        buffer.append("  <RES_MATCH_CRITERIA>").append("</RES_MATCH_CRITERIA>").append("\n");   // RES_MATCH_CRITERIA - Resident matching criteria
        buffer.append("  <RESIDENT_AGE>").append("</RESIDENT_AGE>").append("\n");   // RESIDENT_AGE - Age of resident on the effective date
        buffer.append("  <BIRTHDATE_SUBM_IND>").append("</BIRTHDATE_SUBM_IND>").append("\n");   // BIRTHDATE_SUBM_IND - Birth date submit code
        buffer.append("  <CALC_HIPPS_CODE>").append("</CALC_HIPPS_CODE>").append("\n");   // CALC_HIPPS_CODE - HIPPS group code: recalculated
        buffer.append("  <CALC_HIPPS_VERSION>").append("</CALC_HIPPS_VERSION>").append("\n");   // CALC_HIPPS_VERSION - HIPPS version code: recalculated

        buffer.append("</ASSESSMENT>").append("\n");

        return buffer;
    }

    public String getStartDate() {
        return dateRanger.getStartDate();
    }

    public String getEndDate() {
        return dateRanger.getEndDate();
    }

    @Override
    public void setStartDate(String date) {
        dateRanger.setStartDate(date);
    }

    @Override
    public void setEndDate(String date) throws IllegalArgumentException {
        dateRanger.setEndDate(date);
    }

    /**
     * DOCUMENT READING - SAX callbacks
     *
     */
}
