/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.proto;

import com.mmm.cms.util.Describable;

/**
 * This indicates that status of a diagnosis code during the scoring
 * process, such as a valid or invalid code and a code that is valid
 * and available for scoring, i.e. is part of the Home Health code set
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public enum DiagnosisScoringStatus_EN implements Describable {

    INVALID("Invalid"),
    VALID("Valid but not scorable"),
    VALID_SCORABLE("Valid and scorable");

    private String description;

    private DiagnosisScoringStatus_EN(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

	@Override
	public String toString() {
		return description;
	}
    
    
    
}
