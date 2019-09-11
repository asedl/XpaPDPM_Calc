/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This formats the calendar to YYYYMMDD avoiding any calendar date calculations
 * so that even invalid dates will be properly formatted for OASIS compatibility
 *
 * @author 3M Heath Information Systems for CMS Home Health
 */
public class OasisCalendarFormatter {

	/**
	 * If the Calendar is non-null, it formats the Calendar to YYYYMMDD, or if
	 * null, the formats a blank 8 space string and places it directly into the
	 * StringBuilder
	 *
	 * @param calendar
	 * @param bufferAppendTo
	 * @return
	 */
	public static StringBuilder format(Calendar calendar, final StringBuilder bufferAppendTo) {
		if (calendar == null) {
			bufferAppendTo.append("        ");
		} else {
			bufferAppendTo.append(calendar.get(Calendar.YEAR));
			bufferAppendTo.append(IntegerFormat.INTEGER_FORMAT_DIGITS_2.format(calendar.get(Calendar.MONTH) + 1));
			bufferAppendTo.append(IntegerFormat.INTEGER_FORMAT_DIGITS_2.format(calendar.get(Calendar.DAY_OF_MONTH)));
		}

		return bufferAppendTo;
	}

	public static Calendar parse(String strCalendar) throws NumberFormatException, ParseException {
		GregorianCalendar calendar;
		calendar = new GregorianCalendar(Integer.parseInt(strCalendar.substring(0, 4)),
				Integer.parseInt(strCalendar.substring(4, 6)) - 1,
				Integer.parseInt(strCalendar.substring(6, 8)), 0, 0, 0);
		calendar.setLenient(false);
		try {
			calendar.getTimeInMillis();
		} catch (IllegalArgumentException e) {
			throw new ParseException(strCalendar + " - invalid " + e.getMessage(),
					"MONTH".equals(e.getMessage()) ? 1 : "YEAR".equals(e.getMessage()) ? 0 : 2);
		}

		return calendar;
	}
}
