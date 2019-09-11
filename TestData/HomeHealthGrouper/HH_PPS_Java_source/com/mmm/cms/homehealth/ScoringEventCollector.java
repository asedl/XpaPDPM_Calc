/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.HomeHealthEventIF;
import com.mmm.cms.homehealth.proto.HomeHealthEventListenerIF;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author 3M Health Information Systems  for CMS Home Health for CMS
 */
public class ScoringEventCollector implements HomeHealthEventListenerIF {

    private Collection<HomeHealthEventIF> events;

    public ScoringEventCollector() {
        this.events = new ArrayList<HomeHealthEventIF>();
    }
    
    
    @Override
    public void homeHealthEvent(HomeHealthEventIF event) {
        this.events.add(event);
    }
    
    public Collection<HomeHealthEventIF> getEvents() {
        return this.events;
    }
    
}
