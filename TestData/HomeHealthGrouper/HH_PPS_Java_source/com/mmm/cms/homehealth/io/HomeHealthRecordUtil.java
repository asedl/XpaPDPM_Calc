/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.io;

import com.mmm.cms.homehealth.io.record.Oasis_B_Record;
import com.mmm.cms.homehealth.io.record.Oasis_C_Record;
import com.mmm.cms.homehealth.DiagnosisCode;
import com.mmm.cms.homehealth.DiagnosisCode_Empty;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_B_IF;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecord_C_IF;
import com.mmm.cms.util.IntegerUtils;
import com.mmm.cms.util.ValidateUtils;
import javax.xml.transform.sax.TransformerHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Provides generalized functionality for reading and writing a Home Health
 * Record
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HomeHealthRecordUtil {

    public static Node createHomeHealthNode(Document document, String name,
            String value) {
        Node node;

        node = document.createTextNode(value);
        return node;
    }

    public static void buildTransformerNode(
            TransformerHandler transformerHandler, String name, String value) throws
            SAXException {
        String tmpStr;

        transformerHandler.startElement("", "", name, new AttributesImpl());

        // trim the value - don't care to put in blanks
        if (value != null) {
            tmpStr = value.trim();
        } else {
            tmpStr = "";
        }

        if (!tmpStr.isEmpty()) {
            transformerHandler.characters(tmpStr.toCharArray(), 0, tmpStr.length());
        }

        transformerHandler.endElement("", "", name);
    }

    /**
     * This takes a HomeHealthRecord_C_IF object and converts it to a basic
     * HomeHealthRecord_IF, mapping any values from the C to the basic where
     * needed. The original recordOasisC is not altered.
     *
     * @param record_C
     * @return a non-null, Home Health Record that contains values formatted for
     * scoring with the OASIS-B logic
     */
    public static HomeHealthRecord_B_IF convertToHomeHealthRecord_OasisB(
            HomeHealthRecord_C_IF record_C,
            boolean mapValues) {
        final Oasis_B_Record record_B = new Oasis_B_Record();
        String str;
        int tmpInt;

        record_B.setSTART_CARE_DT(record_C.getSTART_CARE_DT());

        record_B.setINFO_COMPLETED_DT(record_C.getINFO_COMPLETED_DT());

        //Format: 99
        record_B.setASSMT_REASON(record_C.getASSMT_REASON());

        //Format: XX99.XX
        record_B.setPRIMARY_DIAG_ICD(record_C.getPRIMARY_DIAG_ICD());

        //Format: XX99.XX
        record_B.setOTH_DIAG1_ICD(record_C.getOTH_DIAG1_ICD());

        //Format: XX99.XX
        record_B.setOTH_DIAG2_ICD(record_C.getOTH_DIAG2_ICD());

        //Format: XX99.XX
        record_B.setOTH_DIAG3_ICD(record_C.getOTH_DIAG3_ICD());

        //Format: XX99.XX
        record_B.setOTH_DIAG4_ICD(record_C.getOTH_DIAG4_ICD());

        //Format: XX99.XX
        record_B.setOTH_DIAG5_ICD(record_C.getOTH_DIAG5_ICD());

        //Format: X
        record_B.setTHH_IV_INFUSION(record_C.getTHH_IV_INFUSION());

        //Format: X
        record_B.setTHH_PAR_NUTRITION(record_C.getTHH_PAR_NUTRITION());

        //Format: X
        record_B.setTHH_ENT_NUTRITION(record_C.getTHH_ENT_NUTRITION());

        //Format: X
        record_B.setTHH_NONE_ABOVE(record_C.getTHH_NONE_ABOVE());

        //Format: XX
        record_B.setVISION(record_C.getVISION());

        //Format: XX
        str = record_C.getPAIN_FREQ_ACTVTY_MVMT();
        if (mapValues) {
            if ("00".equals(str) || "01".equals(str)) {
                record_B.setPAIN_FREQ_ACTVTY_MVMT("00");
            } else if ("02".equals(str)) {
                record_B.setPAIN_FREQ_ACTVTY_MVMT("01");
            } else if ("03".equals(str)) {
                record_B.setPAIN_FREQ_ACTVTY_MVMT("02");
            } else if ("04".equals(str)) {
                record_B.setPAIN_FREQ_ACTVTY_MVMT("03");
            } else {
                record_B.setPAIN_FREQ_ACTVTY_MVMT(str);
            }
        } else {
            record_B.setPAIN_FREQ_ACTVTY_MVMT(str);
        }

        /**
         * Format: X
         *
         * @deprecated - OASIS-C no longer use this variable for validation of
         * Ulcers
         */
        record_B.setLESION_OPEN_WND(record_C.getLESION_OPEN_WND());

        //Format: X
        record_B.setPRESS_ULCER(record_C.getUNHLD_STG2_PRSR_ULCR());

        //Format: XX
        str = record_C.getNBR_PRSULC_STG1();
        if (mapValues) {
            try {
                tmpInt = IntegerUtils.parseInt(str, 0);
                if (tmpInt > 4) {
                    record_B.setNBR_PRSULC_STG1("04");
                } else {
                    record_B.setNBR_PRSULC_STG1(str);
                }
            } catch (NumberFormatException e) {
                // set the value to blank
                record_B.setNBR_PRSULC_STG1("  ");
            }
        } else {
            record_B.setNBR_PRSULC_STG1(str);
        }

        //Format: XX
        str = record_C.getNBR_PRSULC_STG2();
        if (mapValues) {
            try {
                tmpInt = IntegerUtils.parseInt(str, 0);
                if (tmpInt > 4) {
                    record_B.setNBR_PRSULC_STG2("04");
                } else {
                    record_B.setNBR_PRSULC_STG2(str);
                }
            } catch (NumberFormatException e) {
                // set the value to blank
                record_B.setNBR_PRSULC_STG2("  ");
            }
        } else {
            record_B.setNBR_PRSULC_STG2(str);
        }

        //Format: XX
        str = record_C.getNBR_PRSULC_STG3();
        if (mapValues) {
            try {
                tmpInt = IntegerUtils.parseInt(str, 0);
                if (tmpInt > 4) {
                    record_B.setNBR_PRSULC_STG3("04");
                } else {
                    record_B.setNBR_PRSULC_STG3(str);
                }
            } catch (NumberFormatException e) {
                // set the value to blank
                record_B.setNBR_PRSULC_STG3("  ");
            }
        } else {
            record_B.setNBR_PRSULC_STG3(str);
        }

        //Format: XX
        str = record_C.getNBR_PRSULC_STG4();
        if (mapValues) {
            try {
                tmpInt = IntegerUtils.parseInt(str, 0);
                if (tmpInt > 4) {
                    record_B.setNBR_PRSULC_STG4("04");
                } else {
                    record_B.setNBR_PRSULC_STG4(str);
                }
            } catch (NumberFormatException e) {
                // set the value to blank
                record_B.setNBR_PRSULC_STG4("  ");
            }
        } else {
            record_B.setNBR_PRSULC_STG4(str);
        }

        //Format: X
        // Oasis-C 1308 (d1, d2, d3) variables do not directly map to
        // Oasis-B M0450 - either d1 or d2 will trigger a value for
        // the Oasis-B field
        record_B.setUNOBS_PRSULC(" ");
        str = record_C.getNSTG_DRSG();
        if (mapValues) {
            try {
                tmpInt = IntegerUtils.parseInt(str, 0);
                if (tmpInt > 0 && tmpInt <= 99) {
                    record_B.setUNOBS_PRSULC("1");
                }
            } catch (NumberFormatException e) {
            }

            str = record_C.getNSTG_CVRG();
            try {
                tmpInt = IntegerUtils.parseInt(str, 0);
                if (tmpInt > 0 && tmpInt <= 99) {
                    record_B.setUNOBS_PRSULC("1");
                }
            } catch (NumberFormatException e) {
            }
        }

        //Format: XX
        record_B.setSTG_PRBLM_ULCER(record_C.getSTG_PRBLM_ULCER());

        //-----
        // Stasis Ulcers - observered and unobservered mapping
        //Format: X
        // the statis ulcer is only one character in OASIS-B (M0468)
        // but is it two in OASIS-C (M1330)
        //Format: X
        // Unobserved Stasis Ulcer in OASIS-B (M0474) is not directly
        // represented in OASIS-C (M1330), but uses a specific value
        // of that field to determine the Unobserved value
        str = record_C.getSTAS_ULCR_PRSNT();
        if (str != null) {
            str = str.trim();
            if (str.length() == 2) {
                str = str.substring(1);
            } else if (str.isEmpty()) {
                str = ValidateUtils.SPACE_1;
            }
        }

        if (mapValues) {
            // at this point the value of str is only one character long
            // Stasis Ulcer indicator in OasisC has the following meanings:
            // 00 = patient does not have any stasis ulcers
            // 01 = patient has both observable and unobservable
            // 02 = patient has observable only
            // 03 = patient has only an unobservable stasis ulcer
            if ("3".equals(str)) {
                record_B.setSTAS_ULCR_PRSNT("0");
                record_B.setUNOBS_STASULC("1");
            } else if ("2".equals(str)) {
                record_B.setSTAS_ULCR_PRSNT("1");
                record_B.setUNOBS_STASULC("0");
            } else if ("1".equals(str)) {
                record_B.setSTAS_ULCR_PRSNT("1");
                record_B.setUNOBS_STASULC("1");
            } else {
                record_B.setSTAS_ULCR_PRSNT(str);
                record_B.setUNOBS_STASULC("0");
            }
        } else {
            record_B.setSTAS_ULCR_PRSNT(str);
            record_B.setUNOBS_STASULC(str);
        }

        //Format: XX
        str = record_C.getNBR_STAS_ULCR();
        if (mapValues) {
            if (ValidateUtils.SPACE_2.equals(str)) {
                record_B.setNBR_STAS_ULCR("00");
            } else {
                record_B.setNBR_STAS_ULCR(str);
            }
        } else {
            record_B.setNBR_STAS_ULCR(str);
        }

        //Format: XX
        str = record_C.getSTUS_PRBLM_STAS_ULCR();
        if (mapValues) {
            if ("00".equals(str)) {
                record_B.setSTUS_PRBLM_STAS_ULCR("NA");
            } else {
                record_B.setSTUS_PRBLM_STAS_ULCR(str);
            }
        } else {
            record_B.setSTUS_PRBLM_STAS_ULCR(str);
        }

        //Format: X
        // Surgical wound is two characters in OASIS-C (M1340)
        // but only one character in OASIS-B (M0482)
        str = record_C.getSRGCL_WND_PRSNT();
        if (str != null) {
            str = str.trim();
            if (str.length() == 2) {
                str = str.substring(1);
            } else if (str.isEmpty()) {
                str = ValidateUtils.SPACE_1;
            }
        }

        if (mapValues) {
            if ("02".equals(str)) {
                record_B.setSRGCL_WND_PRSNT("01");
            } else {
                record_B.setSRGCL_WND_PRSNT(str);
            }
        } else {
            record_B.setSRGCL_WND_PRSNT(str);
        }

        //Format: XX
        record_B.setNBR_SURGWND(record_C.getSRGCL_WND_PRSNT());

        //Format: XX
        str = record_C.getSTUS_PRBLM_SRGCL_WND();
        if (mapValues) {
            if ("00".equals(str)) {
                record_B.setSTUS_PRBLM_SRGCL_WND("01");
            } else {
                record_B.setSTUS_PRBLM_SRGCL_WND(str);
            }
        } else {
            record_B.setSTUS_PRBLM_SRGCL_WND(str);
        }

        //Format: XX
        record_B.setWHEN_DYSPNEIC(record_C.getWHEN_DYSPNEIC());

        //Format: XX
        record_B.setUR_INCONT(record_C.getUR_INCONT());

        //Format: XX
        record_B.setBWL_INCONT(record_C.getBWL_INCONT());

        //Format: XX
        record_B.setOSTOMY(record_C.getOSTOMY());

        //Format: XX
        record_B.setCRNT_DRESS_UPPER(record_C.getCRNT_DRESS_UPPER());

        //Format: XX
        record_B.setCRNT_DRESS_LOWER(record_C.getCRNT_DRESS_LOWER());

        //Format: XX
        str = record_C.getCRNT_BATHG();
        if (mapValues) {
            if ("06".equals(str)) {
                record_B.setCRNT_BATHG("05");
            } else if ("05".equals(str)) {
                record_B.setCRNT_BATHG("04");
            } else {
                record_B.setCRNT_BATHG(str);
            }
        } else {
            record_B.setCRNT_BATHG(str);
        }

        //Format: XX
        record_B.setCRNT_TOILTG(record_C.getCRNT_TOILTG());

        //Format: XX
        record_B.setCRNT_TRNSFRNG(record_C.getCRNT_TRNSFRNG());

        //Format: XX
        str = record_C.getCRNT_AMBLTN();
        if (mapValues) {
            if ("02".equals(str)) {
                record_B.setCRNT_AMBLTN("01");
            } else if ("03".equals(str)) {
                record_B.setCRNT_AMBLTN("02");
            } else if ("04".equals(str)) {
                record_B.setCRNT_AMBLTN("03");
            } else if ("05".equals(str)) {
                record_B.setCRNT_AMBLTN("04");
            } else if ("06".equals(str)) {
                record_B.setCRNT_AMBLTN("05");
            } else {
                record_B.setCRNT_AMBLTN(str);
            }
        } else {
            record_B.setCRNT_AMBLTN(str);
        }

        //Format: XX
        str = record_C.getCRNT_MGMT_INJCTN_MDCTN();
        if (mapValues) {
            if ("02".equals(str)) {
                record_B.setCRNT_MGMT_INJCTN_MDCTN("01");
            } else if ("03".equals(str)) {
                record_B.setCRNT_MGMT_INJCTN_MDCTN("02");
            } else {
                record_B.setCRNT_MGMT_INJCTN_MDCTN(str);
            }
        } else {
            record_B.setCRNT_MGMT_INJCTN_MDCTN(str);
        }

        //Format: XX
        record_B.setEPISODE_TIMING(record_C.getEPISODE_TIMING());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_A3(record_C.getPMT_DIAG_ICD_A3());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_B3(record_C.getPMT_DIAG_ICD_B3());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_C3(record_C.getPMT_DIAG_ICD_C3());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_D3(record_C.getPMT_DIAG_ICD_D3());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_E3(record_C.getPMT_DIAG_ICD_E3());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_F3(record_C.getPMT_DIAG_ICD_F3());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_A4(record_C.getPMT_DIAG_ICD_A4());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_B4(record_C.getPMT_DIAG_ICD_B4());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_C4(record_C.getPMT_DIAG_ICD_C4());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_D4(record_C.getPMT_DIAG_ICD_D4());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_E4(record_C.getPMT_DIAG_ICD_E4());

        //Format: X999.XX
        record_B.setPMT_DIAG_ICD_F4(record_C.getPMT_DIAG_ICD_F4());

        //Format: 999
        record_B.setTHER_NEED_NBR(record_C.getTHER_NEED_NBR());

        //Format: 9
        record_B.setTHER_NEED_NA(record_C.getTHER_NEED_NA());

        return record_B;
    }

    /**
     * HH-PPS will not fail a DX code based on a pattern because it uses a valid
     * code set.
     *
     * For OASIS-C1, the dx code may look like the following: SUBMITTING ICD-10
     * DIAGNOSIS CODES If the ICD-10 item is active, it must be submitted as an
     * 8 character, fixed-format string with all blanks replaced with [^], and
     * with a decimal point as the 4th character. The only exception to this
     * rule is that an entirely blank ICD-10 item must be submitted as a single
     * [^]. For example: -The ICD-10 code "A12." would be submitted as
     * [A12.^^^^]. -The ICD-10 code "A12.3" would be submitted as [A12.3^^^].
     * -The ICD-10 code "A12.34" would be submitted as [A12.34^^]. -The ICD-10
     * code "A12.345" would be submitted as [A12.345^] -The ICD-10 code
     * "A12.3456" would be submitted as [A12.3456] -An entirely blank ICD-10
     * code would be submitted as [^].
     *
     * @param strDx
     * @return
     */
    public static DiagnosisCodeIF parseDxCode(String strDx) {
        DiagnosisCodeIF code;

        if (strDx == null) {
            code = DiagnosisCode_Empty.DEFAULT;
        } else {
            final String tmpStr = HomeHealthRecordUtil.trimWithCarets(strDx);
            if (tmpStr.isEmpty()) {
                code = DiagnosisCode_Empty.DEFAULT;
            } else {
                code = new DiagnosisCode(tmpStr);
            }
        }

        return code;
    }

    /**
     * The OASIS-C/C1 spec published in 2013 for use starting in Oct 2014 states
     * that the blank values can be identified with a single caret '^' and any
     * value that has leading or trailing spaces, such as a DX code, in these
     * record formats are replaced with the caret character as well. This method
     * will trim the code of any leading or trailing blank or caret characters.
     *
     * @param strOrig
     * @return
     */
    public static String trimWithCarets(String strOrig) {
        final int count = strOrig.length();
        int len = count;
        int st = 0;
        int off = 0;      /* avoid getfield opcode */

        char[] val = strOrig.toCharArray();    /* avoid getfield opcode */

        while ((st < len) && (val[off + st] <= ' ' || val[off + st] == '^')) {
            st++;
        }
        while ((st < len) && (val[off + len - 1] <= ' ' || val[off + len - 1] == '^')) {
            len--;
        }

        try {
            return (st > 0 || len < count) ? new String(val, st, len - st) : strOrig;
        } catch (Exception ex) {
            return strOrig;
        }
    }

    /**
     * For C 2.10 and C1 2.11, the Therapy Need Number can have a '^' as the
     * only character, or be 1 to 3 characters long. There should not be a '+'
     * or '-'. The number can have leading zeros or trailing spaces or both.
     *
     * Since this is converting to an int, then it will return
     *
     * @param strInt
     * @param defaultValue
     * @return value >= 0 for 0 to 999; default value (usually -1) for '^',
     * null, or blank; -2 for '+' or '-' in the string; -3 for other number
     * format error;
     */
    public static int parseTherapyNeedNumber_C1(String strInt, int defaultValue) {
        int value;

        if (strInt == null) {
            value = defaultValue;
        } else {
            strInt = strInt.trim();

            if (strInt.length() == 0 || strInt.charAt(0) == '^') {
                value = defaultValue;
            } else if (strInt.charAt(0) == '+' || strInt.charAt(0) == '-') {
                value = -2;
            } else {
                try {
                    value = IntegerUtils.parseInt(strInt, -3);
                } catch (NumberFormatException e) {
                    value = -3;
                }
            }
        }

        return value;
    }

    /**
     * This method returns a String that is right justified for the given
     * length.
     *
     * @param str
     * @param length
     * @return
     */
    public static String justifyRight(String str, int length, char fillchar) {
        StringBuilder buffer = new StringBuilder(length);
        int spaceCount;

        // ensure the string is not null and does not have any 
        // spaces
        str = (str == null ? "" : str.trim());

        spaceCount = length - str.length();
        while (spaceCount-- > 0) {
            buffer.append(fillchar);
        }
        buffer.append(str);

        return buffer.toString();
    }

    /**
     * due to edit -3100 of spec If the value of the field is not '^', then this
     * normalizes some fields that may not actually be the valid length but are
     * valid values.
     *
     * The following fields are allowed to have values shorter than the defined
     * length
     * <ul><li>M1308_NBR_PRSULC_STG2</li>
     * <li>M1308_NBR_PRSULC_STG3</li>
     * <li>M1308_NBR_PRSULC_STG4</li>
     * <li>M1308_NSTG_DRSG</li>
     * <li>M1308_NSTG_CVRG</li>
     * <li>M1308_NSTG_DEEP_TISUE</li>
     * <li>M2200_THER_NEED_NBR - this has already been converted to an int, so
     * no other processing</li>
     * </ul>
     *
     * @param oasisRecord
     */
    public static void justifyOasisCValues(Oasis_C_Record oasisRecord) {
        String str;
        final char caret = '^';

        oasisRecord.setNBR_PRSULC_STG2(getNormalizedIntValue(oasisRecord.getNBR_PRSULC_STG2()));
        oasisRecord.setNBR_STG2_AT_SOC_ROC(getNormalizedIntValue(oasisRecord.getNBR_STG2_AT_SOC_ROC()));   // M1308_NBR_STG2_AT_SOC_ROC - Number pu stage 2 at SOC/ROC

        oasisRecord.setNBR_PRSULC_STG3(getNormalizedIntValue(oasisRecord.getNBR_PRSULC_STG3()));
        oasisRecord.setNBR_STG3_AT_SOC_ROC(getNormalizedIntValue(oasisRecord.getNBR_STG3_AT_SOC_ROC()));   // M1308_NBR_STG3_AT_SOC_ROC - Number PU stage 3 at SOC/ROC

        oasisRecord.setNBR_PRSULC_STG4(getNormalizedIntValue(oasisRecord.getNBR_PRSULC_STG4()));
        oasisRecord.setNBR_STG4_AT_SOC_ROC(getNormalizedIntValue(oasisRecord.getNBR_STG4_AT_SOC_ROC()));   // M1308_NBR_STG4_AT_SOC_ROC - Number PU stage 4 at SOC/ROC

        oasisRecord.setNSTG_DRSG(getNormalizedIntValue(oasisRecord.getNSTG_DRSG()));
        oasisRecord.setNSTG_DRSG_SOC_ROC(getNormalizedIntValue(oasisRecord.getNSTG_DRSG_SOC_ROC()));   // M1308_NSTG_DRSG_SOC_ROC - Unstageable: non-removable dressing/device-SOC/ROC

        oasisRecord.setNSTG_CVRG(getNormalizedIntValue(oasisRecord.getNSTG_CVRG()));
        oasisRecord.setNSTG_CVRG_SOC_ROC(getNormalizedIntValue(oasisRecord.getNSTG_CVRG_SOC_ROC()));   // M1308_NSTG_CVRG_SOC_ROC - Unstageable: coverage by slough or eschar-SOC/ROC

        oasisRecord.setNSTG_DEEP_TISUE(getNormalizedIntValue(oasisRecord.getNSTG_DEEP_TISUE()));
        oasisRecord.setNSTG_DEEP_TISSUE_SOC_ROC(getNormalizedIntValue(oasisRecord.getNSTG_DEEP_TISSUE_SOC_ROC()));// M1308_NSTG_DEEP_TISUE_SOC_ROC - Unstageable: suspect deep tissue injury-SOC/ROC

    }

    public static String getNormalizedIntValue(String str) {
        if (str == null || str.isEmpty()) {
            str = "  ";
        } else if (str.length() == 2) {
            // blank strings or caret-blank strings should be left alone
            if (!"  ".equals(str) && !"^ ".equals(str)) {
                // the first character must be a digit
                if (Character.isDigit(str.charAt(0))) {
                    // if the second character is a space, then right justify it
                    if (str.charAt(1) == ' ') {
                        str = HomeHealthRecordUtil.justifyRight(str.trim(), 2, '0');
                    }
                }
            }
        } else if (str.length() == 1) {
            // the first character must be a digit or a caret.
            // if a digit then right justify it, otherwise leave it
            if (Character.isDigit(str.charAt(0))) {
                str = HomeHealthRecordUtil.justifyRight(str.trim(), 2, '0');
            }
        }

        return str;
    }

    /**
     * Ensures that a diagnosis code is 7 character long, padded with spaces
     * where necessary
     *
     * @param dxcode
     * @return
     */
    public static String formatDiagnosisCodeI9(DiagnosisCodeIF dxcode) {
        String value;

        value = dxcode.getCode();
        if (value != null && !value.isEmpty()) {
            if (value.length() < 7) {
                String prefix;
                String suffix;
                int pointIdx = value.indexOf(".");

                if (pointIdx > -1) {
                    prefix = value.substring(0, pointIdx);
                    if (pointIdx < value.length() - 1) {
                        suffix = value.substring(pointIdx + 1);
                    } else {
                        suffix = "  ";
                    }
                } else {
                    prefix = value;
                    suffix = "  ";
                }

                switch (prefix.length()) {
                    case 0:
                        prefix = "    ";
                        break;
                    case 1:
                        prefix = "   " + prefix;
                        break;
                    case 2:
                        prefix = "  " + prefix;
                        break;
                    case 3:
                        prefix = " " + prefix;
                        break;
                }

                switch (suffix.length()) {
                    case 0:
                        suffix = "  ";
                        break;
                    case 1:
                        suffix = suffix + " ";
                        break;
                }

                if (pointIdx > -1) {
                    value = prefix + "." + suffix;
                } else {
                    value = prefix + " " + suffix;
                }
            }
        } else {
            value = "       ";
        }
        return value;
    }
    
    public static String formatDxCode_C1(DiagnosisCodeIF dxCode) {
        return formatDxCode_C1(dxCode, 7);
    }

    public static String formatDxCode_C1(DiagnosisCodeIF dxCode, int codeLength) {
        String tmpStr = dxCode == null ? "" : dxCode.getCode();
        if (tmpStr.isEmpty()) {
            tmpStr = "^       ";
        } else if (tmpStr.length() < codeLength) {
            final StringBuilder buffer = new StringBuilder();
            justifyLeft(buffer, dxCode.getCode(), codeLength, '^');
            tmpStr = buffer.toString();
        }

        return tmpStr;
    }

    public static String formatTherapyNeedNum(int needNum) {
        final StringBuilder buffer = new StringBuilder();

        if (needNum < 10) {
            buffer.append("0");
        }
        if (needNum < 100) {
            buffer.append("0");
        }
        buffer.append(Integer.toString(needNum));
        return buffer.toString();
    }

    
    /**
     * This method adds to a StringBuidler a String that is left justified for
     * the given length and padded with the given fill character; Nulls are
     * treated as empty strings.
     *
     * @param buffer
     * @param str
     * @param length
     * @param fillchar
     * @return
     */
    public static StringBuilder justifyLeft(StringBuilder buffer, String str,
            int length, char fillchar) {
        int spaceCount = length - (str == null ? 0 : str.length());

        if (str != null) {
            buffer.append(str);
        }

        while (spaceCount-- > 0) {
            buffer.append(fillchar);
        }

        return buffer;
    }

    

    

}
