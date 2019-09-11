/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.cpp;

import com.mmm.cms.homehealth.pps.HH_PPS;
import com.mmm.cms.homehealth.proto.ScoringResultsIF;
import com.mmm.cms.util.ScoringResultsFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used allow a DLL to connect to the Java Home Health Grouper
 * implementation. It initializes a Grouper Factory, then allows the record
 * scoring,. with the results formatted as previous groupers had done.
 *
 * update: Jan 2015 - this module uses the HH_PPS class for all processing
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class DllToJavaConnector {

    public static boolean initComplete;
    protected static HH_PPS hhPps;

    /**
     * This initializes the Java Grouper version for use within a CPP-DLL. It
     * use environment variables in order to configure the grouper.
     */
    public static void init() {
        if (hhPps == null) {
            hhPps = HH_PPS.getInstance();
            hhPps.init();
            initComplete = true;
        }
    }

    /**
     * Please use the methods in HH_PPS instead of this - Scores the OASIS 
     * record and returns a basic string that must be parsed by the calling app.
     * 
     * @param record the Oasis B/C record as a string which gets converted to the
     * Home Health record
     * @return String in the format of HIPPS, OASIS TREATMENT, VERSION, FLAG as
     * on continuous array of characters 5 for HIPPS, 18 for OASIS, 6 for
     * version and 1 for flag.
     * 
     * @deprecated
     */
    public static String scoreOasisB(String record) {
        String retCodes = ScoringResultsIF.DEFAULT_BLANK_VALUE;

        try {
            init();
            if (initComplete) {
                ScoringResultsIF scoreResult = hhPps.scoreRecord(record);
                retCodes = ScoringResultsFormatter.formatContinousScore(scoreResult);
            }
        } catch (Throwable e) {
            Logger.getLogger(DllToJavaConnector.class.getName()).log(Level.SEVERE, "HH-PPS: Exception: "
                    + e.toString(), e);
        }

        return retCodes;
    }

    /**
     * This provides the ability to send in a reusable record 'buffer' and have
     * the results put into a reusable return 'buffer'. The return buffer must
     * be 30 characters long.
     *
     * @param record
     * @param returnData
     */
    public static void scoreOasis(char[] record, char[] returnData) {
        final String strRecord = new String(record);
        String strReturn;
        int len;

        init();
        strReturn = ScoringResultsFormatter.formatContinousScore(hhPps.scoreRecord(strRecord));
        len = strReturn.length();
        for (int idx = 0; idx < len; idx++) {
            returnData[idx] = strReturn.charAt(idx);
        }
    }

}
