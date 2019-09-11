/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.

 */
package com.mmm.cms.homehealth.io.util;

import com.mmm.cms.homehealth.HomeHealthRecord;
import com.mmm.cms.homehealth.io.AbstractRecordConverter;
import com.mmm.cms.homehealth.io.HomeHealthRecordUtil;
import com.mmm.cms.homehealth.io.OasisRecordConverterIF;
import com.mmm.cms.homehealth.io.record.Oasis_B_Record;
import static com.mmm.cms.homehealth.io.HomeHealthRecordUtil.formatDiagnosisCodeI9;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.util.IntegerFormat;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.OasisCalendarFormatter;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This utility class provides conversions between OASIS-B data records and the
 * HomeHealthRecord interface.
 *
 * It can be used in conjunction with the OasisRecordFactory, which queries this
 * utility to determine if a specific OASIS record format can be converted using
 * this utility.
 *
 * @see OasisReaderFactory
 * @author 3M Health Information Systems for CMS Home Health
 */
public class Oasis_B_RecordUtil
        extends AbstractRecordConverter
        implements OasisRecordConverterIF {

    public Oasis_B_RecordUtil() {
        super("20070101", "20091231");
    }

    protected Oasis_B_RecordUtil(String startDate, String endDate) {
        super(startDate, endDate);
    }

    /**
     * gets the MOO90 Info Complete date at location: 302-309 inclusive
     *
     * @param record
     * @return
     */
    @Override
    protected String getRecordDate(String record) {
        return record.substring(301, 309);
    }

    /**
     * gets the version cd a location 23 through 33 inclusive
     *
     * @param record
     * @return
     */
    @Override
    protected String getVersionCD(String record) {
        return record.substring(22, 24);
    }

    /**
     * Converts an OASIS string record to an OASIS Body record using the generic
     * Home Health Record interface. Since much of the OASIS information is not
     * needed for Home Health grouping, this method can ignore them or store
     * them with the record using the <code>skipPassthru</code> parameter
     *
     * @param strRecord
     * @param recNum
     * @param skipPassthru
     * @return
     */
    @Override
    public HomeHealthRecordIF convertToHomeHealthRec(String strRecord,
            int recNum, boolean skipPassthru) throws ParseException {
        Oasis_B_Record oasisRecord;
        final Logger logger = Logger.getLogger(getClass().getName());
        String tmpStr;

        // validate that this is an Body record
        if (strRecord == null) {
            throw new ParseException("OASIS record string can not be null", 0);
        } else if (strRecord.length() < getRecordLength()) {
            throw new ParseException("Unknown record due to invalid length of "
                    + strRecord.length() + ", should be at least " + getRecordLength() + " characters.", 0);
        } else if (strRecord.charAt(0) != 'B' || strRecord.charAt(1) != '1') {
            throw new ParseException("Unknown record - not OASIS-B record - ID characters: \""
                    + strRecord.substring(0, 2) + "\"", 0);
        }

        oasisRecord = new Oasis_B_Record();

        //Format: YYYYMMDD
        try {
            oasisRecord.setSTART_CARE_DT(new GregorianCalendar(Integer.parseInt(strRecord.substring(176, 180)), Integer.parseInt(strRecord.substring(180, 182))
                    - 1, Integer.parseInt(strRecord.substring(182, 184))));
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "HH-PPS: Record: {0}: Invalid Or missing M0030_START_CARE_DT of ''{1}''",
                    new Object[]{recNum, strRecord.substring(176, 184)});
        }

        //Format: YYYYMMDD
        try {
            oasisRecord.setINFO_COMPLETED_DT(new GregorianCalendar(Integer.parseInt(strRecord.substring(301, 305)), Integer.parseInt(strRecord.substring(305, 307))
                    - 1, Integer.parseInt(strRecord.substring(307, 309))));
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "HH-PPS: Record: {0}: Invalid Or missing M0090_INFO_COMPLETED_DT of ''{1}''",
                    new Object[]{recNum, strRecord.substring(301, 309)});
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

        //Format: XX
        oasisRecord.setVISION(strRecord.substring(528, 530).intern());

        //Format: XX
        oasisRecord.setPAIN_FREQ_ACTVTY_MVMT(strRecord.substring(534, 536).intern());

        //Format: X
        oasisRecord.setLESION_OPEN_WND(strRecord.substring(537, 538).intern());

        //Format: X
        oasisRecord.setPRESS_ULCER(strRecord.substring(538, 539).intern());

        //Format: XX
        oasisRecord.setNBR_PRSULC_STG1(strRecord.substring(539, 541).intern());

        //Format: XX
        oasisRecord.setNBR_PRSULC_STG2(strRecord.substring(541, 543).intern());

        //Format: XX
        oasisRecord.setNBR_PRSULC_STG3(strRecord.substring(543, 545).intern());

        //Format: XX
        oasisRecord.setNBR_PRSULC_STG4(strRecord.substring(545, 547).intern());

        //Format: X
        oasisRecord.setUNOBS_PRSULC(strRecord.substring(547, 548).intern());

        //Format: XX
        oasisRecord.setSTG_PRBLM_ULCER(strRecord.substring(548, 550).intern());

        //Format: X
        oasisRecord.setSTAS_ULCR_PRSNT(strRecord.substring(552, 553).intern());

        //Format: XX
        oasisRecord.setNBR_STAS_ULCR(strRecord.substring(553, 555).intern());

        //Format: X
        oasisRecord.setUNOBS_STASULC(strRecord.substring(555, 556).intern());

        //Format: XX
        oasisRecord.setSTUS_PRBLM_STAS_ULCR(strRecord.substring(556, 558).intern());

        //Format: X
        oasisRecord.setSRGCL_WND_PRSNT(strRecord.substring(558, 559).intern());

        //Format: XX
        oasisRecord.setNBR_SURGWND(strRecord.substring(559, 561).intern());

        //Format: XX
        oasisRecord.setSTUS_PRBLM_SRGCL_WND(strRecord.substring(562, 564).intern());

        //Format: XX
        oasisRecord.setWHEN_DYSPNEIC(strRecord.substring(564, 566).intern());

        //Format: XX
        oasisRecord.setUR_INCONT(strRecord.substring(572, 574).intern());

        //Format: XX
        oasisRecord.setBWL_INCONT(strRecord.substring(576, 578).intern());

        //Format: XX
        oasisRecord.setOSTOMY(strRecord.substring(578, 580).intern());

        //Format: XX
        oasisRecord.setCRNT_DRESS_UPPER(strRecord.substring(615, 617).intern());

        //Format: XX
        oasisRecord.setCRNT_DRESS_LOWER(strRecord.substring(619, 621).intern());

        //Format: XX
        oasisRecord.setCRNT_BATHG(strRecord.substring(623, 625).intern());

        //Format: XX
        oasisRecord.setCRNT_TOILTG(strRecord.substring(627, 629).intern());

        //Format: XX
        oasisRecord.setCRNT_TRNSFRNG(strRecord.substring(631, 633).intern());

        //Format: XX
        oasisRecord.setCRNT_AMBLTN(strRecord.substring(635, 637).intern());

        //Format: XX
        oasisRecord.setCRNT_MGMT_INJCTN_MDCTN(strRecord.substring(675, 677).intern());

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
        tmpStr = strRecord.substring(864, 867);
        try {
            if ("   ".equals(tmpStr)) {
                oasisRecord.setTHER_NEED_NBR(-1);
                logger.log(Level.SEVERE, "HH-PPS: Record: {0}: missing M0826_THER_NEED_NUM of ''  ''",
                        recNum);

            } else {
                oasisRecord.setTHER_NEED_NBR(IntegerUtils.parseInt(tmpStr, Integer.MAX_VALUE));
            }
        } catch (NumberFormatException e) {
            oasisRecord.setTHER_NEED_NBR(-1);
            logger.log(Level.SEVERE, "HH-PPS: Record: {0}: Invalid Or missing M0826_THER_NEED_NUM of ''{1}''",
                    new Object[]{recNum, tmpStr});
        }

        //Format: 9
        oasisRecord.setTHER_NEED_NA(strRecord.substring(867, 868).intern());

        //Format: X(5)
        oasisRecord.setHIPPS_CODE(strRecord.substring(1080, 1085).intern());

        //
        // The non-home health data with the OASIS record
        //
        if (!skipPassthru) {
            //PassThru
            oasisRecord.setPassThru_1(strRecord.substring(0, 176).intern());

            //PassThru
            oasisRecord.setPassThru_2(strRecord.substring(184, 301).intern());

            //PassThru
            oasisRecord.setPassThru_3(strRecord.substring(311, 403).intern());

            //PassThru
            oasisRecord.setPassThru_4(strRecord.substring(410, 412).intern());

            //PassThru
            oasisRecord.setPassThru_5(strRecord.substring(419, 421).intern());

            //PassThru
            oasisRecord.setPassThru_6(strRecord.substring(428, 430).intern());

            //PassThru
            oasisRecord.setPassThru_7(strRecord.substring(437, 439).intern());

            //PassThru
            oasisRecord.setPassThru_8(strRecord.substring(446, 448).intern());

            //PassThru
            oasisRecord.setPassThru_9(strRecord.substring(455, 457).intern());

            //PassThru
            oasisRecord.setPassThru_10(strRecord.substring(461, 528).intern());

            //PassThru
            oasisRecord.setPassThru_11(strRecord.substring(530, 534).intern());

            //PassThru
            oasisRecord.setPassThru_12(strRecord.substring(536, 537).intern());

            //PassThru
            oasisRecord.setPassThru_13(strRecord.substring(550, 552).intern());

            //PassThru
            oasisRecord.setPassThru_14(strRecord.substring(561, 562).intern());

            //PassThru
            oasisRecord.setPassThru_15(strRecord.substring(566, 572).intern());

            //PassThru
            oasisRecord.setPassThru_16(strRecord.substring(574, 576).intern());

            //PassThru
            oasisRecord.setPassThru_17(strRecord.substring(580, 615).intern());

            //PassThru
            oasisRecord.setPassThru_18(strRecord.substring(617, 619).intern());

            //PassThru
            oasisRecord.setPassThru_19(strRecord.substring(621, 623).intern());

            //PassThru
            oasisRecord.setPassThru_20(strRecord.substring(625, 627).intern());

            //PassThru
            oasisRecord.setPassThru_21(strRecord.substring(629, 631).intern());

            //PassThru
            oasisRecord.setPassThru_22(strRecord.substring(633, 635).intern());

            //PassThru
            oasisRecord.setPassThru_23(strRecord.substring(637, 675).intern());

            //PassThru
            oasisRecord.setPassThru_24(strRecord.substring(677, 778).intern());

            //PassThru
            oasisRecord.setPassThru_25(strRecord.substring(868, 1080).intern());

            int idx = oasisRecord.getPassThru_25().indexOf("Record: ");
            if (idx > -1) {
                String str = oasisRecord.getPassThru_25().substring(idx + 8);
                idx = str.indexOf(" ");
                if (idx > -1) {
                    try {
                        oasisRecord.setTestRecordId(IntegerUtils.parseInt(str.substring(0, idx), Integer.MAX_VALUE));
                    } catch (Exception e) {
                        oasisRecord.setTestRecordId(-1);
                    }
                }
            }

            //PassThru
            oasisRecord.setPassThru_26(strRecord.substring(1085, 1445).intern());
        }

        return oasisRecord;

    }

    /**
     * Converts an OASIS record extended from the HomeHealth Record to a String
     * using the delimiter to separate the values
     *
     * @param homeHealthRecord
     * @param delimiter
     * @return
     */
    @Override
    public StringBuilder convertFromHomeHealthRecDelimeted(
            HomeHealthRecordIF homeHealthRecord, String delimiter) {

        StringBuilder record;
        Oasis_B_Record oasisRecord;

        if (delimiter == null) {
            delimiter = "";
        }
        record = new StringBuilder(1500);

        // make sure there is enough information in the object
        // so that we can output the full string of data.
        if (homeHealthRecord instanceof Oasis_B_Record) {
            oasisRecord = (Oasis_B_Record) homeHealthRecord;
        } else if (homeHealthRecord instanceof HomeHealthRecord) {
            oasisRecord = new Oasis_B_Record((HomeHealthRecord) homeHealthRecord);
            oasisRecord.setPassThruDefaults();
        } else {
            oasisRecord = new Oasis_B_Record();
            oasisRecord.setPassThruDefaults();
        }

        //PassThru
        record.append(oasisRecord.getPassThru_1());
        record.append(delimiter);

        //Format: YYYYMMDD
        OasisCalendarFormatter.format(oasisRecord.getSTART_CARE_DT(), record);
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_2());
        record.append(delimiter);

        //Format: YYYYMMDD
        OasisCalendarFormatter.format(oasisRecord.getINFO_COMPLETED_DT(), record);
        record.append(delimiter);

        //Format: 99
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getASSMT_REASON(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_3());
        record.append(delimiter);

        //Format: XX99.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPRIMARY_DIAG_ICD()));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_4());
        record.append(delimiter);

        //Format: XX99.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getOTH_DIAG1_ICD()));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_5());
        record.append(delimiter);

        //Format: XX99.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getOTH_DIAG2_ICD()));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_6());
        record.append(delimiter);

        //Format: XX99.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getOTH_DIAG3_ICD()));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_7());
        record.append(delimiter);

        //Format: XX99.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getOTH_DIAG4_ICD()));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_8());
        record.append(delimiter);

        //Format: XX99.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getOTH_DIAG5_ICD()));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_9());
        record.append(delimiter);

        //Format: X
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getTHH_IV_INFUSION(), 1, '0'));
        record.append(delimiter);

        //Format: X
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getTHH_PAR_NUTRITION(), 1, '0'));
        record.append(delimiter);

        //Format: X
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getTHH_ENT_NUTRITION(), 1, '0'));
        record.append(delimiter);

        //Format: X
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getTHH_NONE_ABOVE(), 1, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_10());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getVISION(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_11());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getPAIN_FREQ_ACTVTY_MVMT(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_12());
        record.append(delimiter);

        //Format: X
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getLESION_OPEN_WND(), 1, '0'));
        record.append(delimiter);

        //Format: X
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getPRESS_ULCER(), 1, '0'));
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getNBR_PRSULC_STG1(), 2, '0'));
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getNBR_PRSULC_STG2(), 2, '0'));
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getNBR_PRSULC_STG3(), 2, '0'));
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getNBR_PRSULC_STG4(), 2, '0'));
        record.append(delimiter);

        //Format: X
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getUNOBS_PRSULC(), 1, '0'));
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getSTG_PRBLM_ULCER(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_13());
        record.append(delimiter);

        //Format: X
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getSTAS_ULCR_PRSNT(), 1, '0'));
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getNBR_STAS_ULCR(), 2, '0'));
        record.append(delimiter);

        //Format: X
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getUNOBS_STASULC(), 1, '0'));
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getSTUS_PRBLM_STAS_ULCR(), 2, '0'));
        record.append(delimiter);

        //Format: X
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getSRGCL_WND_PRSNT(), 1, '0'));
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getNBR_SURGWND(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_14());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getSTUS_PRBLM_SRGCL_WND(), 2, '0'));
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getWHEN_DYSPNEIC(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_15());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getUR_INCONT(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_16());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getBWL_INCONT(), 2, '0'));
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getOSTOMY(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_17());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getCRNT_DRESS_UPPER(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_18());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getCRNT_DRESS_LOWER(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_19());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getCRNT_BATHG(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_20());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getCRNT_TOILTG(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_21());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getCRNT_TRNSFRNG(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_22());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getCRNT_AMBLTN(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_23());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getCRNT_MGMT_INJCTN_MDCTN(), 2, '0'));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_24());
        record.append(delimiter);

        //Format: XX
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getEPISODE_TIMING(), 2, '0'));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_A3()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_B3()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_C3()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_D3()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_E3()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_F3()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_A4()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_B4()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_C4()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_D4()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_E4()));
        record.append(delimiter);

        //Format: X999.XX
        record.append(formatDiagnosisCodeI9(oasisRecord.getPMT_DIAG_ICD_F4()));
        record.append(delimiter);

        //Format: 999
        if (oasisRecord.getTHER_NEED_NBR() > -1) {
            record.append(IntegerFormat.INTEGER_FORMAT_DIGITS_3.format(oasisRecord.getTHER_NEED_NBR()));
        } else {
            record.append("   ");
        }
        record.append(delimiter);

        //Format: 9
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getTHER_NEED_NA(), 1, ' '));
        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_25());
        record.append(delimiter);

        //Format: X(5)
        record.append(HomeHealthRecordUtil.justifyRight(oasisRecord.getHIPPS_CODE(), 5, ' '));

        record.append(delimiter);

        //PassThru
        record.append(oasisRecord.getPassThru_26());
        record.append(delimiter);

        record.append("%\r\n");

        return record;
    }

    /**
     * Builds a header record explaining the format of the OASIS record. Usually
     * used at the top of a text file.
     *
     * @param delimiter
     * @return
     */
    public StringBuilder toHeaderOasisRecDelimeted(String delimiter) {

        StringBuilder record;
        record = new StringBuilder(1500);

        if (delimiter == null) {
            delimiter = "";
        }

        //PassThru
        record.append("PassThru_1");
        record.append(delimiter);

        //Format: YYYYMMDD
        record.append("M0030_START_CARE_DT");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_2");
        record.append(delimiter);

        //Format: YYYYMMDD
        record.append("M0090_INFO_COMPLETED_DT");
        record.append(delimiter);

        //Format: 99
        record.append("M0100_ASSMT_REASON");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_3");
        record.append(delimiter);

        //Format: XX99.XX
        record.append("M0230_PRIMARY_DIAG_ICD");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_4");
        record.append(delimiter);

        //Format: XX99.XX
        record.append("M0240_OTH_DIAG1_ICD");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_5");
        record.append(delimiter);

        //Format: XX99.XX
        record.append("M0240_OTH_DIAG2_ICD");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_6");
        record.append(delimiter);

        //Format: XX99.XX
        record.append("M0240_OTH_DIAG3_ICD");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_7");
        record.append(delimiter);

        //Format: XX99.XX
        record.append("M0240_OTH_DIAG4_ICD");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_8");
        record.append(delimiter);

        //Format: XX99.XX
        record.append("M0240_OTH_DIAG5_ICD");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_9");
        record.append(delimiter);

        //Format: X
        record.append("M0250_THH_IV_INFUSION");
        record.append(delimiter);

        //Format: X
        record.append("M0250_THH_PAR_NUTRITION");
        record.append(delimiter);

        //Format: X
        record.append("M0250_THH_ENT_NUTRITION");
        record.append(delimiter);

        //Format: X
        record.append("M0250_THH_NONE_ABOVE");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_10");
        record.append(delimiter);

        //Format: XX
        record.append("M0390_VISION");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_11");
        record.append(delimiter);

        //Format: XX
        record.append("M0420_FREQ_PAIN");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_12");
        record.append(delimiter);

        //Format: X
        record.append("M0440_LESION_OPEN_WND");
        record.append(delimiter);

        //Format: X
        record.append("M0445_PRESS_ULCER");
        record.append(delimiter);

        //Format: XX
        record.append("M0450_NBR_PRSULC_STG1");
        record.append(delimiter);

        //Format: XX
        record.append("M0450_NBR_PRSULC_STG2");
        record.append(delimiter);

        //Format: XX
        record.append("M0450_NBR_PRSULC_STG3");
        record.append(delimiter);

        //Format: XX
        record.append("M0450_NBR_PRSULC_STG4");
        record.append(delimiter);

        //Format: X
        record.append("M0450_UNOBS_PRSULC");
        record.append(delimiter);

        //Format: XX
        record.append("M0460_STG_PRBLM_ULCER");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_13");
        record.append(delimiter);

        //Format: X
        record.append("M0468_STASIS_ULCER");
        record.append(delimiter);

        //Format: XX
        record.append("M0470_NBR_STASULC");
        record.append(delimiter);

        //Format: X
        record.append("M0474_UNOBS_STASULC");
        record.append(delimiter);

        //Format: XX
        record.append("M0476_STAT_PRB_STASULC");
        record.append(delimiter);

        //Format: X
        record.append("M0482_SURG_WOUND");
        record.append(delimiter);

        //Format: XX
        record.append("M0484_NBR_SURGWND");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_14");
        record.append(delimiter);

        //Format: XX
        record.append("M0488_STAT_PRB_SURGWND");
        record.append(delimiter);

        //Format: XX
        record.append("M0490_WHEN_DYSPNEIC");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_15");
        record.append(delimiter);

        //Format: XX
        record.append("M0520_UR_INCONT");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_16");
        record.append(delimiter);

        //Format: XX
        record.append("M0540_BWL_INCONT");
        record.append(delimiter);

        //Format: XX
        record.append("M0550_OSTOMY");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_17");
        record.append(delimiter);

        //Format: XX
        record.append("M0650_CUR_DRESS_UPPER");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_18");
        record.append(delimiter);

        //Format: XX
        record.append("M0660_CUR_DRESS_LOWER");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_19");
        record.append(delimiter);

        //Format: XX
        record.append("M0670_CUR_BATHING");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_20");
        record.append(delimiter);

        //Format: XX
        record.append("M0680_CUR_TOILETING");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_21");
        record.append(delimiter);

        //Format: XX
        record.append("M0690_CUR_TRANSFERRING");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_22");
        record.append(delimiter);

        //Format: XX
        record.append("M0700_CUR_AMBULATION");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_23");
        record.append(delimiter);

        //Format: XX
        record.append("M0800_CUR_INJECT_MEDS");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_24");
        record.append(delimiter);

        //Format: XX
        record.append("M0110_EPISODE_TIMING");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_A3");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_B3");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_C3");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_D3");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_E3");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_F3");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_A4");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_B4");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_C4");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_D4");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_E4");
        record.append(delimiter);

        //Format: X999.XX
        record.append("M0246_PMT_DIAG_ICD_F4");
        record.append(delimiter);

        //Format: 999
        record.append("M0826_THER_NEED_NUM");
        record.append(delimiter);

        //Format: 9
        record.append("M0826_THER_NEED_NA");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_25");
        record.append(delimiter);

        //Format: X(5)
        record.append("HIPPS_CODE");
        record.append(delimiter);

        //PassThru
        record.append("PassThru_26");

        record.append("%\r\n");

        return record;
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

}
