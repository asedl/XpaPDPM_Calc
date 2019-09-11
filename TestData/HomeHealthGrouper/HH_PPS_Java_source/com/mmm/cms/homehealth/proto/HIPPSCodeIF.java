/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;



/**
 * This represents the 5 character code used to report the HIPPS for the
 * Grouper.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface HIPPSCodeIF {

    String DEFAULT_BLANK_VALUE = "     ";

    /**
     * gets the Clinical Severity code
     * @return the Clinical Severity character
     */
    char getClinicalSeverity();

    /**
     * gets the Functional Severity code
     * @return the Functional Severity character
     */
    char getFunctionalSeverity();

    /**
     * gets the Grouping step
     * @return the Grouping step number
     */
    int getGroupingStep();

    /**
     * gets the NRS code
     * @return the Non Routine Supplies character
     */
    char getNonRoutineSupplies();

    /**
     * gets the services utilization code
     * @return the Services Utilization character
     */
    char getServicesUtilization();

    /**
     * sets the Clinical Severity character
     * @param clinicalSeverity
     */
    void setClinicalSeverity(char clinicalSeverity);

    /**
     * sets the Functional Severity character
     * @param functionalSeverity
     */
    void setFunctionalSeverity(char functionalSeverity);

    /**
     * sets the Grouping step number
     * @param groupingStep
     */
    void setGroupingStep(int groupingStep);

    /**
     * sets the Non Routine Supplies character
     * @param nonRoutineSupplies
     */
    void setNonRoutineSupplies(char nonRoutineSupplies);

    /**
     * sets the Services Utilization character
     * @param servicesUtilization
     */
    void setServicesUtilization(char servicesUtilization);

    /**
     * gets the entire code as one string
     * @return The combined 5 character code. Will never be null.
     */
    String getCode();
}
