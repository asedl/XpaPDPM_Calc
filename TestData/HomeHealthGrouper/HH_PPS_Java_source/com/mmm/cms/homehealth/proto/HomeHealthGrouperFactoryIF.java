/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;



import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.util.Initializable;
import java.util.List;
import java.util.Properties;


/**
 * This defines a means for selecting a Grouper based on the date range of the
 * Home Health record.
 *
 * September 2012 - added getGrouper() by version String
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface HomeHealthGrouperFactoryIF extends Initializable {

    /**
     * defines the environment variable as "home_health_config"
     */
     String PROPERTY_HOME_HEALTH_CONFIG = "home_health_config";

    /**
     * Determines the HomeHealthGrouperIF appropriate for the record
     * 
     * @param record
     * @return the HomeHealthGrouperIF that is valid to score the record. This 
     * may be null if no HomeHealthGrouperIF is found.
	 * @throws ServiceIssueException - when Therapies are not needed based on the
	 * Therapy Need N/A field in the record
     */
     HomeHealthGrouperIF getGrouper(HomeHealthRecordIF record) throws ServiceIssueException;

    /**
     * gets the HomeHealthGrouperIF based on the version string
     * 
     * @param versionId
     * @return the HomeHealthGrouperIF for the version string.  This 
     * may be null if no HomeHealthGrouperIF is found.
     */
     HomeHealthGrouperIF getGrouper(String versionId);


    /**
     * This returns a non-null list of HomeHealthGrouperIF objects. The list
     * may have a size of 0 if no groupers are available
     *
     * @return non-null List of HomeHealthGrouperIF objects.
     */
     List<HomeHealthGrouperIF> getGroupers();

	 /**
	  * gets the properties used to initialize the factory
	  * @return 
	  */
	 Properties getProperties();
	 
}
