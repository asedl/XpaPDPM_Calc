/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;

import java.util.Iterator;
import java.util.List;

/**
 * Defines a Notifier for Home Health Scoring events.
 *
 * @author 3M Health Information Systems  for CMS Home Health
 * @deprecated as of 2014, because this interface promotes unsafe Thread coding 
 * for the HomeHealthGrouperIF - scheduled for removal in 2015
 */
public interface HomeHealthEventNotifierIF {

    /**
     * This method adds a HomeHealthEventListenerIF to the list of listeners.
     *
     * @param listener
     */
    void addEventListener(HomeHealthEventListenerIF listener);

    /**
     * this method returns an enumerated list of HomeHealthEventListenerIF
     * objects
     *
     * @return
     */
    Iterator<HomeHealthEventListenerIF> getEventListeners();

    /**
     * gets the underlying list of listeners; a little more dangerous than
     * using the getEventListeners() that returns the Iterator
     *
     * @return
     */
    List<HomeHealthEventListenerIF> getEventListenersList();

    /**
     * Sends a event to the listeners.
     *
     * @param event
     */
    void notifyEventListeners(HomeHealthEventIF event);

    /**
     * Removes all the listeners from the list
     *
     */
    void removeEventListeners();

    /**
     * Removes a single event listener from the list
     *
     * @param listener
     */
    void removeEventListener(HomeHealthEventListenerIF listener);

    /**
     * gets the number listeners
     *
     * @return the number of listeners available.
     */
    int getListenerCount();
}
