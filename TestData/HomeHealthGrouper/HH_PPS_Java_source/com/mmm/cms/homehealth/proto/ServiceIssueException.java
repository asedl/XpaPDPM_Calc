/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mmm.cms.homehealth.proto;

/**
 * Exception used when Therapy N/A = 1, i.e. not therapies needed
 * 
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class ServiceIssueException extends Exception {
	
	private HomeHealthGrouperIF grouper;

	public ServiceIssueException(HomeHealthGrouperIF grouper) {
		this.grouper = grouper;
	}

	public ServiceIssueException(HomeHealthGrouperIF grouper, String message) {
		super(message);
		this.grouper = grouper;
	}

	public ServiceIssueException(HomeHealthGrouperIF grouper, String message, Throwable cause) {
		super(message, cause);
		this.grouper = grouper;
	}

	public ServiceIssueException(HomeHealthGrouperIF grouper, Throwable cause) {
		super(cause);
		this.grouper = grouper;
	}


	public HomeHealthGrouperIF getGrouper() {
		return grouper;
	}
	
}
