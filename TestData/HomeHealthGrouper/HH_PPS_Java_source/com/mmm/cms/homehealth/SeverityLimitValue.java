/*
 * Home Health Grouper
 * Developed for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

/**
 * Holds the severity levels used with the HIPPS code.  The level does not have 
 * a name, but does have a value which is used for the HIPPS value.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class SeverityLimitValue {
    /**
     * The score lower limit - inclusive
     */
    private final int lowerLimit;
    /**
     * The score upper limit - inclusive
     */
    private final int upperLimit;
    /**
     * The Alphabetic HIPPS value
     */
    private final char hippsSeverityValue;

    /**
     * Since this object can not be changed, this constructor checks for valid 
     * limit and values.
     * @param lowerLimit - must be >= 0 and less than upperLimit
     * @param upperLimit - must be >= 0 and greater than lowerLimit
     * @param hippsSeverityValue - must be alphabetic upper case
     * @throws IllegalArgumentException 
     */
    public SeverityLimitValue(int lowerLimit, int upperLimit, char hippsSeverityValue) 
                    throws IllegalArgumentException {
        if (lowerLimit > upperLimit) {
            throw new IllegalArgumentException("Low limit of " + lowerLimit + " can not be larger than upperLimit of " + upperLimit);
        } else if (lowerLimit < 0) {
            throw new IllegalArgumentException("Low limit of " + lowerLimit + " can not be less than 0");
        } else if (upperLimit < 0) {
            throw new IllegalArgumentException("Low limit of " + lowerLimit + " can not be less than 0");
        } else if (!Character.isLetter(hippsSeverityValue)) {
            throw new IllegalArgumentException("HIPPS Severity value must be Alphabetic - value used: '" + hippsSeverityValue + "'");
        }
        
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.hippsSeverityValue = Character.isUpperCase(hippsSeverityValue) ? hippsSeverityValue : Character.toUpperCase(hippsSeverityValue);
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public char getHippsSeverityValue() {
        return hippsSeverityValue;
    }
    
}
