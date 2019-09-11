/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.util;

/**
 * Provides a consistent means of identifying an object by its id number.  The
 * id is not associated with Serialization
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface Identifiable {

    /**
     * the id of the object
     * @return the id of the object at run time
     */
    int getId();

    /**
     * Sets the id for the object at run time
     * @param id
     */
    void setId(int id);

}
