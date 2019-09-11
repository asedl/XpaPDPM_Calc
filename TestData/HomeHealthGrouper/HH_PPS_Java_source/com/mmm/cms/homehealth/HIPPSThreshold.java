/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the HIPPS threshold information in order to assign a value based on
 * Episode timing and number of visits to support determining the HIPPS value.
 * The Home Health specs contains a table that defines episode timing associated
 * with the number of visits. This in turn relates to a grouping step and a
 * scoring equation and is split into different Dimensions (Clinical and
 * Functional) each with 3 severity levels.
 *
 * This class focuses on a single episode timing, grouping step and equation
 * combination with its associated severity limits (usually only 3 of them)
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class HIPPSThreshold {

    /**
     * Episode Timing - usually "01", "02", "UK"
     */
    final private String timingValue;
    /**
     * The upper limit number of visits which is an inclusive number.
     */
    final private int visitsUpperLimit;
    /**
     * The grouping step - usually 1 through 5
     */
    final private int groupingStep;
    /**
     * The equation for this combination of timing, visits and grouping step - 
     * usually 1 through 4 for Clinical/Functional and only 1 for 
     * Non-routine Supplies
     */
    final private int equation;
    /**
     * A list of severity limits - usually there are 3 of these
     */
    final private List<SeverityLimitValue> severityLimitValues;

    public HIPPSThreshold(String timingValue, int visitsUpperLimit, int groupingStep, int equation) {
        this.timingValue = timingValue;
        this.visitsUpperLimit = visitsUpperLimit;
        this.groupingStep = groupingStep;
        this.equation = equation;
        this.severityLimitValues = new ArrayList<SeverityLimitValue>(3);
    }

    public void addSeverityLimit(int lowerLimit, int upperLimit, char hippsSeverityValue) {
        this.severityLimitValues.add(new SeverityLimitValue(lowerLimit, upperLimit, hippsSeverityValue));
    }

    public List<SeverityLimitValue> getSeverityLimits() {
        return this.severityLimitValues;
    }

    public String getTimingValue() {
        return timingValue;
    }

    public int getVisitsUpperLimit() {
        return visitsUpperLimit;
    }

    public int getGroupingStep() {
        return groupingStep;
    }

    public int getEquation() {
        return equation;
    }

    /**
     * Returns the equation character representation based on the equation value
     *
     * @param equationValue
     * @return
     * @throws IllegalArgumentException if the equation value is out of range
     */
    public char getHippsSeverityValue(int equationValue) {
        for (SeverityLimitValue limitValue : severityLimitValues) {
            if (equationValue >= limitValue.getLowerLimit() && equationValue <= limitValue.getUpperLimit()) {
                return limitValue.getHippsSeverityValue();
            }
        }
        throw new IllegalArgumentException("Equation Value is not represented by this HIPPThreshold.  Equation value it out of range: " + equationValue);
    }

}
