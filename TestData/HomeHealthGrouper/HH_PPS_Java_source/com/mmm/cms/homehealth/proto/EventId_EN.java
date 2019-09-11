/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.proto;

import com.mmm.cms.util.Describable;

/**
 * Provides Event Ids and descriptions for event types during the record scoring 
 * process.
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public enum EventId_EN implements Describable {
    /**
     * Event Id for scoring general informational event
     */
    GENERAL("General"),
    /**
     * Event Id for scoring general warning during processing
     */
    WARNING("Warning"),
    /**
     * Event Id for scoring Exception
     */
    EXCEPTION("Exception"),
    /**
     * Event Id for scoring has started
     */
    SCORING_STARTING("Scoring Starting"),
    /**
     * Event Id for scoring finished
     */
    SCORING_FINISHED("Scoring Finished"),
    /**
     * Event Id for scoring increased
     */
    SCORING_INCREASED("Scoring Increase"),
    /**
     * Event Id for scoring has started a section
     */
    SCORING_SECTION_STARTING("Scoring Section Starting"),
    /**
     * Event Id for scoring has finished a section
     */
    SCORING_SECTION_FINISHED("Scoring Section Finished"),
    /**
     * Event Id for validation issues
     */
    VALIDATION_ISSUE("Validation Issue");

    private String description;
    
    private EventId_EN(String desc) {
        this.description = desc;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

}
