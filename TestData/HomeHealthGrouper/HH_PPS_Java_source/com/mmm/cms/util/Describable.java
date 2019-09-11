/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.util;

/**
 *	This interface provides a standard for setting and getting the
 *	internal description of an object.
 *
 * @author: 3M Health Information Systems  for CMS Home Health
 */
public interface Describable {
	/**
	 * The description of the object
	 * @return
	 */
	String getDescription();

	/**
	 * Sets the objects internal description
	 */
	void setDescription(String description);
}
