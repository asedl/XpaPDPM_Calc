/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.util;

import com.mmm.cms.homehealth.proto.CommonMessageText;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This formatter converts a Calendar to the day of the year consistent
 * Hexavigesimal value.  Any day of the year will be represented as the same
 * value no matter what year that day belongs to.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class HexavigesimalCalendarFormat extends Format {

    private static final HexavigesimalFormat hexavFormat;

    static {
        hexavFormat = new HexavigesimalFormat();
    }

    /**
     * Main testing module
     *
     * @param args
     */
    public static void main(String args[]) {
        HexavigesimalCalendarFormat format;
        Calendar cal;
        Calendar calendar;
        String converts[][] = new String[366][2];

        format = new HexavigesimalCalendarFormat();
        cal = new GregorianCalendar();
        cal.set(2012, Calendar.JANUARY, 1);
        System.out.print("Initial Date is: ");
        format.printlnCalendar(cal);
        System.out.println();

        for (int idx = 0; idx < converts.length; idx++) {
            String str = format.format(cal);

            converts[idx][0] = str;
            try {
                // convert it back
                calendar = (Calendar) format.parseObject("12" + str);
                converts[idx][1] = (calendar.get(Calendar.MONTH) + 1) + "/" 
                        + calendar.get(Calendar.DAY_OF_MONTH) + "/" 
                        + calendar.get(Calendar.YEAR);

            } catch (ParseException ex) {
                Logger.getLogger(format.getClass().getName()).log(Level.SEVERE, CommonMessageText.EVENT_MSG_HEADER, ex);
                converts[idx][1] = "Could not convert back";
            }
            cal.add(Calendar.DATE, 1);
        }
        
        for (int idx = 0; idx < converts.length; idx++) {
            System.out.println(converts[idx][0] + " = " 
                    + converts[idx][1]);
        }
        
    }

    /**
     * Converts an Object that is a Calendar to a Hexavigesimal value, so that any day of the
     * year is represented as the same value no matter which year it
     * belong do.
     *
     * @param obj
     * @param toAppendTo
     * @param pos
     * @return
     */
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
            FieldPosition pos) {
        if (obj instanceof GregorianCalendar) {
            return format((GregorianCalendar) obj, toAppendTo, pos);
        }

        throw new IllegalArgumentException("HexavigesimalCalendarFormat - only formats GregorianCalendar objects");
    }

    @Override
    public Object parseObject(String string) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        
        return parseObject(string, pos);
    }

    /**
     * This will parse a Hexavigesimal with two parts: Year and Day of year.
     * Each is two characters.  For example a standard date of 01/01/2012 
     * would become 12AA.  So, the source string is 4 characters and would be 
     * converted to a Calendar of 01/01/2012
     * The parsing assumes years beyond 2000
     *
     * @param source
     * @param pos
     * @return Calendar object with the converted date, or null if the date can
     * not be converted
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        Calendar calendar = null;

        if (source != null) {
            int idx = pos.getIndex();
            if (idx + 4 <= source.length()) {
                int year;
                int part1;
                int part2;
                int dayOfYear;

                year = 2000 + Integer.parseInt(source.substring(idx, idx + 2));
                part1 = source.charAt(idx + 2) - 'A';
                part2 = source.charAt(idx + 3) - 'A' + 1;
                dayOfYear = 26 * part1 + part2;

                calendar = new GregorianCalendar();
                calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
                calendar.set(Calendar.YEAR, year);
            }
        }

        return calendar;
    }

    /**
     * Converts a Calendar to a Hexavigesimal value, so that any day of the
     * year is represented as the same value no matter which year it
     * belong do.
     *
     * @param calendar
     * @param toAppendTo
     * @param pos
     * @return
     */
    public StringBuffer format(GregorianCalendar calendar, StringBuffer toAppendTo,
            FieldPosition pos) {

        int yearDay = calendar.get(Calendar.DAY_OF_YEAR);

        // if the day is after Feb 28 and it is NOT a leap year, add 1
        // so that we get the same number regardless of what year it is
        if (yearDay > 59
                && !calendar.isLeapYear(calendar.get(Calendar.YEAR))) {
            ++yearDay;
        }

        // add 26 in order for force the double character date
        return hexavFormat.format(yearDay + 26, toAppendTo, pos);
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
        System.out.print("(day " + calendar.get(Calendar.DAY_OF_YEAR) + ")");
    }

    /**
     * Utility method used for testing
     *
     * @param calendar
     */
    private void printlnCalendar(Calendar calendar) {
        printCalendar(calendar);
        System.out.println();
    }
}
