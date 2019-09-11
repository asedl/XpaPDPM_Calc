/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.vut;

import com.mmm.cms.homehealth.vut.proto.CollectionValidationEditsIF;
import com.mmm.cms.homehealth.vut.proto.OasisEditIF;
import com.mmm.cms.homehealth.vut.proto.OasisValidationEditIF;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the VUT edit interface
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class CollectionValidationEdits extends ArrayList<OasisValidationEditIF>
		implements CollectionValidationEditsIF {

	/**
	 * Ensures there is no duplicate edit/dataItem combinations before adding 
	 * the new edit
	 * 
	 * @param newEdit
	 * @return true if the item as added, false if it was not added
	 */
	@Override
	public boolean add(OasisValidationEditIF newEdit) {
		final long editId = newEdit.getEdit().getId();
		final String dataItem = newEdit.getOasisDataItem().getKey();
		
		for (OasisValidationEditIF edit : this) {
			/*
			 * If the edit/dataItem combo is already in the list, then don't
			 * add it again
			 */
			if (editId == edit.getEdit().getId() && dataItem.equals(edit.getOasisDataItem().getKey())) {
				return false;
			}
		}
		return super.add(newEdit); 
	}

	
	/**
	 * Gets a non-null list of edits based on the edit type/id - the list may be
	 * null.
	 *
	 * @param editType
	 * @return non-null list - may be empty
	 */
	@Override
	public List<OasisValidationEditIF> getEdits(OasisEditIF editType) {
		final List<OasisValidationEditIF> edits = new ArrayList<OasisValidationEditIF>();

		for (OasisValidationEditIF edit : this) {
			if (edit.getEdit() == editType) {
				edits.add(edit);
			}
		}
		return edits;
	}

	/**
	 * Gets a non-null list of edits based on the edit type/id - the list may be
	 * null.
	 *
	 * @param dataItemName
	 * @return non-null list - may be empty
	 */
	@Override
	public List<OasisValidationEditIF> getEdits(String dataItemName) {
		final List<OasisValidationEditIF> edits = new ArrayList<OasisValidationEditIF>();

		for (OasisValidationEditIF edit : this) {
			if (dataItemName.equals(edit.getOasisDataItem().getKey())) {
				edits.add(edit);
			}
		}
		return edits;
	}

	@Override
	public boolean isEditPresent(OasisEditIF editType) {
		boolean present = false;

		for (OasisValidationEditIF edit : this) {
			if (edit.getEdit() == editType) {
				present = true;
				break;
			}
		}
		return present;

	}

	@Override
	public boolean isEditPresent(String dataItemName) {
		boolean present = false;

		for (OasisValidationEditIF edit : this) {
			if (dataItemName.equals(edit.getOasisDataItem().getKey())) {
				present = true;
				break;
			}
		}
		return present;
	}

	@Override
	public String toString() {
		int idx = 0;
		final StringBuilder buffer = new StringBuilder(512);

		buffer.append("CollectionValidationEdits{");
		for (OasisValidationEditIF edit : this) {
			if (idx++ > 0) {
				buffer.append(",");
			}
			buffer.append("{ ");
			buffer.append(edit.toString());
			buffer.append("}");
		}
		buffer.append("}");
		
		return buffer.toString();
	}
	
	
}
