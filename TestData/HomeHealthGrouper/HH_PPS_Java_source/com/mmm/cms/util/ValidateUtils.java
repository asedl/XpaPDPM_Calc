/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.util;

/**
 * Utility class that provides consolidate String validation methods, and
 * predefined sets of data to validate against.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public final class ValidateUtils {

    private ValidateUtils() {
        // because there are only static methods
    }

    /**
     * Array of "0", "1"
     */
    public static final String ARRAY_ZERO_ONE[] = new String[]{"0", "1"};

    /**
     * Array of "0", "1", "2"
     */
    public static final String ARRAY_ZERO_ONE_TWO[] = new String[]{"0", "1", "2"};
    /**
     * Array of "0", "1", "2", "3"
     */
    public static final String ARRAY_ZERO_ONE_TWO_THREE[] = new String[]{"0", "1", "2", "3"};
    
    public static final String ARRAY_DOUBLE_0[] = new String[] { "00", " 0", "0 ", "0" };
    public static final String ARRAY_DOUBLE_1[] = new String[] { "01", " 1", "1 ", "1" };
    public static final String ARRAY_DOUBLE_2[] = new String[] { "02", " 2", "2 ", "2" };
    public static final String ARRAY_DOUBLE_3[] = new String[] { "03", " 3", "3 ", "3" };
    public static final String ARRAY_DOUBLE_4[] = new String[] { "04", " 4", "4 ", "4" };
    public static final String ARRAY_DOUBLE_5[] = new String[] { "05", " 5", "5 ", "5" };
    public static final String ARRAY_DOUBLE_6[] = new String[] { "06", " 6", "6 ", "6" };
    public static final String ARRAY_DOUBLE_7[] = new String[] { "07", " 7", "7 ", "7" };
    public static final String ARRAY_DOUBLE_8[] = new String[] { "08", " 8", "8 ", "8" };
    public static final String ARRAY_DOUBLE_9[] = new String[] { "09", " 9", "9 ", "9" };
    
    
    /**
     * Double zero string
     */
    public static final String DOUBLE_ZERO = "00";
    
    /**
     * Array of "00", "01"
     */
    public static final String ARRAY_DOUBLE_ZERO_ONE[] = new String[]{
                "00", "01"};
    /**
     * Array of "00", "01", "02"
     */
    public static final String ARRAY_DOUBLE_ZERO_ONE_TWO[] = new String[]{
                "00", "01", "02"};
    /**
     * Array of "02", "03", "04"
     */
    public static final String ARRAY_DOUBLE_TWO_THREE_FOUR[] = new String[]{
                "02","03","04"};
    /**
     * Array of "00", "01", "02", "03"
     */
    public static final String ARRAY_DOUBLE_ZERO_ONE_TWO_THREE[] = new String[] {
                "00", "01", "02", "03"};
    
    /**
     * Array of "00", "01", "02", "03", "04"
     */
    public static final String ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR[] = new String[]{"00", "01", "02", "03", "04"};

    /**
     * Array of "00", "01", "02", "03", "04", "05", "06"
     */
    public static final String ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE_SIX[] = new String[]{"00", "01", "02", "03", "04", "05", "06"};

    /**
     * Array of "01", "02", "03", "04"
     */
    public static final String ARRAY_DOUBLE_ONE_TWO_THREE_FOUR[] = new String[]{"01", "02", "03", "04"};
    /**
     * Array of "01", "02", "03"
     */
    public static final String ARRAY_DOUBLE_ONE_TWO_THREE[] = new String[]{"01", "02", "03"};
    /**
     * Array of "00", "01", "02", "03", "04", "05"
     */
    public static final String ARRAY_DOUBLE_ZERO_ONE_TWO_THREE_FOUR_FIVE[] = new String[]{"00", "01", "02", "03", "04", "05"};
    /**
     * Array of "01", "02", "03", "04", "05"
     */
    public static final String ARRAY_DOUBLE_ONE_TWO_THREE_FOUR_FIVE[] = new String[]{"01", "02", "03", "04", "05"};
    /**
     * Array of "01", "02", "03", "04", "05", "06"
     */
    public static final String ARRAY_DOUBLE_ONE_TWO_THREE_FOUR_FIVE_SIX[] = new String[]{"01", "02", "03", "04", "05", "06"};
    /**
     * Array of "01", "03"
     */
    public static final String ARRAY_DOUBLE_ONE_THREE[] = new String[]{"01", "03"};
    /**
     * Array of "01", "02"
     */
    public static final String ARRAY_DOUBLE_ONE_TWO[] = new String[]{"01", "02"};

    /**
     * Array of "02", "03"
     */
    public static final String ARRAY_DOUBLE_TWO_THREE[] = new String[]{"02", "03"};

    /**
     * Array of "02", "03", "04", "05"
     */
    public static final String ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE[] = new String[]{"02", "03", "04", "05"};

    /**
     * Array of "02", "03", "04", "05", "06"
     */
    public static final String ARRAY_DOUBLE_TWO_THREE_FOUR_FIVE_SIX[] = new String[]{"02", "03", "04", "05", "06"};
    /**
     * Array of "03", "04"
     */
    public static final String ARRAY_DOUBLE_THREE_FOUR[] = new String[]{"03", "04"};
    /**
     * Array of "03", "04", "05"
     */
    public static final String ARRAY_DOUBLE_THREE_FOUR_FIVE[] = new String[]{"03", "04", "05"};
    /**
     * Array of "04", "05"
     */
    public static final String ARRAY_DOUBLE_FOUR_FIVE[] = new String[]{"04", "05"};

    /**
     * Array of "04", "05", "06"
     */
    public static final String ARRAY_DOUBLE_FOUR_FIVE_SIX[] = new String[]{"04", "05", "06"};

    /**
     * value of " " - one spaces
     */
    public static final String SPACE_1 = " ";

    /**
     * Array of " " - i.e. SPACE_1
     */
    public static final String ARRAY_SPACE_1[] = new String[]{SPACE_1};

    /**
     * value of " " - two spaces
     */
    public static final String SPACE_2 = "  ";

    /**
     * Array of " "
     */
    public static final String ARRAY_SPACE_2[] = new String[]{SPACE_2};
    /**
     * value of "NA"
     */
    public static final String NOT_APPLICABLE = "NA";

    /**
     * Array of "NA"
     */
    public static final String ARRAY_NOT_APPLICABLE[] = new String[]{NOT_APPLICABLE};

    /**
     * Value of "UK"
     */
    public static final String UNKNOWN_UK = "UK";


    public static final String CARET = "^";

    public static final String ARRAY_CARET_VALUES[] = new String[]{"^", "^ "};

    /**
     * Array of "UK"
     */
    public static final String ARRAY_UNKNOWN_UK[] = new String[]{UNKNOWN_UK};
    
    public static final String DASH = "-";
    
    public static final String ARRAY_DASH_VALUES[] = new String[]{"-", "- ", " -", " - "};

//    /**
//     * Utility method to check the actualValue against the validValues.
//     *
//     * @param actualValue can be null
//     * @param validValues can not be null or length = 0
//     * @return true if the actualValue is contained within the valueValues
//     */
//    public static boolean isValidValue(String actualValue,
//            String validValues[]) {
//        boolean valid = false;
//
//        if (actualValue != null) { // && actualValue.length() > 0) {
//            int idx = validValues.length;
//            while (idx-- > 0) {
//                if (actualValue.equals(validValues[idx])) {
//                    valid = true;
//                    break;
//                }
//            }
//        }
//        return valid;
//    }

//    /**
//     * checks the actualValue against two arrays of valid values. If no valid
//     * value found in validValues, then validValues2 is searched.
//     *
//     * @param actualValue
//     * @param validValues
//     * @param validValues2
//     * @return
//     */
//    public static boolean isValidValue(String actualValue,
//            String validValues[],
//            String validValues2[]) {
//
//        return !isValidValue(actualValue, validValues) ? isValidValue(actualValue, validValues2) : true;
//    }

//    /**
//     * checks the actualValue against three arrays of valid values. If no valid
//     * value is found in validValues, then validValues2 is searched. If no valid
//     * value is found in validValues2, then validValues3 is searched.
//     *
//     * @param actualValue
//     * @param validValues
//     * @param validValues2
//     * @param validValues3
//     * @return
//     */
//    public static boolean isValidValue(String actualValue,
//            String validValues[],
//            String validValues2[],
//            String validValues3[]) {
//
//        return !isValidValue(actualValue, validValues) && !isValidValue(actualValue, validValues2) ? isValidValue(actualValue, validValues3) : true;
//    }

    /**
     * 
     * @param actualValue
     * @param validValues variable list of String arrays that hold the possible
     * valid value for the actualValue.
     * 
     * @return 
     */
    public static boolean isValidValue(final String actualValue, String[]... validValues) {
        boolean bool = false;
        
        outerloop:
        for (String values[] : validValues) {
            for (String value : values) {
                if (value.equals(actualValue)) {
                    // only need to find one match and done
                    bool = true;
                    break outerloop;
                }
            }
        }
        
        return bool;
    }

    /**
     * With the definition of OASIS-C1, the caret '^' character was introduced
     * as a way to represent a blank value or a space, with DX codes possibly
     * having more than one '^' in its value and other variables having a single
     * caret at the beginning. This method will determine if the value is blank
     * based on the containing only spaces or only '^'
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        boolean blank = true;

        if (str != null && !str.isEmpty()) {
            final char chars[] = str.toCharArray();

            if (chars[0] == ' ' || chars[0] == '^') {
                for (int idx = 1; idx < chars.length; idx++) {
                    /*
                     * check for "blank" characters
                     */
                    if (chars[idx] != ' ') {
                        blank = false;
                        break;
                    }
                }
            } else {
                blank = false;
            }
        }
        return blank;
    }

    /**
     * With the definition of OASIS-C1, the caret '^' character was introduced
     * as a way to represent a blank value or a space when it is in the first
     * character position. There can not be anything following the caret except
     * for spaces. This method will determine if the value is blank based on the
     * containing only '^' or actually just spaces
     *
     * @param str
     * @return
     */
    public static boolean isEmptyCaret(String str) {
        boolean blank = true;

        if (str != null) {
            if (!str.isEmpty()) {
                final char chars[] = str.toCharArray();

                if (chars[0] == '^') {
                    for (int idx = 1; idx < chars.length; idx++) {
                        /*
                         * check for blank character
                         */
                        if (chars[idx] != ' ') {
                            blank = false;
                            break;
                        }
                    }
                } else {
                    blank = false;
                }
            } else {
                // this would indicate that the value is all spaces which
                // is not considered blank when using the caret
                blank = false;
            }
        }
        return blank;
    }

    /**
     * In OASIS a number can be the following formats, assuming a field length
     * of 3 characters and a value of 1:
     *      '001', '01 ', '1', '1 ', or '1  '
     * but not:
     *      ' 1 ', '  1'
     *   
     * @param str
     * @return 
     */
    public static boolean isNumeric(String str) {
        boolean bool = true;

        if (str != null) {
            if (str.isEmpty()) {
                // be aware that empty is actually length() == 0
                // so, a string of all spaces is actually considered
                // not empty.
                bool = false;
            } else {
                final char chars[] = str.toCharArray();
                int idx = chars.length;
                boolean digitFound = false;

                while (idx-- > 0) {
                    /*
                     * check for "blank" characters
                     */
                    if (chars[idx] == ' ') {
                        if (digitFound) {
                            // since we are going backwards from the end of the
                            // string, we can have spaces at the end but 
                            // once we hit a Digit, there can not be any spaces
                            // prior to the digit
                            bool = false;
                            break;
                        }
                    } else if (Character.isDigit(chars[idx])) {
                        digitFound = true;
                    } else {
                        // this is not a digit character
                        bool = false;
                        break;
                    } 
                }
                if (!digitFound) {
                    // since no digit is found, this is technically not a number
                    bool = false;
                }
            }
        }

        return bool;
    }

}
