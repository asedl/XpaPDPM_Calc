/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.util;

import com.mmm.cms.homehealth.proto.HomeHealthEventIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * When this class is registered as an Event Listener, it will log the events
 * to the HomeHealthGrouper logger.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class HHEventConsole implements HomeHealthEventListenerIF {

    /**
     * Constructs the console with the getClass().getName() as
     * the logging name
     */
    public HHEventConsole() {
    }

    /**
    * Sends the event to the Home Health logger
    *
    * @param event
    */
    @Override
    public void homeHealthEvent(HomeHealthEventIF event) {
        StringBuilder buffer;
        Logger logger = Logger.getLogger(getClass().getName());

        buffer = new StringBuilder("HHEvent: (");
        buffer.append(event.getEventId());
        buffer.append(")");

        if (event.getGrouper() != null) {
            buffer.append(" - Grouper: " );
            buffer.append(event.getGrouper().getName());
        }

        if (event.getModel() != null) {
            buffer.append(" - Model: '");
            buffer.append(event.getModel().getName());
        }

        if (event.getMessage() != null) {
             buffer.append(" - Message: '");
             buffer.append(event.getMessage());
        }

        switch (event.getEventId()) {

            case VALIDATION_ISSUE:
            case GENERAL:
                logger.log(Level.INFO, buffer.toString());
                break;

            case WARNING:
                logger.log(Level.WARNING, buffer.toString());
                break;

            case EXCEPTION:
                if (event.getException() != null) {
                    buffer.append(" - Exception: '");
                    buffer.append(event.getException().toString());
                    buffer.append("'");
                }
                logger.log(Level.SEVERE, buffer.toString());

                break;

            case SCORING_STARTING:
            case SCORING_FINISHED:
            case SCORING_INCREASED:
            case SCORING_SECTION_STARTING:
            case SCORING_SECTION_FINISHED:
                logger.log(Level.INFO,buffer.toString());
                break;
        }
    }
}
