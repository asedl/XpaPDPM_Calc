/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;



import com.mmm.cms.util.Describable;
import com.mmm.cms.util.Identifiable;


/**
 * Holds the ID and the description for a Diagnostic Group
 * 
 * October 2012 - added alternate scoring methods for use in versions
 * V3413 and later
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface DiagnosticGroupIF extends Identifiable, Describable, Comparable {

    
    /**
     * Gets the alternate primary code scorable flag
     * 
     * @return true if this group has alternate scoring for primary and/or other
     * code positions
     */
    boolean isAlternatePrimaryScorable();

    /**
     * Sets the alternate primary code scorable flag
     * 
     * @param alternatePrimaryScorable 
     */
    void setAlternatePrimaryScorable(boolean alternatePrimaryScorable);
    
}
