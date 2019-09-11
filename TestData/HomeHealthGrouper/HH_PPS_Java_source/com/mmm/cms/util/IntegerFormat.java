/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.util;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Currency;

/**
 * This provides a concrete wrapper around the number format in order to allow
 * output of decimal values. Besides limiting the decimal methods, it provides
 * not additional functionality. Decimal values are not supported.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class IntegerFormat extends NumberFormat {

    /**
     * 3 digit integer formatter
     */
    public static final IntegerFormat INTEGER_FORMAT_DIGITS_3;
    /**
     * 2 digit integer formatter
     */
    public static final IntegerFormat INTEGER_FORMAT_DIGITS_2;

    static {
        INTEGER_FORMAT_DIGITS_2 = new IntegerFormat('0');
        INTEGER_FORMAT_DIGITS_2.setMinimumIntegerDigits(2);
        INTEGER_FORMAT_DIGITS_2.setMaximumIntegerDigits(2);

        INTEGER_FORMAT_DIGITS_3 = new IntegerFormat('0');
        INTEGER_FORMAT_DIGITS_3.setMinimumIntegerDigits(3);
        INTEGER_FORMAT_DIGITS_3.setMaximumIntegerDigits(3);
    }

    private char fillerChar;

    /**
     * Constructor which sets the fill character to the default of 1 space, i.e.
     * ' '
     */
    public IntegerFormat() {
        fillerChar = ' ';
    }

    /**
     * Constructor that sets the filler to the supplied character
     *
     * @param filler
     */
    public IntegerFormat(char filler) {
        fillerChar = filler;
    }

    /**
     * Get the value of fillerChar
     *
     * @return the value of fillerChar
     */
    public char getFillerChar() {
        return fillerChar;
    }

    /**
     * Set the value of fillerChar
     *
     * @param fillerChar new value of fillerChar
     */
    public void setFillerChar(char fillerChar) {
        this.fillerChar = fillerChar;
    }

    /**
     * Not supported for formatting double values
     *
     * @param number
     * @param toAppendTo
     * @param pos
     * @return
     */
    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        throw new UnsupportedOperationException();
    }

    /**
     * Formats a long value to include the filler character if needed
     *
     * @param number
     * @param toAppendTo
     * @param pos
     * @return
     */
    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        final int maxDigits = getMaximumIntegerDigits();
        String str;
        int len;

        // convert to the string and get its length to check
        str = Long.toString(number);
        len = str.length();

        if (len == maxDigits) {
            // use the value as is
            toAppendTo.append(str);

        } else if (len > maxDigits) {
			// just append the right digits
            // i.e. - truncate the left digits
            toAppendTo.append(str.substring(len - maxDigits));

        } else if (len < maxDigits) {
			// the string is not long enough to fit the required minimum
            // so left fill the data with the fillchar
            final int fillCount = maxDigits - len;
            char[] fillChars = new char[fillCount];
            for (int idx = 0; idx < fillChars.length; idx++) {
                fillChars[idx] = fillerChar;
            }
            toAppendTo.append(fillChars);
            toAppendTo.append(str);
        }

        return toAppendTo;
    }

    /**
     * Parses an integer based on the source string. Because it uses
     * Integer.parseInt() it may throw a NumberFormatException when a bad number
     * string is provided.
     *
     * @param source
     * @param parsePosition
     * @throws NumberFormatException
     * @return
     */
    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        return parsePosition == null ? Integer.parseInt(source) : Integer.parseInt(source.substring(parsePosition.getIndex()));
    }

    /**
     * Not supported
     *
     * @param currency
     */
    @Override
    public void setCurrency(Currency currency) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported
     *
     * @param newValue
     */
    @Override
    public void setMaximumFractionDigits(int newValue) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported
     *
     * @param newValue
     */
    @Override
    public void setMinimumFractionDigits(int newValue) {
        throw new UnsupportedOperationException();
    }
}
