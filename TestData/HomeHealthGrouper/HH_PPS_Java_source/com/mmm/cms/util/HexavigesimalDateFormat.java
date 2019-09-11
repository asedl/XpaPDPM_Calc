/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.util;

import com.mmm.cms.homehealth.proto.CommonMessageText;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This formatter converts a Date to the day of the year consistent
 * Hexavigesimal value.  Any day of the year will be represented as the same
 * value no matter what year that day belongs to.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class HexavigesimalDateFormat extends DateFormat {

    private static final HexavigesimalFormat hexavFormat;
    private static final GregorianCalendar gCalendar;
    
    static {
        hexavFormat = new HexavigesimalFormat();
        gCalendar = new GregorianCalendar();
        gCalendar.setLenient(false);
    }

    /**
     * Main testing module
     *
     * @param args
     */
    public static void main(String args[]) {
        HexavigesimalDateFormat format;
        Calendar cal;


        format = new HexavigesimalDateFormat();
        cal = new GregorianCalendar();
        cal.set(2006, Calendar.DECEMBER, 31);
        System.out.print("Initial Date is: ");
        format.printlnCalendar(cal);
        System.out.println();

        for (int j = 0; j < 1000; j++ ) {
            for (int idx = 0; idx < 366; idx++) {
                cal.add(Calendar.DATE, idx);
                format.format(cal.getTime());				
            }
        }
    }

    /**
     * Converts a date to a Hexavigesimal value, so that any day of the
     * year is represented as the same value no matter which year it
     * belong do.
     *
     * @param date
     * @param toAppendTo
     * @param fieldPosition
     * @return
     */
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo,
            FieldPosition fieldPosition) {

        int yearDay;
        synchronized(gCalendar) {
            gCalendar.setTime(date);
            yearDay = gCalendar.get(Calendar.DAY_OF_YEAR);

            // if the day is after Feb 28 and it is NOT a leap year, add 1
            // so that we get the same number regardless of what year it is
            if (yearDay > 59 &&
                !((GregorianCalendar) gCalendar).isLeapYear(gCalendar.get(Calendar.YEAR))) {
                ++yearDay;
            }
        }

        // add 26 in order for force the double character date
        return hexavFormat.format(yearDay + 26, toAppendTo, fieldPosition);
    }

    /**
     * Not supported
     *
     * @param source
     * @param pos
     * @return
     */
    @Override
    public Date parse(String source, ParsePosition pos) {
        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED);
    }

    /**
     * Utility method used for testing
     *
     * @param calendar
     */
    private void printCalendar(Calendar calendar) {
         // define output format and print
         SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm aaa");
         String date = sdf.format(calendar.getTime());
         System.out.print(date);
         System.out.print("(day " + calendar.get(Calendar.DAY_OF_YEAR) + ")" );
     }

    /**
     * Utility method used for testing
     * @param calendar
     */
    private void printlnCalendar(Calendar calendar) {
        printCalendar(calendar);
         System.out.println();
     }

}
