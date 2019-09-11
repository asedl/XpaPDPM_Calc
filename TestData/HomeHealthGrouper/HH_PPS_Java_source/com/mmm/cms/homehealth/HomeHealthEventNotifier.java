/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.HomeHealthEventIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventNotifierIF;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a base class for providing event notification to event listeners.
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class HomeHealthEventNotifier implements HomeHealthEventNotifierIF {

    private List<HomeHealthEventListenerIF> listeners;

    /**
     * Constructor for initializing the list of listeners
     */
    public HomeHealthEventNotifier() {
        listeners = new ArrayList<HomeHealthEventListenerIF>(5);
    }

    /**
     * Adds a scoring listener to this grouper
     * @param listener
     */
    @Override
    public void addEventListener(HomeHealthEventListenerIF listener) {
        listeners.add(listener);
    }

    /**
     * gets an iterator for the event listeners
     * @return An iterator of the scoring listeners.  Will not be null, but
     * may not have any listeners to iterator over
     */
    @Override
    public Iterator<HomeHealthEventListenerIF> getEventListeners() {
        return listeners.iterator();
    }

    /**
     * gets an iterator for the event listeners
     * @return An iterator of the scoring listeners.  Will not be null, but
     * may not have any listeners to iterator over
     */
    public List<HomeHealthEventListenerIF> getEventListenersList() {
        return listeners;
    }
    
    /**
     * If there are listener, the send them the event and let them
     * do what they want.
     *
     * @param event
     */
    @Override
    public void notifyEventListeners(HomeHealthEventIF event) {
        if (listeners.size() > 0) {
            for (HomeHealthEventListenerIF listener : listeners) {
                listener.homeHealthEvent(event);
            }
        }
    }

    /**
     * Removes a listener from this Grouper.
     * @param listener
     */
    @Override
    public void removeEventListener(HomeHealthEventListenerIF listener) {
        listeners.remove(listener);
    }

    /**
     * Removes all the listeners from this Grouper
     */
    @Override
    public void removeEventListeners() {
        listeners.clear();
    }

    /**
     * This a useful when determine when to report events.  Since some
     * reporting takes up processing time unrelated to actual scoring, determine
     * if reporting is necessary (i.e. listeners count > 0) helps to cut down
     * on any unnecessary processing/building of the detailed event information.
     * See the fireScoring... methods for examples of this.
     *
     * @return the number of listeners for this Grouper
     */
    @Override
    public int getListenerCount() {
        return listeners.size();
    }

}
