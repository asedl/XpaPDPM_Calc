/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.proto;



import com.mmm.cms.util.Identifiable;
import com.mmm.cms.util.Namable;

/**
 * This represents a single Casemix Adjustment set.  It corresponds to a 
 * single row in the original "Table 5 Casemix Adjustment Variables" tab
 * of the Pseudo 2.03 Tables spreadsheet
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public interface CaseMixAdjustmentItemIF extends Identifiable, Namable, PointsScoringEquationsIF {

    /**
     * Gets the equation adjustment value
     *
     * @param equationId
     * @return adjustment value
     */
    int getAdjustment(int equationId);

    /**
     * Sets the equation adjustment value
     * @param equationId
     * @param value
     */
    void setAdjustment(int equationId, int value);

}
