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
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * This formatter provides the conversion of a number to the Hexavigesmal
 * value with the possibility of handling 26 unique values. An integer
 * value 26 or above is reduce to Z
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class HexavigesimalFormat extends NumberFormat  {

    /**
     * Main testing module.
     * @param args
     */
    public static void main(String args[]) {
        HexavigesimalFormat format;

        format = new HexavigesimalFormat();

        for (long idx = 0; idx < 366; idx++) {
            System.out.println("Value: " + idx + " = " + format.format(idx));
        }
    }

    /**
     * Not supported
     *
     * @param number
     * @param toAppendTo
     * @param pos
     * @return
     */
    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo,
            FieldPosition pos) {
        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED);
    }

    /**
     * This format uses the following Characters indicators for the following
     * values:
     * 0 or 1 = A
     * 2 = B
     * 3 = C
     * 4 = D
     * 5 = E
     * 6 = F
     * 7 = G
     * 8 = H
     * 9 = I
     * 10 = J
     * 11 = K
     * 12 = L
     * 13 = M
     * 14 = N
     * 15 = O
     * 16 = P
     * 17 = Q
     * 18 = R
     * 19 = S
     * 20 = T
     * 21 = U
     * 22 = V
     * 23 = W
     * 24 = X
     * 25 = Y
     * 26 = Z
     *
     * Any number > 26 will become a multi character string
     *
     * @param number
     * @param toAppendTo
     * @param pos
     * @return
     */
    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo,
            FieldPosition pos) {

        // recursively call this format in order to evaluate the positions
        if (number > 26) {
            if (getMaximumIntegerDigits() != 1) {
                // need to adjust the increments of 26 by 1
                // in order to force the 2nd+ left position to remain
                // with the lower Letter - for example, 52 to be AZ instead
                // of BZ
                if (number % 26 == 0)
                    format(number / 26 - 1, toAppendTo, pos);
                else
                    format(number / 26, toAppendTo, pos);
            } else {
                // since the format is one character but our number is greater
                // than 26, just assume is it 26, instead of trunctate
                number = 26;
            }
        }

        if (number == 0)
            // 0 is the same as 1
            toAppendTo.append('A');
        else {

            // find the remainder of the current number to determine
            // the hexavigesmial value
            number %= 26;
            if (number == 0) {
                // when the number is greater than 0, but a factor of 26,
                // the modulo value will return 0.  This would imply 'A', but
                // that would be invalid.  It should be 'Z'
                toAppendTo.append('Z');
            } else
                toAppendTo.append((char)('A' + number - 1));
        }

        return toAppendTo;
    }

    /**
     * Not supported
     *
     * @param source
     * @param parsePosition
     * @return
     */
    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        throw new UnsupportedOperationException(CommonMessageText.NOT_SUPPORTED);
    }

}
