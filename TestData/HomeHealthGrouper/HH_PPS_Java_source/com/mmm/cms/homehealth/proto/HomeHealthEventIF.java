/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;



/**
 * Describes the Home Health Event used for notify programmatic listeners
 * during the scoring process.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface HomeHealthEventIF  {
	

    /**
     * @return The Grouper associated with event
     */
    HomeHealthGrouperIF getGrouper();

    /**
     * gets the event id
     *
     * @return the event id
     */
    EventId_EN getEventId();

    /**
     * gets the associated Exception, if any
     *
     * @return the Exception associated with the event, or null if no
     * Exception occurred
     */
    Exception getException();

    /**
     * gets the message describing the event
     * @return Message - should not be null
     */
    String getMessage();

    /**
     * gets the scoring model, if any that caused the event
     * @return the scoring model associated with the event, or null if no
     * scoring model was involved
     */
    HomeHealthScoringModelIF getModel();

    /**
     * gets the record associated with the event
     * @return the Home Health record being processed at the time of the event
     */
    HomeHealthRecordIF getRecord();

    /**
     * Sets the event id
     * @param eventId
     */
    void setEventId(EventId_EN eventId);

    /**
     * Sets the Exception
     * @param exception
     */
    void setException(Exception exception);

    /**
     * Sets the message
     * @param message
     */
    void setMessage(String message);

    /**
     * Sets the scoring model
     * @param model
     */
    void setModel(HomeHealthScoringModelIF model);

    /**
     * Sets the Home Health record
     * @param record
     */
    void setRecord(HomeHealthRecordIF record);

}
