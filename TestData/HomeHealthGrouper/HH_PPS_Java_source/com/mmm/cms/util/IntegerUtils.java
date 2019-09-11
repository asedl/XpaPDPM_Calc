/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.util;

/**
 * This provides generic functions for the Integer object and int type
 *
 * @author Tim Gallagher, 3M HIS, Clinical & Economic Research
 */
public class IntegerUtils {

    /**
     * This method parses an integer from the string. If the string does not
     * properly parse to an int, the default value it used. This avoids throwing
     * an exception which is expenses as far as processing.
     *
     * @param strInt
     * @param defaultValue
     * @return
     */
    public static int parseInt(String str, int defaultValue) {
        return parseInt(str, 10, defaultValue, true);

    }

    /**
     * This performs a similar parse to int as Integer.parseInt(String, int)
     * where the int is the radix. The difference is that when it encounters and
     * error, this method will return the default value instead of throwing an
     * exception, and this will trim the string prior to converting.
     *
     * The calling method can then check for the default value and assume an
     * error in parsing without encurring the overhead of the exception
     * processing
     *
     * @param s
     * @param radix
     * @param defaultValue
     * @return
     * @throws NumberFormatException
     */
    public static int parseInt(String s, int radix, int defaultValue, boolean onErrorUseDefault) {
        int result;

        if (s == null || s.isEmpty()) {
            return defaultValue;
        }
                
        if (radix >= Character.MIN_RADIX
                && radix <= Character.MAX_RADIX) {

            if (s.charAt(0) == ' ') {
                if (!onErrorUseDefault) {
                    throw new NumberFormatException("Invalid character in number: \"" + s + "\"");
                }
            }
            s = s.trim();
            boolean negative = false;
            int i = 0, max = s.length();
            int limit;
            int multmin;
            int digit;

            if (max > 0) {
                char firstChar = s.charAt(0);
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                    i++;
                } else if (firstChar == '+') {
                    if (onErrorUseDefault) {
                        return defaultValue;
                    } else {
                        throw new NumberFormatException("Invalid character in number: \"" + s + "\"");
                    }
                } else {
                    limit = -Integer.MAX_VALUE;
                }
                multmin = limit / radix;
                if (i < max) {
                    digit = Character.digit(s.charAt(i++), radix);
                    if (digit < 0) {
                        if (onErrorUseDefault) {
                            return defaultValue;
                        } else {
                            throw new NumberFormatException("Invalid character in number: \"" + s + "\"");
                        }
                    } else {
                        result = -digit;
                    }
                } else {
                    result = 0;
                }

                while (i < max) {
                    // Accumulating negatively avoids surprises near MAX_VALUE
                    digit = Character.digit(s.charAt(i++), radix);
                    if (digit < 0) {
                        return defaultValue;
                    }
                    if (result < multmin) {
                        return defaultValue;
                    }
                    result *= radix;
                    if (result < limit + digit) {
                        return defaultValue;
                    }
                    result -= digit;
                }
                if (negative) {
                    /* Only got "-"  for default value */
                    if (i <= 1) {
                        result = defaultValue;
                    }
                } else {
                    result = -result;
                }

            } else {
                result = defaultValue;
            }
        } else {
            result = defaultValue;
        }

        return result;
    }
}
