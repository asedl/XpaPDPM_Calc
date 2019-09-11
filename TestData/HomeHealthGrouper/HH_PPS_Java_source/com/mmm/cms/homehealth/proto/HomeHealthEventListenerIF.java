/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;


import java.util.EventListener;

/**
 * This defines a class the listens for events for the Home Health grouper.
 * These events are used instead of logging
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface HomeHealthEventListenerIF extends EventListener {

    /**
     * Processes an event for Home Health scoring.  The implementation
     * of this method should be as fast as possible, because the rest of
     * processing may be effected by the time used to process the event.
     * 
     * @param event
     */
    void homeHealthEvent(HomeHealthEventIF event);

}
