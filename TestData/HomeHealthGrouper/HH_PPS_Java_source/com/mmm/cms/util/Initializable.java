/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.util;

import java.util.Properties;

/**
 * Provides a consistent way to initialize the properties of an object
 *
 * @author 3M Heath Information Systems
 */
public interface Initializable {

	/**
	 * This method initializes the implementation class.  The
	 * class should be ready if it does not through an Exception.
	 * @param properties 
	 * @exception java.lang.Exception The exception description.
	 */
	void init(Properties properties) throws	Exception;

}
