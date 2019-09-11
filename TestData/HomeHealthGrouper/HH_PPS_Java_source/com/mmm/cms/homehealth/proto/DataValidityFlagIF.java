/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;



/**
 * Holds the data issues associated with a processed record, and generates
 * the Validity flag to use in the grouper output.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface DataValidityFlagIF {

    /**
     * gets the Manifestation sequence issue flag
     * @return true if there is an issue with the Manifestation Sequence
     */
     boolean isManifestationSequenceIssue();

    /**
     * Set to true if there is a Manifestation Sequence issue.  The default
     * is false.
     * @param bool
     */
     void setManifestationSequenceIssue(boolean bool);

    /**
     * gets the clinical issue flag
     * @return true if there is a Clinical issue
     */
     boolean isClinicalIssue();

    /**
     * Set to true if there is a Clinical issue.  The default
     * is false.
     * @param bool
     */
     void setClinicalIssue(boolean bool);

    /**
     * gets the Functional issue flag
     * @return true if there is a Functional issue
     */
     boolean isFunctionalIssue();

    /**
     * Set to true if there is a Functional issue.  The default
     * is false.
     * @param bool
     */
     void setFunctionalIssue(boolean bool);
    
    /**
     * gets the Services issues flag
     * @return true if there is a Service issue
     */
     boolean isServiceIssue();

    /**
     * Set to true if there is a Services issue.  The default
     * is false.
     * @param bool
     */
     void setServiceIssue(boolean bool);

    /**
     * gets the Validity flag
     * @return the single character representing the overall issues
     */
     String getValidityFlag();

    /**
     * Sets the issues by setting the validity flag value.
     * @param val
     */
     void setValidityFlag(String val);

    /**
     * Resets the values to false and the flag to 1, allowing for object re-use.
     */
     void reset();

    /**
     * gets a general issues flag
     * @return true if any of the issues are present
     */
     boolean isIssuePresent();

}
