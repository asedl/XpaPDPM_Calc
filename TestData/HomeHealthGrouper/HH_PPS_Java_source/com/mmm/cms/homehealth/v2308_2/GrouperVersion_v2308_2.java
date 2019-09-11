/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v2308_2;

import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.v2308_1.GrouperVersion_v2308_1;
import com.mmm.cms.util.ValidateUtils;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This provides the scoring logic for Home Health Prospective Payments System
 * version 2.03 as replicated by 3M. This class handles ICD-9-CM codes that are
 * effective from October 1, 2008 through September 30, 2009 inclusive. Its
 * scoring logic is the same as the class it extends.
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class GrouperVersion_v2308_2 extends GrouperVersion_v2308_1 {

	/**
	 * Constructor that sets the effective start and thru dates to October 1,
	 * 2008 and September 30, 2009 inclusive
	 */
	public GrouperVersion_v2308_2() {
		// effective start October 1, 2008
		// effective thru September 30, 2009
		// no window
		super(new GregorianCalendar(2008, 9, 1), new GregorianCalendar(2009, 8, 30), null);
	}

	/**
	 * Gets the name of this Grouper version
	 *
	 * @return The name of this version, plus version number
	 */
	@Override
	public String getName() {
		return "HHA PPS Grouper - Post September 2008 ICD-9-CM codes - " + getVersion();
	}

	/**
	 * Validates that the record can be scored by this version.
	 *
	 * According to the Readme203.pdf, Source of Data, item 2, page 6: The
	 * Therapy threshold item (M0825 & M0826) is NOT equal to NA. This means
	 * that the cases for which the HIPPS codes is not needed will not be
	 * classified. (This would include, for example, assessments for
	 * non-Medicare/non-Medicaid patients, or Medicare assessments that are not
	 * the basis for casemix classifications for the Medicare episode.) It also
	 * means that the Medicare assessments with M0825 or M0826 coded NA in error
	 * will not be classified. Records with M0825 or M0826 left blank WILL be
	 * classified, with that item treated as invalid data.
	 *
	 * The record's M0090_INFO_COMPLETED_DT must be within the effect start and
	 * the effective thru date, inclusive.
	 *
	 * @param record
	 * @return true if the record is scoreable by this version
	 */
	@Override
	public boolean isValidForVersion(HomeHealthRecordIF record) {
		Calendar date;
		boolean valid;

		// based on line 54 and 55 of psuedo code document
		date = record.getINFO_COMPLETED_DT();
		if (date != null) {

			// check that the date if not before the effective start date
			if (date.before(getEffectiveDateStart())) {
				valid = false;
			} else {
				// ensure that the record it not after the effective thru date
				valid = !date.after(getEffectiveDateThru());
			}
		} else {
			valid = false;
		}

		return valid;
	}

	/**
	 * Gets the descriptions of this version
	 *
	 * @return the description of this version
	 */
	@Override
	public String getDescription() {
		return "Provides the version 2.03 scoring logic for codes valid between October 1, 2008 thru September 30, 2009 inclusive.";
	}
}
