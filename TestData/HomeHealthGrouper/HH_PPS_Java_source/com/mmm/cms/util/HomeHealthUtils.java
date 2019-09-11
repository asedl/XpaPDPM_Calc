/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.util;

/**
 * Holds general utility methods that don't need to be associated with any
 * specific class
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class HomeHealthUtils {

    /**
     * Given an array of ints, sums their values and returns that sum. The
     * array remains unchanged
     *
     * @param array
     * @return sum of integers
     */
    public static int sumIntArray(int array[]) {
        int sum = 0;
        for (int idx = array.length - 1; idx >= 0;) {
            sum += array[idx--];
        }
        return sum;
    }


}
