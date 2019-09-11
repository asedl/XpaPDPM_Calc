/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmm.cms.homehealth.vut;

import com.mmm.cms.homehealth.vut.proto.OasisDataItemIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.vut.proto.OasisValidationEditIF;

/**
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class OasisValidationEdit implements OasisValidationEditIF {

	private OasisEditIF edit;
	private OasisDataItemIF oasisDataItem;

	public OasisValidationEdit() {
		// sets null values
	}

	public OasisValidationEdit(OasisEditIF edit, OasisDataItemIF oasisDataItem) {
		this.edit = edit;
		this.oasisDataItem = oasisDataItem;
	}

	@Override
	public OasisEditIF getEdit() {
		return edit;
	}

	@Override
	public OasisDataItemIF getOasisDataItem() {
		return oasisDataItem;
	}

	@Override
	public void setEdit(OasisEditIF edit) {
		this.edit = edit;
	}

	@Override
	public void setOasisDataItem(OasisDataItemIF oasisDataItem) {
		this.oasisDataItem = oasisDataItem;
	}

	@Override
	public String toString() {
		return "OasisValidationEdit{" + "editId=" + edit + ", oasisDataItem=" + oasisDataItem + '}';
	}
	
	
}
