/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.util;

/**
 * Provides a consistent way to get the name associated with an object at run-time.
 * This not the same as getClass().getName()
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface Namable {

    /**
     * The run-time name
     * @return The run-time name associated with the object
     */
    String getName();

    /**
     * Sets the run-time name associated with the object
     * @param name
     */
    void setName(String name);
}
